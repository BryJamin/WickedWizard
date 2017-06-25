package com.byrjamin.wickedwizard.factories.weapons.enemy;

import com.artemis.Component;
import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.assets.AssetManager;
import com.byrjamin.wickedwizard.ecs.components.Weapon;
import com.byrjamin.wickedwizard.factories.BulletFactory;
import com.byrjamin.wickedwizard.factories.GibletFactory;
import com.byrjamin.wickedwizard.utils.ComponentBag;

/**
 * Created by Home on 23/04/2017.
 */

public class Pistol implements Weapon {


    private float fireRate;
    protected BulletFactory bulletFactory;
    protected GibletFactory gibletFactory;

    public Pistol(AssetManager assetManager, float fireRate){
        this.fireRate = fireRate;
        bulletFactory = new BulletFactory(assetManager);
        gibletFactory = new GibletFactory(assetManager);
    }

    @Override
    public void fire(World world, Entity e, float x, float y, double angleInRadians) {
        Entity bullet = world.createEntity();
        for(Component c : bulletFactory.enemyBulletBag(new ComponentBag(), x, y, angleInRadians)) bullet.edit().add(c);
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
