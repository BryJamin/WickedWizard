package com.byrjamin.wickedwizard.factories.arenas.levels;

import com.artemis.Component;
import com.artemis.utils.Bag;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.utils.Array;
import com.byrjamin.wickedwizard.ecs.components.texture.TextureRegionComponent;
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
import com.byrjamin.wickedwizard.utils.ComponentBag;
import com.byrjamin.wickedwizard.utils.MapCoords;
import com.byrjamin.wickedwizard.utils.Measure;
import com.byrjamin.wickedwizard.utils.enums.Level;

import java.util.Random;

import static com.byrjamin.wickedwizard.factories.arenas.decor.ArenaShellFactory.SECTION_HEIGHT;
import static com.byrjamin.wickedwizard.factories.arenas.decor.ArenaShellFactory.SECTION_WIDTH;
import static com.byrjamin.wickedwizard.factories.arenas.decor.DecorFactory.DECORATIVE_BEAM_WIDTH;

/**
 * Created by Home on 17/05/2017.
 */

public class Level2Rooms extends AbstractFactory implements ArenaRepostiory {

    private ArenaShellFactory arenaShellFactory;
    private ChestFactory chestFactory;
    private DecorFactory decorFactory;
    private ArenaEnemyPlacementFactory arenaEnemyPlacementFactory;
    private Random random;

    private ArenaSkin arenaSkin;

    public Level2Rooms(AssetManager assetManager, ArenaSkin arenaSkin, Random random) {
        super(assetManager);
        this.arenaShellFactory = new com.byrjamin.wickedwizard.factories.arenas.decor.ArenaShellFactory(assetManager, arenaSkin);
        this.chestFactory = new ChestFactory(assetManager);
        this.arenaEnemyPlacementFactory = new com.byrjamin.wickedwizard.factories.arenas.decor.ArenaEnemyPlacementFactory(assetManager, arenaSkin, random);
        this.decorFactory = new com.byrjamin.wickedwizard.factories.arenas.decor.DecorFactory(assetManager, arenaSkin);
        this.random = random;
        this.arenaSkin = arenaSkin;
    }

    public Level2Rooms(AssetManager assetManager, Random random) {
        this(assetManager, Level.TWO.getArenaSkin(), random);
    }


    @Override
    public Array<ArenaCreate> getAllArenas() {
        return  getLevel2RoomArray();
    }


    public Array<ArenaCreate> getLevel2RoomArray(){

        Array<ArenaCreate> ag = new Array<ArenaCreate>();

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
        ag.insert(20, room21Width3SpikePlatforms());
        ag.insert(21, room22VerticalThroughRoomTwocorridors());
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


    public ArenaCreate room1Kugel(){
        return new ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {

                Arena arena = arenaShellFactory.createOmniArenaHiddenGrapple(defaultCoords);

                arena.addEntity(arenaEnemyPlacementFactory.spawnkugelDusche(arena.getWidth() / 2,Measure.units(27.5f)));
                arena.roomType = Arena.RoomType.TRAP;
                return arena;
            }
        };
    }

    public ArenaCreate room2GoatWizardCenter(){
        return new ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {

                Arena arena = arenaShellFactory.createOmniArenaHiddenGrapple(defaultCoords);
                arena.addEntity(arenaEnemyPlacementFactory.spawnGoatWizard(arena.getWidth() / 2,(arena.getHeight() / 2)));
                arena.roomType = Arena.RoomType.TRAP;
                return arena;
            }
        };
    }


    public ArenaCreate room3OneTurretTwoLargeBouncers(){
        return new ArenaCreate() {
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
    public ArenaCreate room4Width2RoomOnlyVerticalExits(){
        return new ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {
                //TODO need to add change mandatory doors

                Arena arena =  new ArenaBuilder(assetManager, arenaSkin)
                        .addSection(new ArenaBuilder.Section(defaultCoords,
                                ArenaBuilder.wall.FULL,
                                ArenaBuilder.wall.NONE,
                                ArenaBuilder.wall.MANDATORYDOOR,
                                ArenaBuilder.wall.DOOR))
                        .addSection(new ArenaBuilder.Section(new MapCoords(defaultCoords.getX() + 1, defaultCoords.getY()),
                                ArenaBuilder.wall.NONE,
                                ArenaBuilder.wall.FULL,
                                ArenaBuilder.wall.DOOR,
                                ArenaBuilder.wall.MANDATORYDOOR)).buildArena();


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


    public ArenaCreate room5trapAmoeba(){
        return new ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {

                Arena arena = arenaShellFactory.createOmniArenaHiddenGrapple(defaultCoords);

                ComponentBag bag = new ChestFactory(assetManager).chestBag(Measure.units(45f), Measure.units(25f), chestFactory.trapODAC());

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

    public ArenaCreate room6largeRoomWithSentries(){
        return new ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {

                Arena arena =  new ArenaBuilder(assetManager, arenaSkin)
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
                                        ArenaBuilder.wall.NONE)).buildArena();


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



    public ArenaCreate room7Height2BlobPatrol() {


        return new ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {

                Arena arena = new ArenaBuilder(assetManager, arenaSkin)
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



    public ArenaCreate room8TreasureLockedCenter() {


        return new ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {

                Arena arena = arenaShellFactory.createSmallArenaNoGrapple(defaultCoords);


                boolean variation = random.nextBoolean();


                if(variation) {

                    arena.addEntity(decorFactory.decorativeBlock(Measure.units(30f), Measure.units(30f), DECORATIVE_BEAM_WIDTH, Measure.units(30f), TextureRegionComponent.BACKGROUND_LAYER_MIDDLE));
                    arena.addEntity(decorFactory.decorativeBlock(Measure.units(70f) - DECORATIVE_BEAM_WIDTH, Measure.units(30f), DECORATIVE_BEAM_WIDTH, Measure.units(30f), TextureRegionComponent.BACKGROUND_LAYER_MIDDLE));
                    arena.addEntity(decorFactory.wallBag(Measure.units(25f), Measure.units(30f), Measure.units(50f), Measure.units(5f)));


                    arena.addEntity(arenaEnemyPlacementFactory.hoarderFactory.hoarder(Measure.units(50f), Measure.units(20f)));
                    arena.addEntity(chestFactory.chestBag(Measure.units(32.5f), Measure.units(35f)));
                    arena.addEntity(chestFactory.chestBag(Measure.units(45f), Measure.units(35f)));
                    arena.addEntity(chestFactory.chestBag(Measure.units(57.5f), Measure.units(35f)));

                } else {


                    arena.addEntity(decorFactory.decorativeBlock(Measure.units(30f), Measure.units(30f), DECORATIVE_BEAM_WIDTH, Measure.units(30f), TextureRegionComponent.BACKGROUND_LAYER_MIDDLE));
                    arena.addEntity(decorFactory.decorativeBlock(Measure.units(70f) - DECORATIVE_BEAM_WIDTH, Measure.units(30f), DECORATIVE_BEAM_WIDTH, Measure.units(30f), TextureRegionComponent.BACKGROUND_LAYER_MIDDLE));
                    arena.addEntity(decorFactory.wallBag(Measure.units(25f), Measure.units(25f), Measure.units(50f), Measure.units(5f)));


                    arena.addEntity(arenaEnemyPlacementFactory.hoarderFactory.hoarder(Measure.units(50f), Measure.units(45f)));
                    arena.addEntity(chestFactory.chestBag(Measure.units(32.5f), Measure.units(30f)));
                    arena.addEntity(chestFactory.chestBag(Measure.units(45f), Measure.units(30f)));
                    arena.addEntity(chestFactory.chestBag(Measure.units(57.5f), Measure.units(30f)));




                }
                //arena.roomType = Arena.RoomType.TRAP;

                return arena;


            }

        };

    }




    public ArenaCreate room9SpikeWallJump() {


        return new ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {

                Arena arena = new ArenaBuilder(assetManager, arenaSkin)
                        .addSection(new ArenaBuilder.Section(defaultCoords,
                                ArenaBuilder.wall.MANDATORYDOOR,
                                ArenaBuilder.wall.MANDATORYDOOR,
                                ArenaBuilder.wall.FULL,
                                ArenaBuilder.wall.FULL))
                        .buildArena();


                arena.addEntity(decorFactory.wallBag(Measure.units(30f), Measure.units(10f), Measure.units(40f), Measure.units(15f)));
                arena.addEntity(decorFactory.spikeWall(Measure.units(30f), Measure.units(25f), Measure.units(40f), Measure.units(5f), 0));

                //Top Spikes
                arena.addEntity(decorFactory.spikeWall(Measure.units(5f), Measure.units(50f), Measure.units(90f), Measure.units(5f), 180));

                return arena;


            }

        };

    }


    public ArenaCreate room10JigCenter() {
        return new ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {
                Arena arena = arenaShellFactory.createOmniArenaHiddenGrapple(new MapCoords());
                arena.roomType = Arena.RoomType.TRAP;
                arena.addEntity(arenaEnemyPlacementFactory.spawnMovingJig(arena.getWidth() / 2, Measure.units(40f)));
                return arena;
            }

        };

    }

    public ArenaCreate room11Height2Treasure() {
        return new ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {

                Arena arena = new ArenaBuilder(assetManager, arenaSkin)
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

    public ArenaCreate room12WallTurretsAndBouncers() {
        return new ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {
                Arena arena = arenaShellFactory.createOmniArenaHiddenGrapple(new MapCoords());
                arena.roomType = Arena.RoomType.TRAP;
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


    public ArenaCreate room13GoatWizardAndBlobs() {
        return new ArenaCreate() {
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


    public ArenaCreate room14WalkingThroughRoom() {
        return new ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {

                boolean isLeft = random.nextBoolean();

                Arena arena = new ArenaBuilder(assetManager, arenaSkin)
                        .addSection(new ArenaBuilder.Section(defaultCoords,
                                isLeft ? ArenaBuilder.wall.MANDATORYDOOR : ArenaBuilder.wall.FULL,
                                isLeft ? ArenaBuilder.wall.FULL : ArenaBuilder.wall.MANDATORYDOOR,
                                ArenaBuilder.wall.MANDATORYGRAPPLE,
                                ArenaBuilder.wall.MANDATORYDOOR)).buildArena();

                float wallPos = isLeft ? arena.getWidth() - Measure.units(40f) : Measure.units(5f);
                float skywallPos = isLeft ? Measure.units(5f) : arena.getWidth() - Measure.units(40);

                arena.addEntity(decorFactory.wallBag(wallPos, Measure.units(5f), Measure.units(35), arena.getHeight()));
                arena.addEntity(decorFactory.wallBag(skywallPos, Measure.units(30f), Measure.units(35), Measure.units(30f)));


                return arena;
            }

        };
    }

    public ArenaCreate room15JigAndTurretWithGuard() {
        return new ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {

                Arena arena = new ArenaBuilder(assetManager, arenaSkin)
                        .addSection(new ArenaBuilder.Section(defaultCoords,
                                ArenaBuilder.wall.DOOR,
                                ArenaBuilder.wall.DOOR,
                                ArenaBuilder.wall.GRAPPLE,
                                ArenaBuilder.wall.DOOR)).buildArena();

                arena.addEntity(decorFactory.wallBag(Measure.units(20f), Measure.units(20f), Measure.units(5f), Measure.units(25f)));
                arena.addEntity(decorFactory.wallBag(arena.getWidth() - Measure.units(25f), Measure.units(20f), Measure.units(5f), Measure.units(25f)));

                arena.addEntity(arenaEnemyPlacementFactory.spawnMovingJig(arena.getWidth() / 2, arena.getHeight() / 2));
                arena.addEntity(arenaEnemyPlacementFactory.spawnBouncer(arena.getWidth() / 2, Measure.units(45f)));

                return arena;
            }

        };
    }



    public ArenaCreate room16TrapTreasureTwoGoatWizards() {
        return new ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {


                boolean chestsAreLeft = random.nextBoolean();

                Arena arena = new ArenaBuilder(assetManager, arenaSkin)
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
                        arenaEnemyPlacementFactory.spawnGoatWizard(
                                chestsAreLeft ? arena.getWidth() - Measure.units(20f) : Measure.units(20f),
                                Measure.units(45f), chestsAreLeft, true),
                        arenaEnemyPlacementFactory.spawnGoatWizard(
                                chestsAreLeft ? arena.getWidth() - Measure.units(20f) : Measure.units(20f), Measure.units(20f), chestsAreLeft, true)
                        );

                arena.addEntity(new ChestFactory(assetManager).chestBag(chestsAreLeft ? Measure.units(40f) : Measure.units(150f), Measure.units(10f),
                        chestFactory.trapODAC()));

                arena.addEntity(new ChestFactory(assetManager).chestBag(chestsAreLeft ? Measure.units(25f) : Measure.units(165f), Measure.units(10f),
                        chestFactory.trapODAC()));

                return arena;
            }

        };
    }




    public ArenaCreate room17ChestInCenterTwoTurretsOnWalls() {
        return new ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {

                Arena arena = new ArenaBuilder(assetManager, arenaSkin)
                        .addSection(new ArenaBuilder.Section(defaultCoords,
                                ArenaBuilder.wall.DOOR,
                                ArenaBuilder.wall.DOOR,
                                ArenaBuilder.wall.FULL,
                                ArenaBuilder.wall.FULL))
                        .buildArena();

                for(int i = 0; i < 5; i++){
                    arena.addEntity(decorFactory.fixedWallTurret(Measure.units(5), Measure.units(30f + (i * 5f)), -90, 4f, 0));
                    arena.addEntity(decorFactory.fixedWallTurret(arena.getWidth() - Measure.units(10), Measure.units(30f + (i * 5f)), 90, 4f, 2));
                }

                arena.addEntity(decorFactory.wallBag(Measure.units(35f), Measure.units(10f), Measure.units(5f), Measure.units(20f)));
                arena.addEntity(decorFactory.wallBag(Measure.units(60f), Measure.units(10f), Measure.units(5f), Measure.units(20f)));

                ChestFactory cf = new ChestFactory(assetManager);
                arena.addEntity(cf.chestBag(Measure.units(45), Measure.units(10f)));



                return arena;
            }

        };
    }



    public ArenaCreate room18TwoSilverheadsOnPlatforms() {
        return new ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {
                Arena arena = new ArenaBuilder(assetManager, arenaSkin)
                        .addSection(new ArenaBuilder.Section(defaultCoords,
                                ArenaBuilder.wall.DOOR,
                                ArenaBuilder.wall.DOOR,
                                ArenaBuilder.wall.GRAPPLE,
                                ArenaBuilder.wall.DOOR))
                        .buildArena();

                arena.roomType = Arena.RoomType.TRAP;


                arena.addEntity(decorFactory.platform(Measure.units(5f), Measure.units(26f), Measure.units(25f)));
                arena.addEntity(decorFactory.platform(arena.getWidth() - Measure.units(30f), Measure.units(26f), Measure.units(25f)));

                arena.addEntity(arenaEnemyPlacementFactory.spawnSilverHead(Measure.units(17.5f), Measure.units(40f)));
                arena.addEntity(arenaEnemyPlacementFactory.spawnSilverHead(arena.getWidth() - Measure.units(17.5f), Measure.units(40f)));

                return arena;
            }

        };
    }



    public ArenaCreate room19MultiWaveRoomEndsInGoatAndChest() {
        return new ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {
                Arena arena = arenaShellFactory.createOmniArenaHiddenGrapple(new MapCoords(), random.nextInt(4));
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


    public ArenaCreate room20MultiWaveRoomEndsInJigAndChest() {
        return new ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {

                Arena arena = arenaShellFactory.createOmniArenaHiddenGrapple(defaultCoords, random.nextInt(4));

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


    public ArenaCreate room21Width3SpikePlatforms() {
        return new ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {


                Arena arena = new ArenaBuilder(assetManager, arenaSkin)
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


    public ArenaCreate room22VerticalThroughRoomTwocorridors() {
        return new ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {

                Arena arena = new ArenaBuilder(assetManager, arenaSkin)
                        .addSection(new ArenaBuilder.Section(defaultCoords,
                                ArenaBuilder.wall.FULL,
                                ArenaBuilder.wall.FULL,
                                ArenaBuilder.wall.MANDATORYDOOR,
                                ArenaBuilder.wall.MANDATORYDOOR))
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




    public ArenaCreate room23HeightTwoWithChestSpawnsJigAndBomb() {
        return new ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {


                boolean isLeft = random.nextBoolean();

                Arena arena = new ArenaBuilder(assetManager, arenaSkin)
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
                        new SpawnerFactory.Spawner() {
                            public Bag<Component> spawnBag(float x, float y) {
                                return new BombFactory(assetManager).bomb(x,y, 1f);
                            }
                        }));

                return arena;
            }

        };
    }



    public ArenaCreate room24BombSpawn() {
        return new ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {

                Arena arena = new ArenaBuilder(assetManager, arenaSkin)
                        .addSection(new ArenaBuilder.Section(defaultCoords,
                                ArenaBuilder.wall.DOOR,
                                ArenaBuilder.wall.DOOR,
                                ArenaBuilder.wall.GRAPPLE,
                                ArenaBuilder.wall.DOOR))
                        .buildArena();

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


    public ArenaCreate room25BombTrap() {
        return new ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {
                Arena arena = new ArenaBuilder(assetManager, arenaSkin)
                        .addSection(new ArenaBuilder.Section(defaultCoords,
                                ArenaBuilder.wall.DOOR,
                                ArenaBuilder.wall.DOOR,
                                ArenaBuilder.wall.GRAPPLE,
                                ArenaBuilder.wall.DOOR))
                        .buildArena();

                arena.addEntity(chestFactory.chestBag(Measure.units(45f), Measure.units(10f), chestFactory.trapODAC()));

                Bag<Bag<Component>> bags = new Bag<Bag<Component>>();

                for(int i = 0; i < 4; i++) bags.add(arenaEnemyPlacementFactory.spawnerFactory.spawnerBag(Measure.units(20f + (i * 20f)), Measure.units(45f),
                        new SpawnerFactory.Spawner() {
                            public Bag<Component> spawnBag(float x, float y) {
                                return new BombFactory(assetManager).bomb(x,y, 1f);
                            }
                        }));

                arena.addWave(bags);

                return arena;
            }

        };
    }




    public ArenaCreate room26largeBattleRoomWithTreasureTrove(){
        return new ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {

                Arena arena =  new ArenaBuilder(assetManager, arenaSkin)
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
                                ArenaBuilder.wall.NONE))
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

    public ArenaCreate room27FastAmoebas(){
        return new ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {

                Arena arena =  new ArenaBuilder(assetManager, arenaSkin)
                        .addSection(new ArenaBuilder.Section(defaultCoords,
                                ArenaBuilder.wall.DOOR,
                                ArenaBuilder.wall.DOOR,
                                ArenaBuilder.wall.GRAPPLE,
                                ArenaBuilder.wall.DOOR)).buildArena();

                arena.addEntity(arenaEnemyPlacementFactory.amoebaFactory.fastamoeba(0, arena.getHeight()));
                arena.addEntity(arenaEnemyPlacementFactory.amoebaFactory.fastamoeba(arena.getWidth() / 2, arena.getHeight()));
                arena.addEntity(arenaEnemyPlacementFactory.amoebaFactory.fastamoeba(arena.getWidth(), arena.getHeight()));

                arena.addEntity(arenaEnemyPlacementFactory.amoebaFactory.fastamoeba(0, arena.getHeight() / 2));
                arena.addEntity(arenaEnemyPlacementFactory.amoebaFactory.fastamoeba(arena.getWidth(), arena.getHeight() / 2));

                return arena;
            }
        };
    }



    public ArenaCreate room28SmallTurnersAndAChest(){
        return new ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {

                Arena arena =  new ArenaBuilder(assetManager, arenaSkin)
                        .addSection(new ArenaBuilder.Section(defaultCoords,
                                ArenaBuilder.wall.DOOR,
                                ArenaBuilder.wall.DOOR,
                                ArenaBuilder.wall.GRAPPLE,
                                ArenaBuilder.wall.DOOR)).buildArena();

                arena.roomType = Arena.RoomType.TRAP;

                boolean bool = random.nextBoolean();

                arena.addWave(arenaEnemyPlacementFactory.spawnkugelDusche(arena.getWidth() / 2, Measure.units(27.5f), bool));
                arena.addWave(arenaEnemyPlacementFactory.spawnkugelDusche(arena.getWidth() / 2, Measure.units(27.5f), !bool));
                arena.addWave(chestFactory.centeredChestBag(arena.getWidth() / 2, arena.getHeight() / 2));



                return arena;
            }
        };
    }


    public ArenaCreate room29Height3GrappleTreasureAndTurner(){
        return new ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {

                boolean isLeftAbove = random.nextBoolean();

                Arena arena =  new ArenaBuilder(assetManager, arenaSkin)
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
                                ArenaBuilder.wall.NONE)).buildArena();


                float oddPosx = isLeftAbove ? arena.getWidth() / 4 * 3 : arena.getWidth() / 4;
                float evenPosX = isLeftAbove ? arena.getWidth() / 4 : arena.getWidth() / 4 * 3;


                arena.addEntity(decorFactory.grapplePointBag(oddPosx, Measure.units(45f)));
                arena.addEntity(decorFactory.grapplePointBag(evenPosX, Measure.units(75)));
                arena.addEntity(decorFactory.grapplePointBag(oddPosx, Measure.units(105f)));
                arena.addEntity(decorFactory.grapplePointBag(evenPosX, Measure.units(135f)));

                arena.addEntity(chestFactory.chestBag(isLeftAbove ? Measure.units(15f) : arena.getWidth() - Measure.units(25f), Measure.units(10f), chestFactory.trapODAC()));

                //arena.addEntity(arenaEnemyPlacementFactory.s);

//                arena.roomType = Arena.RoomType.TRAP;

                arena.addWave(
                        arenaEnemyPlacementFactory.spawnkugelDusche(arena.getWidth() / 2, Measure.units(25f))
                );

                return arena;
            }
        };
    }


    public ArenaCreate room30BlobsAndAmoebasAndTurrets(){
        return new ArenaCreate() {
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
