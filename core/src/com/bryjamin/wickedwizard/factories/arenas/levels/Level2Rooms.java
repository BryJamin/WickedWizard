package com.bryjamin.wickedwizard.factories.arenas.levels;

import com.artemis.Component;
import com.artemis.utils.Bag;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.utils.Array;
import com.bryjamin.wickedwizard.ecs.components.texture.TextureRegionComponent;
import com.bryjamin.wickedwizard.factories.AbstractFactory;
import com.bryjamin.wickedwizard.factories.BombFactory;
import com.bryjamin.wickedwizard.factories.arenas.Arena;
import com.bryjamin.wickedwizard.factories.arenas.decor.ArenaEnemyPlacementFactory;
import com.bryjamin.wickedwizard.factories.arenas.skins.ArenaSkin;
import com.bryjamin.wickedwizard.utils.ComponentBag;
import com.bryjamin.wickedwizard.utils.MapCoords;
import com.bryjamin.wickedwizard.utils.Measure;

import java.util.Random;

/**
 * Created by Home on 17/05/2017.
 */

public class Level2Rooms extends AbstractFactory implements ArenaRepostiory {

    private com.bryjamin.wickedwizard.factories.arenas.decor.ArenaShellFactory arenaShellFactory;
    private com.bryjamin.wickedwizard.factories.chests.ChestFactory chestFactory;
    private com.bryjamin.wickedwizard.factories.arenas.decor.DecorFactory decorFactory;
    private ArenaEnemyPlacementFactory arenaEnemyPlacementFactory;
    private Random random;

    private ArenaSkin arenaSkin;

    public Level2Rooms(AssetManager assetManager, ArenaSkin arenaSkin, Random random) {
        super(assetManager);
        this.arenaShellFactory = new com.bryjamin.wickedwizard.factories.arenas.decor.ArenaShellFactory(assetManager, arenaSkin);
        this.chestFactory = new com.bryjamin.wickedwizard.factories.chests.ChestFactory(assetManager);
        this.arenaEnemyPlacementFactory = new ArenaEnemyPlacementFactory(assetManager, arenaSkin, random);
        this.decorFactory = new com.bryjamin.wickedwizard.factories.arenas.decor.DecorFactory(assetManager, arenaSkin);
        this.random = random;
        this.arenaSkin = arenaSkin;
    }

    public Level2Rooms(AssetManager assetManager, Random random) {
        this(assetManager, com.bryjamin.wickedwizard.utils.enums.Level.TWO.getArenaSkin(), random);
    }


    @Override
    public Array<com.bryjamin.wickedwizard.factories.arenas.ArenaCreate> getAllArenas() {
        return  getLevel2RoomArray();
    }


    public Array<com.bryjamin.wickedwizard.factories.arenas.ArenaCreate> getLevel2RoomArray(){

        Array<com.bryjamin.wickedwizard.factories.arenas.ArenaCreate> ag = new Array<com.bryjamin.wickedwizard.factories.arenas.ArenaCreate>();

        ag.insert(0, room1Kugel()); //Lowered
        ag.insert(1, room2GoatWizardCenter()); //Edited wizard to an easier version
        ag.insert(2, room3OneTurretTwoLargeBouncers()); //Excellent place to view the collision bug
        ag.insert(3, room4Width2RoomOnlyVerticalExits()); //Not much here to be honest
        ag.insert(4, room5trapAmoeba()); // room5GrappleTreasureRoom()); //Only real problem with this room is I never use maces again
        ag.insert(5, room6largeRoomWithSentries()); //Come back to this one?
        ag.insert(6, room7Height2BlobPatrol());
        ag.insert(7, room8TreasureLockedCenter()); //One of these chests should be a trap?
        ag.insert(8, room9SpikeWallJump());
        ag.insert(9, room10JigCenter());
        ag.insert(10, room11Height2Treasure()); //Too technical?
        ag.insert(11, room12WallTurretsAndBouncers());
        ag.insert(12, room13GoatWizardAndBlobs());
        ag.insert(13, room14WalkingThroughRoom());
        ag.insert(14, room15JigAndTurretWithGuard());
        ag.insert(15, room16TrapTreasureTwoGoatWizards()); //Added a mirrored version
        ag.insert(16, room17ChestInCenterTwoTurretsOnWalls());
        ag.insert(17, room18TwoSilverheadsOnPlatforms());
        ag.insert(18, room19MultiWaveRoomEndsInGoatAndChest());
        ag.insert(19, room20MultiWaveRoomEndsInJigAndChest());
        ag.insert(20, room21Width3GroundWallTurretsAndThreeExits());
        ag.insert(21, room22VerticalThroughRoomWithSpikeonWalls());
        ag.insert(22, room23HeightTwoWithChestSpawnsJigAndBomb()); //Maybe make this a trap chest?
        ag.insert(23, room24BombSpawn());
        ag.insert(24, room25BombTrap());
        ag.insert(25, room26largeBattleRoomWithTreasureTrove());
        ag.insert(26, room27FastAmoebas());
        ag.insert(27, room28SmallTurnersAndAChest());
        ag.insert(28, room29Height3GrappleTreasureAndTurner());
        ag.insert(29, room30BlobsAndAmoebasAndTurrets());
        return ag;

    }


    public com.bryjamin.wickedwizard.factories.arenas.ArenaCreate room1Kugel(){
        return new com.bryjamin.wickedwizard.factories.arenas.ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {

                Arena arena = arenaShellFactory.createOmniArenaHiddenGrapple(defaultCoords, Arena.ArenaType.TRAP);

                arena.addEntity(arenaEnemyPlacementFactory.spawnkugelDusche(arena.getWidth() / 2, Measure.units(27.5f)));
                return arena;
            }
        };
    }

    public com.bryjamin.wickedwizard.factories.arenas.ArenaCreate room2GoatWizardCenter(){
        return new com.bryjamin.wickedwizard.factories.arenas.ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {

                Arena arena = arenaShellFactory.createOmniArenaHiddenGrapple(defaultCoords, Arena.ArenaType.TRAP);
                arena.addEntity(arenaEnemyPlacementFactory.spawnGoatWizard(arena.getWidth() / 2,(arena.getHeight() / 2)));
                return arena;
            }
        };
    }


    public com.bryjamin.wickedwizard.factories.arenas.ArenaCreate room3OneTurretTwoLargeBouncers(){
        return new com.bryjamin.wickedwizard.factories.arenas.ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {
                Arena a = arenaShellFactory.createOmniArenaHiddenGrapple(defaultCoords, Arena.ArenaType.TRAP);
                a.addEntity(arenaEnemyPlacementFactory.spawnFixedSentry(a.getWidth() / 2, a.getHeight() / 2));
                a.addEntity(arenaEnemyPlacementFactory.spawnLargeBouncer(a.getWidth() - Measure.units(20), a.getHeight() - Measure.units(15f)));
                a.addEntity(arenaEnemyPlacementFactory.spawnLargeBouncer(Measure.units(20), a.getHeight() - Measure.units(15f)));
                return a;
            }
        };
    }


    //TODO Not a fan.
    public com.bryjamin.wickedwizard.factories.arenas.ArenaCreate room4Width2RoomOnlyVerticalExits(){
        return new com.bryjamin.wickedwizard.factories.arenas.ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {
                //TODO need to add change mandatory doors

                Arena arena =  new com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder(assetManager, arenaSkin, Arena.ArenaType.NORMAL)
                        .addSection(new com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.Section(defaultCoords,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.FULL,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.NONE,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.MANDATORYDOOR,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.DOOR))
                        .addSection(new com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.Section(new MapCoords(defaultCoords.getX() + 1, defaultCoords.getY()),
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.NONE,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.FULL,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.DOOR,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.MANDATORYDOOR)).buildArena();


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


    public com.bryjamin.wickedwizard.factories.arenas.ArenaCreate room5trapAmoeba(){
        return new com.bryjamin.wickedwizard.factories.arenas.ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {

                Arena arena = arenaShellFactory.createOmniArenaHiddenGrapple(defaultCoords, Arena.ArenaType.NORMAL);

                ComponentBag bag = new com.bryjamin.wickedwizard.factories.chests.ChestFactory(assetManager).chestBag(Measure.units(45f), Measure.units(25f), chestFactory.trapODAC());

                Bag<Bag<Component>> bags = new Bag<Bag<Component>>();

                for(int i = 0; i < 3; i++) bags.add(arenaEnemyPlacementFactory.amoebaFactory.amoeba(Measure.units(0), Measure.units(20 * i)));
                for(int i = 0; i < 3; i++) bags.add(arenaEnemyPlacementFactory.amoebaFactory.amoeba(arena.getWidth(), Measure.units(20 * i)));
                for(int i = 0; i < 4; i++) bags.add(arenaEnemyPlacementFactory.amoebaFactory.amoeba(Measure.units(25 * i), arena.getHeight()));
                for(int i = 0; i < 4; i++) bags.add(arenaEnemyPlacementFactory.amoebaFactory.amoeba(Measure.units(25 * i), 0));

                arena.addWave(bags);

                arena.addEntity(bag);
                arena.addEntity(decorFactory.platform(Measure.units(35f), Measure.units(20f), Measure.units(30f)));

                return arena;
            }
        };
    }



    //TODO make better

    public com.bryjamin.wickedwizard.factories.arenas.ArenaCreate room6largeRoomWithSentries(){
        return new com.bryjamin.wickedwizard.factories.arenas.ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {

                Arena arena =  new com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder(assetManager, arenaSkin, Arena.ArenaType.NORMAL)
                        .addSection(new com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.Section(defaultCoords,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.DOOR,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.NONE,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.NONE,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.DOOR))
                        .addSection(new com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.Section(new MapCoords(defaultCoords.getX() + 1, defaultCoords.getY()),
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.NONE,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.DOOR,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.NONE,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.DOOR))
                        .addSection(new com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.Section(new MapCoords(defaultCoords.getX(), defaultCoords.getY() + 1),
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.DOOR,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.NONE,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.GRAPPLE,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.NONE))
                        .addSection(new com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.Section(new MapCoords(defaultCoords.getX() + 1, defaultCoords.getY() + 1),
                                        com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.NONE,
                                        com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.DOOR,
                                        com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.GRAPPLE,
                                        com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.NONE)).buildArena();


                arena.addEntity(decorFactory.platform(0, com.bryjamin.wickedwizard.factories.arenas.decor.ArenaShellFactory.SECTION_HEIGHT + Measure.units(5), arena.getWidth()));

                arena.addEntity(decorFactory.platform(0, com.bryjamin.wickedwizard.factories.arenas.decor.ArenaShellFactory.SECTION_HEIGHT / 2, arena.getWidth()));


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



    public com.bryjamin.wickedwizard.factories.arenas.ArenaCreate room7Height2BlobPatrol() {


        return new com.bryjamin.wickedwizard.factories.arenas.ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {

                Arena arena = new com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder(assetManager, arenaSkin, Arena.ArenaType.NORMAL)
                        .addSection(new com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.Section(defaultCoords,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.MANDATORYDOOR,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.MANDATORYDOOR,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.NONE,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.DOOR))
                        .addSection(new com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.Section(new MapCoords(defaultCoords.getX(), defaultCoords.getY() + 1),
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.FULL,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.FULL,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.MANDATORYGRAPPLE,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.NONE))
                        .buildArena();


                //WALLS
                arena.addEntity(decorFactory.wallBag(Measure.units(25f), Measure.units(30f), Measure.units(75f), Measure.units(5f), arenaSkin));
                arena.addEntity(decorFactory.platform(0, Measure.units(30f), Measure.units(25f)));

                arena.addEntity(decorFactory.wallBag(0, Measure.units(55f), Measure.units(35f), Measure.units(60f), arenaSkin));
                arena.addEntity(decorFactory.wallBag(Measure.units(65f), Measure.units(55f), Measure.units(35f), Measure.units(60f), arenaSkin));
                arena.addEntity(decorFactory.platform(Measure.units(35f), Measure.units(55f), Measure.units(30f)));

                // arena.addEntity(decorFactory.grapplePointBag(arena.getWidth() / 2, Measure.units(70f)));

                arena.addEntity(arenaEnemyPlacementFactory.spawnBlob(Measure.units(15f), Measure.units(20f)));
                arena.addEntity(arenaEnemyPlacementFactory.spawnBlob(arena.getWidth() - Measure.units(15f), Measure.units(45f)));

                return arena;


            }

        };

    }



    public com.bryjamin.wickedwizard.factories.arenas.ArenaCreate room8TreasureLockedCenter() {


        return new com.bryjamin.wickedwizard.factories.arenas.ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {

                Arena arena = arenaShellFactory.createSmallArenaNoGrapple(defaultCoords, Arena.ArenaType.NORMAL);

                boolean variation = random.nextBoolean();

                if(variation) {

                    arena.addEntity(decorFactory.decorativeBlock(Measure.units(30f), Measure.units(30f), com.bryjamin.wickedwizard.factories.arenas.decor.DecorFactory.DECORATIVE_BEAM_WIDTH, Measure.units(30f), TextureRegionComponent.BACKGROUND_LAYER_MIDDLE));
                    arena.addEntity(decorFactory.decorativeBlock(Measure.units(70f) - com.bryjamin.wickedwizard.factories.arenas.decor.DecorFactory.DECORATIVE_BEAM_WIDTH, Measure.units(30f), com.bryjamin.wickedwizard.factories.arenas.decor.DecorFactory.DECORATIVE_BEAM_WIDTH, Measure.units(30f), TextureRegionComponent.BACKGROUND_LAYER_MIDDLE));
                    arena.addEntity(decorFactory.wallBag(Measure.units(25f), Measure.units(30f), Measure.units(50f), Measure.units(5f)));


                    arena.addEntity(arenaEnemyPlacementFactory.hoarderFactory.hoarder(Measure.units(50f), Measure.units(20f)));
                    arena.addEntity(chestFactory.chestBag(Measure.units(32.5f), Measure.units(35f)));
                    arena.addEntity(chestFactory.chestBag(Measure.units(45f), Measure.units(35f)));
                    arena.addEntity(chestFactory.chestBag(Measure.units(57.5f), Measure.units(35f)));

                } else {


                    arena.addEntity(decorFactory.decorativeBlock(Measure.units(30f), Measure.units(30f), com.bryjamin.wickedwizard.factories.arenas.decor.DecorFactory.DECORATIVE_BEAM_WIDTH, Measure.units(30f), TextureRegionComponent.BACKGROUND_LAYER_MIDDLE));
                    arena.addEntity(decorFactory.decorativeBlock(Measure.units(70f) - com.bryjamin.wickedwizard.factories.arenas.decor.DecorFactory.DECORATIVE_BEAM_WIDTH, Measure.units(30f), com.bryjamin.wickedwizard.factories.arenas.decor.DecorFactory.DECORATIVE_BEAM_WIDTH, Measure.units(30f), TextureRegionComponent.BACKGROUND_LAYER_MIDDLE));
                    arena.addEntity(decorFactory.wallBag(Measure.units(25f), Measure.units(25f), Measure.units(50f), Measure.units(5f)));


                    arena.addEntity(arenaEnemyPlacementFactory.hoarderFactory.hoarder(Measure.units(50f), Measure.units(45f)));
                    arena.addEntity(chestFactory.chestBag(Measure.units(32.5f), Measure.units(30f)));
                    arena.addEntity(chestFactory.chestBag(Measure.units(45f), Measure.units(30f)));
                    arena.addEntity(chestFactory.chestBag(Measure.units(57.5f), Measure.units(30f)));




                }
                //arena.arenaType = Arena.ArenaType.TRAP;

                return arena;


            }

        };

    }




    public com.bryjamin.wickedwizard.factories.arenas.ArenaCreate room9SpikeWallJump() {


        return new com.bryjamin.wickedwizard.factories.arenas.ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {

                Arena arena = new com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder(assetManager, arenaSkin, Arena.ArenaType.NORMAL)
                        .addSection(new com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.Section(defaultCoords,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.MANDATORYDOOR,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.MANDATORYDOOR,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.FULL,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.FULL))
                        .buildArena();


                arena.addEntity(decorFactory.wallBag(Measure.units(30f), Measure.units(10f), Measure.units(40f), Measure.units(15f)));
                arena.addEntity(decorFactory.spikeWall(Measure.units(30f), Measure.units(25f), Measure.units(40f), Measure.units(5f), 0));

                //Top Spikes
                arena.addEntity(decorFactory.spikeWall(Measure.units(5f), Measure.units(50f), Measure.units(90f), Measure.units(5f), 180));

                return arena;


            }

        };

    }


    public com.bryjamin.wickedwizard.factories.arenas.ArenaCreate room10JigCenter() {
        return new com.bryjamin.wickedwizard.factories.arenas.ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {
                Arena arena = arenaShellFactory.createOmniArenaHiddenGrapple(new MapCoords(), Arena.ArenaType.TRAP);
                arena.addEntity(arenaEnemyPlacementFactory.spawnMovingJig(arena.getWidth() / 2, Measure.units(40f)));
                return arena;
            }

        };

    }

    public com.bryjamin.wickedwizard.factories.arenas.ArenaCreate room11Height2Treasure() {
        return new com.bryjamin.wickedwizard.factories.arenas.ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {

                Arena arena = new com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder(assetManager, arenaSkin, Arena.ArenaType.NORMAL)
                        .addSection(new com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.Section(defaultCoords,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.DOOR,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.DOOR,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.NONE,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.DOOR))
                        .addSection(new com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.Section(new MapCoords(defaultCoords.getX(), defaultCoords.getY() + 1),
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.FULL,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.FULL,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.FULL,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.NONE))
                        .buildArena();

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

    public com.bryjamin.wickedwizard.factories.arenas.ArenaCreate room12WallTurretsAndBouncers() {
        return new com.bryjamin.wickedwizard.factories.arenas.ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {
                Arena arena = arenaShellFactory.createOmniArenaHiddenGrapple(new MapCoords(), Arena.ArenaType.TRAP);
                arena.addEntity(arenaEnemyPlacementFactory.spawnBouncer(arena.getWidth() / 4, arena.getHeight() / 2));
                arena.addEntity(arenaEnemyPlacementFactory.spawnFixedSentry(arena.getWidth() / 2, arena.getHeight() / 4 * 3));
                arena.addEntity(arenaEnemyPlacementFactory.spawnBouncer(arena.getWidth() / 4 * 3, arena.getHeight() / 2));


                arena.addEntity(decorFactory.inCombatfixedWallTurret(Measure.units(20f), arena.getHeight() - Measure.units(10f), 180, 1.5f, 0));
                //arena.addEntity(decorFactory.fixedWallTurret(Measure.units(25f), arena.getHeight() - Measure.units(10f), 270, 1.5f, 0));
                arena.addEntity(decorFactory.inCombatfixedWallTurret(arena.getWidth() - Measure.units(25f), arena.getHeight() - Measure.units(10f), 180, 1.5f, 0));
                //arena.addEntity(decorFactory.fixedWallTurret(arena.getWidth() - Measure.units(30), arena.getHeight() - Measure.units(10f), 270, 1.5f, 0));

                return arena;
            }

        };

    }


    public com.bryjamin.wickedwizard.factories.arenas.ArenaCreate room13GoatWizardAndBlobs() {
        return new com.bryjamin.wickedwizard.factories.arenas.ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {

                Arena arena =  new com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder(assetManager, arenaSkin, Arena.ArenaType.TRAP)
                        .addSection(new com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.Section(defaultCoords,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.DOOR,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.NONE,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.GRAPPLE,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.DOOR))
                        .addSection(new com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.Section(new MapCoords(defaultCoords.getX() + 1, defaultCoords.getY()),
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.NONE,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.DOOR,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.GRAPPLE,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.DOOR)).buildArena();
                
                arena.addEntity(arenaEnemyPlacementFactory.spawnBlob(arena.getWidth() / 4, arena.getHeight() / 2));
                arena.addEntity(arenaEnemyPlacementFactory.spawnGoatWizard(arena.getWidth() / 2, arena.getHeight() / 2));
                arena.addEntity(arenaEnemyPlacementFactory.spawnBlob(arena.getWidth() / 4 * 3, arena.getHeight() / 2));
                return arena;
            }

        };
    }


    public com.bryjamin.wickedwizard.factories.arenas.ArenaCreate room14WalkingThroughRoom() {
        return new com.bryjamin.wickedwizard.factories.arenas.ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {

                boolean isLeft = random.nextBoolean();

                Arena arena = new com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder(assetManager, arenaSkin, Arena.ArenaType.NORMAL)
                        .addSection(new com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.Section(defaultCoords,
                                isLeft ? com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.MANDATORYDOOR : com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.FULL,
                                isLeft ? com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.FULL : com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.MANDATORYDOOR,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.MANDATORYGRAPPLE,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.MANDATORYDOOR)).buildArena();

                float wallPos = isLeft ? arena.getWidth() - Measure.units(40f) : Measure.units(5f);
                float skywallPos = isLeft ? Measure.units(5f) : arena.getWidth() - Measure.units(40);

                arena.addEntity(decorFactory.wallBag(wallPos, Measure.units(5f), Measure.units(35), arena.getHeight()));
                arena.addEntity(decorFactory.wallBag(skywallPos, Measure.units(30f), Measure.units(35), Measure.units(30f)));


                return arena;
            }

        };
    }

    public com.bryjamin.wickedwizard.factories.arenas.ArenaCreate room15JigAndTurretWithGuard() {
        return new com.bryjamin.wickedwizard.factories.arenas.ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {

                Arena arena = new com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder(assetManager, arenaSkin, Arena.ArenaType.TRAP)
                        .addSection(new com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.Section(defaultCoords,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.DOOR,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.DOOR,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.GRAPPLE,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.DOOR)).buildArena();

                arena.addEntity(decorFactory.wallBag(Measure.units(20f), Measure.units(20f), Measure.units(5f), Measure.units(25f)));
                arena.addEntity(decorFactory.wallBag(arena.getWidth() - Measure.units(25f), Measure.units(20f), Measure.units(5f), Measure.units(25f)));

                arena.addEntity(arenaEnemyPlacementFactory.spawnMovingJig(arena.getWidth() / 2, arena.getHeight() / 2));
                arena.addEntity(arenaEnemyPlacementFactory.spawnBouncer(arena.getWidth() / 2, Measure.units(45f)));

                return arena;
            }

        };
    }



    public com.bryjamin.wickedwizard.factories.arenas.ArenaCreate room16TrapTreasureTwoGoatWizards() {
        return new com.bryjamin.wickedwizard.factories.arenas.ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {


                boolean chestsAreLeft = random.nextBoolean();

                Arena arena = new com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder(assetManager, arenaSkin, Arena.ArenaType.NORMAL)
                        .addSection(new com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.Section(defaultCoords,
                                chestsAreLeft ? com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.FULL : com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.DOOR,
                                chestsAreLeft ? com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.NONE : com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.NONE,
                                chestsAreLeft ? com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.FULL : com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.GRAPPLE,
                                chestsAreLeft ? com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.FULL : com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.DOOR))
                        .addSection(new com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.Section(new MapCoords(defaultCoords.getX() + 1, defaultCoords.getY()),
                                chestsAreLeft ? com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.NONE : com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.NONE,
                                chestsAreLeft ? com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.DOOR : com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.FULL,
                                chestsAreLeft ? com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.GRAPPLE : com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.FULL,
                                chestsAreLeft ? com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.DOOR : com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.FULL)).buildArena();


                arena.addWave(
                        arenaEnemyPlacementFactory.spawnGoatWizard(
                                chestsAreLeft ? arena.getWidth() - Measure.units(20f) : Measure.units(20f),
                                Measure.units(45f), chestsAreLeft, true),
                        arenaEnemyPlacementFactory.spawnGoatWizard(
                                chestsAreLeft ? arena.getWidth() - Measure.units(20f) : Measure.units(20f), Measure.units(20f), chestsAreLeft, true)
                        );

                arena.addEntity(new com.bryjamin.wickedwizard.factories.chests.ChestFactory(assetManager).chestBag(chestsAreLeft ? Measure.units(40f) : Measure.units(150f), Measure.units(10f),
                        chestFactory.trapODAC()));

                arena.addEntity(new com.bryjamin.wickedwizard.factories.chests.ChestFactory(assetManager).chestBag(chestsAreLeft ? Measure.units(25f) : Measure.units(165f), Measure.units(10f),
                        chestFactory.trapODAC()));

                return arena;
            }

        };
    }




    public com.bryjamin.wickedwizard.factories.arenas.ArenaCreate room17ChestInCenterTwoTurretsOnWalls() {
        return new com.bryjamin.wickedwizard.factories.arenas.ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {

                Arena arena = new com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder(assetManager, arenaSkin, Arena.ArenaType.NORMAL)
                        .addSection(new com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.Section(defaultCoords,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.DOOR,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.DOOR,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.FULL,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.FULL))
                        .buildArena();

                for(int i = 0; i < 5; i++){
                    arena.addEntity(decorFactory.fixedWallTurret(Measure.units(5), Measure.units(30f + (i * 5f)), -90, 4f, 0));
                    arena.addEntity(decorFactory.fixedWallTurret(arena.getWidth() - Measure.units(10), Measure.units(30f + (i * 5f)), 90, 4f, 2));
                }

                arena.addEntity(decorFactory.wallBag(Measure.units(35f), Measure.units(10f), Measure.units(5f), Measure.units(20f)));
                arena.addEntity(decorFactory.wallBag(Measure.units(60f), Measure.units(10f), Measure.units(5f), Measure.units(20f)));

                com.bryjamin.wickedwizard.factories.chests.ChestFactory cf = new com.bryjamin.wickedwizard.factories.chests.ChestFactory(assetManager);
                arena.addEntity(cf.chestBag(Measure.units(45), Measure.units(10f)));



                return arena;
            }

        };
    }



    public com.bryjamin.wickedwizard.factories.arenas.ArenaCreate room18TwoSilverheadsOnPlatforms() {
        return new com.bryjamin.wickedwizard.factories.arenas.ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {
                Arena arena = new com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder(assetManager, arenaSkin, Arena.ArenaType.TRAP)
                        .addSection(new com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.Section(defaultCoords,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.DOOR,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.DOOR,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.GRAPPLE,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.DOOR))
                        .buildArena();

                arena.addEntity(decorFactory.platform(Measure.units(5f), Measure.units(26f), Measure.units(25f)));
                arena.addEntity(decorFactory.platform(arena.getWidth() - Measure.units(30f), Measure.units(26f), Measure.units(25f)));

                arena.addEntity(arenaEnemyPlacementFactory.spawnSilverHead(Measure.units(17.5f), Measure.units(40f)));
                arena.addEntity(arenaEnemyPlacementFactory.spawnSilverHead(arena.getWidth() - Measure.units(17.5f), Measure.units(40f)));

                return arena;
            }

        };
    }



    public com.bryjamin.wickedwizard.factories.arenas.ArenaCreate room19MultiWaveRoomEndsInGoatAndChest() {
        return new com.bryjamin.wickedwizard.factories.arenas.ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {
                Arena arena = arenaShellFactory.createOmniArenaHiddenGrapple(new MapCoords(), random.nextInt(4), Arena.ArenaType.TRAP);

                arena.addWave(arenaEnemyPlacementFactory.spawnMovingSentry(arena.getWidth() / 2, Measure.units(40f)),
                        arenaEnemyPlacementFactory.spawnBouncer(arena.getWidth() / 4, Measure.units(40f)),
                        arenaEnemyPlacementFactory.spawnBouncer(arena.getWidth() / 4 * 3, Measure.units(40f)));

                arena.addWave(arenaEnemyPlacementFactory.spawnGoatWizard(arena.getWidth() / 2, Measure.units(40f)));

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


    public com.bryjamin.wickedwizard.factories.arenas.ArenaCreate room20MultiWaveRoomEndsInJigAndChest() {
        return new com.bryjamin.wickedwizard.factories.arenas.ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {

                Arena arena = arenaShellFactory.createOmniArenaHiddenGrapple(defaultCoords, random.nextInt(4), Arena.ArenaType.TRAP);

                arena.addWave(arenaEnemyPlacementFactory.spawnSilverHead(Measure.units(20f), Measure.units(40f)),
                        arenaEnemyPlacementFactory.spawnSilverHead(arena.getWidth() - Measure.units(20f), Measure.units(40f)));

                arena.addWave(arenaEnemyPlacementFactory.spawnJig(arena.getWidth() / 2, Measure.units(40f)));
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


    public com.bryjamin.wickedwizard.factories.arenas.ArenaCreate room21Width3GroundWallTurretsAndThreeExits() {
        return new com.bryjamin.wickedwizard.factories.arenas.ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {


                Arena arena = new com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder(assetManager, arenaSkin, Arena.ArenaType.NORMAL)
                        .addSection(new com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.Section(defaultCoords,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.MANDATORYDOOR,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.NONE,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.FULL,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.DOOR))
                        .addSection(new com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.Section(new MapCoords(defaultCoords.getX() + 1, defaultCoords.getY()),
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.NONE,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.NONE,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.MANDATORYDOOR,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.FULL))
                        .addSection(new com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.Section(new MapCoords(defaultCoords.getX() + 2, defaultCoords.getY()),
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.NONE,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.MANDATORYDOOR,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.FULL,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.DOOR))
                        .buildArena();



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


    public com.bryjamin.wickedwizard.factories.arenas.ArenaCreate room22VerticalThroughRoomWithSpikeonWalls() {
        return new com.bryjamin.wickedwizard.factories.arenas.ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {

                Arena arena = new com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder(assetManager, arenaSkin, Arena.ArenaType.NORMAL)
                        .addSection(new com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.Section(defaultCoords,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.FULL,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.FULL,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.MANDATORYDOOR,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.MANDATORYDOOR))
                        .buildArena();


                if(random.nextBoolean()) {

                    arena.addEntity(decorFactory.wallBag(Measure.units(60f), Measure.units(10f), Measure.units(35f), Measure.units((65f))));
                    arena.addEntity(decorFactory.wallBag(Measure.units(25f), Measure.units(22.5f), Measure.units(35f), Measure.units((20f))));
                    arena.addEntity(decorFactory.spikeWall(Measure.units(5f), Measure.units(10f), Measure.units(5f), Measure.units(50f), 270));

                } else {

                    arena.addEntity(decorFactory.wallBag(Measure.units(5f), Measure.units(10f), Measure.units(35f), Measure.units((65f))));
                    arena.addEntity(decorFactory.wallBag(Measure.units(40f), Measure.units(22.5f), Measure.units(35f), Measure.units((20f))));
                    arena.addEntity(decorFactory.spikeWall(arena.getWidth() - Measure.units(10f), Measure.units(10f), Measure.units(5f), Measure.units(50f), 90));

                }

                return arena;
            }

        };
    }




    public com.bryjamin.wickedwizard.factories.arenas.ArenaCreate room23HeightTwoWithChestSpawnsJigAndBomb() {
        return new com.bryjamin.wickedwizard.factories.arenas.ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {


                boolean isLeft = random.nextBoolean();

                Arena arena = new com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder(assetManager, arenaSkin, Arena.ArenaType.NORMAL)
                        .addSection(new com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.Section(defaultCoords,
                                isLeft ? com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.MANDATORYDOOR : com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.DOOR,
                                isLeft ? com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.DOOR : com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.MANDATORYDOOR,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.NONE,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.FULL))
                        .addSection(new com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.Section(new MapCoords(defaultCoords.getX(), defaultCoords.getY() + 1),
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.FULL,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.FULL,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.MANDATORYGRAPPLE,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.NONE))
                        .buildArena();

                arena.addEntity(decorFactory.wallBag(Measure.units(5f), Measure.units(30f), Measure.units(30f), Measure.units(5f)));
                arena.addEntity(decorFactory.platform(Measure.units(35f), Measure.units(30f), Measure.units(30f)));
                arena.addEntity(decorFactory.wallBag(Measure.units(65f), Measure.units(30f), Measure.units(30f), Measure.units(5f)));


                arena.addEntity(decorFactory.platform(Measure.units(5f), Measure.units(55f), Measure.units(30f)));
                arena.addEntity(decorFactory.wallBag(Measure.units(35f), Measure.units(55f), Measure.units(30f), Measure.units(5f)));
                arena.addEntity(decorFactory.platform(Measure.units(65f), Measure.units(55f), Measure.units(30f)));


                arena.addEntity(chestFactory.chestBag(Measure.units(45f), Measure.units(60f), chestFactory.trapODAC()));

                arena.addWave(arenaEnemyPlacementFactory.spawnMovingJig(arena.getWidth() / 2, Measure.units(85f)));


                arena.addWave(arenaEnemyPlacementFactory.spawnerFactory.spawnerBag(arena.getWidth() / 2, Measure.units(85f),
                        new com.bryjamin.wickedwizard.factories.enemy.SpawnerFactory.Spawner() {
                            public Bag<Component> spawnBag(float x, float y) {
                                return new com.bryjamin.wickedwizard.factories.BombFactory(assetManager).bomb(x,y, BombFactory.BOMB_LIFE);
                            }
                        }));

                return arena;
            }

        };
    }



    public com.bryjamin.wickedwizard.factories.arenas.ArenaCreate room24BombSpawn() {
        return new com.bryjamin.wickedwizard.factories.arenas.ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {

                Arena arena = new com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder(assetManager, arenaSkin, Arena.ArenaType.TRAP)
                        .addSection(new com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.Section(defaultCoords,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.DOOR,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.DOOR,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.GRAPPLE,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.DOOR))
                        .buildArena();

                arena.addWave(arenaEnemyPlacementFactory.spawnerFactory.spawnerBag(arena.getWidth() / 4, Measure.units(45f),
                        new com.bryjamin.wickedwizard.factories.enemy.SpawnerFactory.Spawner() {
                            public Bag<Component> spawnBag(float x, float y) {
                                return new com.bryjamin.wickedwizard.factories.BombFactory(assetManager).bomb(x,y, 1f);
                            }
                        }));

                arena.addWave(arenaEnemyPlacementFactory.spawnerFactory.spawnerBag(arena.getWidth() / 4 * 3, Measure.units(45f),
                        new com.bryjamin.wickedwizard.factories.enemy.SpawnerFactory.Spawner() {
                            public Bag<Component> spawnBag(float x, float y) {
                                return new com.bryjamin.wickedwizard.factories.BombFactory(assetManager).bomb(x,y, 1f);
                            }
                        }));


                arena.addWave(arenaEnemyPlacementFactory.spawnerFactory.spawnerBag(arena.getWidth() / 2, Measure.units(45f),
                        new com.bryjamin.wickedwizard.factories.enemy.SpawnerFactory.Spawner() {
                            public Bag<Component> spawnBag(float x, float y) {
                                return new com.bryjamin.wickedwizard.factories.BombFactory(assetManager).bomb(x,y, 1f);
                            }
                        }));

                return arena;
            }

        };
    }


    public com.bryjamin.wickedwizard.factories.arenas.ArenaCreate room25BombTrap() {
        return new com.bryjamin.wickedwizard.factories.arenas.ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {
                Arena arena = new com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder(assetManager, arenaSkin, Arena.ArenaType.NORMAL)
                        .addSection(new com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.Section(defaultCoords,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.DOOR,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.DOOR,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.GRAPPLE,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.DOOR))
                        .buildArena();

                arena.addEntity(chestFactory.chestBag(Measure.units(45f), Measure.units(10f), chestFactory.trapODAC()));

                Bag<Bag<Component>> bags = new Bag<Bag<Component>>();

                for(int i = 0; i < 4; i++) bags.add(arenaEnemyPlacementFactory.spawnerFactory.spawnerBag(Measure.units(20f + (i * 20f)), Measure.units(45f),
                        new com.bryjamin.wickedwizard.factories.enemy.SpawnerFactory.Spawner() {
                            public Bag<Component> spawnBag(float x, float y) {
                                return new com.bryjamin.wickedwizard.factories.BombFactory(assetManager).bomb(x,y, 1f);
                            }
                        }));

                arena.addWave(bags);

                return arena;
            }

        };
    }




    public com.bryjamin.wickedwizard.factories.arenas.ArenaCreate room26largeBattleRoomWithTreasureTrove(){
        return new com.bryjamin.wickedwizard.factories.arenas.ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {

                Arena arena =  new com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder(assetManager, arenaSkin, Arena.ArenaType.NORMAL)
                        .addSection(new com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.Section(defaultCoords,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.DOOR,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.NONE,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.NONE,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.FULL))
                        .addSection(new com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.Section(new MapCoords(defaultCoords.getX() + 1, defaultCoords.getY()),
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.NONE,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.DOOR,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.NONE,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.FULL))
                        .addSection(new com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.Section(new MapCoords(defaultCoords.getX(), defaultCoords.getY() + 1),
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.DOOR,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.NONE,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.GRAPPLE,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.NONE))
                        .addSection(new com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.Section(new MapCoords(defaultCoords.getX() + 1, defaultCoords.getY() + 1),
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.NONE,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.DOOR,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.GRAPPLE,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.NONE))
                        .buildArena();

                arena.addEntity(decorFactory.platform(Measure.units(5f), Measure.units(65f), Measure.units(25f)));
                arena.addEntity(decorFactory.platform(arena.getWidth() - Measure.units(30f), Measure.units(65f), Measure.units(25f)));

                arena.addEntity(decorFactory.wallBag(Measure.units(30f), Measure.units(25f), Measure.units(140f), Measure.units(45f)));
                arena.addEntity(decorFactory.grapplePointBag(Measure.units(17.5f), Measure.units(50f)));
                arena.addEntity(decorFactory.grapplePointBag(arena.getWidth() - Measure.units(17.5f), Measure.units(50f)));


                boolean variation = random.nextBoolean();

                if(variation) {

                    arena.addEntity(arenaEnemyPlacementFactory.hoarderFactory.hoarder(Measure.units(95f), Measure.units(17.5f)));

                    arena.addEntity(chestFactory.chestBag(Measure.units(75f), Measure.units(10f)));
                    arena.addEntity(chestFactory.chestBag(Measure.units(90f), Measure.units(10f)));
                    arena.addEntity(chestFactory.chestBag(Measure.units(105f), Measure.units(10f)));

                } else {

                    arena.addEntity(chestFactory.chestBag(Measure.units(90f), Measure.units(10f)));
                }

                arena.addEntity(arenaEnemyPlacementFactory.spawnJig(arena.getWidth() / 2, Measure.units(95f)));

                return arena;
            }
        };
    }

    public com.bryjamin.wickedwizard.factories.arenas.ArenaCreate room27FastAmoebas(){
        return new com.bryjamin.wickedwizard.factories.arenas.ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {

                Arena arena =  new com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder(assetManager, arenaSkin, Arena.ArenaType.TRAP)
                        .addSection(new com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.Section(defaultCoords,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.DOOR,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.DOOR,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.GRAPPLE,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.DOOR)).buildArena();

                arena.addEntity(arenaEnemyPlacementFactory.amoebaFactory.fastamoeba(0, arena.getHeight()));
                arena.addEntity(arenaEnemyPlacementFactory.amoebaFactory.fastamoeba(arena.getWidth() / 2, arena.getHeight()));
                arena.addEntity(arenaEnemyPlacementFactory.amoebaFactory.fastamoeba(arena.getWidth(), arena.getHeight()));
                arena.addEntity(arenaEnemyPlacementFactory.amoebaFactory.fastamoeba(0, arena.getHeight() / 2));
                arena.addEntity(arenaEnemyPlacementFactory.amoebaFactory.fastamoeba(arena.getWidth(), arena.getHeight() / 2));

                return arena;
            }
        };
    }



    public com.bryjamin.wickedwizard.factories.arenas.ArenaCreate room28SmallTurnersAndAChest(){
        return new com.bryjamin.wickedwizard.factories.arenas.ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {

                Arena arena =  new com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder(assetManager, arenaSkin, Arena.ArenaType.TRAP)
                        .addSection(new com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.Section(defaultCoords,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.DOOR,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.DOOR,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.GRAPPLE,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.DOOR)).buildArena();

                boolean bool = random.nextBoolean();

                arena.addWave(arenaEnemyPlacementFactory.spawnkugelDusche(arena.getWidth() / 2, Measure.units(27.5f), bool));
                arena.addWave(arenaEnemyPlacementFactory.spawnkugelDusche(arena.getWidth() / 2, Measure.units(27.5f), !bool));
                arena.addWave(chestFactory.centeredChestBag(arena.getWidth() / 2, arena.getHeight() / 2));



                return arena;
            }
        };
    }


    public com.bryjamin.wickedwizard.factories.arenas.ArenaCreate room29Height3GrappleTreasureAndTurner(){
        return new com.bryjamin.wickedwizard.factories.arenas.ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {

                boolean isLeftAbove = random.nextBoolean();

                Arena arena =  new com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder(assetManager, arenaSkin, Arena.ArenaType.NORMAL)
                        .addSection(new com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.Section(defaultCoords,
                                isLeftAbove ? com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.FULL : com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.MANDATORYDOOR,
                                isLeftAbove ? com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.MANDATORYDOOR : com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.FULL,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.NONE,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.DOOR))
                        .addSection(new com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.Section(new MapCoords(defaultCoords.getX(), defaultCoords.getY() + 1),
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.FULL,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.FULL,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.NONE,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.NONE))
                        .addSection(new com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.Section(new MapCoords(defaultCoords.getX(), defaultCoords.getY() + 2),
                                isLeftAbove ? com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.MANDATORYDOOR : com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.FULL,
                                isLeftAbove ? com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.FULL : com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.MANDATORYDOOR,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.GRAPPLE,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.NONE)).buildArena();


                float oddPosx = isLeftAbove ? arena.getWidth() / 4 * 3 : arena.getWidth() / 4;
                float evenPosX = isLeftAbove ? arena.getWidth() / 4 : arena.getWidth() / 4 * 3;


                arena.addEntity(decorFactory.grapplePointBag(oddPosx, Measure.units(45f)));
                arena.addEntity(decorFactory.grapplePointBag(evenPosX, Measure.units(75)));
                arena.addEntity(decorFactory.grapplePointBag(oddPosx, Measure.units(105f)));
                arena.addEntity(decorFactory.grapplePointBag(evenPosX, Measure.units(135f)));

                arena.addEntity(chestFactory.chestBag(isLeftAbove ? Measure.units(15f) : arena.getWidth() - Measure.units(25f), Measure.units(10f), chestFactory.trapODAC()));

                //arena.addEntity(arenaEnemyPlacementFactory.s);

//                arena.arenaType = Arena.ArenaType.TRAP;

                arena.addWave(
                        arenaEnemyPlacementFactory.spawnkugelDusche(arena.getWidth() / 2, Measure.units(25f))
                );

                return arena;
            }
        };
    }


    public com.bryjamin.wickedwizard.factories.arenas.ArenaCreate room30BlobsAndAmoebasAndTurrets(){
        return new com.bryjamin.wickedwizard.factories.arenas.ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {

                Arena arena = arenaShellFactory.createSmallArenaNoGrapple(defaultCoords, Arena.ArenaType.TRAP);


                arena.addEntity(arenaEnemyPlacementFactory.spawnerFactory.spawnerBag(arena.getWidth() / 2, Measure.units(45f),
                        new com.bryjamin.wickedwizard.factories.enemy.SpawnerFactory.Spawner() {
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
