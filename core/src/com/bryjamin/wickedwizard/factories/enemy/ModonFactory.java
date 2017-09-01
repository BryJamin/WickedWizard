package com.bryjamin.wickedwizard.factories.enemy;

import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.IntMap;
import com.bryjamin.wickedwizard.ecs.components.ai.ExpireComponent;
import com.bryjamin.wickedwizard.ecs.components.graphics.CameraShakeComponent;
import com.bryjamin.wickedwizard.ecs.components.movement.CollisionBoundComponent;
import com.bryjamin.wickedwizard.ecs.components.ai.Action;
import com.bryjamin.wickedwizard.ecs.components.movement.FrictionComponent;
import com.bryjamin.wickedwizard.ecs.components.texture.AnimationComponent;
import com.bryjamin.wickedwizard.ecs.components.texture.AnimationStateComponent;
import com.bryjamin.wickedwizard.ecs.components.texture.TextureRegionComponent;

/**
 * Created by Home on 17/06/2017.
 */

public class ModonFactory extends EnemyFactory{


    private final float width = com.bryjamin.wickedwizard.utils.Measure.units(17.5f);
    private final float height = com.bryjamin.wickedwizard.utils.Measure.units(17.5f);

    private final float textureWidth = com.bryjamin.wickedwizard.utils.Measure.units(22.5f);
    private final float textureHeight = com.bryjamin.wickedwizard.utils.Measure.units(22.5f);

    private static final float health = 20;
    private static final float heavyHealth = 30;

    private static final float stompShakeTime = 0.25f;
    private static final float stompIntensity = 0.5f;


    private static final float jumpTimer = 1.5f;

    private final int HEAVY_MODON_IN_AIR_STATE = 100;

    public ModonFactory(AssetManager assetManager) {
        super(assetManager);
        this.loudDeathSound = true;
    }


    public com.bryjamin.wickedwizard.utils.ComponentBag modon(float x, float y){

        x = x - width / 2;
        y = y - height / 2;

        com.bryjamin.wickedwizard.utils.ComponentBag bag = this.defaultEnemyBag(new com.bryjamin.wickedwizard.utils.ComponentBag(), x, y, health);

        bag.add(new com.bryjamin.wickedwizard.ecs.components.movement.GravityComponent());
        bag.add(new com.bryjamin.wickedwizard.ecs.components.movement.VelocityComponent());

        bag.add(new CollisionBoundComponent(new Rectangle(x,y,width,height), true));

        FrictionComponent fc = new FrictionComponent(true, false);
        fc.airFriction = false;

        bag.add(fc);

        bag.add(new AnimationStateComponent(AnimationStateComponent.DEFAULT));
        IntMap<Animation<TextureRegion>> animMap = new IntMap<Animation<TextureRegion>>();
        animMap.put(AnimationStateComponent.DEFAULT, new Animation<TextureRegion>(0.1f / 1f,
                atlas.findRegions(com.bryjamin.wickedwizard.assets.TextureStrings.MODON), Animation.PlayMode.LOOP));
        animMap.put(AnimationStateComponent.FIRING, new Animation<TextureRegion>(0.2f / 1f,
                atlas.findRegions(com.bryjamin.wickedwizard.assets.TextureStrings.MODON_FIRING)));
        bag.add(new AnimationComponent(animMap));

        TextureRegionComponent trc = new TextureRegionComponent(atlas.findRegion(com.bryjamin.wickedwizard.assets.TextureStrings.MODON),
                com.bryjamin.wickedwizard.utils.CenterMath.offsetX(width, textureWidth), 0, textureWidth, textureHeight, TextureRegionComponent.ENEMY_LAYER_MIDDLE);

        bag.add(trc);

        bag.add(modonOnCollisionAction());



        bag.add(new com.bryjamin.wickedwizard.ecs.components.ai.ActionAfterTimeComponent(new Action() {
            @Override
            public void performAction(World world, Entity e) {

                jump(world, e);

                e.getComponent(AnimationStateComponent.class).setDefaultState(AnimationStateComponent.FIRING);
                e.getComponent(AnimationStateComponent.class).stateTime = 0;

                e.edit().add(modonOnCollisionAction());


            }

        }, jumpTimer, true));


        return bag;


    }


    private void jump(World world, Entity e){

        CollisionBoundComponent pcbc = world.getSystem(com.bryjamin.wickedwizard.ecs.systems.FindPlayerSystem.class).getPlayerComponent(CollisionBoundComponent.class);
        CollisionBoundComponent cbc = e.getComponent(CollisionBoundComponent.class);

        boolean isLeftOfPlayer = (cbc.getCenterX() < pcbc.getCenterX());

        e.getComponent(com.bryjamin.wickedwizard.ecs.components.movement.VelocityComponent.class).velocity.x = isLeftOfPlayer ? com.bryjamin.wickedwizard.utils.Measure.units(25f) : -com.bryjamin.wickedwizard.utils.Measure.units(25f);
        e.getComponent(com.bryjamin.wickedwizard.ecs.components.movement.VelocityComponent.class).velocity.y = com.bryjamin.wickedwizard.utils.Measure.units(85);

    }


    public com.bryjamin.wickedwizard.utils.ComponentBag heavyModon(float x, float y){

        x = x - width / 2;
        y = y - height / 2;

        com.bryjamin.wickedwizard.utils.ComponentBag bag = this.defaultEnemyBag(new com.bryjamin.wickedwizard.utils.ComponentBag(), x, y, heavyHealth);

        bag.add(new com.bryjamin.wickedwizard.ecs.components.movement.GravityComponent());
        bag.add(new com.bryjamin.wickedwizard.ecs.components.movement.VelocityComponent());

        bag.add(new CollisionBoundComponent(new Rectangle(x,y,width,height), true));

        FrictionComponent fc = new FrictionComponent(true, false);
        fc.airFriction = false;

        bag.add(fc);

        bag.add(new AnimationStateComponent(HEAVY_MODON_IN_AIR_STATE));
        IntMap<Animation<TextureRegion>> animMap = new IntMap<Animation<TextureRegion>>();
        animMap.put(AnimationStateComponent.DEFAULT, new Animation<TextureRegion>(0.05f / 1f,
                atlas.findRegions(com.bryjamin.wickedwizard.assets.TextureStrings.MODON_HEAVY), Animation.PlayMode.LOOP));
        animMap.put(AnimationStateComponent.FIRING, new Animation<TextureRegion>(0.1f / 1f,
                atlas.findRegions(com.bryjamin.wickedwizard.assets.TextureStrings.MODON_HEAVY_FIRING)));
        animMap.put(HEAVY_MODON_IN_AIR_STATE, new Animation<TextureRegion>(0.05f / 1f,
                atlas.findRegions(com.bryjamin.wickedwizard.assets.TextureStrings.MODON_HEAVY_IN_AIR), Animation.PlayMode.LOOP));
        bag.add(new AnimationComponent(animMap));

        TextureRegionComponent trc = new TextureRegionComponent(atlas.findRegion(com.bryjamin.wickedwizard.assets.TextureStrings.MODON_HEAVY),
                com.bryjamin.wickedwizard.utils.CenterMath.offsetX(width, textureWidth), 0, textureWidth, textureHeight, TextureRegionComponent.ENEMY_LAYER_MIDDLE);

        bag.add(trc);

        bag.add(heavyModonOnCollisionAction());


        bag.add(new com.bryjamin.wickedwizard.ecs.components.ai.ActionAfterTimeComponent(new Action() {
            @Override
            public void performAction(World world, Entity e) {

                jump(world, e);

                e.getComponent(AnimationStateComponent.class).setDefaultState(HEAVY_MODON_IN_AIR_STATE);
                e.getComponent(AnimationStateComponent.class).queueAnimationState(AnimationStateComponent.FIRING);

                e.edit().add(heavyModonOnCollisionAction());


            }

        }, jumpTimer, true));


        return bag;


    }




    public com.bryjamin.wickedwizard.ecs.components.ai.OnCollisionActionComponent heavyModonOnCollisionAction(){

        com.bryjamin.wickedwizard.ecs.components.ai.OnCollisionActionComponent ocac = new com.bryjamin.wickedwizard.ecs.components.ai.OnCollisionActionComponent();
        ocac.bottom = new Action() {
            @Override
            public void performAction(World world, Entity e) {
                e.getComponent(AnimationStateComponent.class).setDefaultState(AnimationStateComponent.DEFAULT);
                com.bryjamin.wickedwizard.factories.weapons.enemy.MultiPistol mp = new com.bryjamin.wickedwizard.factories.weapons.enemy.MultiPistol.PistolBuilder(assetManager)
                        .angles(70,80,90,100,110)
                        .shotScale(3)
                        .shotSpeed(com.bryjamin.wickedwizard.utils.Measure.units(110f))
                        .gravity(true)
                        .build();
                CollisionBoundComponent cbc = e.getComponent(CollisionBoundComponent.class);
                mp.fire(world, e, cbc.getCenterX(), cbc.getCenterY() - com.bryjamin.wickedwizard.utils.Measure.units(5f), 0);
                e.edit().remove(com.bryjamin.wickedwizard.ecs.components.ai.OnCollisionActionComponent.class);

                screenShakeLanding(world, e);

            }
        };


        return ocac;

    }


    public com.bryjamin.wickedwizard.ecs.components.ai.OnCollisionActionComponent modonOnCollisionAction(){

        com.bryjamin.wickedwizard.ecs.components.ai.OnCollisionActionComponent ocac = new com.bryjamin.wickedwizard.ecs.components.ai.OnCollisionActionComponent();
        ocac.bottom = new Action() {
            @Override
            public void performAction(World world, Entity e) {
                e.edit().remove(com.bryjamin.wickedwizard.ecs.components.ai.OnCollisionActionComponent.class);

                screenShakeLanding(world, e);

            }
        };


        return ocac;

    }


    private void screenShakeLanding(World world, Entity e){
        Entity shaker = world.createEntity();
        shaker.edit().add(new ExpireComponent(stompShakeTime));
        shaker.edit().add(new CameraShakeComponent(stompIntensity));
        world.getSystem(com.bryjamin.wickedwizard.ecs.systems.audio.SoundSystem.class).playRandomSound(com.bryjamin.wickedwizard.assets.SoundFileStrings.enemyJumpLandingMegaMix);
    }









}

