package com.byrjamin.wickedwizard.factories.weapons.enemy;

import com.artemis.Component;
import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.assets.AssetManager;
import com.byrjamin.wickedwizard.ecs.components.CollisionBoundComponent;
import com.byrjamin.wickedwizard.utils.ComponentBag;

/**
 * Created by Home on 16/06/2017.
 */

public class MultiPistol extends Pistol{

    private int[] angles = new int[] {0,25,-25};
    private float scale = 4;

    public MultiPistol(AssetManager assetManager, float fireRate) {
        super(assetManager, fireRate);
    }

    public MultiPistol(AssetManager assetManager, float fireRate, int... angles) {
        super(assetManager, fireRate);
        setAngles(angles);
    }


    public void setAngles(int[] angles) {
        this.angles = angles;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    @Override
    public void fire(World world, Entity e, float x, float y, double angleInRadians) {
        for(int i : angles) {
            Entity bullet = world.createEntity();
            double angleOfTravel = angleInRadians + Math.toRadians(i);
            for (Component c : bf.enemyBulletBag(new ComponentBag(), x, y,scale, angleOfTravel)) {
                bullet.edit().add(c);
                //bullet.edit().remove(CollisionBoundComponent.class);
            }
        }
    }

}
