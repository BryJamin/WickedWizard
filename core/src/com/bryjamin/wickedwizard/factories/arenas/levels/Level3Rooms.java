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
import com.bryjamin.wickedwizard.factories.enemy.TurretFactory;
import com.bryjamin.wickedwizard.utils.Measure;

import java.util.Random;

/**
 * Created by Home on 16/06/2017.
 */

public class Level3Rooms extends AbstractFactory implements ArenaRepostiory {

    private com.bryjamin.wickedwizard.factories.arenas.decor.ArenaShellFactory arenaShellFactory;
    private ChestFactory chestFactory;
    private com.bryjamin.wickedwizard.factories.arenas.decor.DecorFactory decorFactory;
    private ArenaEnemyPlacementFactory arenaEnemyPlacementFactory;
    private TurretFactory turretFactory;
    private com.bryjamin.wickedwizard.factories.arenas.decor.BeamTurretFactory beamTurretFactory;
    private Random random;
    private ArenaSkin arenaSkin;

    public Level3Rooms(AssetManager assetManager, ArenaSkin arenaSkin, Random random) {
        super(assetManager);
        this.arenaShellFactory = new com.bryjamin.wickedwizard.factories.arenas.decor.ArenaShellFactory(assetManager, arenaSkin);
        this.chestFactory = new ChestFactory(assetManager);
        this.arenaEnemyPlacementFactory = new ArenaEnemyPlacementFactory(assetManager, arenaSkin, random);
        this.decorFactory = new com.bryjamin.wickedwizard.factories.arenas.decor.DecorFactory(assetManager, arenaSkin);
        this.turretFactory = new TurretFactory(assetManager);
        this.beamTurretFactory = new com.bryjamin.wickedwizard.factories.arenas.decor.BeamTurretFactory(assetManager, arenaSkin);
        this.random = random;
        this.arenaSkin = arenaSkin;
    }


    public Array<com.bryjamin.wickedwizard.factories.arenas.ArenaCreate> getLevel3RoomArray() {

        Array<com.bryjamin.wickedwizard.factories.arenas.ArenaCreate> ag = new Array<com.bryjamin.wickedwizard.factories.arenas.ArenaCreate>();

        ag.insert(0, room1MultiShot());
        ag.insert(1, room2FlyBy());
        ag.insert(2, room3LaserKugel()); //Here
        ag.insert(3, room4MineRun());
        ag.insert(4, room5Modon()); //Update modon designnever use maces again
        ag.insert(5, room6WidthTwoTwoModonOneTri()); //Reached here
        ag.insert(6, room7DoubleFlyBy());
        ag.insert(7, room8Width3CenterTreaureTrapTriAndFlyBy());
        ag.insert(8, room9Width3LaserCenterThing());
        ag.insert(9, room10AmoebaSpawns());
        ag.insert(10,room11Height3TripMine());
        ag.insert(11, room12MinesTriSentryAndBouncers());
        ag.insert(12,room13FlyByMinesAndBouncers());
        ag.insert(13, room14TrapRoomTriAndTwoBouncers());
        ag.insert(14, room15TreasureTrapRoomWithPotentialOfFiveChests()); //Nerfed it has the potential for 5 but not all the spawnTime
        ag.insert(15,room16Width2RedBlobs());
        ag.insert(16,room17SpikesOnBothSideMultiArenaWithTreasure()); //Up for debate
        ag.insert(17,room18LeftToRightMineGauntlet());
        ag.insert(18,room19TwoTreasureAndTriTurretORFlyByTurret());
        ag.insert(19,room20GoatWizardTwoBouncers());
        ag.insert(20,room21GoatWizardsStompersAndSmallBlobs());
        ag.insert(21,room22HorizontalThroughRoomLaserCenter());
        ag.insert(22,room23VerticalMovingTurretsAndAGapInTheMiddleToHide());
        ag.insert(23,room24MultiWaveRoomWithMines());
        ag.insert(24,room25Width3WithArenaInTheCenter());
        ag.insert(25,room26Height3GrapplesAndMines());
        ag.insert(26,room27DBlobs());
        ag.insert(27,room28SilverHeadAndAmoebas());
        ag.insert(28,room29JigAndTurrets());
        ag.insert(29,room30LargeRoomWithTreasureInTheCenter());

        return ag;
    }

    @Override
    public Array<com.bryjamin.wickedwizard.factories.arenas.ArenaCreate> getAllArenas() {
        return  getLevel3RoomArray();
    }


    public com.bryjamin.wickedwizard.factories.arenas.ArenaCreate room1MultiShot() {
        return new com.bryjamin.wickedwizard.factories.arenas.ArenaCreate() {
            @Override
            public Arena createArena(com.bryjamin.wickedwizard.utils.MapCoords defaultCoords) {

                Arena arena = arenaShellFactory.createOmniArenaHiddenGrapple(defaultCoords, Arena.ArenaType.TRAP);
                arena.addEntity(arenaEnemyPlacementFactory.spawnMovingTriSentry(arena.getWidth() / 2, Measure.units(45f)));
                return arena;
            }
        };
    }

    public com.bryjamin.wickedwizard.factories.arenas.ArenaCreate room2FlyBy() {
        return new com.bryjamin.wickedwizard.factories.arenas.ArenaCreate() {
            @Override
            public Arena createArena(com.bryjamin.wickedwizard.utils.MapCoords defaultCoords) {
                Arena arena = arenaShellFactory.createOmniArenaHiddenGrapple(defaultCoords, Arena.ArenaType.TRAP);
                arena.addEntity(arenaEnemyPlacementFactory.spawnMovingFlyByBombSentry(arena.getWidth() / 2, Measure.units(45f)));
                return arena;
            }
        };
    }

    public com.bryjamin.wickedwizard.factories.arenas.ArenaCreate room3LaserKugel() {
        return new com.bryjamin.wickedwizard.factories.arenas.ArenaCreate() {
            @Override
            public Arena createArena(com.bryjamin.wickedwizard.utils.MapCoords defaultCoords) {
                Arena arena = arenaShellFactory.createOmniArenaHiddenGrapple(defaultCoords, Arena.ArenaType.TRAP);
                arena.addEntity(arenaEnemyPlacementFactory.spawnLaserKugel(arena.getWidth() / 2, arena.getHeight() / 2, random.nextBoolean()));
                return arena;
            }
        };
    }


    public com.bryjamin.wickedwizard.factories.arenas.ArenaCreate room4MineRun() {
        return new com.bryjamin.wickedwizard.factories.arenas.ArenaCreate() {
            @Override
            public Arena createArena(com.bryjamin.wickedwizard.utils.MapCoords defaultCoords) {
                Arena arena = new ArenaBuilder(assetManager, arenaSkin, Arena.ArenaType.NORMAL)
                        .addSection(new ArenaBuilder.Section(defaultCoords,
                                ArenaBuilder.wall.FULL,
                                ArenaBuilder.wall.FULL,
                                ArenaBuilder.wall.MANDATORYDOOR,
                                ArenaBuilder.wall.MANDATORYDOOR))
                        .buildArena();


                arena.addEntity(decorFactory.wallBag(Measure.units(60f), Measure.units(10f), Measure.units(35f), Measure.units(15f)));
                arena.addEntity(decorFactory.wallBag(Measure.units(25f), Measure.units(20f), Measure.units(35f), Measure.units(5)));
                arena.addEntity(decorFactory.wallBag(Measure.units(5f), Measure.units(35f), Measure.units(70f), Measure.units(5)));


                //arena.addEntity(arenaEnemyPlacementFactory.spawnLaserKugel(arena.getWidth() / 2, arena.getHeight() / 2, random.nextBoolean()));

                com.bryjamin.wickedwizard.factories.BombFactory bf = new com.bryjamin.wickedwizard.factories.BombFactory(assetManager);
                for(int i = 0; i < 9; i++) arena.addEntity(bf.mine(Measure.units(27.5f + (i * 7.5f)), Measure.units(25f), 0));

                arena.addEntity(bf.mine(Measure.units(12.5f), Measure.units(10f), 0));
                arena.addEntity(bf.mine(Measure.units(20f), Measure.units(10f), 0));

                arena.addEntity(bf.mine(Measure.units(62.5f), Measure.units(40f), 0));
                arena.addEntity(bf.mine(Measure.units(70f), Measure.units(40f), 0));

                return arena;
            }
        };
    }


    public com.bryjamin.wickedwizard.factories.arenas.ArenaCreate room5Modon() {
        return new com.bryjamin.wickedwizard.factories.arenas.ArenaCreate() {
            @Override
            public Arena createArena(com.bryjamin.wickedwizard.utils.MapCoords defaultCoords) {
                Arena arena = arenaShellFactory.createOmniArenaHiddenGrapple(defaultCoords, Arena.ArenaType.TRAP);
                arena.addEntity(arenaEnemyPlacementFactory.spawnModon(arena.getWidth() / 2, arena.getHeight() / 2));
                return arena;
            }
        };
    }

    public com.bryjamin.wickedwizard.factories.arenas.ArenaCreate room6WidthTwoTwoModonOneTri() {
        return new com.bryjamin.wickedwizard.factories.arenas.ArenaCreate() {
            @Override
            public Arena createArena(com.bryjamin.wickedwizard.utils.MapCoords defaultCoords) {

                Arena arena = new ArenaBuilder(assetManager, arenaSkin, Arena.ArenaType.TRAP)
                        .addSection(new ArenaBuilder.Section(defaultCoords,
                                ArenaBuilder.wall.DOOR,
                                ArenaBuilder.wall.NONE,
                                ArenaBuilder.wall.GRAPPLE,
                                ArenaBuilder.wall.DOOR))
                        .addSection(new ArenaBuilder.Section(new com.bryjamin.wickedwizard.utils.MapCoords(defaultCoords.getX() + 1, defaultCoords.getY()),
                                ArenaBuilder.wall.NONE,
                                ArenaBuilder.wall.DOOR,
                                ArenaBuilder.wall.GRAPPLE,
                                ArenaBuilder.wall.DOOR))
                        .buildArena();

                arena.addEntity(arenaEnemyPlacementFactory.spawnModon(arena.getWidth() / 4, arena.getHeight() / 2));
                arena.addEntity(arenaEnemyPlacementFactory.spawnModon(arena.getWidth() / 4 * 3, arena.getHeight() / 2));
                arena.addEntity(arenaEnemyPlacementFactory.spawnMovingTriSentry(arena.getWidth() / 2, Measure.units(45f)));

                return arena;
            }
        };
    }


    public com.bryjamin.wickedwizard.factories.arenas.ArenaCreate room7DoubleFlyBy() {
        return new com.bryjamin.wickedwizard.factories.arenas.ArenaCreate() {
            @Override
            public Arena createArena(com.bryjamin.wickedwizard.utils.MapCoords defaultCoords) {
                Arena arena = arenaShellFactory.createOmniArenaHiddenGrapple(defaultCoords, Arena.ArenaType.TRAP);

                arena.addEntity(decorFactory.wallBag(Measure.units(20f), Measure.units(30f), Measure.units(10), Measure.units(5f)));

                arena.addEntity(decorFactory.wallBag(arena.getWidth() - Measure.units(30f), Measure.units(30f), Measure.units(10), Measure.units(5f)));

                arena.addEntity(arenaEnemyPlacementFactory.spawnMovingFlyByBombSentry(arena.getWidth() / 4, Measure.units(45f)));
                arena.addEntity(arenaEnemyPlacementFactory.spawnMovingFlyByBombSentry(arena.getWidth() / 4 * 3, Measure.units(45f)));

                return arena;
            }
        };
    }



    public com.bryjamin.wickedwizard.factories.arenas.ArenaCreate room8Width3CenterTreaureTrapTriAndFlyBy() {
        return new com.bryjamin.wickedwizard.factories.arenas.ArenaCreate() {
            @Override
            public Arena createArena(com.bryjamin.wickedwizard.utils.MapCoords defaultCoords) {

                Arena arena = new ArenaBuilder(assetManager, arenaSkin, Arena.ArenaType.NORMAL)
                        .addSection(new ArenaBuilder.Section(defaultCoords,
                                ArenaBuilder.wall.MANDATORYDOOR,
                                ArenaBuilder.wall.NONE,
                                ArenaBuilder.wall.GRAPPLE,
                                ArenaBuilder.wall.NONE))
                        .addSection(new ArenaBuilder.Section(new com.bryjamin.wickedwizard.utils.MapCoords(defaultCoords.getX() + 1, defaultCoords.getY()),
                                ArenaBuilder.wall.NONE,
                                ArenaBuilder.wall.NONE,
                                ArenaBuilder.wall.FULL,
                                ArenaBuilder.wall.FULL))
                        .addSection(new ArenaBuilder.Section(new com.bryjamin.wickedwizard.utils.MapCoords(defaultCoords.getX() + 2, defaultCoords.getY()),
                                ArenaBuilder.wall.NONE,
                                ArenaBuilder.wall.MANDATORYDOOR,
                                ArenaBuilder.wall.GRAPPLE,
                                ArenaBuilder.wall.NONE))
                        .buildArena();



                //arena.addEntity(decorFactory.wallBag(Measure.units(20f), Measure.units(25f), Measure.units(15f), Measure.units(5f)));
                arena.addEntity(decorFactory.outOfCombatPlatform(Measure.units(5f), Measure.units(5f), arena.getWidth()));
                arena.addEntity(decorFactory.wallBag(Measure.units(5f), Measure.units(-5f), arena.getWidth(), Measure.units(5f)));
                arena.addEntity(decorFactory.spikeWall(Measure.units(5f), Measure.units(0), arena.getWidth(), Measure.units(5f), 0));

                arena.addEntity(decorFactory.wallBag(Measure.units(100f), Measure.units(45f), Measure.units(5f), Measure.units(10f)));
                arena.addEntity(decorFactory.wallBag(Measure.units(195f), Measure.units(45f), Measure.units(5f), Measure.units(10f)));

                arena.addEntity(decorFactory.appearInCombatWallPush(Measure.units(100f), Measure.units(10f), Measure.units(5f), Measure.units(35f), 0));
                arena.addEntity(decorFactory.appearInCombatWallPush(Measure.units(195f), Measure.units(10f), Measure.units(5f), Measure.units(35f), 180));


                arena.addWave(arenaEnemyPlacementFactory.spawnModon(arena.getWidth() / 2, Measure.units(45f)),
                        arenaEnemyPlacementFactory.spawnFixedTriSentry(arena.getWidth() / 2, Measure.units(45f)));

                arena.addWave(arenaEnemyPlacementFactory.spawnMovingTriSentry(Measure.units(120), Measure.units(45f)),
                        arenaEnemyPlacementFactory.spawnMovingSentry(arena.getWidth() - Measure.units(120), Measure.units(45f)));



                arena.addEntity(chestFactory.chestBag(Measure.units(140f), Measure.units(10f), chestFactory.trapODAC()));
                arena.addEntity(chestFactory.chestBag(Measure.units(155f), Measure.units(10f), chestFactory.trapODAC()));

                return arena;
            }
        };
    }


    public com.bryjamin.wickedwizard.factories.arenas.ArenaCreate room9Width3LaserCenterThing() {
        return new com.bryjamin.wickedwizard.factories.arenas.ArenaCreate() {
            @Override
            public Arena createArena(com.bryjamin.wickedwizard.utils.MapCoords defaultCoords) {

                Arena arena = new ArenaBuilder(assetManager, arenaSkin, Arena.ArenaType.NORMAL)
                        .addSection(new ArenaBuilder.Section(defaultCoords,
                                ArenaBuilder.wall.MANDATORYDOOR,
                                ArenaBuilder.wall.NONE,
                                ArenaBuilder.wall.GRAPPLE,
                                ArenaBuilder.wall.DOOR))
                        .addSection(new ArenaBuilder.Section(new com.bryjamin.wickedwizard.utils.MapCoords(defaultCoords.getX() + 1, defaultCoords.getY()),
                                ArenaBuilder.wall.NONE,
                                ArenaBuilder.wall.NONE,
                                ArenaBuilder.wall.FULL,
                                ArenaBuilder.wall.FULL))
                        .addSection(new ArenaBuilder.Section(new com.bryjamin.wickedwizard.utils.MapCoords(defaultCoords.getX() + 2, defaultCoords.getY()),
                                ArenaBuilder.wall.NONE,
                                ArenaBuilder.wall.MANDATORYDOOR,
                                ArenaBuilder.wall.GRAPPLE,
                                ArenaBuilder.wall.DOOR))
                        .buildArena();



                arena.addEntity(decorFactory.wallBag(Measure.units(100f), Measure.units(45f), Measure.units(5f), Measure.units(10f)));
                arena.addEntity(decorFactory.wallBag(Measure.units(195f), Measure.units(45f), Measure.units(5f), Measure.units(10f)));
                arena.addEntity(decorFactory.wallBag(Measure.units(100f), Measure.units(10f), Measure.units(5f), Measure.units(10f)));
                arena.addEntity(decorFactory.wallBag(Measure.units(195f), Measure.units(10f), Measure.units(5f), Measure.units(10f)));


                arena.addEntity(beamTurretFactory.laserChain(
                        Measure.units(145f),
                        Measure.units(25f),
                        2,
                        new com.bryjamin.wickedwizard.factories.weapons.enemy.LaserOrbitalTask.LaserBuilder(assetManager)
                        .orbitalAndIntervalSize(Measure.units(5f))
                        .speedInDegrees(random.nextBoolean() ? 1f : -1f)
                        .numberOfOrbitals(10)
                        .chargeTime(0)
                        .angles(0, 180).build()));

                if(random.nextBoolean()) arena.addEntity(chestFactory.chestBag(Measure.units(145f), Measure.units(10f)));


                return arena;
            }
        };
    }


    public com.bryjamin.wickedwizard.factories.arenas.ArenaCreate room10AmoebaSpawns() {
        return new com.bryjamin.wickedwizard.factories.arenas.ArenaCreate() {
            @Override
            public Arena createArena(com.bryjamin.wickedwizard.utils.MapCoords defaultCoords) {

                Arena arena = arenaShellFactory.createOmniArenaHiddenGrapple(defaultCoords, Arena.ArenaType.TRAP);

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


    public com.bryjamin.wickedwizard.factories.arenas.ArenaCreate room11Height3TripMine() {
        return new com.bryjamin.wickedwizard.factories.arenas.ArenaCreate() {
            @Override
            public Arena createArena(com.bryjamin.wickedwizard.utils.MapCoords defaultCoords) {

                Arena arena = new ArenaBuilder(assetManager, arenaSkin, Arena.ArenaType.NORMAL)
                        .addSection(new ArenaBuilder.Section(defaultCoords,
                                ArenaBuilder.wall.DOOR,
                                ArenaBuilder.wall.DOOR,
                                ArenaBuilder.wall.NONE,
                                ArenaBuilder.wall.MANDATORYDOOR))
                        .addSection(new ArenaBuilder.Section(new com.bryjamin.wickedwizard.utils.MapCoords(defaultCoords.getX(), defaultCoords.getY() + 1),
                                ArenaBuilder.wall.FULL,
                                ArenaBuilder.wall.FULL,
                                ArenaBuilder.wall.NONE,
                                ArenaBuilder.wall.NONE))
                        .addSection(new ArenaBuilder.Section(new com.bryjamin.wickedwizard.utils.MapCoords(defaultCoords.getX(), defaultCoords.getY() + 2),
                                ArenaBuilder.wall.DOOR,
                                ArenaBuilder.wall.DOOR,
                                ArenaBuilder.wall.MANDATORYGRAPPLE,
                                ArenaBuilder.wall.NONE))
                        .buildArena();


                arena.addEntity(decorFactory.wallBag(Measure.units(40f), Measure.units(30f), Measure.units(20f), Measure.units(5f)));
                arena.addEntity(decorFactory.wallBag(Measure.units(15f), Measure.units(50f), Measure.units(20f), Measure.units(5f)));
                arena.addEntity(decorFactory.wallBag(Measure.units(65f), Measure.units(50f), Measure.units(20f), Measure.units(5f)));


                arena.addEntity(decorFactory.grapplePointBag(arena.getWidth() / 2, Measure.units(70f)));
                arena.addEntity(decorFactory.wallBag(Measure.units(15f), Measure.units(90f), Measure.units(20f), Measure.units(5f)));
                arena.addEntity(decorFactory.wallBag(Measure.units(65f), Measure.units(90f), Measure.units(20f), Measure.units(5f)));

                arena.addEntity(decorFactory.grapplePointBag(arena.getWidth() / 2, Measure.units(110f)));


                arena.addEntity(decorFactory.wallBag(Measure.units(5f), Measure.units(125f), Measure.units(20f), Measure.units(5f)));
                arena.addEntity(decorFactory.wallBag(arena.getWidth() - Measure.units(25f), Measure.units(125f), Measure.units(20f), Measure.units(5f)));


                com.bryjamin.wickedwizard.factories.BombFactory bf = new com.bryjamin.wickedwizard.factories.BombFactory(assetManager);

                arena.addEntity(bf.multiDirectionalSeaMine(Measure.units(20f), Measure.units(30f), random.nextBoolean(), random.nextBoolean()));
                arena.addEntity(bf.multiDirectionalSeaMine(arena.getWidth() - Measure.units(20f), Measure.units(30f), random.nextBoolean(), random.nextBoolean()));
                arena.addEntity(bf.multiDirectionalSeaMine(Measure.units(50f), arena.getHeight() / 2, random.nextBoolean(), random.nextBoolean()));
                arena.addEntity(bf.multiDirectionalSeaMine(Measure.units(20f), arena.getHeight() / 2, random.nextBoolean(), random.nextBoolean()));

                arena.addEntity(bf.multiDirectionalSeaMine(Measure.units(20f), Measure.units(100f), random.nextBoolean(), random.nextBoolean()));
                arena.addEntity(bf.multiDirectionalSeaMine(arena.getWidth() / 4 , Measure.units(140f), random.nextBoolean(), random.nextBoolean()));
                arena.addEntity(bf.multiDirectionalSeaMine(arena.getWidth() / 4 * 3, Measure.units(140f), random.nextBoolean(), random.nextBoolean()));

                return arena;
            }
        };
    }


    //TODO too easy?
    public com.bryjamin.wickedwizard.factories.arenas.ArenaCreate room12MinesTriSentryAndBouncers() {
        return new com.bryjamin.wickedwizard.factories.arenas.ArenaCreate() {
            @Override
            public Arena createArena(com.bryjamin.wickedwizard.utils.MapCoords defaultCoords) {

                Arena arena = arenaShellFactory.createOmniArenaHiddenGrapple(defaultCoords, Arena.ArenaType.TRAP);

                com.bryjamin.wickedwizard.factories.BombFactory bf = new com.bryjamin.wickedwizard.factories.BombFactory(assetManager);
                arena.addEntity(bf.multiDirectionalSeaMine(Measure.units(20f), Measure.units(25f), random.nextBoolean(), random.nextBoolean()));
                arena.addEntity(bf.multiDirectionalSeaMine(arena.getWidth() - Measure.units(20f), Measure.units(25f), random.nextBoolean(), random.nextBoolean()));

                arena.addEntity(arenaEnemyPlacementFactory.spawnFixedTriSentry(arena.getWidth() / 2, Measure.units(45f)));
                arena.addEntity(arenaEnemyPlacementFactory.spawnBouncer(arena.getWidth() / 2, arena.getHeight() / 2));

                return arena;
            }
        };
    }


    public com.bryjamin.wickedwizard.factories.arenas.ArenaCreate room13FlyByMinesAndBouncers() {
        return new com.bryjamin.wickedwizard.factories.arenas.ArenaCreate() {
            @Override
            public Arena createArena(com.bryjamin.wickedwizard.utils.MapCoords defaultCoords) {

                Arena arena = arenaShellFactory.createOmniArenaHiddenGrapple(defaultCoords, Arena.ArenaType.TRAP);

                arena.addEntity(decorFactory.wallBag(Measure.units(25f), Measure.units(30f), Measure.units(5f), Measure.units(5f)));
                arena.addEntity(decorFactory.wallBag(arena.getWidth() - Measure.units(30f), Measure.units(30f), Measure.units(5f), Measure.units(5f)));


                com.bryjamin.wickedwizard.factories.BombFactory bf = new com.bryjamin.wickedwizard.factories.BombFactory(assetManager);
                arena.addEntity(bf.multiDirectionalSeaMine(Measure.units(20f), Measure.units(40f), random.nextBoolean(), random.nextBoolean()));
                arena.addEntity(bf.multiDirectionalSeaMine(arena.getWidth() - Measure.units(20f), Measure.units(40f), random.nextBoolean(), random.nextBoolean()));

                arena.addEntity(arenaEnemyPlacementFactory.spawnFixedFlyByBombSentry(arena.getWidth() / 2, Measure.units(45f)));
                arena.addEntity(arenaEnemyPlacementFactory.spawnBouncer(arena.getWidth() / 4 * 3, Measure.units(45f)));
                arena.addEntity(arenaEnemyPlacementFactory.spawnBouncer(arena.getWidth() / 4, Measure.units(45f)));

                return arena;
            }
        };
    }



    //TODO change to 2?
    public com.bryjamin.wickedwizard.factories.arenas.ArenaCreate room14TrapRoomTriAndTwoBouncers() {
        return new com.bryjamin.wickedwizard.factories.arenas.ArenaCreate() {
            @Override
            public Arena createArena(com.bryjamin.wickedwizard.utils.MapCoords defaultCoords) {

                Arena arena = arenaShellFactory.createOmniArenaHiddenGrapple(defaultCoords, Arena.ArenaType.NORMAL);
                arena.addEntity(chestFactory.chestBag(Measure.units(45f), Measure.units(10f), chestFactory.trapODAC()));
                arena.addWave(arenaEnemyPlacementFactory.spawnBouncer(Measure.units(15), Measure.units(50f)),
                        arenaEnemyPlacementFactory.spawnFixedTriSentry(arena.getWidth() / 2, arena.getHeight() / 2),
                        arenaEnemyPlacementFactory.spawnBouncer(arena.getWidth() - Measure.units(15), Measure.units(50f)));


                return arena;
            }
        };
    }



    public com.bryjamin.wickedwizard.factories.arenas.ArenaCreate room15TreasureTrapRoomWithPotentialOfFiveChests() {
        return new com.bryjamin.wickedwizard.factories.arenas.ArenaCreate() {
            @Override
            public Arena createArena(com.bryjamin.wickedwizard.utils.MapCoords defaultCoords) {

                Arena arena = arenaShellFactory.createOmniArenaHiddenGrapple(defaultCoords, Arena.ArenaType.NORMAL);

                boolean variation = random.nextBoolean();

                //Right
                if(variation) {
                    arena.addEntity(decorFactory.platform(arena.getWidth() - Measure.units(35f), Measure.units(26f), Measure.units(30f)));
                    arena.addEntity(chestFactory.chestBag(Measure.units(67.5f), Measure.units(30f)));
                    arena.addEntity(chestFactory.chestBag(Measure.units(82.5f), Measure.units(30f)));

                    arena.addEntity(arenaEnemyPlacementFactory.hoarderFactory.hoarder(Measure.units(20f), Measure.units(25f)));

                } else {
                    arena.addEntity(arenaEnemyPlacementFactory.turretFactory.fixedFlyByBombSentry(arena.getWidth() - Measure.units(20f), Measure.units(45f)));
                }
                arena.addEntity(decorFactory.wallBag(arena.getWidth() - Measure.units(40f), Measure.units(30f), Measure.units(5f), Measure.units(25f)));


                //Left
                if(!variation) {
                    arena.addEntity(decorFactory.platform(Measure.units(5), Measure.units(26f), Measure.units(30f)));
                    arena.addEntity(chestFactory.chestBag(Measure.units(7.5f), Measure.units(30f)));
                    arena.addEntity(chestFactory.chestBag(Measure.units(22.5f), Measure.units(30f)));

                    arena.addEntity(arenaEnemyPlacementFactory.hoarderFactory.hoarder(arena.getWidth() - Measure.units(20f), Measure.units(25f)));

                } else {
                    arena.addEntity(arenaEnemyPlacementFactory.turretFactory.fixedFlyByBombSentry(Measure.units(20f), Measure.units(45f)));
                }
                arena.addEntity(decorFactory.wallBag(Measure.units(35f), Measure.units(30f), Measure.units(5f), Measure.units(25f)));


                return arena;
            }
        };
    }


    public com.bryjamin.wickedwizard.factories.arenas.ArenaCreate room16Width2RedBlobs() {
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
                arena.addEntity(decorFactory.wallBag(0, Measure.units(30f), Measure.units(75f), Measure.units(5f),arenaSkin));

                //RIGHT
                arena.addEntity(decorFactory.wallBag(arena.getWidth() - Measure.units(75f), Measure.units(30f), Measure.units(75f), Measure.units(5f),arenaSkin));

                arena.addEntity(decorFactory.platform(Measure.units(75f), Measure.units(30f), Measure.units(50f)));

                arena.addEntity(arenaEnemyPlacementFactory.spawnAngryBlob(arena.getWidth() / 2 , Measure.units(45f)));

                arena.addEntity(arenaEnemyPlacementFactory.spawnAngryBlob(Measure.units(20f) , Measure.units(20f)));

                arena.addEntity(arenaEnemyPlacementFactory.spawnAngryBlob(arena.getWidth() - Measure.units(20f) , Measure.units(20f)));

                return arena;
            }
        };
    }


    public com.bryjamin.wickedwizard.factories.arenas.ArenaCreate room17SpikesOnBothSideMultiArenaWithTreasure() {
        return new com.bryjamin.wickedwizard.factories.arenas.ArenaCreate() {
            @Override
            public Arena createArena(com.bryjamin.wickedwizard.utils.MapCoords defaultCoords) {

                Arena arena =  new ArenaBuilder(assetManager, arenaSkin, Arena.ArenaType.TRAP)
                        .addSection(new ArenaBuilder.Section(defaultCoords,
                                ArenaBuilder.wall.FULL,
                                ArenaBuilder.wall.FULL,
                                ArenaBuilder.wall.MANDATORYGRAPPLE,
                                ArenaBuilder.wall.MANDATORYDOOR))
                        .buildArena();

                arena.addEntity(decorFactory.spikeWall(Measure.units(5f), Measure.units(10f), Measure.units(5f), Measure.units(50f), 270));
                arena.addEntity(decorFactory.spikeWall(Measure.units(90f), Measure.units(10f), Measure.units(5f), Measure.units(50f), 90));

                arena.addWave(arenaEnemyPlacementFactory.spawnMovingTriSentry(arena.getWidth() / 2, Measure.units(45f)));

                arena.addWave(arenaEnemyPlacementFactory.spawnGoatWizard(arena.getWidth() / 2, Measure.units(45f)));

                arena.addWave(arenaEnemyPlacementFactory.spawnerFactory.spawnerBag(arena.getWidth() / 2, arena.getHeight() / 2,
                        new SpawnerFactory.Spawner() {
                            public Bag<Component> spawnBag(float x, float y) {
                                return chestFactory.centeredChestBag(x,y);
                            }
                        }));

                com.bryjamin.wickedwizard.factories.BombFactory bf = new com.bryjamin.wickedwizard.factories.BombFactory(assetManager);

                arena.addEntity(bf.multiDirectionalSeaMine(arena.getWidth() / 4, arena.getHeight() / 2, random.nextBoolean(), random.nextBoolean()));
                arena.addEntity(bf.multiDirectionalSeaMine(arena.getWidth() / 4 * 3, arena.getHeight() / 2, random.nextBoolean(), random.nextBoolean()));

                return arena;
            }
        };
    }


    //TODO make a mirror version?
    public com.bryjamin.wickedwizard.factories.arenas.ArenaCreate room18LeftToRightMineGauntlet() {
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


                arena.addEntity(decorFactory.wallBag(Measure.units(25f), Measure.units(10f), Measure.units(20f), Measure.units(35f)));
                arena.addEntity(decorFactory.wallBag(Measure.units(55f), Measure.units(20f), Measure.units(20f), Measure.units(35f)));

                com.bryjamin.wickedwizard.factories.BombFactory bf = new com.bryjamin.wickedwizard.factories.BombFactory(assetManager);

                for(int i = 0; i < 4; i++) arena.addEntity(bf.mine(Measure.units(47.5f + (i * 7.5f)), Measure.units(10f), 0));

                for(int i = 0; i < 5; i++) arena.addEntity(bf.mine(Measure.units(45f), Measure.units(10f + (i * 7.5f)), -90));
                for(int i = 0; i < 5; i++) arena.addEntity(bf.mine(Measure.units(50f), Measure.units(20f + (i * 7.5f)), 90));

                for(int i = 0; i < 3; i++) arena.addEntity(bf.mine(Measure.units(25f + (i * 7.5f)), Measure.units(45f), 0));





                return arena;
            }
        };
    }


    public com.bryjamin.wickedwizard.factories.arenas.ArenaCreate room19TwoTreasureAndTriTurretORFlyByTurret() {

        return new com.bryjamin.wickedwizard.factories.arenas.ArenaCreate() {
            @Override
            public Arena createArena(com.bryjamin.wickedwizard.utils.MapCoords defaultCoords) {

                Arena arena = arenaShellFactory.createOmniArenaHiddenGrapple(defaultCoords, Arena.ArenaType.NORMAL);

                boolean isTri = random.nextBoolean();
                boolean isTreasureOnLeft = random.nextBoolean();

                arena.addEntity(arenaEnemyPlacementFactory.hoarderFactory.hoarder(
                        isTreasureOnLeft ? Measure.units(20f) : arena.getWidth() - Measure.units(20f),
                        Measure.units(47.5f)));


                arena.addEntity(decorFactory.wallBag(isTreasureOnLeft ? Measure.units(5f) : arena.getWidth() - Measure.units(35f), Measure.units(30f), Measure.units(30f), Measure.units(5)));
                arena.addEntity(chestFactory.chestBag(isTreasureOnLeft ? Measure.units(7.5f) : arena.getWidth() - Measure.units(17.5f), Measure.units(35f)));
                arena.addEntity(chestFactory.chestBag(isTreasureOnLeft ? Measure.units(22.5f) : arena.getWidth() - Measure.units(32.5f), Measure.units(35f)));
                arena.addEntity(isTri ? turretFactory.fixedMultiSentry(isTreasureOnLeft ? Measure.units(85f) : arena.getWidth() - Measure.units(85f), Measure.units(45f)) :
                        turretFactory.fixedFlyByBombSentry(isTreasureOnLeft ? Measure.units(85f) : arena.getWidth() - Measure.units(85f), Measure.units(45f)));


                return arena;
            }
        };
    }


    public com.bryjamin.wickedwizard.factories.arenas.ArenaCreate room20GoatWizardTwoBouncers() {

        return new com.bryjamin.wickedwizard.factories.arenas.ArenaCreate() {
            @Override
            public Arena createArena(com.bryjamin.wickedwizard.utils.MapCoords defaultCoords) {

                Arena arena = arenaShellFactory.createOmniArenaHiddenGrapple(defaultCoords, Arena.ArenaType.TRAP);

                arena.addEntity(arenaEnemyPlacementFactory.spawnGoatWizard(arena.getWidth() / 2, Measure.units(45f)));

                arena.addEntity(arenaEnemyPlacementFactory.spawnBouncer(Measure.units(20f), Measure.units(35f)));
                arena.addEntity(arenaEnemyPlacementFactory.spawnBouncer(arena.getWidth() - Measure.units(20f), Measure.units(35f)));

                arena.addEntity(decorFactory.inCombatfixedWallTurret(Measure.units(20f), Measure.units(50f), 180, 1.5f, 1.5f));
                arena.addEntity(decorFactory.inCombatfixedWallTurret(arena.getWidth() - Measure.units(25f), Measure.units(50f), 180, 1.5f, 1.5f));


                return arena;
            }
        };
    }



    public com.bryjamin.wickedwizard.factories.arenas.ArenaCreate room21GoatWizardsStompersAndSmallBlobs() {

        return new com.bryjamin.wickedwizard.factories.arenas.ArenaCreate() {
            @Override
            public Arena createArena(com.bryjamin.wickedwizard.utils.MapCoords defaultCoords) {

                Arena arena =  new ArenaBuilder(assetManager, arenaSkin, Arena.ArenaType.TRAP)
                        .addSection(new ArenaBuilder.Section(defaultCoords,
                                ArenaBuilder.wall.DOOR,
                                ArenaBuilder.wall.NONE,
                                ArenaBuilder.wall.GRAPPLE,
                                ArenaBuilder.wall.DOOR))
                        .addSection(new ArenaBuilder.Section(new com.bryjamin.wickedwizard.utils.MapCoords(defaultCoords.getX() + 1, defaultCoords.getY()),
                                ArenaBuilder.wall.NONE,
                                ArenaBuilder.wall.DOOR,
                                ArenaBuilder.wall.GRAPPLE,
                                ArenaBuilder.wall.DOOR))
                        .buildArena();

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


                return arena;
            }
        };
    }




    public com.bryjamin.wickedwizard.factories.arenas.ArenaCreate room22HorizontalThroughRoomLaserCenter() {

        return new com.bryjamin.wickedwizard.factories.arenas.ArenaCreate() {
            @Override
            public Arena createArena(com.bryjamin.wickedwizard.utils.MapCoords defaultCoords) {

                Arena arena =  new ArenaBuilder(assetManager, arenaSkin, Arena.ArenaType.TRAP)
                        .addSection(new ArenaBuilder.Section(defaultCoords,
                                ArenaBuilder.wall.MANDATORYDOOR,
                                ArenaBuilder.wall.MANDATORYDOOR,
                                ArenaBuilder.wall.FULL,
                                ArenaBuilder.wall.FULL))
                        .buildArena();

                for(int i = 0; i < 4; i++) arena.addEntity(decorFactory.inCombatfixedWallTurret(Measure.units(40f + (i * 5f)), Measure.units(50f), 180, 1.5f, 1.5f));

                boolean isLeft = random.nextBoolean();

                arena.addWave(arenaEnemyPlacementFactory.spawnFixedTriSentry(!isLeft ? Measure.units(20f) : arena.getWidth() - Measure.units(20f), Measure.units(45f)));
                arena.addWave(arenaEnemyPlacementFactory.spawnFixedTriSentry(isLeft ? Measure.units(20f) : arena.getWidth() - Measure.units(20f), Measure.units(45f)));

                return arena;
            }
        };
    }







    public com.bryjamin.wickedwizard.factories.arenas.ArenaCreate room23VerticalMovingTurretsAndAGapInTheMiddleToHide() {

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


                arena.addEntity(decorFactory.wallBag(Measure.units(40), Measure.units(20f), Measure.units(20f), Measure.units(5f)));
                arena.addEntity(decorFactory.wallBag(Measure.units(45), Measure.units(25f), Measure.units(10f), Measure.units(15f)));

                boolean bool = random.nextBoolean();

                if(random.nextBoolean()) {
                    arena.addEntity(arenaEnemyPlacementFactory.spawnMovingVerticalTriSentry(Measure.units(20f), arena.getHeight() / 2 + Measure.units(2.5f), bool));
                    arena.addEntity(arenaEnemyPlacementFactory.spawnVertivalMovingFlyByBombSentry(arena.getWidth() - Measure.units(20f), arena.getHeight() / 2 + Measure.units(2.5f), !bool));
                } else {
                    arena.addEntity(arenaEnemyPlacementFactory.spawnVertivalMovingFlyByBombSentry(Measure.units(20f), arena.getHeight() / 2 + Measure.units(2.5f), bool));
                    arena.addEntity(arenaEnemyPlacementFactory.spawnMovingVerticalTriSentry(arena.getWidth() - Measure.units(20f), arena.getHeight() / 2 + Measure.units(2.5f), !bool));
                }


                return arena;
            }
        };
    }



    public com.bryjamin.wickedwizard.factories.arenas.ArenaCreate room24MultiWaveRoomWithMines() {

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

                com.bryjamin.wickedwizard.factories.BombFactory bf = new com.bryjamin.wickedwizard.factories.BombFactory(assetManager);

                arena.addEntity(bf.multiDirectionalSeaMine(arena.getWidth() / 4 * 3, arena.getHeight() / 2, random.nextBoolean(), random.nextBoolean()));
                arena.addEntity(bf.multiDirectionalSeaMine(arena.getWidth() / 4, arena.getHeight() / 2, random.nextBoolean(), random.nextBoolean()));

                arena.addWave(arenaEnemyPlacementFactory.spawnModon(arena.getWidth() / 2, arena.getHeight() / 2 + Measure.units(5f)));

                arena.addWave(bf.fadeInSeaMine(arena.getWidth() / 4 * 3, arena.getHeight() / 2, random.nextBoolean(), random.nextBoolean()),
                        bf.fadeInSeaMine(arena.getWidth() / 4, arena.getHeight() / 2, random.nextBoolean(), random.nextBoolean()),
                        arenaEnemyPlacementFactory.spawnMovingJig(arena.getWidth() / 2, arena.getHeight() / 2 + Measure.units(2.5f)));


                arena.addWave(arenaEnemyPlacementFactory.spawnerFactory.spawnerBag(arena.getWidth() / 2, arena.getHeight() / 2,
                        new SpawnerFactory.Spawner() {
                            public Bag<Component> spawnBag(float x, float y) {
                                return chestFactory.centeredChestBag(x,y);
                            }
                        }));

                return arena;
            }
        };
    }




    public com.bryjamin.wickedwizard.factories.arenas.ArenaCreate room25Width3WithArenaInTheCenter() {

        return new com.bryjamin.wickedwizard.factories.arenas.ArenaCreate() {
            @Override
            public Arena createArena(com.bryjamin.wickedwizard.utils.MapCoords defaultCoords) {

                Arena arena =  new ArenaBuilder(assetManager, arenaSkin, Arena.ArenaType.NORMAL)
                        .addSection(new ArenaBuilder.Section(defaultCoords,
                                ArenaBuilder.wall.MANDATORYDOOR,
                                ArenaBuilder.wall.NONE,
                                ArenaBuilder.wall.FULL,
                                ArenaBuilder.wall.FULL))
                        .addSection(new ArenaBuilder.Section(new com.bryjamin.wickedwizard.utils.MapCoords(defaultCoords.getX() + 1, defaultCoords.getY()),
                                ArenaBuilder.wall.NONE,
                                ArenaBuilder.wall.NONE,
                                ArenaBuilder.wall.GRAPPLE,
                                ArenaBuilder.wall.DOOR))
                        .addSection(new ArenaBuilder.Section(new com.bryjamin.wickedwizard.utils.MapCoords(defaultCoords.getX() + 2, defaultCoords.getY()),
                                ArenaBuilder.wall.NONE,
                                ArenaBuilder.wall.MANDATORYDOOR,
                                ArenaBuilder.wall.FULL,
                                ArenaBuilder.wall.FULL))
                        .buildArena();

                arena.addWave(arenaEnemyPlacementFactory.spawnModon(arena.getWidth() / 2, arena.getHeight() / 2));

                arena.addWave(arenaEnemyPlacementFactory.spawnkugelDusche(arena.getWidth() / 2, Measure.units(32.5f)));


                arena.addWave(arenaEnemyPlacementFactory.spawnAngryBlob(arena.getWidth() / 2, Measure.units(32.5f)),
                        arenaEnemyPlacementFactory.spawnMovingFlyByBombSentry(Measure.units(125f), Measure.units(45f)),
                        arenaEnemyPlacementFactory.spawnMovingFlyByBombSentry(arena.getWidth() - Measure.units(125f), Measure.units(45f)));

                arena.addWave(
                        arenaEnemyPlacementFactory.spawnMovingTriSentry(Measure.units(125f), Measure.units(45f)),
                        arenaEnemyPlacementFactory.spawnMovingTriSentry(arena.getWidth() - Measure.units(125f), Measure.units(45f)));

                arena.shuffleWaves();


                arena.addEntity(chestFactory.chestBag(Measure.units(132.5f), Measure.units(10f), chestFactory.trapODAC()));
                arena.addEntity(chestFactory.chestBag(Measure.units(145f), Measure.units(10f), chestFactory.trapODAC()));
                arena.addEntity(chestFactory.chestBag(Measure.units(157.5f), Measure.units(10f), chestFactory.trapODAC()));

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



    public com.bryjamin.wickedwizard.factories.arenas.ArenaCreate room26Height3GrapplesAndMines() {

        return new com.bryjamin.wickedwizard.factories.arenas.ArenaCreate() {
            @Override
            public Arena createArena(com.bryjamin.wickedwizard.utils.MapCoords defaultCoords) {

                Arena arena =  new ArenaBuilder(assetManager, arenaSkin, Arena.ArenaType.NORMAL)
                        .addSection(new ArenaBuilder.Section(defaultCoords,
                                ArenaBuilder.wall.DOOR,
                                ArenaBuilder.wall.DOOR,
                                ArenaBuilder.wall.NONE,
                                ArenaBuilder.wall.MANDATORYDOOR))
                        .addSection(new ArenaBuilder.Section(new com.bryjamin.wickedwizard.utils.MapCoords(defaultCoords.getX(), defaultCoords.getY() + 1),
                                ArenaBuilder.wall.FULL,
                                ArenaBuilder.wall.FULL,
                                ArenaBuilder.wall.NONE,
                                ArenaBuilder.wall.NONE))
                        .addSection(new ArenaBuilder.Section(new com.bryjamin.wickedwizard.utils.MapCoords(defaultCoords.getX(), defaultCoords.getY() + 2),
                                ArenaBuilder.wall.FULL,
                                ArenaBuilder.wall.FULL,
                                ArenaBuilder.wall.MANDATORYGRAPPLE,
                                ArenaBuilder.wall.NONE))
                        .buildArena();

                boolean bool = random.nextBoolean();

                com.bryjamin.wickedwizard.factories.BombFactory bf = new com.bryjamin.wickedwizard.factories.BombFactory(assetManager);

                for(int i = 0; i < 4; i++) {
                    arena.addEntity(decorFactory.grapplePointBag( i % 2 == (bool ? 0 : 1) ? arena.getWidth() / 4 * 3 : arena.getWidth() / 4, Measure.units(45f + (i * 30))));
                }

                int zeroOrOne = random.nextBoolean() ? 0 : 1;

                for(int i = 0; i < 5; i++) {
                    arena.addEntity(bf.horizontalSeaMine(Measure.units(45f), Measure.units(25f + (i * 30)), i % 2 == zeroOrOne));
                }




                return arena;
            }
        };
    }


    public com.bryjamin.wickedwizard.factories.arenas.ArenaCreate room27DBlobs() {

        return new com.bryjamin.wickedwizard.factories.arenas.ArenaCreate() {
            @Override
            public Arena createArena(com.bryjamin.wickedwizard.utils.MapCoords defaultCoords) {

                Arena arena = arenaShellFactory.createSmallArenaNoGrapple(defaultCoords, Arena.ArenaType.TRAP);

                arena.arenaType = Arena.ArenaType.TRAP;

                final boolean isLeft = random.nextBoolean();

                arena.addEntity(decorFactory.wallBag(isLeft ? Measure.units(5f) : arena.getWidth() - Measure.units(65f), Measure.units(30f), Measure.units(60f), Measure.units(5f)));


                arena.addEntity(arenaEnemyPlacementFactory.spawnerFactory.spawnerBag(isLeft ? arena.getWidth() / 4 : arena.getWidth() / 4 * 3, Measure.units(45f),
                        new SpawnerFactory.Spawner() {
                            public Bag<Component> spawnBag(float x, float y) {
                                return arenaEnemyPlacementFactory.blobFactory.angrySmallBag(x,y, !isLeft); //This is so it heads towards the wall first
                            }
                        }, 4));

                return arena;
            }
        };
    }


    public com.bryjamin.wickedwizard.factories.arenas.ArenaCreate room28SilverHeadAndAmoebas() {

        return new com.bryjamin.wickedwizard.factories.arenas.ArenaCreate() {
            @Override
            public Arena createArena(com.bryjamin.wickedwizard.utils.MapCoords defaultCoords) {

                Arena arena = arenaShellFactory.createSmallArenaNoGrapple(defaultCoords, Arena.ArenaType.TRAP);

                arena.addEntity(decorFactory.platform(Measure.units(35f), Measure.units(25f), Measure.units(30f)));

                arena.addEntity(arenaEnemyPlacementFactory.spawnSilverHead(arena.getWidth() / 2, Measure.units(45f)));

                arena.addEntity(arenaEnemyPlacementFactory.spawnerFactory.spawnerBag(arena.getWidth() / 4, Measure.units(45f),
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


    public com.bryjamin.wickedwizard.factories.arenas.ArenaCreate room29JigAndTurrets() {

        return new com.bryjamin.wickedwizard.factories.arenas.ArenaCreate() {
            @Override
            public Arena createArena(com.bryjamin.wickedwizard.utils.MapCoords defaultCoords) {

                Arena arena = arenaShellFactory.createOmniArenaHiddenGrapple(defaultCoords, Arena.ArenaType.TRAP);

                arena.addEntity(arenaEnemyPlacementFactory.spawnMovingJig(arena.getWidth() / 2, Measure.units(40f)));

                arena.addEntity(decorFactory.inCombatfixedWallTurret(Measure.units(20f), Measure.units(50f), 180, 1.5f, 1.5f));
                arena.addEntity(decorFactory.inCombatfixedWallTurret(arena.getWidth() - Measure.units(25f), Measure.units(50f), 180, 1.5f, 1.5f));


                return arena;
            }
        };
    }




    //TODO maybe make chests more rare here?
    public com.bryjamin.wickedwizard.factories.arenas.ArenaCreate room30LargeRoomWithTreasureInTheCenter() {

        return new com.bryjamin.wickedwizard.factories.arenas.ArenaCreate() {
            @Override
            public Arena createArena(com.bryjamin.wickedwizard.utils.MapCoords defaultCoords) {

                Arena arena =  new ArenaBuilder(assetManager, arenaSkin, Arena.ArenaType.NORMAL)
                        .addSection(new ArenaBuilder.Section(defaultCoords,
                                ArenaBuilder.wall.DOOR,
                                ArenaBuilder.wall.NONE,
                                ArenaBuilder.wall.NONE,
                                ArenaBuilder.wall.NONE))
                        .addSection(new ArenaBuilder.Section(new com.bryjamin.wickedwizard.utils.MapCoords(defaultCoords.getX() + 1, defaultCoords.getY()),
                                ArenaBuilder.wall.NONE,
                                ArenaBuilder.wall.DOOR,
                                ArenaBuilder.wall.NONE,
                                ArenaBuilder.wall.NONE))
                        .addSection(new ArenaBuilder.Section(new com.bryjamin.wickedwizard.utils.MapCoords(defaultCoords.getX(), defaultCoords.getY() + 1),
                                ArenaBuilder.wall.FULL,
                                ArenaBuilder.wall.NONE,
                                ArenaBuilder.wall.GRAPPLE,
                                ArenaBuilder.wall.NONE))
                        .addSection(new ArenaBuilder.Section(new com.bryjamin.wickedwizard.utils.MapCoords(defaultCoords.getX() + 1, defaultCoords.getY() + 1),
                                ArenaBuilder.wall.NONE,
                                ArenaBuilder.wall.FULL,
                                ArenaBuilder.wall.GRAPPLE,
                                ArenaBuilder.wall.NONE)).buildArena();

                arena.addEntity(decorFactory.wallBag(Measure.units(0f), Measure.units(-5f), arena.getWidth(), Measure.units(5f)));



                //LeftSide
                arena.addEntity(decorFactory.wallBag(Measure.units(5f), Measure.units(0), Measure.units(35f), Measure.units(10f)));
                arena.addEntity(decorFactory.wallBag(Measure.units(35), Measure.units(10f), Measure.units(5f), Measure.units(40f)));
                arena.addEntity(decorFactory.wallBag(Measure.units(40f), Measure.units(45f), Measure.units(35f), Measure.units(5f)));
                arena.addEntity(decorFactory.grapplePointBag(Measure.units(20f), Measure.units(50f)));


                //RightSide
                arena.addEntity(decorFactory.wallBag(arena.getWidth() - Measure.units(40f), Measure.units(0), Measure.units(35f), Measure.units(10f)));
                arena.addEntity(decorFactory.wallBag(arena.getWidth() - Measure.units(40f), Measure.units(10f), Measure.units(5f), Measure.units(40f)));
                arena.addEntity(decorFactory.wallBag(arena.getWidth() - Measure.units(75f), Measure.units(45f), Measure.units(35f), Measure.units(5f)));
                arena.addEntity(decorFactory.grapplePointBag(arena.getWidth() - Measure.units(20f), Measure.units(50f)));



                boolean leftHasMines = random.nextBoolean();

                com.bryjamin.wickedwizard.factories.BombFactory bf = new com.bryjamin.wickedwizard.factories.BombFactory(assetManager);

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


                boolean bool = random.nextBoolean();

                arena.addEntity(bf.horizontalSeaMine(Measure.units(55f), Measure.units(20f), bool));
                arena.addEntity(bf.horizontalSeaMine(arena.getWidth() - Measure.units(55f), Measure.units(30f), bool));


                arena.addEntity(decorFactory.grapplePointBag(arena.getWidth() / 2, Measure.units(40f)));
                arena.addEntity(decorFactory.spikeWall(Measure.units(30f), Measure.units(0f),arena.getWidth(), Measure.units(5f), 0));

                return arena;
            }
        };
    }













}
