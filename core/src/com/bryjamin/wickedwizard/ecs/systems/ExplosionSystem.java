package com.bryjamin.wickedwizard.ecs.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySubscription;
import com.artemis.systems.EntityProcessingSystem;
import com.artemis.utils.IntBag;
import com.bryjamin.wickedwizard.ecs.components.texture.BlinkOnHitComponent;
import com.bryjamin.wickedwizard.ecs.components.movement.CollisionBoundComponent;
import com.bryjamin.wickedwizard.ecs.components.ExplosionComponent;
import com.bryjamin.wickedwizard.ecs.components.identifiers.BulletComponent;
import com.bryjamin.wickedwizard.ecs.components.identifiers.EnemyComponent;
import com.bryjamin.wickedwizard.ecs.components.identifiers.FriendlyComponent;

/**
 * Created by Home on 23/05/2017.
 */

public class ExplosionSystem extends EntityProcessingSystem {


    ComponentMapper<CollisionBoundComponent> cbm;
    ComponentMapper<com.bryjamin.wickedwizard.ecs.components.HealthComponent> hm;
    ComponentMapper<ExplosionComponent> em;
    ComponentMapper<FriendlyComponent> fm;
    ComponentMapper<BlinkOnHitComponent> bm;
    ComponentMapper<BulletComponent> bulm;


    @SuppressWarnings("unchecked")
    public ExplosionSystem() {
        super(Aspect.all(ExplosionComponent.class, CollisionBoundComponent.class));
    }


    @Override
    protected void process(Entity e) {

        EntitySubscription subscription = world.getAspectSubscriptionManager().
                get(Aspect.all(com.bryjamin.wickedwizard.ecs.components.HealthComponent.class, CollisionBoundComponent.class).one(com.bryjamin.wickedwizard.ecs.components.identifiers.PlayerComponent.class, EnemyComponent.class));

        /*or
        EntitySubscription subscription = world.getAspectSubscriptionManager().
                get(Aspect.all(HealthComponent.class, CollisionBoundComponent.class));
        */

        IntBag entities = subscription.getEntities();


        CollisionBoundComponent explosionBound = cbm.get(e);

        for(int i = 0; i < entities.size(); i++){

            int entity = entities.get(i);

            CollisionBoundComponent cbc = cbm.get(entity);
            com.bryjamin.wickedwizard.ecs.components.HealthComponent hc = hm.get(entity);

            if(explosionBound.bound.overlaps(cbc.bound)){
                hc.applyDamage(em.get(e).damage);
            }


        }

        e.edit().remove(ExplosionComponent.class);


    }
}
