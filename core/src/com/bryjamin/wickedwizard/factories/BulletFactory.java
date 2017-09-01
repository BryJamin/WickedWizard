package com.bryjamin.wickedwizard.factories;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.bryjamin.wickedwizard.ecs.components.texture.TextureRegionComponent;

/**
 * Created by Home on 29/03/2017.
 */

public class BulletFactory extends AbstractFactory {



    public BulletFactory(AssetManager assetManager) {
        super(assetManager);

    }

    private float width = com.bryjamin.wickedwizard.utils.Measure.units(1);
    private float height = com.bryjamin.wickedwizard.utils.Measure.units(1);


    public com.bryjamin.wickedwizard.utils.ComponentBag basicBulletBag(float x, float y, float scale) {
        return basicBulletBag(x ,y ,scale ,atlas.findRegion(com.bryjamin.wickedwizard.assets.TextureStrings.BLOCK), new Color(1,1,1,1));
    }

    public com.bryjamin.wickedwizard.utils.ComponentBag basicBulletBag(float x, float y, float scale, Color color) {
        return basicBulletBag(x ,y ,scale ,atlas.findRegion(com.bryjamin.wickedwizard.assets.TextureStrings.BLOCK), color);
    }

    public com.bryjamin.wickedwizard.utils.ComponentBag basicEnemyBulletBag(float x, float y, float scale) {
        com.bryjamin.wickedwizard.utils.ComponentBag bag = basicBulletBag(x ,y ,scale ,atlas.findRegion(com.bryjamin.wickedwizard.assets.TextureStrings.BLOCK) , new Color(Color.RED));
        bag.add(new com.bryjamin.wickedwizard.ecs.components.identifiers.EnemyComponent());
        bag.add(new com.bryjamin.wickedwizard.ecs.components.ai.OnDeathActionComponent(new com.bryjamin.wickedwizard.factories.weapons.Giblets.GibletBuilder(assetManager)
                .numberOfGibletPairs(3)
                .size(com.bryjamin.wickedwizard.utils.Measure.units(0.5f))
                .minSpeed(com.bryjamin.wickedwizard.utils.Measure.units(10f))
                .maxSpeed(com.bryjamin.wickedwizard.utils.Measure.units(20f))
                .colors(new Color(Color.RED))
                .intangible(false)
                .expiryTime(0.2f).build()));
        return bag;
    }


    public com.bryjamin.wickedwizard.utils.ComponentBag basicBulletBag(float x, float y, float scale, TextureRegion textureRegion, Color color) {
        com.bryjamin.wickedwizard.utils.ComponentBag bag = new com.bryjamin.wickedwizard.utils.ComponentBag();

        float width = this.width * scale;
        float height = this.height * scale;

        float cX = x - width / 2;
        float cY = y - height / 2;

        bag.add(new com.bryjamin.wickedwizard.ecs.components.movement.PositionComponent(cX, cY));
        bag.add(new com.bryjamin.wickedwizard.ecs.components.identifiers.BulletComponent());
        //bag.add(new ExpireComponent(10f)); //TODO Probably doesn't have to be this long (or delete bullets if they leave the room bounds)
        bag.add(new com.bryjamin.wickedwizard.ecs.components.movement.CollisionBoundComponent(new Rectangle
                (cX,cY, width, height)));

        TextureRegionComponent trc = new TextureRegionComponent(textureRegion, width, height, TextureRegionComponent.PLAYER_LAYER_FAR);
        trc.DEFAULT = color;
        trc.color = color;

        bag.add(trc);
        return bag;
    }


}
