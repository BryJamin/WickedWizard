package com.byrjamin.wickedwizard.factories.weapons.enemy;

import com.artemis.Component;
import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector3;
import com.byrjamin.wickedwizard.ecs.components.CollisionBoundComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.ExpiryRangeComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.OnDeathActionComponent;
import com.byrjamin.wickedwizard.ecs.components.identifiers.EnemyComponent;
import com.byrjamin.wickedwizard.ecs.components.identifiers.IntangibleComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.GravityComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.VelocityComponent;
import com.byrjamin.wickedwizard.factories.GibletFactory;
import com.byrjamin.wickedwizard.utils.BagSearch;
import com.byrjamin.wickedwizard.utils.BulletMath;
import com.byrjamin.wickedwizard.utils.Measure;

/**
 * Created by Home on 16/06/2017.
 */

public class MultiPistol extends Pistol{

    private float shotScale = 4;
    private float fireRate = 1.5f;
    private float shotSpeed = Measure.units(50);
    private float expireRange;

    private boolean gravity = false;
    private boolean expire = false;
    private boolean intangible = false;
    private boolean enemy = true;

    private int[] angles = new int[] {0,25,-25};

    private Color color = new Color(Color.RED);




    public static class PistolBuilder{

        //Required Parameters
        private final AssetManager assetManager;

        //Optional Parameters
        private float shotScale = 4;
        private float fireRate = 1.5f;
        private float shotSpeed = Measure.units(50);
        private float expireRange = Measure.units(100f);

        private boolean gravity = false;
        private boolean expire = false;
        private boolean intangible = false;
        private boolean enemy = true;

        private int[] angles = new int[]{0};

        private Color color = new Color(Color.RED);

        public PistolBuilder(AssetManager assetManager) {
            this.assetManager = assetManager;
        }

        public PistolBuilder shotScale(float val)
        { shotScale = val; return this; }

        public PistolBuilder fireRate(float val)
        { fireRate = val; return this; }

        public PistolBuilder shotSpeed(float val)
        { shotSpeed = val; return this; }

        public PistolBuilder expireRange(float val)
        { expireRange = val; return this; }

        public PistolBuilder gravity(boolean val)
        { gravity = val; return this; }

        public PistolBuilder intangible(boolean val)
        { intangible = val; return this; }

        public PistolBuilder expire(boolean val)
        { expire = val; return this; }

        public PistolBuilder enemy(boolean val)
        { enemy = val; return this; }

        public PistolBuilder angles(int... val)
        { angles = val; return this; }

        public PistolBuilder color(Color val)
        { color = val; return this; }

        public MultiPistol build() {
            return new MultiPistol(this);
        }


    }

    public MultiPistol(PistolBuilder pb){
        super(pb.assetManager, pb.fireRate);

        this.shotScale = pb.shotScale;
        this.fireRate = pb.fireRate;
        this.shotSpeed = pb.shotSpeed;

        this.expireRange = pb.expireRange;
        this.gravity = pb.gravity;
        this.expire = pb.expire;
        this.intangible = pb.intangible;
        this.enemy = pb.enemy;

        this.angles = pb.angles;

        this.color = pb.color;

    }


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
        this.shotScale = scale;
    }

    @Override
    public void fire(World world, Entity e, float x, float y, double angleInRadians) {
        for(int i : angles) {
            Entity bullet = world.createEntity();
            double angleOfTravel = angleInRadians + Math.toRadians(i);
            for (Component c : bulletFactory.basicBulletBag(x, y, shotScale, color)) {
                bullet.edit().add(c);

                bullet.edit().add(new VelocityComponent(BulletMath.velocityX(shotSpeed, angleOfTravel),
                        BulletMath.velocityY(shotSpeed, angleOfTravel)));

                if(enemy) bullet.edit().add(new EnemyComponent());

                if(gravity) bullet.edit().add(new GravityComponent());

                if(intangible) bullet.edit().add(new IntangibleComponent());

                if(expire) {

                    CollisionBoundComponent cbc = bullet.getComponent(CollisionBoundComponent.class);
                    bullet.edit().add(new ExpiryRangeComponent(
                            new Vector3(cbc.getCenterX(), cbc.getCenterY(), 0), expireRange));
                }


                bullet.edit().add(new OnDeathActionComponent(gibletFactory.giblets(5, 0.2f, (int)
                        Measure.units(10f), (int) Measure.units(20f),Measure.units(0.5f), new Color(Color.RED))));
                //bullet.edit().remove(CollisionBoundComponent.class);
            }
        }
    }

}
