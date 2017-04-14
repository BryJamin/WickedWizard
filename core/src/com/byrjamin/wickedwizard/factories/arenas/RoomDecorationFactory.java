package com.byrjamin.wickedwizard.factories.arenas;

import com.artemis.Component;
import com.artemis.utils.Bag;
import com.badlogic.gdx.utils.Array;
import com.byrjamin.wickedwizard.archive.gameobject.enemies.KugelDusche;
import com.byrjamin.wickedwizard.factories.enemy.KugelDuscheFactory;
import com.byrjamin.wickedwizard.factories.enemy.SilverHeadFactory;
import com.byrjamin.wickedwizard.factories.enemy.SpawnerFactory;
import com.byrjamin.wickedwizard.factories.enemy.BlobFactory;
import com.byrjamin.wickedwizard.factories.enemy.TurretFactory;
import com.byrjamin.wickedwizard.utils.Measure;

/**
 * Created by Home on 26/03/2017.
 */

public class RoomDecorationFactory {

    public interface RoomDecoration {
        void setUpArena(Arena a);
    }

    public static RoomDecoration[] setUpArena = new RoomDecoration[] {
            new RoomDecoration() { public void setUpArena(Arena a) {blobRoom(a);}},
            new RoomDecoration() { public void setUpArena(Arena a) {turretRoom(a);}},
           // new RoomDecoration() { public void setUpArena(Arena a) {movingTurretRoom(a);}},
            new RoomDecoration() { public void setUpArena(Arena a) {movingTurretRoomRight(a);}},
            new RoomDecoration() { public void setUpArena(Arena a) {movingDoubleTurretRoom(a);}},
            //new RoomDecoration() { public void setUpArena(Arena a) {blobRoom(a);}},
            //new RoomDecoration() { public void setUpArena(Arena a) {blobRoom(a);}},
    };

    public static void blobRoom(Arena a){
        Array<SpawnerFactory.Spawner> s = new Array<SpawnerFactory.Spawner>();
        s.add(new SpawnerFactory.Spawner() {
            public Bag<Component> spawnBag(float x, float y) {
                return com.byrjamin.wickedwizard.factories.enemy.BlobFactory.blobBag(x,y);
            }
        });
        s.add(new SpawnerFactory.Spawner() {
            public Bag<Component> spawnBag(float x, float y) {
                return com.byrjamin.wickedwizard.factories.enemy.BlobFactory.smallblobBag(x,y);
            }
        });
        //SpawnerFactory.spawnerBag(a.getWidth() / 4, a.getHeight() / 2, s);
        a.addEntity(SpawnerFactory.spawnerBag(a.getWidth() / 4, a.getHeight() / 2, s));
    }

    public static void turretRoom(Arena a){
        a.addEntity(TurretFactory.fixedTurret(Measure.units(10), Measure.units(40)));
    }

    public static void movingTurretRoom(Arena a){
        Array<SpawnerFactory.Spawner> s = new Array<SpawnerFactory.Spawner>();
        s.add(new SpawnerFactory.Spawner() {
            public Bag<Component> spawnBag(float x, float y) {
                return TurretFactory.movingTurret(x,y);
            }
        });
        a.addEntity(SpawnerFactory.spawnerBag(Measure.units(10), Measure.units(40), s));
    }

    public static void movingTurretRoomRight(Arena a){
        Array<SpawnerFactory.Spawner> s = new Array<SpawnerFactory.Spawner>();
        s.add(new SpawnerFactory.Spawner() {
            public Bag<Component> spawnBag(float x, float y) {
                return TurretFactory.movingTurret(x,y);
            }
        });
        a.addEntity(SpawnerFactory.spawnerBag(a.getWidth() - Measure.units(20), Measure.units(40), s));
    }


    public static void kugelDusche(Arena a){
        Array<SpawnerFactory.Spawner> s = new Array<SpawnerFactory.Spawner>();
        s.add(new SpawnerFactory.Spawner() {
            public Bag<Component> spawnBag(float x, float y) {
                return KugelDuscheFactory.kugelDusche(x,y);
            }
        });
        a.addEntity(SpawnerFactory.spawnerBag(a.getWidth() / 2,(a.getHeight() / 2) + Measure.units(2.5f), s));
    }

    public static void silverHead(Arena a){
        Array<SpawnerFactory.Spawner> s = new Array<SpawnerFactory.Spawner>();
        s.add(new SpawnerFactory.Spawner() {
            public Bag<Component> spawnBag(float x, float y) {
                return SilverHeadFactory.silverHead(x,y);
            }
        });
        a.addEntity(SpawnerFactory.spawnerBag(a.getWidth() / 2,(a.getHeight() / 2) + Measure.units(2.5f), s));
    }



    public static void movingDoubleTurretRoom(Arena a){
        Array<SpawnerFactory.Spawner> s = new Array<SpawnerFactory.Spawner>();
        s.add(new SpawnerFactory.Spawner() {
            public Bag<Component> spawnBag(float x, float y) {
                return TurretFactory.movingTurret(x,y);
            }
        });
        a.addEntity(SpawnerFactory.spawnerBag(Measure.units(10), Measure.units(40), s));

        s = new Array<SpawnerFactory.Spawner>();
        s.add(new SpawnerFactory.Spawner() {
            public Bag<Component> spawnBag(float x, float y) {
                return TurretFactory.movingTurret(x,y);
            }
        });
        a.addEntity(SpawnerFactory.spawnerBag(a.getWidth() - Measure.units(20), Measure.units(40), s));

    }


    public static void biggablobba(Arena a){

        a.addEntity(BlobFactory.BiggaBlobbaBag(a.getWidth() / 2, Measure.units(4)));

/*        Array<SpawnerFactory.Spawner> s = new Array<SpawnerFactory.Spawner>();
        s.add(new SpawnerFactory.Spawner() {
            public Bag<Component> spawnBag(float x, float y) {
                return BlobFactory.BiggaBlobbaBag(x,y);
            }
        });
        a.addEntity(SpawnerFactory.spawnerBag(a.getWidth() / 2, Measure.units(4), s));*/
    }



//    BiggaBlobbaBag





}
