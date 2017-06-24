package com.byrjamin.wickedwizard.factories;

import com.artemis.Component;
import com.artemis.Entity;
import com.artemis.World;
import com.artemis.utils.Bag;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.byrjamin.wickedwizard.ecs.components.BlinkComponent;
import com.byrjamin.wickedwizard.ecs.components.HealthComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.ExpireComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.ExpiryRangeComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.OnDeathActionComponent;
import com.byrjamin.wickedwizard.ecs.components.identifiers.BulletComponent;
import com.byrjamin.wickedwizard.ecs.components.CollisionBoundComponent;
import com.byrjamin.wickedwizard.ecs.components.identifiers.EnemyComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.PositionComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.VelocityComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.TextureRegionComponent;
import com.byrjamin.wickedwizard.utils.BagSearch;
import com.byrjamin.wickedwizard.utils.ComponentBag;
import com.byrjamin.wickedwizard.utils.Measure;

/**
 * Created by Home on 29/03/2017.
 */

public class BulletFactory extends AbstractFactory {



    public BulletFactory(AssetManager assetManager) {
        super(assetManager);

    }

    private float width = Measure.units(1);
    private float height = Measure.units(1);

    public Entity createEnemyBullet(World world, float x, float y, double angleOfTravel){
        Entity e = world.createEntity();
        for(Component c : basicEnemyBulletBag(x,y,4)){
            e.edit().add(c);
        }
        e.edit().add(new VelocityComponent((float) (Measure.units(50) * Math.cos(angleOfTravel)), (float) (Measure.units(50) * Math.sin(angleOfTravel))));
        return e;
    }

    public ComponentBag enemyBulletBag(ComponentBag fill, float x, float y, double angleOfTravel) {
        return enemyBulletBag(fill,x,y,4,angleOfTravel);
    }



    public ComponentBag enemyBulletBag(ComponentBag fill, float x, float y, float scale, double angleOfTravel) {
        for(Component c : basicEnemyBulletBag(x,y,scale)){
            fill.add(c);
        }
        fill.add(new VelocityComponent((float) (Measure.units(50) * Math.cos(angleOfTravel)), (float) (Measure.units(50) * Math.sin(angleOfTravel))));
        fill.add(new ExpiryRangeComponent(new Vector3(BagSearch.getObjectOfTypeClass(PositionComponent.class, fill).position), Measure.units(1f)));

        return fill;
    }






    public Bag<Component> basicBulletBag(float x, float y, float scale) {
        return basicBulletBag(x ,y ,scale ,atlas.findRegion("block"), new Color(1,1,1,1));
    }

    public Bag<Component> basicEnemyBulletBag(float x, float y, float scale) {

        Bag<Component> bag = basicBulletBag(x ,y ,scale ,atlas.findRegion("block") , new Color(Color.RED));
        bag.add(new EnemyComponent());


        OnDeathActionComponent odc = BagSearch.getObjectOfTypeClass(OnDeathActionComponent.class, bag);
        odc.task = new GibletFactory(assetManager).giblets(5, 0.2f, (int) Measure.units(10f), (int) Measure.units(20f),Measure.units(0.5f), new Color(Color.RED));

        return bag;
    }

    public Bag<Component> basicEnemyBulletBagNoGibs(float x, float y, float scale) {

        Bag<Component> bag = basicBulletBag(x ,y ,scale ,atlas.findRegion("block") , new Color(Color.RED));
        bag.add(new EnemyComponent());
        return bag;
    }


    public Bag<Component> basicBulletBag(float x, float y, float scale, TextureRegion textureRegion, Color color) {
        Bag<Component> bag = new Bag<Component>();

        float width = this.width * scale;
        float height = this.height * scale;

        float cX = x - width / 2;
        float cY = y - height / 2;

        bag.add(new PositionComponent(cX, cY));
        bag.add(new BulletComponent());
        //bag.add(new ExpireComponent(10f)); //TODO Probably doesn't have to be this long (or delete bullets if they leave the room bounds)
        bag.add(new CollisionBoundComponent(new Rectangle
                (cX,cY, width, height)));

        TextureRegionComponent trc = new TextureRegionComponent(textureRegion, width, height, TextureRegionComponent.PLAYER_LAYER_FAR);
        trc.DEFAULT = color;
        trc.color = color;
        bag.add(new OnDeathActionComponent());

        bag.add(trc);
        return bag;
    }



    public Bag<Component> basicBulletBlockBag(float x, float y, float scale, TextureRegion textureRegion, Color color) {
        Bag<Component> bag = new Bag<Component>();

        float width = this.width * scale;
        float height = this.height * scale;

        float cX = x - width / 2;
        float cY = y - height / 2;

        bag.add(new PositionComponent(cX, cY));
        bag.add(new BulletComponent());
        bag.add(new ExpireComponent(10f));//TODO Probably doesn't have to be this long (or delete bullets if they leave the room bounds)
        bag.add(new HealthComponent(3));
        bag.add(new BlinkComponent());
        bag.add(new CollisionBoundComponent(new Rectangle
                (cX,cY, width, height), true));



        TextureRegionComponent trc = new TextureRegionComponent(textureRegion, 0, 0,  width, height, TextureRegionComponent.PLAYER_LAYER_FAR);
        trc.DEFAULT = color;
        trc.color = color;
        bag.add(trc);
        return bag;
    }


    public ComponentBag enemyBlockBulletBag(ComponentBag fill, float x, float y, double angleOfTravel) {
        for(Component c : basicBulletBlockBag(x,y,4, atlas.findRegion("block") , new Color(Color.BLACK))){
            fill.add(c);
        }
        fill.add(new VelocityComponent((float) (Measure.units(20) * Math.cos(angleOfTravel)), (float) (Measure.units(20) * Math.sin(angleOfTravel))));
        fill.add(new EnemyComponent());
        return fill;
    }







}
