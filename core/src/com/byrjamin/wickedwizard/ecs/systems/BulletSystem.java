package com.byrjamin.wickedwizard.ecs.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySubscription;
import com.artemis.systems.EntityProcessingSystem;
import com.artemis.utils.IntBag;
import com.byrjamin.wickedwizard.ecs.components.BlinkComponent;
import com.byrjamin.wickedwizard.ecs.components.BulletComponent;
import com.byrjamin.wickedwizard.ecs.components.CollisionBoundComponent;
import com.byrjamin.wickedwizard.ecs.components.EnemyComponent;
import com.byrjamin.wickedwizard.ecs.components.FriendlyComponent;
import com.byrjamin.wickedwizard.ecs.components.HealthComponent;

/**
 * Created by Home on 05/03/2017.
 */
public class BulletSystem extends EntityProcessingSystem {


    ComponentMapper<CollisionBoundComponent> cbm;
    ComponentMapper<HealthComponent> hm;
    ComponentMapper<EnemyComponent> em;
    ComponentMapper<FriendlyComponent> fm;
    ComponentMapper<BlinkComponent> bm;


    @SuppressWarnings("unchecked")
    public BulletSystem() {
        super(Aspect.all(BulletComponent.class, CollisionBoundComponent.class).one(EnemyComponent.class, FriendlyComponent.class));
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void process(Entity e) {

        CollisionBoundComponent vc = cbm.get(e);

        if(em.has(e)){
            CollisionBoundComponent pcbc = world.getSystem(FindPlayerSystem.class).getPC(CollisionBoundComponent.class);
            if(pcbc.bound.overlaps(vc.bound)){

                BlinkComponent bc = world.getSystem(FindPlayerSystem.class).getPC(BlinkComponent.class);

                if(!bc.isHit) {
                    HealthComponent hc = world.getSystem(FindPlayerSystem.class).getPC(HealthComponent.class);
                    hc.health = hc.health - 1;
                    bc.isHit = true;
                    System.out.println(hc.health);
                }

                e.deleteFromWorld();

            }

        } else if(fm.has(e)){
            EntitySubscription subscription = world.getAspectSubscriptionManager().get(Aspect.all(EnemyComponent.class, CollisionBoundComponent.class, HealthComponent.class));
            IntBag entityIds = subscription.getEntities();

            for(int i = 0; i < entityIds.size(); i++){
                if(cbm.get(entityIds.get(i)).bound.overlaps(vc.bound)){
                    HealthComponent hc = hm.get(entityIds.get(i));
                    hc.health = hc.health - 1;

                    if(bm.has(entityIds.get(i))){
                        BlinkComponent bc = bm.get(entityIds.get(i));
                        bc.isHit = true;
                    }

                    world.getSystem(OnDeathSystem.class).kill(e);

                    break; //TODO Note this is so you cna't hit two enemies at once. Might get rid of it later
                }
            }
        }


        //pc.position.add(vc.velocity.x * world.delta, vc.velocity.y * world.delta);

    }


}
