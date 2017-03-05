package com.byrjamin.wickedwizard.factories;

import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.math.Rectangle;
import com.byrjamin.wickedwizard.components.PositionComponent;
import com.byrjamin.wickedwizard.components.WallComponent;
import com.byrjamin.wickedwizard.helper.Measure;

/**
 * Created by Home on 04/03/2017.
 */
public class EntityFactory {

    public static Entity createWall(World world, float x, float y, float width, float height){
        Entity e = world.createEntity();
        e.edit().add(new PositionComponent(x,y));
        e.edit().add(new WallComponent(new Rectangle(x,y, width, height)));
        return e;
    }

    public static Entity createWall(World world, Rectangle r){
        return createWall(world, r.x, r.y, r.width, r.height);
    }

}
