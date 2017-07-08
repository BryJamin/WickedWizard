package com.byrjamin.wickedwizard.factories.enemy.bosses;

import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.byrjamin.wickedwizard.assets.TextureStrings;
import com.byrjamin.wickedwizard.ecs.components.BlinkComponent;
import com.byrjamin.wickedwizard.ecs.components.CollisionBoundComponent;
import com.byrjamin.wickedwizard.ecs.components.HealthComponent;
import com.byrjamin.wickedwizard.ecs.components.Weapon;
import com.byrjamin.wickedwizard.ecs.components.WeaponComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.Action;
import com.byrjamin.wickedwizard.ecs.components.ai.ActionAfterTimeComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.Condition;
import com.byrjamin.wickedwizard.ecs.components.ai.ConditionalActionComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.FiringAIComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.OnDeathActionComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.PhaseComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.Task;
import com.byrjamin.wickedwizard.ecs.components.identifiers.ChildComponent;
import com.byrjamin.wickedwizard.ecs.components.identifiers.IntangibleComponent;
import com.byrjamin.wickedwizard.ecs.components.identifiers.OnlyPlayerBulletsComponent;
import com.byrjamin.wickedwizard.ecs.components.identifiers.ParentComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.PositionComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.VelocityComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.FadeComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.TextureRegionComponent;
import com.byrjamin.wickedwizard.ecs.systems.FindChildSystem;
import com.byrjamin.wickedwizard.ecs.systems.FindPlayerSystem;
import com.byrjamin.wickedwizard.ecs.systems.ai.OnDeathSystem;
import com.byrjamin.wickedwizard.factories.GibletFactory;
import com.byrjamin.wickedwizard.factories.enemy.EnemyFactory;
import com.byrjamin.wickedwizard.factories.weapons.enemy.LaserBeam;
import com.byrjamin.wickedwizard.factories.weapons.enemy.MultiPistol;
import com.byrjamin.wickedwizard.utils.BulletMath;
import com.byrjamin.wickedwizard.utils.CenterMath;
import com.byrjamin.wickedwizard.utils.ComponentBag;
import com.byrjamin.wickedwizard.utils.Measure;
import com.byrjamin.wickedwizard.utils.Pair;

/**
 * Created by Home on 08/07/2017.
 */

public class BossArchnoid extends EnemyFactory {

    private static final float health = 100;

    private static final float bodyWidth = Measure.units(70f);
    private static final float bodyHeight = Measure.units(60f);

    private static final float bodyTextureWidth = Measure.units(70f);
    private static final float bodyTextureHeight = Measure.units(60f);

    private static final float enrageDistance = Measure.units(550f);

    private static final float quadShotFireRate = 0.5f;

    private static final float quadShotPhaseTime = 0.9f;


    private static final float speed = Measure.units(10f);


    //Gaps
    private static final float quadShotGap = 0.8f;



    //Spread Phase
    private static final float timeBetweenNextSpread = 1.0f;
    private static final float spreadPhasetime = 3.0f;


    //Laser Phase
    private static final float timeBetweenNextLaser = 1.5f;
    private static final float laserPhasetime = 5.0f;




    //Wall Phase
    private static final float wallDistance = Measure.units(60f);
    private static final float timeTillWall = 0.5f;
    private static final float wallPhasetime = 6.5f;

    private static final float legHeight = Measure.units(60f);
    private static final float legWidth = Measure.units(20f);
    private static final float legHealth = 20;

    private static final float legOffSetX = -Measure.units(40f);
    private static final float rightlegOffSetX = Measure.units(60f);


    private GibletFactory gibletFactory;


    public BossArchnoid(AssetManager assetManager) {
        super(assetManager);

        this.gibletFactory = new GibletFactory(assetManager);

    }




    public ComponentBag archnoid(final float x, final float y){

        ComponentBag bag = this.defaultEnemyBag(new ComponentBag(), x, y, health);

        bag.add(new CollisionBoundComponent(new Rectangle(x, y, bodyWidth, bodyHeight), true));


        bag.add(new VelocityComponent(speed, 0));

        bag.add(new IntangibleComponent());
        bag.add(new ParentComponent());


        PhaseComponent phaseComponent = new PhaseComponent();
        phaseComponent.addPhase(quadShotPhaseTime, new BulletHurdlePhase(true));
        //empty phase to add some time between hurdles
        phaseComponent.addPhase(quadShotGap, emptyTask());
        phaseComponent.addPhase(quadShotPhaseTime, new BulletHurdlePhase(false));
        //empty phase to add some time between hurdles
        //phaseComponent.addPhase(0.2f,emptyTask());
        phaseComponent.addPhase(laserPhasetime, new LaserPhase());
        phaseComponent.addPhase(spreadPhasetime, new BulletSpreadPhase());
        phaseComponent.addPhase(wallPhasetime, new BlockingWallPhase());
        bag.add(phaseComponent);


        ConditionalActionComponent cac = new ConditionalActionComponent();

        cac.add(new Condition() {

            private Vector3 startPosition = new Vector3(x, y, 0);

            private float distanceTravelled;


            @Override
            public boolean condition(World world, Entity entity) {

                PositionComponent positionComponent = entity.getComponent(PositionComponent.class);


                float distance = startPosition.dst(positionComponent.position);

                distanceTravelled += Math.abs(distance);
                startPosition.set(positionComponent.position);

                if(distanceTravelled > enrageDistance){
                    return true;
                }

                return false;
            }
        }, new Task() {
            @Override
            public void performAction(World world, Entity e) {
                e.getComponent(VelocityComponent.class).velocity.x = 0;
                world.getSystem(FindPlayerSystem.class).getPC(HealthComponent.class).health = 1;
                world.getSystem(FindPlayerSystem.class).getPC(HealthComponent.class).applyDamage(2);
            }

            @Override
            public void cleanUpAction(World world, Entity e) {

            }
        });


        bag.add(cac);

        bag.add(new TextureRegionComponent(atlas.findRegion(TextureStrings.BLOCK),
                CenterMath.offsetX(bodyWidth, bodyTextureWidth),
                CenterMath.offsetY(bodyHeight, bodyTextureHeight),
                bodyTextureWidth,
                bodyTextureHeight,
                TextureRegionComponent.PLAYER_LAYER_MIDDLE));



        return bag;
    }


    public Task emptyTask(){
        return new Task() {
            @Override
            public void performAction(World world, Entity e) {

            }

            @Override
            public void cleanUpAction(World world, Entity e) {

            }
        };
    }





    public Pair<Action, Condition> bouceBackTaskConditionPair(final float pushAngleInDegrees){
        return new Pair<Action, Condition>(
                new Action() {
                    @Override
                    public void performAction(World world, Entity e) {
                        VelocityComponent vc = world.getSystem(FindPlayerSystem.class).getPC(VelocityComponent.class);
                        vc.velocity.x = BulletMath.velocityX(Measure.units(50f), Math.toRadians(pushAngleInDegrees));
                        vc.velocity.y = BulletMath.velocityY(Measure.units(50f), Math.toRadians(pushAngleInDegrees));
                    }
                },
                new Condition() {
                    @Override
                    public boolean condition(World world, Entity entity) {
                        return entity.getComponent(CollisionBoundComponent.class).bound.overlaps(world.getSystem(FindPlayerSystem.class).getPC(CollisionBoundComponent.class).bound);

                    }
                }
        );
    }



    private class BulletHurdlePhase implements Task {

        private Weapon weapon;
        private boolean isLower;


        BulletHurdlePhase(boolean isLower){
            weapon = new MultiPistol.PistolBuilder(assetManager)
                    .enemy(true)
                    .bulletOffsets(0, Measure.units(5f), -Measure.units(5f), Measure.units(10f), -Measure.units(10f))
                    .fireRate(0.25f)
                    .intangible(true)
                    .build();
            this.isLower = isLower;
        }


        @Override
        public void performAction(World world, Entity e) {
            e.edit().add(new WeaponComponent(weapon));
            e.edit().add(new FiringAIComponent(FiringAIComponent.AI.UNTARGETED, 0, 0 , isLower ? -Measure.units(7.5f) : Measure.units(10f)));
        }

        @Override
        public void cleanUpAction(World world, Entity e) {
            e.edit().remove(WeaponComponent.class);
            e.edit().remove(FiringAIComponent.class);
        }
    }




    private class BlockingWallPhase implements Task{

        @Override
        public void performAction(World world, Entity e) {

            e.edit().add(new ActionAfterTimeComponent(new Action() {
                @Override
                public void performAction(World world, Entity e) {

                    for(int j = 0; j < 4; j++) {
                        for (int i = 0; i < 12; i++) {
                            createBlock(world, e.getComponent(ParentComponent.class),
                                    e.getComponent(PositionComponent.class).getX() + e.getComponent(CollisionBoundComponent.class).bound.getWidth() + (wallDistance + j * Measure.units(5f)) , Measure.units(0f) + Measure.units(i * 5), 180, new Color(0, 0, 0, 0));
                        }
                    }
                }
            }, timeTillWall));

        }

        @Override
        public void cleanUpAction(World world, Entity e) {
            e.edit().remove(ActionAfterTimeComponent.class);
        }


        public void createBlock(World world, ParentComponent pc, float x, float y, final float pushAngle, Color color){

            float width = Measure.units(5);
            float height = Measure.units(5f);

            Entity e = world.createEntity();
            e.edit().add(new PositionComponent(x,y));
            e.edit().add(new CollisionBoundComponent(new Rectangle(x,y, width, height), true));
            //e.edit().add(new EnemyComponent());
            e.edit().add(new OnlyPlayerBulletsComponent());
            e.edit().add(new BlinkComponent());
            e.edit().add(new HealthComponent(1));
            e.edit().add(new FadeComponent(true, 1, false));
            e.edit().add(new OnDeathActionComponent(gibletFactory.defaultGiblets(new Color(Color.BLACK))));

            ChildComponent c = new ChildComponent();
            pc.children.add(c);
            e.edit().add(c);

            TextureRegionComponent trc = new TextureRegionComponent(atlas.findRegion(TextureStrings.BLOCK), 0, 0,  Measure.units(5), Measure.units(5), TextureRegionComponent.PLAYER_LAYER_FAR);
            trc.DEFAULT = color;
            trc.color = color;
            e.edit().add(trc);


            ConditionalActionComponent cac = new ConditionalActionComponent();
            cac.add(bouceBackTaskConditionPair(pushAngle));
            cac.add(new Condition() {
                @Override
                public boolean condition(World world, Entity entity) {
                    Entity parent = world.getSystem(FindChildSystem.class).findParentEntity(entity.getComponent(ChildComponent.class));


                    if (parent != null) {
                        return parent.getComponent(CollisionBoundComponent.class).bound.overlaps(entity.getComponent(CollisionBoundComponent.class).bound);
                    }

                    return false;
                }
            }, new Action() {
                @Override
                public void performAction(World world, Entity e) {
                    world.getSystem(OnDeathSystem.class).kill(e);
                }
            });


            e.edit().add(cac);
        }


    }












    public class LaserPhase implements Task{


        private LaserBeam sideLasers;
        private LaserBeam centerLasers;

        public LaserPhase(){
            sideLasers = new LaserBeam.LaserBeamBuilder(assetManager)
                    .chargingLaserWidth(Measure.units(1000f))
                    .chargingLaserHeight(Measure.units(5f))
                    .activeLaserWidth(Measure.units(1000f))
                    .activeLaserHeight(Measure.units(7.5f))
                    .useWidthAsCenter(false)
                    .build();

            centerLasers = new LaserBeam.LaserBeamBuilder(assetManager)
                    .chargingLaserWidth(Measure.units(1000f))
                    .chargingLaserHeight(Measure.units(20f))
                    .activeLaserWidth(Measure.units(1000f))
                    .activeLaserHeight(Measure.units(25f))
                    .useWidthAsCenter(false)
                    .build();

        }

        @Override
        public void performAction(World world, Entity e) {

            e.edit().add(new ActionAfterTimeComponent(new Action() {

                boolean switchbool;

                @Override
                public void performAction(World world, Entity e) {

                    if(switchbool) {
                        sideLasers.createBeam(world, e.getComponent(PositionComponent.class).getX(),
                                e.getComponent(PositionComponent.class).getY() + Measure.units(12.5f));
                        sideLasers.createBeam(world, e.getComponent(PositionComponent.class).getX(),
                                e.getComponent(PositionComponent.class).getY() + Measure.units(47.5f));
                        switchbool = !switchbool;
                    } else {

                        centerLasers.createBeam(world, e.getComponent(PositionComponent.class).getX(),
                                e.getComponent(PositionComponent.class).getY() + Measure.units(22.5f));
                        switchbool = !switchbool;
                    }
                }
            }, timeBetweenNextLaser, true));
        }

        @Override
        public void cleanUpAction(World world, Entity e) {

        }
    }




    public class BulletSpreadPhase implements Task{


        private Weapon spreadWeapon;

        public BulletSpreadPhase(){
            spreadWeapon = new MultiPistol.PistolBuilder(assetManager)
                    .enemy(true)
                    .angles(80, 60, 40, 20, 0, -20, -40, -60, -80)
                    .shotScale(2.5f)
                    .build();
        }

        @Override
        public void performAction(World world, Entity e) {


            e.edit().add(new ActionAfterTimeComponent(new Action() {

                boolean switchbool;

                @Override
                public void performAction(World world, Entity e) {

                    CollisionBoundComponent cbc = e.getComponent(CollisionBoundComponent.class);

                    if(switchbool) {
                        spreadWeapon.fire(world, e, cbc.bound.x + cbc.bound.width, cbc.getCenterY() - Measure.units(7.5f), 0);
                        switchbool = !switchbool;
                    } else {
                        spreadWeapon.fire(world, e,  cbc.bound.x + cbc.bound.width, cbc.getCenterY() + Measure.units(7.5f), 0);
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
