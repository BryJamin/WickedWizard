package com.byrjamin.wickedwizard.factories.arenas.levels;

import com.artemis.Component;
import com.artemis.utils.Bag;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.utils.Array;
import com.byrjamin.wickedwizard.factories.AbstractFactory;
import com.byrjamin.wickedwizard.factories.BombFactory;
import com.byrjamin.wickedwizard.factories.arenas.Arena;
import com.byrjamin.wickedwizard.factories.arenas.ArenaBuilder;
import com.byrjamin.wickedwizard.factories.arenas.ArenaCreate;
import com.byrjamin.wickedwizard.factories.arenas.decor.ArenaEnemyPlacementFactory;
import com.byrjamin.wickedwizard.factories.arenas.decor.ArenaShellFactory;
import com.byrjamin.wickedwizard.factories.arenas.decor.DecorFactory;
import com.byrjamin.wickedwizard.factories.arenas.skins.ArenaSkin;
import com.byrjamin.wickedwizard.factories.chests.ChestFactory;
import com.byrjamin.wickedwizard.factories.enemy.SpawnerFactory;
import com.byrjamin.wickedwizard.factories.enemy.TurretFactory;
import com.byrjamin.wickedwizard.utils.MapCoords;
import com.byrjamin.wickedwizard.utils.Measure;

import java.util.Random;

/**
 * Created by Home on 16/06/2017.
 */

public class Level3Rooms extends AbstractFactory implements ArenaRepostiory {

    private ArenaShellFactory arenaShellFactory;
    private ChestFactory chestFactory;
    private DecorFactory decorFactory;
    private ArenaEnemyPlacementFactory arenaEnemyPlacementFactory;
    private TurretFactory turretFactory;
    private Random random;
    private ArenaSkin arenaSkin;

    public Level3Rooms(AssetManager assetManager, ArenaSkin arenaSkin, Random random) {
        super(assetManager);
        this.arenaShellFactory = new ArenaShellFactory(assetManager, arenaSkin);
        this.chestFactory = new ChestFactory(assetManager);
        this.arenaEnemyPlacementFactory = new ArenaEnemyPlacementFactory(assetManager, arenaSkin, random);
        this.decorFactory = new DecorFactory(assetManager, arenaSkin);
        this.turretFactory = new TurretFactory(assetManager);
        this.random = random;
        this.arenaSkin = arenaSkin;
    }


    public Array<ArenaCreate> getLevel3RoomArray() {
        Array<ArenaCreate> ag = new Array<ArenaCreate>();
        ag.add(room1MultiShot());
        ag.add(room2FlyBy());
        ag.add(room3LaserKugel());
        ag.add(room4MineRun());
        ag.add(room5Modon());
        ag.add(room6WidthTwoTwoModonOneTri());
        ag.add(room7DoubleFlyBy());
        ag.add(room8Width3CenterTreaureTrapTriAndFlyBy());
        ag.add(room9Width3LaserCenterThing());
        ag.add(room10AmoebaSpawns());
        ag.add(room11Height3TripMine());
        ag.add(room12FloatyMinesAndBombs());
        ag.add(room13FlyByMinesAndBouncers());
        ag.add(room14TrapRoomTriAndOneBouncers());
        ag.add(room15TreasureRoomWhichIsLikeAnItemRoom());
        ag.add(room16Width2RedBlobs());
        ag.add(room17SpikesOnBothSideMultiArenaWithTreasure());
        ag.add(room18LeftToRightMineGauntlet());
        ag.add(room19TwoTreasureAndTriTurretORFlyByTurret());
        ag.add(room20GoatWizardTwoBouncers());
        ag.add(room21GoatWizardsStompersAndSmallBlobs());
        ag.add(room22HorizontalThroughRoomLaserCenter());
        ag.add(room23VerticalMovingTurretsAndAGapInTheMiddleToHide());
        ag.add(room24MultiWaveRoomWithMines());
        ag.add(room25Width3WithArenaInTheCenter());
        ag.add(room26Height3GrapplesAndMines());
        ag.add(room27DBlobs());
        ag.add(room28SilverHeadAndAmoebas());
        ag.add(room29JigAndTurrets());
        ag.add(room30LargeRoomWithTreasureInTheCenter());
        return ag;
    }

    @Override
    public Array<ArenaCreate> getAllArenas() {
        return  getLevel3RoomArray();
    }


    public ArenaCreate room1MultiShot() {
        return new ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {

                Arena arena = arenaShellFactory.createOmniArenaHiddenGrapple(defaultCoords);

                arena.addEntity(arenaEnemyPlacementFactory.spawnMovingTriSentry(arena.getWidth() / 2, Measure.units(45f)));
                arena.roomType = Arena.RoomType.TRAP;
                return arena;
            }
        };
    }

    public ArenaCreate room2FlyBy() {
        return new ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {
                Arena arena = arenaShellFactory.createOmniArenaHiddenGrapple(defaultCoords);
                arena.addEntity(arenaEnemyPlacementFactory.spawnMovingFlyByBombSentry(arena.getWidth() / 2, Measure.units(45f)));
                arena.roomType = Arena.RoomType.TRAP;
                return arena;
            }
        };
    }

    public ArenaCreate room3LaserKugel() {
        return new ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {
                Arena arena = arenaShellFactory.createOmniArenaHiddenGrapple(defaultCoords);
                arena.addEntity(arenaEnemyPlacementFactory.spawnLaserKugel(arena.getWidth() / 2, arena.getHeight() / 2, random.nextBoolean()));
                arena.roomType = Arena.RoomType.TRAP;
                return arena;
            }
        };
    }


    public ArenaCreate room4MineRun() {
        return new ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {
                Arena arena = new Arena(arenaSkin, defaultCoords);


                arena = new ArenaBuilder(assetManager, arenaSkin)
                        .addSection(new ArenaBuilder.Section(defaultCoords,
                                ArenaBuilder.wall.FULL,
                                ArenaBuilder.wall.FULL,
                                ArenaBuilder.wall.MANDATORYDOOR,
                                ArenaBuilder.wall.MANDATORYDOOR))
                        .buildArena(arena);

                arena.roomType = Arena.RoomType.TRAP;

                arena.addEntity(decorFactory.wallBag(Measure.units(60f), Measure.units(10f), Measure.units(35f), Measure.units(15f)));
                arena.addEntity(decorFactory.wallBag(Measure.units(25f), Measure.units(20f), Measure.units(35f), Measure.units(5)));
                arena.addEntity(decorFactory.wallBag(Measure.units(5f), Measure.units(35f), Measure.units(70f), Measure.units(5)));


                //arena.addEntity(arenaEnemyPlacementFactory.spawnLaserKugel(arena.getWidth() / 2, arena.getHeight() / 2, random.nextBoolean()));

                BombFactory bf = new BombFactory(assetManager);
                for(int i = 0; i < 9; i++) arena.addEntity(bf.mine(Measure.units(27.5f + (i * 7.5f)), Measure.units(25f), 0));

                arena.addEntity(bf.mine(Measure.units(12.5f), Measure.units(10f), 0));
                arena.addEntity(bf.mine(Measure.units(20f), Measure.units(10f), 0));

                arena.addEntity(bf.mine(Measure.units(62.5f), Measure.units(40f), 0));
                arena.addEntity(bf.mine(Measure.units(70f), Measure.units(40f), 0));


                arena.addEntity(decorFactory.spikeWall(Measure.units(90f), Measure.units(35f), Measure.units(5f), Measure.units(5f), 90));
                arena.addEntity(decorFactory.spikeWall(Measure.units(5f), Measure.units(20f), Measure.units(5f), Measure.units(5f), -90));


                arena.addEntity(chestFactory.chestBag(Measure.units(15f), Measure.units(40f)));


                return arena;
            }
        };
    }


    public ArenaCreate room5Modon() {
        return new ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {
                Arena arena = arenaShellFactory.createOmniArenaHiddenGrapple(defaultCoords);
                arena.addEntity(arenaEnemyPlacementFactory.spawnModon(arena.getWidth() / 2, arena.getHeight() / 2));
                arena.roomType = Arena.RoomType.TRAP;
                return arena;
            }
        };
    }

    public ArenaCreate room6WidthTwoTwoModonOneTri() {
        return new ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {
                Arena arena = new Arena(arenaSkin, defaultCoords,
                        new MapCoords(defaultCoords.getX() + 1, defaultCoords.getY()));


                arena = new ArenaBuilder(assetManager, arenaSkin)
                        .addSection(new ArenaBuilder.Section(defaultCoords,
                                ArenaBuilder.wall.DOOR,
                                ArenaBuilder.wall.NONE,
                                ArenaBuilder.wall.GRAPPLE,
                                ArenaBuilder.wall.DOOR))
                        .addSection(new ArenaBuilder.Section(new MapCoords(defaultCoords.getX() + 1, defaultCoords.getY()),
                                ArenaBuilder.wall.NONE,
                                ArenaBuilder.wall.DOOR,
                                ArenaBuilder.wall.GRAPPLE,
                                ArenaBuilder.wall.DOOR))
                        .buildArena(arena);

                arena.roomType = Arena.RoomType.TRAP;

                arena.addEntity(arenaEnemyPlacementFactory.spawnModon(arena.getWidth() / 4, arena.getHeight() / 2));
                arena.addEntity(arenaEnemyPlacementFactory.spawnModon(arena.getWidth() / 4 * 3, arena.getHeight() / 2));
                arena.addEntity(arenaEnemyPlacementFactory.spawnMovingTriSentry(arena.getWidth() / 2, Measure.units(45f)));

                return arena;
            }
        };
    }


    public ArenaCreate room7DoubleFlyBy() {
        return new ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {
                Arena arena = arenaShellFactory.createSmallArenaNoGrapple(defaultCoords);

                arena.roomType = Arena.RoomType.TRAP;

                arena.addEntity(decorFactory.wallBag(Measure.units(20f), Measure.units(30f), Measure.units(10), Measure.units(5f)));

                //arena.addEntity(decorFactory.platform(Measure.units(5f), Measure.units(30f), Measure.units(15f)));
                arena.addEntity(decorFactory.wallBag(arena.getWidth() - Measure.units(30f), Measure.units(30f), Measure.units(10), Measure.units(5f)));

                arena.addEntity(arenaEnemyPlacementFactory.spawnMovingFlyByBombSentry(arena.getWidth() / 4, Measure.units(45f)));
                arena.addEntity(arenaEnemyPlacementFactory.spawnMovingFlyByBombSentry(arena.getWidth() / 4 * 3, Measure.units(45f)));

                return arena;
            }
        };
    }



    public ArenaCreate room8Width3CenterTreaureTrapTriAndFlyBy() {
        return new ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {
                Arena arena = new Arena(arenaSkin, defaultCoords,
                        new MapCoords(defaultCoords.getX() + 1, defaultCoords.getY()),
                        new MapCoords(defaultCoords.getX() + 2, defaultCoords.getY()));

                arena = new ArenaBuilder(assetManager, arenaSkin)
                        .addSection(new ArenaBuilder.Section(defaultCoords,
                                ArenaBuilder.wall.MANDATORYDOOR,
                                ArenaBuilder.wall.NONE,
                                ArenaBuilder.wall.GRAPPLE,
                                ArenaBuilder.wall.NONE))
                        .addSection(new ArenaBuilder.Section(new MapCoords(defaultCoords.getX() + 1, defaultCoords.getY()),
                                ArenaBuilder.wall.NONE,
                                ArenaBuilder.wall.NONE,
                                ArenaBuilder.wall.FULL,
                                ArenaBuilder.wall.FULL))
                        .addSection(new ArenaBuilder.Section(new MapCoords(defaultCoords.getX() + 2, defaultCoords.getY()),
                                ArenaBuilder.wall.NONE,
                                ArenaBuilder.wall.MANDATORYDOOR,
                                ArenaBuilder.wall.GRAPPLE,
                                ArenaBuilder.wall.NONE))
                        .buildArena(arena);



                //arena.addEntity(decorFactory.wallBag(Measure.units(20f), Measure.units(25f), Measure.units(15f), Measure.units(5f)));
                arena.addEntity(decorFactory.outOfCombatPlatform(Measure.units(5f), Measure.units(5f), arena.getWidth()));
                arena.addEntity(decorFactory.wallBag(Measure.units(5f), Measure.units(-5f), arena.getWidth(), Measure.units(5f)));
                arena.addEntity(decorFactory.spikeWall(Measure.units(5f), Measure.units(0), arena.getWidth(), Measure.units(5f), 0));

                arena.addEntity(decorFactory.wallBag(Measure.units(100f), Measure.units(45f), Measure.units(5f), Measure.units(10f)));
                arena.addEntity(decorFactory.wallBag(Measure.units(195f), Measure.units(45f), Measure.units(5f), Measure.units(10f)));
                //arena.addEntity(decorFactory.wallBag(arena.getWidth() - Measure.units(35f), Measure.units(25f), Measure.units(15f), Measure.units(5f)));

     /*           arena.addEntity(arenaEnemyPlacementFactory.spawnMovingTriSentry(arena.getWidth() / 4, Measure.units(17.5f)));
                arena.addEntity(arenaEnemyPlacementFactory.spawnMovingFlyByBombSentry(arena.getWidth() / 2, Measure.units(45f)));
*/
                arena.addWave(arenaEnemyPlacementFactory.spawnModon(arena.getWidth() / 2, Measure.units(45f)),
                        arenaEnemyPlacementFactory.spawnFixedTriSentry(arena.getWidth() / 2, Measure.units(45f)));

                arena.addWave(arenaEnemyPlacementFactory.spawnMovingTriSentry(Measure.units(120), Measure.units(45f)),
                        arenaEnemyPlacementFactory.spawnMovingSentry(arena.getWidth() - Measure.units(110), Measure.units(45f)));



                arena.addEntity(chestFactory.chestBag(Measure.units(140f), Measure.units(10f), chestFactory.trapODAC()));
                arena.addEntity(chestFactory.chestBag(Measure.units(155f), Measure.units(10f), chestFactory.trapODAC()));

                return arena;
            }
        };
    }


    public ArenaCreate room9Width3LaserCenterThing() {
        return new ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {
                Arena arena = new Arena(arenaSkin, defaultCoords,
                        new MapCoords(defaultCoords.getX() + 1, defaultCoords.getY()),
                        new MapCoords(defaultCoords.getX() + 2, defaultCoords.getY()));

                arena = new ArenaBuilder(assetManager, arenaSkin)
                        .addSection(new ArenaBuilder.Section(defaultCoords,
                                ArenaBuilder.wall.MANDATORYDOOR,
                                ArenaBuilder.wall.NONE,
                                ArenaBuilder.wall.GRAPPLE,
                                ArenaBuilder.wall.DOOR))
                        .addSection(new ArenaBuilder.Section(new MapCoords(defaultCoords.getX() + 1, defaultCoords.getY()),
                                ArenaBuilder.wall.NONE,
                                ArenaBuilder.wall.NONE,
                                ArenaBuilder.wall.FULL,
                                ArenaBuilder.wall.FULL))
                        .addSection(new ArenaBuilder.Section(new MapCoords(defaultCoords.getX() + 2, defaultCoords.getY()),
                                ArenaBuilder.wall.NONE,
                                ArenaBuilder.wall.MANDATORYDOOR,
                                ArenaBuilder.wall.GRAPPLE,
                                ArenaBuilder.wall.DOOR))
                        .buildArena(arena);



                arena.addEntity(decorFactory.wallBag(Measure.units(100f), Measure.units(45f), Measure.units(5f), Measure.units(10f)));
                arena.addEntity(decorFactory.wallBag(Measure.units(195f), Measure.units(45f), Measure.units(5f), Measure.units(10f)));
                arena.addEntity(decorFactory.wallBag(Measure.units(100f), Measure.units(10f), Measure.units(5f), Measure.units(10f)));
                arena.addEntity(decorFactory.wallBag(Measure.units(195f), Measure.units(10f), Measure.units(5f), Measure.units(10f)));


                arena.addEntity(decorFactory.laserChain(Measure.units(147.5f), Measure.units(30f)));

                arena.addEntity(chestFactory.chestBag(Measure.units(145f), Measure.units(10f)));


                return arena;
            }
        };
    }


    public ArenaCreate room10AmoebaSpawns() {
        return new ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {
                final Arena arena = arenaShellFactory.createOmniArenaHiddenGrapple(defaultCoords);


                arena.roomType = Arena.RoomType.TRAP;

                arena.addEntity(arenaEnemyPlacementFactory.spawnerFactory.spawnerBag(arena.getWidth() / 4, Measure.units(45f),
                        new SpawnerFactory.Spawner() {
                            public Bag<Component> spawnBag(float x, float y) {
                                return arenaEnemyPlacementFactory.amoebaFactory.fastamoeba(x,y);
                            }
                        }, 3));

                arena.addEntity(arenaEnemyPlacementFactory.spawnerFactory.spawnerBag(arena.getWidth() / 2, Measure.units(45f),
                        new SpawnerFactory.Spawner() {
                            public Bag<Component> spawnBag(float x, float y) {
                                return arenaEnemyPlacementFactory.amoebaFactory.fastamoeba(x,y);
                            }
                        }, 3));

                arena.addEntity(arenaEnemyPlacementFactory.spawnerFactory.spawnerBag(arena.getWidth() / 4 * 3, Measure.units(45f),
                        new SpawnerFactory.Spawner() {
                            public Bag<Component> spawnBag(float x, float y) {
                                return arenaEnemyPlacementFactory.amoebaFactory.fastamoeba(x,y);
                            }
                        }, 3));

                return arena;
            }
        };
    }


    public ArenaCreate room11Height3TripMine() {
        return new ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {

                Arena arena = new Arena(arenaSkin, defaultCoords,
                        new MapCoords(defaultCoords.getX(), defaultCoords.getY() + 1),
                        new MapCoords(defaultCoords.getX(), defaultCoords.getY() + 2));

                arena = new ArenaBuilder(assetManager, arenaSkin)
                        .addSection(new ArenaBuilder.Section(defaultCoords,
                                ArenaBuilder.wall.DOOR,
                                ArenaBuilder.wall.DOOR,
                                ArenaBuilder.wall.NONE,
                                ArenaBuilder.wall.MANDATORYDOOR))
                        .addSection(new ArenaBuilder.Section(new MapCoords(defaultCoords.getX(), defaultCoords.getY() + 1),
                                ArenaBuilder.wall.FULL,
                                ArenaBuilder.wall.FULL,
                                ArenaBuilder.wall.NONE,
                                ArenaBuilder.wall.NONE))
                        .addSection(new ArenaBuilder.Section(new MapCoords(defaultCoords.getX(), defaultCoords.getY() + 2),
                                ArenaBuilder.wall.DOOR,
                                ArenaBuilder.wall.DOOR,
                                ArenaBuilder.wall.MANDATORYGRAPPLE,
                                ArenaBuilder.wall.NONE))
                        .buildArena(arena);


                arena.addEntity(decorFactory.wallBag(Measure.units(40f), Measure.units(30f), Measure.units(20f), Measure.units(5f)));
                arena.addEntity(decorFactory.wallBag(Measure.units(15f), Measure.units(50f), Measure.units(20f), Measure.units(5f)));
                arena.addEntity(decorFactory.wallBag(Measure.units(65f), Measure.units(50f), Measure.units(20f), Measure.units(5f)));


                arena.addEntity(decorFactory.grapplePointBag(arena.getWidth() / 2, Measure.units(70f)));
                arena.addEntity(decorFactory.wallBag(Measure.units(15f), Measure.units(90f), Measure.units(20f), Measure.units(5f)));
                arena.addEntity(decorFactory.wallBag(Measure.units(65f), Measure.units(90f), Measure.units(20f), Measure.units(5f)));

                arena.addEntity(decorFactory.grapplePointBag(arena.getWidth() / 2, Measure.units(110f)));


                arena.addEntity(decorFactory.wallBag(Measure.units(5f), Measure.units(125f), Measure.units(20f), Measure.units(5f)));
                arena.addEntity(decorFactory.wallBag(arena.getWidth() - Measure.units(25f), Measure.units(125f), Measure.units(20f), Measure.units(5f)));


                BombFactory bf = new BombFactory(assetManager);

                arena.addEntity(bf.seaMine(Measure.units(50f), Measure.units(20f), random.nextBoolean(), random.nextBoolean()));
                arena.addEntity(bf.seaMine(Measure.units(50f), arena.getHeight() / 2, random.nextBoolean(), random.nextBoolean()));
                arena.addEntity(bf.seaMine(Measure.units(20f), arena.getHeight() / 2, random.nextBoolean(), random.nextBoolean()));

                arena.addEntity(bf.seaMine(Measure.units(20f), Measure.units(100f), random.nextBoolean(), random.nextBoolean()));
                arena.addEntity(bf.seaMine(arena.getWidth() / 4 , Measure.units(140f), random.nextBoolean(), random.nextBoolean()));
                arena.addEntity(bf.seaMine(arena.getWidth() / 4 * 3, Measure.units(140f), random.nextBoolean(), random.nextBoolean()));

                return arena;
            }
        };
    }


    //TODO too easy?
    public ArenaCreate room12FloatyMinesAndBombs() {
        return new ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {

                Arena arena = arenaShellFactory.createOmniArenaHiddenGrapple(defaultCoords);

                arena.roomType = Arena.RoomType.TRAP;


                arena.addEntity(decorFactory.wallBag(Measure.units(20f), Measure.units(30f), Measure.units(5f), Measure.units(5f)));
                arena.addEntity(decorFactory.wallBag(arena.getWidth() - Measure.units(25f), Measure.units(30f), Measure.units(5f), Measure.units(5f)));


                BombFactory bf = new BombFactory(assetManager);
                arena.addEntity(bf.seaMine(Measure.units(15f), Measure.units(20f), random.nextBoolean(), random.nextBoolean()));
                arena.addEntity(bf.seaMine(arena.getWidth() - Measure.units(15f),Measure.units(20f), random.nextBoolean(), random.nextBoolean()));

                arena.addEntity(arenaEnemyPlacementFactory.spawnFixedTriSentry(arena.getWidth() / 2, Measure.units(45f)));
                arena.addEntity(arenaEnemyPlacementFactory.spawnBouncer(arena.getWidth() / 2, arena.getHeight() / 2));
                //arena.addEntity(arenaEnemyPlacementFactory.spawnFixedTriSentry(arena.getWidth() / 2, Measure.units(45f)));


                return arena;
            }
        };
    }


    public ArenaCreate room13FlyByMinesAndBouncers() {
        return new ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {

                Arena arena = arenaShellFactory.createOmniArenaHiddenGrapple(defaultCoords);

                arena.roomType = Arena.RoomType.TRAP;


                arena.addEntity(decorFactory.wallBag(Measure.units(20f), Measure.units(30f), Measure.units(5f), Measure.units(5f)));
                arena.addEntity(decorFactory.wallBag(arena.getWidth() - Measure.units(25f), Measure.units(30f), Measure.units(5f), Measure.units(5f)));


                BombFactory bf = new BombFactory(assetManager);
                arena.addEntity(bf.seaMine(Measure.units(15f), Measure.units(20f), random.nextBoolean(), random.nextBoolean()));
                arena.addEntity(bf.seaMine(arena.getWidth() - Measure.units(15f),Measure.units(20f), random.nextBoolean(), random.nextBoolean()));

                arena.addEntity(arenaEnemyPlacementFactory.spawnFixedFlyByBombSentry(arena.getWidth() / 2, Measure.units(45f)));
                arena.addEntity(arenaEnemyPlacementFactory.spawnBouncer(arena.getWidth() / 4 * 3, Measure.units(45f)));
                arena.addEntity(arenaEnemyPlacementFactory.spawnBouncer(arena.getWidth() / 4, Measure.units(45f)));
                //arena.addEntity(arenaEnemyPlacementFactory.spawnFixedTriSentry(arena.getWidth() / 2, Measure.units(45f)));


                return arena;
            }
        };
    }



    //TODO change to 2?
    public ArenaCreate room14TrapRoomTriAndOneBouncers() {
        return new ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {

                Arena arena = arenaShellFactory.createOmniArenaHiddenGrapple(defaultCoords);
/*

                arena.addEntity(decorFactory.wallBag(Measure.units(20f), Measure.units(30f), Measure.units(5f), Measure.units(5f)));
                arena.addEntity(decorFactory.wallBag(arena.getWidth() - Measure.units(25f), Measure.units(30f), Measure.units(5f), Measure.units(5f)));*/

                arena.addEntity(chestFactory.chestBag(Measure.units(45f), Measure.units(10f), chestFactory.trapODAC()));



                arena.addWave(arenaEnemyPlacementFactory.spawnBouncer(Measure.units(15), Measure.units(50f)),
                        arenaEnemyPlacementFactory.spawnFixedTriSentry(arena.getWidth() / 2, arena.getHeight() / 2),
                        arenaEnemyPlacementFactory.spawnBouncer(arena.getWidth() - Measure.units(15), Measure.units(50f)));


                return arena;
            }
        };
    }



    public ArenaCreate room15TreasureRoomWhichIsLikeAnItemRoom() {
        return new ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {

                Arena arena = arenaShellFactory.createOmniArenaHiddenGrapple(defaultCoords);
                arena.addEntity(decorFactory.lockWall(Measure.units(5), Measure.units(30f), Measure.units(30f), Measure.units(5f)));
                arena.addEntity(decorFactory.lockWall(arena.getWidth() - Measure.units(35f), Measure.units(30f), Measure.units(30f), Measure.units(5f)));

                arena.addEntity(decorFactory.wallBag(arena.getWidth() - Measure.units(40f), Measure.units(30f), Measure.units(5f), Measure.units(25f)));
                arena.addEntity(decorFactory.wallBag(Measure.units(35f), Measure.units(30f), Measure.units(5f), Measure.units(25f)));


                arena.addEntity(chestFactory.chestBag(Measure.units(7.5f), Measure.units(35f)));
                arena.addEntity(chestFactory.chestBag(Measure.units(22.5f), Measure.units(35f)));

                arena.addEntity(chestFactory.chestBag(Measure.units(67.5f), Measure.units(35f)));
                arena.addEntity(chestFactory.chestBag(Measure.units(82.5f), Measure.units(35f)));


                //TODO trap chest. could hold a bomb or a mimic or something the rest are actual chests.
                arena.addEntity(chestFactory.chestBag(Measure.units(45f), Measure.units(10f), chestFactory.trapODAC()));

                arena.addWave(arenaEnemyPlacementFactory.spawnFixedFlyByBombSentry(arena.getWidth() / 2, Measure.units(30f)));

                return arena;
            }
        };
    }


    public ArenaCreate room16Width2RedBlobs() {
        return new ArenaCreate() {
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

                //LEFT
                arena.addEntity(decorFactory.wallBag(0, Measure.units(30f), Measure.units(75f), Measure.units(5f),arenaSkin));
                //arena.addEntity(decorFactory.platform(Measure.units(50f), Measure.units(30f), Measure.units(30f)));

                //RIGHT
                arena.addEntity(decorFactory.wallBag(arena.getWidth() - Measure.units(75f), Measure.units(30f), Measure.units(75f), Measure.units(5f),arenaSkin));

                arena.addEntity(decorFactory.platform(Measure.units(75f), Measure.units(30f), Measure.units(50f)));

                arena.addEntity(arenaEnemyPlacementFactory.spawnAngryBlob(arena.getWidth() / 2 , Measure.units(45f)));

                arena.addEntity(arenaEnemyPlacementFactory.spawnAngryBlob(Measure.units(20f) , Measure.units(15f)));

                arena.addEntity(arenaEnemyPlacementFactory.spawnAngryBlob(arena.getWidth() - Measure.units(20f) , Measure.units(15f)));

                //arena.addEntity(decorFactory.wallBag(Measure.units(80f), Measure.units(30f), Measure.units(40f),Measure.units(5), arenaSkin));

                return arena;
            }
        };
    }


    public ArenaCreate room17SpikesOnBothSideMultiArenaWithTreasure() {
        return new ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {

                Arena arena = new Arena(Arena.RoomType.TRAP, arenaSkin, defaultCoords);

                arena =  new ArenaBuilder(assetManager, arenaSkin)
                        .addSection(new ArenaBuilder.Section(defaultCoords,
                                ArenaBuilder.wall.FULL,
                                ArenaBuilder.wall.FULL,
                                ArenaBuilder.wall.MANDATORYGRAPPLE,
                                ArenaBuilder.wall.MANDATORYDOOR))
                        .buildArena(arena);

                arena.addEntity(decorFactory.spikeWall(Measure.units(5f), Measure.units(10f), Measure.units(15f), Measure.units(5f), 0));
                arena.addEntity(decorFactory.spikeWall(arena.getWidth() - Measure.units(20f), Measure.units(10f), Measure.units(15f), Measure.units(5f), 0));


                arena.roomType = Arena.RoomType.TRAP;

                arena.addWave(arenaEnemyPlacementFactory.spawnMovingTriSentry(arena.getWidth() / 2, Measure.units(45f)));

                arena.addWave(arenaEnemyPlacementFactory.spawnGoatWizard(arena.getWidth() / 2, Measure.units(45f)));

                arena.addWave(arenaEnemyPlacementFactory.spawnerFactory.spawnerBag(arena.getWidth() / 2, arena.getHeight() / 2,
                        new SpawnerFactory.Spawner() {
                            public Bag<Component> spawnBag(float x, float y) {
                                return chestFactory.centeredChestBag(x,y);
                            }
                        }));

                BombFactory bf = new BombFactory(assetManager);

                arena.addEntity(bf.seaMine(arena.getWidth() / 4, arena.getWidth() / 2, random.nextBoolean(), random.nextBoolean()));
                arena.addEntity(bf.seaMine(arena.getWidth() / 4 * 3, arena.getWidth() / 2, random.nextBoolean(), random.nextBoolean()));

                return arena;
            }
        };
    }


    //TODO make a mirror version?
    public ArenaCreate room18LeftToRightMineGauntlet() {
        return new ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {

                Arena arena = new Arena(Arena.RoomType.TRAP, arenaSkin, defaultCoords);

                arena =  new ArenaBuilder(assetManager, arenaSkin)
                        .addSection(new ArenaBuilder.Section(defaultCoords,
                                ArenaBuilder.wall.MANDATORYDOOR,
                                ArenaBuilder.wall.MANDATORYDOOR,
                                ArenaBuilder.wall.FULL,
                                ArenaBuilder.wall.FULL))
                        .buildArena(arena);


                arena.addEntity(decorFactory.wallBag(Measure.units(25f), Measure.units(10f), Measure.units(20f), Measure.units(35f)));
                arena.addEntity(decorFactory.wallBag(Measure.units(55f), Measure.units(20f), Measure.units(20f), Measure.units(35f)));

                arena.addEntity(decorFactory.grapplePointBag(Measure.units(50f), Measure.units(50f)));
                //arena.addEntity(decorFactory.grapplePointBag(Measure.units(15f), Measure.units(50f)));

                BombFactory bf = new BombFactory(assetManager);

                for(int i = 0; i < 4; i++) arena.addEntity(bf.mine(Measure.units(47.5f + (i * 7.5f)), Measure.units(10f), 0));

                for(int i = 0; i < 5; i++) arena.addEntity(bf.mine(Measure.units(45f), Measure.units(10f + (i * 7.5f)), -90));
                for(int i = 0; i < 5; i++) arena.addEntity(bf.mine(Measure.units(50f), Measure.units(20f + (i * 7.5f)), 90));

                for(int i = 0; i < 3; i++) arena.addEntity(bf.mine(Measure.units(25f + (i * 7.5f)), Measure.units(45f), 0));





                return arena;
            }
        };
    }


    public ArenaCreate room19TwoTreasureAndTriTurretORFlyByTurret() {

        return new ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {

                Arena arena = arenaShellFactory.createOmniArenaHiddenGrapple(defaultCoords);

                boolean isTri = random.nextBoolean();
                boolean isTreasureOnLeft = random.nextBoolean();

                arena.addEntity(decorFactory.wallBag(isTreasureOnLeft ? Measure.units(35f) : arena.getWidth() - Measure.units(40f), Measure.units(35f), Measure.units(5f), Measure.units(20f), arenaSkin));
                // arena.addEntity(decorFactory.wallBag(Measure.units(35f), Measure.units(35f), Measure.units(5f), Measure.units(20f), arenaSkin));
                //arena.addEntity(decorFactory.wallBag(arena.getWidth() - Measure.units(40f), Measure.units(35f), Measure.units(5f), Measure.units(20f), arenaSkin));
                arena.addEntity(decorFactory.lockWall(isTreasureOnLeft ? Measure.units(5f) : arena.getWidth() - Measure.units(35f), Measure.units(35f), Measure.units(30f), Measure.units(5)));
                arena.addEntity(chestFactory.chestBag(isTreasureOnLeft ? Measure.units(7.5f) : arena.getWidth() - Measure.units(17.5f), Measure.units(40f)));
                arena.addEntity(chestFactory.chestBag(isTreasureOnLeft ? Measure.units(22.5f) : arena.getWidth() - Measure.units(32.5f), Measure.units(40f)));
                arena.addEntity(isTri ? turretFactory.fixedMultiSentry(isTreasureOnLeft ? Measure.units(85f) : arena.getWidth() - Measure.units(85f), Measure.units(45f)) :
                        turretFactory.fixedFlyByBombSentry(isTreasureOnLeft ? Measure.units(85f) : arena.getWidth() - Measure.units(85f), Measure.units(45f)));


                return arena;
            }
        };
    }


    public ArenaCreate room20GoatWizardTwoBouncers() {

        return new ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {

                Arena arena = arenaShellFactory.createOmniArenaHiddenGrapple(defaultCoords);

                arena.roomType = Arena.RoomType.TRAP;

                arena.addEntity(arenaEnemyPlacementFactory.spawnGoatWizard(arena.getWidth() / 2, Measure.units(45f)));

                arena.addEntity(arenaEnemyPlacementFactory.spawnBouncer(Measure.units(20f), Measure.units(35f)));
                arena.addEntity(arenaEnemyPlacementFactory.spawnBouncer(arena.getWidth() - Measure.units(20f), Measure.units(35f)));

                arena.addEntity(decorFactory.fixedWallTurret(Measure.units(20f), Measure.units(50f), 180, 1.5f, 1.5f));
                arena.addEntity(decorFactory.fixedWallTurret(arena.getWidth() - Measure.units(25f), Measure.units(50f), 180, 1.5f, 1.5f));


                return arena;
            }
        };
    }



    public ArenaCreate room21GoatWizardsStompersAndSmallBlobs() {

        return new ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {

                Arena arena = new Arena(Arena.RoomType.TRAP, arenaSkin, defaultCoords,
                        new MapCoords(defaultCoords.getX() + 1, defaultCoords.getY()));

                arena =  new ArenaBuilder(assetManager, arenaSkin)
                        .addSection(new ArenaBuilder.Section(defaultCoords,
                                ArenaBuilder.wall.DOOR,
                                ArenaBuilder.wall.NONE,
                                ArenaBuilder.wall.GRAPPLE,
                                ArenaBuilder.wall.DOOR))
                        .addSection(new ArenaBuilder.Section(new MapCoords(defaultCoords.getX() + 1, defaultCoords.getY()),
                                ArenaBuilder.wall.NONE,
                                ArenaBuilder.wall.DOOR,
                                ArenaBuilder.wall.GRAPPLE,
                                ArenaBuilder.wall.DOOR))
                        .buildArena(arena);
/*

                arena.fffffffffff(chestFactory.
                );
*/

                arena.addWave(arenaEnemyPlacementFactory.spawnModon(arena.getWidth() / 2, Measure.units(45f)),
                        arenaEnemyPlacementFactory.spawnGoatWizard(Measure.units(20f), Measure.units(35f)),
                        arenaEnemyPlacementFactory.spawnSmallAngryBlob(Measure.units(20f), Measure.units(20f)),
                        arenaEnemyPlacementFactory.spawnSmallAngryBlob(arena.getWidth() - Measure.units(20f), Measure.units(20f))
                );

                arena.addWave(arenaEnemyPlacementFactory.spawnerFactory.spawnerBag(arena.getWidth() / 2, arena.getHeight() / 2,
                        new SpawnerFactory.Spawner() {
                            public Bag<Component> spawnBag(float x, float y) {
                                return chestFactory.centeredChestBag(x,y);
                            }
                        }));

/*                arena.addEntity(arenaEnemyPlacementFactory.spawnGoatWizard(Measure.units(20f), Measure.units(35f)));
                arena.addEntity(arenaEnemyPlacementFactory.spawnGoatWizard(arena.getWidth() / 2 - Measure.units(20f), Measure.units(35f)));


                arena.addEntity(decorFactory.fixedWallTurret(Measure.units(20f), Measure.units(50f), 180, 1.5f, 1.5f));
                arena.addEntity(decorFactory.fixedWallTurret(arena.getWidth() - Measure.units(25f), Measure.units(50f), 180, 1.5f, 1.5f));*/


                return arena;
            }
        };
    }




    public ArenaCreate room22HorizontalThroughRoomLaserCenter() {

        return new ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {

                Arena arena = new Arena(Arena.RoomType.TRAP, arenaSkin, defaultCoords);

                arena =  new ArenaBuilder(assetManager, arenaSkin)
                        .addSection(new ArenaBuilder.Section(defaultCoords,
                                ArenaBuilder.wall.MANDATORYDOOR,
                                ArenaBuilder.wall.MANDATORYDOOR,
                                ArenaBuilder.wall.FULL,
                                ArenaBuilder.wall.FULL))
                        .buildArena(arena);

                for(int i = 0; i < 6; i++) arena.addEntity(decorFactory.fixedWallTurret(Measure.units(35f + (i * 5f)), Measure.units(50f), 180, 1.5f, 1.5f));

                boolean isLeft = random.nextBoolean();

                arena.addWave(arenaEnemyPlacementFactory.spawnFixedTriSentry(!isLeft ? Measure.units(20f) : arena.getWidth() - Measure.units(20f), Measure.units(45f)));
                arena.addWave(arenaEnemyPlacementFactory.spawnFixedTriSentry(isLeft ? Measure.units(20f) : arena.getWidth() - Measure.units(20f), Measure.units(45f)));

                return arena;
            }
        };
    }







    public ArenaCreate room23VerticalMovingTurretsAndAGapInTheMiddleToHide() {

        return new ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {

                Arena arena = new Arena(Arena.RoomType.TRAP, arenaSkin, defaultCoords);

                arena =  new ArenaBuilder(assetManager, arenaSkin)
                        .addSection(new ArenaBuilder.Section(defaultCoords,
                                ArenaBuilder.wall.DOOR,
                                ArenaBuilder.wall.DOOR,
                                ArenaBuilder.wall.DOOR,
                                ArenaBuilder.wall.DOOR))
                        .buildArena(arena);


                arena.addEntity(decorFactory.wallBag(Measure.units(40), Measure.units(20f), Measure.units(20f), Measure.units(5f)));
                arena.addEntity(decorFactory.wallBag(Measure.units(45), Measure.units(25f), Measure.units(10f), Measure.units(15f)));

                boolean bool = random.nextBoolean();

                arena.addEntity(arenaEnemyPlacementFactory.spawnMovingVerticalTriSentry(Measure.units(20f), arena.getHeight() / 2 + Measure.units(2.5f), bool));
                arena.addEntity(arenaEnemyPlacementFactory.spawnMovingVerticalTriSentry(arena.getWidth() - Measure.units(20f) ,arena.getHeight() / 2 + Measure.units(2.5f), !bool));


                return arena;
            }
        };
    }



    public ArenaCreate room24MultiWaveRoomWithMines() {

        return new ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {

                Arena arena = new Arena(Arena.RoomType.TRAP, arenaSkin, defaultCoords);

                arena =  new ArenaBuilder(assetManager, arenaSkin)
                        .addSection(new ArenaBuilder.Section(defaultCoords,
                                ArenaBuilder.wall.DOOR,
                                ArenaBuilder.wall.DOOR,
                                ArenaBuilder.wall.DOOR,
                                ArenaBuilder.wall.DOOR))
                        .buildArena(arena);

                BombFactory bf = new BombFactory(assetManager);

                arena.addWave(bf.seaMine(arena.getWidth() / 4 * 3, arena.getHeight() / 2, random.nextBoolean(), random.nextBoolean()),
                        bf.seaMine(arena.getWidth() / 4, arena.getHeight() / 2, random.nextBoolean(), random.nextBoolean()),
                        arenaEnemyPlacementFactory.spawnModon(arena.getWidth() / 2, arena.getHeight() / 2 + Measure.units(5f)));

                arena.addWave(bf.seaMine(arena.getWidth() / 4 * 3, arena.getHeight() / 2, random.nextBoolean(), random.nextBoolean()),
                        bf.seaMine(arena.getWidth() / 4, arena.getHeight() / 2, random.nextBoolean(), random.nextBoolean()),
                        arenaEnemyPlacementFactory.spawnMovingJig(arena.getWidth() / 2, arena.getHeight() / 2 + Measure.units(2.5f)));


                arena.addWave(arenaEnemyPlacementFactory.spawnerFactory.spawnerBag(arena.getWidth() / 2, arena.getHeight() / 2,
                        new SpawnerFactory.Spawner() {
                            public Bag<Component> spawnBag(float x, float y) {
                                return chestFactory.centeredChestBag(x,y);
                            }
                        }));

      /*          arena.addEntity(arenaEnemyPlacementFactory.spawnMovingVerticalTriSentry(Measure.units(20f), arena.getHeight() / 2 + Measure.units(2.5f), bool));
                arena.addEntity(arenaEnemyPlacementFactory.spawnMovingVerticalTriSentry(arena.getWidth() - Measure.units(20f) ,arena.getHeight() / 2 + Measure.units(2.5f), !bool));
*/

                return arena;
            }
        };
    }




    public ArenaCreate room25Width3WithArenaInTheCenter() {

        return new ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {

                Arena arena = new Arena(arenaSkin, defaultCoords,
                        new MapCoords(defaultCoords.getX() + 1, defaultCoords.getY()),
                        new MapCoords(defaultCoords.getX() + 2, defaultCoords.getY()));

                arena =  new ArenaBuilder(assetManager, arenaSkin)
                        .addSection(new ArenaBuilder.Section(defaultCoords,
                                ArenaBuilder.wall.MANDATORYDOOR,
                                ArenaBuilder.wall.NONE,
                                ArenaBuilder.wall.FULL,
                                ArenaBuilder.wall.FULL))
                        .addSection(new ArenaBuilder.Section(new MapCoords(defaultCoords.getX() + 1, defaultCoords.getY()),
                                ArenaBuilder.wall.NONE,
                                ArenaBuilder.wall.NONE,
                                ArenaBuilder.wall.GRAPPLE,
                                ArenaBuilder.wall.DOOR))
                        .addSection(new ArenaBuilder.Section(new MapCoords(defaultCoords.getX() + 2, defaultCoords.getY()),
                                ArenaBuilder.wall.NONE,
                                ArenaBuilder.wall.MANDATORYDOOR,
                                ArenaBuilder.wall.FULL,
                                ArenaBuilder.wall.FULL))
                        .buildArena(arena);

                arena.addWave(arenaEnemyPlacementFactory.spawnModon(arena.getWidth() / 2, arena.getHeight() / 2));

                arena.addWave(arenaEnemyPlacementFactory.spawnkugelDusche(arena.getWidth() / 2, Measure.units(32.5f)));


                arena.addWave(arenaEnemyPlacementFactory.spawnAngryBlob(arena.getWidth() / 2, Measure.units(32.5f)),
                        arenaEnemyPlacementFactory.spawnMovingFlyByBombSentry(Measure.units(125f), Measure.units(45f)),
                        arenaEnemyPlacementFactory.spawnMovingFlyByBombSentry(arena.getWidth() - Measure.units(125f), Measure.units(45f)));


                arena.addWave(
                        arenaEnemyPlacementFactory.spawnMovingTriSentry(Measure.units(125f), Measure.units(45f)),
                        arenaEnemyPlacementFactory.spawnMovingTriSentry(arena.getWidth() - Measure.units(125f), Measure.units(45f)));


                arena.addEntity(chestFactory.chestBag(Measure.units(132.5f), Measure.units(15f), chestFactory.trapODAC()));
                arena.addEntity(chestFactory.chestBag(Measure.units(145f), Measure.units(15f), chestFactory.trapODAC()));
                arena.addEntity(chestFactory.chestBag(Measure.units(157.5f), Measure.units(15f), chestFactory.trapODAC()));

                //left
                arena.addEntity(decorFactory.wallBag(Measure.units(35f), Measure.units(10f), Measure.units(10f), Measure.units(25f)));
                arena.addEntity(decorFactory.wallBag(Measure.units(45f), Measure.units(10f), Measure.units(55f), Measure.units(35f)));

                arena.addEntity(decorFactory.appearInCombatWallPush(Measure.units(95f), Measure.units(45f), Measure.units(5f), Measure.units(10f), 0));
                arena.addEntity(decorFactory.appearInCombatWallPush(arena.getWidth() - Measure.units(100f), Measure.units(45f), Measure.units(5f), Measure.units(10f), 180));


                arena.addEntity(decorFactory.wallBag(arena.getWidth() - Measure.units(45f), Measure.units(10f), Measure.units(10f), Measure.units(25f)));
                arena.addEntity(decorFactory.wallBag(arena.getWidth() - Measure.units(100f), Measure.units(10f), Measure.units(55f), Measure.units(35f)));

                return arena;
            }
        };
    }



    public ArenaCreate room26Height3GrapplesAndMines() {

        return new ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {

                Arena arena = new Arena(arenaSkin, defaultCoords,
                        new MapCoords(defaultCoords.getX(), defaultCoords.getY() + 1),
                        new MapCoords(defaultCoords.getX(), defaultCoords.getY() + 2));

                arena =  new ArenaBuilder(assetManager, arenaSkin)
                        .addSection(new ArenaBuilder.Section(defaultCoords,
                                ArenaBuilder.wall.DOOR,
                                ArenaBuilder.wall.DOOR,
                                ArenaBuilder.wall.NONE,
                                ArenaBuilder.wall.MANDATORYDOOR))
                        .addSection(new ArenaBuilder.Section(new MapCoords(defaultCoords.getX(), defaultCoords.getY() + 1),
                                ArenaBuilder.wall.FULL,
                                ArenaBuilder.wall.FULL,
                                ArenaBuilder.wall.NONE,
                                ArenaBuilder.wall.NONE))
                        .addSection(new ArenaBuilder.Section(new MapCoords(defaultCoords.getX(), defaultCoords.getY() + 2),
                                ArenaBuilder.wall.FULL,
                                ArenaBuilder.wall.FULL,
                                ArenaBuilder.wall.MANDATORYGRAPPLE,
                                ArenaBuilder.wall.NONE))
                        .buildArena(arena);

                boolean bool = random.nextBoolean();

                BombFactory bf = new BombFactory(assetManager);

                for(int i = 0; i < 4; i++) {
                    arena.addEntity(decorFactory.grapplePointBag( i % 2 == (bool ? 0 : 1) ? arena.getWidth() / 4 * 3 : arena.getWidth() / 4, Measure.units(45f + (i * 30))));
                }

                for(int i = 0; i < 4; i++) {
                    arena.addEntity(bf.seaMine(arena.getWidth() / 2, Measure.units(30f + (i * 30)), random.nextBoolean(), random.nextBoolean()));
                }




                return arena;
            }
        };
    }


    public ArenaCreate room27DBlobs() {

        return new ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {

                Arena arena = arenaShellFactory.createSmallArenaNoGrapple(defaultCoords);

                arena.roomType = Arena.RoomType.TRAP;

                boolean isLeft = random.nextBoolean();

                arena.addEntity(decorFactory.wallBag(isLeft ? Measure.units(5f) : arena.getWidth() - Measure.units(65f), Measure.units(30f), Measure.units(60f), Measure.units(5f)));


                arena.addEntity(arenaEnemyPlacementFactory.spawnerFactory.spawnerBag(isLeft ? arena.getWidth() / 4 : arena.getWidth() / 4 * 3, Measure.units(45f),
                        new SpawnerFactory.Spawner() {
                            public Bag<Component> spawnBag(float x, float y) {
                                return arenaEnemyPlacementFactory.blobFactory.angrySmallBag(x,y, random.nextBoolean());
                            }
                        }, 4));

                return arena;
            }
        };
    }


    public ArenaCreate room28SilverHeadAndAmoebas() {

        return new ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {

                Arena arena = arenaShellFactory.createSmallArenaNoGrapple(defaultCoords);

                arena.roomType = Arena.RoomType.TRAP;

                arena.addEntity(decorFactory.platform(Measure.units(35f), Measure.units(20), Measure.units(30f)));

                arena.addEntity(arenaEnemyPlacementFactory.spawnSilverHead(arena.getWidth() / 2, Measure.units(45f)));

                arena.addEntity(arenaEnemyPlacementFactory.spawnerFactory.spawnerBag(arena.getWidth() / 4, Measure.units(45f),
                        new SpawnerFactory.Spawner() {
                            public Bag<Component> spawnBag(float x, float y) {
                                return arenaEnemyPlacementFactory.amoebaFactory.fastamoeba(x,y);
                            }
                        }, 4));

                arena.addEntity(arenaEnemyPlacementFactory.spawnerFactory.spawnerBag(arena.getWidth() / 4 * 3, Measure.units(45f),
                        new SpawnerFactory.Spawner() {
                            public Bag<Component> spawnBag(float x, float y) {
                                return arenaEnemyPlacementFactory.amoebaFactory.fastamoeba(x,y);
                            }
                        }, 4));

                return arena;
            }
        };
    }


    public ArenaCreate room29JigAndTurrets() {

        return new ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {

                Arena arena = arenaShellFactory.createOmniArenaHiddenGrapple(defaultCoords);

                arena.roomType = Arena.RoomType.TRAP;


                arena.addEntity(arenaEnemyPlacementFactory.spawnMovingJig(arena.getWidth() / 2, Measure.units(40f)));


                arena.addEntity(decorFactory.fixedWallTurret(Measure.units(20f), Measure.units(50f), 180, 1.5f, 1.5f));
                arena.addEntity(decorFactory.fixedWallTurret(arena.getWidth() - Measure.units(25f), Measure.units(50f), 180, 1.5f, 1.5f));


                return arena;
            }
        };
    }



    public ArenaCreate room30LargeRoomWithTreasureInTheCenter() {

        return new ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {

                Arena arena = new Arena(arenaSkin, defaultCoords,
                        new MapCoords(defaultCoords.getX(), defaultCoords.getY() + 1),
                        new MapCoords(defaultCoords.getX() + 1, defaultCoords.getY()),
                        new MapCoords(defaultCoords.getX() + 1, defaultCoords.getY() + 1));

                arena =  new ArenaBuilder(assetManager, arenaSkin)
                        .addSection(new ArenaBuilder.Section(defaultCoords,
                                ArenaBuilder.wall.DOOR,
                                ArenaBuilder.wall.NONE,
                                ArenaBuilder.wall.NONE,
                                ArenaBuilder.wall.NONE))
                        .addSection(new ArenaBuilder.Section(new MapCoords(defaultCoords.getX() + 1, defaultCoords.getY()),
                                ArenaBuilder.wall.NONE,
                                ArenaBuilder.wall.DOOR,
                                ArenaBuilder.wall.NONE,
                                ArenaBuilder.wall.NONE))
                        .addSection(new ArenaBuilder.Section(new MapCoords(defaultCoords.getX(), defaultCoords.getY() + 1),
                                ArenaBuilder.wall.FULL,
                                ArenaBuilder.wall.NONE,
                                ArenaBuilder.wall.GRAPPLE,
                                ArenaBuilder.wall.NONE))
                        .addSection(new ArenaBuilder.Section(new MapCoords(defaultCoords.getX() + 1, defaultCoords.getY() + 1),
                                ArenaBuilder.wall.NONE,
                                ArenaBuilder.wall.FULL,
                                ArenaBuilder.wall.GRAPPLE,
                                ArenaBuilder.wall.NONE)).buildArena(arena);

                arena.addEntity(decorFactory.wallBag(Measure.units(0f), Measure.units(-5f), arena.getWidth(), Measure.units(5f)));



                //LeftSide
                arena.addEntity(decorFactory.wallBag(Measure.units(5f), Measure.units(0), Measure.units(35f), Measure.units(10f)));
                arena.addEntity(decorFactory.wallBag(Measure.units(35), Measure.units(10f), Measure.units(5f), Measure.units(40f)));
                arena.addEntity(decorFactory.wallBag(Measure.units(40f), Measure.units(45f), Measure.units(35f), Measure.units(5f)));
                arena.addEntity(decorFactory.grapplePointBag(Measure.units(20f), Measure.units(50f)));

                //arena.addEntity(decorFactory.grapplePointBag(Measure.units()));


                //RightSide
                arena.addEntity(decorFactory.wallBag(arena.getWidth() - Measure.units(40f), Measure.units(0), Measure.units(35f), Measure.units(10f)));
                arena.addEntity(decorFactory.wallBag(arena.getWidth() - Measure.units(40f), Measure.units(10f), Measure.units(5f), Measure.units(40f)));
                arena.addEntity(decorFactory.wallBag(arena.getWidth() - Measure.units(75f), Measure.units(45f), Measure.units(35f), Measure.units(5f)));
                arena.addEntity(decorFactory.grapplePointBag(arena.getWidth() - Measure.units(20f), Measure.units(50f)));



                boolean leftHasMines = random.nextBoolean();

                BombFactory bf = new BombFactory(assetManager);

                //Center
                arena.addEntity(decorFactory.wallBag(Measure.units(50f), Measure.units(0f), Measure.units(20f), Measure.units(10f)));
                if(leftHasMines) {
                    arena.addEntity(bf.mine(Measure.units(50f), Measure.units(10f), 0));
                    arena.addEntity(bf.mine(Measure.units(65f), Measure.units(10f), 0));
                }
                arena.addEntity(chestFactory.chestBag(Measure.units(55f), Measure.units(10f)));
                arena.addEntity(decorFactory.wallBag(arena.getWidth() - Measure.units(70f), Measure.units(0f), Measure.units(20f), Measure.units(10f)));
                if(!leftHasMines) {
                    arena.addEntity(bf.mine(arena.getWidth() - Measure.units(70f), Measure.units(10f), 0));
                    arena.addEntity(bf.mine(arena.getWidth() - Measure.units(55f), Measure.units(10f), 0));
                }
                arena.addEntity(chestFactory.chestBag(arena.getWidth() - Measure.units(65f), Measure.units(10f)));


                arena.addEntity(bf.seaMine(Measure.units(55f), Measure.units(20f), random.nextBoolean(), random.nextBoolean()));
                arena.addEntity(bf.seaMine(arena.getWidth() - Measure.units(55f), Measure.units(20f), random.nextBoolean(), random.nextBoolean()));


                arena.addEntity(decorFactory.grapplePointBag(arena.getWidth() / 2, Measure.units(40f)));
                arena.addEntity(decorFactory.spikeWall(Measure.units(30f), Measure.units(0f),arena.getWidth(), Measure.units(5f), 0));


                //TODO add if neccessary
                //arena.addEntity(arenaEnemyPlacementFactory.spawnFixedTriSentry(arena.getWidth() / 2, Measure.units(75f)));

                return arena;
            }
        };
    }













}
