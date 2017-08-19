package com.byrjamin.wickedwizard.factories.arenas.levels;

import com.artemis.Component;
import com.artemis.Entity;
import com.artemis.World;
import com.artemis.utils.Bag;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.utils.Array;
import com.byrjamin.wickedwizard.ecs.components.ai.Action;
import com.byrjamin.wickedwizard.ecs.components.ai.ActionAfterTimeComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.ExpireComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.TextureRegionComponent;
import com.byrjamin.wickedwizard.ecs.systems.audio.MusicSystem;
import com.byrjamin.wickedwizard.ecs.systems.graphical.MessageBannerSystem;
import com.byrjamin.wickedwizard.factories.AbstractFactory;
import com.byrjamin.wickedwizard.factories.arenas.Arena;
import com.byrjamin.wickedwizard.factories.arenas.ArenaBuilder;
import com.byrjamin.wickedwizard.factories.arenas.ArenaCreate;
import com.byrjamin.wickedwizard.factories.arenas.decor.ArenaEnemyPlacementFactory;
import com.byrjamin.wickedwizard.factories.arenas.decor.ArenaShellFactory;
import com.byrjamin.wickedwizard.factories.arenas.decor.DecorFactory;
import com.byrjamin.wickedwizard.factories.arenas.decor.MessageFactory;
import com.byrjamin.wickedwizard.factories.arenas.skins.ArenaSkin;
import com.byrjamin.wickedwizard.factories.chests.ChestFactory;
import com.byrjamin.wickedwizard.factories.enemy.HoarderFactory;
import com.byrjamin.wickedwizard.factories.enemy.SpawnerFactory;
import com.byrjamin.wickedwizard.utils.ComponentBag;
import com.byrjamin.wickedwizard.utils.MapCoords;
import com.byrjamin.wickedwizard.utils.Measure;
import com.byrjamin.wickedwizard.utils.enums.Level;

import java.util.Random;

/**
 * Created by Home on 13/04/2017.
 */

public class Level1Rooms extends AbstractFactory implements ArenaRepostiory {

    private ArenaShellFactory arenaShellFactory;
    private ChestFactory chestFactory;
    private DecorFactory decorFactory;
    private ArenaEnemyPlacementFactory arenaEnemyPlacementFactory;

    private ArenaSkin arenaSkin;
    private Random random;

    public Level1Rooms(AssetManager assetManager, ArenaSkin arenaSkin, Random random) {
        super(assetManager);
        this.arenaShellFactory = new com.byrjamin.wickedwizard.factories.arenas.decor.ArenaShellFactory(assetManager, arenaSkin);
        this.chestFactory = new ChestFactory(assetManager);
        this.arenaEnemyPlacementFactory = new com.byrjamin.wickedwizard.factories.arenas.decor.ArenaEnemyPlacementFactory(assetManager, arenaSkin, random);
        this.decorFactory = new com.byrjamin.wickedwizard.factories.arenas.decor.DecorFactory(assetManager, arenaSkin);
        this.random = random;
        this.arenaSkin = arenaSkin;
    }

    @Override
    public Array<ArenaCreate> getAllArenas() {
        return  getLevel1RoomArray();
    }

    public Array<ArenaCreate> getLevel1RoomArray(){
        Array<ArenaCreate> ag = new Array<ArenaCreate>();
        ag.insert(0, room1blobLeft());
        ag.insert(1, room2blobRight());
        ag.insert(2, room3blobRightAndLeft());
        ag.insert(3, room4blobRightNoBlock());
        ag.insert(4, room5SquareMovingSentry());
        ag.insert(5, room6SquareTwoSentries());
        ag.insert(6, room7Silverhead());
        ag.insert(7, room8TwoLockedChest()); //Touched up
        ag.insert(8, room9deadEndW2()); //T
        ag.insert(9, room10Height2());
        ag.insert(10, room11SquareCenterTwoBouncers());
        ag.insert(11, room12Square2bouncersAndTwoTinyblocks());
        ag.insert(12, room13LargeBouncer());
        ag.insert(13, room14SilverheadCenter()); //T
        ag.insert(14, room15FourBouncersSpawnedTwoWalls());
        ag.insert(15, room16TreasureTwoturretWallsOneChest());
        ag.insert(16, room17verticalTwoFixedTurrets());
        ag.insert(17, room18TrapTwoBouncerOneTurret());
        ag.insert(18, room19ThroughNarrowLasers());
        ag.insert(19, room20CenterFixedTurret());
        ag.insert(20, room21Width2TopBottomSeperation());
        ag.insert(21, room22TwoBlock3Bouncers());
        ag.insert(22, room23CenterSmallBlobSpawner());
        ag.insert(23, room24TwoTreasureAndFixedTurret());
        ag.insert(24, room25BlobUpTop());
        ag.insert(25, room26Width2And2Sentries());
        ag.insert(26, room27ThreeExitsOneTurretNoLock());
        ag.insert(27, room28LargeBouncerGrappleRoom());
        ag.insert(28, room29Height2GrappleUp());
        ag.insert(29, room30BlobAndBouncer());
        return ag;
    }



    public ArenaCreate startingArena(){
        return new ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {
                Arena arena = arenaShellFactory.createOmniArenaSquareCenter(defaultCoords);
                arena.addEntity(new MessageFactory().nextLevelMessageBagAndMusic(Level.ONE));
                return arena;
            }
        };
    }



    public ArenaCreate room1blobLeft(){
        return new ArenaCreate () {
            @Override
            public Arena createArena(MapCoords defaultCoords) {
                Arena a = arenaShellFactory.createOmniArenaSquareCenter(defaultCoords);
                a.addEntity(arenaEnemyPlacementFactory.spawnBlob(a.getWidth() / 4, a.getHeight() / 2));
                a.roomType = Arena.RoomType.TRAP;

/*                a.addEntity(chestFactory.chestBag(Measure.units(15f), Measure.units(10f)));
                a.addEntity(chestFactory.chestBag(Measure.units(25f), Measure.units(10f)));
                a.addEntity(chestFactory.chestBag(Measure.units(35f), Measure.units(10f)));
                a.addEntity(chestFactory.chestBag(Measure.units(45f), Measure.units(10f)));
                a.addEntity(chestFactory.chestBag(Measure.units(55f), Measure.units(10f)));
                a.addEntity(chestFactory.chestBag(Measure.units(65f), Measure.units(10f)));
                a.addEntity(chestFactory.chestBag(Measure.units(75f), Measure.units(10f)));
                a.addEntity(chestFactory.chestBag(Measure.units(85f), Measure.units(10f)));
                a.addEntity(chestFactory.chestBag(Measure.units(95f), Measure.units(10f)));*/

                return a;
            }



        };
    }

    public ArenaCreate room2blobRight(){
        return new ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {
                Arena a = arenaShellFactory.createOmniArenaSquareCenter(defaultCoords);
                a.addEntity(arenaEnemyPlacementFactory.spawnBlob(a.getWidth() / 4 * 3, a.getHeight() / 2));
                a.roomType = Arena.RoomType.TRAP;
                return a;
            }
        };
    }

    public ArenaCreate room4blobRightNoBlock(){
        return new ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {
                Arena a = arenaShellFactory.createOmniArenaHiddenGrapple(defaultCoords);
                a.addEntity(arenaEnemyPlacementFactory.spawnBlob(a.getWidth() / 4 * 3, a.getHeight() / 2));
                a.roomType = Arena.RoomType.TRAP;
                return a;
            }
        };
    }

    public ArenaCreate room3blobRightAndLeft(){
        return new ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {
                Arena a = arenaShellFactory.createOmniArenaSquareCenter(defaultCoords);

                a.addWave(arenaEnemyPlacementFactory.spawnBlob(a.getWidth() / 4, a.getHeight() / 2));
                a.addWave(arenaEnemyPlacementFactory.spawnBlob(a.getWidth() / 4 * 3, a.getHeight() / 2));

                a.roomType = Arena.RoomType.TRAP;
                return a;
            }
        };
    }

    public ArenaCreate room5SquareMovingSentry(){
        return new ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {
                Arena a = arenaShellFactory.createOmniArenaSquareCenter(defaultCoords);
                a.addEntity(arenaEnemyPlacementFactory.spawnMovingSentry(a.getWidth() - Measure.units(20), a.getHeight() - Measure.units(15)));
                a.roomType = Arena.RoomType.TRAP;
                return a;
            }
        };
    }

    public ArenaCreate room6SquareTwoSentries(){
        return new ArenaCreate() {
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


    public ArenaCreate room7Silverhead(){
        return new ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {
                Arena a = arenaShellFactory.createOmniArenaSquareCenter(defaultCoords);
                a.addEntity(arenaEnemyPlacementFactory.spawnSilverHead(a.getWidth() / 2, Measure.units(40f)));
                a.roomType = Arena.RoomType.TRAP;
                return a;
            }
        };
    }


    public ArenaCreate room8TwoLockedChest(){
        return new ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {

                boolean chestsAreLeft = random.nextBoolean();

                Arena a = arenaShellFactory.createEitherNoLeftOrNoRightArena(defaultCoords, chestsAreLeft);

                float chestPosX = chestsAreLeft ? Measure.units(6.5f) : a.getWidth() -  Measure.units(16.5f);
                float chest2PosX = chestsAreLeft ? Measure.units(19f) : a.getWidth() - Measure.units(29f);

                float hoarderPosX = chestsAreLeft ? Measure.units(17.5f) : a.getWidth() -  Measure.units(17.5f);
                a.addEntity(chestFactory.chestBag(chestPosX, Measure.units(10f)));
                a.addEntity(chestFactory.chestBag(chest2PosX, Measure.units(10f)));

                a.addEntity(arenaEnemyPlacementFactory.hoarderFactory.hoarder(hoarderPosX, Measure.units(27.5f)));

              //  a.addEntity(decorFactory.destructibleWall(lockPosX, Measure.units(10f), Measure.units(5f), Measure.units(10f)));

              //  a.addEntity(decorFactory.wallBag(wallPosX, Measure.units(20f), Measure.units(30f), Measure.units(10f), arenaSkin.getWallTint()));

                //RoomDecorationFactory.spawnBlob(a);
                return a;
            }
        };
    }


    public ArenaCreate room9deadEndW2(){

        return new ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {

                boolean mirror = random.nextBoolean();

                Arena a = arenaShellFactory.createWidth2DeadEndArena(defaultCoords, mirror);

                float posX = mirror ? Measure.units(5) : a.getWidth() - Measure.units(10f);
                float lowertTurretPosX = mirror ? Measure.units(50) : a.getWidth() - Measure.units(55);
                float chestPosX = mirror ? Measure.units(17.5f) : a.getWidth() - Measure.units(27.5f);
                float blockerPosX = mirror ? a.getWidth() - Measure.units(80f) : Measure.units(80f);
                float lowertTurretWallPosX = mirror ? a.getWidth() - Measure.units(160f) : Measure.units(150f);

                float angle =  mirror ? -90 : 90;

                a.addEntity(chestFactory.chestBag(chestPosX, Measure.units(10f)));

                a.addEntity(decorFactory.wallBag(blockerPosX, Measure.units(45f), Measure.units(10f), Measure.units(10f), arenaSkin));
                a.addEntity(decorFactory.wallBag(blockerPosX, Measure.units(10f), Measure.units(10f), Measure.units(10f), arenaSkin));


                a.addEntity(decorFactory.wallBag(lowertTurretWallPosX, Measure.units(45f), Measure.units(10f), Measure.units(10f), arenaSkin));
                a.addEntity(decorFactory.wallBag(lowertTurretWallPosX, Measure.units(10f), Measure.units(10f), Measure.units(10f), arenaSkin));



                a.addEntity(decorFactory.fixedWallTurret(lowertTurretPosX, Measure.units(47.5f),  angle, 3.0f, 0f));
                a.addEntity(decorFactory.fixedWallTurret(posX, Measure.units(40f),  angle, 3.0f, 1.0f));
                a.addEntity(decorFactory.fixedWallTurret(posX, Measure.units(30f),  angle, 3.0f, 1.0f));
                a.addEntity(decorFactory.fixedWallTurret(posX, Measure.units(20f),  angle, 3.0f, 1.0f));
                a.addEntity(decorFactory.fixedWallTurret(lowertTurretPosX, Measure.units(12.5f),  angle, 3.0f, 0f));


                return a;
            }
        };


    }


    public ArenaCreate room10Height2(){

        return new ArenaCreate() {
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


    public ArenaCreate room11SquareCenterTwoBouncers(){

        return new ArenaCreate() {
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



    public ArenaCreate room12Square2bouncersAndTwoTinyblocks(){

        return new ArenaCreate() {
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


    public ArenaCreate room13LargeBouncer(){

        return new ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {
                Arena a = arenaShellFactory.createOmniArenaSquareCenter(defaultCoords);
                a.addEntity(arenaEnemyPlacementFactory.spawnLargeBouncer(a.getWidth() / 2,(a.getHeight() - Measure.units(15f))));
                a.roomType = Arena.RoomType.TRAP;
                return a;
            }
        };
    }


    public ArenaCreate room14SilverheadCenter() {

        return new ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {

                Arena arena = new Arena(arenaSkin, defaultCoords);


                arena.setWidth(com.byrjamin.wickedwizard.factories.arenas.decor.ArenaShellFactory.SECTION_WIDTH);
                arena.setHeight(com.byrjamin.wickedwizard.factories.arenas.decor.ArenaShellFactory.SECTION_HEIGHT);

                arena = new ArenaBuilder(assetManager, arenaSkin)
                        .addSection(new ArenaBuilder.Section(defaultCoords,
                                ArenaBuilder.wall.MANDATORYDOOR,
                                ArenaBuilder.wall.MANDATORYDOOR,
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


    public ArenaCreate room15FourBouncersSpawnedTwoWalls() {

        return new ArenaCreate() {
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


    public ArenaCreate room16TreasureTwoturretWallsOneChest() {

        return new ArenaCreate() {
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

                if(random.nextBoolean()) {
                    arena.addEntity(decorFactory.wallBag(0, Measure.units(30f), Measure.units(45f), arena.getHeight(), arenaSkin));
                    arena.addEntity(decorFactory.wallBag(arena.getWidth() - Measure.units(45f), Measure.units(30f),
                            Measure.units(45f), Measure.units(15f), arenaSkin));

                    arena.addEntity(chestFactory.chestBag(arena.getWidth() - Measure.units(35f),
                            Measure.units(45)));

                } else {
                    arena.addEntity(decorFactory.wallBag(0, Measure.units(30f), arena.getWidth(), arena.getHeight(), arenaSkin));
                }

                arena.addEntity(decorFactory.fixedWallTurret(Measure.units(25), Measure.units(25f),  180, 3.0f, 1.5f));
                arena.addEntity(decorFactory.fixedWallTurret(Measure.units(30), Measure.units(25f),  180, 3.0f, 1.5f));
                arena.addEntity(decorFactory.fixedWallTurret(Measure.units(35), Measure.units(25f),  180, 3.0f, 1.5f));
                arena.addEntity(decorFactory.fixedWallTurret(Measure.units(40), Measure.units(25f),  180, 3.0f, 1.5f));

                arena.addEntity(decorFactory.fixedWallTurret(Measure.units(55), Measure.units(25f), 180, 3.0f, 1.5f));
                arena.addEntity(decorFactory.fixedWallTurret(Measure.units(60), Measure.units(25f),  180, 3.0f, 1.5f));
                arena.addEntity(decorFactory.fixedWallTurret(Measure.units(65), Measure.units(25f),  180, 3.0f, 1.5f));
                arena.addEntity(decorFactory.fixedWallTurret(Measure.units(70), Measure.units(25f),  180, 3.0f, 1.5f));

/*                arena.addEntity(turretFactory.fixedTurret(posX, Measure.units(41f),  angle, 3.0f, 1.5f));
                arena.addEntity(turretFactory.fixedTurret(posX, Measure.units(32f),  angle, 3.0f, 1.5f));
                arena.addEntity(turretFactory.fixedTurret(posX, Measure.units(23f),  angle, 3.0f, 1.5f));
                arena.addEntity(turretFactory.fixedTurret(posX, Measure.units(14f),  angle, 3.0f, 0f));*/

                return arena;
            }
        };
    }





    public ArenaCreate room18TrapTwoBouncerOneTurret() {

        return new ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {

                Arena arena = new Arena(arenaSkin, defaultCoords);

                arena =  new ArenaBuilder(assetManager, arenaSkin)
                        .addSection(new ArenaBuilder.Section(defaultCoords,
                                ArenaBuilder.wall.DOOR,
                                ArenaBuilder.wall.DOOR,
                                ArenaBuilder.wall.GRAPPLE,
                                ArenaBuilder.wall.FULL))
                        .buildArena(arena);



                arena.addWave(arenaEnemyPlacementFactory.spawnBouncer(Measure.units(15), Measure.units(50f)),
                        arenaEnemyPlacementFactory.spawnBouncer(arena.getWidth() - Measure.units(15), Measure.units(50f)),
                        arenaEnemyPlacementFactory.spawnFixedSentry(arena.getWidth() / 2, arena.getHeight() / 2));

                ComponentBag bag = new ChestFactory(assetManager).chestBag(Measure.units(45f), Measure.units(10f), chestFactory.trapODAC());
                arena.addEntity(bag);

                return arena;
            }
        };
    }



    public ArenaCreate room17verticalTwoFixedTurrets() {

        return new ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {
                Arena arena = new Arena(arenaSkin, defaultCoords);


                arena.roomType = Arena.RoomType.TRAP;

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

    public ArenaCreate room19ThroughNarrowLasers() {

        return new ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {
                Arena arena = new Arena(arenaSkin, defaultCoords);

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




    public ArenaCreate room20CenterFixedTurret() {

        return new ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {
                Arena arena = new Arena(arenaSkin, defaultCoords);

                arena.roomType = Arena.RoomType.TRAP;

                arena =  new ArenaBuilder(assetManager, arenaSkin)
                        .addSection(new ArenaBuilder.Section(defaultCoords,
                                ArenaBuilder.wall.DOOR,
                                ArenaBuilder.wall.DOOR,
                                ArenaBuilder.wall.GRAPPLE,
                                ArenaBuilder.wall.DOOR))
                        .buildArena(arena);

                arena.addEntity(arenaEnemyPlacementFactory.spawnFixedSentry(arena.getWidth() / 2, arena.getHeight() / 2));

                return arena;
            }
        };
    }



    public ArenaCreate room21Width2TopBottomSeperation() {

        return new ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {
                Arena arena = new Arena(arenaSkin, defaultCoords, new MapCoords(defaultCoords.getX() + 1, defaultCoords.getY()));

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




    public ArenaCreate room22TwoBlock3Bouncers() {

        return new ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {
                Arena arena = new Arena(arenaSkin, defaultCoords);

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

                return arena;
            }
        };
    }


    public ArenaCreate room23CenterSmallBlobSpawner() {

        return new ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {
                Arena arena = new Arena(arenaSkin, defaultCoords);

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
                                return arenaEnemyPlacementFactory.blobFactory.smallblobBag(x,y, random.nextBoolean());
                            }
                        }, 3));


                return arena;
            }
        };
    }



    public ArenaCreate room24TwoTreasureAndFixedTurret() {

        return new ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {

                Arena arena = arenaShellFactory.createOmniArenaHiddenGrapple(defaultCoords);

                boolean chestsAreLeft = random.nextBoolean();

                arena.addEntity(arenaEnemyPlacementFactory.hoarderFactory.hoarder(
                        chestsAreLeft ? Measure.units(20f) : arena.getWidth() - Measure.units(20f),
                        Measure.units(47.5f)));


                arena.addEntity(decorFactory.wallBag(chestsAreLeft ? Measure.units(5f) : arena.getWidth() - Measure.units(35f), Measure.units(30f), Measure.units(30f), Measure.units(5)));
                arena.addEntity(chestFactory.chestBag(chestsAreLeft ? Measure.units(7.5f) : arena.getWidth() - Measure.units(17.5f), Measure.units(35f)));
                arena.addEntity(chestFactory.chestBag(chestsAreLeft ? Measure.units(22.5f) : arena.getWidth() - Measure.units(32.5f), Measure.units(35f)));
                arena.addEntity(arenaEnemyPlacementFactory.turretFactory.fixedLockOnTurret(chestsAreLeft ? Measure.units(85f) : arena.getWidth() - Measure.units(85f), Measure.units(45f)));


                return arena;
            }
        };
    }


    public ArenaCreate room25BlobUpTop() {

        return new ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {

                Arena arena = arenaShellFactory.createSmallArenaNoGrapple(defaultCoords);

                boolean blobIsLeft = random.nextBoolean();

                arena.addEntity(decorFactory.wallBag(blobIsLeft ? Measure.units(5f) : arena.getWidth() - Measure.units(60f), Measure.units(30f), Measure.units(60f), Measure.units(5f), arenaSkin));
                arena.addEntity(arenaEnemyPlacementFactory.spawnBlob(blobIsLeft ? Measure.units(15f) : arena.getWidth() - Measure.units(15f), Measure.units(45f)));

                //TODO add turret?
                return arena;
            }
        };
    }

    public ArenaCreate room26Width2And2Sentries(){

        return new ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {
                Arena arena = new Arena(arenaSkin, defaultCoords,
                        new MapCoords(defaultCoords.getX() + 1, defaultCoords.getY()));


                arena =  new ArenaBuilder(assetManager, arenaSkin)
                        .addSection(new ArenaBuilder.Section(defaultCoords,
                                ArenaBuilder.wall.DOOR,
                                ArenaBuilder.wall.NONE,
                                random.nextBoolean() ? ArenaBuilder.wall.FULL : ArenaBuilder.wall.GRAPPLE,
                                ArenaBuilder.wall.DOOR))
                        .addSection(new ArenaBuilder.Section(new MapCoords(defaultCoords.getX() + 1, defaultCoords.getY()),
                                ArenaBuilder.wall.NONE,
                                ArenaBuilder.wall.DOOR,
                                random.nextBoolean() ? ArenaBuilder.wall.FULL : ArenaBuilder.wall.GRAPPLE,
                                ArenaBuilder.wall.DOOR)).buildArena(arena);


                arena.addEntity(arenaEnemyPlacementFactory.spawnMovingSentry(arena.getWidth() - Measure.units(20), arena.getHeight() - Measure.units(15)));
                arena.addEntity(arenaEnemyPlacementFactory.spawnMovingSentry(Measure.units(20), arena.getHeight() - Measure.units(15)));
                arena.roomType = Arena.RoomType.TRAP;
                return arena;
            }
        };
    }


    public ArenaCreate room27ThreeExitsOneTurretNoLock() {

        return new ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {

                Arena arena = new Arena(arenaSkin, defaultCoords);

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

    public ArenaCreate room28LargeBouncerGrappleRoom() {

        return new ArenaCreate() {
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



    public ArenaCreate room29Height2GrappleUp() {

        return new ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {


                Arena arena = new Arena(arenaSkin, defaultCoords,
                        new MapCoords(defaultCoords.getX(), defaultCoords.getY() + 1));

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


    public ArenaCreate room30BlobAndBouncer() {

        return new ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {


                Arena arena = new Arena(arenaSkin, defaultCoords);

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

}
