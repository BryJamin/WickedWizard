package com.byrjamin.wickedwizard.factories.enemy.bosses;

import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IntMap;
import com.byrjamin.wickedwizard.assets.ColorResource;
import com.byrjamin.wickedwizard.assets.SoundFileStrings;
import com.byrjamin.wickedwizard.assets.TextureStrings;
import com.byrjamin.wickedwizard.ecs.components.movement.CollisionBoundComponent;
import com.byrjamin.wickedwizard.ecs.components.Weapon;
import com.byrjamin.wickedwizard.ecs.components.WeaponComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.Task;
import com.byrjamin.wickedwizard.ecs.components.ai.ActionAfterTimeComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.ExpireComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.ExpiryRangeComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.FiringAIComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.OnDeathActionComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.PhaseComponent;
import com.byrjamin.wickedwizard.ecs.components.identifiers.BulletComponent;
import com.byrjamin.wickedwizard.ecs.components.identifiers.EnemyComponent;
import com.byrjamin.wickedwizard.ecs.components.identifiers.IntangibleComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.OrbitComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.PositionComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.VelocityComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.AnimationComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.AnimationStateComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.FadeComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.TextureRegionComponent;
import com.byrjamin.wickedwizard.ecs.systems.audio.SoundSystem;
import com.byrjamin.wickedwizard.ecs.systems.level.RoomTransitionSystem;
import com.byrjamin.wickedwizard.factories.arenas.Arena;
import com.byrjamin.wickedwizard.factories.enemy.EnemyFactory;
import com.byrjamin.wickedwizard.factories.weapons.Giblets;
import com.byrjamin.wickedwizard.utils.BulletMath;
import com.byrjamin.wickedwizard.utils.ComponentBag;
import com.byrjamin.wickedwizard.utils.Measure;
import com.byrjamin.wickedwizard.utils.collider.HitBox;
import com.byrjamin.wickedwizard.utils.enums.Direction;

import java.util.Random;

/**
 * Created by Home on 27/05/2017.
 */

public class BossWanda extends EnemyFactory {

    private static final float width = Measure.units(7.5f);
    private static final float height = Measure.units(7.5f);

    private static final float textureWidth = Measure.units(10);
    private static final float textureHeight = Measure.units(10);

    private static final float textureOffsetX = -Measure.units(1.25f);
    private static final float textureOffsetY = 0;


    private static final float shieldBlastFireRate = 0.1f;
    private static final float shieldBlastWarningTime = 0.75f;

    //Phases
    private static final float fadeDashFadeTime = 0.15f;
    private static final float fadeDashPhaseTime = 0.5f;

    private static final float firingPhaseTime = 2.5f;

    private Giblets.GibletBuilder gibletBuilder;


    public BossWanda(AssetManager assetManager) {
        super(assetManager);

        this.gibletBuilder = new Giblets.GibletBuilder(assetManager)
                .numberOfGibletPairs(3)
                .size(Measure.units(0.5f))
                .minSpeed(Measure.units(10f))
                .maxSpeed(Measure.units(20f))
                .colors(new Color(Color.RED))
                .mixes(SoundFileStrings.queitExplosionMegaMix)
                .intangible(false)
                .expiryTime(0.2f);

    }



    public ComponentBag wanda(float x, float y) {

        x = x - width / 2;
        y = y - height / 2;


        ComponentBag bag = defaultBossBag(new ComponentBag(), x , y, 75);

        bag.add(new VelocityComponent());

        bag.add(new CollisionBoundComponent(new Rectangle(x,y,width,height), true));

        bag.add(new TextureRegionComponent(atlas.findRegion(TextureStrings.WANDA_STANDING),
                textureOffsetX,
                textureOffsetY,
                textureWidth,
                textureHeight,
                TextureRegionComponent.FOREGROUND_LAYER_MIDDLE));


        bag.add(new AnimationStateComponent(0));
        IntMap<Animation<TextureRegion>> animMap = new IntMap<Animation<TextureRegion>>();
        animMap.put(0, new Animation<TextureRegion>(0.1f / 1f, atlas.findRegions(TextureStrings.WANDA_STANDING), Animation.PlayMode.LOOP));
        animMap.put(AnimationStateComponent.FIRING, new Animation<TextureRegion>(0.05f / 1f, atlas.findRegions(TextureStrings.WANDA_FIRING)));

        bag.add(new AnimationComponent(animMap));


        bag.add(new FiringAIComponent());


        PhaseComponent pc = new PhaseComponent();
        PhaseFadeDash phaseFadeDash = new PhaseFadeDash();
        Phase3 p3 = new Phase3(Direction.LEFT, Direction.RIGHT);



        //Alternate between jump and spinny

        pc.addPhase(fadeDashPhaseTime, phaseFadeDash);
        pc.addPhase(firingPhaseTime, p3);
        pc.addPhase(fadeDashPhaseTime, phaseFadeDash);
        pc.addPhase(firingPhaseTime, p3);
        pc.addPhase(fadeDashPhaseTime, phaseFadeDash);
        pc.addPhase(firingPhaseTime, new Phase3AOEORCenterFire());
        //pc.addPhase(fadeDashPhaseTime, phaseFadeDash);
        //pc.addPhase(2.5f, new AOEShieldBlastTask());

        bag.add(pc);

        return bag;


    }



    private class Phase3AOEORCenterFire implements Task {

        private Array<Task> tasksShuffle = new Array<Task>();
        private Array<Task> tasks = new Array<Task>();

        private Task currentTask;


        private Phase3AOEORCenterFire(){
            Task aoe = new AOEShieldBlastTask();
            Task aboveFire =  new Phase3(Direction.UP);
            tasksShuffle.addAll(aoe, aboveFire);
        }


        @Override
        public void performAction(World world, Entity e) {

            if(tasks.size == 0) {
                tasksShuffle.shuffle();
                tasks.addAll(tasksShuffle);
            }

            currentTask = tasks.pop();
            currentTask.performAction(world, e);


        }

        @Override
        public void cleanUpAction(World world, Entity e) {
            currentTask.cleanUpAction(world, e);
        }
    }



    private class AOEShieldBlastTask implements Task {
        @Override
        public void performAction(World world, Entity e) {
            e.edit().add(new FadeComponent(true, fadeDashFadeTime, false));
            e.edit().add(new WeaponComponent(new ShieldBlast(), shieldBlastWarningTime));
            Arena a = world.getSystem(RoomTransitionSystem.class).getCurrentArena();
            PositionComponent pc = e.getComponent(PositionComponent.class);

            float x = a.getWidth() / 2 - width / 2;
            float y = a.getHeight() / 2 - height / 2;

            pc.position.set(x, y, 0);


            for(int i = 0; i < 4; i ++){
                Entity warning = world.createEntity();
                warning.edit().add(new PositionComponent(x,y));
                warning.edit().add(new CollisionBoundComponent(new Rectangle(x,y, Measure.units(2f), Measure.units(2f))));
                warning.edit().add(new OrbitComponent(pc.position, Measure.units(10f), -10, 90 * i, width / 2, height / 2));
                warning.edit().add(new IntangibleComponent());

                TextureRegionComponent trc = new TextureRegionComponent(atlas.findRegion(TextureStrings.BLOCK),
                        Measure.units(2f),
                        Measure.units(2f),
                        TextureRegionComponent.ENEMY_LAYER_NEAR, new Color(ColorResource.ENEMY_BULLET_COLOR));

                warning.edit().add(trc);

                warning.edit().add(new FadeComponent(true, 0.5f, false));
                warning.edit().add(new ExpireComponent(shieldBlastWarningTime));


            }




        }

        @Override
        public void cleanUpAction(World world, Entity e) {
            e.edit().remove(FadeComponent.class);
            e.edit().remove(WeaponComponent.class);
        }
    }

    private class PhaseFadeDash implements Task {
        Random random = new Random();

        @Override
        public void performAction(World world, Entity e) {
            //e.edit().add(new GravityComponent());
            e.edit().add(new FadeComponent(false, fadeDashFadeTime, false));
            e.edit().add(new IntangibleComponent());

            //TODO make a harmless component or something
            e.getComponent(CollisionBoundComponent.class).hitBoxes.clear();

            e.getComponent(VelocityComponent.class).velocity.x = random.nextBoolean() ? Measure.units(100f) : -Measure.units(100f);
        }

        @Override
        public void cleanUpAction(World world, Entity e) {
            //e.edit().remove(GravityComponent.class);
            e.edit().remove(FadeComponent.class);
            e.edit().remove(IntangibleComponent.class);
            e.getComponent(VelocityComponent.class).velocity.x = 0;
            e.getComponent(CollisionBoundComponent.class).hitBoxes.add(new HitBox(e.getComponent(CollisionBoundComponent.class).bound));
        }
    }


    private class Phase3 implements Task {

        private Direction direction;

        private Array<Direction> directionChoices = new Array<Direction>();
        private Array<Direction> directions = new Array<Direction>();

        public Phase3(Direction... direction){
            directionChoices.addAll(direction);
        }

        @Override
        public void performAction(World world, Entity e) {


            if(directions.size == 0) {
                directions.addAll(directionChoices);
                directionChoices.shuffle();
            }


            direction = directions.pop();

            e.edit().add(new FadeComponent(true, fadeDashFadeTime, false));
            Arena a = world.getSystem(RoomTransitionSystem.class).getCurrentArena();
            PositionComponent pc = e.getComponent(PositionComponent.class);

            float y = (a.getHeight() - Measure.units(15f)) - height / 2;

            float[] angles;

            switch (direction) {
                case UP:
                default:
                    pc.position.set(a.getWidth() / 2 - width / 2, y, 0);
                    angles = new float[]{200, 210,220,230,240,300,310,320, 330, 340};
                    break;

                case LEFT:
                    pc.position.set(Measure.units(7.5f), y, 0);
                    angles = new float[]{0, 337.5f, 315,292.5f, 270};
                    break;
                case RIGHT:
                    pc.position.set(a.getWidth() - Measure.units(7.5f) - width, y , 0);
                    angles = new float[]{180,202.5f, 225,245.5f, 270};
                    break;
            }


            e.edit().add(new WeaponComponent(new FanWeapon(angles), 1f));

        }

        @Override
        public void cleanUpAction(World world, Entity e) {

            e.edit().remove(FadeComponent.class);
            e.edit().remove(WeaponComponent.class);

        }
    }



    private class FanWeapon implements Weapon {

        private float[] angles = new float[]{0, 315, 270, 225, 180};

        private FanWeapon(float[] angles){
            this.angles = angles;
        }

        @Override
        public void fire(World world, Entity e, float x, float y, double angleInRadians) {

            for(int i = 0; i < angles.length; i++) {
                createBlock(world, new Vector3(e.getComponent(CollisionBoundComponent.class).getCenterX(),
                        e.getComponent(CollisionBoundComponent.class).getCenterY(),
                        0), angles[i], new Color(Color.RED));
            }

            world.getSystem(SoundSystem.class).playRandomSound(SoundFileStrings.enemyFireMegaMix);

        }

        @Override
        public float getBaseFireRate() {
            return 0.5f;
        }

        @Override
        public float getBaseDamage() {
            return 0;
        }

        public void createBlock(World world, Vector3 centerOfOrbit, final float startAngle, Color color) {

            Entity e = world.createEntity();
            float radius = Measure.units(10);
            float size = Measure.units(5);

            final float x = (float) (centerOfOrbit.x + (radius * Math.cos(Math.toRadians(startAngle)))) - size / 2;
            final float y = (float) (centerOfOrbit.y + (radius * Math.sin(Math.toRadians(startAngle)))) - size / 2;

            e.edit().add(new PositionComponent(x, y));
            e.edit().add(new VelocityComponent());
            e.edit().add(new BulletComponent());
            e.edit().add(new EnemyComponent());
            e.edit().add(new CollisionBoundComponent(new Rectangle(x, y, size, size), true));
            e.edit().add(new ExpiryRangeComponent(new Vector3(x, y, 0), Measure.units(200f)));
            //e.edit().add(new OrbitComponent(centerOfOrbit, radius, 2, startAngle, width / 2, height / 2));
            e.edit().add(new FadeComponent(true, 0.2f, false));
            e.edit().add(new OnDeathActionComponent(gibletBuilder.build()));

            TextureRegionComponent trc = new TextureRegionComponent(atlas.findRegion(TextureStrings.BLOCK), size, size, TextureRegionComponent.ENEMY_LAYER_FAR);
            trc.DEFAULT = color;
            trc.color = color;
            e.edit().add(trc);


            e.edit().add(new ActionAfterTimeComponent(new Task() {
                @Override
                public void performAction(World world, Entity e) {
                    VelocityComponent vc = e.getComponent(VelocityComponent.class);
                    vc.velocity.x = BulletMath.velocityX(Measure.units(80f), Math.toRadians(startAngle));
                    vc.velocity.y = BulletMath.velocityY(Measure.units(80f), Math.toRadians(startAngle));

                    world.getSystem(SoundSystem.class).playRandomSound(SoundFileStrings.enemyFireMegaMix);
                }

                @Override
                public void cleanUpAction(World world, Entity e) {

                }
            }, 0.5f));

        }


    }

    private class ShieldBlast implements Weapon {

        private float[] angles = new float[]{0, 45, 90, 135, 180, 225, 270, 315};


        @Override
        public void fire(World world, Entity e, float x, float y, double angleInRadians) {


            for(int i = 0; i < angles.length; i++) {
                createBlock(world, new Vector3(e.getComponent(CollisionBoundComponent.class).getCenterX(),
                        e.getComponent(CollisionBoundComponent.class).getCenterY(),
                        0), angles[i], new Color(Color.RED));
                angles[i] += 22.5f;
            }

            world.getSystem(SoundSystem.class).playRandomSound(SoundFileStrings.enemyFireMegaMix);



        }

        @Override
        public float getBaseFireRate() {
            return shieldBlastFireRate;
        }

        @Override
        public float getBaseDamage() {
            return 0;
        }


        public void createBlock(World world, Vector3 centerOfOrbit, final float startAngle, Color color){

            Entity e = world.createEntity();
            float radius = Measure.units(10);

            float size = Measure.units(5);

            final float x = (float) (centerOfOrbit.x + (radius * Math.cos(Math.toRadians(startAngle)))) - size / 2;
            final float y = (float) (centerOfOrbit.y + (radius * Math.sin(Math.toRadians(startAngle)))) - size / 2;

            e.edit().add(new PositionComponent(x, y));
            e.edit().add(new VelocityComponent());
            e.edit().add(new BulletComponent());
            e.edit().add(new EnemyComponent());
            e.edit().add(new CollisionBoundComponent(new Rectangle(x,y,size,size), true));
            e.edit().add(new ExpiryRangeComponent(new Vector3(x,y,0), Measure.units(20f)));
            //e.edit().add(new OrbitComponent(centerOfOrbit, radius, 2, startAngle, width / 2, height / 2));
            e.edit().add(new FadeComponent(true, 0.2f, false));
            e.edit().add(new OnDeathActionComponent(gibletBuilder.build()));

            TextureRegionComponent trc = new TextureRegionComponent(atlas.findRegion(TextureStrings.BLOCK), size, size, TextureRegionComponent.ENEMY_LAYER_FAR);
            trc.DEFAULT = color;
            trc.color = color;
            e.edit().add(trc);


            e.edit().add(new ActionAfterTimeComponent(new Task() {
                @Override
                public void performAction(World world, Entity e) {
                    VelocityComponent vc = e.getComponent(VelocityComponent.class);
                    vc.velocity.x = BulletMath.velocityX(Measure.units(125f), Math.toRadians(startAngle));
                    vc.velocity.y = BulletMath.velocityY(Measure.units(125f), Math.toRadians(startAngle));
                }

                @Override
                public void cleanUpAction(World world, Entity e) {

                }
            }, 0.2f));







        }


    }










}