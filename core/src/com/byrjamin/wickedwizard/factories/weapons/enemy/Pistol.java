package com.byrjamin.wickedwizard.factories.weapons.enemy;

import com.artemis.Aspect;
import com.artemis.Component;
import com.artemis.Entity;
import com.artemis.World;
import com.byrjamin.wickedwizard.ecs.components.Weapon;
import com.byrjamin.wickedwizard.factories.BulletFactory;
import com.byrjamin.wickedwizard.utils.ComponentBag;

/**
 * Created by Home on 23/04/2017.
 */

public class Pistol implements Weapon {


    private float fireRate;

    public Pistol(float fireRate){
        this.fireRate = fireRate;
    }

    @Override
    public void fire(World world, Entity e, float x, float y, double angle) {

        System.out.println("Let's fire now shall me");

        System.out.println("x is " + x);
        System.out.println("y us " + y);

        System.out.println("angle is " + angle);

        System.out.println(world.getAspectSubscriptionManager().get(Aspect.all()).getEntities().size());

        Entity bullet = world.createEntity();

        for(Component c : BulletFactory.enemyBulletBag(new ComponentBag(), x, y, angle)) bullet.edit().add(c);


        System.out.println(world.getAspectSubscriptionManager().get(Aspect.all()).getEntities().size());
    }

    @Override
    public float getBaseFireRate() {
        return fireRate;
    }

    @Override
    public float getBaseDamage() {
        return 0;
    }
}
