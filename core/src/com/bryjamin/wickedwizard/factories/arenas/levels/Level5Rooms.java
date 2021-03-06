package com.bryjamin.wickedwizard.factories.arenas.levels;

import com.artemis.Component;
import com.artemis.utils.Bag;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.bryjamin.wickedwizard.ecs.components.texture.TextureRegionComponent;
import com.bryjamin.wickedwizard.factories.AbstractFactory;
import com.bryjamin.wickedwizard.factories.arenas.Arena;
import com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder;
import com.bryjamin.wickedwizard.factories.arenas.ArenaCreate;
import com.bryjamin.wickedwizard.factories.arenas.decor.ArenaEnemyPlacementFactory;
import com.bryjamin.wickedwizard.factories.arenas.skins.ArenaSkin;
import com.bryjamin.wickedwizard.factories.weapons.enemy.LaserOrbitalTask;
import com.bryjamin.wickedwizard.utils.MapCoords;
import com.bryjamin.wickedwizard.utils.Measure;

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


    public ArenaCreate room1PentaSentry() {
        return new ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {

                Arena arena =  new ArenaBuilder(assetManager, arenaSkin, Arena.ArenaType.TRAP)
                        .addSection(new ArenaBuilder.Section(defaultCoords,
                                ArenaBuilder.wall.DOOR,
                                ArenaBuilder.wall.DOOR,
                                ArenaBuilder.wall.FULL,
                                ArenaBuilder.wall.DOOR))
                        .buildArena();
                arena.addEntity(arenaEnemyPlacementFactory.turretFactory.fixedPentaSentry(arena.getWidth() / 2, Measure.units(45f)));
                return arena;
            }
        };
    }

    public ArenaCreate room2DoubleFlyBy() {
        return new ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {
                Arena arena =  new ArenaBuilder(assetManager, arenaSkin, Arena.ArenaType.TRAP)
                        .addSection(new ArenaBuilder.Section(defaultCoords,
                                ArenaBuilder.wall.DOOR,
                                ArenaBuilder.wall.DOOR,
                                ArenaBuilder.wall.FULL,
                                ArenaBuilder.wall.DOOR))
                        .buildArena();
                arena.addEntity(arenaEnemyPlacementFactory.turretFactory.fixedFlyByDoubleBombSentry(arena.getWidth() / 2, Measure.units(45f)));
                return arena;
            }
        };
    }


    public ArenaCreate room3Width2DoubleFlyByAndPentaSentryMoving() {
        return new ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {

                boolean ceilingIsLeft = random.nextBoolean();

                Arena arena =  new ArenaBuilder(assetManager, arenaSkin, Arena.ArenaType.TRAP)
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
                        .buildArena();

                arena.addEntity(arenaEnemyPlacementFactory.spawnMovingPentaSentry(arena.getWidth() / 4, Measure.units(45f)));
                arena.addEntity(arenaEnemyPlacementFactory.spawnMovingFlyByDoubleBombSentry(arena.getWidth() / 4 * 3, Measure.units(45f)));

                return arena;
            }
        };
    }





    public ArenaCreate room4TrapRoomPentaAndDoubleFlyBy() {
        return new ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {
                Arena arena = arenaShellFactory.createOmniArenaHiddenGrapple(defaultCoords, random.nextInt(4), Arena.ArenaType.TRAP);

                arena.addWave(arenaEnemyPlacementFactory.spawnMovingPentaSentry(arena.getWidth() / 2, Measure.units(45f)));
                arena.addWave(arenaEnemyPlacementFactory.spawnMovingFlyByDoubleBombSentry(arena.getWidth() / 2, Measure.units(45f)));

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




    public ArenaCreate room5HeavyModon() {
        return new ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {
                Arena arena = arenaShellFactory.createOmniArenaHiddenGrapple(defaultCoords, Arena.ArenaType.TRAP);
                arena.addEntity(arenaEnemyPlacementFactory.spawnHeavyModon(arena.getWidth() / 2, Measure.units(40f)));
                return arena;
            }
        };
    }




    public ArenaCreate room6GhostlyEncounter() {
        return new ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {
                Arena arena = arenaShellFactory.createOmniArenaHiddenGrapple(defaultCoords, Arena.ArenaType.TRAP);

                boolean startsLeft = random.nextBoolean();

                arena.addEntity(arenaEnemyPlacementFactory.cowlFactory.cowl(arena.getWidth() / 2, Measure.units(40f),
                        new Vector3(arena.getWidth() / 2, Measure.units(45f), 0),
                        Measure.units(5f),
                        180, startsLeft));
                return arena;
            }
        };
    }


    public ArenaCreate room7SingularGhostlyEncounterWithACenter() {
        return new ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {
                Arena arena = arenaShellFactory.createOmniArenaHiddenGrapple(defaultCoords, Arena.ArenaType.TRAP);
                boolean startsLeft = random.nextBoolean();

                boolean isOnLeft = random.nextBoolean();

                arena.addWave(arenaEnemyPlacementFactory.cowlFactory.cowl(arena.getWidth() / 2, Measure.units(40f),
                        new Vector3(isOnLeft ? Measure.units(10f) : arena.getWidth() - Measure.units(10f),  arena.getHeight() / 2, 0),
                        Measure.units(10f),
                        isOnLeft ? 180 : 0, startsLeft));


                return arena;
            }
        };
    }



    public ArenaCreate room8Lazerus() {
        return new ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {
                Arena arena = arenaShellFactory.createOmniArenaSquareCenter(defaultCoords, Arena.ArenaType.TRAP);
                arena.addEntity(arenaEnemyPlacementFactory.spawnLaserus(arena.getWidth() / 2, Measure.units(40f)));
                return arena;
            }
        };
    }


    public ArenaCreate room9TwoLazerus() {
        return new ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {
                Arena arena = arenaShellFactory.createOmniArenaHiddenGrapple(defaultCoords, random.nextInt(4), Arena.ArenaType.TRAP);

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

                Arena arena = arenaShellFactory.createOmniArenaHiddenGrapple(defaultCoords, random.nextInt(4), Arena.ArenaType.TRAP);

                LaserOrbitalTask.LaserBuilder lb = new LaserOrbitalTask.LaserBuilder(assetManager)
                        .orbitalAndIntervalSize(Measure.units(3f))
                        .speedInDegrees(1f)
                        .numberOfOrbitals(12)
                        .angles(0,180);

                arena.addEntity(beamTurretFactory.inCombatLaserChain(Measure.units(20f), Measure.units(25f),2,
                        lb.build()));

                arena.addEntity(beamTurretFactory.inCombatLaserChain(arena.getWidth() - Measure.units(30f), Measure.units(25f),2,
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

                Arena arena =  new ArenaBuilder(assetManager, arenaSkin, Arena.ArenaType.NORMAL)
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
                        .buildArena();


                arena.addEntity(decorFactory.wallBag(Measure.units(5f), Measure.units(35f), Measure.units(130f), Measure.units(5f)));

                arena.addEntity(decorFactory.platform(Measure.units(135f), Measure.units(35f), Measure.units(30)));

                arena.addEntity(decorFactory.wallBag(Measure.units(165f), Measure.units(35f), Measure.units(130f), Measure.units(5f)));

                //left
                if(random.nextBoolean()) arena.addEntity(chestFactory.chestBag(Measure.units(10f), Measure.units(25f)));
                arena.addEntity(decorFactory.wallBag(Measure.units(5), Measure.units(10f), Measure.units(20f), Measure.units(15f)));

                arena.addEntity(arenaEnemyPlacementFactory.pylonFactory.ghostPylonBag(Measure.units(80f), Measure.units(15f), 0));
                arena.addEntity(decorFactory.wallBag(Measure.units(75f), Measure.units(10f), Measure.units(20f), Measure.units(5f)));

                //right
                arena.addEntity(arenaEnemyPlacementFactory.pylonFactory.ghostPylonBag(Measure.units(215f), Measure.units(15f), 0));
                arena.addEntity(decorFactory.wallBag(Measure.units(210f), Measure.units(10f), Measure.units(20f), Measure.units(5f)));

                if(random.nextBoolean() ) arena.addEntity(chestFactory.chestBag(Measure.units(280), Measure.units(25f)));
                arena.addEntity(decorFactory.wallBag(Measure.units(275f), Measure.units(10f), Measure.units(20f), Measure.units(15f)));

                arena.addEntity(arenaEnemyPlacementFactory.pylonFactory.ghostPylonBag(Measure.units(145f), Measure.units(15f), 0));
                arena.addEntity(decorFactory.wallBag(Measure.units(140f), Measure.units(10f), Measure.units(20f), Measure.units(5f)));


                return arena;
            }
        };
    }





    public ArenaCreate room12Height3GrappleLaserChests(){
        return new ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {

                Arena arena =  new ArenaBuilder(assetManager, arenaSkin, Arena.ArenaType.NORMAL)
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
                        .buildArena();


                LaserOrbitalTask.LaserBuilder lb = new LaserOrbitalTask.LaserBuilder(assetManager)
                        .orbitalAndIntervalSize(Measure.units(20f))
                        .numberOfOrbitals(7)
                        .expiryTime(1)
                        .angles(90);

                arena.addEntity(beamTurretFactory.timedLaserChain(Measure.units(35f), Measure.units(25f), 6, 4f,
                        lb.build()));

                arena.addEntity(beamTurretFactory.timedLaserChain(Measure.units(35f), Measure.units(160), 6, 4f,
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

                Arena arena = arenaShellFactory.createOmniArenaHiddenGrapple(defaultCoords, random.nextInt(4), Arena.ArenaType.TRAP);

                com.bryjamin.wickedwizard.factories.weapons.enemy.LaserBeam laserBeam = new com.bryjamin.wickedwizard.factories.weapons.enemy.LaserBeam.LaserBeamBuilder(assetManager)
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

                arena.addWave(arenaEnemyPlacementFactory.spawnLaserBouncer(arena.getWidth() / 2, Measure.units(40f)));
                arena.addWave(arenaEnemyPlacementFactory.spawnLaserus(arena.getWidth() / 2, Measure.units(40f)));

                arena.shuffleWaves();
                return arena;
            }
        };
    }



    public ArenaCreate room14Width2KnightsAndGoats(){
        return new ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {

                Arena arena =  new ArenaBuilder(assetManager, arenaSkin, Arena.ArenaType.TRAP)
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
                        .buildArena();

                arena.addEntity(arenaEnemyPlacementFactory.spawnKnight(Measure.units(25f), Measure.units(25f)));
                arena.addEntity(arenaEnemyPlacementFactory.spawnGoatWizard(arena.getWidth() / 2, Measure.units(45f)));
                arena.addEntity(arenaEnemyPlacementFactory.spawnKnight(arena.getWidth() - Measure.units(25f), Measure.units(25f)));


                return arena;
            }
        };
    }




    public ArenaCreate room15LaserModonAndABouncer(){
        return new ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {

                Arena arena = arenaShellFactory.createOmniArenaHiddenGrapple(defaultCoords, random.nextInt(4), Arena.ArenaType.TRAP);

                arena.addEntity(arenaEnemyPlacementFactory.spawnHeavyModon(arena.getWidth() / 2, Measure.units(40f)));
                arena.addEntity(arenaEnemyPlacementFactory.spawnBouncer(
                        random.nextBoolean() ? Measure.units(20f) : arena.getWidth() - Measure.units(20f), Measure.units(45f)));

                return arena;
            }
        };
    }



    public ArenaCreate room16Width2LasersTreasureAndPentaSentry(){
        return new ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {

                boolean chestsAreLeft = random.nextBoolean();

                Arena arena =  new ArenaBuilder(assetManager, arenaSkin, Arena.ArenaType.NORMAL)
                        .addSection(new ArenaBuilder.Section(defaultCoords,
                                ArenaBuilder.wall.FULL,
                                ArenaBuilder.wall.NONE,
                                chestsAreLeft ? ArenaBuilder.wall.FULL : ArenaBuilder.wall.GRAPPLE,
                                chestsAreLeft ? ArenaBuilder.wall.FULL : ArenaBuilder.wall.DOOR))
                        .addSection(new ArenaBuilder.Section(new MapCoords(defaultCoords.getX() + 1, defaultCoords.getY()),
                                ArenaBuilder.wall.NONE,
                                ArenaBuilder.wall.FULL,
                                chestsAreLeft ? ArenaBuilder.wall.GRAPPLE : ArenaBuilder.wall.FULL,
                                chestsAreLeft ? ArenaBuilder.wall.DOOR : ArenaBuilder.wall.FULL))
                        .buildArena();

                LaserOrbitalTask.LaserBuilder lb = new LaserOrbitalTask.LaserBuilder(assetManager)
                        .chargeTime(0.5f)
                        .numberOfOrbitals(5)
                        .angles(270)
                        .expiryTime(1.5f)
                        .orbitalAndIntervalSize(Measure.units(20f));

                arena.addEntity(beamTurretFactory.timedLaserChain(Measure.units(65f), arena.getHeight() - Measure.units(15f), 6, 3f,
                        lb.build()));

                arena.addEntity(beamTurretFactory.timedLaserChain(Measure.units(105f), arena.getHeight() - Measure.units(15f), 6, 3f,
                        lb.build()));


                arena.addEntity(chestFactory.chestBag(chestsAreLeft ? Measure.units(30f) : Measure.units(160f), Measure.units(10f), chestFactory.trapODAC()));

                arena.addWave(arenaEnemyPlacementFactory.spawnFixedPentaSentry(chestsAreLeft ? Measure.units(35f) : Measure.units(165f), Measure.units(40f)));

                return arena;
            }
        };
    }




    public ArenaCreate room17BlobAndLaserus(){
        return new ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {

                Arena arena = arenaShellFactory.createOmniArenaSquareCenter(defaultCoords, Arena.ArenaType.TRAP);
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


                Arena arena =  new ArenaBuilder(assetManager, arenaSkin, Arena.ArenaType.TRAP)
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
                        .buildArena();

                arena.addEntity(arenaEnemyPlacementFactory.spawnLaserus(Measure.units(20f), Measure.units(40f)));
                arena.addEntity(arenaEnemyPlacementFactory.spawnLaserus(arena.getWidth() - Measure.units(20f), Measure.units(40f)));
                arena.addEntity(arenaEnemyPlacementFactory.spawnFixedPentaSentry(arena.getWidth() / 2, Measure.units(40f)));

                return arena;
            }
        };
    }


    public ArenaCreate room19TwoGhostPylons(){
        return new ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {

                Arena arena = arenaShellFactory.createOmniArenaHiddenGrapple(defaultCoords, random.nextInt(4), Arena.ArenaType.TRAP);

                arena.arenaType = Arena.ArenaType.TRAP;

                arena.addEntity(arenaEnemyPlacementFactory.pylonFactory.ghostPylonBag(Measure.units(5f), Measure.units(40f), -90));
                arena.addEntity(arenaEnemyPlacementFactory.pylonFactory.ghostPylonBag(arena.getWidth() - Measure.units(15f), Measure.units(40f), 90));

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


                Arena arena =  new ArenaBuilder(assetManager, arenaSkin, Arena.ArenaType.TRAP)
                        .addSection(new ArenaBuilder.Section(defaultCoords,
                                ArenaBuilder.wall.DOOR,
                                ArenaBuilder.wall.DOOR,
                                ArenaBuilder.wall.GRAPPLE,
                                ArenaBuilder.wall.FULL))
                        .buildArena();

                boolean bool = random.nextBoolean();

                arena.addEntity(arenaEnemyPlacementFactory.cowlFactory.cowl(arena.getWidth() / 2, Measure.units(40f),
                        new Vector3(arena.getWidth() / 2, Measure.units(45f), 0),
                        Measure.units(5f),
                        180, bool));

                arena.addEntity(decorFactory.wallBag(Measure.units(35f), Measure.units(10f), Measure.units(30f), Measure.units(10f)));


                return arena;
            }
        };
    }



    public ArenaCreate room21RightSnake(){
        return new ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {
                Arena arena = arenaShellFactory.createOmniArenaHiddenGrapple(defaultCoords, random.nextInt(4), Arena.ArenaType.TRAP);

                boolean bool = random.nextBoolean();

                arena.addEntity(arenaEnemyPlacementFactory.spawnRightSnake(arena.getWidth() / 2, arena.getHeight() / 2, bool ? com.bryjamin.wickedwizard.utils.enums.Direction.LEFT : com.bryjamin.wickedwizard.utils.enums.Direction.RIGHT, 10));
                return arena;
            }
        };
    }



    public ArenaCreate room22LeftSnake(){
        return new ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {
                Arena arena = arenaShellFactory.createOmniArenaHiddenGrapple(defaultCoords, random.nextInt(4), Arena.ArenaType.TRAP);

                boolean bool = random.nextBoolean();

                arena.addEntity(arenaEnemyPlacementFactory.spawnLeftSnake(arena.getWidth() / 2, arena.getHeight() / 2, bool ? com.bryjamin.wickedwizard.utils.enums.Direction.LEFT : com.bryjamin.wickedwizard.utils.enums.Direction.RIGHT, 10));
                return arena;
            }
        };
    }



    public ArenaCreate room23JumpingJack(){
        return new ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {
                Arena arena = arenaShellFactory.createOmniArenaHiddenGrapple(defaultCoords, random.nextInt(4), Arena.ArenaType.TRAP);

                arena.addEntity(arenaEnemyPlacementFactory.spawnJumpingJack(arena.getWidth() / 2, arena.getHeight() / 2, random.nextBoolean()));
                return arena;
            }
        };
    }


    public ArenaCreate room24SmallVerticalRoomSnake(){
        return new ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {

                Arena arena =  new ArenaBuilder(assetManager, arenaSkin, Arena.ArenaType.TRAP)
                        .addSection(new ArenaBuilder.Section(defaultCoords,
                                ArenaBuilder.wall.FULL,
                                ArenaBuilder.wall.FULL,
                                ArenaBuilder.wall.GRAPPLE,
                                ArenaBuilder.wall.DOOR))
                        .buildArena();

                arena.addEntity(decorFactory.wallBag(Measure.units(5f), Measure.units(10f), Measure.units(25f), Measure.units(45f)));
                arena.addEntity(decorFactory.wallBag(arena.getWidth() - Measure.units(30f), Measure.units(10f), Measure.units(25f), Measure.units(45f)));


                arena.addEntity(random.nextBoolean() ?
                        arenaEnemyPlacementFactory.spawnLeftSnake(arena.getWidth() / 2, Measure.units(35f), com.bryjamin.wickedwizard.utils.enums.Direction.UP, 7) :
                        arenaEnemyPlacementFactory.spawnRightSnake(arena.getWidth() / 2, Measure.units(35f), com.bryjamin.wickedwizard.utils.enums.Direction.UP, 7));
                return arena;
            }
        };
    }



    public ArenaCreate room25SmallRoomHorizontalSnake(){
        return new ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {
                Arena arena =  new ArenaBuilder(assetManager, arenaSkin, Arena.ArenaType.TRAP)
                        .addSection(new ArenaBuilder.Section(defaultCoords,
                                ArenaBuilder.wall.DOOR,
                                ArenaBuilder.wall.DOOR,
                                ArenaBuilder.wall.FULL,
                                ArenaBuilder.wall.FULL))
                        .buildArena();

                arena.addEntity(decorFactory.wallBag(Measure.units(5f), Measure.units(30f), Measure.units(90f), Measure.units(25f)));

                arena.addEntity(random.nextBoolean() ?
                        arenaEnemyPlacementFactory.spawnLeftSnake(arena.getWidth() / 2, Measure.units(22.5f), com.bryjamin.wickedwizard.utils.enums.Direction.UP, 7) :
                        arenaEnemyPlacementFactory.spawnRightSnake(arena.getWidth() / 2, Measure.units(22.5f), com.bryjamin.wickedwizard.utils.enums.Direction.UP, 7));
                return arena;
            }
        };
    }



    public ArenaCreate room26Width3MiddleArena(){
        return new ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {


                Arena arena =  new ArenaBuilder(assetManager, arenaSkin, Arena.ArenaType.NORMAL)
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
                        .buildArena();


                arena.addEntity(decorFactory.wallBag(Measure.units(5f), Measure.units(30f), Measure.units(95f), Measure.units(30f)));
                arena.addEntity(decorFactory.wallBag(arena.getWidth() - Measure.units(100f), Measure.units(30f), Measure.units(95f), Measure.units(30f)));

                arena.addEntity(decorFactory.appearInCombatWallPush(Measure.units(95f), Measure.units(10f), Measure.units(5f), Measure.units(30f), 0));
                arena.addEntity(decorFactory.appearInCombatWallPush(arena.getWidth() - Measure.units(100f), Measure.units(10f), Measure.units(5f), Measure.units(30f), 190));


                arena.addEntity(chestFactory.chestBag(Measure.units(132.5f), Measure.units(10f), chestFactory.trapODAC()));
                arena.addEntity(chestFactory.chestBag(Measure.units(145f), Measure.units(10f), chestFactory.trapODAC()));
                arena.addEntity(chestFactory.chestBag(Measure.units(157.5f), Measure.units(10f), chestFactory.trapODAC()));

                arena.addWave(arenaEnemyPlacementFactory.spawnJumpingJack(arena.getWidth() / 2, Measure.units(45), random.nextBoolean()));
                arena.addWave(arenaEnemyPlacementFactory.spawnMovingPentaSentry(arena.getWidth() / 2, Measure.units(45f)));
                arena.addWave(arenaEnemyPlacementFactory.spawnKnight(Measure.units(130f), Measure.units(45f)),
                        arenaEnemyPlacementFactory.spawnLaserus(Measure.units(170f), Measure.units(45f)));

                arena.shuffleWaves();

                return arena;
            }
        };
    }







    public ArenaCreate room27Height3Empty(){
        return new ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {

                Arena arena = new ArenaBuilder(assetManager, arenaSkin, Arena.ArenaType.NORMAL)
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
                        .buildArena();


                arena.addEntity(decorFactory.wallBag(Measure.units(40f), Measure.units(30f), Measure.units(20f), Measure.units(5f)));


                arena.addEntity(decorFactory.grapplePointBag(arena.getWidth() / 2, Measure.units(70f)));
                arena.addEntity(decorFactory.grapplePointBag(arena.getWidth() / 2, Measure.units(110f)));

                //Left
                arena.addEntity(decorFactory.wallBag(Measure.units(5f), Measure.units(55f), Measure.units(35f), Measure.units(35f)));
                arena.addEntity(decorFactory.wallBag(Measure.units(5f), Measure.units(40f), Measure.units(15f), Measure.units(25f)));

                //Right
                arena.addEntity(decorFactory.wallBag(arena.getWidth() - Measure.units(40f), Measure.units(55f), Measure.units(35f), Measure.units(35f)));
                arena.addEntity(decorFactory.wallBag(arena.getWidth() - Measure.units(20f), Measure.units(40f), Measure.units(15f), Measure.units(25f)));

                arena.addEntity(decorFactory.platform(Measure.units(25f), Measure.units(125f), Measure.units(70f)));
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

                Arena arena = new ArenaBuilder(assetManager, arenaSkin, Arena.ArenaType.NORMAL)
                        .addSection(new ArenaBuilder.Section(defaultCoords,
                                ArenaBuilder.wall.DOOR,
                                ArenaBuilder.wall.DOOR,
                                ArenaBuilder.wall.GRAPPLE,
                                ArenaBuilder.wall.DOOR))
                        .buildArena();

                boolean lockedChestsAreLeft = random.nextBoolean();


                arena.addEntity(decorFactory.platform(lockedChestsAreLeft ? Measure.units(5f) : arena.getWidth() - Measure.units(35f), Measure.units(31f), Measure.units(30f)));
                arena.addEntity(decorFactory.wallBag(lockedChestsAreLeft ? Measure.units(35f) : arena.getWidth() - Measure.units(40f), Measure.units(35f), Measure.units(5f), Measure.units(20f)));


                arena.addEntity(chestFactory.chestBag(lockedChestsAreLeft ? Measure.units(7.5f) : arena.getWidth() - Measure.units(17.5f), Measure.units(40f)));
                arena.addEntity(chestFactory.chestBag(lockedChestsAreLeft ? Measure.units(22.5f) : arena.getWidth() - Measure.units(32.5f), Measure.units(40f)));
                arena.addEntity(chestFactory.chestBag(lockedChestsAreLeft ? Measure.units(70f) : arena.getWidth() - Measure.units(80f), Measure.units(10f), chestFactory.trapODAC()));

                arena.addEntity(arenaEnemyPlacementFactory.hoarderFactory.hoarder(lockedChestsAreLeft ? Measure.units(75f)  : Measure.units(25f), Measure.units(45f)));


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

                Arena arena = arenaShellFactory.createOmniArenaHiddenGrapple(defaultCoords, Arena.ArenaType.TRAP);

                arena.arenaType = Arena.ArenaType.TRAP;

                boolean bool = random.nextBoolean();

                arena.addWave(arenaEnemyPlacementFactory.spawnLaserus(arena.getWidth() / 2, Measure.units(40f)),
                        bool ? arenaEnemyPlacementFactory.spawnRightSnake(Measure.units(20f), Measure.units(35f), com.bryjamin.wickedwizard.utils.enums.Direction.LEFT,4)
                                : arenaEnemyPlacementFactory.spawnLeftSnake(arena.getWidth() - Measure.units(20f), Measure.units(35f), com.bryjamin.wickedwizard.utils.enums.Direction.RIGHT, 4));

                arena.addWave(arenaEnemyPlacementFactory.spawnLaserus(arena.getWidth() / 2, Measure.units(40f)),
                        !bool ? arenaEnemyPlacementFactory.spawnRightSnake(Measure.units(20f), Measure.units(35f), com.bryjamin.wickedwizard.utils.enums.Direction.LEFT,4)
                                : arenaEnemyPlacementFactory.spawnLeftSnake(arena.getWidth() - Measure.units(20f), Measure.units(35f), com.bryjamin.wickedwizard.utils.enums.Direction.RIGHT, 4));

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




    public ArenaCreate room30Height3ThroughRoomWithHorizontalLasers() {
        return new ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {


                boolean door = random.nextBoolean();

                Arena arena = new ArenaBuilder(assetManager, arenaSkin, Arena.ArenaType.NORMAL)
                        .addSection(new ArenaBuilder.Section(defaultCoords,
                                door ? ArenaBuilder.wall.DOOR : ArenaBuilder.wall.MANDATORYDOOR,
                                door ? ArenaBuilder.wall.MANDATORYDOOR : ArenaBuilder.wall.DOOR,
                                ArenaBuilder.wall.NONE,
                                ArenaBuilder.wall.DOOR))
                        .addSection(new ArenaBuilder.Section(new MapCoords(defaultCoords.getX(), defaultCoords.getY() + 1),
                                ArenaBuilder.wall.FULL,
                                ArenaBuilder.wall.FULL,
                                ArenaBuilder.wall.NONE,
                                ArenaBuilder.wall.NONE))
                        .addSection(new ArenaBuilder.Section(new MapCoords(defaultCoords.getX(), defaultCoords.getY() + 2),
                                door ? ArenaBuilder.wall.MANDATORYDOOR : ArenaBuilder.wall.DOOR,
                                door ? ArenaBuilder.wall.DOOR : ArenaBuilder.wall.MANDATORYDOOR,
                                ArenaBuilder.wall.GRAPPLE,
                                ArenaBuilder.wall.NONE))
                        .buildArena();


                LaserOrbitalTask.LaserBuilder lb = new LaserOrbitalTask.LaserBuilder(assetManager)
                        .orbitalAndIntervalSize(Measure.units(5f))
                        .numberOfOrbitals(6)
                        .angles(0);

                LaserOrbitalTask.LaserBuilder empty = new LaserOrbitalTask.LaserBuilder(assetManager)
                        .orbitalAndIntervalSize(Measure.units(5f));

                for(int i = 0; i < 5; i++) {

                    float y = Measure.units(40f + (i * 40f));

                    arena.addEntity(beamTurretFactory.laserChain(Measure.units(60f), y, 2,
                            lb.build()));
                    arena.addEntity(beamTurretFactory.laserChain(Measure.units(85f), y, 2,
                            empty.build()));

                    arena.addEntity(beamTurretFactory.laserChain(Measure.units(5f), y, 2,
                            lb.build()));
                    arena.addEntity(beamTurretFactory.laserChain(Measure.units(30f), y, 2,
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
