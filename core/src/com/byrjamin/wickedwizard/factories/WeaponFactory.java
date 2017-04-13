package com.byrjamin.wickedwizard.factories;

import com.artemis.Component;
import com.artemis.Entity;
import com.artemis.World;
import com.artemis.utils.Bag;
import com.byrjamin.wickedwizard.ecs.components.EnemyComponent;
import com.byrjamin.wickedwizard.ecs.components.Weapon;
import com.byrjamin.wickedwizard.ecs.components.movement.GravityComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.VelocityComponent;
import com.byrjamin.wickedwizard.utils.ComponentBag;
import com.byrjamin.wickedwizard.utils.Measure;

/**
 * Created by Home on 13/04/2017.
 */

public class WeaponFactory {

    public static Weapon EnemyWeapon(){
        return new Weapon() {
            @Override
            public void fire(World world, float x, float y, double angle) {
                BulletFactory.createEnemyBullet(world, x, y, angle);
            }
        };
    }

    public static Weapon PlayerWeapon(){
        return new Weapon() {
            @Override
            public void fire(World world, float x, float y, double angle) {
                BulletFactory.createBullet(world, x, y, angle);
            }
        };
    }





    public static Weapon SilverHeadWeapon(){
        return new Weapon() {
            @Override
            public void fire(World world, float x, float y, double angle) {

                int[] angles = new int[] {0,30,60,80,100,120,150,180};
                //Math.toRadians()
                for(int i : angles){
                    double angleOfTravel = angle + Math.toRadians(i);
                    Bag<Component> bag = BulletFactory.basicEnemyBulletBag(x, y, 1.7f);
                    bag.add(new VelocityComponent((float) (Measure.units(75) * Math.cos(angleOfTravel)), (float) (Measure.units(75) * Math.sin(angleOfTravel))));
                    bag.add(new GravityComponent());

                    Entity e = world.createEntity();

                    for(Component c : bag){
                        e.edit().add(c);
                    }

                }

            }
        };
    }


}
