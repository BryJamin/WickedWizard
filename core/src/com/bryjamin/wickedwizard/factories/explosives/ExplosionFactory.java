package com.bryjamin.wickedwizard.factories.explosives;

import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
import com.bryjamin.wickedwizard.assets.ColorResource;
import com.bryjamin.wickedwizard.assets.SoundFileStrings;
import com.bryjamin.wickedwizard.ecs.components.ExplosionComponent;
import com.bryjamin.wickedwizard.ecs.components.ai.Action;
import com.bryjamin.wickedwizard.ecs.components.ai.ExpireComponent;
import com.bryjamin.wickedwizard.ecs.components.graphics.CameraShakeComponent;
import com.bryjamin.wickedwizard.ecs.components.identifiers.EnemyComponent;
import com.bryjamin.wickedwizard.ecs.components.identifiers.FriendlyComponent;
import com.bryjamin.wickedwizard.ecs.components.identifiers.IntangibleComponent;
import com.bryjamin.wickedwizard.ecs.components.movement.CollisionBoundComponent;
import com.bryjamin.wickedwizard.ecs.components.movement.PositionComponent;
import com.bryjamin.wickedwizard.factories.AbstractFactory;
import com.bryjamin.wickedwizard.factories.weapons.Giblets;
import com.bryjamin.wickedwizard.utils.BagToEntity;
import com.bryjamin.wickedwizard.utils.ComponentBag;
import com.bryjamin.wickedwizard.utils.Measure;

/**
 * Created by BB on 19/09/2017.
 */

public class ExplosionFactory extends AbstractFactory {

    private static final float defaultExplosionSize = Measure.units(20f);

    private Giblets.GibletBuilder gibletBuilder;

    public ExplosionFactory(AssetManager assetManager) {
        super(assetManager);
        this.gibletBuilder = new Giblets.GibletBuilder(assetManager);

        this.gibletBuilder.numberOfGibletPairs(15)
                .expiryTime(0.35f)
                .fadeChance(0.25f)
                .size(Measure.units(1.5f))
                .minSpeed(Measure.units(0f))
                .maxSpeed(Measure.units(75f))
                .mixes(SoundFileStrings.explosionMegaMix)
                .colors(new Color(ColorResource.BOMB_ORANGE), new Color(ColorResource.BOMB_RED), new Color(ColorResource.BOMB_YELLOW));



    }


    public Action neutralExplosionTask(){
        return new Action() {
            @Override
            public void performAction(World world, Entity e) {

                CollisionBoundComponent cbc = e.getComponent(CollisionBoundComponent.class);

                BagToEntity.bagToEntity(world.createEntity(),
                        bombExplosion(cbc.getCenterX(), cbc.getCenterY(), defaultExplosionSize, defaultExplosionSize, 1));

                gibletBuilder.build()
                        .performAction(world, e);

            }
        };
    }



    public Action enemyExplosionTask(){
        return new Action() {
            @Override
            public void performAction(World world, Entity e) {

                CollisionBoundComponent cbc = e.getComponent(CollisionBoundComponent.class);

                BagToEntity.bagToEntity(world.createEntity(),
                        enemyBombExplosion(cbc.getCenterX(), cbc.getCenterY(), defaultExplosionSize, defaultExplosionSize, 1));

                gibletBuilder.build()
                        .performAction(world, e);

            }
        };
    }


    public Action friendlyExplosionTask(final float damage){
        return new Action() {
            @Override
            public void performAction(World world, Entity e) {

                CollisionBoundComponent cbc = e.getComponent(CollisionBoundComponent.class);

                BagToEntity.bagToEntity(world.createEntity(),
                        friendlyBombExplosion(cbc.getCenterX(), cbc.getCenterY(), defaultExplosionSize, defaultExplosionSize, damage));

                gibletBuilder.build()
                        .performAction(world, e);

            }
        };
    }


    private ComponentBag bombExplosion(float x, float y, float width, float height, float damage) {

        x = x - width / 2;
        y = y - height / 2;

        ComponentBag bag = new ComponentBag();
        bag.add(new ExplosionComponent(damage));
        bag.add(new CollisionBoundComponent(new Rectangle(x, y, width, height)));
        bag.add(new PositionComponent(x, y));
        bag.add(new CameraShakeComponent(0.5f));
        bag.add(new ExpireComponent(0.15f));
        bag.add(new IntangibleComponent());

        return bag;

    }


    public ComponentBag enemyBombExplosion(float x, float y, float width, float height, float damage) {

        ComponentBag bag = bombExplosion(x, y, width, height, damage);
        bag.add(new EnemyComponent());

        return bag;

    }


    public ComponentBag friendlyBombExplosion(float x, float y, float width, float height, float damage) {

        ComponentBag bag = bombExplosion(x, y, width, height, damage);
        bag.add(new FriendlyComponent());

        return bag;

    }

}
