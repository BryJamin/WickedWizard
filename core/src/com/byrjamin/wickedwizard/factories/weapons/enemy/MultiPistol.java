package com.byrjamin.wickedwizard.factories.weapons.enemy;

import com.artemis.Component;
import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector3;
import com.byrjamin.wickedwizard.assets.ColorResource;
import com.byrjamin.wickedwizard.ecs.components.CollisionBoundComponent;
import com.byrjamin.wickedwizard.ecs.components.Weapon;
import com.byrjamin.wickedwizard.ecs.components.ai.ExpiryRangeComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.OnDeathActionComponent;
import com.byrjamin.wickedwizard.ecs.components.identifiers.EnemyComponent;
import com.byrjamin.wickedwizard.ecs.components.identifiers.IntangibleComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.GravityComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.VelocityComponent;
import com.byrjamin.wickedwizard.factories.BulletFactory;
import com.byrjamin.wickedwizard.factories.GibletFactory;
import com.byrjamin.wickedwizard.utils.BagSearch;
import com.byrjamin.wickedwizard.utils.BulletMath;
import com.byrjamin.wickedwizard.utils.Measure;

/**
 * Created by Home on 16/06/2017.
 */

public class MultiPistol implements Weapon {


    private final AssetManager assetManager;

    private final BulletFactory bulletFactory;
    private final GibletFactory gibletFactory;

    private final float shotScale;
    private final float fireRate;
    private final float shotSpeed;
    private final float expireRange;

    private final boolean gravity;
    private final boolean expire;
    private final boolean intangible;
    private final boolean enemy;

    private final int[] angles;
    private final float[] bulletOffsets;

    private final Color color;

    private final OnDeathActionComponent customOnDeathAction;




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

        private float[] bulletOffsets = new float[]{0};

        private OnDeathActionComponent customOnDeathAction = null;

        private Color color = new Color(ColorResource.ENEMY_BULLET_COLOR);

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
        { expireRange = val; expire(true); return this; }

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

        public PistolBuilder bulletOffsets(float... val)
        { bulletOffsets = val; return this; }

        public PistolBuilder color(Color val)
        { color = val; return this; }

        public PistolBuilder customOnDeathAction(OnDeathActionComponent val)
        { customOnDeathAction = val; return this; }

        public MultiPistol build() {
            return new MultiPistol(this);
        }


    }

    public MultiPistol(PistolBuilder pb){

        this.assetManager = pb.assetManager;

        this.bulletFactory = new BulletFactory(assetManager);
        this.gibletFactory = new GibletFactory(assetManager);

        this.shotScale = pb.shotScale;
        this.fireRate = pb.fireRate;
        this.shotSpeed = pb.shotSpeed;

        this.expireRange = pb.expireRange;
        this.gravity = pb.gravity;
        this.expire = pb.expire;
        this.intangible = pb.intangible;
        this.enemy = pb.enemy;

        this.angles = pb.angles;
        this.bulletOffsets = pb.bulletOffsets;

        this.customOnDeathAction = pb.customOnDeathAction;

        this.color = pb.color;

    }

    @Override
    public void fire(World world, Entity e, float x, float y, double angleInRadians) {


        for (float f : bulletOffsets) {

            for (int i : angles) {
                Entity bullet = world.createEntity();
                double angleOfTravel = angleInRadians + Math.toRadians(i);

                System.out.println(angleInRadians);

                for (Component c : bulletFactory.basicBulletBag(
                        x + (BulletMath.velocityX(f, angleInRadians + (Math.PI / 2))),
                        y + (BulletMath.velocityY(f, angleInRadians + (Math.PI / 2))),
                        shotScale,
                        color)) {
                    bullet.edit().add(c);
                }

                bullet.edit().add(new VelocityComponent(BulletMath.velocityX(shotSpeed, angleOfTravel),
                        BulletMath.velocityY(shotSpeed, angleOfTravel)));

                if (enemy) bullet.edit().add(new EnemyComponent());

                if (gravity) bullet.edit().add(new GravityComponent());

                if (intangible) bullet.edit().add(new IntangibleComponent());

                if (expire) {

                    CollisionBoundComponent cbc = bullet.getComponent(CollisionBoundComponent.class);
                    bullet.edit().add(new ExpiryRangeComponent(
                            new Vector3(cbc.getCenterX(), cbc.getCenterY(), 0), expireRange));
                }

                if(customOnDeathAction == null) {
                    bullet.edit().add(new OnDeathActionComponent(gibletFactory.giblets(5, 0.2f, (int)
                            Measure.units(10f), (int) Measure.units(20f), Measure.units(0.5f), new Color(color))));
                } else {
                    bullet.edit().add(customOnDeathAction);
                }
                //bullet.edit().remove(CollisionBoundComponent.class);
            }
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
