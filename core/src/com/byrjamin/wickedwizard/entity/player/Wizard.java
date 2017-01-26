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
import com.byrjamin.wickedwizard.helper.timer.StateTimer;
import com.byrjamin.wickedwizard.item.ItemPresets;
import com.byrjamin.wickedwizard.maps.rooms.Room;
import com.byrjamin.wickedwizard.helper.AnimationPacker;
import com.byrjamin.wickedwizard.helper.BoundsDrawer;
import com.byrjamin.wickedwizard.helper.Measure;
import com.byrjamin.wickedwizard.spelltypes.Projectile;
import com.byrjamin.wickedwizard.helper.timer.Reloader;
import com.byrjamin.wickedwizard.screens.PlayScreen;

/**
 * Created by Home on 23/10/2016.
 */
public class Wizard extends Entity{

    public float HEIGHT = Measure.units(6);
    public float WIDTH = Measure.units(6);
    private float MOVEMENT = Measure.units(125f);
    private float DRAG = Measure.units(20f);

    private float GRAPPLE_MOVEMENT = Measure.units(10f);
    private float MAX_GRAPPLE_LAUNCH = Measure.units(50f);
    private float MAX_GRAPPLE_MOVEMENT = Measure.units(150f);
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
    private boolean fallThrough = false;

    private float moveTarget;

    private float yFlyTarget;
    private float xFlyTarget;

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

    private StateTimer fallthroughTimer = new StateTimer(0.05f);

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

        fallthroughTimer.update(dt);
        if(fallthroughTimer.isFinished()){
            fallThrough = false;
        }

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
        //boundsUpdate();
        //System.out.println(velocity.x * dt);
        //System.out.println(position.x + " BEFORE");

        position.add(velocity.x * dt, velocity.y * dt);
        //System.out.println(position.x + " AFTER");
        bounds.y = position.y;
        bounds.x = position.x;
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

    public void positionUpdate(){
        position.x = bounds.x;
        position.y = bounds.y;
    }

    public DIRECTION getDirection() {
        return direction;
    }

    public void dash(float dashTarget) {
        if(!isDashing()) {
            movementState = MOVESTATE.DASHING;
            currentAnimation = dashAnimation;
            this.xFlyTarget = dashTarget;
            direction = xFlyTarget <= getCenterX() ? DIRECTION.LEFT : DIRECTION.RIGHT;
        }
    }

    public void flyTo(float x, float y) {
        float angle = calculateAngle(getCenterX(), getCenterY(), x, y);
        xFlyTarget = x;
        yFlyTarget = y;

        direction = xFlyTarget > getX() ? DIRECTION.RIGHT : DIRECTION.LEFT;

        velocity.x = velocity.x > 0 ? MAX_GRAPPLE_LAUNCH / 2 : -MAX_GRAPPLE_LAUNCH / 2;

        fallThrough = true;
        fallthroughTimer.reset();

        flyVelocity = new Vector2((float) Math.cos(angle) * GRAPPLE_MOVEMENT, (float) Math.sin(angle) * GRAPPLE_MOVEMENT);
        velocity = new Vector2();

        movementState = MOVESTATE.FLYING;
    }

    public void cancelMovement(){
        movementState = MOVESTATE.MOVING;
    }

    //TODO can be refactored for sure.
    public void movementUpdate(float dt){

        if(direction == DIRECTION.LEFT) {
            if (xFlyTarget <= getCenterX()) {
                velocity.x = (-MOVEMENT);
            } else {
                this.setCenterX(xFlyTarget);
                velocity.x = 0;
                movementState = MOVESTATE.STANDING;
            }
        }

        if(direction == DIRECTION.RIGHT) {
            if (xFlyTarget >= getCenterX()) {
                velocity.x = (MOVEMENT);
            } else {
                this.setCenterX(xFlyTarget);
                velocity.x = 0;
                movementState = MOVESTATE.STANDING;
            }
        }
    }

    public void flyUpdate(float dt){

        if(Math.abs(velocity.x) < MAX_GRAPPLE_MOVEMENT && Math.abs(velocity.y) < MAX_GRAPPLE_MOVEMENT) {
            velocity.add(flyVelocity);
        }

        if(bounds.contains(xFlyTarget, yFlyTarget)){
            velocity.x = 0;

            if(velocity.y > MAX_GRAPPLE_LAUNCH) {
                velocity.y = MAX_GRAPPLE_LAUNCH;
            }

            movementState = MOVESTATE.STANDING;

        }

        fallThrough = true;

    }

    public Rectangle mockUpdate(float dt) {
        Rectangle mock = new Rectangle(bounds);
        mock.x = position.x + velocity.x * dt;
        mock.y = position.y + velocity.y * dt;
        //System.out.println(velocity.x);

        return mock;
    }

    public void reduceHealth(float i){
        if(!isDashing() && !isInvunerable() && !isFlying()) {//TODO so op.
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

    public void cancelMovementRetainVerticalSpeed(){
        movementState = MOVESTATE.STANDING;

        if(velocity.y > MAX_GRAPPLE_LAUNCH) {
            velocity.y = MAX_GRAPPLE_LAUNCH;
        }
        velocity.x = 0;
    }

    public void cancelMovementHorizontalSpeed(){
        movementState = MOVESTATE.STANDING;

        if(Math.abs(velocity.x) > MAX_GRAPPLE_LAUNCH / 2) {
            velocity.x = velocity.x > 0 ? MAX_GRAPPLE_LAUNCH / 2 : -MAX_GRAPPLE_LAUNCH / 2;
        }

        if(velocity.y > MAX_GRAPPLE_LAUNCH) {
            velocity.y = MAX_GRAPPLE_LAUNCH;
        }
    }

    public void cancelMovementRetainHorizontalSpeed(){
        movementState = MOVESTATE.STANDING;

        if(Math.abs(velocity.x) > MAX_GRAPPLE_LAUNCH / 2) {
            velocity.x = velocity.x > 0 ? MAX_GRAPPLE_LAUNCH / 2 : -MAX_GRAPPLE_LAUNCH / 2;
        }

        System.out.println(velocity.x);
    }

    public void stopMovement(){
        movementState = MOVESTATE.STANDING;
        velocity.setZero();System.out.println(velocity.x);
    }


   public void applyGravity(float dt){
           velocity.add(gravity);
    }

    public void resetGravity(){
        velocity.y = 0;
        isFalling = false;

        if(movementState == MOVESTATE.STANDING){
            velocity.x = 0;
        }
    }

    public void fall(){
        if(!isFlying()) {
            isFalling = true;
            fallThrough = false;
        }
    }

    public void land(){
        isFalling = false;
        velocity.y = 0;
        if(movementState == MOVESTATE.FLYING){
            velocity.setZero();
            flyVelocity.setZero();
        }
        movementState = MOVESTATE.STANDING;
    }


    public void toggleFallthroughOn(){
        fallThrough = true;
        fallthroughTimer.reset();
    }

    public void toggleFallthroughOff(){
        fallThrough = false;
    }

    public boolean isFallThrough() {
        return fallThrough;
    }

    public void fireProjectile(float input_x, float input_y){
        startfireAnimation();

        float angle = calculateAngle(getCenterX(), getCenterY(), input_x,input_y);

        if(angle >= 0) direction = (angle <= (Math.PI / 2)) ? DIRECTION.RIGHT : DIRECTION.LEFT;
         else direction = (angle >= -(Math.PI / 2)) ? DIRECTION.RIGHT : DIRECTION.LEFT;

        activeBullets.addProjectile(new Projectile.ProjectileBuilder(getCenterX() , getCenterY(), input_x,input_y)
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

    public void setVerticalVelocity(float y){
        velocity.y = y;
    };


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
