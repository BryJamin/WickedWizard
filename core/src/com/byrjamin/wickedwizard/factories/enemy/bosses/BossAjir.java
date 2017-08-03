package com.byrjamin.wickedwizard.factories.enemy.bosses;

import com.artemis.Aspect;
import com.artemis.Entity;
import com.artemis.World;
import com.artemis.utils.IntBag;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IntMap;
import com.byrjamin.wickedwizard.assets.TextureStrings;
import com.byrjamin.wickedwizard.ecs.components.CollisionBoundComponent;
import com.byrjamin.wickedwizard.ecs.components.Weapon;
import com.byrjamin.wickedwizard.ecs.components.WeaponComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.Action;
import com.byrjamin.wickedwizard.ecs.components.ai.ActionAfterTimeComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.FiringAIComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.OnDeathActionComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.PhaseComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.Task;
import com.byrjamin.wickedwizard.ecs.components.identifiers.BulletComponent;
import com.byrjamin.wickedwizard.ecs.components.identifiers.EnemyComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.BounceComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.VelocityComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.AnimationComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.AnimationStateComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.TextureRegionComponent;
import com.byrjamin.wickedwizard.ecs.systems.ai.OnDeathSystem;
import com.byrjamin.wickedwizard.factories.enemy.EnemyFactory;
import com.byrjamin.wickedwizard.factories.weapons.Giblets;
import com.byrjamin.wickedwizard.factories.weapons.enemy.LaserBeam;
import com.byrjamin.wickedwizard.factories.weapons.enemy.MultiPistol;
import com.byrjamin.wickedwizard.utils.CenterMath;
import com.byrjamin.wickedwizard.utils.ComponentBag;
import com.byrjamin.wickedwizard.utils.Measure;
import com.byrjamin.wickedwizard.utils.collider.HitBox;

import java.util.Random;

/**
 * Created by Home on 07/07/2017.
 */

public class BossAjir extends EnemyFactory{


    private static final float width = Measure.units(25f);
    private static final float height = Measure.units(25f);

    private static final float textureWidth = Measure.units(25);
    private static final float textureHeight = Measure.units(25f);


    private static final float bodyHitBoxWidth = Measure.units(7f);
    private static final float bodyHitBoxHeight = Measure.units(20f);

    private static final float armsHitBoxWidth = Measure.units(14);
    private static final float armsHitBoxHeight = Measure.units(5);
    private static final float armsHitBoxOffsetY = Measure.units(12.5f);

    private static final float chargingLaserWidth = Measure.units(5f);
    private static final float chargingLaserHeight = Measure.units(100f);
    private static final float activeLaserWidth = Measure.units(7.5f);
    private static final float activeLaserHeight = Measure.units(100f);

    private static final float activeLaserTime = 0.4f;
    private static final float chargingLaserTime = 1.6f;

    private static final float speed = Measure.units(10f);



    private static final float health = 80;

    private static final float timeBetweenSplitAction = 1.0f;
    private static final float timeBetweenLasers = 1.3f;

    private static final float shotSpeed = Measure.units(25f);

    //Phase
    private static final float deathFromAbovePhase = 10.0f;
    private static final float bulletSplitterWeaponPhase = 10.0f;


    private static final int NORMAL_STATE = 0;
    private static final int FIRING_STATE = 2;
    private static final int FIRING_LASERS_STATE = 4;


    private Giblets.GibletBuilder splitterWeaponGiblets;


    private Random random;

    public BossAjir(AssetManager assetManager) {
        super(assetManager);
        this.random = new Random();

        this.splitterWeaponGiblets = new Giblets.GibletBuilder(assetManager)
                .numberOfGibletPairs(6)
                .size(Measure.units(0.75f))
                .fadeChance(0.5f)
                .minSpeed(Measure.units(30f))
                .maxSpeed(Measure.units(40f))
                .colors(new Color(Color.RED))
                .intangible(false)
                .expiryTime(0.4f);

    }

    public ComponentBag ajir(float x, float y){

        x = x - width / 2;
        y = y - height / 2;

        ComponentBag bag = this.defaultEnemyBagNoLootNoDeath(new ComponentBag(), x, y, health);

        bag.add(new OnDeathActionComponent(new Action() {
            @Override
            public void performAction(World world, Entity e) {
                deathClone(world, e);
                IntBag intBag = world.getAspectSubscriptionManager().get(Aspect.all(EnemyComponent.class, BulletComponent.class)).getEntities();
                for(int i = 0; i < intBag.size(); i++){
                    world.getEntity(intBag.get(i)).getComponent(OnDeathActionComponent.class).action = splitterWeaponGiblets.build();
                    world.getSystem(OnDeathSystem.class).kill(world.getEntity(intBag.get(i)));
                }
                bossEndFlash(world);
            }
        }));

        bag.add(new CollisionBoundComponent(new Rectangle(x, y, width, height),
                new HitBox(new Rectangle(x, y, bodyHitBoxWidth, bodyHitBoxHeight),
                        CenterMath.offsetX(width, bodyHitBoxWidth),
                        CenterMath.offsetY(height, bodyHitBoxHeight)),
                new HitBox(new Rectangle(x, y, armsHitBoxWidth, armsHitBoxHeight),
                        CenterMath.offsetX(width, armsHitBoxWidth),
                        armsHitBoxOffsetY)));

        bag.add(new TextureRegionComponent(atlas.findRegion(TextureStrings.AJIR),
                CenterMath.offsetX(width, textureWidth),
                CenterMath.offsetY(height, textureHeight),
                textureWidth, textureHeight, TextureRegionComponent.ENEMY_LAYER_MIDDLE));

        bag.add(new AnimationStateComponent(NORMAL_STATE));
        IntMap<Animation<TextureRegion>> animMap = new IntMap<Animation<TextureRegion>>();
        animMap.put(NORMAL_STATE, new Animation<TextureRegion>(1f / 10f, atlas.findRegions(TextureStrings.AJIR), Animation.PlayMode.LOOP));
        animMap.put(FIRING_STATE, new Animation<TextureRegion>(1f / 10f, atlas.findRegions(TextureStrings.AJIR_FIRING)));
        animMap.put(FIRING_LASERS_STATE, new Animation<TextureRegion>(1f / 10f, atlas.findRegions(TextureStrings.AJIR_FIRING), Animation.PlayMode.LOOP));

        bag.add(new AnimationComponent(animMap));


        PhaseComponent phaseComponent = new PhaseComponent();
        phaseComponent.addPhase(bulletSplitterWeaponPhase, new AjirSplitterWeaponPhase(random));
        phaseComponent.addPhase(deathFromAbovePhase, new AjirDeathFromAbovePhase(random));

        bag.add(phaseComponent);

        return bag;


    }


    private class AjirSplitterWeaponPhase implements Task {


        private Random random;
        private AjirSplitterWeapon ajirSplitterWeapon;

        //TODO change these to different angles once the art is finalized (Directly below is not fair)
        private int[] possibleAngles = new int[]{0, 45, 90, 135, 180};

        public AjirSplitterWeaponPhase(Random random){
            this.random = random;
            this.ajirSplitterWeapon = new AjirSplitterWeapon(assetManager);
        }


        @Override
        public void performAction(World world, Entity e) {

            e.edit().add(new WeaponComponent(ajirSplitterWeapon, 0.5f));
            e.edit().add(new FiringAIComponent(Math.toRadians(possibleAngles[random.nextInt(possibleAngles.length)])));

            e.edit().add(new ActionAfterTimeComponent(new Action() {
                @Override
                public void performAction(World world, Entity e) {
                    e.getComponent(FiringAIComponent.class).firingAngleInRadians = Math.toRadians(possibleAngles[random.nextInt(possibleAngles.length)]);
                }
            }, timeBetweenSplitAction, true));
        }

        @Override
        public void cleanUpAction(World world, Entity e) {
            e.edit().remove(WeaponComponent.class);
            e.edit().remove(FiringAIComponent.class);
            e.edit().remove(ActionAfterTimeComponent.class);
        }
    }




    private class AjirDeathFromAbovePhase implements Task {

        private Random random;

        private LaserBeam laserBeam;

        public AjirDeathFromAbovePhase(Random random){
            this.random = random;

            laserBeam = new LaserBeam.LaserBeamBuilder(assetManager)
                    .chargingLaserWidth(chargingLaserWidth)
                    .chargingLaserHeight(chargingLaserHeight)
                    .activeLaserWidth(activeLaserWidth)
                    .activeLaserHeight(activeLaserHeight)
                    .chargingLaserTime(chargingLaserTime)
                    .activeLaserTime(activeLaserTime)
                    .layer(TextureRegionComponent.FOREGROUND_LAYER_MIDDLE)
                    .build();

        }

        @Override
        public void performAction(World world, Entity e) {

            e.edit().add(new VelocityComponent(random.nextBoolean() ? speed : -speed, 0));
            e.edit().add(new BounceComponent());

            e.edit().add(new ActionAfterTimeComponent(new Action() {

                Array<Integer> firstChoice = new Array<Integer>();
                Array<Float> positions = new Array<Float>();
                Array<Integer> possiblePositions = new Array<Integer>();

                int previousChoiceOne;
                int previousChoiceTwo;

                int numberOfPositions = 11;

                float gapBetweenPositions = 7.5f;

                @Override
                public void performAction(World world, Entity e) {

                    //TODO this might not work actually. Needs to be tested

                    //Picks the first position of the laser duo beam.
                    //Does not pick last first position

                    for(int i=0; i < numberOfPositions; i++){
                        if(i != previousChoiceOne) firstChoice.add(i);
                    }

                    int choice = firstChoice.get(random.nextInt(firstChoice.size));;

                    previousChoiceOne = choice;


                    //Ensures there is atleast a gap of two between lasers and the previous two
                    //positions are not selected again.
                    for(int i=0; i < numberOfPositions; i++){
                        if((i < choice - 2 || i > choice + 2) && i != previousChoiceTwo && i != previousChoiceOne) possiblePositions.add(i);
                    }


                    int secondChoice = possiblePositions.get(random.nextInt(possiblePositions.size));
                    previousChoiceTwo = secondChoice;


                    //Multiples the choices by the gap between positions.  To convert the single digit number
                    //into a position in the arena.
                    positions.add(Measure.units(10f + (choice * gapBetweenPositions)));
                    positions.add(Measure.units(10f + (secondChoice * gapBetweenPositions)));

                    for(Float f : positions) {
                        laserBeam.createBeam(world, f, Measure.units(10f));

                        //createBeam(world, f, Measure.units(10f));
                    }

                    positions.clear();
                    possiblePositions.clear();
                    firstChoice.clear();


                    e.getComponent(AnimationStateComponent.class).queueAnimationState(FIRING_STATE);

                }
            }, timeBetweenLasers, true));




        }

        @Override
        public void cleanUpAction(World world, Entity e) {
            e.edit().remove(ActionAfterTimeComponent.class);
            e.edit().remove(BounceComponent.class);
            e.edit().remove(VelocityComponent.class);
        }


    }


    private class AjirSplitterWeapon implements Weapon {

        private Weapon weapoonFiredOnBulletDeath;

        private Weapon giantBulletThatSplits;

        public AjirSplitterWeapon(AssetManager assetManager){

            this.weapoonFiredOnBulletDeath = new MultiPistol.PistolBuilder(assetManager)
                    .angles(0,30,60,90,120,150,180,210,240,270,300,330)
                    .shotScale(2)
                    .expire(true)
                    .build();

            this.giantBulletThatSplits = new MultiPistol.PistolBuilder(assetManager)
                    .shotScale(10)
                    .shotSpeed(shotSpeed)
                    .customOnDeathAction(new OnDeathActionComponent(new Action() {
                        @Override
                        public void performAction(World world, Entity e) {
                            CollisionBoundComponent cbc = e.getComponent(CollisionBoundComponent.class);
                            //splitterWeaponGiblets.build().performAction(world, e);
                            weapoonFiredOnBulletDeath.fire(world, e , cbc.getCenterX(), cbc.getCenterY(), 0);
                        }
                    }))
                    .build();
        }

        @Override
        public void fire(World world, Entity e, float x, float y, double angleInRadians) {
            giantBulletThatSplits.fire(world, e, x, y, angleInRadians);
        }

        @Override
        public float getBaseFireRate() {
            return timeBetweenSplitAction;
        }

        @Override
        public float getBaseDamage() {
            return 0;
        }
    }






}
