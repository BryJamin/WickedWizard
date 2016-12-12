package com.byrjamin.wickedwizard.sprites.enemies;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.byrjamin.wickedwizard.MainGame;
import com.byrjamin.wickedwizard.arenas.Arena;
import com.byrjamin.wickedwizard.arenas.EnemyBullets;
import com.byrjamin.wickedwizard.deck.cards.spelltypes.Projectile;
import com.byrjamin.wickedwizard.helper.AnimationPacker;
import com.byrjamin.wickedwizard.helper.Reloader;
import com.byrjamin.wickedwizard.screens.PlayScreen;

/**
 * Created by Home on 20/11/2016.
 */
public class Turret extends Enemy{

    private enum movement {
        WALKING, ATTACKING
    }

    private float MOVEMENT = MainGame.GAME_UNITS * 30;
    private float SQUARE_SIZE = MainGame.GAME_UNITS * 10;
    private float DEFAULT_SHOT_SPEED = 2.5f;

    private Reloader reloader;

    private float reloadTimer;

    private float fireRate = 1f;

    private boolean isVertical = false;

    private Projectile projectile;

    private Array<Projectile.DISPELL> dispellSequence;

    private final float shotspeed;

    public static class TurretBuilder {

        //Required Parameters
        private final float posX;
        private final float posY;

        //Optional Parameters
        private float health = 4;
        private float scale = 1;
        private float speed = 1;
        private float shotSpeed = 1;
        private float initialFiringDelay = 3.0f;
        private float reloadSpeed = 1f;

        private Array<Projectile.DISPELL> dispellSequence = new Array<Projectile.DISPELL>();


        public TurretBuilder(float posX, float posY) {
            this.posX = posX;
            this.posY = posY;
        }

        public TurretBuilder health(float val)
        { health = val; return this; }

        public TurretBuilder scale(float val)
        { scale = val; return this; }

        public TurretBuilder speed(float val)
        { speed = val; return this; }

        public TurretBuilder shotSpeed(float val)
        { shotSpeed = val; return this; }

        public TurretBuilder initialFiringDelay(float val)
        { initialFiringDelay = val; return this; }

        public TurretBuilder reloadSpeed(float val)
        { reloadSpeed = val; return this; }

        public TurretBuilder dispellSequence(Projectile.DISPELL val)
        { dispellSequence.add(val); return this; }

        public TurretBuilder dispellSequence(Projectile.DISPELL[] val)
        { dispellSequence.addAll(val); return this; }


        public Turret build() {
            return new Turret(this);
        }


    }

    public Turret(TurretBuilder builder){
        super();
        this.setHealth(builder.health);
        this.getSprite().setRegion(PlayScreen.atlas.findRegion("blob"));
        this.getSprite().setSize(SQUARE_SIZE * builder.scale, SQUARE_SIZE * builder.scale);
        this.getSprite().setCenter(builder.posX, builder.posY);
        this.setDyingAnimation(AnimationPacker.genAnimation(0.05f / 1f, "blob_dying"));
        MOVEMENT = MOVEMENT * builder.speed;
        DEFAULT_SHOT_SPEED = DEFAULT_SHOT_SPEED * builder.shotSpeed;
        reloader = new Reloader(builder.reloadSpeed, builder.initialFiringDelay);
        if(builder.dispellSequence.size == 0) {
            dispellSequence = new Array<Projectile.DISPELL>();
            dispellSequence.add(Projectile.DISPELL.HORIZONTAL);
        } else {
            dispellSequence = builder.dispellSequence;
        }
        shotspeed = builder.shotSpeed;
    }



    @Override
    public void update(float dt) {




    }

    @Override
    public void update(float dt, Arena a) {
        flashTimer(dt);
        updateMovement(dt);
        if(this.getState() == STATE.DYING){
            dyingUpdate(dt);
        }
        reloader.update(dt);
        fire(dt,a);
    }


    public void updateMovement(float dt){
        if(this.getSprite().getX() + this.getSprite().getWidth() > MainGame.GAME_WIDTH){
            MOVEMENT = -MainGame.GAME_UNITS * 10;
        } else if(this.getSprite().getX() <= 0){
            MOVEMENT = MainGame.GAME_UNITS * 10;
        }
        this.getSprite().translateX(MOVEMENT * dt);
    }


    //public void flipX()


    /**
     * Checks to see if the Turret can fire, if it can it fires.
     *
     * Currently this turret is set to alternate between Vertical and horizontal dispellables.
     *
     * This will be changed to be an option when generating a Turret.
     * @param dt
     * @param a
     */
    public void fire(float dt, Arena a){
        if (reloader.isReady()) {
            EnemyBullets.activeBullets.add(new Projectile.ProjectileBuilder(this.getSprite().getX(), this.getSprite().getY(), a.getWizard().getSprite().getX(),a.getWizard().getSprite().getY())
                    .spriteString("bullet")
                    .damage(1)
                    .HORIZONTAL_VELOCITY(DEFAULT_SHOT_SPEED)
                    .dispell(dispellSequence.get(0))
                    .build());

            dispellSequence.add(dispellSequence.get(0));
            dispellSequence.removeIndex(0);

        }
    }

    @Override
    public void draw(SpriteBatch batch){
        super.draw(batch);
    }

}
