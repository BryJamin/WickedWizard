package com.bryjamin.wickedwizard.factories.arenas.decor;

import com.artemis.Component;
import com.artemis.utils.Bag;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.utils.Array;
import com.bryjamin.wickedwizard.factories.AbstractFactory;
import com.bryjamin.wickedwizard.factories.BombFactory;
import com.bryjamin.wickedwizard.factories.arenas.skins.ArenaSkin;
import com.bryjamin.wickedwizard.factories.enemy.AlurmFactory;
import com.bryjamin.wickedwizard.factories.enemy.AmoebaFactory;
import com.bryjamin.wickedwizard.factories.enemy.BlobFactory;
import com.bryjamin.wickedwizard.factories.enemy.BouncerFactory;
import com.bryjamin.wickedwizard.factories.enemy.CowlFactory;
import com.bryjamin.wickedwizard.factories.enemy.GoatWizardFactory;
import com.bryjamin.wickedwizard.factories.enemy.HoarderFactory;
import com.bryjamin.wickedwizard.factories.enemy.JigFactory;
import com.bryjamin.wickedwizard.factories.enemy.JumpingJackFactory;
import com.bryjamin.wickedwizard.factories.enemy.KnightFactory;
import com.bryjamin.wickedwizard.factories.enemy.KugelDuscheFactory;
import com.bryjamin.wickedwizard.factories.enemy.LaserusFactory;
import com.bryjamin.wickedwizard.factories.enemy.ModonFactory;
import com.bryjamin.wickedwizard.factories.enemy.PylonFactory;
import com.bryjamin.wickedwizard.factories.enemy.SilverHeadFactory;
import com.bryjamin.wickedwizard.factories.enemy.SnakeFactory;
import com.bryjamin.wickedwizard.factories.enemy.SpawnerFactory;
import com.bryjamin.wickedwizard.factories.enemy.SwitchFactory;
import com.bryjamin.wickedwizard.factories.enemy.TurretFactory;
import com.bryjamin.wickedwizard.utils.ComponentBag;
import com.bryjamin.wickedwizard.utils.Measure;

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
    public HoarderFactory hoarderFactory;
    public JumpingJackFactory jumpingJackFactory;
    public JigFactory jigFactory;
    public KnightFactory knightFactory;
    public KugelDuscheFactory kugelDuscheFactory;
    public LaserusFactory laserusFactory;
    public ModonFactory modonFactory;
    public PylonFactory pylonFactory;
    public SilverHeadFactory silverHeadFactory;
    public SnakeFactory snakeFactory;
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
        this.hoarderFactory = new HoarderFactory(assetManager);
        this.jigFactory = new JigFactory(assetManager);
        this.jumpingJackFactory = new JumpingJackFactory(assetManager);
        this.knightFactory = new KnightFactory(assetManager);
        this.kugelDuscheFactory = new KugelDuscheFactory(assetManager);
        this.laserusFactory = new LaserusFactory(assetManager);
        this.modonFactory = new ModonFactory(assetManager);
        this.pylonFactory = new PylonFactory(assetManager);
        this.silverHeadFactory = new SilverHeadFactory(assetManager);
        this.snakeFactory = new SnakeFactory(assetManager);
        this.spawnerFactory = new SpawnerFactory(assetManager, arenaSkin);
        this.switchFactory = new SwitchFactory(assetManager);
        this.turretFactory = new TurretFactory(assetManager);
        this.random = random;
        this.arenaSkin = arenaSkin;
    }

    public Bag<Component> spawnAmoeba(float x, float y){
        Array<com.bryjamin.wickedwizard.factories.enemy.SpawnerFactory.Spawner> s = new Array<com.bryjamin.wickedwizard.factories.enemy.SpawnerFactory.Spawner>();
        s.add(new com.bryjamin.wickedwizard.factories.enemy.SpawnerFactory.Spawner() {
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
        Array<com.bryjamin.wickedwizard.factories.enemy.SpawnerFactory.Spawner> s = new Array<com.bryjamin.wickedwizard.factories.enemy.SpawnerFactory.Spawner>();
        s.add(new com.bryjamin.wickedwizard.factories.enemy.SpawnerFactory.Spawner() {
            public Bag<Component> spawnBag(float x, float y) {
                return alurmFactory.alurm(x,y, startsRight, startsUp);
            }
        });
        return spawnerFactory.spawnerBag(x, y, s);
    }

    public ComponentBag spawnMovingJig(float x, float y){
        return spawnMovingJig(x, y, random.nextBoolean());
    }

    public ComponentBag spawnMovingJig(float x, float y, final boolean startsRight){
        Array<com.bryjamin.wickedwizard.factories.enemy.SpawnerFactory.Spawner> s = new Array<com.bryjamin.wickedwizard.factories.enemy.SpawnerFactory.Spawner>();
        s.add(new com.bryjamin.wickedwizard.factories.enemy.SpawnerFactory.Spawner() {
            public Bag<Component> spawnBag(float x, float y) {
                return jigFactory.movingJig(x,y, startsRight);
            }
        });
        return spawnerFactory.spawnerBag(x, y, s);
    }

    public ComponentBag spawnJig(float x, float y){
        Array<com.bryjamin.wickedwizard.factories.enemy.SpawnerFactory.Spawner> s = new Array<com.bryjamin.wickedwizard.factories.enemy.SpawnerFactory.Spawner>();
        s.add(new com.bryjamin.wickedwizard.factories.enemy.SpawnerFactory.Spawner() {
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
        Array<com.bryjamin.wickedwizard.factories.enemy.SpawnerFactory.Spawner> s = new Array<com.bryjamin.wickedwizard.factories.enemy.SpawnerFactory.Spawner>();
        s.add(new com.bryjamin.wickedwizard.factories.enemy.SpawnerFactory.Spawner() {
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
        Array<com.bryjamin.wickedwizard.factories.enemy.SpawnerFactory.Spawner> s = new Array<com.bryjamin.wickedwizard.factories.enemy.SpawnerFactory.Spawner>();
        s.add(new com.bryjamin.wickedwizard.factories.enemy.SpawnerFactory.Spawner() {
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
        Array<com.bryjamin.wickedwizard.factories.enemy.SpawnerFactory.Spawner> s = new Array<com.bryjamin.wickedwizard.factories.enemy.SpawnerFactory.Spawner>();
        s.add(new com.bryjamin.wickedwizard.factories.enemy.SpawnerFactory.Spawner() {
            public Bag<Component> spawnBag(float x, float y) {
                return blobFactory.angrySmallBag(x,y, startsRight);
            }
        });
        return spawnerFactory.spawnerBag(x, y, s);
    }


    public ComponentBag spawnFixedSentry(float x, float y) {
        Array<com.bryjamin.wickedwizard.factories.enemy.SpawnerFactory.Spawner> s = new Array<com.bryjamin.wickedwizard.factories.enemy.SpawnerFactory.Spawner>();
        s.add(new com.bryjamin.wickedwizard.factories.enemy.SpawnerFactory.Spawner() {
            public Bag<Component> spawnBag(float x, float y) {
                return turretFactory.fixedLockOnTurret(x,y);
            }
        });
        return spawnerFactory.spawnerBag(x, y, s);
    }

    public ComponentBag spawnMovingSentry(float x, float y){
        return spawnMovingSentry(x, y, random.nextBoolean());
    }

    public ComponentBag spawnMovingSentry(float x, float y, final boolean startsRight){
        Array<com.bryjamin.wickedwizard.factories.enemy.SpawnerFactory.Spawner> s = new Array<com.bryjamin.wickedwizard.factories.enemy.SpawnerFactory.Spawner>();
        s.add(new com.bryjamin.wickedwizard.factories.enemy.SpawnerFactory.Spawner() {
            public Bag<Component> spawnBag(float x, float y) {
                return turretFactory.movingSentry(x,y, startsRight);
            }
        });
        return spawnerFactory.spawnerBag(x, y, s);
    }

    public ComponentBag spawnFixedTriSentry(float x, float y){
        Array<com.bryjamin.wickedwizard.factories.enemy.SpawnerFactory.Spawner> s = new Array<com.bryjamin.wickedwizard.factories.enemy.SpawnerFactory.Spawner>();
        s.add(new com.bryjamin.wickedwizard.factories.enemy.SpawnerFactory.Spawner() {
            public Bag<Component> spawnBag(float x, float y) {
                return turretFactory.fixedMultiSentry(x,y);
            }
        });
        return spawnerFactory.spawnerBag(x, y, s);
    }

    public ComponentBag spawnMovingTriSentry(float x, float y){
        return spawnMovingTriSentry(x, y, random.nextBoolean());
    }

    public ComponentBag spawnMovingTriSentry(float x, float y, final boolean startsRight){
        Array<com.bryjamin.wickedwizard.factories.enemy.SpawnerFactory.Spawner> s = new Array<com.bryjamin.wickedwizard.factories.enemy.SpawnerFactory.Spawner>();
        s.add(new com.bryjamin.wickedwizard.factories.enemy.SpawnerFactory.Spawner() {
            public Bag<Component> spawnBag(float x, float y) {
                return turretFactory.movingHorizontalMultiSentry(x,y, startsRight);
            }
        });
        return spawnerFactory.spawnerBag(x, y, s);
    }

    public ComponentBag spawnMovingVerticalTriSentry(float x, float y, final boolean startsUp){
        Array<com.bryjamin.wickedwizard.factories.enemy.SpawnerFactory.Spawner> s = new Array<com.bryjamin.wickedwizard.factories.enemy.SpawnerFactory.Spawner>();
        s.add(new com.bryjamin.wickedwizard.factories.enemy.SpawnerFactory.Spawner() {
            public Bag<Component> spawnBag(float x, float y) {
                return turretFactory.movingVerticalMultiSentry(x,y, startsUp);
            }
        });
        return spawnerFactory.spawnerBag(x, y, s);
    }

    public ComponentBag spawnFixedPentaSentry(float x, float y){
        Array<com.bryjamin.wickedwizard.factories.enemy.SpawnerFactory.Spawner> s = new Array<com.bryjamin.wickedwizard.factories.enemy.SpawnerFactory.Spawner>();
        s.add(new com.bryjamin.wickedwizard.factories.enemy.SpawnerFactory.Spawner() {
            public Bag<Component> spawnBag(float x, float y) {
                return turretFactory.fixedPentaSentry(x,y);
            }
        });
        return spawnerFactory.spawnerBag(x, y, 1,1.0f, 1.5f, s);
    }

    public ComponentBag spawnMovingPentaSentry(float x, float y){
        Array<com.bryjamin.wickedwizard.factories.enemy.SpawnerFactory.Spawner> s = new Array<com.bryjamin.wickedwizard.factories.enemy.SpawnerFactory.Spawner>();
        s.add(new com.bryjamin.wickedwizard.factories.enemy.SpawnerFactory.Spawner() {
            public Bag<Component> spawnBag(float x, float y) {
                return turretFactory.movingPentaSentry(x,y, random.nextBoolean(), random.nextBoolean());
            }
        });
        return spawnerFactory.spawnerBag(x, y, 1,1.0f, 1.5f, s);
    }

    public ComponentBag spawnFixedFlyByBombSentry(float x, float y){
        Array<com.bryjamin.wickedwizard.factories.enemy.SpawnerFactory.Spawner> s = new Array<com.bryjamin.wickedwizard.factories.enemy.SpawnerFactory.Spawner>();
        s.add(new com.bryjamin.wickedwizard.factories.enemy.SpawnerFactory.Spawner() {
            public Bag<Component> spawnBag(float x, float y) {
                return turretFactory.fixedFlyByBombSentry(x,y);
            }
        });
        return spawnerFactory.spawnerBag(x, y, s);
    }

    public ComponentBag spawnMovingFlyByBombSentry(float x, float y){
        return spawnMovingFlyByBombSentry(x, y, random.nextBoolean());
    }

    public ComponentBag spawnMovingFlyByBombSentry(float x, float y, final boolean startsRight){
        Array<com.bryjamin.wickedwizard.factories.enemy.SpawnerFactory.Spawner> s = new Array<com.bryjamin.wickedwizard.factories.enemy.SpawnerFactory.Spawner>();
        s.add(new com.bryjamin.wickedwizard.factories.enemy.SpawnerFactory.Spawner() {
            public Bag<Component> spawnBag(float x, float y) {
                return turretFactory.movingFlyByBombSentry(x,y, startsRight);
            }
        });
        return spawnerFactory.spawnerBag(x, y, s);
    }

    public ComponentBag spawnVertivalMovingFlyByBombSentry(float x, float y, final boolean startsUp){
        Array<com.bryjamin.wickedwizard.factories.enemy.SpawnerFactory.Spawner> s = new Array<com.bryjamin.wickedwizard.factories.enemy.SpawnerFactory.Spawner>();
        s.add(new com.bryjamin.wickedwizard.factories.enemy.SpawnerFactory.Spawner() {
            public Bag<Component> spawnBag(float x, float y) {
                return turretFactory.movingVerticalFlyByBombSentry(x,y, startsUp);
            }
        });
        return spawnerFactory.spawnerBag(x, y, s);
    }


    public ComponentBag spawnFixedFlyByDoubleBombSentry(float x, float y){
        Array<com.bryjamin.wickedwizard.factories.enemy.SpawnerFactory.Spawner> s = new Array<com.bryjamin.wickedwizard.factories.enemy.SpawnerFactory.Spawner>();
        s.add(new com.bryjamin.wickedwizard.factories.enemy.SpawnerFactory.Spawner() {
            public Bag<Component> spawnBag(float x, float y) {
                return turretFactory.fixedFlyByDoubleBombSentry(x,y);
            }
        });
        return spawnerFactory.spawnerBag(x, y, 1,1.0f, 1.5f,s);
    }

    public ComponentBag spawnMovingFlyByDoubleBombSentry(float x, float y, final boolean startsRight, final boolean startsUp){
        Array<com.bryjamin.wickedwizard.factories.enemy.SpawnerFactory.Spawner> s = new Array<com.bryjamin.wickedwizard.factories.enemy.SpawnerFactory.Spawner>();
        s.add(new com.bryjamin.wickedwizard.factories.enemy.SpawnerFactory.Spawner() {
            public Bag<Component> spawnBag(float x, float y) {
                return turretFactory.movingFlyByDoubleBombSentry(x,y, startsRight, startsUp);
            }
        });
        return spawnerFactory.spawnerBag(x, y, 1,1.0f, 1.5f, s);
    }

    public ComponentBag spawnMovingFlyByDoubleBombSentry(float x, float y){
        return spawnMovingFlyByDoubleBombSentry(x, y, random.nextBoolean(), random.nextBoolean());
    }


    public ComponentBag spawnkugelDusche(float x, float y){
        return spawnkugelDusche(x, y, random.nextBoolean());
    }

    public ComponentBag spawnkugelDusche(float x, float y, final boolean turnsLeft){
        Array<com.bryjamin.wickedwizard.factories.enemy.SpawnerFactory.Spawner> s = new Array<com.bryjamin.wickedwizard.factories.enemy.SpawnerFactory.Spawner>();
        s.add(new com.bryjamin.wickedwizard.factories.enemy.SpawnerFactory.Spawner() {
            public Bag<Component> spawnBag(float x, float y) {
                return kugelDuscheFactory.kugelDusche(x,y, turnsLeft);
            }
        });
        return spawnerFactory.spawnerBag(x, y, s);
    }

    public ComponentBag spawnLaserKugel(float x, float y, final boolean isLeft){
        Array<com.bryjamin.wickedwizard.factories.enemy.SpawnerFactory.Spawner> s = new Array<com.bryjamin.wickedwizard.factories.enemy.SpawnerFactory.Spawner>();
        s.add(new com.bryjamin.wickedwizard.factories.enemy.SpawnerFactory.Spawner() {
            public Bag<Component> spawnBag(float x, float y) {
                return kugelDuscheFactory.laserKugel(x,y, isLeft);
            }
        });
        return spawnerFactory.spawnerBag(x, y, s);
    }

    public ComponentBag spawnSilverHead(float x, float y){
        Array<com.bryjamin.wickedwizard.factories.enemy.SpawnerFactory.Spawner> s = new Array<com.bryjamin.wickedwizard.factories.enemy.SpawnerFactory.Spawner>();
        s.add(new com.bryjamin.wickedwizard.factories.enemy.SpawnerFactory.Spawner() {
            public Bag<Component> spawnBag(float x, float y) {
                return silverHeadFactory.silverHead(x,y);
            }
        });
        return spawnerFactory.spawnerBag(x, y + Measure.units(2.5f), s);
    }

    public ComponentBag spawnBouncer(float x, float y){
        Array<com.bryjamin.wickedwizard.factories.enemy.SpawnerFactory.Spawner> s = new Array<com.bryjamin.wickedwizard.factories.enemy.SpawnerFactory.Spawner>();
        s.add(new com.bryjamin.wickedwizard.factories.enemy.SpawnerFactory.Spawner() {
            public Bag<Component> spawnBag(float x, float y) {
                return bouncerFactory.smallBouncer(x,y, random.nextBoolean());
            }
        });
        return spawnerFactory.spawnerBag(x, y, s);
    }

    public ComponentBag spawnBouncer(float x, float y, final boolean startsRight){
        Array<com.bryjamin.wickedwizard.factories.enemy.SpawnerFactory.Spawner> s = new Array<com.bryjamin.wickedwizard.factories.enemy.SpawnerFactory.Spawner>();
        s.add(new com.bryjamin.wickedwizard.factories.enemy.SpawnerFactory.Spawner() {
            public Bag<Component> spawnBag(float x, float y) {
                return bouncerFactory.smallBouncer(x,y, startsRight);
            }
        });
        return spawnerFactory.spawnerBag(x, y, s);
    }

    public ComponentBag spawnLaserBouncer(float x, float y){
        Array<com.bryjamin.wickedwizard.factories.enemy.SpawnerFactory.Spawner> s = new Array<com.bryjamin.wickedwizard.factories.enemy.SpawnerFactory.Spawner>();
        s.add(new com.bryjamin.wickedwizard.factories.enemy.SpawnerFactory.Spawner() {
            public Bag<Component> spawnBag(float x, float y) {
                return bouncerFactory.laserBouncer(x,y, random.nextBoolean());
            }
        });
        return spawnerFactory.spawnerBag(x, y, s);
    }

    public ComponentBag spawnLaserBouncer(float x, float y, final boolean startsRight){
        Array<com.bryjamin.wickedwizard.factories.enemy.SpawnerFactory.Spawner> s = new Array<com.bryjamin.wickedwizard.factories.enemy.SpawnerFactory.Spawner>();
        s.add(new com.bryjamin.wickedwizard.factories.enemy.SpawnerFactory.Spawner() {
            public Bag<Component> spawnBag(float x, float y) {
                return bouncerFactory.laserBouncer(x,y, startsRight);
            }
        });
        return spawnerFactory.spawnerBag(x, y, s);
    }

    public ComponentBag spawnLargeBouncer(float x, float y){
        return spawnLargeBouncer(x, y, random.nextBoolean());
    }

    public ComponentBag spawnLargeBouncer(float x, float y, final boolean startsRight){
        Array<com.bryjamin.wickedwizard.factories.enemy.SpawnerFactory.Spawner> s = new Array<com.bryjamin.wickedwizard.factories.enemy.SpawnerFactory.Spawner>();
        s.add(new com.bryjamin.wickedwizard.factories.enemy.SpawnerFactory.Spawner() {
            public Bag<Component> spawnBag(float x, float y) {
                return bouncerFactory.largeBouncer(x,y, startsRight);
            }
        });
        return spawnerFactory.spawnerBag(x, y, s);
    }

    public ComponentBag spawnGoatWizard(float x, float y){
        return spawnGoatWizard(x,y,random.nextBoolean(), random.nextBoolean());
    }

    public ComponentBag spawnGoatWizard(float x, float y, final boolean startsRight, final boolean startsUp){
        Array<com.bryjamin.wickedwizard.factories.enemy.SpawnerFactory.Spawner> s = new Array<com.bryjamin.wickedwizard.factories.enemy.SpawnerFactory.Spawner>();
        s.add(new com.bryjamin.wickedwizard.factories.enemy.SpawnerFactory.Spawner() {
            public Bag<Component> spawnBag(float x, float y) {
                return goatWizardFactory.goatWizard(x,y, startsRight, startsUp);
            }
        });
        return spawnerFactory.spawnerBag(x, y, s);
    }

    public ComponentBag spawnKnight(float x, float y){
        return spawnKnight(x,y,random.nextBoolean(), random.nextBoolean());
    }

    public ComponentBag spawnKnight(float x, float y, final boolean startsRight, final boolean startsUp){
        Array<com.bryjamin.wickedwizard.factories.enemy.SpawnerFactory.Spawner> s = new Array<com.bryjamin.wickedwizard.factories.enemy.SpawnerFactory.Spawner>();
        s.add(new com.bryjamin.wickedwizard.factories.enemy.SpawnerFactory.Spawner() {
            public Bag<Component> spawnBag(float x, float y) {
                return knightFactory.knightBag(x,y, startsRight, startsUp);
            }
        });
        return spawnerFactory.spawnerBag(x, y, s);
    }

    public ComponentBag spawnModon(float x, float y){
        Array<com.bryjamin.wickedwizard.factories.enemy.SpawnerFactory.Spawner> s = new Array<com.bryjamin.wickedwizard.factories.enemy.SpawnerFactory.Spawner>();
        s.add(new com.bryjamin.wickedwizard.factories.enemy.SpawnerFactory.Spawner() {
            public Bag<Component> spawnBag(float x, float y) {
                return modonFactory.modon(x,y);
            }
        });
        return spawnerFactory.spawnerBag(x, y, 1,1.0f,1.5f, s);
    }


    public ComponentBag spawnHeavyModon(float x, float y){
        Array<com.bryjamin.wickedwizard.factories.enemy.SpawnerFactory.Spawner> s = new Array<com.bryjamin.wickedwizard.factories.enemy.SpawnerFactory.Spawner>();
        s.add(new com.bryjamin.wickedwizard.factories.enemy.SpawnerFactory.Spawner() {
            public Bag<Component> spawnBag(float x, float y) {
                return modonFactory.heavyModon(x,y);
            }
        });
        return spawnerFactory.spawnerBag(x, y,1,1.0f,1.5f, s);
    }


    public ComponentBag spawnLaserus(float x, float y, final boolean startsRight, final boolean startsUp){
        Array<com.bryjamin.wickedwizard.factories.enemy.SpawnerFactory.Spawner> s = new Array<com.bryjamin.wickedwizard.factories.enemy.SpawnerFactory.Spawner>();
        s.add(new com.bryjamin.wickedwizard.factories.enemy.SpawnerFactory.Spawner() {
            public Bag<Component> spawnBag(float x, float y) {
                return laserusFactory.laserus(x,y, startsRight, startsUp);
            }
        });
        return spawnerFactory.spawnerBag(x, y, s);
    }

    public ComponentBag spawnLaserus(float x, float y){
        return spawnLaserus(x, y, random.nextBoolean(), random.nextBoolean());
    }


    public ComponentBag spawnRightSnake(float x, float y, final com.bryjamin.wickedwizard.utils.enums.Direction direction, int numberOfParts){
        Array<com.bryjamin.wickedwizard.factories.enemy.SpawnerFactory.Spawner> s = new Array<com.bryjamin.wickedwizard.factories.enemy.SpawnerFactory.Spawner>();
        s.add(new com.bryjamin.wickedwizard.factories.enemy.SpawnerFactory.Spawner() {
            public Bag<Component> spawnBag(float x, float y) {
                return snakeFactory.rightSnake(x,y, direction);
            }
        });
        return spawnerFactory.spawnerBag(x, y, numberOfParts,1.0f, 0.25f, 1, s);
    }

    public ComponentBag spawnLeftSnake(float x, float y, final com.bryjamin.wickedwizard.utils.enums.Direction direction, int numberOfParts){
        Array<com.bryjamin.wickedwizard.factories.enemy.SpawnerFactory.Spawner> s = new Array<com.bryjamin.wickedwizard.factories.enemy.SpawnerFactory.Spawner>();
        s.add(new com.bryjamin.wickedwizard.factories.enemy.SpawnerFactory.Spawner() {
            public Bag<Component> spawnBag(float x, float y) {
                return snakeFactory.leftSnake(x,y, direction);
            }
        });
        return spawnerFactory.spawnerBag(x, y, numberOfParts,1.0f, 0.25f, 1, s);
    }

    public ComponentBag spawnJumpingJack(float x, float y, final boolean startsRight){
        Array<com.bryjamin.wickedwizard.factories.enemy.SpawnerFactory.Spawner> s = new Array<com.bryjamin.wickedwizard.factories.enemy.SpawnerFactory.Spawner>();
        s.add(new com.bryjamin.wickedwizard.factories.enemy.SpawnerFactory.Spawner() {
            public Bag<Component> spawnBag(float x, float y) {
                return jumpingJackFactory.jumpingJack(x,y, startsRight);
            }
        });
        return spawnerFactory.spawnerBag(x, y, s);
    }

}
