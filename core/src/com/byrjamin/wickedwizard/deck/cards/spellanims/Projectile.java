package com.byrjamin.wickedwizard.deck.cards.spellanims;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.byrjamin.wickedwizard.MainGame;
import com.byrjamin.wickedwizard.screens.PlayScreen;
import com.byrjamin.wickedwizard.sprites.enemies.Enemy;
import com.byrjamin.wickedwizard.sprites.enemies.EnemySpawner;

/**
 * Created by Home on 07/11/2016.
 */
public class Projectile {

    public int STATE;
    public final static int DEAD = 1;
    public final static int EXPLODING = 2;
    public final static int ALIVE = 3;

    double projectAngle;

    private Animation explosion_animation;

    private boolean animationFinished = false;

    float gradient;
    float time;
    Sprite sprite;

    TextureRegion explosion;


    private Vector2 position;
    private Rectangle damageRadius;

    private int damage;

    private float HORIZONTAL_VELOCITY = 20f;


    public Projectile(){
        sprite = PlayScreen.atlas.createSprite("blob_0");
        sprite.setSize((float) MainGame.GAME_UNITS * 5, MainGame.GAME_UNITS * 5);
        sprite.setRotation((float) Math.toDegrees(projectAngle));

        Array<TextureRegion> animation;

        animation = new Array<TextureRegion>();

        // Create an array of TextureRegions
        animation.add(PlayScreen.atlas.findRegion("explosion0"));
        animation.add(PlayScreen.atlas.findRegion("explosion1"));
        animation.add(PlayScreen.atlas.findRegion("explosion2"));
        animation.add(PlayScreen.atlas.findRegion("explosion3"));

        explosion_animation = new Animation(0.07f / 1f, animation);

        time = 0;
    }


    public void projectileSetup(float x1,float y1, float x2, float y2){
        sprite.setCenter(x1, y1);
        calculateAngle(x1,y1,x2,y2);

    }

    public Projectile(float x1,float y1, float x2, float y2, String s, int damage){

        sprite = PlayScreen.atlas.createSprite(s);
        sprite.setSize((float) MainGame.GAME_UNITS * 5, MainGame.GAME_UNITS * 5);
        sprite.setCenter(x1, y1);
        calculateAngle(x1,y1,x2,y2);
        sprite.setRotation((float) Math.toDegrees(projectAngle));

        this.damage = damage;

        Array<TextureRegion> animation;

        animation = new Array<TextureRegion>();

        // Create an array of TextureRegions
        animation.add(PlayScreen.atlas.findRegion("explosion0"));
        animation.add(PlayScreen.atlas.findRegion("explosion1"));
        animation.add(PlayScreen.atlas.findRegion("explosion2"));
        animation.add(PlayScreen.atlas.findRegion("explosion3"));

        explosion_animation = new Animation(0.07f / 1f, animation);

        time = 0;

        STATE = ALIVE;

    }

    public Projectile(float x1,float y1, float x2, float y2, String s, int damage, Rectangle radius){

        sprite = PlayScreen.atlas.createSprite(s);
        sprite.setSize((float) MainGame.GAME_UNITS * 5, MainGame.GAME_UNITS * 5);
        sprite.setCenter(x1, y1);
        calculateAngle(x1,y1,x2,y2);
        sprite.setRotation((float) Math.toDegrees(projectAngle));

        damageRadius = radius;

        this.damage = damage;

        Array<TextureRegion> animation;

        animation = new Array<TextureRegion>();

        // Create an array of TextureRegions
        animation.add(PlayScreen.atlas.findRegion("explosion0"));
        animation.add(PlayScreen.atlas.findRegion("explosion1"));
        animation.add(PlayScreen.atlas.findRegion("explosion2"));
        animation.add(PlayScreen.atlas.findRegion("explosion3"));

        explosion_animation = new Animation(0.07f / 1f, animation);

        time = 0;

        STATE = ALIVE;

    }


//TODO More math T---T


    public void calculateAngle(float x1,float y1, float x2, float y2){
        projectAngle = (Math.atan2(y2 - y1, x2 - x1));
    }

    /**
     * As this projectile isn't effected by gravity it just travels in a straight line using trigonometry
     * @param dt
     */
    public void update(float dt){
        this.getSprite().setX(this.getSprite().getX() + (float) (HORIZONTAL_VELOCITY * Math.cos(projectAngle)));
        this.getSprite().setY(this.getSprite().getY() + (float) (HORIZONTAL_VELOCITY * Math.sin(projectAngle)));
    }

    public void update(float dt, Array<Enemy> e){

        if(STATE == Projectile.ALIVE) {
            aliveUpdate(dt, e);
        } else if(STATE == Projectile.EXPLODING){
            time += dt;
            if(explosion_animation.isAnimationFinished(time)){
                this.setSTATE(Projectile.DEAD);
            }
            explosion = explosion_animation.getKeyFrame(time);
        }

    }


    public void aliveUpdate(float dt, Array<Enemy> e){

        outOfBoundsCheck();
        //TODO if bullet hits the ground it shoudl run it's death animation
        damageCheck(e);
        travelUpdate();

    }


    public void outOfBoundsCheck(){
        if(getSprite().getX() > MainGame.GAME_WIDTH || getSprite().getX() < 0
                || getSprite().getY() > MainGame.GAME_HEIGHT
                || getSprite().getY() < 0){
            this.setSTATE(Projectile.DEAD);
        }
    }

    public void travelUpdate(){
        System.out.println("Old");
        this.getSprite().setX(this.getSprite().getX() + (float) (HORIZONTAL_VELOCITY * Math.cos(projectAngle)));
        this.getSprite().setY(this.getSprite().getY() + (float) (HORIZONTAL_VELOCITY * Math.sin(projectAngle)));
    }

    public void damageCheck(Array<Enemy> e){
        if(getSprite().getY() <= PlayScreen.GROUND_Y){
            this.getSprite().setY(PlayScreen.GROUND_Y);
            if(damageRadius != null){
                explosionHit(getSprite(), getDamageRadius(), e);
            }
            this.setSTATE(Projectile.EXPLODING);
        }

        if(damageRadius == null) {
            singleTargetProjectileDamageCheck(e);
        } else {
            multipleTargetProjectileDamageCheck(e);
        }
    }


    //TODO This effect multiple enemies
    public void singleTargetProjectileDamageCheck(Array<Enemy> e){

        for (Enemy enemy : e) {
            if(getSprite().getBoundingRectangle().overlaps(enemy.getSprite().getBoundingRectangle())){
                enemy.reduceHealth(damage);
                this.setSTATE(EXPLODING);
                return;
            }
        }
    }

    /**
     * For reach enemy that exists check if the bullet hit the enemy,
     * If this is the case check to see if the rectangle hits any other enemies
     * @param e
     */
    public void multipleTargetProjectileDamageCheck(Array<Enemy> e) {

        for (Enemy enemy : e) {
            if(getSprite().getBoundingRectangle().overlaps(enemy.getSprite().getBoundingRectangle())){
                explosionHit(getSprite(), getDamageRadius(), e);
                return;
            }
        }

    }

    public void explosionHit(Sprite bullet, Rectangle explosionRadius, Array<Enemy> e){
        Vector2 temp = new Vector2();
        bullet.getBoundingRectangle().getCenter(temp);
        explosionRadius.setCenter(temp);
        System.out.println(temp.x);
        //damageRadius = new Rectangle(temp.x, temp.y, MainGame.GAME_UNITS * 15, MainGame.GAME_UNITS * 15);
        for(Enemy enemy: e){
            if(explosionRadius.overlaps(enemy.getSprite().getBoundingRectangle())){
                enemy.reduceHealth(damage);
                this.setSTATE(EXPLODING);
            }

        }
    }


    public void draw(SpriteBatch batch){

        if(getSTATE() == Projectile.ALIVE){
            this.getSprite().draw(batch);
        } else if (getSTATE() == Projectile.EXPLODING){
            if(explosion != null){
                batch.draw(explosion, damageRadius.getX(), damageRadius.getY(), damageRadius.getWidth(), damageRadius.getHeight());
            }
        }

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

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public int getSTATE() {
        return STATE;
    }

    public void setSTATE(int STATE) {
        this.STATE = STATE;
    }

    public Rectangle getDamageRadius() {
        return damageRadius;
    }

    public void setDamageRadius(Rectangle damageRadius) {
        this.damageRadius = damageRadius;
    }
}
