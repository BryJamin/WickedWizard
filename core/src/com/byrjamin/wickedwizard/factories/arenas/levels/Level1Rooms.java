package com.byrjamin.wickedwizard.factories.arenas.levels;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.utils.Array;
import com.byrjamin.wickedwizard.factories.AbstractFactory;
import com.byrjamin.wickedwizard.factories.arenas.Arena;
import com.byrjamin.wickedwizard.factories.arenas.ArenaBuilder;
import com.byrjamin.wickedwizard.factories.arenas.ArenaGen;
import com.byrjamin.wickedwizard.factories.arenas.decor.ArenaEnemyPlacementFactory;
import com.byrjamin.wickedwizard.factories.arenas.decor.ArenaShellFactory;
import com.byrjamin.wickedwizard.factories.arenas.decor.DecorFactory;
import com.byrjamin.wickedwizard.factories.arenas.skins.ArenaSkin;
import com.byrjamin.wickedwizard.factories.chests.ChestFactory;
import com.byrjamin.wickedwizard.factories.enemy.TurretFactory;
import com.byrjamin.wickedwizard.utils.MapCoords;
import com.byrjamin.wickedwizard.utils.Measure;

import java.util.Random;

/**
 * Created by Home on 13/04/2017.
 */

public class Level1Rooms extends AbstractFactory {

    ArenaShellFactory arenaShellFactory;
    ChestFactory chestFactory;
    DecorFactory decorFactory;
    ArenaEnemyPlacementFactory arenaEnemyPlacementFactory;
    TurretFactory turretFactory;

    ArenaSkin arenaSkin;

    public Level1Rooms(AssetManager assetManager, ArenaSkin arenaSkin) {
        super(assetManager);
        this.arenaShellFactory = new com.byrjamin.wickedwizard.factories.arenas.decor.ArenaShellFactory(assetManager, arenaSkin);
        this.chestFactory = new ChestFactory(assetManager);
        this.arenaEnemyPlacementFactory = new com.byrjamin.wickedwizard.factories.arenas.decor.ArenaEnemyPlacementFactory(assetManager, arenaSkin);
        this.decorFactory = new com.byrjamin.wickedwizard.factories.arenas.decor.DecorFactory(assetManager, arenaSkin);
        this.turretFactory = new TurretFactory(assetManager);
        this.arenaSkin = arenaSkin;
    }

    public Array<ArenaGen> getLevel1RoomArray(){
        Array<ArenaGen> ag = new Array<ArenaGen>();
        ag.add(room1blobLeft());
        ag.add(room1blobRight());
        ag.add(room1blobRightAndLeft());
        ag.add(room1blobRightNoBlock());
        ag.add(room2());
        ag.add(room3());
        ag.add(room5());
        ag.add(room6());
        //ag.add(room7LetterI());
        ag.add(room8());
        ag.add(deadEndW2());
        ag.add(room10Height2());
        ag.add(room11());
        ag.add(room12());
        ag.add(room13LargeBouncer());
        ag.add(room14());
        ag.add(room15());
        ag.add(room16());
        return ag;
    }

    public ArenaGen room1blobLeft(){
        return new ArenaGen() {
            @Override
            public Arena createArena() {
                Arena a = arenaShellFactory.createOmniArenaSquareCenter();
                a.addEntity(arenaEnemyPlacementFactory.spawnBlob(a.getWidth() / 4, a.getHeight() / 2));
                a.roomType = Arena.RoomType.TRAP;
                return a;
            }
        };
    }

    public ArenaGen room1blobRight(){
        return new ArenaGen() {
            @Override
            public Arena createArena() {
                Arena a = arenaShellFactory.createOmniArenaSquareCenter();
                a.addEntity(arenaEnemyPlacementFactory.spawnBlob(a.getWidth() / 4 * 3, a.getHeight() / 2));
                a.roomType = Arena.RoomType.TRAP;
                return a;
            }
        };
    }

    public ArenaGen room1blobRightNoBlock(){
        return new ArenaGen() {
            @Override
            public Arena createArena() {
                Arena a = arenaShellFactory.createOmniArenaHiddenGrapple(new MapCoords());
                a.addEntity(arenaEnemyPlacementFactory.spawnBlob(a.getWidth() / 4 * 3, a.getHeight() / 2));
                a.roomType = Arena.RoomType.TRAP;
                return a;
            }
        };
    }

    public ArenaGen room1blobRightAndLeft(){
        return new ArenaGen() {
            @Override
            public Arena createArena() {
                Arena a = arenaShellFactory.createOmniArenaSquareCenter();

                a.addWave(arenaEnemyPlacementFactory.spawnBlob(a.getWidth() / 4 * 3, a.getHeight() / 2));
                a.addWave(arenaEnemyPlacementFactory.spawnBlob(a.getWidth() / 4, a.getHeight() / 2));

                a.roomType = Arena.RoomType.TRAP;
                return a;
            }
        };
    }

    public ArenaGen room2(){
        return new ArenaGen() {
            @Override
            public Arena createArena() {
                Arena a = arenaShellFactory.createOmniArenaSquareCenter();
                a.addEntity(arenaEnemyPlacementFactory.spawnMovingSentry(a.getWidth() - Measure.units(20), a.getHeight() - Measure.units(15)));
                a.roomType = Arena.RoomType.TRAP;
                return a;
            }
        };
    }

    public ArenaGen room3(){
        return new ArenaGen() {
            @Override
            public Arena createArena() {
                Arena a = arenaShellFactory.createOmniArenaSquareCenter();

                a.addEntity(arenaEnemyPlacementFactory.spawnMovingSentry(a.getWidth() - Measure.units(20), a.getHeight() - Measure.units(15)));
                a.addEntity(arenaEnemyPlacementFactory.spawnMovingSentry(Measure.units(20), a.getHeight() - Measure.units(15)));

                a.roomType = Arena.RoomType.TRAP;
                return a;
            }
        };
    }


    public ArenaGen room5(){
        return new ArenaGen() {
            @Override
            public Arena createArena() {
                Arena a = arenaShellFactory.createOmniArenaSquareCenter();
                arenaEnemyPlacementFactory.spawnSilverHead(a, a.getWidth() / 2, Measure.units(40f));
                a.roomType = Arena.RoomType.TRAP;
                return a;
            }
        };
    }

    public ArenaGen room6(){
        return new ArenaGen() {
            @Override
            public Arena createArena() {
                Arena a = arenaShellFactory.createWidth2ArenaWithVerticalDoors();

                a.addEntity(arenaEnemyPlacementFactory.spawnMovingSentry(a.getWidth() - Measure.units(20), a.getHeight() - Measure.units(15)));
                a.addEntity(arenaEnemyPlacementFactory.spawnMovingSentry(Measure.units(20), a.getHeight() - Measure.units(15)));
                a.roomType = Arena.RoomType.TRAP;
                return a;
            }
        };
    }

    public ArenaGen room7LetterI(){
        return new ArenaGen() {
            @Override
            public Arena createArena() {
                Arena a = arenaShellFactory.createLetterIArena(new MapCoords(0,0));
               // RoomDecorationFactory.movingDoubleTurretRoom(a);
                a.addEntity(decorFactory.grapplePointBag(a.getWidth() / 2, Measure.units(40f)));
                a.addEntity(decorFactory.grapplePointBag(a.getWidth() / 2, Measure.units(70f)));
                a.addEntity(decorFactory.grapplePointBag(a.getWidth() / 2, Measure.units(100f)));
                a.addEntity(decorFactory.grapplePointBag(a.getWidth() / 2, Measure.units(130f)));
                a.addEntity(decorFactory.grapplePointBag(a.getWidth() / 2, Measure.units(160f)));
                return a;
            }
        };
    }


    public ArenaGen room8(){
        return new ArenaGen() {
            @Override
            public Arena createArena() {

                Random random = new Random();
                boolean mirror = random.nextBoolean();

                Arena a = arenaShellFactory.createDeadEndArena(new MapCoords(0,0), mirror);

                float chestPosX = mirror ? a.getWidth() / 4 : a.getWidth() - a.getWidth() / 4;

                a.addEntity(chestFactory.lockedChestBag(chestPosX, a.getHeight() / 4));
                //RoomDecorationFactory.spawnBlob(a);
                return a;
            }
        };
    }


    public ArenaGen deadEndW2(){

        return new ArenaGen() {
            @Override
            public Arena createArena() {

                Random random = new Random();
                boolean mirror = random.nextBoolean();

                Arena a = arenaShellFactory.createWidth2DeadEndArena(new MapCoords(0, 0), mirror);

                float posX = mirror ? Measure.units(10) : a.getWidth() - Measure.units(10f);
                float chestPosX = mirror ? Measure.units(30f) : a.getWidth() - Measure.units(30f);
                float blockerPosX = mirror ? a.getWidth() - Measure.units(80f) : Measure.units(80f);

                float angle =  mirror ? 0 : 180;

                a.addEntity(chestFactory.chestBag(chestPosX, Measure.units(15f)));

                a.addEntity(decorFactory.wallBag(blockerPosX, Measure.units(45f), Measure.units(10f), Measure.units(10f), arenaSkin));
                a.addEntity(decorFactory.wallBag(blockerPosX, Measure.units(10f), Measure.units(10f), Measure.units(10f), arenaSkin));

                a.addEntity(turretFactory.fixedTurret(posX, Measure.units(50f),  angle, 3.0f, 0f));
                a.addEntity(turretFactory.fixedTurret(posX, Measure.units(41f),  angle, 3.0f, 1.5f));
                a.addEntity(turretFactory.fixedTurret(posX, Measure.units(32f),  angle, 3.0f, 1.5f));
                a.addEntity(turretFactory.fixedTurret(posX, Measure.units(23f),  angle, 3.0f, 1.5f));
                a.addEntity(turretFactory.fixedTurret(posX, Measure.units(14f),  angle, 3.0f, 0f));


                return a;
            }
        };


    }


    public ArenaGen room10Height2(){

        return new ArenaGen() {
            @Override
            public Arena createArena() {
                Arena a = arenaShellFactory.createHeight2Arena(new MapCoords(0,0));

                a.addEntity(decorFactory.wallBag(a.getWidth() - Measure.units(60f), Measure.units(35f),
                        Measure.units(60f), Measure.units(5), arenaSkin));

                a.addEntity(decorFactory.wallBag(0, Measure.units(65f),
                        Measure.units(60f), Measure.units(5), arenaSkin));

                a.addEntity(decorFactory.platform(Measure.units(5), Measure.units(35f), Measure.units(35f)));

                a.addEntity(decorFactory.platform(Measure.units(60f), Measure.units(65f), Measure.units(35f)));



                return a;
            }
        };
    }


    public ArenaGen room11(){

        return new ArenaGen() {
            @Override
            public Arena createArena() {
                Arena a = arenaShellFactory.createOmniArenaSquareCenter(new MapCoords(0,0));
                a.addEntity(arenaEnemyPlacementFactory.spawnBouncer(a.getWidth() / 4,(a.getHeight() - a.getHeight() / 4) + Measure.units(2.5f)));
                a.addEntity(arenaEnemyPlacementFactory.spawnBouncer(a.getWidth() - a.getWidth() / 4,(a.getHeight() - a.getHeight() / 4) + Measure.units(2.5f)));
                a.roomType = Arena.RoomType.TRAP;

                return a;
            }
        };
    }



    public ArenaGen room12(){

        return new ArenaGen() {
            @Override
            public Arena createArena() {
                Arena a = arenaShellFactory.createOmniArenaSquareCenter(new MapCoords(0,0));


                a.addEntity(arenaEnemyPlacementFactory.spawnBouncer(a.getWidth() / 4,(a.getHeight() - a.getHeight() / 4) + Measure.units(2.5f)));
                a.addEntity(arenaEnemyPlacementFactory.spawnBouncer(a.getWidth() / 4,(a.getHeight() - a.getHeight() / 4) + Measure.units(2.5f)));

                a.addEntity(arenaEnemyPlacementFactory.spawnBouncer(a.getWidth() - a.getWidth() / 4,(a.getHeight() - a.getHeight() / 4) + Measure.units(2.5f)));

                a.roomType = Arena.RoomType.TRAP;

                a.addEntity(decorFactory.wallBag(a.getWidth() - Measure.units(25f), Measure.units(35f),
                        Measure.units(5f), Measure.units(5), arenaSkin));

                a.addEntity(decorFactory.wallBag(Measure.units(20f), Measure.units(35f),
                        Measure.units(5f), Measure.units(5), arenaSkin));


                return a;
            }
        };
    }


    public ArenaGen room13LargeBouncer(){

        return new ArenaGen() {
            @Override
            public Arena createArena() {
                Arena a = arenaShellFactory.createOmniArenaSquareCenter(new MapCoords(0,0));
                a.addEntity(arenaEnemyPlacementFactory.spawnLargeBouncer(a.getWidth() / 2,(a.getHeight() - Measure.units(15f))));
                a.roomType = Arena.RoomType.TRAP;
                return a;
            }
        };
    }


    public ArenaGen room14() {

        return new ArenaGen() {
            @Override
            public Arena createArena() {
                MapCoords m = new MapCoords();
                Arena arena = new Arena(arenaSkin, m);

                arena.setWidth(com.byrjamin.wickedwizard.factories.arenas.decor.ArenaShellFactory.SECTION_WIDTH);
                arena.setHeight(com.byrjamin.wickedwizard.factories.arenas.decor.ArenaShellFactory.SECTION_HEIGHT);

                arena = new ArenaBuilder(assetManager, arenaSkin)
                        .addSection(new ArenaBuilder.Section(m,
                                ArenaBuilder.wall.DOOR,
                                ArenaBuilder.wall.DOOR,
                                ArenaBuilder.wall.FULL,
                                ArenaBuilder.wall.FULL))
                        .buildArena(arena);

                arena.addEntity(decorFactory.wallBag(Measure.units(40f), Measure.units(10f), Measure.units(20f), Measure.units(25f), arenaSkin));
                arena.addEntity(decorFactory.platform(Measure.units(5), Measure.units(30f), Measure.units(35f)));
                arena.addEntity(decorFactory.platform(Measure.units(60f), Measure.units(30f), Measure.units(35f)));
                arenaEnemyPlacementFactory.spawnSilverHead(arena, arena.getWidth() / 2, Measure.units(45f));


                return arena;
            }
        };
    }


    public ArenaGen room15() {

        return new ArenaGen() {
            @Override
            public Arena createArena() {
                MapCoords m = new MapCoords();
                Arena arena = new Arena(arenaSkin, m);


                arena.setWidth(com.byrjamin.wickedwizard.factories.arenas.decor.ArenaShellFactory.SECTION_WIDTH);
                arena.setHeight(com.byrjamin.wickedwizard.factories.arenas.decor.ArenaShellFactory.SECTION_HEIGHT);
                arena.roomType = Arena.RoomType.TRAP;

                arena =  new ArenaBuilder(assetManager, arenaSkin)
                        .addSection(new ArenaBuilder.Section(m,
                                ArenaBuilder.wall.DOOR,
                                ArenaBuilder.wall.DOOR,
                                ArenaBuilder.wall.FULL,
                                ArenaBuilder.wall.FULL))
                        .buildArena(arena);

                arena.addEntity(decorFactory.wallBag(Measure.units(20f), Measure.units(10f), Measure.units(5f), Measure.units(25f), arenaSkin));
                arena.addEntity(decorFactory.platform(Measure.units(5), Measure.units(30f), Measure.units(15f)));

                arena.addEntity(decorFactory.wallBag(arena.getWidth() - Measure.units(25f), Measure.units(10f), Measure.units(5f), Measure.units(25f), arenaSkin));
                arena.addEntity(decorFactory.platform(arena.getWidth() - Measure.units(20f), Measure.units(30f), Measure.units(15f)));

                arena.addEntity(arenaEnemyPlacementFactory.bouncerFactory.smallBouncer(Measure.units(30f), Measure.units(20f)));
                arena.addEntity(arenaEnemyPlacementFactory.bouncerFactory.smallBouncer(Measure.units(40f), Measure.units(20f)));
                arena.addEntity(arenaEnemyPlacementFactory.bouncerFactory.smallBouncer(Measure.units(50f), Measure.units(20f)));
                arena.addEntity(arenaEnemyPlacementFactory.bouncerFactory.smallBouncer(Measure.units(60f), Measure.units(20f)));
                arena.addEntity(arenaEnemyPlacementFactory.bouncerFactory.smallBouncer(Measure.units(70f), Measure.units(20f)));



                return arena;
            }
        };
    }


    public ArenaGen room16() {

        return new ArenaGen() {
            @Override
            public Arena createArena() {
                MapCoords m = new MapCoords();
                Arena arena = new Arena(arenaSkin, m);


                arena.setWidth(com.byrjamin.wickedwizard.factories.arenas.decor.ArenaShellFactory.SECTION_WIDTH);
                arena.setHeight(com.byrjamin.wickedwizard.factories.arenas.decor.ArenaShellFactory.SECTION_HEIGHT);

                arena =  new ArenaBuilder(assetManager, arenaSkin)
                        .addSection(new ArenaBuilder.Section(m,
                                ArenaBuilder.wall.DOOR,
                                ArenaBuilder.wall.DOOR,
                                ArenaBuilder.wall.FULL,
                                ArenaBuilder.wall.FULL))
                        .buildArena(arena);

                arena.addEntity(decorFactory.wallBag(0, Measure.units(30f), Measure.units(45f), arena.getHeight(), arenaSkin));
                arena.addEntity(decorFactory.wallBag(arena.getWidth() - Measure.units(45f), Measure.units(30f),
                        Measure.units(45f), Measure.units(15f), arenaSkin));

                arena.addEntity(decorFactory.fixedWallTurret(Measure.units(25), Measure.units(25f),  -90, 3.0f, 1.5f));
                arena.addEntity(decorFactory.fixedWallTurret(Measure.units(30), Measure.units(25f),  -90, 3.0f, 1.5f));
                arena.addEntity(decorFactory.fixedWallTurret(Measure.units(35), Measure.units(25f),  -90, 3.0f, 1.5f));
                arena.addEntity(decorFactory.fixedWallTurret(Measure.units(40), Measure.units(25f),  -90, 3.0f, 1.5f));

                arena.addEntity(decorFactory.fixedWallTurret(Measure.units(55), Measure.units(25f),  -90, 3.0f, 1.5f));
                arena.addEntity(decorFactory.fixedWallTurret(Measure.units(60), Measure.units(25f),  -90, 3.0f, 1.5f));
                arena.addEntity(decorFactory.fixedWallTurret(Measure.units(65), Measure.units(25f),  -90, 3.0f, 1.5f));
                arena.addEntity(decorFactory.fixedWallTurret(Measure.units(70), Measure.units(25f),  -90, 3.0f, 1.5f));


                arena.addEntity(chestFactory.chestBag(arena.getWidth() - Measure.units(30f),
                        Measure.units(50f)));
/*                arena.addEntity(turretFactory.fixedTurret(posX, Measure.units(41f),  angle, 3.0f, 1.5f));
                arena.addEntity(turretFactory.fixedTurret(posX, Measure.units(32f),  angle, 3.0f, 1.5f));
                arena.addEntity(turretFactory.fixedTurret(posX, Measure.units(23f),  angle, 3.0f, 1.5f));
                arena.addEntity(turretFactory.fixedTurret(posX, Measure.units(14f),  angle, 3.0f, 0f));*/

                return arena;
            }
        };
    }

}
