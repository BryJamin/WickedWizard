package com.byrjamin.wickedwizard.factories.items.passives.firerate;

import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.byrjamin.wickedwizard.ecs.components.StatComponent;
import com.byrjamin.wickedwizard.factories.items.AbstractItem;
import com.byrjamin.wickedwizard.factories.items.Item;
import com.byrjamin.wickedwizard.factories.items.passives.PresetStatIncrease;
import com.byrjamin.wickedwizard.screens.PlayScreen;
import com.byrjamin.wickedwizard.utils.Pair;

/**
 * Created by Home on 16/04/2017.
 */

public class ItemSwiftShot extends AbstractItem {


    @Override
    public boolean applyEffect(World world, Entity player) {
        player.getComponent(StatComponent.class).fireRate  += PresetStatIncrease.major;
        return true;
    }

    @Override
    public Pair<String, Integer> getRegionName() {
        return new Pair<String, Integer>("item/SwiftShot", 0);
    }

    @Override
    public String getName() {
        return "Swift Shot";
    }

    @Override
    public String getDescription() {
        return "FireRate++";
    }
}
