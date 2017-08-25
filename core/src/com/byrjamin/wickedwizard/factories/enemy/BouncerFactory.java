package com.byrjamin.wickedwizard.factories.enemy;

import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.IntMap;
import com.byrjamin.wickedwizard.assets.TextureStrings;
import com.byrjamin.wickedwizard.ecs.components.identifiers.LootComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.CollisionBoundComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.OnCollisionActionComponent;
import com.byrjamin.wickedwizard.ecs.components.WeaponComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.Action;
import com.byrjamin.wickedwizard.ecs.components.ai.Task;
import com.byrjamin.wickedwizard.ecs.components.ai.OnDeathActionComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.BounceComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.VelocityComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.AnimationComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.AnimationStateComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.TextureRegionComponent;
import com.byrjamin.wickedwizard.factories.weapons.enemy.MultiPistol;
import com.byrjamin.wickedwizard.utils.BagSearch;
import com.byrjamin.wickedwizard.utils.BagToEntity;
import com.byrjamin.wickedwizard.utils.ComponentBag;
import com.byrjamin.wickedwizard.utils.Measure;

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

        x = x - width / 2;
        y = y - height / 2;
        ComponentBag bag = basicBouncer(x, y, width, height, speed, random.nextBoolean());

        return bag;
    }

    public ComponentBag smallBouncer(float x, float y, boolean startsLeft){

        x = x - width / 2;
        y = y - height / 2;
        ComponentBag bag = basicBouncer(x, y, width, height, speed, startsLeft);

        return bag;
    }

    public ComponentBag largeBouncer(float x, float y){

        ComponentBag bag = basicBouncer(x, y, width * 2, height * 2, speed / 2, random.nextBoolean());
        BagSearch.removeObjectOfTypeClass(OnDeathActionComponent.class, bag);
        bag.add(new LootComponent());

        bag.add(new OnDeathActionComponent(new Task() {
            @Override
            public void performAction(World world, Entity e) {
                CollisionBoundComponent cbc = e.getComponent(CollisionBoundComponent.class);
                BagToEntity.bagToEntity(world.createEntity(), smallBouncer(cbc.getCenterX(), cbc.getCenterY(), true));
                BagToEntity.bagToEntity(world.createEntity(), smallBouncer(cbc.getCenterX(), cbc.getCenterY(), false));
            }

            @Override
            public void cleanUpAction(World world, Entity e) {

            }
        }));

        return bag;
    }

    private ComponentBag basicBouncer(float x, float y, float width, float height, float speed, boolean startsLeft) {

        x = x -width / 2;
        y = y - height / 2;


        ComponentBag bag = new ComponentBag();
        bag.add(new CollisionBoundComponent(new Rectangle(x, y, width, height), true));

        this.defaultEnemyBagNoLoot(bag, x, y, 3);

        bag.add(new AnimationStateComponent(0));
        IntMap<Animation<TextureRegion>> animMap = new IntMap<Animation<TextureRegion>>();
        animMap.put(0, new Animation<TextureRegion>(0.15f / 1f,
                atlas.findRegions(TextureStrings.BOUNCER_DEFAULT), Animation.PlayMode.LOOP));

        bag.add(new AnimationComponent(animMap));
        bag.add(new TextureRegionComponent(atlas.findRegion(TextureStrings.BOUNCER_DEFAULT),
                0, 0, width, height,
                TextureRegionComponent.ENEMY_LAYER_MIDDLE));

        bag.add(new BounceComponent());
        bag.add(new VelocityComponent(startsLeft ? -speed : speed,speed));

        return bag;
    }

    public ComponentBag laserBouncer(float x, float y, boolean isLeft){


        ComponentBag bag = basicBouncer(x,y,width,height,speed, isLeft);
        BagSearch.removeObjectOfTypeClass(TextureRegionComponent.class, bag);
        BagSearch.removeObjectOfTypeClass(AnimationStateComponent.class, bag);
        BagSearch.removeObjectOfTypeClass(AnimationComponent.class, bag);

        bag.add(new AnimationStateComponent(0));
        IntMap<Animation<TextureRegion>> animMap = new IntMap<Animation<TextureRegion>>();
        animMap.put(0, new Animation<TextureRegion>(0.15f / 1f,
                atlas.findRegions(TextureStrings.BOUNCER_RED), Animation.PlayMode.LOOP));

        bag.add(new AnimationComponent(animMap));
        bag.add(new TextureRegionComponent(atlas.findRegion(TextureStrings.BOUNCER_RED),
                0, 0, width, height,
                TextureRegionComponent.ENEMY_LAYER_MIDDLE));

        //BagSearch.removeObjectOfTypeClass(VelocityComponent.class, bag);

        MultiPistol multiPistol = new MultiPistol.PistolBuilder(assetManager)
                .fireRate(1.5f)
                .angles(0,30,60,90,120,150,180,210,240,270,300,330)
                .shotScale(2)
                .expire(true)
                .expireRange(Measure.units(50f))
                .build();

        bag.add(new WeaponComponent(multiPistol, 0f));

        Action action = new Action() {
            @Override
            public void performAction(World world, Entity e) {
                e.getComponent(WeaponComponent.class).weapon.fire(world, e, e.getComponent(CollisionBoundComponent.class).getCenterX(), e.getComponent(CollisionBoundComponent.class).getCenterY(), 0);
            }
        };

        bag.add(new OnCollisionActionComponent(action,action,action,action));


        return bag;


    }



}
