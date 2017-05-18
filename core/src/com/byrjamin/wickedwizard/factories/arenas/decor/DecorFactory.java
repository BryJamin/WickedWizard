package com.byrjamin.wickedwizard.factories.arenas.decor;

import com.artemis.Component;
import com.artemis.utils.Bag;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.IntMap;
import com.byrjamin.wickedwizard.assets.TextureStrings;
import com.byrjamin.wickedwizard.ecs.components.ActiveOnTouchComponent;
import com.byrjamin.wickedwizard.ecs.components.WeaponComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.FiringAIComponent;
import com.byrjamin.wickedwizard.ecs.components.object.PlatformComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.AnimationComponent;
import com.byrjamin.wickedwizard.ecs.components.CollisionBoundComponent;
import com.byrjamin.wickedwizard.ecs.components.object.DoorComponent;
import com.byrjamin.wickedwizard.ecs.components.object.GrappleableComponent;
import com.byrjamin.wickedwizard.ecs.components.object.LockComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.PositionComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.AnimationStateComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.TextureRegionBatchComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.TextureRegionComponent;
import com.byrjamin.wickedwizard.ecs.components.object.WallComponent;
import com.byrjamin.wickedwizard.factories.AbstractFactory;
import com.byrjamin.wickedwizard.factories.BackgroundFactory;
import com.byrjamin.wickedwizard.factories.arenas.skins.ArenaSkin;
import com.byrjamin.wickedwizard.factories.weapons.enemy.Pistol;
import com.byrjamin.wickedwizard.utils.ComponentBag;
import com.byrjamin.wickedwizard.utils.Measure;
import com.byrjamin.wickedwizard.utils.MapCoords;
import com.byrjamin.wickedwizard.utils.enums.Direction;

/**
 * Created by Home on 04/03/2017.
 */
public class DecorFactory extends AbstractFactory {

    private ArenaSkin arenaSkin;
    private BackgroundFactory bf;

    public DecorFactory(AssetManager assetManager, ArenaSkin arenaSkin) {
        super(assetManager);
        this.arenaSkin = arenaSkin;
        bf = new BackgroundFactory();
    }


    public Bag<Component> wallBag(float x, float y, float width, float height, ArenaSkin arenaSkin){
        Bag<Component> bag = new Bag<Component>();
        bag.add(new PositionComponent(x,y));
        bag.add(new WallComponent(new Rectangle(x,y, width, height)));

        TextureRegionBatchComponent trbc = bf.generateTRBC(width, height, Measure.units(5),
                arenaSkin.getWallTexture(),
                TextureRegionComponent.PLAYER_LAYER_FAR);
        trbc.color = arenaSkin.getWallTint();
        bag.add(trbc);

        return bag;
    }


    public ComponentBag chevronBag(float x, float y, float rotationInDegrees){

        float width = Measure.units(8);
        float height = Measure.units(8);

        x = x - width;
        y = y - height;


        ComponentBag bag = new ComponentBag();
        bag.add(new PositionComponent(x,y));

        AnimationStateComponent sc = new AnimationStateComponent();
        bag.add(sc);

        IntMap<Animation<TextureRegion>> aniMap = new IntMap<Animation<TextureRegion>>();
        aniMap.put(sc.getDefaultState(),
                new Animation<TextureRegion>(1 / 35f, atlas.findRegions("chevron"), Animation.PlayMode.LOOP_PINGPONG));
        bag.add(new AnimationComponent(aniMap));

        TextureRegionComponent trc = new TextureRegionComponent(aniMap.get(sc.getDefaultState()).getKeyFrame(sc.stateTime), width, height,
                TextureRegionComponent.BACKGROUND_LAYER_NEAR);

        trc.rotation = rotationInDegrees;

        bag.add(trc);

        return bag;


    }


    public Bag<Component> platform(float x, float y, float width){
        Bag<Component> bag = new Bag<Component>();
        bag.add(new PositionComponent(x,y));
        bag.add(new CollisionBoundComponent(new Rectangle(x,y + Measure.units(2.5f),width,Measure.units(2.5f))));
        bag.add(new PlatformComponent());
        //bag.add(new WallComponent(new Rectangle(x,y, width, Measure.units(5f))));

        TextureRegionBatchComponent trbc = bf.generateTRBC(width, Measure.units(5f), Measure.units(5),
                atlas.findRegions("platform"),
                TextureRegionComponent.PLAYER_LAYER_FAR);
        trbc.color = arenaSkin.getWallTint();
        trbc.offsetY = -1;
        //trbc.color = new Color(1,1,1, 0.2f);
        bag.add(trbc);

        return bag;
    }

/*
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
*/

/*
    public Bag<Component> wallBag(Rectangle r){
        return wallBag(r.x, r.y, r.width, r.height);
    }
*/

    public Bag<Component> doorBag(float x, float y, MapCoords current, MapCoords leaveCoords, Direction exit){
        return doorBag(x,y,true,current,leaveCoords,exit);
    }


    public Bag<Component> doorBag(float x, float y, boolean isVertical, MapCoords current, MapCoords leaveCoords, Direction exit){

        Bag<Component> bag = new Bag<Component>();
        bag.add(new PositionComponent(x,y));

        float width = isVertical ? Measure.units(5) : Measure.units(20);
        float height = isVertical ? Measure.units(20) : Measure.units(5);

        bag.add(new CollisionBoundComponent(new Rectangle(x,y,width, height)));
        bag.add(new DoorComponent(current, leaveCoords, exit));

        AnimationStateComponent sc = new AnimationStateComponent();
        sc.setDefaultState(AnimationStateComponent.State.UNLOCKED.getState());
        sc.setCurrentState(AnimationStateComponent.State.UNLOCKED.getState());
        sc.stateTime = 100f;
        bag.add(sc);

        IntMap<Animation<TextureRegion>> aniMap = new IntMap<Animation<TextureRegion>>();
        aniMap.put(AnimationStateComponent.State.LOCKED.getState(),
                new Animation<TextureRegion>(1 / 35f, atlas.findRegions("block_door")));
        aniMap.put(AnimationStateComponent.State.UNLOCKED.getState(),
                new Animation<TextureRegion>(1 / 35f, atlas.findRegions("block_door"), Animation.PlayMode.REVERSED));
        bag.add(new AnimationComponent(aniMap));
        //TODO explains the giant blob

        TextureRegionComponent trc = new TextureRegionComponent(aniMap.get(AnimationStateComponent.State.UNLOCKED.getState()).getKeyFrame(sc.stateTime),
                0, 0, Measure.units(27), Measure.units(22),
                TextureRegionComponent.PLAYER_LAYER_FAR);

        trc.color = arenaSkin.getWallTint();
        trc.DEFAULT = arenaSkin.getWallTint();

        trc.rotation = isVertical ? 0 : 90;
        //trc.setColor(0.7f, 0, 0f, 1);
        bag.add(trc);
        //bag.add(new GrappleableComponent());
        bag.add(new LockComponent());
        return bag;
    }


    public Bag<Component> horizontalDoorBag(float x, float y, MapCoords current, MapCoords leaveCoords, Direction exit){

        Bag<Component> bag = new Bag<Component>();
        bag.add(new PositionComponent(x,y));

        float width = Measure.units(20);
        float height = Measure.units(5);

        bag.add(new CollisionBoundComponent(new Rectangle(x,y,width, height)));
        bag.add(new DoorComponent(current, leaveCoords, exit));

        AnimationStateComponent sc = new AnimationStateComponent();
        sc.setDefaultState(AnimationStateComponent.State.UNLOCKED.getState());
        sc.setCurrentState(AnimationStateComponent.State.UNLOCKED.getState());
        sc.stateTime = 100f;
        bag.add(sc);

        IntMap<Animation<TextureRegion>> aniMap = new IntMap<Animation<TextureRegion>>();
        aniMap.put(AnimationStateComponent.State.LOCKED.getState(),
                new Animation<TextureRegion>(1 / 35f, atlas.findRegions("block_door")));
        aniMap.put(AnimationStateComponent.State.UNLOCKED.getState(),
                new Animation<TextureRegion>(1 / 35f, atlas.findRegions("block_door"), Animation.PlayMode.REVERSED));
        bag.add(new AnimationComponent(aniMap));
        //TODO explains the giant blob

        TextureRegionComponent trc = new TextureRegionComponent(aniMap.get(AnimationStateComponent.State.UNLOCKED.getState()).getKeyFrame(sc.stateTime),
                -Measure.units(2.5f), Measure.units(2.5f), Measure.units(27), Measure.units(22),
                TextureRegionComponent.PLAYER_LAYER_FAR);

        trc.color = arenaSkin.getWallTint();
        trc.DEFAULT = arenaSkin.getWallTint();

        trc.rotation = 90;
        //trc.setColor(0.7f, 0, 0f, 1);
        bag.add(trc);
        //bag.add(new GrappleableComponent());
        bag.add(new LockComponent());
        return bag;
    }






    public Bag<Component> grateBag(float x, float y, MapCoords current, MapCoords leaveCoords, Direction exit){
        Bag<Component> bag = new Bag<Component>();

        float width = Measure.units(10);
        float height = Measure.units(10);

        x = x - width / 2;
        y = y - height / 2;

        bag.add(new PositionComponent(x,y));
        bag.add(new CollisionBoundComponent(new Rectangle(x,y,width, height)));
        bag.add(new DoorComponent(current, leaveCoords, exit));

        //trc.setColor(0.7f, 0, 0f, 1);



        AnimationStateComponent sc = new AnimationStateComponent();
        sc.setDefaultState(AnimationStateComponent.State.UNLOCKED.getState());
        sc.stateTime = 100f;
        bag.add(sc);

        IntMap<Animation<TextureRegion>> aniMap = new IntMap<Animation<TextureRegion>>();
        aniMap.put(AnimationStateComponent.State.LOCKED.getState(),
                new Animation<TextureRegion>(1 / 35f, atlas.findRegions("hole"), Animation.PlayMode.REVERSED));
        aniMap.put(AnimationStateComponent.State.UNLOCKED.getState(),
                new Animation<TextureRegion>(1 / 35f, atlas.findRegions("hole")));
        bag.add(new AnimationComponent(aniMap));


        TextureRegionComponent trc = new TextureRegionComponent(aniMap.get(AnimationStateComponent.State.UNLOCKED.getState()).getKeyFrame(sc.stateTime), width, height,
                TextureRegionComponent.BACKGROUND_LAYER_NEAR);
        trc.color = arenaSkin.getBackgroundTint();

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





    public ComponentBag fixedWallTurret(float x, float y, float angleInDegrees, float fireRate, float fireDelay){

        float width = Measure.units(5);
        float height = Measure.units(5);


        ComponentBag bag = new ComponentBag();
        bag.add(new PositionComponent(x,y));

        TextureRegionComponent trc = new TextureRegionComponent(atlas.findRegion("wall_turret"), width, height, TextureRegionComponent.ENEMY_LAYER_MIDDLE);
        trc.rotation = angleInDegrees + 90;
        trc.DEFAULT = arenaSkin.getWallTint();
        trc.color = arenaSkin.getWallTint();

        bag.add(trc);

        //Hazard?
        bag.add(new CollisionBoundComponent(new Rectangle(x,y, width, height), true));
/*        bag.add(new TextureRegionComponent(PlayScreen.atlas.findRegion(TextureStrings.BLOB_STANDING),
                -Measure.units(1f), 0, Measure.units(12), Measure.units(12), TextureRegionComponent.ENEMY_LAYER_MIDDLE
        ));*/

        WeaponComponent wc = new WeaponComponent(new Pistol(assetManager, fireRate), fireDelay);
        bag.add(wc);
        bag.add(new FiringAIComponent(angleInDegrees));

        return bag;
    }




}
