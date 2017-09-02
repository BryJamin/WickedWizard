package com.bryjamin.wickedwizard.factories.arenas.decor;

import com.artemis.Component;
import com.artemis.utils.Bag;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.utils.Array;
import com.bryjamin.wickedwizard.factories.arenas.skins.ArenaSkin;
import com.bryjamin.wickedwizard.factories.enemy.JigFactory;
import com.bryjamin.wickedwizard.utils.Measure;

import java.util.Random;

/**
 * Created by Home on 26/03/2017.
 */

public class ArenaEnemyPlacementFactory extends com.bryjamin.wickedwizard.factories.AbstractFactory {


    public com.bryjamin.wickedwizard.factories.enemy.AlurmFactory alurmFactory;
    public com.bryjamin.wickedwizard.factories.enemy.AmoebaFactory amoebaFactory;
    public com.bryjamin.wickedwizard.factories.enemy.BlobFactory blobFactory;
    public com.bryjamin.wickedwizard.factories.BombFactory bombFactory;
    public com.bryjamin.wickedwizard.factories.enemy.BouncerFactory bouncerFactory;
    public com.bryjamin.wickedwizard.factories.enemy.CowlFactory cowlFactory;
    public com.bryjamin.wickedwizard.factories.enemy.GoatWizardFactory goatWizardFactory;
    public com.bryjamin.wickedwizard.factories.enemy.HoarderFactory hoarderFactory;
    public com.bryjamin.wickedwizard.factories.enemy.JumpingJackFactory jumpingJackFactory;
    public JigFactory jigFactory;
    public com.bryjamin.wickedwizard.factories.enemy.KnightFactory knightFactory;
    public com.bryjamin.wickedwizard.factories.enemy.KugelDuscheFactory kugelDuscheFactory;
    public com.bryjamin.wickedwizard.factories.enemy.LaserusFactory laserusFactory;
    public com.bryjamin.wickedwizard.factories.enemy.ModonFactory modonFactory;
    public com.bryjamin.wickedwizard.factories.enemy.PylonFactory pylonFactory;
    public com.bryjamin.wickedwizard.factories.enemy.SilverHeadFactory silverHeadFactory;
    public com.bryjamin.wickedwizard.factories.enemy.SnakeFactory snakeFactory;
    public com.bryjamin.wickedwizard.factories.enemy.SpawnerFactory spawnerFactory;
    public com.bryjamin.wickedwizard.factories.enemy.SwitchFactory switchFactory;
    public com.bryjamin.wickedwizard.factories.enemy.TurretFactory turretFactory;

    private ArenaSkin arenaSkin;
    private Random random;

    //TODO convert this into a class where you spawn enemies.

    public ArenaEnemyPlacementFactory(AssetManager assetManager, ArenaSkin arenaSkin, Random random) {
        super(assetManager);
        this.alurmFactory = new com.bryjamin.wickedwizard.factories.enemy.AlurmFactory(assetManager);
        this.amoebaFactory = new com.bryjamin.wickedwizard.factories.enemy.AmoebaFactory(assetManager);
        this.blobFactory = new com.bryjamin.wickedwizard.factories.enemy.BlobFactory(assetManager);
        this.bombFactory = new com.bryjamin.wickedwizard.factories.BombFactory(assetManager);
        this.bouncerFactory = new com.bryjamin.wickedwizard.factories.enemy.BouncerFactory(assetManager);
        this.cowlFactory = new com.bryjamin.wickedwizard.factories.enemy.CowlFactory(assetManager);
        this.goatWizardFactory = new com.bryjamin.wickedwizard.factories.enemy.GoatWizardFactory(assetManager);
        this.hoarderFactory = new com.bryjamin.wickedwizard.factories.enemy.HoarderFactory(assetManager);
        this.jigFactory = new JigFactory(assetManager);
        this.jumpingJackFactory = new com.bryjamin.wickedwizard.factories.enemy.JumpingJackFactory(assetManager);
        this.knightFactory = new com.bryjamin.wickedwizard.factories.enemy.KnightFactory(assetManager);
        this.kugelDuscheFactory = new com.bryjamin.wickedwizard.factories.enemy.KugelDuscheFactory(assetManager);
        this.laserusFactory = new com.bryjamin.wickedwizard.factories.enemy.LaserusFactory(assetManager);
        this.modonFactory = new com.bryjamin.wickedwizard.factories.enemy.ModonFactory(assetManager);
        this.pylonFactory = new com.bryjamin.wickedwizard.factories.enemy.PylonFactory(assetManager);
        this.silverHeadFactory = new com.bryjamin.wickedwizard.factories.enemy.SilverHeadFactory(assetManager);
        this.snakeFactory = new com.bryjamin.wickedwizard.factories.enemy.SnakeFactory(assetManager);
        this.spawnerFactory = new com.bryjamin.wickedwizard.factories.enemy.SpawnerFactory(assetManager, arenaSkin);
        this.switchFactory = new com.bryjamin.wickedwizard.factories.enemy.SwitchFactory(assetManager);
        this.turretFactory = new com.bryjamin.wickedwizard.factories.enemy.TurretFactory(assetManager);
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

    public com.bryjamin.wickedwizard.utils.ComponentBag spawnAlurm(float x, float y){
        return spawnAlurm(x,y,random.nextBoolean(), random.nextBoolean());
    }

    public com.bryjamin.wickedwizard.utils.ComponentBag spawnAlurm(float x, float y, final boolean startsRight, final boolean startsUp){
        Array<com.bryjamin.wickedwizard.factories.enemy.SpawnerFactory.Spawner> s = new Array<com.bryjamin.wickedwizard.factories.enemy.SpawnerFactory.Spawner>();
        s.add(new com.bryjamin.wickedwizard.factories.enemy.SpawnerFactory.Spawner() {
            public Bag<Component> spawnBag(float x, float y) {
                return alurmFactory.alurm(x,y, startsRight, startsUp);
            }
        });
        return spawnerFactory.spawnerBag(x, y, s);
    }

    public com.bryjamin.wickedwizard.utils.ComponentBag spawnMovingJig(float x, float y){
        return spawnMovingJig(x, y, random.nextBoolean());
    }

    public com.bryjamin.wickedwizard.utils.ComponentBag spawnMovingJig(float x, float y, final boolean startsRight){
        Array<com.bryjamin.wickedwizard.factories.enemy.SpawnerFactory.Spawner> s = new Array<com.bryjamin.wickedwizard.factories.enemy.SpawnerFactory.Spawner>();
        s.add(new com.bryjamin.wickedwizard.factories.enemy.SpawnerFactory.Spawner() {
            public Bag<Component> spawnBag(float x, float y) {
                return jigFactory.movingJig(x,y, startsRight);
            }
        });
        return spawnerFactory.spawnerBag(x, y, s);
    }

    public com.bryjamin.wickedwizard.utils.ComponentBag spawnJig(float x, float y){
        Array<com.bryjamin.wickedwizard.factories.enemy.SpawnerFactory.Spawner> s = new Array<com.bryjamin.wickedwizard.factories.enemy.SpawnerFactory.Spawner>();
        s.add(new com.bryjamin.wickedwizard.factories.enemy.SpawnerFactory.Spawner() {
            public Bag<Component> spawnBag(float x, float y) {
                return jigFactory.stationaryJig(x,y);
            }
        });
        return spawnerFactory.spawnerBag(x, y, s);
    }

    public com.bryjamin.wickedwizard.utils.ComponentBag spawnBlob(float x, float y){
        return spawnBlob(x,y,random.nextBoolean());
    }

    public com.bryjamin.wickedwizard.utils.ComponentBag spawnBlob(float x, float y, final boolean startRight){
        Array<com.bryjamin.wickedwizard.factories.enemy.SpawnerFactory.Spawner> s = new Array<com.bryjamin.wickedwizard.factories.enemy.SpawnerFactory.Spawner>();
        s.add(new com.bryjamin.wickedwizard.factories.enemy.SpawnerFactory.Spawner() {
            public Bag<Component> spawnBag(float x, float y) {
                return blobFactory.blobBag(x,y, startRight);
            }
        });
        return spawnerFactory.spawnerBag(x, y, s);
    }

    public com.bryjamin.wickedwizard.utils.ComponentBag spawnAngryBlob(float x, float y){
        return spawnAngryBlob(x,y,random.nextBoolean());
    }


    public com.bryjamin.wickedwizard.utils.ComponentBag spawnAngryBlob(float x, float y, final boolean startRight){
        Array<com.bryjamin.wickedwizard.factories.enemy.SpawnerFactory.Spawner> s = new Array<com.bryjamin.wickedwizard.factories.enemy.SpawnerFactory.Spawner>();
        s.add(new com.bryjamin.wickedwizard.factories.enemy.SpawnerFactory.Spawner() {
            public Bag<Component> spawnBag(float x, float y) {
                return blobFactory.angryBlobBag(x,y, startRight);
            }
        });
        return spawnerFactory.spawnerBag(x, y, s);
    }

    public com.bryjamin.wickedwizard.utils.ComponentBag spawnSmallAngryBlob(float x, float y){
        return spawnSmallAngryBlob(x,y,random.nextBoolean());
    }

    public com.bryjamin.wickedwizard.utils.ComponentBag spawnSmallAngryBlob(float x, float y, final boolean startsRight){
        Array<com.bryjamin.wickedwizard.factories.enemy.SpawnerFactory.Spawner> s = new Array<com.bryjamin.wickedwizard.factories.enemy.SpawnerFactory.Spawner>();
        s.add(new com.bryjamin.wickedwizard.factories.enemy.SpawnerFactory.Spawner() {
            public Bag<Component> spawnBag(float x, float y) {
                return blobFactory.angrySmallBag(x,y, startsRight);
            }
        });
        return spawnerFactory.spawnerBag(x, y, s);
    }


    public com.bryjamin.wickedwizard.utils.ComponentBag spawnFixedSentry(float x, float y) {
        Array<com.bryjamin.wickedwizard.factories.enemy.SpawnerFactory.Spawner> s = new Array<com.bryjamin.wickedwizard.factories.enemy.SpawnerFactory.Spawner>();
        s.add(new com.bryjamin.wickedwizard.factories.enemy.SpawnerFactory.Spawner() {
            public Bag<Component> spawnBag(float x, float y) {
                return turretFactory.fixedLockOnTurret(x,y);
            }
        });
        return spawnerFactory.spawnerBag(x, y, s);
    }

    public com.bryjamin.wickedwizard.utils.ComponentBag spawnMovingSentry(float x, float y){
        return spawnMovingSentry(x, y, random.nextBoolean());
    }

    public com.bryjamin.wickedwizard.utils.ComponentBag spawnMovingSentry(float x, float y, final boolean startsRight){
        Array<com.bryjamin.wickedwizard.factories.enemy.SpawnerFactory.Spawner> s = new Array<com.bryjamin.wickedwizard.factories.enemy.SpawnerFactory.Spawner>();
        s.add(new com.bryjamin.wickedwizard.factories.enemy.SpawnerFactory.Spawner() {
            public Bag<Component> spawnBag(float x, float y) {
                return turretFactory.movingSentry(x,y, startsRight);
            }
        });
        return spawnerFactory.spawnerBag(x, y, s);
    }

    public com.bryjamin.wickedwizard.utils.ComponentBag spawnFixedTriSentry(float x, float y){
        Array<com.bryjamin.wickedwizard.factories.enemy.SpawnerFactory.Spawner> s = new Array<com.bryjamin.wickedwizard.factories.enemy.SpawnerFactory.Spawner>();
        s.add(new com.bryjamin.wickedwizard.factories.enemy.SpawnerFactory.Spawner() {
            public Bag<Component> spawnBag(float x, float y) {
                return turretFactory.fixedMultiSentry(x,y);
            }
        });
        return spawnerFactory.spawnerBag(x, y, s);
    }

    public com.bryjamin.wickedwizard.utils.ComponentBag spawnMovingTriSentry(float x, float y){
        return spawnMovingTriSentry(x, y, random.nextBoolean());
    }

    public com.bryjamin.wickedwizard.utils.ComponentBag spawnMovingTriSentry(float x, float y, final boolean startsRight){
        Array<com.bryjamin.wickedwizard.factories.enemy.SpawnerFactory.Spawner> s = new Array<com.bryjamin.wickedwizard.factories.enemy.SpawnerFactory.Spawner>();
        s.add(new com.bryjamin.wickedwizard.factories.enemy.SpawnerFactory.Spawner() {
            public Bag<Component> spawnBag(float x, float y) {
                return turretFactory.movingHorizontalMultiSentry(x,y, startsRight);
            }
        });
        return spawnerFactory.spawnerBag(x, y, s);
    }

    public com.bryjamin.wickedwizard.utils.ComponentBag spawnMovingVerticalTriSentry(float x, float y, final boolean startsUp){
        Array<com.bryjamin.wickedwizard.factories.enemy.SpawnerFactory.Spawner> s = new Array<com.bryjamin.wickedwizard.factories.enemy.SpawnerFactory.Spawner>();
        s.add(new com.bryjamin.wickedwizard.factories.enemy.SpawnerFactory.Spawner() {
            public Bag<Component> spawnBag(float x, float y) {
                return turretFactory.movingVerticalMultiSentry(x,y, startsUp);
            }
        });
        return spawnerFactory.spawnerBag(x, y, s);
    }

    public com.bryjamin.wickedwizard.utils.ComponentBag spawnFixedPentaSentry(float x, float y){
        Array<com.bryjamin.wickedwizard.factories.enemy.SpawnerFactory.Spawner> s = new Array<com.bryjamin.wickedwizard.factories.enemy.SpawnerFactory.Spawner>();
        s.add(new com.bryjamin.wickedwizard.factories.enemy.SpawnerFactory.Spawner() {
            public Bag<Component> spawnBag(float x, float y) {
                return turretFactory.fixedPentaSentry(x,y);
            }
        });
        return spawnerFactory.spawnerBag(x, y, 1,1.0f, 1.5f, s);
    }

    public com.bryjamin.wickedwizard.utils.ComponentBag spawnMovingPentaSentry(float x, float y){
        Array<com.bryjamin.wickedwizard.factories.enemy.SpawnerFactory.Spawner> s = new Array<com.bryjamin.wickedwizard.factories.enemy.SpawnerFactory.Spawner>();
        s.add(new com.bryjamin.wickedwizard.factories.enemy.SpawnerFactory.Spawner() {
            public Bag<Component> spawnBag(float x, float y) {
                return turretFactory.movingPentaSentry(x,y, random.nextBoolean(), random.nextBoolean());
            }
        });
        return spawnerFactory.spawnerBag(x, y, 1,1.0f, 1.5f, s);
    }

    public com.bryjamin.wickedwizard.utils.ComponentBag spawnFixedFlyByBombSentry(float x, float y){
        Array<com.bryjamin.wickedwizard.factories.enemy.SpawnerFactory.Spawner> s = new Array<com.bryjamin.wickedwizard.factories.enemy.SpawnerFactory.Spawner>();
        s.add(new com.bryjamin.wickedwizard.factories.enemy.SpawnerFactory.Spawner() {
            public Bag<Component> spawnBag(float x, float y) {
                return turretFactory.fixedFlyByBombSentry(x,y);
            }
        });
        return spawnerFactory.spawnerBag(x, y, s);
    }

    public com.bryjamin.wickedwizard.utils.ComponentBag spawnMovingFlyByBombSentry(float x, float y){
        return spawnMovingFlyByBombSentry(x, y, random.nextBoolean());
    }

    public com.bryjamin.wickedwizard.utils.ComponentBag spawnMovingFlyByBombSentry(float x, float y, final boolean startsRight){
        Array<com.bryjamin.wickedwizard.factories.enemy.SpawnerFactory.Spawner> s = new Array<com.bryjamin.wickedwizard.factories.enemy.SpawnerFactory.Spawner>();
        s.add(new com.bryjamin.wickedwizard.factories.enemy.SpawnerFactory.Spawner() {
            public Bag<Component> spawnBag(float x, float y) {
                return turretFactory.movingFlyByBombSentry(x,y, startsRight);
            }
        });
        return spawnerFactory.spawnerBag(x, y, s);
    }

    public com.bryjamin.wickedwizard.utils.ComponentBag spawnVertivalMovingFlyByBombSentry(float x, float y, final boolean startsUp){
        Array<com.bryjamin.wickedwizard.factories.enemy.SpawnerFactory.Spawner> s = new Array<com.bryjamin.wickedwizard.factories.enemy.SpawnerFactory.Spawner>();
        s.add(new com.bryjamin.wickedwizard.factories.enemy.SpawnerFactory.Spawner() {
            public Bag<Component> spawnBag(float x, float y) {
                return turretFactory.movingVerticalFlyByBombSentry(x,y, startsUp);
            }
        });
        return spawnerFactory.spawnerBag(x, y, s);
    }


    public com.bryjamin.wickedwizard.utils.ComponentBag spawnFixedFlyByDoubleBombSentry(float x, float y){
        Array<com.bryjamin.wickedwizard.factories.enemy.SpawnerFactory.Spawner> s = new Array<com.bryjamin.wickedwizard.factories.enemy.SpawnerFactory.Spawner>();
        s.add(new com.bryjamin.wickedwizard.factories.enemy.SpawnerFactory.Spawner() {
            public Bag<Component> spawnBag(float x, float y) {
                return turretFactory.fixedFlyByDoubleBombSentry(x,y);
            }
        });
        return spawnerFactory.spawnerBag(x, y, 1,1.0f, 1.5f,s);
    }

    public com.bryjamin.wickedwizard.utils.ComponentBag spawnMovingFlyByDoubleBombSentry(float x, float y){
        Array<com.bryjamin.wickedwizard.factories.enemy.SpawnerFactory.Spawner> s = new Array<com.bryjamin.wickedwizard.factories.enemy.SpawnerFactory.Spawner>();
        s.add(new com.bryjamin.wickedwizard.factories.enemy.SpawnerFactory.Spawner() {
            public Bag<Component> spawnBag(float x, float y) {
                return turretFactory.movingFlyByDoubleBombSentry(x,y, random.nextBoolean(), random.nextBoolean());
            }
        });
        return spawnerFactory.spawnerBag(x, y, 1,1.0f, 1.5f, s);
    }


    public com.bryjamin.wickedwizard.utils.ComponentBag spawnkugelDusche(float x, float y){
        return spawnkugelDusche(x, y, random.nextBoolean());
    }

    public com.bryjamin.wickedwizard.utils.ComponentBag spawnkugelDusche(float x, float y, final boolean turnsLeft){
        Array<com.bryjamin.wickedwizard.factories.enemy.SpawnerFactory.Spawner> s = new Array<com.bryjamin.wickedwizard.factories.enemy.SpawnerFactory.Spawner>();
        s.add(new com.bryjamin.wickedwizard.factories.enemy.SpawnerFactory.Spawner() {
            public Bag<Component> spawnBag(float x, float y) {
                return kugelDuscheFactory.kugelDusche(x,y, turnsLeft);
            }
        });
        return spawnerFactory.spawnerBag(x, y, s);
    }

    public com.bryjamin.wickedwizard.utils.ComponentBag spawnLaserKugel(float x, float y, final boolean isLeft){
        Array<com.bryjamin.wickedwizard.factories.enemy.SpawnerFactory.Spawner> s = new Array<com.bryjamin.wickedwizard.factories.enemy.SpawnerFactory.Spawner>();
        s.add(new com.bryjamin.wickedwizard.factories.enemy.SpawnerFactory.Spawner() {
            public Bag<Component> spawnBag(float x, float y) {
                return kugelDuscheFactory.laserKugel(x,y, isLeft);
            }
        });
        return spawnerFactory.spawnerBag(x, y, s);
    }

    public com.bryjamin.wickedwizard.utils.ComponentBag spawnSilverHead(float x, float y){
        Array<com.bryjamin.wickedwizard.factories.enemy.SpawnerFactory.Spawner> s = new Array<com.bryjamin.wickedwizard.factories.enemy.SpawnerFactory.Spawner>();
        s.add(new com.bryjamin.wickedwizard.factories.enemy.SpawnerFactory.Spawner() {
            public Bag<Component> spawnBag(float x, float y) {
                return silverHeadFactory.silverHead(x,y);
            }
        });
        return spawnerFactory.spawnerBag(x, y + Measure.units(2.5f), s);
    }

    public com.bryjamin.wickedwizard.utils.ComponentBag spawnBouncer(float x, float y){
        Array<com.bryjamin.wickedwizard.factories.enemy.SpawnerFactory.Spawner> s = new Array<com.bryjamin.wickedwizard.factories.enemy.SpawnerFactory.Spawner>();
        s.add(new com.bryjamin.wickedwizard.factories.enemy.SpawnerFactory.Spawner() {
            public Bag<Component> spawnBag(float x, float y) {
                return bouncerFactory.smallBouncer(x,y, random.nextBoolean());
            }
        });
        return spawnerFactory.spawnerBag(x, y, s);
    }

    public com.bryjamin.wickedwizard.utils.ComponentBag spawnBouncer(float x, float y, final boolean startsRight){
        Array<com.bryjamin.wickedwizard.factories.enemy.SpawnerFactory.Spawner> s = new Array<com.bryjamin.wickedwizard.factories.enemy.SpawnerFactory.Spawner>();
        s.add(new com.bryjamin.wickedwizard.factories.enemy.SpawnerFactory.Spawner() {
            public Bag<Component> spawnBag(float x, float y) {
                return bouncerFactory.smallBouncer(x,y, startsRight);
            }
        });
        return spawnerFactory.spawnerBag(x, y, s);
    }

    public com.bryjamin.wickedwizard.utils.ComponentBag spawnLaserBouncer(float x, float y){
        Array<com.bryjamin.wickedwizard.factories.enemy.SpawnerFactory.Spawner> s = new Array<com.bryjamin.wickedwizard.factories.enemy.SpawnerFactory.Spawner>();
        s.add(new com.bryjamin.wickedwizard.factories.enemy.SpawnerFactory.Spawner() {
            public Bag<Component> spawnBag(float x, float y) {
                return bouncerFactory.laserBouncer(x,y, random.nextBoolean());
            }
        });
        return spawnerFactory.spawnerBag(x, y, s);
    }

    public com.bryjamin.wickedwizard.utils.ComponentBag spawnLaserBouncer(float x, float y, final boolean startsRight){
        Array<com.bryjamin.wickedwizard.factories.enemy.SpawnerFactory.Spawner> s = new Array<com.bryjamin.wickedwizard.factories.enemy.SpawnerFactory.Spawner>();
        s.add(new com.bryjamin.wickedwizard.factories.enemy.SpawnerFactory.Spawner() {
            public Bag<Component> spawnBag(float x, float y) {
                return bouncerFactory.laserBouncer(x,y, startsRight);
            }
        });
        return spawnerFactory.spawnerBag(x, y, s);
    }

    public com.bryjamin.wickedwizard.utils.ComponentBag spawnLargeBouncer(float x, float y){
        return spawnLargeBouncer(x, y, random.nextBoolean());
    }

    public com.bryjamin.wickedwizard.utils.ComponentBag spawnLargeBouncer(float x, float y, final boolean startsRight){
        Array<com.bryjamin.wickedwizard.factories.enemy.SpawnerFactory.Spawner> s = new Array<com.bryjamin.wickedwizard.factories.enemy.SpawnerFactory.Spawner>();
        s.add(new com.bryjamin.wickedwizard.factories.enemy.SpawnerFactory.Spawner() {
            public Bag<Component> spawnBag(float x, float y) {
                return bouncerFactory.largeBouncer(x,y, startsRight);
            }
        });
        return spawnerFactory.spawnerBag(x, y, s);
    }

    public com.bryjamin.wickedwizard.utils.ComponentBag spawnGoatWizard(float x, float y){
        return spawnGoatWizard(x,y,random.nextBoolean(), random.nextBoolean());
    }

    public com.bryjamin.wickedwizard.utils.ComponentBag spawnGoatWizard(float x, float y, final boolean startsRight, final boolean startsUp){
        Array<com.bryjamin.wickedwizard.factories.enemy.SpawnerFactory.Spawner> s = new Array<com.bryjamin.wickedwizard.factories.enemy.SpawnerFactory.Spawner>();
        s.add(new com.bryjamin.wickedwizard.factories.enemy.SpawnerFactory.Spawner() {
            public Bag<Component> spawnBag(float x, float y) {
                return goatWizardFactory.goatWizard(x,y, startsRight, startsUp);
            }
        });
        return spawnerFactory.spawnerBag(x, y, s);
    }

    public com.bryjamin.wickedwizard.utils.ComponentBag spawnKnight(float x, float y){
        return spawnKnight(x,y,random.nextBoolean(), random.nextBoolean());
    }

    public com.bryjamin.wickedwizard.utils.ComponentBag spawnKnight(float x, float y, final boolean startsRight, final boolean startsUp){
        Array<com.bryjamin.wickedwizard.factories.enemy.SpawnerFactory.Spawner> s = new Array<com.bryjamin.wickedwizard.factories.enemy.SpawnerFactory.Spawner>();
        s.add(new com.bryjamin.wickedwizard.factories.enemy.SpawnerFactory.Spawner() {
            public Bag<Component> spawnBag(float x, float y) {
                return knightFactory.knightBag(x,y, startsRight, startsUp);
            }
        });
        return spawnerFactory.spawnerBag(x, y, s);
    }

    public com.bryjamin.wickedwizard.utils.ComponentBag spawnModon(float x, float y){
        Array<com.bryjamin.wickedwizard.factories.enemy.SpawnerFactory.Spawner> s = new Array<com.bryjamin.wickedwizard.factories.enemy.SpawnerFactory.Spawner>();
        s.add(new com.bryjamin.wickedwizard.factories.enemy.SpawnerFactory.Spawner() {
            public Bag<Component> spawnBag(float x, float y) {
                return modonFactory.modon(x,y);
            }
        });
        return spawnerFactory.spawnerBag(x, y, 1,1.0f,1.5f, s);
    }


    public com.bryjamin.wickedwizard.utils.ComponentBag spawnHeavyModon(float x, float y){
        Array<com.bryjamin.wickedwizard.factories.enemy.SpawnerFactory.Spawner> s = new Array<com.bryjamin.wickedwizard.factories.enemy.SpawnerFactory.Spawner>();
        s.add(new com.bryjamin.wickedwizard.factories.enemy.SpawnerFactory.Spawner() {
            public Bag<Component> spawnBag(float x, float y) {
                return modonFactory.heavyModon(x,y);
            }
        });
        return spawnerFactory.spawnerBag(x, y,1,1.0f,1.5f, s);
    }


    public com.bryjamin.wickedwizard.utils.ComponentBag spawnLaserus(float x, float y, final boolean startsRight, final boolean startsUp){
        Array<com.bryjamin.wickedwizard.factories.enemy.SpawnerFactory.Spawner> s = new Array<com.bryjamin.wickedwizard.factories.enemy.SpawnerFactory.Spawner>();
        s.add(new com.bryjamin.wickedwizard.factories.enemy.SpawnerFactory.Spawner() {
            public Bag<Component> spawnBag(float x, float y) {
                return laserusFactory.laserus(x,y, startsRight, startsUp);
            }
        });
        return spawnerFactory.spawnerBag(x, y, s);
    }

    public com.bryjamin.wickedwizard.utils.ComponentBag spawnLaserus(float x, float y){
        return spawnLaserus(x, y, random.nextBoolean(), random.nextBoolean());
    }


    public com.bryjamin.wickedwizard.utils.ComponentBag spawnRightSnake(float x, float y, final com.bryjamin.wickedwizard.utils.enums.Direction direction, int numberOfParts){
        Array<com.bryjamin.wickedwizard.factories.enemy.SpawnerFactory.Spawner> s = new Array<com.bryjamin.wickedwizard.factories.enemy.SpawnerFactory.Spawner>();
        s.add(new com.bryjamin.wickedwizard.factories.enemy.SpawnerFactory.Spawner() {
            public Bag<Component> spawnBag(float x, float y) {
                return snakeFactory.rightSnake(x,y, direction);
            }
        });
        return spawnerFactory.spawnerBag(x, y, numberOfParts,1.0f, 0.25f, 1, s);
    }

    public com.bryjamin.wickedwizard.utils.ComponentBag spawnLeftSnake(float x, float y, final com.bryjamin.wickedwizard.utils.enums.Direction direction, int numberOfParts){
        Array<com.bryjamin.wickedwizard.factories.enemy.SpawnerFactory.Spawner> s = new Array<com.bryjamin.wickedwizard.factories.enemy.SpawnerFactory.Spawner>();
        s.add(new com.bryjamin.wickedwizard.factories.enemy.SpawnerFactory.Spawner() {
            public Bag<Component> spawnBag(float x, float y) {
                return snakeFactory.leftSnake(x,y, direction);
            }
        });
        return spawnerFactory.spawnerBag(x, y, numberOfParts,1.0f, 0.25f, 1, s);
    }

    public com.bryjamin.wickedwizard.utils.ComponentBag spawnJumpingJack(float x, float y, final boolean startsRight){
        Array<com.bryjamin.wickedwizard.factories.enemy.SpawnerFactory.Spawner> s = new Array<com.bryjamin.wickedwizard.factories.enemy.SpawnerFactory.Spawner>();
        s.add(new com.bryjamin.wickedwizard.factories.enemy.SpawnerFactory.Spawner() {
            public Bag<Component> spawnBag(float x, float y) {
                return jumpingJackFactory.jumpingJack(x,y, startsRight);
            }
        });
        return spawnerFactory.spawnerBag(x, y, s);
    }

}
