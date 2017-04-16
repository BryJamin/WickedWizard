package com.byrjamin.wickedwizard.factories.items;

import com.badlogic.gdx.math.Rectangle;
import com.byrjamin.wickedwizard.ecs.components.CollisionBoundComponent;
import com.byrjamin.wickedwizard.ecs.components.ItemComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.GravityComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.PositionComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.VelocityComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.TextureRegionComponent;
import com.byrjamin.wickedwizard.screens.PlayScreen;
import com.byrjamin.wickedwizard.utils.ComponentBag;
import com.byrjamin.wickedwizard.utils.Measure;

import java.util.Random;

/**
 * Created by Home on 09/04/2017.
 */

public class ItemFactory {


    public static ComponentBag createItemBag(float x, float y, Item item){

        ComponentBag bag = new ComponentBag();
        bag.add(new PositionComponent(x,y));

        Random random = new Random();

        bag.add(new VelocityComponent(random.nextInt((int) Measure.units(60f)) -Measure.units(30f), Measure.units(30f)));
        bag.add(new GravityComponent());
        bag.add(new ItemComponent(item));
        bag.add(new CollisionBoundComponent(new Rectangle(x,y, Measure.units(5), Measure.units(5))));
        bag.add(new TextureRegionComponent(item.getRegion(), Measure.units(5), Measure.units(5),
                TextureRegionComponent.PLAYER_LAYER_FAR));

        return bag;
    }


    public static ComponentBag createFloatingItemBag(float x, float y, Item item){

        float width = Measure.units(8);
        float height = Measure.units(8);

        x = x - width / 2;
        y = y - height / 2;

        ComponentBag bag = new ComponentBag();
        bag.add(new PositionComponent(x,y));
        bag.add(new ItemComponent(item));
        bag.add(new CollisionBoundComponent(new Rectangle(x,y, width, height)));
        bag.add(new TextureRegionComponent(item.getRegion(), width, height,
                TextureRegionComponent.PLAYER_LAYER_FAR));
        return bag;
    }




}
