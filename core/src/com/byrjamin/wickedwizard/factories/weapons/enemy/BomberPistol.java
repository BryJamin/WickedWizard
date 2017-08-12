package com.byrjamin.wickedwizard.factories.weapons.enemy;

import com.artemis.Component;
import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.assets.AssetManager;
import com.byrjamin.wickedwizard.ecs.components.Weapon;
import com.byrjamin.wickedwizard.ecs.components.movement.VelocityComponent;
import com.byrjamin.wickedwizard.factories.BombFactory;
import com.byrjamin.wickedwizard.utils.BulletMath;
import com.byrjamin.wickedwizard.utils.Measure;

/**
 * Created by Home on 02/07/2017.
 */

public class BomberPistol implements Weapon{

    private AssetManager assetManager;

    private BombFactory bombFactory;

    private float shotScale = 4;
    private float fireRate;
    private float shotSpeedX;
    private float shotSpeedY;
    private float bombLife;

    private int[] angles;




    public static class PistolBuilder{

        //Required Parameters
        private final AssetManager assetManager;

        //Optional Parameters
        private float shotScale = 4;
        private float fireRate = 1.5f;
        private float shotSpeedX = Measure.units(50);
        private float shotSpeedY = Measure.units(50f);
        private float bombLife = 1f;

        private int[] angles = new int[]{0};

        public PistolBuilder(AssetManager assetManager) {
            this.assetManager = assetManager;
        }

        public PistolBuilder shotScale(float val)
        { shotScale = val; return this; }

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

        public BomberPistol build() {
            return new BomberPistol(this);
        }


    }

    public BomberPistol(PistolBuilder pb){

        this.assetManager = pb.assetManager;
        this.bombFactory = new BombFactory(assetManager);

        this.shotScale = pb.shotScale;
        this.fireRate = pb.fireRate;
        this.shotSpeedX = pb.shotSpeedX;
        this.shotSpeedY = pb.shotSpeedY;

        this.bombLife = pb.bombLife;


        this.angles = pb.angles;

    }

    public void setAngles(int[] angles) {
        this.angles = angles;
    }

    public void setScale(float scale) {
        this.shotScale = scale;
    }



    @Override
    public void fire(World world, Entity e, float x, float y, double angleInRadians) {
        for(int i : angles) {
            Entity bomb = world.createEntity();
            double angleOfTravel = angleInRadians + Math.toRadians(i);
            for (Component c : bombFactory.bomb(x, y, bombLife)) {
                bomb.edit().add(c);
            }

            bomb.edit().add(new VelocityComponent(BulletMath.velocityX(shotSpeedX, angleOfTravel),
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
