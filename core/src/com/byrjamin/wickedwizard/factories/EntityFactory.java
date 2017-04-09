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
import com.byrjamin.wickedwizard.ecs.components.movement.BounceComponent;
import com.byrjamin.wickedwizard.ecs.components.BulletComponent;
import com.byrjamin.wickedwizard.ecs.components.CollisionBoundComponent;
import com.byrjamin.wickedwizard.ecs.components.object.DoorComponent;
import com.byrjamin.wickedwizard.ecs.components.EnemyComponent;
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
import com.byrjamin.wickedwizard.archive.maps.MapCoords;
import com.byrjamin.wickedwizard.screens.PlayScreen;

/**
 * Created by Home on 04/03/2017.
 */
public class EntityFactory {

    public static Bag<Component> wallBag(float x, float y, float width, float height){
        Bag<Component> bag = new Bag<Component>();
        bag.add(new PositionComponent(x,y));
        bag.add(new WallComponent(new Rectangle(x,y, width, height)));

        TextureRegionBatchComponent trbc = BackgroundFactory.generateTRBC(width, height, Measure.units(5),
                PlayScreen.atlas.findRegions("brick"), TextureRegionComponent.FOREGROUND_LAYER_FAR);

        bag.add(trbc);

        return bag;
    }

    public static Bag<Component> wallBag(Rectangle r){
        return wallBag(r.x, r.y, r.width, r.height);
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

        TextureRegionComponent trc = new TextureRegionComponent(AnimationPacker.genAnimation(1 / 35f, "door", Animation.PlayMode.REVERSED).getKeyFrame(sc.stateTime),
                -Measure.units(8.5f), 0, Measure.units(22), Measure.units(20),
                TextureRegionComponent.FOREGROUND_LAYER_MIDDLE);
        bag.add(trc);
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

        TextureRegionComponent trc = new TextureRegionComponent(PlayScreen.atlas.findRegion("grate"), width, height,
                TextureRegionComponent.BACKGROUND_LAYER_NEAR);
        bag.add(trc);
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
        e.edit().add(new TextureRegionComponent(PlayScreen.atlas.findRegion(TextureStrings.BLOB_STANDING),
                -Measure.units(1f), 0, Measure.units(12), Measure.units(12),
                TextureRegionComponent.ENEMY_LAYER_MIDDLE));
        return e;
    }



    public static Entity createBullet(World world, float x, float y, double angleOfTravel){
        Entity e = world.createEntity();
        e.edit().add(new PositionComponent(x - Measure.units(1),y - Measure.units(1)));
        //e.edit().add(new PositionComponent(x,y));
        e.edit().add(new BulletComponent());
        e.edit().add(new VelocityComponent((float) (Measure.units(100) * Math.cos(angleOfTravel)), (float) (Measure.units(100) * Math.sin(angleOfTravel))));
        e.edit().add(new CollisionBoundComponent(new Rectangle(x - Measure.units(1),y - Measure.units(1),Measure.units(2), Measure.units(2))));
        e.edit().add(new TextureRegionComponent(PlayScreen.atlas.findRegion("bullet"), Measure.units(2), Measure.units(2), TextureRegionComponent.PLAYER_LAYER_FAR));
        return e;
    }

    public static Entity createEnemyBullet(World world, float x, float y, double angleOfTravel){
        Entity e = world.createEntity();
        e.edit().add(new PositionComponent(x - Measure.units(2),y - Measure.units(2)));
        e.edit().add(new BulletComponent());
        e.edit().add(new VelocityComponent((float) (Measure.units(50) * Math.cos(angleOfTravel)), (float) (Measure.units(50) * Math.sin(angleOfTravel))));
        e.edit().add(new CollisionBoundComponent(new Rectangle(x - Measure.units(2),y - Measure.units(2),Measure.units(4), Measure.units(4))));

        TextureRegionComponent trc = new TextureRegionComponent(PlayScreen.atlas.findRegion("bullet"), Measure.units(4), Measure.units(4), TextureRegionComponent.PLAYER_LAYER_FAR);
        trc.DEFAULT = Color.RED;
        trc.color = Color.RED;
        e.edit().add(trc);
        return e;
    }


    public static Bag<Component> grapplePointBag(float x, float y){

        float width = Measure.units(10);
        float height = Measure.units(10);

        Bag<Component> bag = new Bag<Component>();
        bag.add(new PositionComponent(x - width /  2,y - height /2 ));
        bag.add(new CollisionBoundComponent(new Rectangle(x - width / 2,y - height / 2, width, height)));
        bag.add(new TextureRegionComponent(PlayScreen.atlas.findRegion(TextureStrings.GRAPPLE),
                Measure.units(2.5f),
                Measure.units(2.5f),
                Measure.units(5),
                Measure.units(5),
                TextureRegionComponent.BACKGROUND_LAYER_NEAR));
        bag.add(new GrappleableComponent());

        return bag;

    }

}
