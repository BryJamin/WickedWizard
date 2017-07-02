package com.byrjamin.wickedwizard.factories.enemy.bosses;

import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.IntMap;
import com.byrjamin.wickedwizard.assets.TextureStrings;
import com.byrjamin.wickedwizard.ecs.components.CollisionBoundComponent;
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
import com.byrjamin.wickedwizard.ecs.systems.level.RoomTransitionSystem;
import com.byrjamin.wickedwizard.factories.BulletFactory;
import com.byrjamin.wickedwizard.factories.DeathFactory;
import com.byrjamin.wickedwizard.factories.GibletFactory;
import com.byrjamin.wickedwizard.factories.arenas.Arena;
import com.byrjamin.wickedwizard.factories.enemy.EnemyFactory;
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

    private DeathFactory df;
    private BulletFactory bf;

    private final float width = Measure.units(7.5f);
    private final float height = Measure.units(7.5f);

    private final float textureWidth = Measure.units(10);
    private final float textureHeight = Measure.units(10);

    private final float textureOffsetX = -Measure.units(1.25f);
    private final float textureOffsetY = 0;

    private final GibletFactory gibletFactory;


    public BossWanda(AssetManager assetManager) {
        super(assetManager);
        this.df = new DeathFactory(assetManager);
        this.bf = new BulletFactory(assetManager);
        this.gibletFactory = new GibletFactory(assetManager);
    }



    public ComponentBag wanda(float x, float y) {

        x = x - width / 2;
        y = y - height / 2;


        ComponentBag bag = new ComponentBag();
        defaultEnemyBag(bag, x , y, 75);

        bag.add(new VelocityComponent());
       // bag.add(new GravityComponent());

        bag.add(new CollisionBoundComponent(new Rectangle(x,y,width,height), true));

        bag.add(new TextureRegionComponent(atlas.findRegion(TextureStrings.WANDA_STANDING),
                textureOffsetX,
                textureOffsetY,
                textureWidth,
                textureHeight,
                TextureRegionComponent.ENEMY_LAYER_MIDDLE));


        bag.add(new AnimationStateComponent(0));
        IntMap<Animation<TextureRegion>> animMap = new IntMap<Animation<TextureRegion>>();
        animMap.put(0, new Animation<TextureRegion>(0.1f / 1f, atlas.findRegions(TextureStrings.WANDA_STANDING), Animation.PlayMode.LOOP));
        animMap.put(AnimationStateComponent.FIRING, new Animation<TextureRegion>(0.05f / 1f, atlas.findRegions(TextureStrings.WANDA_FIRING)));

        bag.add(new AnimationComponent(animMap));


        bag.add(new FiringAIComponent());


        PhaseComponent pc = new PhaseComponent();
        Phase1AOE p1 = new Phase1AOE();
        PhaseFadeDash p2 = new PhaseFadeDash();
        Phase3 p3 = new Phase3(Direction.LEFT);

        pc.addPhase(0.5f, p2);
        pc.addPhase(2.5f, p3);
        pc.addPhase(0.5f, p2);
        pc.addPhase(2.5f, p3);
        pc.addPhase(0.5f, p2);
        pc.addPhase(2.5f, p3);
        pc.addPhase(0.5f, p2);
        pc.addPhase(2.5f, p1);

        bag.add(pc);

        return bag;


    }



    private class Phase1AOE implements Task {
        @Override
        public void performAction(World world, Entity e) {
            e.edit().add(new FadeComponent(true, 0.5f, false));
            e.edit().add(new WeaponComponent(new ShieldBlast(), 0.5f));
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

                TextureRegionComponent trc = new TextureRegionComponent(atlas.findRegion("block"),
                        Measure.units(2f),
                        Measure.units(2f),
                        TextureRegionComponent.ENEMY_LAYER_NEAR, new Color(0xff000099));

                warning.edit().add(trc);

                warning.edit().add(new FadeComponent(true, 0.5f, false));
                warning.edit().add(new ExpireComponent(0.5f));


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
            e.edit().add(new FadeComponent(false, 0.25f, false));
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

        public Phase3(Direction direction){
            this.direction = direction;
        }

        @Override
        public void performAction(World world, Entity e) {
            e.edit().add(new FadeComponent(true, 0.5f, false));
            Arena a = world.getSystem(RoomTransitionSystem.class).getCurrentArena();
            PositionComponent pc = e.getComponent(PositionComponent.class);

            float y = (a.getHeight() - Measure.units(15f)) - height / 2;

            float[] angles;

            switch (direction) {
                case UP:
                default:
                    pc.position.set(a.getWidth() / 2 - width / 2, y, 0);
                    direction = Direction.LEFT;
                    angles = new float[]{220,230,240,250,260,270,280,290,300,310,320};
                    break;

                case LEFT:
                    pc.position.set(Measure.units(10f) - width / 2, y, 0);
                    direction = Direction.RIGHT;
                    angles = new float[]{0, 337.5f, 315,292.5f, 270};
                    break;
                case RIGHT:
                    pc.position.set(a.getWidth() - Measure.units(15f) - width / 2, y , 0);
                    direction = Direction.UP;
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
            e.edit().add(new OnDeathActionComponent(gibletFactory.defaultGiblets(new Color(Color.RED))));

            TextureRegionComponent trc = new TextureRegionComponent(atlas.findRegion("block"), size, size, TextureRegionComponent.PLAYER_LAYER_FAR);
            trc.DEFAULT = color;
            trc.color = color;
            e.edit().add(trc);


            e.edit().add(new ActionAfterTimeComponent(new Task() {
                @Override
                public void performAction(World world, Entity e) {
                    VelocityComponent vc = e.getComponent(VelocityComponent.class);
                    vc.velocity.x = BulletMath.velocityX(Measure.units(80f), Math.toRadians(startAngle));
                    vc.velocity.y = BulletMath.velocityY(Measure.units(80f), Math.toRadians(startAngle));

                    System.out.println("angle is :" + startAngle);

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



        }

        @Override
        public float getBaseFireRate() {
            return 0.1f;
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
            e.edit().add(new OnDeathActionComponent(gibletFactory.defaultGiblets(new Color(Color.RED))));

            TextureRegionComponent trc = new TextureRegionComponent(atlas.findRegion("block"), size, size, TextureRegionComponent.PLAYER_LAYER_FAR);
            trc.DEFAULT = color;
            trc.color = color;
            e.edit().add(trc);


            e.edit().add(new ActionAfterTimeComponent(new Task() {
                @Override
                public void performAction(World world, Entity e) {
                    VelocityComponent vc = e.getComponent(VelocityComponent.class);
                    vc.velocity.x = BulletMath.velocityX(Measure.units(125f), Math.toRadians(startAngle));
                    vc.velocity.y = BulletMath.velocityY(Measure.units(125f), Math.toRadians(startAngle));

                    System.out.println("angle is :" + startAngle);

                }

                @Override
                public void cleanUpAction(World world, Entity e) {

                }
            }, 0.2f));







        }


    }










}