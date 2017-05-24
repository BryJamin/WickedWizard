package com.byrjamin.wickedwizard.ecs.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySubscription;
import com.artemis.systems.EntityProcessingSystem;
import com.artemis.utils.IntBag;
import com.byrjamin.wickedwizard.ecs.components.BlinkComponent;
import com.byrjamin.wickedwizard.ecs.components.CollisionBoundComponent;
import com.byrjamin.wickedwizard.ecs.components.ExplosionComponent;
import com.byrjamin.wickedwizard.ecs.components.HealthComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.ExpireComponent;
import com.byrjamin.wickedwizard.ecs.components.identifiers.BulletComponent;
import com.byrjamin.wickedwizard.ecs.components.identifiers.EnemyComponent;
import com.byrjamin.wickedwizard.ecs.components.identifiers.FriendlyComponent;
import com.byrjamin.wickedwizard.ecs.components.identifiers.PlayerComponent;
import com.byrjamin.wickedwizard.ecs.components.object.PickUpComponent;

/**
 * Created by Home on 23/05/2017.
 */

public class ExplosionSystem extends EntityProcessingSystem {


    ComponentMapper<CollisionBoundComponent> cbm;
    ComponentMapper<HealthComponent> hm;
    ComponentMapper<ExplosionComponent> em;
    ComponentMapper<FriendlyComponent> fm;
    ComponentMapper<BlinkComponent> bm;
    ComponentMapper<BulletComponent> bulm;


    @SuppressWarnings("unchecked")
    public ExplosionSystem() {
        super(Aspect.all(ExplosionComponent.class, CollisionBoundComponent.class));
    }


    @Override
    protected void process(Entity e) {

        EntitySubscription subscription = world.getAspectSubscriptionManager().
                get(Aspect.all(HealthComponent.class, CollisionBoundComponent.class).one(PlayerComponent.class, EnemyComponent.class));

        /*or
        EntitySubscription subscription = world.getAspectSubscriptionManager().
                get(Aspect.all(HealthComponent.class, CollisionBoundComponent.class));
        */

        System.out.println("YO");

        IntBag entities = subscription.getEntities();


        CollisionBoundComponent explosionBound = cbm.get(e);

        for(int i = 0; i < entities.size(); i++){

            int entity = entities.get(i);


            System.out.println("YO");

            CollisionBoundComponent cbc = cbm.get(entity);
            HealthComponent hc = hm.get(entity);

            if(explosionBound.bound.overlaps(cbc.bound)){
                hc.applyDamage(em.get(e).damage);
            }


        }

        e.edit().remove(ExplosionComponent.class);


    }
}
