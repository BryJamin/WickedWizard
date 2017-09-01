package com.bryjamin.wickedwizard.factories.arenas.levels;

import com.artemis.Component;
import com.artemis.utils.Bag;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.bryjamin.wickedwizard.ecs.components.texture.TextureRegionComponent;
import com.bryjamin.wickedwizard.factories.AbstractFactory;
import com.bryjamin.wickedwizard.factories.arenas.decor.ArenaEnemyPlacementFactory;
import com.bryjamin.wickedwizard.factories.arenas.skins.ArenaSkin;

import java.util.Random;

/**
 * Created by Home on 24/06/2017.
 */

public class Level5Rooms extends AbstractFactory implements ArenaRepostiory{

    private com.bryjamin.wickedwizard.factories.arenas.decor.ArenaShellFactory arenaShellFactory;
    private com.bryjamin.wickedwizard.factories.chests.ChestFactory chestFactory;
    private com.bryjamin.wickedwizard.factories.arenas.decor.DecorFactory decorFactory;
    private com.bryjamin.wickedwizard.factories.arenas.decor.BeamTurretFactory beamTurretFactory;
    private ArenaEnemyPlacementFactory arenaEnemyPlacementFactory;
    private Random random;
    private ArenaSkin arenaSkin;

    public Level5Rooms(AssetManager assetManager, ArenaSkin arenaSkin, Random random) {
        super(assetManager);
        this.arenaShellFactory = new com.bryjamin.wickedwizard.factories.arenas.decor.ArenaShellFactory(assetManager, arenaSkin);
        this.chestFactory = new com.bryjamin.wickedwizard.factories.chests.ChestFactory(assetManager);
        this.arenaEnemyPlacementFactory = new ArenaEnemyPlacementFactory(assetManager, arenaSkin, random);
        this.decorFactory = new com.bryjamin.wickedwizard.factories.arenas.decor.DecorFactory(assetManager, arenaSkin);
        this.beamTurretFactory = new com.bryjamin.wickedwizard.factories.arenas.decor.BeamTurretFactory(assetManager, arenaSkin);
        this.random = random;
        this.arenaSkin = arenaSkin;
    }


    @Override
    public Array<com.bryjamin.wickedwizard.factories.arenas.ArenaCreate> getAllArenas() {
        return  getLevel5RoomArray();
    }

    public Array<com.bryjamin.wickedwizard.factories.arenas.ArenaCreate> getLevel5RoomArray() {

        Array<com.bryjamin.wickedwizard.factories.arenas.ArenaCreate> ag = new Array<com.bryjamin.wickedwizard.factories.arenas.ArenaCreate>();



        ag.insert(0, room1PentaSentry());
        ag.insert(1, room2DoubleFlyBy());
        ag.insert(2, room3Width2DoubleFlyByAndPentaSentryMoving());
        ag.insert(3, room4TrapRoomPentaAndDoubleFlyBy());
        ag.insert(4, room5HeavyModon());
        ag.insert(5, room6GhostlyEncounter());
        ag.insert(6, room7SingularGhostlyEncounterWithACenter());
        ag.insert(7, room8Lazerus());
        ag.insert(8, room9TwoLazerus());
        ag.insert(9, room10SwitchsAndDoubleLasers()); //Unfun?
        ag.insert(10,room11GhostPylonWidth3PossibleTreasure()); //Maybe make them also have the chance to be bombs/mimics?
        ag.insert(11,room12Height3GrappleLaserChests()); //Maybe convert to laser? dunno
        ag.insert(12,room13DoubleLaserArenaWithBouncersAndLaserus());
        ag.insert(13,room14Width2KnightsAndGoats());
        ag.insert(14, room15LaserModonAndABouncer()); //Not very inspired
        ag.insert(15, room16Width2LasersTreasureAndPentaSentry());
        ag.insert(16,room17BlobAndLaserus());
        ag.insert(17,room18TwoLaserusAndAPenta());
        ag.insert(18,room19TwoGhostPylons());
        ag.insert(19,room20TwoGhostsOppositeDirectionSteppingStone());
        ag.insert(20,room21RightSnake());
        ag.insert(21,room22LeftSnake());
        ag.insert(22,room23JumpingJack());
        ag.insert(23,room24SmallVerticalRoomSnake());
        ag.insert(24,room25SmallRoomHorizontalSnake());
        ag.insert(25,room26Width3MiddleArena());
        ag.insert(26, room27Height3Empty());
        ag.insert(27,room28OmniTreasureRoom());
        ag.insert(28,room29JackLaserAndSnake()); //Too hard?
        ag.insert(29,room30Height3ThroughRoomWithHorizontalLasers());

        return ag;
    }


    public com.bryjamin.wickedwizard.factories.arenas.ArenaCreate room1PentaSentry() {
        return new com.bryjamin.wickedwizard.factories.arenas.ArenaCreate() {
            @Override
            public com.bryjamin.wickedwizard.factories.arenas.Arena createArena(com.bryjamin.wickedwizard.utils.MapCoords defaultCoords) {

                com.bryjamin.wickedwizard.factories.arenas.Arena arena =  new com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder(assetManager, arenaSkin, com.bryjamin.wickedwizard.factories.arenas.Arena.ArenaType.TRAP)
                        .addSection(new com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.Section(defaultCoords,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.DOOR,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.DOOR,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.FULL,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.DOOR))
                        .buildArena();
                arena.addEntity(arenaEnemyPlacementFactory.turretFactory.fixedPentaSentry(arena.getWidth() / 2, com.bryjamin.wickedwizard.utils.Measure.units(45f)));
                return arena;
            }
        };
    }

    public com.bryjamin.wickedwizard.factories.arenas.ArenaCreate room2DoubleFlyBy() {
        return new com.bryjamin.wickedwizard.factories.arenas.ArenaCreate() {
            @Override
            public com.bryjamin.wickedwizard.factories.arenas.Arena createArena(com.bryjamin.wickedwizard.utils.MapCoords defaultCoords) {
                com.bryjamin.wickedwizard.factories.arenas.Arena arena =  new com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder(assetManager, arenaSkin, com.bryjamin.wickedwizard.factories.arenas.Arena.ArenaType.TRAP)
                        .addSection(new com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.Section(defaultCoords,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.DOOR,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.DOOR,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.FULL,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.DOOR))
                        .buildArena();
                arena.addEntity(arenaEnemyPlacementFactory.turretFactory.fixedFlyByDoubleBombSentry(arena.getWidth() / 2, com.bryjamin.wickedwizard.utils.Measure.units(45f)));
                return arena;
            }
        };
    }


    public com.bryjamin.wickedwizard.factories.arenas.ArenaCreate room3Width2DoubleFlyByAndPentaSentryMoving() {
        return new com.bryjamin.wickedwizard.factories.arenas.ArenaCreate() {
            @Override
            public com.bryjamin.wickedwizard.factories.arenas.Arena createArena(com.bryjamin.wickedwizard.utils.MapCoords defaultCoords) {

                boolean ceilingIsLeft = random.nextBoolean();

                com.bryjamin.wickedwizard.factories.arenas.Arena arena =  new com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder(assetManager, arenaSkin, com.bryjamin.wickedwizard.factories.arenas.Arena.ArenaType.TRAP)
                        .addSection(new com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.Section(defaultCoords,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.DOOR,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.NONE,
                                ceilingIsLeft ? com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.GRAPPLE : com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.FULL,
                                ceilingIsLeft ? com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.FULL : com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.DOOR))
                        .addSection(new com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.Section(new com.bryjamin.wickedwizard.utils.MapCoords(defaultCoords.getX() + 1, defaultCoords.getY()),
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.NONE,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.DOOR,
                                ceilingIsLeft ? com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.FULL : com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.GRAPPLE,
                                ceilingIsLeft ? com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.DOOR : com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.FULL))
                        .buildArena();

                arena.addEntity(arenaEnemyPlacementFactory.spawnMovingPentaSentry(arena.getWidth() / 4, com.bryjamin.wickedwizard.utils.Measure.units(45f)));
                arena.addEntity(arenaEnemyPlacementFactory.spawnMovingFlyByDoubleBombSentry(arena.getWidth() / 4 * 3, com.bryjamin.wickedwizard.utils.Measure.units(45f)));

                return arena;
            }
        };
    }





    public com.bryjamin.wickedwizard.factories.arenas.ArenaCreate room4TrapRoomPentaAndDoubleFlyBy() {
        return new com.bryjamin.wickedwizard.factories.arenas.ArenaCreate() {
            @Override
            public com.bryjamin.wickedwizard.factories.arenas.Arena createArena(com.bryjamin.wickedwizard.utils.MapCoords defaultCoords) {
                com.bryjamin.wickedwizard.factories.arenas.Arena arena = arenaShellFactory.createOmniArenaHiddenGrapple(defaultCoords, random.nextInt(4), com.bryjamin.wickedwizard.factories.arenas.Arena.ArenaType.TRAP);

                arena.addWave(arenaEnemyPlacementFactory.spawnMovingPentaSentry(arena.getWidth() / 2, com.bryjamin.wickedwizard.utils.Measure.units(45f)));
                arena.addWave(arenaEnemyPlacementFactory.spawnMovingFlyByDoubleBombSentry(arena.getWidth() / 2, com.bryjamin.wickedwizard.utils.Measure.units(45f)));

                arena.shuffleWaves();

                arena.addWave(arenaEnemyPlacementFactory.spawnerFactory.spawnerBag(arena.getWidth() / 2, arena.getHeight() / 2,
                        new com.bryjamin.wickedwizard.factories.enemy.SpawnerFactory.Spawner() {
                            public Bag<Component> spawnBag(float x, float y) {
                                return chestFactory.centeredChestBag(x,y);
                            }
                        }));

                return arena;
            }
        };
    }




    public com.bryjamin.wickedwizard.factories.arenas.ArenaCreate room5HeavyModon() {
        return new com.bryjamin.wickedwizard.factories.arenas.ArenaCreate() {
            @Override
            public com.bryjamin.wickedwizard.factories.arenas.Arena createArena(com.bryjamin.wickedwizard.utils.MapCoords defaultCoords) {
                com.bryjamin.wickedwizard.factories.arenas.Arena arena = arenaShellFactory.createOmniArenaHiddenGrapple(defaultCoords, com.bryjamin.wickedwizard.factories.arenas.Arena.ArenaType.TRAP);
                arena.addEntity(arenaEnemyPlacementFactory.spawnHeavyModon(arena.getWidth() / 2, com.bryjamin.wickedwizard.utils.Measure.units(40f)));
                return arena;
            }
        };
    }




    public com.bryjamin.wickedwizard.factories.arenas.ArenaCreate room6GhostlyEncounter() {
        return new com.bryjamin.wickedwizard.factories.arenas.ArenaCreate() {
            @Override
            public com.bryjamin.wickedwizard.factories.arenas.Arena createArena(com.bryjamin.wickedwizard.utils.MapCoords defaultCoords) {
                com.bryjamin.wickedwizard.factories.arenas.Arena arena = arenaShellFactory.createOmniArenaHiddenGrapple(defaultCoords, com.bryjamin.wickedwizard.factories.arenas.Arena.ArenaType.TRAP);

                boolean startsLeft = random.nextBoolean();

                arena.addEntity(arenaEnemyPlacementFactory.cowlFactory.cowl(arena.getWidth() / 2, com.bryjamin.wickedwizard.utils.Measure.units(40f),
                        new Vector3(arena.getWidth() / 2, com.bryjamin.wickedwizard.utils.Measure.units(45f), 0),
                        com.bryjamin.wickedwizard.utils.Measure.units(5f),
                        180, startsLeft));
                return arena;
            }
        };
    }


    public com.bryjamin.wickedwizard.factories.arenas.ArenaCreate room7SingularGhostlyEncounterWithACenter() {
        return new com.bryjamin.wickedwizard.factories.arenas.ArenaCreate() {
            @Override
            public com.bryjamin.wickedwizard.factories.arenas.Arena createArena(com.bryjamin.wickedwizard.utils.MapCoords defaultCoords) {
                com.bryjamin.wickedwizard.factories.arenas.Arena arena = arenaShellFactory.createOmniArenaHiddenGrapple(defaultCoords, com.bryjamin.wickedwizard.factories.arenas.Arena.ArenaType.TRAP);
                boolean startsLeft = random.nextBoolean();

                boolean isOnLeft = random.nextBoolean();

                arena.addWave(arenaEnemyPlacementFactory.cowlFactory.cowl(arena.getWidth() / 2, com.bryjamin.wickedwizard.utils.Measure.units(40f),
                        new Vector3(isOnLeft ? com.bryjamin.wickedwizard.utils.Measure.units(10f) : arena.getWidth() - com.bryjamin.wickedwizard.utils.Measure.units(10f),  arena.getHeight() / 2, 0),
                        com.bryjamin.wickedwizard.utils.Measure.units(10f),
                        isOnLeft ? 180 : 0, startsLeft));


                return arena;
            }
        };
    }



    public com.bryjamin.wickedwizard.factories.arenas.ArenaCreate room8Lazerus() {
        return new com.bryjamin.wickedwizard.factories.arenas.ArenaCreate() {
            @Override
            public com.bryjamin.wickedwizard.factories.arenas.Arena createArena(com.bryjamin.wickedwizard.utils.MapCoords defaultCoords) {
                com.bryjamin.wickedwizard.factories.arenas.Arena arena = arenaShellFactory.createOmniArenaSquareCenter(defaultCoords, com.bryjamin.wickedwizard.factories.arenas.Arena.ArenaType.TRAP);
                arena.addEntity(arenaEnemyPlacementFactory.spawnLaserus(arena.getWidth() / 2, com.bryjamin.wickedwizard.utils.Measure.units(40f)));
                return arena;
            }
        };
    }


    public com.bryjamin.wickedwizard.factories.arenas.ArenaCreate room9TwoLazerus() {
        return new com.bryjamin.wickedwizard.factories.arenas.ArenaCreate() {
            @Override
            public com.bryjamin.wickedwizard.factories.arenas.Arena createArena(com.bryjamin.wickedwizard.utils.MapCoords defaultCoords) {
                com.bryjamin.wickedwizard.factories.arenas.Arena arena = arenaShellFactory.createOmniArenaHiddenGrapple(defaultCoords, random.nextInt(4), com.bryjamin.wickedwizard.factories.arenas.Arena.ArenaType.TRAP);

                boolean startsRight = random.nextBoolean();
                boolean startsUp = random.nextBoolean();

                arena.addEntity(arenaEnemyPlacementFactory.spawnLaserus(arena.getWidth() / 4, com.bryjamin.wickedwizard.utils.Measure.units(40f), startsRight, startsUp));
                arena.addEntity(arenaEnemyPlacementFactory.spawnLaserus(arena.getWidth() / 4 * 3, com.bryjamin.wickedwizard.utils.Measure.units(40f), !startsRight, startsUp));
                return arena;
            }
        };
    }


    public com.bryjamin.wickedwizard.factories.arenas.ArenaCreate room10SwitchsAndDoubleLasers(){
        return new com.bryjamin.wickedwizard.factories.arenas.ArenaCreate() {
            @Override
            public com.bryjamin.wickedwizard.factories.arenas.Arena createArena(com.bryjamin.wickedwizard.utils.MapCoords defaultCoords) {

                com.bryjamin.wickedwizard.factories.arenas.Arena arena = arenaShellFactory.createOmniArenaHiddenGrapple(defaultCoords, random.nextInt(4), com.bryjamin.wickedwizard.factories.arenas.Arena.ArenaType.TRAP);

                com.bryjamin.wickedwizard.factories.weapons.enemy.LaserOrbitalTask.LaserBuilder lb = new com.bryjamin.wickedwizard.factories.weapons.enemy.LaserOrbitalTask.LaserBuilder(assetManager)
                        .orbitalAndIntervalSize(com.bryjamin.wickedwizard.utils.Measure.units(3f))
                        .speedInDegrees(1f)
                        .numberOfOrbitals(12)
                        .angles(0,180);

                arena.addEntity(beamTurretFactory.inCombatLaserChain(com.bryjamin.wickedwizard.utils.Measure.units(20f), com.bryjamin.wickedwizard.utils.Measure.units(25f),2,
                        lb.build()));

                arena.addEntity(beamTurretFactory.inCombatLaserChain(arena.getWidth() - com.bryjamin.wickedwizard.utils.Measure.units(30f), com.bryjamin.wickedwizard.utils.Measure.units(25f),2,
                        lb.speedInDegrees(-1f).build()));

                arena.addEntity(arenaEnemyPlacementFactory.switchFactory.switchBag(com.bryjamin.wickedwizard.utils.Measure.units(5f), com.bryjamin.wickedwizard.utils.Measure.units(40f), -90));
                arena.addEntity(arenaEnemyPlacementFactory.switchFactory.switchBag(arena.getWidth() - com.bryjamin.wickedwizard.utils.Measure.units(10f), com.bryjamin.wickedwizard.utils.Measure.units(40f), 90));
                arena.addEntity(arenaEnemyPlacementFactory.switchFactory.switchBag(com.bryjamin.wickedwizard.utils.Measure.units(15f), com.bryjamin.wickedwizard.utils.Measure.units(27.5f), 90));
                arena.addEntity(arenaEnemyPlacementFactory.switchFactory.switchBag(arena.getWidth() - com.bryjamin.wickedwizard.utils.Measure.units(20f), com.bryjamin.wickedwizard.utils.Measure.units(27.5f), -90));
                arena.addEntity(arenaEnemyPlacementFactory.switchFactory.switchBag(com.bryjamin.wickedwizard.utils.Measure.units(30f), com.bryjamin.wickedwizard.utils.Measure.units(27.5f), -90));
                arena.addEntity(arenaEnemyPlacementFactory.switchFactory.switchBag(com.bryjamin.wickedwizard.utils.Measure.units(65f), com.bryjamin.wickedwizard.utils.Measure.units(27.5f), 90));
                return arena;
            }
        };
    }



    public com.bryjamin.wickedwizard.factories.arenas.ArenaCreate room11GhostPylonWidth3PossibleTreasure(){
        return new com.bryjamin.wickedwizard.factories.arenas.ArenaCreate() {
            @Override
            public com.bryjamin.wickedwizard.factories.arenas.Arena createArena(com.bryjamin.wickedwizard.utils.MapCoords defaultCoords) {

                com.bryjamin.wickedwizard.factories.arenas.Arena arena =  new com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder(assetManager, arenaSkin, com.bryjamin.wickedwizard.factories.arenas.Arena.ArenaType.NORMAL)
                        .addSection(new com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.Section(defaultCoords,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.FULL,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.NONE,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.DOOR,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.FULL))
                        .addSection(new com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.Section(new com.bryjamin.wickedwizard.utils.MapCoords(defaultCoords.getX() + 1, defaultCoords.getY()),
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.NONE,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.NONE,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.FULL,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.FULL))
                        .addSection(new com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.Section(new com.bryjamin.wickedwizard.utils.MapCoords(defaultCoords.getX() + 2, defaultCoords.getY()),
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.NONE,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.FULL,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.DOOR,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.FULL))
                        .buildArena();


                arena.addEntity(decorFactory.wallBag(com.bryjamin.wickedwizard.utils.Measure.units(5f), com.bryjamin.wickedwizard.utils.Measure.units(35f), com.bryjamin.wickedwizard.utils.Measure.units(130f), com.bryjamin.wickedwizard.utils.Measure.units(5f)));

                arena.addEntity(decorFactory.platform(com.bryjamin.wickedwizard.utils.Measure.units(135f), com.bryjamin.wickedwizard.utils.Measure.units(35f), com.bryjamin.wickedwizard.utils.Measure.units(30)));

                arena.addEntity(decorFactory.wallBag(com.bryjamin.wickedwizard.utils.Measure.units(165f), com.bryjamin.wickedwizard.utils.Measure.units(35f), com.bryjamin.wickedwizard.utils.Measure.units(130f), com.bryjamin.wickedwizard.utils.Measure.units(5f)));

                //left
                arena.addEntity(arenaEnemyPlacementFactory.pylonFactory.ghostPylonBag(com.bryjamin.wickedwizard.utils.Measure.units(15f), com.bryjamin.wickedwizard.utils.Measure.units(15f), 0));
                arena.addEntity(decorFactory.wallBag(com.bryjamin.wickedwizard.utils.Measure.units(10f), com.bryjamin.wickedwizard.utils.Measure.units(10f), com.bryjamin.wickedwizard.utils.Measure.units(20f), com.bryjamin.wickedwizard.utils.Measure.units(5f)));

                arena.addEntity(arenaEnemyPlacementFactory.pylonFactory.ghostPylonBag(com.bryjamin.wickedwizard.utils.Measure.units(80f), com.bryjamin.wickedwizard.utils.Measure.units(15f), 0));
                arena.addEntity(decorFactory.wallBag(com.bryjamin.wickedwizard.utils.Measure.units(75f), com.bryjamin.wickedwizard.utils.Measure.units(10f), com.bryjamin.wickedwizard.utils.Measure.units(20f), com.bryjamin.wickedwizard.utils.Measure.units(5f)));

                //right



                arena.addEntity(arenaEnemyPlacementFactory.pylonFactory.ghostPylonBag(com.bryjamin.wickedwizard.utils.Measure.units(215f), com.bryjamin.wickedwizard.utils.Measure.units(15f), 0));
                arena.addEntity(decorFactory.wallBag(com.bryjamin.wickedwizard.utils.Measure.units(210f), com.bryjamin.wickedwizard.utils.Measure.units(10f), com.bryjamin.wickedwizard.utils.Measure.units(20f), com.bryjamin.wickedwizard.utils.Measure.units(5f)));

                arena.addEntity(arenaEnemyPlacementFactory.pylonFactory.ghostPylonBag(com.bryjamin.wickedwizard.utils.Measure.units(280f), com.bryjamin.wickedwizard.utils.Measure.units(15f), 0));
                arena.addEntity(decorFactory.wallBag(com.bryjamin.wickedwizard.utils.Measure.units(275f), com.bryjamin.wickedwizard.utils.Measure.units(10f), com.bryjamin.wickedwizard.utils.Measure.units(20f), com.bryjamin.wickedwizard.utils.Measure.units(5f)));


                arena.addEntity(arenaEnemyPlacementFactory.pylonFactory.ghostPylonBag(com.bryjamin.wickedwizard.utils.Measure.units(145f), com.bryjamin.wickedwizard.utils.Measure.units(15f), 0));
                arena.addEntity(decorFactory.wallBag(com.bryjamin.wickedwizard.utils.Measure.units(140f), com.bryjamin.wickedwizard.utils.Measure.units(10f), com.bryjamin.wickedwizard.utils.Measure.units(20f), com.bryjamin.wickedwizard.utils.Measure.units(5f)));


                if(random.nextBoolean()) arena.addEntity(chestFactory.chestBag(com.bryjamin.wickedwizard.utils.Measure.units(45), com.bryjamin.wickedwizard.utils.Measure.units(10f)));
                if(random.nextBoolean()) arena.addEntity(chestFactory.chestBag(com.bryjamin.wickedwizard.utils.Measure.units(245f), com.bryjamin.wickedwizard.utils.Measure.units(10f)));


                return arena;
            }
        };
    }





    public com.bryjamin.wickedwizard.factories.arenas.ArenaCreate room12Height3GrappleLaserChests(){
        return new com.bryjamin.wickedwizard.factories.arenas.ArenaCreate() {
            @Override
            public com.bryjamin.wickedwizard.factories.arenas.Arena createArena(com.bryjamin.wickedwizard.utils.MapCoords defaultCoords) {

                com.bryjamin.wickedwizard.factories.arenas.Arena arena =  new com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder(assetManager, arenaSkin, com.bryjamin.wickedwizard.factories.arenas.Arena.ArenaType.NORMAL)
                        .addSection(new com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.Section(defaultCoords,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.FULL,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.FULL,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.NONE,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.FULL))
                        .addSection(new com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.Section(new com.bryjamin.wickedwizard.utils.MapCoords(defaultCoords.getX(), defaultCoords.getY() + 1),
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.FULL,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.FULL,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.NONE,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.NONE))
                        .addSection(new com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.Section(new com.bryjamin.wickedwizard.utils.MapCoords(defaultCoords.getX(), defaultCoords.getY() + 2),
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.FULL,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.FULL,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.NONE,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.NONE))
                        .addSection(new com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.Section(new com.bryjamin.wickedwizard.utils.MapCoords(defaultCoords.getX(), defaultCoords.getY() + 3),
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.DOOR,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.DOOR,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.GRAPPLE,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.NONE))
                        .buildArena();


                com.bryjamin.wickedwizard.factories.weapons.enemy.LaserOrbitalTask.LaserBuilder lb = new com.bryjamin.wickedwizard.factories.weapons.enemy.LaserOrbitalTask.LaserBuilder(assetManager)
                        .orbitalAndIntervalSize(com.bryjamin.wickedwizard.utils.Measure.units(20f))
                        .numberOfOrbitals(7)
                        .expiryTime(1)
                        .angles(90);

                arena.addEntity(beamTurretFactory.timedLaserChain(com.bryjamin.wickedwizard.utils.Measure.units(35f), com.bryjamin.wickedwizard.utils.Measure.units(25f), 6, 4f,
                        lb.build()));

                arena.addEntity(beamTurretFactory.timedLaserChain(com.bryjamin.wickedwizard.utils.Measure.units(35f), com.bryjamin.wickedwizard.utils.Measure.units(160), 6, 4f,
                        new com.bryjamin.wickedwizard.factories.weapons.enemy.LaserOrbitalTask.LaserBuilder(assetManager).build()));


                arena.addEntity(decorFactory.grapplePointBag(com.bryjamin.wickedwizard.utils.Measure.units(20f), com.bryjamin.wickedwizard.utils.Measure.units(50f)));


                arena.addEntity(decorFactory.grapplePointBag(arena.getWidth() - com.bryjamin.wickedwizard.utils.Measure.units(20f), com.bryjamin.wickedwizard.utils.Measure.units(100f)));


                arena.addEntity(decorFactory.grapplePointBag(com.bryjamin.wickedwizard.utils.Measure.units(20f), com.bryjamin.wickedwizard.utils.Measure.units(150f)));

                arena.addEntity(decorFactory.platform(com.bryjamin.wickedwizard.utils.Measure.units(5), com.bryjamin.wickedwizard.utils.Measure.units(156f), arena.getWidth()));


                //TODO make one either a mimic or an explody chest?

                arena.addEntity(chestFactory.chestBag(com.bryjamin.wickedwizard.utils.Measure.units(35f), com.bryjamin.wickedwizard.utils.Measure.units(10f)));
                arena.addEntity(chestFactory.chestBag(com.bryjamin.wickedwizard.utils.Measure.units(55f), com.bryjamin.wickedwizard.utils.Measure.units(10f)));



                return arena;
            }
        };
    }


    public com.bryjamin.wickedwizard.factories.arenas.ArenaCreate room13DoubleLaserArenaWithBouncersAndLaserus(){
        return new com.bryjamin.wickedwizard.factories.arenas.ArenaCreate() {
            @Override
            public com.bryjamin.wickedwizard.factories.arenas.Arena createArena(com.bryjamin.wickedwizard.utils.MapCoords defaultCoords) {

                com.bryjamin.wickedwizard.factories.arenas.Arena arena = arenaShellFactory.createOmniArenaHiddenGrapple(defaultCoords, random.nextInt(4), com.bryjamin.wickedwizard.factories.arenas.Arena.ArenaType.TRAP);

                com.bryjamin.wickedwizard.factories.weapons.enemy.LaserBeam laserBeam = new com.bryjamin.wickedwizard.factories.weapons.enemy.LaserBeam.LaserBeamBuilder(assetManager)
                        .chargingLaserHeight(com.bryjamin.wickedwizard.utils.Measure.units(50f))
                        .chargingLaserWidth(com.bryjamin.wickedwizard.utils.Measure.units(3.5f))
                        .activeLaserHeight(com.bryjamin.wickedwizard.utils.Measure.units(50f))
                        .activeLaserWidth(com.bryjamin.wickedwizard.utils.Measure.units(5f))
                        .layer(TextureRegionComponent.ENEMY_LAYER_FAR)
                        .build();


                arena.addEntity(beamTurretFactory.inCombatTimedLaserBeam(com.bryjamin.wickedwizard.utils.Measure.units(20f), com.bryjamin.wickedwizard.utils.Measure.units(52.5f), 1, -com.bryjamin.wickedwizard.utils.Measure.units(50f), 3f,
                        laserBeam));

                arena.addEntity(beamTurretFactory.inCombatTimedLaserBeam(arena.getWidth() - com.bryjamin.wickedwizard.utils.Measure.units(25f), com.bryjamin.wickedwizard.utils.Measure.units(52.5f), 1,-com.bryjamin.wickedwizard.utils.Measure.units(50f), 3f,
                        laserBeam));

                arena.addWave(arenaEnemyPlacementFactory.spawnLaserBouncer(arena.getWidth() / 2, com.bryjamin.wickedwizard.utils.Measure.units(40f)));
                arena.addWave(arenaEnemyPlacementFactory.spawnLaserus(arena.getWidth() / 2, com.bryjamin.wickedwizard.utils.Measure.units(40f)));

                arena.shuffleWaves();
                return arena;
            }
        };
    }



    public com.bryjamin.wickedwizard.factories.arenas.ArenaCreate room14Width2KnightsAndGoats(){
        return new com.bryjamin.wickedwizard.factories.arenas.ArenaCreate() {
            @Override
            public com.bryjamin.wickedwizard.factories.arenas.Arena createArena(com.bryjamin.wickedwizard.utils.MapCoords defaultCoords) {

                com.bryjamin.wickedwizard.factories.arenas.Arena arena =  new com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder(assetManager, arenaSkin, com.bryjamin.wickedwizard.factories.arenas.Arena.ArenaType.TRAP)
                        .addSection(new com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.Section(defaultCoords,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.DOOR,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.NONE,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.GRAPPLE,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.DOOR))
                        .addSection(new com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.Section(new com.bryjamin.wickedwizard.utils.MapCoords(defaultCoords.getX() + 1, defaultCoords.getY()),
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.NONE,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.DOOR,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.GRAPPLE,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.DOOR))
                        .buildArena();

                arena.addEntity(arenaEnemyPlacementFactory.spawnKnight(com.bryjamin.wickedwizard.utils.Measure.units(25f), com.bryjamin.wickedwizard.utils.Measure.units(25f)));
                arena.addEntity(arenaEnemyPlacementFactory.spawnGoatWizard(arena.getWidth() / 2, com.bryjamin.wickedwizard.utils.Measure.units(45f)));
                arena.addEntity(arenaEnemyPlacementFactory.spawnKnight(arena.getWidth() - com.bryjamin.wickedwizard.utils.Measure.units(25f), com.bryjamin.wickedwizard.utils.Measure.units(25f)));


                return arena;
            }
        };
    }




    public com.bryjamin.wickedwizard.factories.arenas.ArenaCreate room15LaserModonAndABouncer(){
        return new com.bryjamin.wickedwizard.factories.arenas.ArenaCreate() {
            @Override
            public com.bryjamin.wickedwizard.factories.arenas.Arena createArena(com.bryjamin.wickedwizard.utils.MapCoords defaultCoords) {

                com.bryjamin.wickedwizard.factories.arenas.Arena arena = arenaShellFactory.createOmniArenaHiddenGrapple(defaultCoords, random.nextInt(4), com.bryjamin.wickedwizard.factories.arenas.Arena.ArenaType.TRAP);

                arena.addEntity(arenaEnemyPlacementFactory.spawnHeavyModon(arena.getWidth() / 2, com.bryjamin.wickedwizard.utils.Measure.units(40f)));
                arena.addEntity(arenaEnemyPlacementFactory.spawnBouncer(
                        random.nextBoolean() ? com.bryjamin.wickedwizard.utils.Measure.units(20f) : arena.getWidth() - com.bryjamin.wickedwizard.utils.Measure.units(20f), com.bryjamin.wickedwizard.utils.Measure.units(45f)));

                return arena;
            }
        };
    }



    public com.bryjamin.wickedwizard.factories.arenas.ArenaCreate room16Width2LasersTreasureAndPentaSentry(){
        return new com.bryjamin.wickedwizard.factories.arenas.ArenaCreate() {
            @Override
            public com.bryjamin.wickedwizard.factories.arenas.Arena createArena(com.bryjamin.wickedwizard.utils.MapCoords defaultCoords) {

                boolean chestsAreLeft = random.nextBoolean();

                com.bryjamin.wickedwizard.factories.arenas.Arena arena =  new com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder(assetManager, arenaSkin, com.bryjamin.wickedwizard.factories.arenas.Arena.ArenaType.NORMAL)
                        .addSection(new com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.Section(defaultCoords,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.FULL,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.NONE,
                                chestsAreLeft ? com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.FULL : com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.GRAPPLE,
                                chestsAreLeft ? com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.FULL : com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.DOOR))
                        .addSection(new com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.Section(new com.bryjamin.wickedwizard.utils.MapCoords(defaultCoords.getX() + 1, defaultCoords.getY()),
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.NONE,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.FULL,
                                chestsAreLeft ? com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.GRAPPLE : com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.FULL,
                                chestsAreLeft ? com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.DOOR : com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.FULL))
                        .buildArena();

                com.bryjamin.wickedwizard.factories.weapons.enemy.LaserOrbitalTask.LaserBuilder lb = new com.bryjamin.wickedwizard.factories.weapons.enemy.LaserOrbitalTask.LaserBuilder(assetManager)
                        .chargeTime(0.5f)
                        .numberOfOrbitals(5)
                        .angles(270)
                        .expiryTime(1.5f)
                        .orbitalAndIntervalSize(com.bryjamin.wickedwizard.utils.Measure.units(20f));

                arena.addEntity(beamTurretFactory.timedLaserChain(com.bryjamin.wickedwizard.utils.Measure.units(65f), arena.getHeight() - com.bryjamin.wickedwizard.utils.Measure.units(15f), 6, 3f,
                        lb.build()));

                arena.addEntity(beamTurretFactory.timedLaserChain(com.bryjamin.wickedwizard.utils.Measure.units(105f), arena.getHeight() - com.bryjamin.wickedwizard.utils.Measure.units(15f), 6, 3f,
                        lb.build()));


                arena.addEntity(chestFactory.chestBag(chestsAreLeft ? com.bryjamin.wickedwizard.utils.Measure.units(30f) : com.bryjamin.wickedwizard.utils.Measure.units(160f), com.bryjamin.wickedwizard.utils.Measure.units(10f), chestFactory.trapODAC()));

                arena.addWave(arenaEnemyPlacementFactory.spawnFixedPentaSentry(chestsAreLeft ? com.bryjamin.wickedwizard.utils.Measure.units(35f) : com.bryjamin.wickedwizard.utils.Measure.units(165f), com.bryjamin.wickedwizard.utils.Measure.units(40f)));

                return arena;
            }
        };
    }




    public com.bryjamin.wickedwizard.factories.arenas.ArenaCreate room17BlobAndLaserus(){
        return new com.bryjamin.wickedwizard.factories.arenas.ArenaCreate() {
            @Override
            public com.bryjamin.wickedwizard.factories.arenas.Arena createArena(com.bryjamin.wickedwizard.utils.MapCoords defaultCoords) {

                com.bryjamin.wickedwizard.factories.arenas.Arena arena = arenaShellFactory.createOmniArenaSquareCenter(defaultCoords, com.bryjamin.wickedwizard.factories.arenas.Arena.ArenaType.TRAP);
                arena.addEntity(arenaEnemyPlacementFactory.spawnAngryBlob(random.nextBoolean() ? com.bryjamin.wickedwizard.utils.Measure.units(20f) : arena.getWidth() - com.bryjamin.wickedwizard.utils.Measure.units(20f), com.bryjamin.wickedwizard.utils.Measure.units(20f)));
                arena.addEntity(arenaEnemyPlacementFactory.spawnLaserus(arena.getWidth() / 2, com.bryjamin.wickedwizard.utils.Measure.units(45f)));

                return arena;
            }
        };
    }


    public com.bryjamin.wickedwizard.factories.arenas.ArenaCreate room18TwoLaserusAndAPenta(){
        return new com.bryjamin.wickedwizard.factories.arenas.ArenaCreate() {
            @Override
            public com.bryjamin.wickedwizard.factories.arenas.Arena createArena(com.bryjamin.wickedwizard.utils.MapCoords defaultCoords) {


                com.bryjamin.wickedwizard.factories.arenas.Arena arena =  new com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder(assetManager, arenaSkin, com.bryjamin.wickedwizard.factories.arenas.Arena.ArenaType.TRAP)
                        .addSection(new com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.Section(defaultCoords,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.DOOR,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.NONE,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.GRAPPLE,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.DOOR))
                        .addSection(new com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.Section(new com.bryjamin.wickedwizard.utils.MapCoords(defaultCoords.getX() + 1, defaultCoords.getY()),
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.NONE,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.DOOR,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.GRAPPLE,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.DOOR))
                        .buildArena();

                arena.addEntity(arenaEnemyPlacementFactory.spawnLaserus(com.bryjamin.wickedwizard.utils.Measure.units(20f), com.bryjamin.wickedwizard.utils.Measure.units(40f)));
                arena.addEntity(arenaEnemyPlacementFactory.spawnLaserus(arena.getWidth() - com.bryjamin.wickedwizard.utils.Measure.units(20f), com.bryjamin.wickedwizard.utils.Measure.units(40f)));
                arena.addEntity(arenaEnemyPlacementFactory.spawnFixedPentaSentry(arena.getWidth() / 2, com.bryjamin.wickedwizard.utils.Measure.units(40f)));

                return arena;
            }
        };
    }


    public com.bryjamin.wickedwizard.factories.arenas.ArenaCreate room19TwoGhostPylons(){
        return new com.bryjamin.wickedwizard.factories.arenas.ArenaCreate() {
            @Override
            public com.bryjamin.wickedwizard.factories.arenas.Arena createArena(com.bryjamin.wickedwizard.utils.MapCoords defaultCoords) {

                com.bryjamin.wickedwizard.factories.arenas.Arena arena = arenaShellFactory.createOmniArenaHiddenGrapple(defaultCoords, random.nextInt(4), com.bryjamin.wickedwizard.factories.arenas.Arena.ArenaType.TRAP);

                arena.arenaType = com.bryjamin.wickedwizard.factories.arenas.Arena.ArenaType.TRAP;

                arena.addEntity(arenaEnemyPlacementFactory.pylonFactory.ghostPylonBag(com.bryjamin.wickedwizard.utils.Measure.units(5f), com.bryjamin.wickedwizard.utils.Measure.units(40f), -90));
                arena.addEntity(arenaEnemyPlacementFactory.pylonFactory.ghostPylonBag(arena.getWidth() - com.bryjamin.wickedwizard.utils.Measure.units(15f), com.bryjamin.wickedwizard.utils.Measure.units(40f), 90));

                arena.addEntity(decorFactory.wallBag(com.bryjamin.wickedwizard.utils.Measure.units(25f), com.bryjamin.wickedwizard.utils.Measure.units(20f), com.bryjamin.wickedwizard.utils.Measure.units(5f), com.bryjamin.wickedwizard.utils.Measure.units(25f)));
                arena.addEntity(decorFactory.wallBag(arena.getWidth() - com.bryjamin.wickedwizard.utils.Measure.units(30f), com.bryjamin.wickedwizard.utils.Measure.units(20f), com.bryjamin.wickedwizard.utils.Measure.units(5f), com.bryjamin.wickedwizard.utils.Measure.units(25f)));


                return arena;
            }
        };
    }


    public com.bryjamin.wickedwizard.factories.arenas.ArenaCreate room20TwoGhostsOppositeDirectionSteppingStone(){
        return new com.bryjamin.wickedwizard.factories.arenas.ArenaCreate() {
            @Override
            public com.bryjamin.wickedwizard.factories.arenas.Arena createArena(com.bryjamin.wickedwizard.utils.MapCoords defaultCoords) {


                com.bryjamin.wickedwizard.factories.arenas.Arena arena =  new com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder(assetManager, arenaSkin, com.bryjamin.wickedwizard.factories.arenas.Arena.ArenaType.TRAP)
                        .addSection(new com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.Section(defaultCoords,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.DOOR,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.DOOR,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.GRAPPLE,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.FULL))
                        .buildArena();

                boolean bool = random.nextBoolean();

                arena.addEntity(arenaEnemyPlacementFactory.cowlFactory.cowl(arena.getWidth() / 2, com.bryjamin.wickedwizard.utils.Measure.units(40f),
                        new Vector3(arena.getWidth() / 2, com.bryjamin.wickedwizard.utils.Measure.units(45f), 0),
                        com.bryjamin.wickedwizard.utils.Measure.units(5f),
                        180, bool));

                arena.addEntity(decorFactory.wallBag(com.bryjamin.wickedwizard.utils.Measure.units(35f), com.bryjamin.wickedwizard.utils.Measure.units(10f), com.bryjamin.wickedwizard.utils.Measure.units(30f), com.bryjamin.wickedwizard.utils.Measure.units(10f)));


                return arena;
            }
        };
    }



    public com.bryjamin.wickedwizard.factories.arenas.ArenaCreate room21RightSnake(){
        return new com.bryjamin.wickedwizard.factories.arenas.ArenaCreate() {
            @Override
            public com.bryjamin.wickedwizard.factories.arenas.Arena createArena(com.bryjamin.wickedwizard.utils.MapCoords defaultCoords) {
                com.bryjamin.wickedwizard.factories.arenas.Arena arena = arenaShellFactory.createOmniArenaHiddenGrapple(defaultCoords, random.nextInt(4), com.bryjamin.wickedwizard.factories.arenas.Arena.ArenaType.TRAP);

                boolean bool = random.nextBoolean();

                arena.addEntity(arenaEnemyPlacementFactory.spawnRightSnake(arena.getWidth() / 2, arena.getHeight() / 2, bool ? com.bryjamin.wickedwizard.utils.enums.Direction.LEFT : com.bryjamin.wickedwizard.utils.enums.Direction.RIGHT, 10));
                return arena;
            }
        };
    }



    public com.bryjamin.wickedwizard.factories.arenas.ArenaCreate room22LeftSnake(){
        return new com.bryjamin.wickedwizard.factories.arenas.ArenaCreate() {
            @Override
            public com.bryjamin.wickedwizard.factories.arenas.Arena createArena(com.bryjamin.wickedwizard.utils.MapCoords defaultCoords) {
                com.bryjamin.wickedwizard.factories.arenas.Arena arena = arenaShellFactory.createOmniArenaHiddenGrapple(defaultCoords, random.nextInt(4), com.bryjamin.wickedwizard.factories.arenas.Arena.ArenaType.TRAP);

                boolean bool = random.nextBoolean();

                arena.addEntity(arenaEnemyPlacementFactory.spawnLeftSnake(arena.getWidth() / 2, arena.getHeight() / 2, bool ? com.bryjamin.wickedwizard.utils.enums.Direction.LEFT : com.bryjamin.wickedwizard.utils.enums.Direction.RIGHT, 10));
                return arena;
            }
        };
    }



    public com.bryjamin.wickedwizard.factories.arenas.ArenaCreate room23JumpingJack(){
        return new com.bryjamin.wickedwizard.factories.arenas.ArenaCreate() {
            @Override
            public com.bryjamin.wickedwizard.factories.arenas.Arena createArena(com.bryjamin.wickedwizard.utils.MapCoords defaultCoords) {
                com.bryjamin.wickedwizard.factories.arenas.Arena arena = arenaShellFactory.createOmniArenaHiddenGrapple(defaultCoords, random.nextInt(4), com.bryjamin.wickedwizard.factories.arenas.Arena.ArenaType.TRAP);

                arena.addEntity(arenaEnemyPlacementFactory.spawnJumpingJack(arena.getWidth() / 2, arena.getHeight() / 2, random.nextBoolean()));
                return arena;
            }
        };
    }


    public com.bryjamin.wickedwizard.factories.arenas.ArenaCreate room24SmallVerticalRoomSnake(){
        return new com.bryjamin.wickedwizard.factories.arenas.ArenaCreate() {
            @Override
            public com.bryjamin.wickedwizard.factories.arenas.Arena createArena(com.bryjamin.wickedwizard.utils.MapCoords defaultCoords) {

                com.bryjamin.wickedwizard.factories.arenas.Arena arena =  new com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder(assetManager, arenaSkin, com.bryjamin.wickedwizard.factories.arenas.Arena.ArenaType.TRAP)
                        .addSection(new com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.Section(defaultCoords,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.FULL,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.FULL,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.GRAPPLE,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.DOOR))
                        .buildArena();

                arena.addEntity(decorFactory.wallBag(com.bryjamin.wickedwizard.utils.Measure.units(5f), com.bryjamin.wickedwizard.utils.Measure.units(10f), com.bryjamin.wickedwizard.utils.Measure.units(25f), com.bryjamin.wickedwizard.utils.Measure.units(45f)));
                arena.addEntity(decorFactory.wallBag(arena.getWidth() - com.bryjamin.wickedwizard.utils.Measure.units(30f), com.bryjamin.wickedwizard.utils.Measure.units(10f), com.bryjamin.wickedwizard.utils.Measure.units(25f), com.bryjamin.wickedwizard.utils.Measure.units(45f)));


                arena.addEntity(random.nextBoolean() ?
                        arenaEnemyPlacementFactory.spawnLeftSnake(arena.getWidth() / 2, com.bryjamin.wickedwizard.utils.Measure.units(35f), com.bryjamin.wickedwizard.utils.enums.Direction.UP, 7) :
                        arenaEnemyPlacementFactory.spawnRightSnake(arena.getWidth() / 2, com.bryjamin.wickedwizard.utils.Measure.units(35f), com.bryjamin.wickedwizard.utils.enums.Direction.UP, 7));
                return arena;
            }
        };
    }



    public com.bryjamin.wickedwizard.factories.arenas.ArenaCreate room25SmallRoomHorizontalSnake(){
        return new com.bryjamin.wickedwizard.factories.arenas.ArenaCreate() {
            @Override
            public com.bryjamin.wickedwizard.factories.arenas.Arena createArena(com.bryjamin.wickedwizard.utils.MapCoords defaultCoords) {
                com.bryjamin.wickedwizard.factories.arenas.Arena arena =  new com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder(assetManager, arenaSkin, com.bryjamin.wickedwizard.factories.arenas.Arena.ArenaType.TRAP)
                        .addSection(new com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.Section(defaultCoords,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.DOOR,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.DOOR,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.FULL,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.FULL))
                        .buildArena();

                arena.addEntity(decorFactory.wallBag(com.bryjamin.wickedwizard.utils.Measure.units(5f), com.bryjamin.wickedwizard.utils.Measure.units(30f), com.bryjamin.wickedwizard.utils.Measure.units(90f), com.bryjamin.wickedwizard.utils.Measure.units(25f)));

                arena.addEntity(random.nextBoolean() ?
                        arenaEnemyPlacementFactory.spawnLeftSnake(arena.getWidth() / 2, com.bryjamin.wickedwizard.utils.Measure.units(22.5f), com.bryjamin.wickedwizard.utils.enums.Direction.UP, 7) :
                        arenaEnemyPlacementFactory.spawnRightSnake(arena.getWidth() / 2, com.bryjamin.wickedwizard.utils.Measure.units(22.5f), com.bryjamin.wickedwizard.utils.enums.Direction.UP, 7));
                return arena;
            }
        };
    }



    public com.bryjamin.wickedwizard.factories.arenas.ArenaCreate room26Width3MiddleArena(){
        return new com.bryjamin.wickedwizard.factories.arenas.ArenaCreate() {
            @Override
            public com.bryjamin.wickedwizard.factories.arenas.Arena createArena(com.bryjamin.wickedwizard.utils.MapCoords defaultCoords) {


                com.bryjamin.wickedwizard.factories.arenas.Arena arena =  new com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder(assetManager, arenaSkin, com.bryjamin.wickedwizard.factories.arenas.Arena.ArenaType.NORMAL)
                        .addSection(new com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.Section(defaultCoords,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.MANDATORYDOOR,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.NONE,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.FULL,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.DOOR))
                        .addSection(new com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.Section(new com.bryjamin.wickedwizard.utils.MapCoords(defaultCoords.getX() + 1, defaultCoords.getY()),
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.NONE,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.NONE,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.GRAPPLE,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.FULL))
                        .addSection(new com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.Section(new com.bryjamin.wickedwizard.utils.MapCoords(defaultCoords.getX() + 2, defaultCoords.getY()),
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.NONE,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.MANDATORYDOOR,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.FULL,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.DOOR))
                        .buildArena();


                arena.addEntity(decorFactory.wallBag(com.bryjamin.wickedwizard.utils.Measure.units(5f), com.bryjamin.wickedwizard.utils.Measure.units(30f), com.bryjamin.wickedwizard.utils.Measure.units(95f), com.bryjamin.wickedwizard.utils.Measure.units(30f)));
                arena.addEntity(decorFactory.wallBag(arena.getWidth() - com.bryjamin.wickedwizard.utils.Measure.units(100f), com.bryjamin.wickedwizard.utils.Measure.units(30f), com.bryjamin.wickedwizard.utils.Measure.units(95f), com.bryjamin.wickedwizard.utils.Measure.units(30f)));

                arena.addEntity(decorFactory.appearInCombatWallPush(com.bryjamin.wickedwizard.utils.Measure.units(95f), com.bryjamin.wickedwizard.utils.Measure.units(10f), com.bryjamin.wickedwizard.utils.Measure.units(5f), com.bryjamin.wickedwizard.utils.Measure.units(30f), 0));
                arena.addEntity(decorFactory.appearInCombatWallPush(arena.getWidth() - com.bryjamin.wickedwizard.utils.Measure.units(100f), com.bryjamin.wickedwizard.utils.Measure.units(10f), com.bryjamin.wickedwizard.utils.Measure.units(5f), com.bryjamin.wickedwizard.utils.Measure.units(30f), 190));


                arena.addEntity(chestFactory.chestBag(com.bryjamin.wickedwizard.utils.Measure.units(132.5f), com.bryjamin.wickedwizard.utils.Measure.units(10f), chestFactory.trapODAC()));
                arena.addEntity(chestFactory.chestBag(com.bryjamin.wickedwizard.utils.Measure.units(145f), com.bryjamin.wickedwizard.utils.Measure.units(10f), chestFactory.trapODAC()));
                arena.addEntity(chestFactory.chestBag(com.bryjamin.wickedwizard.utils.Measure.units(157.5f), com.bryjamin.wickedwizard.utils.Measure.units(10f), chestFactory.trapODAC()));

                arena.addWave(arenaEnemyPlacementFactory.spawnJumpingJack(arena.getWidth() / 2, com.bryjamin.wickedwizard.utils.Measure.units(45), random.nextBoolean()));
                arena.addWave(arenaEnemyPlacementFactory.spawnMovingPentaSentry(arena.getWidth() / 2, com.bryjamin.wickedwizard.utils.Measure.units(45f)));
                arena.addWave(arenaEnemyPlacementFactory.spawnKnight(com.bryjamin.wickedwizard.utils.Measure.units(130f), com.bryjamin.wickedwizard.utils.Measure.units(45f)),
                        arenaEnemyPlacementFactory.spawnLaserus(com.bryjamin.wickedwizard.utils.Measure.units(170f), com.bryjamin.wickedwizard.utils.Measure.units(45f)));

                arena.shuffleWaves();

                return arena;
            }
        };
    }







    public com.bryjamin.wickedwizard.factories.arenas.ArenaCreate room27Height3Empty(){
        return new com.bryjamin.wickedwizard.factories.arenas.ArenaCreate() {
            @Override
            public com.bryjamin.wickedwizard.factories.arenas.Arena createArena(com.bryjamin.wickedwizard.utils.MapCoords defaultCoords) {

                com.bryjamin.wickedwizard.factories.arenas.Arena arena = new com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder(assetManager, arenaSkin, com.bryjamin.wickedwizard.factories.arenas.Arena.ArenaType.NORMAL)
                        .addSection(new com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.Section(defaultCoords,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.DOOR,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.DOOR,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.NONE,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.MANDATORYDOOR))
                        .addSection(new com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.Section(new com.bryjamin.wickedwizard.utils.MapCoords(defaultCoords.getX(), defaultCoords.getY() + 1),
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.FULL,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.FULL,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.NONE,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.NONE))
                        .addSection(new com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.Section(new com.bryjamin.wickedwizard.utils.MapCoords(defaultCoords.getX(), defaultCoords.getY() + 2),
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.DOOR,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.DOOR,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.MANDATORYGRAPPLE,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.NONE))
                        .buildArena();


                arena.addEntity(decorFactory.wallBag(com.bryjamin.wickedwizard.utils.Measure.units(40f), com.bryjamin.wickedwizard.utils.Measure.units(30f), com.bryjamin.wickedwizard.utils.Measure.units(20f), com.bryjamin.wickedwizard.utils.Measure.units(5f)));


                arena.addEntity(decorFactory.grapplePointBag(arena.getWidth() / 2, com.bryjamin.wickedwizard.utils.Measure.units(70f)));
                arena.addEntity(decorFactory.grapplePointBag(arena.getWidth() / 2, com.bryjamin.wickedwizard.utils.Measure.units(110f)));

                //Left
                arena.addEntity(decorFactory.wallBag(com.bryjamin.wickedwizard.utils.Measure.units(5f), com.bryjamin.wickedwizard.utils.Measure.units(55f), com.bryjamin.wickedwizard.utils.Measure.units(40f), com.bryjamin.wickedwizard.utils.Measure.units(35f)));
                arena.addEntity(decorFactory.wallBag(com.bryjamin.wickedwizard.utils.Measure.units(5f), com.bryjamin.wickedwizard.utils.Measure.units(40f), com.bryjamin.wickedwizard.utils.Measure.units(15f), com.bryjamin.wickedwizard.utils.Measure.units(25f)));

                //Right
                arena.addEntity(decorFactory.wallBag(arena.getWidth() - com.bryjamin.wickedwizard.utils.Measure.units(40f), com.bryjamin.wickedwizard.utils.Measure.units(55f), com.bryjamin.wickedwizard.utils.Measure.units(40f), com.bryjamin.wickedwizard.utils.Measure.units(35f)));
                arena.addEntity(decorFactory.wallBag(arena.getWidth() - com.bryjamin.wickedwizard.utils.Measure.units(20f), com.bryjamin.wickedwizard.utils.Measure.units(40f), com.bryjamin.wickedwizard.utils.Measure.units(15f), com.bryjamin.wickedwizard.utils.Measure.units(25f)));

                arena.addEntity(decorFactory.wallBag(com.bryjamin.wickedwizard.utils.Measure.units(5f), com.bryjamin.wickedwizard.utils.Measure.units(110f), com.bryjamin.wickedwizard.utils.Measure.units(20f), com.bryjamin.wickedwizard.utils.Measure.units(20f)));
                arena.addEntity(decorFactory.wallBag(arena.getWidth() - com.bryjamin.wickedwizard.utils.Measure.units(25f), com.bryjamin.wickedwizard.utils.Measure.units(110f), com.bryjamin.wickedwizard.utils.Measure.units(20f), com.bryjamin.wickedwizard.utils.Measure.units(20f)));


                return arena;
            }
        };
    }





    public com.bryjamin.wickedwizard.factories.arenas.ArenaCreate room28OmniTreasureRoom(){
        return new com.bryjamin.wickedwizard.factories.arenas.ArenaCreate() {
            @Override
            public com.bryjamin.wickedwizard.factories.arenas.Arena createArena(com.bryjamin.wickedwizard.utils.MapCoords defaultCoords) {

                com.bryjamin.wickedwizard.factories.arenas.Arena arena = new com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder(assetManager, arenaSkin, com.bryjamin.wickedwizard.factories.arenas.Arena.ArenaType.NORMAL)
                        .addSection(new com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.Section(defaultCoords,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.DOOR,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.DOOR,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.GRAPPLE,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.DOOR))
                        .buildArena();

                boolean lockedChestsAreLeft = random.nextBoolean();


                arena.addEntity(decorFactory.platform(lockedChestsAreLeft ? com.bryjamin.wickedwizard.utils.Measure.units(5f) : arena.getWidth() - com.bryjamin.wickedwizard.utils.Measure.units(35f), com.bryjamin.wickedwizard.utils.Measure.units(31f), com.bryjamin.wickedwizard.utils.Measure.units(30f)));
                arena.addEntity(decorFactory.wallBag(lockedChestsAreLeft ? com.bryjamin.wickedwizard.utils.Measure.units(35f) : arena.getWidth() - com.bryjamin.wickedwizard.utils.Measure.units(40f), com.bryjamin.wickedwizard.utils.Measure.units(35f), com.bryjamin.wickedwizard.utils.Measure.units(5f), com.bryjamin.wickedwizard.utils.Measure.units(20f)));


                arena.addEntity(chestFactory.chestBag(lockedChestsAreLeft ? com.bryjamin.wickedwizard.utils.Measure.units(7.5f) : arena.getWidth() - com.bryjamin.wickedwizard.utils.Measure.units(17.5f), com.bryjamin.wickedwizard.utils.Measure.units(40f)));
                arena.addEntity(chestFactory.chestBag(lockedChestsAreLeft ? com.bryjamin.wickedwizard.utils.Measure.units(22.5f) : arena.getWidth() - com.bryjamin.wickedwizard.utils.Measure.units(32.5f), com.bryjamin.wickedwizard.utils.Measure.units(40f)));
                arena.addEntity(chestFactory.chestBag(lockedChestsAreLeft ? com.bryjamin.wickedwizard.utils.Measure.units(70f) : arena.getWidth() - com.bryjamin.wickedwizard.utils.Measure.units(80f), com.bryjamin.wickedwizard.utils.Measure.units(10f), chestFactory.trapODAC()));

                arena.addEntity(arenaEnemyPlacementFactory.hoarderFactory.hoarder(lockedChestsAreLeft ? com.bryjamin.wickedwizard.utils.Measure.units(75f)  : com.bryjamin.wickedwizard.utils.Measure.units(25f), com.bryjamin.wickedwizard.utils.Measure.units(45f)));


                arena.addWave(arenaEnemyPlacementFactory.spawnLaserus(lockedChestsAreLeft ? com.bryjamin.wickedwizard.utils.Measure.units(70f) : arena.getWidth() - com.bryjamin.wickedwizard.utils.Measure.units(70f), com.bryjamin.wickedwizard.utils.Measure.units(45f)));

                arena.addEntity(decorFactory.appearInCombatWallPush(lockedChestsAreLeft ? com.bryjamin.wickedwizard.utils.Measure.units(5f) : arena.getWidth() - com.bryjamin.wickedwizard.utils.Measure.units(40f),
                        com.bryjamin.wickedwizard.utils.Measure.units(10f), com.bryjamin.wickedwizard.utils.Measure.units(35f),
                        com.bryjamin.wickedwizard.utils.Measure.units(25f),
                        lockedChestsAreLeft ? 0 : 180));


                return arena;
            }
        };
    }



    public com.bryjamin.wickedwizard.factories.arenas.ArenaCreate room29JackLaserAndSnake() {
        return new com.bryjamin.wickedwizard.factories.arenas.ArenaCreate() {
            @Override
            public com.bryjamin.wickedwizard.factories.arenas.Arena createArena(com.bryjamin.wickedwizard.utils.MapCoords defaultCoords) {

                com.bryjamin.wickedwizard.factories.arenas.Arena arena = arenaShellFactory.createOmniArenaHiddenGrapple(defaultCoords, com.bryjamin.wickedwizard.factories.arenas.Arena.ArenaType.TRAP);

                arena.arenaType = com.bryjamin.wickedwizard.factories.arenas.Arena.ArenaType.TRAP;

                boolean bool = random.nextBoolean();

                arena.addWave(arenaEnemyPlacementFactory.spawnLaserus(arena.getWidth() / 2, com.bryjamin.wickedwizard.utils.Measure.units(40f)),
                        bool ? arenaEnemyPlacementFactory.spawnRightSnake(com.bryjamin.wickedwizard.utils.Measure.units(20f), com.bryjamin.wickedwizard.utils.Measure.units(35f), com.bryjamin.wickedwizard.utils.enums.Direction.LEFT,4)
                                : arenaEnemyPlacementFactory.spawnLeftSnake(arena.getWidth() - com.bryjamin.wickedwizard.utils.Measure.units(20f), com.bryjamin.wickedwizard.utils.Measure.units(35f), com.bryjamin.wickedwizard.utils.enums.Direction.RIGHT, 4));

                arena.addWave(arenaEnemyPlacementFactory.spawnLaserus(arena.getWidth() / 2, com.bryjamin.wickedwizard.utils.Measure.units(40f)),
                        !bool ? arenaEnemyPlacementFactory.spawnRightSnake(com.bryjamin.wickedwizard.utils.Measure.units(20f), com.bryjamin.wickedwizard.utils.Measure.units(35f), com.bryjamin.wickedwizard.utils.enums.Direction.LEFT,4)
                                : arenaEnemyPlacementFactory.spawnLeftSnake(arena.getWidth() - com.bryjamin.wickedwizard.utils.Measure.units(20f), com.bryjamin.wickedwizard.utils.Measure.units(35f), com.bryjamin.wickedwizard.utils.enums.Direction.RIGHT, 4));

                arena.shuffleWaves();

                arena.addWave(arenaEnemyPlacementFactory.spawnerFactory.spawnerBag(arena.getWidth() / 2, arena.getHeight() / 2,
                        new com.bryjamin.wickedwizard.factories.enemy.SpawnerFactory.Spawner() {
                            public Bag<Component> spawnBag(float x, float y) {
                                return chestFactory.centeredChestBag(x,y);
                            }
                        }));

                return arena;

            }

        };

    }




    public com.bryjamin.wickedwizard.factories.arenas.ArenaCreate room30Height3ThroughRoomWithHorizontalLasers() {
        return new com.bryjamin.wickedwizard.factories.arenas.ArenaCreate() {
            @Override
            public com.bryjamin.wickedwizard.factories.arenas.Arena createArena(com.bryjamin.wickedwizard.utils.MapCoords defaultCoords) {


                boolean door = random.nextBoolean();

                com.bryjamin.wickedwizard.factories.arenas.Arena arena = new com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder(assetManager, arenaSkin, com.bryjamin.wickedwizard.factories.arenas.Arena.ArenaType.NORMAL)
                        .addSection(new com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.Section(defaultCoords,
                                door ? com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.DOOR : com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.MANDATORYDOOR,
                                door ? com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.MANDATORYDOOR : com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.DOOR,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.NONE,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.DOOR))
                        .addSection(new com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.Section(new com.bryjamin.wickedwizard.utils.MapCoords(defaultCoords.getX(), defaultCoords.getY() + 1),
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.FULL,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.FULL,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.NONE,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.NONE))
                        .addSection(new com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.Section(new com.bryjamin.wickedwizard.utils.MapCoords(defaultCoords.getX(), defaultCoords.getY() + 2),
                                door ? com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.MANDATORYDOOR : com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.DOOR,
                                door ? com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.DOOR : com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.MANDATORYDOOR,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.GRAPPLE,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.NONE))
                        .buildArena();


                com.bryjamin.wickedwizard.factories.weapons.enemy.LaserOrbitalTask.LaserBuilder lb = new com.bryjamin.wickedwizard.factories.weapons.enemy.LaserOrbitalTask.LaserBuilder(assetManager)
                        .orbitalAndIntervalSize(com.bryjamin.wickedwizard.utils.Measure.units(5f))
                        .numberOfOrbitals(6)
                        .angles(0);

                com.bryjamin.wickedwizard.factories.weapons.enemy.LaserOrbitalTask.LaserBuilder empty = new com.bryjamin.wickedwizard.factories.weapons.enemy.LaserOrbitalTask.LaserBuilder(assetManager)
                        .orbitalAndIntervalSize(com.bryjamin.wickedwizard.utils.Measure.units(5f));

                for(int i = 0; i < 5; i++) {

                    float y = com.bryjamin.wickedwizard.utils.Measure.units(40f + (i * 40f));

                    arena.addEntity(beamTurretFactory.laserChain(com.bryjamin.wickedwizard.utils.Measure.units(60f), y, 2,
                            lb.build()));
                    arena.addEntity(beamTurretFactory.laserChain(com.bryjamin.wickedwizard.utils.Measure.units(85f), y, 2,
                            empty.build()));

                    arena.addEntity(beamTurretFactory.laserChain(com.bryjamin.wickedwizard.utils.Measure.units(5f), y, 2,
                            lb.build()));
                    arena.addEntity(beamTurretFactory.laserChain(com.bryjamin.wickedwizard.utils.Measure.units(30f), y, 2,
                            empty.build()));

                }

                arena.addEntity(decorFactory.grapplePointBag(arena.getWidth() / 2, com.bryjamin.wickedwizard.utils.Measure.units(45f)));
                arena.addEntity(decorFactory.grapplePointBag(arena.getWidth() / 2, com.bryjamin.wickedwizard.utils.Measure.units(85f)));
                arena.addEntity(decorFactory.grapplePointBag(arena.getWidth() / 2, com.bryjamin.wickedwizard.utils.Measure.units(125f)));

                arena.addEntity(decorFactory.platform(com.bryjamin.wickedwizard.utils.Measure.units(15), com.bryjamin.wickedwizard.utils.Measure.units(125f), com.bryjamin.wickedwizard.utils.Measure.units(15f)));
                arena.addEntity(decorFactory.platform(com.bryjamin.wickedwizard.utils.Measure.units(70), com.bryjamin.wickedwizard.utils.Measure.units(125f), com.bryjamin.wickedwizard.utils.Measure.units(15f)));

                if(random.nextBoolean()) arena.addEntity(chestFactory.chestBag(com.bryjamin.wickedwizard.utils.Measure.units(5), com.bryjamin.wickedwizard.utils.Measure.units(90f)));


                return arena;

            }

        };

    }


}
