package com.byrjamin.wickedwizard.factories.weapons;

import com.artemis.Component;
import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector3;
import com.byrjamin.wickedwizard.assets.SoundFileStrings;
import com.byrjamin.wickedwizard.ecs.components.ai.ExpiryRangeComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.OnDeathActionComponent;
import com.byrjamin.wickedwizard.ecs.components.identifiers.BulletComponent;
import com.byrjamin.wickedwizard.ecs.components.identifiers.FriendlyComponent;
import com.byrjamin.wickedwizard.ecs.components.StatComponent;
import com.byrjamin.wickedwizard.ecs.components.Weapon;
import com.byrjamin.wickedwizard.ecs.components.movement.VelocityComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.TextureRegionComponent;
import com.byrjamin.wickedwizard.ecs.systems.audio.SoundSystem;
import com.byrjamin.wickedwizard.factories.BulletFactory;
import com.byrjamin.wickedwizard.utils.Measure;

import java.util.Random;

/**
 * Created by Home on 15/04/2017.
 */

public class PlayerPistol implements Weapon{

    private float baseDamage = 0.1f;

    private static final float defaultFireRate = 0.3f;

    private float baseFireRate = 0.3f;
    private BulletFactory bulletFactory;
    private Giblets.GibletBuilder gibletBuilder;
    private CritCalculator critCalculator;

    private static final float shotSpeedMultiplier = 2.5f;

    private static final float range = Measure.units(50f);


    public PlayerPistol(AssetManager assetManager) {
        bulletFactory = new BulletFactory(assetManager);
        gibletBuilder = new Giblets.GibletBuilder(assetManager);
        critCalculator = new CritCalculator(new Random());
    }


    //public void applyWeaponConfig()

    @Override
    public void fire(World world, Entity e, float x, float y, double angleInRadians) {



        if(e.getComponent(StatComponent.class) == null) return;

        StatComponent sc =  e.getComponent(StatComponent.class);

        boolean isCrit = critCalculator.isCrit(sc.crit, sc.accuracy, sc.luck);

        baseFireRate = defaultFireRate / (1 + (sc.fireRate / 10));

        Entity bullet = world.createEntity();
        for(Component c : bulletFactory.basicBulletBag(x,y,2, isCrit ? new Color(0,0,0,1) : new Color(1,1,1,1))){
            bullet.edit().add(c);
        }
        bullet.edit().add(new FriendlyComponent());
        bullet.edit().add(new VelocityComponent(
                (float) (Measure.units(100 + (sc.shotSpeed * shotSpeedMultiplier)) * Math.cos(angleInRadians)),
                (float) (Measure.units(100 + (sc.shotSpeed * shotSpeedMultiplier)) * Math.sin(angleInRadians))));

        bullet.edit().add(new ExpiryRangeComponent(new Vector3(x,y,0),
                range + (sc.range * Measure.units(5f))));

        if(isCrit) {
            bullet.edit().add(new OnDeathActionComponent(gibletBuilder
                    .numberOfGibletPairs(5)
                    .expiryTime(0.4f)
                    .maxSpeed(Measure.units(40f))
                    .mixes(SoundFileStrings.queitExplosionMegaMix)
                    .size(Measure.units(0.5f))
                    .intangible(false)
                    .colors(new Color(Color.BLACK), new Color(Color.DARK_GRAY), new Color(Color.WHITE))
                    .build()));
        } else {
            bullet.edit().add(new OnDeathActionComponent(gibletBuilder
                    .numberOfGibletPairs(3)
                    .expiryTime(0.2f)
                    .maxSpeed(Measure.units(20f))
                    .size(Measure.units(0.5f))
                    .mixes(SoundFileStrings.queitExplosionMegaMix)
                    .intangible(false)
                    .colors(new Color(Color.WHITE))
                    .build()));
        }
        if(world.getMapper(StatComponent.class).has(e)) {
            bullet.getComponent(BulletComponent.class).damage = (!isCrit) ?
                    baseDamage * (1 + e.getComponent(StatComponent.class).damage) :
                    (baseDamage * ((1 + (e.getComponent(StatComponent.class).damage)) * 2f));  //crit multiplier
            System.out.println("Bullet damage" + bullet.getComponent(BulletComponent.class).damage);
        }


        world.getSystem(SoundSystem.class).playSound(SoundFileStrings.playerFireMix);
        //world.getSystem(FindPlayerSystem.class)
    }

    @Override
    public float getBaseFireRate() {
        return baseFireRate;
    }

    @Override
    public float getBaseDamage() {
        return baseDamage;
    }


    public float getRange() {
        return range;
    }

}
