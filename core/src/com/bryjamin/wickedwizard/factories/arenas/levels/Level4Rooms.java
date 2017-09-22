package com.bryjamin.wickedwizard.factories.arenas.levels;

import com.artemis.Component;
import com.artemis.utils.Bag;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.utils.Array;
import com.bryjamin.wickedwizard.ecs.components.texture.TextureRegionComponent;
import com.bryjamin.wickedwizard.factories.AbstractFactory;
import com.bryjamin.wickedwizard.factories.arenas.Arena;
import com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder;
import com.bryjamin.wickedwizard.factories.arenas.decor.ArenaEnemyPlacementFactory;
import com.bryjamin.wickedwizard.factories.arenas.decor.BeamTurretFactory;
import com.bryjamin.wickedwizard.factories.arenas.skins.ArenaSkin;
import com.bryjamin.wickedwizard.factories.chests.ChestFactory;
import com.bryjamin.wickedwizard.factories.enemy.SpawnerFactory;
import com.bryjamin.wickedwizard.factories.explosives.BombFactory;
import com.bryjamin.wickedwizard.factories.weapons.enemy.LaserBeam;
import com.bryjamin.wickedwizard.utils.MapCoords;
import com.bryjamin.wickedwizard.utils.Measure;

import java.util.Random;

/**
 * Created by Home on 20/06/2017.
 */

public class Level4Rooms extends AbstractFactory implements ArenaRepostiory {

    private com.bryjamin.wickedwizard.factories.arenas.decor.ArenaShellFactory arenaShellFactory;
    private ChestFactory chestFactory;
    private com.bryjamin.wickedwizard.factories.arenas.decor.DecorFactory decorFactory;
    private ArenaEnemyPlacementFactory arenaEnemyPlacementFactory;
    private BombFactory bombFactory;
    private BeamTurretFactory beamTurretFactory;
    private Random random;
    private ArenaSkin arenaSkin;

    public Level4Rooms(AssetManager assetManager, ArenaSkin arenaSkin, Random random) {
        super(assetManager);
        this.arenaShellFactory = new com.bryjamin.wickedwizard.factories.arenas.decor.ArenaShellFactory(assetManager, arenaSkin);
        this.chestFactory = new ChestFactory(assetManager);
        this.arenaEnemyPlacementFactory = new ArenaEnemyPlacementFactory(assetManager, arenaSkin, random);
        this.decorFactory = new com.bryjamin.wickedwizard.factories.arenas.decor.DecorFactory(assetManager, arenaSkin);
        this.beamTurretFactory = new BeamTurretFactory(assetManager, arenaSkin);
        this.bombFactory = new BombFactory(assetManager);
        this.random = random;
        this.arenaSkin = arenaSkin;
    }


    @Override
    public Array<com.bryjamin.wickedwizard.factories.arenas.ArenaCreate> getAllArenas() {
        return  getLevel4RoomArray();
    }

    public Array<com.bryjamin.wickedwizard.factories.arenas.ArenaCreate> getLevel4RoomArray() {
        Array<com.bryjamin.wickedwizard.factories.arenas.ArenaCreate> ag = new Array<com.bryjamin.wickedwizard.factories.arenas.ArenaCreate>();

        ag.insert(0, room1LaserBouncers());
        ag.insert(1, room2FlyByAndPylons());
        ag.insert(2, room3BouncersAndVerticalPylons());
        ag.insert(3, room4FixedTriAndVerticalPylons());
        ag.insert(4, room5Alurm());
        ag.insert(5, room6Knight());
        ag.insert(6, room72KnightsWidth2());
        ag.insert(7, room8LaserBouncersAndAWall());
        ag.insert(8, room9TrappedLaserBouncer());
        ag.insert(9, room10Width4GrappleChallenge());
        ag.insert(10,room11Width2STrapTwoKnights());
        ag.insert(11,room12TwoAlurmsOneChestOneKnight());
        ag.insert(12,room13largeBattleRoomWithArenaOnTopAndSpikeTreasureOnBottom());
        ag.insert(13, room14LargeBattleRoomWithEnemiesOnTopAndAMassiveLaser());
        ag.insert(14,room15SmallBattleRoomBlobAndLaser());
        ag.insert(15,room16SwitchAndLasers());
        ag.insert(16,room17Height2SwitchAndLasers()); // Might need to refactor this one
        ag.insert(17,room18SwitchsAndLasersAgain());
        ag.insert(18,room19GoatWizardAndKnight());
        ag.insert(19,room20AlurmAndFlyBy());
        ag.insert(20,room21LaserArenaWithIntermittantLasters()); //I'll come back to this
        ag.insert(21, room22HorizontalThroughRoomLaserCentersAndEnemies()); //Also this one
        ag.insert(22, room23HoarderWithLasers()); //Also this one
        ag.insert(23,room24LaserBouncerLaserTrapTreasure());
        ag.insert(24,room25TreasureLaserFloorAndLockedChestMirror()); //This could be a place for rain. Or a bit backdrop
        ag.insert(25,room26DoubleAlurmWithASqaureCenter());
        ag.insert(26, room27Width2GiantLaserSwitchInTheCenter());
        ag.insert(27, room28Height3TwoGiantScenicLasers());
        ag.insert(28, room29BoringHeight2TreasureRoomWithLasers());
        ag.insert(29, room30WidthTwoPylonsAndAlurms());

        return ag;
    }



    public com.bryjamin.wickedwizard.factories.arenas.ArenaCreate room1LaserBouncers() {
        return new com.bryjamin.wickedwizard.factories.arenas.ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {
                Arena arena = arenaShellFactory.createOmniArenaHiddenGrapple(defaultCoords, Arena.ArenaType.TRAP);
                arena.addEntity(arenaEnemyPlacementFactory.spawnLaserBouncer(arena.getWidth() / 4, Measure.units(45f), random.nextBoolean()));
                arena.addEntity(arenaEnemyPlacementFactory.spawnBouncer(arena.getWidth() / 4 * 3, Measure.units(45f)));
                return arena;
            }
        };
    }


    public com.bryjamin.wickedwizard.factories.arenas.ArenaCreate room2FlyByAndPylons() {
        return new com.bryjamin.wickedwizard.factories.arenas.ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {
                Arena arena = arenaShellFactory.createOmniArenaHiddenGrapple(defaultCoords, Arena.ArenaType.TRAP);
                arena.addEntity(arenaEnemyPlacementFactory.pylonFactory.pylonBag(arena.getWidth() - Measure.units(15f), Measure.units(30f), 90));
                arena.addEntity(arenaEnemyPlacementFactory.pylonFactory.pylonBag(Measure.units(5f), Measure.units(30f), -90));
                arena.addEntity(arenaEnemyPlacementFactory.spawnMovingFlyByBombSentry(arena.getWidth() / 2, Measure.units(45f)));
                return arena;
            }
        };
    }




    public com.bryjamin.wickedwizard.factories.arenas.ArenaCreate room3BouncersAndVerticalPylons() {
        return new com.bryjamin.wickedwizard.factories.arenas.ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {
                Arena arena = arenaShellFactory.createOmniArenaHiddenGrapple(defaultCoords, Arena.ArenaType.TRAP);
                arena.addEntity(arenaEnemyPlacementFactory.pylonFactory.pylonBag(arena.getWidth() - Measure.units(25f), arena.getHeight() - Measure.units(15f), 180));
                arena.addEntity(arenaEnemyPlacementFactory.pylonFactory.pylonBag(Measure.units(15f), arena.getHeight() - Measure.units(15f), 180));
                arena.addEntity(arenaEnemyPlacementFactory.spawnBouncer(arena.getWidth() / 4 * 3, arena.getHeight() / 2));
                arena.addEntity(arenaEnemyPlacementFactory.spawnBouncer(arena.getWidth() / 4  , arena.getHeight() / 2));
                return arena;
            }
        };
    }


    public com.bryjamin.wickedwizard.factories.arenas.ArenaCreate room4FixedTriAndVerticalPylons() {
        return new com.bryjamin.wickedwizard.factories.arenas.ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {
                Arena arena = arenaShellFactory.createOmniArenaHiddenGrapple(defaultCoords, Arena.ArenaType.TRAP);
                arena.addEntity(arenaEnemyPlacementFactory.pylonFactory.pylonBag(arena.getWidth() - Measure.units(25f), arena.getHeight() - Measure.units(15f), 180));
                arena.addEntity(arenaEnemyPlacementFactory.pylonFactory.pylonBag(Measure.units(15f), arena.getHeight() - Measure.units(15f), 180));
                arena.addEntity(arenaEnemyPlacementFactory.spawnFixedSentry(arena.getWidth() / 2, Measure.units(45f)));
                return arena;
            }
        };
    }



    public com.bryjamin.wickedwizard.factories.arenas.ArenaCreate room5Alurm() {
        return new com.bryjamin.wickedwizard.factories.arenas.ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {
                Arena arena = arenaShellFactory.createOmniArenaHiddenGrapple(defaultCoords, Arena.ArenaType.TRAP);
                arena.addEntity(arenaEnemyPlacementFactory.spawnAlurm(arena.getWidth() / 2, Measure.units(45f)));
                return arena;
            }
        };
    }



    public com.bryjamin.wickedwizard.factories.arenas.ArenaCreate room6Knight() {
        return new com.bryjamin.wickedwizard.factories.arenas.ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {
                Arena arena = arenaShellFactory.createOmniArenaHiddenGrapple(defaultCoords, Arena.ArenaType.TRAP);
                arena.addEntity(arenaEnemyPlacementFactory.spawnKnight(arena.getWidth() / 2, Measure.units(40f), random.nextBoolean(), random.nextBoolean()));
                return arena;
            }
        };
    }



    public com.bryjamin.wickedwizard.factories.arenas.ArenaCreate room72KnightsWidth2() {
        return new com.bryjamin.wickedwizard.factories.arenas.ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {

                Arena arena =  new ArenaBuilder(assetManager, arenaSkin, Arena.ArenaType.TRAP,
                        new ArenaBuilder.Section(defaultCoords,
                                ArenaBuilder.wall.DOOR,
                                ArenaBuilder.wall.NONE,
                                ArenaBuilder.wall.GRAPPLE,
                                ArenaBuilder.wall.DOOR),
                        new ArenaBuilder.Section(new MapCoords(defaultCoords.getX() + 1, defaultCoords.getY()),
                                ArenaBuilder.wall.NONE,
                                ArenaBuilder.wall.DOOR,
                                ArenaBuilder.wall.GRAPPLE,
                                ArenaBuilder.wall.DOOR))
                        .buildArena();

                arena.addEntity(arenaEnemyPlacementFactory.spawnKnight(arena.getWidth() / 4, Measure.units(45f), random.nextBoolean(), random.nextBoolean()));
                arena.addEntity(arenaEnemyPlacementFactory.spawnKnight(arena.getWidth() / 4 * 3, Measure.units(45f), random.nextBoolean(), random.nextBoolean()));

                return arena;
            }
        };
    }



    public com.bryjamin.wickedwizard.factories.arenas.ArenaCreate room8LaserBouncersAndAWall() {
        return new com.bryjamin.wickedwizard.factories.arenas.ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {

                Arena arena = arenaShellFactory.createOmniArenaSquareCenter(defaultCoords, Arena.ArenaType.TRAP);

                arena.addEntity(arenaEnemyPlacementFactory.spawnLaserBouncer(arena.getWidth() / 2, Measure.units(45f), random.nextBoolean()));

                arena.addEntity(decorFactory.wallBag(Measure.units(20f), Measure.units(25f), Measure.units(5f), Measure.units(15f)));
                arena.addEntity(decorFactory.wallBag(arena.getWidth() - Measure.units(25f), Measure.units(25f), Measure.units(5f), Measure.units(15f)));

                return arena;
            }
        };
    }


    public com.bryjamin.wickedwizard.factories.arenas.ArenaCreate room9TrappedLaserBouncer() {
        return new com.bryjamin.wickedwizard.factories.arenas.ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {

                Arena arena = arenaShellFactory.createSmallArenaNoGrapple(defaultCoords, random.nextInt(), Arena.ArenaType.NORMAL);

                arena.addEntity(arenaEnemyPlacementFactory.spawnLaserBouncer(arena.getWidth() / 2, Measure.units(47.5f), random.nextBoolean()));
                //arena.addEntity(arenaEnemyPlacementFactory.spawnBouncer(arena.getWidth() / 4 * 3, Measure.units(45f)));

                arena.addEntity(decorFactory.wallBag(Measure.units(40f), Measure.units(20f), Measure.units(20f), Measure.units(5f)));
                arena.addEntity(decorFactory.wallBag(Measure.units(40f), Measure.units(35f), Measure.units(20f), Measure.units(5f)));

                arena.addEntity(decorFactory.wallBag(Measure.units(35f), Measure.units(20f), Measure.units(5f), Measure.units(20f)));
                arena.addEntity(decorFactory.wallBag(arena.getWidth() - Measure.units(40f), Measure.units(20f), Measure.units(5f), Measure.units(20f)));


                return arena;
            }
        };
    }


    public com.bryjamin.wickedwizard.factories.arenas.ArenaCreate room10Width4GrappleChallenge() {
        return new com.bryjamin.wickedwizard.factories.arenas.ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {

                Arena arena =  new ArenaBuilder(assetManager, arenaSkin, Arena.ArenaType.NORMAL)
                        .addSection(new ArenaBuilder.Section(defaultCoords,
                                ArenaBuilder.wall.MANDATORYDOOR,
                                ArenaBuilder.wall.NONE,
                                ArenaBuilder.wall.FULL, //upperDoorIsLeft ? ArenaBuilder.wall.DOOR : ArenaBuilder.wall.FULL,
                                ArenaBuilder.wall.NONE))
                        .addSection(new ArenaBuilder.Section(new MapCoords(defaultCoords.getX() + 1, defaultCoords.getY()),
                                ArenaBuilder.wall.NONE ,
                                ArenaBuilder.wall.NONE,
                                ArenaBuilder.wall.FULL,
                                ArenaBuilder.wall.NONE))
                        .addSection(new ArenaBuilder.Section(new MapCoords(defaultCoords.getX() + 2, defaultCoords.getY()),
                                ArenaBuilder.wall.NONE ,
                                ArenaBuilder.wall.NONE,
                                ArenaBuilder.wall.FULL,//!upperDoorIsLeft ? ArenaBuilder.wall.DOOR : ArenaBuilder.wall.FULL,
                                ArenaBuilder.wall.NONE))
                        .addSection(new ArenaBuilder.Section(new MapCoords(defaultCoords.getX() + 3, defaultCoords.getY()),
                                ArenaBuilder.wall.NONE ,
                                ArenaBuilder.wall.MANDATORYDOOR,
                                ArenaBuilder.wall.FULL,//!upperDoorIsLeft ? ArenaBuilder.wall.DOOR : ArenaBuilder.wall.FULL,
                                ArenaBuilder.wall.NONE))
                        .buildArena();


                float bottomWallWidth = Measure.units(45f);

                arena.addEntity(decorFactory.wallBag(Measure.units(0), Measure.units(-5f), arena.getWidth(), Measure.units(5)));

                //left
                arena.addEntity(decorFactory.wallBag(Measure.units(5), Measure.units(0), bottomWallWidth, Measure.units(10f)));
                arena.addEntity(decorFactory.wallBag(arena.getWidth() - bottomWallWidth - Measure.units(5f), Measure.units(0), bottomWallWidth, Measure.units(10f)));

                arena.addEntity(decorFactory.spikeWall(0, 0, arena.getWidth(), Measure.units(5f), 0));

                for(int i = 0; i < 5; i++) arena.addEntity(decorFactory.grapplePointBag(Measure.units(100 + (i * 50)), Measure.units(35f)));

                //Used for start direction of mine
                int mod  = random.nextBoolean() ? 0 : 1;

                for(int i = 0; i <= 7; i++) {
                    arena.addEntity(arenaEnemyPlacementFactory.bombFactory.verticalSeaMine(Measure.units(20) + Measure.units(i * 50f), Measure.units(25f), i % 2 == mod));
                }


                return arena;
            }
        };
    }



    public com.bryjamin.wickedwizard.factories.arenas.ArenaCreate room11Width2STrapTwoKnights() {
        return new com.bryjamin.wickedwizard.factories.arenas.ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {


                boolean chestsAreLeft = random.nextBoolean();

                Arena arena = new ArenaBuilder(assetManager, arenaSkin, Arena.ArenaType.NORMAL)
                        .addSection(new ArenaBuilder.Section(defaultCoords,
                                chestsAreLeft ? ArenaBuilder.wall.FULL : ArenaBuilder.wall.DOOR,
                                chestsAreLeft ? ArenaBuilder.wall.NONE : ArenaBuilder.wall.NONE,
                                chestsAreLeft ? ArenaBuilder.wall.FULL : ArenaBuilder.wall.GRAPPLE,
                                chestsAreLeft ? ArenaBuilder.wall.FULL : ArenaBuilder.wall.DOOR))
                        .addSection(new ArenaBuilder.Section(new MapCoords(defaultCoords.getX() + 1, defaultCoords.getY()),
                                chestsAreLeft ? ArenaBuilder.wall.NONE : ArenaBuilder.wall.NONE,
                                chestsAreLeft ? ArenaBuilder.wall.DOOR : ArenaBuilder.wall.FULL,
                                chestsAreLeft ? ArenaBuilder.wall.GRAPPLE : ArenaBuilder.wall.FULL,
                                chestsAreLeft ? ArenaBuilder.wall.DOOR : ArenaBuilder.wall.FULL)).buildArena();


                arena.addWave(
                        arenaEnemyPlacementFactory.spawnKnight(chestsAreLeft ? arena.getWidth() - Measure.units(20f) : Measure.units(20f),
                                Measure.units(45f), chestsAreLeft, true),
                        arenaEnemyPlacementFactory.spawnKnight(chestsAreLeft ? arena.getWidth() - Measure.units(20f) : Measure.units(20f), Measure.units(20f), chestsAreLeft, true)
                );


                arena.addEntity(new ChestFactory(assetManager).chestBag(chestsAreLeft ? Measure.units(40f) : Measure.units(150f), Measure.units(10f),
                        chestFactory.trapODAC()));

                arena.addEntity(new ChestFactory(assetManager).chestBag(chestsAreLeft ? Measure.units(25f) : Measure.units(165f), Measure.units(10f),
                        chestFactory.trapODAC()));

                return arena;

            }

        };
    }



    public com.bryjamin.wickedwizard.factories.arenas.ArenaCreate room12TwoAlurmsOneChestOneKnight() {
        return new com.bryjamin.wickedwizard.factories.arenas.ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {
                Arena arena = arenaShellFactory.createOmniArenaHiddenGrapple(defaultCoords, random.nextInt(4), Arena.ArenaType.TRAP);

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


    public com.bryjamin.wickedwizard.factories.arenas.ArenaCreate room13largeBattleRoomWithArenaOnTopAndSpikeTreasureOnBottom(){
        return new com.bryjamin.wickedwizard.factories.arenas.ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {

                Arena arena =  new ArenaBuilder(assetManager, arenaSkin, Arena.ArenaType.NORMAL)
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
                                ArenaBuilder.wall.NONE)).buildArena();



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


                float bottomWallWidth = Measure.units(30f);

                //Bottom
                arena.addEntity(decorFactory.wallBag(Measure.units(0), Measure.units(-5f), arena.getWidth(), Measure.units(5)));
                arena.addEntity(decorFactory.wallBag(Measure.units(5), Measure.units(0), bottomWallWidth, Measure.units(10f)));
                arena.addEntity(decorFactory.wallBag(arena.getWidth() - bottomWallWidth - Measure.units(5), Measure.units(0), bottomWallWidth, Measure.units(10f)));
                arena.addEntity(decorFactory.spikeWall(0, 0, arena.getWidth(), Measure.units(5f), 0));

                float midWallHeight = Measure.units(50f);

                arena.addEntity(chestFactory.chestBag(Measure.units(95), midWallHeight));
                arena.addEntity(decorFactory.wallBag(Measure.units(90f), Measure.units(0f), Measure.units(20f), midWallHeight));
                arena.addEntity(bombFactory.mine(Measure.units(90f), midWallHeight, 0));
                arena.addEntity(bombFactory.mine(Measure.units(105f), midWallHeight, 0));


                return arena;
            }
        };
    }




    public com.bryjamin.wickedwizard.factories.arenas.ArenaCreate room14LargeBattleRoomWithEnemiesOnTopAndAMassiveLaser(){
        return new com.bryjamin.wickedwizard.factories.arenas.ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {

                Arena arena =  new ArenaBuilder(assetManager, arenaSkin, Arena.ArenaType.TRAP)
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
                                ArenaBuilder.wall.GRAPPLE,
                                ArenaBuilder.wall.NONE))
                        .addSection(new ArenaBuilder.Section(new MapCoords(defaultCoords.getX() + 1, defaultCoords.getY() + 1),
                                ArenaBuilder.wall.NONE,
                                ArenaBuilder.wall.FULL,
                                ArenaBuilder.wall.GRAPPLE,
                                ArenaBuilder.wall.NONE)).buildArena();


                arena.addEntity(decorFactory.grapplePointBag(Measure.units(30f), Measure.units(50f)));
                arena.addEntity(decorFactory.grapplePointBag(Measure.units(30f), Measure.units(95f)));
                // arena.addEntity(decorFactory.grapplePointBag(Measure.units(30f), Measure.units(110f)));
                arena.addEntity(decorFactory.grapplePointBag(arena.getWidth() - Measure.units(35f), Measure.units(50f)));
                arena.addEntity(decorFactory.grapplePointBag(arena.getWidth() - Measure.units(35f), Measure.units(95f)));
                //arena.addEntity(decorFactory.grapplePointBag(arena.getWidth() - Measure.units(35f), Measure.units(110f)));
                arena.addEntity(decorFactory.platform(0, Measure.units(65f), arena.getWidth()));

                arena.addEntity(beamTurretFactory.inCombatLaserChain(Measure.units(90f), Measure.units(50f),4,
                        new com.bryjamin.wickedwizard.factories.weapons.enemy.LaserOrbitalTask.LaserBuilder(assetManager)
                                .orbitalAndIntervalSize(Measure.units(15f))
                                .speedInDegrees(random.nextBoolean() ? 0.225f : -0.225f)
                                .numberOfOrbitals(10)
                                .chargeTime(0.5f)
                                .disperseTime(1f)
                                .angles(90, 270)
                                .build()));


                arena.addEntity(random.nextBoolean() ?
                        arenaEnemyPlacementFactory.spawnFixedFlyByBombSentry(arena.getWidth() / 2, Measure.units(35f)) :
                        arenaEnemyPlacementFactory.spawnFixedTriSentry(arena.getWidth() / 2, Measure.units(35f)));


                arena.addEntity(arenaEnemyPlacementFactory.spawnFixedFlyByBombSentry(arena.getWidth() / 2, Measure.units(100f)));



                return arena;
            }
        };
    }


    public com.bryjamin.wickedwizard.factories.arenas.ArenaCreate room15SmallBattleRoomBlobAndLaser(){
        return new com.bryjamin.wickedwizard.factories.arenas.ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {

                Arena arena = arenaShellFactory.createSmallArenaNoGrapple(defaultCoords, random.nextInt(4), Arena.ArenaType.TRAP);

                boolean isLeft = random.nextBoolean();

                arena.addEntity(beamTurretFactory.inCombatLaserChain(Measure.units(45f), Measure.units(25f),2,
                        new com.bryjamin.wickedwizard.factories.weapons.enemy.LaserOrbitalTask.LaserBuilder(assetManager)
                                .orbitalAndIntervalSize(Measure.units(3f))
                                .speedInDegrees(isLeft ? 0.6f : -0.6f)
                                .numberOfOrbitals(20)
                                .chargeTime(1f)
                                .angles(isLeft ? 0 : 180)
                                .build()));

                arena.addEntity(arenaEnemyPlacementFactory.spawnAngryBlob(Measure.units(50f), Measure.units(45f), !isLeft));

                return arena;
            }
        };
    }



    public com.bryjamin.wickedwizard.factories.arenas.ArenaCreate room16SwitchAndLasers(){
        return new com.bryjamin.wickedwizard.factories.arenas.ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {

                Arena arena = arenaShellFactory.createSmallArenaNoGrapple(defaultCoords, random.nextInt(4), Arena.ArenaType.TRAP);

                arena.addEntity(beamTurretFactory.inCombatLaserChain(Measure.units(45f), Measure.units(25f),2,
                        new com.bryjamin.wickedwizard.factories.weapons.enemy.LaserOrbitalTask.LaserBuilder(assetManager)
                                .orbitalAndIntervalSize(Measure.units(2.5f))
                                .speedInDegrees(random.nextBoolean() ? 1f : -1f)
                                .numberOfOrbitals(25)
                                .chargeTime(0.5f)
                                .angles(0, 180)
                                .build()));

                arena.addEntity(arenaEnemyPlacementFactory.switchFactory.switchBag(Measure.units(5f), Measure.units(40f), -90));
                arena.addEntity(arenaEnemyPlacementFactory.switchFactory.switchBag(arena.getWidth() - Measure.units(10f), Measure.units(40f), 90));
                arena.addEntity(arenaEnemyPlacementFactory.switchFactory.switchBag(Measure.units(40f), Measure.units(27.5f), 90));
                arena.addEntity(arenaEnemyPlacementFactory.switchFactory.switchBag(Measure.units(55f), Measure.units(27.5f), -90));

                return arena;
            }
        };
    }




    public com.bryjamin.wickedwizard.factories.arenas.ArenaCreate room17Height2SwitchAndLasers(){
        return new com.bryjamin.wickedwizard.factories.arenas.ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {

                Arena arena =  new ArenaBuilder(assetManager, arenaSkin, Arena.ArenaType.TRAP)
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
                        .buildArena();


                int numberOfParts = 15;

                com.bryjamin.wickedwizard.factories.weapons.enemy.LaserOrbitalTask.LaserBuilder laserBuilder = new com.bryjamin.wickedwizard.factories.weapons.enemy.LaserOrbitalTask.LaserBuilder(assetManager)
                        .orbitalAndIntervalSize(Measure.units(2.5f))
                        .speedInDegrees(0)
                        .numberOfOrbitals(numberOfParts)
                        .chargeTime(0.25f);

                arena.addEntity(beamTurretFactory.inCombatLaserChain(Measure.units(30f), Measure.units(65f),2,
                        laserBuilder.angles(180).build()));

                arena.addEntity(beamTurretFactory.inCombatLaserChain(Measure.units(60f), Measure.units(65f),2,
                        laserBuilder.angles(0).build()));


                arena.addEntity(beamTurretFactory.inCombatLaserChain(Measure.units(30f), Measure.units(30f),2,
                        laserBuilder.angles(180).build()));

                arena.addEntity(beamTurretFactory.inCombatLaserChain(Measure.units(60f), Measure.units(30f),2,
                        laserBuilder.angles(0).build()));


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

                return arena;
            }
        };
    }




    //TODO check
    public com.bryjamin.wickedwizard.factories.arenas.ArenaCreate room18SwitchsAndLasersAgain(){
        return new com.bryjamin.wickedwizard.factories.arenas.ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {

                Arena arena = arenaShellFactory.createSmallArenaNoGrapple(defaultCoords, random.nextInt(4), Arena.ArenaType.TRAP);

                arena.addEntity(beamTurretFactory.inCombatLaserChain(Measure.units(45f), Measure.units(25f),2,
                        new com.bryjamin.wickedwizard.factories.weapons.enemy.LaserOrbitalTask.LaserBuilder(assetManager)
                                .orbitalAndIntervalSize(Measure.units(4f))
                                .speedInDegrees(random.nextBoolean() ? 1f : -1f)
                                .numberOfOrbitals(15)
                                .chargeTime(0.5f)
                                .angles(0, 180)
                                .build()));


                arena.addWave(arenaEnemyPlacementFactory.switchFactory.fadeInswitchBag(Measure.units(5f), Measure.units(40f), -90));
                arena.addWave(arenaEnemyPlacementFactory.switchFactory.fadeInswitchBag(arena.getWidth() - Measure.units(10f), Measure.units(40f), 90));
                arena.addWave(arenaEnemyPlacementFactory.switchFactory.fadeInswitchBag(Measure.units(40f), Measure.units(27.5f), 90));
                arena.addWave(arenaEnemyPlacementFactory.switchFactory.fadeInswitchBag(Measure.units(5f), Measure.units(20f), -90));
                arena.addWave(arenaEnemyPlacementFactory.switchFactory.fadeInswitchBag(Measure.units(55f), Measure.units(27.5f), -90));
                arena.addWave(arenaEnemyPlacementFactory.switchFactory.fadeInswitchBag(Measure.units(20f), Measure.units(50f), 180));
                arena.addWave(arenaEnemyPlacementFactory.switchFactory.fadeInswitchBag(arena.getWidth() - Measure.units(25f), Measure.units(50f), 180));
                arena.addWave(arenaEnemyPlacementFactory.switchFactory.fadeInswitchBag(arena.getWidth() - Measure.units(10f), Measure.units(20f), 90));
                arena.shuffleWaves();

                arena.addWave(chestFactory.centeredChestBag(arena.getWidth() / 2, Measure.units(45f)));

                return arena;
            }
        };
    }



    public com.bryjamin.wickedwizard.factories.arenas.ArenaCreate room19GoatWizardAndKnight(){
        return new com.bryjamin.wickedwizard.factories.arenas.ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {

                Arena arena = arenaShellFactory.createOmniArenaHiddenGrapple(defaultCoords, random.nextInt(4), Arena.ArenaType.TRAP);

                boolean bool = random.nextBoolean() ;

                arena.addWave(arenaEnemyPlacementFactory.spawnGoatWizard(bool ? arena.getWidth() / 4 * 3 : arena.getWidth() / 4, Measure.units(45f)));


                arena.addWave(arenaEnemyPlacementFactory.spawnKnight(bool ? arena.getWidth() / 4 : arena.getWidth() / 4 * 3, Measure.units(45f)));


                arena.addEntity(chestFactory.chestBag(Measure.units(45f), Measure.units(10f), chestFactory.trapODAC()));

                return arena;
            }
        };
    }


    public com.bryjamin.wickedwizard.factories.arenas.ArenaCreate room20AlurmAndFlyBy(){
        return new com.bryjamin.wickedwizard.factories.arenas.ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {

                Arena arena = arenaShellFactory.createOmniArenaHiddenGrapple(defaultCoords, random.nextInt(4), Arena.ArenaType.TRAP);

                boolean bool = random.nextBoolean();

                arena.addWave(arenaEnemyPlacementFactory.spawnMovingFlyByBombSentry(bool ? arena.getWidth() / 4 * 3 : arena.getWidth() / 4, Measure.units(45f)),
                        arenaEnemyPlacementFactory.spawnAlurm(bool ? arena.getWidth() / 4 : arena.getWidth() / 4 * 3, Measure.units(45f)));


                return arena;
            }
        };
    }



    public com.bryjamin.wickedwizard.factories.arenas.ArenaCreate room21LaserArenaWithIntermittantLasters() {
        return new com.bryjamin.wickedwizard.factories.arenas.ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {

                Arena arena = arenaShellFactory.createOmniArenaHiddenGrapple(defaultCoords, random.nextInt(4), Arena.ArenaType.TRAP);

                LaserBeam laserBeam = new LaserBeam.LaserBeamBuilder(assetManager)
                        .chargingLaserHeight(Measure.units(50f))
                        .chargingLaserWidth(Measure.units(3.5f))
                        .activeLaserHeight(Measure.units(50f))
                        .activeLaserWidth(Measure.units(5f))
                        .layer(TextureRegionComponent.ENEMY_LAYER_FAR)
                        .build();


                arena.addEntity(beamTurretFactory.inCombatTimedLaserBeam(Measure.units(20f), Measure.units(52.5f), 1, -Measure.units(50f), 3f,
                        laserBeam));

                arena.addEntity(beamTurretFactory.inCombatTimedLaserBeam(arena.getWidth() - Measure.units(25f), Measure.units(52.5f), 1,-Measure.units(50f), 3f,
                        laserBeam));

                arena.addEntity(arenaEnemyPlacementFactory.spawnFixedTriSentry(arena.getWidth() / 2, Measure.units(45f)));
                //arena.addEntity(arenaEnemyPlacementFactory.spawnBouncer(arena.getWidth() / 2, Measure.units(45f)));

                return arena;
            }
        };
    }


    public com.bryjamin.wickedwizard.factories.arenas.ArenaCreate room22HorizontalThroughRoomLaserCentersAndEnemies() {

        return new com.bryjamin.wickedwizard.factories.arenas.ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {

                Arena arena =  new ArenaBuilder(assetManager, arenaSkin, Arena.ArenaType.TRAP)
                        .addSection(new ArenaBuilder.Section(defaultCoords,
                                ArenaBuilder.wall.MANDATORYDOOR,
                                ArenaBuilder.wall.MANDATORYDOOR,
                                ArenaBuilder.wall.FULL,
                                ArenaBuilder.wall.FULL))
                        .buildArena();

                LaserBeam laserBeam = new LaserBeam.LaserBeamBuilder(assetManager)
                        .chargingLaserHeight(Measure.units(50f))
                        .chargingLaserWidth(Measure.units(3.5f))
                        .activeLaserHeight(Measure.units(50f))
                        .activeLaserWidth(Measure.units(5f))
                        .layer(TextureRegionComponent.ENEMY_LAYER_FAR)
                        .build();


                for(int i = 0; i < 4; i++) arena.addEntity(beamTurretFactory.inCombatTimedLaserBeam(Measure.units(35f + (i * 7.5f)), Measure.units(52.5f), 1, -Measure.units(50f), 4f, true,
                        laserBeam));

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
    public com.bryjamin.wickedwizard.factories.arenas.ArenaCreate room23HoarderWithLasers() {


        return new com.bryjamin.wickedwizard.factories.arenas.ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {

                Arena arena = arenaShellFactory.createSmallArenaNoGrapple(defaultCoords, Arena.ArenaType.TRAP);

                arena.addEntity(decorFactory.wallBag(Measure.units(20f), Measure.units(30f), Measure.units(60f), Measure.units(5f)));

                boolean variation = random.nextBoolean();

                if(variation) arena.addEntity(arenaEnemyPlacementFactory.hoarderFactory.hoarder(Measure.units(50f), Measure.units(45f)));
                else  arena.addEntity(arenaEnemyPlacementFactory.hoarderFactory.hoarder(Measure.units(50f), Measure.units(17.5f)));


                arena.addEntity(chestFactory.chestBag(Measure.units(30f), Measure.units(35f)));

                arena.addEntity(chestFactory.chestBag(Measure.units(45f), Measure.units(35f)));

                arena.addEntity(chestFactory.chestBag(Measure.units(60f), Measure.units(35f)));
                //arena.arenaType = Arena.ArenaType.TRAP;


                LaserBeam laserBeam = new LaserBeam.LaserBeamBuilder(assetManager)
                        .chargingLaserHeight(Measure.units(2f))
                        .chargingLaserWidth(Measure.units(15f))
                        .activeLaserHeight(Measure.units(2.5f))
                        .activeLaserWidth(Measure.units(15f))
                        .centerLaserUsingWidth(false)
                        .layer(TextureRegionComponent.ENEMY_LAYER_FAR)
                        .build();


                arena.addEntity(beamTurretFactory.timedLaserBeam(Measure.units(17.5f), Measure.units(31.25f), 0.5f, -Measure.units(15f), 3f,
                        laserBeam));

                arena.addEntity(beamTurretFactory.timedLaserBeam(Measure.units(80f), Measure.units(31.25f), 0.5f, Measure.units(0), 3f,
                        laserBeam));


                return arena;


            }

        };

    }



    //TODO add the chance of a booby trap chest
    public com.bryjamin.wickedwizard.factories.arenas.ArenaCreate room24LaserBouncerLaserTrapTreasure() {


        return new com.bryjamin.wickedwizard.factories.arenas.ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {

                Arena arena =  new ArenaBuilder(assetManager, arenaSkin, Arena.ArenaType.NORMAL)
                        .addSection(new ArenaBuilder.Section(defaultCoords,
                                ArenaBuilder.wall.DOOR,
                                ArenaBuilder.wall.DOOR,
                                ArenaBuilder.wall.FULL,
                                ArenaBuilder.wall.FULL))
                        .buildArena();

                arena.addEntity(chestFactory.chestBag(Measure.units(45f), Measure.units(10f), chestFactory.trapODAC()));

                arena.addWave(arenaEnemyPlacementFactory.spawnLaserBouncer(arena.getWidth() / 2, Measure.units(45f)));

                LaserBeam laserBeam = new LaserBeam.LaserBeamBuilder(assetManager)
                        .chargingLaserHeight(Measure.units(30f))
                        .chargingLaserWidth(Measure.units(3.5f))
                        .activeLaserHeight(Measure.units(30f))
                        .activeLaserWidth(Measure.units(5f))
                        .layer(TextureRegionComponent.ENEMY_LAYER_FAR)
                        .build();


                boolean mirror = random.nextBoolean();

                arena.addEntity(beamTurretFactory.timedLaserBeam(mirror ? Measure.units(25f) : arena.getWidth() - Measure.units(35f), Measure.units(30f), 2, Measure.units(0f), 3f,
                        laserBeam));

                arena.addEntity(beamTurretFactory.timedLaserBeam(mirror ? arena.getWidth() - Measure.units(35f) : Measure.units(25f), Measure.units(25f), 2, -Measure.units(30f), 3f,
                        laserBeam));

                return arena;


            }

        };

    }


    public com.bryjamin.wickedwizard.factories.arenas.ArenaCreate room25TreasureLaserFloorAndLockedChestMirror() {


        return new com.bryjamin.wickedwizard.factories.arenas.ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {

                boolean chestsAreLeft = random.nextBoolean();

                Arena arena =  new ArenaBuilder(assetManager, arenaSkin, Arena.ArenaType.NORMAL)
                        .addSection(new ArenaBuilder.Section(defaultCoords,
                                chestsAreLeft ? ArenaBuilder.wall.FULL : ArenaBuilder.wall.DOOR,
                                chestsAreLeft ? ArenaBuilder.wall.DOOR : ArenaBuilder.wall.FULL,
                                ArenaBuilder.wall.GRAPPLE,
                                ArenaBuilder.wall.NONE))
                        .buildArena();

                arena.addEntity(decorFactory.wallBag(0, Measure.units(-5f), arena.getWidth(), Measure.units(5f)));

                float chestPosX = chestsAreLeft ? Measure.units(12.5f) : arena.getWidth() -  Measure.units(12.5f);
                float chest2PosX = chestsAreLeft ? Measure.units(27.5f) : arena.getWidth() - Measure.units(27.5f);

                float chestPosY = Measure.units(27.5f);

                arena.addEntity(chestFactory.centeredChestBag(chestPosX, chestPosY));
                arena.addEntity(chestFactory.centeredChestBag(chest2PosX, chestPosY));

                float hoarderPosX = chestsAreLeft ? Measure.units(20f) : arena.getWidth() - Measure.units(20f);
                float hoarderPosY = Measure.units(42.5f);

                arena.addEntity(arenaEnemyPlacementFactory.hoarderFactory.hoarder(
                        hoarderPosX,
                        hoarderPosY));


                float leftwallPosX = !chestsAreLeft ? Measure.units(5f) : arena.getWidth() - Measure.units(25f);
                float rightTallWallPosX = chestsAreLeft ? Measure.units(5f) : arena.getWidth() - Measure.units(35f);

                arena.addEntity(decorFactory.wallBag(leftwallPosX, 0, Measure.units(20f), Measure.units(10f)));
                arena.addEntity(decorFactory.wallBag(rightTallWallPosX, 0, Measure.units(30f), Measure.units(25f)));

                com.bryjamin.wickedwizard.factories.weapons.enemy.LaserOrbitalTask.LaserBuilder lb = new com.bryjamin.wickedwizard.factories.weapons.enemy.LaserOrbitalTask.LaserBuilder(assetManager)
                        .chargeTime(0.5f)
                        .numberOfOrbitals(20)
                        .angles(chestsAreLeft ? 180 : 0)
                        .orbitalAndIntervalSize(Measure.units(2.5f));




                arena.addEntity(beamTurretFactory.laserChain(!chestsAreLeft ? Measure.units(22.5f) : arena.getWidth() - Measure.units(27.5f), Measure.units(2.5f), 1,
                        lb.build()));


               // arena.addEntity(decorFactory.wallBag(wallPosX, Measure.units(20f), Measure.units(35f), Measure.units(10f), arenaSkin.getWallTint()));

                //RoomDecorationFactory.spawnBlob(a);
                return arena;
            }
        };

    }



    public com.bryjamin.wickedwizard.factories.arenas.ArenaCreate room26DoubleAlurmWithASqaureCenter() {


        return new com.bryjamin.wickedwizard.factories.arenas.ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {


                Arena arena = arenaShellFactory.createOmniArenaSquareCenter(defaultCoords, Arena.ArenaType.TRAP);

                arena.addEntity(arenaEnemyPlacementFactory.spawnAlurm(arena.getWidth() / 4, Measure.units(45f)));
                arena.addEntity(arenaEnemyPlacementFactory.spawnAlurm(arena.getWidth() / 4 * 3, Measure.units(45f)));

                return arena;
            }
        };

    }




    public com.bryjamin.wickedwizard.factories.arenas.ArenaCreate room27Width2GiantLaserSwitchInTheCenter() {


        return new com.bryjamin.wickedwizard.factories.arenas.ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {


                Arena arena =  new ArenaBuilder(assetManager, arenaSkin, Arena.ArenaType.TRAP)
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
                        .buildArena();

                //Barriers //TODO add this into builder?
                arena.addEntity(decorFactory.wallBag(0, Measure.units(70f), arena.getWidth(), Measure.units(5f)));
                arena.addEntity(decorFactory.wallBag(-Measure.units(5f), 0, Measure.units(5), Measure.units(70f)));
                arena.addEntity(decorFactory.wallBag(arena.getWidth(), 0, Measure.units(5), Measure.units(70f)));

                com.bryjamin.wickedwizard.factories.weapons.enemy.LaserOrbitalTask.LaserBuilder lb = new com.bryjamin.wickedwizard.factories.weapons.enemy.LaserOrbitalTask.LaserBuilder(assetManager)
                        .chargeTime(0.5f)
                        .numberOfOrbitals(20)
                        .angles(180, 0)
                        .speedInDegrees(random.nextBoolean() ? 0.5f : -0.5f)
                        .expiryTime(5f)
                        .orbitalAndIntervalSize(Measure.units(10f));

                arena.addEntity(beamTurretFactory.inCombatTimedLaserChain(Measure.units(95f), Measure.units(55f), 2, 10,
                        lb.build()));


                arena.addEntity(arenaEnemyPlacementFactory.switchFactory.switchBag(Measure.units(97.5f), Measure.units(50f), 180));

                arena.addEntity(arenaEnemyPlacementFactory.switchFactory.switchBag(arena.getWidth() - Measure.units(10f), Measure.units(35f), 90));

                arena.addEntity(arenaEnemyPlacementFactory.switchFactory.switchBag(Measure.units(5f), Measure.units(35f), -90));

                return arena;
            }
        };

    }

    public com.bryjamin.wickedwizard.factories.arenas.ArenaCreate room28Height3TwoGiantScenicLasers() {


        return new com.bryjamin.wickedwizard.factories.arenas.ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {


                Arena arena =  new ArenaBuilder(assetManager, arenaSkin, Arena.ArenaType.NORMAL)
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
                        .buildArena();



                for(int i = 0; i < 4; i++){
                    arena.addEntity(decorFactory.grapplePointBag(arena.getWidth() / 2, Measure.units(40f + (i * 37.5f))));
                }


                LaserBeam laserBeam = new LaserBeam.LaserBeamBuilder(assetManager)
                        .chargingLaserHeight(Measure.units(200f))
                        .chargingLaserWidth(Measure.units(18.5f))
                        .chargingLaserTime(1f)
                        .activeLaserHeight(Measure.units(200f))
                        .activeLaserWidth(Measure.units(20f))
                        .activeLaserTime(5f)
                        .layer(TextureRegionComponent.ENEMY_LAYER_FAR)
                        .build();


                arena.addEntity(beamTurretFactory.timedLaserBeam(Measure.units(10f), -Measure.units(10f), 6, 0, 3f, true,
                        laserBeam));

                arena.addEntity(beamTurretFactory.timedLaserBeam(arena.getWidth() - Measure.units(40f), -Measure.units(10f), 6, 0, 3f, true,
                        laserBeam));

                arena.addEntity(beamTurretFactory.laserChain(Measure.units(10f), arena.getHeight() - Measure.units(15f), 6,
                        new com.bryjamin.wickedwizard.factories.weapons.enemy.LaserOrbitalTask.LaserBuilder(assetManager).build()));

                arena.addEntity(beamTurretFactory.laserChain(arena.getWidth() - Measure.units(40f), arena.getHeight() - Measure.units(15f), 6,
                        new com.bryjamin.wickedwizard.factories.weapons.enemy.LaserOrbitalTask.LaserBuilder(assetManager).build()));

                return arena;
            }
        };

    }



    public com.bryjamin.wickedwizard.factories.arenas.ArenaCreate room29BoringHeight2TreasureRoomWithLasers() {

        return new com.bryjamin.wickedwizard.factories.arenas.ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {

                Arena arena =  new ArenaBuilder(assetManager, arenaSkin, Arena.ArenaType.NORMAL)
                        .addSection(new ArenaBuilder.Section(defaultCoords,
                                ArenaBuilder.wall.FULL,
                                ArenaBuilder.wall.FULL,
                                ArenaBuilder.wall.NONE,
                                ArenaBuilder.wall.FULL))
                        .addSection(new ArenaBuilder.Section(new MapCoords(defaultCoords.getX(), defaultCoords.getY() + 1),
                                ArenaBuilder.wall.DOOR,
                                ArenaBuilder.wall.DOOR,
                                ArenaBuilder.wall.GRAPPLE,
                                ArenaBuilder.wall.NONE)).buildArena();

                float width = Measure.units(30f);

                arena.addEntity(decorFactory.wallBag(arena.getWidth() - width - Measure.units(5f), Measure.units(30f),
                        width, Measure.units(40f), arenaSkin));

                arena.addEntity(decorFactory.wallBag(Measure.units(5f), Measure.units(30f),
                        width, Measure.units(40f), arenaSkin));


                boolean one = random.nextBoolean();
                int both = random.nextInt(4);



                arena.addEntity(decorFactory.grapplePointBag(arena.getWidth() / 2, Measure.units(55f)));


                if(both == 3) {
                    arena.addEntity(chestFactory.chestBag(Measure.units(75f), Measure.units(10f)));
                    arena.addEntity(chestFactory.chestBag(Measure.units(15f), Measure.units(10f)));
                } else {
                    if(one)  arena.addEntity(chestFactory.chestBag(Measure.units(75f), Measure.units(10f)));
                    else arena.addEntity(chestFactory.chestBag(Measure.units(15f), Measure.units(10f)));
                }


                com.bryjamin.wickedwizard.factories.weapons.enemy.LaserOrbitalTask.LaserBuilder empty = new com.bryjamin.wickedwizard.factories.weapons.enemy.LaserOrbitalTask.LaserBuilder(assetManager);


                LaserBeam laserBeam = new LaserBeam.LaserBeamBuilder(assetManager)
                        .chargingLaserHeight(Measure.units(2f))
                        .chargingLaserWidth(Measure.units(30f))
                        .activeLaserHeight(Measure.units(2.5f))
                        .activeLaserWidth(Measure.units(30f))
                        .centerLaserUsingWidth(false)
                        .layer(TextureRegionComponent.ENEMY_LAYER_FAR)
                        .build();



                for(int i = 0; i < 5; i++) {
                    arena.addEntity(beamTurretFactory.timedLaserBeam(Measure.units(32.5f), Measure.units(32.5f + (i * 7.5f)), 1, 0, 3,
                            laserBeam));

                    arena.addEntity(beamTurretFactory.laserChain(Measure.units(62.5f), Measure.units(32.5f + (i * 7.5f)), 1,
                            empty.build()));
                }


                return arena;





            }
        };

    }



    public com.bryjamin.wickedwizard.factories.arenas.ArenaCreate room30WidthTwoPylonsAndAlurms() {

        return new com.bryjamin.wickedwizard.factories.arenas.ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {

                Arena arena =  new ArenaBuilder(assetManager, arenaSkin, Arena.ArenaType.NORMAL)
                        .addSection(new ArenaBuilder.Section(defaultCoords,
                                ArenaBuilder.wall.DOOR,
                                ArenaBuilder.wall.NONE,
                                ArenaBuilder.wall.GRAPPLE,
                                ArenaBuilder.wall.DOOR))
                        .addSection(new ArenaBuilder.Section(new MapCoords(defaultCoords.getX() + 1, defaultCoords.getY()),
                                ArenaBuilder.wall.NONE,
                                ArenaBuilder.wall.DOOR,
                                ArenaBuilder.wall.GRAPPLE,
                                ArenaBuilder.wall.DOOR)).buildArena();


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
