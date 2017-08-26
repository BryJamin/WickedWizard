package com.byrjamin.wickedwizard.factories.enemy.bosses;

import com.artemis.Component;
import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IntMap;
import com.byrjamin.wickedwizard.assets.TextureStrings;
import com.byrjamin.wickedwizard.ecs.components.ai.InCombatActionComponent;
import com.byrjamin.wickedwizard.ecs.components.identifiers.ArenaLockComponent;
import com.byrjamin.wickedwizard.ecs.components.identifiers.BossComponent;
import com.byrjamin.wickedwizard.ecs.components.identifiers.EnemyComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.CollisionBoundComponent;
import com.byrjamin.wickedwizard.ecs.components.Weapon;
import com.byrjamin.wickedwizard.ecs.components.WeaponComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.Action;
import com.byrjamin.wickedwizard.ecs.components.ai.ActionAfterTimeComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.Condition;
import com.byrjamin.wickedwizard.ecs.components.ai.ConditionalActionComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.ExpireComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.FiringAIComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.FollowPositionComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.OnDeathActionComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.PhaseComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.Task;
import com.byrjamin.wickedwizard.ecs.components.identifiers.ChildComponent;
import com.byrjamin.wickedwizard.ecs.components.identifiers.IntangibleComponent;
import com.byrjamin.wickedwizard.ecs.components.identifiers.ParentComponent;
import com.byrjamin.wickedwizard.ecs.components.identifiers.UnpackableComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.PositionComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.AnimationComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.AnimationStateComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.FadeComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.TextureRegionComponent;
import com.byrjamin.wickedwizard.ecs.systems.FindChildSystem;
import com.byrjamin.wickedwizard.ecs.systems.FindPlayerSystem;
import com.byrjamin.wickedwizard.ecs.systems.graphical.CameraSystem;
import com.byrjamin.wickedwizard.ecs.systems.level.RoomTransitionSystem;
import com.byrjamin.wickedwizard.factories.arenas.Arena;
import com.byrjamin.wickedwizard.factories.arenas.decor.ArenaShellFactory;
import com.byrjamin.wickedwizard.factories.arenas.presetmaps.EndGameMap;
import com.byrjamin.wickedwizard.factories.enemy.EnemyFactory;
import com.byrjamin.wickedwizard.factories.weapons.enemy.LaserBeam;
import com.byrjamin.wickedwizard.factories.weapons.enemy.MultiPistol;
import com.byrjamin.wickedwizard.utils.BagSearch;
import com.byrjamin.wickedwizard.utils.CenterMath;
import com.byrjamin.wickedwizard.utils.ComponentBag;
import com.byrjamin.wickedwizard.utils.Measure;
import com.byrjamin.wickedwizard.utils.collider.HitBox;

import java.util.Random;

/**
 * Created by Home on 09/07/2017.
 */

public class BossEnd extends EnemyFactory {

    private static final float mainBodyHeight = Measure.units(24f);
    private static final float mainBodyWidth = Measure.units(24f);

    private static final float mainBodyHealth = 40;

    private static final float handHealth = 40;

    private static final float handsHeight = Measure.units(12f);
    private static final float handsWidth = Measure.units(12f);

    private static final float handsTextureWidth = Measure.units(15f);
    private static final float handsTextureHeight = Measure.units(15f);

    private static final float bottomHandOffsetX = Measure.units(30f);
    private static final float bottomHandOffsetY = -Measure.units(5);

    private static final float upperHandOffsetX = Measure.units(20f);
    private static final float upperHandOffsetY = Measure.units(20f);


    private static final float handCharingTime = 2.0f;


    //Hand States
    private static final int FLOATING_STATE = 0;
    private static final int CHARGING_STATE = 2;
    private static final int FIRING_STATE = 4;



    //SplitterWeapon
    private static final float firstSplitterSpeed = Measure.units(20f);
    private static final float firstSplitterRange = Measure.units(25f);
    private static final float secondSplitterSpeed = Measure.units(20f);
    private static final float secondSplitterRange = Measure.units(15f);
    private static final float finalSplitterSpeed = Measure.units(50f);;


    private static final float fauxPhaseTime = 100f;

    private static final float timeBetweenLaserFiredForDeathFromAbove = 0.25f;


    //LaserDancePhase
    private static final float numberOfLasersFired = 4;
    private static final float timeBetweenLaserFiredForDance = 1.25f;


    //PhaseTimers
    private static final float splitterPhase = 5.0f;
    private static final float deathFromAbovePhase = 7.5f;
    private static final float laserDancePhase = (numberOfLasersFired + 1) * timeBetweenLaserFiredForDance;
    private static final float gatlingPhase = 7.5f;




    private static final Random random = new Random();

    public BossEnd(AssetManager assetManager) {
        super(assetManager);
    }



    public ComponentBag end(float x, float y){

        x = x - mainBodyWidth / 2;
        y = y - mainBodyHeight / 2;

        ComponentBag bag = this.defaultBossBag(new ComponentBag(), x, y, mainBodyHealth);
        BagSearch.getObjectOfTypeClass(OnDeathActionComponent.class, bag);
        BagSearch.removeObjectOfTypeClass(OnDeathActionComponent.class, bag);
        BagSearch.removeObjectOfTypeClass(BossComponent.class, bag);
        BagSearch.removeObjectOfTypeClass(EnemyComponent.class, bag);
        bag.add(new ArenaLockComponent());

        bag.add(new OnDeathActionComponent(new Action() {
            @Override
            public void performAction(World world, Entity e) {
                Entity flash = bossEndFlash(world);

                flash.edit().add(new UnpackableComponent());

                deathClone(world, e);
            }
        }));

        bag.add(new CollisionBoundComponent(new Rectangle(x, y, mainBodyWidth, mainBodyHeight)));

        bag.add(new TextureRegionComponent(atlas.findRegion(TextureStrings.BLOCK),
                mainBodyWidth,
                mainBodyHeight,
                TextureRegionComponent.ENEMY_LAYER_MIDDLE));

        bag.add(new IntangibleComponent());
        bag.add(new ParentComponent());


        bag.add(new ActionAfterTimeComponent(new Action() {
            @Override
            public void performAction(World world, Entity e) {

                CollisionBoundComponent playerCbc = world.getSystem(FindPlayerSystem.class).getPlayerComponent(CollisionBoundComponent.class);
                PositionComponent playerPosition = world.getSystem(FindPlayerSystem.class).getPlayerComponent(PositionComponent.class);
                CollisionBoundComponent endCbc = e.getComponent(CollisionBoundComponent.class);


                playerCbc.setCenter(endCbc.getCenterX(), endCbc.getCenterY());
                playerPosition.position.set(playerCbc.bound.x, playerCbc.bound.y, playerPosition.position.z);

                e.edit().add(new BossComponent());

                e.edit().remove(ActionAfterTimeComponent.class);
                e.edit().add(new ActionAfterTimeComponent(new SummonArms(), 0.5f));


                world.getSystem(CameraSystem.class).snapCameraUpdate(playerCbc);

            }
        }, 0, true));

        return bag;

    }



    private ConditionalActionComponent summonArmsConditional() {

        return new ConditionalActionComponent(new Condition() {
            @Override
            public boolean condition(World world, Entity entity) {
                return entity.getComponent(ParentComponent.class).children.size <= 0;
            }
        }, new Action() {
            @Override
            public void performAction(World world, Entity e) {
                e.edit().remove(PhaseComponent.class);

                e.edit().add(new FadeComponent(true, 0.5f, false, e.getComponent(TextureRegionComponent.class).color.a, 1));

                e.getComponent(CollisionBoundComponent.class).hitBoxes.add(new HitBox(new Rectangle(e.getComponent(PositionComponent.class).getX(),e.getComponent(PositionComponent.class).getY(), mainBodyWidth, mainBodyHeight)));
                e.edit().remove(ConditionalActionComponent.class);
                e.edit().add(new ActionAfterTimeComponent(new SummonArms(), 10f));
            }
        });
    }


    private class SummonArms implements Action {
        @Override
        public void performAction(World world, Entity e) {

            createLowerLeftHand(world, e);
            createLowerRightHand(world, e);
            createUpperLeftHand(world, e);
            createUpperRightHand(world, e);

            e.edit().add(new FadeComponent(false, 0.5f, false, 0.5f, 1));


            PhaseComponent phaseComponent = new PhaseComponent();
            phaseComponent.addPhase(fauxPhaseTime, new NextHand(random));
            e.edit().add(phaseComponent);

            e.getComponent(CollisionBoundComponent.class).hitBoxes.clear();

            e.edit().add(summonArmsConditional());

        }
    }



    private class NextHand implements Task {

        private Random random;

        Array<ChildComponent> previouslySelectedHands = new Array<ChildComponent>();
        Array<ChildComponent> currentlySelectedHands = new Array<ChildComponent>();


        public NextHand(Random random){
            this.random = random;
        }

        @Override
        public void performAction(World world, Entity e) {

            ParentComponent parentComponent = e.getComponent(ParentComponent.class);
            if(parentComponent.children.size <= 0) return;
            Array<ChildComponent> childComponents = parentComponent.children;

            currentlySelectedHands.clear();
            boolean reset = true;
            for(ChildComponent c : childComponents){
                if(!previouslySelectedHands.contains(c, true)){
                    currentlySelectedHands.add(c);
                    reset = false;
                }
            }

            if(reset){
                currentlySelectedHands.addAll(childComponents);
                previouslySelectedHands.clear();
            }

            ChildComponent cc = currentlySelectedHands.removeIndex(random.nextInt(currentlySelectedHands.size));
            previouslySelectedHands.add(cc);

            Entity child = world.getSystem(FindChildSystem.class).findChildEntity(cc);

            HandAction handAction =  (HandAction) (child.getComponent(WeaponComponent.class).weapon);
            handAction.performHandAction(world, child);
            e.getComponent(PhaseComponent.class).currentPhaseTime = fauxPhaseTime - handAction.getHandPhaseTime();
            //TODO select next hand
            //if hand has no firing component, fire weapon
            //if hadn has firing componenet add firing component and remove it after some time
            //get class of weapon and decidew what to do?
        }

        @Override
        public void cleanUpAction(World world, Entity e) {

        }


    }






    private Task chargingAnimationTask(){
        return new Task() {
            @Override
            public void performAction(World world, Entity e) {
                e.getComponent(AnimationStateComponent.class).setDefaultState(CHARGING_STATE);
            }

            @Override
            public void cleanUpAction(World world, Entity e) {
                e.getComponent(AnimationStateComponent.class).setDefaultState(FLOATING_STATE);
            }
        };
    }





    private Entity createUpperLeftHand(World world, Entity parent){
        PositionComponent parentPositionCompnent = parent.getComponent(PositionComponent.class);
        Entity hand = createBaseHand(world, parent, true);
        hand.edit().add(new FollowPositionComponent(parentPositionCompnent.position, CenterMath.offsetX(mainBodyWidth, handsWidth) - upperHandOffsetX, upperHandOffsetY));
        hand.edit().add(new WeaponComponent(new EndDeathFromAboveLasers(assetManager, random)));
        return hand;
    }


    private Entity createLowerLeftHand(World world, Entity parent){
        PositionComponent parentPositionCompnent = parent.getComponent(PositionComponent.class);
        Entity hand = createBaseHand(world, parent, true);
        hand.edit().add(new FollowPositionComponent(parentPositionCompnent.position, CenterMath.offsetX(mainBodyWidth, handsWidth) - bottomHandOffsetX, bottomHandOffsetY));
        hand.edit().add(new WeaponComponent(new LeftHandEndSplitterWeapon(assetManager)));
       //
       //  hand.edit().add(new FiringAIComponent(90));
        return hand;
    }

    private Entity createUpperRightHand(World world, Entity parent){
        PositionComponent parentPositionCompnent = parent.getComponent(PositionComponent.class);
        Entity hand = createBaseHand(world, parent, false);
        hand.edit().add(new FollowPositionComponent(parentPositionCompnent.position, CenterMath.offsetX(mainBodyWidth, handsWidth) + upperHandOffsetX, upperHandOffsetY));
        hand.edit().add(new WeaponComponent(new EndGatlingGun(assetManager, random)));
        //hand.edit().add(new FiringAIComponent());
        return hand;
    }

    private Entity createLowerRightHand(World world, Entity parent){
        PositionComponent parentPositionCompnent = parent.getComponent(PositionComponent.class);
        Entity hand = createBaseHand(world, parent, false);
        hand.edit().add(new FollowPositionComponent(parentPositionCompnent.position, CenterMath.offsetX(mainBodyWidth, handsWidth) + bottomHandOffsetX, bottomHandOffsetY));
        hand.edit().add(new WeaponComponent(new EndLaserDance(assetManager, random)));
        return hand;

    }


    private Entity createBaseHand(World world, Entity parent, boolean isLeft){

        final float tempPos = -10000f;

        Entity hand = world.createEntity();

        for(Component c : this.defaultEnemyBag(new ComponentBag(), tempPos, tempPos, handHealth)){
            hand.edit().add(c);
        }

        hand.edit().add(new CollisionBoundComponent(new Rectangle(tempPos, tempPos, handsWidth, handsHeight)));
        hand.edit().add(new BossComponent());
        TextureRegionComponent trc = new TextureRegionComponent(atlas.findRegion(TextureStrings.BLOCK),
                CenterMath.offsetX(handsWidth, handsTextureWidth),
                CenterMath.offsetY(handsHeight, handsTextureHeight),
                handsTextureWidth, handsTextureHeight,
                TextureRegionComponent.ENEMY_LAYER_NEAR);

        trc.scaleX = isLeft ? 1 : -1;

        hand.edit().add(trc);



        hand.edit().add(new FadeComponent(true, 1f, false));

        hand.edit().add(new ActionAfterTimeComponent(new Action() {
            @Override
            public void performAction(World world, Entity e) {
                e.getComponent(CollisionBoundComponent.class).hitBoxes.add(new HitBox(new Rectangle(tempPos, tempPos, handsWidth, handsHeight)));
            }
        }, 1f));

        hand.edit().add(new ChildComponent(parent.getComponent(ParentComponent.class)));


        hand.edit().add(new AnimationStateComponent(FLOATING_STATE));
        IntMap<Animation<TextureRegion>> animMap = new IntMap<Animation<TextureRegion>>();
        animMap.put(FLOATING_STATE, new Animation<TextureRegion>(1f / 10f, atlas.findRegions(TextureStrings.END_HAND), Animation.PlayMode.LOOP));
        animMap.put(CHARGING_STATE, new Animation<TextureRegion>(1f / 18f, atlas.findRegions(TextureStrings.END_HAND_CHARGING), Animation.PlayMode.LOOP));
        animMap.put(FIRING_STATE, new Animation<TextureRegion>(1f / 10f, atlas.findRegions(TextureStrings.END_HAND_FIRING), Animation.PlayMode.LOOP));
        animMap.put(AnimationStateComponent.FIRING, new Animation<TextureRegion>(1 / 10f, atlas.findRegions(TextureStrings.END_HAND_FIRING)));
        hand.edit().add(new AnimationComponent(animMap));


        return hand;

    }

    private interface HandAction {
        void performHandAction(World world, Entity hand);
        float getHandPhaseTime();
    }


    private class LeftHandEndSplitterWeapon implements Weapon, HandAction {

        private Weapon weapoonFiredOnBulletDeath;

        private Weapon firstSplitter;
        private Weapon secondSplitter;

        int[] firstSplitAngles = new int[]{90, -35, 215};

        public LeftHandEndSplitterWeapon(AssetManager assetManager){
            this.weapoonFiredOnBulletDeath = new MultiPistol.PistolBuilder(assetManager)
                    .angles(0,30,60,90,120,150,180,210,240,270,300,330)
                    .shotScale(2)
                    .shotSpeed(finalSplitterSpeed)
                    .intangible(true)
                    .expire(true)
                    //.expireRange(Measure.units(50f))
                    .build();


            this.secondSplitter = new MultiPistol.PistolBuilder(assetManager)
                    .angles(0)
                    .intangible(true)
                    .expireRange(secondSplitterRange)
                    .shotSpeed(secondSplitterSpeed)
                    .shotScale(4)
                    .customOnDeathAction(new OnDeathActionComponent(new Action() {
                        @Override
                        public void performAction(World world, Entity e) {
                            CollisionBoundComponent cbc = e.getComponent(CollisionBoundComponent.class);
                            weapoonFiredOnBulletDeath.fire(world,e ,cbc.getCenterX(),cbc.getCenterY(),0);
                        }
                    }))
                    .build();


            this.firstSplitter = new MultiPistol.PistolBuilder(assetManager)
                    .angles(0)
                    .intangible(true)
                    .expireRange(firstSplitterRange)
                    .shotSpeed(firstSplitterSpeed)
                    .shotScale(8)
                    .customOnDeathAction(new OnDeathActionComponent(new Action() {
                        @Override
                        public void performAction(World world, Entity e) {
                            CollisionBoundComponent cbc = e.getComponent(CollisionBoundComponent.class);
                            //gibletFactory.defaultGiblets(new Color(Color.RED));
                            for(int i : firstSplitAngles) {
                                double angleInRadians = Math.toRadians(i);
                                secondSplitter.fire(world, e, cbc.getCenterX(), cbc.getCenterY(), angleInRadians);
                            }
                        }
                    }))
                    .build();

        }

        @Override
        public void fire(World world, Entity e, float x, float y, double angleInRadians) {
            firstSplitter.fire(world, e, x, y, angleInRadians);
        }

        @Override
        public float getBaseFireRate() {
            return 5;
        }

        @Override
        public float getBaseDamage() {
            return 0;
        }

        @Override
        public void performHandAction(World world, Entity hand) {
            PhaseComponent phaseComponent = new PhaseComponent();
            phaseComponent.addPhase(handCharingTime, chargingAnimationTask());
            phaseComponent.addPhase(getHandPhaseTime(), new Task() {
                @Override
                public void performAction(World world, Entity e) {
                    CollisionBoundComponent cbc = e.getComponent(CollisionBoundComponent.class);
                    e.getComponent(WeaponComponent.class).weapon.fire(world,e ,cbc.getCenterX(),cbc.getCenterY(), Math.toRadians(90));
                }

                @Override
                public void cleanUpAction(World world, Entity e) {
                    e.edit().remove(PhaseComponent.class);
                }
            });

            hand.edit().add(phaseComponent);
        }

        @Override
        public float getHandPhaseTime() {
            return splitterPhase;
        }
    }


    /**
     * Queues up 10 lasers and then fires then in the same order
     *
     * To do, as the boss spose
     *
     *
     *
     */
    private class EndDeathFromAboveLasers implements Weapon, HandAction {

        private LaserBeam laserBeam;
        private Random random;


        private float offSetFromBoss = Measure.units(10f);

        //TODO this weapon should take in the bosses position and then use it when centering the lasers
        //TODO as the player should be stuck in the middle of the boss
        //TODO so the lasers don't need to cover the whole room

        private Array<Integer> positionIndentifierArray = new Array<Integer>();

        public EndDeathFromAboveLasers(AssetManager assetManager, Random random){

            laserBeam = new LaserBeam.LaserBeamBuilder(assetManager)
                    .chargingLaserWidth(Measure.units(5f))
                    .chargingLaserHeight(Measure.units(200f))
                    .activeLaserWidth(Measure.units(7.5f))
                    .activeLaserHeight(Measure.units(200f))
                    .chargingLaserTime(4f)
                    .layer(TextureRegionComponent.FOREGROUND_LAYER_NEAR)
                    .build();
            this.random = random;
            positionIndentifierArray.addAll(0,1,2,3,4,5,6,7,8,9,10);

        }

        //TODO maybe add two safe spots but they can not be adjacent

        @Override
        public void fire(World world, Entity e, float x, float y, double angleInRadians) {


            //e.getComponent(AnimationStateComponent.class).queueAnimationState(FIRING_STATE);

            positionIndentifierArray.shuffle();
            Entity entityForLaserAction = world.createEntity();

            entityForLaserAction.edit().add(new ExpireComponent(20f));
            entityForLaserAction.edit().add(new ActionAfterTimeComponent(new Action() {

                int count = 0;
                @Override
                public void performAction(World world, Entity e) {
                    if(count < 10) {
                        laserBeam.createBeam(world, offSetFromBoss + Measure.units(positionIndentifierArray.get(count) * 7.5f), -Measure.units(50f));
                        count++;
                    }
                }
            }, timeBetweenLaserFiredForDeathFromAbove, true));
/*            for(Integer positionIndentifier : positionIndentifierArray){
            }*/
        }

        @Override
        public float getBaseFireRate() {
            return 70;
        }

        @Override
        public float getBaseDamage() {
            return 0;
        }

        @Override
        public void performHandAction(World world, Entity hand) {

            PhaseComponent phaseComponent = new PhaseComponent();
            phaseComponent.addPhase(handCharingTime, chargingAnimationTask());
            phaseComponent.addPhase(getHandPhaseTime(), new Task() {
                @Override
                public void performAction(World world, Entity e) {
                    CollisionBoundComponent cbc = e.getComponent(CollisionBoundComponent.class);
                    e.getComponent(AnimationStateComponent.class).setDefaultState(FIRING_STATE);
                    e.getComponent(WeaponComponent.class).weapon.fire(world,e ,cbc.getCenterX(),cbc.getCenterY(), 0);
                }

                @Override
                public void cleanUpAction(World world, Entity e) {
                    e.getComponent(AnimationStateComponent.class).setDefaultState(AnimationStateComponent.DEFAULT);
                    e.edit().remove(PhaseComponent.class);
                }
            });

            hand.edit().add(phaseComponent);

        }

        @Override
        public float getHandPhaseTime() {
            return deathFromAbovePhase;
        }
    }





    private class EndLaserDance implements Weapon, HandAction {

        private LaserBeam sideBeam;
        private LaserBeam verticalBeam;
        private Random random;

        private boolean isTopHorizontalBeam = false;
        private boolean isHorizontalBeamPhase = false;


        private float offSetFromBoss;


        public EndLaserDance(AssetManager assetManager, Random random){

            verticalBeam = new LaserBeam.LaserBeamBuilder(assetManager)
                    .chargingLaserWidth(Measure.units(7.5f))
                    .chargingLaserHeight(Measure.units(100f))
                    .activeLaserWidth(Measure.units(12.5f))
                    .activeLaserHeight(Measure.units(100f))
                    .centerLaserUsingWidth(true)
                    .chargingLaserTime(1f)
                    .build();

            sideBeam = new LaserBeam.LaserBeamBuilder(assetManager)
                    .chargingLaserWidth(Measure.units(100f))
                    .chargingLaserHeight(Measure.units(7.5f))
                    .activeLaserWidth(Measure.units(100f))
                    .activeLaserHeight(Measure.units(12.5f))
                    .centerLaserUsingWidth(false)
                    .chargingLaserTime(1f)
                    .build();

            this.random = random;

        }

        @Override
        public void fire(World world, Entity e, float x, float y, double angleInRadians) {

            Entity entityForLaserAction = world.createEntity();

            entityForLaserAction.edit().add(new ExpireComponent(20f));
            entityForLaserAction.edit().add(new ActionAfterTimeComponent(new Action() {

                int count = 0;
                @Override
                public void performAction(World world, Entity e) {
                    if(count < numberOfLasersFired) {


                        //sideBeam.createBeam(world, 0, Measure.units(15f));

                        if(isTopHorizontalBeam){
                            sideBeam.createBeam(world, 0, Measure.units(35f));
                            isTopHorizontalBeam = !isTopHorizontalBeam;

                            if(random.nextBoolean()){
                                verticalBeam.createBeam(world, Measure.units(35f), 0);
                            } else {
                                verticalBeam.createBeam(world, Measure.units(50f), 0);
                            }

                        } else {
                            sideBeam.createBeam(world, 0, Measure.units(15f));
                            isTopHorizontalBeam = !isTopHorizontalBeam;
                        }

                        //laserBeam.createBeam(world, , 0);
                    }
                    count++;
                }
            }, 0, timeBetweenLaserFiredForDance, true));
/*            for(Integer positionIndentifier : positionIndentifierArray){
            }*/
        }

        @Override
        public float getBaseFireRate() {
            return 70;
        }

        @Override
        public float getBaseDamage() {
            return 0;
        }


        @Override
        public void performHandAction(World world, Entity hand) {



            PhaseComponent phaseComponent = new PhaseComponent();
            phaseComponent.addPhase(handCharingTime, chargingAnimationTask());
            phaseComponent.addPhase(getHandPhaseTime(), new Task() {
                @Override
                public void performAction(World world, Entity e) {
                    CollisionBoundComponent cbc = e.getComponent(CollisionBoundComponent.class);
                    e.getComponent(AnimationStateComponent.class).setDefaultState(FIRING_STATE);
                    e.getComponent(WeaponComponent.class).weapon.fire(world,e ,cbc.getCenterX(),cbc.getCenterY(), 0);
                }

                @Override
                public void cleanUpAction(World world, Entity e) {
                    e.getComponent(AnimationStateComponent.class).setDefaultState(FLOATING_STATE);
                    e.edit().remove(PhaseComponent.class);
                }
            });


            hand.edit().add(phaseComponent);
        }

        @Override
        public float getHandPhaseTime() {
            return laserDancePhase;
        }
    }








    private class EndGatlingGun implements Weapon, HandAction {

        private Weapon pistol;
        private Random random;

        int count = 0;


        public EndGatlingGun(AssetManager assetManager, Random random){
            pistol = new MultiPistol.PistolBuilder(assetManager)
                    .shotScale(1.5f)
                    .shotSpeed(Measure.units(30f))
                    .intangible(true)
                    .bulletOffsets(Measure.units(2.5f))
                    .expireRange(Measure.units(100f))
                    .build();
            this.random = random;
        }

        @Override
        public void fire(World world, Entity e, float x, float y, double angleInRadians) {
            double varianace = Math.toRadians(random.nextInt(30) - 15);


            if(count % 3 == 0) {
                pistol.fire(world, e, x, y, angleInRadians + varianace);
            } else {



                varianace = Math.toRadians(random.nextInt(20) + 20);

                if(count % 2 == 0){
                    pistol.fire(world, e, x, y, angleInRadians + varianace);
                } else {

                    pistol.fire(world, e, x, y, angleInRadians - varianace);

                }


            }



            count++;
        }

        @Override
        public float getBaseFireRate() {
            return 0.25f;
        }

        @Override
        public float getBaseDamage() {
            return 0;
        }


        @Override
        public void performHandAction(World world, Entity hand) {
            CollisionBoundComponent cbc = hand.getComponent(CollisionBoundComponent.class);


            PhaseComponent phaseComponent = new PhaseComponent();
            phaseComponent.addPhase(handCharingTime, chargingAnimationTask());
            phaseComponent.addPhase(gatlingPhase, new Task() {
                @Override
                public void performAction(World world, Entity e) {
                    e.edit().add(new FiringAIComponent());
                }

                @Override
                public void cleanUpAction(World world, Entity e) {
                    e.edit().remove(FiringAIComponent.class);
                    e.edit().remove(PhaseComponent.class);
                }
            });


            hand.edit().add(phaseComponent);

        }

        @Override
        public float getHandPhaseTime() {
            return gatlingPhase;
        }
    }







}
