package com.bryjamin.wickedwizard.factories.enemy;

import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.assets.AssetManager;
import com.bryjamin.wickedwizard.assets.SoundFileStrings;
import com.bryjamin.wickedwizard.ecs.components.ai.Action;
import com.bryjamin.wickedwizard.ecs.components.ai.OnDeathActionComponent;
import com.bryjamin.wickedwizard.ecs.components.identifiers.EnemyComponent;
import com.bryjamin.wickedwizard.ecs.components.identifiers.LootComponent;
import com.bryjamin.wickedwizard.ecs.components.movement.PositionComponent;
import com.bryjamin.wickedwizard.ecs.components.texture.BlinkOnHitComponent;
import com.bryjamin.wickedwizard.factories.AbstractFactory;
import com.bryjamin.wickedwizard.factories.weapons.Giblets;
import com.bryjamin.wickedwizard.utils.ComponentBag;
import com.bryjamin.wickedwizard.utils.Measure;

/**
 * Created by Home on 22/04/2017.
 */

public class EnemyFactory extends AbstractFactory {

    protected boolean loudDeathSound = false;

    public EnemyFactory(AssetManager assetManager) {
        super(assetManager);
    }

    protected ComponentBag defaultEnemyBag (ComponentBag fillbag, float x, float y, float health) {
        return enemyBag(fillbag, x, y, health, true, true);
    }

    protected ComponentBag defaultEnemyBagNoLootNoDeath (ComponentBag fillbag, float x, float y, float health) {
        return enemyBag(fillbag, x, y, health, false, false);
    }

    public ComponentBag defaultEnemyBagNoLoot (ComponentBag fillbag, float x, float y, float health) {
        return enemyBag(fillbag, x, y, health, false, true);
    }


    private ComponentBag enemyBag(ComponentBag fillbag, float x, float y, float health, boolean loot, boolean deathAction){
        fillbag.add(new PositionComponent(x, y));
        fillbag.add(new com.bryjamin.wickedwizard.ecs.components.HealthComponent(health));
        fillbag.add(new BlinkOnHitComponent());
        fillbag.add(new EnemyComponent());
        if(loot) fillbag.add(new LootComponent());
        if(deathAction)  fillbag.add(new OnDeathActionComponent(defaultDeathAction()));
        return fillbag;
    }



    protected Action defaultDeathAction(){

        return new Action() {
            @Override
            public void performAction(World world, Entity e) {

                new Giblets.GibletBuilder(assetManager)
                        .intangible(false)
                        .minSpeed(Measure.units(10f))
                        .maxSpeed(Measure.units(50f))
                        .expiryTime(0.6f)
                        .fadeChance(0.75f)
                        .intangible(true)
                        .mixes(loudDeathSound ? SoundFileStrings.bigExplosionMegaMix : SoundFileStrings.explosionMegaMix)
                        .numberOfGibletPairs(5)
                        .size(Measure.units(1f))
                        .build().performAction(world, e);
            }
        };
    }



}
