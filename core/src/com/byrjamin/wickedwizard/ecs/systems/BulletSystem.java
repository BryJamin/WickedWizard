package com.byrjamin.wickedwizard.ecs.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySubscription;
import com.artemis.systems.EntityProcessingSystem;
import com.artemis.utils.IntBag;
import com.byrjamin.wickedwizard.ecs.components.texture.BlinkOnHitComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.OnHitActionComponent;
import com.byrjamin.wickedwizard.ecs.components.identifiers.BulletComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.CollisionBoundComponent;
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
    ComponentMapper<BlinkOnHitComponent> blinkMapper;
    ComponentMapper<OnHitActionComponent> onHitActionMapper;
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

                BlinkOnHitComponent bc = world.getSystem(FindPlayerSystem.class).getPlayerComponent(BlinkOnHitComponent.class);

                if(!bc.isHit) {
                    HealthComponent hc = world.getSystem(FindPlayerSystem.class).getPlayerComponent(HealthComponent.class);
                    hc.applyDamage(1);
                }

                world.getSystem(com.byrjamin.wickedwizard.ecs.systems.ai.OnDeathSystem.class).kill(e);

            }

        } else if(fm.has(e)){
            EntitySubscription subscription = world.getAspectSubscriptionManager().get(Aspect.all(CollisionBoundComponent.class, HealthComponent.class).exclude(PlayerComponent.class).one(EnemyComponent.class,OnlyPlayerBulletsComponent.class) );
            IntBag entityIds = subscription.getEntities();
            bulletScan(e, entityIds);
        }

        EntitySubscription subscription = world.getAspectSubscriptionManager().get(Aspect.all(
                CollisionBoundComponent.class, HealthComponent.class).exclude(EnemyComponent.class, PlayerComponent.class, OnlyPlayerBulletsComponent.class));
        IntBag entityIds = subscription.getEntities();
        bulletScan(e, entityIds);


    }


    public void bulletScan(Entity bullet, IntBag entityIds){


        for(int i = 0; i < entityIds.size(); i++){

            int entity = entityIds.get(i);

            for(HitBox hb : cbm.get(entity).hitBoxes){
                if(hb.hitbox.overlaps(cbm.get(bullet).bound) && !cbm.get(entity).hitBoxDisabled){
                    HealthComponent hc = hm.get(entity);

                    System.out.println("Damage is " + bulm.get(bullet).damage);
                    hc.applyDamage(bulm.get(bullet).damage);
                    if(blinkMapper.has(entity)){
                        BlinkOnHitComponent bc = blinkMapper.get(entityIds.get(i));
                        bc.isHit = true;
                    }

                    if(onHitActionMapper.has(entity)){
                        onHitActionMapper.get(entity).action.performAction(world, world.getEntity(entity));
                    }

                   // if)

                    world.getSystem(OnDeathSystem.class).kill(bullet);
                    break;
                }
            }

        }

    }


}
