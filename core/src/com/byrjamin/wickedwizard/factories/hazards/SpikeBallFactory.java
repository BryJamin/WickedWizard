package com.byrjamin.wickedwizard.factories.hazards;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.math.Rectangle;
import com.byrjamin.wickedwizard.ecs.components.CollisionBoundComponent;
import com.byrjamin.wickedwizard.ecs.components.identifiers.HazardComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.BounceComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.PositionComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.VelocityComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.TextureRegionComponent;
import com.byrjamin.wickedwizard.factories.AbstractFactory;
import com.byrjamin.wickedwizard.utils.ComponentBag;
import com.byrjamin.wickedwizard.utils.Measure;

/**
 * Created by Home on 24/05/2017.
 */

public class SpikeBallFactory extends AbstractFactory{


    public SpikeBallFactory(AssetManager assetManager) {
        super(assetManager);
    }


    public ComponentBag spikeBall(float x, float y, float width, float height){

        ComponentBag bag = new ComponentBag();

        x = x - width / 2;
        y = y - height / 2;

        bag.add(new PositionComponent(x,y));
        bag.add(new CollisionBoundComponent(new Rectangle(x,y,width,height), true));

        bag.add(new TextureRegionComponent(atlas.findRegion("mace"),
                width,
                height, TextureRegionComponent.ENEMY_LAYER_NEAR));

        bag.add(new HazardComponent());
        return bag;
    }


    public ComponentBag bouncingSpikeBall(float x, float y, float width, float height, VelocityComponent vc){
        ComponentBag bag = spikeBall(x,y,width,height);
        bag.add(new BounceComponent());
        bag.add(vc);
        return bag;
    }


}
