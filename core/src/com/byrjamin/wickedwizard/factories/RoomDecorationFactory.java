package com.byrjamin.wickedwizard.factories;

import com.artemis.Component;
import com.artemis.utils.Bag;
import com.badlogic.gdx.utils.Array;
import com.byrjamin.wickedwizard.factories.enemy.TurretFactory;
import com.byrjamin.wickedwizard.helper.Measure;

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
            //new RoomDecoration() { public void setUpArena(Arena a) {blobRoom(a);}},
            //new RoomDecoration() { public void setUpArena(Arena a) {blobRoom(a);}},
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
        SpawnerFactory.spawnerBag(a.getWidth() / 4, a.getHeight() / 2, s);
        a.addEntity(SpawnerFactory.spawnerBag(a.getWidth() / 4, a.getHeight() / 2, s));
    }

    public static void turretRoom(Arena a){

        a.addEntity(TurretFactory.fixedTurret(Measure.units(10), Measure.units(40)));
    }






}
