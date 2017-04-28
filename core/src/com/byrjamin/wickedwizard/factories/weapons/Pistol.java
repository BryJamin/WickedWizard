package com.byrjamin.wickedwizard.factories.weapons;

import com.artemis.Component;
import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.assets.AssetManager;
import com.byrjamin.wickedwizard.ecs.components.identifiers.BulletComponent;
import com.byrjamin.wickedwizard.ecs.components.identifiers.FriendlyComponent;
import com.byrjamin.wickedwizard.ecs.components.StatComponent;
import com.byrjamin.wickedwizard.ecs.components.Weapon;
import com.byrjamin.wickedwizard.ecs.components.movement.VelocityComponent;
import com.byrjamin.wickedwizard.factories.BulletFactory;
import com.byrjamin.wickedwizard.utils.Measure;

/**
 * Created by Home on 15/04/2017.
 */

public class Pistol implements Weapon{

    private float baseDamage = 1;
    private float baseFireRate = 1;
    private BulletFactory bulletFactory;

    public Pistol(AssetManager assetManager) {
        bulletFactory = new BulletFactory(assetManager);
    }

    @Override
    public void fire(World world, Entity e, float x, float y, double angle) {

        Entity bullet = world.createEntity();
        for(Component c : bulletFactory.basicBulletBag(x,y,1)){
            bullet.edit().add(c);
        }
        bullet.edit().add(new FriendlyComponent());
        bullet.edit().add(new VelocityComponent((float) (Measure.units(150) * Math.cos(angle)), (float) (Measure.units(150) * Math.sin(angle))));

        if(world.getMapper(StatComponent.class).has(e)) {
            bullet.getComponent(BulletComponent.class).damage = baseDamage * e.getComponent(StatComponent.class).damage;
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
}
