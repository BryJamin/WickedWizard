package com.byrjamin.wickedwizard.sprites;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
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
        position = new Vector3(100, 400, 0);
        sprite.setPosition(position.x, position.y);
    }


    public void update(float deltaTime){



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
