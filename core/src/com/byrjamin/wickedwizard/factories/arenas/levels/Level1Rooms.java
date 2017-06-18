package com.byrjamin.wickedwizard.factories.arenas.levels;

import com.artemis.Component;
import com.artemis.Entity;
import com.artemis.World;
import com.artemis.utils.Bag;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Array;
import com.byrjamin.wickedwizard.ecs.components.ai.Task;
import com.byrjamin.wickedwizard.ecs.components.ai.OnDeathActionComponent;
import com.byrjamin.wickedwizard.ecs.systems.level.RoomTransitionSystem;
import com.byrjamin.wickedwizard.factories.AbstractFactory;
import com.byrjamin.wickedwizard.factories.GibletFactory;
import com.byrjamin.wickedwizard.factories.arenas.Arena;
import com.byrjamin.wickedwizard.factories.arenas.ArenaBuilder;
import com.byrjamin.wickedwizard.factories.arenas.ArenaGen;
import com.byrjamin.wickedwizard.factories.arenas.decor.ArenaEnemyPlacementFactory;
import com.byrjamin.wickedwizard.factories.arenas.decor.ArenaShellFactory;
import com.byrjamin.wickedwizard.factories.arenas.decor.DecorFactory;
import com.byrjamin.wickedwizard.factories.arenas.skins.ArenaSkin;
import com.byrjamin.wickedwizard.factories.chests.ChestFactory;
import com.byrjamin.wickedwizard.factories.enemy.SpawnerFactory;
import com.byrjamin.wickedwizard.factories.enemy.TurretFactory;
import com.byrjamin.wickedwizard.utils.BagToEntity;
import com.byrjamin.wickedwizard.utils.ComponentBag;
import com.byrjamin.wickedwizard.utils.MapCoords;
import com.byrjamin.wickedwizard.utils.Measure;
import com.byrjamin.wickedwizard.utils.WeightedObject;

import java.util.Random;

/**
 * Created by Home on 13/04/2017.
 */

public class Level1Rooms extends AbstractFactory implements LevelRoomSet {

    private ArenaShellFactory arenaShellFactory;
    private ChestFactory chestFactory;
    private DecorFactory decorFactory;
    private ArenaEnemyPlacementFactory arenaEnemyPlacementFactory;
    private TurretFactory turretFactory;

    private ArenaSkin arenaSkin;
    private Random random;

    public Level1Rooms(AssetManager assetManager, ArenaSkin arenaSkin, Random random) {
        super(assetManager);
        this.arenaShellFactory = new com.byrjamin.wickedwizard.factories.arenas.decor.ArenaShellFactory(assetManager, arenaSkin);
        this.chestFactory = new ChestFactory(assetManager);
        this.arenaEnemyPlacementFactory = new com.byrjamin.wickedwizard.factories.arenas.decor.ArenaEnemyPlacementFactory(assetManager, arenaSkin, random);
        this.decorFactory = new com.byrjamin.wickedwizard.factories.arenas.decor.DecorFactory(assetManager, arenaSkin);
        this.turretFactory = new TurretFactory(assetManager);
        this.random = random;
        this.arenaSkin = arenaSkin;
    }


    @Override
    public Array<WeightedObject<ArenaGen>> getAllRooms() {
        Array<WeightedObject<ArenaGen>>  arenas = new Array<WeightedObject<ArenaGen>>();

        arenas.addAll(new WeightedObject<ArenaGen>(room1blobLeft(), 20),
                cwo(room1blobLeft()),
                cwo(room1blobRight()),
                cwo(room1blobLeft()),
                cwo(room1blobRightAndLeft()),
                cwo(room1blobRightNoBlock()),
                cwo(room2()),
                cwo(room3SquareTwoSentries()),
                cwo(room5Silverhead()),
                cwo(room6Width2And2Sentries()),
                cwo(room8TwoLockedChest())
                );





        return null;
    }

    public WeightedObject<ArenaGen> cwo (ArenaGen ag){
        return new WeightedObject<ArenaGen>(ag, 20);
    }

    public Array<ArenaGen> getLevel1RoomArray(){
        Array<ArenaGen> ag = new Array<ArenaGen>();
        ag.add(room1blobLeft());
        ag.add(room1blobRight());
        ag.add(room1blobRightAndLeft());
        ag.add(room1blobRightNoBlock());
        ag.add(room2());
        ag.add(room3SquareTwoSentries());
        ag.add(room5Silverhead());
        ag.add(room6Width2And2Sentries());
        //ag.add(room7LetterI());
        ag.add(room8TwoLockedChest());
        ag.add(room9deadEndW2());
        ag.add(room10Height2());
        ag.add(room11SquareCenterTwoBouncers());
        ag.add(room12Square2bouncersAndTwoTinyblocks());
        ag.add(room13LargeBouncer());
        ag.add(room14silverheadCenter());
        ag.add(room15WorstRoomIHaveMade());
        ag.add(room16treasureTwoturretWallsOneChest());
        ag.add(room17verticalTwoFixedTurrets());
        ag.add(room18trapTwobounceoneturret());
        ag.add(room19ThroughNarrowLasers());
        ag.add(room20CenterFixedTurret());
        ag.add(room21Width2TopBottomSeperation());
        ag.add(room22FourBlock3BouncersWidth2TopBottomSeperation());
        ag.add(room23CenterSmallspawner());
        ag.add(room24TwoTreasureAndFixedTurret());
        ag.add(room25BlobUpTop());
        ag.add(room26ThreeExitsOneTurretNoLock());
        ag.add(room27LargeBouncerGrappleRoom());
        ag.add(room28Height2GrappleUp());
        ag.add(room29BlobAndBouncer());
        return ag;
    }





    public ArenaGen room1blobLeft(){
        return new ArenaGen() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {
                Arena a = arenaShellFactory.createOmniArenaSquareCenter(defaultCoords);
                a.addEntity(arenaEnemyPlacementFactory.spawnBlob(a.getWidth() / 4, a.getHeight() / 2));
                a.roomType = Arena.RoomType.TRAP;
                return a;
            }
        };
    }

    public ArenaGen room1blobRight(){
        return new ArenaGen() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {
                Arena a = arenaShellFactory.createOmniArenaSquareCenter(defaultCoords);
                a.addEntity(arenaEnemyPlacementFactory.spawnBlob(a.getWidth() / 4 * 3, a.getHeight() / 2));
                a.roomType = Arena.RoomType.TRAP;
                return a;
            }
        };
    }

    public ArenaGen room1blobRightNoBlock(){
        return new ArenaGen() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {
                Arena a = arenaShellFactory.createOmniArenaHiddenGrapple(defaultCoords);
                a.addEntity(arenaEnemyPlacementFactory.spawnBlob(a.getWidth() / 4 * 3, a.getHeight() / 2));
                a.roomType = Arena.RoomType.TRAP;
                return a;
            }
        };
    }

    public ArenaGen room1blobRightAndLeft(){
        return new ArenaGen() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {
                Arena a = arenaShellFactory.createOmniArenaSquareCenter(defaultCoords);

                a.addWave(arenaEnemyPlacementFactory.spawnBlob(a.getWidth() / 4 * 3, a.getHeight() / 2));
                a.addWave(arenaEnemyPlacementFactory.spawnBlob(a.getWidth() / 4, a.getHeight() / 2));

                a.roomType = Arena.RoomType.TRAP;
                return a;
            }
        };
    }

    public ArenaGen room2(){
        return new ArenaGen() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {
                Arena a = arenaShellFactory.createOmniArenaSquareCenter(defaultCoords);
                a.addEntity(arenaEnemyPlacementFactory.spawnMovingSentry(a.getWidth() - Measure.units(20), a.getHeight() - Measure.units(15)));
                a.roomType = Arena.RoomType.TRAP;
                return a;
            }
        };
    }

    public ArenaGen room3SquareTwoSentries(){
        return new ArenaGen() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {
                Arena a = arenaShellFactory.createOmniArenaSquareCenter(defaultCoords);

                a.addEntity(arenaEnemyPlacementFactory.spawnMovingSentry(a.getWidth() - Measure.units(20), a.getHeight() - Measure.units(15)));
                a.addEntity(arenaEnemyPlacementFactory.spawnMovingSentry(Measure.units(20), a.getHeight() - Measure.units(15)));

                a.roomType = Arena.RoomType.TRAP;
                return a;
            }
        };
    }


    public ArenaGen room5Silverhead(){
        return new ArenaGen() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {
                Arena a = arenaShellFactory.createOmniArenaSquareCenter(defaultCoords);
                a.addEntity(arenaEnemyPlacementFactory.spawnSilverHead(a.getWidth() / 2, Measure.units(40f)));
                a.roomType = Arena.RoomType.TRAP;
                return a;
            }
        };
    }

    public ArenaGen room6Width2And2Sentries(){

        return new ArenaGen() {
        @Override
        public Arena createArena(MapCoords defaultCoords) {
            Arena a = arenaShellFactory.createWidth2ArenaWithVerticalDoors(defaultCoords);

            a.addEntity(arenaEnemyPlacementFactory.spawnMovingSentry(a.getWidth() - Measure.units(20), a.getHeight() - Measure.units(15)));
            a.addEntity(arenaEnemyPlacementFactory.spawnMovingSentry(Measure.units(20), a.getHeight() - Measure.units(15)));
            a.roomType = Arena.RoomType.TRAP;
            return a;
        }
    };
}
    public ArenaGen room7LetterI(){
        return new ArenaGen() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {
                Arena a = arenaShellFactory.createLetterIArena(defaultCoords);
               // RoomDecorationFactory.movingDoubleTurretRoom(a);
                a.addEntity(decorFactory.grapplePointBag(a.getWidth() / 2, Measure.units(40f)));
                a.addEntity(decorFactory.grapplePointBag(a.getWidth() / 2, Measure.units(70f)));
                a.addEntity(decorFactory.grapplePointBag(a.getWidth() / 2, Measure.units(100f)));
                a.addEntity(decorFactory.grapplePointBag(a.getWidth() / 2, Measure.units(130f)));
                a.addEntity(decorFactory.grapplePointBag(a.getWidth() / 2, Measure.units(160f)));
                return a;
            }
        };
    }


    public ArenaGen room8TwoLockedChest(){
        return new ArenaGen() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {

                random = new Random();
                boolean mirror = random.nextBoolean();

                Arena a = arenaShellFactory.createEitherNoLeftOrNoRightArena(defaultCoords, mirror);

                float chestPosX = mirror ? Measure.units(10f) : a.getWidth() -  Measure.units(10f);
                float chest2PosX = mirror ? Measure.units(20f) : a.getWidth() - Measure.units(20f);

                float wallPosX = mirror ? 0 : a.getWidth() - Measure.units(35f);
                float lockPosX = mirror ? Measure.units(25f) : a.getWidth() - Measure.units(35f);

                a.addEntity(chestFactory.centeredChestBag(chestPosX, a.getHeight() / 4));
                a.addEntity(chestFactory.centeredChestBag(chest2PosX, a.getHeight() / 4));

                a.addEntity(decorFactory.lockBox(lockPosX, Measure.units(10f), Measure.units(10f), Measure.units(10f)));

                a.addEntity(decorFactory.wallBag(wallPosX, Measure.units(20f), Measure.units(35f), Measure.units(10f), arenaSkin.getWallTint()));

                //RoomDecorationFactory.spawnBlob(a);
                return a;
            }
        };
    }


    public ArenaGen room9deadEndW2(){

        return new ArenaGen() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {

                boolean mirror = random.nextBoolean();

                Arena a = arenaShellFactory.createWidth2DeadEndArena(defaultCoords, mirror);

                float posX = mirror ? Measure.units(5) : a.getWidth() - Measure.units(10f);
                float lowertTurretPosX = mirror ? Measure.units(50) : a.getWidth() - Measure.units(55);
                float chestPosX = mirror ? Measure.units(30f) : a.getWidth() - Measure.units(30f);
                float blockerPosX = mirror ? a.getWidth() - Measure.units(80f) : Measure.units(80f);
                float lowertTurretWallPosX = mirror ? a.getWidth() - Measure.units(160f) : Measure.units(150f);

                float angle =  mirror ? -90 : 90;

                a.addEntity(chestFactory.centeredChestBag(chestPosX, Measure.units(15f)));

                a.addEntity(decorFactory.wallBag(blockerPosX, Measure.units(45f), Measure.units(10f), Measure.units(10f), arenaSkin));
                a.addEntity(decorFactory.wallBag(blockerPosX, Measure.units(10f), Measure.units(10f), Measure.units(10f), arenaSkin));


                a.addEntity(decorFactory.wallBag(lowertTurretWallPosX, Measure.units(45f), Measure.units(10f), Measure.units(10f), arenaSkin));
                a.addEntity(decorFactory.wallBag(lowertTurretWallPosX, Measure.units(10f), Measure.units(10f), Measure.units(10f), arenaSkin));



                a.addEntity(decorFactory.fixedWallTurret(lowertTurretPosX, Measure.units(50f),  angle, 3.0f, 0f));
                a.addEntity(decorFactory.fixedWallTurret(posX, Measure.units(40f),  angle, 3.0f, 1f));
                a.addEntity(decorFactory.fixedWallTurret(posX, Measure.units(30f),  angle, 3.0f, 1f));
                a.addEntity(decorFactory.fixedWallTurret(posX, Measure.units(20f),  angle, 3.0f, 1f));
                a.addEntity(decorFactory.fixedWallTurret(lowertTurretPosX, Measure.units(10f),  angle, 3.0f, 0f));


                return a;
            }
        };


    }


    public ArenaGen room10Height2(){

        return new ArenaGen() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {

                //TODO make two doors mandatory


                Arena arena = new Arena(arenaSkin, defaultCoords, new MapCoords(defaultCoords.getX(), defaultCoords.getY() + 1));

                arena.setWidth(ArenaShellFactory.SECTION_WIDTH);
                arena.setHeight(ArenaShellFactory.SECTION_HEIGHT * 2);

                boolean isLeftBottomTopRight = random.nextBoolean();

                arena =  new ArenaBuilder(assetManager, arenaSkin)
                        .addSection(new ArenaBuilder.Section(defaultCoords,
                                isLeftBottomTopRight ? ArenaBuilder.wall.MANDATORYDOOR : ArenaBuilder.wall.DOOR,
                                isLeftBottomTopRight ? ArenaBuilder.wall.DOOR : ArenaBuilder.wall.MANDATORYDOOR,
                                ArenaBuilder.wall.NONE,
                                ArenaBuilder.wall.FULL))
                        .addSection(new ArenaBuilder.Section(new MapCoords(defaultCoords.getX(), defaultCoords.getY() + 1),
                                isLeftBottomTopRight ? ArenaBuilder.wall.DOOR : ArenaBuilder.wall.MANDATORYDOOR,
                                isLeftBottomTopRight ? ArenaBuilder.wall.MANDATORYDOOR : ArenaBuilder.wall.DOOR,
                                ArenaBuilder.wall.FULL,
                                ArenaBuilder.wall.NONE)).buildArena(arena);


                arena.addEntity(decorFactory.wallBag(arena.getWidth() - Measure.units(60f), Measure.units(35f),
                        Measure.units(60f), Measure.units(5), arenaSkin));

                arena.addEntity(decorFactory.wallBag(0, Measure.units(65f),
                        Measure.units(60f), Measure.units(5), arenaSkin));

                arena.addEntity(decorFactory.platform(Measure.units(5), Measure.units(35f), Measure.units(35f)));

                arena.addEntity(decorFactory.platform(Measure.units(60f), Measure.units(65f), Measure.units(35f)));



                return arena;
            }
        };
    }


    public ArenaGen room11SquareCenterTwoBouncers(){

        return new ArenaGen() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {
                Arena a = arenaShellFactory.createOmniArenaSquareCenter(defaultCoords);
                a.addEntity(arenaEnemyPlacementFactory.spawnBouncer(a.getWidth() / 4,(a.getHeight() - a.getHeight() / 4) + Measure.units(2.5f)));
                a.addEntity(arenaEnemyPlacementFactory.spawnBouncer(a.getWidth() - a.getWidth() / 4,(a.getHeight() - a.getHeight() / 4) + Measure.units(2.5f)));
                a.roomType = Arena.RoomType.TRAP;

                return a;
            }
        };
    }



    public ArenaGen room12Square2bouncersAndTwoTinyblocks(){

        return new ArenaGen() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {
                Arena a = arenaShellFactory.createOmniArenaSquareCenter(defaultCoords);


                a.addEntity(arenaEnemyPlacementFactory.spawnBouncer(a.getWidth() / 4,(a.getHeight() - a.getHeight() / 4) + Measure.units(2.5f)));
                a.addEntity(arenaEnemyPlacementFactory.spawnBouncer(a.getWidth() - a.getWidth() / 4,(a.getHeight() - a.getHeight() / 4) + Measure.units(2.5f)));

                a.roomType = Arena.RoomType.TRAP;

                a.addEntity(decorFactory.wallBag(a.getWidth() - Measure.units(25f), Measure.units(35f),
                        Measure.units(5f), Measure.units(5), arenaSkin));

                a.addEntity(decorFactory.wallBag(Measure.units(20f), Measure.units(35f),
                        Measure.units(5f), Measure.units(5), arenaSkin));


                return a;
            }
        };
    }


    public ArenaGen room13LargeBouncer(){

        return new ArenaGen() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {
                Arena a = arenaShellFactory.createOmniArenaSquareCenter(defaultCoords);
                a.addEntity(arenaEnemyPlacementFactory.spawnLargeBouncer(a.getWidth() / 2,(a.getHeight() - Measure.units(15f))));
                a.roomType = Arena.RoomType.TRAP;
                return a;
            }
        };
    }


    public ArenaGen room14silverheadCenter() {

        return new ArenaGen() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {

                Arena arena = new Arena(arenaSkin, defaultCoords);


                arena.setWidth(com.byrjamin.wickedwizard.factories.arenas.decor.ArenaShellFactory.SECTION_WIDTH);
                arena.setHeight(com.byrjamin.wickedwizard.factories.arenas.decor.ArenaShellFactory.SECTION_HEIGHT);

                arena = new ArenaBuilder(assetManager, arenaSkin)
                        .addSection(new ArenaBuilder.Section(defaultCoords,
                                ArenaBuilder.wall.DOOR,
                                ArenaBuilder.wall.DOOR,
                                ArenaBuilder.wall.FULL,
                                ArenaBuilder.wall.FULL))
                        .buildArena(arena);

                arena.addEntity(decorFactory.wallBag(Measure.units(40f), Measure.units(10f), Measure.units(20f), Measure.units(25f), arenaSkin));
                arena.addEntity(decorFactory.platform(Measure.units(5), Measure.units(30f), Measure.units(35f)));
                arena.addEntity(decorFactory.platform(Measure.units(60f), Measure.units(30f), Measure.units(35f)));
                arena.addEntity(arenaEnemyPlacementFactory.spawnSilverHead(arena.getWidth() / 2, Measure.units(45f)));


                return arena;
            }
        };
    }


    public ArenaGen room15WorstRoomIHaveMade() {

        return new ArenaGen() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {

                Arena arena = new Arena(arenaSkin, defaultCoords);


                arena.setWidth(ArenaShellFactory.SECTION_WIDTH);
                arena.setHeight(ArenaShellFactory.SECTION_HEIGHT);
                arena.roomType = Arena.RoomType.TRAP;

                arena =  new ArenaBuilder(assetManager, arenaSkin)
                        .addSection(new ArenaBuilder.Section(defaultCoords,
                                ArenaBuilder.wall.DOOR,
                                ArenaBuilder.wall.DOOR,
                                ArenaBuilder.wall.FULL,
                                ArenaBuilder.wall.FULL))
                        .buildArena(arena);

                arena.addEntity(decorFactory.wallBag(Measure.units(20f), Measure.units(20f), Measure.units(5f), Measure.units(25f), arenaSkin));

                arena.addEntity(decorFactory.wallBag(arena.getWidth() - Measure.units(25f), Measure.units(20f), Measure.units(5f), Measure.units(25f), arenaSkin));


                arena.addEntity(arenaEnemyPlacementFactory.spawnerFactory.spawnerBag(arena.getWidth() / 2, arena.getHeight() / 2,
                        new SpawnerFactory.Spawner() {
                            public Bag<Component> spawnBag(float x, float y) {
                                return arenaEnemyPlacementFactory.bouncerFactory.smallBouncer(x,y);
                            }
                        }, 4));




                return arena;
            }
        };
    }


    public ArenaGen room16treasureTwoturretWallsOneChest() {

        return new ArenaGen() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {
                Arena arena = new Arena(arenaSkin, defaultCoords);


                arena.setWidth(com.byrjamin.wickedwizard.factories.arenas.decor.ArenaShellFactory.SECTION_WIDTH);
                arena.setHeight(com.byrjamin.wickedwizard.factories.arenas.decor.ArenaShellFactory.SECTION_HEIGHT);

                arena =  new ArenaBuilder(assetManager, arenaSkin)
                        .addSection(new ArenaBuilder.Section(defaultCoords,
                                ArenaBuilder.wall.DOOR,
                                ArenaBuilder.wall.DOOR,
                                ArenaBuilder.wall.FULL,
                                ArenaBuilder.wall.FULL))
                        .buildArena(arena);

                arena.mandatoryDoors.addAll(arena.doors);

                arena.addEntity(decorFactory.wallBag(0, Measure.units(30f), Measure.units(45f), arena.getHeight(), arenaSkin));
                arena.addEntity(decorFactory.wallBag(arena.getWidth() - Measure.units(45f), Measure.units(30f),
                        Measure.units(45f), Measure.units(15f), arenaSkin));

                arena.addEntity(decorFactory.fixedWallTurret(Measure.units(25), Measure.units(25f),  180, 3.0f, 1.5f));
                arena.addEntity(decorFactory.fixedWallTurret(Measure.units(30), Measure.units(25f),  180, 3.0f, 1.5f));
                arena.addEntity(decorFactory.fixedWallTurret(Measure.units(35), Measure.units(25f),  180, 3.0f, 1.5f));
                arena.addEntity(decorFactory.fixedWallTurret(Measure.units(40), Measure.units(25f),  180, 3.0f, 1.5f));

                arena.addEntity(decorFactory.fixedWallTurret(Measure.units(55), Measure.units(25f), 180, 3.0f, 1.5f));
                arena.addEntity(decorFactory.fixedWallTurret(Measure.units(60), Measure.units(25f),  180, 3.0f, 1.5f));
                arena.addEntity(decorFactory.fixedWallTurret(Measure.units(65), Measure.units(25f),  180, 3.0f, 1.5f));
                arena.addEntity(decorFactory.fixedWallTurret(Measure.units(70), Measure.units(25f),  180, 3.0f, 1.5f));


                arena.addEntity(chestFactory.centeredChestBag(arena.getWidth() - Measure.units(30f),
                        Measure.units(50f)));
/*                arena.addEntity(turretFactory.fixedTurret(posX, Measure.units(41f),  angle, 3.0f, 1.5f));
                arena.addEntity(turretFactory.fixedTurret(posX, Measure.units(32f),  angle, 3.0f, 1.5f));
                arena.addEntity(turretFactory.fixedTurret(posX, Measure.units(23f),  angle, 3.0f, 1.5f));
                arena.addEntity(turretFactory.fixedTurret(posX, Measure.units(14f),  angle, 3.0f, 0f));*/

                return arena;
            }
        };
    }





    public ArenaGen room18trapTwobounceoneturret() {

        return new ArenaGen() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {

                Arena arena = new Arena(arenaSkin, defaultCoords);


                arena.roomType = Arena.RoomType.TRAP;

                arena.setWidth(com.byrjamin.wickedwizard.factories.arenas.decor.ArenaShellFactory.SECTION_WIDTH);
                arena.setHeight(com.byrjamin.wickedwizard.factories.arenas.decor.ArenaShellFactory.SECTION_HEIGHT);

                arena =  new ArenaBuilder(assetManager, arenaSkin)
                        .addSection(new ArenaBuilder.Section(defaultCoords,
                                ArenaBuilder.wall.DOOR,
                                ArenaBuilder.wall.DOOR,
                                ArenaBuilder.wall.GRAPPLE,
                                ArenaBuilder.wall.FULL))
                        .buildArena(arena);

                ComponentBag bag = new ChestFactory(assetManager).centeredChestBag(arena.getWidth() / 2, Measure.units(15f),
                        new OnDeathActionComponent(new Task() {
                            @Override
                            public void performAction(World world, Entity e) {
                                new GibletFactory(assetManager).giblets(5,0.4f,
                                        Measure.units(20f), Measure.units(100f), Measure.units(1f), new Color(Color.WHITE)).performAction(world, e);

                                Arena arena = world.getSystem(RoomTransitionSystem.class).getCurrentArena();

                                BagToEntity.bagToEntity(world.createEntity(),
                                        arenaEnemyPlacementFactory.spawnBouncer(Measure.units(15), Measure.units(50f)));

                                BagToEntity.bagToEntity(world.createEntity(),
                                        arenaEnemyPlacementFactory.spawnBouncer(arena.getWidth() - Measure.units(15), Measure.units(50f)));

                                BagToEntity.bagToEntity(world.createEntity(),
                                        arenaEnemyPlacementFactory.spawnFixedSentry(arena.getWidth() / 2, arena.getHeight() / 2));
                            }

                            @Override
                            public void cleanUpAction(World world, Entity e) {

                            }
                        })

                );

                arena.addEntity(bag);

/*
                arena.addEntity(arenaEnemyPlacementFactory.turretFactory.fixedLockOnTurret(Measure.units(10f), Measure.units(15f)));
                arena.addEntity(arenaEnemyPlacementFactory.turretFactory.fixedLockOnTurret(arena.getWidth() - Measure.units(10f), Measure.units(15f)));

                arena.addEntity(decorFactory.wallBag(Measure.units(0), Measure.units(20f), Measure.units(40f), Measure.units(40f), arenaSkin));
                arena.addEntity(decorFactory.wallBag(arena.getWidth() - Measure.units(40f), Measure.units(20f), Measure.units(40f), Measure.units(40f), arenaSkin));
*/


                return arena;
            }
        };
    }



    public ArenaGen room17verticalTwoFixedTurrets() {

        return new ArenaGen() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {
                Arena arena = new Arena(arenaSkin, defaultCoords);


                arena.roomType = Arena.RoomType.TRAP;


                arena.setWidth(com.byrjamin.wickedwizard.factories.arenas.decor.ArenaShellFactory.SECTION_WIDTH);
                arena.setHeight(com.byrjamin.wickedwizard.factories.arenas.decor.ArenaShellFactory.SECTION_HEIGHT);

                arena =  new ArenaBuilder(assetManager, arenaSkin)
                        .addSection(new ArenaBuilder.Section(defaultCoords,
                                ArenaBuilder.wall.FULL,
                                ArenaBuilder.wall.FULL,
                                ArenaBuilder.wall.GRAPPLE,
                                ArenaBuilder.wall.DOOR))
                        .buildArena(arena);


                arena.addEntity(arenaEnemyPlacementFactory.turretFactory.fixedLockOnTurret(Measure.units(10f), Measure.units(15f)));
                arena.addEntity(arenaEnemyPlacementFactory.turretFactory.fixedLockOnTurret(arena.getWidth() - Measure.units(10f), Measure.units(15f)));

                arena.addEntity(decorFactory.wallBag(Measure.units(0), Measure.units(20f), Measure.units(40f), Measure.units(40f), arenaSkin));
                arena.addEntity(decorFactory.wallBag(arena.getWidth() - Measure.units(40f), Measure.units(20f), Measure.units(40f), Measure.units(40f), arenaSkin));

                return arena;
            }
        };
    }

    public ArenaGen room19ThroughNarrowLasers() {

        return new ArenaGen() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {
                Arena arena = new Arena(arenaSkin, defaultCoords);

                arena.setWidth(com.byrjamin.wickedwizard.factories.arenas.decor.ArenaShellFactory.SECTION_WIDTH);
                arena.setHeight(com.byrjamin.wickedwizard.factories.arenas.decor.ArenaShellFactory.SECTION_HEIGHT);

                arena =  new ArenaBuilder(assetManager, arenaSkin)
                        .addSection(new ArenaBuilder.Section(defaultCoords,
                                ArenaBuilder.wall.FULL,
                                ArenaBuilder.wall.FULL,
                                ArenaBuilder.wall.DOOR,
                                ArenaBuilder.wall.DOOR))
                        .buildArena(arena);

                arena.mandatoryDoors.addAll(arena.doors);

                //TODO good place to try offsets you can technically fall into this room

                arena.addEntity(decorFactory.wallBag(Measure.units(0), Measure.units(0), Measure.units(40f), Measure.units(60f), arenaSkin));
                arena.addEntity(decorFactory.wallBag(arena.getWidth() - Measure.units(40f), Measure.units(0), Measure.units(40f), Measure.units(60f), arenaSkin));


                arena.addEntity(decorFactory.wallBag(Measure.units(40f), Measure.units(22.5f), Measure.units(10f), Measure.units(5f), arenaSkin));
                arena.addEntity(decorFactory.fixedWallTurret(arena.getWidth() - Measure.units(45f), Measure.units(22.5f),  90, 3.0f, 1.5f));

                arena.addEntity(decorFactory.wallBag(Measure.units(50f), Measure.units(35f), Measure.units(10f), Measure.units(5f), arenaSkin));
                arena.addEntity(decorFactory.fixedWallTurret(Measure.units(40f), Measure.units(35f),  -90, 3.0f, 1.5f));

                arena.addEntity(decorFactory.platform(Measure.units(40f), Measure.units(45f),Measure.units(20f)));

               // arena.addEntity(decorFactory.wallBag(Measure.units(40f), Measure.units(45f), Measure.units(10f), Measure.units(5f), arenaSkin));


                return arena;
            }
        };
    }




    public ArenaGen room20CenterFixedTurret() {

        return new ArenaGen() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {
                Arena arena = new Arena(arenaSkin, defaultCoords);

                boolean isMirrored = random.nextBoolean();

                arena.roomType = Arena.RoomType.TRAP;

                //TODO sort of like a square. Has a wall that in combat that spawns and moves upwards pushing the player upwards
                //TODO two blob spawners are one either side kind of like this

                //TODO leave it for level 2/3 or something or the weekend when I can created the technology

                // ----------
                // S    u    s
                // ----------


                arena.setWidth(ArenaShellFactory.SECTION_WIDTH);
                arena.setHeight(ArenaShellFactory.SECTION_HEIGHT);

                arena =  new ArenaBuilder(assetManager, arenaSkin)
                        .addSection(new ArenaBuilder.Section(defaultCoords,
                                ArenaBuilder.wall.DOOR,
                                ArenaBuilder.wall.DOOR,
                                ArenaBuilder.wall.GRAPPLE,
                                ArenaBuilder.wall.DOOR))
                        .buildArena(arena);

                //TODO good place to try offsets you can technically fall into this room

                float spawnPosX = isMirrored ? Measure.units(10f) : arena.getWidth() - Measure.units(10f);
                float turretPosX = isMirrored ? arena.getWidth() - Measure.units(10f) : Measure.units(10f);


                arena.addEntity(arenaEnemyPlacementFactory.spawnFixedSentry(arena.getWidth() / 2, arena.getHeight() / 2));


                // arena.addEntity(decorFactory.wallBag(Measure.units(40f), Measure.units(45f), Measure.units(10f), Measure.units(5f), arenaSkin));


                return arena;
            }
        };
    }



    public ArenaGen room21Width2TopBottomSeperation() {

        return new ArenaGen() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {
                Arena arena = new Arena(arenaSkin, defaultCoords, new MapCoords(defaultCoords.getX() + 1, defaultCoords.getY()));

                arena.setWidth(ArenaShellFactory.SECTION_WIDTH * 2);
                arena.setHeight(ArenaShellFactory.SECTION_HEIGHT);

                arena =  new ArenaBuilder(assetManager, arenaSkin)
                        .addSection(new ArenaBuilder.Section(defaultCoords,
                                ArenaBuilder.wall.DOOR,
                                ArenaBuilder.wall.NONE,
                                ArenaBuilder.wall.DOOR,
                                ArenaBuilder.wall.DOOR))
                        .addSection(new ArenaBuilder.Section(new MapCoords(defaultCoords.getX() + 1, defaultCoords.getY()),
                                ArenaBuilder.wall.NONE,
                                ArenaBuilder.wall.DOOR,
                                ArenaBuilder.wall.DOOR,
                                ArenaBuilder.wall.DOOR))
                        .buildArena(arena);


                //arena.addEntity(arenaEnemyPlacementFactory.spawnFixedSentry(arena.getWidth() / 2, arena.getHeight() / 2));

                //LEFT
                arena.addEntity(decorFactory.wallBag(0, Measure.units(30f), Measure.units(50f), Measure.units(5f),arenaSkin));
                arena.addEntity(decorFactory.platform(Measure.units(50f), Measure.units(30f), Measure.units(30f)));

                //RIGHT
                arena.addEntity(decorFactory.wallBag(arena.getWidth() - Measure.units(50f), Measure.units(30f), Measure.units(50f), Measure.units(5f),arenaSkin));
                arena.addEntity(decorFactory.platform(arena.getWidth() - Measure.units(80f), Measure.units(30f), Measure.units(30f)));



                arena.addEntity(decorFactory.wallBag(Measure.units(80f), Measure.units(30f), Measure.units(40f),Measure.units(5), arenaSkin));


                arena.addEntity(arenaEnemyPlacementFactory.silverHeadFactory.silverHead(arena.getWidth() / 2, Measure.units(40f)));
                arena.addEntity(arenaEnemyPlacementFactory.spawnBlob(Measure.units(15f), Measure.units(20f)));
                arena.addEntity(arenaEnemyPlacementFactory.spawnBlob(arena.getWidth() - Measure.units(15f), Measure.units(20f)));


                //TODO split of top and bottom blobs at the bottom silver in the middle turret get spawned in at top (Prox trigger needs to be looked at)
                // arena.addEntity(decorFactory.wallBag(Measure.units(40f), Measure.units(45f), Measure.units(10f), Measure.units(5f), arenaSkin));


                return arena;
            }
        };
    }




    public ArenaGen room22FourBlock3BouncersWidth2TopBottomSeperation() {

        return new ArenaGen() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {
                Arena arena = new Arena(arenaSkin, defaultCoords);

                arena.setWidth(ArenaShellFactory.SECTION_WIDTH);
                arena.setHeight(ArenaShellFactory.SECTION_HEIGHT);

                arena.roomType = Arena.RoomType.TRAP;

                arena =  new ArenaBuilder(assetManager, arenaSkin)
                        .addSection(new ArenaBuilder.Section(defaultCoords,
                                ArenaBuilder.wall.DOOR,
                                ArenaBuilder.wall.DOOR,
                                ArenaBuilder.wall.GRAPPLE,
                                ArenaBuilder.wall.DOOR))
                        .buildArena(arena);

                //LEFT
                arena.addEntity(decorFactory.wallBag(Measure.units(20f), Measure.units(30f), Measure.units(5f), Measure.units(5f),arenaSkin));
                arena.addEntity(decorFactory.wallBag(arena.getWidth() - Measure.units(25f), Measure.units(30f), Measure.units(5f), Measure.units(5f),arenaSkin));


                arena.addEntity(arenaEnemyPlacementFactory.spawnBouncer(Measure.units(12.5f), arena.getHeight() / 2 + Measure.units(2.5f)));
                arena.addEntity(arenaEnemyPlacementFactory.spawnBouncer(arena.getWidth() / 2, arena.getHeight() / 2 + Measure.units(2.5f) ));
                arena.addEntity(arenaEnemyPlacementFactory.spawnBouncer(arena.getWidth() - Measure.units(12.5f), arena.getHeight() / 2 + Measure.units(2.5f)));
/*
                //RIGHT
                arena.addEntity(decorFactory.wallBag(arena.getWidth() - Measure.units(50f), Measure.units(30f), Measure.units(50f), Measure.units(5f),arenaSkin));
                arena.addEntity(decorFactory.platform(arena.getWidth() - Measure.units(80f), Measure.units(30f), Measure.units(30f)));



                arena.addEntity(decorFactory.wallBag(Measure.units(80f), Measure.units(30f), Measure.units(40f),Measure.units(5), arenaSkin));


                arena.addEntity(arenaEnemyPlacementFactory.silverHeadFactory.silverHead(arena.getWidth() / 2, Measure.units(40f)));
                arena.addEntity(arenaEnemyPlacementFactory.spawnBlob(Measure.units(15f), Measure.units(20f)));
                arena.addEntity(arenaEnemyPlacementFactory.spawnBlob(arena.getWidth() - Measure.units(15f), Measure.units(20f)));*/


                //TODO split of top and bottom blobs at the bottom silver in the middle turret get spawned in at top (Prox trigger needs to be looked at)
                // arena.addEntity(decorFactory.wallBag(Measure.units(40f), Measure.units(45f), Measure.units(10f), Measure.units(5f), arenaSkin));


                return arena;
            }
        };
    }


    public ArenaGen room23CenterSmallspawner() {

        return new ArenaGen() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {
                Arena arena = new Arena(arenaSkin, defaultCoords);

                arena.setWidth(ArenaShellFactory.SECTION_WIDTH);
                arena.setHeight(ArenaShellFactory.SECTION_HEIGHT);

                arena.roomType = Arena.RoomType.TRAP;

                arena =  new ArenaBuilder(assetManager, arenaSkin)
                        .addSection(new ArenaBuilder.Section(defaultCoords,
                                ArenaBuilder.wall.DOOR,
                                ArenaBuilder.wall.DOOR,
                                ArenaBuilder.wall.GRAPPLE,
                                ArenaBuilder.wall.DOOR))
                        .buildArena(arena);

                arena.addEntity(arenaEnemyPlacementFactory.spawnerFactory.spawnerBag(arena.getWidth() / 2, arena.getHeight() / 2,
                        new SpawnerFactory.Spawner() {
                            public Bag<Component> spawnBag(float x, float y) {
                                return arenaEnemyPlacementFactory.blobFactory.smallblobBag(x,y);
                            }
                        }, 3));


                return arena;
            }
        };
    }



    public ArenaGen room24TwoTreasureAndFixedTurret() {

        return new ArenaGen() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {

                Arena arena = arenaShellFactory.createOmniArenaHiddenGrapple(defaultCoords);

                arena.addEntity(decorFactory.wallBag(Measure.units(35f), Measure.units(35f), Measure.units(5f), Measure.units(20f), arenaSkin));
               // arena.addEntity(decorFactory.wallBag(Measure.units(35f), Measure.units(35f), Measure.units(5f), Measure.units(20f), arenaSkin));
                //arena.addEntity(decorFactory.wallBag(arena.getWidth() - Measure.units(40f), Measure.units(35f), Measure.units(5f), Measure.units(20f), arenaSkin));
                arena.addEntity(decorFactory.lockWall(Measure.units(5f), Measure.units(35f), Measure.units(30f), Measure.units(5)));
                arena.addEntity(chestFactory.chestBag(Measure.units(7.5f), Measure.units(40f)));
                arena.addEntity(chestFactory.chestBag(Measure.units(22.5f), Measure.units(40f)));
                arena.addEntity(turretFactory.fixedLockOnTurret(Measure.units(85f), Measure.units(45f)));


                return arena;
            }
        };
    }


    public ArenaGen room25BlobUpTop() {

        return new ArenaGen() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {

                Arena arena = arenaShellFactory.createSmallArenaNoGrapple(defaultCoords);
                arena.addEntity(decorFactory.wallBag(arena.getWidth() - Measure.units(60f), Measure.units(30f), Measure.units(60f), Measure.units(5f), arenaSkin));
                arena.addEntity(arenaEnemyPlacementFactory.spawnBlob(arena.getWidth() - Measure.units(15f), Measure.units(45f)));

                //TODO add turret?
                return arena;
            }
        };
    }


    public ArenaGen room26ThreeExitsOneTurretNoLock() {

        return new ArenaGen() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {

                Arena arena = new Arena(arenaSkin, defaultCoords);

                arena.setWidth(ArenaShellFactory.SECTION_WIDTH);
                arena.setHeight(ArenaShellFactory.SECTION_HEIGHT);

                arena.roomType = Arena.RoomType.TRAP;

                boolean isLeftDoor = random.nextBoolean();
                boolean isVerticalMandatory = random.nextBoolean();

                ArenaBuilder.wall w = isVerticalMandatory ? ArenaBuilder.wall.MANDATORYDOOR : ArenaBuilder.wall.DOOR;

                arena =  new ArenaBuilder(assetManager, arenaSkin)
                        .addSection(new ArenaBuilder.Section(defaultCoords,
                                isLeftDoor ? w : ArenaBuilder.wall.FULL,
                                isLeftDoor ? ArenaBuilder.wall.FULL :  w,
                                ArenaBuilder.wall.DOOR,
                                ArenaBuilder.wall.MANDATORYDOOR))
                        .buildArena(arena);
                //TODO add turret?

                float bigWallWidth = Measure.units(35f);
                float bigWallposX = isLeftDoor ? arena.getWidth() - bigWallWidth - Measure.units(5) : Measure.units(5f);
                float smallWallposX = isLeftDoor ? arena.getWidth() - bigWallWidth - Measure.units(20) : Measure.units(25f);

                float turretPox = isLeftDoor ? Measure.units(15f) : arena.getWidth() - Measure.units(15f);

                arena.addEntity(decorFactory.wallBag(bigWallposX, 0, bigWallWidth, arena.getHeight(), arenaSkin));
                arena.addEntity(decorFactory.wallBag(smallWallposX, Measure.units(25f), bigWallWidth, Measure.units(10f), arenaSkin));

                arena.addEntity(arenaEnemyPlacementFactory.spawnFixedSentry(turretPox, Measure.units(45f)));

                return arena;
            }
        };
    }

    public ArenaGen room27LargeBouncerGrappleRoom() {

        return new ArenaGen() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {
                Arena a = arenaShellFactory.createOmniArenaHiddenGrapple(defaultCoords);

                a.addEntity(arenaEnemyPlacementFactory.spawnLargeBouncer(a.getWidth() / 2, a.getHeight() / 2));


                a.addEntity(decorFactory.wallBag(a.getWidth() - Measure.units(25f), Measure.units(35f),
                        Measure.units(5f), Measure.units(5), arenaSkin));

                a.addEntity(decorFactory.wallBag(Measure.units(20f), Measure.units(35f),
                        Measure.units(5f), Measure.units(5), arenaSkin));

                //a.addEntity(arenaEnemyPlacementFactory.spawnMovingSentry(Measure.units(20), a.getHeight() - Measure.units(15)));
                a.roomType = Arena.RoomType.TRAP;
                return a;
            }
        };

    }



    public ArenaGen room28Height2GrappleUp() {

        return new ArenaGen() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {


                Arena arena = new Arena(arenaSkin, defaultCoords,
                        new MapCoords(defaultCoords.getX(), defaultCoords.getY() + 1));


                arena.setHeight(ArenaShellFactory.SECTION_HEIGHT * 2);
                arena.setWidth(ArenaShellFactory.SECTION_WIDTH);

                arena =  new ArenaBuilder(assetManager, arenaSkin)
                        .addSection(new ArenaBuilder.Section(defaultCoords,
                                ArenaBuilder.wall.DOOR,
                                ArenaBuilder.wall.DOOR,
                                ArenaBuilder.wall.NONE,
                                ArenaBuilder.wall.DOOR))
                        .addSection(new ArenaBuilder.Section(new MapCoords(defaultCoords.getX(), defaultCoords.getY() + 1),
                                ArenaBuilder.wall.FULL,
                                ArenaBuilder.wall.FULL,
                                ArenaBuilder.wall.MANDATORYGRAPPLE,
                                ArenaBuilder.wall.NONE))
                        .buildArena(arena);



           //     arena.addEntity(decorFactory.grapplePointBag(Measure.units(20f), Measure.units(30f)));
                arena.addEntity(decorFactory.grapplePointBag(arena.getWidth() / 4 * 3, Measure.units(45f)));
                arena.addEntity(decorFactory.grapplePointBag(arena.getWidth() / 4, Measure.units(75)));
            //    arena.addEntity(decorFactory.grapplePointBag(Measure.units(20f), Measure.units(70f)));



                return arena;
            }
        };

    }


    public ArenaGen room29BlobAndBouncer() {

        return new ArenaGen() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {


                Arena arena = new Arena(arenaSkin, defaultCoords);

                arena.setHeight(ArenaShellFactory.SECTION_HEIGHT);
                arena.setWidth(ArenaShellFactory.SECTION_WIDTH);

                arena.roomType = Arena.RoomType.TRAP;

                arena =  new ArenaBuilder(assetManager, arenaSkin)
                        .addSection(new ArenaBuilder.Section(defaultCoords,
                                ArenaBuilder.wall.DOOR,
                                ArenaBuilder.wall.DOOR,
                                ArenaBuilder.wall.DOOR,
                                ArenaBuilder.wall.DOOR))
                        .buildArena(arena);


                arena.addEntity(decorFactory.wallBag(Measure.units(35f), Measure.units(30f), Measure.units(30f), Measure.units(5f), arenaSkin));
                arena.addEntity(arenaEnemyPlacementFactory.spawnBouncer(arena.getWidth() / 2, Measure.units(45f)));
                arena.addEntity(arenaEnemyPlacementFactory.spawnBlob(arena.getWidth() / 2, Measure.units(20f)));


                return arena;
            }
        };

    }


















//TODO IDEA since proximitry trigger is going to be revamped for blob and others seperate a small room in two with a platform
    //TODO place blob ontop (Have it be an open room)



    @Override
    public Array<WeightedObject<ArenaGen>> getOmniRooms() {
        return null;
    }
}
