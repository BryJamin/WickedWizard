package com.byrjamin.wickedwizard.factories.weapons.enemy;

import com.artemis.Component;
import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector3;
import com.byrjamin.wickedwizard.assets.ColorResource;
import com.byrjamin.wickedwizard.assets.SoundFileStrings;
import com.byrjamin.wickedwizard.ecs.components.identifiers.BulletComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.CollisionBoundComponent;
import com.byrjamin.wickedwizard.ecs.components.Weapon;
import com.byrjamin.wickedwizard.ecs.components.ai.ExpiryRangeComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.OnDeathActionComponent;
import com.byrjamin.wickedwizard.ecs.components.identifiers.EnemyComponent;
import com.byrjamin.wickedwizard.ecs.components.identifiers.FriendlyComponent;
import com.byrjamin.wickedwizard.ecs.components.identifiers.IntangibleComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.GravityComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.VelocityComponent;
import com.byrjamin.wickedwizard.ecs.systems.audio.SoundSystem;
import com.byrjamin.wickedwizard.factories.BulletFactory;
import com.byrjamin.wickedwizard.factories.weapons.Giblets;
import com.byrjamin.wickedwizard.utils.BulletMath;
import com.byrjamin.wickedwizard.utils.Measure;

/**
 * Created by Home on 16/06/2017.
 */

public class MultiPistol implements Weapon {


    private final AssetManager assetManager;

    private final BulletFactory bulletFactory;

    private final float shotScale;
    private final float fireRate;
    private final float shotSpeed;
    private final float expireRange;
    private final float damage;


    private final boolean gravity;
    private final boolean expire;
    private final boolean intangible;
    private final boolean enemy;
    private final boolean friendly;

    private final int[] angles;
    private final float[] bulletOffsets;

    private final Color color;

    private final OnDeathActionComponent customOnDeathAction;


    private final Giblets giblets;

    private static final float gibletScaleModifier = 0.125f;



    public static class PistolBuilder{

        //Required Parameters
        private final AssetManager assetManager;

        //Optional Parameters
        private float shotScale = 4;
        private float fireRate = 1.5f;
        private float shotSpeed = Measure.units(50);
        private float expireRange = Measure.units(100f);
        private float damage = 1f;

        private boolean gravity = false;
        private boolean expire = false;
        private boolean intangible = false;
        private boolean enemy = true;
        private boolean friendly = false;

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

        public PistolBuilder damage(float val)
        { damage = val; expire(true); return this; }

        public PistolBuilder gravity(boolean val)
        { gravity = val; return this; }

        public PistolBuilder intangible(boolean val)
        { intangible = val; return this; }

        public PistolBuilder expire(boolean val)
        { expire = val; return this; }

        public PistolBuilder enemy(boolean val)
        { enemy = val; return this; }

        public PistolBuilder friendly(boolean val)
        { friendly = val; return this; }

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


        this.shotScale = pb.shotScale;
        this.fireRate = pb.fireRate;
        this.shotSpeed = pb.shotSpeed;
        this.damage = pb.damage;

        this.expireRange = pb.expireRange;
        this.gravity = pb.gravity;
        this.expire = pb.expire;
        this.intangible = pb.intangible;
        this.enemy = pb.enemy;
        this.friendly = pb.friendly;

        this.angles = pb.angles;
        this.bulletOffsets = pb.bulletOffsets;

        this.customOnDeathAction = pb.customOnDeathAction;

        this.color = pb.color;


        this.giblets = new Giblets.GibletBuilder(assetManager)
                .numberOfGibletPairs(3)
                .fadeChance(0.0f)
                .size(Measure.units(gibletScaleModifier * shotScale)) //Was just a flat 0.5f, but this is an experiment
                .minSpeed(Measure.units(10f))
                .maxSpeed(Measure.units(20f))
                //.mixes(SoundFileStrings.queitExplosionMegaMix)
                .colors(color)
                .intangible(false)
                .expiryTime(0.2f)
                .build();


    }

    @Override
    public void fire(World world, Entity e, float x, float y, double angleInRadians) {


        for (float f : bulletOffsets) {

            for (int i : angles) {
                Entity bullet = world.createEntity();
                double angleOfTravel = angleInRadians + Math.toRadians(i);

                for (Component c : bulletFactory.basicBulletBag(
                        x + (BulletMath.velocityX(f, angleInRadians + (Math.PI / 2))),
                        y + (BulletMath.velocityY(f, angleInRadians + (Math.PI / 2))),
                        shotScale,
                        color)) {
                    bullet.edit().add(c);
                }

                bullet.getComponent(BulletComponent.class).damage = this.damage;

                bullet.edit().add(new VelocityComponent(BulletMath.velocityX(shotSpeed, angleOfTravel),
                        BulletMath.velocityY(shotSpeed, angleOfTravel)));

                if (enemy) bullet.edit().add(new EnemyComponent());

                if (friendly) bullet.edit().add(new FriendlyComponent());

                if (gravity) bullet.edit().add(new GravityComponent());

                if (intangible) bullet.edit().add(new IntangibleComponent());

                if (expire) {

                    CollisionBoundComponent cbc = bullet.getComponent(CollisionBoundComponent.class);
                    bullet.edit().add(new ExpiryRangeComponent(
                            new Vector3(cbc.getCenterX(), cbc.getCenterY(), 0), expireRange));
                }



                if(customOnDeathAction == null) {
                    bullet.edit().add(new OnDeathActionComponent(giblets));
                } else {
                    bullet.edit().add(customOnDeathAction);
                }
            }


        }


        world.getSystem(SoundSystem.class).playSound(SoundFileStrings.enemyFireMix);


    }

    @Override
    public float getBaseFireRate() {
        return fireRate;
    }

    @Override
    public float getBaseDamage() {
        return damage;
    }

}
