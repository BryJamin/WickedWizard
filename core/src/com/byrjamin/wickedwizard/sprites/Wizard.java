package com.byrjamin.wickedwizard.sprites;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.byrjamin.wickedwizard.MainGame;
import com.byrjamin.wickedwizard.screens.PlayScreen;

/**
 * Created by Home on 23/10/2016.
 */
public class Wizard {

    private int HEIGHT = MainGame.GAME_UNITS * 10;
    private int WIDTH = MainGame.GAME_UNITS * 10;
    private static final int GRAVITY = -MainGame.GAME_UNITS;

    private Vector3 position;

    private Vector3 velocity;

    private int health = 10;



    private Sprite sprite;


    public Wizard() {
        sprite = PlayScreen.atlas.createSprite("wiz");
        sprite.setSize((float) HEIGHT, (float) WIDTH);
        position = new Vector3(300, 400, 0);
        sprite.setPosition(position.x, PlayScreen.GROUND_Y);
    }


    public void update(float deltaTime){

        if(this.getSprite().getY() > PlayScreen.GROUND_Y) {
            this.getSprite().translateY(GRAVITY);
            if (this.getSprite().getY() <= PlayScreen.GROUND_Y) {
                this.getSprite().setY(PlayScreen.GROUND_Y);
            }
        }



    }


    public void teleport(float posX, float posY){
        if(posY > PlayScreen.GROUND_Y) {
            this.getSprite().setCenter(posX, posY);
        }
    }

    public void reduceHealth(int i){
        health -= i;
    }

    public void draw(SpriteBatch batch){
        if(this.getHealth() <= 0){
            return;
        }
        this.getSprite().draw(batch);
    };

    public Vector2 getCenter(){
        return new Vector2(this.getSprite().getX() + WIDTH / 2, this.getSprite().getY() + HEIGHT / 2);
    }

    public Sprite getSprite() {
        return sprite;
    }

    public int getHealth() {
        return health;
    }

    public Vector3 getPosition() {
        return position;
    }
}
