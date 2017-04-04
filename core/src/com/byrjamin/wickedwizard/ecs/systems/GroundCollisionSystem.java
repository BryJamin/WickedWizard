package com.byrjamin.wickedwizard.ecs.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySubscription;
import com.artemis.systems.EntityProcessingSystem;
import com.artemis.utils.IntBag;
import com.badlogic.gdx.math.Rectangle;
import com.byrjamin.wickedwizard.ecs.components.movement.BounceComponent;
import com.byrjamin.wickedwizard.ecs.components.BulletComponent;
import com.byrjamin.wickedwizard.ecs.components.CollisionBoundComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.MoveToComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.PositionComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.VelocityComponent;
import com.byrjamin.wickedwizard.ecs.components.object.WallComponent;
import com.byrjamin.wickedwizard.helper.collider.Collider;

/**
 * Created by Home on 04/03/2017.
 */
public class GroundCollisionSystem extends EntityProcessingSystem {

    ComponentMapper<PositionComponent> pm;
    ComponentMapper<VelocityComponent> vm;
    ComponentMapper<CollisionBoundComponent> cbm;
    ComponentMapper<WallComponent> wm;
    ComponentMapper<BulletComponent> bm;
    ComponentMapper<MoveToComponent> mtm;

    @SuppressWarnings("unchecked")
    public GroundCollisionSystem() {
        super(Aspect.all(PositionComponent.class, VelocityComponent.class, CollisionBoundComponent.class).exclude(BounceComponent.class));
    }


    @Override
    protected void begin() {
        super.begin();
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void process(Entity e) {

        EntitySubscription subscription = world.getAspectSubscriptionManager().get(Aspect.all(WallComponent.class));
        IntBag entityIds = subscription.getEntities();

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

        cbc.getRecentCollisions().clear();

        for(int i = 0; i < entityIds.size(); i++) {

            Rectangle r = wm.get(entityIds.get(i)).bound;
            Collider.Collision c = Collider.collision(cbc.bound, futureRectangle, r);

            if(c != Collider.Collision.NONE){
                switch(c){
                    case TOP: vc.velocity.y = 0;
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



    }





}
