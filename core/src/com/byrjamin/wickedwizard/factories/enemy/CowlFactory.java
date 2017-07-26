package com.byrjamin.wickedwizard.factories.enemy;

import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.IntMap;
import com.byrjamin.wickedwizard.assets.ColorResource;
import com.byrjamin.wickedwizard.assets.TextureStrings;
import com.byrjamin.wickedwizard.ecs.components.CollisionBoundComponent;
import com.byrjamin.wickedwizard.ecs.components.WeaponComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.Action;
import com.byrjamin.wickedwizard.ecs.components.ai.ActionAfterTimeComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.FiringAIComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.ProximityTriggerAIComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.Task;
import com.byrjamin.wickedwizard.ecs.components.movement.OrbitComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.PositionComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.AnimationComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.AnimationStateComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.FadeComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.TextureRegionComponent;
import com.byrjamin.wickedwizard.ecs.systems.FindPlayerSystem;
import com.byrjamin.wickedwizard.factories.weapons.enemy.MultiPistol;
import com.byrjamin.wickedwizard.utils.ComponentBag;
import com.byrjamin.wickedwizard.utils.Measure;

/**
 * Created by Home on 25/06/2017.
 */

public class CowlFactory extends EnemyFactory{

    public float width = Measure.units(7.5f);
    public float height = Measure.units(7.5f);

    private final static float radius = Measure.units(30f);
    private final static float speedInDegrees = 1f;

    public float cowlHealth = 10f;

    public CowlFactory(AssetManager assetManager) {
        super(assetManager);
    }


    public ComponentBag cowl(float x, float y, final float startingAngleInDegrees, final boolean startsLeft){

        x = x - width / 2;
        y = y - width / 2;

        ComponentBag bag = this.defaultEnemyBag(new ComponentBag(), x , y, cowlHealth);

        FadeComponent fc = new FadeComponent(true, 1.5f, true);
        fc.minAlpha = 0.5f;
        fc.maxAlpha = 1f;

        bag.add(fc);

        bag.add(new CollisionBoundComponent(new Rectangle(x,y, width, height), true));
        bag.add(new TextureRegionComponent(atlas.findRegion(TextureStrings.COWL),
                0, 0, width, height, TextureRegionComponent.FOREGROUND_LAYER_NEAR, new Color(1f,1f,1f,0f)
        ));

        bag.add(new AnimationStateComponent(0));
        IntMap<Animation<TextureRegion>> animMap = new IntMap<Animation<TextureRegion>>();
        animMap.put(0, new Animation<TextureRegion>(0.15f / 1f, atlas.findRegions(TextureStrings.COWL), Animation.PlayMode.LOOP_RANDOM));
        animMap.put(AnimationStateComponent.FIRING, new Animation<TextureRegion>(0.10f / 1f, atlas.findRegions(TextureStrings.COWL_FIRING)));

        bag.add(new AnimationComponent(animMap));



        WeaponComponent wc = new WeaponComponent(
                new MultiPistol.PistolBuilder(assetManager)
                        .color(new Color(ColorResource.GHOST_BULLET_COLOR))
                        .fireRate(0.5f)
                        .shotScale(3)
                        //.bulletOffsets(Measure.units(2.5f), -Measure.units(2.5f))
                        .shotSpeed(Measure.units(60f))
                        .angles(0)
                        .intangible(true)
                        .build());
        bag.add(wc);


        bag.add(new ProximityTriggerAIComponent(new Task() {
            @Override
            public void performAction(World world, Entity e) {
                e.edit().add(new FiringAIComponent(270));
                //e.getComponent(WeaponComponent.class).addChargeTime(1f);
            }

            @Override
            public void cleanUpAction(World world, Entity e) {
                e.edit().remove(FiringAIComponent.class);
            }
        }, true));


        bag.add(new ActionAfterTimeComponent(new Action() {
            @Override
            public void performAction(World world, Entity e) {

                PositionComponent pc = world.getSystem(FindPlayerSystem.class).getPlayerComponent(PositionComponent.class);
                e.edit().add(new OrbitComponent(pc.position, radius, startsLeft ? speedInDegrees : -speedInDegrees, startingAngleInDegrees));

            }
        }, 0f));


        return bag;

    }


}
