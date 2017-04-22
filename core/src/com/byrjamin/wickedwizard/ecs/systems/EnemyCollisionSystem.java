package com.byrjamin.wickedwizard.ecs.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySubscription;
import com.artemis.systems.EntityProcessingSystem;
import com.artemis.utils.IntBag;
import com.byrjamin.wickedwizard.ecs.components.BlinkComponent;
import com.byrjamin.wickedwizard.ecs.components.identifiers.BulletComponent;
import com.byrjamin.wickedwizard.ecs.components.CollisionBoundComponent;
import com.byrjamin.wickedwizard.ecs.components.identifiers.EnemyComponent;
import com.byrjamin.wickedwizard.ecs.components.HealthComponent;
import com.byrjamin.wickedwizard.ecs.components.identifiers.PlayerComponent;
import com.byrjamin.wickedwizard.utils.collider.HitBox;

/**
 * Created by Home on 05/03/2017.
 */
public class EnemyCollisionSystem extends EntityProcessingSystem {

    ComponentMapper<HealthComponent> hm;
    ComponentMapper<CollisionBoundComponent> cbm;
    ComponentMapper<BlinkComponent> bm;

    @SuppressWarnings("unchecked")
    public EnemyCollisionSystem() {
        super(Aspect.all(PlayerComponent.class));
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void process(Entity e) {

        EntitySubscription subscription = world.getAspectSubscriptionManager().get(Aspect.all(EnemyComponent.class).exclude(BulletComponent.class));
        IntBag entityIds = subscription.getEntities();

        for(int i = 0; i < entityIds.size(); i++) {
            if(cbm.has(entityIds.get(i))){

                for(HitBox hb : cbm.get(entityIds.get(i)).hitBoxes)
                if(cbm.get(e).bound.overlaps(hb.hitbox)){
                    if(!bm.get(e).isHit) {
                        hm.get(e).health -= 1;
                        bm.get(e).isHit = true;
                        break;
                    }
                }
            }
        }

        //PositionComponent pc = pm.get(e);
        //VelocityComponent vc = vm.get(e);

        //pc.position.add(vc.velocity.x * world.delta, vc.velocity.y * world.delta);

    }


}