package com.byrjamin.wickedwizard.factories.arenas.levels;

import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Array;
import com.byrjamin.wickedwizard.ecs.components.ai.Task;
import com.byrjamin.wickedwizard.ecs.components.ai.OnDeathActionComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.VelocityComponent;
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
        ag.add(room6largeBattleRoom());
        ag.add(room6trapAmoeba());
        ag.add(room7Height2BlobPatrol());
        ag.add(room8TreasureLockedCenter());
        ag.add(room9SpikeWallJump());
        ag.add(room10JigCenter());
        ag.add(room11Height2Treasure());
        ag.add(room12WallTurretsAndBouncers());
        ag.add(room13GoatWizardAndBlobs());
        ag.add(room14WalkingThroughRoom());
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
                                ArenaBuilder.wall.DOOR,
                                ArenaBuilder.wall.DOOR))
                        .buildArena(arena);

                arena.addEntity(arenaEnemyPlacementFactory.kugelDuscheFactory.kugelDusche(arena.getWidth() / 2,(arena.getHeight() / 2) + Measure.units(2.5f)));
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


/*
                arena.addEntity(arenaEnemyPlacementFactory.spawnBouncer(Measure.units(15f), Measure.units(17.5f)));

                arena.addEntity(arenaEnemyPlacementFactory.spawnBouncer(Measure.units(15f), Measure.units(27.5f)));
                arena.addEntity(arenaEnemyPlacementFactory.spawnBouncer(arena.getWidth() - Measure.units(20f), Measure.units(17.5f)));
                arena.addEntity(arenaEnemyPlacementFactory.spawnBouncer(arena.getWidth() - Measure.units(20f), Measure.units(32.5f)));
*/



                arena.addEntity(decorFactory.fixedWallTurret(Measure.units(5), arena.getHeight() - Measure.units(10),  -90, 3.0f, 1.5f));
                arena.addEntity(decorFactory.fixedWallTurret(Measure.units(10), arena.getHeight() - Measure.units(10),  -90, 3.0f, 1.5f));
                arena.addEntity(decorFactory.fixedWallTurret(Measure.units(15), arena.getHeight() - Measure.units(10),  -90, 3.0f, 1.5f));

                arena.addEntity(decorFactory.fixedWallTurret(arena.getWidth() - Measure.units(10), arena.getHeight() - Measure.units(10),  -90, 3.0f, 1.5f));
                arena.addEntity(decorFactory.fixedWallTurret(arena.getWidth() - Measure.units(15), arena.getHeight() - Measure.units(10),  -90, 3.0f, 1.5f));
                arena.addEntity(decorFactory.fixedWallTurret(arena.getWidth() - Measure.units(20), arena.getHeight() - Measure.units(10),  -90, 3.0f, 1.5f));

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

                arena.setWidth(SECTION_WIDTH);
                arena.setHeight(SECTION_HEIGHT * 3);

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

    public ArenaGen room6largeBattleRoom(){
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




                arena.addEntity(arenaEnemyPlacementFactory.spawnMovingSentry(Measure.units(40f), arena.getHeight() / 2 - Measure.units(5)));


                arena.addEntity(arenaEnemyPlacementFactory.spawnMovingSentry(Measure.units(40f), Measure.units(100f)));

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
                                ArenaBuilder.wall.NONE))
                        .buildArena(arena);


                arena.addEntity(decorFactory.wallBag(0, Measure.units(5f), Measure.units(20f), Measure.units(5f)));
                arena.addEntity(decorFactory.wallBag(Measure.units(80f), Measure.units(5f), Measure.units(20f), Measure.units(5f)));
                arena.addEntity(decorFactory.wallBag(0, 0, arena.getWidth(), Measure.units(5f)));

                arena.addEntity(decorFactory.spikeWall(Measure.units(20f), Measure.units(5f), Measure.units(60f), Measure.units(5f), 0));

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


                arena.addEntity(decorFactory.fixedWallTurret(Measure.units(20f), arena.getHeight() - Measure.units(10f), 270, 1.5f, 0));
                //arena.addEntity(decorFactory.fixedWallTurret(Measure.units(25f), arena.getHeight() - Measure.units(10f), 270, 1.5f, 0));
                arena.addEntity(decorFactory.fixedWallTurret(arena.getWidth() - Measure.units(25f), arena.getHeight() - Measure.units(10f), 270, 1.5f, 0));
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
                Arena arena = new Arena(arenaSkin, new MapCoords());

                boolean isLeft = false; //random.nextBoolean();

                arena.setWidth(SECTION_WIDTH);
                arena.setHeight(SECTION_HEIGHT);

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



}
