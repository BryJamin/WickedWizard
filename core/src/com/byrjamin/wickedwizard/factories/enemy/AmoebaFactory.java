package com.byrjamin.wickedwizard.factories.enemy;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.IntMap;
import com.byrjamin.wickedwizard.assets.TextureStrings;
import com.byrjamin.wickedwizard.ecs.components.CollisionBoundComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.ExploderComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.MoveToPlayerComponent;
import com.byrjamin.wickedwizard.ecs.components.identifiers.IntangibleComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.AccelerantComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.BounceComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.DirectionalComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.PositionComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.VelocityComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.AnimationComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.AnimationStateComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.TextureRegionComponent;
import com.byrjamin.wickedwizard.utils.ComponentBag;
import com.byrjamin.wickedwizard.utils.Measure;

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


    public ComponentBag fastamoeba(float x , float y){
        x = x - width / 2;
        y = y - height / 2;
        return amoebaBag(x,y, Measure.units(10f), new Color(Color.RED));
    }



    public ComponentBag amoebaBag(float x, float y, float speed, Color color){

        ComponentBag bag = new ComponentBag();
        //bag.add(new PositionComponent(x,y));
        this.defaultEnemyBagNoLoot(bag, x, y, width, height, 1);

        bag.add(new CollisionBoundComponent(new Rectangle(x, y, width, height), true));
        bag.add(new VelocityComponent());
        bag.add(new MoveToPlayerComponent());
        bag.add(new AccelerantComponent(Measure.units(5f), Measure.units(5f)));
        bag.add(new ExploderComponent());

        bag.add(new IntangibleComponent());
        //bag.add(new DirectionalComponent());

        bag.add(new AnimationStateComponent(0));
        IntMap<Animation<TextureRegion>> animMap = new IntMap<Animation<TextureRegion>>();
        animMap.put(0, new Animation<TextureRegion>(0.15f / 1f,
                atlas.findRegions(TextureStrings.AMOEBA), Animation.PlayMode.LOOP));

        bag.add(new AnimationComponent(animMap));

        TextureRegionComponent tfc = new TextureRegionComponent(atlas.findRegion(TextureStrings.AMOEBA),
                textureoffsetX, textureoffsetY, textureWidth, textureHeight,
                TextureRegionComponent.FOREGROUND_LAYER_MIDDLE, new Color(238f / 255f, 187f / 255f, 240f / 255f, 1));

        tfc.scaleX = -1;

        bag.add(tfc);

/*        bag.add(new TextureRegionComponent(atlas.findRegion(TextureStrings.AMOEBA),
                textureoffsetX, textureoffsetY, textureWidth, textureHeight,
                TextureRegionComponent.ENEMY_LAYER_MIDDLE, new Color(Color.CHARTREUSE)));*/

        return bag;


    }

    public ComponentBag amoeba(float x, float y) {

        x = x - width / 2;
        y = y - height / 2;

        return amoebaBag(x,y, Measure.units(5f), new Color(238f / 255f, 187f / 255f, 240f / 255f, 1));

    }









}
