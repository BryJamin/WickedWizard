package com.byrjamin.wickedwizard.ecs.systems.physics;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySubscription;
import com.artemis.systems.EntityProcessingSystem;
import com.artemis.utils.IntBag;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.byrjamin.wickedwizard.ecs.components.ActiveOnTouchComponent;
import com.byrjamin.wickedwizard.ecs.components.identifiers.GrappleComponent;
import com.byrjamin.wickedwizard.ecs.components.identifiers.IntangibleComponent;
import com.byrjamin.wickedwizard.ecs.components.identifiers.PlayerComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.BounceComponent;
import com.byrjamin.wickedwizard.ecs.components.identifiers.BulletComponent;
import com.byrjamin.wickedwizard.ecs.components.CollisionBoundComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.MoveToComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.PositionComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.VelocityComponent;
import com.byrjamin.wickedwizard.ecs.components.object.DoorComponent;
import com.byrjamin.wickedwizard.ecs.components.object.GrappleableComponent;
import com.byrjamin.wickedwizard.ecs.components.object.PlatformComponent;
import com.byrjamin.wickedwizard.ecs.components.object.WallComponent;
import com.byrjamin.wickedwizard.ecs.systems.ai.OnDeathSystem;
import com.byrjamin.wickedwizard.utils.collider.Collider;

/**
 * Created by Home on 04/03/2017.
 */
public class CollisionSystem extends EntityProcessingSystem {

    ComponentMapper<PositionComponent> pm;
    ComponentMapper<PlayerComponent> playerm;
    ComponentMapper<VelocityComponent> vm;
    ComponentMapper<CollisionBoundComponent> cbm;
    ComponentMapper<WallComponent> wm;
    ComponentMapper<BulletComponent> bulletm;
    ComponentMapper<BounceComponent> boucem;
    ComponentMapper<GrappleComponent> grapplem;
    ComponentMapper<MoveToComponent> mtm;


    private Rectangle futureRectangle = new Rectangle();
    private Rectangle nearByCheckRectangle = new Rectangle();
    private Array<Rectangle> collidableobjects = new Array<Rectangle>();

    private Aspect.Builder wall;
    private Aspect.Builder playerWalls;
    private Aspect.Builder platforms;

    @SuppressWarnings("unchecked")
    public  CollisionSystem() {
        super(Aspect.all(PositionComponent.class, VelocityComponent.class, CollisionBoundComponent.class).exclude(IntangibleComponent.class));
        wall = Aspect.all(WallComponent.class);
        playerWalls = Aspect.all(DoorComponent.class, CollisionBoundComponent.class).exclude(ActiveOnTouchComponent.class, GrappleableComponent.class);
        platforms = Aspect.all(PlatformComponent.class, CollisionBoundComponent.class).exclude(ActiveOnTouchComponent.class);
    }


    @Override
    protected void begin() {
        super.begin();
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void process(Entity e) {

        PositionComponent pc = pm.get(e);
        VelocityComponent vc = vm.get(e);
        CollisionBoundComponent cbc = cbm.get(e);
        cbm.get(e).getRecentCollisions().clear();

        collidableobjects.clear();

        addNearByCollidableObjects(cbc.bound, collidableobjects, wall);

        if(!playerm.has(e)) {
            addNearByCollidableObjects(cbc.bound, collidableobjects, playerWalls);
        }

        if(!playerm.has(e) && !bulletm.has(e) && !grapplem.has(e)) {
            addNearByCollidableObjects(cbc.bound, collidableobjects, platforms);
        }



        futureRectangle.set(cbc.bound.x, cbc.bound.y, cbc.bound.getWidth(), cbc.bound.getHeight());
        //System.out.println(futureRectangle.getX());

        if(!bulletm.has(e)) {
            futureRectangle.x += (vc.velocity.x * world.delta);
            //System.out.println(futureRectangle.getX());
            futureRectangle.y += (vc.velocity.y * world.delta);
        }

        //BoundsDrawer.drawBounds(world.getSystem(RenderingSystem.class).batch, futureRectangle);

        for(Rectangle r : collidableobjects) {

            Collider.Collision c = Collider.collision(cbc.bound, futureRectangle, r);

            if(c != Collider.Collision.NONE){

                switch(c){
                    case BOTTOM: //vc.velocity.y = 0;
                    case TOP: //vc.velocity.y = 0;
                        break;
                    //TODO When grappling if you collide with something the target destination should be removed
                    case LEFT:
                    case RIGHT:
                        break;
                }
                pc.position.x = cbc.bound.getX();
                pc.position.y = cbc.bound.getY();

                if(bulletm.has(e) || grapplem.has(e)){
                    world.getSystem(OnDeathSystem.class).kill(e);
                }

            }

            cbc.getRecentCollisions().add(c);

        }



    }



    public void addCollidableObjects(Array<Rectangle> collidableObjects, Aspect.Builder aspect){
        EntitySubscription subscription = world.getAspectSubscriptionManager().get(aspect);
        IntBag entityIds = subscription.getEntities();

        for(int i = 0; i < entityIds.size(); i++){
            if(wm.has(entityIds.get(i))) {
                collidableObjects.add(wm.get(entityIds.get(i)).bound);
            } else if(cbm.has(entityIds.get(i))) {
                collidableObjects.add(cbm.get(entityIds.get(i)).bound);
            }
        }
    }


    public void addNearByCollidableObjects(Rectangle r, Array<Rectangle> collidableObjects, Aspect.Builder aspect){
        EntitySubscription subscription = world.getAspectSubscriptionManager().get(aspect);
        IntBag entityIds = subscription.getEntities();

        for(int i = 0; i < entityIds.size(); i++){
            if(wm.has(entityIds.get(i))) {
                if(isNearBy(r, wm.get(entityIds.get(i)).bound)) collidableObjects.add(wm.get(entityIds.get(i)).bound);
            } else if(cbm.has(entityIds.get(i))) {
                if(isNearBy(r, cbm.get(entityIds.get(i)).bound)) collidableObjects.add(cbm.get(entityIds.get(i)).bound);
            }
        }
    }

    public boolean isNearBy(Rectangle dynamic, Rectangle stationary){

        float minX = dynamic.getX() - (dynamic.getWidth() * 3 / 2 - dynamic.getWidth() / 2);
        float maxX = minX + dynamic.getWidth() * 3;

        float minY = dynamic.getY() - (dynamic.getHeight() * 3 / 2 - dynamic.getHeight() / 2);
        float maxY = minX + dynamic.getHeight() * 3;

        nearByCheckRectangle.set(minX, minY, dynamic.getWidth() * 3, dynamic.getHeight() * 3);

        return stationary.overlaps(nearByCheckRectangle);

    }



}
