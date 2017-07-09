package com.byrjamin.wickedwizard.factories.enemy.bosses;

import com.artemis.Component;
import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.byrjamin.wickedwizard.assets.TextureStrings;
import com.byrjamin.wickedwizard.ecs.components.CollisionBoundComponent;
import com.byrjamin.wickedwizard.ecs.components.Weapon;
import com.byrjamin.wickedwizard.ecs.components.WeaponComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.Action;
import com.byrjamin.wickedwizard.ecs.components.ai.ActionAfterTimeComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.ExpireComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.ExpiryRangeComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.FiringAIComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.FollowPositionComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.OnDeathActionComponent;
import com.byrjamin.wickedwizard.ecs.components.identifiers.ChildComponent;
import com.byrjamin.wickedwizard.ecs.components.identifiers.EnemyComponent;
import com.byrjamin.wickedwizard.ecs.components.identifiers.IntangibleComponent;
import com.byrjamin.wickedwizard.ecs.components.identifiers.ParentComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.PositionComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.VelocityComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.TextureRegionComponent;
import com.byrjamin.wickedwizard.factories.BulletFactory;
import com.byrjamin.wickedwizard.factories.GibletFactory;
import com.byrjamin.wickedwizard.factories.enemy.EnemyFactory;
import com.byrjamin.wickedwizard.factories.weapons.enemy.LaserBeam;
import com.byrjamin.wickedwizard.factories.weapons.enemy.MultiPistol;
import com.byrjamin.wickedwizard.utils.BulletMath;
import com.byrjamin.wickedwizard.utils.CenterMath;
import com.byrjamin.wickedwizard.utils.ComponentBag;
import com.byrjamin.wickedwizard.utils.Measure;

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


    private static final float bottomHandOffsetX = Measure.units(30f);
    private static final float bottomHandOffsetY = -Measure.units(10f);

    private static final float upperHandOffsetX = Measure.units(20f);
    private static final float upperHandOffsetY = Measure.units(15f);



    //SplitterWeapon
    private static final float firstSplitterSpeed = Measure.units(20f);
    private static final float firstSplitterRange = Measure.units(35f);
    private static final float secondSplitterSpeed = Measure.units(20f);
    private static final float secondSplitterRange = Measure.units(25f);
    private static final float finalSplitterSpeed = Measure.units(50f);;

    private static final Random random = new Random();

    public BossEnd(AssetManager assetManager) {
        super(assetManager);
    }



    public ComponentBag end(float x, float y){

        x = x - mainBodyWidth / 2;
        y = y - mainBodyHeight / 2;

        ComponentBag bag = this.defaultEnemyBag(new ComponentBag(), x, y, mainBodyHealth);

        bag.add(new CollisionBoundComponent(new Rectangle(x, y, mainBodyWidth, mainBodyHeight)));

        bag.add(new TextureRegionComponent(atlas.findRegion(TextureStrings.BLOCK),
                mainBodyWidth,
                mainBodyHeight,
                TextureRegionComponent.ENEMY_LAYER_MIDDLE,
                new Color(Color.WHITE.r, Color.WHITE.g, Color.WHITE.b, 0.5f)));

        bag.add(new IntangibleComponent());
        bag.add(new ParentComponent());


        bag.add(new ActionAfterTimeComponent(new Action() {
            @Override
            public void performAction(World world, Entity e) {
                createLowerLeftHand(world, e);
                createLowerRightHand(world, e);
                createUpperLeftHand(world, e);
                createUpperRightHand(world, e);
            }
        }));




        return bag;



    }







    private Entity createUpperLeftHand(World world, Entity parent){
        PositionComponent parentPositionCompnent = parent.getComponent(PositionComponent.class);
        Entity hand = createBaseHand(world, parent, true);
        hand.edit().add(new FollowPositionComponent(parentPositionCompnent.position, CenterMath.offsetX(mainBodyWidth, handsWidth) - upperHandOffsetX, upperHandOffsetY));
        return hand;
    }


    private Entity createLowerLeftHand(World world, Entity parent){
        PositionComponent parentPositionCompnent = parent.getComponent(PositionComponent.class);
        Entity hand = createBaseHand(world, parent, true);
        hand.edit().add(new FollowPositionComponent(parentPositionCompnent.position, CenterMath.offsetX(mainBodyWidth, handsWidth) - bottomHandOffsetX, bottomHandOffsetY));
        //hand.edit().add(new WeaponComponent(new EndSplitterWeapon(assetManager)));
        hand.edit().add(new WeaponComponent(new EndDeathFromAboveLasers(assetManager, random)));
        hand.edit().add(new FiringAIComponent(90));
        return hand;
    }

    private Entity createUpperRightHand(World world, Entity parent){
        PositionComponent parentPositionCompnent = parent.getComponent(PositionComponent.class);
        Entity hand = createBaseHand(world, parent, false);
        hand.edit().add(new FollowPositionComponent(parentPositionCompnent.position, CenterMath.offsetX(mainBodyWidth, handsWidth) + upperHandOffsetX, upperHandOffsetY));
        return hand;
    }

    private Entity createLowerRightHand(World world, Entity parent){
        PositionComponent parentPositionCompnent = parent.getComponent(PositionComponent.class);
        Entity hand = createBaseHand(world, parent, false);
        hand.edit().add(new FollowPositionComponent(parentPositionCompnent.position, CenterMath.offsetX(mainBodyWidth, handsWidth) + bottomHandOffsetX, bottomHandOffsetY));
        return hand;

    }


    private Entity createBaseHand(World world, Entity parent, boolean isLeft){

        float tempPos = -10000f;

        Entity hand = world.createEntity();

        for(Component c : this.defaultEnemyBag(new ComponentBag(), tempPos, tempPos, handHealth)){
            hand.edit().add(c);
        }

        hand.edit().add(new CollisionBoundComponent(new Rectangle(tempPos, tempPos, handsWidth, handsHeight), true));
        hand.edit().add(new TextureRegionComponent(atlas.findRegion(TextureStrings.BLOCK), handsWidth, handsHeight,
                TextureRegionComponent.ENEMY_LAYER_NEAR));

        hand.edit().add(new ChildComponent(parent.getComponent(ParentComponent.class)));

        return hand;

    }



    private class EndSplitterWeapon implements Weapon {

        private BulletFactory bulletFactory;
        private GibletFactory gibletFactory;

        private Weapon weapoonFiredOnBulletDeath;

        private Weapon firstSplitter;
        private Weapon secondSplitter;

        int[] firstSplitAngles = new int[]{90, -35, 215};

        public EndSplitterWeapon(AssetManager assetManager){
            this.bulletFactory = new BulletFactory(assetManager);
            this.gibletFactory = new GibletFactory(assetManager);
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
                    .shotScale(10f)
                    .customOnDeathAction(new OnDeathActionComponent(new Action() {
                        @Override
                        public void performAction(World world, Entity e) {
                            CollisionBoundComponent cbc = e.getComponent(CollisionBoundComponent.class);
                            gibletFactory.defaultGiblets(new Color(Color.RED));
                            weapoonFiredOnBulletDeath.fire(world,e ,cbc.getCenterX(),cbc.getCenterY(),0);
                        }
                    }))
                    .build();


            this.firstSplitter = new MultiPistol.PistolBuilder(assetManager)
                    .angles(0)
                    .intangible(true)
                    .expireRange(firstSplitterRange)
                    .shotSpeed(firstSplitterSpeed)
                    .shotScale(15f)
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
    }


    /**
     * Queues up 10 lasers and then fires then in the same order
     *
     * To do, as the boss spose
     *
     *
     *
     */
    private class EndDeathFromAboveLasers implements Weapon {

        private LaserBeam laserBeam;
        private Random random;


        private float offSetFromBoss;

        //TODO this weapon should take in the bosses position and then use it when centering the lasers
        //TODO as the player should be stuck in the middle of the boss
        //TODO so the lasers don't need to cover the whole room

        private Array<Integer> positionIndentifierArray = new Array<Integer>();

        public EndDeathFromAboveLasers(AssetManager assetManager, Random random){

            laserBeam = new LaserBeam.LaserBeamBuilder(assetManager)
                    .chargingLaserWidth(Measure.units(5f))
                    .chargingLaserHeight(Measure.units(100f))
                    .activeLaserWidth(Measure.units(7.5f))
                    .activeLaserHeight(Measure.units(100f))
                    .chargingLaserTime(4f)
                    .build();
            this.random = random;
            positionIndentifierArray.addAll(0,1,2,3,4,5,6,7,8,9,10);

        }

        @Override
        public void fire(World world, Entity e, float x, float y, double angleInRadians) {

            positionIndentifierArray.shuffle();
            Entity entityForLaserAction = world.createEntity();

            entityForLaserAction.edit().add(new ExpireComponent(20f));
            entityForLaserAction.edit().add(new ActionAfterTimeComponent(new Action() {

                int count = 0;
                @Override
                public void performAction(World world, Entity e) {
                    if(count < 10) {
                        laserBeam.createBeam(world, offSetFromBoss + Measure.units(positionIndentifierArray.get(count) * 7.5f), 0);
                        count++;
                    }
                }
            }, 0.2f, true));
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
    }





}
