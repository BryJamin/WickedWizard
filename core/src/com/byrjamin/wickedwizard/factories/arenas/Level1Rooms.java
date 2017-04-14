package com.byrjamin.wickedwizard.factories.arenas;

import com.badlogic.gdx.utils.Array;

/**
 * Created by Home on 13/04/2017.
 */

public class Level1Rooms {

    public static ArenaGen[] agArray = {room1(), room2(), room3()};

    public static Array<ArenaGen> getLevel1RoomArray(){
        Array<ArenaGen> ag = new Array<ArenaGen>();
       //ag.add(room1());
       // ag.add(room2());
       // ag.add(room3());
        ag.add(room4());
        ag.add(room5());
        return ag;
    }

    public static ArenaGen room1(){
        return new ArenaGen() {
            @Override
            public Arena createArena() {
                Arena a = RoomFactory.createOmniArena();
                RoomDecorationFactory.blobRoom(a);
                return a;
            }
        };
    }

    public static ArenaGen room2(){
        return new ArenaGen() {
            @Override
            public Arena createArena() {
                Arena a = RoomFactory.createOmniArena();
                RoomDecorationFactory.movingTurretRoomRight(a);
                return a;
            }
        };
    }

    public static ArenaGen room3(){
        return new ArenaGen() {
            @Override
            public Arena createArena() {
                Arena a = RoomFactory.createOmniArena();
                RoomDecorationFactory.movingDoubleTurretRoom(a);
                return a;
            }
        };
    }

    public static ArenaGen room4(){
        return new ArenaGen() {
            @Override
            public Arena createArena() {
                Arena a = RoomFactory.createOmniArena();
                RoomDecorationFactory.kugelDusche(a);
                return a;
            }
        };
    }

    public static ArenaGen room5(){
        return new ArenaGen() {
            @Override
            public Arena createArena() {
                Arena a = RoomFactory.createOmniArena();
                RoomDecorationFactory.silverHead(a);
                return a;
            }
        };
    }




}
