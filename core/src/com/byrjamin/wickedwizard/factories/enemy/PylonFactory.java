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
import com.byrjamin.wickedwizard.ecs.components.OnCollisionActionComponent;
import com.byrjamin.wickedwizard.ecs.components.Weapon;
import com.byrjamin.wickedwizard.ecs.components.WeaponComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.Condition;
import com.byrjamin.wickedwizard.ecs.components.ai.ConditionalActionComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.FiringAIComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.ProximityTriggerAIComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.Task;
import com.byrjamin.wickedwizard.ecs.components.identifiers.EnemyComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.AnimationComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.AnimationStateComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.TextureRegionComponent;
import com.byrjamin.wickedwizard.ecs.systems.graphical.CameraSystem;
import com.byrjamin.wickedwizard.factories.weapons.enemy.MultiPistol;
import com.byrjamin.wickedwizard.utils.BagSearch;
import com.byrjamin.wickedwizard.utils.ComponentBag;
import com.byrjamin.wickedwizard.utils.Measure;
import com.byrjamin.wickedwizard.utils.collider.Collider;

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




    public ComponentBag pylonBag(float x, float y, float rotationInDegrees){

        ComponentBag bag = this.defaultEnemyBag(new ComponentBag(), x, y, health);

        BagSearch.removeObjectOfTypeClass(EnemyComponent.class, bag);

        bag.add(new CollisionBoundComponent(new Rectangle(x,y,width,height), true));


        bag.add(new AnimationStateComponent(0));
        IntMap<Animation<TextureRegion>> animMap = new IntMap<Animation<TextureRegion>>();
        animMap.put(0, new Animation<TextureRegion>(0.05f / 1f,
                atlas.findRegions(TextureStrings.PYLON_SPAWNING), Animation.PlayMode.LOOP));
        animMap.put(PYLONCHARGE, new Animation<TextureRegion>(0.15f / 1f,
                atlas.findRegions(TextureStrings.PYLON_CHARGING), Animation.PlayMode.LOOP));

        bag.add(new AnimationComponent(animMap));

        TextureRegionComponent tfc = new TextureRegionComponent(atlas.findRegion(TextureStrings.AMOEBA),
                width, height,
                TextureRegionComponent.FOREGROUND_LAYER_MIDDLE);
        tfc.rotation = rotationInDegrees;

        bag.add(tfc);


        int[] ints = new int[] {0,45,90,135,180};
        for(int i = 0; i < ints.length; i++) ints[i] = ints[i] + (int) rotationInDegrees;

        MultiPistol mp = new MultiPistol.PistolBuilder(assetManager)
                .fireRate(1.5f)
                .angles(ints)
                .shotScale(3)
                .build();

        bag.add(spawningCondition(0, mp));

        return bag;

    }

    public ComponentBag ghostPylonBag(float x, float y, float rotationInDegrees){

        ComponentBag bag = this.defaultEnemyBag(new ComponentBag(), x, y, health);

        BagSearch.removeObjectOfTypeClass(EnemyComponent.class, bag);

        bag.add(new CollisionBoundComponent(new Rectangle(x,y,width,height), true));


        bag.add(new AnimationStateComponent(0));
        IntMap<Animation<TextureRegion>> animMap = new IntMap<Animation<TextureRegion>>();
        animMap.put(0, new Animation<TextureRegion>(0.05f / 1f,
                atlas.findRegions(TextureStrings.PYLON_SPAWNING), Animation.PlayMode.LOOP));
        animMap.put(PYLONCHARGE, new Animation<TextureRegion>(0.15f / 1f,
                atlas.findRegions(TextureStrings.PYLON_CHARGING), Animation.PlayMode.LOOP));

        bag.add(new AnimationComponent(animMap));

        TextureRegionComponent tfc = new TextureRegionComponent(atlas.findRegion(TextureStrings.AMOEBA),
                width, height,
                TextureRegionComponent.FOREGROUND_LAYER_MIDDLE, new Color(ColorResource.GHOST_BULLET_COLOR));
        tfc.rotation = rotationInDegrees;

        bag.add(tfc);

        int[] ints = new int[] {0,45,90,135,180};
        for(int i = 0; i < ints.length; i++) ints[i] = ints[i] + (int) rotationInDegrees;

        MultiPistol mp = new MultiPistol.PistolBuilder(assetManager).fireRate(1.5f)
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

        return new ConditionalActionComponent(new Condition() {
            @Override
            public boolean condition(World world, Entity entity) {

                AnimationComponent ac = entity.getComponent(AnimationComponent.class);
                AnimationStateComponent sc = entity.getComponent(AnimationStateComponent.class);

                if (ac.animations.containsKey(sc.getCurrentState())) {
                    return ac.animations.get(sc.getCurrentState()).isAnimationFinished(sc.stateTime);
                }

                return false;
            }
        }, new Task() {
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
        return new ConditionalActionComponent(new Condition() {
            @Override
            public boolean condition(World world, Entity entity) {

                AnimationComponent ac = entity.getComponent(AnimationComponent.class);
                AnimationStateComponent sc = entity.getComponent(AnimationStateComponent.class);

                if (ac.animations.containsKey(sc.getCurrentState())) {
                    boolean isFinished = ac.animations.get(sc.getCurrentState()).isAnimationFinished(sc.stateTime);

                    if(isFinished) sc.stateTime = 0;

                    boolean isOnCamera = world.getSystem(CameraSystem.class).isOnCamera(entity.getComponent(CollisionBoundComponent.class).bound);

                    return isFinished && isOnCamera;

                }

                return false;
            }
        }, new Task() {
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
