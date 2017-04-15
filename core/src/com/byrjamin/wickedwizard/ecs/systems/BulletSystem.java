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
import com.byrjamin.wickedwizard.ecs.components.StatComponent;
import com.byrjamin.wickedwizard.ecs.components.WeaponComponent;
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
            CollisionBoundComponent pcbc = world.getSystem(FindPlayerSystem.class).getPC(CollisionBoundComponent.class);
            if(pcbc.bound.overlaps(cbc.bound)){

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

                for(HitBox hb : cbm.get(entityIds.get(i)).hitBoxes){
                    if(hb.hitbox.overlaps(cbc.bound)){
                        HealthComponent hc = hm.get(entityIds.get(i));

                        System.out.println("Damage is " + bulm.get(e).damage);
                        hc.applyDamage(bulm.get(e).damage);
                        if(bm.has(entityIds.get(i))){
                            BlinkComponent bc = bm.get(entityIds.get(i));
                            bc.isHit = true;
                        }
                        world.getSystem(OnDeathSystem.class).kill(e);
                        break;
                    }
                }

            }
        }


        //pc.position.add(vc.velocity.x * world.delta, vc.velocity.y * world.delta);

    }


}
