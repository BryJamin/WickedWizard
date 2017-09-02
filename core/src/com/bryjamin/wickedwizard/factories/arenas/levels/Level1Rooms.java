package com.bryjamin.wickedwizard.factories.arenas.levels;

import com.artemis.Component;
import com.artemis.utils.Bag;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.utils.Array;
import com.bryjamin.wickedwizard.factories.AbstractFactory;
import com.bryjamin.wickedwizard.factories.arenas.Arena;
import com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder;
import com.bryjamin.wickedwizard.factories.arenas.decor.ArenaEnemyPlacementFactory;
import com.bryjamin.wickedwizard.factories.arenas.skins.ArenaSkin;
import com.bryjamin.wickedwizard.factories.chests.ChestFactory;
import com.bryjamin.wickedwizard.factories.enemy.SpawnerFactory;
import com.bryjamin.wickedwizard.utils.Measure;

import java.util.Random;

/**
 * Created by Home on 13/04/2017.
 */

public class Level1Rooms extends AbstractFactory implements ArenaRepostiory {

    private com.bryjamin.wickedwizard.factories.arenas.decor.ArenaShellFactory arenaShellFactory;
    private ChestFactory chestFactory;
    private com.bryjamin.wickedwizard.factories.arenas.decor.DecorFactory decorFactory;
    private ArenaEnemyPlacementFactory arenaEnemyPlacementFactory;

    private ArenaSkin arenaSkin;
    private Random random;

    public Level1Rooms(AssetManager assetManager, ArenaSkin arenaSkin, Random random) {
        super(assetManager);
        this.arenaShellFactory = new com.bryjamin.wickedwizard.factories.arenas.decor.ArenaShellFactory(assetManager, arenaSkin);
        this.chestFactory = new ChestFactory(assetManager);
        this.arenaEnemyPlacementFactory = new ArenaEnemyPlacementFactory(assetManager, arenaSkin, random);
        this.decorFactory = new com.bryjamin.wickedwizard.factories.arenas.decor.DecorFactory(assetManager, arenaSkin);
        this.random = random;
        this.arenaSkin = arenaSkin;
    }

    @Override
    public Array<com.bryjamin.wickedwizard.factories.arenas.ArenaCreate> getAllArenas() {
        return  getLevel1RoomArray();
    }

    public Array<com.bryjamin.wickedwizard.factories.arenas.ArenaCreate> getLevel1RoomArray(){
        Array<com.bryjamin.wickedwizard.factories.arenas.ArenaCreate> ag = new Array<com.bryjamin.wickedwizard.factories.arenas.ArenaCreate>();
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


    public com.bryjamin.wickedwizard.factories.arenas.ArenaCreate room1blobLeft(){
        return new com.bryjamin.wickedwizard.factories.arenas.ArenaCreate() {
            @Override
            public Arena createArena(com.bryjamin.wickedwizard.utils.MapCoords defaultCoords) {
                Arena a = arenaShellFactory.createOmniArenaSquareCenter(defaultCoords, Arena.ArenaType.TRAP);
                a.addEntity(arenaEnemyPlacementFactory.spawnBlob(a.getWidth() / 4, a.getHeight() / 2));

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

    public com.bryjamin.wickedwizard.factories.arenas.ArenaCreate room2blobRight(){
        return new com.bryjamin.wickedwizard.factories.arenas.ArenaCreate() {
            @Override
            public Arena createArena(com.bryjamin.wickedwizard.utils.MapCoords defaultCoords) {
                Arena a = arenaShellFactory.createOmniArenaSquareCenter(defaultCoords, Arena.ArenaType.TRAP);
                a.addEntity(arenaEnemyPlacementFactory.spawnBlob(a.getWidth() / 4 * 3, a.getHeight() / 2));
                return a;
            }
        };
    }

    public com.bryjamin.wickedwizard.factories.arenas.ArenaCreate room4blobRightNoBlock(){
        return new com.bryjamin.wickedwizard.factories.arenas.ArenaCreate() {
            @Override
            public Arena createArena(com.bryjamin.wickedwizard.utils.MapCoords defaultCoords) {
                Arena a = arenaShellFactory.createOmniArenaHiddenGrapple(defaultCoords, Arena.ArenaType.TRAP);
                a.addEntity(arenaEnemyPlacementFactory.spawnBlob(a.getWidth() / 4 * 3, a.getHeight() / 2));
                return a;
            }
        };
    }

    public com.bryjamin.wickedwizard.factories.arenas.ArenaCreate room3blobRightAndLeft(){
        return new com.bryjamin.wickedwizard.factories.arenas.ArenaCreate() {
            @Override
            public Arena createArena(com.bryjamin.wickedwizard.utils.MapCoords defaultCoords) {
                Arena a = arenaShellFactory.createOmniArenaSquareCenter(defaultCoords, Arena.ArenaType.TRAP);

                a.addWave(arenaEnemyPlacementFactory.spawnBlob(a.getWidth() / 4, a.getHeight() / 2));
                a.addWave(arenaEnemyPlacementFactory.spawnBlob(a.getWidth() / 4 * 3, a.getHeight() / 2));
                return a;
            }
        };
    }

    public com.bryjamin.wickedwizard.factories.arenas.ArenaCreate room5SquareMovingSentry(){
        return new com.bryjamin.wickedwizard.factories.arenas.ArenaCreate() {
            @Override
            public Arena createArena(com.bryjamin.wickedwizard.utils.MapCoords defaultCoords) {
                Arena a = arenaShellFactory.createOmniArenaSquareCenter(defaultCoords, Arena.ArenaType.TRAP);
                a.addEntity(arenaEnemyPlacementFactory.spawnMovingSentry(a.getWidth() - Measure.units(20), a.getHeight() - Measure.units(15)));
                return a;
            }
        };
    }

    public com.bryjamin.wickedwizard.factories.arenas.ArenaCreate room6SquareTwoSentries(){
        return new com.bryjamin.wickedwizard.factories.arenas.ArenaCreate() {
            @Override
            public Arena createArena(com.bryjamin.wickedwizard.utils.MapCoords defaultCoords) {
                Arena a = arenaShellFactory.createOmniArenaSquareCenter(defaultCoords, Arena.ArenaType.TRAP);

                a.addEntity(arenaEnemyPlacementFactory.spawnMovingSentry(a.getWidth() - Measure.units(20), a.getHeight() - Measure.units(15)));
                a.addEntity(arenaEnemyPlacementFactory.spawnMovingSentry(Measure.units(20), a.getHeight() - Measure.units(15)));
                return a;
            }
        };
    }


    public com.bryjamin.wickedwizard.factories.arenas.ArenaCreate room7Silverhead(){
        return new com.bryjamin.wickedwizard.factories.arenas.ArenaCreate() {
            @Override
            public Arena createArena(com.bryjamin.wickedwizard.utils.MapCoords defaultCoords) {
                Arena a = arenaShellFactory.createOmniArenaSquareCenter(defaultCoords, Arena.ArenaType.TRAP);
                a.addEntity(arenaEnemyPlacementFactory.spawnSilverHead(a.getWidth() / 2, Measure.units(40f)));
                return a;
            }
        };
    }


    public com.bryjamin.wickedwizard.factories.arenas.ArenaCreate room8TwoLockedChest(){
        return new com.bryjamin.wickedwizard.factories.arenas.ArenaCreate() {
            @Override
            public Arena createArena(com.bryjamin.wickedwizard.utils.MapCoords defaultCoords) {

                boolean chestsAreLeft = random.nextBoolean();

                Arena a = arenaShellFactory.createEitherNoLeftOrNoRightArena(defaultCoords, Arena.ArenaType.NORMAL, chestsAreLeft);

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


    public com.bryjamin.wickedwizard.factories.arenas.ArenaCreate room9deadEndW2(){

        return new com.bryjamin.wickedwizard.factories.arenas.ArenaCreate() {
            @Override
            public Arena createArena(com.bryjamin.wickedwizard.utils.MapCoords defaultCoords) {

                boolean mirror = random.nextBoolean();

                Arena a = arenaShellFactory.createWidth2DeadEndArena(defaultCoords, mirror, Arena.ArenaType.NORMAL);

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


    public com.bryjamin.wickedwizard.factories.arenas.ArenaCreate room10Height2(){

        return new com.bryjamin.wickedwizard.factories.arenas.ArenaCreate() {
            @Override
            public Arena createArena(com.bryjamin.wickedwizard.utils.MapCoords defaultCoords) {

                //TODO make two doors mandatory

                boolean isLeftBottomTopRight = random.nextBoolean();

                Arena arena =  new ArenaBuilder(assetManager, arenaSkin, Arena.ArenaType.NORMAL)
                        .addSection(new ArenaBuilder.Section(defaultCoords,
                                isLeftBottomTopRight ? ArenaBuilder.wall.MANDATORYDOOR : ArenaBuilder.wall.DOOR,
                                isLeftBottomTopRight ? ArenaBuilder.wall.DOOR : ArenaBuilder.wall.MANDATORYDOOR,
                                ArenaBuilder.wall.NONE,
                                ArenaBuilder.wall.FULL))
                        .addSection(new ArenaBuilder.Section(new com.bryjamin.wickedwizard.utils.MapCoords(defaultCoords.getX(), defaultCoords.getY() + 1),
                                isLeftBottomTopRight ? ArenaBuilder.wall.DOOR : ArenaBuilder.wall.MANDATORYDOOR,
                                isLeftBottomTopRight ? ArenaBuilder.wall.MANDATORYDOOR : ArenaBuilder.wall.DOOR,
                                ArenaBuilder.wall.FULL,
                                ArenaBuilder.wall.NONE)).buildArena();


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


    public com.bryjamin.wickedwizard.factories.arenas.ArenaCreate room11SquareCenterTwoBouncers(){

        return new com.bryjamin.wickedwizard.factories.arenas.ArenaCreate() {
            @Override
            public Arena createArena(com.bryjamin.wickedwizard.utils.MapCoords defaultCoords) {

                Arena a = arenaShellFactory.createOmniArenaSquareCenter(defaultCoords, Arena.ArenaType.TRAP);
                a.addEntity(arenaEnemyPlacementFactory.spawnBouncer(a.getWidth() / 4,(a.getHeight() - a.getHeight() / 4) + Measure.units(2.5f)));
                a.addEntity(arenaEnemyPlacementFactory.spawnBouncer(a.getWidth() - a.getWidth() / 4,(a.getHeight() - a.getHeight() / 4) + Measure.units(2.5f)));

                return a;
            }
        };
    }



    public com.bryjamin.wickedwizard.factories.arenas.ArenaCreate room12Square2bouncersAndTwoTinyblocks(){

        return new com.bryjamin.wickedwizard.factories.arenas.ArenaCreate() {
            @Override
            public Arena createArena(com.bryjamin.wickedwizard.utils.MapCoords defaultCoords) {
                Arena a = arenaShellFactory.createOmniArenaSquareCenter(defaultCoords, Arena.ArenaType.TRAP);


                a.addEntity(arenaEnemyPlacementFactory.spawnBouncer(a.getWidth() / 4,(a.getHeight() - a.getHeight() / 4) + Measure.units(2.5f)));
                a.addEntity(arenaEnemyPlacementFactory.spawnBouncer(a.getWidth() - a.getWidth() / 4,(a.getHeight() - a.getHeight() / 4) + Measure.units(2.5f)));

                a.addEntity(decorFactory.wallBag(a.getWidth() - Measure.units(25f), Measure.units(35f),
                        Measure.units(5f), Measure.units(5), arenaSkin));

                a.addEntity(decorFactory.wallBag(Measure.units(20f), Measure.units(35f),
                        Measure.units(5f), Measure.units(5), arenaSkin));


                return a;
            }
        };
    }


    public com.bryjamin.wickedwizard.factories.arenas.ArenaCreate room13LargeBouncer(){

        return new com.bryjamin.wickedwizard.factories.arenas.ArenaCreate() {
            @Override
            public Arena createArena(com.bryjamin.wickedwizard.utils.MapCoords defaultCoords) {
                Arena a = arenaShellFactory.createOmniArenaSquareCenter(defaultCoords, Arena.ArenaType.TRAP);
                a.addEntity(arenaEnemyPlacementFactory.spawnLargeBouncer(a.getWidth() / 2,(a.getHeight() - Measure.units(15f))));
                return a;
            }
        };
    }


    public com.bryjamin.wickedwizard.factories.arenas.ArenaCreate room14SilverheadCenter() {

        return new com.bryjamin.wickedwizard.factories.arenas.ArenaCreate() {
            @Override
            public Arena createArena(com.bryjamin.wickedwizard.utils.MapCoords defaultCoords) {

                Arena arena = new ArenaBuilder(assetManager, arenaSkin, Arena.ArenaType.TRAP)
                        .addSection(new ArenaBuilder.Section(defaultCoords,
                                ArenaBuilder.wall.MANDATORYDOOR,
                                ArenaBuilder.wall.MANDATORYDOOR,
                                ArenaBuilder.wall.FULL,
                                ArenaBuilder.wall.FULL))
                        .buildArena();

                arena.addEntity(decorFactory.wallBag(Measure.units(40f), Measure.units(10f), Measure.units(20f), Measure.units(25f), arenaSkin));
                arena.addEntity(decorFactory.platform(Measure.units(5), Measure.units(30f), Measure.units(35f)));
                arena.addEntity(decorFactory.platform(Measure.units(60f), Measure.units(30f), Measure.units(35f)));
                arena.addEntity(arenaEnemyPlacementFactory.spawnSilverHead(arena.getWidth() / 2, Measure.units(45f)));


                return arena;
            }
        };
    }


    public com.bryjamin.wickedwizard.factories.arenas.ArenaCreate room15FourBouncersSpawnedTwoWalls() {

        return new com.bryjamin.wickedwizard.factories.arenas.ArenaCreate() {
            @Override
            public Arena createArena(com.bryjamin.wickedwizard.utils.MapCoords defaultCoords) {

                Arena arena =  new ArenaBuilder(assetManager, arenaSkin, Arena.ArenaType.TRAP)
                        .addSection(new ArenaBuilder.Section(defaultCoords,
                                ArenaBuilder.wall.DOOR,
                                ArenaBuilder.wall.DOOR,
                                ArenaBuilder.wall.FULL,
                                ArenaBuilder.wall.FULL))
                        .buildArena();

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


    public com.bryjamin.wickedwizard.factories.arenas.ArenaCreate room16TreasureTwoturretWallsOneChest() {

        return new com.bryjamin.wickedwizard.factories.arenas.ArenaCreate() {
            @Override
            public Arena createArena(com.bryjamin.wickedwizard.utils.MapCoords defaultCoords) {
                Arena arena =  new ArenaBuilder(assetManager, arenaSkin, Arena.ArenaType.NORMAL)
                        .addSection(new ArenaBuilder.Section(defaultCoords,
                                ArenaBuilder.wall.MANDATORYDOOR,
                                ArenaBuilder.wall.MANDATORYDOOR,
                                ArenaBuilder.wall.FULL,
                                ArenaBuilder.wall.FULL))
                        .buildArena();

                if(random.nextBoolean()) {
                    arena.addEntity(decorFactory.wallBag(0, Measure.units(30f), Measure.units(45f), arena.getHeight(), arenaSkin));
                    arena.addEntity(decorFactory.wallBag(arena.getWidth() - Measure.units(45f), Measure.units(30f),
                            Measure.units(45f), Measure.units(15f), arenaSkin));

                    arena.addEntity(chestFactory.chestBag(arena.getWidth() - Measure.units(35f),
                            Measure.units(45)));

                } else {
                    arena.addEntity(decorFactory.wallBag(0, Measure.units(30f), arena.getWidth(), arena.getHeight(), arenaSkin));
                }

                arena.addEntity(decorFactory.fixedWallTurret(Measure.units(25), Measure.units(25f),  180, 3.0f, 1.0f));
                arena.addEntity(decorFactory.fixedWallTurret(Measure.units(30), Measure.units(25f),  180, 3.0f, 1.0f));
                arena.addEntity(decorFactory.fixedWallTurret(Measure.units(35), Measure.units(25f),  180, 3.0f, 1.0f));
                arena.addEntity(decorFactory.fixedWallTurret(Measure.units(40), Measure.units(25f),  180, 3.0f, 1.0f));

                arena.addEntity(decorFactory.fixedWallTurret(Measure.units(55), Measure.units(25f), 180, 3.0f, 1.0f));
                arena.addEntity(decorFactory.fixedWallTurret(Measure.units(60), Measure.units(25f),  180, 3.0f, 1.0f));
                arena.addEntity(decorFactory.fixedWallTurret(Measure.units(65), Measure.units(25f),  180, 3.0f, 1.0f));
                arena.addEntity(decorFactory.fixedWallTurret(Measure.units(70), Measure.units(25f),  180, 3.0f, 1.0f));

/*                arena.addEntity(turretFactory.fixedTurret(posX, Measure.units(41f),  angle, 3.0f, 1.5f));
                arena.addEntity(turretFactory.fixedTurret(posX, Measure.units(32f),  angle, 3.0f, 1.5f));
                arena.addEntity(turretFactory.fixedTurret(posX, Measure.units(23f),  angle, 3.0f, 1.5f));
                arena.addEntity(turretFactory.fixedTurret(posX, Measure.units(14f),  angle, 3.0f, 0f));*/

                return arena;
            }
        };
    }




    public com.bryjamin.wickedwizard.factories.arenas.ArenaCreate room17verticalTwoFixedTurrets() {

        return new com.bryjamin.wickedwizard.factories.arenas.ArenaCreate() {
            @Override
            public Arena createArena(com.bryjamin.wickedwizard.utils.MapCoords defaultCoords) {
                Arena arena =  new ArenaBuilder(assetManager, arenaSkin, Arena.ArenaType.TRAP)
                        .addSection(new ArenaBuilder.Section(defaultCoords,
                                ArenaBuilder.wall.FULL,
                                ArenaBuilder.wall.FULL,
                                ArenaBuilder.wall.GRAPPLE,
                                ArenaBuilder.wall.DOOR))
                        .buildArena();


                arena.addEntity(arenaEnemyPlacementFactory.turretFactory.fixedLockOnTurret(Measure.units(10f), Measure.units(15f)));
                arena.addEntity(arenaEnemyPlacementFactory.turretFactory.fixedLockOnTurret(arena.getWidth() - Measure.units(10f), Measure.units(15f)));

                arena.addEntity(decorFactory.wallBag(Measure.units(0), Measure.units(20f), Measure.units(40f), Measure.units(40f), arenaSkin));
                arena.addEntity(decorFactory.wallBag(arena.getWidth() - Measure.units(40f), Measure.units(20f), Measure.units(40f), Measure.units(40f), arenaSkin));

                return arena;
            }
        };
    }


    public com.bryjamin.wickedwizard.factories.arenas.ArenaCreate room18TrapTwoBouncerOneTurret() {

        return new com.bryjamin.wickedwizard.factories.arenas.ArenaCreate() {
            @Override
            public Arena createArena(com.bryjamin.wickedwizard.utils.MapCoords defaultCoords) {

                Arena arena =  new ArenaBuilder(assetManager, arenaSkin, Arena.ArenaType.NORMAL)
                        .addSection(new ArenaBuilder.Section(defaultCoords,
                                ArenaBuilder.wall.DOOR,
                                ArenaBuilder.wall.DOOR,
                                ArenaBuilder.wall.GRAPPLE,
                                ArenaBuilder.wall.FULL))
                        .buildArena();



                arena.addWave(arenaEnemyPlacementFactory.spawnBouncer(Measure.units(15), Measure.units(50f)),
                        arenaEnemyPlacementFactory.spawnBouncer(arena.getWidth() - Measure.units(15), Measure.units(50f)),
                        arenaEnemyPlacementFactory.spawnFixedSentry(arena.getWidth() / 2, arena.getHeight() / 2));

                com.bryjamin.wickedwizard.utils.ComponentBag bag = new ChestFactory(assetManager).chestBag(Measure.units(45f), Measure.units(10f), chestFactory.trapODAC());
                arena.addEntity(bag);

                return arena;
            }
        };
    }


    public com.bryjamin.wickedwizard.factories.arenas.ArenaCreate room19ThroughNarrowLasers() {

        return new com.bryjamin.wickedwizard.factories.arenas.ArenaCreate() {
            @Override
            public Arena createArena(com.bryjamin.wickedwizard.utils.MapCoords defaultCoords) {
                Arena arena =  new ArenaBuilder(assetManager, arenaSkin, Arena.ArenaType.NORMAL)
                        .addSection(new ArenaBuilder.Section(defaultCoords,
                                ArenaBuilder.wall.FULL,
                                ArenaBuilder.wall.FULL,
                                ArenaBuilder.wall.DOOR,
                                ArenaBuilder.wall.DOOR))
                        .buildArena();

                arena.mandatoryDoors.addAll(arena.doors);

                //TODO good place to try offsets you can technically fall into this room

                arena.addEntity(decorFactory.wallBag(Measure.units(0), Measure.units(0), Measure.units(40f), Measure.units(60f), arenaSkin));
                arena.addEntity(decorFactory.wallBag(arena.getWidth() - Measure.units(40f), Measure.units(0), Measure.units(40f), Measure.units(60f), arenaSkin));


                arena.addEntity(decorFactory.wallBag(Measure.units(40f), Measure.units(22.5f), Measure.units(10f), Measure.units(5f), arenaSkin));
                arena.addEntity(decorFactory.fixedWallTurret(arena.getWidth() - Measure.units(45f), Measure.units(22.5f),  90, 3.0f, 1.5f));

                arena.addEntity(decorFactory.wallBag(Measure.units(50f), Measure.units(35f), Measure.units(10f), Measure.units(5f), arenaSkin));
                arena.addEntity(decorFactory.fixedWallTurret(Measure.units(40f), Measure.units(35f),  -90, 3.0f, 1.5f));

                arena.addEntity(decorFactory.platform(Measure.units(40f), Measure.units(45f), Measure.units(20f)));

               // arena.addEntity(decorFactory.wallBag(Measure.units(40f), Measure.units(45f), Measure.units(10f), Measure.units(5f), arenaSkin));


                return arena;
            }
        };
    }




    public com.bryjamin.wickedwizard.factories.arenas.ArenaCreate room20CenterFixedTurret() {

        return new com.bryjamin.wickedwizard.factories.arenas.ArenaCreate() {
            @Override
            public Arena createArena(com.bryjamin.wickedwizard.utils.MapCoords defaultCoords) {
                Arena arena =  new ArenaBuilder(assetManager, arenaSkin, Arena.ArenaType.TRAP)
                        .addSection(new ArenaBuilder.Section(defaultCoords,
                                ArenaBuilder.wall.DOOR,
                                ArenaBuilder.wall.DOOR,
                                ArenaBuilder.wall.GRAPPLE,
                                ArenaBuilder.wall.DOOR))
                        .buildArena();

                arena.addEntity(arenaEnemyPlacementFactory.spawnFixedSentry(arena.getWidth() / 2, arena.getHeight() / 2));

                return arena;
            }
        };
    }



    public com.bryjamin.wickedwizard.factories.arenas.ArenaCreate room21Width2TopBottomSeperation() {

        return new com.bryjamin.wickedwizard.factories.arenas.ArenaCreate() {
            @Override
            public Arena createArena(com.bryjamin.wickedwizard.utils.MapCoords defaultCoords) {
                Arena arena =  new ArenaBuilder(assetManager, arenaSkin, Arena.ArenaType.NORMAL)
                        .addSection(new ArenaBuilder.Section(defaultCoords,
                                ArenaBuilder.wall.DOOR,
                                ArenaBuilder.wall.NONE,
                                ArenaBuilder.wall.DOOR,
                                ArenaBuilder.wall.DOOR))
                        .addSection(new ArenaBuilder.Section(new com.bryjamin.wickedwizard.utils.MapCoords(defaultCoords.getX() + 1, defaultCoords.getY()),
                                ArenaBuilder.wall.NONE,
                                ArenaBuilder.wall.DOOR,
                                ArenaBuilder.wall.DOOR,
                                ArenaBuilder.wall.DOOR))
                        .buildArena();

                //LEFT
                arena.addEntity(decorFactory.wallBag(0, Measure.units(30f), Measure.units(50f), Measure.units(5f),arenaSkin));
                arena.addEntity(decorFactory.platform(Measure.units(50f), Measure.units(30f), Measure.units(30f)));

                //RIGHT
                arena.addEntity(decorFactory.wallBag(arena.getWidth() - Measure.units(50f), Measure.units(30f), Measure.units(50f), Measure.units(5f),arenaSkin));
                arena.addEntity(decorFactory.platform(arena.getWidth() - Measure.units(80f), Measure.units(30f), Measure.units(30f)));



                arena.addEntity(decorFactory.wallBag(Measure.units(80f), Measure.units(30f), Measure.units(40f), Measure.units(5), arenaSkin));


                arena.addEntity(arenaEnemyPlacementFactory.silverHeadFactory.silverHead(arena.getWidth() / 2, Measure.units(40f)));
                arena.addEntity(arenaEnemyPlacementFactory.spawnBlob(Measure.units(15f), Measure.units(20f)));
                arena.addEntity(arenaEnemyPlacementFactory.spawnBlob(arena.getWidth() - Measure.units(15f), Measure.units(20f)));


                //TODO split of top and bottom blobs at the bottom silver in the middle turret get spawned in at top (Prox trigger needs to be looked at)
                // arena.addEntity(decorFactory.wallBag(Measure.units(40f), Measure.units(45f), Measure.units(10f), Measure.units(5f), arenaSkin));


                return arena;
            }
        };
    }




    public com.bryjamin.wickedwizard.factories.arenas.ArenaCreate room22TwoBlock3Bouncers() {

        return new com.bryjamin.wickedwizard.factories.arenas.ArenaCreate() {
            @Override
            public Arena createArena(com.bryjamin.wickedwizard.utils.MapCoords defaultCoords) {
                Arena arena =  new ArenaBuilder(assetManager, arenaSkin, Arena.ArenaType.TRAP)
                        .addSection(new ArenaBuilder.Section(defaultCoords,
                                ArenaBuilder.wall.DOOR,
                                ArenaBuilder.wall.DOOR,
                                ArenaBuilder.wall.GRAPPLE,
                                ArenaBuilder.wall.DOOR))
                        .buildArena();

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


    public com.bryjamin.wickedwizard.factories.arenas.ArenaCreate room23CenterSmallBlobSpawner() {

        return new com.bryjamin.wickedwizard.factories.arenas.ArenaCreate() {
            @Override
            public Arena createArena(com.bryjamin.wickedwizard.utils.MapCoords defaultCoords) {
                Arena arena =  new ArenaBuilder(assetManager, arenaSkin, Arena.ArenaType.TRAP)
                        .addSection(new ArenaBuilder.Section(defaultCoords,
                                ArenaBuilder.wall.DOOR,
                                ArenaBuilder.wall.DOOR,
                                ArenaBuilder.wall.GRAPPLE,
                                ArenaBuilder.wall.DOOR))
                        .buildArena();

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



    public com.bryjamin.wickedwizard.factories.arenas.ArenaCreate room24TwoTreasureAndFixedTurret() {

        return new com.bryjamin.wickedwizard.factories.arenas.ArenaCreate() {
            @Override
            public Arena createArena(com.bryjamin.wickedwizard.utils.MapCoords defaultCoords) {

                Arena arena = arenaShellFactory.createOmniArenaHiddenGrapple(defaultCoords, Arena.ArenaType.NORMAL);

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


    public com.bryjamin.wickedwizard.factories.arenas.ArenaCreate room25BlobUpTop() {

        return new com.bryjamin.wickedwizard.factories.arenas.ArenaCreate() {
            @Override
            public Arena createArena(com.bryjamin.wickedwizard.utils.MapCoords defaultCoords) {

                Arena arena = arenaShellFactory.createSmallArenaNoGrapple(defaultCoords, Arena.ArenaType.NORMAL);

                boolean blobIsLeft = random.nextBoolean();

                arena.addEntity(decorFactory.wallBag(blobIsLeft ? Measure.units(5f) : arena.getWidth() - Measure.units(60f), Measure.units(30f), Measure.units(60f), Measure.units(5f), arenaSkin));
                arena.addEntity(arenaEnemyPlacementFactory.spawnBlob(blobIsLeft ? Measure.units(15f) : arena.getWidth() - Measure.units(15f), Measure.units(45f)));

                //TODO add turret?
                return arena;
            }
        };
    }

    public com.bryjamin.wickedwizard.factories.arenas.ArenaCreate room26Width2And2Sentries(){

        return new com.bryjamin.wickedwizard.factories.arenas.ArenaCreate() {
            @Override
            public Arena createArena(com.bryjamin.wickedwizard.utils.MapCoords defaultCoords) {
                Arena arena =  new ArenaBuilder(assetManager, arenaSkin, Arena.ArenaType.TRAP)
                        .addSection(new ArenaBuilder.Section(defaultCoords,
                                ArenaBuilder.wall.DOOR,
                                ArenaBuilder.wall.NONE,
                                random.nextBoolean() ? ArenaBuilder.wall.FULL : ArenaBuilder.wall.GRAPPLE,
                                ArenaBuilder.wall.DOOR))
                        .addSection(new ArenaBuilder.Section(new com.bryjamin.wickedwizard.utils.MapCoords(defaultCoords.getX() + 1, defaultCoords.getY()),
                                ArenaBuilder.wall.NONE,
                                ArenaBuilder.wall.DOOR,
                                random.nextBoolean() ? ArenaBuilder.wall.FULL : ArenaBuilder.wall.GRAPPLE,
                                ArenaBuilder.wall.DOOR)).buildArena();


                arena.addEntity(arenaEnemyPlacementFactory.spawnMovingSentry(arena.getWidth() - Measure.units(20), arena.getHeight() - Measure.units(15)));
                arena.addEntity(arenaEnemyPlacementFactory.spawnMovingSentry(Measure.units(20), arena.getHeight() - Measure.units(15)));
                arena.arenaType = Arena.ArenaType.TRAP;
                return arena;
            }
        };
    }


    public com.bryjamin.wickedwizard.factories.arenas.ArenaCreate room27ThreeExitsOneTurretNoLock() {

        return new com.bryjamin.wickedwizard.factories.arenas.ArenaCreate() {
            @Override
            public Arena createArena(com.bryjamin.wickedwizard.utils.MapCoords defaultCoords) {

                boolean isLeftDoor = random.nextBoolean();
                boolean isVerticalMandatory = random.nextBoolean();

                ArenaBuilder.wall w = isVerticalMandatory ? ArenaBuilder.wall.MANDATORYDOOR : ArenaBuilder.wall.DOOR;

                Arena arena =  new ArenaBuilder(assetManager, arenaSkin, Arena.ArenaType.TRAP)
                        .addSection(new ArenaBuilder.Section(defaultCoords,
                                isLeftDoor ? w : ArenaBuilder.wall.FULL,
                                isLeftDoor ? ArenaBuilder.wall.FULL :  w,
                                ArenaBuilder.wall.DOOR,
                                ArenaBuilder.wall.MANDATORYDOOR))
                        .buildArena();
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

    public com.bryjamin.wickedwizard.factories.arenas.ArenaCreate room28LargeBouncerGrappleRoom() {

        return new com.bryjamin.wickedwizard.factories.arenas.ArenaCreate() {
            @Override
            public Arena createArena(com.bryjamin.wickedwizard.utils.MapCoords defaultCoords) {
                Arena a = arenaShellFactory.createOmniArenaHiddenGrapple(defaultCoords, Arena.ArenaType.TRAP);

                a.addEntity(arenaEnemyPlacementFactory.spawnLargeBouncer(a.getWidth() / 2, a.getHeight() / 2));


                a.addEntity(decorFactory.wallBag(a.getWidth() - Measure.units(25f), Measure.units(35f),
                        Measure.units(5f), Measure.units(5), arenaSkin));

                a.addEntity(decorFactory.wallBag(Measure.units(20f), Measure.units(35f),
                        Measure.units(5f), Measure.units(5), arenaSkin));

                return a;
            }
        };

    }



    public com.bryjamin.wickedwizard.factories.arenas.ArenaCreate room29Height2GrappleUp() {

        return new com.bryjamin.wickedwizard.factories.arenas.ArenaCreate() {
            @Override
            public Arena createArena(com.bryjamin.wickedwizard.utils.MapCoords defaultCoords) {

                Arena arena =  new ArenaBuilder(assetManager, arenaSkin, Arena.ArenaType.NORMAL)
                        .addSection(new ArenaBuilder.Section(defaultCoords,
                                ArenaBuilder.wall.DOOR,
                                ArenaBuilder.wall.DOOR,
                                ArenaBuilder.wall.NONE,
                                ArenaBuilder.wall.DOOR))
                        .addSection(new ArenaBuilder.Section(new com.bryjamin.wickedwizard.utils.MapCoords(defaultCoords.getX(), defaultCoords.getY() + 1),
                                ArenaBuilder.wall.FULL,
                                ArenaBuilder.wall.FULL,
                                ArenaBuilder.wall.MANDATORYGRAPPLE,
                                ArenaBuilder.wall.NONE))
                        .buildArena();

                arena.addEntity(decorFactory.grapplePointBag(arena.getWidth() / 4 * 3, Measure.units(45f)));
                arena.addEntity(decorFactory.grapplePointBag(arena.getWidth() / 4, Measure.units(75)));

                return arena;
            }
        };

    }


    public com.bryjamin.wickedwizard.factories.arenas.ArenaCreate room30BlobAndBouncer() {

        return new com.bryjamin.wickedwizard.factories.arenas.ArenaCreate() {
            @Override
            public Arena createArena(com.bryjamin.wickedwizard.utils.MapCoords defaultCoords) {


                Arena arena =  new ArenaBuilder(assetManager, arenaSkin, Arena.ArenaType.TRAP)
                        .addSection(new ArenaBuilder.Section(defaultCoords,
                                ArenaBuilder.wall.DOOR,
                                ArenaBuilder.wall.DOOR,
                                ArenaBuilder.wall.DOOR,
                                ArenaBuilder.wall.DOOR))
                        .buildArena();


                arena.addEntity(decorFactory.wallBag(Measure.units(35f), Measure.units(30f), Measure.units(30f), Measure.units(5f), arenaSkin));
                arena.addEntity(arenaEnemyPlacementFactory.spawnBouncer(arena.getWidth() / 2, Measure.units(45f)));
                arena.addEntity(arenaEnemyPlacementFactory.spawnBlob(arena.getWidth() / 2, Measure.units(20f)));


                return arena;
            }
        };

    }

}
