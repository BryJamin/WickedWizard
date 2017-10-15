package com.bryjamin.wickedwizard.ecs.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySubscription;
import com.artemis.systems.EntityProcessingSystem;
import com.artemis.utils.IntBag;
import com.bryjamin.wickedwizard.ecs.components.ai.OnHitActionComponent;
import com.bryjamin.wickedwizard.ecs.components.identifiers.EnemyComponent;
import com.bryjamin.wickedwizard.ecs.components.identifiers.FriendlyComponent;
import com.bryjamin.wickedwizard.ecs.components.identifiers.PlayerComponent;
import com.bryjamin.wickedwizard.ecs.components.movement.CollisionBoundComponent;
import com.bryjamin.wickedwizard.ecs.components.texture.BlinkOnHitComponent;

/**
 * Created by Home on 05/03/2017.
 */
public class BulletSystem extends EntityProcessingSystem {


    ComponentMapper<CollisionBoundComponent> cbm;
    ComponentMapper<com.bryjamin.wickedwizard.ecs.components.HealthComponent> hm;
    ComponentMapper<EnemyComponent> em;
    ComponentMapper<FriendlyComponent> fm;
    ComponentMapper<BlinkOnHitComponent> blinkMapper;
    ComponentMapper<OnHitActionComponent> onHitActionMapper;
    ComponentMapper<com.bryjamin.wickedwizard.ecs.components.identifiers.BulletComponent> bulm;


    @SuppressWarnings("unchecked")
    public BulletSystem() {
        super(Aspect.all(com.bryjamin.wickedwizard.ecs.components.identifiers.BulletComponent.class, CollisionBoundComponent.class).one(EnemyComponent.class, FriendlyComponent.class));
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
                    com.bryjamin.wickedwizard.ecs.components.HealthComponent hc = world.getSystem(FindPlayerSystem.class).getPlayerComponent(com.bryjamin.wickedwizard.ecs.components.HealthComponent.class);
                    hc.applyDamage(1);
                }

                world.getSystem(com.bryjamin.wickedwizard.ecs.systems.ai.OnDeathSystem.class).kill(e);

            }

        } else if(fm.has(e)){
            EntitySubscription subscription = world.getAspectSubscriptionManager().get(Aspect.all(CollisionBoundComponent.class, com.bryjamin.wickedwizard.ecs.components.HealthComponent.class).exclude(PlayerComponent.class).one(EnemyComponent.class, com.bryjamin.wickedwizard.ecs.components.identifiers.OnlyPlayerBulletsComponent.class) );
            IntBag entityIds = subscription.getEntities();
            bulletScan(e, entityIds);

        }

        EntitySubscription subscription = world.getAspectSubscriptionManager().get(Aspect.all(
                CollisionBoundComponent.class, com.bryjamin.wickedwizard.ecs.components.HealthComponent.class).exclude(EnemyComponent.class, PlayerComponent.class, com.bryjamin.wickedwizard.ecs.components.identifiers.OnlyPlayerBulletsComponent.class));
        IntBag entityIds = subscription.getEntities();
        bulletScan(e, entityIds);


    }


    public void bulletScan(Entity bullet, IntBag entityIds){


        for(int i = 0; i < entityIds.size(); i++){

            int entity = entityIds.get(i);

            for(com.bryjamin.wickedwizard.utils.collider.HitBox hb : cbm.get(entity).hitBoxes){

                if(hb.hitbox.overlaps(cbm.get(bullet).bound) && !cbm.get(entity).hitBoxDisabled){

                    com.bryjamin.wickedwizard.ecs.components.HealthComponent hc = hm.get(entity);

                    hc.applyDamage(bulm.get(bullet).damage);
                    if(blinkMapper.has(entity)){
                        BlinkOnHitComponent bc = blinkMapper.get(entityIds.get(i));
                        bc.isHit = true;
                    }

                    if(onHitActionMapper.has(entity)){
                        onHitActionMapper.get(entity).action.performAction(world, world.getEntity(entity));
                    }

                   // if)

                    world.getSystem(com.bryjamin.wickedwizard.ecs.systems.ai.OnDeathSystem.class).kill(bullet);
                    break;
                }
            }

        }

    }


}
