package com.bryjamin.wickedwizard.factories.enemy.bosses;

import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.IntMap;
import com.bryjamin.wickedwizard.assets.TextureStrings;
import com.bryjamin.wickedwizard.ecs.components.WeaponComponent;
import com.bryjamin.wickedwizard.ecs.components.ai.Action;
import com.bryjamin.wickedwizard.ecs.components.ai.ActionAfterTimeComponent;
import com.bryjamin.wickedwizard.ecs.components.ai.FiringAIComponent;
import com.bryjamin.wickedwizard.ecs.components.ai.Task;
import com.bryjamin.wickedwizard.ecs.components.movement.CollisionBoundComponent;
import com.bryjamin.wickedwizard.ecs.components.texture.AnimationComponent;
import com.bryjamin.wickedwizard.ecs.components.texture.AnimationStateComponent;
import com.bryjamin.wickedwizard.ecs.components.texture.TextureRegionComponent;
import com.bryjamin.wickedwizard.factories.weapons.enemy.LaserOrbitalTask;
import com.bryjamin.wickedwizard.factories.weapons.enemy.MultiPistol;
import com.bryjamin.wickedwizard.utils.ComponentBag;
import com.bryjamin.wickedwizard.utils.Measure;

import java.util.Random;

/**
 * Created by Home on 27/05/2017.
 */

public class BossKugelDusc extends BossFactory {


    private static final int SLOW_SPIN_STATE = 2;
    private static final int FAST_SPIN_STATE = 4;

    private static final float width = Measure.units(30f);
    private static final float height = Measure.units(30f);

    private static final float health = 80;

    //SPRAY

    private static final float sprayAndPrayPhaseTime = 8f;

    private static final float weaponStartUpTime = 0.5f;
    private static final float changeInFiringAngleInDegrees = 25;
    private static final float shotSpeed = Measure.units(35f);
    private static final float fireRate = 0.5f;


    private static final float gapBetweenPhases = 0.25f;


    //LaserPhase
    private static final float laserSquareSize = Measure.units(8.5f);
    private static final int[] laserAnglesAntiClockWise = {-10,80,170, 260};
    private static final int[] laserAnglesClockWise = {20,110,200, 290};
    private static final float speedInDegrees = 0.5f;

    private static final float laserPhaseTime = 12.5f;


    public BossKugelDusc(AssetManager assetManager) {
        super(assetManager);
    }


    public ComponentBag giantKugelDusche(float x, float y) {
        return giantKugelDusche(x, y, new Random().nextBoolean());
    };

    public ComponentBag giantKugelDusche(float x, float y, boolean isLeft){


        x = x - width / 2;
        y = y - height / 2;

        ComponentBag bag = this.defaultBossBag(new ComponentBag(), x , y, health);
        bag.add(new CollisionBoundComponent(new Rectangle(x, y, width, height), true));


        bag.add(new AnimationStateComponent(SLOW_SPIN_STATE));
        IntMap<Animation<TextureRegion>> animMap = new IntMap<Animation<TextureRegion>>();
        animMap.put(SLOW_SPIN_STATE, new Animation<TextureRegion>(0.1f, atlas.findRegions(TextureStrings.KUGELDUSCHE_EMPTY),
                (isLeft) ? Animation.PlayMode.LOOP : Animation.PlayMode.LOOP_REVERSED));

        animMap.put(FAST_SPIN_STATE, new Animation<TextureRegion>(0.075f, atlas.findRegions(TextureStrings.KUGELDUSCHE_LASER),
                (isLeft) ? Animation.PlayMode.LOOP : Animation.PlayMode.LOOP_REVERSED));


        bag.add(new AnimationComponent(animMap));


        TextureRegionComponent trc = new TextureRegionComponent(atlas.findRegion(TextureStrings.KUGELDUSCHE_EMPTY),
                width, height, TextureRegionComponent.ENEMY_LAYER_MIDDLE);

        trc.color = new Color(Color.BLACK);
        trc.DEFAULT = new Color(Color.BLACK);

        bag.add(trc);
       // bag.add(new WeaponComponent(new KugelWeapon(left), 0.5f));

        LaserOrbitalTask.LaserBuilder laserBuilder =  new LaserOrbitalTask.LaserBuilder(assetManager)
                .angles(isLeft ? laserAnglesAntiClockWise : laserAnglesClockWise)
                .orbitalAndIntervalSize(laserSquareSize)
                .speedInDegrees(isLeft ? speedInDegrees : -speedInDegrees)
                .numberOfOrbitals(15)
                .chargeTime(1.5f)
                .disperseTime(0.5f);

        LaserOrbitalTask laserOrbitalTask = laserBuilder.build();

        LaserOrbitalTask secondLaserOrbitalTask = laserBuilder
                .angles(!isLeft ? laserAnglesAntiClockWise : laserAnglesClockWise)
                .speedInDegrees(!isLeft ? speedInDegrees : -speedInDegrees)
                .build();


        com.bryjamin.wickedwizard.ecs.components.ai.PhaseComponent pc = new com.bryjamin.wickedwizard.ecs.components.ai.PhaseComponent();
        pc.addPhase(sprayAndPrayPhaseTime, new SprayAndPrayPhase(isLeft));
        pc.addPhase(gapBetweenPhases, startToSpin());
        pc.addPhase(laserPhaseTime,laserOrbitalTask);
        pc.addPhase(sprayAndPrayPhaseTime, new SprayAndPrayPhase(!isLeft));
        pc.addPhase(gapBetweenPhases, startToSpin());
        pc.addPhase(laserPhaseTime, secondLaserOrbitalTask);
        bag.add(pc);



        return bag;


    }

    public Task startToSpin(){
        return new Task() {
            @Override
            public void performAction(World world, Entity e) {
                e.getComponent(AnimationStateComponent.class).setDefaultState(FAST_SPIN_STATE);
            }

            @Override
            public void cleanUpAction(World world, Entity e) {

            }
        };
    }


    private class SprayAndPrayPhase implements Task {

        private com.bryjamin.wickedwizard.factories.weapons.enemy.MultiPistol pistol;

        private boolean turnsLeft;


        public SprayAndPrayPhase(boolean turnsLeft){
            pistol = new MultiPistol.PistolBuilder(assetManager)
                    .fireRate(fireRate)
                    .angles(0,45,90,135,180,225,270,315)
                    .shotScale(4f)
                    .shotSpeed(shotSpeed)
                    .build();

            this.turnsLeft = turnsLeft;
        }



        @Override
        public void performAction(World world, Entity e) {
            e.getComponent(AnimationStateComponent.class).setDefaultState(SLOW_SPIN_STATE);
            e.edit().add(new WeaponComponent(pistol, weaponStartUpTime));
            e.edit().add(new FiringAIComponent(0));

            e.edit().add(new ActionAfterTimeComponent(new Action() {
                @Override
                public void performAction(World world, Entity e) {
                    e.getComponent(FiringAIComponent.class).firingAngleInRadians += Math.toRadians((turnsLeft) ? changeInFiringAngleInDegrees : -changeInFiringAngleInDegrees);
                }
            }, fireRate, true));

        }

        @Override
        public void cleanUpAction(World world, Entity e) {
            e.edit().remove(WeaponComponent.class);
            e.edit().remove(FiringAIComponent.class);
            e.edit().remove(ActionAfterTimeComponent.class);
        }
    }


}
