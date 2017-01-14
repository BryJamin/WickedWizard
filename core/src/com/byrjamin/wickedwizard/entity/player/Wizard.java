package com.byrjamin.wickedwizard.entity.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.byrjamin.wickedwizard.MainGame;
import com.byrjamin.wickedwizard.entity.Entity;
import com.byrjamin.wickedwizard.item.ItemPresets;
import com.byrjamin.wickedwizard.maps.rooms.Room;
import com.byrjamin.wickedwizard.helper.AnimationPacker;
import com.byrjamin.wickedwizard.helper.BoundsDrawer;
import com.byrjamin.wickedwizard.helper.Measure;
import com.byrjamin.wickedwizard.spelltypes.Projectile;
import com.byrjamin.wickedwizard.helper.Reloader;
import com.byrjamin.wickedwizard.screens.PlayScreen;

/**
 * Created by Home on 23/10/2016.
 */
public class Wizard extends Entity{

    public float HEIGHT = Measure.units(6);
    public float WIDTH = Measure.units(6);
    private float MOVEMENT = Measure.units(200f);
    private static final int GRAVITY = -MainGame.GAME_UNITS;

    private float invinciblityFrames = 1.0f;
    private float invinciblityTimer;
    private float blinkTimer;
    private boolean isInvisible;

    private Vector2 position;
    private Vector2 velocity = new Vector2(0, 0);
    private Vector2 gravity = new Vector2(0, -50f);

    private Vector3 input = new Vector3();

    private Rectangle bounds;

    private com.byrjamin.wickedwizard.entity.player.ActiveBullets activeBullets = new com.byrjamin.wickedwizard.entity.player.ActiveBullets();

    private Reloader reloader;

    private boolean isFalling = true;

    private float dashTarget;

    public enum STATE {
        CHARGING, FIRING, STANDING
    }

    private enum DIRECTION {
        LEFT, RIGHT, UP, DOWN
    }

    private STATE currentState = STATE.STANDING;

    private DIRECTION direction = DIRECTION.RIGHT;

    private boolean dashing = false;

    //STATS
    private int health = 3;
    private int armor;
    private float damage = 1;
    private float reloadRate = 0.3f;
    //private float windUp = 0.5f;
    private float windUpAnimationTime = 0.0f;

    private float animationTime;
    private float chargeTime;

    private Animation standingAnimation;
    private Animation firingAnimation;
    private Animation windUpAnimation;
    private Animation dashAnimation;
    private Animation currentAnimation;

    private TextureRegion currentFrame;


    private Array<ItemPresets> items = new Array<ItemPresets>();


    public Wizard(float posX, float posY) {

        currentFrame = PlayScreen.atlas.findRegion("squ_walk");
       // sprite.setPosition(200, 400);

        position = new Vector2(posX, posY);
        velocity = new Vector2();
        bounds = new Rectangle(posX,posY,WIDTH, HEIGHT);

        reloader = new Reloader(reloadRate);

        //Note firing animation speed is equal to the reloadRate divided by 10.
        standingAnimation = AnimationPacker.genAnimation(1 / 10f, "squ_walk", Animation.PlayMode.LOOP);
        firingAnimation = AnimationPacker.genAnimation(0.15f / 10, "squ_firing");
        windUpAnimation = AnimationPacker.genAnimation(windUpAnimationTime, "circle");
        dashAnimation = AnimationPacker.genAnimation(0.05f, "squ_dash", Animation.PlayMode.LOOP_REVERSED);

    }


    public void update(float dt, OrthographicCamera gamecam, Room room){
        animationTime += dt;

        if(currentState == STATE.STANDING){
            currentAnimation = standingAnimation;
        }

        if(currentState == STATE.CHARGING){
            //reloader.update(dt);
            chargeTime += dt;
            if(windUpAnimation.isAnimationFinished(chargeTime)){
                currentState = STATE.FIRING;
            }
        }

        if(currentState == STATE.FIRING){
            reloader.update(dt);
           // stateTime+= dt;
            if(reloader.isReady()){
                float x1 = Gdx.input.getX();
                float y1 = Gdx.input.getY();
                input = new Vector3(x1, y1, 0);
                //This is so inputs match up to the game co-ordinates.
                gamecam.unproject(input);
                fireProjectile(input.x, input.y);
                //reloader.update(dt);
            }
        }



        if(dashing){
            currentAnimation = dashAnimation;
            dashing = dashUpdate(dt);
        }

        activeBullets.updateProjectile(dt, room, (Entity[]) room.getEnemies().toArray(Entity.class));

        applyGravity(dt, room);

        currentFrame = currentAnimation.getKeyFrame(animationTime);

        damageFramesUpdate(dt);
        boundsUpdate();

        //System.out.println("STATE IS: " + currentState);

    }

    public void draw(SpriteBatch batch){
        if(this.getHealth() <= 0){
            return;
        }

        if(!isInvisible) {
            boolean flip = (getDirection() == DIRECTION.LEFT);
            batch.draw(currentFrame, flip ? position.x + WIDTH + Measure.units(0.5f) : position.x - Measure.units(0.5f), position.y, flip ? - (WIDTH + WIDTH / 6) : WIDTH + WIDTH / 6, HEIGHT + HEIGHT / 6);
        }

        if(currentState == STATE.CHARGING) {
            batch.draw(windUpAnimation.getKeyFrame(chargeTime), getCenterX() - 250, getCenterY() - 270, 500, 500);
        }

        activeBullets.draw(batch);
        BoundsDrawer.drawBounds(batch, bounds);
    }

    private void boundsUpdate(){
        bounds.x = position.x;
        bounds.y = position.y;
    }

    public DIRECTION getDirection() {
        return direction;
    }

    public void moveRight(float dt){
        position.x = position.x + MOVEMENT * dt;
        direction = DIRECTION.RIGHT;
    }

    public void moveLeft(float dt){
        position.x = position.x - MOVEMENT * dt;
        direction = DIRECTION.LEFT;
    }

    public void moveDown(float dt){
        position.y = position.y - MOVEMENT * dt;
        direction = DIRECTION.RIGHT;
    }

    public void moveUp(float dt){
        position.y = position.y + MOVEMENT * dt;
        direction = DIRECTION.RIGHT;
    }

    public void moveTo(){

    }

    public void dash(float dashTarget) {
        if(!dashing && !getFiringAnimation()) {
            dashing = true;
            currentAnimation = dashAnimation;
            this.dashTarget = dashTarget;
        }
    }

    //TODO can be refactored for sure.
    public boolean dashUpdate(float dt){
        if(dashTarget <= getCenterX()){
            direction = DIRECTION.LEFT;
            position.x = position.x - MOVEMENT * dt;
            boundsUpdate();
            if(this.getCenterX() <= dashTarget){
                this.setCenterX(dashTarget);
                return false;
            }
        }

        if(dashTarget >= getCenterX()){
            direction = DIRECTION.RIGHT;
            position.x = position.x + MOVEMENT * dt;
            boundsUpdate();
            if(this.getCenterX() >= dashTarget){
                this.setCenterX(dashTarget);
                return false;
            }
        }
        return true;
    }

    public void reduceHealth(float i){
        if(!dashing && !isInvunerable()) {
            invinciblityTimer = invinciblityFrames;
            health -= i;
        }
    }

    @Override
    public boolean isHit(Rectangle r) {
        return r.overlaps(bounds);
    }


    public boolean isInvunerable(){
        return invinciblityTimer > 0;
    }

    public void damageFramesUpdate(float dt){

        if(invinciblityTimer >= 0) {
            invinciblityTimer -= dt;
            blinkTimer -= dt;
            if (blinkTimer < 0) {
                blinkTimer = 0.1f;
                isInvisible = !isInvisible;
            }
        } else {
            isInvisible = false;
        }
    }

    public void cancelDash(){
        dashing = false;
    }


   public void applyGravity(float dt, Room room){

       if(room.state != Room.STATE.EXIT) {

           if (isFalling) {
               velocity.add(gravity);
               position.add(velocity.x * dt, velocity.y * dt);
               bounds.y = position.y;
           }


           Rectangle r = room.getOverlappingRectangle(bounds);
           if (r != null) {

               isFalling = false;
               velocity = new Vector2();
               position.y = r.getY() + r.getHeight();
               bounds.y = r.getY() + r.getHeight();
           } else {
               isFalling = true;
           }

       }
    }

    public void fireProjectile(float input_x, float input_y){
        startfireAnimation();

        //This only really matters if I make it so the bullets are drawn in front so they don't appear from
        //the middle of the character. If to decide to just draw from the back I can remove this piece of code
        //angle bits.
        float angle = calculateAngle(getCenterX(), getCenterY(), input_x,input_y);

        if(angle >= 0){
            if(angle <= (Math.PI / 2)) direction = DIRECTION.RIGHT;
            else direction = DIRECTION.LEFT;
        } else {
            if(angle >= -(Math.PI / 2)) direction = DIRECTION.RIGHT;
            else direction = DIRECTION.LEFT;
        }

        float x1 = (getCenterX()) + ((WIDTH / 2) * (float) Math.cos(angle)); //+ (WIDTH / 2)) * (float) Math.cos(angle);
        float y1 = (getCenterY()) + ((WIDTH / 2) * (float) Math.sin(angle));; //+ (HEIGHT / 2)) * (float) Math.sin(angle);

        activeBullets.addProjectile(new Projectile.ProjectileBuilder(x1 , y1, input_x,input_y)
                .damage(1)
                .drawingColor(Color.WHITE)
                .speed(Measure.units(150f))
                .build());
    }

    private void startfireAnimation() {
        currentAnimation = firingAnimation;
        animationTime = 0;
    }

    public float calculateAngle(float x1,float y1, float x2, float y2){
        return (float) (Math.atan2(y2 - y1, x2 - x1));
    }

    public void startFiring() {
            currentState = STATE.FIRING;
            currentAnimation = firingAnimation;
            animationTime = 0;
            chargeTime = 0;
    }


    public void stopFiring() {
        if(currentState == STATE.FIRING || currentState == STATE.CHARGING){
            currentState = STATE.STANDING;
            chargeTime = 0;
        }
    }


    public int getHealth() {
        return health;
    }

    public void setX(float x){
        position.x = x;
    }

    public float getX() {
        return position.x;
    }

    public void setY(float y){
        position.y = y;
    }

    public float getY() {
        return position.y;
    }


    public boolean isFalling() {
        return isFalling;
    }

    public Rectangle getBounds(){
        return bounds;
    }

    public void setCenterX(float posX){
        position.x = posX - WIDTH / 2;
    }

    public float getCenterX(){
        return position.x + WIDTH /2;
    }

    public void setCenterY(float posY){
        position.x = posY - HEIGHT / 2;
    }

    public float getCenterY(){
        return position.y + HEIGHT /2;
    }

    public void increaseDamage(float d){
        damage += d;
    }

    public void increaseHealth(int h){
        health += h;
    }

    public void addItem(ItemPresets i){
        items.add(i);
    }

    public boolean isCharing(){
        return currentState == STATE.CHARGING;
    }

    public boolean isDashing(){
        return dashing;
    }

    public boolean getFiringAnimation(){
        return currentState == STATE.FIRING;
    }

    public STATE getCurrentState() {
        return currentState;
    }

    public void setCurrentState(STATE currentState) {
        this.currentState = currentState;
    }

}
