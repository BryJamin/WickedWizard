package com.byrjamin.wickedwizard.factories.enemy;

import com.artemis.Aspect;
import com.artemis.Entity;
import com.artemis.World;
import com.artemis.utils.IntBag;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.IntMap;
import com.byrjamin.wickedwizard.assets.SoundFileStrings;
import com.byrjamin.wickedwizard.assets.TextureStrings;
import com.byrjamin.wickedwizard.ecs.components.BlinkOnHitComponent;
import com.byrjamin.wickedwizard.ecs.components.CollisionBoundComponent;
import com.byrjamin.wickedwizard.ecs.components.HealthComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.Action;
import com.byrjamin.wickedwizard.ecs.components.ai.ActionAfterTimeComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.Condition;
import com.byrjamin.wickedwizard.ecs.components.ai.ConditionalActionComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.FollowPositionComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.OnDeathActionComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.OnHitActionComponent;
import com.byrjamin.wickedwizard.ecs.components.audio.SoundEmitterComponent;
import com.byrjamin.wickedwizard.ecs.components.identifiers.ChestComponent;
import com.byrjamin.wickedwizard.ecs.components.identifiers.ChildComponent;
import com.byrjamin.wickedwizard.ecs.components.identifiers.ParentComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.BounceComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.PositionComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.VelocityComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.AnimationComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.AnimationStateComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.FadeComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.TextureRegionComponent;
import com.byrjamin.wickedwizard.ecs.systems.ai.OnDeathSystem;
import com.byrjamin.wickedwizard.utils.CenterMath;
import com.byrjamin.wickedwizard.utils.ComponentBag;
import com.byrjamin.wickedwizard.utils.Measure;
import com.byrjamin.wickedwizard.utils.collider.Collider;

/**
 * Created by Home on 01/08/2017.
 */

public class HoarderFactory extends EnemyFactory {


    private final float width = Measure.units(5);
    private final float height = Measure.units(5);


    private final float portalWidth = Measure.units(12.5f);
    private final float portalHeight = Measure.units(12.5f);

    private final float hitboxWidth = Measure.units(40);
    private final float hitboxHeight = Measure.units(40);

    private static final float health = 5;

    private final float Vspeed = Measure.units(15);
    private final float speed = Measure.units(30f);

    private final float textureWidth = Measure.units(20);
    private final float textureHeight = Measure.units(20);

    private final float textureOffsetX = -Measure.units(1f);
    private final float textureOffsetY = 0;

    private static final float chestFadeOutTime = 6f;
    private static final float chestFadeInTime = 0.25f;

    private static final float chestReappearanceSpeed = Measure.units(30f);

    public HoarderFactory(AssetManager assetManager) {
        super(assetManager);
    }



    public ComponentBag hoarder(float x, float y){

        x = x - width / 2;
        y = y - height / 2;

        ComponentBag bag = new ComponentBag();

        bag.add(new PositionComponent(x, y));
        bag.add(new HealthComponent(health));
        bag.add(new BlinkOnHitComponent());
        bag.add(new SoundEmitterComponent(SoundFileStrings.spawningMix));


        bag.add(new CollisionBoundComponent(new Rectangle(x, y, width, height), true));

        bag.add(new AnimationStateComponent(0));
        IntMap<Animation<TextureRegion>> animMap = new IntMap<Animation<TextureRegion>>();
        animMap.put(0, new Animation<TextureRegion>(0.15f / 1f,
                atlas.findRegions(TextureStrings.ALURM), Animation.PlayMode.LOOP));
        animMap.put(AnimationStateComponent.FIRING, new Animation<TextureRegion>(0.05f / 1f,
                atlas.findRegions(TextureStrings.ALURM_FIRING), Animation.PlayMode.LOOP));

        bag.add(new AnimationComponent(animMap));
        bag.add(new TextureRegionComponent(atlas.findRegion(TextureStrings.ALURM),
                (width / 2) - (textureWidth / 2), (height / 2) - (textureHeight / 2), textureWidth, textureHeight,
                TextureRegionComponent.ENEMY_LAYER_NEAR));

        bag.add(new FadeComponent(false, chestFadeOutTime, false));

        bag.add(new VelocityComponent());
        bag.add(new BounceComponent());


        bag.add(new ParentComponent());

        bag.add(new OnHitActionComponent(new Action() {
            @Override
            public void performAction(World world, Entity e) {

                VelocityComponent vc = e.getComponent(VelocityComponent.class);

                vc.velocity.y = MathUtils.random.nextBoolean() ? speed : -speed;
                vc.velocity.x = MathUtils.random.nextBoolean() ? speed : -speed;

            }
        }));


        bag.add(new OnDeathActionComponent(new Action() {
            @Override
            public void performAction(World world, Entity e) {
                defaultDeathAction().performAction(world, e);
                new ShowChestAction().performAction(world, e);
            }
        }));

        bag.add(new ActionAfterTimeComponent(new Action() {
            @Override
            public void performAction(World world, Entity e) {

                new HideChestAction().performAction(world, e);

                createSpawnerAnimation(world, e.getComponent(ParentComponent.class), e);


                e.getComponent(ActionAfterTimeComponent.class).resetTime = chestFadeOutTime;
                e.getComponent(ActionAfterTimeComponent.class).action = new Action() {
                    @Override
                    public void performAction(World world, Entity e) {
                        IntBag bag = world.getAspectSubscriptionManager().get(Aspect.all(ChestComponent.class)).getEntities();

                        for(int i = 0; i < bag.size(); i++) {
                            world.getEntity(bag.get(i)).deleteFromWorld();
                        }

                        e.deleteFromWorld();
                    }
                };


            }
        }, 0 , true));


        return bag;

    }


    private Entity createSpawnerAnimation(World world, ParentComponent hoarderParentComponent, Entity followEntity){

        PositionComponent pc = followEntity.getComponent(PositionComponent.class);
        CollisionBoundComponent cbc = followEntity.getComponent(CollisionBoundComponent.class);

        Entity spawner = world.createEntity();
        spawner.edit().add(new PositionComponent(cbc.bound.x, cbc.bound.y));
        spawner.edit().add(new FollowPositionComponent(pc.position,
                CenterMath.offsetX(cbc.bound.getWidth(), portalWidth),
                CenterMath.offsetY(cbc.bound.getHeight(), portalHeight)));


        spawner.edit().add(new AnimationStateComponent(AnimationStateComponent.DEFAULT));
        Animation<TextureRegion> a = new Animation<TextureRegion>(1.0f / 35f, atlas.findRegions(TextureStrings.SPAWNER), Animation.PlayMode.LOOP);
        IntMap<Animation<TextureRegion>> animMap = new IntMap<Animation<TextureRegion>>();
        animMap.put(AnimationStateComponent.DEFAULT, a);
        spawner.edit().add(new AnimationComponent(animMap));
        spawner.edit().add(new TextureRegionComponent(atlas.findRegion(TextureStrings.SPAWNER), portalWidth, portalHeight,
                TextureRegionComponent.ENEMY_LAYER_MIDDLE, new Color(Color.WHITE)));

        spawner.edit().add(new FadeComponent(false, chestFadeOutTime, false));

        spawner.edit().add(new ChildComponent(hoarderParentComponent));

        return spawner;


    };

    private class HideChestAction implements Action {

        @Override
        public void performAction(World world, Entity e) {


            IntBag bag = world.getAspectSubscriptionManager().get(Aspect.all(ChestComponent.class)).getEntities();

            for(int i = 0; i < bag.size(); i++) {
                Entity chest = world.getEntity(bag.get(i));

                chest.edit().add(new FadeComponent(false,chestFadeOutTime, false));

                CollisionBoundComponent cbc = chest.getComponent(CollisionBoundComponent.class);
                cbc.hitBoxDisabled = true;

                createSpawnerAnimation(world, e.getComponent(ParentComponent.class), chest);

            }

        }








    }


    private class ShowChestAction implements Action {

        @Override
        public void performAction(World world, Entity e) {
            IntBag bag = world.getAspectSubscriptionManager().get(Aspect.all(ChestComponent.class)).getEntities();

            for(int i = 0; i < bag.size(); i++) {
                Entity chest = world.getEntity(bag.get(i));
                chest.edit().remove(FadeComponent.class);
                chest.edit().add(new FadeComponent(true,chestFadeInTime, false));
                chest.getComponent(VelocityComponent.class).velocity.y = chestReappearanceSpeed;
                chest.getComponent(CollisionBoundComponent.class).hitBoxDisabled = false;
            }
        }
    }


}

