package com.byrjamin.wickedwizard.factories.enemy;

import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.IntMap;
import com.byrjamin.wickedwizard.assets.TextureStrings;
import com.byrjamin.wickedwizard.ecs.components.CollisionBoundComponent;
import com.byrjamin.wickedwizard.ecs.components.WeaponComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.FiringAIComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.Task;
import com.byrjamin.wickedwizard.ecs.components.ai.PhaseComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.GravityComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.VelocityComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.AnimationComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.AnimationStateComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.TextureRegionComponent;
import com.byrjamin.wickedwizard.factories.weapons.WeaponFactory;
import com.byrjamin.wickedwizard.utils.ComponentBag;
import com.byrjamin.wickedwizard.utils.Measure;

/**
 * Created by Home on 11/04/2017.
 */

public class SilverHeadFactory extends EnemyFactory {

    private final int STANDING = 0;
    private final int CLOSING = 1;
    private final int OPENING = 2;
    private final int CHARING = 3;

    private final float width = Measure.units(9);
    private final float height = Measure.units(9f);

    private final float health = 11f;

    private final float textureWidth = Measure.units(9);
    private final float textureHeight = Measure.units(9);

    private final float textureOffsetX = 0;
    private final float textureOffsetY = 0;

    private final float accelX = Measure.units(5f);
    private final float maxX = Measure.units(20f);

    private final float chargeAccelX = Measure.units(5f);
    private final float chargeMaxX = Measure.units(5f);

    private WeaponFactory wf;

    public SilverHeadFactory(AssetManager assetManager) {
        super(assetManager);
        wf = new WeaponFactory(assetManager);
    }

    public ComponentBag silverHead(float x, float y){

        x = x - width / 2;
        y = y - height / 2;

        ComponentBag bag = this.defaultEnemyBag(new ComponentBag(), x , y,health);
        bag.add(new VelocityComponent(0, 0));
        bag.add(new CollisionBoundComponent(new Rectangle(x,y, width, height), true));
        bag.add(new GravityComponent());
        //bag.add(new AccelerantComponent(accelX, 0, maxX, 0));
        //bag.add(new MoveToPlayerComponent());
        bag.add(new AnimationStateComponent(STANDING));
        IntMap<Animation<TextureRegion>> animMap = new IntMap<Animation<TextureRegion>>();
        animMap.put(STANDING, new Animation<TextureRegion>(0.15f, atlas.findRegions(TextureStrings.SILVERHEAD_ST), Animation.PlayMode.LOOP));
        animMap.put(CLOSING, new Animation<TextureRegion>(0.1f, atlas.findRegions(TextureStrings.SILVERHEAD_HIDING)));
        animMap.put(OPENING, new Animation<TextureRegion>(0.1f,  atlas.findRegions(TextureStrings.SILVERHEAD_HIDING), Animation.PlayMode.REVERSED));
        animMap.put(CHARING, new Animation<TextureRegion>(0.1f,  atlas.findRegions(TextureStrings.SILVERHEAD_CHARGING)));
        bag.add(new AnimationComponent(animMap));

        bag.add(new TextureRegionComponent(animMap.get(STANDING).getKeyFrame(0),
                textureOffsetX,
                textureOffsetY,
                textureWidth,
                textureHeight,
                TextureRegionComponent.ENEMY_LAYER_MIDDLE));

        Task task1 = new Task(){

            @Override
            public void performAction(World world, Entity e) {
                e.getComponent(AnimationStateComponent.class).setDefaultState(CLOSING);
            }

            @Override
            public void cleanUpAction(World world, Entity e) {

            }
        };

        Task task2 = new Task(){

            @Override
            public void performAction(World world, Entity e) {
                e.getComponent(AnimationStateComponent.class).setDefaultState(CHARING);
            }

            @Override
            public void cleanUpAction(World world, Entity e) {

            }
        };

        Task task3 = new Task(){

            WeaponComponent wc = new WeaponComponent(wf.SilverHeadWeapon(), 2.0f);
            FiringAIComponent fc = new FiringAIComponent(Math.toRadians(0));

            @Override
            public void performAction(World world, Entity e) {
                e.getComponent(AnimationStateComponent.class).setDefaultState(OPENING);
                wc.timer.skip();
                e.edit().add(wc);
                e.edit().add(fc);
            }


            @Override
            public void cleanUpAction(World world, Entity e) {
                e.edit().remove(wc);
                e.edit().remove(fc);
            }
        };

        Task task4 = new Task(){

            @Override
            public void performAction(World world, Entity e) {
                e.getComponent(AnimationStateComponent.class).setDefaultState(STANDING);
/*                AccelerantComponent ac = e.getComponent(AccelerantComponent.class);
                ac.accelX = accelX;
                ac.maxX = maxX;*/
            }

            @Override
            public void cleanUpAction(World world, Entity e) {
         /*       AccelerantComponent ac = e.getComponent(AccelerantComponent.class);
                ac.accelX = chargeAccelX;
                ac.maxX = chargeMaxX;*/
            }
        };

        PhaseComponent pc = new PhaseComponent();

        pc.addPhase(animMap.get(CLOSING).getAnimationDuration(), task1);
        pc.addPhase(animMap.get(CHARING).getAnimationDuration(), task2);
        pc.addPhase(animMap.get(OPENING).getAnimationDuration(), task3);
        pc.addPhase(2.0f, task4);
        pc.addPhaseSequence(2,3,0,1);


        bag.add(pc);

        return bag;

    }





}
