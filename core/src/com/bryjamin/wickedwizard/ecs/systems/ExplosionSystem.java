package com.bryjamin.wickedwizard.ecs.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySubscription;
import com.artemis.systems.EntityProcessingSystem;
import com.artemis.utils.IntBag;
import com.bryjamin.wickedwizard.ecs.components.ExplosionComponent;
import com.bryjamin.wickedwizard.ecs.components.HealthComponent;
import com.bryjamin.wickedwizard.ecs.components.identifiers.BulletComponent;
import com.bryjamin.wickedwizard.ecs.components.identifiers.EnemyComponent;
import com.bryjamin.wickedwizard.ecs.components.identifiers.FriendlyComponent;
import com.bryjamin.wickedwizard.ecs.components.identifiers.PlayerComponent;
import com.bryjamin.wickedwizard.ecs.components.movement.CollisionBoundComponent;

/**
 * Created by Home on 23/05/2017.
 */

public class ExplosionSystem extends EntityProcessingSystem {


    ComponentMapper<CollisionBoundComponent> cbm;
    ComponentMapper<HealthComponent> hm;
    ComponentMapper<ExplosionComponent> em;

    ComponentMapper<FriendlyComponent> fm;

    ComponentMapper<PlayerComponent> playerm;

    ComponentMapper<EnemyComponent> enemym;




    ComponentMapper<BulletComponent> bulm;


    @SuppressWarnings("unchecked")
    public ExplosionSystem() {
        super(Aspect.all(ExplosionComponent.class, CollisionBoundComponent.class));
    }


    @Override
    protected boolean checkProcessing() {
        return this.getEntities().size() > 0;
    }

    @Override
    protected void process(Entity explosionEntity) {

        EntitySubscription subscription = world.getAspectSubscriptionManager().
                get(Aspect.all(HealthComponent.class, CollisionBoundComponent.class).one(PlayerComponent.class, EnemyComponent.class));

        IntBag entities = subscription.getEntities();


        CollisionBoundComponent explosionBound = cbm.get(explosionEntity);

        for(int i = 0; i < entities.size(); i++){

            int entity = entities.get(i);

            if(playerm.has(entity) && fm.has(explosionEntity)){
                continue;
            }

            if(enemym.has(entity) && enemym.has(explosionEntity)){
                continue;
            }

            CollisionBoundComponent cbc = cbm.get(entity);
            HealthComponent hc = hm.get(entity);

            if(explosionBound.bound.overlaps(cbc.bound)){
                hc.applyDamage(em.get(explosionEntity).damage);
            }


        }
        explosionEntity.edit().remove(ExplosionComponent.class);


    }
}
