package com.byrjamin.wickedwizard.ecs.systems.ai;

import com.artemis.Aspect;
import com.artemis.BaseSystem;
import com.artemis.Component;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySubscription;
import com.artemis.utils.IntBag;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.byrjamin.wickedwizard.ecs.components.CollisionBoundComponent;
import com.byrjamin.wickedwizard.ecs.components.OnDeathComponent;
import com.byrjamin.wickedwizard.ecs.components.identifiers.EnemyComponent;
import com.byrjamin.wickedwizard.ecs.components.identifiers.LootComponent;
import com.byrjamin.wickedwizard.ecs.components.identifiers.MinionComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.PositionComponent;
import com.byrjamin.wickedwizard.ecs.systems.LuckSystem;
import com.byrjamin.wickedwizard.utils.ComponentBag;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;

import javafx.geometry.Pos;

/**
 * Created by Home on 01/04/2017.
 */

public class OnDeathSystem  extends BaseSystem {

    ComponentMapper<OnDeathComponent> odm;
    ComponentMapper<LootComponent> lm;
    ComponentMapper<CollisionBoundComponent> cbm;
    ComponentMapper<PositionComponent> pm;

    ComponentMapper<MinionComponent> mm;

    @Override
    protected void processSystem() {

    }

    @Override
    protected boolean checkProcessing() {
        return false;
    }


    public void kill(Entity deadEntity) {

        PositionComponent pc = pm.get(deadEntity);

        if(odm.has(deadEntity)){

            System.out.println("so...");

            System.out.println(odm.get(deadEntity).getComponentBags().size);

            for (ComponentBag bag : odm.get(deadEntity).getComponentBags()) {
                Entity newEntity = world.createEntity();
                System.out.println("why though");
                for (Component c : bag) {

                    newEntity.edit().add(c);
                }


                if(cbm.has(deadEntity)){
                    CollisionBoundComponent cbc = cbm.get(deadEntity);

                    if(cbm.has (newEntity)) {
                        CollisionBoundComponent odcbc = cbm.get(newEntity);

                        newEntity.getComponent(PositionComponent.class).position = new Vector3(cbc.getCenterX(), cbc.getCenterY(), 0);

                        if(cbm.has(newEntity)){
                            Rectangle r = newEntity.getComponent(CollisionBoundComponent.class).bound;
                            r.x = cbc.getCenterX();
                            r.y = cbc.getCenterY();
                        }


                    } else {

                        newEntity.getComponent(PositionComponent.class).position = new Vector3(pc.position);
                    }

                } else {

                    newEntity.getComponent(PositionComponent.class).position = new Vector3(pc.position);
                }
            }
        }

        if(lm.has(deadEntity) && cbm.has(deadEntity) && !mm.has(deadEntity)) {
            CollisionBoundComponent cbc = cbm.get(deadEntity);
            world.getSystem(LuckSystem.class).spawnPickUp(cbc.getCenterX(), cbc.getCenterY());
        }

        deadEntity.deleteFromWorld();


        EntitySubscription subscription = world.getAspectSubscriptionManager().get(Aspect.all());
        IntBag entityIds = subscription.getEntities();
        //System.out.println(entityIds.size());
    }


}
