package com.byrjamin.wickedwizard.factories.enemy;

import com.artemis.Component;
import com.artemis.utils.Bag;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.IntMap;
import com.byrjamin.wickedwizard.assets.TextureStrings;
import com.byrjamin.wickedwizard.ecs.components.movement.BounceComponent;
import com.byrjamin.wickedwizard.ecs.components.CollisionBoundComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.FiringAIComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.PositionComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.VelocityComponent;
import com.byrjamin.wickedwizard.ecs.components.WeaponComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.AnimationComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.AnimationStateComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.TextureRegionComponent;
import com.byrjamin.wickedwizard.factories.AbstractFactory;
import com.byrjamin.wickedwizard.factories.weapons.WeaponFactory;
import com.byrjamin.wickedwizard.factories.weapons.enemy.Pistol;
import com.byrjamin.wickedwizard.utils.ComponentBag;
import com.byrjamin.wickedwizard.utils.Measure;
import com.byrjamin.wickedwizard.screens.PlayScreen;

import java.util.Random;

/**
 * Created by Home on 26/03/2017.
 */

public class TurretFactory extends EnemyFactory {

    WeaponFactory wf;

    public TurretFactory(AssetManager assetManager) {
        super(assetManager);
        wf = new WeaponFactory(assetManager);
    }

    final float width = Measure.units(9f);
    final float height = Measure.units(9f);

    public Bag<Component> fixedLockOnTurret(float x, float y){

        x = x - this.width / 2;
        y = y - this.width / 2;

        Bag<Component> bag = this.defaultEnemyBag(new ComponentBag(), x , y, width, height, 10);
        bag.add(new CollisionBoundComponent(new Rectangle(x,y, width, height), true));
        bag.add(new TextureRegionComponent(atlas.findRegion(TextureStrings.SENTRY),
                0, 0, width, height, TextureRegionComponent.ENEMY_LAYER_MIDDLE
                ));

        WeaponComponent wc = new WeaponComponent(wf.enemyWeapon(), 2f);
        bag.add(wc);
        bag.add(new FiringAIComponent());

        bag.add(new AnimationStateComponent(0));
        IntMap<Animation<TextureRegion>> animMap = new IntMap<Animation<TextureRegion>>();
        animMap.put(0, new Animation<TextureRegion>(0.15f / 1f, atlas.findRegions(TextureStrings.SENTRY), Animation.PlayMode.LOOP_RANDOM));

        bag.add(new AnimationComponent(animMap));


        return bag;
    }


    public Bag<Component> fixedTurret(float x, float y, float angleInDegrees, float fireRate, float fireDelay){

        float width = Measure.units(10f);
        float height = Measure.units(10f);

        x = x - width / 2;
        y = y - width / 2;

        Bag<Component> bag = this.defaultEnemyBag(new ComponentBag(), x , y, width, height, 10);
        bag.add(new CollisionBoundComponent(new Rectangle(x,y, width, height), true));
/*        bag.add(new TextureRegionComponent(PlayScreen.atlas.findRegion(TextureStrings.BLOB_STANDING),
                -Measure.units(1f), 0, Measure.units(12), Measure.units(12), TextureRegionComponent.ENEMY_LAYER_MIDDLE
        ));*/

        WeaponComponent wc = new WeaponComponent(new Pistol(assetManager, fireRate), fireDelay);
        bag.add(wc);
        bag.add(new FiringAIComponent(angleInDegrees));

        return bag;
    }

    public Bag<Component> movingTurret(float x, float y){

        x = x - width / 2;
        y = y - width / 2;

        Bag<Component> bag = this.defaultEnemyBag(new ComponentBag(), x , y, width, height, 10);

        Random random = new Random();
        if(random.nextBoolean()) {
            bag.add(new VelocityComponent(300, 0));
        } else {
            bag.add(new VelocityComponent(-300, 0));
        }
        bag.add(new CollisionBoundComponent(new Rectangle(x,y, Measure.units(9), Measure.units(9)), true));
        bag.add(new TextureRegionComponent(atlas.findRegion(TextureStrings.SENTRY),
                0, 0, width, height, TextureRegionComponent.ENEMY_LAYER_MIDDLE
        ));

        bag.add(new AnimationStateComponent(0));
        IntMap<Animation<TextureRegion>> animMap = new IntMap<Animation<TextureRegion>>();
        animMap.put(0, new Animation<TextureRegion>(0.15f / 1f, atlas.findRegions(TextureStrings.SENTRY), Animation.PlayMode.LOOP_RANDOM));


        bag.add(new AnimationComponent(animMap));

        WeaponComponent wc = new WeaponComponent(wf.enemyWeapon(), 2f);
        bag.add(wc);
        bag.add(new FiringAIComponent());
        bag.add(new BounceComponent());

        return bag;
    }




}
