package com.byrjamin.wickedwizard.factories.arenas;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.utils.Array;
import com.byrjamin.wickedwizard.factories.AbstractFactory;
import com.byrjamin.wickedwizard.factories.EntityFactory;
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
    EntityFactory entityFactory;
    RoomDecorationFactory roomDecorationFactory;
    TurretFactory turretFactory;

    public Level1Rooms(AssetManager assetManager) {
        super(assetManager);
        this.arenaShellFactory = new ArenaShellFactory(assetManager);
        this.chestFactory = new ChestFactory(assetManager);
        this.roomDecorationFactory = new RoomDecorationFactory(assetManager);
        this.entityFactory = new EntityFactory(assetManager);
        this.turretFactory = new TurretFactory(assetManager);
    }

    public ArenaGen[] agArray = {room1(), room2(), room3()};

    public Array<ArenaGen> getLevel1RoomArray(){
        Array<ArenaGen> ag = new Array<ArenaGen>();
        ag.add(room1());
        ag.add(room2());
        ag.add(room3());
        ag.add(room4());
        ag.add(room5());
        ag.add(room6());
        ag.add(room7LetterI());
        ag.add(room8());
        ag.add(deadEndW2());
        ag.add(room10());
        ag.add(room11());
        ag.add(room12());
        ag.add(room13());
        return ag;
    }

    public ArenaGen room1(){
        return new ArenaGen() {
            @Override
            public Arena createArena() {
                Arena a = arenaShellFactory.createOmniArena();
                roomDecorationFactory.blobRoom(a);
                a.roomType = Arena.RoomType.TRAP;
                return a;
            }
        };
    }

    public ArenaGen room2(){
        return new ArenaGen() {
            @Override
            public Arena createArena() {
                Arena a = arenaShellFactory.createOmniArena();
                roomDecorationFactory.movingTurretRoomRight(a);
                a.roomType = Arena.RoomType.TRAP;
                return a;
            }
        };
    }

    public ArenaGen room3(){
        return new ArenaGen() {
            @Override
            public Arena createArena() {
                Arena a = arenaShellFactory.createOmniArena();
                roomDecorationFactory.movingDoubleTurretRoom(a);
                a.roomType = Arena.RoomType.TRAP;
                return a;
            }
        };
    }

    public ArenaGen room4(){
        return new ArenaGen() {
            @Override
            public Arena createArena() {
                Arena a = arenaShellFactory.createOmniArena();
                roomDecorationFactory.kugelDusche(a);
                a.roomType = Arena.RoomType.TRAP;
                return a;
            }
        };
    }

    public ArenaGen room5(){
        return new ArenaGen() {
            @Override
            public Arena createArena() {
                Arena a = arenaShellFactory.createOmniArena();
                roomDecorationFactory.silverHead(a);
                a.roomType = Arena.RoomType.TRAP;
                return a;
            }
        };
    }

    public ArenaGen room6(){
        return new ArenaGen() {
            @Override
            public Arena createArena() {
                Arena a = arenaShellFactory.createWidth2Arena();
                roomDecorationFactory.movingDoubleTurretRoom(a);
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
                a.addEntity(entityFactory.grapplePointBag(a.getWidth() / 2, Measure.units(40f)));
                a.addEntity(entityFactory.grapplePointBag(a.getWidth() / 2, Measure.units(70f)));
                a.addEntity(entityFactory.grapplePointBag(a.getWidth() / 2, Measure.units(100f)));
                a.addEntity(entityFactory.grapplePointBag(a.getWidth() / 2, Measure.units(130f)));
                a.addEntity(entityFactory.grapplePointBag(a.getWidth() / 2, Measure.units(160f)));
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
                //RoomDecorationFactory.blobRoom(a);
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

                a.addEntity(entityFactory.wallBag(blockerPosX, Measure.units(45f), Measure.units(10f), Measure.units(10f)));
                a.addEntity(entityFactory.wallBag(blockerPosX, Measure.units(10f), Measure.units(10f), Measure.units(10f)));

                a.addEntity(turretFactory.fixedTurret(posX, Measure.units(50f),  angle, 3.0f, 0f));
                a.addEntity(turretFactory.fixedTurret(posX, Measure.units(41f),  angle, 3.0f, 1.5f));
                a.addEntity(turretFactory.fixedTurret(posX, Measure.units(32f),  angle, 3.0f, 1.5f));
                a.addEntity(turretFactory.fixedTurret(posX, Measure.units(23f),  angle, 3.0f, 1.5f));
                a.addEntity(turretFactory.fixedTurret(posX, Measure.units(14f),  angle, 3.0f, 0f));


                return a;
            }
        };


    }


    public ArenaGen room10(){

        return new ArenaGen() {
            @Override
            public Arena createArena() {
                Arena a = arenaShellFactory.createHeight2Arena(new MapCoords(0,0));

                a.addEntity(entityFactory.wallBag(a.getWidth() - Measure.units(60f), Measure.units(35f),
                        Measure.units(60f), Measure.units(5)));

                a.addEntity(entityFactory.wallBag(0, Measure.units(65f),
                        Measure.units(60f), Measure.units(5)));
                return a;
            }
        };
    }


    public ArenaGen room11(){

        return new ArenaGen() {
            @Override
            public Arena createArena() {
                Arena a = arenaShellFactory.createOmniArena(new MapCoords(0,0));
                roomDecorationFactory.spawnBouncer(a, a.getWidth() / 4,(a.getHeight() - a.getHeight() / 4) + Measure.units(2.5f));
                roomDecorationFactory.spawnBouncer(a, a.getWidth() - a.getWidth() / 4,(a.getHeight() - a.getHeight() / 4) + Measure.units(2.5f));
                a.roomType = Arena.RoomType.TRAP;

                return a;
            }
        };
    }



    public ArenaGen room12(){

        return new ArenaGen() {
            @Override
            public Arena createArena() {
                Arena a = arenaShellFactory.createOmniArena(new MapCoords(0,0));
                roomDecorationFactory.spawnBouncer(a, a.getWidth() / 4,(a.getHeight() - a.getHeight() / 4) + Measure.units(2.5f));
                roomDecorationFactory.spawnBouncer(a, a.getWidth() - a.getWidth() / 4,(a.getHeight() - a.getHeight() / 4) + Measure.units(2.5f));

                a.roomType = Arena.RoomType.TRAP;

                a.addEntity(entityFactory.wallBag(a.getWidth() - Measure.units(25f), Measure.units(35f),
                        Measure.units(5f), Measure.units(5)));

                a.addEntity(entityFactory.wallBag(Measure.units(20f), Measure.units(35f),
                        Measure.units(5f), Measure.units(5)));


                return a;
            }
        };
    }


    public ArenaGen room13(){

        return new ArenaGen() {
            @Override
            public Arena createArena() {
                Arena a = arenaShellFactory.createOmniArena(new MapCoords(0,0));
                roomDecorationFactory.spawnLargeBouncer(a, a.getWidth() / 2,(a.getHeight() - a.getHeight() / 2));
                a.roomType = Arena.RoomType.TRAP;
                return a;
            }
        };
    }

}
