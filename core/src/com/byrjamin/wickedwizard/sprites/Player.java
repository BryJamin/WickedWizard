package com.byrjamin.wickedwizard.sprites;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.byrjamin.wickedwizard.MainGame;
import com.byrjamin.wickedwizard.screens.PlayScreen;

/**
 * Created by Home on 23/10/2016.
 */
public class Player {

    private int HEIGHT = MainGame.GAME_UNITS * 10;
    private int WIDTH = MainGame.GAME_UNITS * 10;

    private Vector3 position;

    private int health = 10;



    private Sprite sprite;


    public Player() {
        sprite = PlayScreen.atlas.createSprite("wiz");
        sprite.setSize((float) HEIGHT, (float) WIDTH);
        position = new Vector3(300, 400, 0);
        sprite.setPosition(position.x, PlayScreen.GROUND_Y);
    }


    public void update(float deltaTime){



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
