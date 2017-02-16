package com.byrjamin.wickedwizard.entity.enemies;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.byrjamin.wickedwizard.MainGame;
import com.byrjamin.wickedwizard.helper.GravMaster2000;
import com.byrjamin.wickedwizard.maps.rooms.Room;
import com.byrjamin.wickedwizard.helper.AnimationPacker;
import com.byrjamin.wickedwizard.helper.BoundsDrawer;
import com.byrjamin.wickedwizard.helper.Measure;
import com.byrjamin.wickedwizard.assets.TextureStrings;

/**
 * Basic starter enemy, used for testing purposes.
 */
public class Blob extends Enemy {

    private Animation<TextureRegion> walk;
    private Animation<TextureRegion> attack;
    private Animation<TextureRegion> currentAnimation;

    private TextureRegion currentFrame;

    private GravMaster2000 g2000 = new GravMaster2000();

    private Rectangle hitBoz;

    //Optional Parameters
    private float MOVEMENT;
    private float HEIGHT;
    private float scale;
    private float speed;
    private float WIDTH;

    private enum movement {
        WALKING, ATTACKING
    }

    private movement blob;


    public static class BlobBuilder{

        //Required Parameters
        private final float posX;
        private final float posY;

        //Optional Parameters
        private float health = 4;
        private float MOVEMENT = MainGame.GAME_UNITS * 15;
        private float HEIGHT = Measure.units(10);
        private float WIDTH = Measure.units(10);
        private float scale = 1;
        private float speed = 1;


        public BlobBuilder(float posX, float posY) {
            this.posX = posX;
            this.posY = posY;
        }

        public BlobBuilder health(float val)
        { health = val; return this; }

        public BlobBuilder HEIGHT(float val)
        { HEIGHT = val; return this; }

        public BlobBuilder WIDTH(float val)
        { WIDTH = val; return this; }

        public BlobBuilder scale(float val)
        { scale = val; return this; }

        public BlobBuilder speed(float val)
        { speed = val; return this; }

        public BlobBuilder MOVEMENT(float val)
        { MOVEMENT = val; return this; }

        public Blob build() {
            return new Blob(this);
        }


    }





    public Blob(BlobBuilder b) {
        super();

        MOVEMENT = b.MOVEMENT;
        scale = b.scale;
        speed = b.speed;

        HEIGHT = b.HEIGHT * scale;
        WIDTH = b.WIDTH * scale;

        position = new Vector2(b.posX, b.posY);
        hitBoz = new Rectangle(b.posX + (Measure.units(1) * scale), b.posY,
                WIDTH - (Measure.units(2.5f) * scale),
                HEIGHT - (Measure.units(2.5f) * scale));

        bounds.add(hitBoz);

        this.setHealth(b.health);
        this.setBlob_state(movement.WALKING);

        walk = AnimationPacker.genAnimation(0.25f / 1f, TextureStrings.BLOB_STANDING, Animation.PlayMode.LOOP);
        attack = AnimationPacker.genAnimation(0.25f / 1f, TextureStrings.BLOB_ATTACKING);
        this.setDyingAnimation(AnimationPacker.genAnimation(0.05f / 1f, TextureStrings.BLOB_DYING));

        currentAnimation = walk;
        currentFrame = currentAnimation.getKeyFrame(time);

        //this.setDyingAnimation();
    }

    @Override
    public void update(float dt, Room r) {
        super.update(dt, r);
        if(this.getState() == STATE.ALIVE){
            aliveUpdate(dt, r);
        } else if(this.getState() == STATE.DYING){
            dyingUpdate(dt);
        }
        currentFrame = currentAnimation.getKeyFrame(time);
    }


    //TODO The way this blob attacks is slightly incorrect, just in the animation is finished no matter
    //TODO where the wizard is it takes damage.
    //TODO what should happen is that it attack an area infront of it, which I guess can count as a projectile
    public void aliveUpdate(float dt,  Room room){


        //depending on the wizard is flip left or right.

        //TODO this is terrible please fix once you introduced moving wizard


        time += dt;

        //Changing state of blob to walking or attacking
        if(this.getBlob_state() == movement.WALKING ){

            int direction = room.getWizard().getX() > position.x ? -1 : 1;
            position.x = position.x - MOVEMENT * dt * direction * speed;
            hitBoz.x = hitBoz.x  - MOVEMENT * dt * direction * speed;

            if(hitBoz.overlaps(room.getWizard().getBounds())) {
                currentAnimation = attack;
                time = 0;
                this.setBlob_state(movement.ATTACKING);
            }
        } else {

            if(!hitBoz.overlaps(room.getWizard().getBounds())) {
                if(currentAnimation != walk) {
                    currentAnimation = walk;
                    time = 0;
                }
                this.setBlob_state(movement.WALKING);
            }

            if(currentAnimation.isAnimationFinished(time)){
                room.getWizard().reduceHealth(2);
                time = 0;
            }
        }

        //Applying Gravity;
        applyGravity(dt, room);


    }

    public void dyingUpdate(float dt){
        time+=dt;

        currentAnimation = this.getDyingAnimation();

        if(this.getDyingAnimation().isAnimationFinished(time)){
            this.setState(STATE.DEAD);
        }
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
        BoundsDrawer.drawBounds(batch, hitBoz);
    }

    public void applyGravity(float dt, Room room){

        g2000.update(dt, hitBoz, room.getGroundBoundaries());
        position.y = hitBoz.y;

    }

    public void multX(){
        MOVEMENT = MOVEMENT * -1;
    }


    public void startAttackAnimation(){
        if(currentAnimation != attack) {
            currentAnimation = attack;
        }
    }

    public movement getBlob_state() {
        return blob;
    }

    public void setBlob_state(movement blob_state) {
        this.blob = blob_state;
    }




}
