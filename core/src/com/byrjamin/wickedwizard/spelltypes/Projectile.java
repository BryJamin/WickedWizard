package com.byrjamin.wickedwizard.spelltypes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.byrjamin.wickedwizard.MainGame;
import com.byrjamin.wickedwizard.helper.AnimationPacker;
import com.byrjamin.wickedwizard.helper.BoundsDrawer;
import com.byrjamin.wickedwizard.screens.PlayScreen;
import com.byrjamin.wickedwizard.player.Wizard;
import com.byrjamin.wickedwizard.enemy.Enemy;

/**
 * Created by Home on 07/11/2016.
 */
public class Projectile {

    public enum STATE {
        DEAD, EXPLODING, ALIVE
    }

    STATE state;

    double projectAngle;

    private Animation explosion_animation;

    private boolean animationFinished = false;

    float time;

    TextureRegion explosionTextureRegion;


    private Vector2 position;

    private float GRAVITY = -0.5f;
    private float HORIZONTAL_VELOCITY = 20f;
    private float VERTICAL_VELOCITY = 30f;

    private Vector2 velocity;

    //Required Parameters
    private final float x1;
    private final float y1;
    private final float x2;
    private final float y2;

    //Optional Parameters
    private float damage;
    private boolean gravity;
    private Animation explosionAnimation;
    private Dispellable dispellable;
    private Sprite sprite;
    private String spriteString;
    private Rectangle areaOfEffect;

    public static class ProjectileBuilder{

        //Required Parameters
        private final float x1;
        private final float y1;
        private final float x2;
        private final float y2;

        //Optional Parameters
        private float damage = 0;
        private boolean gravity = false;
        private Animation explosionAnimation;
        private Dispellable dispellable = new Dispellable(Dispellable.DISPELL.NONE);
        private Sprite sprite;
        private String spriteString = "fire";
        private Rectangle areaOfEffect;
        private float HORIZONTAL_VELOCITY = 20f;


        public ProjectileBuilder(float x1, float y1, float x2, float y2) {
            this.x1 = x1;
            this.y1 = y1;
            this.x2 = x2;
            this.y2 = y2;
        }

        public ProjectileBuilder damage(float val)
        { damage = val; return this; }

        public ProjectileBuilder gravity(boolean val)
        { gravity = val; return this; }

        public ProjectileBuilder HORIZONTAL_VELOCITY(float val)
        { HORIZONTAL_VELOCITY = val; return this; }

        public ProjectileBuilder explosionAnimation(Animation val)
        { explosionAnimation = val; return this; }

        public ProjectileBuilder dispellable(Dispellable val)
        { dispellable = val; return this; }

        public ProjectileBuilder sprite(Sprite val)
        { sprite = val; return this; }

        public ProjectileBuilder spriteString(String val)
        { spriteString = val; return this; }

        public ProjectileBuilder areaOfEffect(Rectangle val)
        { areaOfEffect = val; return this; }

        public Projectile build() {
            return new Projectile(this);
        }


    }

    public Projectile(ProjectileBuilder builder){
        x1 = builder.x1;
        x2 = builder.x2;
        y1 = builder.y1;
        y2 = builder.y2;
        damage = builder.damage;
        gravity = builder.gravity;
        explosionAnimation = builder.explosionAnimation;
        dispellable = builder.dispellable;
        sprite = builder.sprite;
        spriteString = builder.spriteString;
        HORIZONTAL_VELOCITY = builder.HORIZONTAL_VELOCITY;
        //TODO fix this crap
        sprite = PlayScreen.atlas.createSprite(spriteString);
        calculateAngle(x1,y1,x2,y2);
        sprite.setSize(MainGame.GAME_UNITS * 3, MainGame.GAME_UNITS * 3);

        //sprite.setOrigin(x1, y1);

        sprite.setCenter(x1, y1);
        sprite.setOriginCenter();
        sprite.rotate((float) Math.toDegrees(projectAngle));

        areaOfEffect = builder.areaOfEffect;
        time = 0;
        state = STATE.ALIVE;

        explosion_animation = AnimationPacker.genAnimation(0.05f / 1f, "explosion");

        if(gravity) {
            velocity = new Vector2(0, (float) (VERTICAL_VELOCITY * Math.sin(projectAngle)));
        }

    }

    public void projectileSetup(float x1,float y1, float x2, float y2){
        sprite.setCenter(x1, y1);
        calculateAngle(x1,y1,x2,y2);
    }

//TODO More math T---T
    public void calculateAngle(float x1,float y1, float x2, float y2){
        projectAngle = (Math.atan2(y2 - y1, x2 - x1));
    }

/*    public void update(float dt, Array<Enemy> e){

        if(getState() == STATE.ALIVE) {
            aliveUpdate(dt, e);
        } else if(getState() == STATE.EXPLODING){
            time += dt;
            if(explosion_animation.isAnimationFinished(time)){
                this.setState(STATE.DEAD);
            }
            explosionTextureRegion = explosion_animation.getKeyFrame(time);
        }

    }

    public void update(float dt, Wizard w){

        if(getState() == STATE.ALIVE) {
            aliveUpdate(dt, w);
        } else if(getState() == STATE.EXPLODING){
            time += dt;
            if(explosion_animation.isAnimationFinished(time)){
                this.setState(STATE.DEAD);
            }
            explosionTextureRegion = explosion_animation.getKeyFrame(time);
        }

    }*/


    public void update(float dt){
        if(getState() == STATE.ALIVE) {
            outOfBoundsCheck();
            travelUpdate();
        } else if(getState() == STATE.EXPLODING){
            time += dt;
            if(explosion_animation.isAnimationFinished(time)){
                this.setState(STATE.DEAD);
            }
            explosionTextureRegion = explosion_animation.getKeyFrame(time);
        }
    }

/*    public void aliveUpdate(float dt, Wizard w){

        outOfBoundsCheck();
        //TODO if bullet hits the ground it shoudl run it's death animation
        damageCheck(w);
        travelUpdate();

    }*/


    /**
     * Checks if the bullets go offScreen, if they do, Remove them.
     */
    public void outOfBoundsCheck(){
        if((getSprite().getX() > MainGame.GAME_WIDTH || getSprite().getX() < 0
                || getSprite().getY() > MainGame.GAME_HEIGHT
                || getSprite().getY() < 0) && !gravity){
            this.setState(STATE.DEAD);
        } else if(getSprite().getX() > MainGame.GAME_WIDTH || getSprite().getX() < 0
                || getSprite().getY() < 0) {
            this.setState(STATE.DEAD);
        }
    }

    public void travelUpdate(){

        float x;
        float y;


        x = this.getSprite().getX() + (this.getSprite().getWidth() / 2) + (float) (HORIZONTAL_VELOCITY * Math.cos(projectAngle));
        //this.getSprite().setX(this.getSprite().getX() + (float) (HORIZONTAL_VELOCITY * Math.cos(projectAngle)));
        if(gravity){
            velocity.add(0, GRAVITY);
            y = this.getSprite().getY() + velocity.y;
            //this.getSprite().setY(this.getSprite().getY() + velocity.y);



        } else {
            y = this.getSprite().getY() + (this.getSprite().getHeight() / 2) + (float) (HORIZONTAL_VELOCITY * Math.sin(projectAngle));

            //this.getSprite().setY(this.getSprite().getY() + (float) (HORIZONTAL_VELOCITY * Math.sin(projectAngle)));
        }

        this.getSprite().setCenter(x,y);

    }

    public void dispellProjectile(Dispellable.DISPELL dispell){

        if(state == STATE.ALIVE) {
            dispellable.dispel(dispell);
            if (dispellable.isDispelled()) {
                time = 0;
                this.setState(STATE.EXPLODING);
            }
        }

    }

    public void dispellProjectile(){
            time = 0;
            this.setState(STATE.EXPLODING);
    }

   /* public void damageCheck(Rectangle... r){
        if(getSprite().getY() <= PlayScreen.GROUND_Y){
            this.getSprite().setY(PlayScreen.GROUND_Y);
            if(areaOfEffect != null){
                explosionHit(getSprite(), getAreaOfEffect(), r);
            }
            this.setState(STATE.EXPLODING);
        }

        if(areaOfEffect == null) {
            singleTargetProjectileDamageCheck(e);
        } else {
            multipleTargetProjectileDamageCheck(e);
        }
    }

    public void damageCheck(Wizard w){
        if(getSprite().getY() <= PlayScreen.GROUND_Y){
            this.getSprite().setY(PlayScreen.GROUND_Y);
            if(areaOfEffect != null){
                explosionHit(getSprite(), getAreaOfEffect(), w);
            }
            this.setState(STATE.EXPLODING);
        }

        if(areaOfEffect == null) {
            singleTargetProjectileDamageCheck(w);
        } else {
            multipleTargetProjectileDamageCheck(w);
        }
    }


    *//**
     * Checks if the projectile has hit any of the enemies on screen, if it has the bullet deals damage
     * and switches to another state.
     *
     * If the enemy is already dying the bullet passes through the enemy.
     *
     * @param enemies - Array of Enemies
     *//*
    public void singleTargetProjectileDamageCheck(Rectangle... hitboxes){

        for (Rectangle r : hitboxes) {
            if(e.isHit(getSprite().getBoundingRectangle())){
                e.reduceHealth(damage);
                this.setState(STATE.EXPLODING);
                return;
            }
        }
    }

    *//**
     * Checks if the projectile has hit the wizard, if it has the projectile deals damage and switches state.
     * @param w
     *//*
    public void singleTargetProjectileDamageCheck(Wizard w){
        if(getSprite().getBoundingRectangle().overlaps(w.getBounds())){
            w.reduceHealth(damage);
            this.setState(STATE.EXPLODING);
        }
    }

    *//**
     * For reach enemy that exists check if the bullet hit the enemy,
     * If this is the case check to see if the rectangle hits any other enemies
     * @param e
     *//*
    public void multipleTargetProjectileDamageCheck(Array<Enemy> e) {
        for (Enemy enemy : e) {
            for(Rectangle r : enemy.getBounds()){
                if(getSprite().getBoundingRectangle().overlaps(r)){
                    explosionHit(getSprite(), getAreaOfEffect(), e);
                    return;
                }
            }
        }
    }

    public void multipleTargetProjectileDamageCheck(Wizard w) {
        if(getSprite().getBoundingRectangle().overlaps(w.getBounds())){
                explosionHit(getSprite(), getAreaOfEffect(), w);
        }
    }

    *//**
     * Gets the center of the Projectile Sprite and applies the Area of effect Rectangle from
     * that position.
     *
     * Then checks to see how many enemies were within the area of effect and applies damage.
     *
     * @param bullet - The projectile's Sprite
     * @param explosionRadius - Area of Effect of the Explosion
     * @param enemies - Array of enemies
     *//*
    public void explosionHit(Sprite bullet, Rectangle explosionRadius, Array<Enemy> enemies){
        Vector2 temp = new Vector2();
        bullet.getBoundingRectangle().getCenter(temp);
        explosionRadius.setCenter(temp);
        //damageRadius = new Rectangle(temp.x, temp.y, MainGame.GAME_UNITS * 15, MainGame.GAME_UNITS * 15);
        for(Enemy e: enemies){
            if(e.isHit(explosionRadius)){
                e.reduceHealth(damage);
                this.setState(STATE.EXPLODING);
            }
        }
    }

    public void explosionHit(Sprite bullet, Rectangle explosionRadius, Wizard w) {
        Vector2 temp = new Vector2();
        bullet.getBoundingRectangle().getCenter(temp);
        explosionRadius.setCenter(temp);
        //damageRadius = new Rectangle(temp.x, temp.y, MainGame.GAME_UNITS * 15, MainGame.GAME_UNITS * 15);
        if (explosionRadius.overlaps(w.getBounds())) {
            w.reduceHealth(damage);
            this.setState(STATE.EXPLODING);
        }
    }*/

    public void draw(SpriteBatch batch){

        BoundsDrawer.drawBounds(batch, this.getSprite().getBoundingRectangle());

        if(getState() == STATE.ALIVE){
            alive_draw(batch);
        } else if (getState() == STATE.EXPLODING){
            if(areaOfEffect != null && explosionTextureRegion != null){
                batch.draw(explosionTextureRegion, areaOfEffect.getX(), areaOfEffect.getY(), areaOfEffect.getWidth(), areaOfEffect.getHeight());
            } else {
                if(explosionTextureRegion != null) {
                    batch.draw(explosionTextureRegion, sprite.getX(), sprite.getY(), sprite.getWidth(), sprite.getHeight());
                }
            }
        }

    }

    /**
     * Colors the bullet based on the direction it can be dispelled (This will be tweaked) //TODO look back
     * @param batch
     */
    public void alive_draw(SpriteBatch batch){
        Color temp = this.getSprite().getColor();
        switch(dispellable.getDispel()){
            case VERTICAL: this.getSprite().setColor(Color.toFloatBits(255, 43, 43,255));
                break;
            case HORIZONTAL: this.getSprite().setColor(Color.toFloatBits(0, 255, 199,255));
                break;
            default: this.getSprite().setColor(Color.WHITE);
        }

        this.getSprite().draw(batch);
        this.getSprite().setColor(temp);


    }

    public Sprite getSprite() {
        return sprite;
    }

    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
    }


    public void setSpriteRegion(TextureRegion r){
        sprite.setRegion(r);
    }

    public float getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public Projectile.STATE getState() {
        return state;
    }

    public void setState(Projectile.STATE state) {
        this.state = state;
    }

    public Rectangle getAreaOfEffect() {
        return areaOfEffect;
    }

    public void setAreaOfEffect(Rectangle areaOfEffect) {
        this.areaOfEffect = areaOfEffect;
    }



}
