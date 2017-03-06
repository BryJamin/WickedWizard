package com.byrjamin.wickedwizard.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.OrderedSet;
import com.byrjamin.wickedwizard.components.CollisionBoundComponent;
import com.byrjamin.wickedwizard.components.PositionComponent;
import com.byrjamin.wickedwizard.components.VelocityComponent;
import com.byrjamin.wickedwizard.components.WallComponent;
import com.byrjamin.wickedwizard.helper.BoundsDrawer;

/**
 * Created by Home on 04/03/2017.
 */
public class WallSystem extends EntityProcessingSystem {

    ComponentMapper<WallComponent> wm;
    ComponentMapper<VelocityComponent> vm;

    OrderedSet<Rectangle> walls;

    @SuppressWarnings("unchecked")
    public WallSystem() {
        super(Aspect.all(WallComponent.class));
        walls = new OrderedSet<Rectangle>();
    }

    @Override
    protected void process(Entity e) {
        WallComponent wc = wm.get(e);
        walls.add(wc.bound);
        BoundsDrawer.drawBounds(world.getSystem(RenderingSystem.class).batch, walls.orderedItems());
    }

    @Override
    protected boolean checkProcessing() {
        return walls.size == 0;
    }

    public OrderedSet<Rectangle> getWalls() {
        return walls;
    }
}
