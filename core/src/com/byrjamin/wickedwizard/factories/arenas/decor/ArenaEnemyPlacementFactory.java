package com.byrjamin.wickedwizard.factories.arenas.decor;

import com.artemis.Component;
import com.artemis.utils.Bag;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.utils.Array;
import com.byrjamin.wickedwizard.factories.AbstractFactory;
import com.byrjamin.wickedwizard.factories.BombFactory;
import com.byrjamin.wickedwizard.factories.arenas.Arena;
import com.byrjamin.wickedwizard.factories.arenas.skins.ArenaSkin;
import com.byrjamin.wickedwizard.factories.enemy.AlurmFactory;
import com.byrjamin.wickedwizard.factories.enemy.AmoebaFactory;
import com.byrjamin.wickedwizard.factories.enemy.BouncerFactory;
import com.byrjamin.wickedwizard.factories.enemy.CowlFactory;
import com.byrjamin.wickedwizard.factories.enemy.GoatWizardFactory;
import com.byrjamin.wickedwizard.factories.enemy.JigFactory;
import com.byrjamin.wickedwizard.factories.enemy.KnightFactory;
import com.byrjamin.wickedwizard.factories.enemy.KugelDuscheFactory;
import com.byrjamin.wickedwizard.factories.enemy.ModonFactory;
import com.byrjamin.wickedwizard.factories.enemy.PylonFactory;
import com.byrjamin.wickedwizard.factories.enemy.SilverHeadFactory;
import com.byrjamin.wickedwizard.factories.enemy.SpawnerFactory;
import com.byrjamin.wickedwizard.factories.enemy.BlobFactory;
import com.byrjamin.wickedwizard.factories.enemy.SwitchFactory;
import com.byrjamin.wickedwizard.factories.enemy.TurretFactory;
import com.byrjamin.wickedwizard.utils.ComponentBag;
import com.byrjamin.wickedwizard.utils.Measure;

import java.util.Random;

/**
 * Created by Home on 26/03/2017.
 */

public class ArenaEnemyPlacementFactory extends AbstractFactory {


    public AlurmFactory alurmFactory;
    public AmoebaFactory amoebaFactory;
    public BlobFactory blobFactory;
    public BombFactory bombFactory;
    public BouncerFactory bouncerFactory;
    public CowlFactory cowlFactory;
    public GoatWizardFactory goatWizardFactory;
    public JigFactory jigFactory;
    public KnightFactory knightFactory;
    public KugelDuscheFactory kugelDuscheFactory;
    public ModonFactory modonFactory;
    public PylonFactory pylonFactory;
    public SilverHeadFactory silverHeadFactory;
    public SpawnerFactory spawnerFactory;
    public SwitchFactory switchFactory;
    public TurretFactory turretFactory;

    private ArenaSkin arenaSkin;
    private Random random;

    //TODO convert this into a class where you spawn enemies.

    public ArenaEnemyPlacementFactory(AssetManager assetManager, ArenaSkin arenaSkin, Random random) {
        super(assetManager);
        this.alurmFactory = new AlurmFactory(assetManager);
        this.amoebaFactory = new AmoebaFactory(assetManager);
        this.blobFactory = new BlobFactory(assetManager);
        this.bombFactory = new BombFactory(assetManager);
        this.bouncerFactory = new BouncerFactory(assetManager);
        this.cowlFactory = new CowlFactory(assetManager);
        this.goatWizardFactory = new GoatWizardFactory(assetManager);
        this.jigFactory = new JigFactory(assetManager);
        this.knightFactory = new KnightFactory(assetManager);
        this.kugelDuscheFactory = new KugelDuscheFactory(assetManager);
        this.modonFactory = new ModonFactory(assetManager);
        this.pylonFactory = new PylonFactory(assetManager);
        this.silverHeadFactory = new SilverHeadFactory(assetManager);
        this.spawnerFactory = new SpawnerFactory(assetManager, arenaSkin);
        this.switchFactory = new SwitchFactory(assetManager);
        this.turretFactory = new TurretFactory(assetManager);
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
        return spawnerFactory.spawnerBag(x, y, s);
    }

    public ComponentBag spawnAlurm(float x, float y){
        return spawnAlurm(x,y,random.nextBoolean(), random.nextBoolean());
    }

    public ComponentBag spawnAlurm(float x, float y, final boolean startsRight, final boolean startsUp){
        Array<SpawnerFactory.Spawner> s = new Array<SpawnerFactory.Spawner>();
        s.add(new SpawnerFactory.Spawner() {
            public Bag<Component> spawnBag(float x, float y) {
                return alurmFactory.alurm(x,y, startsRight, startsUp);
            }
        });
        return spawnerFactory.spawnerBag(x, y, s);
    }

    public ComponentBag spawnMovingJig(float x, float y){
        Array<SpawnerFactory.Spawner> s = new Array<SpawnerFactory.Spawner>();
        s.add(new SpawnerFactory.Spawner() {
            public Bag<Component> spawnBag(float x, float y) {
                return jigFactory.movingJig(x,y);
            }
        });
        return spawnerFactory.spawnerBag(x, y, s);
    }

    public ComponentBag spawnJig(float x, float y){
        Array<SpawnerFactory.Spawner> s = new Array<SpawnerFactory.Spawner>();
        s.add(new SpawnerFactory.Spawner() {
            public Bag<Component> spawnBag(float x, float y) {
                return jigFactory.stationaryJig(x,y);
            }
        });
        return spawnerFactory.spawnerBag(x, y, s);
    }

    public ComponentBag spawnBlob(float x, float y){
        return spawnBlob(x,y,random.nextBoolean());
    }

    public ComponentBag spawnBlob(float x, float y, final boolean startRight){
        Array<SpawnerFactory.Spawner> s = new Array<SpawnerFactory.Spawner>();
        s.add(new SpawnerFactory.Spawner() {
            public Bag<Component> spawnBag(float x, float y) {
                return blobFactory.blobBag(x,y, startRight);
            }
        });
        return spawnerFactory.spawnerBag(x, y, s);
    }

    public ComponentBag spawnAngryBlob(float x, float y){
        return spawnAngryBlob(x,y,random.nextBoolean());
    }


    public ComponentBag spawnAngryBlob(float x, float y, final boolean startRight){
        Array<SpawnerFactory.Spawner> s = new Array<SpawnerFactory.Spawner>();
        s.add(new SpawnerFactory.Spawner() {
            public Bag<Component> spawnBag(float x, float y) {
                return blobFactory.angryBlobBag(x,y, startRight);
            }
        });
        return spawnerFactory.spawnerBag(x, y, s);
    }

    public ComponentBag spawnSmallAngryBlob(float x, float y){
        return spawnSmallAngryBlob(x,y,random.nextBoolean());
    }

    public ComponentBag spawnSmallAngryBlob(float x, float y, final boolean startsRight){
        Array<SpawnerFactory.Spawner> s = new Array<SpawnerFactory.Spawner>();
        s.add(new SpawnerFactory.Spawner() {
            public Bag<Component> spawnBag(float x, float y) {
                return blobFactory.angrySmallBag(x,y, startsRight);
            }
        });
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

    public ComponentBag spawnFixedPentaSentry(float x, float y){
        Array<SpawnerFactory.Spawner> s = new Array<SpawnerFactory.Spawner>();
        s.add(new SpawnerFactory.Spawner() {
            public Bag<Component> spawnBag(float x, float y) {
                return turretFactory.fixedPentaSentry(x,y);
            }
        });
        return spawnerFactory.spawnerBag(x, y, s, 1, 1.5f);
    }

    public ComponentBag spawnMovingPentaSentry(float x, float y){
        Array<SpawnerFactory.Spawner> s = new Array<SpawnerFactory.Spawner>();
        s.add(new SpawnerFactory.Spawner() {
            public Bag<Component> spawnBag(float x, float y) {
                return turretFactory.movingPentaSentry(x,y, random.nextBoolean(), random.nextBoolean());
            }
        });
        return spawnerFactory.spawnerBag(x, y, s, 1, 1.5f);
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

    public ComponentBag spawnFixedFlyByDoubleBombSentry(float x, float y){
        Array<SpawnerFactory.Spawner> s = new Array<SpawnerFactory.Spawner>();
        s.add(new SpawnerFactory.Spawner() {
            public Bag<Component> spawnBag(float x, float y) {
                return turretFactory.fixedFlyByDoubleBombSentry(x,y);
            }
        });
        return spawnerFactory.spawnerBag(x, y, s, 1, 1.5f);
    }

    public ComponentBag spawnMovingFlyByDoubleBombSentry(float x, float y){
        Array<SpawnerFactory.Spawner> s = new Array<SpawnerFactory.Spawner>();
        s.add(new SpawnerFactory.Spawner() {
            public Bag<Component> spawnBag(float x, float y) {
                return turretFactory.movingFlyByDoubleBombSentry(x,y, random.nextBoolean(), random.nextBoolean());
            }
        });
        return spawnerFactory.spawnerBag(x, y, s, 1, 1.5f);
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

    public ComponentBag spawnLaserBouncer(float x, float y, final boolean startsLeft){
        Array<SpawnerFactory.Spawner> s = new Array<SpawnerFactory.Spawner>();
        s.add(new SpawnerFactory.Spawner() {
            public Bag<Component> spawnBag(float x, float y) {
                return bouncerFactory.laserBouncer(x,y, startsLeft);
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

    public ComponentBag spawnKnight(float x, float y){
        return spawnKnight(x,y,random.nextBoolean(), random.nextBoolean());
    }

    public ComponentBag spawnKnight(float x, float y, final boolean startsRight, final boolean startsUp){
        Array<SpawnerFactory.Spawner> s = new Array<SpawnerFactory.Spawner>();
        s.add(new SpawnerFactory.Spawner() {
            public Bag<Component> spawnBag(float x, float y) {
                return knightFactory.knightBag(x,y, startsRight, startsUp);
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


    public ComponentBag spawnHeavyModon(float x, float y){
        Array<SpawnerFactory.Spawner> s = new Array<SpawnerFactory.Spawner>();
        s.add(new SpawnerFactory.Spawner() {
            public Bag<Component> spawnBag(float x, float y) {
                return modonFactory.heavyModon(x,y);
            }
        });
        return spawnerFactory.spawnerBag(x, y, s,1,1.5f);
    }


    public ComponentBag spawnCowl(float x, float y){
        Array<SpawnerFactory.Spawner> s = new Array<SpawnerFactory.Spawner>();
        s.add(new SpawnerFactory.Spawner() {
            public Bag<Component> spawnBag(float x, float y) {
                return cowlFactory.cowl(x,y, 0, random.nextBoolean());
            }
        });
        return spawnerFactory.spawnerBag(x, y, s);
    }

}
