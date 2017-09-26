package com.bryjamin.wickedwizard.factories.enemy;

import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.IntMap;
import com.bryjamin.wickedwizard.assets.ColorResource;
import com.bryjamin.wickedwizard.assets.TextureStrings;
import com.bryjamin.wickedwizard.ecs.components.WeaponComponent;
import com.bryjamin.wickedwizard.ecs.components.ai.Action;
import com.bryjamin.wickedwizard.ecs.components.ai.ActionAfterTimeComponent;
import com.bryjamin.wickedwizard.ecs.components.ai.FiringAIComponent;
import com.bryjamin.wickedwizard.ecs.components.ai.ProximityTriggerAIComponent;
import com.bryjamin.wickedwizard.ecs.components.ai.Task;
import com.bryjamin.wickedwizard.ecs.components.identifiers.ArenaLockComponent;
import com.bryjamin.wickedwizard.ecs.components.identifiers.EnemyComponent;
import com.bryjamin.wickedwizard.ecs.components.movement.CollisionBoundComponent;
import com.bryjamin.wickedwizard.ecs.components.movement.OrbitComponent;
import com.bryjamin.wickedwizard.ecs.components.texture.AnimationComponent;
import com.bryjamin.wickedwizard.ecs.components.texture.AnimationStateComponent;
import com.bryjamin.wickedwizard.ecs.components.texture.FadeComponent;
import com.bryjamin.wickedwizard.ecs.components.texture.TextureRegionComponent;
import com.bryjamin.wickedwizard.factories.weapons.enemy.MultiPistol;
import com.bryjamin.wickedwizard.utils.BagSearch;
import com.bryjamin.wickedwizard.utils.ComponentBag;
import com.bryjamin.wickedwizard.utils.Measure;


/**
 * Created by Home on 25/06/2017.
 */

public class CowlFactory extends EnemyFactory {

    public float width = Measure.units(7.5f);
    public float height = Measure.units(7.5f);

    private final static float radius = Measure.units(30f);
    private final static float speedInDegrees = 2f;

    private final static float fireRate = 1.25f;
    private final static float weaponChargeTime = 0.75f;

    private final static float shotSpeed = Measure.units(40f);

    public float cowlHealth = 10f;

    public CowlFactory(AssetManager assetManager) {
        super(assetManager);
    }


    public ComponentBag cowl(float x, float y, final Vector3 orbitCenter, final float radius, final float startingAngleInDegrees, final boolean startsLeft){

        x = x - width / 2;
        y = y - height / 2;

        ComponentBag bag = this.defaultEnemyBag(new ComponentBag(), x , y, cowlHealth);
        BagSearch.removeObjectOfTypeClass(EnemyComponent.class, bag);

/*        FadeComponent fc = new FadeComponent(false, 0.5f, true);
        bag.add(fc);*/

        bag.add(new CollisionBoundComponent(new Rectangle(x,y, width, height), true));
        bag.add(new TextureRegionComponent(atlas.findRegion(TextureStrings.COWL),
                0, 0, width, height, TextureRegionComponent.FOREGROUND_LAYER_NEAR, new Color(1f,1f,1f,0f)
        ));
        bag.add(new ArenaLockComponent());

        bag.add(new AnimationStateComponent(0));
        IntMap<Animation<TextureRegion>> animMap = new IntMap<Animation<TextureRegion>>();
        animMap.put(0, new Animation<TextureRegion>(0.15f / 1f, atlas.findRegions(TextureStrings.COWL), Animation.PlayMode.LOOP_RANDOM));
        animMap.put(AnimationStateComponent.FIRING, new Animation<TextureRegion>(0.10f / 1f, atlas.findRegions(TextureStrings.COWL_FIRING)));

        bag.add(new AnimationComponent(animMap));

        bag.add(new ProximityTriggerAIComponent(new Task() {
            @Override
            public void performAction(World world, Entity e) {
                e.edit().add(new FiringAIComponent());
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

                e.edit().add(new EnemyComponent());

                e.edit().remove(FadeComponent.class);

                e.edit().add(new OrbitComponent(orbitCenter, radius, startsLeft ? speedInDegrees : -speedInDegrees, startingAngleInDegrees));

                FadeComponent fc = new FadeComponent(true, 1.5f, true);
                fc.minAlpha = 0f;
                fc.maxAlpha = 1f;
                e.edit().add(fc);


                WeaponComponent wc = new WeaponComponent(
                        new MultiPistol.PistolBuilder(assetManager)
                                .angles(0, 30, 60, 90, 120,150,180,210,240,270,300,330)
                                //.angles(0, 30, -30, 60, -60)
                                .color(new Color(ColorResource.GHOST_BULLET_COLOR))
                                .fireRate(fireRate)
                                .expireRange(Measure.units(60f))
                                .shotScale(3)
                                //.gravity(true)
                                //.bulletOffsets(Measure.units(2.5f), -Measure.units(2.5f))
                                .shotSpeed(shotSpeed)
                                .intangible(true)
                                .build(), weaponChargeTime);
                e.edit().add(wc);



            }
        }, 0.25f));


        return bag;

    }


}
