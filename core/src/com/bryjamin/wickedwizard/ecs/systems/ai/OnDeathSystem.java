package com.bryjamin.wickedwizard.ecs.systems.ai;

import com.artemis.BaseSystem;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.badlogic.gdx.utils.Array;
import com.bryjamin.wickedwizard.ecs.components.ai.OnDeathActionComponent;
import com.bryjamin.wickedwizard.ecs.components.identifiers.ChildComponent;
import com.bryjamin.wickedwizard.ecs.components.identifiers.LootComponent;
import com.bryjamin.wickedwizard.ecs.components.identifiers.MinionComponent;
import com.bryjamin.wickedwizard.ecs.components.identifiers.ParentComponent;
import com.bryjamin.wickedwizard.ecs.components.identifiers.PlayerComponent;
import com.bryjamin.wickedwizard.ecs.components.movement.CollisionBoundComponent;
import com.bryjamin.wickedwizard.ecs.components.movement.PositionComponent;
import com.bryjamin.wickedwizard.ecs.systems.FindChildSystem;
import com.bryjamin.wickedwizard.ecs.systems.LuckSystem;

/**
 * Created by BB on 01/04/2017.
 *
 * OnDeathSystem is used to properly dispose of Entities within the world
 * If an entity has an action to perform on death the 'kill' method runs said action.
 * If it has loot to drop the LootSystem is called to create loot
 * If the entity is a parent or child component a parent removes itself and it's children.
 * A child removes itself and removes itself from it's parent
 */

public class OnDeathSystem  extends BaseSystem {

    ComponentMapper<OnDeathActionComponent> odam;
    ComponentMapper<LootComponent> lm;
    ComponentMapper<CollisionBoundComponent> cbm;
    ComponentMapper<PositionComponent> pm;
    ComponentMapper<ParentComponent> parentm;

    ComponentMapper<PlayerComponent> playerm;
    ComponentMapper<ChildComponent> cm;

    ComponentMapper<MinionComponent> mm;


    private Array<ChildComponent> temporaryChildStore = new Array<ChildComponent>();

    @Override
    protected void processSystem() {}

    @Override
    protected boolean checkProcessing() {
        return false;
    }


    public void kill(Entity deadEntity) {

        if(odam.has(deadEntity)) odam.get(deadEntity).action.performAction(world, deadEntity);


        if(lm.has(deadEntity) && cbm.has(deadEntity) && !mm.has(deadEntity)) {
            CollisionBoundComponent cbc = cbm.get(deadEntity);
            world.getSystem(LuckSystem.class).rollForLoot(lm.get(deadEntity), cbc.getCenterX(), cbc.getCenterY());
        }

        if(cm.has(deadEntity)){
            Entity parent = world.getSystem(FindChildSystem.class).findParentEntity(cm.get(deadEntity));
            if(parent != null) {
                parent.getComponent(ParentComponent.class).children.removeValue(cm.get(deadEntity), true);
            }
        }

        if(parentm.has(deadEntity)) killChildComponents(parentm.get(deadEntity));


        deadEntity.deleteFromWorld();
    }


    /**
     * Runs the kill command on the child entities of a parent
     * @param parentComponent - The parent component with child entities
     */
    public void killChildComponents(ParentComponent parentComponent){

        temporaryChildStore.clear();
        temporaryChildStore.addAll(parentComponent.children);

        for(ChildComponent c : temporaryChildStore){
            Entity child = world.getSystem(FindChildSystem.class).findChildEntity(c);
            if(child != null) {
                kill(child);
            }
            parentComponent.children.removeValue(c, true);
        }

    }


    /**
     * Deletes all child components from the parent component within running any 'death actions'
     * the child entity might have
     * @param parentComponent - The parent component with child entities
     */
    public void killChildComponentsIgnoreOnDeath(ParentComponent parentComponent){

        temporaryChildStore.clear();
        temporaryChildStore.addAll(parentComponent.children);

        for(ChildComponent c : temporaryChildStore){
            Entity child = world.getSystem(FindChildSystem.class).findChildEntity(c);
            if(child != null) {
                child.deleteFromWorld();
            }
            parentComponent.children.removeValue(c, true);
        }

    }

}
