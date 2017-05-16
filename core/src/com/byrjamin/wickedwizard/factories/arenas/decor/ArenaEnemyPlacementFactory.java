package com.byrjamin.wickedwizard.factories.arenas.decor;

import com.artemis.Component;
import com.artemis.utils.Bag;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.utils.Array;
import com.byrjamin.wickedwizard.factories.AbstractFactory;
import com.byrjamin.wickedwizard.factories.arenas.Arena;
import com.byrjamin.wickedwizard.factories.enemy.BouncerFactory;
import com.byrjamin.wickedwizard.factories.enemy.KugelDuscheFactory;
import com.byrjamin.wickedwizard.factories.enemy.SilverHeadFactory;
import com.byrjamin.wickedwizard.factories.enemy.SpawnerFactory;
import com.byrjamin.wickedwizard.factories.enemy.BlobFactory;
import com.byrjamin.wickedwizard.factories.enemy.TurretFactory;
import com.byrjamin.wickedwizard.utils.Measure;

/**
 * Created by Home on 26/03/2017.
 */

public class ArenaEnemyPlacementFactory extends AbstractFactory {


    public BlobFactory blobFactory;
    public BouncerFactory bouncerFactory;
    public KugelDuscheFactory kugelDuscheFactory;
    public SilverHeadFactory silverHeadFactory;
    public SpawnerFactory spawnerFactory;
    public TurretFactory turretFactory;

    //TODO convert this into a class where you spawn enemies.

    public ArenaEnemyPlacementFactory(AssetManager assetManager) {
        super(assetManager);
        this.blobFactory = new BlobFactory(assetManager);
        this.bouncerFactory = new BouncerFactory(assetManager);
        this.kugelDuscheFactory = new KugelDuscheFactory(assetManager);
        this.silverHeadFactory = new SilverHeadFactory(assetManager);
        this.spawnerFactory = new SpawnerFactory(assetManager);
        this.turretFactory = new TurretFactory(assetManager);
    }

    public void spawnBlob(Arena a, float x, float y){
        Array<SpawnerFactory.Spawner> s = new Array<SpawnerFactory.Spawner>();
        s.add(new SpawnerFactory.Spawner() {
            public Bag<Component> spawnBag(float x, float y) {
                return blobFactory.blobBag(x,y);
            }
        });
        s.add(new SpawnerFactory.Spawner() {
            public Bag<Component> spawnBag(float x, float y) {
                return blobFactory.smallblobBag(x,y);
            }
        });
        //SpawnerFactory.spawnerBag(a.getWidth() / 4, a.getHeight() / 2, s);
        a.addEntity(spawnerFactory.spawnerBag(x, y, s));
    }

    public void turretRoom(Arena a){
        a.addEntity(turretFactory.fixedLockOnTurret(a.getWidth() - Measure.units(20), a.getHeight() - Measure.units(25)));
    }

    public void movingTurretRoom(Arena a){
        Array<SpawnerFactory.Spawner> s = new Array<SpawnerFactory.Spawner>();
        s.add(new SpawnerFactory.Spawner() {
            public Bag<Component> spawnBag(float x, float y) {
                return turretFactory.movingTurret(x,y);
            }
        });
        a.addEntity(spawnerFactory.spawnerBag(Measure.units(20), Measure.units(50), s));
    }

    public void movingTurretRoomRight(Arena a){
        Array<SpawnerFactory.Spawner> s = new Array<SpawnerFactory.Spawner>();
        s.add(new SpawnerFactory.Spawner() {
            public Bag<Component> spawnBag(float x, float y) {
                return turretFactory.movingTurret(x,y);
            }
        });
        a.addEntity(spawnerFactory.spawnerBag(a.getWidth() - Measure.units(20), a.getHeight() - Measure.units(15), s));
    }


    public void kugelDusche(Arena a){
        Array<SpawnerFactory.Spawner> s = new Array<SpawnerFactory.Spawner>();
        s.add(new SpawnerFactory.Spawner() {
            public Bag<Component> spawnBag(float x, float y) {
                return kugelDuscheFactory.kugelDusche(x,y);
            }
        });
        a.addEntity(spawnerFactory.spawnerBag(a.getWidth() / 2,(a.getHeight() / 2) + Measure.units(2.5f), s));
    }

    public void silverHead(Arena a, float x, float y){
        Array<SpawnerFactory.Spawner> s = new Array<SpawnerFactory.Spawner>();
        s.add(new SpawnerFactory.Spawner() {
            public Bag<Component> spawnBag(float x, float y) {
                return silverHeadFactory.silverHead(x,y);
            }
        });
        a.addEntity(spawnerFactory.spawnerBag(x, y + Measure.units(2.5f), s));
    }

    public void silverHead(Arena a){
        Array<SpawnerFactory.Spawner> s = new Array<SpawnerFactory.Spawner>();
        s.add(new SpawnerFactory.Spawner() {
            public Bag<Component> spawnBag(float x, float y) {
                return silverHeadFactory.silverHead(x,y);
            }
        });
        a.addEntity(spawnerFactory.spawnerBag(a.getWidth() / 2,(a.getHeight() / 2) + Measure.units(2.5f), s));
    }


    public void spawnBouncer(Arena a, float x, float y){
        Array<SpawnerFactory.Spawner> s = new Array<SpawnerFactory.Spawner>();
        s.add(new SpawnerFactory.Spawner() {
            public Bag<Component> spawnBag(float x, float y) {
                return bouncerFactory.smallBouncer(x,y);
            }
        });
        a.addEntity(spawnerFactory.spawnerBag(x, y, s));
    }

    public void spawnLargeBouncer(Arena a, float x, float y){
        Array<SpawnerFactory.Spawner> s = new Array<SpawnerFactory.Spawner>();
        s.add(new SpawnerFactory.Spawner() {
            public Bag<Component> spawnBag(float x, float y) {
                return bouncerFactory.largeBouncer(x,y);
            }
        });
        a.addEntity(spawnerFactory.spawnerBag(x, y, s));
    }



    public void movingDoubleTurretRoom(Arena a){
        Array<SpawnerFactory.Spawner> s = new Array<SpawnerFactory.Spawner>();
        s.add(new SpawnerFactory.Spawner() {
            public Bag<Component> spawnBag(float x, float y) {
                return turretFactory.movingTurret(x,y);
            }
        });
        a.addEntity(spawnerFactory.spawnerBag(a.getWidth() - Measure.units(20), a.getHeight() - Measure.units(15), s));

        s = new Array<SpawnerFactory.Spawner>();
        s.add(new SpawnerFactory.Spawner() {
            public Bag<Component> spawnBag(float x, float y) {
                return turretFactory.movingTurret(x,y);
            }
        });
        a.addEntity(spawnerFactory.spawnerBag(Measure.units(20), a.getHeight() - Measure.units(15), s));

    }


    public void biggablobba(Arena a){
        a.addEntity(blobFactory.BiggaBlobbaBag(a.getWidth() / 2, Measure.units(20)));
    }

}
