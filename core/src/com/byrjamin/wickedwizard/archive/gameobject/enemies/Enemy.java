package com.byrjamin.wickedwizard.archive.gameobject.enemies;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.byrjamin.wickedwizard.archive.gameobject.GameObject;
import com.byrjamin.wickedwizard.utils.AnimationPacker;
import com.byrjamin.wickedwizard.archive.maps.rooms.Room;
import com.byrjamin.wickedwizard.utils.BoundsDrawer;
import com.byrjamin.wickedwizard.archive.gameobject.ActiveBullets;
import com.byrjamin.wickedwizard.assets.TextureStrings;

/**
 * Abstract class for enemies within the game
 * Most enemies will draw from this class.
 */
public class Enemy extends GameObject {

    public enum STATE {
        DEAD, DYING, ALIVE
    }

    protected float WIDTH;
    protected float HEIGHT;

    protected float scale;
    protected float speed;

    protected Vector2 position = new Vector2();
    protected Vector2 velocity = new Vector2();

    public STATE state;

    protected float health;

    public boolean isFlashing;

    private float flashTimer;
    protected float time = 0;

    private Animation<TextureRegion> dyingAnimation;

    protected Array<Rectangle> bounds = new Array<Rectangle>();
    protected Rectangle collisionBound;

    protected ActiveBullets bullets = new ActiveBullets();

    protected Color drawingColor;

    protected TextureRegion currentFrame;


    public Enemy(){
        state = STATE.ALIVE;
        isFlashing = false;
        dyingAnimation = AnimationPacker.genAnimation(0.1f, TextureStrings.EXPLOSION);
    }

    public abstract static class EnemyBuilder<T extends EnemyBuilder> {

        //Required Parameters
        private final float posX;
        private final float posY;

        //Optional Parameters
        private float healthScale = 1;
        private float scale = 1;
        private float speed = 1;
        private float health = 5;

        public EnemyBuilder(float posX, float posY) {
            this.posX = posX;
            this.posY = posY;
        }

        public abstract T getThis();

        public T healthScale(float val)
        { healthScale = val; return getThis(); }

        public T health(float val)
        { health = val; return getThis(); }

        public T scale(float val)
        { scale = val; return getThis(); }

        public T speed(float val)
        { speed = val; return getThis(); }

        public Enemy build() {
            return new Enemy(this);
        }

    }

    public Enemy(EnemyBuilder b){
        this();
        position = new Vector2(b.posX, b.posY);
        speed = b.speed;
        scale = b.scale;
        health = b.health;
        health *= b.healthScale;
        //HEIGHT *= b.scale;
        //WIDTH *= b.scale;
    }


    //TODO this is a cop out method since I can't really think of a way to do this
    //TODO just in draw when draw itself is currently not well crafted.
    public void bulletDraw(SpriteBatch batch){
        bullets.draw(batch);
    }


    public void draw(SpriteBatch batch){
        if(isFlashing) {
            Color color = batch.getColor();
            batch.setColor(new Color(0.0f,0.0f,0.0f,0.95f));
            batch.draw(currentFrame, position.x, position.y, WIDTH, HEIGHT);
            batch.setColor(color);
        } else {
            batch.draw(currentFrame, position.x, position.y, WIDTH, HEIGHT);
        }
        BoundsDrawer.drawBounds(batch, bounds);
    }


    /**
     * Reduces the health of the enemy based on the number inserted,
     * but only if the enemy is targetable
     * @param i -
     */
    public void reduceHealth(float i){
        //System.out.println("Enemy took: "+i+" damage");
        this.setHealth(this.getHealth() - i);

        if(this.getHealth() <= 0 && getState() != STATE.DYING){
            time = 0;
            this.setState(STATE.DYING);
        }
        resetFlashTimer();
    }

    /**
     * If the enemy has finished flashing set isFlashing to false.
     * @param dt
     */
    public void flashTimer(float dt){
        if(flashTimer <= 0) {
            flashTimer = 0;
            isFlashing = false;
        } else {
            isFlashing = true;
            flashTimer -= dt;
        }
    }

    /**
     * Adds 0.05f to the flashtimer so the enemy can start to flash.
     */
    public void resetFlashTimer(){
        flashTimer += 0.05f;
    }


    public void update(float dt, Room r){
        flashTimer(dt);
        bullets.updateProjectile(dt, r, r.getWizard());

        if(state == STATE.ALIVE) {
            for (Rectangle bound : bounds) {
                if (bound.overlaps(r.getWizard().getBounds())) {
                    r.getWizard().reduceHealth(1);
                }
            }
        }
    }

    public Vector2 getPosition() {
        return position;
    }

    public void setPosition(Vector2 position) {
        this.position = position;
    }

    public void setHealth(float health) {
        this.health = health;
    }

    public float getHealth() {
        return health;
    }

    public Animation<TextureRegion> getDyingAnimation() {
        return dyingAnimation;
    }

    public void setDyingAnimation(Animation<TextureRegion> dyingAnimation) {
        this.dyingAnimation = dyingAnimation;
    }

    public STATE getState() {
        return state;
    }

    public void setState(STATE state) {
        this.state = state;
    }

    public void dyingUpdate(float dt){
        time+=dt;

        currentFrame = this.getDyingAnimation().getKeyFrame(time);

        if(this.getDyingAnimation().isAnimationFinished(time)){
            this.setState(STATE.DEAD);
        }
    }


    public boolean isHit(Rectangle r){
        for(Rectangle bound : bounds){
            if((state == STATE.ALIVE) && bound.overlaps(r)){
                return true;
            }
        }
        return false;
    }

    public void setCenter(float posX, float posY){
        float diffX = position.x - (posX - WIDTH / 2);
        float diffY = position.y - posY - HEIGHT / 2;
        position.x = posX - WIDTH / 2;
        position.y = posY - HEIGHT / 2;
    }

    public Array<Rectangle> getBounds() {
        return bounds;
    }

    public Rectangle getCollisionBound() {
        return collisionBound;
    }

    public ActiveBullets getBullets() {
        return bullets;
    }
}
