package com.byrjamin.wickedwizard.ecs.systems;

import com.artemis.Aspect;
import com.artemis.BaseSystem;
import com.artemis.Component;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySubscription;
import com.artemis.utils.IntBag;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.byrjamin.wickedwizard.ecs.components.OnDeathComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.PositionComponent;
import com.byrjamin.wickedwizard.ecs.components.object.WallComponent;
import com.byrjamin.wickedwizard.utils.ComponentBag;
import com.sun.xml.internal.ws.dump.LoggingDumpTube;

import javafx.geometry.Pos;

/**
 * Created by Home on 01/04/2017.
 */

public class OnDeathSystem  extends BaseSystem {

    ComponentMapper<OnDeathComponent> odm;
    ComponentMapper<PositionComponent> pm;

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

            for (ComponentBag bag : odm.get(deadEntity).getComponentBags()) {
                Entity e = world.createEntity();
                for (Component c : bag) {
                    e.edit().add(c);
                }
                e.getComponent(PositionComponent.class).position = new Vector3(pc.position);
            }
        }
        deadEntity.deleteFromWorld();


        EntitySubscription subscription = world.getAspectSubscriptionManager().get(Aspect.all());
        IntBag entityIds = subscription.getEntities();
        System.out.println(entityIds.size());
    }


}
