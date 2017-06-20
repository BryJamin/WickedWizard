package com.byrjamin.wickedwizard.factories.arenas.decor;

import com.artemis.Component;
import com.artemis.utils.Bag;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.utils.Array;
import com.byrjamin.wickedwizard.factories.AbstractFactory;
import com.byrjamin.wickedwizard.factories.arenas.Arena;
import com.byrjamin.wickedwizard.factories.arenas.skins.ArenaSkin;
import com.byrjamin.wickedwizard.factories.enemy.AmoebaFactory;
import com.byrjamin.wickedwizard.factories.enemy.BouncerFactory;
import com.byrjamin.wickedwizard.factories.enemy.GoatWizardFactory;
import com.byrjamin.wickedwizard.factories.enemy.JigFactory;
import com.byrjamin.wickedwizard.factories.enemy.KugelDuscheFactory;
import com.byrjamin.wickedwizard.factories.enemy.ModonFactory;
import com.byrjamin.wickedwizard.factories.enemy.SilverHeadFactory;
import com.byrjamin.wickedwizard.factories.enemy.SpawnerFactory;
import com.byrjamin.wickedwizard.factories.enemy.BlobFactory;
import com.byrjamin.wickedwizard.factories.enemy.TurretFactory;
import com.byrjamin.wickedwizard.utils.ComponentBag;
import com.byrjamin.wickedwizard.utils.Measure;

import java.util.Random;

/**
 * Created by Home on 26/03/2017.
 */

public class ArenaEnemyPlacementFactory extends AbstractFactory {


    public AmoebaFactory amoebaFactory;
    public BlobFactory blobFactory;
    public BouncerFactory bouncerFactory;
    public KugelDuscheFactory kugelDuscheFactory;
    public SilverHeadFactory silverHeadFactory;
    public SpawnerFactory spawnerFactory;
    public TurretFactory turretFactory;
    public JigFactory jigFactory;
    public GoatWizardFactory goatWizardFactory;
    public ModonFactory modonFactory;
    private ArenaSkin arenaSkin;
    private Random random;

    //TODO convert this into a class where you spawn enemies.

    public ArenaEnemyPlacementFactory(AssetManager assetManager, ArenaSkin arenaSkin, Random random) {
        super(assetManager);
        this.blobFactory = new BlobFactory(assetManager);
        this.bouncerFactory = new BouncerFactory(assetManager);
        this.kugelDuscheFactory = new KugelDuscheFactory(assetManager);
        this.silverHeadFactory = new SilverHeadFactory(assetManager);
        this.spawnerFactory = new SpawnerFactory(assetManager, arenaSkin);
        this.turretFactory = new TurretFactory(assetManager);
        this.goatWizardFactory = new GoatWizardFactory(assetManager);
        this.amoebaFactory = new AmoebaFactory(assetManager);
        this.jigFactory = new JigFactory(assetManager);
        this.modonFactory = new ModonFactory(assetManager);
        this.random = random;
        this.arenaSkin = arenaSkin;
    }

    public Bag<Component> spawnAmoeba(float x, float y){
        Array<SpawnerFactory.Spawner> s = new Array<SpawnerFactory.Spawner>();
        s.add(new SpawnerFactory.Spawner() {
            public Bag<Component> spawnBag(float x, float y) {
                return amoebaFactory.amoeba(x,y);
            }
        });
        //SpawnerFactory.spawnerBag(a.getWidth() / 4, a.getHeight() / 2, s);
        return spawnerFactory.spawnerBag(x, y, s);
    }


    public Bag<Component> spawnMovingJig(float x, float y){
        Array<SpawnerFactory.Spawner> s = new Array<SpawnerFactory.Spawner>();
        s.add(new SpawnerFactory.Spawner() {
            public Bag<Component> spawnBag(float x, float y) {
                return jigFactory.movingJig(x,y);
            }
        });
        //SpawnerFactory.spawnerBag(a.getWidth() / 4, a.getHeight() / 2, s);
        return spawnerFactory.spawnerBag(x, y, s);
    }

    public Bag<Component> spawnJig(float x, float y){
        Array<SpawnerFactory.Spawner> s = new Array<SpawnerFactory.Spawner>();
        s.add(new SpawnerFactory.Spawner() {
            public Bag<Component> spawnBag(float x, float y) {
                return jigFactory.stationaryJig(x,y);
            }
        });
        //SpawnerFactory.spawnerBag(a.getWidth() / 4, a.getHeight() / 2, s);
        return spawnerFactory.spawnerBag(x, y, s);
    }

    public Bag<Component> spawnBlob(float x, float y){
        Array<SpawnerFactory.Spawner> s = new Array<SpawnerFactory.Spawner>();
        s.add(new SpawnerFactory.Spawner() {
            public Bag<Component> spawnBag(float x, float y) {
                return blobFactory.blobBag(x,y);
            }
        });
        //SpawnerFactory.spawnerBag(a.getWidth() / 4, a.getHeight() / 2, s);
        return spawnerFactory.spawnerBag(x, y, s);
    }


    public ComponentBag spawnAngryBlob(float x, float y){
        Array<SpawnerFactory.Spawner> s = new Array<SpawnerFactory.Spawner>();
        s.add(new SpawnerFactory.Spawner() {
            public Bag<Component> spawnBag(float x, float y) {
                return blobFactory.angryBlobBag(x,y);
            }
        });
        //SpawnerFactory.spawnerBag(a.getWidth() / 4, a.getHeight() / 2, s);
        return spawnerFactory.spawnerBag(x, y, s);
    }

    public ComponentBag spawnSmallAngryBlob(float x, float y){
        Array<SpawnerFactory.Spawner> s = new Array<SpawnerFactory.Spawner>();
        s.add(new SpawnerFactory.Spawner() {
            public Bag<Component> spawnBag(float x, float y) {
                return blobFactory.angrySmallBag(x,y);
            }
        });
        //SpawnerFactory.spawnerBag(a.getWidth() / 4, a.getHeight() / 2, s);
        return spawnerFactory.spawnerBag(x, y, s);
    }


    public ComponentBag spawnFixedSentry(float x, float y) {
        Array<SpawnerFactory.Spawner> s = new Array<SpawnerFactory.Spawner>();
        s.add(new SpawnerFactory.Spawner() {
            public Bag<Component> spawnBag(float x, float y) {
                return turretFactory.fixedLockOnTurret(x,y);
            }
        });
        return spawnerFactory.spawnerBag(x, y, s);
    }

    public ComponentBag spawnMovingSentry(float x, float y){
        Array<SpawnerFactory.Spawner> s = new Array<SpawnerFactory.Spawner>();
        s.add(new SpawnerFactory.Spawner() {
            public Bag<Component> spawnBag(float x, float y) {
                return turretFactory.movingSentry(x,y);
            }
        });
        return spawnerFactory.spawnerBag(x, y, s);
    }

    public ComponentBag spawnFixedTriSentry(float x, float y){
        Array<SpawnerFactory.Spawner> s = new Array<SpawnerFactory.Spawner>();
        s.add(new SpawnerFactory.Spawner() {
            public Bag<Component> spawnBag(float x, float y) {
                return turretFactory.fixedMultiSentry(x,y);
            }
        });
        return spawnerFactory.spawnerBag(x, y, s);
    }

    public ComponentBag spawnMovingTriSentry(float x, float y){
        Array<SpawnerFactory.Spawner> s = new Array<SpawnerFactory.Spawner>();
        s.add(new SpawnerFactory.Spawner() {
            public Bag<Component> spawnBag(float x, float y) {
                return turretFactory.movingHorizontalMultiSentry(x,y);
            }
        });
        return spawnerFactory.spawnerBag(x, y, s);
    }

    public ComponentBag spawnMovingVerticalTriSentry(float x, float y, final boolean startsUp){
        Array<SpawnerFactory.Spawner> s = new Array<SpawnerFactory.Spawner>();
        s.add(new SpawnerFactory.Spawner() {
            public Bag<Component> spawnBag(float x, float y) {
                return turretFactory.movingVerticalMultiSentry(x,y, startsUp);
            }
        });
        return spawnerFactory.spawnerBag(x, y, s);
    }

    public ComponentBag spawnFixedFlyByBombSentry(float x, float y){
        Array<SpawnerFactory.Spawner> s = new Array<SpawnerFactory.Spawner>();
        s.add(new SpawnerFactory.Spawner() {
            public Bag<Component> spawnBag(float x, float y) {
                return turretFactory.fixedFlyByBombSentry(x,y);
            }
        });
        return spawnerFactory.spawnerBag(x, y, s);
    }

    public ComponentBag spawnMovingFlyByBombSentry(float x, float y){
        Array<SpawnerFactory.Spawner> s = new Array<SpawnerFactory.Spawner>();
        s.add(new SpawnerFactory.Spawner() {
            public Bag<Component> spawnBag(float x, float y) {
                return turretFactory.movingFlyByBombSentry(x,y);
            }
        });
        return spawnerFactory.spawnerBag(x, y, s);
    }

    public ComponentBag spawnkugelDusche(float x, float y){
        Array<SpawnerFactory.Spawner> s = new Array<SpawnerFactory.Spawner>();
        s.add(new SpawnerFactory.Spawner() {
            public Bag<Component> spawnBag(float x, float y) {
                return kugelDuscheFactory.kugelDusche(x,y);
            }
        });
        return spawnerFactory.spawnerBag(x, y, s);
    }

    public ComponentBag spawnLaserKugel(float x, float y, final boolean isLeft){
        Array<SpawnerFactory.Spawner> s = new Array<SpawnerFactory.Spawner>();
        s.add(new SpawnerFactory.Spawner() {
            public Bag<Component> spawnBag(float x, float y) {
                return kugelDuscheFactory.laserKugel(x,y, isLeft);
            }
        });
        return spawnerFactory.spawnerBag(x, y, s);
    }

    public ComponentBag spawnSilverHead(float x, float y){
        Array<SpawnerFactory.Spawner> s = new Array<SpawnerFactory.Spawner>();
        s.add(new SpawnerFactory.Spawner() {
            public Bag<Component> spawnBag(float x, float y) {
                return silverHeadFactory.silverHead(x,y);
            }
        });
        return spawnerFactory.spawnerBag(x, y + Measure.units(2.5f), s);
    }

    public ComponentBag spawnBouncer(float x, float y){
        Array<SpawnerFactory.Spawner> s = new Array<SpawnerFactory.Spawner>();
        s.add(new SpawnerFactory.Spawner() {
            public Bag<Component> spawnBag(float x, float y) {
                return bouncerFactory.smallBouncer(x,y, random.nextBoolean());
            }
        });
        return spawnerFactory.spawnerBag(x, y, s);
    }

    public ComponentBag spawnLaserBouncer(float x, float y){
        Array<SpawnerFactory.Spawner> s = new Array<SpawnerFactory.Spawner>();
        s.add(new SpawnerFactory.Spawner() {
            public Bag<Component> spawnBag(float x, float y) {
                return bouncerFactory.laserBouncer(x,y, random.nextBoolean());
            }
        });
        return spawnerFactory.spawnerBag(x, y, s);
    }

    public ComponentBag spawnLargeBouncer(float x, float y){
        Array<SpawnerFactory.Spawner> s = new Array<SpawnerFactory.Spawner>();
        s.add(new SpawnerFactory.Spawner() {
            public Bag<Component> spawnBag(float x, float y) {
                return bouncerFactory.largeBouncer(x,y);
            }
        });
        return spawnerFactory.spawnerBag(x, y, s);
    }

    public ComponentBag spawnGoatWizard(float x, float y){
        Array<SpawnerFactory.Spawner> s = new Array<SpawnerFactory.Spawner>();
        s.add(new SpawnerFactory.Spawner() {
            public Bag<Component> spawnBag(float x, float y) {
                return goatWizardFactory.goatWizard(x,y);
            }
        });
        return spawnerFactory.spawnerBag(x, y, s);
    }

    public ComponentBag spawnModon(float x, float y){
        Array<SpawnerFactory.Spawner> s = new Array<SpawnerFactory.Spawner>();
        s.add(new SpawnerFactory.Spawner() {
            public Bag<Component> spawnBag(float x, float y) {
                return modonFactory.modon(x,y);
            }
        });
        return spawnerFactory.spawnerBag(x, y, s,1,1.5f);
    }

}
