package com.byrjamin.wickedwizard.factories;

import com.artemis.Component;
import com.artemis.Entity;
import com.artemis.World;
import com.artemis.utils.Bag;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.byrjamin.wickedwizard.ecs.components.BlinkComponent;
import com.byrjamin.wickedwizard.ecs.components.HealthComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.ExpireComponent;
import com.byrjamin.wickedwizard.ecs.components.identifiers.BulletComponent;
import com.byrjamin.wickedwizard.ecs.components.CollisionBoundComponent;
import com.byrjamin.wickedwizard.ecs.components.identifiers.EnemyComponent;
import com.byrjamin.wickedwizard.ecs.components.identifiers.FriendlyComponent;
import com.byrjamin.wickedwizard.ecs.components.OnDeathComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.PositionComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.VelocityComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.TextureRegionComponent;
import com.byrjamin.wickedwizard.utils.ComponentBag;
import com.byrjamin.wickedwizard.utils.Measure;
import com.byrjamin.wickedwizard.screens.PlayScreen;

/**
 * Created by Home on 29/03/2017.
 */

public class BulletFactory {


    private static float width = Measure.units(1);
    private static float height = Measure.units(1);



    public static Entity createBullet(World world, float x, float y, double angleOfTravel){
        Entity e = world.createEntity();
        for(Component c : basicBulletBag(x,y,1,PlayScreen.atlas.findRegion("bullet"))){
            e.edit().add(c);
        }
        e.edit().add(new FriendlyComponent());
        e.edit().add(new VelocityComponent((float) (Measure.units(90) * Math.cos(angleOfTravel)), (float) (Measure.units(90) * Math.sin(angleOfTravel))));

        return e;
    }

    public static Entity createEnemyBullet(World world, float x, float y, double angleOfTravel){
        Entity e = world.createEntity();
        for(Component c : basicEnemyBulletBag(x,y,2)){
            e.edit().add(c);
        }
        e.edit().add(new VelocityComponent((float) (Measure.units(50) * Math.cos(angleOfTravel)), (float) (Measure.units(50) * Math.sin(angleOfTravel))));
        return e;
    }

    public static ComponentBag enemyBulletBag(ComponentBag fill, float x, float y, double angleOfTravel) {
        for(Component c : basicEnemyBulletBag(x,y,2)){
            fill.add(c);
        }
        fill.add(new VelocityComponent((float) (Measure.units(50) * Math.cos(angleOfTravel)), (float) (Measure.units(50) * Math.sin(angleOfTravel))));
        return fill;
    }







    public static Bag<Component> basicBulletBag(float x, float y, float scale, TextureRegion textureRegion) {
        return basicBulletBag(x ,y ,scale ,textureRegion , Color.WHITE);
    }

    public static Bag<Component> basicEnemyBulletBag(float x, float y, float scale) {

        Bag<Component> bag = basicBulletBag(x ,y ,scale ,PlayScreen.atlas.findRegion("bullet") , Color.RED);
        bag.add(new EnemyComponent());
        return bag;
    }


    public static Bag<Component> basicBulletBag(float x, float y, float scale, TextureRegion textureRegion, Color color) {
        Bag<Component> bag = new Bag<Component>();

        float width = BulletFactory.width * scale;
        float height = BulletFactory.height * scale;

        float cX = x - width / 2;
        float cY = y - height / 2;

        bag.add(new PositionComponent(cX, cY));
        bag.add(new BulletComponent());
        bag.add(new ExpireComponent(10f)); //TODO Probably doesn't have to be this long (or delete bullets if they leave the room bounds)
        bag.add(new CollisionBoundComponent(new Rectangle
                (cX,cY, width, height)));

        OnDeathComponent odc = new OnDeathComponent();
        bag.add(DeathFactory.basicOnDeathExplosion(odc, width, height));

        TextureRegionComponent trc = new TextureRegionComponent(textureRegion,-width / 2,-height / 2,  width * 2, height * 2, TextureRegionComponent.PLAYER_LAYER_FAR);
        trc.DEFAULT = color;
        trc.color = color;
        bag.add(trc);
        return bag;
    }



    public static Bag<Component> basicBulletBlockBag(float x, float y, float scale, TextureRegion textureRegion, Color color) {
        Bag<Component> bag = new Bag<Component>();

        float width = BulletFactory.width * scale;
        float height = BulletFactory.height * scale;

        float cX = x - width / 2;
        float cY = y - height / 2;

        bag.add(new PositionComponent(cX, cY));
        bag.add(new BulletComponent());
        bag.add(new ExpireComponent(10f));//TODO Probably doesn't have to be this long (or delete bullets if they leave the room bounds)
        bag.add(new HealthComponent(3));
        bag.add(new BlinkComponent());
        bag.add(new CollisionBoundComponent(new Rectangle
                (cX,cY, width, height), true));

        OnDeathComponent odc = new OnDeathComponent();
        bag.add(DeathFactory.basicOnDeathExplosion(odc, width, height));

        TextureRegionComponent trc = new TextureRegionComponent(textureRegion, 0, 0,  width, height, TextureRegionComponent.PLAYER_LAYER_FAR);
        trc.DEFAULT = color;
        trc.color = color;
        bag.add(trc);
        return bag;
    }


    public static ComponentBag enemyBlockBulletBag(ComponentBag fill, float x, float y, double angleOfTravel) {
        for(Component c : basicBulletBlockBag(x,y,4, PlayScreen.atlas.findRegion("block") , Color.BLACK)){
            fill.add(c);
        }
        fill.add(new VelocityComponent((float) (Measure.units(20) * Math.cos(angleOfTravel)), (float) (Measure.units(20) * Math.sin(angleOfTravel))));
        fill.add(new EnemyComponent());
        return fill;
    }







}
