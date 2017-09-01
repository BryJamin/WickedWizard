package com.byrjamin.wickedwizard.factories.enemy;

import com.artemis.Aspect;
import com.artemis.Entity;
import com.artemis.World;
import com.artemis.utils.IntBag;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.byrjamin.wickedwizard.assets.ColorResource;
import com.byrjamin.wickedwizard.assets.SoundFileStrings;
import com.byrjamin.wickedwizard.assets.TextureStrings;
import com.byrjamin.wickedwizard.ecs.components.graphics.CameraShakeComponent;
import com.byrjamin.wickedwizard.ecs.components.identifiers.ArenaLockComponent;
import com.byrjamin.wickedwizard.ecs.components.identifiers.BossComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.BlinkOnHitComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.CollisionBoundComponent;
import com.byrjamin.wickedwizard.ecs.components.HealthComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.Action;
import com.byrjamin.wickedwizard.ecs.components.ai.ActionAfterTimeComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.ExpireComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.FollowPositionComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.Task;
import com.byrjamin.wickedwizard.ecs.components.ai.OnDeathActionComponent;
import com.byrjamin.wickedwizard.ecs.components.identifiers.BulletComponent;
import com.byrjamin.wickedwizard.ecs.components.identifiers.EnemyComponent;
import com.byrjamin.wickedwizard.ecs.components.identifiers.LootComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.PositionComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.FadeComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.TextureRegionComponent;
import com.byrjamin.wickedwizard.ecs.systems.FindPlayerSystem;
import com.byrjamin.wickedwizard.ecs.systems.ai.OnDeathSystem;
import com.byrjamin.wickedwizard.ecs.systems.audio.SoundSystem;
import com.byrjamin.wickedwizard.factories.AbstractFactory;
import com.byrjamin.wickedwizard.factories.arenas.decor.ArenaShellFactory;
import com.byrjamin.wickedwizard.factories.weapons.Giblets;
import com.byrjamin.wickedwizard.utils.ComponentBag;
import com.byrjamin.wickedwizard.utils.Measure;

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
        fillbag.add(new HealthComponent(health));
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
