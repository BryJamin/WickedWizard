package com.byrjamin.wickedwizard.factories.weapons;

import com.artemis.Component;
import com.artemis.Entity;
import com.artemis.World;
import com.artemis.utils.Bag;
import com.badlogic.gdx.assets.AssetManager;
import com.byrjamin.wickedwizard.ecs.components.Weapon;
import com.byrjamin.wickedwizard.ecs.components.movement.GravityComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.VelocityComponent;
import com.byrjamin.wickedwizard.factories.AbstractFactory;
import com.byrjamin.wickedwizard.factories.BulletFactory;
import com.byrjamin.wickedwizard.utils.Measure;

/**
 * Created by Home on 13/04/2017.
 */

public class WeaponFactory extends AbstractFactory {


    BulletFactory bf;

    public WeaponFactory(AssetManager assetManager) {
        super(assetManager);
        bf = new BulletFactory(assetManager);
    }

    public Weapon enemyWeapon(){
        return new Weapon() {
            @Override
            public void fire(World world, Entity e, float x, float y, double angle) {
                bf.createEnemyBullet(world, x, y, angle);
            }

            @Override
            public float getBaseFireRate() {
                return 2f;
            }

            @Override
            public float getBaseDamage() {
                return 1f;
            }
        };
    }


    public Weapon SilverHeadWeapon(){
        return new Weapon() {
            @Override
            public void fire(World world, Entity e, float x, float y, double angle) {

                int[] angles = new int[] {0,30,60,80,100,120,150,180};
                //Math.toRadians()
                for(int i : angles){
                    double angleOfTravel = angle + Math.toRadians(i);
                    Bag<Component> bag = bf.basicEnemyBulletBag(x, y, 4);
                    bag.add(new VelocityComponent((float) (Measure.units(75) * Math.cos(angleOfTravel)), (float) (Measure.units(75) * Math.sin(angleOfTravel))));
                    bag.add(new GravityComponent());

                    Entity bullet = world.createEntity();

                    for(Component c : bag){
                        bullet.edit().add(c);
                    }

                }

            }

            @Override
            public float getBaseFireRate() {
                return 1;
            }

            @Override
            public float getBaseDamage() {
                return 1;
            }
        };
    }


}
