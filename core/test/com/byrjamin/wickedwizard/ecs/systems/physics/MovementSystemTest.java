package com.byrjamin.wickedwizard.ecs.systems.physics;

import com.artemis.Entity;
import com.artemis.World;
import com.artemis.WorldConfiguration;
import com.artemis.WorldConfigurationBuilder;
import com.byrjamin.wickedwizard.GameTest;
import com.byrjamin.wickedwizard.ecs.components.BlinkComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.PositionComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.VelocityComponent;
import com.byrjamin.wickedwizard.ecs.systems.graphical.AnimationSystem;
import com.byrjamin.wickedwizard.ecs.systems.graphical.BoundsDrawingSystem;
import com.byrjamin.wickedwizard.ecs.systems.graphical.FadeSystem;
import com.byrjamin.wickedwizard.ecs.systems.graphical.RenderingSystem;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by Home on 24/04/2017.
 */

public class MovementSystemTest extends GameTest {



    @Test
    public void testMovement() throws Exception {


        WorldConfiguration config = new WorldConfigurationBuilder()
                .with(WorldConfigurationBuilder.Priority.HIGHEST,
                        new MovementSystem()
                )
                .build();

        World world = new World(config);
        world.setDelta(0.016f);

        Entity e = world.createEntity();
        e.edit().add(new PositionComponent(0,0));
        e.edit().add(new VelocityComponent(100, 0));

        world.process();

        Assert.assertEquals(e.getComponent(PositionComponent.class).position.x, 0 + (100 * 0.016), 0.001);



    }

}
