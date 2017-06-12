package com.byrjamin.wickedwizard.factories.enemy;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.IntMap;
import com.byrjamin.wickedwizard.assets.TextureStrings;
import com.byrjamin.wickedwizard.ecs.components.CollisionBoundComponent;
import com.byrjamin.wickedwizard.ecs.components.WeaponComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.FiringAIComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.BounceComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.VelocityComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.AnimationComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.AnimationStateComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.TextureRegionComponent;
import com.byrjamin.wickedwizard.factories.weapons.enemy.CircleBlast;
import com.byrjamin.wickedwizard.utils.ComponentBag;
import com.byrjamin.wickedwizard.utils.Measure;

/**
 * Created by Home on 11/06/2017.
 */

public class JigFactory extends EnemyFactory{

    private float width = Measure.units(10f);
    private float height = Measure.units(10f);

    private float texwidth = Measure.units(10f);
    private float texheight = Measure.units(10f);

    private float health = 15;

    public JigFactory(AssetManager assetManager) {
        super(assetManager);
    }


    public ComponentBag stationaryJig(float x, float y){

        x = x - width / 2;
        y = y - height / 2;

        ComponentBag bag = new ComponentBag();
        this.defaultEnemyBag(bag, x,y,width,height,15);


        bag.add(new CollisionBoundComponent(new Rectangle(x,y,width,height), true));


        TextureRegionComponent trc = new TextureRegionComponent(atlas.findRegion(TextureStrings.JIG_FIRING),
                (width / 2) - (texwidth / 2), (height / 2) - (texheight / 2), texwidth, texheight, TextureRegionComponent.ENEMY_LAYER_MIDDLE);

        trc.color = new Color(91f / 255f,50f / 255f,86f / 255f, 1);
        trc.DEFAULT = new Color(91f / 255f,50f / 255f,86f / 255f, 1);

        bag.add(trc);
/*
        bag.add(new VelocityComponent(Measure.units(10f), 0));
        bag.add(new BounceComponent());*/

        bag.add(new AnimationStateComponent(0));
        IntMap<Animation<TextureRegion>> animMap = new IntMap<Animation<TextureRegion>>();
        animMap.put(0, new Animation<TextureRegion>(0.3f / 1f,
                atlas.findRegions(TextureStrings.JIG_FIRING), Animation.PlayMode.LOOP));
        animMap.put(AnimationStateComponent.FIRING, new Animation<TextureRegion>(0.1f / 1f,
                atlas.findRegions(TextureStrings.JIG_FIRING)));
        bag.add(new AnimationComponent(animMap));


        bag.add(new FiringAIComponent(0,0));

        CircleBlast cb = new CircleBlast(assetManager, new int[] {0, 60,120,180,240,300}, 0.75f, 30f, true);
        cb.setSpeed(Measure.units(20f));
        cb.setSize(3);

        bag.add(new WeaponComponent(cb, 0));

        return bag;


    }








}
