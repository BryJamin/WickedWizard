package com.byrjamin.wickedwizard.factories.enemy;


import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.IntMap;
import com.byrjamin.wickedwizard.assets.TextureStrings;
import com.byrjamin.wickedwizard.ecs.components.CollisionBoundComponent;
import com.byrjamin.wickedwizard.ecs.components.Weapon;
import com.byrjamin.wickedwizard.ecs.components.WeaponComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.FiringAIComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.ProximityTriggerAIComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.Task;
import com.byrjamin.wickedwizard.ecs.components.movement.BounceComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.VelocityComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.AnimationComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.AnimationStateComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.TextureRegionComponent;
import com.byrjamin.wickedwizard.factories.weapons.enemy.LaserOrbitalTask;
import com.byrjamin.wickedwizard.utils.ComponentBag;
import com.byrjamin.wickedwizard.utils.Measure;

/**
 * Created by Home on 25/06/2017.
 */
public class LaserusFactory extends EnemyFactory{

    public float width = Measure.units(10f);
    public float height = Measure.units(10f);

    private final float hSpeed = Measure.units(20f);
    private final float vSpeed = Measure.units(15f);



    public float laserusHealth = 20f;

    public LaserusFactory(AssetManager assetManager) {
        super(assetManager);
    }


    public ComponentBag laserus(float x, float y, boolean startsRight, boolean startsUp){

        x = x - width / 2;
        y = y - height / 2;

        ComponentBag bag = this.defaultEnemyBag(new ComponentBag(), x , y, laserusHealth);

        bag.add(new CollisionBoundComponent(new Rectangle(x,y, width, height), true));
        bag.add(new TextureRegionComponent(atlas.findRegion(TextureStrings.LASERUS),
                0, 0, width, height, TextureRegionComponent.ENEMY_LAYER_MIDDLE
        ));

        bag.add(new AnimationStateComponent(0));
        IntMap<Animation<TextureRegion>> animMap = new IntMap<Animation<TextureRegion>>();
        animMap.put(0, new Animation<TextureRegion>(0.15f / 1f, atlas.findRegions(TextureStrings.LASERUS), Animation.PlayMode.LOOP));
        animMap.put(AnimationStateComponent.FIRING, new Animation<TextureRegion>(0.10f / 1f, atlas.findRegions(TextureStrings.LASERUS_FIRING)));

        bag.add(new AnimationComponent(animMap));

        bag.add(new VelocityComponent(startsRight ? hSpeed : -hSpeed, startsUp ? vSpeed : - vSpeed));
        bag.add(new BounceComponent());


        final LaserOrbitalTask lb = new LaserOrbitalTask.LaserBuilder(assetManager)
                .angles(0,90,180,270)
                .chargeTime(0.2f)
                .numberOfOrbitals(20)
                .orbitalAndIntervalSize(Measure.units(5f))
                .expiryTime(0.25f)
                .disperseTime(0.5f)
                .layer(TextureRegionComponent.FOREGROUND_LAYER_NEAR)
                .build();

        WeaponComponent wc = new WeaponComponent(
                new Weapon() {
                    @Override
                    public void fire(World world, Entity e, float x, float y, double angleInRadians) {
                        lb.performAction(world, e);
                    }

                    @Override
                    public float getBaseFireRate() {
                        return 1.5f;
                    }

                    @Override
                    public float getBaseDamage() {
                        return 0;
                    }
                },
        1f);
        bag.add(wc);


        bag.add(new ProximityTriggerAIComponent(new Task() {
            @Override
            public void performAction(World world, Entity e) {
                e.edit().add(new FiringAIComponent());
            }

            @Override
            public void cleanUpAction(World world, Entity e) {
                e.edit().remove(FiringAIComponent.class);
            }
        }, true));




        return bag;

    }


}
