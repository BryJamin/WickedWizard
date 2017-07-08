package com.byrjamin.wickedwizard.factories;

import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.IntMap;
import com.byrjamin.wickedwizard.assets.TextureStrings;
import com.byrjamin.wickedwizard.ecs.components.CollisionBoundComponent;
import com.byrjamin.wickedwizard.ecs.components.ExplosionComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.ProximityTriggerAIComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.Task;
import com.byrjamin.wickedwizard.ecs.components.ai.Condition;
import com.byrjamin.wickedwizard.ecs.components.ai.ConditionalActionComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.ExpireComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.OnDeathActionComponent;
import com.byrjamin.wickedwizard.ecs.components.identifiers.IntangibleComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.BounceComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.FrictionComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.GravityComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.PositionComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.VelocityComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.AnimationComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.AnimationStateComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.FadeComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.TextureRegionComponent;
import com.byrjamin.wickedwizard.utils.BagToEntity;
import com.byrjamin.wickedwizard.utils.ComponentBag;
import com.byrjamin.wickedwizard.utils.Measure;
import com.byrjamin.wickedwizard.utils.collider.HitBox;

/**
 * Created by Home on 23/05/2017.
 */

public class BombFactory extends  AbstractFactory{


    private GibletFactory gf;

    public BombFactory(AssetManager assetManager) {
        super(assetManager);
        this.gf = new GibletFactory(assetManager);
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
        bag.add(new GravityComponent());
        bag.add(new FrictionComponent(true, false, false));
        return bag;
    }


    public ComponentBag seaMine(float x, float y, boolean startsLeft, boolean startsUp){

        float width = Measure.units(10);
        float height = Measure.units(10);

        float speed = Measure.units(5f);


        ComponentBag bag = new ComponentBag();
        bag.add(new PositionComponent(x,y));

        TextureRegionComponent trc = new TextureRegionComponent(atlas.findRegion(TextureStrings.AIR_MINE), width, height, TextureRegionComponent.ENEMY_LAYER_MIDDLE);
        bag.add(trc);

        //Hazard?
        bag.add(new CollisionBoundComponent(new Rectangle(x,y, width, height), true));

        bag.add(new FadeComponent(true, 0.5f, false));

        bag.add(new BounceComponent());
        bag.add(new VelocityComponent(startsLeft ? speed : - speed, startsUp ? speed : -speed));


        bag.add(new ProximityTriggerAIComponent(new Task() {
            @Override
            public void performAction(World world, Entity e) {
                e.edit().add(new ExpireComponent(1f));
                e.edit().add(new AnimationStateComponent(0));
                IntMap<Animation<TextureRegion>> animMap = new IntMap<Animation<TextureRegion>>();
                animMap.put(0, new Animation<TextureRegion>(0.125f / 1f, atlas.findRegions(TextureStrings.AIR_MINE), Animation.PlayMode.LOOP));
                e.edit().add(new AnimationComponent(animMap));
                e.edit().add(new OnDeathActionComponent(explosionTask()));
            }

            @Override
            public void cleanUpAction(World world, Entity e) {

            }
        }, new HitBox(new Rectangle(x,y,width,height))));

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

        bag.add(new GravityComponent());
        bag.add(new VelocityComponent());
        bag.add(new ExpireComponent(life));
        bag.add(new FrictionComponent(true, false, false));

        bag.add(new TextureRegionComponent(atlas.findRegion(TextureStrings.BOMB),
                width, height,
                TextureRegionComponent.ENEMY_LAYER_MIDDLE));


        bag.add(new AnimationStateComponent(0));
        IntMap<Animation<TextureRegion>> animMap = new IntMap<Animation<TextureRegion>>();
        animMap.put(0, new Animation<TextureRegion>(0.25f / 1f, atlas.findRegions(TextureStrings.BOMB), Animation.PlayMode.LOOP));
        bag.add(new AnimationComponent(animMap));

        ConditionalActionComponent cac = new ConditionalActionComponent( new Condition() {
            @Override
            public boolean condition(World world, Entity entity) {
                return entity.getComponent(ExpireComponent.class).expiryTime < 0.75f;
            }
        }, new Task() {
            @Override
            public void performAction(World world, Entity e) {
                e.getComponent(AnimationComponent.class).animations.get(0).setFrameDuration(0.05f / 1f);
            }

            @Override
            public void cleanUpAction(World world, Entity e) {

            }
        });

        bag.add(cac);

        //TODO bombs do not have explosion component at the moment

        OnDeathActionComponent onDeathActionComponent = new OnDeathActionComponent(new Task() {
            @Override
            public void performAction(World world, Entity e) {

                CollisionBoundComponent cbc = e.getComponent(CollisionBoundComponent.class);

                BagToEntity.bagToEntity(world.createEntity(),
                        bombExplosion(cbc.getCenterX(), cbc.getCenterY(), Measure.units(20f), Measure.units(20f)));

                gf.bombGiblets(10, 0.35f, 0,
                        Measure.units(75f),
                        Measure.units(1.5f),
                        new Color(246/ 255f, 45f/255f, 45f/255f, 1f)).performAction(world, e);

                gf.bombGiblets(10, 0.35f, 0,
                        Measure.units(75f),
                        Measure.units(1.5f),
                        new Color(255f/ 255f, 124f/255f, 0f/255f, 1f)).performAction(world, e);

                gf.bombGiblets(10, 0.35f, 0,
                        Measure.units(75f),
                        Measure.units(1.5f),
                        new Color(249f/ 255f, 188f/255f, 4f/255f, 1f)).performAction(world, e);
            }

            @Override
            public void cleanUpAction(World world, Entity e) {

            }
        });

        bag.add(onDeathActionComponent);

        return bag;

    }


    public Task explosionTask(){
        return new Task() {
            @Override
            public void performAction(World world, Entity e) {

                CollisionBoundComponent cbc = e.getComponent(CollisionBoundComponent.class);

                BagToEntity.bagToEntity(world.createEntity(),
                        bombExplosion(cbc.getCenterX(), cbc.getCenterY(), Measure.units(20f), Measure.units(20f)));

                gf.bombGiblets(10, 0.35f, 0,
                        Measure.units(75f),
                        Measure.units(1.5f),
                        new Color(246 / 255f, 45f / 255f, 45f / 255f, 1f)).performAction(world, e);

                gf.bombGiblets(10, 0.35f, 0,
                        Measure.units(75f),
                        Measure.units(1.5f),
                        new Color(255f / 255f, 124f / 255f, 0f / 255f, 1f)).performAction(world, e);

                gf.bombGiblets(10, 0.35f, 0,
                        Measure.units(75f),
                        Measure.units(1.5f),
                        new Color(249f / 255f, 188f / 255f, 4f / 255f, 1f)).performAction(world, e);
            }

            @Override
            public void cleanUpAction(World world, Entity e) {

            }

        };
    }


    public ComponentBag bombExplosion(float x, float y, float width, float height) {

        x = x - width / 2;
        y = y - width / 2;

        ComponentBag bag = new ComponentBag();
        bag.add(new ExplosionComponent(1));
        bag.add(new CollisionBoundComponent(new Rectangle(x, y, width, height)));
        bag.add(new PositionComponent(x, y));
        bag.add(new ExpireComponent(2f));
        bag.add(new IntangibleComponent());

        return bag;

    }


}
