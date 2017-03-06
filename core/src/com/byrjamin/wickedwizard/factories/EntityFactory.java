package com.byrjamin.wickedwizard.factories;

import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.math.Rectangle;
import com.byrjamin.wickedwizard.assets.TextureStrings;
import com.byrjamin.wickedwizard.components.BulletComponent;
import com.byrjamin.wickedwizard.components.CollisionBoundComponent;
import com.byrjamin.wickedwizard.components.EnemyComponent;
import com.byrjamin.wickedwizard.components.FriendlyComponent;
import com.byrjamin.wickedwizard.components.GravityComponent;
import com.byrjamin.wickedwizard.components.HealthComponent;
import com.byrjamin.wickedwizard.components.PlayerComponent;
import com.byrjamin.wickedwizard.components.PositionComponent;
import com.byrjamin.wickedwizard.components.TextureRegionComponent;
import com.byrjamin.wickedwizard.components.VelocityComponent;
import com.byrjamin.wickedwizard.components.WallComponent;
import com.byrjamin.wickedwizard.components.WeaponComponent;
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
        e.edit().add(new WeaponComponent(0.3f));
        e.edit().add(new HealthComponent(10));
        e.edit().add(new TextureRegionComponent(PlayScreen.atlas.findRegion("squ_walk"),-Measure.units(0.5f), 0, Measure.units(6), Measure.units(6)));
        return e;
    }


    public static Entity createBlob(World world, float x, float y){
        Entity e = world.createEntity();
        e.edit().add(new PositionComponent(x,y));
        e.edit().add(new VelocityComponent(0, 0));
        e.edit().add(new CollisionBoundComponent(new Rectangle(0,0,Measure.units(9), Measure.units(9))));
        e.edit().add(new GravityComponent());
        e.edit().add(new EnemyComponent());
        e.edit().add(new HealthComponent(15));
        e.edit().add(new TextureRegionComponent(PlayScreen.atlas.findRegion(TextureStrings.BLOB_STANDING),-Measure.units(1f), 0, Measure.units(12), Measure.units(12)));
        return e;
    }

    public static Entity createBullet(World world, float x, float y, double angleOfTravel){
        Entity e = world.createEntity();
        e.edit().add(new PositionComponent(x - Measure.units(1),y - Measure.units(1)));
        //e.edit().add(new PositionComponent(x,y));
        e.edit().add(new BulletComponent());
        e.edit().add(new FriendlyComponent());
        e.edit().add(new VelocityComponent((float) (Measure.units(100) * Math.cos(angleOfTravel)), (float) (Measure.units(100) * Math.sin(angleOfTravel))));
        e.edit().add(new CollisionBoundComponent(new Rectangle(0,0,Measure.units(2), Measure.units(2))));
        e.edit().add(new TextureRegionComponent(PlayScreen.atlas.findRegion("bullet"), Measure.units(2), Measure.units(2)));
        return e;
    }


}
