package com.byrjamin.wickedwizard.factories.enemy.bosses;

import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.IntMap;
import com.byrjamin.wickedwizard.assets.SoundFileStrings;
import com.byrjamin.wickedwizard.assets.TextureStrings;
import com.byrjamin.wickedwizard.ecs.components.CollisionBoundComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.OnCollisionActionComponent;
import com.byrjamin.wickedwizard.ecs.components.Weapon;
import com.byrjamin.wickedwizard.ecs.components.WeaponComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.Action;
import com.byrjamin.wickedwizard.ecs.components.ai.ActionAfterTimeComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.Condition;
import com.byrjamin.wickedwizard.ecs.components.ai.FiringAIComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.PhaseComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.Task;
import com.byrjamin.wickedwizard.ecs.components.movement.BounceComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.GravityComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.PositionComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.VelocityComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.AnimationComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.AnimationStateComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.TextureRegionComponent;
import com.byrjamin.wickedwizard.ecs.systems.audio.SoundSystem;
import com.byrjamin.wickedwizard.ecs.systems.level.RoomTransitionSystem;
import com.byrjamin.wickedwizard.factories.BombFactory;
import com.byrjamin.wickedwizard.factories.enemy.EnemyFactory;
import com.byrjamin.wickedwizard.factories.weapons.enemy.BomberPistol;
import com.byrjamin.wickedwizard.factories.weapons.enemy.MinePistol;
import com.byrjamin.wickedwizard.utils.CenterMath;
import com.byrjamin.wickedwizard.utils.ComponentBag;
import com.byrjamin.wickedwizard.utils.Measure;
import com.byrjamin.wickedwizard.utils.Pair;
import com.byrjamin.wickedwizard.utils.collider.Collider;
import com.byrjamin.wickedwizard.utils.collider.HitBox;

import java.util.Random;

/**
 * Created by Home on 02/07/2017.
 */

public class MrBoomyBoss extends EnemyFactory {

    private static final float height = Measure.units(12f);
    private static final float width = Measure.units(12f);

    private static final float hitBoxWidth = Measure.units(9f);
    private static final float hitBoxHeight = Measure.units(10f);


    private static final float jumpSpeed = Measure.units(100f);
    private static final float speed = Measure.units(45f);
    private static final float airTime = 0.5f;
    private static final float dropTime = 0.5f;

    private static final float health = 80;

    private static final int DEFAULT_ANIMATION = 0;
    private static final int EXPLODING_ANIMATION = 2;
    private static final int SIDEVIEW_ANIMATION = 4;


    private static final float airBomberWeaponFireRate = 1.5f;
    private static final float groundBomberWeaponFireRate = 0.5f;

    private BombFactory bombFactory;


    //Phase
    private static final float groundBomberPhaseTime = 5.0f;

    private static final float airBomberPhaseTime = 8.5f;

    public MrBoomyBoss(AssetManager assetManager) {
        super(assetManager);
        this.bombFactory = new BombFactory(assetManager);
    }



    public ComponentBag mrBoomy(float x, float y){

        x = x - width / 2;
        y = y - height / 2;

        ComponentBag bag = this.defaultBossBag(new ComponentBag(), x , y, health);

        bag.add(new CollisionBoundComponent(new Rectangle(x, y, width, height),
                new HitBox(new Rectangle(x,y,hitBoxWidth,hitBoxHeight),
                        CenterMath.offsetX(width, hitBoxWidth), CenterMath.offsetY(height, hitBoxHeight))));
        bag.add(new VelocityComponent());
        bag.add(new GravityComponent());

        bag.add(new TextureRegionComponent(atlas.findRegion(TextureStrings.MR_BOOMY_FACING), width, height,
                TextureRegionComponent.ENEMY_LAYER_MIDDLE));

        bag.add(new AnimationStateComponent(0));
        IntMap<Animation<TextureRegion>> animMap = new IntMap<Animation<TextureRegion>>();
        animMap.put(DEFAULT_ANIMATION, new Animation<TextureRegion>(1f / 10f, atlas.findRegions(TextureStrings.MR_BOOMY_FACING), Animation.PlayMode.LOOP));
        animMap.put(AnimationStateComponent.FIRING, new Animation<TextureRegion>(1f / 21f, atlas.findRegions(TextureStrings.MR_BOOMY_FACING)));
        animMap.put(EXPLODING_ANIMATION, new Animation<TextureRegion>(1f / 25f, atlas.findRegions(TextureStrings.MR_BOOMY_DETONATE), Animation.PlayMode.LOOP));
        animMap.put(SIDEVIEW_ANIMATION, new Animation<TextureRegion>(1f / 10f, atlas.findRegions(TextureStrings.MR_BOOMY_SIDE), Animation.PlayMode.LOOP));

        bag.add(new AnimationComponent(animMap));



        PhaseComponent pc = new PhaseComponent();
        pc.addPhase(groundBomberPhaseTime, groundBomberPhase());
        pc.addPhase(airBomberPhaseTime , jumpingAirBomberPhase()); //, moveToPhaseTimingAndPositionCondition(airBomberPhaseTime));
        pc.addPhase(new Pair<Task, Condition>(dropAndExplode(), new Condition() {
            @Override
            public boolean condition(World world, Entity entity) {
                return entity.getComponent(CollisionBoundComponent.class).getRecentCollisions().contains(Collider.Collision.BOTTOM, true);
            }
        }));
        pc.addPhase(groundBomberPhaseTime, groundBomberPhase());

        bag.add(pc);

        return bag;

    }


    private Condition moveToPhaseTimingAndPositionCondition(final float time){
        return new Condition() {
            @Override
            public boolean condition(World world, Entity entity) {
                return entity.getComponent(PhaseComponent.class).currentPhaseTime > time &&
                        entity.getComponent(CollisionBoundComponent.class).bound.contains(world.getSystem(RoomTransitionSystem.class).getCurrentArena().getWidth() / 2,
                                entity.getComponent(PositionComponent.class).getY());
            }
        };
    }



    public Task jumpingAirBomberPhase() {
        return new Task() {

            Random random = new Random();

            @Override
            public void performAction(World world, Entity e) {
                VelocityComponent vc = e.getComponent(VelocityComponent.class);
                vc.velocity.y = jumpSpeed;
                e.edit().add(new GravityComponent());
                e.edit().add(new FiringAIComponent(0));
                e.edit().add(new WeaponComponent(new AirBomberWeapon(assetManager), 0.5f));

                e.edit().add(new BounceComponent(true, false));

                e.edit().add(new ActionAfterTimeComponent(new Action() {
                    @Override
                    public void performAction(World world, Entity e) {

                        VelocityComponent vc = e.getComponent(VelocityComponent.class);
                        vc.velocity.x = random.nextBoolean() ? vc.velocity.x = -speed : speed;
                        e.edit().remove(new GravityComponent());
                        e.getComponent(VelocityComponent.class).velocity.y = 0;
                    }


                }, airTime));
            }

            @Override
            public void cleanUpAction(World world, Entity e) {
                e.edit().remove(FiringAIComponent.class);
                e.edit().remove(WeaponComponent.class);
                e.edit().remove(BounceComponent.class);
                e.edit().remove(ActionAfterTimeComponent.class);
            }
        };
    }


    public Task dropAndExplode() {
        return new Task() {
            @Override
            public void performAction(World world, Entity e) {

                e.getComponent(VelocityComponent.class).velocity.x = 0;
                e.getComponent(AnimationStateComponent.class).setDefaultState(EXPLODING_ANIMATION);

                    e.edit().add(new ActionAfterTimeComponent(new Action() {
                    @Override
                    public void performAction(World world, Entity e) {
                        e.edit().add(new GravityComponent());
                        OnCollisionActionComponent ocac = new OnCollisionActionComponent();
                        ocac.bottom = new Action() {
                            @Override
                            public void performAction(World world, Entity e) {
                                bombFactory.explosionTask().performAction(world, e);
                                e.edit().remove(OnCollisionActionComponent.class);
                            }
                        };
                        e.edit().add(ocac);
                    }

                }, dropTime));
            }

            @Override
            public void cleanUpAction(World world, Entity e) {
                e.edit().remove(ActionAfterTimeComponent.class);
            }
        };
    }



    private Task groundBomberPhase() {
        return new Task() {
            @Override
            public void performAction(World world, Entity e) {
                e.getComponent(AnimationStateComponent.class).setDefaultState(SIDEVIEW_ANIMATION);
                e.edit().add(new FiringAIComponent());
                e.edit().add(new WeaponComponent(new DropBomberWeapon(assetManager), 0));
            }

            @Override
            public void cleanUpAction(World world, Entity e) {
                e.getComponent(AnimationStateComponent.class).setDefaultState(DEFAULT_ANIMATION);
                e.edit().remove(FiringAIComponent.class);
                e.edit().remove(WeaponComponent.class);
            }
        };
    }




    private class AirBomberWeapon implements Weapon {

        private Weapon bombWeapon;
        private Weapon mineWeapon;
        private boolean isBomb;

        AirBomberWeapon(AssetManager assetManager){
            bombWeapon = new BomberPistol.PistolBuilder(assetManager)
                    .angles(240, 300)
                    .build();
            mineWeapon = new MinePistol.PistolBuilder(assetManager)
                    .angles(270)
                    .build();
        }

        @Override
        public void fire(World world, Entity e, float x, float y, double angleInRadians) {
            if(isBomb) {
                bombWeapon.fire(world, e, x, y, angleInRadians);
                isBomb = !isBomb;
            } else {
                mineWeapon.fire(world, e, x, y, angleInRadians);
                isBomb = !isBomb;
            }

            world.getSystem(SoundSystem.class).playRandomSound(SoundFileStrings.enemyFireMix);
        }

        @Override
        public float getBaseFireRate() {
            return airBomberWeaponFireRate;
        }

        @Override
        public float getBaseDamage() {
            return 0;
        }
    }


    private class DropBomberWeapon implements Weapon {

        private Weapon bombWeapon;
        private boolean isLeft;
        private Random random = new Random();

        DropBomberWeapon(AssetManager assetManager){
            bombWeapon = new BomberPistol.PistolBuilder(assetManager)
                    .angles(0)
                    .shotSpeedY(Measure.units(75f))
                    .shotSpeedX(Measure.units(75f))
                    .build();
        }

        @Override
        public void fire(World world, Entity e, float x, float y, double angleInRadians) {
            if(isLeft) {
                int i = random.nextInt(90) + 90;
                e.getComponent(TextureRegionComponent.class).scaleX = 1;
                bombWeapon.fire(world, e, x, y, Math.toRadians(i));
                isLeft = !isLeft;
            } else {
                e.getComponent(TextureRegionComponent.class).scaleX = -1;
                bombWeapon.fire(world, e , x, y, Math.toRadians(random.nextInt(90)));
                isLeft = !isLeft;
            }

            world.getSystem(SoundSystem.class).playRandomSound(SoundFileStrings.enemyFireMix);

        }

        @Override
        public float getBaseFireRate() {
            return groundBomberWeaponFireRate;
        }

        @Override
        public float getBaseDamage() {
            return 0;
        }
    }





}
