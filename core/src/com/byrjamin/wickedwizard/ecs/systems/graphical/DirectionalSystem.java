package com.byrjamin.wickedwizard.ecs.systems.graphical;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.World;
import com.byrjamin.wickedwizard.ecs.components.movement.DirectionalComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.PositionComponent;
import com.byrjamin.wickedwizard.utils.enums.Direction;

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

   static ComponentMapper<DirectionalComponent> dm;
    ComponentMapper<PositionComponent> pm;

    public DirectionalSystem() {
        super(Aspect.all(DirectionalComponent.class));
    }

    @Override
    protected void processSystem() {

        for(Entity e : this.getEntities()){
            dm.get(e).priority = DirectionalComponent.PRIORITY.LOWEST;
        }


    }

    @Override
    protected boolean checkProcessing() {
        return true;
    }


    public static void changeDirection(World world, Entity e, Direction direction, DirectionalComponent.PRIORITY priority) {
        if(dm.has(e)){
            DirectionalComponent dc = dm.get(e);
            dc.setDirection(direction, priority);
        }
    }

}
