package com.bryjamin.wickedwizard.ecs.systems.graphical;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.bryjamin.wickedwizard.ecs.components.movement.DirectionalComponent;
import com.bryjamin.wickedwizard.ecs.components.movement.PositionComponent;
import com.bryjamin.wickedwizard.utils.enums.Direction;



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


    public static void changeDirection(Entity e, Direction direction, DirectionalComponent.PRIORITY priority) {
        if(dm.has(e)){
            DirectionalComponent dc = dm.get(e);
            dc.setDirection(direction, priority);
        }
    }


}
