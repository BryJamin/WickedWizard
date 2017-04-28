package com.byrjamin.wickedwizard.factories;

import com.artemis.Component;
import com.artemis.Entity;
import com.artemis.World;
import com.artemis.utils.Bag;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.IntMap;
import com.byrjamin.wickedwizard.assets.TextureStrings;
import com.byrjamin.wickedwizard.ecs.components.ActiveOnTouchComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.AnimationComponent;
import com.byrjamin.wickedwizard.ecs.components.BlinkComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.BounceComponent;
import com.byrjamin.wickedwizard.ecs.components.identifiers.BulletComponent;
import com.byrjamin.wickedwizard.ecs.components.CollisionBoundComponent;
import com.byrjamin.wickedwizard.ecs.components.object.DoorComponent;
import com.byrjamin.wickedwizard.ecs.components.identifiers.EnemyComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.FiringAIComponent;
import com.byrjamin.wickedwizard.ecs.components.object.GrappleableComponent;
import com.byrjamin.wickedwizard.ecs.components.HealthComponent;
import com.byrjamin.wickedwizard.ecs.components.object.LockComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.PositionComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.AnimationStateComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.TextureRegionBatchComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.TextureRegionComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.VelocityComponent;
import com.byrjamin.wickedwizard.ecs.components.object.WallComponent;
import com.byrjamin.wickedwizard.ecs.components.WeaponComponent;
import com.byrjamin.wickedwizard.utils.AnimationPacker;
import com.byrjamin.wickedwizard.utils.Measure;
import com.byrjamin.wickedwizard.utils.MapCoords;
import com.byrjamin.wickedwizard.screens.PlayScreen;

/**
 * Created by Home on 04/03/2017.
 */
public class EntityFactory extends AbstractFactory {

    public EntityFactory(AssetManager assetManager) {
        super(assetManager);
    }

    public Bag<Component> wallBag(float x, float y, float width, float height){
        Bag<Component> bag = new Bag<Component>();
        bag.add(new PositionComponent(x,y));
        bag.add(new WallComponent(new Rectangle(x,y, width, height)));

        TextureRegionBatchComponent trbc = BackgroundFactory.generateTRBC(width, height, Measure.units(5),
                atlas.findRegions("brick"), TextureRegionComponent.PLAYER_LAYER_FAR);
        trbc.setColor(0.7f, 0, 0f, 1);
        bag.add(trbc);

        return bag;
    }

    public Bag<Component> wallBag(Rectangle r){
        return wallBag(r.x, r.y, r.width, r.height);
    }


    public Bag<Component> doorBag(float x, float y, MapCoords current, MapCoords leaveCoords, DoorComponent.DIRECTION exit){

        Bag<Component> bag = new Bag<Component>();
        bag.add(new PositionComponent(x,y));
        bag.add(new CollisionBoundComponent(new Rectangle(x,y,Measure.units(5), Measure.units(20))));
        bag.add(new DoorComponent(current, leaveCoords, exit));

        AnimationStateComponent sc = new AnimationStateComponent();
        sc.setState(AnimationStateComponent.State.UNLOCKED.getState());
        sc.stateTime = 100f;
        bag.add(sc);

        IntMap<Animation<TextureRegion>> aniMap = new IntMap<Animation<TextureRegion>>();
        aniMap.put(AnimationStateComponent.State.LOCKED.getState(),
                new Animation<TextureRegion>(1 / 35f, atlas.findRegions("door")));
        aniMap.put(AnimationStateComponent.State.UNLOCKED.getState(),
                new Animation<TextureRegion>(1 / 35f, atlas.findRegions("door"), Animation.PlayMode.REVERSED));
        bag.add(new AnimationComponent(aniMap));
        //TODO explains the giant blob

        TextureRegionComponent trc = new TextureRegionComponent(aniMap.get(AnimationStateComponent.State.UNLOCKED.getState()).getKeyFrame(sc.stateTime),
                -Measure.units(8.5f), 0, Measure.units(22), Measure.units(20),
                TextureRegionComponent.PLAYER_LAYER_FAR);
        trc.setColor(0.7f, 0, 0f, 1);
        bag.add(trc);
        //bag.add(new GrappleableComponent());
        bag.add(new LockComponent());
        return bag;
    }

    public Bag<Component> grateBag(float x, float y, MapCoords current, MapCoords leaveCoords, DoorComponent.DIRECTION exit){
        Bag<Component> bag = new Bag<Component>();

        float width = Measure.units(10);
        float height = Measure.units(10);

        x = x - width / 2;
        y = y - height / 2;

        bag.add(new PositionComponent(x,y));
        bag.add(new CollisionBoundComponent(new Rectangle(x,y,width, height)));
        bag.add(new DoorComponent(current, leaveCoords, exit));

        TextureRegionComponent trc = new TextureRegionComponent(atlas.findRegion("grate"), width, height,
                TextureRegionComponent.BACKGROUND_LAYER_NEAR);
        trc.setColor(0.7f, 0, 0f, 1);



        AnimationStateComponent sc = new AnimationStateComponent();
        sc.setState(AnimationStateComponent.State.UNLOCKED.getState());
        sc.stateTime = 100f;
        bag.add(sc);

        IntMap<Animation<TextureRegion>> aniMap = new IntMap<Animation<TextureRegion>>();
        aniMap.put(AnimationStateComponent.State.LOCKED.getState(),
                new Animation<TextureRegion>(1 / 35f, atlas.findRegions("grate"), Animation.PlayMode.REVERSED));
        aniMap.put(AnimationStateComponent.State.UNLOCKED.getState(),
                new Animation<TextureRegion>(1 / 35f, atlas.findRegions("grate")));
        bag.add(new AnimationComponent(aniMap));


        bag.add(trc);
        bag.add(new GrappleableComponent());
        bag.add(new ActiveOnTouchComponent());
        bag.add(new LockComponent());
        return bag;
    }

    public Bag<Component> grapplePointBag(float x, float y){

        float width = Measure.units(10);
        float height = Measure.units(10);

        Bag<Component> bag = new Bag<Component>();
        bag.add(new PositionComponent(x - width /  2,y - height /2 ));
        bag.add(new CollisionBoundComponent(new Rectangle(x - width / 2,y - height / 2, width, height)));
        bag.add(new TextureRegionComponent(atlas.findRegion(TextureStrings.GRAPPLE),
                Measure.units(2.5f),
                Measure.units(2.5f),
                Measure.units(5),
                Measure.units(5),
                TextureRegionComponent.BACKGROUND_LAYER_NEAR));
        bag.add(new GrappleableComponent());

        return bag;

    }

}
