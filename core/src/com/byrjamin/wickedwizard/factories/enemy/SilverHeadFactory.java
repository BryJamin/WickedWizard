package com.byrjamin.wickedwizard.factories.enemy;

import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.IntMap;
import com.byrjamin.wickedwizard.assets.TextureStrings;
import com.byrjamin.wickedwizard.ecs.components.movement.CollisionBoundComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.Task;
import com.byrjamin.wickedwizard.ecs.components.ai.PhaseComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.GravityComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.VelocityComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.AnimationComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.AnimationStateComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.TextureRegionComponent;
import com.byrjamin.wickedwizard.factories.weapons.enemy.MultiPistol;
import com.byrjamin.wickedwizard.utils.ComponentBag;
import com.byrjamin.wickedwizard.utils.Measure;

/**
 * Created by Home on 11/04/2017.
 */

public class SilverHeadFactory extends EnemyFactory {

    private final static int STANDING = 0;
    private final static int CLOSING = 2;
    private final static int OPENING = 4;
    private final static int CHARING = 8;

    private final static float width = Measure.units(9);
    private final static float height = Measure.units(9f);

    private final static float health = 11f;

    private final static float textureWidth = Measure.units(9);
    private final static float textureHeight = Measure.units(9);

    private final static float textureOffsetX = 0;
    private final static float textureOffsetY = 0;

    private final static float accelX = Measure.units(5f);
    private final static float maxX = Measure.units(20f);

    private final static float chargeAccelX = Measure.units(5f);
    private final static float chargeMaxX = Measure.units(5f);

    private MultiPistol silverHeadWeapon;

    public SilverHeadFactory(AssetManager assetManager) {
        super(assetManager);
        silverHeadWeapon = new MultiPistol.PistolBuilder(assetManager)
                .angles(0,30,60,80,100,120,150,180)
                .shotSpeed(Measure.units(75f))
                .shotScale(3)
                .gravity(true)
                .build();
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
        animMap.put(STANDING, new Animation<TextureRegion>(0.125f, atlas.findRegions(TextureStrings.SILVERHEAD_ST), Animation.PlayMode.LOOP));
        animMap.put(CLOSING, new Animation<TextureRegion>(0.05f, atlas.findRegions(TextureStrings.SILVERHEAD_HIDING)));
        animMap.put(OPENING, new Animation<TextureRegion>(0.05f,  atlas.findRegions(TextureStrings.SILVERHEAD_HIDING), Animation.PlayMode.REVERSED));
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

            @Override
            public void performAction(World world, Entity e) {
                e.getComponent(AnimationStateComponent.class).setDefaultState(OPENING);
                CollisionBoundComponent cbc = e.getComponent(CollisionBoundComponent.class);
                silverHeadWeapon.fire(world, e, cbc.getCenterX(), cbc.getCenterY(), 0);
            }

            @Override
            public void cleanUpAction(World world, Entity e) {
            }
        };

        Task task4 = new Task(){
            @Override
            public void performAction(World world, Entity e) {
                e.getComponent(AnimationStateComponent.class).setDefaultState(STANDING);
            }

            @Override
            public void cleanUpAction(World world, Entity e) {
            }
        };

        PhaseComponent pc = new PhaseComponent();

        pc.addPhase(2.0f, task4);
        pc.addPhase(animMap.get(CLOSING).getAnimationDuration(), task1);
        pc.addPhase(animMap.get(CHARING).getAnimationDuration(), task2);
        pc.addPhase(animMap.get(OPENING).getAnimationDuration(), task3);
        bag.add(pc);

        return bag;

    }





}
