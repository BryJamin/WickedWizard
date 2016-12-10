package com.byrjamin.wickedwizard.sprites.enemies;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.byrjamin.wickedwizard.MainGame;
import com.byrjamin.wickedwizard.arenas.Arena;
import com.byrjamin.wickedwizard.screens.PlayScreen;
import com.byrjamin.wickedwizard.sprites.Wizard;

/**
 * Basic starter enemy, used for testing purposes.
 */
public class Blob extends Enemy {

    private int HEIGHT = MainGame.GAME_UNITS * 10;
    private int WIDTH = MainGame.GAME_UNITS * 10;



    private Vector3 position;
    private Vector3 velocity;


    private Animation walk;
    private Animation attack;
    private Animation currentAnimation;

    private boolean isFalling = true;

    private int MOVEMENT = MainGame.GAME_UNITS * 30;
    private static final int GRAVITY = -7;

 //   private float time;

    private enum movement {
        WALKING, ATTACKING
    }

    private movement blob;


    public Blob(float posX, float posY) {
        super(posX, posY);
        Sprite sprite = PlayScreen.atlas.createSprite("blob_0");

        Array<TextureRegion> walkAnimation;

        walkAnimation = new Array<TextureRegion>();

        // Create an array of TextureRegions
        walkAnimation.add(PlayScreen.atlas.findRegion("blob_0"));
        walkAnimation.add(PlayScreen.atlas.findRegion("blob_1"));
        walkAnimation.add(PlayScreen.atlas.findRegion("blob_0"));
        walkAnimation.add(PlayScreen.atlas.findRegion("blob_2"));

        Array<TextureRegion> attackAnimation = new Array<TextureRegion>();

        attackAnimation.add(PlayScreen.atlas.findRegion("blob_0"));
        attackAnimation.add(PlayScreen.atlas.findRegion("blob_2"));
        attackAnimation.add(PlayScreen.atlas.findRegion("blob_2"));
        attackAnimation.add(PlayScreen.atlas.findRegion("blob_2"));
        attackAnimation.add(PlayScreen.atlas.findRegion("blob_2"));
        attackAnimation.add(PlayScreen.atlas.findRegion("blob_1"));


        Array<TextureRegion> dyingAnimationAr = new Array<TextureRegion>();

        dyingAnimationAr.add(PlayScreen.atlas.findRegion("blob_dying00"));
        dyingAnimationAr.add(PlayScreen.atlas.findRegion("blob_dying01"));
        dyingAnimationAr.add(PlayScreen.atlas.findRegion("blob_dying02"));
        dyingAnimationAr.add(PlayScreen.atlas.findRegion("blob_dying03"));
        dyingAnimationAr.add(PlayScreen.atlas.findRegion("blob_dying04"));
        dyingAnimationAr.add(PlayScreen.atlas.findRegion("blob_dying05"));
        dyingAnimationAr.add(PlayScreen.atlas.findRegion("blob_dying06"));
        dyingAnimationAr.add(PlayScreen.atlas.findRegion("blob_dying07"));
        dyingAnimationAr.add(PlayScreen.atlas.findRegion("blob_dying08"));
        dyingAnimationAr.add(PlayScreen.atlas.findRegion("blob_dying09"));

        this.setDyingAnimation(new Animation(0.05f / 1f, dyingAnimationAr));

        walk = new Animation(0.25f / 1f, walkAnimation, Animation.PlayMode.LOOP);
        attack = new Animation(0.25f / 1f, attackAnimation);

        currentAnimation = walk;

        time = 0f;

        sprite.setSize((float) HEIGHT, (float) WIDTH);
        sprite.flip(true, true);
        position = new Vector3(posX, posY, 0);
        velocity = new Vector3(0, 50, 0);
        sprite.setPosition(posX, posY);
        this.setSprite(sprite);
        this.setHealth(4);
        this.setBlob_state(blob.WALKING);
    }

    @Override
    public void update(float dt, Arena arena) {

        flashTimer(dt);

        if(this.getState() == STATE.ALIVE){
            aliveUpdate(dt, arena);
        } else if(this.getState() == STATE.DYING){
            dyingUpdate(dt);
        }


    }

    @Override
    public void update(float dt) {

    }



    //TODO The way this blob attacks is slightly incorrect, just in the animation is finished no matter
    //TODO where the wizard is it takes damage.
    //TODO what should happen is that it attack an area infront of it, which I guess can count as a projectile
    public void aliveUpdate(float dt,  Arena arena){


        if(arena.getWizard().getSprite().getX() > this.getSprite().getX()){
            MOVEMENT = -MainGame.GAME_UNITS * 10;
        } else {
            MOVEMENT = MainGame.GAME_UNITS * 10;
        }


        time += dt;
        //Applying Gravity
        applyGravity(dt, arena);

        //Changing state of blob to walking or attacking
        if(this.getBlob_state() == movement.WALKING ){

            getSprite().setX(getSprite().getX() - MOVEMENT * dt);

            if(this.getSprite().getBoundingRectangle().overlaps(arena.getWizard().getSprite().getBoundingRectangle())) {
                if(currentAnimation != attack) {
                    currentAnimation = attack;
                    time = 0;
                }
                this.setBlob_state(movement.ATTACKING);
            }
        } else {

            if(!this.getSprite().getBoundingRectangle().overlaps(arena.getWizard().getSprite().getBoundingRectangle())) {
                if(currentAnimation != walk) {
                    currentAnimation = walk;
                    time = 0;
                }
                this.setBlob_state(movement.WALKING);
            }

            if(currentAnimation.isAnimationFinished(time)){
                arena.getWizard().reduceHealth(2);
                time = 0;
            }
        }
        //this.getSprite().setPosition(this.getSprite().getPosition().x + (5f * dt), this.getPosition().y);
        this.getSprite().setRegion(currentAnimation.getKeyFrame(time));


    }

    public void applyGravity(float dt, Arena arena){

        if(isFalling){
            Rectangle r = arena.getOverlappingRectangle(this.getSprite().getBoundingRectangle());
            if(r != null) {
                this.getSprite().setY(r.getY() + r.getHeight());
                isFalling = false;
            } else {
                this.velocity.add(0, GRAVITY, 0);
                this.getSprite().translateY(velocity.y);
            }

        }
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
