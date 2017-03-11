package com.byrjamin.wickedwizard.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySubscription;
import com.artemis.systems.EntityProcessingSystem;
import com.artemis.utils.IntBag;
import com.byrjamin.wickedwizard.components.BlinkComponent;
import com.byrjamin.wickedwizard.components.BulletComponent;
import com.byrjamin.wickedwizard.components.CollisionBoundComponent;
import com.byrjamin.wickedwizard.components.EnemyComponent;
import com.byrjamin.wickedwizard.components.HealthComponent;
import com.byrjamin.wickedwizard.components.PlayerComponent;
import com.byrjamin.wickedwizard.components.PositionComponent;
import com.byrjamin.wickedwizard.components.VelocityComponent;
import com.byrjamin.wickedwizard.components.WallComponent;

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
                if(cbm.get(e).bound.overlaps(cbm.get(entityIds.get(i)).bound)){
                    if(!bm.get(e).isHit) {
                        hm.get(e).health -= 1;
                        bm.get(e).isHit = true;
                    }
                }
            }
        }

        //PositionComponent pc = pm.get(e);
        //VelocityComponent vc = vm.get(e);

        //pc.position.add(vc.velocity.x * world.delta, vc.velocity.y * world.delta);

    }


}