package com.byrjamin.wickedwizard.factories.enemy.bosses;

import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.IntMap;
import com.byrjamin.wickedwizard.assets.ColorResource;
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
import com.byrjamin.wickedwizard.ecs.components.ai.ExpireComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.FiringAIComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.OnDeathActionComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.PhaseComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.Task;
import com.byrjamin.wickedwizard.ecs.components.identifiers.ChildComponent;
import com.byrjamin.wickedwizard.ecs.components.identifiers.EnemyComponent;
import com.byrjamin.wickedwizard.ecs.components.identifiers.OnlyPlayerBulletsComponent;
import com.byrjamin.wickedwizard.ecs.components.identifiers.ParentComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.BounceComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.OrbitComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.PositionComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.VelocityComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.AnimationComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.AnimationStateComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.FadeComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.TextureRegionComponent;
import com.byrjamin.wickedwizard.ecs.systems.FindPlayerSystem;
import com.byrjamin.wickedwizard.ecs.systems.ai.OnDeathSystem;
import com.byrjamin.wickedwizard.ecs.systems.level.RoomTransitionSystem;
import com.byrjamin.wickedwizard.factories.GibletFactory;
import com.byrjamin.wickedwizard.factories.arenas.Arena;
import com.byrjamin.wickedwizard.factories.arenas.decor.ArenaShellFactory;
import com.byrjamin.wickedwizard.factories.enemy.EnemyFactory;
import com.byrjamin.wickedwizard.factories.weapons.enemy.MultiPistol;
import com.byrjamin.wickedwizard.utils.BulletMath;
import com.byrjamin.wickedwizard.utils.ComponentBag;
import com.byrjamin.wickedwizard.utils.Measure;
import com.byrjamin.wickedwizard.utils.collider.Collider;
import com.byrjamin.wickedwizard.utils.collider.HitBox;
import com.byrjamin.wickedwizard.utils.enums.Direction;

import java.util.Random;

/**
 * Created by Home on 05/07/2017.
 */

public class BossWraithCowl extends EnemyFactory {

    private static final float width = Measure.units(20f);
    private static final float height = Measure.units(20f);

    private static final float speed = Measure.units(20f);

    private static final float health = 80;


    private static final int SAD_STATE = 0;
    private static final int MAD_STATE = 2;


    //Flash Positions
    private static final Vector2 wraithLeftPosition = new Vector2(Measure.units(12.7f), Measure.units(22));
    private static final Vector2 wraithRightPosition = new Vector2(Measure.units(100- 12.7f) - width, Measure.units(22));
    private static final Vector2 wraithUpPosition = new Vector2(Measure.units(40f), Measure.units(32.5f));

    private static final Vector2 playerLeftPosition = new Vector2(Measure.units(75), Measure.units(10f));
    private static final Vector2 playerRightPosition = new Vector2(Measure.units(10), Measure.units(10f));
    private static final Vector2 playerUpPosition = new Vector2(Measure.units(45f), Measure.units(10f));

    private static final Vector2 wallLeftPosition = new Vector2(Measure.units(40f), Measure.units(10f));
    private static final Vector2 wallRightPosition = new Vector2(Measure.units(55f), Measure.units(10f));
    private static final Vector2 wallUpPosition = new Vector2(Measure.units(5f), Measure.units(25f));


    //Orbit Phase
    private static final float orbitCenterWidth = Measure.units(40f);
    private static final float orbitCenterHeight = Measure.units(5f);
    private static final float speedOfInvisibleCenter = Measure.units(30f);
    private static final float changeInDegrees = 1.5f;
    private static final float startingDegrees = 0;


    private static final Vector2 orbitCenterPosition = new Vector2(Measure.units(50f), Measure.units(25f));




    private static int texturelayer = TextureRegionComponent.FOREGROUND_LAYER_NEAR;


    private static final float radius = Measure.units(45f);


    private GibletFactory gibletFactory;


    private Random random = new Random(); //TODO pull in the global random

    public BossWraithCowl(AssetManager assetManager) {
        super(assetManager);
        this.gibletFactory = new GibletFactory(assetManager);
    }


    public ComponentBag wraithCowl(float x, float y){

        x = x - width / 2;
        y = y - height / 2;

        ComponentBag bag = this.defaultEnemyBag(new ComponentBag(), x, y, health);

        bag.add(new CollisionBoundComponent(new Rectangle(x, y, width, height)));
        bag.add(new TextureRegionComponent(atlas.findRegion(TextureStrings.COWL), width, height,
                texturelayer));
        bag.add(new ParentComponent());


        bag.add(new AnimationStateComponent());
        IntMap<Animation<TextureRegion>> animMap = new IntMap<Animation<TextureRegion>>();
        animMap.put(SAD_STATE, new Animation<TextureRegion>(0.05f / 1f, atlas.findRegions(TextureStrings.ADOJ), Animation.PlayMode.LOOP));
        animMap.put(AnimationStateComponent.FIRING, new Animation<TextureRegion>(0.025f / 1f, atlas.findRegions(TextureStrings.ADOJ_FIRING)));
        bag.add(new AnimationComponent(animMap));



        bag.add(new FadeComponent(false, 0.5f, false));


        bag.add(new ActionAfterTimeComponent(new Action() {
            @Override
            public void performAction(World world, Entity e) {

                FadeInPhaseLeft fadeInPhaseLeft = new FadeInPhaseLeft(random);

                e.getComponent(CollisionBoundComponent.class)
                        .hitBoxes.add(new HitBox(new Rectangle(e.getComponent(CollisionBoundComponent.class).bound)));

                PhaseComponent phaseComponent = new PhaseComponent();
                phaseComponent.addPhase(6f, fadeInPhaseLeft);
                phaseComponent.addPhase(6f, fadeInPhaseLeft);
                phaseComponent.addPhase(6f, fadeInPhaseLeft);
                phaseComponent.addPhase(6f, fadeInPhaseLeft);
                phaseComponent.addPhase(10f, new OrbitalChasePhase(random));

                e.edit().add(phaseComponent);

            }
        }, 0.7f));

        return bag;


    }




    private class FadeOutPhase implements Task {

        @Override
        public void performAction(World world, Entity e) {
            e.edit().add(new FadeComponent(false, 0.5f, false));
        }

        @Override
        public void cleanUpAction(World world, Entity e) {
            e.edit().remove(FadeComponent.class);
        }
    }

    private class FadeInPhaseLeft implements Task {


        private Weapon ghostMeapon;
        private Weapon higherWeapon;
        private Random random;

        private Direction positionInRoom;

        public FadeInPhaseLeft(Direction direction, Random random){
            ghostMeapon = new MultiPistol.PistolBuilder(assetManager)
                    .fireRate(2f)
                    .angles(0, -20, 20, 40, -40)
                    .intangible(true)
                    .shotScale(3)
                    .color(ColorResource.GHOST_BULLET_COLOR)
                    .enemy(true)
                    .expireRange(Measure.units(100f))
                    .build();


            higherWeapon = new MultiPistol.PistolBuilder(assetManager)
                    .fireRate(2f)
                    .angles(0)
                    .shotScale(8)
                    .shotSpeed(Measure.units(30f))
                    .intangible(true)
                    .color(ColorResource.GHOST_BULLET_COLOR)
                    .enemy(true)
                    .expireRange(Measure.units(100f))
                    .build();;


            this.random = random;
            this.positionInRoom = direction;
        }


        public FadeInPhaseLeft(Random random){
            this(Direction.LEFT, random);
            Direction[] choice = new Direction[]{Direction.LEFT, Direction.RIGHT, Direction.UP};
            positionInRoom = choice[random.nextInt(choice.length)];
        }


        @Override
        public void performAction(World world, Entity e) {


            e.edit().add(new FadeComponent(true, 0.5f, false));

            PositionComponent pc = e.getComponent(PositionComponent.class);

            switch (positionInRoom){

                case LEFT:
                    pc.position.set(wraithLeftPosition, 0);

                    for(int i = 0; i < 9; i++){
                        createBlock(world, e.getComponent(ParentComponent.class), wallLeftPosition.x, wallLeftPosition.y + Measure.units(i * 5), 0, new Color(0,0,0,0));
                    }

                    world.getSystem(FindPlayerSystem.class).getPC(PositionComponent.class).position.set(playerLeftPosition, 0);
                    e.edit().add(new VelocityComponent(0, random.nextBoolean() ? speed : -speed));
                    e.edit().add(new FiringAIComponent(0));
                    e.edit().add(new WeaponComponent(ghostMeapon, 0.5f));

                    break;
                case RIGHT:
                    pc.position.set(wraithRightPosition, 0);


                    for(int i = 0; i < 9; i++){
                        createBlock(world, e.getComponent(ParentComponent.class), wallRightPosition.x, wallRightPosition.y + Measure.units(i * 5), 180, new Color(0,0,0,0));
                    }

                    world.getSystem(FindPlayerSystem.class).getPC(PositionComponent.class).position.set(playerRightPosition, 0);
                    e.edit().add(new VelocityComponent(0, random.nextBoolean() ? speed : -speed));
                    e.edit().add(new FiringAIComponent(180));
                    e.edit().add(new WeaponComponent(ghostMeapon, 0.5f));

                    break;
                case UP:

                    pc.position.set(wraithUpPosition, 0);

                    for(int i = 0; i < 18; i++){
                        createBlock(world, e.getComponent(ParentComponent.class), wallUpPosition.x + Measure.units((i * 5)), wallUpPosition.y, 270, new Color(0,0,0,0));
                    }

                    world.getSystem(FindPlayerSystem.class).getPC(PositionComponent.class).position.set(playerUpPosition, 0);
                    e.edit().add(new VelocityComponent(random.nextBoolean() ? speed : -speed, 0));
                    e.edit().add(new FiringAIComponent());
                    e.edit().add(new WeaponComponent(higherWeapon, 0.5f));

            }

            e.edit().add(new BounceComponent());

            positionInRoom = pickNextDirection(positionInRoom);
            createWhiteFlash(world);


        }

        @Override
        public void cleanUpAction(World world, Entity e) {
            world.getSystem(OnDeathSystem.class).killChildComponentsIgnoreOnDeath(e.getComponent(ParentComponent.class));
            e.edit().remove(FadeComponent.class);
            e.edit().remove(FiringAIComponent.class);
            e.edit().remove(WeaponComponent.class);
            e.edit().remove(VelocityComponent.class);
        }


        public Direction pickNextDirection(Direction direction){

            switch (direction){
                case LEFT: direction = random.nextBoolean() ? Direction.UP : Direction.RIGHT;
                    break;
                case RIGHT: direction = random.nextBoolean() ? Direction.LEFT : Direction.UP;
                    break;
                case UP: direction = random.nextBoolean() ? Direction.LEFT : Direction.RIGHT;
                    break;
                default: direction = random.nextBoolean() ? Direction.LEFT : Direction.RIGHT;
                    break;
            }

            return direction;

        }

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
        e.edit().add(new HealthComponent(3));
        e.edit().add(new FadeComponent(true, 1, false));
       e.edit().add(new OnDeathActionComponent(gibletFactory.defaultGiblets(new Color(Color.BLACK))));

        ChildComponent c = new ChildComponent();
        pc.children.add(c);
        e.edit().add(c);

        TextureRegionComponent trc = new TextureRegionComponent(atlas.findRegion(TextureStrings.BLOCK), 0, 0,  Measure.units(5), Measure.units(5), TextureRegionComponent.PLAYER_LAYER_FAR);
        trc.DEFAULT = color;
        trc.color = color;
        e.edit().add(trc);


        e.edit().add(new ConditionalActionComponent(new Condition() {
            @Override
            public boolean condition(World world, Entity entity) {
                return entity.getComponent(CollisionBoundComponent.class).bound.overlaps(world.getSystem(FindPlayerSystem.class).getPC(CollisionBoundComponent.class).bound);

            }


        }, new Task() {
            @Override
            public void performAction(World world, Entity e) {
                VelocityComponent vc = world.getSystem(FindPlayerSystem.class).getPC(VelocityComponent.class);
                vc.velocity.x = BulletMath.velocityX(Measure.units(50f), Math.toRadians(pushAngle));
                vc.velocity.y = BulletMath.velocityY(Measure.units(50f), Math.toRadians(pushAngle));
            }

            @Override
            public void cleanUpAction(World world, Entity e) {

            }
        }));

    }



    private class OrbitalChasePhase implements Task {

        private Random random;

        private OrbitalChasePhase(Random random){
            this.random = random;
        }


        @Override
        public void performAction(World world, Entity e) {


            ParentComponent parentComponent = e.getComponent(ParentComponent.class);


            Arena arena = world.getSystem(RoomTransitionSystem.class).getCurrentArena();

            //create center to orbit
            Entity center = world.createEntity();
            PositionComponent centerPosition = new PositionComponent(orbitCenterPosition.x - orbitCenterWidth / 2, orbitCenterPosition.y - orbitCenterHeight / 2);
            center.edit().add(new CollisionBoundComponent(new Rectangle(centerPosition.getX(), centerPosition.getY(), orbitCenterWidth, orbitCenterHeight)));
            center.edit().add(centerPosition);
            center.edit().add(new BounceComponent());
            center.edit().add(new VelocityComponent(random.nextBoolean() ? speedOfInvisibleCenter : -speedOfInvisibleCenter, 0));
            center.edit().add(new ChildComponent(parentComponent));



            float startAngleInDegrees = random.nextInt(360);

            float orbitOffsetX = orbitCenterWidth / 2;
            float orbitOffsetY = orbitCenterHeight / 2;

            //Wraith
            e.edit().add(new FadeComponent(true, 0.5f, false));
            e.edit().add(new OrbitComponent(centerPosition.position, radius, changeInDegrees, startAngleInDegrees, orbitOffsetX, orbitOffsetY));


            //Fake Wraiths
            for(int i = 40; i < 360; i+=40) {
                Entity fakeWraith = world.createEntity();
                fakeWraith.edit().add(new PositionComponent());
                fakeWraith.edit().add(new CollisionBoundComponent(new Rectangle(0,0, width, height), true));
                fakeWraith.edit().add(new EnemyComponent());
                fakeWraith.edit().add(new OrbitComponent(centerPosition.position, radius, changeInDegrees, startAngleInDegrees + i, orbitOffsetX, orbitOffsetY));
                TextureRegionComponent trc = new TextureRegionComponent(atlas.findRegion(TextureStrings.COWL), width, height,
                        texturelayer);
                trc.color.a = 0;
                fakeWraith.edit().add(trc);

                FadeComponent fadeComponent = new FadeComponent(true, 0.5f, true);
                fadeComponent.maxAlpha = 0.5f;

                fakeWraith.edit().add(fadeComponent);
                fakeWraith.edit().add(new ChildComponent(parentComponent));


            }


            //player

            CollisionBoundComponent playerCbc = world.getSystem(FindPlayerSystem.class)
                    .getPC(CollisionBoundComponent.class);
            playerCbc.setCenter(arena.getWidth() / 2, arena.getHeight() / 2);
            world.getSystem(FindPlayerSystem.class).getPC(PositionComponent.class)
                    .position.set(playerCbc.bound.x, playerCbc.bound.y, 0);



            createWhiteFlash(world);



        }

        @Override
        public void cleanUpAction(World world, Entity e) {
            world.getSystem(OnDeathSystem.class).killChildComponents(e.getComponent(ParentComponent.class));
            e.edit().remove(OrbitComponent.class);

        }
    }



    public Entity createWhiteFlash(World world){
        Entity e = world.createEntity();
        e.edit().add(new PositionComponent(0, 0));
        e.edit().add(new TextureRegionComponent(atlas.findRegion(TextureStrings.BLOCK), ArenaShellFactory.SECTION_WIDTH, ArenaShellFactory.SECTION_HEIGHT, TextureRegionComponent.FOREGROUND_LAYER_NEAR,
                new Color(Color.WHITE)));
        e.edit().add(new ExpireComponent(0.2f));
        return e;
    }





}
