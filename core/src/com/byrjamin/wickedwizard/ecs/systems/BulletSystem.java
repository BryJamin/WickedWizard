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
import com.byrjamin.wickedwizard.ecs.components.identifiers.FriendlyComponent;
import com.byrjamin.wickedwizard.ecs.components.HealthComponent;
import com.byrjamin.wickedwizard.ecs.components.identifiers.OnlyPlayerBulletsComponent;
import com.byrjamin.wickedwizard.ecs.components.identifiers.PlayerComponent;
import com.byrjamin.wickedwizard.ecs.systems.ai.OnDeathSystem;
import com.byrjamin.wickedwizard.utils.collider.HitBox;

/**
 * Created by Home on 05/03/2017.
 */
public class BulletSystem extends EntityProcessingSystem {


    ComponentMapper<CollisionBoundComponent> cbm;
    ComponentMapper<HealthComponent> hm;
    ComponentMapper<EnemyComponent> em;
    ComponentMapper<FriendlyComponent> fm;
    ComponentMapper<BlinkComponent> bm;
    ComponentMapper<BulletComponent> bulm;


    @SuppressWarnings("unchecked")
    public BulletSystem() {
        super(Aspect.all(BulletComponent.class, CollisionBoundComponent.class).one(EnemyComponent.class, FriendlyComponent.class));
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void process(Entity e) {

        CollisionBoundComponent cbc = cbm.get(e);

        if(em.has(e)){
            CollisionBoundComponent pcbc = world.getSystem(FindPlayerSystem.class).getPlayerComponent(CollisionBoundComponent.class);
            if(pcbc.bound.overlaps(cbc.bound)){

                BlinkComponent bc = world.getSystem(FindPlayerSystem.class).getPlayerComponent(BlinkComponent.class);

                if(!bc.isHit) {
                    HealthComponent hc = world.getSystem(FindPlayerSystem.class).getPlayerComponent(HealthComponent.class);
                    hc.applyDamage(1);
                }

                world.getSystem(com.byrjamin.wickedwizard.ecs.systems.ai.OnDeathSystem.class).kill(e);

            }

        } else if(fm.has(e)){
            EntitySubscription subscription = world.getAspectSubscriptionManager().get(Aspect.all(CollisionBoundComponent.class, HealthComponent.class).one(EnemyComponent.class,OnlyPlayerBulletsComponent.class) );
            IntBag entityIds = subscription.getEntities();
            bulletScan(e, entityIds);
        }

        EntitySubscription subscription = world.getAspectSubscriptionManager().get(Aspect.all(
                CollisionBoundComponent.class, HealthComponent.class).exclude(EnemyComponent.class, PlayerComponent.class, OnlyPlayerBulletsComponent.class));
        IntBag entityIds = subscription.getEntities();
        bulletScan(e, entityIds);


        //pc.position.add(vc.velocity.x * world.delta, vc.velocity.y * world.delta);

    }


    public void bulletScan(Entity bullet, IntBag entityIds){

        for(int i = 0; i < entityIds.size(); i++){

            for(HitBox hb : cbm.get(entityIds.get(i)).hitBoxes){
                if(hb.hitbox.overlaps(cbm.get(bullet).bound)){
                    HealthComponent hc = hm.get(entityIds.get(i));

                    System.out.println("Damage is " + bulm.get(bullet).damage);
                    hc.applyDamage(bulm.get(bullet).damage);
                    if(bm.has(entityIds.get(i))){
                        BlinkComponent bc = bm.get(entityIds.get(i));
                        bc.isHit = true;
                    }
                    world.getSystem(OnDeathSystem.class).kill(bullet);
                    break;
                }
            }

        }

    }


}
