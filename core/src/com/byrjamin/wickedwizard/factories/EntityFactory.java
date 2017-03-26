package com.byrjamin.wickedwizard.factories;

import com.artemis.Component;
import com.artemis.Entity;
import com.artemis.World;
import com.artemis.utils.Bag;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.IntMap;
import com.byrjamin.wickedwizard.assets.TextureStrings;
import com.byrjamin.wickedwizard.ecs.components.ActiveOnTouchComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.AnimationComponent;
import com.byrjamin.wickedwizard.ecs.components.BlinkComponent;
import com.byrjamin.wickedwizard.ecs.components.BounceComponent;
import com.byrjamin.wickedwizard.ecs.components.BulletComponent;
import com.byrjamin.wickedwizard.ecs.components.CollisionBoundComponent;
import com.byrjamin.wickedwizard.ecs.components.DoorComponent;
import com.byrjamin.wickedwizard.ecs.components.EnemyComponent;
import com.byrjamin.wickedwizard.ecs.components.FiringAIComponent;
import com.byrjamin.wickedwizard.ecs.components.FriendlyComponent;
import com.byrjamin.wickedwizard.ecs.components.GrappleableComponent;
import com.byrjamin.wickedwizard.ecs.components.GravityComponent;
import com.byrjamin.wickedwizard.ecs.components.HealthComponent;
import com.byrjamin.wickedwizard.ecs.components.LockComponent;
import com.byrjamin.wickedwizard.ecs.components.MoveToComponent;
import com.byrjamin.wickedwizard.ecs.components.PlayerComponent;
import com.byrjamin.wickedwizard.ecs.components.PositionComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.AnimationStateComponent;
import com.byrjamin.wickedwizard.ecs.components.MoveToPlayerComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.TextureRegionBatchComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.TextureRegionComponent;
import com.byrjamin.wickedwizard.ecs.components.VelocityComponent;
import com.byrjamin.wickedwizard.ecs.components.WallComponent;
import com.byrjamin.wickedwizard.ecs.components.WeaponComponent;
import com.byrjamin.wickedwizard.helper.AnimationPacker;
import com.byrjamin.wickedwizard.helper.Measure;
import com.byrjamin.wickedwizard.maps.MapCoords;
import com.byrjamin.wickedwizard.screens.PlayScreen;

/**
 * Created by Home on 04/03/2017.
 */
public class EntityFactory {

    public static Entity createWall(World world, float x, float y, float width, float height){
        Entity e = world.createEntity();
        e.edit().add(new PositionComponent(x,y));
        e.edit().add(new WallComponent(new Rectangle(x,y, width, height)));
        return e;
    }

    public static Entity createWall(World world, Rectangle r){
        return createWall(world, r.x, r.y, r.width, r.height);
    }

    public static Bag<Component> wallBag(float x, float y, float width, float height){
        Bag<Component> bag = new Bag<Component>();
        bag.add(new PositionComponent(x,y));
        bag.add(new WallComponent(new Rectangle(x,y, width, height)));

        TextureRegionBatchComponent trbc = BackgroundFactory.generateTRBC(width, height, Measure.units(5),
                PlayScreen.atlas.findRegions("brick"));
        trbc.layer = -9;

        bag.add(trbc);

        return bag;
    }

    public static Bag<Component> wallBag(Rectangle r){
        return wallBag(r.x, r.y, r.width, r.height);
    }


    public static Entity createPlayer(World world){
        Entity e = world.createEntity();
        e.edit().add(new PositionComponent(600,900));
        e.edit().add(new VelocityComponent(0, 0));
        e.edit().add(new PlayerComponent());
        e.edit().add(new CollisionBoundComponent(new Rectangle(0,0,100, 100)));
        e.edit().add(new GravityComponent());

        AnimationStateComponent sc = new AnimationStateComponent();
        sc.setState(0);
        e.edit().add(sc);

        IntMap<Animation<TextureRegion>> k = new IntMap<Animation<TextureRegion>>();
        k.put(0, TextureStrings.SQU_WALK);
        k.put(1, TextureStrings.SQU_FIRING);

        e.edit().add(new AnimationComponent(k));


        WeaponComponent wc = new WeaponComponent(0.3f);
        wc.additionalComponenets.add(new FriendlyComponent());
        e.edit().add(wc);
        e.edit().add(new HealthComponent(10));
        e.edit().add(new BlinkComponent(1, BlinkComponent.BLINKTYPE.FLASHING));

        TextureRegionComponent trc = new TextureRegionComponent(PlayScreen.atlas.findRegion("squ_walk"),-Measure.units(0.5f), 0, Measure.units(6), Measure.units(6));
        trc.layer = 2;
        e.edit().add(trc);
        return e;
    }


    public static Bag<Component> playerBag(){

        Bag<Component> bag = new Bag<Component>();
        bag.add(new PositionComponent(600,900));
        bag.add(new PositionComponent(600,900));
        bag.add(new VelocityComponent(0, 0));
        bag.add(new PlayerComponent());
        bag.add(new CollisionBoundComponent(new Rectangle(0,0,100, 100)));
        bag.add(new GravityComponent());
        bag.add(new MoveToComponent(Measure.units(115f)));

        AnimationStateComponent sc = new AnimationStateComponent();
        sc.setState(0);
        bag.add(sc);

        IntMap<Animation<TextureRegion>> k = new IntMap<Animation<TextureRegion>>();
        k.put(0, TextureStrings.SQU_WALK);
        k.put(1, TextureStrings.SQU_FIRING);

        bag.add(new AnimationComponent(k));


        WeaponComponent wc = new WeaponComponent(0.3f);
        wc.additionalComponenets.add(new FriendlyComponent());
        bag.add(wc);
        bag.add(new HealthComponent(3));
        bag.add(new BlinkComponent(1, BlinkComponent.BLINKTYPE.FLASHING));

        TextureRegionComponent trc = new TextureRegionComponent(PlayScreen.atlas.findRegion("squ_walk"),-Measure.units(0.5f), 0, Measure.units(6), Measure.units(6));
        trc.layer = 2;
        bag.add(trc);

        return bag;
    }


    public static Entity createBlob(World world, float x, float y){
        Entity e = world.createEntity();
        e.edit().add(new PositionComponent(x,y));
        e.edit().add(new VelocityComponent(0, 0));
        e.edit().add(new CollisionBoundComponent(new Rectangle(0,0,Measure.units(9), Measure.units(9))));
        e.edit().add(new GravityComponent());
        e.edit().add(new EnemyComponent());
        e.edit().add(new MoveToPlayerComponent());
        e.edit().add(new HealthComponent(10));
        e.edit().add(new BlinkComponent());
        AnimationStateComponent sc = new AnimationStateComponent();
        sc.setState(0);
        e.edit().add(sc);
        IntMap<Animation<TextureRegion>> k = new IntMap<Animation<TextureRegion>>();
        k.put(0, AnimationPacker.genAnimation(0.25f / 1f, TextureStrings.BLOB_STANDING, Animation.PlayMode.LOOP));
        e.edit().add(new AnimationComponent(k));
        e.edit().add(new TextureRegionComponent(PlayScreen.atlas.findRegion(TextureStrings.BLOB_STANDING),-Measure.units(1f), 0, Measure.units(12), Measure.units(12)));
        return e;
    }


    public static Bag<Component> blobBag(float x, float y){

        Bag<Component> bag = new Bag<Component>();

        bag.add(new PositionComponent(x,y));
        bag.add(new VelocityComponent(0, 0));
        bag.add(new CollisionBoundComponent(new Rectangle(x,y,Measure.units(9), Measure.units(9))));
        bag.add(new GravityComponent());
        bag.add(new EnemyComponent());
        bag.add(new MoveToPlayerComponent());
        bag.add(new HealthComponent(10));
        bag.add(new BlinkComponent());
        AnimationStateComponent sc = new AnimationStateComponent();
        sc.setState(0);
        bag.add(sc);
        IntMap<Animation<TextureRegion>> k = new IntMap<Animation<TextureRegion>>();
        k.put(0, AnimationPacker.genAnimation(0.25f / 1f, TextureStrings.BLOB_STANDING, Animation.PlayMode.LOOP));
        bag.add(new AnimationComponent(k));
        bag.add(new TextureRegionComponent(PlayScreen.atlas.findRegion(TextureStrings.BLOB_STANDING),-Measure.units(1f), 0, Measure.units(12), Measure.units(12)));
        return bag;
    }

    public static Bag<Component> doorBag(float x, float y, MapCoords current, MapCoords leaveCoords, DoorComponent.DIRECTION exit){

        Bag<Component> bag = new Bag<Component>();
        bag.add(new PositionComponent(x,y));
        bag.add(new CollisionBoundComponent(new Rectangle(x,y,Measure.units(5), Measure.units(20))));
        bag.add(new DoorComponent(current, leaveCoords, exit));

        AnimationStateComponent sc = new AnimationStateComponent();
        sc.setState(AnimationStateComponent.State.UNLOCKED.getState());
        sc.stateTime = 100f;
        bag.add(sc);

        IntMap<Animation<TextureRegion>> aniMap = new IntMap<Animation<TextureRegion>>();
        aniMap.put(AnimationStateComponent.State.LOCKED.getState(), AnimationPacker.genAnimation(1 / 35f, "door"));
        aniMap.put(AnimationStateComponent.State.UNLOCKED.getState(), AnimationPacker.genAnimation(1 / 35f, "door", Animation.PlayMode.REVERSED));
        bag.add(new AnimationComponent(aniMap));
        //TODO explains the giant blob
        bag.add(new TextureRegionComponent(AnimationPacker.genAnimation(1 / 35f, "door", Animation.PlayMode.REVERSED).getKeyFrame(sc.stateTime),-Measure.units(8.5f), 0, Measure.units(22), Measure.units(20)));
        bag.add(new GrappleableComponent());
        bag.add(new LockComponent());
        return bag;
    }

    public static Bag<Component> grateBag(float x, float y, MapCoords current, MapCoords leaveCoords, DoorComponent.DIRECTION exit){
        Bag<Component> bag = new Bag<Component>();

        float width = Measure.units(10);
        float height = Measure.units(10);

        x = x - width / 2;
        y = y - height / 2;

        bag.add(new PositionComponent(x,y));
        bag.add(new CollisionBoundComponent(new Rectangle(x,y,width, height)));
        bag.add(new DoorComponent(current, leaveCoords, exit));
        bag.add(new TextureRegionComponent(PlayScreen.atlas.findRegion("grate"), width, height));
        bag.add(new GrappleableComponent());
        bag.add(new ActiveOnTouchComponent());
        bag.add(new LockComponent());
        return bag;
    }


    public static Entity createBouncer(World world, float x, float y){
        Entity e = world.createEntity();
        e.edit().add(new PositionComponent(x,y));
        e.edit().add(new VelocityComponent(600, 0));
        e.edit().add(new CollisionBoundComponent(new Rectangle(0,0,Measure.units(9), Measure.units(9))));
        //e.edit().add(new GravityComponent());
        e.edit().add(new BounceComponent());
        e.edit().add(new EnemyComponent());
        e.edit().add(new HealthComponent(10));
        e.edit().add(new BlinkComponent());

        WeaponComponent wc = new WeaponComponent(2f);
        wc.additionalComponenets.add(new EnemyComponent());
        e.edit().add(wc);

        e.edit().add(new FiringAIComponent());

        AnimationStateComponent sc = new AnimationStateComponent();
        sc.setState(0);
        e.edit().add(sc);
        IntMap<Animation<TextureRegion>> k = new IntMap<Animation<TextureRegion>>();
        k.put(0, AnimationPacker.genAnimation(0.25f / 1f, TextureStrings.BLOB_STANDING, Animation.PlayMode.LOOP));
        e.edit().add(new AnimationComponent(k));
        e.edit().add(new TextureRegionComponent(PlayScreen.atlas.findRegion(TextureStrings.BLOB_STANDING),-Measure.units(1f), 0, Measure.units(12), Measure.units(12)));
        return e;
    }


    public static Entity createMovingTurret(World world, float x, float y){
        Entity e = world.createEntity();
        e.edit().add(new PositionComponent(x,y));
        e.edit().add(new VelocityComponent(300, 0));
        e.edit().add(new CollisionBoundComponent(new Rectangle(0,0,Measure.units(9), Measure.units(9))));
        //e.edit().add(new GravityComponent());
        e.edit().add(new BounceComponent());
        e.edit().add(new EnemyComponent());
        e.edit().add(new HealthComponent(10));
        e.edit().add(new BlinkComponent());

        WeaponComponent wc = new WeaponComponent(2f);
        wc.additionalComponenets.add(new EnemyComponent());
        e.edit().add(wc);

        e.edit().add(new FiringAIComponent());

        AnimationStateComponent sc = new AnimationStateComponent();
        sc.setState(0);
        e.edit().add(sc);
        IntMap<Animation<TextureRegion>> k = new IntMap<Animation<TextureRegion>>();
        k.put(0, AnimationPacker.genAnimation(0.25f / 1f, TextureStrings.BLOB_STANDING, Animation.PlayMode.LOOP));
        e.edit().add(new AnimationComponent(k));
        e.edit().add(new TextureRegionComponent(PlayScreen.atlas.findRegion(TextureStrings.BLOB_STANDING),-Measure.units(1f), 0, Measure.units(12), Measure.units(12)));
        return e;
    }


    public static Entity createBullet(World world, float x, float y, double angleOfTravel){
        Entity e = world.createEntity();
        e.edit().add(new PositionComponent(x - Measure.units(1),y - Measure.units(1)));
        //e.edit().add(new PositionComponent(x,y));
        e.edit().add(new BulletComponent());
        e.edit().add(new VelocityComponent((float) (Measure.units(100) * Math.cos(angleOfTravel)), (float) (Measure.units(100) * Math.sin(angleOfTravel))));
        e.edit().add(new CollisionBoundComponent(new Rectangle(0,0,Measure.units(2), Measure.units(2))));
        e.edit().add(new TextureRegionComponent(PlayScreen.atlas.findRegion("bullet"), Measure.units(2), Measure.units(2)));
        return e;
    }

    public static Entity createEnemyBullet(World world, float x, float y, double angleOfTravel){
        Entity e = world.createEntity();
        e.edit().add(new PositionComponent(x - Measure.units(2),y - Measure.units(2)));
        e.edit().add(new BulletComponent());
        e.edit().add(new VelocityComponent((float) (Measure.units(30) * Math.cos(angleOfTravel)), (float) (Measure.units(30) * Math.sin(angleOfTravel))));
        e.edit().add(new CollisionBoundComponent(new Rectangle(0,0,Measure.units(4), Measure.units(4))));

        TextureRegionComponent trc = new TextureRegionComponent(PlayScreen.atlas.findRegion("bullet"), Measure.units(4), Measure.units(4));
        trc.DEFAULT = Color.RED;
        trc.color = Color.RED;
        e.edit().add(trc);
        return e;
    }


    public static Bag<Component> grapplePointBag(float x, float y){

        Bag<Component> bag = new Bag<Component>();
        bag.add(new PositionComponent(x,y));
        bag.add(new CollisionBoundComponent(new Rectangle(x,y, Measure.units(10), Measure.units(10))));
        bag.add(new TextureRegionComponent(PlayScreen.atlas.findRegion(TextureStrings.GRAPPLE),
                Measure.units(2.5f),
                Measure.units(2.5f),
                Measure.units(5),
                Measure.units(5)));
        bag.add(new GrappleableComponent());

        return bag;

    }

}
