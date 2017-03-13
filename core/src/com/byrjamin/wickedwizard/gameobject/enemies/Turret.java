package com.byrjamin.wickedwizard.gameobject.enemies;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.byrjamin.wickedwizard.MainGame;
import com.byrjamin.wickedwizard.helper.Measure;
import com.byrjamin.wickedwizard.helper.collider.Collider;
import com.byrjamin.wickedwizard.maps.rooms.Room;
import com.byrjamin.wickedwizard.gameobject.ActiveBullets;
import com.byrjamin.wickedwizard.spelltypes.Projectile;
import com.byrjamin.wickedwizard.helper.AnimationPacker;
import com.byrjamin.wickedwizard.helper.timer.Reloader;
import com.byrjamin.wickedwizard.screens.PlayScreen;
import com.byrjamin.wickedwizard.assets.TextureStrings;

/**
 * Created by Home on 20/11/2016.
 */
public class Turret extends Enemy {

    private enum movement {
        WALKING, ATTACKING
    }

    private float MOVEMENT = Measure.units(15);
    private float NMOVEMENT = -MOVEMENT;
    private float PMOVEMENT = MOVEMENT;
    private float SQUARE_SIZE = MainGame.GAME_UNITS * 10;
    private float DEFAULT_SHOT_SPEED = 2.5f;

    private Vector2 velocity = new Vector2();

    private Reloader reloader;

    private float reloadTimer;

    private ActiveBullets activeBullets = new ActiveBullets();

    private float fireRate = 1f;

    private boolean isVertical = false;

    private Projectile projectile;

    private final float shotspeed;

    private float scale = 1;

/*    public static class TurretBuilder {

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

        public Turret build() {
            return new Turret(this);
        }


    }*/


    public static class TurretBuilder extends com.byrjamin.wickedwizard.gameobject.enemies.GroundedEnemy.GBuilder {

        //Optional Parameters
        private float scale = 1;
        private float speed = 1;
        private float shotSpeed = 1;
        private float initialFiringDelay = 3.0f;
        private float reloadSpeed = 1f;


        public TurretBuilder(float posX, float posY) {
            super(posX, posY);
            health(7);
        }

        public TurretBuilder shotSpeed(float val)
        { shotSpeed = val; return this; }

        public TurretBuilder initialFiringDelay(float val)
        { initialFiringDelay = val; return this; }

        public TurretBuilder reloadSpeed(float val)
        { reloadSpeed = val; return this; }

        @Override
        public Turret build() {
            return new Turret(this);
        }
    }





    public Turret(TurretBuilder b){
        super(b);
        currentFrame = PlayScreen.atlas.findRegion(TextureStrings.BLOB_STANDING);
        WIDTH = SQUARE_SIZE;
        HEIGHT = SQUARE_SIZE;
        MOVEMENT = MOVEMENT * speed;
        DEFAULT_SHOT_SPEED = DEFAULT_SHOT_SPEED * b.shotSpeed;
        reloader = new Reloader(b.reloadSpeed, b.initialFiringDelay);
        collisionBound  = new Rectangle(position.x + (Measure.units(1) * scale), position.y,
                WIDTH - (Measure.units(2.5f) * scale),
                HEIGHT - (Measure.units(2.5f) * scale));

        bounds.add(collisionBound);
        velocity = new Vector2(MOVEMENT, 0);
        shotspeed = b.shotSpeed;
        this.setDyingAnimation(AnimationPacker.genAnimation(0.05f / 1f, TextureStrings.BLOB_DYING));
    }

    @Override
    public void update(float dt, Room r) {
        super.update(dt, r);

        if(this.getState() == STATE.DYING || this.getState() == STATE.DEAD){
            dyingUpdate(dt);
        } else {
            updateMovement(dt, r);
            reloader.update(dt);
            fire(dt, r);
        }
    }


    public void updateMovement(float dt, Room r){
        position.add(velocity.x * dt, 0);
        collisionBound.x = position.x + (Measure.units(1) * scale);
        collisionBound.y = position.y;
    }

    @Override
    public void applyCollision(Collider.Collision collision) {
        switch(collision) {
            case LEFT: velocity.x = -MOVEMENT;
                break;
            case RIGHT: velocity.x = MOVEMENT;
                break;
        }
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
            bullets.addProjectile(new Projectile.ProjectileBuilder(position.x + WIDTH / 2, position.y + HEIGHT / 2, a.getWizard().getCenterX(),a.getWizard().getCenterY())
                    .damage(1)
                    .speed(Measure.units(60f))
                    .drawingColor(Color.CYAN)
                    .build());
        }
    }

    @Override
    public void draw(SpriteBatch batch){
        super.draw(batch);
        //BoundsDrawer.drawBounds(batch, hitBox);
    }



}
