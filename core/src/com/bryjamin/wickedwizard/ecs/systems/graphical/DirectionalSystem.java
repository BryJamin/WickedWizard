package com.bryjamin.wickedwizard.ecs.systems.graphical;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.World;

/**
 * Created by BB on 07/04/2017.
 *
 * Used to determine the direction a Entity with a directional Component should be facing
 *
 * Resets the directional priority after ever iteration
 *
 * //TODO until I add a face onto the player character this isn't really used much
 *
 */

public class DirectionalSystem extends EntitySystem {

   static ComponentMapper<com.bryjamin.wickedwizard.ecs.components.movement.DirectionalComponent> dm;
    ComponentMapper<com.bryjamin.wickedwizard.ecs.components.movement.PositionComponent> pm;

    public DirectionalSystem() {
        super(Aspect.all(com.bryjamin.wickedwizard.ecs.components.movement.DirectionalComponent.class));
    }

    @Override
    protected void processSystem() {

        for(Entity e : this.getEntities()){
            dm.get(e).priority = com.bryjamin.wickedwizard.ecs.components.movement.DirectionalComponent.PRIORITY.LOWEST;
        }


    }

    @Override
    protected boolean checkProcessing() {
        return true;
    }


    public static void changeDirection(World world, Entity e, com.bryjamin.wickedwizard.utils.enums.Direction direction, com.bryjamin.wickedwizard.ecs.components.movement.DirectionalComponent.PRIORITY priority) {
        if(dm.has(e)){
            com.bryjamin.wickedwizard.ecs.components.movement.DirectionalComponent dc = dm.get(e);
            dc.setDirection(direction, priority);
        }
    }

}
