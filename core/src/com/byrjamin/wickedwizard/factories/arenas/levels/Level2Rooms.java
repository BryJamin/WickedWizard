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
import com.byrjamin.wickedwizard.ecs.components.movement.VelocityComponent;
import com.byrjamin.wickedwizard.ecs.systems.level.RoomTransitionSystem;
import com.byrjamin.wickedwizard.factories.AbstractFactory;
import com.byrjamin.wickedwizard.factories.BombFactory;
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
import com.byrjamin.wickedwizard.factories.hazards.SpikeBallFactory;
import com.byrjamin.wickedwizard.utils.BagToEntity;
import com.byrjamin.wickedwizard.utils.ComponentBag;
import com.byrjamin.wickedwizard.utils.MapCoords;
import com.byrjamin.wickedwizard.utils.Measure;

import java.util.Random;

import static com.byrjamin.wickedwizard.factories.arenas.decor.ArenaShellFactory.SECTION_HEIGHT;
import static com.byrjamin.wickedwizard.factories.arenas.decor.ArenaShellFactory.SECTION_WIDTH;

/**
 * Created by Home on 17/05/2017.
 */

public class Level2Rooms extends AbstractFactory{

    ArenaShellFactory arenaShellFactory;
    ChestFactory chestFactory;
    DecorFactory decorFactory;
    ArenaEnemyPlacementFactory arenaEnemyPlacementFactory;
    TurretFactory turretFactory;
    Random random;

    ArenaSkin arenaSkin;

    public Level2Rooms(AssetManager assetManager, ArenaSkin arenaSkin, Random random) {
        super(assetManager);
        this.arenaShellFactory = new com.byrjamin.wickedwizard.factories.arenas.decor.ArenaShellFactory(assetManager, arenaSkin);
        this.chestFactory = new ChestFactory(assetManager);
        this.arenaEnemyPlacementFactory = new com.byrjamin.wickedwizard.factories.arenas.decor.ArenaEnemyPlacementFactory(assetManager, arenaSkin, random);
        this.decorFactory = new com.byrjamin.wickedwizard.factories.arenas.decor.DecorFactory(assetManager, arenaSkin);
        this.turretFactory = new TurretFactory(assetManager);
        this.random = random;
        this.arenaSkin = arenaSkin;
    }


    public Array<ArenaGen> getLevel2RoomArray(){
        Array<ArenaGen> ag = new Array<ArenaGen>();
        ag.add(room1Kugel());
        ag.add(room2goatWizardCenter());
        ag.add(room3OneTurretTwoLargeBouncers());
        ag.add(room4width2RoomOnlyVerticalExits());
        ag.add(room5grappleTreasureRoom());
        ag.add(room6largeRoomWithSentries());
        ag.add(room6trapAmoeba());
        ag.add(room7Height2BlobPatrol());
        ag.add(room8TreasureLockedCenter());
        ag.add(room9SpikeWallJump());
        ag.add(room10JigCenter());
        ag.add(room11Height2Treasure());
        ag.add(room12WallTurretsAndBouncers());
        ag.add(room13GoatWizardAndBlobs());
        ag.add(room14WalkingThroughRoom());
        ag.add(room15JigAndTurretWithGuard());
        ag.add(room16TreasureGoatTrap());
        ag.add(room17ChestInCenterTwoTurretsOnWalls());
        ag.add(room18TwoSilverheadsOnPlatforms());
        ag.add(room19MultiWaveRoomEndsInGoat());
        ag.add(room20MultiWaveRoomEndsInJig());
        ag.add(room21Width3SpikePlatforms());
        ag.add(room22VerticalThroughRoomTwocorridors());
        ag.add(room23PlatformingGauntlet());
        ag.add(room24BombSpawn());
        ag.add(room25BombTrap());
        ag.add(room26largeBattleRoomWithTreasureTrove());
        ag.add(room27BouncersAndAmoebas());
        ag.add(room28SmallGurnerAndAmoeba());
        ag.add(room29Height3GrappleTreasureAndGurner());
        ag.add(room30BlobsAndAmoebasAndTurrets());
        return ag;
    }


    public ArenaGen room1Kugel(){
        return new ArenaGen() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {

                Arena arena = new Arena(arenaSkin, defaultCoords);

                arena.setWidth(SECTION_WIDTH);
                arena.setHeight(SECTION_HEIGHT);

                arena = new ArenaBuilder(assetManager, arenaSkin)
                        .addSection(new ArenaBuilder.Section(defaultCoords,
                                ArenaBuilder.wall.DOOR,
                                ArenaBuilder.wall.DOOR,
                                ArenaBuilder.wall.GRAPPLE,
                                ArenaBuilder.wall.DOOR))
                        .buildArena(arena);

                arena.addEntity(arenaEnemyPlacementFactory.spawnkugelDusche(arena.getWidth() / 2,(arena.getHeight() / 2) + Measure.units(2.5f)));
                arena.roomType = Arena.RoomType.TRAP;
                return arena;
            }
        };
    }

    public ArenaGen room2goatWizardCenter(){
        return new ArenaGen() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {

                Arena arena = new Arena(arenaSkin, defaultCoords);

                arena.setWidth(SECTION_WIDTH);
                arena.setHeight(SECTION_HEIGHT);

                arena = new ArenaBuilder(assetManager, arenaSkin)
                        .addSection(new ArenaBuilder.Section(defaultCoords,
                                ArenaBuilder.wall.DOOR,
                                ArenaBuilder.wall.DOOR,
                                ArenaBuilder.wall.FULL,
                                ArenaBuilder.wall.DOOR))
                        .buildArena(arena);

                arena.addEntity(arenaEnemyPlacementFactory.spawnGoatWizard(arena.getWidth() / 2,(arena.getHeight() / 2)));
                arena.roomType = Arena.RoomType.TRAP;
                return arena;
            }
        };
    }


    public ArenaGen room3OneTurretTwoLargeBouncers(){
        return new ArenaGen() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {
                Arena a = arenaShellFactory.createOmniArenaHiddenGrapple(defaultCoords);
                a.addEntity(arenaEnemyPlacementFactory.spawnFixedSentry(a.getWidth() / 2, a.getHeight() / 2));
                a.addEntity(arenaEnemyPlacementFactory.spawnLargeBouncer(a.getWidth() - Measure.units(20), a.getHeight() - Measure.units(15f)));
                a.addEntity(arenaEnemyPlacementFactory.spawnLargeBouncer(Measure.units(20), a.getHeight() - Measure.units(15f)));
                a.roomType = Arena.RoomType.TRAP;
                return a;
            }
        };
    }






    //TODO not a fan of the enemy placement
    public ArenaGen room4width2RoomOnlyVerticalExits(){
        return new ArenaGen() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {
                //TODO need to add change mandatory doors

                Arena arena = new Arena(arenaSkin, defaultCoords, new MapCoords(defaultCoords.getX() + 1, defaultCoords.getY()));
                arena.roomType = Arena.RoomType.TRAP;

                arena.setWidth(SECTION_WIDTH * 2);
                arena.setHeight(SECTION_HEIGHT);

                arena =  new ArenaBuilder(assetManager, arenaSkin)
                        .addSection(new ArenaBuilder.Section(defaultCoords,
                                ArenaBuilder.wall.FULL,
                                ArenaBuilder.wall.NONE,
                                ArenaBuilder.wall.MANDATORYDOOR,
                                ArenaBuilder.wall.DOOR))
                        .addSection(new ArenaBuilder.Section(new MapCoords(defaultCoords.getX() + 1, defaultCoords.getY()),
                                ArenaBuilder.wall.NONE,
                                ArenaBuilder.wall.FULL,
                                ArenaBuilder.wall.DOOR,
                                ArenaBuilder.wall.MANDATORYDOOR)).buildArena(arena);


                arena.addEntity(decorFactory.wallBag(Measure.units(20), Measure.units(20), arena.getWidth() - Measure.units(40), Measure.units(25f), arenaSkin));

                arena.addEntity(decorFactory.grapplePointBag(Measure.units(12.5f), (arena.getHeight() / 2) + Measure.units(7.5f)));
                arena.addEntity(decorFactory.grapplePointBag(arena.getWidth() - Measure.units(12.5f), (arena.getHeight() / 2) + Measure.units(7.5f)));

                arena.addEntity(decorFactory.fixedWallTurret(Measure.units(5), arena.getHeight() - Measure.units(10),  180, 3.0f, 1.5f));
                arena.addEntity(decorFactory.fixedWallTurret(Measure.units(10), arena.getHeight() - Measure.units(10),  180, 3.0f, 1.5f));
                arena.addEntity(decorFactory.fixedWallTurret(Measure.units(15), arena.getHeight() - Measure.units(10),  180, 3.0f, 1.5f));

                arena.addEntity(decorFactory.fixedWallTurret(arena.getWidth() - Measure.units(10), arena.getHeight() - Measure.units(10),  180, 3.0f, 1.5f));
                arena.addEntity(decorFactory.fixedWallTurret(arena.getWidth() - Measure.units(15), arena.getHeight() - Measure.units(10),  180, 3.0f, 1.5f));
                arena.addEntity(decorFactory.fixedWallTurret(arena.getWidth() - Measure.units(20), arena.getHeight() - Measure.units(10),  180, 3.0f, 1.5f));

                return arena;
            }
        };
    }


    public ArenaGen room5grappleTreasureRoom(){
        return new ArenaGen() {
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
                                ArenaBuilder.wall.FULL))
                        .addSection(new ArenaBuilder.Section(new MapCoords(defaultCoords.getX(), defaultCoords.getY() + 1),
                                ArenaBuilder.wall.FULL,
                                ArenaBuilder.wall.FULL,
                                ArenaBuilder.wall.NONE,
                                ArenaBuilder.wall.NONE))
                        .addSection(new ArenaBuilder.Section(new MapCoords(defaultCoords.getX(), defaultCoords.getY() + 2),
                                ArenaBuilder.wall.FULL,
                                ArenaBuilder.wall.FULL,
                                ArenaBuilder.wall.DOOR,
                                ArenaBuilder.wall.NONE)).buildArena(arena);


                arena.addEntity(decorFactory.grapplePointBag(Measure.units(22.5f), Measure.units(40f)));
                arena.addEntity(decorFactory.grapplePointBag(arena.getWidth() - Measure.units(22.5f), Measure.units(40f)));

                arena.addEntity(decorFactory.grapplePointBag(Measure.units(22.5f), Measure.units(80f)));
                arena.addEntity(decorFactory.grapplePointBag(arena.getWidth() - Measure.units(22.5f), Measure.units(80f)));

                arena.addEntity(decorFactory.grapplePointBag(Measure.units(22.5f), Measure.units(120f)));
                arena.addEntity(decorFactory.grapplePointBag(arena.getWidth() - Measure.units(22.5f), Measure.units(120f)));

     /*           arena.addEntity(decorFactory.grapplePointBag(Measure.units(22.5f), Measure.units(160f)));
                arena.addEntity(decorFactory.grapplePointBag(arena.getWidth() - Measure.units(22.5f), Measure.units(160f)));
*/
                arena.addEntity(decorFactory.grapplePointBag(arena.getWidth() / 2, Measure.units(165f)));
                //arena.addEntity(decorFactory.grapplePointBag(arena.getWidth() - Measure.units(22.5f), Measure.units(160f)));

                arena.addEntity(new SpikeBallFactory(assetManager).bouncingSpikeBall(arena.getWidth() / 2, Measure.units(25f),
                        Measure.units(5f), Measure.units(5),
                        new VelocityComponent(Measure.units(20), 0)));

                arena.addEntity(new SpikeBallFactory(assetManager).bouncingSpikeBall(arena.getWidth() / 2, Measure.units(65f),
                        Measure.units(5f), Measure.units(5),
                        new VelocityComponent(-Measure.units(20), 0)));

                arena.addEntity(new SpikeBallFactory(assetManager).bouncingSpikeBall(arena.getWidth() / 2, Measure.units(105f),
                        Measure.units(5f), Measure.units(5),
                        new VelocityComponent(Measure.units(20), 0)));

                arena.addEntity(new SpikeBallFactory(assetManager).bouncingSpikeBall(arena.getWidth() / 2, Measure.units(145f),
                        Measure.units(5f), Measure.units(5),
                        new VelocityComponent(-Measure.units(20), 0)));


                arena.addEntity(new ChestFactory(assetManager).centeredChestBag(arena.getWidth() / 2, Measure.units(30f)));


                arena.addEntity(decorFactory.platform(0, SECTION_HEIGHT * 2 + SECTION_HEIGHT / 2, SECTION_WIDTH));

                return arena;
            }
        };
    }



    //TODO make better

    public ArenaGen room6largeRoomWithSentries(){
        return new ArenaGen() {
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
                                ArenaBuilder.wall.DOOR))
                        .addSection(new ArenaBuilder.Section(new MapCoords(defaultCoords.getX() + 1, defaultCoords.getY()),
                                ArenaBuilder.wall.NONE,
                                ArenaBuilder.wall.DOOR,
                                ArenaBuilder.wall.NONE,
                                ArenaBuilder.wall.DOOR))
                        .addSection(new ArenaBuilder.Section(new MapCoords(defaultCoords.getX(), defaultCoords.getY() + 1),
                                ArenaBuilder.wall.DOOR,
                                ArenaBuilder.wall.NONE,
                                ArenaBuilder.wall.GRAPPLE,
                                ArenaBuilder.wall.NONE))
                        .addSection(new ArenaBuilder.Section(new MapCoords(defaultCoords.getX() + 1, defaultCoords.getY() + 1),
                                        ArenaBuilder.wall.NONE,
                                        ArenaBuilder.wall.DOOR,
                                        ArenaBuilder.wall.GRAPPLE,
                                        ArenaBuilder.wall.NONE)).buildArena(arena);


                arena.addEntity(decorFactory.platform(0, SECTION_HEIGHT + Measure.units(5), arena.getWidth()));

                arena.addEntity(decorFactory.platform(0, SECTION_HEIGHT / 2, arena.getWidth()));


                arena.addEntity(decorFactory.grapplePointBag(arena.getWidth() / 2, arena.getHeight() / 2 - Measure.units(5)));




                //High
                arena.addEntity(arenaEnemyPlacementFactory.spawnMovingSentry(Measure.units(100f), Measure.units(100f)));

                //Mid
                arena.addEntity(arenaEnemyPlacementFactory.spawnLargeBouncer(arena.getWidth() / 2, arena.getHeight() / 2));

                //LOW
                arena.addEntity(arenaEnemyPlacementFactory.spawnMovingSentry(Measure.units(40f), Measure.units(20f)));


                return arena;
            }
        };
    }


    public ArenaGen room6trapAmoeba(){
        return new ArenaGen() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {

                Arena arena = arenaShellFactory.createOmniArenaHiddenGrapple(defaultCoords);
                arena.roomType = Arena.RoomType.TRAP;

                ComponentBag bag = new ChestFactory(assetManager).centeredChestBag(arena.getWidth() / 2, arena.getHeight() / 2,
                        new OnDeathActionComponent(new Task() {
                            @Override
                            public void performAction(World world, Entity e) {
                                new GibletFactory(assetManager).giblets(5,0.4f,
                                        Measure.units(20f), Measure.units(100f), Measure.units(1f), new Color(Color.WHITE)).performAction(world, e);

                                Arena arena = world.getSystem(RoomTransitionSystem.class).getCurrentArena();

                                for(int i = 0; i < 3; i++) {
                                    BagToEntity.bagToEntity(world.createEntity(),
                                            arenaEnemyPlacementFactory.amoebaFactory.amoeba(Measure.units(0), Measure.units(20 * i)));
                                }

                                for(int i = 0; i < 3; i++) {
                                    BagToEntity.bagToEntity(world.createEntity(),
                                            arenaEnemyPlacementFactory.amoebaFactory.amoeba(arena.getWidth(), Measure.units(20 * i)));
                                }

                                for(int i = 0; i < 6; i++) {
                                    BagToEntity.bagToEntity(world.createEntity(),
                                            arenaEnemyPlacementFactory.amoebaFactory.amoeba(Measure.units(20 * i), arena.getHeight()));
                                }

                                for(int i = 0; i < 6; i++) {
                                    BagToEntity.bagToEntity(world.createEntity(),
                                            arenaEnemyPlacementFactory.amoebaFactory.amoeba(Measure.units(20 * i), 0));
                                }

                            }

                            @Override
                            public void cleanUpAction(World world, Entity e) {

                            }
                        })

                        );

                arena.addEntity(bag);
                arena.addEntity(decorFactory.platform(Measure.units(35f), Measure.units(20f), Measure.units(30f)));

                return arena;
            }
        };
    }


    public ArenaGen room7Height2BlobPatrol() {


        return new ArenaGen() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {

                Arena arena = new Arena(arenaSkin, defaultCoords,
                        new MapCoords(defaultCoords.getX(), defaultCoords.getY() + 1));
                //arena.roomType = Arena.RoomType.TRAP;

                arena.setWidth(SECTION_WIDTH);
                arena.setHeight(SECTION_HEIGHT * 2);

                arena = new ArenaBuilder(assetManager, arenaSkin)
                        .addSection(new ArenaBuilder.Section(defaultCoords,
                                ArenaBuilder.wall.MANDATORYDOOR,
                                ArenaBuilder.wall.MANDATORYDOOR,
                                ArenaBuilder.wall.NONE,
                                ArenaBuilder.wall.DOOR))
                        .addSection(new ArenaBuilder.Section(new MapCoords(defaultCoords.getX(), defaultCoords.getY() + 1),
                                ArenaBuilder.wall.FULL,
                                ArenaBuilder.wall.FULL,
                                ArenaBuilder.wall.MANDATORYGRAPPLE,
                                ArenaBuilder.wall.NONE))
                        .buildArena(arena);


                //WALLS
                arena.addEntity(decorFactory.wallBag(Measure.units(25f), Measure.units(30f), Measure.units(75f), Measure.units(5f), arenaSkin));
                arena.addEntity(decorFactory.platform(0, Measure.units(30f), Measure.units(25f)));

                arena.addEntity(decorFactory.wallBag(0, Measure.units(55f), Measure.units(35f), Measure.units(60f), arenaSkin));
                arena.addEntity(decorFactory.wallBag(Measure.units(65f), Measure.units(55f), Measure.units(35f), Measure.units(60f), arenaSkin));
                arena.addEntity(decorFactory.platform(Measure.units(35f), Measure.units(55f), Measure.units(30f)));

                // arena.addEntity(decorFactory.grapplePointBag(arena.getWidth() / 2, Measure.units(70f)));

                arena.addEntity(arenaEnemyPlacementFactory.spawnBlob(Measure.units(15f), Measure.units(15f)));
                arena.addEntity(arenaEnemyPlacementFactory.spawnBlob(arena.getWidth() - Measure.units(15f), Measure.units(45f)));

                return arena;


            }

        };

    }



    public ArenaGen room8TreasureLockedCenter() {


        return new ArenaGen() {
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

                return arena;


            }

        };

    }




    public ArenaGen room9SpikeWallJump() {


        return new ArenaGen() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {

                Arena arena = new Arena(arenaSkin, defaultCoords);
                //arena.roomType = Arena.RoomType.TRAP;

                arena.setWidth(SECTION_WIDTH);
                arena.setHeight(SECTION_HEIGHT);

                arena = new ArenaBuilder(assetManager, arenaSkin)
                        .addSection(new ArenaBuilder.Section(defaultCoords,
                                ArenaBuilder.wall.MANDATORYDOOR,
                                ArenaBuilder.wall.MANDATORYDOOR,
                                ArenaBuilder.wall.FULL,
                                ArenaBuilder.wall.FULL))
                        .buildArena(arena);


                arena.addEntity(decorFactory.wallBag(Measure.units(20f), Measure.units(10f), Measure.units(60f), Measure.units(15f)));
                arena.addEntity(decorFactory.spikeWall(Measure.units(20f), Measure.units(25f), Measure.units(60f), Measure.units(5f), 0));

                return arena;


            }

        };

    }


    public ArenaGen room10JigCenter() {
        return new ArenaGen() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {
                Arena arena = arenaShellFactory.createOmniArenaHiddenGrapple(new MapCoords());
                arena.roomType = Arena.RoomType.TRAP;
                arena.addEntity(arenaEnemyPlacementFactory.spawnJig(arena.getWidth() / 2, arena.getHeight() / 2));
                return arena;
            }

        };

    }

    public ArenaGen room11Height2Treasure() {
        return new ArenaGen() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {

                Arena arena = new Arena(arenaSkin, defaultCoords,
                        new MapCoords(defaultCoords.getX(), defaultCoords.getY() + 1));

                arena.setWidth(SECTION_WIDTH);
                arena.setHeight(SECTION_HEIGHT * 2);

                arena = new ArenaBuilder(assetManager, arenaSkin)
                        .addSection(new ArenaBuilder.Section(defaultCoords,
                                ArenaBuilder.wall.DOOR,
                                ArenaBuilder.wall.DOOR,
                                ArenaBuilder.wall.NONE,
                                ArenaBuilder.wall.DOOR))
                        .addSection(new ArenaBuilder.Section(new MapCoords(defaultCoords.getX(), defaultCoords.getY() + 1),
                                ArenaBuilder.wall.FULL,
                                ArenaBuilder.wall.FULL,
                                ArenaBuilder.wall.FULL,
                                ArenaBuilder.wall.NONE))
                        .buildArena(arena);

                arena.addEntity(decorFactory.wallBag(Measure.units(5f), Measure.units(30f), Measure.units(30f), Measure.units(20f)));
                arena.addEntity(decorFactory.spikeWall(Measure.units(35), Measure.units(30f), Measure.units(5f), Measure.units(20f), 270));

                arena.addEntity(decorFactory.grapplePointBag(arena.getWidth() /2 , Measure.units(40f)));

                arena.addEntity(decorFactory.wallBag(arena.getWidth() - Measure.units(35f), Measure.units(30f), Measure.units(30f), Measure.units(20f)));
                arena.addEntity(decorFactory.spikeWall(arena.getWidth() - Measure.units(40f), Measure.units(30f), Measure.units(5f), Measure.units(20f), 90));


                //Center Wall
                arena.addEntity(decorFactory.wallBag(Measure.units(35f), Measure.units(75f), Measure.units(30f), Measure.units(20f)));


                arena.addEntity(decorFactory.spikeWall(Measure.units(5f), Measure.units(50f), Measure.units(30f), Measure.units(5f), 0));
                arena.addEntity(decorFactory.grapplePointBag(Measure.units(20f), Measure.units(75f)));

                arena.addEntity(decorFactory.spikeWall(arena.getWidth() - Measure.units(35f), Measure.units(50f), Measure.units(30f), Measure.units(5f), 0));
                arena.addEntity(decorFactory.grapplePointBag(arena.getWidth() - Measure.units(20f), Measure.units(75f)));

                arena.addEntity(chestFactory.centeredChestBag(arena.getWidth() / 2, Measure.units(100f)));
                //arena.addEntity(arenaEnemyPlacementFactory.spawnJig(arena.getWidth() / 2, arena.getHeight() / 2));
                return arena;
            }

        };

    }

    public ArenaGen room12WallTurretsAndBouncers() {
        return new ArenaGen() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {
                Arena arena = arenaShellFactory.createOmniArenaHiddenGrapple(new MapCoords());
                arena.roomType = Arena.RoomType.TRAP;
                arena.addEntity(arenaEnemyPlacementFactory.spawnBouncer(arena.getWidth() / 4, arena.getHeight() / 2));
                arena.addEntity(arenaEnemyPlacementFactory.spawnFixedSentry(arena.getWidth() / 2, arena.getHeight() / 4 * 3));
                arena.addEntity(arenaEnemyPlacementFactory.spawnBouncer(arena.getWidth() / 4 * 3, arena.getHeight() / 2));


                arena.addEntity(decorFactory.fixedWallTurret(Measure.units(20f), arena.getHeight() - Measure.units(10f), 180, 1.5f, 0));
                //arena.addEntity(decorFactory.fixedWallTurret(Measure.units(25f), arena.getHeight() - Measure.units(10f), 270, 1.5f, 0));
                arena.addEntity(decorFactory.fixedWallTurret(arena.getWidth() - Measure.units(25f), arena.getHeight() - Measure.units(10f), 180, 1.5f, 0));
                //arena.addEntity(decorFactory.fixedWallTurret(arena.getWidth() - Measure.units(30), arena.getHeight() - Measure.units(10f), 270, 1.5f, 0));

                return arena;
            }

        };

    }


    public ArenaGen room13GoatWizardAndBlobs() {
        return new ArenaGen() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {
                Arena arena = arenaShellFactory.createWidth2WithAllDoorsArena(new MapCoords());
                arena.roomType = Arena.RoomType.TRAP;
                arena.addEntity(arenaEnemyPlacementFactory.spawnBlob(arena.getWidth() / 4, arena.getHeight() / 2));
                arena.addEntity(arenaEnemyPlacementFactory.spawnGoatWizard(arena.getWidth() / 2, arena.getHeight() / 2));
                arena.addEntity(arenaEnemyPlacementFactory.spawnBlob(arena.getWidth() / 4 * 3, arena.getHeight() / 2));
                return arena;
            }

        };
    }


    public ArenaGen room14WalkingThroughRoom() {
        return new ArenaGen() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {
                Arena arena = new Arena(arenaSkin, defaultCoords);

                boolean isLeft = random.nextBoolean();

                arena = new ArenaBuilder(assetManager, arenaSkin)
                        .addSection(new ArenaBuilder.Section(defaultCoords,
                                isLeft ? ArenaBuilder.wall.MANDATORYDOOR : ArenaBuilder.wall.FULL,
                                isLeft ? ArenaBuilder.wall.FULL : ArenaBuilder.wall.MANDATORYDOOR,
                                ArenaBuilder.wall.MANDATORYGRAPPLE,
                                ArenaBuilder.wall.MANDATORYDOOR)).buildArena(arena);

                float wallPos = isLeft ? arena.getWidth() - Measure.units(40f) : Measure.units(5f);
                float skywallPos = isLeft ? Measure.units(5f) : arena.getWidth() - Measure.units(40);

                arena.addEntity(decorFactory.wallBag(wallPos, Measure.units(5f), Measure.units(35), arena.getHeight()));
                arena.addEntity(decorFactory.wallBag(skywallPos, Measure.units(30f), Measure.units(35), Measure.units(30f)));


                return arena;
            }

        };
    }

    public ArenaGen room15JigAndTurretWithGuard() {
        return new ArenaGen() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {
                Arena arena = new Arena(arenaSkin, defaultCoords);

                arena.roomType = Arena.RoomType.TRAP;

                arena = new ArenaBuilder(assetManager, arenaSkin)
                        .addSection(new ArenaBuilder.Section(defaultCoords,
                                ArenaBuilder.wall.DOOR,
                                ArenaBuilder.wall.DOOR,
                                ArenaBuilder.wall.GRAPPLE,
                                ArenaBuilder.wall.DOOR)).buildArena(arena);

                arena.addEntity(decorFactory.wallBag(Measure.units(20f), Measure.units(20f), Measure.units(5f), Measure.units(25f)));
                arena.addEntity(decorFactory.wallBag(arena.getWidth() - Measure.units(25f), Measure.units(20f), Measure.units(5f), Measure.units(25f)));

                arena.addEntity(arenaEnemyPlacementFactory.spawnJig(arena.getWidth() / 2, arena.getHeight() / 2));
                arena.addEntity(arenaEnemyPlacementFactory.spawnBouncer(arena.getWidth() / 2, Measure.units(50f)));

                return arena;
            }

        };
    }



    public ArenaGen room16TreasureGoatTrap() {
        return new ArenaGen() {
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
                        arenaEnemyPlacementFactory.spawnGoatWizard(Measure.units(20f), Measure.units(50f)),
                        arenaEnemyPlacementFactory.spawnGoatWizard(Measure.units(20f), Measure.units(20f))
                        );

                arena.addEntity(new ChestFactory(assetManager).chestBag(Measure.units(150f), Measure.units(10f),
                        chestFactory.trapODAC()));

                arena.addEntity(new ChestFactory(assetManager).chestBag(Measure.units(165f), Measure.units(10f),
                        chestFactory.trapODAC()));

                return arena;
            }

        };
    }




    public ArenaGen room17ChestInCenterTwoTurretsOnWalls() {
        return new ArenaGen() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {
                Arena arena = new Arena(arenaSkin, defaultCoords);

                arena = new ArenaBuilder(assetManager, arenaSkin)
                        .addSection(new ArenaBuilder.Section(defaultCoords,
                                ArenaBuilder.wall.DOOR,
                                ArenaBuilder.wall.DOOR,
                                ArenaBuilder.wall.FULL,
                                ArenaBuilder.wall.FULL))
                        .buildArena(arena);

                for(int i = 0; i < 5; i++){
                    arena.addEntity(decorFactory.fixedWallTurret(Measure.units(5), Measure.units(30f + (i * 5f)), -90, 4f, 0));
                    arena.addEntity(decorFactory.fixedWallTurret(arena.getWidth() - Measure.units(10), Measure.units(30f + (i * 5f)), 90, 4f, 2));
                }

                arena.addEntity(decorFactory.wallBag(Measure.units(35f), Measure.units(10f), Measure.units(5f), Measure.units(20f)));
                arena.addEntity(decorFactory.wallBag(Measure.units(60f), Measure.units(10f), Measure.units(5f), Measure.units(20f)));
                //arena.addEntity(decorFactory.platform(Measure.units(40f), Measure.units(30f), Measure.units(5f)));

                ChestFactory cf = new ChestFactory(assetManager);

                //TODO on harder levels also spawn a turret
/*                arena.fffffffffff(
                        arenaEnemyPlacementFactory.spaw(Measure.units(20f), Measure.units(50f)),
                        arenaEnemyPlacementFactory.spawnGoatWizard(Measure.units(20f), Measure.units(20f))
                );*/

                arena.addEntity(cf.chestBag(Measure.units(45), Measure.units(10f)));



                return arena;
            }

        };
    }



    public ArenaGen room18TwoSilverheadsOnPlatforms() {
        return new ArenaGen() {
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

                arena.roomType = Arena.RoomType.TRAP;


                arena.addEntity(decorFactory.platform(Measure.units(5f), Measure.units(26f), Measure.units(25f)));
                arena.addEntity(decorFactory.platform(arena.getWidth() - Measure.units(30f), Measure.units(26f), Measure.units(25f)));

                arena.addEntity(arenaEnemyPlacementFactory.spawnSilverHead(Measure.units(20f), Measure.units(40f)));
                arena.addEntity(arenaEnemyPlacementFactory.spawnSilverHead(arena.getWidth() - Measure.units(20f), Measure.units(40f)));

                return arena;
            }

        };
    }



    public ArenaGen room19MultiWaveRoomEndsInGoat() {
        return new ArenaGen() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {
                Arena arena = new Arena(arenaSkin, defaultCoords);

                int i = random.nextInt(4);

                arena = new ArenaBuilder(assetManager, arenaSkin)
                        .addSection(new ArenaBuilder.Section(defaultCoords,
                                i != 0 ? ArenaBuilder.wall.DOOR : ArenaBuilder.wall.FULL,
                                i != 1 ? ArenaBuilder.wall.DOOR : ArenaBuilder.wall.FULL,
                                i != 2 ? ArenaBuilder.wall.GRAPPLE : ArenaBuilder.wall.FULL,
                                i != 3 ? ArenaBuilder.wall.DOOR : ArenaBuilder.wall.FULL))
                        .buildArena(arena);

                arena.roomType = Arena.RoomType.TRAP;

                arena.addWave(arenaEnemyPlacementFactory.spawnMovingSentry(arena.getWidth() / 2, Measure.units(40f)),
                        arenaEnemyPlacementFactory.spawnBouncer(arena.getWidth() / 4, Measure.units(40f)),
                        arenaEnemyPlacementFactory.spawnBouncer(arena.getWidth() / 4 * 3, Measure.units(40f)));

                arena.addWave(arenaEnemyPlacementFactory.spawnGoatWizard(arena.getWidth() / 2, Measure.units(40f)));

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


    public ArenaGen room20MultiWaveRoomEndsInJig() {
        return new ArenaGen() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {
                Arena arena = new Arena(arenaSkin, defaultCoords);

                int i = random.nextInt(4);

                arena = new ArenaBuilder(assetManager, arenaSkin)
                        .addSection(new ArenaBuilder.Section(defaultCoords,
                                i != 0 ? ArenaBuilder.wall.DOOR : ArenaBuilder.wall.FULL,
                                i != 1 ? ArenaBuilder.wall.DOOR : ArenaBuilder.wall.FULL,
                                i != 2 ? ArenaBuilder.wall.GRAPPLE : ArenaBuilder.wall.FULL,
                                i != 3 ? ArenaBuilder.wall.DOOR : ArenaBuilder.wall.FULL))
                        .buildArena(arena);

                arena.roomType = Arena.RoomType.TRAP;

                arena.addWave(arenaEnemyPlacementFactory.spawnSilverHead(Measure.units(20f), Measure.units(40f)),
                        arenaEnemyPlacementFactory.spawnSilverHead(arena.getWidth() - Measure.units(20f), Measure.units(40f)));

                arena.addWave(arenaEnemyPlacementFactory.spawnJig(arena.getWidth() / 2, Measure.units(40f)));
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


    public ArenaGen room21Width3SpikePlatforms() {
        return new ArenaGen() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {
                Arena arena = new Arena(arenaSkin, defaultCoords,
                        new MapCoords(defaultCoords.getX() + 1, defaultCoords.getY()),
                                new MapCoords(defaultCoords.getX() + 2, defaultCoords.getY()));

                arena = new ArenaBuilder(assetManager, arenaSkin)
                        .addSection(new ArenaBuilder.Section(defaultCoords,
                                ArenaBuilder.wall.MANDATORYDOOR,
                                ArenaBuilder.wall.NONE,
                                ArenaBuilder.wall.FULL,
                                ArenaBuilder.wall.DOOR))
                        .addSection(new ArenaBuilder.Section(new MapCoords(defaultCoords.getX() + 1, defaultCoords.getY()),
                                ArenaBuilder.wall.NONE,
                                ArenaBuilder.wall.NONE,
                                ArenaBuilder.wall.MANDATORYDOOR,
                                ArenaBuilder.wall.FULL))
                        .addSection(new ArenaBuilder.Section(new MapCoords(defaultCoords.getX() + 2, defaultCoords.getY()),
                                ArenaBuilder.wall.NONE,
                                ArenaBuilder.wall.MANDATORYDOOR,
                                ArenaBuilder.wall.FULL,
                                ArenaBuilder.wall.DOOR))
                        .buildArena(arena);



                for(int i = 0; i < 6; i++){
                    arena.addEntity(decorFactory.fixedWallTurret(Measure.units(80f + (i * 10f)), Measure.units(10f), 0, 1.5f, 0));
                }

                for(int i = 0; i < 6; i++){
                    arena.addEntity(decorFactory.fixedWallTurret(Measure.units(165f + (i * 10f)), Measure.units(10f), 0, 1.5f, 0));
                }

                arena.addEntity(arenaEnemyPlacementFactory.spawnFixedSentry(Measure.units(15f), Measure.units(45f)));
                arena.addEntity(arenaEnemyPlacementFactory.spawnFixedSentry(arena.getWidth() - Measure.units(15f), Measure.units(45f)));

                arena.addEntity(decorFactory.wallBag(Measure.units(135f), Measure.units(30f), Measure.units(30f), Measure.units(5f)));


                return arena;
            }

        };
    }


    public ArenaGen room22VerticalThroughRoomTwocorridors() {
        return new ArenaGen() {
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


                arena.addEntity(decorFactory.wallBag(Measure.units(60f), Measure.units(10f), Measure.units(35f), Measure.units((20f))));
                arena.addEntity(decorFactory.wallBag(Measure.units(25f), Measure.units(20f), Measure.units(35f), Measure.units((20f))));
                arena.addEntity(decorFactory.spikeWall(Measure.units(20f), Measure.units(20f), Measure.units(5f), Measure.units(20f), 90));
                arena.addEntity(decorFactory.spikeWall(Measure.units(5f), Measure.units(20f), Measure.units(5f), Measure.units(20f), 270));

                arena.addEntity(chestFactory.chestBag(Measure.units(70), Measure.units(50f)));

                //arena.addEntity(decorFactory.destructibleBlock(Measure.units(80f), Measure.units(50f), Measure.units(5f), Measure.units(5f), new Color(Color.BLACK)));


                return arena;
            }

        };
    }




    public ArenaGen room23PlatformingGauntlet() {
        return new ArenaGen() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {
                Arena arena = new Arena(arenaSkin, defaultCoords, new MapCoords(defaultCoords.getX(), defaultCoords.getY() + 1));

                boolean isLeft = random.nextBoolean();

                arena = new ArenaBuilder(assetManager, arenaSkin)
                        .addSection(new ArenaBuilder.Section(defaultCoords,
                                isLeft ? ArenaBuilder.wall.MANDATORYDOOR : ArenaBuilder.wall.DOOR,
                                isLeft ? ArenaBuilder.wall.DOOR : ArenaBuilder.wall.MANDATORYDOOR,
                                ArenaBuilder.wall.NONE,
                                ArenaBuilder.wall.FULL))
                        .addSection(new ArenaBuilder.Section(new MapCoords(defaultCoords.getX(), defaultCoords.getY() + 1),
                                ArenaBuilder.wall.FULL,
                                ArenaBuilder.wall.FULL,
                                ArenaBuilder.wall.MANDATORYGRAPPLE,
                                ArenaBuilder.wall.NONE))
                        .buildArena(arena);

                arena.addEntity(decorFactory.wallBag(Measure.units(5f), Measure.units(30f), Measure.units(30f), Measure.units(5f)));
                arena.addEntity(decorFactory.platform(Measure.units(35f), Measure.units(30f), Measure.units(30f)));
                arena.addEntity(decorFactory.wallBag(Measure.units(65f), Measure.units(30f), Measure.units(30f), Measure.units(5f)));


                arena.addEntity(decorFactory.platform(Measure.units(5f), Measure.units(55f), Measure.units(30f)));
                arena.addEntity(decorFactory.wallBag(Measure.units(35f), Measure.units(55f), Measure.units(30f), Measure.units(5f)));
                arena.addEntity(decorFactory.platform(Measure.units(65f), Measure.units(55f), Measure.units(30f)));


                arena.addEntity(chestFactory.chestBag(Measure.units(45f), Measure.units(60f), chestFactory.trapODAC()));

                arena.addWave(arenaEnemyPlacementFactory.spawnMovingJig(arena.getWidth() / 2, Measure.units(85f)));


                arena.addWave(arenaEnemyPlacementFactory.spawnerFactory.spawnerBag(arena.getWidth() / 2, Measure.units(85f),
                        new SpawnerFactory.Spawner() {
                            public Bag<Component> spawnBag(float x, float y) {
                                return new BombFactory(assetManager).bomb(x,y, 1f);
                            }
                        }));

                return arena;
            }

        };
    }



    public ArenaGen room24BombSpawn() {
        return new ArenaGen() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {
                Arena arena = new Arena(arenaSkin, defaultCoords);

                arena = new ArenaBuilder(assetManager, arenaSkin)
                        .addSection(new ArenaBuilder.Section(defaultCoords,
                                ArenaBuilder.wall.DOOR,
                                ArenaBuilder.wall.DOOR,
                                ArenaBuilder.wall.GRAPPLE,
                                ArenaBuilder.wall.DOOR))
                        .buildArena(arena);

                arena.roomType = Arena.RoomType.TRAP;


                arena.addWave(arenaEnemyPlacementFactory.spawnerFactory.spawnerBag(arena.getWidth() / 4, Measure.units(45f),
                        new SpawnerFactory.Spawner() {
                            public Bag<Component> spawnBag(float x, float y) {
                                return new BombFactory(assetManager).bomb(x,y, 1f);
                            }
                        }));

                arena.addWave(arenaEnemyPlacementFactory.spawnerFactory.spawnerBag(arena.getWidth() / 4 * 3, Measure.units(45f),
                        new SpawnerFactory.Spawner() {
                            public Bag<Component> spawnBag(float x, float y) {
                                return new BombFactory(assetManager).bomb(x,y, 1f);
                            }
                        }));


                arena.addWave(arenaEnemyPlacementFactory.spawnerFactory.spawnerBag(arena.getWidth() / 2, Measure.units(45f),
                        new SpawnerFactory.Spawner() {
                            public Bag<Component> spawnBag(float x, float y) {
                                return new BombFactory(assetManager).bomb(x,y, 1f);
                            }
                        }));

                return arena;
            }

        };
    }


    public ArenaGen room25BombTrap() {
        return new ArenaGen() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {
                Arena arena = new Arena(arenaSkin, defaultCoords);

                arena = new ArenaBuilder(assetManager, arenaSkin)
                        .addSection(new ArenaBuilder.Section(defaultCoords,
                                ArenaBuilder.wall.DOOR,
                                ArenaBuilder.wall.DOOR,
                                ArenaBuilder.wall.GRAPPLE,
                                ArenaBuilder.wall.DOOR))
                        .buildArena(arena);


                arena.addEntity(chestFactory.chestBag(Measure.units(45f), Measure.units(10f), chestFactory.trapODAC()));


                arena.addWave(arenaEnemyPlacementFactory.spawnerFactory.spawnerBag(Measure.units(15f), Measure.units(45f),
                        new SpawnerFactory.Spawner() {
                            public Bag<Component> spawnBag(float x, float y) {
                                return new BombFactory(assetManager).bomb(x,y, 1f);
                            }
                        }),
                        arenaEnemyPlacementFactory.spawnerFactory.spawnerBag(Measure.units(30f), Measure.units(45f),
                                new SpawnerFactory.Spawner() {
                                    public Bag<Component> spawnBag(float x, float y) {
                                        return new BombFactory(assetManager).bomb(x,y, 1f);
                                    }
                                }),
                        arenaEnemyPlacementFactory.spawnerFactory.spawnerBag(Measure.units(45f), Measure.units(45f),
                                new SpawnerFactory.Spawner() {
                                    public Bag<Component> spawnBag(float x, float y) {
                                        return new BombFactory(assetManager).bomb(x,y, 1f);
                                    }
                                }),
                        arenaEnemyPlacementFactory.spawnerFactory.spawnerBag(Measure.units(60f), Measure.units(45f),
                                new SpawnerFactory.Spawner() {
                                    public Bag<Component> spawnBag(float x, float y) {
                                        return new BombFactory(assetManager).bomb(x,y, 1f);
                                    }
                                }),
                        arenaEnemyPlacementFactory.spawnerFactory.spawnerBag(Measure.units(75f), Measure.units(45f),
                                new SpawnerFactory.Spawner() {
                                    public Bag<Component> spawnBag(float x, float y) {
                                        return new BombFactory(assetManager).bomb(x,y, 1f);
                                    }
                                }),
                        arenaEnemyPlacementFactory.spawnerFactory.spawnerBag(Measure.units(85f), Measure.units(45f),
                                new SpawnerFactory.Spawner() {
                                    public Bag<Component> spawnBag(float x, float y) {
                                        return new BombFactory(assetManager).bomb(x,y, 1f);
                                    }
                                })


                        );

                return arena;
            }

        };
    }




    public ArenaGen room26largeBattleRoomWithTreasureTrove(){
        return new ArenaGen() {
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
                                ArenaBuilder.wall.FULL))
                        .addSection(new ArenaBuilder.Section(new MapCoords(defaultCoords.getX() + 1, defaultCoords.getY()),
                                ArenaBuilder.wall.NONE,
                                ArenaBuilder.wall.DOOR,
                                ArenaBuilder.wall.NONE,
                                ArenaBuilder.wall.FULL))
                        .addSection(new ArenaBuilder.Section(new MapCoords(defaultCoords.getX(), defaultCoords.getY() + 1),
                                ArenaBuilder.wall.DOOR,
                                ArenaBuilder.wall.NONE,
                                ArenaBuilder.wall.GRAPPLE,
                                ArenaBuilder.wall.NONE))
                        .addSection(new ArenaBuilder.Section(new MapCoords(defaultCoords.getX() + 1, defaultCoords.getY() + 1),
                                ArenaBuilder.wall.NONE,
                                ArenaBuilder.wall.DOOR,
                                ArenaBuilder.wall.GRAPPLE,
                                ArenaBuilder.wall.NONE)).buildArena(arena);

                arena.addEntity(decorFactory.wallBag(Measure.units(5f), Measure.units(65f), Measure.units(5f), Measure.units(5f)));
                arena.addEntity(decorFactory.wallBag(arena.getWidth() - Measure.units(10f), Measure.units(65f), Measure.units(5f), Measure.units(5f)));

                arena.addEntity(decorFactory.wallBag(Measure.units(30f), Measure.units(25f), Measure.units(130f), Measure.units(45f)));
                arena.addEntity(decorFactory.grapplePointBag(Measure.units(20f), Measure.units(50f)));
                arena.addEntity(decorFactory.grapplePointBag(arena.getWidth() - Measure.units(20f), Measure.units(50f)));

                arena.addEntity(decorFactory.lockBox(Measure.units(30f), Measure.units(10f), Measure.units(15f), Measure.units(15f)));
                arena.addEntity(decorFactory.lockBox(arena.getWidth() - Measure.units(55f), Measure.units(10f), Measure.units(15f), Measure.units(15f)));
                arena.addEntity(chestFactory.chestBag(Measure.units(50f), Measure.units(10f)));

                arena.addEntity(chestFactory.chestBag(arena.getWidth() - Measure.units(70f), Measure.units(10f)));

                arena.addEntity(decorFactory.lockWall(Measure.units(65f), Measure.units(10f), Measure.units(5f), Measure.units(15f)));
                arena.addEntity(decorFactory.lockWall(arena.getWidth() - Measure.units(80), Measure.units(10f), Measure.units(5f), Measure.units(15f)));

                arena.addEntity(chestFactory.chestBag(Measure.units(75f), Measure.units(10f)));
                arena.addEntity(chestFactory.chestBag(Measure.units(90f), Measure.units(10f)));
                arena.addEntity(chestFactory.chestBag(Measure.units(105f), Measure.units(10f)));


                arena.addEntity(arenaEnemyPlacementFactory.spawnJig(arena.getWidth() / 2, Measure.units(95f)));
                arena.addEntity(arenaEnemyPlacementFactory.spawnMovingSentry(arena.getWidth() / 4 * 3, Measure.units(105f)));


                return arena;
            }
        };
    }

    public ArenaGen room27BouncersAndAmoebas(){
        return new ArenaGen() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {

                Arena arena = new Arena(arenaSkin, defaultCoords);

                arena =  new ArenaBuilder(assetManager, arenaSkin)
                        .addSection(new ArenaBuilder.Section(defaultCoords,
                                ArenaBuilder.wall.DOOR,
                                ArenaBuilder.wall.DOOR,
                                ArenaBuilder.wall.GRAPPLE,
                                ArenaBuilder.wall.DOOR)).buildArena(arena);

                arena.roomType = Arena.RoomType.TRAP;


               // arena.addEntity(decorFactory.wallBag(Measure.units(30f), Measure.units(30f), Measure.units(40f), Measure.units(5f)));

/*
                arena.addEntity(arenaEnemyPlacementFactory.spawnBouncer(arena.getWidth() / 4 * 3, Measure.units(45f)));
                arena.addEntity(arenaEnemyPlacementFactory.spawnBouncer(arena.getWidth() / 4, Measure.units(45f)));*/


                arena.addEntity(arenaEnemyPlacementFactory.amoebaFactory.fastamoeba(0, arena.getHeight()));
                arena.addEntity(arenaEnemyPlacementFactory.amoebaFactory.fastamoeba(arena.getWidth() / 2, arena.getHeight()));
                arena.addEntity(arenaEnemyPlacementFactory.amoebaFactory.fastamoeba(arena.getWidth(), arena.getHeight()));

                arena.addEntity(arenaEnemyPlacementFactory.amoebaFactory.fastamoeba(0, arena.getHeight() / 2));
                arena.addEntity(arenaEnemyPlacementFactory.amoebaFactory.fastamoeba(arena.getWidth(), arena.getHeight() / 2));
                arena.addEntity(arenaEnemyPlacementFactory.amoebaFactory.fastamoeba(arena.getWidth() / 2, 0));

                //arena.addEntity(arenaEnemyPlacementFactory.spawnLargeBouncer());




                return arena;
            }
        };
    }



    public ArenaGen room28SmallGurnerAndAmoeba(){
        return new ArenaGen() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {

                Arena arena = new Arena(arenaSkin, defaultCoords);

                arena =  new ArenaBuilder(assetManager, arenaSkin)
                        .addSection(new ArenaBuilder.Section(defaultCoords,
                                ArenaBuilder.wall.DOOR,
                                ArenaBuilder.wall.DOOR,
                                ArenaBuilder.wall.GRAPPLE,
                                ArenaBuilder.wall.DOOR)).buildArena(arena);

                arena.roomType = Arena.RoomType.TRAP;

                arena.addEntity(arenaEnemyPlacementFactory.spawnkugelDusche(arena.getWidth() / 2, arena.getHeight() / 2));


                arena.addEntity(arenaEnemyPlacementFactory.amoebaFactory.amoeba(0, arena.getHeight()));
                arena.addEntity(arenaEnemyPlacementFactory.amoebaFactory.amoeba(arena.getWidth(), arena.getHeight()));


                return arena;
            }
        };
    }


    public ArenaGen room29Height3GrappleTreasureAndGurner(){
        return new ArenaGen() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {

                Arena arena = new Arena(arenaSkin, defaultCoords,
                        new MapCoords(defaultCoords.getX(), defaultCoords.getY() + 1),
                        new MapCoords(defaultCoords.getX(), defaultCoords.getY() + 2));


                boolean isLeftAbove = random.nextBoolean();
                //arena.roomType = Arena.RoomType.TRAP;

                arena =  new ArenaBuilder(assetManager, arenaSkin)
                        .addSection(new ArenaBuilder.Section(defaultCoords,
                                isLeftAbove ? ArenaBuilder.wall.FULL : ArenaBuilder.wall.MANDATORYDOOR,
                                isLeftAbove ? ArenaBuilder.wall.MANDATORYDOOR : ArenaBuilder.wall.FULL,
                                ArenaBuilder.wall.NONE,
                                ArenaBuilder.wall.DOOR))
                        .addSection(new ArenaBuilder.Section(new MapCoords(defaultCoords.getX(), defaultCoords.getY() + 1),
                                ArenaBuilder.wall.FULL,
                                ArenaBuilder.wall.FULL,
                                ArenaBuilder.wall.NONE,
                                ArenaBuilder.wall.NONE))
                        .addSection(new ArenaBuilder.Section(new MapCoords(defaultCoords.getX(), defaultCoords.getY() + 2),
                                isLeftAbove ? ArenaBuilder.wall.MANDATORYDOOR : ArenaBuilder.wall.FULL,
                                isLeftAbove ? ArenaBuilder.wall.FULL : ArenaBuilder.wall.MANDATORYDOOR,
                                ArenaBuilder.wall.GRAPPLE,
                                ArenaBuilder.wall.NONE)).buildArena(arena);


                float oddPosx = isLeftAbove ? arena.getWidth() / 4 * 3 : arena.getWidth() / 4;
                float evenPosX = isLeftAbove ? arena.getWidth() / 4 : arena.getWidth() / 4 * 3;


                arena.addEntity(decorFactory.grapplePointBag(oddPosx, Measure.units(45f)));
                arena.addEntity(decorFactory.grapplePointBag(evenPosX, Measure.units(75)));
                arena.addEntity(decorFactory.grapplePointBag(oddPosx, Measure.units(105f)));
                arena.addEntity(decorFactory.grapplePointBag(evenPosX, Measure.units(135f)));

                arena.addEntity(chestFactory.chestBag(isLeftAbove ? Measure.units(15f) : arena.getWidth() - Measure.units(25f), Measure.units(20f), chestFactory.trapODAC()));

                //arena.addEntity(arenaEnemyPlacementFactory.s);

//                arena.roomType = Arena.RoomType.TRAP;

                arena.addWave(
                        arenaEnemyPlacementFactory.spawnkugelDusche(arena.getWidth() / 2, Measure.units(25f))
                );

                return arena;
            }
        };
    }


    public ArenaGen room30BlobsAndAmoebasAndTurrets(){
        return new ArenaGen() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {

                Arena arena = arenaShellFactory.createSmallArenaNoGrapple(defaultCoords);

                arena.roomType = Arena.RoomType.TRAP;

                arena.addEntity(arenaEnemyPlacementFactory.spawnerFactory.spawnerBag(arena.getWidth() / 2, Measure.units(45f),
                        new SpawnerFactory.Spawner() {
                            public Bag<Component> spawnBag(float x, float y) {
                                return arenaEnemyPlacementFactory.blobFactory.smallblobBag(x, y, random.nextBoolean());
                            }
                        }, 3));


                arena.addEntity(decorFactory.wallBag(Measure.units(30f), Measure.units(30f), Measure.units(40f), Measure.units(5f)));


                arena.addEntity(arenaEnemyPlacementFactory.amoebaFactory.fastamoeba(0, arena.getHeight()));
                arena.addEntity(arenaEnemyPlacementFactory.amoebaFactory.fastamoeba(arena.getWidth() / 2, arena.getHeight()));
                arena.addEntity(arenaEnemyPlacementFactory.amoebaFactory.fastamoeba(arena.getWidth(), arena.getHeight()));

                return arena;

            }

        };
    }



}
