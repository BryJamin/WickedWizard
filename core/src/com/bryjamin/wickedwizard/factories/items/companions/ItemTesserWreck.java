package com.bryjamin.wickedwizard.factories.items.companions;

import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.graphics.Color;
import com.bryjamin.wickedwizard.assets.ColorResource;
import com.bryjamin.wickedwizard.assets.SoundFileStrings;
import com.bryjamin.wickedwizard.ecs.components.AdditionalWeaponComponent;
import com.bryjamin.wickedwizard.ecs.components.Weapon;
import com.bryjamin.wickedwizard.ecs.components.WeaponComponent;
import com.bryjamin.wickedwizard.ecs.components.ai.OnDeathActionComponent;
import com.bryjamin.wickedwizard.ecs.components.texture.ColorChangeComponent;
import com.bryjamin.wickedwizard.ecs.systems.graphical.RenderingSystem;
import com.bryjamin.wickedwizard.factories.items.Companion;
import com.bryjamin.wickedwizard.factories.items.ItemLayout;
import com.bryjamin.wickedwizard.factories.items.ItemResource;
import com.bryjamin.wickedwizard.factories.weapons.enemy.MultiPistol;
import com.bryjamin.wickedwizard.utils.Measure;

/**
 * Created by BB on 20/09/2017.
 */

public class ItemTesserWreck implements Companion {


    @Override
    public boolean applyEffect(World world, Entity player) {


        final Weapon weapon = new MultiPistol.PistolBuilder(world.getSystem(RenderingSystem.class).assetManager)
                .damage(0.1f)
                .color(new Color(ColorResource.ENEMY_BULLET_COLOR))
                .mixes(SoundFileStrings.wipeFireMegaMix)
                .colorChangeComponent(new ColorChangeComponent(new Color(ColorResource.ENEMY_BULLET_COLOR), new Color(1,1,1,1), 0.05f, true))
                .shotScale(1.0f)
                .shotSpeed(Measure.units(150))
                .expireRange(Measure.units(30f))
                .customOnDeathAction(new OnDeathActionComponent())
                .enemy(false)
                .friendly(true)
                .fireRate(0f)
                .build();

        player.getComponent(AdditionalWeaponComponent.class).add(new WeaponComponent(weapon, weapon.getBaseFireRate()));

        return true;
    }

    @Override
    public ItemLayout getValues() {
        return ItemResource.Companion.tesserWreck;
    }

}