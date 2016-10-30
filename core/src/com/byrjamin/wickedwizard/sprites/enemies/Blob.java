package com.byrjamin.wickedwizard.sprites.enemies;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.byrjamin.wickedwizard.MainGame;
import com.byrjamin.wickedwizard.screens.PlayScreen;

/**
 * Created by Home on 28/10/2016.
 */
public class Blob extends Enemy {

    private int HEIGHT = MainGame.GAME_UNITS * 10;
    private int WIDTH = MainGame.GAME_UNITS * 10;


    public Blob(int posX, int posY) {
        super(posX, posY);
        Sprite sprite = PlayScreen.atlas.createSprite("wiz");
        sprite.setSize((float) HEIGHT, (float) WIDTH);
        sprite.flip(true, false);
        sprite.setPosition(posX, posY);
        this.setSprite(sprite);

        this.setHealth(10);
    }

    @Override
    public void draw(SpriteBatch batch) {
        if(this.getHealth() <= 0){
            return;
        }
        this.getSprite().draw(batch);
    }



}
