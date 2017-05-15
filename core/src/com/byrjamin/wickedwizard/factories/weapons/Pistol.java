package com.byrjamin.wickedwizard.factories.weapons;

import com.artemis.Component;
import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector3;
import com.byrjamin.wickedwizard.ecs.components.OnDeathComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.ExpiryRangeComponent;
import com.byrjamin.wickedwizard.ecs.components.identifiers.BulletComponent;
import com.byrjamin.wickedwizard.ecs.components.identifiers.FriendlyComponent;
import com.byrjamin.wickedwizard.ecs.components.StatComponent;
import com.byrjamin.wickedwizard.ecs.components.Weapon;
import com.byrjamin.wickedwizard.ecs.components.movement.VelocityComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.TextureRegionComponent;
import com.byrjamin.wickedwizard.factories.BulletFactory;
import com.byrjamin.wickedwizard.factories.GibletFactory;
import com.byrjamin.wickedwizard.utils.Measure;

import java.util.Random;

/**
 * Created by Home on 15/04/2017.
 */

public class Pistol implements Weapon{

    private float baseDamage = 1;
    private float baseFireRate = 1;
    private BulletFactory bulletFactory;
    private GibletFactory gibletFactory;
    private CritCalculator critCalculator;

    public Pistol(AssetManager assetManager) {
        bulletFactory = new BulletFactory(assetManager);
        gibletFactory = new GibletFactory(assetManager);
        critCalculator = new CritCalculator(new Random());
    }

    @Override
    public void fire(World world, Entity e, float x, float y, double angle) {

        if(e.getComponent(StatComponent.class) == null) return;

        StatComponent sc =  e.getComponent(StatComponent.class);
        boolean isCrit = critCalculator.isCrit(sc.crit, sc.accuracy, sc.luck);

        Entity bullet = world.createEntity();
        for(Component c : bulletFactory.basicBulletBag(x,y,1)){
            bullet.edit().add(c);
        }
        bullet.edit().add(new FriendlyComponent());
        bullet.edit().add(new VelocityComponent((float) (Measure.units(100) * Math.cos(angle)), (float) (Measure.units(100) * Math.sin(angle))));

        bullet.edit().add(new ExpiryRangeComponent(new Vector3(x,y,0),
                getRange() + (sc.range * Measure.units(5f))));

        if(isCrit) bullet.getComponent(TextureRegionComponent.class).color.set(0,0,0,1);

        if(isCrit) {
            gibletFactory.giblets(bullet.getComponent(OnDeathComponent.class), 10,0.4f, (int) Measure.units(20f), (int) Measure.units(40f),Measure.units(0.5f), new Color(0, 0, 0, 1));
        } else {
            gibletFactory.giblets(bullet.getComponent(OnDeathComponent.class), 5, 0.2f, (int) Measure.units(10f), (int) Measure.units(20f),Measure.units(0.5f), new Color(1, 1, 1, 1));
        }
        if(world.getMapper(StatComponent.class).has(e)) {
            bullet.getComponent(BulletComponent.class).damage = (!isCrit) ?
                    baseDamage * e.getComponent(StatComponent.class).damage :
                    baseDamage * e.getComponent(StatComponent.class).damage * 1.25f; //crit multiplier
        }
        //world.getSystem(FindPlayerSystem.class)
    }

    @Override
    public float getBaseFireRate() {
        return 0.3f;
    }

    @Override
    public float getBaseDamage() {
        return 1;
    }


    public float getRange() {
        return Measure.units(50f);
    }

}
