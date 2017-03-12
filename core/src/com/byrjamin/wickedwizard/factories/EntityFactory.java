package com.byrjamin.wickedwizard.factories;

import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.IntMap;
import com.byrjamin.wickedwizard.assets.TextureStrings;
import com.byrjamin.wickedwizard.components.AnimationComponent;
import com.byrjamin.wickedwizard.components.BlinkComponent;
import com.byrjamin.wickedwizard.components.BounceComponent;
import com.byrjamin.wickedwizard.components.BulletComponent;
import com.byrjamin.wickedwizard.components.CollisionBoundComponent;
import com.byrjamin.wickedwizard.components.EnemyComponent;
import com.byrjamin.wickedwizard.components.FiringAIComponent;
import com.byrjamin.wickedwizard.components.FriendlyComponent;
import com.byrjamin.wickedwizard.components.GravityComponent;
import com.byrjamin.wickedwizard.components.HealthComponent;
import com.byrjamin.wickedwizard.components.PlayerComponent;
import com.byrjamin.wickedwizard.components.PositionComponent;
import com.byrjamin.wickedwizard.components.StateComponent;
import com.byrjamin.wickedwizard.components.MoveToPlayerComponent;
import com.byrjamin.wickedwizard.components.TextureRegionComponent;
import com.byrjamin.wickedwizard.components.VelocityComponent;
import com.byrjamin.wickedwizard.components.WallComponent;
import com.byrjamin.wickedwizard.components.WeaponComponent;
import com.byrjamin.wickedwizard.helper.AnimationPacker;
import com.byrjamin.wickedwizard.helper.Measure;
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


    public static Entity createPlayer(World world){
        Entity e = world.createEntity();
        e.edit().add(new PositionComponent(600,900));
        e.edit().add(new VelocityComponent(0, 0));
        e.edit().add(new PlayerComponent());
        e.edit().add(new CollisionBoundComponent(new Rectangle(0,0,100, 100)));
        e.edit().add(new GravityComponent());

        StateComponent sc = new StateComponent();
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
        StateComponent sc = new StateComponent();
        sc.setState(0);
        e.edit().add(sc);
        IntMap<Animation<TextureRegion>> k = new IntMap<Animation<TextureRegion>>();
        k.put(0, AnimationPacker.genAnimation(0.25f / 1f, TextureStrings.BLOB_STANDING, Animation.PlayMode.LOOP));
        e.edit().add(new AnimationComponent(k));
        e.edit().add(new TextureRegionComponent(PlayScreen.atlas.findRegion(TextureStrings.BLOB_STANDING),-Measure.units(1f), 0, Measure.units(12), Measure.units(12)));
        return e;
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

        StateComponent sc = new StateComponent();
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

        StateComponent sc = new StateComponent();
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

}
