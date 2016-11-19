package com.byrjamin.wickedwizard.sprites.enemies;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.byrjamin.wickedwizard.MainGame;
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
    private Animation current;

    private int MOVEMENT = MainGame.GAME_UNITS * 30;
    private static final int GRAVITY = -7;

    private float time;

    private enum blob_state {
        WALKING, ATTACKING
    }

    private blob_state blob;


    public Blob(int posX, int posY) {
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


        walk = new Animation(0.25f / 1f, walkAnimation, Animation.PlayMode.LOOP);
        attack = new Animation(0.25f / 1f, attackAnimation);

        current = walk;

        time = 0f;

        sprite.setSize((float) HEIGHT, (float) WIDTH);
        sprite.flip(true, true);
        position = new Vector3(posX, posY, 0);
        velocity = new Vector3(0, 50, 0);
        sprite.setPosition(posX, posY);
        this.setSprite(sprite);
        this.setHealth(10);
        this.setBlob_state(blob.WALKING);
    }

    @Override
    public void update(float dt, Wizard wizard) {

        if(wizard.getSprite().getX() > this.getSprite().getX()){
           MOVEMENT = -MainGame.GAME_UNITS * 30;
        } else {
            MOVEMENT = MainGame.GAME_UNITS * 30;
        }


        time += dt;
        //Applying Gravity
        if(this.getSprite().getY() > PlayScreen.GROUND_Y) {
            this.velocity.add(0, GRAVITY, 0);
            this.getSprite().translateY(velocity.y);
            if (this.getSprite().getY() <= PlayScreen.GROUND_Y) {
                this.getSprite().setY(PlayScreen.GROUND_Y);
            }
        }

        //Changing state of blob to walking or attacking
        if(this.getBlob_state() == blob.WALKING){

            getSprite().setX(getSprite().getX() - MOVEMENT * dt);

            if(this.getSprite().getBoundingRectangle().overlaps(wizard.getSprite().getBoundingRectangle())) {
                if(current != attack) {
                    current = attack;
                    time = 0;
                }
                this.setBlob_state(blob.ATTACKING);
            }
        } else {

            if(!this.getSprite().getBoundingRectangle().overlaps(wizard.getSprite().getBoundingRectangle())) {
                if(current != walk) {
                    current = walk;
                    time = 0;
                }
                this.setBlob_state(blob.WALKING);
            }

            if(current.isAnimationFinished(time)){
                wizard.reduceHealth(2);
                System.out.println(wizard.getHealth());
                time = 0;
            }



        }


        //this.getSprite().setPosition(this.getSprite().getPosition().x + (5f * dt), this.getPosition().y);
        this.getSprite().setRegion(current.getKeyFrame(time));
    }

    @Override
    public void update(float dt) {

    }



    public void multX(){
        MOVEMENT = MOVEMENT * -1;
    }


    public void startAttackAnimation(){
        if(current != attack) {
            current = attack;
        }
    }

    public blob_state getBlob_state() {
        return blob;
    }

    public void setBlob_state(blob_state blob_state) {
        this.blob = blob_state;
    }




}
