package com.bryjamin.wickedwizard.factories.weapons.enemy;

import com.artemis.Component;
import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.assets.AssetManager;
import com.bryjamin.wickedwizard.ecs.components.Weapon;
import com.bryjamin.wickedwizard.factories.explosives.BombFactory;
import com.bryjamin.wickedwizard.utils.BulletMath;
import com.bryjamin.wickedwizard.utils.Measure;

/**
 * Created by Home on 02/07/2017.
 */

public class MinePistol implements Weapon {

    private AssetManager assetManager;

    private BombFactory bombFactory;

    private float fireRate;
    private float shotSpeedX;
    private float shotSpeedY;
    private float bombLife;

    private int[] angles;




    public static class PistolBuilder{

        //Required Parameters
        private final AssetManager assetManager;

        //Optional Parameters
        private float fireRate = 1.5f;
        private float shotSpeedX = Measure.units(50);
        private float shotSpeedY = Measure.units(50f);
        private float bombLife = 1f;

        private int[] angles = new int[]{0};

        public PistolBuilder(AssetManager assetManager) {
            this.assetManager = assetManager;
        }


        public PistolBuilder fireRate(float val)
        { fireRate = val; return this; }

        public PistolBuilder shotSpeed(float val)
        { shotSpeedX = val; shotSpeedY = val; return this; }

        public PistolBuilder shotSpeedX(float val)
        { shotSpeedX = val; return this; }

        public PistolBuilder shotSpeedY(float val)
        { shotSpeedY = val; return this; }

        public PistolBuilder bombLife(float val)
        { bombLife = val; return this; }

        public PistolBuilder angles(int... val)
        { angles = val; return this; }

        public MinePistol build() {
            return new MinePistol(this);
        }


    }

    public MinePistol(PistolBuilder pb){

        this.assetManager = pb.assetManager;
        this.bombFactory = new BombFactory(assetManager);
        this.fireRate = pb.fireRate;
        this.shotSpeedX = pb.shotSpeedX;
        this.shotSpeedY = pb.shotSpeedY;
        this.angles = pb.angles;
    }


    @Override
    public void fire(World world, Entity e, float x, float y, double angleInRadians) {
        for(int i : angles) {
            Entity bomb = world.createEntity();
            double angleOfTravel = angleInRadians + Math.toRadians(i);
            for (Component c : bombFactory.gravMine(x, y)) {
                bomb.edit().add(c);
            }

            bomb.edit().add(new com.bryjamin.wickedwizard.ecs.components.movement.VelocityComponent(BulletMath.velocityX(shotSpeedX, angleOfTravel),
                    BulletMath.velocityY(shotSpeedY, angleOfTravel)));
            //bullet.edit().remove(CollisionBoundComponent.class);
        }
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