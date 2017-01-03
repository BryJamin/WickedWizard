package com.byrjamin.wickedwizard.enemy.enemies;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.byrjamin.wickedwizard.MainGame;
import com.byrjamin.wickedwizard.helper.Measure;
import com.byrjamin.wickedwizard.maps.rooms.Room;
import com.byrjamin.wickedwizard.enemy.EnemyBullets;
import com.byrjamin.wickedwizard.spelltypes.Dispellable;
import com.byrjamin.wickedwizard.spelltypes.Projectile;
import com.byrjamin.wickedwizard.helper.AnimationPacker;
import com.byrjamin.wickedwizard.helper.Reloader;
import com.byrjamin.wickedwizard.screens.PlayScreen;
import com.byrjamin.wickedwizard.staticstrings.TextureStrings;

/**
 * Created by Home on 20/11/2016.
 */
public class Turret extends com.byrjamin.wickedwizard.enemy.Enemy {

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

    private Rectangle hitBox;

    private Array<Dispellable.DISPELL> dispellSequence;

    private final float shotspeed;

    private float scale = 1;

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

        private Array<Dispellable.DISPELL> dispellSequence = new Array<Dispellable.DISPELL>();


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

        public TurretBuilder dispellSequence(Dispellable.DISPELL val)
        { dispellSequence.add(val); return this; }

        public TurretBuilder dispellSequence(Dispellable.DISPELL[] val)
        { dispellSequence.addAll(val); return this; }


        public Turret build() {
            return new Turret(this);
        }


    }

    public Turret(TurretBuilder builder){
        super();
        this.setHealth(builder.health);


        currentFrame = PlayScreen.atlas.findRegion(TextureStrings.BLOB_STANDING);

        WIDTH = SQUARE_SIZE;
        HEIGHT = SQUARE_SIZE;
        position = new Vector2(builder.posX, builder.posY);

        this.setDyingAnimation(AnimationPacker.genAnimation(0.05f / 1f, TextureStrings.BLOB_DYING));
        MOVEMENT = MOVEMENT * builder.speed;
        DEFAULT_SHOT_SPEED = DEFAULT_SHOT_SPEED * builder.shotSpeed;
        reloader = new Reloader(builder.reloadSpeed, builder.initialFiringDelay);

        hitBox  = new Rectangle(builder.posX + (Measure.units(1) * builder.scale), builder.posY,
                WIDTH - (Measure.units(2.5f) * builder.scale),
                HEIGHT - (Measure.units(2.5f) * builder.scale));

        bounds.add(hitBox);

        if(builder.dispellSequence.size == 0) {
            dispellSequence = new Array<Dispellable.DISPELL>();
            dispellSequence.add(Dispellable.DISPELL.HORIZONTAL);
        } else {
            dispellSequence = builder.dispellSequence;
        }
        shotspeed = builder.shotSpeed;
    }

    @Override
    public void update(float dt, Room r) {
        flashTimer(dt);
        updateMovement(dt);
        if(this.getState() == STATE.DYING){
            dyingUpdate(dt);
        }
        reloader.update(dt);
        fire(dt, r);
    }


    public void updateMovement(float dt){
        if(position.x + WIDTH > MainGame.GAME_WIDTH){
            MOVEMENT = -MainGame.GAME_UNITS * 10;
        } else if(position.x <= 0){
            MOVEMENT = MainGame.GAME_UNITS * 10;
        }

        position.add(MOVEMENT *dt, 0);

        hitBox.x = position.x + (Measure.units(1) * scale);
        hitBox.y = position.y;
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
    public void fire(float dt, Room a){
        if (reloader.isReady()) {
            EnemyBullets.activeBullets.add(new Projectile.ProjectileBuilder(position.x + WIDTH / 2, position.y + HEIGHT / 2, a.getWizard().getCenterX(),a.getWizard().getCenterY())
                    .spriteString("bullet")
                    .damage(1)
                    .HORIZONTAL_VELOCITY(15f)
                    .dispellable(new Dispellable(dispellSequence.get(0)))
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
