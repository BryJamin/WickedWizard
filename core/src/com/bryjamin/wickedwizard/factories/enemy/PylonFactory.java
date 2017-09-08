package com.bryjamin.wickedwizard.factories.enemy;

import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.IntMap;
import com.bryjamin.wickedwizard.assets.ColorResource;
import com.bryjamin.wickedwizard.assets.TextureStrings;
import com.bryjamin.wickedwizard.ecs.components.Weapon;
import com.bryjamin.wickedwizard.ecs.components.ai.ConditionalActionComponent;
import com.bryjamin.wickedwizard.ecs.components.identifiers.EnemyComponent;
import com.bryjamin.wickedwizard.ecs.components.movement.CollisionBoundComponent;
import com.bryjamin.wickedwizard.ecs.components.texture.AnimationComponent;
import com.bryjamin.wickedwizard.ecs.components.texture.AnimationStateComponent;
import com.bryjamin.wickedwizard.ecs.components.texture.TextureRegionComponent;
import com.bryjamin.wickedwizard.ecs.systems.graphical.CameraSystem;
import com.bryjamin.wickedwizard.utils.ComponentBag;
import com.bryjamin.wickedwizard.utils.Measure;

/**
 * Created by Home on 21/06/2017.
 */

public class PylonFactory extends EnemyFactory {

    public float width = Measure.units(10f);
    public float height = Measure.units(10f);

    private final float health = 9f;

    private final static int PYLONCHARGE = 2;


    public PylonFactory(AssetManager assetManager) {
        super(assetManager);
    }


    private ComponentBag basePylonBag(float x, float y, float rotationInDegrees, Color color){

        ComponentBag bag = this.defaultEnemyBag(new ComponentBag(), x, y, health);

        com.bryjamin.wickedwizard.utils.BagSearch.removeObjectOfTypeClass(EnemyComponent.class, bag);

        bag.add(new CollisionBoundComponent(new Rectangle(x,y,width,height), true));


        bag.add(new AnimationStateComponent(0));
        IntMap<Animation<TextureRegion>> animMap = new IntMap<Animation<TextureRegion>>();
        animMap.put(0, new Animation<TextureRegion>( 1f / 25f,
                atlas.findRegions(TextureStrings.PYLON_SPAWNING)));
        animMap.put(PYLONCHARGE, new Animation<TextureRegion>(0.15f / 1f,
                atlas.findRegions(TextureStrings.PYLON_CHARGING), Animation.PlayMode.LOOP));

        bag.add(new AnimationComponent(animMap));

        TextureRegionComponent tfc = new TextureRegionComponent(atlas.findRegion(TextureStrings.PYLON_SPAWNING),
                width, height,
                TextureRegionComponent.FOREGROUND_LAYER_MIDDLE, new Color(color));
        tfc.rotation = rotationInDegrees;

        bag.add(tfc);

        return bag;

    }


    public ComponentBag pylonBag(float x, float y, float rotationInDegrees){

        ComponentBag bag = basePylonBag(x, y, rotationInDegrees, ColorResource.ENEMY_BULLET_COLOR);

        int[] ints = new int[] {0,45,90,135,180};
        for(int i = 0; i < ints.length; i++) ints[i] = ints[i] + (int) rotationInDegrees;

        com.bryjamin.wickedwizard.factories.weapons.enemy.MultiPistol mp = new com.bryjamin.wickedwizard.factories.weapons.enemy.MultiPistol.PistolBuilder(assetManager)
                .fireRate(1.5f)
                .angles(ints)
                .shotScale(3)
                .build();

        bag.add(spawningCondition(0, mp));

        return bag;

    }

    public ComponentBag ghostPylonBag(float x, float y, float rotationInDegrees){

        ComponentBag bag = basePylonBag(x, y, rotationInDegrees, ColorResource.GHOST_BULLET_COLOR);

        int[] ints = new int[] {0,45,90,135,180};
        for(int i = 0; i < ints.length; i++) ints[i] = ints[i] + (int) rotationInDegrees;

        com.bryjamin.wickedwizard.factories.weapons.enemy.MultiPistol mp = new com.bryjamin.wickedwizard.factories.weapons.enemy.MultiPistol.PistolBuilder(assetManager).fireRate(1.5f)
                .angles(ints)
                .shotScale(3)
                .color(new Color(ColorResource.GHOST_BULLET_COLOR))
                .intangible(true)
                .expire(true)
                .expireRange(Measure.units(100f))
                .build();

        bag.add(spawningCondition(0, mp));

        return bag;

    }


    private ConditionalActionComponent spawningCondition(final float rotationInRadians, final Weapon weapon){

        return new ConditionalActionComponent(new com.bryjamin.wickedwizard.ecs.components.ai.Condition() {
            @Override
            public boolean condition(World world, Entity entity) {

                AnimationComponent ac = entity.getComponent(AnimationComponent.class);
                AnimationStateComponent sc = entity.getComponent(AnimationStateComponent.class);

                if (ac.animations.containsKey(sc.getCurrentState())) {
                    return ac.animations.get(sc.getCurrentState()).isAnimationFinished(sc.stateTime);
                }

                return false;
            }
        }, new com.bryjamin.wickedwizard.ecs.components.ai.Task() {
            @Override
            public void performAction(World world, Entity e) {
                e.edit().add(new EnemyComponent());
                e.getComponent(AnimationStateComponent.class).setDefaultState(PYLONCHARGE);
                e.edit().remove(ConditionalActionComponent.class);
                e.edit().add(firingCondition(rotationInRadians, weapon));
            }

            @Override
            public void cleanUpAction(World world, Entity e) {

            }
        });
    }



    private ConditionalActionComponent firingCondition(final float rotationInRadians, final Weapon weapon){
        return new ConditionalActionComponent(new com.bryjamin.wickedwizard.ecs.components.ai.Condition() {
            @Override
            public boolean condition(World world, Entity entity) {

                AnimationComponent ac = entity.getComponent(AnimationComponent.class);
                AnimationStateComponent sc = entity.getComponent(AnimationStateComponent.class);

                if (ac.animations.containsKey(sc.getCurrentState())) {
                    boolean isFinished = ac.animations.get(sc.getCurrentState()).isAnimationFinished(sc.stateTime);

                    if(isFinished) sc.stateTime = 0;

                    boolean isOnCamera = CameraSystem.isOnCamera(entity.getComponent(CollisionBoundComponent.class).bound,
                            world.getSystem(CameraSystem.class).getGamecam());

                    return isFinished && isOnCamera;

                }

                return false;
            }
        }, new com.bryjamin.wickedwizard.ecs.components.ai.Task() {
            @Override
            public void performAction(World world, Entity e) {
                CollisionBoundComponent cbc = e.getComponent(CollisionBoundComponent.class);
                weapon.fire(world, e, cbc.getCenterX(), cbc.getCenterY(), rotationInRadians);
            }

            @Override
            public void cleanUpAction(World world, Entity e) {

            }
        });
    }


}
