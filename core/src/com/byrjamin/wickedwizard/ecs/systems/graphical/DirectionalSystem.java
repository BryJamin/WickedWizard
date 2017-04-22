package com.byrjamin.wickedwizard.ecs.systems.graphical;

import com.artemis.Aspect;
import com.artemis.BaseSystem;
import com.artemis.Component;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.World;
import com.badlogic.gdx.math.Vector2;
import com.byrjamin.wickedwizard.ecs.components.OnDeathComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.DirectionalComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.PositionComponent;
import com.byrjamin.wickedwizard.utils.ComponentBag;
import com.byrjamin.wickedwizard.utils.enums.Direction;

/**
 * Created by Home on 07/04/2017.
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
