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
import com.byrjamin.wickedwizard.utils.enums.Direction;

import java.util.Random;

/**
 * Created by Home on 24/06/2017.
 */

public class Level5Rooms extends AbstractFactory implements ArenaRepostiory{

    private ArenaShellFactory arenaShellFactory;
    private ChestFactory chestFactory;
    private DecorFactory decorFactory;
    private ArenaEnemyPlacementFactory arenaEnemyPlacementFactory;
    private BombFactory bombFactory;
    private Random random;
    private ArenaSkin arenaSkin;

    public Level5Rooms(AssetManager assetManager, ArenaSkin arenaSkin, Random random) {
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
        return  getLevel5RoomArray();
    }

    public Array<ArenaCreate> getLevel5RoomArray() {







        Array<ArenaCreate> ag = new Array<ArenaCreate>();



        ag.insert(0, room1PentaSentry());
        ag.insert(1, room2DoubleFlyBy());
        ag.insert(2, room3Width2DoubleFlyByAndPentaSentryMoving());
        ag.insert(3, room4TrapRoomPentaAndDoubleFlyBy());
        ag.insert(4, room5HeavyModon());
        ag.insert(5, room6GhostlyEncounter());
        ag.insert(6, room7SingularGhostlyEncounterWithACenter());
        ag.insert(7, room8Lazerus());
        ag.insert(8, room9TwoLazerus());
        ag.insert(9, room10SwitchsAndDoubleLasers());
        ag.insert(10,room11GhostPylonWidth3PossibleTreasure());
        ag.insert(11,room12Height3GrappleLaserChests());
        ag.insert(12,room13DoubleLaserArenaWithBouncersAndLaserus());
        ag.insert(13,room14Width2KnightsAndGoats());
        ag.insert(14,room15LaserModonAndAFlyBy());
        ag.insert(15,room16Width2LasersAndBlobs());
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
        ag.insert(26,room27Height3TreasureRoom());
        ag.insert(27,room28OmniTreasureRoom());
        ag.insert(28,room29JackLaserAndSnake());
        ag.insert(29,room30Height3ThroughRoomWithHorizontalLasers());

        return ag;
    }


    public ArenaCreate room1PentaSentry() {
        return new ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {
                Arena arena = new Arena(arenaSkin, defaultCoords);
                arena =  new ArenaBuilder(assetManager, arenaSkin)
                        .addSection(new ArenaBuilder.Section(defaultCoords,
                                ArenaBuilder.wall.DOOR,
                                ArenaBuilder.wall.DOOR,
                                ArenaBuilder.wall.FULL,
                                ArenaBuilder.wall.DOOR))
                        .buildArena(arena);
                arena.addEntity(arenaEnemyPlacementFactory.turretFactory.fixedPentaSentry(arena.getWidth() / 2, Measure.units(45f)));
                arena.roomType = Arena.RoomType.TRAP;
                return arena;
            }
        };
    }

    public ArenaCreate room2DoubleFlyBy() {
        return new ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {
                Arena arena = new Arena(arenaSkin, defaultCoords);
                arena =  new ArenaBuilder(assetManager, arenaSkin)
                        .addSection(new ArenaBuilder.Section(defaultCoords,
                                ArenaBuilder.wall.DOOR,
                                ArenaBuilder.wall.DOOR,
                                ArenaBuilder.wall.FULL,
                                ArenaBuilder.wall.DOOR))
                        .buildArena(arena);
                arena.addEntity(arenaEnemyPlacementFactory.turretFactory.fixedFlyByDoubleBombSentry(arena.getWidth() / 2, Measure.units(45f)));
                arena.roomType = Arena.RoomType.TRAP;
                return arena;
            }
        };
    }


    public ArenaCreate room3Width2DoubleFlyByAndPentaSentryMoving() {
        return new ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {
                Arena arena = new Arena(arenaSkin, defaultCoords,
                        new MapCoords(defaultCoords.getX() + 1, defaultCoords.getY()));

                boolean ceilingIsLeft = random.nextBoolean();

                arena =  new ArenaBuilder(assetManager, arenaSkin)
                        .addSection(new ArenaBuilder.Section(defaultCoords,
                                ArenaBuilder.wall.DOOR,
                                ArenaBuilder.wall.NONE,
                                ceilingIsLeft ? ArenaBuilder.wall.GRAPPLE : ArenaBuilder.wall.FULL,
                                ceilingIsLeft ? ArenaBuilder.wall.FULL : ArenaBuilder.wall.DOOR))
                        .addSection(new ArenaBuilder.Section(new MapCoords(defaultCoords.getX() + 1, defaultCoords.getY()),
                                ArenaBuilder.wall.NONE,
                                ArenaBuilder.wall.DOOR,
                                ceilingIsLeft ? ArenaBuilder.wall.FULL : ArenaBuilder.wall.GRAPPLE,
                                ceilingIsLeft ? ArenaBuilder.wall.DOOR : ArenaBuilder.wall.FULL))
                        .buildArena(arena);
                arena.addEntity(arenaEnemyPlacementFactory.spawnMovingPentaSentry(arena.getWidth() / 4, Measure.units(45f)));
                arena.addEntity(arenaEnemyPlacementFactory.spawnMovingFlyByDoubleBombSentry(arena.getWidth() / 4 * 3, Measure.units(45f)));
                arena.roomType = Arena.RoomType.TRAP;
                return arena;
            }
        };
    }





    public ArenaCreate room4TrapRoomPentaAndDoubleFlyBy() {
        return new ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {
                Arena arena = arenaShellFactory.createOmniArenaHiddenGrapple(defaultCoords, random.nextInt(4));

                arena.roomType = Arena.RoomType.TRAP;

                arena.addWave(arenaEnemyPlacementFactory.spawnMovingPentaSentry(arena.getWidth() / 2, Measure.units(45f)));
                arena.addWave(arenaEnemyPlacementFactory.spawnMovingFlyByDoubleBombSentry(arena.getWidth() / 2, Measure.units(45f)));

                arena.shuffleWaves();

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




    public ArenaCreate room5HeavyModon() {
        return new ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {
                Arena arena = arenaShellFactory.createOmniArenaHiddenGrapple(defaultCoords);
                arena.roomType = Arena.RoomType.TRAP;
                arena.addEntity(arenaEnemyPlacementFactory.spawnHeavyModon(arena.getWidth() / 2, Measure.units(40f)));
                return arena;
            }
        };
    }




    public ArenaCreate room6GhostlyEncounter() {
        return new ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {
                Arena arena = arenaShellFactory.createOmniArenaHiddenGrapple(defaultCoords);
                arena.roomType = Arena.RoomType.TRAP;

                boolean startsLeft = random.nextBoolean();

                arena.addEntity(arenaEnemyPlacementFactory.cowlFactory.cowl(arena.getWidth() / 2, Measure.units(40f), 0, startsLeft));
                arena.addEntity(arenaEnemyPlacementFactory.cowlFactory.cowl(arena.getWidth() / 2, Measure.units(40f), 180, startsLeft));
                return arena;
            }
        };
    }


    public ArenaCreate room7SingularGhostlyEncounterWithACenter() {
        return new ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {
                Arena arena = arenaShellFactory.createOmniArenaSquareCenter(defaultCoords);
                arena.roomType = Arena.RoomType.TRAP;
                boolean startsLeft = random.nextBoolean();
                arena.addEntity(arenaEnemyPlacementFactory.cowlFactory.cowl(arena.getWidth() / 2, Measure.units(40f), 0, startsLeft));
                return arena;
            }
        };
    }



    public ArenaCreate room8Lazerus() {
        return new ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {
                Arena arena = arenaShellFactory.createOmniArenaSquareCenter(defaultCoords);
                arena.roomType = Arena.RoomType.TRAP;
                arena.addEntity(arenaEnemyPlacementFactory.spawnLaserus(arena.getWidth() / 2, Measure.units(40f)));
                return arena;
            }
        };
    }


    public ArenaCreate room9TwoLazerus() {
        return new ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {
                Arena arena = arenaShellFactory.createOmniArenaHiddenGrapple(defaultCoords, random.nextInt(4));
                arena.roomType = Arena.RoomType.TRAP;

                boolean startsRight = random.nextBoolean();
                boolean startsUp = random.nextBoolean();

                arena.addEntity(arenaEnemyPlacementFactory.spawnLaserus(arena.getWidth() / 4, Measure.units(40f), startsRight, startsUp));
                arena.addEntity(arenaEnemyPlacementFactory.spawnLaserus(arena.getWidth() / 4 * 3, Measure.units(40f), !startsRight, startsUp));
                return arena;
            }
        };
    }


    public ArenaCreate room10SwitchsAndDoubleLasers(){
        return new ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {

                Arena arena = arenaShellFactory.createSmallArenaNoGrapple(defaultCoords, random.nextInt(4));
                arena.roomType = Arena.RoomType.TRAP;


                LaserOrbitalTask.LaserBuilder lb = new LaserOrbitalTask.LaserBuilder(assetManager)
                        .orbitalAndIntervalSize(Measure.units(2.5f))
                        .speedInDegrees(1f)
                        .numberOfOrbitals(12)
                        .angles(0,180);

                arena.addEntity(decorFactory.inCombatLaserChain(Measure.units(20f), Measure.units(25f),2,
                        lb.build()));

                arena.addEntity(decorFactory.inCombatLaserChain(arena.getWidth() - Measure.units(30f), Measure.units(25f),2,
                        lb.speedInDegrees(-1f).build()));

                arena.addEntity(arenaEnemyPlacementFactory.switchFactory.switchBag(Measure.units(5f), Measure.units(40f), -90));
                arena.addEntity(arenaEnemyPlacementFactory.switchFactory.switchBag(arena.getWidth() - Measure.units(10f), Measure.units(40f), 90));
                arena.addEntity(arenaEnemyPlacementFactory.switchFactory.switchBag(Measure.units(15f), Measure.units(27.5f), 90));
                arena.addEntity(arenaEnemyPlacementFactory.switchFactory.switchBag(arena.getWidth() - Measure.units(20f), Measure.units(27.5f), -90));
                arena.addEntity(arenaEnemyPlacementFactory.switchFactory.switchBag(Measure.units(30f), Measure.units(27.5f), -90));
                arena.addEntity(arenaEnemyPlacementFactory.switchFactory.switchBag(Measure.units(65f), Measure.units(27.5f), 90));
                return arena;
            }
        };
    }



    public ArenaCreate room11GhostPylonWidth3PossibleTreasure(){
        return new ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {

                Arena arena = new Arena(arenaSkin, defaultCoords,
                        new MapCoords(defaultCoords.getX() + 1, defaultCoords.getY()),
                        new MapCoords(defaultCoords.getX() + 2, defaultCoords.getY()));

                arena =  new ArenaBuilder(assetManager, arenaSkin)
                        .addSection(new ArenaBuilder.Section(defaultCoords,
                                ArenaBuilder.wall.FULL,
                                ArenaBuilder.wall.NONE,
                                ArenaBuilder.wall.DOOR,
                                ArenaBuilder.wall.FULL))
                        .addSection(new ArenaBuilder.Section(new MapCoords(defaultCoords.getX() + 1, defaultCoords.getY()),
                                ArenaBuilder.wall.NONE,
                                ArenaBuilder.wall.NONE,
                                ArenaBuilder.wall.FULL,
                                ArenaBuilder.wall.FULL))
                        .addSection(new ArenaBuilder.Section(new MapCoords(defaultCoords.getX() + 2, defaultCoords.getY()),
                                ArenaBuilder.wall.NONE,
                                ArenaBuilder.wall.FULL,
                                ArenaBuilder.wall.DOOR,
                                ArenaBuilder.wall.FULL))
                        .buildArena(arena);


                arena.addEntity(decorFactory.wallBag(Measure.units(5f), Measure.units(35f), Measure.units(130f), Measure.units(5f)));

                arena.addEntity(decorFactory.platform(Measure.units(135f), Measure.units(35f), Measure.units(30)));

                arena.addEntity(decorFactory.wallBag(Measure.units(165f), Measure.units(35f), Measure.units(130f), Measure.units(5f)));

                //left
                arena.addEntity(arenaEnemyPlacementFactory.pylonFactory.ghostPylonBag(Measure.units(15f), Measure.units(15f), 0));
                arena.addEntity(decorFactory.wallBag(Measure.units(10f), Measure.units(10f), Measure.units(20f), Measure.units(5f)));

                arena.addEntity(arenaEnemyPlacementFactory.pylonFactory.ghostPylonBag(Measure.units(80f), Measure.units(15f), 0));
                arena.addEntity(decorFactory.wallBag(Measure.units(75f), Measure.units(10f), Measure.units(20f), Measure.units(5f)));

                //right



                arena.addEntity(arenaEnemyPlacementFactory.pylonFactory.ghostPylonBag(Measure.units(215f), Measure.units(15f), 0));
                arena.addEntity(decorFactory.wallBag(Measure.units(210f), Measure.units(10f), Measure.units(20f), Measure.units(5f)));

                arena.addEntity(arenaEnemyPlacementFactory.pylonFactory.ghostPylonBag(Measure.units(280f), Measure.units(15f), 0));
                arena.addEntity(decorFactory.wallBag(Measure.units(275f), Measure.units(10f), Measure.units(20f), Measure.units(5f)));


                arena.addEntity(arenaEnemyPlacementFactory.pylonFactory.ghostPylonBag(Measure.units(145f), Measure.units(15f), 0));
                arena.addEntity(decorFactory.wallBag(Measure.units(140f), Measure.units(10f), Measure.units(20f), Measure.units(5f)));


                if(random.nextBoolean()) arena.addEntity(chestFactory.chestBag(Measure.units(45), Measure.units(10f)));
                if(random.nextBoolean()) arena.addEntity(chestFactory.chestBag(Measure.units(245f), Measure.units(10f)));


                return arena;
            }
        };
    }





    public ArenaCreate room12Height3GrappleLaserChests(){
        return new ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {

                Arena arena = new Arena(arenaSkin, defaultCoords,
                        new MapCoords(defaultCoords.getX(), defaultCoords.getY() + 1),
                        new MapCoords(defaultCoords.getX(), defaultCoords.getY() + 2),
                        new MapCoords(defaultCoords.getX(), defaultCoords.getY() + 3));

                arena =  new ArenaBuilder(assetManager, arenaSkin)
                        .addSection(new ArenaBuilder.Section(defaultCoords,
                                ArenaBuilder.wall.FULL,
                                ArenaBuilder.wall.FULL,
                                ArenaBuilder.wall.NONE,
                                ArenaBuilder.wall.FULL))
                        .addSection(new ArenaBuilder.Section(new MapCoords(defaultCoords.getX(), defaultCoords.getY() + 1),
                                ArenaBuilder.wall.FULL,
                                ArenaBuilder.wall.FULL,
                                ArenaBuilder.wall.NONE,
                                ArenaBuilder.wall.NONE))
                        .addSection(new ArenaBuilder.Section(new MapCoords(defaultCoords.getX(), defaultCoords.getY() + 2),
                                ArenaBuilder.wall.FULL,
                                ArenaBuilder.wall.FULL,
                                ArenaBuilder.wall.NONE,
                                ArenaBuilder.wall.NONE))
                        .addSection(new ArenaBuilder.Section(new MapCoords(defaultCoords.getX(), defaultCoords.getY() + 3),
                                ArenaBuilder.wall.DOOR,
                                ArenaBuilder.wall.DOOR,
                                ArenaBuilder.wall.GRAPPLE,
                                ArenaBuilder.wall.NONE))
                        .buildArena(arena);


                LaserOrbitalTask.LaserBuilder lb = new LaserOrbitalTask.LaserBuilder(assetManager)
                        .orbitalAndIntervalSize(Measure.units(20f))
                        .numberOfOrbitals(7)
                        .expiryTime(1)
                        .angles(90);

                arena.addEntity(decorFactory.timedLaserChain(Measure.units(35f), Measure.units(25f), 6, 4f,
                        lb.build()));

                arena.addEntity(decorFactory.timedLaserChain(Measure.units(35f), Measure.units(160), 6, 4f,
                        new LaserOrbitalTask.LaserBuilder(assetManager).build()));


                arena.addEntity(decorFactory.grapplePointBag(Measure.units(20f), Measure.units(50f)));


                arena.addEntity(decorFactory.grapplePointBag(arena.getWidth() - Measure.units(20f), Measure.units(100f)));


                arena.addEntity(decorFactory.grapplePointBag(Measure.units(20f), Measure.units(150f)));

                arena.addEntity(decorFactory.platform(Measure.units(5), Measure.units(156f), arena.getWidth()));


                //TODO make one either a mimic or an explody chest?

                arena.addEntity(chestFactory.chestBag(Measure.units(35f), Measure.units(10f)));
                arena.addEntity(chestFactory.chestBag(Measure.units(55f), Measure.units(10f)));



                return arena;
            }
        };
    }


    public ArenaCreate room13DoubleLaserArenaWithBouncersAndLaserus(){
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

                arena.addWave(arenaEnemyPlacementFactory.spawnLaserBouncer(arena.getWidth() / 2, Measure.units(40f)));
                arena.addWave(arenaEnemyPlacementFactory.spawnLaserus(arena.getWidth() / 2, Measure.units(40f)));

                return arena;
            }
        };
    }



    public ArenaCreate room14Width2KnightsAndGoats(){
        return new ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {

                Arena arena = new Arena(arenaSkin, defaultCoords,
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


                arena.roomType = Arena.RoomType.TRAP;

                arena.addEntity(arenaEnemyPlacementFactory.spawnKnight(Measure.units(20f), Measure.units(20f)));
                arena.addEntity(arenaEnemyPlacementFactory.spawnGoatWizard(arena.getWidth() / 2, Measure.units(45f)));
                arena.addEntity(arenaEnemyPlacementFactory.spawnKnight(arena.getWidth() - Measure.units(20f), Measure.units(20f)));


                return arena;
            }
        };
    }




    public ArenaCreate room15LaserModonAndAFlyBy(){
        return new ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {

                Arena arena = arenaShellFactory.createOmniArenaHiddenGrapple(defaultCoords, random.nextInt(4));


                arena.roomType = Arena.RoomType.TRAP;

                arena.addEntity(arenaEnemyPlacementFactory.spawnHeavyModon(arena.getWidth() / 2, Measure.units(40f)));
                arena.addEntity(arenaEnemyPlacementFactory.spawnBouncer(
                        random.nextBoolean() ? Measure.units(20f) : arena.getWidth() - Measure.units(20f), Measure.units(45f)));

                return arena;
            }
        };
    }



    public ArenaCreate room16Width2LasersAndBlobs(){
        return new ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {

                Arena arena = new Arena(arenaSkin, defaultCoords,
                        new MapCoords(defaultCoords.getX() + 1, defaultCoords.getY()));

                arena =  new ArenaBuilder(assetManager, arenaSkin)
                        .addSection(new ArenaBuilder.Section(defaultCoords,
                                ArenaBuilder.wall.FULL,
                                ArenaBuilder.wall.NONE,
                                ArenaBuilder.wall.GRAPPLE,
                                ArenaBuilder.wall.DOOR))
                        .addSection(new ArenaBuilder.Section(new MapCoords(defaultCoords.getX() + 1, defaultCoords.getY()),
                                ArenaBuilder.wall.NONE,
                                ArenaBuilder.wall.FULL,
                                ArenaBuilder.wall.FULL,
                                ArenaBuilder.wall.FULL))
                        .buildArena(arena);

                LaserOrbitalTask.LaserBuilder lb = new LaserOrbitalTask.LaserBuilder(assetManager)
                        .chargeTime(0.5f)
                        .numberOfOrbitals(20)
                        .angles(270)
                        .expiryTime(1.5f)
                        .orbitalAndIntervalSize(Measure.units(20f));

                arena.addEntity(decorFactory.timedLaserChain(Measure.units(65f), arena.getHeight() - Measure.units(15f), 6, 3f,
                        lb.build()));

                arena.addEntity(decorFactory.timedLaserChain(Measure.units(105f), arena.getHeight() - Measure.units(15f), 6, 3f,
                        lb.build()));


                arena.addEntity(chestFactory.chestBag(Measure.units(160f), Measure.units(10f), chestFactory.trapODAC()));

                arena.addWave(arenaEnemyPlacementFactory.spawnFixedPentaSentry(Measure.units(165f), Measure.units(40f)));

                return arena;
            }
        };
    }




    public ArenaCreate room17BlobAndLaserus(){
        return new ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {

                Arena arena = arenaShellFactory.createOmniArenaSquareCenter(defaultCoords);

                arena.roomType = Arena.RoomType.TRAP;
                arena.addEntity(arenaEnemyPlacementFactory.spawnAngryBlob(random.nextBoolean() ? Measure.units(20f) : arena.getWidth() - Measure.units(20f), Measure.units(20f)));
                arena.addEntity(arenaEnemyPlacementFactory.spawnLaserus(arena.getWidth() / 2, Measure.units(45f)));

                return arena;
            }
        };
    }


    public ArenaCreate room18TwoLaserusAndAPenta(){
        return new ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {


                Arena arena = new Arena(arenaSkin, defaultCoords,
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


                arena.roomType = Arena.RoomType.TRAP;
                arena.addEntity(arenaEnemyPlacementFactory.spawnLaserus(Measure.units(20f), Measure.units(40f)));
                arena.addEntity(arenaEnemyPlacementFactory.spawnLaserus(arena.getWidth() - Measure.units(20f), Measure.units(40f)));
                arena.addEntity(arenaEnemyPlacementFactory.spawnMovingPentaSentry(arena.getWidth() / 2, Measure.units(40f)));

                return arena;
            }
        };
    }


    public ArenaCreate room19TwoGhostPylons(){
        return new ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {

                Arena arena = arenaShellFactory.createOmniArenaHiddenGrapple(defaultCoords, random.nextInt(4));

                arena.roomType = Arena.RoomType.TRAP;

                arena.addEntity(arenaEnemyPlacementFactory.pylonFactory.ghostPylonBag(Measure.units(5f), Measure.units(30f), -90));
                arena.addEntity(arenaEnemyPlacementFactory.pylonFactory.ghostPylonBag(arena.getWidth() - Measure.units(15f), Measure.units(30f), 90));

                arena.addEntity(decorFactory.wallBag(Measure.units(25f), Measure.units(20f), Measure.units(5f), Measure.units(25f)));
                arena.addEntity(decorFactory.wallBag(arena.getWidth() - Measure.units(30f), Measure.units(20f), Measure.units(5f), Measure.units(25f)));


                return arena;
            }
        };
    }


    public ArenaCreate room20TwoGhostsOppositeDirectionSteppingStone(){
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

                boolean bool = random.nextBoolean();

                arena.roomType = Arena.RoomType.TRAP;

                arena.addEntity(arenaEnemyPlacementFactory.cowlFactory.cowl(0,0,0, bool));
                arena.addEntity(arenaEnemyPlacementFactory.cowlFactory.cowl(0,0,180, !bool));

                arena.addEntity(decorFactory.wallBag(Measure.units(35f), Measure.units(10f), Measure.units(30f), Measure.units(10f)));


                return arena;
            }
        };
    }



    public ArenaCreate room21RightSnake(){
        return new ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {
                Arena arena = arenaShellFactory.createOmniArenaHiddenGrapple(defaultCoords, random.nextInt(4));

                boolean bool = random.nextBoolean();

                arena.roomType = Arena.RoomType.TRAP;

                arena.addEntity(arenaEnemyPlacementFactory.spawnRightSnake(arena.getWidth() / 2, arena.getHeight() / 2, Direction.LEFT, 10));
                return arena;
            }
        };
    }



    public ArenaCreate room22LeftSnake(){
        return new ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {
                Arena arena = arenaShellFactory.createOmniArenaHiddenGrapple(defaultCoords, random.nextInt(4));

                boolean bool = random.nextBoolean();

                arena.roomType = Arena.RoomType.TRAP;

                arena.addEntity(arenaEnemyPlacementFactory.spawnLeftSnake(arena.getWidth() / 2, arena.getHeight() / 2, Direction.LEFT, 10));
                return arena;
            }
        };
    }



    public ArenaCreate room23JumpingJack(){
        return new ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {
                Arena arena = arenaShellFactory.createOmniArenaHiddenGrapple(defaultCoords, random.nextInt(4));

                arena.roomType = Arena.RoomType.TRAP;

                arena.addEntity(arenaEnemyPlacementFactory.spawnJumpingJack(arena.getWidth() / 2, arena.getHeight() / 2, random.nextBoolean()));
                return arena;
            }
        };
    }


    public ArenaCreate room24SmallVerticalRoomSnake(){
        return new ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {
                Arena arena = new Arena(arenaSkin, defaultCoords);

                arena =  new ArenaBuilder(assetManager, arenaSkin)
                        .addSection(new ArenaBuilder.Section(defaultCoords,
                                ArenaBuilder.wall.FULL,
                                ArenaBuilder.wall.FULL,
                                ArenaBuilder.wall.GRAPPLE,
                                ArenaBuilder.wall.DOOR))
                        .buildArena(arena);
                arena.roomType = Arena.RoomType.TRAP;


                arena.addEntity(decorFactory.wallBag(Measure.units(5f), Measure.units(10f), Measure.units(25f), Measure.units(45f)));
                arena.addEntity(decorFactory.wallBag(arena.getWidth() - Measure.units(30f), Measure.units(10f), Measure.units(25f), Measure.units(45f)));


                arena.addEntity(arenaEnemyPlacementFactory.spawnLeftSnake(arena.getWidth() / 2, arena.getHeight() / 2, Direction.UP, 7));
                return arena;
            }
        };
    }



    public ArenaCreate room25SmallRoomHorizontalSnake(){
        return new ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {
                Arena arena = new Arena(arenaSkin, defaultCoords);

                arena =  new ArenaBuilder(assetManager, arenaSkin)
                        .addSection(new ArenaBuilder.Section(defaultCoords,
                                ArenaBuilder.wall.DOOR,
                                ArenaBuilder.wall.DOOR,
                                ArenaBuilder.wall.FULL,
                                ArenaBuilder.wall.FULL))
                        .buildArena(arena);
                arena.roomType = Arena.RoomType.TRAP;


                arena.addEntity(decorFactory.wallBag(Measure.units(5f), Measure.units(30f), Measure.units(90f), Measure.units(25f)));

                arena.addEntity(random.nextBoolean() ?
                        arenaEnemyPlacementFactory.spawnLeftSnake(arena.getWidth() / 2, Measure.units(22.5f), Direction.UP, 7) :
                        arenaEnemyPlacementFactory.spawnRightSnake(arena.getWidth() / 2, Measure.units(22.5f), Direction.UP, 7));
                return arena;
            }
        };
    }



    public ArenaCreate room26Width3MiddleArena(){
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
                                ArenaBuilder.wall.DOOR))
                        .addSection(new ArenaBuilder.Section(new MapCoords(defaultCoords.getX() + 1, defaultCoords.getY()),
                                ArenaBuilder.wall.NONE,
                                ArenaBuilder.wall.NONE,
                                ArenaBuilder.wall.GRAPPLE,
                                ArenaBuilder.wall.FULL))
                        .addSection(new ArenaBuilder.Section(new MapCoords(defaultCoords.getX() + 2, defaultCoords.getY()),
                                ArenaBuilder.wall.NONE,
                                ArenaBuilder.wall.MANDATORYDOOR,
                                ArenaBuilder.wall.FULL,
                                ArenaBuilder.wall.DOOR))
                        .buildArena(arena);


                arena.addEntity(decorFactory.wallBag(Measure.units(5f), Measure.units(30f), Measure.units(95f), Measure.units(30f)));
                arena.addEntity(decorFactory.wallBag(arena.getWidth() - Measure.units(100f), Measure.units(30f), Measure.units(95f), Measure.units(30f)));

                arena.addEntity(decorFactory.appearInCombatWallPush(Measure.units(95f), Measure.units(10f), Measure.units(5f), Measure.units(30f), 0));
                arena.addEntity(decorFactory.appearInCombatWallPush(arena.getWidth() - Measure.units(100f), Measure.units(10f), Measure.units(5f), Measure.units(30f), 190));


                arena.addEntity(chestFactory.chestBag(Measure.units(132.5f), Measure.units(15f), chestFactory.trapODAC()));
                arena.addEntity(chestFactory.chestBag(Measure.units(145f), Measure.units(15f), chestFactory.trapODAC()));
                arena.addEntity(chestFactory.chestBag(Measure.units(157.5f), Measure.units(15f), chestFactory.trapODAC()));

                arena.addWave(arenaEnemyPlacementFactory.spawnJumpingJack(arena.getWidth() / 2, Measure.units(45), random.nextBoolean()));
                arena.addWave(arenaEnemyPlacementFactory.spawnMovingPentaSentry(arena.getWidth() / 2, Measure.units(45f)));
                arena.addWave(arenaEnemyPlacementFactory.spawnKnight(Measure.units(130f), Measure.units(45f)),
                        arenaEnemyPlacementFactory.spawnLaserus(Measure.units(170f), Measure.units(45f)));

                return arena;
            }
        };
    }







    public ArenaCreate room27Height3TreasureRoom(){
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


                arena.addEntity(decorFactory.grapplePointBag(arena.getWidth() / 2, Measure.units(70f)));

                //Left
                arena.addEntity(decorFactory.wallBag(Measure.units(20f), Measure.units(90f), Measure.units(20f), Measure.units(5f)));
                arena.addEntity(decorFactory.wallBag(Measure.units(20f), Measure.units(60f), Measure.units(20f), Measure.units(30f)));
                arena.addEntity(decorFactory.wallBag(Measure.units(5f), Measure.units(40f), Measure.units(15f), Measure.units(25f)));

                //Right
                arena.addEntity(decorFactory.wallBag(arena.getWidth() - Measure.units(40f), Measure.units(90f), Measure.units(20f), Measure.units(5f)));
                arena.addEntity(decorFactory.wallBag(arena.getWidth() - Measure.units(40f), Measure.units(60f), Measure.units(20f), Measure.units(30f)));
                arena.addEntity(decorFactory.wallBag(arena.getWidth() - Measure.units(20f), Measure.units(40f), Measure.units(15f), Measure.units(25f)));

                arena.addEntity(decorFactory.lockWall(Measure.units(20f), Measure.units(95f), Measure.units(5), Measure.units(15f)));
                arena.addEntity(chestFactory.chestBag(Measure.units(7.5f), Measure.units(80f)));
                arena.addEntity(decorFactory.platform(Measure.units(5f), Measure.units(75f), Measure.units(15f)));
                arena.addEntity(chestFactory.chestBag(Measure.units(7.5f), Measure.units(65f)));

                arena.addEntity(chestFactory.chestBag(arena.getWidth() - Measure.units(17.5f), Measure.units(65f)));

                arena.addEntity(decorFactory.wallBag(Measure.units(60f), Measure.units(90f), Measure.units(20f), Measure.units(5f)));

                arena.addEntity(decorFactory.grapplePointBag(arena.getWidth() / 2, Measure.units(110f)));


                arena.addEntity(decorFactory.wallBag(Measure.units(5f), Measure.units(110f), Measure.units(20f), Measure.units(20f)));
                arena.addEntity(decorFactory.wallBag(arena.getWidth() - Measure.units(25f), Measure.units(110f), Measure.units(20f), Measure.units(20f)));


                return arena;
            }
        };
    }





    public ArenaCreate room28OmniTreasureRoom(){
        return new ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {

                Arena arena = new Arena(arenaSkin, defaultCoords);

                arena = new ArenaBuilder(assetManager, arenaSkin)
                        .addSection(new ArenaBuilder.Section(defaultCoords,
                                ArenaBuilder.wall.DOOR,
                                ArenaBuilder.wall.DOOR,
                                ArenaBuilder.wall.DOOR,
                                ArenaBuilder.wall.DOOR))
                        .buildArena(arena);

                boolean lockedChestsAreLeft = random.nextBoolean();


                arena.addEntity(decorFactory.lockWall(lockedChestsAreLeft ? Measure.units(5f) : arena.getWidth() - Measure.units(35f), Measure.units(35f), Measure.units(30f), Measure.units(5f)));
                arena.addEntity(decorFactory.wallBag(lockedChestsAreLeft ? Measure.units(35f) : arena.getWidth() - Measure.units(40f), Measure.units(35f), Measure.units(5f), Measure.units(25f)));


                arena.addEntity(chestFactory.chestBag(lockedChestsAreLeft ? Measure.units(7.5f) : arena.getWidth() - Measure.units(17.5f), Measure.units(40f)));
                arena.addEntity(chestFactory.chestBag(lockedChestsAreLeft ? Measure.units(22.5f) : arena.getWidth() - Measure.units(32.5f), Measure.units(40f)));


                arena.addEntity(chestFactory.chestBag(lockedChestsAreLeft ? Measure.units(65f) : arena.getWidth() - Measure.units(75f), Measure.units(10f), chestFactory.trapODAC()));


                arena.addWave(arenaEnemyPlacementFactory.spawnLaserus(lockedChestsAreLeft ? Measure.units(70f) : arena.getWidth() - Measure.units(70f), Measure.units(45f)));

                arena.addEntity(decorFactory.appearInCombatWallPush(lockedChestsAreLeft ? Measure.units(5f) : arena.getWidth() - Measure.units(40f),
                        Measure.units(10f), Measure.units(35f),
                        Measure.units(25f),
                        lockedChestsAreLeft ? 0 : 180));




                return arena;
            }
        };
    }



    public ArenaCreate room29JackLaserAndSnake() {
        return new ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {

                Arena arena = arenaShellFactory.createOmniArenaHiddenGrapple(defaultCoords);

                arena.roomType = Arena.RoomType.TRAP;

                boolean bool = random.nextBoolean();

                arena.addWave(arenaEnemyPlacementFactory.spawnLaserus(arena.getWidth() / 2, Measure.units(40f)),
                        bool ? arenaEnemyPlacementFactory.spawnRightSnake(Measure.units(20f), Measure.units(35f), Direction.LEFT,4)
                                : arenaEnemyPlacementFactory.spawnLeftSnake(arena.getWidth() - Measure.units(20f), Measure.units(35f), Direction.RIGHT, 4));

                arena.addWave(arenaEnemyPlacementFactory.spawnLaserus(arena.getWidth() / 2, Measure.units(40f)),
                        !bool ? arenaEnemyPlacementFactory.spawnRightSnake(Measure.units(20f), Measure.units(35f), Direction.LEFT,4)
                                : arenaEnemyPlacementFactory.spawnLeftSnake(arena.getWidth() - Measure.units(20f), Measure.units(35f), Direction.RIGHT, 4));

                arena.addWave();

                return arena;

            }

        };

    }




    public ArenaCreate room30Height3ThroughRoomWithHorizontalLasers() {
        return new ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {

                Arena arena = new Arena(arenaSkin, defaultCoords,
                        new MapCoords(defaultCoords.getX(), defaultCoords.getY() + 1),
                        new MapCoords(defaultCoords.getX(), defaultCoords.getY() + 2));

                arena = new ArenaBuilder(assetManager, arenaSkin)
                        .addSection(new ArenaBuilder.Section(defaultCoords,
                                ArenaBuilder.wall.DOOR,
                                ArenaBuilder.wall.MANDATORYDOOR,
                                ArenaBuilder.wall.NONE,
                                ArenaBuilder.wall.DOOR))
                        .addSection(new ArenaBuilder.Section(new MapCoords(defaultCoords.getX(), defaultCoords.getY() + 1),
                                ArenaBuilder.wall.FULL,
                                ArenaBuilder.wall.FULL,
                                ArenaBuilder.wall.NONE,
                                ArenaBuilder.wall.NONE))
                        .addSection(new ArenaBuilder.Section(new MapCoords(defaultCoords.getX(), defaultCoords.getY() + 2),
                                ArenaBuilder.wall.DOOR,
                                ArenaBuilder.wall.DOOR,
                                ArenaBuilder.wall.GRAPPLE,
                                ArenaBuilder.wall.NONE))
                        .buildArena(arena);


                boolean bool = random.nextBoolean();

                arena.addWave(arenaEnemyPlacementFactory.spawnLaserus(arena.getWidth() / 2, Measure.units(40f)),
                        bool ? arenaEnemyPlacementFactory.spawnRightSnake(Measure.units(20f), Measure.units(35f), Direction.LEFT,4)
                                : arenaEnemyPlacementFactory.spawnLeftSnake(arena.getWidth() - Measure.units(20f), Measure.units(35f), Direction.RIGHT, 4));

                arena.addWave(arenaEnemyPlacementFactory.spawnLaserus(arena.getWidth() / 2, Measure.units(40f)),
                        !bool ? arenaEnemyPlacementFactory.spawnRightSnake(Measure.units(20f), Measure.units(35f), Direction.LEFT,4)
                                : arenaEnemyPlacementFactory.spawnLeftSnake(arena.getWidth() - Measure.units(20f), Measure.units(35f), Direction.RIGHT, 4));

                arena.addWave(arenaEnemyPlacementFactory.spawnerFactory.spawnerBag(arena.getWidth() / 2, arena.getHeight() / 2,
                        new SpawnerFactory.Spawner() {
                            public Bag<Component> spawnBag(float x, float y) {
                                return chestFactory.centeredChestBag(x,y);
                            }
                        }));


                LaserOrbitalTask.LaserBuilder lb = new LaserOrbitalTask.LaserBuilder(assetManager)
                        .orbitalAndIntervalSize(Measure.units(5f))
                        .numberOfOrbitals(6)
                        .angles(0);

                LaserOrbitalTask.LaserBuilder empty = new LaserOrbitalTask.LaserBuilder(assetManager)
                        .orbitalAndIntervalSize(Measure.units(5f));

                for(int i = 0; i < 5; i++) {

                    float y = Measure.units(40f + (i * 40f));

                    arena.addEntity(decorFactory.laserChain(Measure.units(60f), y, 2,
                            lb.build()));
                    arena.addEntity(decorFactory.laserChain(Measure.units(85f), y, 2,
                            empty.build()));

                    arena.addEntity(decorFactory.laserChain(Measure.units(5f), y, 2,
                            lb.build()));
                    arena.addEntity(decorFactory.laserChain(Measure.units(30f), y, 2,
                            empty.build()));

                }

                arena.addEntity(decorFactory.grapplePointBag(arena.getWidth() / 2, Measure.units(45f)));
                arena.addEntity(decorFactory.grapplePointBag(arena.getWidth() / 2, Measure.units(85f)));
                arena.addEntity(decorFactory.grapplePointBag(arena.getWidth() / 2, Measure.units(125f)));

                arena.addEntity(decorFactory.platform(Measure.units(15), Measure.units(125f), Measure.units(15f)));
                arena.addEntity(decorFactory.platform(Measure.units(70), Measure.units(125f), Measure.units(15f)));

                if(random.nextBoolean()) arena.addEntity(chestFactory.chestBag(Measure.units(5), Measure.units(90f)));


                return arena;

            }

        };

    }


}
