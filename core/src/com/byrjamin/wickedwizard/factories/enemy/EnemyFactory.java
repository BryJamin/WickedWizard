package com.byrjamin.wickedwizard.factories.enemy;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.byrjamin.wickedwizard.ecs.components.BlinkComponent;
import com.byrjamin.wickedwizard.ecs.components.HealthComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.OnDeathActionComponent;
import com.byrjamin.wickedwizard.ecs.components.identifiers.EnemyComponent;
import com.byrjamin.wickedwizard.ecs.components.identifiers.LootComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.PositionComponent;
import com.byrjamin.wickedwizard.factories.AbstractFactory;
import com.byrjamin.wickedwizard.factories.DeathFactory;
import com.byrjamin.wickedwizard.factories.GibletFactory;
import com.byrjamin.wickedwizard.utils.ComponentBag;
import com.byrjamin.wickedwizard.utils.Measure;

/**
 * Created by Home on 22/04/2017.
 */

public class EnemyFactory extends AbstractFactory {

    GibletFactory gibletFactory;

    public EnemyFactory(AssetManager assetManager) {
        super(assetManager);
        this.gibletFactory = new GibletFactory(assetManager);
    }

    protected ComponentBag defaultEnemyBag (ComponentBag fillbag, float x, float y, float width, float height, float health) {
        fillbag.add(new PositionComponent(x, y));
        fillbag.add(new LootComponent());
        fillbag.add(new HealthComponent(health));
        fillbag.add(new BlinkComponent());
        fillbag.add(new EnemyComponent());
        fillbag.add(new OnDeathActionComponent(gibletFactory.giblets(5,0.4f,
                Measure.units(20f), Measure.units(100f), Measure.units(1f), new Color(Color.WHITE))));
        return fillbag;

    }


}
