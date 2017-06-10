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
import com.byrjamin.wickedwizard.ecs.components.object.PlatformComponent;
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

        CollisionBoundComponent cbc = cbm.get(e);
        VelocityComponent vc = vm.get(e);
        PositionComponent pc = pm.get(e);

        for(Collider.Collision c : cbc.getRecentCollisions()){

            if(c != Collider.Collision.NONE) {
                switch (c) {
                    case TOP:
                        vc.velocity.y = Math.abs(vc.velocity.y);
                        break;
                    case BOTTOM:
                        vc.velocity.y = -Math.abs(vc.velocity.y);
                        break;
                    case LEFT:
                        vc.velocity.x = Math.abs(vc.velocity.x);
                        break;
                    case RIGHT:
                        vc.velocity.x = -Math.abs(vc.velocity.x);
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
