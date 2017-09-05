package com.bryjamin.wickedwizard.factories.enemy.bosses;

import com.artemis.Aspect;
import com.artemis.Entity;
import com.artemis.World;
import com.artemis.utils.IntBag;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.bryjamin.wickedwizard.assets.ColorResource;
import com.bryjamin.wickedwizard.assets.SoundFileStrings;
import com.bryjamin.wickedwizard.ecs.components.ai.Action;
import com.bryjamin.wickedwizard.ecs.components.ai.ActionAfterTimeComponent;
import com.bryjamin.wickedwizard.ecs.components.ai.ExpireComponent;
import com.bryjamin.wickedwizard.ecs.components.graphics.CameraShakeComponent;
import com.bryjamin.wickedwizard.ecs.components.movement.CollisionBoundComponent;
import com.bryjamin.wickedwizard.ecs.components.texture.FadeComponent;
import com.bryjamin.wickedwizard.ecs.components.texture.TextureRegionComponent;
import com.bryjamin.wickedwizard.factories.AbstractFactory;
import com.bryjamin.wickedwizard.utils.ComponentBag;
import com.bryjamin.wickedwizard.utils.Measure;

/**
 * Created by BB on 01/09/2017.
 */

public class BossFactory extends AbstractFactory {


    public BossFactory(AssetManager assetManager) {
        super(assetManager);
    }


    protected ComponentBag defaultBossBag (final ComponentBag fillbag, float x, float y, float health) {
        fillbag.add(new com.bryjamin.wickedwizard.ecs.components.movement.PositionComponent(x, y));
        fillbag.add(new com.bryjamin.wickedwizard.ecs.components.HealthComponent(health));
        fillbag.add(new com.bryjamin.wickedwizard.ecs.components.texture.BlinkOnHitComponent());
        fillbag.add(new com.bryjamin.wickedwizard.ecs.components.identifiers.BossComponent());
        fillbag.add(new com.bryjamin.wickedwizard.ecs.components.identifiers.EnemyComponent());
        fillbag.add(new com.bryjamin.wickedwizard.ecs.components.ai.OnDeathActionComponent(new Action() {
            @Override
            public void performAction(World world, Entity e) {
                deathClone(world, e);
                clearBullets(world);
                Entity flash = bossEndFlash(world);
            }
        }));

        return fillbag;

    }

    protected ComponentBag defaultBossBagNoDeath (final ComponentBag fillbag, float x, float y, float health) {
        fillbag.add(new com.bryjamin.wickedwizard.ecs.components.movement.PositionComponent(x, y));
        fillbag.add(new com.bryjamin.wickedwizard.ecs.components.HealthComponent(health));
        fillbag.add(new com.bryjamin.wickedwizard.ecs.components.texture.BlinkOnHitComponent());
        fillbag.add(new com.bryjamin.wickedwizard.ecs.components.identifiers.BossComponent());
        fillbag.add(new com.bryjamin.wickedwizard.ecs.components.identifiers.EnemyComponent());
        return fillbag;
    }



    protected Entity deathClone(World world, Entity e){

        Entity deathClone = world.createEntity();
        deathClone.edit().add(e.getComponent(com.bryjamin.wickedwizard.ecs.components.movement.PositionComponent.class));
        deathClone.edit().add(e.getComponent(TextureRegionComponent.class));
        deathClone.edit().add(e.getComponent(CollisionBoundComponent.class));
        deathClone.edit().add(new ExpireComponent(1.45f));
        deathClone.edit().add(new CameraShakeComponent(0.75f));
        deathClone.edit().add(new com.bryjamin.wickedwizard.ecs.components.identifiers.ArenaLockComponent());

        deathClone.edit().add(new ActionAfterTimeComponent(new Action() {

            com.bryjamin.wickedwizard.factories.weapons.Giblets giblets = new com.bryjamin.wickedwizard.factories.weapons.Giblets.GibletBuilder(assetManager)
                    .intangible(false)
                    .minSpeed(Measure.units(10f))
                    .maxSpeed(Measure.units(100f))
                    .expiryTime(0.6f)
                    .fadeChance(0.75f)
                    .intangible(true)
                    .numberOfGibletPairs(8)
                    .mixes(SoundFileStrings.bigExplosionMegaMix)
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
        IntBag intBag = world.getAspectSubscriptionManager().get(Aspect.all(com.bryjamin.wickedwizard.ecs.components.identifiers.EnemyComponent.class, com.bryjamin.wickedwizard.ecs.components.identifiers.BulletComponent.class)).getEntities();
        for(int i = 0; i < intBag.size(); i++){
            world.getSystem(com.bryjamin.wickedwizard.ecs.systems.ai.OnDeathSystem.class).kill(world.getEntity(intBag.get(i)));
        }
    }

    protected Entity bossEndFlash(World world){

        float width = com.bryjamin.wickedwizard.factories.arenas.decor.ArenaShellFactory.SECTION_WIDTH * 3;
        float height = com.bryjamin.wickedwizard.factories.arenas.decor.ArenaShellFactory.SECTION_HEIGHT * 3;

        Entity flash = world.createEntity();
        flash.edit().add(new com.bryjamin.wickedwizard.ecs.components.ai.FollowPositionComponent(world.getSystem(com.bryjamin.wickedwizard.ecs.systems.FindPlayerSystem.class).getPlayerComponent(com.bryjamin.wickedwizard.ecs.components.movement.PositionComponent.class).position, -width / 2, -height / 2));
        flash.edit().add(new com.bryjamin.wickedwizard.ecs.components.movement.PositionComponent(0, 0));
        flash.edit().add(new TextureRegionComponent(atlas.findRegion(com.bryjamin.wickedwizard.assets.TextureStrings.BLOCK), width, height, TextureRegionComponent.FOREGROUND_LAYER_NEAR,
                new Color(Color.WHITE)));
        flash.edit().add(new FadeComponent(true, 1.4f, 1));
        flash.edit().add(new ExpireComponent(4f));

        return flash;
    }









}
