package com.byrjamin.wickedwizard.ecs.systems.ai;

import com.artemis.BaseSystem;
import com.artemis.Component;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.byrjamin.wickedwizard.ecs.components.CollisionBoundComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.OnDeathActionComponent;
import com.byrjamin.wickedwizard.ecs.components.identifiers.ChildComponent;
import com.byrjamin.wickedwizard.ecs.components.identifiers.LootComponent;
import com.byrjamin.wickedwizard.ecs.components.identifiers.MinionComponent;
import com.byrjamin.wickedwizard.ecs.components.identifiers.ParentComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.PositionComponent;
import com.byrjamin.wickedwizard.ecs.systems.FindChildSystem;
import com.byrjamin.wickedwizard.ecs.systems.LuckSystem;
import com.byrjamin.wickedwizard.utils.ComponentBag;

/**
 * Created by Home on 01/04/2017.
 */

public class OnDeathSystem  extends BaseSystem {

    ComponentMapper<OnDeathActionComponent> odam;
    ComponentMapper<LootComponent> lm;
    ComponentMapper<CollisionBoundComponent> cbm;
    ComponentMapper<PositionComponent> pm;
    ComponentMapper<ParentComponent> parentm;
    ComponentMapper<ChildComponent> cm;

    ComponentMapper<MinionComponent> mm;

    @Override
    protected void processSystem() {

    }

    @Override
    protected boolean checkProcessing() {
        return false;
    }


    public void kill(Entity deadEntity) {

        if(odam.has(deadEntity)){
            odam.get(deadEntity).action.performAction(world, deadEntity);
        }


        if(lm.has(deadEntity) && cbm.has(deadEntity) && !mm.has(deadEntity)) {
            CollisionBoundComponent cbc = cbm.get(deadEntity);
            for(int i = 0; i < lm.get(deadEntity).maxDrops; i++) {
                world.getSystem(LuckSystem.class).spawnPickUp(cbc.getCenterX(), cbc.getCenterY());
            }
        }

        if(cm.has(deadEntity)){
            Entity parent = world.getSystem(FindChildSystem.class).findParentEntity(cm.get(deadEntity));
            if(parent != null) {
                parent.getComponent(ParentComponent.class).children.removeValue(cm.get(deadEntity), true);
            }
        }

        if(parentm.has(deadEntity)){
            Array<ChildComponent> children = new Array<ChildComponent>();
            children.addAll(parentm.get(deadEntity).children);

            for(ChildComponent c : children){
                Entity child = world.getSystem(FindChildSystem.class).findChildEntity(c);
                if(child != null) {
                    kill(child);
                }
                parentm.get(deadEntity).children.removeValue(c, true);
            }

        }


        deadEntity.deleteFromWorld();

     //   System.out.println(entityIds.size());
    }


}
