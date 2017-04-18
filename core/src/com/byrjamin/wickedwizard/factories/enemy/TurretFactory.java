package com.byrjamin.wickedwizard.factories.enemy;

import com.artemis.Component;
import com.artemis.utils.Bag;
import com.badlogic.gdx.math.Rectangle;
import com.byrjamin.wickedwizard.assets.TextureStrings;
import com.byrjamin.wickedwizard.ecs.components.BlinkComponent;
import com.byrjamin.wickedwizard.ecs.components.OnDeathComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.BounceComponent;
import com.byrjamin.wickedwizard.ecs.components.CollisionBoundComponent;
import com.byrjamin.wickedwizard.ecs.components.EnemyComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.FiringAIComponent;
import com.byrjamin.wickedwizard.ecs.components.HealthComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.PositionComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.VelocityComponent;
import com.byrjamin.wickedwizard.ecs.components.WeaponComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.TextureRegionComponent;
import com.byrjamin.wickedwizard.factories.DeathFactory;
import com.byrjamin.wickedwizard.factories.weapons.WeaponFactory;
import com.byrjamin.wickedwizard.factories.items.pickups.HealthUp;
import com.byrjamin.wickedwizard.factories.items.ItemFactory;
import com.byrjamin.wickedwizard.utils.Measure;
import com.byrjamin.wickedwizard.screens.PlayScreen;

import java.util.Random;

/**
 * Created by Home on 26/03/2017.
 */

public class TurretFactory {

    static float width = Measure.units(9f);
    static float height = Measure.units(9f);

    public static Bag<Component> fixedTurret(float x, float y){

        x = x - width / 2;
        y = y - width / 2;


        Bag<Component> bag = new Bag<Component>();
        bag.add(new PositionComponent(x,y));
        bag.add(new CollisionBoundComponent(new Rectangle(x,y, Measure.units(9), Measure.units(9)), true));
        bag.add(new EnemyComponent());
        bag.add(new HealthComponent(10));
        bag.add(new TextureRegionComponent(PlayScreen.atlas.findRegion(TextureStrings.BLOB_STANDING),
                -Measure.units(1f), 0, Measure.units(12), Measure.units(12), TextureRegionComponent.ENEMY_LAYER_MIDDLE
                ));
        bag.add(new BlinkComponent());

        WeaponComponent wc = new WeaponComponent(WeaponFactory.EnemyWeapon(), 2f);
        bag.add(wc);
        bag.add(new FiringAIComponent());

        OnDeathComponent odc = new OnDeathComponent();
        odc.getComponentBags().addAll(ItemFactory.createPickUpBag(0,0, new HealthUp()));
        bag.add(DeathFactory.basicOnDeathExplosion(odc, Measure.units(9), Measure.units(9), 0,0));

        return bag;
    }


    public static Bag<Component> movingTurret(float x, float y){

        x = x - width / 2;
        y = y - width / 2;

        Bag<Component> bag = new Bag<Component>();
        bag.add(new PositionComponent(x,y));

        Random random = new Random();
        if(random.nextBoolean()) {
            bag.add(new VelocityComponent(300, 0));
        } else {
            bag.add(new VelocityComponent(-300, 0));
        }
        bag.add(new CollisionBoundComponent(new Rectangle(x,y, Measure.units(9), Measure.units(9)), true));
        bag.add(new EnemyComponent());
        bag.add(new HealthComponent(10));
        bag.add(new TextureRegionComponent(PlayScreen.atlas.findRegion(TextureStrings.BLOB_STANDING),
                -Measure.units(1f), 0, Measure.units(12), Measure.units(12),
                TextureRegionComponent.ENEMY_LAYER_MIDDLE));
        bag.add(new BlinkComponent());

        WeaponComponent wc = new WeaponComponent(WeaponFactory.EnemyWeapon(), 2f);
        bag.add(wc);
        bag.add(new FiringAIComponent());
        bag.add(new BounceComponent());

        OnDeathComponent odc = new OnDeathComponent();
        odc.getComponentBags().addAll(ItemFactory.createPickUpBag(0,0, new HealthUp()));
        bag.add(DeathFactory.basicOnDeathExplosion(odc, Measure.units(9), Measure.units(9), 0,0));

        return bag;
    }



    /*

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


     */





}
