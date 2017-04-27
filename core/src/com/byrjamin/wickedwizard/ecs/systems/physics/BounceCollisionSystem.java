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
import com.byrjamin.wickedwizard.ecs.components.identifiers.PlayerComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.BounceComponent;
import com.byrjamin.wickedwizard.ecs.components.identifiers.BulletComponent;
import com.byrjamin.wickedwizard.ecs.components.CollisionBoundComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.PositionComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.VelocityComponent;
import com.byrjamin.wickedwizard.ecs.components.object.DoorComponent;
import com.byrjamin.wickedwizard.ecs.components.object.WallComponent;
import com.byrjamin.wickedwizard.utils.collider.Collider;

/**
 * Created by Home on 11/03/2017.
 */
public class BounceCollisionSystem extends EntityProcessingSystem {

    ComponentMapper<PositionComponent> pm;
    ComponentMapper<PlayerComponent> playerm;
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

        EntitySubscription subscription2 = world.getAspectSubscriptionManager().get(Aspect.all(DoorComponent.class, CollisionBoundComponent.class).exclude(ActiveOnTouchComponent.class));
        IntBag doorEntityIds = subscription2.getEntities();

        PositionComponent pc = pm.get(e);
        VelocityComponent vc = vm.get(e);
        CollisionBoundComponent cbc = cbm.get(e);

        Rectangle futureRectangle = new Rectangle(cbc.bound);
        futureRectangle.x += vc.velocity.x * world.delta;
        futureRectangle.y += vc.velocity.y * world.delta;


        Array<Rectangle> collidableobjects = new Array<Rectangle>();

        for(int i = 0; i < entityIds.size(); i++){
            collidableobjects.add(wm.get(entityIds.get(i)).bound);
        }

        if(!playerm.has(e)) {
            for(int i = 0; i < doorEntityIds.size(); i++){
                collidableobjects.add(cbm.get(doorEntityIds.get(i)).bound);
            }

        }


        for(Rectangle r : collidableobjects) {
            Collider.Collision c = Collider.collision(cbc.bound, futureRectangle, r);
            if(c != Collider.Collision.NONE) {
                switch (c) {
                    case TOP:
                        vc.velocity.y = Math.abs(vc.velocity.y);
                        break;
                    case BOTTOM:
                        vc.velocity.y = -Math.abs(vc.velocity.y);
                        break;
                    case LEFT:
                        vc.velocity.x = -Math.abs(vc.velocity.x);
                        break;
                    case RIGHT:
                        vc.velocity.x = Math.abs(vc.velocity.x);
                        break;
                }
                pc.position.x = cbc.bound.getX();
                pc.position.y = cbc.bound.getY();

                if (bm.has(e)) {
                    e.deleteFromWorld();
                }
            }
        }

    }





}
