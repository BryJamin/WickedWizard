package com.bryjamin.wickedwizard.factories.items.passives.luck;

import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.math.MathUtils;
import com.bryjamin.wickedwizard.ecs.components.StatComponent;
import com.bryjamin.wickedwizard.factories.items.Item;
import com.bryjamin.wickedwizard.factories.items.ItemLayout;
import com.bryjamin.wickedwizard.factories.items.ItemResource;

/**
 * Created by BB on 20/09/2017.
 */

public class ItemLuckyRoll implements Item {

    @Override
    public boolean applyEffect(World world, Entity player) {

        StatComponent statComponent = player.getComponent(StatComponent.class);
        float increase = 1.0f + (float) Math.floor((double) player.getComponent(StatComponent.class).luck / 2.0f);

        //cap
        if(increase > 5) increase = 5;

        increase = MathUtils.random.nextInt((int) increase) + 1;

        int i = MathUtils.random.nextInt(5);

        switch (i){
            case 0: statComponent.damage += increase; break;
            case 1: statComponent.fireRate += increase; break;
            case 2: statComponent.range += increase; break;
            case 3: statComponent.shotSpeed += increase; break;
            case 4: statComponent.accuracy += increase; break;
            case 5: statComponent.range += increase; break;
        }

        return true;
    }

    @Override
    public ItemLayout getValues() {
        return ItemResource.Luck.luckyRoll;
    }
}
