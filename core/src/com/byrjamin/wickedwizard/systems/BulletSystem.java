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
import com.byrjamin.wickedwizard.components.FriendlyComponent;
import com.byrjamin.wickedwizard.components.HealthComponent;
import com.byrjamin.wickedwizard.components.PositionComponent;
import com.byrjamin.wickedwizard.components.VelocityComponent;
import com.byrjamin.wickedwizard.components.WallComponent;

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
            Entity player = world.getSystem(FindPlayerSystem.class).getPlayer();
            CollisionBoundComponent pcbc = cbm.get(player);
            if(pcbc.bound.overlaps(vc.bound)){

                BlinkComponent bc = bm.get(player);

                if(!bc.isHit) {
                    HealthComponent hc = hm.get(player);
                    hc.health = hc.health - 1;
                    bc.isHit = true;
                    System.out.println(hc.health);
                }

                System.out.println(bc.isHit);

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

                    e.deleteFromWorld();
                }
            }
        }


        //pc.position.add(vc.velocity.x * world.delta, vc.velocity.y * world.delta);

    }


}
