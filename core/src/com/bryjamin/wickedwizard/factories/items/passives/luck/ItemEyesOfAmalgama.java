package com.bryjamin.wickedwizard.factories.items.passives.luck;

import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.math.MathUtils;
import com.bryjamin.wickedwizard.ecs.components.StatComponent;
import com.bryjamin.wickedwizard.factories.items.Item;
import com.bryjamin.wickedwizard.factories.items.ItemLayout;
import com.bryjamin.wickedwizard.factories.items.ItemResource;
import com.bryjamin.wickedwizard.factories.items.passives.PresetStatIncrease;


/**
 * Created by BB on 27/08/2017.
 */

public class ItemEyesOfAmalgama implements Item {


    @Override
    public boolean applyEffect(World world, Entity player) {
        player.getComponent(StatComponent.class).luck += PresetStatIncrease.major;
        player.getComponent(StatComponent.class).shotSpeed += MathUtils.random.nextBoolean() ? 0 : PresetStatIncrease.minor;
        player.getComponent(StatComponent.class).damage += MathUtils.random.nextBoolean() ? 0 : PresetStatIncrease.minor;
        player.getComponent(StatComponent.class).fireRate += MathUtils.random.nextBoolean() ? 0 : PresetStatIncrease.minor;
        player.getComponent(StatComponent.class).accuracy += MathUtils.random.nextBoolean() ? 0 : PresetStatIncrease.minor;
        player.getComponent(StatComponent.class).range += MathUtils.random.nextBoolean() ? 0 : PresetStatIncrease.minor;
        return true;
    }

    @Override
    public ItemLayout getValues() {
        return ItemResource.Luck.eyesOfAmalgama;
    }


}
