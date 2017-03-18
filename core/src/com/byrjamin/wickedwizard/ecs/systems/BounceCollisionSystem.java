package com.byrjamin.wickedwizard.ecs.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySubscription;
import com.artemis.systems.EntityProcessingSystem;
import com.artemis.utils.IntBag;
import com.badlogic.gdx.math.Rectangle;
import com.byrjamin.wickedwizard.ecs.components.BounceComponent;
import com.byrjamin.wickedwizard.ecs.components.BulletComponent;
import com.byrjamin.wickedwizard.ecs.components.CollisionBoundComponent;
import com.byrjamin.wickedwizard.ecs.components.PositionComponent;
import com.byrjamin.wickedwizard.ecs.components.VelocityComponent;
import com.byrjamin.wickedwizard.ecs.components.WallComponent;
import com.byrjamin.wickedwizard.helper.collider.Collider;

/**
 * Created by Home on 11/03/2017.
 */
public class BounceCollisionSystem extends EntityProcessingSystem {

    ComponentMapper<PositionComponent> pm;
    ComponentMapper<VelocityComponent> vm;
    ComponentMapper<CollisionBoundComponent> cbm;
    ComponentMapper<WallComponent> wm;
    ComponentMapper<BulletComponent> bm;

    @SuppressWarnings("unchecked")
    public BounceCollisionSystem() {
        super(Aspect.all(PositionComponent.class, VelocityComponent.class, CollisionBoundComponent.class, BounceComponent.class));
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

        cbc.bound.x = pc.getX();
        cbc.bound.y = pc.getY();
        Rectangle futureRectangle = new Rectangle(cbc.bound);
        futureRectangle.x += vc.velocity.x * world.delta;
        futureRectangle.y += vc.velocity.y * world.delta;

        for(int i = 0; i < entityIds.size(); i++) {

            Rectangle r = wm.get(entityIds.get(i)).bound;
            Collider.Collision c = Collider.collision(cbc.bound, futureRectangle, r);
            if(c != Collider.Collision.NONE){
                switch(c){
                    case TOP: vc.velocity.y = Math.abs(vc.velocity.y);
                        break;
                    case BOTTOM: vc.velocity.y = -Math.abs(vc.velocity.y);
                        break;
                    case LEFT: vc.velocity.x = -Math.abs(vc.velocity.x);
                        break;
                    case RIGHT: vc.velocity.x = Math.abs(vc.velocity.x);
                        break;
                }
                pc.position.x = cbc.bound.getX();
                pc.position.y = cbc.bound.getY();

                if(bm.has(e)){
                    e.deleteFromWorld();
                }

            }
           // BoundsDrawer.drawBounds(world.getSystem(RenderingSystem.class).batch, r);
        }

        //BoundsDrawer.drawBounds(world.getSystem(RenderingSystem.class).batch, cbc.bound);

        //pc.position.add(vc.velocity.x * world.delta, vc.velocity.y * world.delta);

    }





}
