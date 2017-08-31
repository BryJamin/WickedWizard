package com.byrjamin.wickedwizard.factories;

import com.artemis.Component;
import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.byrjamin.wickedwizard.assets.TextureStrings;
import com.byrjamin.wickedwizard.ecs.components.ai.OnDeathActionComponent;
import com.byrjamin.wickedwizard.ecs.components.identifiers.BulletComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.CollisionBoundComponent;
import com.byrjamin.wickedwizard.ecs.components.identifiers.EnemyComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.PositionComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.VelocityComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.TextureRegionComponent;
import com.byrjamin.wickedwizard.factories.weapons.Giblets;
import com.byrjamin.wickedwizard.utils.ComponentBag;
import com.byrjamin.wickedwizard.utils.Measure;

/**
 * Created by Home on 29/03/2017.
 */

public class BulletFactory extends AbstractFactory {



    public BulletFactory(AssetManager assetManager) {
        super(assetManager);

    }

    private float width = Measure.units(1);
    private float height = Measure.units(1);


    public ComponentBag basicBulletBag(float x, float y, float scale) {
        return basicBulletBag(x ,y ,scale ,atlas.findRegion(TextureStrings.BLOCK), new Color(1,1,1,1));
    }

    public ComponentBag basicBulletBag(float x, float y, float scale, Color color) {
        return basicBulletBag(x ,y ,scale ,atlas.findRegion(TextureStrings.BLOCK), color);
    }

    public ComponentBag basicEnemyBulletBag(float x, float y, float scale) {
        ComponentBag bag = basicBulletBag(x ,y ,scale ,atlas.findRegion(TextureStrings.BLOCK) , new Color(Color.RED));
        bag.add(new EnemyComponent());
        bag.add(new OnDeathActionComponent(new Giblets.GibletBuilder(assetManager)
                .numberOfGibletPairs(3)
                .size(Measure.units(0.5f))
                .minSpeed(Measure.units(10f))
                .maxSpeed(Measure.units(20f))
                .colors(new Color(Color.RED))
                .intangible(false)
                .expiryTime(0.2f).build()));
        return bag;
    }


    public ComponentBag basicBulletBag(float x, float y, float scale, TextureRegion textureRegion, Color color) {
        ComponentBag bag = new ComponentBag();

        float width = this.width * scale;
        float height = this.height * scale;

        float cX = x - width / 2;
        float cY = y - height / 2;

        bag.add(new PositionComponent(cX, cY));
        bag.add(new BulletComponent());
        //bag.add(new ExpireComponent(10f)); //TODO Probably doesn't have to be this long (or delete bullets if they leave the room bounds)
        bag.add(new CollisionBoundComponent(new Rectangle
                (cX,cY, width, height)));

        TextureRegionComponent trc = new TextureRegionComponent(textureRegion, width, height, TextureRegionComponent.PLAYER_LAYER_FAR);
        trc.DEFAULT = color;
        trc.color = color;

        bag.add(trc);
        return bag;
    }


}
