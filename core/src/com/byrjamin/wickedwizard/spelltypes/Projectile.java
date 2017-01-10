package com.byrjamin.wickedwizard.spelltypes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.byrjamin.wickedwizard.MainGame;
import com.byrjamin.wickedwizard.helper.AnimationPacker;
import com.byrjamin.wickedwizard.helper.BoundsDrawer;
import com.byrjamin.wickedwizard.maps.rooms.Room;
import com.byrjamin.wickedwizard.screens.PlayScreen;
import com.byrjamin.wickedwizard.staticstrings.TextureStrings;

/**
 * Created by Home on 07/11/2016.
 */
public class Projectile {

    public enum STATE {
        DEAD, EXPLODING, ALIVE
    }

    STATE state;

    double projectAngle;
    float xDistance;
    float yDistance;

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

        //sprite.setBounds();

        areaOfEffect = builder.areaOfEffect;
        time = 0;
        state = STATE.ALIVE;

        explosion_animation = AnimationPacker.genAnimation(0.02f / 1f, TextureStrings.EXPLOSION);

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
        xDistance = (float) (HORIZONTAL_VELOCITY * Math.cos(projectAngle));
        yDistance = (float) (HORIZONTAL_VELOCITY * Math.sin(projectAngle));
    }

    public void update(float dt){
        if(getState() == STATE.ALIVE) {
            travelUpdate();
        } else if(getState() == STATE.EXPLODING){
            time += dt;
            if(explosion_animation.isAnimationFinished(time)){
                this.setState(STATE.DEAD);
            }
            explosionTextureRegion = explosion_animation.getKeyFrame(time);
        }
    }

    public void travelUpdate(){

        float x;
        float y;


        x = this.getSprite().getX() + (this.getSprite().getWidth() / 2) + xDistance;
        //this.getSprite().setX(this.getSprite().getX() + (float) (HORIZONTAL_VELOCITY * Math.cos(projectAngle)));
        if(gravity){
            velocity.add(0, GRAVITY);
            y = this.getSprite().getY() + velocity.y;
            //this.getSprite().setY(this.getSprite().getY() + velocity.y);
        } else {
            y = this.getSprite().getY() + (this.getSprite().getHeight() / 2) + yDistance;
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

    public boolean isGravity() {
        return gravity;
    }

    public Rectangle getAreaOfEffect() {
        return areaOfEffect;
    }

    public void setAreaOfEffect(Rectangle areaOfEffect) {
        this.areaOfEffect = areaOfEffect;
    }



}
