package com.byrjamin.wickedwizard.factories.weapons.enemy;

import com.artemis.Aspect;
import com.artemis.Component;
import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.assets.AssetManager;
import com.byrjamin.wickedwizard.ecs.components.Weapon;
import com.byrjamin.wickedwizard.ecs.components.texture.AnimationStateComponent;
import com.byrjamin.wickedwizard.factories.BulletFactory;
import com.byrjamin.wickedwizard.utils.ComponentBag;

/**
 * Created by Home on 23/04/2017.
 */

public class Pistol implements Weapon {


    private float fireRate;
    private BulletFactory bf;

    public Pistol(AssetManager assetManager, float fireRate){
        this.fireRate = fireRate;
        bf = new BulletFactory(assetManager);
    }

    @Override
    public void fire(World world, Entity e, float x, float y, double angle) {
        Entity bullet = world.createEntity();
        for(Component c : bf.enemyBulletBag(new ComponentBag(), x, y, angle)) bullet.edit().add(c);
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
