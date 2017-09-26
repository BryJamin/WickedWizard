package com.bryjamin.wickedwizard.ecs.systems.ai;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySubscription;
import com.artemis.systems.EntityProcessingSystem;
import com.artemis.utils.IntBag;
import com.bryjamin.wickedwizard.ecs.components.identifiers.HazardComponent;
import com.bryjamin.wickedwizard.ecs.components.movement.CollisionBoundComponent;
import com.bryjamin.wickedwizard.ecs.components.texture.BlinkOnHitComponent;

/**
 * Created by BB on 05/03/2017.
 *
 * Gets all entities in the world that have an Enemy Component or Hazard Component
 *
 * If the player's hitbox overlaps the hitbox of any of entites a set amount of damage is applied
 *
 */
public class EnemyCollisionSystem extends EntityProcessingSystem {

    ComponentMapper<com.bryjamin.wickedwizard.ecs.components.HealthComponent> healthMapper;
    ComponentMapper<CollisionBoundComponent> cbm;
    ComponentMapper<com.bryjamin.wickedwizard.ecs.components.ai.ExploderComponent> exploderMapper;

    private static final int enemyDamage = 1;

    @SuppressWarnings("unchecked")
    public EnemyCollisionSystem() {
        super(Aspect.all(com.bryjamin.wickedwizard.ecs.components.identifiers.PlayerComponent.class, BlinkOnHitComponent.class));
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void process(Entity e) {

        EntitySubscription subscription = world.getAspectSubscriptionManager().get(Aspect.one(com.bryjamin.wickedwizard.ecs.components.identifiers.EnemyComponent.class, HazardComponent.class).exclude(com.bryjamin.wickedwizard.ecs.components.identifiers.BulletComponent.class));
        IntBag entityIds = subscription.getEntities();

        for (int i = 0; i < entityIds.size(); i++) {
            if (cbm.has(entityIds.get(i))) {

                for (com.bryjamin.wickedwizard.utils.collider.HitBox hb : cbm.get(entityIds.get(i)).hitBoxes)
                    if (cbm.get(e).bound.overlaps(hb.hitbox)) {
                        healthMapper.get(e).applyDamage(enemyDamage);
                        if(exploderMapper.has(entityIds.get(i))){
                            world.getSystem(OnDeathSystem.class).kill(world.getEntity((entityIds.get(i))));
                        }
                        break;
                    }
            }
        }

    }


}