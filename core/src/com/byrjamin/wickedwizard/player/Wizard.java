package com.byrjamin.wickedwizard.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.byrjamin.wickedwizard.MainGame;
import com.byrjamin.wickedwizard.maps.rooms.Room;
import com.byrjamin.wickedwizard.helper.AnimationPacker;
import com.byrjamin.wickedwizard.helper.BoundsDrawer;
import com.byrjamin.wickedwizard.helper.Measure;
import com.byrjamin.wickedwizard.spelltypes.BlastWave;
import com.byrjamin.wickedwizard.spelltypes.Dispellable;
import com.byrjamin.wickedwizard.spelltypes.Projectile;
import com.byrjamin.wickedwizard.helper.Reloader;
import com.byrjamin.wickedwizard.item.Item;
import com.byrjamin.wickedwizard.screens.PlayScreen;

/**
 * Created by Home on 23/10/2016.
 */
public class Wizard {

    public float HEIGHT = Measure.units(7);
    public float WIDTH = Measure.units(7);
    private float MOVEMENT = Measure.units(100f);
    private static final int GRAVITY = -MainGame.GAME_UNITS;

    private Vector2 position;
    private Vector2 velocity = new Vector2(0, 0);
    private Vector2 gravity = new Vector2(0, -50f);

    private Vector3 input = new Vector3();

    private float Acceleration = 20f;

    private Rectangle bounds;

    private ActiveBullets activeBullets = new ActiveBullets();
    private Array<BlastWave> blastWaves = new Array<BlastWave>();

    private Reloader reloader;

    private boolean isFalling = true;

    private enum STATE {
        CHARGING, FIRING, STANDING,AOECIRCLETHING
    }

    private enum DIRECTION {
        LEFT, RIGHT, UP, DOWN
    }

    private DIRECTION direction = DIRECTION.RIGHT;

    private STATE currentState = STATE.STANDING;

    //STATS
    private int health = 3;
    private int armor;
    private float damage = 1;
    private float reloadRate = 0.3f;
    private float windUp = 0.15f;

    private float animationTime;
    private float stateTime;

    private Animation standing;
    private Animation firing;
    private Animation windUpAnimation;
    private Animation currentAnimation;

    private TextureRegion currentFrame;


    private Array<Item> items = new Array<Item>();


    public Wizard(float posX, float posY) {


        currentFrame = PlayScreen.atlas.findRegion("squ_walk");
       // sprite.setPosition(200, 400);

        position = new Vector2(posX, posY);
        velocity = new Vector2();
        bounds = new Rectangle(posX + Measure.units(0.5f),posY,WIDTH - Measure.units(1f), HEIGHT - Measure.units(1));

        reloader = new Reloader(reloadRate, windUp);

        //Note firing animation speed is equal to the reloadRate divided by 10.
        standing = AnimationPacker.genAnimation(1 / 10f, "squ_walk", Animation.PlayMode.LOOP);
        firing = AnimationPacker.genAnimation(reloadRate / 10, "squ_firing");
        windUpAnimation = AnimationPacker.genAnimation(0.03f, "circle");

    }


    public void update(float dt, OrthographicCamera gamecam, Room room){
        animationTime += dt;

        if(currentState == STATE.STANDING){
            currentAnimation = standing;
            //animationTime = 0;
        }

        if(currentState == STATE.CHARGING){
            reloader.update(dt);
            stateTime+= dt;
            if(reloader.isReady()){
                currentState = STATE.FIRING;
            }
        }

        if(currentState == STATE.FIRING){
            reloader.update(dt);
            stateTime+= dt;
            if(reloader.isReady()){
                float x1 = Gdx.input.getX();
                float y1 = Gdx.input.getY();
                input = new Vector3(x1, y1, 0);
                //This is so inputs match up to the game co-ordinates.
                gamecam.unproject(input);
                fireProjectile(input.x, input.y);
            }
        }

        activeBullets.update(dt, gamecam, room.getEnemies());

        for(BlastWave b : blastWaves){
            b.update(dt);
            if(b.outOfBounds(room)){
                blastWaves.removeValue(b, true);
            }
        }

        applyGravity(dt, room);

        currentFrame = currentAnimation.getKeyFrame(animationTime);

        boundsUpdate();
    }

    public void draw(SpriteBatch batch){
        if(this.getHealth() <= 0){
            return;
        }


        boolean flip = (getDirection() == DIRECTION.LEFT);
        batch.draw(currentFrame, flip ? position.x + WIDTH : position.x, position.y, flip ? -WIDTH : WIDTH, HEIGHT);

        if(currentState == STATE.CHARGING) {
            batch.draw(windUpAnimation.getKeyFrame(stateTime), getCenterX() - 250, getCenterY() - 270, 500, 500);
        }

        activeBullets.draw(batch);

        for(BlastWave b : blastWaves){
            b.draw(batch);
        }

        BoundsDrawer.drawBounds(batch, bounds);
    }


/*

    public void teleport(float posX, float posY){
        if(posY > PlayScreen.GROUND_Y) {
            this.getSprite().setCenter(posX, posY);
        }
    }
*/

    private void boundsUpdate(){
        bounds.x = position.x + Measure.units(0.5f);
        bounds.y = position.y;
    }

    public DIRECTION getDirection() {
        return direction;
    }

    public void moveRight(float dt){
        position.x = position.x + MOVEMENT * dt;
        boundsUpdate();
        direction = DIRECTION.RIGHT;
    }

    public void moveLeft(float dt){
        position.x = position.x - MOVEMENT * dt;
        boundsUpdate();
        direction = DIRECTION.LEFT;
    }

    public void reduceHealth(float i){
        health -= i;
    }

    public Vector2 getCenter(){
        return new Vector2(getCenterX(), getCenterY());
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

    public void addItem(Item i){
        items.add(i);
    }


    public void applyItem(Item i){
        this.damage += i.getDamageIncrease();
        this.health += i.getHealthIncrease();
    items.add(i);
}

   public void applyGravity(float dt, Room room){

       if(isFalling) {
           velocity.add(gravity);
           position.add(velocity.x * dt, velocity.y * dt);
           bounds.y = position.y;
       }


       Rectangle r = room.getOverlappingRectangle(bounds);
       if(r != null) {
           isFalling = false;
           velocity = new Vector2();
           position.y = r.getY() + r.getHeight();
           bounds.y = r.getY() + r.getHeight();
       } else {

       }
    }

    public void fireProjectile(float input_x, float input_y){
        startfireAnimation();

        //This only really matters if I make it so the bullets are drawn in front so they don't appear from
        //the middle of the character. If to decide to just draw from the back I can remove this piece of code
        //angle bits.
        float angle = calculateAngle(getCenterX(), getCenterY(), input_x,input_y);

        float x1 = (getCenterX()) + ((WIDTH / 2) * (float) Math.cos(angle)); //+ (WIDTH / 2)) * (float) Math.cos(angle);
        float y1 = (getCenterY()) + ((WIDTH / 2) * (float) Math.sin(angle));; //+ (HEIGHT / 2)) * (float) Math.sin(angle);

        activeBullets.addProjectile(new Projectile.ProjectileBuilder(x1 , y1, input_x,input_y)
                .spriteString("bullet")
                .damage(1)
                .HORIZONTAL_VELOCITY(50f)
                .build());
    }

    private void startfireAnimation() {
        currentAnimation = firing;
        animationTime = 0;
    }

    public float calculateAngle(float x1,float y1, float x2, float y2){
        return (float) (Math.atan2(y2 - y1, x2 - x1));
    }

    public void dispell(Vector3 touchDownInput, Vector3 touchUpInput) {

        float x1 = touchDownInput.x;
        float y1 = touchDownInput.y;

        float x2 = touchUpInput.x;
        float y2 = touchUpInput.y;

        if(Math.abs(x1 - x2) > Measure.units(10) && Math.abs(y1 - y2) < Math.abs(x1 - x2)){
            blastWaves.add(new BlastWave(getCenterX(), getCenterY(), Dispellable.DISPELL.HORIZONTAL));
        } else if((Math.abs(y1 - y2) > Measure.units(10) && Math.abs(x1 - x2) < Math.abs(y1 - y2))) {
            blastWaves.add(new BlastWave(getCenterX(), getCenterY(), Dispellable.DISPELL.VERTICAL));
        }


    }

    public int getHealth() {
        return health;
    }

    public Reloader getReloader() {
        return reloader;
    }

    public void setReloader(Reloader reloader) {
        this.reloader = reloader;
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


    public Rectangle getBounds(){
        return bounds;
    }

    public void startFiring() {
        currentState = STATE.CHARGING;
        currentAnimation = firing;
        animationTime = 0;
        stateTime = 0;
    }


    public void stopFiring() {
        currentState = STATE.STANDING;
        reloader.addWindUp(windUp);
        stateTime = 0;
    }


    public boolean isCharing(){
        return currentState == STATE.CHARGING;
    }

    public STATE getCurrentState() {
        return currentState;
    }

    public void setCurrentState(STATE currentState) {
        this.currentState = currentState;
    }

    public Array<BlastWave> getBlastWaves() {
        return blastWaves;
    }
}
