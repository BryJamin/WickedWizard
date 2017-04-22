package com.byrjamin.wickedwizard.factories.enemy;

import com.byrjamin.wickedwizard.ecs.components.BlinkComponent;
import com.byrjamin.wickedwizard.ecs.components.HealthComponent;
import com.byrjamin.wickedwizard.ecs.components.OnDeathComponent;
import com.byrjamin.wickedwizard.ecs.components.identifiers.EnemyComponent;
import com.byrjamin.wickedwizard.ecs.components.identifiers.LootComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.PositionComponent;
import com.byrjamin.wickedwizard.factories.DeathFactory;
import com.byrjamin.wickedwizard.factories.items.ItemFactory;
import com.byrjamin.wickedwizard.factories.items.pickups.MoneyPlus1;
import com.byrjamin.wickedwizard.utils.ComponentBag;

/**
 * Created by Home on 22/04/2017.
 */

public class EnemyFactory {



    public static ComponentBag defaultEnemyBag (ComponentBag fillbag, float x, float y, float width, float height, float health) {
        fillbag.add(new PositionComponent(x, y));
        fillbag.add(new LootComponent());
        fillbag.add(new HealthComponent(health));
        fillbag.add(new BlinkComponent());
        fillbag.add(new EnemyComponent());
        OnDeathComponent odc = new OnDeathComponent();
        fillbag.add(DeathFactory.basicOnDeathExplosion(odc, width, height, 0,0));
        return fillbag;

    }


}