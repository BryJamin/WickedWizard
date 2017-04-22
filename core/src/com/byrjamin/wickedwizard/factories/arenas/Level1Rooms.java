package com.byrjamin.wickedwizard.factories.arenas;

import com.badlogic.gdx.utils.Array;
import com.byrjamin.wickedwizard.factories.EntityFactory;
import com.byrjamin.wickedwizard.utils.MapCoords;
import com.byrjamin.wickedwizard.utils.Measure;

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
                a.addEntity(EntityFactory.grapplePointBag(a.getWidth() / 2, Measure.units(30f)));
                a.addEntity(EntityFactory.grapplePointBag(a.getWidth() / 2, Measure.units(60f)));
                a.addEntity(EntityFactory.grapplePointBag(a.getWidth() / 2, Measure.units(90f)));
                a.addEntity(EntityFactory.grapplePointBag(a.getWidth() / 2, Measure.units(120f)));
                return a;
            }
        };
    }


    public static ArenaGen room8(){
        return new ArenaGen() {
            @Override
            public Arena createArena() {
                Arena a = ArenaShellFactory.createOmniArena(new MapCoords(0,0));
                return a;
            }
        };
    }



}
