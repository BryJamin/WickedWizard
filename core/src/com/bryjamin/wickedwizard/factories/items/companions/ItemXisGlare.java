package com.bryjamin.wickedwizard.factories.items.companions;

import com.artemis.Entity;
import com.artemis.World;
import com.bryjamin.wickedwizard.assets.ColorResource;
import com.bryjamin.wickedwizard.ecs.components.AdditionalWeaponComponent;
import com.bryjamin.wickedwizard.ecs.components.Weapon;
import com.bryjamin.wickedwizard.ecs.components.WeaponComponent;
import com.bryjamin.wickedwizard.ecs.systems.graphical.RenderingSystem;
import com.bryjamin.wickedwizard.factories.items.Companion;
import com.bryjamin.wickedwizard.factories.items.ItemLayout;
import com.bryjamin.wickedwizard.factories.items.ItemResource;
import com.bryjamin.wickedwizard.factories.weapons.enemy.MultiPistol;
import com.bryjamin.wickedwizard.utils.Measure;

/**
 * Created by BB on 20/09/2017.
 */

public class ItemXisGlare implements Companion {


    @Override
    public boolean applyEffect(World world, Entity player) {

        final Weapon weapon = new MultiPistol.PistolBuilder(world.getSystem(RenderingSystem.class).assetManager)
                .damage(1f)
                .color(ColorResource.BLOB_GREEN)
                .shotScale(2.5f)
                .shotSpeed(Measure.units(150))
                .enemy(false)
                .friendly(true)
                .fireRate(2.0f)
                .build();

        player.getComponent(AdditionalWeaponComponent.class).add(new WeaponComponent(weapon, weapon.getBaseFireRate()));

        return true;
    }

    @Override
    public ItemLayout getValues() {
        return ItemResource.Companion.xisGlare;
    }

}
