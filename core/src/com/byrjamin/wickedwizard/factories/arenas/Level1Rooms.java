package com.byrjamin.wickedwizard.factories.arenas;

import com.badlogic.gdx.utils.Array;
import com.byrjamin.wickedwizard.factories.EntityFactory;
import com.byrjamin.wickedwizard.factories.chests.ChestFactory;
import com.byrjamin.wickedwizard.factories.enemy.TurretFactory;
import com.byrjamin.wickedwizard.utils.MapCoords;
import com.byrjamin.wickedwizard.utils.Measure;

import java.util.Random;

/**
 * Created by Home on 13/04/2017.
 */

public class Level1Rooms {

    public static ArenaGen[] agArray = {room1(), room2(), room3()};

    public static Array<ArenaGen> getLevel1RoomArray(){
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
        return ag;
    }

    public static ArenaGen room1(){
        return new ArenaGen() {
            @Override
            public Arena createArena() {
                Arena a = ArenaShellFactory.createOmniArena();
                RoomDecorationFactory.blobRoom(a);
                a.roomType = Arena.RoomType.TRAP;
                return a;
            }
        };
    }

    public static ArenaGen room2(){
        return new ArenaGen() {
            @Override
            public Arena createArena() {
                Arena a = ArenaShellFactory.createOmniArena();
                RoomDecorationFactory.movingTurretRoomRight(a);
                a.roomType = Arena.RoomType.TRAP;
                return a;
            }
        };
    }

    public static ArenaGen room3(){
        return new ArenaGen() {
            @Override
            public Arena createArena() {
                Arena a = ArenaShellFactory.createOmniArena();
                RoomDecorationFactory.movingDoubleTurretRoom(a);
                a.roomType = Arena.RoomType.TRAP;
                return a;
            }
        };
    }

    public static ArenaGen room4(){
        return new ArenaGen() {
            @Override
            public Arena createArena() {
                Arena a = ArenaShellFactory.createOmniArena();
                RoomDecorationFactory.kugelDusche(a);
                a.roomType = Arena.RoomType.TRAP;
                return a;
            }
        };
    }

    public static ArenaGen room5(){
        return new ArenaGen() {
            @Override
            public Arena createArena() {
                Arena a = ArenaShellFactory.createOmniArena();
                RoomDecorationFactory.silverHead(a);
                a.roomType = Arena.RoomType.TRAP;
                return a;
            }
        };
    }

    public static ArenaGen room6(){
        return new ArenaGen() {
            @Override
            public Arena createArena() {
                Arena a = ArenaShellFactory.createWidth2Arena();
                RoomDecorationFactory.movingDoubleTurretRoom(a);
                a.roomType = Arena.RoomType.TRAP;
                return a;
            }
        };
    }

    public static ArenaGen room7LetterI(){
        return new ArenaGen() {
            @Override
            public Arena createArena() {
                Arena a = ArenaShellFactory.createLetterIArena(new MapCoords(0,0));
               // RoomDecorationFactory.movingDoubleTurretRoom(a);
                a.addEntity(EntityFactory.grapplePointBag(a.getWidth() / 2, Measure.units(40f)));
                a.addEntity(EntityFactory.grapplePointBag(a.getWidth() / 2, Measure.units(70f)));
                a.addEntity(EntityFactory.grapplePointBag(a.getWidth() / 2, Measure.units(100f)));
                a.addEntity(EntityFactory.grapplePointBag(a.getWidth() / 2, Measure.units(130f)));
                a.addEntity(EntityFactory.grapplePointBag(a.getWidth() / 2, Measure.units(160f)));
                return a;
            }
        };
    }


    public static ArenaGen room8(){
        return new ArenaGen() {
            @Override
            public Arena createArena() {

                Random random = new Random();
                boolean mirror = random.nextBoolean();

                Arena a = ArenaShellFactory.createDeadEndArena(new MapCoords(0,0), mirror);

                float chestPosX = mirror ? a.getWidth() / 4 : a.getWidth() - a.getWidth() / 4;

                a.addEntity(ChestFactory.lockedChestBag(chestPosX, a.getHeight() / 4));
                //RoomDecorationFactory.blobRoom(a);
                return a;
            }
        };
    }


    public static ArenaGen deadEndW2(){

        return new ArenaGen() {
            @Override
            public Arena createArena() {

                Random random = new Random();
                boolean mirror = random.nextBoolean();

                Arena a = ArenaShellFactory.createWidth2DeadEndArena(new MapCoords(0, 0), mirror);

                float posX = mirror ? Measure.units(10) : a.getWidth() - Measure.units(10f);
                float chestPosX = mirror ? Measure.units(30f) : a.getWidth() - Measure.units(30f);
                float blockerPosX = mirror ? a.getWidth() - Measure.units(80f) : Measure.units(80f);

                float angle =  mirror ? 0 : 180;

                a.addEntity(ChestFactory.chestBag(chestPosX, Measure.units(15f)));

                a.addEntity(EntityFactory.wallBag(blockerPosX, Measure.units(45f), Measure.units(10f), Measure.units(10f)));
                a.addEntity(EntityFactory.wallBag(blockerPosX, Measure.units(10f), Measure.units(10f), Measure.units(10f)));

                a.addEntity(TurretFactory.fixedTurret(posX, Measure.units(50f),  angle, 3.0f, 1.5f));
                a.addEntity(TurretFactory.fixedTurret(posX, Measure.units(41f),  angle, 3.0f, 0));
                a.addEntity(TurretFactory.fixedTurret(posX, Measure.units(32f),  angle, 3.0f, 0));
                a.addEntity(TurretFactory.fixedTurret(posX, Measure.units(23f),  angle, 3.0f, 0));
                a.addEntity(TurretFactory.fixedTurret(posX, Measure.units(14f),  angle, 3.0f, 1.5f));


                return a;
            }
        };


    }


    public static ArenaGen room10(){


        return new ArenaGen() {
            @Override
            public Arena createArena() {
                Arena a = ArenaShellFactory.createHeight2Arena(new MapCoords(0,0));

                a.addEntity(EntityFactory.wallBag(a.getWidth() - Measure.units(60f), Measure.units(35f),
                        Measure.units(60f), Measure.units(5)));

                a.addEntity(EntityFactory.wallBag(0, Measure.units(65f),
                        Measure.units(60f), Measure.units(5)));

                // RoomDecorationFactory.movingDoubleTurretRoom(a);
                return a;
            }
        };






    }


}
