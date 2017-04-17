package com.byrjamin.wickedwizard.factories.enemy;

import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.IntMap;
import com.byrjamin.wickedwizard.assets.TextureStrings;
import com.byrjamin.wickedwizard.ecs.components.BlinkComponent;
import com.byrjamin.wickedwizard.ecs.components.CollisionBoundComponent;
import com.byrjamin.wickedwizard.ecs.components.EnemyComponent;
import com.byrjamin.wickedwizard.ecs.components.HealthComponent;
import com.byrjamin.wickedwizard.ecs.components.OnDeathComponent;
import com.byrjamin.wickedwizard.ecs.components.WeaponComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.FiringAIComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.MoveToPlayerComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.Action;
import com.byrjamin.wickedwizard.ecs.components.ai.PhaseComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.AccelerantComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.GravityComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.PositionComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.VelocityComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.AnimationComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.AnimationStateComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.TextureRegionComponent;
import com.byrjamin.wickedwizard.factories.DeathFactory;
import com.byrjamin.wickedwizard.factories.weapons.WeaponFactory;
import com.byrjamin.wickedwizard.utils.AnimationPacker;
import com.byrjamin.wickedwizard.utils.ComponentBag;
import com.byrjamin.wickedwizard.utils.Measure;

/**
 * Created by Home on 11/04/2017.
 */

public class SilverHeadFactory {


    private static int STANDING = 0;
    private static int CLOSING = 1;
    private static int OPENING = 2;
    private static int CHARING = 3;

    private static float width = Measure.units(9);
    private static float height = Measure.units(9f);

    private static float health = 11f;

    private static final float textureWidth = Measure.units(12);
    private static final float textureHeight = Measure.units(12);

    private static final float textureOffsetX = -Measure.units(1.5f);
    private static final float textureOffsetY = 0;

    private static final float accelX = Measure.units(5f);
    private static final float maxX = Measure.units(20f);

    private static final float chargeAccelX = Measure.units(5f);
    private static final float chargeMaxX = Measure.units(5f);

    public static ComponentBag silverHead(float x, float y){

        x = x - width / 2;
        y = y - height / 2;

        ComponentBag bag = new ComponentBag();
        bag.add(new PositionComponent(x,y));
        bag.add(new VelocityComponent(0, 0));
        bag.add(new CollisionBoundComponent(new Rectangle(x,y, width, height), true));
        bag.add(new GravityComponent());
        bag.add(new EnemyComponent());
        bag.add(new AccelerantComponent(accelX, 0, maxX, 0));
        bag.add(new MoveToPlayerComponent());
        bag.add(new HealthComponent(health));
        bag.add(new BlinkComponent());
        //bag.add(new MoveToPlayerComponent());
        AnimationStateComponent sc = new AnimationStateComponent();
        sc.setState(STANDING);
        bag.add(sc);
        IntMap<Animation<TextureRegion>> animMap = new IntMap<Animation<TextureRegion>>();
        animMap.put(STANDING, AnimationPacker.genAnimation(0.15f, TextureStrings.SILVERHEAD_ST, Animation.PlayMode.LOOP));
        animMap.put(CLOSING, AnimationPacker.genAnimation(0.1f, TextureStrings.SILVERHEAD_HIDING));
        animMap.put(OPENING, AnimationPacker.genAnimation(0.1f, TextureStrings.SILVERHEAD_HIDING, Animation.PlayMode.REVERSED));
        animMap.put(CHARING, AnimationPacker.genAnimation(0.1f, TextureStrings.SILVERHEAD_CHARGING));
        bag.add(new AnimationComponent(animMap));

        bag.add(DeathFactory.basicOnDeathExplosion(new OnDeathComponent(), width, height));

        bag.add(new TextureRegionComponent(animMap.get(STANDING).getKeyFrame(0),
                textureOffsetX,
                textureOffsetY,
                textureWidth,
                textureHeight,
                TextureRegionComponent.ENEMY_LAYER_MIDDLE));

        Action action1 = new Action(){

            @Override
            public void performAction(World w, Entity e) {
                e.getComponent(AnimationStateComponent.class).setState(CLOSING);
            }

            @Override
            public void cleanUpAction(World w, Entity e) {

            }
        };

        Action action2 = new Action(){

            @Override
            public void performAction(World w, Entity e) {
                e.getComponent(AnimationStateComponent.class).setState(CHARING);
            }

            @Override
            public void cleanUpAction(World w, Entity e) {

            }
        };

        Action action3 = new Action(){

            WeaponComponent wc = new WeaponComponent(WeaponFactory.SilverHeadWeapon(), 2.0f);
            FiringAIComponent fc = new FiringAIComponent(Math.toRadians(0));

            @Override
            public void performAction(World w, Entity e) {
                e.getComponent(AnimationStateComponent.class).setState(OPENING);
                wc.timer.skip();
                e.edit().add(wc);
                e.edit().add(fc);
            }


            @Override
            public void cleanUpAction(World w, Entity e) {
                e.edit().remove(wc);
                e.edit().remove(fc);
            }
        };

        Action action4 = new Action(){

            @Override
            public void performAction(World w, Entity e) {
                e.getComponent(AnimationStateComponent.class).setState(STANDING);
                AccelerantComponent ac = e.getComponent(AccelerantComponent.class);
                ac.accelX = accelX;
                ac.maxX = maxX;
            }

            @Override
            public void cleanUpAction(World w, Entity e) {
                AccelerantComponent ac = e.getComponent(AccelerantComponent.class);
                ac.accelX = chargeAccelX;
                ac.maxX = chargeMaxX;
            }
        };

        PhaseComponent pc = new PhaseComponent();

        pc.addPhase(animMap.get(CLOSING).getAnimationDuration(), action1);
        pc.addPhase(animMap.get(CHARING).getAnimationDuration(), action2);
        pc.addPhase(animMap.get(OPENING).getAnimationDuration(), action3);
        pc.addPhase(2.0f, action4);
        pc.addPhaseSequence(2,3,0,1);


        bag.add(pc);

        return bag;

    }





}
