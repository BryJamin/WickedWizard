package com.byrjamin.wickedwizard.factories.arenas;

import com.artemis.Component;
import com.artemis.utils.Bag;
import com.badlogic.gdx.utils.Array;
import com.byrjamin.wickedwizard.ecs.components.ai.FiringAIComponent;
import com.byrjamin.wickedwizard.factories.EntityFactory;
import com.byrjamin.wickedwizard.factories.chests.ChestFactory;
import com.byrjamin.wickedwizard.factories.enemy.TurretFactory;
import com.byrjamin.wickedwizard.utils.BagSearch;
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
        ag.add(room7());
        //ag.add(room8());
        ag.add(room9());
        return ag;
    }

    public static ArenaGen room1(){
        return new ArenaGen() {
            @Override
            public Arena createArena() {
                Arena a = ArenaShellFactory.createOmniArena();
                RoomDecorationFactory.blobRoom(a);
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
                return a;
            }
        };
    }

    public static ArenaGen room7(){
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
                Arena a = ArenaShellFactory.createDeadEndArena(new MapCoords(0,0), true);
                a.addEntity(ChestFactory.chestBag(Measure.units(30f), Measure.units(15f)));


                Random random = new Random();

                boolean mirror = true;


                float posX = mirror ? Measure.units(10) : a.getWidth() - Measure.units(10f);
                float chestPosX = mirror ? Measure.units(30f) : a.getWidth() - Measure.units(30f);
                float blockerPosX = mirror ? a.getWidth() - Measure.units(80f) : Measure.units(80f);

/*
                FiringAIComponent delay = mirror ? 0 : 180, 3.0f);
                FiringAIComponent normal = mirror ? 0 : 180);*/

                float angle =  mirror ? 0 : 180;

                a.addEntity(TurretFactory.fixedTurret(posX, Measure.units(50f),  angle, 3.0f, 1.5f));
                a.addEntity(TurretFactory.fixedTurret(posX, Measure.units(41f),  angle, 3.0f, 0f));
                a.addEntity(TurretFactory.fixedTurret(posX, Measure.units(32f),  angle, 3.0f, 0f));
                a.addEntity(TurretFactory.fixedTurret(posX, Measure.units(23f),  angle, 3.0f, 0f));
                a.addEntity(TurretFactory.fixedTurret(posX, Measure.units(14f),  angle, 3.0f, 1.5f));
                return a;
            }
        };
    }


    public static ArenaGen room9(){

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


}
