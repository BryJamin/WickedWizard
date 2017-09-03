package com.bryjamin.wickedwizard.factories;

import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.IntMap;
import com.bryjamin.wickedwizard.assets.ColorResource;
import com.bryjamin.wickedwizard.assets.SoundFileStrings;
import com.bryjamin.wickedwizard.assets.TextureStrings;
import com.bryjamin.wickedwizard.ecs.components.ExplosionComponent;
import com.bryjamin.wickedwizard.ecs.components.ai.Action;
import com.bryjamin.wickedwizard.ecs.components.ai.Condition;
import com.bryjamin.wickedwizard.ecs.components.ai.ConditionalActionComponent;
import com.bryjamin.wickedwizard.ecs.components.ai.ExpireComponent;
import com.bryjamin.wickedwizard.ecs.components.ai.OnDeathActionComponent;
import com.bryjamin.wickedwizard.ecs.components.ai.ProximityTriggerAIComponent;
import com.bryjamin.wickedwizard.ecs.components.ai.Task;
import com.bryjamin.wickedwizard.ecs.components.graphics.CameraShakeComponent;
import com.bryjamin.wickedwizard.ecs.components.identifiers.IntangibleComponent;
import com.bryjamin.wickedwizard.ecs.components.movement.BounceComponent;
import com.bryjamin.wickedwizard.ecs.components.movement.CollisionBoundComponent;
import com.bryjamin.wickedwizard.ecs.components.movement.FrictionComponent;
import com.bryjamin.wickedwizard.ecs.components.movement.PositionComponent;
import com.bryjamin.wickedwizard.ecs.components.movement.VelocityComponent;
import com.bryjamin.wickedwizard.ecs.components.texture.AnimationComponent;
import com.bryjamin.wickedwizard.ecs.components.texture.AnimationStateComponent;
import com.bryjamin.wickedwizard.ecs.components.texture.FadeComponent;
import com.bryjamin.wickedwizard.ecs.components.texture.TextureRegionComponent;
import com.bryjamin.wickedwizard.factories.weapons.Giblets;
import com.bryjamin.wickedwizard.utils.BagSearch;
import com.bryjamin.wickedwizard.utils.BagToEntity;
import com.bryjamin.wickedwizard.utils.ComponentBag;
import com.bryjamin.wickedwizard.utils.Measure;
import com.bryjamin.wickedwizard.utils.collider.HitBox;

/**
 * Created by Home on 23/05/2017.
 */

public class BombFactory extends AbstractFactory {

    public static final float BOMB_LIFE = 1f;


    private Giblets.GibletBuilder gibletBuilder;

    private static final float mineSpeed = Measure.units(10f);




    private static final float defaultExplosionSize = Measure.units(20f);

    public BombFactory(AssetManager assetManager) {
        super(assetManager);
        this.gibletBuilder = new Giblets.GibletBuilder(assetManager);
    }


    //TODO bouncey bomb that is a different color

    public ComponentBag mine(float x, float y, float angleInDegrees){

        float width = Measure.units(5);
        float height = Measure.units(5);


        ComponentBag bag = new ComponentBag();
        bag.add(new PositionComponent(x,y));

        TextureRegionComponent trc = new TextureRegionComponent(atlas.findRegion(TextureStrings.MINE), width, height, TextureRegionComponent.ENEMY_LAYER_MIDDLE);
        trc.rotation = angleInDegrees;
        bag.add(trc);

        //Hazard?
        bag.add(new CollisionBoundComponent(new Rectangle(x,y, width, height), true));


        bag.add(new ProximityTriggerAIComponent(new Task() {
            @Override
            public void performAction(World world, Entity e) {
                e.edit().add(new ExpireComponent(1f));
                e.edit().add(new AnimationStateComponent(0));
                IntMap<Animation<TextureRegion>> animMap = new IntMap<Animation<TextureRegion>>();
                animMap.put(0, new Animation<TextureRegion>(0.25f / 1f, atlas.findRegions(TextureStrings.MINE), Animation.PlayMode.LOOP));
                e.edit().add(new AnimationComponent(animMap));
                e.edit().add(new OnDeathActionComponent(explosionTask()));
            }

            @Override
            public void cleanUpAction(World world, Entity e) {

            }
        }, new HitBox(new Rectangle(x,y,width,height))));

        return bag;

    }

    public ComponentBag gravMine(float x, float y){
        ComponentBag bag = mine(x , y, 0);
        bag.add(new com.bryjamin.wickedwizard.ecs.components.movement.GravityComponent());
        bag.add(new FrictionComponent(true, false, false));
        return bag;
    }

    public ComponentBag fadeInSeaMine(float x, float y, boolean startsLeft, boolean starsUp){
        ComponentBag bag = multiDirectionalSeaMine(x, y, startsLeft, starsUp);
        bag.add(new FadeComponent(true, 0.5f, false));
        TextureRegionComponent trc = BagSearch.getObjectOfTypeClass(TextureRegionComponent.class, bag);
        if(trc != null) trc.color.a = 0f;

        return bag;
    }


    public ComponentBag stationarySeaMine(float x, float y){

        float width = Measure.units(10);
        float height = Measure.units(10);

        ComponentBag bag = new ComponentBag();
        bag.add(new PositionComponent(x,y));

        TextureRegionComponent trc = new TextureRegionComponent(atlas.findRegion(TextureStrings.AIR_MINE), width, height, TextureRegionComponent.ENEMY_LAYER_MIDDLE);
        bag.add(trc);

        bag.add(new CollisionBoundComponent(new Rectangle(x,y, width, height), true));

        bag.add(new ProximityTriggerAIComponent(new Task() {
            @Override
            public void performAction(World world, Entity e) {
                e.edit().add(new ExpireComponent(1f));
                e.edit().add(new AnimationStateComponent(AnimationStateComponent.DEFAULT));
                IntMap<Animation<TextureRegion>> animMap = new IntMap<Animation<TextureRegion>>();
                animMap.put(AnimationStateComponent.DEFAULT, new Animation<TextureRegion>(0.125f / 1f, atlas.findRegions(TextureStrings.AIR_MINE), Animation.PlayMode.LOOP));
                e.edit().add(new AnimationComponent(animMap));
                e.edit().add(new OnDeathActionComponent(explosionTask()));
            }

            @Override
            public void cleanUpAction(World world, Entity e) {

            }
        }, new HitBox(new Rectangle(x,y,width,height))));

        return bag;
    }


    public ComponentBag horizontalSeaMine(float x, float y, boolean startsRight){

        ComponentBag bag = stationarySeaMine(x, y);
        bag.add(new BounceComponent());
        bag.add(new VelocityComponent(startsRight ? mineSpeed : -mineSpeed, 0));

        return bag;

    }

    public ComponentBag verticalSeaMine(float x, float y, boolean startsUp){

        ComponentBag bag = stationarySeaMine(x, y);
        bag.add(new BounceComponent());
        bag.add(new VelocityComponent(0, startsUp ? mineSpeed : -mineSpeed));

        return bag;

    }

    public ComponentBag multiDirectionalSeaMine(float x, float y, boolean startsLeft, boolean startsUp){

        ComponentBag bag = stationarySeaMine(x, y);
        bag.add(new BounceComponent());
        bag.add(new VelocityComponent(startsLeft ? mineSpeed : - mineSpeed, startsUp ? mineSpeed : -mineSpeed));

        return bag;

    }

    public ComponentBag bomb(float x, float y, float life){

        float width = Measure.units(5);
        float height = Measure.units(5);

        x = x - width / 2;
        y = y - width / 2;

        ComponentBag bag = new ComponentBag();

        bag.add(new PositionComponent(x, y));
        bag.add(new CollisionBoundComponent(new Rectangle(x,y,width,height)));

        bag.add(new com.bryjamin.wickedwizard.ecs.components.movement.GravityComponent());
        bag.add(new VelocityComponent());
        bag.add(new ExpireComponent(life));
        bag.add(new FrictionComponent(true, false, false));

        bag.add(new TextureRegionComponent(atlas.findRegion(TextureStrings.BOMB),
                width, height,
                TextureRegionComponent.ENEMY_LAYER_MIDDLE));


        bag.add(new AnimationStateComponent(AnimationStateComponent.DEFAULT));
        IntMap<Animation<TextureRegion>> animMap = new IntMap<Animation<TextureRegion>>();
        animMap.put(AnimationStateComponent.DEFAULT, new Animation<TextureRegion>(0.25f / 1f, atlas.findRegions(TextureStrings.BOMB), Animation.PlayMode.LOOP));
        bag.add(new AnimationComponent(animMap));

        ConditionalActionComponent cac = new ConditionalActionComponent( new Condition() {
            @Override
            public boolean condition(World world, Entity entity) {
                return entity.getComponent(ExpireComponent.class).expiryTime < 0.75f;
            }
        }, new Action() {
            @Override
            public void performAction(World world, Entity e) {
                e.getComponent(AnimationComponent.class).animations.get(0).setFrameDuration(0.05f / 1f);
            }
        });

        bag.add(cac);

        bag.add(new OnDeathActionComponent(explosionTask()));

        return bag;

    }


    public Action explosionTask(){
        return new Action() {
            @Override
            public void performAction(World world, Entity e) {

                CollisionBoundComponent cbc = e.getComponent(CollisionBoundComponent.class);

                BagToEntity.bagToEntity(world.createEntity(),
                        bombExplosion(cbc.getCenterX(), cbc.getCenterY(), defaultExplosionSize, defaultExplosionSize));

                gibletBuilder.numberOfGibletPairs(15)
                        .expiryTime(0.35f)
                        .fadeChance(0.25f)
                        .size(Measure.units(1.5f))
                        .minSpeed(Measure.units(0f))
                        .maxSpeed(Measure.units(75f))
                        .mixes(SoundFileStrings.explosionMegaMix)
                        .colors(new Color(ColorResource.BOMB_ORANGE), new Color(ColorResource.BOMB_RED), new Color(ColorResource.BOMB_YELLOW))
                        .build().performAction(world, e);

            }
        };
    }


    public ComponentBag bombExplosion(float x, float y, float width, float height) {

        x = x - width / 2;
        y = y - height / 2;

        ComponentBag bag = new ComponentBag();
        bag.add(new ExplosionComponent(1));
        bag.add(new CollisionBoundComponent(new Rectangle(x, y, width, height)));
        bag.add(new PositionComponent(x, y));
        bag.add(new CameraShakeComponent(0.5f));
        bag.add(new ExpireComponent(0.15f));
        bag.add(new IntangibleComponent());

        return bag;

    }


}
