package com.byrjamin.wickedwizard.deck.cards.spellanims;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.byrjamin.wickedwizard.MainGame;
import com.byrjamin.wickedwizard.screens.PlayScreen;
import com.byrjamin.wickedwizard.sprites.enemies.EnemySpawner;

/**
 * Created by Home on 07/11/2016.
 */
public class Projectile {

    double projectAngle;

    float gradient;
    Sprite sprite;


    private Vector2 position;

    private float HORIZONTAL_VELOCITY = 20f;

    public Projectile(float x1,float y1, float x2, float y2){

        sprite = PlayScreen.atlas.createSprite("blob_0");
        sprite.setSize((float) MainGame.GAME_UNITS * 5, MainGame.GAME_UNITS * 5);
        sprite.setCenter(x1, y1);
        calculateAngle(x1,y1,x2,y2);
    }


//TODO More math T---T


    public void calculateAngle(float x1,float y1, float x2, float y2){
        projectAngle = (Math.atan2(y2 - y1, x2 - x1));
    }

    /**
     * As this projectile isn't effected by gravity it just travels in a straight line using trigonometry
     * @param dt
     */
    public void update(float dt){
        this.getSprite().setX(this.getSprite().getX() + (float) (HORIZONTAL_VELOCITY * Math.cos(projectAngle)));
        this.getSprite().setY(this.getSprite().getY() + (float) (HORIZONTAL_VELOCITY * Math.sin(projectAngle)));
    }

    public void update(float dt, EnemySpawner e){
        this.getSprite().setX(this.getSprite().getX() + (float) (HORIZONTAL_VELOCITY * Math.cos(projectAngle)));
        this.getSprite().setY(this.getSprite().getY() + (float) (HORIZONTAL_VELOCITY * Math.sin(projectAngle)));
    }

    public void draw(SpriteBatch batch){
        this.getSprite().draw(batch);
    }

    public Sprite getSprite() {
        return sprite;
    }

    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
    }
}
