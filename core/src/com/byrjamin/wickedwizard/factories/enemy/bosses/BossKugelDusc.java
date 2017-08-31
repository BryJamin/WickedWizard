package com.byrjamin.wickedwizard.factories.enemy.bosses;

import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.IntMap;
import com.byrjamin.wickedwizard.assets.TextureStrings;
import com.byrjamin.wickedwizard.ecs.components.movement.CollisionBoundComponent;
import com.byrjamin.wickedwizard.ecs.components.WeaponComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.Action;
import com.byrjamin.wickedwizard.ecs.components.ai.Task;
import com.byrjamin.wickedwizard.ecs.components.ai.ActionAfterTimeComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.FiringAIComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.PhaseComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.AnimationComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.AnimationStateComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.TextureRegionComponent;
import com.byrjamin.wickedwizard.factories.enemy.EnemyFactory;
import com.byrjamin.wickedwizard.factories.weapons.enemy.LaserOrbitalTask;
import com.byrjamin.wickedwizard.factories.weapons.enemy.MultiPistol;
import com.byrjamin.wickedwizard.utils.ComponentBag;
import com.byrjamin.wickedwizard.utils.Measure;

import java.util.Random;

/**
 * Created by Home on 27/05/2017.
 */

public class BossKugelDusc extends EnemyFactory{

    private static final float width = Measure.units(30f);
    private static final float height = Measure.units(30f);

    private static final float health = 90;

    //SPRAY

    private static final float sprayAndPrayPhaseTime = 10f;

    private static final float weaponStartUpTime = 0.5f;
    private static final float changeInFiringAngleInDegrees = 25;
    private static final float shotSpeed = Measure.units(35f);
    private static final float fireRate = 0.5f;



    //LaserPhase
    private static final float laserSquareSize = Measure.units(8.5f);
    private static final float speedInDegrees = 0.5f;

    private static final float laserPhaseTime = 12.5f;


    public BossKugelDusc(AssetManager assetManager) {
        super(assetManager);
    }


    public ComponentBag giantKugelDusche(float x, float y) {
        return giantKugelDusche(x, y, new Random().nextBoolean());
    };

    public ComponentBag giantKugelDusche(float x, float y, boolean isLeft){

        final boolean left = isLeft;

        x = x - width / 2;
        y = y - height / 2;

        ComponentBag bag = this.defaultBossBag(new ComponentBag(), x , y, health);
        bag.add(new CollisionBoundComponent(new Rectangle(x, y, width, height), true));


        bag.add(new AnimationStateComponent(0));
        IntMap<Animation<TextureRegion>> animMap = new IntMap<Animation<TextureRegion>>();
        animMap.put(0, new Animation<TextureRegion>(0.1f / 1f, atlas.findRegions(TextureStrings.KUGELDUSCHE_EMPTY),
                (left) ? Animation.PlayMode.LOOP : Animation.PlayMode.LOOP_REVERSED));


        bag.add(new AnimationComponent(animMap));


        TextureRegionComponent trc = new TextureRegionComponent(atlas.findRegion(TextureStrings.KUGELDUSCHE_EMPTY),
                width, height, TextureRegionComponent.ENEMY_LAYER_MIDDLE);

        trc.color = new Color(Color.BLACK);
        trc.DEFAULT = new Color(Color.BLACK);

        bag.add(trc);
       // bag.add(new WeaponComponent(new KugelWeapon(left), 0.5f));

        LaserOrbitalTask.LaserBuilder laserBuilder =  new LaserOrbitalTask.LaserBuilder(assetManager)
                .angles(-10,80,170, 260)
                .orbitalAndIntervalSize(laserSquareSize)
                .speedInDegrees(isLeft ? speedInDegrees : -speedInDegrees)
                .numberOfOrbitals(15)
                .chargeTime(1.5f)
                .disperseTime(0.5f);

        LaserOrbitalTask laserOrbitalTask = laserBuilder.build();

        LaserOrbitalTask secondLaserOrbitalTask = laserBuilder
                .speedInDegrees(!isLeft ? speedInDegrees : -speedInDegrees)
                .build();


        PhaseComponent pc = new PhaseComponent();
        pc.addPhase(sprayAndPrayPhaseTime, new SprayAndPrayPhase(left));
        pc.addPhase(laserPhaseTime,laserOrbitalTask);
        pc.addPhase(sprayAndPrayPhaseTime, new SprayAndPrayPhase(!left));
        pc.addPhase(laserPhaseTime, secondLaserOrbitalTask);
        bag.add(pc);



        return bag;


    }


    private class SprayAndPrayPhase implements Task {

        private MultiPistol pistol;

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
