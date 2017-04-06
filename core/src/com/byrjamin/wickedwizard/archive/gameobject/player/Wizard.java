package com.byrjamin.wickedwizard.archive.gameobject.player;

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
import com.byrjamin.wickedwizard.utils.timer.StateTimer;
import com.byrjamin.wickedwizard.archive.item.ItemPresets;
import com.byrjamin.wickedwizard.archive.maps.rooms.Room;
import com.byrjamin.wickedwizard.utils.AnimationPacker;
import com.byrjamin.wickedwizard.utils.BoundsDrawer;
import com.byrjamin.wickedwizard.utils.Measure;
import com.byrjamin.wickedwizard.archive.spelltypes.Projectile;
import com.byrjamin.wickedwizard.screens.PlayScreen;

/**
 * Created by Home on 23/10/2016.
 */
public class Wizard extends com.byrjamin.wickedwizard.archive.gameobject.GameObject {

    public float HEIGHT = Measure.units(5);
    public float WIDTH = Measure.units(5);
    private float MOVEMENT = Measure.units(115f);
    private float ACCELERATION = Measure.units(10f);
    private float DRAG = Measure.units(20f);

    private float GRAPPLE_MOVEMENT = Measure.units(15f);
    private float MAX_GRAPPLE_LAUNCH = Measure.units(60f);
    private float MAX_GRAPPLE_MOVEMENT = Measure.units(150f);
    private float JUMP_HEIGHT = Measure.units(3.5f);

    private float invinciblityFrames = 1.0f;
    private float invinciblityTimer;
    private float blinkTimer;
    private boolean isInvisible;

    private Vector2 position;
    private Vector2 velocity = new Vector2(0, 0);
    private Vector2 gravity = new Vector2(0, -Measure.units(3f));

    private int max_airshots = 2;
    private int airshots = 0;

    private Vector3 input = new Vector3();

    private Rectangle bounds;

    private com.byrjamin.wickedwizard.archive.gameobject.ActiveBullets activeBullets = new com.byrjamin.wickedwizard.archive.gameobject.ActiveBullets();

    private StateTimer reloadTimer;

    private boolean isFalling = true;
    private boolean fallThrough = false;
    private boolean bulletHover = true;

    private float yFlyTarget;
    private float xFlyTarget;

    private Vector2 flyVelocity = new Vector2();

    public boolean isDead() {
        return currentState == STATE.DEAD;
    }


    public enum STATE {
        CHARGING, FIRING, IDLE, DEAD
    }

    public enum MOVESTATE {
        DASHING, STANDING, FLYING, JUMPING
    }

    private enum DIRECTION {
        LEFT, RIGHT, UP, DOWN
    }

    private STATE currentState = STATE.IDLE;

    private MOVESTATE movementState = MOVESTATE.STANDING;

    private DIRECTION facingDirection = DIRECTION.RIGHT;
    private DIRECTION directionOfTravel = DIRECTION.RIGHT;

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

    private Animation<TextureRegion> standingAnimation;
    private Animation<TextureRegion> firingAnimation;
    private Animation<TextureRegion> windUpAnimation;
    private Animation<TextureRegion> dashAnimation;
    private Animation<TextureRegion> currentAnimation ;

    private TextureRegion currentFrame;

    private int inputPoll;

    private Array<ItemPresets> items = new Array<ItemPresets>();


    public Wizard(float posX, float posY) {

        currentFrame = PlayScreen.atlas.findRegion("squ_walk");
       // sprite.setPosition(200, 400);

        position = new Vector2(posX, posY);
        velocity = new Vector2();
        bounds = new Rectangle(posX,posY,WIDTH, HEIGHT);
        reloadTimer = new StateTimer(reloadRate, 0);

        //Note firing animation speed is equal to the reloadRate divided by 10.
        standingAnimation = AnimationPacker.genAnimation(1 / 10f, "squ_walk", Animation.PlayMode.LOOP);
        firingAnimation = AnimationPacker.genAnimation(0.15f / 10, "squ_firing");
        windUpAnimation = AnimationPacker.genAnimation(windUpAnimationTime, "circle");
        dashAnimation = AnimationPacker.genAnimation(0.05f, "squ_dash", Animation.PlayMode.LOOP_REVERSED);

    }


    public void update(float dt, OrthographicCamera gamecam, Room room){
        //animationTime += dt;
        if(health <= 0) {
            currentState = STATE.DEAD;
            return;
        }

        reloadTimer.update(dt);

        fallthroughTimer.update(dt);
        if(fallthroughTimer.isFinished()){
            fallThrough = false;
        }

        if(currentState == STATE.IDLE) {
            currentAnimation = standingAnimation;
        }

        if(currentState == STATE.FIRING){
                // stateTime+= dt;
            if(reloadTimer.isFinished()){
                float x1 = Gdx.input.getX(inputPoll);
                float y1 = Gdx.input.getY(inputPoll);
                input = new Vector3(x1, y1, 0);
                //This is so inputs match up to the game co-ordinates.
                gamecam.unproject(input);
                fireProjectile(input.x, input.y);
                reloadTimer.reset();
            }
        }

        if(movementState == MOVESTATE.DASHING){
            currentAnimation = standingAnimation;
            movementUpdate(dt);
        }

        if(movementState == MOVESTATE.FLYING){
            flyUpdate(dt);
        }

        activeBullets.updateProjectile(dt, room, (com.byrjamin.wickedwizard.archive.gameobject.GameObject[]) room.getEnemies().toArray(com.byrjamin.wickedwizard.archive.gameobject.GameObject.class));

        if(!isFlying() && bulletHover) {
            velocity.add(gravity);
        }

        currentFrame = currentAnimation.getKeyFrame(animationTime+=dt);

        damageFramesUpdate(dt);

        position.add(velocity.x * dt, velocity.y * dt);

        bounds.y = position.y;
        bounds.x = position.x;
    }

    public void draw(SpriteBatch batch){
        if(this.getHealth() <= 0){
            return;
        }

        activeBullets.draw(batch);

        if(!isInvisible) {
            boolean flip = (getFacingDirection() == DIRECTION.LEFT);
            batch.draw(currentFrame, flip ? position.x + WIDTH + Measure.units(0.5f) : position.x - Measure.units(0.5f), position.y, flip ? - (WIDTH + WIDTH / 6) : WIDTH + WIDTH / 6, HEIGHT + HEIGHT / 6);
        }

        if(currentState == STATE.CHARGING) {
            batch.draw(windUpAnimation.getKeyFrame(chargeTime), getCenterX() - 250, getCenterY() - 270, 500, 500);
        }

        BoundsDrawer.drawBounds(batch, bounds);
    }

    public void positionUpdate(){
        position.x = bounds.x;
        position.y = bounds.y;
    }

    public DIRECTION getFacingDirection() {
        return facingDirection;
    }

    public void moveTo(float dashTarget) {
        if(!bounds.contains(dashTarget, this.getCenterY())) {
            movementState = MOVESTATE.DASHING;
            currentAnimation = dashAnimation;
            this.xFlyTarget = dashTarget;
            directionOfTravel = xFlyTarget <= getCenterX() ? DIRECTION.LEFT : DIRECTION.RIGHT;
            facingDirection = xFlyTarget <= getCenterX() ? DIRECTION.LEFT : DIRECTION.RIGHT;
        }
    }

    public void flyTo(float x, float y) {
        float angle = calculateAngle(getCenterX(), getCenterY(), x, y);
        xFlyTarget = x;
        yFlyTarget = y;
        resetAirshots();

        facingDirection = xFlyTarget > getX() ? DIRECTION.RIGHT : DIRECTION.LEFT;

        velocity.x = velocity.x > 0 ? MAX_GRAPPLE_LAUNCH / 2 : -MAX_GRAPPLE_LAUNCH / 2;

        fallThrough = true;
        fallthroughTimer.reset();

        flyVelocity = new Vector2((float) Math.cos(angle) * GRAPPLE_MOVEMENT, (float) Math.sin(angle) * GRAPPLE_MOVEMENT);
        velocity = new Vector2();

        movementState = MOVESTATE.FLYING;
    }

    public void resetAirshots(){
        airshots = 0;
    }
    //TODO can be refactored for sure.
    public void movementUpdate(float dt){

        if(directionOfTravel == DIRECTION.LEFT) {
            if (xFlyTarget <= getCenterX()) {
                velocity.x = (-MOVEMENT);
            } else {
                this.setCenterX(xFlyTarget);
                velocity.x = 0;
                movementState = MOVESTATE.STANDING;
            }
        }

        if(directionOfTravel == DIRECTION.RIGHT) {
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
            setCenterX(xFlyTarget);
            setCenterY(yFlyTarget);
            if(velocity.y > MAX_GRAPPLE_LAUNCH) {
                velocity.y = MAX_GRAPPLE_LAUNCH;
            } else if(velocity.y < 0){
                velocity.y = 0;
            }
            movementState = MOVESTATE.STANDING;
        }


        fallThrough = true;

    }

    public Rectangle mockUpdate(float dt) {
        Rectangle mock = new Rectangle(bounds);
        mock.x = position.x + velocity.x * dt;
        mock.y = position.y + velocity.y * dt;
        return mock;
    }

    public void reduceHealth(float i){
        if(!isInvunerable()) {//TODO so op.
            invinciblityTimer = invinciblityFrames;
            health -= i;

            if(health < 0){
                currentState = STATE.DEAD;
            }
        }
    }

    @Override
    public boolean isHit(Rectangle r) {
        return r.overlaps(bounds);
    }

    public void jump(){
        velocity.y = Measure.units(JUMP_HEIGHT);
        velocity.x = velocity.x / 2;
        movementState = MOVESTATE.JUMPING;
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
        velocity.setZero();
    }

    public void resetGravity(){
        if(velocity.y <= 0) {
            velocity.y = 0;
        }
        isFalling = false;

        if(movementState == MOVESTATE.FLYING) {
            movementState = MOVESTATE.STANDING;
        }

        if(movementState != MOVESTATE.DASHING){
            velocity.x = 0;
        }
        resetAirshots();
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

        if(angle >= 0) facingDirection = (angle <= (Math.PI / 2)) ? DIRECTION.RIGHT : DIRECTION.LEFT;
         else facingDirection = (angle >= -(Math.PI / 2)) ? DIRECTION.RIGHT : DIRECTION.LEFT;

        if((Math.toDegrees(angle) < -10 && Math.toDegrees(angle) > -170) && airshots != max_airshots && !isFlying() && !isMovementState(MOVESTATE.JUMPING)) {
            //System.out.println(velocity.y);
            velocity.x = 0;
            velocity.y = 10;
            bulletHover = false;
            airshots++;
            movementState = MOVESTATE.STANDING;
        } else if(airshots == max_airshots) {
            bulletHover = true;
        } else {
            bulletHover = true;
        }

        activeBullets.addProjectile(new Projectile.ProjectileBuilder(getCenterX() , getCenterY(), input_x,input_y)
                .damage(damage)
                .drawingColor(Color.WHITE)
                .speed(Measure.units(150f))
                .scale(0.7f)
              //  .gravity(true)
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

            if(isFlying() || isMovementState(MOVESTATE.JUMPING)){
                movementState = MOVESTATE.STANDING;
                //velocity.x = 0;
                //velocity.y = 0;
            }

        }
    }


    public void stopFiring() {
        if(currentState == STATE.FIRING || currentState == STATE.CHARGING){
            currentState = STATE.IDLE;
            chargeTime = 0;
            bulletHover = true;
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
        position.y = posY - HEIGHT / 2;
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

    public boolean isState(STATE state){
        return state == currentState;
    }

    public boolean isMovementState(MOVESTATE state){
        return state == movementState;
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

    public com.byrjamin.wickedwizard.archive.gameobject.ActiveBullets getActiveBullets() {
        return activeBullets;
    }

    public void setCurrentState(STATE currentState) {
        this.currentState = currentState;
    }

}
