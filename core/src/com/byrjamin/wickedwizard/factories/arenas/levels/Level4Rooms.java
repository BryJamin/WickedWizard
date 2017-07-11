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
import com.byrjamin.wickedwizard.factories.weapons.enemy.LaserOrbitalTask;
import com.byrjamin.wickedwizard.utils.MapCoords;
import com.byrjamin.wickedwizard.utils.Measure;

import java.util.Random;

/**
 * Created by Home on 20/06/2017.
 */

public class Level4Rooms extends AbstractFactory implements ArenaRepostiory {

    private ArenaShellFactory arenaShellFactory;
    private ChestFactory chestFactory;
    private DecorFactory decorFactory;
    private ArenaEnemyPlacementFactory arenaEnemyPlacementFactory;
    private BombFactory bombFactory;
    private Random random;
    private ArenaSkin arenaSkin;

    public Level4Rooms(AssetManager assetManager, ArenaSkin arenaSkin, Random random) {
        super(assetManager);
        this.arenaShellFactory = new ArenaShellFactory(assetManager, arenaSkin);
        this.chestFactory = new ChestFactory(assetManager);
        this.arenaEnemyPlacementFactory = new ArenaEnemyPlacementFactory(assetManager, arenaSkin, random);
        this.decorFactory = new DecorFactory(assetManager, arenaSkin);
        this.bombFactory = new BombFactory(assetManager);
        this.random = random;
        this.arenaSkin = arenaSkin;
    }


    @Override
    public Array<ArenaCreate> getAllArenas() {
        return  getLevel4RoomArray();
    }

    public Array<ArenaCreate> getLevel4RoomArray() {
        Array<ArenaCreate> ag = new Array<ArenaCreate>();
        ag.add(room1LaserBouncers());
        ag.add(room2FlyByAndPylons());
        ag.add(room3BouncersAndVerticalPylons());
        ag.add(room4FixedTriAndVerticalPylons());
        ag.add(room5Alurm());
        ag.add(room6Knight());
        ag.add(room72KnightsWidth2());
        ag.add(room82LaserBouncersAndAWall());
        ag.add(room9TrappedLaserBouncer());
        ag.add(room10Width3GrappleChallenge());
        ag.add(room11Width2STrapTwoKnights());
        ag.add(room12TwoAlurmsOneChestOneKnight());
        ag.add(room13largeBattleRoomWithArenaOnTopAndSpikeTreasureOnBottom());
        ag.add(room14largeBattleRoomWithEnemiesOnTopAndAMassiveLaser());
        ag.add(room15SmallBattleRoomBlobAndLaser());
        ag.add(room16SwitchAndLasers());
        ag.add(room17Height2SwitchAndLasers());
        ag.add(room18SwitchsAndLasersAgain());
        ag.add(room19GoatWizardAndKnight());
        ag.add(room20AlurmAndFlyBy());
        ag.add(room21LaserArenaWithIntermittantLasters());
        ag.add(room22HorizontalThroughRoomActualLaserCenter());
        ag.add(room23TreasureLockedCenter());
        ag.add(room24LaserBouncerLaserTrapTreasure());
        ag.add(room25TreasureLaserFloorAndLockedChestMirror());
        ag.add(room26DoubleAlurmWithASqaureCenter());
        ag.add(room27Width2SwitchInTheCenter());
        ag.add(room28Height3TwoLasersSwitchInTheCenter());
        ag.add(room29BoringHeight2TreasureRoom());
        ag.add(room30WidthTwoPylonsAlurmsAndKnight());

        return ag;
    }



    public ArenaCreate room1LaserBouncers() {
        return new ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {
                Arena arena = arenaShellFactory.createOmniArenaHiddenGrapple(defaultCoords);
                arena.addEntity(arenaEnemyPlacementFactory.spawnLaserBouncer(arena.getWidth() / 4, Measure.units(45f), random.nextBoolean()));
                arena.addEntity(arenaEnemyPlacementFactory.spawnBouncer(arena.getWidth() / 4 * 3, Measure.units(45f)));
                arena.roomType = Arena.RoomType.TRAP;
                return arena;
            }
        };
    }


    public ArenaCreate room2FlyByAndPylons() {
        return new ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {
                Arena arena = arenaShellFactory.createOmniArenaHiddenGrapple(defaultCoords);
                arena.addEntity(arenaEnemyPlacementFactory.pylonFactory.pylonBag(arena.getWidth() - Measure.units(15f), Measure.units(30f), 90));
                arena.addEntity(arenaEnemyPlacementFactory.pylonFactory.pylonBag(Measure.units(5f), Measure.units(30f), -90));
                arena.addEntity(arenaEnemyPlacementFactory.spawnMovingFlyByBombSentry(arena.getWidth() / 2, Measure.units(45f)));
                arena.roomType = Arena.RoomType.TRAP;
                return arena;
            }
        };
    }




    public ArenaCreate room3BouncersAndVerticalPylons() {
        return new ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {
                Arena arena = arenaShellFactory.createOmniArenaHiddenGrapple(defaultCoords);
                arena.addEntity(arenaEnemyPlacementFactory.pylonFactory.pylonBag(arena.getWidth() - Measure.units(25f), arena.getHeight() - Measure.units(15f), 180));
                arena.addEntity(arenaEnemyPlacementFactory.pylonFactory.pylonBag(Measure.units(15f), arena.getHeight() - Measure.units(15f), 180));
                arena.addEntity(arenaEnemyPlacementFactory.spawnBouncer(arena.getWidth() / 4 * 3, arena.getHeight() / 2));
                arena.addEntity(arenaEnemyPlacementFactory.spawnBouncer(arena.getWidth() / 4  , arena.getHeight() / 2));
                arena.roomType = Arena.RoomType.TRAP;
                return arena;
            }
        };
    }


    public ArenaCreate room4FixedTriAndVerticalPylons() {
        return new ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {
                Arena arena = arenaShellFactory.createOmniArenaHiddenGrapple(defaultCoords);
                arena.addEntity(arenaEnemyPlacementFactory.pylonFactory.pylonBag(arena.getWidth() - Measure.units(25f), arena.getHeight() - Measure.units(15f), 180));
                arena.addEntity(arenaEnemyPlacementFactory.pylonFactory.pylonBag(Measure.units(15f), arena.getHeight() - Measure.units(15f), 180));
                arena.addEntity(arenaEnemyPlacementFactory.spawnFixedSentry(arena.getWidth() / 2, Measure.units(45f)));
                //arena.addEntity(arenaEnemyPlacementFactory.spawnBouncer(arena.getWidth() / 4  , arena.getHeight() / 2));
                arena.roomType = Arena.RoomType.TRAP;
                return arena;
            }
        };
    }



    public ArenaCreate room5Alurm() {
        return new ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {
                Arena arena = arenaShellFactory.createOmniArenaHiddenGrapple(defaultCoords);
                arena.addEntity(arenaEnemyPlacementFactory.spawnAlurm(arena.getWidth() / 2, Measure.units(45f)));
                //arena.addEntity(arenaEnemyPlacementFactory.spawnBouncer(arena.getWidth() / 4  , arena.getHeight() / 2));
                arena.roomType = Arena.RoomType.TRAP;
                return arena;
            }
        };
    }



    public ArenaCreate room6Knight() {
        return new ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {
                Arena arena = arenaShellFactory.createOmniArenaHiddenGrapple(defaultCoords);
                arena.addEntity(arenaEnemyPlacementFactory.spawnKnight(arena.getWidth() / 2, Measure.units(45f), random.nextBoolean(), random.nextBoolean()));
                //arena.addEntity(arenaEnemyPlacementFactory.spawnBouncer(arena.getWidth() / 4  , arena.getHeight() / 2));
                arena.roomType = Arena.RoomType.TRAP;
                return arena;
            }
        };
    }



    public ArenaCreate room72KnightsWidth2() {
        return new ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {

                Arena arena = new Arena(arenaSkin, defaultCoords,
                        new MapCoords(defaultCoords.getX() + 1, defaultCoords.getY()));

                arena =  new ArenaBuilder(assetManager, arenaSkin)
                        .addSection(new ArenaBuilder.Section(defaultCoords,
                                ArenaBuilder.wall.MANDATORYDOOR,
                                ArenaBuilder.wall.NONE,
                                ArenaBuilder.wall.FULL,
                                ArenaBuilder.wall.FULL))
                        .addSection(new ArenaBuilder.Section(new MapCoords(defaultCoords.getX() + 1, defaultCoords.getY()),
                                ArenaBuilder.wall.NONE ,
                                ArenaBuilder.wall.DOOR,
                                ArenaBuilder.wall.GRAPPLE,
                                ArenaBuilder.wall.DOOR))
                        .buildArena(arena);

                arena.addEntity(arenaEnemyPlacementFactory.spawnKnight(arena.getWidth() / 4, Measure.units(45f), random.nextBoolean(), random.nextBoolean()));
                arena.addEntity(arenaEnemyPlacementFactory.spawnKnight(arena.getWidth() / 4 * 3, Measure.units(45f), random.nextBoolean(), random.nextBoolean()));
                //arena.addEntity(arenaEnemyPlacementFactory.spawnBouncer(arena.getWidth() / 4  , arena.getHeight() / 2));
                arena.roomType = Arena.RoomType.TRAP;
                return arena;
            }
        };
    }



    public ArenaCreate room82LaserBouncersAndAWall() {
        return new ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {

                Arena arena = arenaShellFactory.createOmniArenaSquareCenter(defaultCoords);

                arena.addEntity(arenaEnemyPlacementFactory.spawnLaserBouncer(arena.getWidth() / 2, Measure.units(45f), random.nextBoolean()));
                //arena.addEntity(arenaEnemyPlacementFactory.spawnBouncer(arena.getWidth() / 4 * 3, Measure.units(45f)));


                arena.addEntity(decorFactory.wallBag(Measure.units(20f), Measure.units(25f), Measure.units(5f), Measure.units(15f)));
                arena.addEntity(decorFactory.wallBag(arena.getWidth() - Measure.units(25f), Measure.units(25f), Measure.units(5f), Measure.units(15f)));

                //arena.addEntity(arenaEnemyPlacementFactory.spawnBouncer(arena.getWidth() / 4  , arena.getHeight() / 2));
                arena.roomType = Arena.RoomType.TRAP;
                return arena;
            }
        };
    }


    public ArenaCreate room9TrappedLaserBouncer() {
        return new ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {

                Arena arena = arenaShellFactory.createSmallArenaNoGrapple(defaultCoords, random.nextInt());

                arena.addEntity(arenaEnemyPlacementFactory.bouncerFactory.laserBouncer(arena.getWidth() / 2, Measure.units(30f), random.nextBoolean()));
                //arena.addEntity(arenaEnemyPlacementFactory.spawnBouncer(arena.getWidth() / 4 * 3, Measure.units(45f)));

                arena.addEntity(decorFactory.wallBag(Measure.units(45f), Measure.units(20f), Measure.units(10f), Measure.units(5f), arenaSkin));
                arena.addEntity(decorFactory.wallBag(Measure.units(45f), Measure.units(35f), Measure.units(10f), Measure.units(5f), arenaSkin));

                arena.addEntity(decorFactory.wallBag(Measure.units(35f), Measure.units(25f), Measure.units(5f), Measure.units(10f)));
                arena.addEntity(decorFactory.wallBag(arena.getWidth() - Measure.units(40f), Measure.units(25f), Measure.units(5f), Measure.units(10f)));

                //arena.addEntity(arenaEnemyPlacementFactory.spawnBouncer(arena.getWidth() / 4  , arena.getHeight() / 2));
                //arena.roomType = Arena.RoomType.TRAP;
                return arena;
            }
        };
    }


    public ArenaCreate room10Width3GrappleChallenge() {
        return new ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {

                Arena arena = new Arena(arenaSkin, defaultCoords,
                        new MapCoords(defaultCoords.getX() + 1, defaultCoords.getY()),
                        new MapCoords(defaultCoords.getX() + 2, defaultCoords.getY()));

                boolean upperDoorIsLeft = random.nextBoolean();

                arena =  new ArenaBuilder(assetManager, arenaSkin)
                        .addSection(new ArenaBuilder.Section(defaultCoords,
                                ArenaBuilder.wall.MANDATORYDOOR,
                                ArenaBuilder.wall.NONE,
                                upperDoorIsLeft ? ArenaBuilder.wall.DOOR : ArenaBuilder.wall.FULL,
                                ArenaBuilder.wall.NONE))
                        .addSection(new ArenaBuilder.Section(new MapCoords(defaultCoords.getX() + 1, defaultCoords.getY()),
                                ArenaBuilder.wall.NONE ,
                                ArenaBuilder.wall.NONE,
                                ArenaBuilder.wall.FULL,
                                ArenaBuilder.wall.NONE))
                        .addSection(new ArenaBuilder.Section(new MapCoords(defaultCoords.getX() + 2, defaultCoords.getY()),
                                ArenaBuilder.wall.NONE ,
                                ArenaBuilder.wall.MANDATORYDOOR,
                                !upperDoorIsLeft ? ArenaBuilder.wall.DOOR : ArenaBuilder.wall.FULL,
                                ArenaBuilder.wall.NONE))
                        .buildArena(arena);

                arena.addEntity(decorFactory.wallBag(Measure.units(0), Measure.units(-5f), arena.getWidth(), Measure.units(5)));

                //left
                arena.addEntity(decorFactory.wallBag(Measure.units(5), Measure.units(0), Measure.units(20f), Measure.units(10f)));
                arena.addEntity(decorFactory.wallBag(arena.getWidth() - Measure.units(25), Measure.units(0), Measure.units(20f), Measure.units(10f)));

                arena.addEntity(decorFactory.spikeWall(0, 0, arena.getWidth(), Measure.units(5f), 0));

                for(int i = 0; i < 5; i++) arena.addEntity(decorFactory.grapplePointBag(Measure.units(50 + (i * 50)), Measure.units(35f)));

                for(int i = 0; i <= 5; i++) {
                    arena.addEntity(arenaEnemyPlacementFactory.bombFactory.seaMine(Measure.units(30) + Measure.units(i * 48f), Measure.units(32.5f), random.nextBoolean(), random.nextBoolean()));
                }


                //arena.addEntity(arenaEnemyPlacementFactory.spawnBouncer(arena.getWidth() / 4  , arena.getHeight() / 2));
                //arena.roomType = Arena.RoomType.TRAP;
                return arena;
            }
        };
    }



    public ArenaCreate room11Width2STrapTwoKnights() {
        return new ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {
                Arena arena = new Arena(arenaSkin, defaultCoords, new MapCoords(defaultCoords.getX() + 1, defaultCoords.getY()));

                arena = new ArenaBuilder(assetManager, arenaSkin)
                        .addSection(new ArenaBuilder.Section(defaultCoords,
                                ArenaBuilder.wall.DOOR,
                                ArenaBuilder.wall.NONE,
                                ArenaBuilder.wall.GRAPPLE,
                                ArenaBuilder.wall.DOOR))
                        .addSection(new ArenaBuilder.Section(new MapCoords(defaultCoords.getX() + 1, defaultCoords.getY()),
                                ArenaBuilder.wall.NONE,
                                ArenaBuilder.wall.FULL,
                                ArenaBuilder.wall.FULL,
                                ArenaBuilder.wall.FULL)).buildArena(arena);


                arena.addWave(
                        arenaEnemyPlacementFactory.spawnKnight(Measure.units(20f), Measure.units(50f), true, true),
                        arenaEnemyPlacementFactory.spawnKnight(Measure.units(20f), Measure.units(20f), true, true)
                );

                arena.addEntity(new ChestFactory(assetManager).chestBag(Measure.units(150f), Measure.units(10f),
                        chestFactory.trapODAC()));

                arena.addEntity(new ChestFactory(assetManager).chestBag(Measure.units(165f), Measure.units(10f),
                        chestFactory.trapODAC()));

                return arena;
            }

        };
    }



    public ArenaCreate room12TwoAlurmsOneChestOneKnight() {
        return new ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {
                Arena arena = arenaShellFactory.createOmniArenaHiddenGrapple(defaultCoords, random.nextInt(4));

                arena.roomType = Arena.RoomType.TRAP;

                arena.addWave(
                        arenaEnemyPlacementFactory.spawnAlurm(arena.getWidth() / 4, Measure.units(40f)),
                        arenaEnemyPlacementFactory.spawnAlurm(arena.getWidth() / 4 * 3, Measure.units(40f)));

                arena.addWave(arenaEnemyPlacementFactory.spawnKnight(arena.getWidth() / 2, Measure.units(40f)));

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


    public ArenaCreate room13largeBattleRoomWithArenaOnTopAndSpikeTreasureOnBottom(){
        return new ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {

                Arena arena = new Arena(arenaSkin, defaultCoords,
                        new MapCoords(defaultCoords.getX(), defaultCoords.getY() + 1),
                        new MapCoords(defaultCoords.getX() + 1, defaultCoords.getY()),
                        new MapCoords(defaultCoords.getX() + 1, defaultCoords.getY() + 1));
                //arena.roomType = Arena.RoomType.TRAP;

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



                float upperGroundPosY = Measure.units(65f);



                arena.addEntity(decorFactory.platform(Measure.units(5), upperGroundPosY, Measure.units(25f)));
                arena.addEntity(decorFactory.platform(arena.getWidth() - Measure.units(30), upperGroundPosY, Measure.units(25f)));
                arena.addEntity(decorFactory.wallBag(Measure.units(30f), upperGroundPosY, Measure.units(140f), Measure.units(5f)));

                arena.addEntity(decorFactory.grapplePointBag(Measure.units(20f), Measure.units(45f)));
                arena.addEntity(decorFactory.grapplePointBag(arena.getWidth() - Measure.units(20f), Measure.units(45f)));

                arena.addEntity(chestFactory.chestBag(Measure.units(95), Measure.units(70f), chestFactory.trapODAC()));

                arena.addWave(arenaEnemyPlacementFactory.spawnModon(arena.getWidth() / 4, Measure.units(85f)),
                        arenaEnemyPlacementFactory.spawnModon(arena.getWidth() / 4 * 3, Measure.units(85f)),
                        arenaEnemyPlacementFactory.spawnAlurm(arena.getWidth() / 2, Measure.units(105f)));


                arena.addEntity(decorFactory.appearInCombatWallPush(Measure.units(0), upperGroundPosY - Measure.units(5f), arena.getWidth(), Measure.units(5), 90));

                //Bottom
                arena.addEntity(decorFactory.wallBag(Measure.units(0), Measure.units(-5f), arena.getWidth(), Measure.units(5)));
                arena.addEntity(decorFactory.wallBag(Measure.units(5), Measure.units(0), Measure.units(20f), Measure.units(10f)));
                arena.addEntity(decorFactory.wallBag(arena.getWidth() - Measure.units(25), Measure.units(0), Measure.units(20f), Measure.units(10f)));
                arena.addEntity(decorFactory.spikeWall(0, 0, arena.getWidth(), Measure.units(5f), 0));

                arena.addEntity(chestFactory.chestBag(Measure.units(95), Measure.units(10f)));
                arena.addEntity(decorFactory.wallBag(Measure.units(90f), Measure.units(0f), Measure.units(20f), Measure.units(10f)));
                arena.addEntity(bombFactory.mine(Measure.units(90f), Measure.units(10f), 0));
                arena.addEntity(bombFactory.mine(Measure.units(105f), Measure.units(10f), 0));


                return arena;
            }
        };
    }




    public ArenaCreate room14largeBattleRoomWithEnemiesOnTopAndAMassiveLaser(){
        return new ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {

                Arena arena = new Arena(arenaSkin, defaultCoords,
                        new MapCoords(defaultCoords.getX(), defaultCoords.getY() + 1),
                        new MapCoords(defaultCoords.getX() + 1, defaultCoords.getY()),
                        new MapCoords(defaultCoords.getX() + 1, defaultCoords.getY() + 1));
                arena.roomType = Arena.RoomType.TRAP;

                arena =  new ArenaBuilder(assetManager, arenaSkin)
                        .addSection(new ArenaBuilder.Section(defaultCoords,
                                ArenaBuilder.wall.DOOR,
                                ArenaBuilder.wall.NONE,
                                ArenaBuilder.wall.NONE,
                                ArenaBuilder.wall.DOOR))
                        .addSection(new ArenaBuilder.Section(new MapCoords(defaultCoords.getX() + 1, defaultCoords.getY()),
                                ArenaBuilder.wall.NONE,
                                ArenaBuilder.wall.DOOR,
                                ArenaBuilder.wall.NONE,
                                ArenaBuilder.wall.DOOR))
                        .addSection(new ArenaBuilder.Section(new MapCoords(defaultCoords.getX(), defaultCoords.getY() + 1),
                                ArenaBuilder.wall.FULL,
                                ArenaBuilder.wall.NONE,
                                ArenaBuilder.wall.DOOR,
                                ArenaBuilder.wall.NONE))
                        .addSection(new ArenaBuilder.Section(new MapCoords(defaultCoords.getX() + 1, defaultCoords.getY() + 1),
                                ArenaBuilder.wall.NONE,
                                ArenaBuilder.wall.FULL,
                                ArenaBuilder.wall.DOOR,
                                ArenaBuilder.wall.NONE)).buildArena(arena);


                arena.addEntity(decorFactory.grapplePointBag(Measure.units(30f), Measure.units(50f)));
                arena.addEntity(decorFactory.grapplePointBag(Measure.units(30f), Measure.units(95f)));
                // arena.addEntity(decorFactory.grapplePointBag(Measure.units(30f), Measure.units(110f)));
                arena.addEntity(decorFactory.grapplePointBag(arena.getWidth() - Measure.units(35f), Measure.units(50f)));
                arena.addEntity(decorFactory.grapplePointBag(arena.getWidth() - Measure.units(35f), Measure.units(95f)));
                //arena.addEntity(decorFactory.grapplePointBag(arena.getWidth() - Measure.units(35f), Measure.units(110f)));
                arena.addEntity(decorFactory.platform(0, Measure.units(75f), arena.getWidth()));

                arena.addEntity(decorFactory.inCombatLaserChain(Measure.units(90f), Measure.units(50f),4,
                        new LaserOrbitalTask(assetManager, Measure.units(15f), random.nextBoolean() ? 0.325f : -0.325f, 10, 0.5f, new int[]{0,180})));


                arena.addEntity(random.nextBoolean() ?
                        arenaEnemyPlacementFactory.spawnFixedFlyByBombSentry(arena.getWidth() / 2, Measure.units(35f)) :
                        arenaEnemyPlacementFactory.spawnFixedTriSentry(arena.getWidth() / 2, Measure.units(35f)));


                arena.addEntity(arenaEnemyPlacementFactory.spawnSmallAngryBlob(Measure.units(20f), Measure.units(95f)));
                arena.addEntity(arenaEnemyPlacementFactory.spawnSmallAngryBlob(arena.getWidth() - Measure.units(20f), Measure.units(95f)));


                return arena;
            }
        };
    }


    public ArenaCreate room15SmallBattleRoomBlobAndLaser(){
        return new ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {

                Arena arena = arenaShellFactory.createSmallArenaNoGrapple(defaultCoords, random.nextInt(4));
                arena.roomType = Arena.RoomType.TRAP;


                arena.addEntity(decorFactory.inCombatLaserChain(Measure.units(45f), Measure.units(25f),2,
                        new LaserOrbitalTask(assetManager, Measure.units(2.5f), random.nextBoolean() ? 0.325f : -0.325f, 25, 0.5f, new int[]{0,180})));
                arena.addEntity(arenaEnemyPlacementFactory.spawnAngryBlob(Measure.units(20f), Measure.units(15f)));

                return arena;
            }
        };
    }



    public ArenaCreate room16SwitchAndLasers(){
        return new ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {

                Arena arena = arenaShellFactory.createSmallArenaNoGrapple(defaultCoords, random.nextInt(4));
                arena.roomType = Arena.RoomType.TRAP;


                arena.addEntity(decorFactory.inCombatLaserChain(Measure.units(45f), Measure.units(25f),2,
                        new LaserOrbitalTask(assetManager, Measure.units(2.5f), random.nextBoolean() ? 1f : -1f, 25, 0.5f, new int[]{0,180})));


                arena.addEntity(arenaEnemyPlacementFactory.switchFactory.switchBag(Measure.units(5f), Measure.units(40f), -90));
                arena.addEntity(arenaEnemyPlacementFactory.switchFactory.switchBag(arena.getWidth() - Measure.units(10f), Measure.units(40f), 90));
                arena.addEntity(arenaEnemyPlacementFactory.switchFactory.switchBag(Measure.units(40f), Measure.units(27.5f), 90));
                arena.addEntity(arenaEnemyPlacementFactory.switchFactory.switchBag(Measure.units(55f), Measure.units(27.5f), -90));

                return arena;
            }
        };
    }




    public ArenaCreate room17Height2SwitchAndLasers(){
        return new ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {

                Arena arena = new Arena(arenaSkin,
                        defaultCoords,
                        new MapCoords(defaultCoords.getX(), defaultCoords.getY() + 1));
                arena.roomType = Arena.RoomType.TRAP;

                arena =  new ArenaBuilder(assetManager, arenaSkin)
                        .addSection(new ArenaBuilder.Section(defaultCoords,
                                ArenaBuilder.wall.FULL,
                                ArenaBuilder.wall.FULL,
                                ArenaBuilder.wall.NONE,
                                ArenaBuilder.wall.MANDATORYDOOR))
                        .addSection(new ArenaBuilder.Section(new MapCoords(defaultCoords.getX(), defaultCoords.getY() + 1),
                                ArenaBuilder.wall.FULL,
                                ArenaBuilder.wall.FULL,
                                ArenaBuilder.wall.MANDATORYDOOR,
                                ArenaBuilder.wall.NONE))
                        .buildArena(arena);


                boolean bool = random.nextBoolean();
                float changeInDegrees = 1f;
                int numberOfParts = 15;

                arena.addEntity(decorFactory.inCombatLaserChain(Measure.units(30f), Measure.units(65f),2,
                        new LaserOrbitalTask(assetManager, Measure.units(2.5f),
                                0, numberOfParts, 0.5f, new int[]{180})));

                arena.addEntity(decorFactory.inCombatLaserChain(Measure.units(60f), Measure.units(65f),2,
                        new LaserOrbitalTask(assetManager, Measure.units(2.5f),
                                0, numberOfParts, 0.5f, new int[]{0})));


                arena.addEntity(decorFactory.inCombatLaserChain(Measure.units(30f), Measure.units(30f),2,
                        new LaserOrbitalTask(assetManager, Measure.units(2.5f),
                                0, numberOfParts, 0.5f, new int[]{180})));

                arena.addEntity(decorFactory.inCombatLaserChain(Measure.units(60f), Measure.units(30f),2,
                        new LaserOrbitalTask(assetManager, Measure.units(2.5f),
                                0, numberOfParts, 0.5f, new int[]{180})));


                arena.addEntity(arenaEnemyPlacementFactory.switchFactory.switchBag(Measure.units(5f), Measure.units(50), -90));
                arena.addEntity(arenaEnemyPlacementFactory.switchFactory.switchBag(arena.getWidth() - Measure.units(10f), Measure.units(50), 90));
                arena.addEntity(arenaEnemyPlacementFactory.switchFactory.switchBag(Measure.units(10f), arena.getHeight() - Measure.units(35f), 180));
                arena.addEntity(arenaEnemyPlacementFactory.switchFactory.switchBag(arena.getWidth() - Measure.units(15f),arena.getHeight() - Measure.units(35f), 180));
                arena.addEntity(arenaEnemyPlacementFactory.switchFactory.switchBag(Measure.units(32.5f), Measure.units(25f), 180));
                arena.addEntity(arenaEnemyPlacementFactory.switchFactory.switchBag(arena.getWidth() - Measure.units(37.5f),Measure.units(25f), 180));

                arena.addEntity(decorFactory.wallBag(Measure.units(5f), Measure.units(90f), Measure.units(15f), Measure.units(5f)));
                arena.addEntity(decorFactory.wallBag(arena.getWidth() - Measure.units(20f), Measure.units(90f), Measure.units(15f), Measure.units(5f)));
                arena.addEntity(decorFactory.platform(Measure.units(20f), Measure.units(90f), Measure.units(60f)));

                arena.addEntity(decorFactory.grapplePointBag(arena.getWidth() / 2, Measure.units(55f)));
               // for(int i = 0; i < 3; i++) decorFactory.grapplePointBag(Measure.)

                return arena;
            }
        };
    }




    public ArenaCreate room18SwitchsAndLasersAgain(){
        return new ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {

                Arena arena = arenaShellFactory.createSmallArenaNoGrapple(defaultCoords, random.nextInt(4));
                arena.roomType = Arena.RoomType.TRAP;


                arena.addEntity(decorFactory.inCombatLaserChain(Measure.units(45f), Measure.units(25f),2,
                        new LaserOrbitalTask(assetManager, Measure.units(2.5f), random.nextBoolean() ? 1f : -1f, 25, 0.5f, new int[]{0,180})));


                arena.addWave(arenaEnemyPlacementFactory.switchFactory.fadeInswitchBag(Measure.units(5f), Measure.units(40f), -90));
                arena.addWave(arenaEnemyPlacementFactory.switchFactory.fadeInswitchBag(arena.getWidth() - Measure.units(10f), Measure.units(40f), 90));
                arena.addWave(arenaEnemyPlacementFactory.switchFactory.fadeInswitchBag(Measure.units(40f), Measure.units(27.5f), 90));
                arena.addWave(arenaEnemyPlacementFactory.switchFactory.fadeInswitchBag(Measure.units(5f), Measure.units(20f), -90));
                arena.addWave(arenaEnemyPlacementFactory.switchFactory.fadeInswitchBag(Measure.units(55f), Measure.units(27.5f), -90));
                arena.addWave(arenaEnemyPlacementFactory.switchFactory.fadeInswitchBag(Measure.units(20f), Measure.units(50f), 180));
                arena.addWave(arenaEnemyPlacementFactory.switchFactory.fadeInswitchBag(arena.getWidth() - Measure.units(25f), Measure.units(50f), 180));
                arena.addWave(arenaEnemyPlacementFactory.switchFactory.fadeInswitchBag(arena.getWidth() - Measure.units(10f), Measure.units(20f), 90));

                arena.addWave(chestFactory.centeredChestBag(arena.getWidth() / 2, arena.getHeight() / 2));

                return arena;
            }
        };
    }



    public ArenaCreate room19GoatWizardAndKnight(){
        return new ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {

                Arena arena = arenaShellFactory.createOmniArenaHiddenGrapple(defaultCoords, random.nextInt(4));
                //arena.roomType = Arena.RoomType.TRAP;


                arena.addWave(arenaEnemyPlacementFactory.spawnKnight(arena.getWidth() / 4, Measure.units(45f)),
                        arenaEnemyPlacementFactory.spawnGoatWizard(arena.getWidth() / 4 * 3, Measure.units(45f)));

                arena.addEntity(chestFactory.chestBag(Measure.units(45f), Measure.units(10f), chestFactory.trapODAC()));

                return arena;
            }
        };
    }


    public ArenaCreate room20AlurmAndFlyBy(){
        return new ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {

                Arena arena = arenaShellFactory.createOmniArenaHiddenGrapple(defaultCoords, random.nextInt(4));

                arena.roomType = Arena.RoomType.TRAP;

                boolean bool = random.nextBoolean();

                arena.addWave(arenaEnemyPlacementFactory.spawnMovingFlyByBombSentry(bool ? arena.getWidth() / 4 * 3 : arena.getWidth() / 4, Measure.units(45f)),
                        arenaEnemyPlacementFactory.spawnAlurm(bool ? arena.getWidth() / 4 : arena.getWidth() / 4 * 3, Measure.units(45f)));


                return arena;
            }
        };
    }



    public ArenaCreate room21LaserArenaWithIntermittantLasters() {
        return new ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {

                Arena arena = arenaShellFactory.createOmniArenaHiddenGrapple(defaultCoords, random.nextInt(4));

                arena.roomType = Arena.RoomType.TRAP;


                LaserOrbitalTask.LaserBuilder lb = new LaserOrbitalTask.LaserBuilder(assetManager)
                        .orbitalSize(Measure.units(2.5f))
                        .chargeTime(0.5f)
                        .numberOfOrbitals(10)
                        .angles(270)
                        .expiryTime(1.5f)
                        .orbitalAndIntervalSize(Measure.units(5f));

                arena.addEntity(decorFactory.inCombatTimedLaserChain(Measure.units(20f), Measure.units(52.5f), 1, 3f,
                        lb.build()));

                arena.addEntity(decorFactory.inCombatTimedLaserChain(arena.getWidth() - Measure.units(25f), Measure.units(52.5f), 1, 3f,
                        lb.build()));

                arena.addEntity(arenaEnemyPlacementFactory.spawnFixedTriSentry(arena.getWidth() / 2, Measure.units(45f)));
                arena.addEntity(arenaEnemyPlacementFactory.spawnBouncer(arena.getWidth() / 2, Measure.units(45f)));

                return arena;
            }
        };
    }


    public ArenaCreate room22HorizontalThroughRoomActualLaserCenter() {

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

                LaserOrbitalTask.LaserBuilder lb = new LaserOrbitalTask.LaserBuilder(assetManager)
                        .orbitalSize(Measure.units(2.5f))
                        .chargeTime(1f)
                        .numberOfOrbitals(10)
                        .angles(270)
                        .expiryTime(3f)
                        .orbitalAndIntervalSize(Measure.units(5f));


                for(int i = 0; i < 4; i++) arena.addEntity(decorFactory.inCombatTimedLaserChain(Measure.units(35f + (i * 7.5f)), Measure.units(52.5f), 1, 6f,
                        lb.build()));

                boolean isLeft = random.nextBoolean();

                arena.addWave(random.nextBoolean() ?
                        arenaEnemyPlacementFactory.spawnFixedTriSentry(!isLeft ? Measure.units(20f) : arena.getWidth() - Measure.units(20f), Measure.units(45f)) :
                        arenaEnemyPlacementFactory.spawnMovingFlyByBombSentry(!isLeft ? Measure.units(20f) : arena.getWidth() - Measure.units(20f), Measure.units(45f)));
                arena.addWave(random.nextBoolean() ?
                        arenaEnemyPlacementFactory.spawnFixedTriSentry(isLeft ? Measure.units(20f) : arena.getWidth() - Measure.units(20f), Measure.units(45f)) :
                        arenaEnemyPlacementFactory.spawnMovingFlyByBombSentry(!isLeft ? Measure.units(20f) : arena.getWidth() - Measure.units(20f), Measure.units(45f)));

                return arena;
            }
        };
    }


    //TODO add the chance of a booby trap chest
    public ArenaCreate room23TreasureLockedCenter() {


        return new ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {

                Arena arena = arenaShellFactory.createSmallArenaNoGrapple(defaultCoords);

                arena.addEntity(decorFactory.wallBag(Measure.units(20f), Measure.units(40f), Measure.units(60f), Measure.units(5)));
                arena.addEntity(decorFactory.wallBag(Measure.units(20f), Measure.units(25f), Measure.units(5f), Measure.units(15f)));
                arena.addEntity(decorFactory.wallBag(Measure.units(75f), Measure.units(25f), Measure.units(5f), Measure.units(15f)));
                arena.addEntity(decorFactory.lockWall(Measure.units(25f), Measure.units(25f), Measure.units(50f), Measure.units(5f)));


                arena.addEntity(chestFactory.chestBag(Measure.units(30f), Measure.units(30f)));

                arena.addEntity(chestFactory.chestBag(Measure.units(45f), Measure.units(30f)));

                arena.addEntity(chestFactory.chestBag(Measure.units(60f), Measure.units(30f)));
                //arena.roomType = Arena.RoomType.TRAP;


                LaserOrbitalTask.LaserBuilder lb = new LaserOrbitalTask.LaserBuilder(assetManager)
                        .orbitalSize(Measure.units(2.5f))
                        .chargeTime(0.5f)
                        .numberOfOrbitals(4)
                        .angles(180)
                        .expiryTime(1.5f)
                        .orbitalAndIntervalSize(Measure.units(5f));

                arena.addEntity(decorFactory.timedLaserChain(Measure.units(17.5f), Measure.units(35f), 1, 3f,
                        lb.build()));

                arena.addEntity(decorFactory.timedLaserChain(Measure.units(77.5f), Measure.units(35f), 1, 3f,
                        lb.angles(0).build()));




                return arena;


            }

        };

    }



    //TODO add the chance of a booby trap chest
    public ArenaCreate room24LaserBouncerLaserTrapTreasure() {


        return new ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {

                Arena arena = new Arena(arenaSkin, defaultCoords);

                arena =  new ArenaBuilder(assetManager, arenaSkin)
                        .addSection(new ArenaBuilder.Section(defaultCoords,
                                ArenaBuilder.wall.MANDATORYDOOR,
                                ArenaBuilder.wall.MANDATORYDOOR,
                                ArenaBuilder.wall.FULL,
                                ArenaBuilder.wall.FULL))
                        .buildArena(arena);

                arena.addEntity(chestFactory.chestBag(Measure.units(45f), Measure.units(10f), chestFactory.trapODAC()));

                arena.addWave(arenaEnemyPlacementFactory.spawnLaserBouncer(arena.getWidth() / 2, Measure.units(45f)));
                //arena.addEntity(chestFactory.chestBag(Measure.units(45f), Measure.units(30f)));

                //arena.addEntity(chestFactory.chestBag(Measure.units(60f), Measure.units(30f)));
                //arena.roomType = Arena.RoomType.TRAP;


                LaserOrbitalTask.LaserBuilder lb = new LaserOrbitalTask.LaserBuilder(assetManager)
                        .chargeTime(0.5f)
                        .numberOfOrbitals(7)
                        .angles(270)
                        .expiryTime(1.5f)
                        .orbitalAndIntervalSize(Measure.units(5f));

                boolean mirror = random.nextBoolean();

                arena.addEntity(decorFactory.timedLaserChain(mirror ? Measure.units(25f) : arena.getWidth() - Measure.units(35f), Measure.units(30f), 2, 3,
                        lb.build()));

                arena.addEntity(decorFactory.timedLaserChain(mirror ? arena.getWidth() - Measure.units(35f) : Measure.units(25f), Measure.units(25f), 2, 3,
                        lb.angles(90).build()));

                return arena;


            }

        };

    }


    public ArenaCreate room25TreasureLaserFloorAndLockedChestMirror() {


        return new ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {

                boolean chestsAreLeft = random.nextBoolean();

                Arena arena = new Arena(arenaSkin, defaultCoords);

                arena =  new ArenaBuilder(assetManager, arenaSkin)
                        .addSection(new ArenaBuilder.Section(defaultCoords,
                                chestsAreLeft ? ArenaBuilder.wall.FULL : ArenaBuilder.wall.DOOR,
                                ArenaBuilder.wall.FULL,
                                ArenaBuilder.wall.GRAPPLE,
                                ArenaBuilder.wall.NONE))
                        .buildArena(arena);

                arena.addEntity(decorFactory.wallBag(0, Measure.units(-5f), arena.getWidth(), Measure.units(5f)));

                float chestPosX = chestsAreLeft ? Measure.units(11.25f) : arena.getWidth() -  Measure.units(11.25f);
                float chest2PosX = chestsAreLeft ? Measure.units(23.75f) : arena.getWidth() - Measure.units(23.75f);


                float leftwallPosX = !chestsAreLeft ? Measure.units(5f) : arena.getWidth() - Measure.units(20f);
                float rightwallPosX = chestsAreLeft ? Measure.units(5f) : arena.getWidth() - Measure.units(35f);
                arena.addEntity(decorFactory.wallBag(leftwallPosX, 0, Measure.units(15f), Measure.units(10f)));
                arena.addEntity(decorFactory.wallBag(rightwallPosX, 0, Measure.units(30f), Measure.units(10f)));

                arena.addEntity(decorFactory.wallBag(chestsAreLeft ? Measure.units(30f) : arena.getWidth() - Measure.units(35f),
                        Measure.units(10f), Measure.units(5f), Measure.units(20f)));

                LaserOrbitalTask.LaserBuilder lb = new LaserOrbitalTask.LaserBuilder(assetManager)
                        .chargeTime(0.5f)
                        .numberOfOrbitals(20)
                        .angles(chestsAreLeft ? 180 : 0)
                        .orbitalAndIntervalSize(Measure.units(2.5f));

                arena.addEntity(decorFactory.laserChain(!chestsAreLeft ? Measure.units(17.5f) : arena.getWidth() - Measure.units(22.5f), Measure.units(2.5f), 1,
                        lb.build()));


                float wallPosX = chestsAreLeft ? 0 : arena.getWidth() - Measure.units(35f);
                float lockPosX = chestsAreLeft ? Measure.units(5f) : arena.getWidth() - Measure.units(30f);

                arena.addEntity(chestFactory.centeredChestBag(chestPosX, arena.getHeight() / 4));
                arena.addEntity(chestFactory.centeredChestBag(chest2PosX, arena.getHeight() / 4));

                arena.addEntity(decorFactory.lockWall(lockPosX, Measure.units(25f), Measure.units(25f), Measure.units(5f)));

               // arena.addEntity(decorFactory.wallBag(wallPosX, Measure.units(20f), Measure.units(35f), Measure.units(10f), arenaSkin.getWallTint()));

                //RoomDecorationFactory.spawnBlob(a);
                return arena;
            }
        };

    }



    public ArenaCreate room26DoubleAlurmWithASqaureCenter() {


        return new ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {


                Arena arena = arenaShellFactory.createOmniArenaSquareCenter(defaultCoords);

                arena.roomType = Arena.RoomType.TRAP;

                arena.addEntity(arenaEnemyPlacementFactory.spawnAlurm(arena.getWidth() / 4, Measure.units(45f)));
                arena.addEntity(arenaEnemyPlacementFactory.spawnAlurm(arena.getWidth() / 4 * 3, Measure.units(45f)));
                // arena.addEntity(decorFactory.wallBag(wallPosX, Measure.units(20f), Measure.units(35f), Measure.units(10f), arenaSkin.getWallTint()));

                //RoomDecorationFactory.spawnBlob(a);
                return arena;
            }
        };

    }




    public ArenaCreate room27Width2SwitchInTheCenter() {


        return new ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {


                Arena arena = new Arena(arenaSkin, defaultCoords,
                        new MapCoords(defaultCoords.getX() + 1, defaultCoords.getY()));

                arena =  new ArenaBuilder(assetManager, arenaSkin)
                        .addSection(new ArenaBuilder.Section(defaultCoords,
                                ArenaBuilder.wall.DOOR,
                                ArenaBuilder.wall.NONE,
                                ArenaBuilder.wall.NONE,
                                ArenaBuilder.wall.DOOR))
                        .addSection(new ArenaBuilder.Section(new MapCoords(defaultCoords.getX() + 1, defaultCoords.getY()),
                                ArenaBuilder.wall.NONE,
                                ArenaBuilder.wall.DOOR,
                                ArenaBuilder.wall.NONE,
                                ArenaBuilder.wall.DOOR))
                        .buildArena(arena);

                //Barriers //TODO add this into builder?
                arena.addEntity(decorFactory.wallBag(0, Measure.units(70f), arena.getWidth(), Measure.units(5f)));
                arena.addEntity(decorFactory.wallBag(0, 0, Measure.units(5), Measure.units(70f)));
                arena.addEntity(decorFactory.wallBag(0, arena.getWidth(), Measure.units(5), Measure.units(70f)));


                arena.roomType = Arena.RoomType.TRAP;


                LaserOrbitalTask.LaserBuilder lb = new LaserOrbitalTask.LaserBuilder(assetManager)
                        .chargeTime(0.5f)
                        .numberOfOrbitals(20)
                        .angles(180, 0)
                        .speedInDegrees(random.nextBoolean() ? 0.5f : -0.5f)
                        .expiryTime(5f)
                        //.orbitalSize(Measure.units(10f))
                        .orbitalAndIntervalSize(Measure.units(10f));

                arena.addEntity(decorFactory.inCombatTimedLaserChain(Measure.units(95f), Measure.units(55f), 2, 10,
                        lb.build()));


                arena.addEntity(arenaEnemyPlacementFactory.switchFactory.switchBag(Measure.units(97.5f), Measure.units(50f), 180));

                arena.addEntity(arenaEnemyPlacementFactory.switchFactory.switchBag(arena.getWidth() - Measure.units(10f), Measure.units(35f), -90));

                arena.addEntity(arenaEnemyPlacementFactory.switchFactory.switchBag(Measure.units(5f), Measure.units(35f), 90));
                //RoomDecorationFactory.spawnBlob(a);
                return arena;
            }
        };

    }

    public ArenaCreate room28Height3TwoLasersSwitchInTheCenter() {


        return new ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {


                Arena arena = new Arena(arenaSkin, defaultCoords,
                        new MapCoords(defaultCoords.getX(), defaultCoords.getY() + 1),
                        new MapCoords(defaultCoords.getX(), defaultCoords.getY() + 2));

                arena =  new ArenaBuilder(assetManager, arenaSkin)
                        .addSection(new ArenaBuilder.Section(defaultCoords,
                                ArenaBuilder.wall.FULL,
                                ArenaBuilder.wall.FULL,
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
                                ArenaBuilder.wall.MANDATORYDOOR,
                                ArenaBuilder.wall.NONE))
                        .buildArena(arena);



                for(int i = 0; i < 4; i++){
                    arena.addEntity(decorFactory.grapplePointBag(arena.getWidth() / 2, Measure.units(40f + (i * 37.5f))));
                   // arena.addEntity(decorFactory.grapplePointBag(arena.getWidth() - Measure.units(25f), Measure.units(40f + (i * 40f))));
                }

                arena.roomType = Arena.RoomType.TRAP;


                LaserOrbitalTask.LaserBuilder lb = new LaserOrbitalTask.LaserBuilder(assetManager)
                        .chargeTime(0.5f)
                        .numberOfOrbitals(20)
                        .angles(90)
                        //.orbitalSize(Measure.units(10f))
                        .orbitalAndIntervalSize(Measure.units(20f));

                arena.addEntity(decorFactory.laserChain(Measure.units(10f), -Measure.units(10f), 6,
                        lb.build()));

                arena.addEntity(decorFactory.laserChain(Measure.units(10f), arena.getHeight() - Measure.units(15f), 6,
                        new LaserOrbitalTask.LaserBuilder(assetManager).build()));

                arena.addEntity(decorFactory.laserChain(arena.getWidth() - Measure.units(40f), -Measure.units(10f), 6,
                        lb.build()));

                arena.addEntity(decorFactory.laserChain(arena.getWidth() - Measure.units(40f), arena.getHeight() - Measure.units(15f), 6,
                        new LaserOrbitalTask.LaserBuilder(assetManager).build()));


      /*          arena.addEntity(decorFactory.timedLaserChain(Measure.units(25f), Measure.units(40f), 6, 10,
                        lb.build()));*/

                return arena;
            }
        };

    }



    public ArenaCreate room29BoringHeight2TreasureRoom() {

        return new ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {

                //TODO make two doors mandatory


                Arena arena = new Arena(arenaSkin, defaultCoords, new MapCoords(defaultCoords.getX(), defaultCoords.getY() + 1));

                arena =  new ArenaBuilder(assetManager, arenaSkin)
                        .addSection(new ArenaBuilder.Section(defaultCoords,
                                ArenaBuilder.wall.FULL,
                                ArenaBuilder.wall.FULL,
                                ArenaBuilder.wall.NONE,
                                ArenaBuilder.wall.FULL))
                        .addSection(new ArenaBuilder.Section(new MapCoords(defaultCoords.getX(), defaultCoords.getY() + 1),
                                ArenaBuilder.wall.DOOR,
                                ArenaBuilder.wall.DOOR,
                                ArenaBuilder.wall.GRAPPLE,
                                ArenaBuilder.wall.NONE)).buildArena(arena);


                //for(int i = 0; i < 2; i++){

                    //float y = Measure.units(30f + (i * 35f));
                    float width = Measure.units(30f);

                    arena.addEntity(decorFactory.wallBag(arena.getWidth() - width - Measure.units(5f), Measure.units(30f),
                            width, Measure.units(40f), arenaSkin));

                    arena.addEntity(decorFactory.wallBag(Measure.units(5f), Measure.units(30f),
                            width, Measure.units(40f), arenaSkin));

                //}


                boolean isBottomLeftLock = false;

                arena.addEntity(decorFactory.grapplePointBag(arena.getWidth() / 2, Measure.units(55f)));

                arena.addEntity(decorFactory.lockWall(isBottomLeftLock ? Measure.units(30f) : Measure.units(65f), Measure.units(10f), Measure.units(5f), Measure.units(20f)));


                arena.addEntity(chestFactory.chestBag(isBottomLeftLock ? Measure.units(75f) : Measure.units(15f), Measure.units(10f)));

                arena.addEntity(chestFactory.chestBag(isBottomLeftLock ? Measure.units(6.5f) : arena.getWidth() - Measure.units(16.5f), Measure.units(10f)));
                arena.addEntity(chestFactory.chestBag(isBottomLeftLock ? Measure.units(18.5f) : arena.getWidth() - Measure.units(28.5f), Measure.units(10f)));




                LaserOrbitalTask.LaserBuilder lb = new LaserOrbitalTask.LaserBuilder(assetManager)
                        .chargeTime(0.5f)
                        .numberOfOrbitals(50)
                        .expiryTime(1.5f)
                        .angles(0)
                        //.orbitalSize(Measure.units(10f))
                        .orbitalAndIntervalSize(Measure.units(2.5f));

                LaserOrbitalTask.LaserBuilder empty = new LaserOrbitalTask.LaserBuilder(assetManager);


                for(int i = 0; i < 5; i++) {
                    arena.addEntity(decorFactory.timedLaserChain(Measure.units(32.5f), Measure.units(32.5f + (i * 7.5f)), 1, 3,
                            lb.build()));

                    arena.addEntity(decorFactory.laserChain(Measure.units(62.5f), Measure.units(32.5f + (i * 7.5f)), 1,
                            empty.build()));
                }


                return arena;





            }
        };

    }



    public ArenaCreate room30WidthTwoPylonsAlurmsAndKnight() {

        return new ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {

                //TODO make two doors mandatory


                Arena arena = new Arena(arenaSkin, defaultCoords, new MapCoords(defaultCoords.getX() + 1, defaultCoords.getY()));

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
                                ArenaBuilder.wall.DOOR)).buildArena(arena);


                arena.addEntity(arenaEnemyPlacementFactory.pylonFactory.pylonBag(Measure.units(95f), arena.getHeight() - Measure.units(15f), 180));
                arena.addEntity(arenaEnemyPlacementFactory.pylonFactory.pylonBag(arena.getWidth() - Measure.units(35f), arena.getHeight() - Measure.units(15f), 180));
                arena.addEntity(arenaEnemyPlacementFactory.pylonFactory.pylonBag(Measure.units(15f), arena.getHeight() - Measure.units(15f), 180));


                arena.addEntity(arenaEnemyPlacementFactory.spawnAlurm(Measure.units(15f), Measure.units(45f)));
                arena.addEntity(arenaEnemyPlacementFactory.spawnAlurm(arena.getWidth() - Measure.units(15f), Measure.units(45f)));


                return arena;





            }
        };

    }




}
