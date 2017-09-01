package com.bryjamin.wickedwizard.factories.enemy.bosses;

import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.IntMap;
import com.bryjamin.wickedwizard.assets.ColorResource;
import com.bryjamin.wickedwizard.ecs.components.movement.CollisionBoundComponent;
import com.bryjamin.wickedwizard.ecs.components.Weapon;
import com.bryjamin.wickedwizard.ecs.components.ai.Action;
import com.bryjamin.wickedwizard.ecs.components.ai.ActionAfterTimeComponent;
import com.bryjamin.wickedwizard.ecs.components.ai.Condition;
import com.bryjamin.wickedwizard.ecs.components.ai.ConditionalActionComponent;
import com.bryjamin.wickedwizard.ecs.components.ai.PhaseComponent;
import com.bryjamin.wickedwizard.ecs.components.texture.AnimationComponent;
import com.bryjamin.wickedwizard.ecs.components.texture.AnimationStateComponent;
import com.bryjamin.wickedwizard.ecs.components.texture.FadeComponent;
import com.bryjamin.wickedwizard.ecs.components.texture.TextureRegionComponent;
import com.bryjamin.wickedwizard.utils.BulletMath;
import com.bryjamin.wickedwizard.utils.CenterMath;

/**
 * Created by Home on 08/07/2017.
 */

public class BossAmalgama extends BossFactory {

    private static final float health = 100;

    private static final float bodyWidth = com.bryjamin.wickedwizard.utils.Measure.units(70f);
    private static final float bodyHeight = com.bryjamin.wickedwizard.utils.Measure.units(60f);

    private static final float bodyTextureWidth = com.bryjamin.wickedwizard.utils.Measure.units(85f);
    private static final float bodyTextureOffsetX = com.bryjamin.wickedwizard.utils.Measure.units(0f);
    private static final float bodyTextureHeight = com.bryjamin.wickedwizard.utils.Measure.units(85f);

    private static final float enrageDistance = com.bryjamin.wickedwizard.utils.Measure.units(550f);

    private static final float quadShotFireRate = 0.5f;

    private static final float quadShotPhaseTime = 0.9f;


    private static final float speed = com.bryjamin.wickedwizard.utils.Measure.units(10f);


    //Gaps
    private static final float quadShotGap = 0.8f;



    //Spread Phase
    private static final float timeBetweenNextSpread = 1.0f;
    private static final float spreadPhasetime = 3.0f;


    //Laser Phase
    private static final float timeBetweenNextLaser = 1.5f;
    private static final float laserPhasetime = 5.0f;




    //Wall Phase
    private static final float wallDistance = com.bryjamin.wickedwizard.utils.Measure.units(60f);
    private static final float timeTillWall = 0.5f;
    private static final float wallPhasetime = 6.5f;

    //States
    private static int RED_EYE_STATE = 0;
    private static int BLUE_EYE_STATE = 2;
    private static int GREEN_EYE_STATE = 4;
    private static int YELLOW_EYE_STATE = 8;

    private static float STATE_FRAME_DURATION = 1 / 10f;

    private com.bryjamin.wickedwizard.factories.weapons.Giblets.GibletBuilder gibletBuilder;


    public BossAmalgama(AssetManager assetManager) {
        super(assetManager);

        Color gibletColor = new Color(Color.BLACK);


        this.gibletBuilder = new com.bryjamin.wickedwizard.factories.weapons.Giblets.GibletBuilder(assetManager)
                .numberOfGibletPairs(2)
                .size(com.bryjamin.wickedwizard.utils.Measure.units(0.5f))
                .minSpeed(com.bryjamin.wickedwizard.utils.Measure.units(10f))
                .maxSpeed(com.bryjamin.wickedwizard.utils.Measure.units(20f))
                .colors(gibletColor)
                .intangible(true)
                .expiryTime(0.2f);

    }




    public com.bryjamin.wickedwizard.utils.ComponentBag amalgama(final float x, final float y){

        com.bryjamin.wickedwizard.utils.ComponentBag bag = this.defaultBossBag(new com.bryjamin.wickedwizard.utils.ComponentBag(), x, y, health);

        bag.add(new CollisionBoundComponent(new Rectangle(x, y, bodyWidth, bodyHeight), true));


        bag.add(new com.bryjamin.wickedwizard.ecs.components.movement.VelocityComponent(speed, 0));

        bag.add(new com.bryjamin.wickedwizard.ecs.components.identifiers.IntangibleComponent());
        bag.add(new com.bryjamin.wickedwizard.ecs.components.identifiers.ParentComponent());



        bag.add(new TextureRegionComponent(atlas.findRegion(com.bryjamin.wickedwizard.assets.TextureStrings.AMALGAMA_RED_EYE),
                CenterMath.offsetX(bodyWidth, bodyTextureWidth) + bodyTextureOffsetX,
                CenterMath.offsetY(bodyHeight, bodyTextureHeight),
                bodyTextureWidth,
                bodyTextureHeight,
                TextureRegionComponent.PLAYER_LAYER_NEAR));


        bag.add(new AnimationStateComponent(RED_EYE_STATE));
        IntMap<Animation<TextureRegion>> animMap = new IntMap<Animation<TextureRegion>>();
        animMap.put(RED_EYE_STATE, new Animation<TextureRegion>(STATE_FRAME_DURATION, atlas.findRegions(com.bryjamin.wickedwizard.assets.TextureStrings.AMALGAMA_RED_EYE), Animation.PlayMode.LOOP));
        animMap.put(BLUE_EYE_STATE, new Animation<TextureRegion>(STATE_FRAME_DURATION, atlas.findRegions(com.bryjamin.wickedwizard.assets.TextureStrings.AMALGAMA_BLUE_EYE), Animation.PlayMode.LOOP));
        animMap.put(GREEN_EYE_STATE, new Animation<TextureRegion>(STATE_FRAME_DURATION, atlas.findRegions(com.bryjamin.wickedwizard.assets.TextureStrings.AMALGAMA_GREEN_EYE), Animation.PlayMode.LOOP));
        animMap.put(YELLOW_EYE_STATE, new Animation<TextureRegion>(STATE_FRAME_DURATION, atlas.findRegions(com.bryjamin.wickedwizard.assets.TextureStrings.AMALGAMA_YELLOW_EYE), Animation.PlayMode.LOOP));
        bag.add(new AnimationComponent(animMap));






        PhaseComponent phaseComponent = new PhaseComponent();
        phaseComponent.addPhase(laserPhasetime, new LaserPhase());
        phaseComponent.addPhase(spreadPhasetime, new BulletSpreadPhase());
        phaseComponent.addPhase(wallPhasetime, new BlockingWallPhase());
        phaseComponent.addPhase(quadShotPhaseTime, new BulletHurdlePhase(true));
        //empty phase to add some time between hurdles
        phaseComponent.addPhase(quadShotGap, emptyTask());
        phaseComponent.addPhase(quadShotPhaseTime, new BulletHurdlePhase(false));
        //empty phase to add some time between hurdles
        //phaseComponent.addPhase(0.2f,emptyTask());
        bag.add(phaseComponent);


        ConditionalActionComponent cac = new ConditionalActionComponent();

        cac.add(bouceBackTaskConditionPair(0));

        cac.add(new com.bryjamin.wickedwizard.ecs.components.ai.Condition() {

            private Vector3 startPosition = new Vector3(x, y, 0);

            private float distanceTravelled;


            @Override
            public boolean condition(World world, Entity entity) {

                com.bryjamin.wickedwizard.ecs.components.movement.PositionComponent positionComponent = entity.getComponent(com.bryjamin.wickedwizard.ecs.components.movement.PositionComponent.class);


                float distance = startPosition.dst(positionComponent.position);

                distanceTravelled += Math.abs(distance);
                startPosition.set(positionComponent.position);

                //TODO A tiny bit not clean maybe add option to remove conditions upon fufillment
                if(distanceTravelled > enrageDistance && entity.getComponent(com.bryjamin.wickedwizard.ecs.components.movement.VelocityComponent.class).velocity.x != 0){
                    return true;
                }

                return false;
            }
        }, new Action() {

            @Override
            public void performAction(World world, Entity e) {
                e.getComponent(com.bryjamin.wickedwizard.ecs.components.movement.VelocityComponent.class).velocity.x = 0;

                e.getComponent(PhaseComponent.class).getCurrentPhase().cleanUpAction(world , e);
                e.edit().remove(PhaseComponent.class);

                PhaseComponent phaseComponent = new PhaseComponent();
                phaseComponent.addPhase(laserPhasetime, new LaserPhase());
                phaseComponent.addPhase(spreadPhasetime, new BulletSpreadPhase());
                //phaseComponent.addPhase(wallPhasetime, new BlockingWallPhase());
                phaseComponent.addPhase(quadShotPhaseTime, new BulletHurdlePhase(true));
                //empty phase to add some time between hurdles
                phaseComponent.addPhase(quadShotGap, emptyTask());
                phaseComponent.addPhase(quadShotPhaseTime, new BulletHurdlePhase(false));

                e.edit().add(phaseComponent);
                //phaseComponent.addPhase(wallPhasetime, new BlockingWallPhase());

             //   world.getSystem(FindPlayerSystem.class).getPlayerComponent(HealthComponent.class).health = 1;
             //   world.getSystem(FindPlayerSystem.class).getPlayerComponent(HealthComponent.class).applyDamage(2);
            }
        });


        bag.add(cac);



        return bag;
    }


    public com.bryjamin.wickedwizard.ecs.components.ai.Task emptyTask(){
        return new com.bryjamin.wickedwizard.ecs.components.ai.Task() {
            @Override
            public void performAction(World world, Entity e) {

            }

            @Override
            public void cleanUpAction(World world, Entity e) {

            }
        };
    }





    public com.bryjamin.wickedwizard.utils.Pair<Action, Condition> bouceBackTaskConditionPair(final float pushAngleInDegrees){
        return new com.bryjamin.wickedwizard.utils.Pair<Action, Condition>(
                new Action() {
                    @Override
                    public void performAction(World world, Entity e) {
                        com.bryjamin.wickedwizard.ecs.components.movement.VelocityComponent vc = world.getSystem(com.bryjamin.wickedwizard.ecs.systems.FindPlayerSystem.class).getPlayerComponent(com.bryjamin.wickedwizard.ecs.components.movement.VelocityComponent.class);
                        vc.velocity.x = BulletMath.velocityX(com.bryjamin.wickedwizard.utils.Measure.units(50f), Math.toRadians(pushAngleInDegrees));
                        vc.velocity.y = BulletMath.velocityY(com.bryjamin.wickedwizard.utils.Measure.units(50f), Math.toRadians(pushAngleInDegrees));
                    }
                },
                new com.bryjamin.wickedwizard.ecs.components.ai.Condition() {
                    @Override
                    public boolean condition(World world, Entity entity) {
                        return entity.getComponent(CollisionBoundComponent.class).bound.overlaps(world.getSystem(com.bryjamin.wickedwizard.ecs.systems.FindPlayerSystem.class).getPlayerComponent(CollisionBoundComponent.class).bound);

                    }
                }
        );
    }



    private class BulletHurdlePhase implements com.bryjamin.wickedwizard.ecs.components.ai.Task {

        private Weapon weapon;
        private boolean isLower;


        BulletHurdlePhase(boolean isLower){
            weapon = new com.bryjamin.wickedwizard.factories.weapons.enemy.MultiPistol.PistolBuilder(assetManager)
                    .enemy(true)
                    .bulletOffsets(0, com.bryjamin.wickedwizard.utils.Measure.units(5f), -com.bryjamin.wickedwizard.utils.Measure.units(5f), com.bryjamin.wickedwizard.utils.Measure.units(10f), -com.bryjamin.wickedwizard.utils.Measure.units(10f))
                    .fireRate(0.25f)
                    .intangible(true)
                    .build();
            this.isLower = isLower;
        }


        @Override
        public void performAction(World world, Entity e) {

            e.getComponent(AnimationStateComponent.class).setDefaultState(RED_EYE_STATE);

            e.edit().add(new com.bryjamin.wickedwizard.ecs.components.WeaponComponent(weapon));
            e.edit().add(new com.bryjamin.wickedwizard.ecs.components.ai.FiringAIComponent(com.bryjamin.wickedwizard.ecs.components.ai.FiringAIComponent.AI.UNTARGETED, 0, 0 , isLower ? -com.bryjamin.wickedwizard.utils.Measure.units(7.5f) : com.bryjamin.wickedwizard.utils.Measure.units(10f)));
        }

        @Override
        public void cleanUpAction(World world, Entity e) {
            e.edit().remove(com.bryjamin.wickedwizard.ecs.components.WeaponComponent.class);
            e.edit().remove(com.bryjamin.wickedwizard.ecs.components.ai.FiringAIComponent.class);
        }
    }




    private class BlockingWallPhase implements com.bryjamin.wickedwizard.ecs.components.ai.Task {

        @Override
        public void performAction(World world, Entity e) {

            e.getComponent(AnimationStateComponent.class).setDefaultState(BLUE_EYE_STATE);


            e.edit().add(new ActionAfterTimeComponent(new Action() {
                @Override
                public void performAction(World world, Entity e) {

                    for(int j = 0; j < 4; j++) {
                        for (int i = 0; i < 9; i++) {
                            createBlock(world, e.getComponent(com.bryjamin.wickedwizard.ecs.components.identifiers.ParentComponent.class),
                                    e.getComponent(com.bryjamin.wickedwizard.ecs.components.movement.PositionComponent.class).getX() + e.getComponent(CollisionBoundComponent.class).bound.getWidth() + (wallDistance + j * com.bryjamin.wickedwizard.utils.Measure.units(5f)) , com.bryjamin.wickedwizard.utils.Measure.units(10f) + com.bryjamin.wickedwizard.utils.Measure.units(i * 5), 180, new Color(0, 0, 0, 0));
                        }
                    }
                }
            }, timeTillWall));

        }

        @Override
        public void cleanUpAction(World world, Entity e) {
            e.edit().remove(ActionAfterTimeComponent.class);
        }


        public void createBlock(World world, com.bryjamin.wickedwizard.ecs.components.identifiers.ParentComponent pc, float x, float y, final float pushAngle, Color color){

            float width = com.bryjamin.wickedwizard.utils.Measure.units(5);
            float height = com.bryjamin.wickedwizard.utils.Measure.units(5f);

            Entity e = world.createEntity();
            e.edit().add(new com.bryjamin.wickedwizard.ecs.components.movement.PositionComponent(x,y));
            e.edit().add(new CollisionBoundComponent(new Rectangle(x,y, width, height), true));
            //e.edit().add(new EnemyComponent());
            e.edit().add(new com.bryjamin.wickedwizard.ecs.components.identifiers.OnlyPlayerBulletsComponent());
            e.edit().add(new com.bryjamin.wickedwizard.ecs.components.texture.BlinkOnHitComponent());
            e.edit().add(new com.bryjamin.wickedwizard.ecs.components.HealthComponent(1));
            e.edit().add(new FadeComponent(true, 0.5f, false));
            e.edit().add(new com.bryjamin.wickedwizard.ecs.components.ai.OnDeathActionComponent(gibletBuilder.build()));

            com.bryjamin.wickedwizard.ecs.components.identifiers.ChildComponent c = new com.bryjamin.wickedwizard.ecs.components.identifiers.ChildComponent();
            pc.children.add(c);
            e.edit().add(c);

            TextureRegionComponent trc = new TextureRegionComponent(atlas.findRegion(com.bryjamin.wickedwizard.assets.TextureStrings.BLOCK), 0, 0,  com.bryjamin.wickedwizard.utils.Measure.units(5), com.bryjamin.wickedwizard.utils.Measure.units(5), TextureRegionComponent.PLAYER_LAYER_FAR);
            trc.DEFAULT = color;
            trc.color = color;
            e.edit().add(trc);


            ConditionalActionComponent cac = new ConditionalActionComponent();
            cac.add(bouceBackTaskConditionPair(pushAngle));
            cac.add(new com.bryjamin.wickedwizard.ecs.components.ai.Condition() {
                @Override
                public boolean condition(World world, Entity entity) {
                    Entity parent = world.getSystem(com.bryjamin.wickedwizard.ecs.systems.FindChildSystem.class).findParentEntity(entity.getComponent(com.bryjamin.wickedwizard.ecs.components.identifiers.ChildComponent.class));


                    if (parent != null) {
                        return parent.getComponent(CollisionBoundComponent.class).bound.overlaps(entity.getComponent(CollisionBoundComponent.class).bound);
                    }

                    return false;
                }
            }, new Action() {
                @Override
                public void performAction(World world, Entity e) {
                    world.getSystem(com.bryjamin.wickedwizard.ecs.systems.ai.OnDeathSystem.class).kill(e);
                }
            });


            e.edit().add(cac);
        }


    }












    public class LaserPhase implements com.bryjamin.wickedwizard.ecs.components.ai.Task {




        private com.bryjamin.wickedwizard.factories.weapons.enemy.LaserBeam sideLasers;
        private com.bryjamin.wickedwizard.factories.weapons.enemy.LaserBeam centerLasers;

        public LaserPhase(){
            sideLasers = new com.bryjamin.wickedwizard.factories.weapons.enemy.LaserBeam.LaserBeamBuilder(assetManager)
                    .chargingLaserWidth(com.bryjamin.wickedwizard.utils.Measure.units(1000f))
                    .chargingLaserHeight(com.bryjamin.wickedwizard.utils.Measure.units(5f))
                    .activeLaserWidth(com.bryjamin.wickedwizard.utils.Measure.units(1000f))
                    .activeLaserHeight(com.bryjamin.wickedwizard.utils.Measure.units(7.5f))
                    .centerLaserUsingWidth(false)
                    .layer(TextureRegionComponent.ENEMY_LAYER_NEAR)
                    .build();

            centerLasers = new com.bryjamin.wickedwizard.factories.weapons.enemy.LaserBeam.LaserBeamBuilder(assetManager)
                    .chargingLaserWidth(com.bryjamin.wickedwizard.utils.Measure.units(1000f))
                    .chargingLaserHeight(com.bryjamin.wickedwizard.utils.Measure.units(20f))
                    .activeLaserWidth(com.bryjamin.wickedwizard.utils.Measure.units(1000f))
                    .activeLaserHeight(com.bryjamin.wickedwizard.utils.Measure.units(25f))
                    .centerLaserUsingWidth(false)
                    .layer(TextureRegionComponent.ENEMY_LAYER_NEAR)
                    .build();

        }

        @Override
        public void performAction(World world, Entity e) {


            e.getComponent(AnimationStateComponent.class).setDefaultState(YELLOW_EYE_STATE);

            e.edit().add(new ActionAfterTimeComponent(new Action() {

                boolean switchbool;

                @Override
                public void performAction(World world, Entity e) {

                    if(switchbool) {
                        sideLasers.createBeam(world, e.getComponent(com.bryjamin.wickedwizard.ecs.components.movement.PositionComponent.class).getX(),
                                e.getComponent(com.bryjamin.wickedwizard.ecs.components.movement.PositionComponent.class).getY() + com.bryjamin.wickedwizard.utils.Measure.units(12.5f));
                        sideLasers.createBeam(world, e.getComponent(com.bryjamin.wickedwizard.ecs.components.movement.PositionComponent.class).getX(),
                                e.getComponent(com.bryjamin.wickedwizard.ecs.components.movement.PositionComponent.class).getY() + com.bryjamin.wickedwizard.utils.Measure.units(47.5f));
                        switchbool = !switchbool;
                    } else {

                        centerLasers.createBeam(world, e.getComponent(com.bryjamin.wickedwizard.ecs.components.movement.PositionComponent.class).getX(),
                                e.getComponent(com.bryjamin.wickedwizard.ecs.components.movement.PositionComponent.class).getY() + com.bryjamin.wickedwizard.utils.Measure.units(22.5f));
                        switchbool = !switchbool;
                    }
                }
            }, timeBetweenNextLaser, true));
        }

        @Override
        public void cleanUpAction(World world, Entity e) {

        }
    }




    public class BulletSpreadPhase implements com.bryjamin.wickedwizard.ecs.components.ai.Task {


        private Weapon spreadWeapon;

        public BulletSpreadPhase(){
            spreadWeapon = new com.bryjamin.wickedwizard.factories.weapons.enemy.MultiPistol.PistolBuilder(assetManager)
                    .enemy(true)
                    .color(ColorResource.GHOST_BULLET_COLOR)
                    .intangible(true)
                    .angles(80, 60, 40, 20, 0, -20, -40, -60, -80)
                    .shotScale(2.5f)
                    .build();
        }

        @Override
        public void performAction(World world, Entity e) {

            e.getComponent(AnimationStateComponent.class).setDefaultState(GREEN_EYE_STATE);


            e.edit().add(new ActionAfterTimeComponent(new Action() {

                boolean switchbool;

                @Override
                public void performAction(World world, Entity e) {

                    CollisionBoundComponent cbc = e.getComponent(CollisionBoundComponent.class);

                    if(switchbool) {
                        spreadWeapon.fire(world, e, cbc.bound.x + cbc.bound.width - com.bryjamin.wickedwizard.utils.Measure.units(5f), cbc.getCenterY() - com.bryjamin.wickedwizard.utils.Measure.units(7.5f), 0);
                        switchbool = !switchbool;
                    } else {
                        spreadWeapon.fire(world, e,  cbc.bound.x + cbc.bound.width - com.bryjamin.wickedwizard.utils.Measure.units(5f), cbc.getCenterY() + com.bryjamin.wickedwizard.utils.Measure.units(7.5f), 0);
                        switchbool = !switchbool;
                    }
                }
            }, 0, timeBetweenNextSpread, true));
        }

        @Override
        public void cleanUpAction(World world, Entity e) {
            e.edit().remove(ActionAfterTimeComponent.class);
        }
    }

















}
