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
import com.byrjamin.wickedwizard.entity.*;
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
    private float MOVEMENT = Measure.units(150f);
    private float GRAPPLE_MOVEMENT = Measure.units(15f);
    private float MAX_GRAPPLE_LAUNCH = Measure.units(50f);
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

    private com.byrjamin.wickedwizard.entity.ActiveBullets activeBullets = new com.byrjamin.wickedwizard.entity.ActiveBullets();

    private Reloader reloader;

    private boolean isFalling = true;
    private boolean fallThrough = true;

    private float moveTarget;

    private float yTarget;
    private float xTarget;

    private Vector2 flyVelocity = new Vector2();

    public boolean isDead() {
        return health <= 0;
    }


    public enum STATE {
        CHARGING, FIRING, IDLE
    }

    private boolean controlScheme = true;

    public enum MOVESTATE {
        DASHING, MOVING, STANDING, FLYING
    }

    private enum DIRECTION {
        LEFT, RIGHT, UP, DOWN
    }

    private STATE currentState = STATE.IDLE;

    private MOVESTATE movementState = MOVESTATE.STANDING;

    private DIRECTION direction = DIRECTION.RIGHT;

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

    private int inputPoll;


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
        //animationTime += dt;

        if(currentState == STATE.IDLE){
            currentAnimation = standingAnimation;
        }

        if(currentState == STATE.CHARGING){
            //reloader.update(dt);
            chargeTime += dt;
            if(windUpAnimation.isAnimationFinished(chargeTime)){
                currentState = STATE.FIRING;
            }
        }

        //Purely for testing

        if(currentState == STATE.FIRING){
            reloader.update(dt);
                // stateTime+= dt;
            if(reloader.isReady()){
                float x1 = Gdx.input.getX(inputPoll);
                float y1 = Gdx.input.getY(inputPoll);
                input = new Vector3(x1, y1, 0);
                //This is so inputs match up to the game co-ordinates.
                gamecam.unproject(input);
                fireProjectile(input.x, input.y);
                //reloader.update(dt);
            }
        }



        if(movementState != MOVESTATE.STANDING && movementState != MOVESTATE.FLYING){
            if(isDashing()) {
                currentAnimation = dashAnimation;
            }
            movementUpdate(dt);
        }

        if(movementState == MOVESTATE.FLYING){
            flyUpdate(dt);
        }

        activeBullets.updateProjectile(dt, room, (Entity[]) room.getEnemies().toArray(Entity.class));

        if(!isFlying()) {
            applyGravity(dt);
        }

        currentFrame = currentAnimation.getKeyFrame(animationTime+=dt);

        damageFramesUpdate(dt);
        boundsUpdate();
    }

    public void switchControlScheme(){
        controlScheme = !controlScheme;
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

    public void moveDown(float dt){
        position.y = position.y - MOVEMENT * dt;
        direction = DIRECTION.RIGHT;
    }

    public void moveUp(float dt){
        position.y = position.y + MOVEMENT * dt;
        direction = DIRECTION.RIGHT;
    }

    public void moveTo(float dt){

    }

    public void dash(float dashTarget) {
        if(!isDashing()) {
            movementState = MOVESTATE.DASHING;
            currentAnimation = dashAnimation;
            this.moveTarget = dashTarget;
            direction = moveTarget <= getCenterX() ? DIRECTION.LEFT : DIRECTION.RIGHT;
        }
    }

    /**
     * Sets the next movement location of the wizard, as long as the wizard isn't already moving
     * and the target isn't the same target
     * @param moveTarget - Movement Target
     */
    public void move(float moveTarget) {
        if(movementState != MOVESTATE.MOVING && moveTarget != this.moveTarget) {
            movementState = MOVESTATE.MOVING;
            this.moveTarget = moveTarget;
            direction = moveTarget <= getCenterX() ? DIRECTION.LEFT : DIRECTION.RIGHT;
        }
    }

    public void flyTo(float x, float y) {
        float angle = calculateAngle(getCenterX(), getCenterY(), x, y);
        xTarget = x;
        yTarget = y;

        flyVelocity = new Vector2((float) Math.cos(angle) * GRAPPLE_MOVEMENT, (float) Math.sin(angle) * GRAPPLE_MOVEMENT);
        velocity = new Vector2();

        movementState = MOVESTATE.FLYING;
    }

    public void cancelMovement(){
        movementState = MOVESTATE.MOVING;
    }

    //TODO can be refactored for sure.
    public void movementUpdate(float dt){
        if(moveTarget <= getCenterX()){
            position.x = position.x - MOVEMENT * dt;
            boundsUpdate();

            if(this.getCenterX() <= moveTarget){
                this.setCenterX(moveTarget);
                movementState = MOVESTATE.STANDING;
            }
        }

        if(moveTarget >= getCenterX()){
            position.x = position.x + MOVEMENT * dt;
            boundsUpdate();
            if(this.getCenterX() >= moveTarget){
                this.setCenterX(moveTarget);
                movementState = MOVESTATE.STANDING;
            }
        }
    }

    public void flyUpdate(float dt){
        velocity.add(flyVelocity);
        position.add(velocity.x * dt, velocity.y * dt);
        bounds.y = position.y;

        if(getCenterY() > yTarget){
            velocity.x = 0;

            //max grapple launch speed
            if(velocity.y > MAX_GRAPPLE_LAUNCH) {
                velocity.y = MAX_GRAPPLE_LAUNCH;
            }

            setCenterY(yTarget);
            setCenterX(xTarget);

            movementState = MOVESTATE.STANDING;
        }

    }

    public void reduceHealth(float i){
        if(!isDashing() && !isInvunerable()) {
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
        movementState = MOVESTATE.STANDING;
    }


   public void applyGravity(float dt){
       if (isFalling) {
           velocity.add(gravity);
           position.add(velocity.x * dt, velocity.y * dt);
           bounds.y = position.y;
       }
    }

    public void fall(){
        isFalling = true;
        fallThrough = false;
    }

    public void land(){
        isFalling = false;
        velocity.y = 0;
    }

    public float getMockFallY() {
        System.out.println("MOCK Y = " + (position.y + velocity.y));
        return (position.y + velocity.y);
    }

    public void toggleFallthroughOn(){
        fallThrough = true;
    }

    public void toggleFallthroughOff(){
        fallThrough = false;
    }

    public boolean isFallThrough() {
        return fallThrough;
    }

    public void fireProjectile(float input_x, float input_y){
        startfireAnimation();
        //This only really matters if I make it so the bullets are drawn in front so they don't appear from
        //the middle of the character. If to decide to just draw from the back I can remove this piece of code
        //angle bits.
        float angle = calculateAngle(getCenterX(), getCenterY(), input_x,input_y);

        if(angle >= 0) direction = (angle <= (Math.PI / 2)) ? DIRECTION.RIGHT : DIRECTION.LEFT;
         else direction = (angle >= -(Math.PI / 2)) ? DIRECTION.RIGHT : DIRECTION.LEFT;

        float x1 = (getCenterX()) + ((WIDTH / 2) * (float) Math.cos(angle)); //+ (WIDTH / 2)) * (float) Math.cos(angle);
        float y1 = (getCenterY()) + ((WIDTH / 2) * (float) Math.sin(angle));; //+ (HEIGHT / 2)) * (float) Math.sin(angle);

        activeBullets.addProjectile(new Projectile.ProjectileBuilder(x1 , y1, input_x,input_y)
                .damage(damage)
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

    public void startFiring(int poll) {
        if(!isFiring()) {
            currentState = STATE.FIRING;
            currentAnimation = firingAnimation;
            animationTime = 0;
            chargeTime = 0;
            this.inputPoll = poll;
        }
    }


    public void stopFiring() {
        if(currentState == STATE.FIRING || currentState == STATE.CHARGING){
            currentState = STATE.IDLE;
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

    public int getInputPoll() {
        return inputPoll;
    }

    public boolean isCharing(){
        return currentState == STATE.CHARGING;
    }

    public boolean isDashing(){
        return movementState == MOVESTATE.DASHING;
    }

    public boolean isFiring(){
        return currentState == STATE.FIRING;
    }

    public boolean isFlying(){
        return movementState == MOVESTATE.FLYING;
    }

    public STATE getCurrentState() {
        return currentState;
    }

    public void setCurrentState(STATE currentState) {
        this.currentState = currentState;
    }

}
