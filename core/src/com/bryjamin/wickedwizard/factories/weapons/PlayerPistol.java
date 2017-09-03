package com.bryjamin.wickedwizard.factories.weapons;

import com.artemis.Component;
import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector3;
import com.bryjamin.wickedwizard.assets.SoundFileStrings;
import com.bryjamin.wickedwizard.ecs.components.StatComponent;
import com.bryjamin.wickedwizard.ecs.components.Weapon;
import com.bryjamin.wickedwizard.ecs.components.ai.ExpiryRangeComponent;
import com.bryjamin.wickedwizard.ecs.components.ai.OnDeathActionComponent;
import com.bryjamin.wickedwizard.ecs.components.identifiers.BulletComponent;
import com.bryjamin.wickedwizard.ecs.components.identifiers.FriendlyComponent;
import com.bryjamin.wickedwizard.ecs.systems.audio.SoundSystem;
import com.bryjamin.wickedwizard.factories.BulletFactory;
import com.bryjamin.wickedwizard.utils.Measure;

/**
 * Created by Home on 15/04/2017.
 */

public class PlayerPistol implements Weapon{

    private float baseDamage = 1;

    private static final float defaultFireRate = 0.3f;

    private BulletFactory bulletFactory;
    private com.bryjamin.wickedwizard.factories.weapons.Giblets.GibletBuilder gibletBuilder;

    private static final float shotSpeedMultiplier = 2.5f;

    private static final float range = Measure.units(50f);


    private static final float defaultShotScale = 2;
    private static final float shotScaleMultiplier = 0.5f;

    private com.bryjamin.wickedwizard.ecs.components.StatComponent playerStats;


    public PlayerPistol(AssetManager assetManager, com.bryjamin.wickedwizard.ecs.components.StatComponent playerStats) {
        bulletFactory = new BulletFactory(assetManager);
        gibletBuilder = new com.bryjamin.wickedwizard.factories.weapons.Giblets.GibletBuilder(assetManager);
        this.playerStats = playerStats;
    }


    @Override
    public void fire(World world, Entity e, float x, float y, double angleInRadians) {

        boolean isCrit = com.bryjamin.wickedwizard.factories.weapons.CritCalculator.isCrit(playerStats.crit, playerStats.accuracy, playerStats.luck);

        Entity bullet = world.createEntity();

        float shotscale = defaultShotScale + (playerStats.shotSize * shotScaleMultiplier);
        if(shotscale >= 4.5f) shotscale = 4.5f;
        if(shotscale <= 1f) shotscale = 1f;

        for(Component c : bulletFactory.basicBulletBag(x,y, shotscale, isCrit ? new Color(0,0,0,1) : new Color(1,1,1,1))){
            bullet.edit().add(c);
        }
        bullet.edit().add(new FriendlyComponent());
        bullet.edit().add(new com.bryjamin.wickedwizard.ecs.components.movement.VelocityComponent(
                (float) (Measure.units(100 + (playerStats.shotSpeed * shotSpeedMultiplier)) * Math.cos(angleInRadians)),
                (float) (Measure.units(100 + (playerStats.shotSpeed * shotSpeedMultiplier)) * Math.sin(angleInRadians))));

        bullet.edit().add(new ExpiryRangeComponent(new Vector3(x,y,0),
                range + (playerStats.range * Measure.units(5f))));

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
                    baseDamage * (1 + e.getComponent(StatComponent.class).damage * 0.1f) :
                    baseDamage * ((1 + (e.getComponent(StatComponent.class).damage * 0.1f)) * 2f);  //crit multiplier
           // System.out.println("Bullet damage" + bullet.getComponent(BulletComponent.class).damage);
        }


        world.getSystem(SoundSystem.class).playRandomSound(SoundFileStrings.playerFireMegaMix);
        //world.getSystem(FindPlayerSystem.class)
    }

    @Override
    public float getBaseFireRate() {
        return com.bryjamin.wickedwizard.factories.weapons.CritCalculator.calculateFireRate(defaultFireRate, playerStats.fireRate);
    }

    @Override
    public float getBaseDamage() {
        return baseDamage;
    }


    public float getRange() {
        return range;
    }

}
