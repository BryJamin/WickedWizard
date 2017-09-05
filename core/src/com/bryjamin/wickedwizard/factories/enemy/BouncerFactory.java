package com.bryjamin.wickedwizard.factories.enemy;

import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.IntMap;
import com.bryjamin.wickedwizard.ecs.components.ai.Action;
import com.bryjamin.wickedwizard.ecs.components.ai.OnDeathActionComponent;
import com.bryjamin.wickedwizard.ecs.components.identifiers.LootComponent;
import com.bryjamin.wickedwizard.ecs.components.movement.CollisionBoundComponent;
import com.bryjamin.wickedwizard.ecs.components.texture.AnimationComponent;
import com.bryjamin.wickedwizard.ecs.components.texture.AnimationStateComponent;
import com.bryjamin.wickedwizard.ecs.components.texture.TextureRegionComponent;
import com.bryjamin.wickedwizard.utils.ComponentBag;
import com.bryjamin.wickedwizard.utils.Measure;

import java.util.Random;

/**
 * Created by Home on 29/03/2017.
 */

public class BouncerFactory extends EnemyFactory {

    public BouncerFactory(AssetManager assetManager) {
        super(assetManager);
    }

    private static final float width = Measure.units(5);
    private static final float height = Measure.units(5);

    private static final Random random = new Random();

    private static final float speed = Measure.units(25f);

    public ComponentBag smallBouncer(float x, float y){
        ComponentBag bag = basicBouncer(x, y, width, height, speed, random.nextBoolean());
        return bag;
    }

    public ComponentBag smallBouncer(float x, float y, boolean startsLeft){
        ComponentBag bag = basicBouncer(x, y, width, height, speed, startsLeft);
        return bag;
    }

    public ComponentBag largeBouncer(float x, float y, boolean startsRight){

        ComponentBag bag = basicBouncer(x, y, width * 2, height * 2, speed / 2, startsRight);
        com.bryjamin.wickedwizard.utils.BagSearch.removeObjectOfTypeClass(OnDeathActionComponent.class, bag);
        bag.add(new LootComponent());

        bag.add(new OnDeathActionComponent(new com.bryjamin.wickedwizard.ecs.components.ai.Task() {
            @Override
            public void performAction(World world, Entity e) {
                CollisionBoundComponent cbc = e.getComponent(CollisionBoundComponent.class);
                com.bryjamin.wickedwizard.utils.BagToEntity.bagToEntity(world.createEntity(), smallBouncer(cbc.getCenterX(), cbc.getCenterY(), true));
                com.bryjamin.wickedwizard.utils.BagToEntity.bagToEntity(world.createEntity(), smallBouncer(cbc.getCenterX(), cbc.getCenterY(), false));
            }

            @Override
            public void cleanUpAction(World world, Entity e) {

            }
        }));

        return bag;
    }

    private ComponentBag basicBouncer(float x, float y, float width, float height, float speed, boolean startsRight) {

        x = x -width / 2;
        y = y - height / 2;


        ComponentBag bag = new ComponentBag();
        bag.add(new CollisionBoundComponent(new Rectangle(x, y, width, height), true));

        this.defaultEnemyBagNoLoot(bag, x, y, 3);

        bag.add(new AnimationStateComponent(0));
        IntMap<Animation<TextureRegion>> animMap = new IntMap<Animation<TextureRegion>>();
        animMap.put(0, new Animation<TextureRegion>(0.15f / 1f,
                atlas.findRegions(com.bryjamin.wickedwizard.assets.TextureStrings.BOUNCER_DEFAULT), Animation.PlayMode.LOOP));

        bag.add(new AnimationComponent(animMap));
        bag.add(new TextureRegionComponent(atlas.findRegion(com.bryjamin.wickedwizard.assets.TextureStrings.BOUNCER_DEFAULT),
                0, 0, width, height,
                TextureRegionComponent.ENEMY_LAYER_MIDDLE));

        bag.add(new com.bryjamin.wickedwizard.ecs.components.movement.BounceComponent());
        bag.add(new com.bryjamin.wickedwizard.ecs.components.movement.VelocityComponent(startsRight ? speed : -speed,speed));

        return bag;
    }

    public ComponentBag laserBouncer(float x, float y, boolean startsRight){


        ComponentBag bag = basicBouncer(x,y,width,height,speed, startsRight);
        com.bryjamin.wickedwizard.utils.BagSearch.removeObjectOfTypeClass(TextureRegionComponent.class, bag);
        com.bryjamin.wickedwizard.utils.BagSearch.removeObjectOfTypeClass(AnimationStateComponent.class, bag);
        com.bryjamin.wickedwizard.utils.BagSearch.removeObjectOfTypeClass(AnimationComponent.class, bag);

        bag.add(new AnimationStateComponent(0));
        IntMap<Animation<TextureRegion>> animMap = new IntMap<Animation<TextureRegion>>();
        animMap.put(0, new Animation<TextureRegion>(0.15f / 1f,
                atlas.findRegions(com.bryjamin.wickedwizard.assets.TextureStrings.BOUNCER_RED), Animation.PlayMode.LOOP));

        bag.add(new AnimationComponent(animMap));
        bag.add(new TextureRegionComponent(atlas.findRegion(com.bryjamin.wickedwizard.assets.TextureStrings.BOUNCER_RED),
                0, 0, width, height,
                TextureRegionComponent.ENEMY_LAYER_MIDDLE));

        //BagSearch.removeObjectOfTypeClass(VelocityComponent.class, bag);

        com.bryjamin.wickedwizard.factories.weapons.enemy.MultiPistol multiPistol = new com.bryjamin.wickedwizard.factories.weapons.enemy.MultiPistol.PistolBuilder(assetManager)
                .fireRate(1.5f)
                .angles(0,30,60,90,120,150,180,210,240,270,300,330)
                .shotScale(2)
                .expire(true)
                .expireRange(Measure.units(50f))
                .build();

        bag.add(new com.bryjamin.wickedwizard.ecs.components.WeaponComponent(multiPistol, 0f));

        Action action = new Action() {
            @Override
            public void performAction(World world, Entity e) {
                e.getComponent(com.bryjamin.wickedwizard.ecs.components.WeaponComponent.class).weapon.fire(world, e, e.getComponent(CollisionBoundComponent.class).getCenterX(), e.getComponent(CollisionBoundComponent.class).getCenterY(), 0);
            }
        };

        bag.add(new com.bryjamin.wickedwizard.ecs.components.ai.OnCollisionActionComponent(action,action,action,action));


        return bag;


    }



}
