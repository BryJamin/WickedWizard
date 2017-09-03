package com.bryjamin.wickedwizard.factories.enemy;

import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.IntMap;
import com.bryjamin.wickedwizard.ecs.components.ai.Action;
import com.bryjamin.wickedwizard.ecs.components.ai.ActionAfterTimeComponent;
import com.bryjamin.wickedwizard.ecs.components.movement.CollisionBoundComponent;
import com.bryjamin.wickedwizard.ecs.components.texture.AnimationComponent;
import com.bryjamin.wickedwizard.ecs.components.texture.AnimationStateComponent;
import com.bryjamin.wickedwizard.ecs.components.texture.TextureRegionComponent;
import com.bryjamin.wickedwizard.factories.weapons.enemy.MultiPistol;
import com.bryjamin.wickedwizard.utils.ComponentBag;
import com.bryjamin.wickedwizard.utils.Measure;

/**
 * Created by Home on 13/04/2017.
 */

public class KugelDuscheFactory extends EnemyFactory {

    private final float width = Measure.units(10f);
    private final float height = Measure.units(10f);

    private final float health = 11f;

    private final float textureWidth = Measure.units(12);
    private final float textureHeight = Measure.units(12);

    private final float textureOffsetX = -Measure.units(1.5f);
    private final float textureOffsetY = 0;


    private static final float kugelHealth = 20;
    private static final float laserKugelHealth = 25;

    private final float changeAngleTime = 0.15f;
    private final static float changeInDegrees = 11;

    public KugelDuscheFactory(AssetManager assetManager) {
        super(assetManager);
    }


    public ComponentBag kugelDusche(float x, float y, final boolean isLeft){

        x = x - width / 2;
        y = y - height / 2;

        ComponentBag bag = this.defaultEnemyBag(new ComponentBag(), x , y, kugelHealth);
        bag.add(new CollisionBoundComponent(new Rectangle(x, y, width, height), true));


        bag.add(new AnimationStateComponent(0));
        IntMap<Animation<TextureRegion>> animMap = new IntMap<Animation<TextureRegion>>();
        animMap.put(0, new Animation<TextureRegion>(0.1f / 1f, atlas.findRegions(com.bryjamin.wickedwizard.assets.TextureStrings.KUGELDUSCHE_EMPTY),
                (isLeft) ? Animation.PlayMode.LOOP : Animation.PlayMode.LOOP_REVERSED));


        bag.add(new AnimationComponent(animMap));


        TextureRegionComponent trc = new TextureRegionComponent(atlas.findRegion(com.bryjamin.wickedwizard.assets.TextureStrings.KUGELDUSCHE_EMPTY),
                width, height, TextureRegionComponent.ENEMY_LAYER_MIDDLE);

        trc.color = new Color(Color.BLACK);
        trc.DEFAULT = new Color(Color.BLACK);

        bag.add(trc);
        bag.add(new com.bryjamin.wickedwizard.ecs.components.ai.FiringAIComponent(90));
        bag.add(new com.bryjamin.wickedwizard.ecs.components.WeaponComponent(new MultiPistol.PistolBuilder(assetManager)
                .angles(0, 180)
                .fireRate(0.2f)
                .shotSpeed(Measure.units(40f))
                .build(), 0.1f));

        bag.add(new ActionAfterTimeComponent(new Action() {
            @Override
            public void performAction(World world, Entity e) {
                e.getComponent(com.bryjamin.wickedwizard.ecs.components.ai.FiringAIComponent.class).firingAngleInRadians += Math.toRadians((isLeft) ? changeInDegrees : -changeInDegrees);
            }
        }, changeAngleTime, true));

        return bag;


    }



    //TODO since this changes this should be a different animation
    public ComponentBag laserKugel(float x, float y, boolean isLeft){

        x = x - width / 2;
        y = y - height / 2;

        ComponentBag bag = this.defaultEnemyBag(new ComponentBag(), x , y, laserKugelHealth);
        bag.add(new CollisionBoundComponent(new Rectangle(x, y, width, height), true));


        bag.add(new AnimationStateComponent(AnimationStateComponent.DEFAULT));
        IntMap<Animation<TextureRegion>> animMap = new IntMap<Animation<TextureRegion>>();
        animMap.put(AnimationStateComponent.DEFAULT, new Animation<TextureRegion>(0.1f / 1f, atlas.findRegions(com.bryjamin.wickedwizard.assets.TextureStrings.KUGELDUSCHE_LASER),
                (isLeft) ? Animation.PlayMode.LOOP : Animation.PlayMode.LOOP_REVERSED));


        bag.add(new AnimationComponent(animMap));


        TextureRegionComponent trc = new TextureRegionComponent(atlas.findRegion(com.bryjamin.wickedwizard.assets.TextureStrings.KUGELDUSCHE_LASER),
                width, height, TextureRegionComponent.ENEMY_LAYER_MIDDLE);

        trc.color = new Color(Color.BLACK);
        trc.DEFAULT = new Color(Color.BLACK);

        bag.add(trc);

        bag.add(new ActionAfterTimeComponent(new com.bryjamin.wickedwizard.factories.weapons.enemy.LaserOrbitalTask.LaserBuilder(assetManager)
                .orbitalAndIntervalSize(Measure.units(5f))
                .speedInDegrees(isLeft ? 1.25f : -1.25f)
                .numberOfOrbitals(16)
                .layer(TextureRegionComponent.ENEMY_LAYER_MIDDLE)
                .chargeTime(1f)
                .angles(0, 180)
                .build()));

        return bag;


    }



}
