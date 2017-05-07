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
import com.byrjamin.wickedwizard.ecs.components.identifiers.IntangibleComponent;
import com.byrjamin.wickedwizard.ecs.components.identifiers.PlayerComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.BounceComponent;
import com.byrjamin.wickedwizard.ecs.components.identifiers.BulletComponent;
import com.byrjamin.wickedwizard.ecs.components.CollisionBoundComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.MoveToComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.PositionComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.VelocityComponent;
import com.byrjamin.wickedwizard.ecs.components.object.DoorComponent;
import com.byrjamin.wickedwizard.ecs.components.object.PlatformComponent;
import com.byrjamin.wickedwizard.ecs.components.object.WallComponent;
import com.byrjamin.wickedwizard.ecs.systems.ai.OnDeathSystem;
import com.byrjamin.wickedwizard.utils.collider.Collider;

/**
 * Created by Home on 04/03/2017.
 */
public class GroundCollisionSystem extends EntityProcessingSystem {

    ComponentMapper<PositionComponent> pm;
    ComponentMapper<PlayerComponent> playerm;
    ComponentMapper<VelocityComponent> vm;
    ComponentMapper<CollisionBoundComponent> cbm;
    ComponentMapper<WallComponent> wm;
    ComponentMapper<BulletComponent> bm;
    ComponentMapper<MoveToComponent> mtm;

    @SuppressWarnings("unchecked")
    public GroundCollisionSystem() {
        super(Aspect.all(PositionComponent.class, VelocityComponent.class, CollisionBoundComponent.class).exclude(BounceComponent.class, IntangibleComponent.class));
    }


    @Override
    protected void begin() {
        super.begin();
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void process(Entity e) {

        Array<Rectangle> collidableobjects = new Array<Rectangle>();

        EntitySubscription subscription = world.getAspectSubscriptionManager().get(Aspect.all(WallComponent.class));
        IntBag entityIds = subscription.getEntities();

        for(int i = 0; i < entityIds.size(); i++){
            collidableobjects.add(wm.get(entityIds.get(i)).bound);
        }

        subscription = world.getAspectSubscriptionManager().get(Aspect.all(DoorComponent.class, CollisionBoundComponent.class).exclude(ActiveOnTouchComponent.class));
        entityIds = subscription.getEntities();

        if(!playerm.has(e)) {
            for(int i = 0; i < entityIds.size(); i++){
                collidableobjects.add(cbm.get(entityIds.get(i)).bound);
            }

        }

        subscription = world.getAspectSubscriptionManager().get(Aspect.all(PlatformComponent.class, CollisionBoundComponent.class).exclude(ActiveOnTouchComponent.class));
        entityIds = subscription.getEntities();

        if(!playerm.has(e) && !bm.has(e)) {

            for(int i = 0; i < entityIds.size(); i++){
                collidableobjects.add(cbm.get(entityIds.get(i)).bound);
            }
        }

        PositionComponent pc = pm.get(e);
        VelocityComponent vc = vm.get(e);
        CollisionBoundComponent cbc = cbm.get(e);



        Rectangle futureRectangle = new Rectangle(cbc.bound);
        //System.out.println(futureRectangle.getX());

        if(!bm.has(e)) {
            futureRectangle.x += (vc.velocity.x * world.delta);
            //System.out.println(futureRectangle.getX());
            futureRectangle.y += (vc.velocity.y * world.delta);
        }
        //BoundsDrawer.drawBounds(world.getSystem(RenderingSystem.class).batch, futureRectangle);

        for(Rectangle r : collidableobjects) {

            Collider.Collision c = Collider.collision(cbc.bound, futureRectangle, r);

            if(c != Collider.Collision.NONE){

                switch(c){
                    case TOP: //vc.velocity.y = 0;
                    case BOTTOM: //vc.velocity.y = 0;
                        break;
                    //TODO When grappling if you collide with something the target destination should be removed
                    case LEFT:
                    case RIGHT:
                        vc.velocity.x = 0;
                        break;
                }
                pc.position.x = cbc.bound.getX();
                pc.position.y = cbc.bound.getY();

                if(bm.has(e)){
                    world.getSystem(OnDeathSystem.class).kill(e);
                }

            }

            cbc.getRecentCollisions().add(c);

        }

        if((cbc.getRecentCollisions().contains(Collider.Collision.TOP, true ) ||
                cbc.getRecentCollisions().contains(Collider.Collision.BOTTOM, true)) &&
                !( cbc.getRecentCollisions().contains(Collider.Collision.LEFT, true) ||
                        cbc.getRecentCollisions().contains(Collider.Collision.RIGHT, true))){

            vc.velocity.y = 0;

        }



    }





}
