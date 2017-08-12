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

    public EnemyFactory(AssetManager assetManager) {
        super(assetManager);
    }

    protected ComponentBag defaultEnemyBag (ComponentBag fillbag, float x, float y, float health) {
        fillbag.add(new PositionComponent(x, y));
        fillbag.add(new LootComponent());
        fillbag.add(new HealthComponent(health));
        fillbag.add(new BlinkOnHitComponent());
        fillbag.add(new EnemyComponent());
        fillbag.add(new OnDeathActionComponent(new Action() {
            @Override
            public void performAction(World world, Entity e) {

                new Giblets.GibletBuilder(assetManager)
                        .intangible(false)
                        .minSpeed(Measure.units(10f))
                        .maxSpeed(Measure.units(50f))
                        .expiryTime(0.6f)
                        .fadeChance(0.75f)
                        .intangible(true)
                        .numberOfGibletPairs(5)
                        .size(Measure.units(1f))
                        .build().performAction(world, e);
/*

                gibletFactory.giblets(5, 0.4f,
                        Measure.units(20f), Measure.units(100f), Measure.units(1f), new Color(Color.WHITE)).performAction(world, e);*/
                world.getSystem(SoundSystem.class).playRandomSound(SoundFileStrings.explosionMegaMix);
            }
        }));

      //  fillbag.add(new HitSoundComponent(SoundFileStrings.hitMegaMix));

        return fillbag;

    }


    protected ComponentBag defaultBossBag (final ComponentBag fillbag, float x, float y, float health) {
        fillbag.add(new PositionComponent(x, y));
        //fillbag.add(new LootComponent());
        fillbag.add(new HealthComponent(health));
        fillbag.add(new BlinkOnHitComponent());
        fillbag.add(new EnemyComponent());
        fillbag.add(new OnDeathActionComponent(new Action() {
            @Override
            public void performAction(World world, Entity e) {
                deathClone(world, e);
                clearBullets(world);
                Entity flash = bossEndFlash(world);
            }
        }));

        return fillbag;

    }


    protected Entity deathClone(World world, Entity e){

        Entity deathClone = world.createEntity();
        deathClone.edit().add(e.getComponent(PositionComponent.class));
        deathClone.edit().add(e.getComponent(TextureRegionComponent.class));
        deathClone.edit().add(e.getComponent(CollisionBoundComponent.class));
        deathClone.edit().add(new ExpireComponent(1.45f));
        deathClone.edit().add(new EnemyComponent());

        deathClone.edit().add(new ActionAfterTimeComponent(new Action() {

            Giblets giblets = new Giblets.GibletBuilder(assetManager)
                    .intangible(false)
                    .minSpeed(Measure.units(10f))
                    .maxSpeed(Measure.units(100f))
                    .expiryTime(0.6f)
                    .fadeChance(0.75f)
                    .intangible(true)
                    .numberOfGibletPairs(10)
                    .mixes(SoundFileStrings.explosionMegaMix)
                    .size(Measure.units(1f))
                    .colors(new Color(new Color(Color.RED)), new Color(ColorResource.BOMB_ORANGE), new Color(Color.BLACK))
                    .build();

            @Override
            public void performAction(World world, Entity e) {
                giblets.performAction(world, e);
            }
        }, 0.1f, true));

        return deathClone;

    }

    protected void clearBullets(World world){

        IntBag intBag = world.getAspectSubscriptionManager().get(Aspect.all(EnemyComponent.class, BulletComponent.class)).getEntities();
        for(int i = 0; i < intBag.size(); i++){
            world.getSystem(OnDeathSystem.class).kill(world.getEntity(intBag.get(i)));
        }


    }

    protected Entity bossEndFlash(World world){

        float width = ArenaShellFactory.SECTION_WIDTH * 3;
        float height = ArenaShellFactory.SECTION_HEIGHT * 3;

        Entity flash = world.createEntity();
        flash.edit().add(new FollowPositionComponent(world.getSystem(FindPlayerSystem.class).getPlayerComponent(PositionComponent.class).position, -width / 2, -height / 2));
        flash.edit().add(new PositionComponent(0, 0));
        flash.edit().add(new TextureRegionComponent(atlas.findRegion(TextureStrings.BLOCK), width, height, TextureRegionComponent.FOREGROUND_LAYER_NEAR,
                new Color(Color.WHITE)));
        flash.edit().add(new FadeComponent(true, 1.4f, 1));
        flash.edit().add(new ExpireComponent(4f));

        return flash;
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
                        .numberOfGibletPairs(5)
                        .size(Measure.units(1f))
                        .build().performAction(world, e);

                world.getSystem(SoundSystem.class).playSound(SoundFileStrings.explosionMix1);

            }
        };
    }

    protected ComponentBag defaultEnemyBagNoLootNoDeath (ComponentBag fillbag, float x, float y, float health) {
        fillbag.add(new PositionComponent(x, y));
        fillbag.add(new HealthComponent(health));
        fillbag.add(new BlinkOnHitComponent());
        fillbag.add(new EnemyComponent());
        return fillbag;
    }




    ComponentBag defaultEnemyBagNoLoot (ComponentBag fillbag, float x, float y, float health) {

        fillbag = defaultEnemyBagNoLootNoDeath(fillbag, x, y, health);

        fillbag.add(new OnDeathActionComponent(new Task() {
            @Override
            public void performAction(World world, Entity e) {

                new Giblets.GibletBuilder(assetManager)
                        .intangible(false)
                        .minSpeed(Measure.units(10f))
                        .maxSpeed(Measure.units(50f))
                        .expiryTime(0.6f)
                        .fadeChance(0.75f)
                        .intangible(true)
                        .numberOfGibletPairs(5)
                        .size(Measure.units(1f))
                        .build().performAction(world, e);

                world.getSystem(SoundSystem.class).playSound(SoundFileStrings.explosionMix1);
            }

            @Override
            public void cleanUpAction(World world, Entity e) {

            }
        }));
        return fillbag;
    }


}
