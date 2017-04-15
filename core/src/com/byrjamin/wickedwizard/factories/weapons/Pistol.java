package com.byrjamin.wickedwizard.factories.weapons;

import com.artemis.Component;
import com.artemis.Entity;
import com.artemis.World;
import com.byrjamin.wickedwizard.ecs.components.FriendlyComponent;
import com.byrjamin.wickedwizard.ecs.components.Weapon;
import com.byrjamin.wickedwizard.ecs.components.movement.VelocityComponent;
import com.byrjamin.wickedwizard.ecs.systems.FindPlayerSystem;
import com.byrjamin.wickedwizard.screens.PlayScreen;
import com.byrjamin.wickedwizard.utils.Measure;

import static com.byrjamin.wickedwizard.factories.BulletFactory.basicBulletBag;

/**
 * Created by Home on 15/04/2017.
 */

public class Pistol implements Weapon{

    private float baseDamage = 1;
    private float baseFireRate = 1;


    @Override
    public void fire(World world, float x, float y, double angle) {

        Entity e = world.createEntity();
        for(Component c : basicBulletBag(x,y,1, PlayScreen.atlas.findRegion("bullet"))){
            e.edit().add(c);
        }
        e.edit().add(new FriendlyComponent());
        e.edit().add(new VelocityComponent((float) (Measure.units(90) * Math.cos(angle)), (float) (Measure.units(90) * Math.sin(angle))));

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
