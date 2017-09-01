package com.bryjamin.wickedwizard.factories.enemy;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.IntMap;
import com.bryjamin.wickedwizard.assets.ColorResource;
import com.bryjamin.wickedwizard.ecs.components.movement.CollisionBoundComponent;
import com.bryjamin.wickedwizard.ecs.components.ai.ExploderComponent;
import com.bryjamin.wickedwizard.ecs.components.ai.MoveToPlayerComponent;
import com.bryjamin.wickedwizard.ecs.components.identifiers.IntangibleComponent;
import com.bryjamin.wickedwizard.ecs.components.movement.AccelerantComponent;
import com.bryjamin.wickedwizard.ecs.components.texture.AnimationComponent;
import com.bryjamin.wickedwizard.ecs.components.texture.AnimationStateComponent;
import com.bryjamin.wickedwizard.ecs.components.texture.FadeComponent;
import com.bryjamin.wickedwizard.ecs.components.texture.TextureRegionComponent;
import com.bryjamin.wickedwizard.utils.Measure;

/**
 * Created by Home on 26/05/2017.
 */

public class AmoebaFactory extends EnemyFactory {

    public AmoebaFactory(AssetManager assetManager) {
        super(assetManager);
    }

    private float width = Measure.units(2.5f);
    private float height = Measure.units(2.5f);
    private float textureWidth = Measure.units(4.5f);
    private float textureHeight = Measure.units(4.5f);
    private float textureoffsetX = (width / 2) - (textureWidth / 2);
    private float textureoffsetY = (height / 2) - (textureHeight / 2);

    private static final float fastSpeed = Measure.units(20f);

    public com.bryjamin.wickedwizard.utils.ComponentBag fastamoeba(float x , float y){
        x = x - width / 2;
        y = y - height / 2;
        return amoebaBag(x,y, fastSpeed, new Color(ColorResource.AMOEBA_FAST_PURPLE));
    }



    public com.bryjamin.wickedwizard.utils.ComponentBag amoebaBag(float x, float y, float speed, Color color){

        com.bryjamin.wickedwizard.utils.ComponentBag bag = new com.bryjamin.wickedwizard.utils.ComponentBag();
        //bag.add(new PositionComponent(x,y));
        this.defaultEnemyBagNoLoot(bag, x, y, 1);

        bag.add(new CollisionBoundComponent(new Rectangle(x, y, width, height), true));
        bag.add(new com.bryjamin.wickedwizard.ecs.components.movement.VelocityComponent());
        bag.add(new MoveToPlayerComponent());
        bag.add(new AccelerantComponent(speed, speed));
        bag.add(new ExploderComponent());

        bag.add(new FadeComponent(true, 0.5f, false));

        bag.add(new IntangibleComponent());
        //bag.add(new DirectionalComponent());

        bag.add(new AnimationStateComponent(0));
        IntMap<Animation<TextureRegion>> animMap = new IntMap<Animation<TextureRegion>>();
        animMap.put(0, new Animation<TextureRegion>(0.15f / 1f,
                atlas.findRegions(com.bryjamin.wickedwizard.assets.TextureStrings.AMOEBA), Animation.PlayMode.LOOP));

        bag.add(new AnimationComponent(animMap));

        TextureRegionComponent tfc = new TextureRegionComponent(atlas.findRegion(com.bryjamin.wickedwizard.assets.TextureStrings.AMOEBA),
                textureoffsetX, textureoffsetY, textureWidth, textureHeight,
                TextureRegionComponent.FOREGROUND_LAYER_MIDDLE, color);

        tfc.scaleX = -1;

        bag.add(tfc);

/*        bag.add(new TextureRegionComponent(atlas.findRegion(TextureStrings.AMOEBA),
                textureoffsetX, textureoffsetY, textureWidth, textureHeight,
                TextureRegionComponent.ENEMY_LAYER_MIDDLE, new Color(Color.CHARTREUSE)));*/

        return bag;


    }

    public com.bryjamin.wickedwizard.utils.ComponentBag amoeba(float x, float y) {

        x = x - width / 2;
        y = y - height / 2;

        return amoebaBag(x,y, Measure.units(5f), ColorResource.AMOEBA_BLUE);//some kind of blue

    }









}
