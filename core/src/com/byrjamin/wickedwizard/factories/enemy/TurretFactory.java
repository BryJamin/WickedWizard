package com.byrjamin.wickedwizard.factories.enemy;

import com.artemis.Component;
import com.artemis.Entity;
import com.artemis.World;
import com.artemis.utils.Bag;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.IntMap;
import com.byrjamin.wickedwizard.assets.TextureStrings;
import com.byrjamin.wickedwizard.ecs.components.Weapon;
import com.byrjamin.wickedwizard.ecs.components.ai.ProximityTriggerAIComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.Task;
import com.byrjamin.wickedwizard.ecs.components.movement.BounceComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.CollisionBoundComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.FiringAIComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.FrictionComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.VelocityComponent;
import com.byrjamin.wickedwizard.ecs.components.WeaponComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.AnimationComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.AnimationStateComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.TextureRegionComponent;
import com.byrjamin.wickedwizard.factories.BombFactory;
import com.byrjamin.wickedwizard.factories.weapons.enemy.MultiPistol;
import com.byrjamin.wickedwizard.utils.BagToEntity;
import com.byrjamin.wickedwizard.utils.BulletMath;
import com.byrjamin.wickedwizard.utils.ComponentBag;
import com.byrjamin.wickedwizard.utils.Measure;

import java.util.Random;

/**
 * Created by Home on 26/03/2017.
 */

public class TurretFactory extends EnemyFactory {

    private BombFactory bombFactory;

    private final static float sentryHealth = 10;
    private final static float triSentryHealth = 15;
    private final static float flyByHealth = 15;
    private final static float pentaHealth = 30;
    private final static float doubleFlyByHealth = 30;

    private final static float turretSpeed = Measure.units(15f);

    private final static float turretWeaponFireRate = 2.0f;

    public TurretFactory(AssetManager assetManager) {
        super(assetManager);
        bombFactory = new BombFactory(assetManager);
    }

    final float width = Measure.units(10f);
    final float height = Measure.units(10f);

    final float upgradeWidth = Measure.units(15f);
    final float upgradeHeight = Measure.units(15f);

    private float upgradeSpeed = Measure.units(10f);

    public Bag<Component> fixedLockOnTurret(float x, float y){
        x = x - width / 2;
        y = y - width / 2;

        ComponentBag bag = this.defaultEnemyBag(new ComponentBag(), x , y, sentryHealth);

        bag.add(new CollisionBoundComponent(new Rectangle(x,y, width, height), true));
        bag.add(new TextureRegionComponent(atlas.findRegion(TextureStrings.SENTRY),
                0, 0, width, height, TextureRegionComponent.ENEMY_LAYER_MIDDLE
        ));

        bag.add(new AnimationStateComponent(0));
        IntMap<Animation<TextureRegion>> animMap = new IntMap<Animation<TextureRegion>>();
        animMap.put(0, new Animation<TextureRegion>(0.15f / 1f, atlas.findRegions(TextureStrings.SENTRY), Animation.PlayMode.LOOP_RANDOM));
        animMap.put(AnimationStateComponent.FIRING, new Animation<TextureRegion>(0.10f / 1f, atlas.findRegions(TextureStrings.SENTRY_FIRING)));

        bag.add(new AnimationComponent(animMap));

        WeaponComponent wc = new WeaponComponent(new MultiPistol.PistolBuilder(assetManager)
                .fireRate(turretWeaponFireRate)
                .angles(0).build(), turretWeaponFireRate);
        bag.add(wc);

        bag.add(defaultTurretTrigger());

        return bag;
    }

    public Bag<Component> movingSentry(float x, float y, boolean startsRight){

        Bag<Component> bag = fixedLockOnTurret(x,y);
        bag.add(new VelocityComponent(startsRight ? turretSpeed : -turretSpeed, 0));
        bag.add(new BounceComponent());

        return bag;
    }



    public ProximityTriggerAIComponent defaultTurretTrigger(){
        return new ProximityTriggerAIComponent(new Task() {
            @Override
            public void performAction(World world, Entity e) {
                e.edit().add(new FiringAIComponent());
            }

            @Override
            public void cleanUpAction(World world, Entity e) {
                e.edit().remove(FiringAIComponent.class);
            }
        },
                true);
    }




    public ComponentBag fixedMultiSentry(float x, float y){
        x = x - width / 2;
        y = y - width / 2;

        ComponentBag bag = this.defaultEnemyBag(new ComponentBag(), x , y, triSentryHealth);
        bag.add(new CollisionBoundComponent(new Rectangle(x,y, width,height), true));
        bag.add(new TextureRegionComponent(atlas.findRegion(TextureStrings.SENTRY_TRI),
                0, 0, width, height, TextureRegionComponent.ENEMY_LAYER_MIDDLE
        ));

        bag.add(new AnimationStateComponent(0));
        IntMap<Animation<TextureRegion>> animMap = new IntMap<Animation<TextureRegion>>();
        animMap.put(0, new Animation<TextureRegion>(0.15f / 1f, atlas.findRegions(TextureStrings.SENTRY_TRI), Animation.PlayMode.LOOP_RANDOM));
        animMap.put(AnimationStateComponent.FIRING, new Animation<TextureRegion>(0.10f / 1f, atlas.findRegions(TextureStrings.SENTRY_FIRING_TRI)));

        bag.add(new AnimationComponent(animMap));

        WeaponComponent wc = new WeaponComponent(new MultiPistol.PistolBuilder(assetManager)
                .fireRate(turretWeaponFireRate)
                .angles(0,25,-25)
                .build(), turretWeaponFireRate);
        bag.add(wc);

        bag.add(defaultTurretTrigger());

        return bag;
    }


    public Bag<Component> movingHorizontalMultiSentry(float x, float y, boolean startsRight){
        ComponentBag bag = fixedMultiSentry(x,y);
        bag.add(new VelocityComponent(startsRight ? turretSpeed : -turretSpeed, 0));
        bag.add(new BounceComponent());

        return bag;
    }

    public Bag<Component> movingVerticalMultiSentry(float x, float y, boolean startsUp){

        ComponentBag bag = fixedMultiSentry(x,y);
        bag.add(new VelocityComponent(0, startsUp ? turretSpeed : -turretSpeed));
        bag.add(new BounceComponent());

        return bag;
    }


    public ComponentBag fixedFlyByBombSentry(float x, float y){
        x = x - width / 2;
        y = y - width / 2;

        ComponentBag bag = this.defaultEnemyBag(new ComponentBag(), x , y, flyByHealth);
        bag.add(new CollisionBoundComponent(new Rectangle(x,y, width, height), true));
        bag.add(new TextureRegionComponent(atlas.findRegion(TextureStrings.FLYBY),
                0, 0, width, height, TextureRegionComponent.ENEMY_LAYER_MIDDLE
        ));

        bag.add(new AnimationStateComponent(0));
        IntMap<Animation<TextureRegion>> animMap = new IntMap<Animation<TextureRegion>>();
        animMap.put(0, new Animation<TextureRegion>(0.15f / 1f, atlas.findRegions(TextureStrings.FLYBY), Animation.PlayMode.LOOP_RANDOM));
        animMap.put(AnimationStateComponent.FIRING, new Animation<TextureRegion>(0.10f / 1f, atlas.findRegions(TextureStrings.FLYBY_FIRING)));

        bag.add(new AnimationComponent(animMap));

        WeaponComponent wc = new WeaponComponent(new Weapon() {

            @Override
            public void fire(World world, Entity e, float x, float y, double angleInRadians) {
                Entity newEntity = BagToEntity.bagToEntity(world.createEntity(), bombFactory.bomb(x,y,1f));

                FrictionComponent fc = new FrictionComponent();
                fc.airFriction = false;

                newEntity.edit().add(fc);

                //TODO maybe improve this? should bp,bs just drop vertically and not be aimed?

                newEntity.getComponent(VelocityComponent.class).velocity.y = BulletMath.velocityY(Measure.units(75f), angleInRadians);
                newEntity.getComponent(VelocityComponent.class).velocity.x = BulletMath.velocityX(Measure.units(75f), angleInRadians);
            }

            @Override
            public float getBaseFireRate() {
                return turretWeaponFireRate;
            }

            @Override
            public float getBaseDamage() {
                return 0;
            }
        }, turretWeaponFireRate);
        bag.add(wc);

        bag.add(defaultTurretTrigger());

        return bag;
    }


    public Bag<Component> movingFlyByBombSentry(float x, float y, boolean startsRight){

        ComponentBag bag = fixedFlyByBombSentry(x,y);
        bag.add(new VelocityComponent(startsRight ? turretSpeed : -turretSpeed, 0));
        bag.add(new BounceComponent());

        return bag;
    }

    public Bag<Component> movingVerticalFlyByBombSentry(float x, float y, boolean startsUp){
        ComponentBag bag = fixedFlyByBombSentry(x,y);
        bag.add(new VelocityComponent(0, startsUp ? turretSpeed : -turretSpeed));
        bag.add(new BounceComponent());

        return bag;
    }





    public ComponentBag fixedPentaSentry(float x, float y){
        x = x - upgradeWidth / 2;
        y = y - upgradeHeight / 2;

        ComponentBag bag = this.defaultEnemyBag(new ComponentBag(), x , y, pentaHealth);
        bag.add(new CollisionBoundComponent(new Rectangle(x,y, upgradeWidth,upgradeHeight), true));
        bag.add(new TextureRegionComponent(atlas.findRegion(TextureStrings.SENTRY_PENTA),
                upgradeWidth, upgradeHeight, TextureRegionComponent.ENEMY_LAYER_MIDDLE
        ));

        bag.add(new AnimationStateComponent(0));
        IntMap<Animation<TextureRegion>> animMap = new IntMap<Animation<TextureRegion>>();
        animMap.put(0, new Animation<TextureRegion>(0.15f / 1f, atlas.findRegions(TextureStrings.SENTRY_PENTA), Animation.PlayMode.LOOP));
        animMap.put(AnimationStateComponent.FIRING, new Animation<TextureRegion>(0.15f / 1f, atlas.findRegions(TextureStrings.SENTRY_FIRING_PENTA)));

        bag.add(new AnimationComponent(animMap));

        WeaponComponent wc = new WeaponComponent(
                new MultiPistol.PistolBuilder(assetManager)
                        .angles(0,25,50,-25,-50)
                        .fireRate(turretWeaponFireRate)
                        .build(), turretWeaponFireRate);
        bag.add(wc);

        bag.add(defaultTurretTrigger());

        return bag;
    }

    public ComponentBag movingPentaSentry(float x, float y, boolean startsRight, boolean startsUp){
        ComponentBag bag = fixedPentaSentry(x,y);
        bag.add(new VelocityComponent(startsRight ? upgradeSpeed : -upgradeSpeed, startsUp ? upgradeSpeed : -upgradeSpeed));
        bag.add(new BounceComponent());
        return bag;
    }




    public ComponentBag fixedFlyByDoubleBombSentry(float x, float y){
        x = x - upgradeWidth / 2;
        y = y - upgradeHeight / 2;

        ComponentBag bag = this.defaultEnemyBag(new ComponentBag(), x , y, doubleFlyByHealth);
        bag.add(new CollisionBoundComponent(new Rectangle(x,y, upgradeWidth, upgradeHeight), true));
        bag.add(new TextureRegionComponent(atlas.findRegion(TextureStrings.FLYBY_DOUBLE),
                0, 0, upgradeWidth, upgradeHeight, TextureRegionComponent.ENEMY_LAYER_MIDDLE
        ));

        bag.add(new AnimationStateComponent(0));
        IntMap<Animation<TextureRegion>> animMap = new IntMap<Animation<TextureRegion>>();
        animMap.put(0, new Animation<TextureRegion>(0.15f / 1f, atlas.findRegions(TextureStrings.FLYBY_DOUBLE), Animation.PlayMode.LOOP));
        animMap.put(AnimationStateComponent.FIRING, new Animation<TextureRegion>(0.10f / 1f, atlas.findRegions(TextureStrings.FLYBY_DOUBLE_FIRING)));

        bag.add(new AnimationComponent(animMap));

        WeaponComponent wc = new WeaponComponent(new Weapon() {


            int[] angles = new int[]{0,30, -35};

            @Override
            public void fire(World world, Entity e, float x, float y, double angleInRadians) {

                for(int i : angles) {
                    Entity newEntity = BagToEntity.bagToEntity(world.createEntity(), bombFactory.bomb(x, y, 1f));
                    FrictionComponent fc = new FrictionComponent();
                    fc.airFriction = false;
                    newEntity.edit().add(fc);
                    //TODO maybe improve this? should bp,bs just drop vertically and not be aimed?
                    newEntity.getComponent(VelocityComponent.class).velocity.y = BulletMath.velocityY(Measure.units(75f), angleInRadians + Math.toRadians(i));
                    newEntity.getComponent(VelocityComponent.class).velocity.x = BulletMath.velocityX(Measure.units(75f), angleInRadians + Math.toRadians(i));

                }
            }

            @Override
            public float getBaseFireRate() {
                return turretWeaponFireRate;
            }

            @Override
            public float getBaseDamage() {
                return 0;
            }
        }, turretWeaponFireRate);
        bag.add(wc);

        bag.add(defaultTurretTrigger());

        return bag;
    }


    public ComponentBag movingFlyByDoubleBombSentry(float x, float y, boolean startsRight, boolean startsUp){
        ComponentBag bag = fixedFlyByDoubleBombSentry(x,y);
        bag.add(new VelocityComponent(startsRight ? upgradeSpeed : -upgradeSpeed, startsUp ? upgradeSpeed : -upgradeSpeed));
        bag.add(new BounceComponent());
        return bag;
    }










}
