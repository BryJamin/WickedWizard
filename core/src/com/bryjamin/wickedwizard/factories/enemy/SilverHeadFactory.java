package com.bryjamin.wickedwizard.factories.enemy;

import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.IntMap;
import com.bryjamin.wickedwizard.ecs.components.movement.CollisionBoundComponent;
import com.bryjamin.wickedwizard.ecs.components.ai.PhaseComponent;
import com.bryjamin.wickedwizard.ecs.components.texture.AnimationComponent;
import com.bryjamin.wickedwizard.ecs.components.texture.AnimationStateComponent;
import com.bryjamin.wickedwizard.ecs.components.texture.TextureRegionComponent;

/**
 * Created by Home on 11/04/2017.
 */

public class SilverHeadFactory extends EnemyFactory {

    private final static int STANDING = 0;
    private final static int CLOSING = 2;
    private final static int OPENING = 4;
    private final static int CHARING = 8;

    private final static float width = com.bryjamin.wickedwizard.utils.Measure.units(9);
    private final static float height = com.bryjamin.wickedwizard.utils.Measure.units(9f);

    private final static float health = 11f;

    private final static float textureWidth = com.bryjamin.wickedwizard.utils.Measure.units(9);
    private final static float textureHeight = com.bryjamin.wickedwizard.utils.Measure.units(9);

    private final static float textureOffsetX = 0;
    private final static float textureOffsetY = 0;

    private final static float accelX = com.bryjamin.wickedwizard.utils.Measure.units(5f);
    private final static float maxX = com.bryjamin.wickedwizard.utils.Measure.units(20f);

    private final static float chargeAccelX = com.bryjamin.wickedwizard.utils.Measure.units(5f);
    private final static float chargeMaxX = com.bryjamin.wickedwizard.utils.Measure.units(5f);

    private com.bryjamin.wickedwizard.factories.weapons.enemy.MultiPistol silverHeadWeapon;

    public SilverHeadFactory(AssetManager assetManager) {
        super(assetManager);
        silverHeadWeapon = new com.bryjamin.wickedwizard.factories.weapons.enemy.MultiPistol.PistolBuilder(assetManager)
                .angles(0,30,60,80,100,120,150,180)
                .shotSpeed(com.bryjamin.wickedwizard.utils.Measure.units(75f))
                .shotScale(3)
                .gravity(true)
                .build();
    }

    public com.bryjamin.wickedwizard.utils.ComponentBag silverHead(float x, float y){

        x = x - width / 2;
        y = y - height / 2;

        com.bryjamin.wickedwizard.utils.ComponentBag bag = this.defaultEnemyBag(new com.bryjamin.wickedwizard.utils.ComponentBag(), x , y,health);
        bag.add(new com.bryjamin.wickedwizard.ecs.components.movement.VelocityComponent(0, 0));
        bag.add(new CollisionBoundComponent(new Rectangle(x,y, width, height), true));
        bag.add(new com.bryjamin.wickedwizard.ecs.components.movement.GravityComponent());
        //bag.add(new AccelerantComponent(accelX, 0, maxX, 0));
        //bag.add(new MoveToPlayerComponent());
        bag.add(new AnimationStateComponent(STANDING));
        IntMap<Animation<TextureRegion>> animMap = new IntMap<Animation<TextureRegion>>();
        animMap.put(STANDING, new Animation<TextureRegion>(0.125f, atlas.findRegions(com.bryjamin.wickedwizard.assets.TextureStrings.SILVERHEAD_ST), Animation.PlayMode.LOOP));
        animMap.put(CLOSING, new Animation<TextureRegion>(0.05f, atlas.findRegions(com.bryjamin.wickedwizard.assets.TextureStrings.SILVERHEAD_HIDING)));
        animMap.put(OPENING, new Animation<TextureRegion>(0.05f,  atlas.findRegions(com.bryjamin.wickedwizard.assets.TextureStrings.SILVERHEAD_HIDING), Animation.PlayMode.REVERSED));
        animMap.put(CHARING, new Animation<TextureRegion>(0.1f,  atlas.findRegions(com.bryjamin.wickedwizard.assets.TextureStrings.SILVERHEAD_CHARGING)));
        bag.add(new AnimationComponent(animMap));

        bag.add(new TextureRegionComponent(animMap.get(STANDING).getKeyFrame(0),
                textureOffsetX,
                textureOffsetY,
                textureWidth,
                textureHeight,
                TextureRegionComponent.ENEMY_LAYER_MIDDLE));

        com.bryjamin.wickedwizard.ecs.components.ai.Task task1 = new com.bryjamin.wickedwizard.ecs.components.ai.Task(){

            @Override
            public void performAction(World world, Entity e) {
                e.getComponent(AnimationStateComponent.class).setDefaultState(CLOSING);
            }

            @Override
            public void cleanUpAction(World world, Entity e) {

            }
        };

        com.bryjamin.wickedwizard.ecs.components.ai.Task task2 = new com.bryjamin.wickedwizard.ecs.components.ai.Task(){

            @Override
            public void performAction(World world, Entity e) {
                e.getComponent(AnimationStateComponent.class).setDefaultState(CHARING);
            }

            @Override
            public void cleanUpAction(World world, Entity e) {

            }
        };

        com.bryjamin.wickedwizard.ecs.components.ai.Task task3 = new com.bryjamin.wickedwizard.ecs.components.ai.Task(){

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

        com.bryjamin.wickedwizard.ecs.components.ai.Task task4 = new com.bryjamin.wickedwizard.ecs.components.ai.Task(){
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
