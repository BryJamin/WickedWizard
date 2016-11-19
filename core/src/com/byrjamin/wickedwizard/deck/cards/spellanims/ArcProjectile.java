package com.byrjamin.wickedwizard.deck.cards.spellanims;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.byrjamin.wickedwizard.MainGame;
import com.byrjamin.wickedwizard.screens.PlayScreen;
import com.byrjamin.wickedwizard.sprites.enemies.Enemy;

/**
 * Created by Home on 17/11/2016.
 */
public class ArcProjectile extends  Projectile {

    private float GRAVITY = -1;
    private float VERTICAL_VELOCITY = 40f;
    private float HORIZONTAL_VELOCITY = 40f;

    private Vector2 velocity;



    public ArcProjectile(float x1, float y1, float x2, float y2, String s, int damage) {
        super(x1, y1, x2, y2, s, damage);
        velocity = new Vector2(0,(float) (VERTICAL_VELOCITY * Math.sin(projectAngle)));
    }

    public ArcProjectile(float x1, float y1, float x2, float y2, String s, int damage, Rectangle r) {
        super(x1, y1, x2, y2, s, damage, r);
        velocity = new Vector2(0,(float) (VERTICAL_VELOCITY * Math.sin(projectAngle)));
    }

    @Override
    public void outOfBoundsCheck(){
        if(getSprite().getX() > MainGame.GAME_WIDTH || getSprite().getX() < 0
                || getSprite().getY() < 0) {
            this.setState(STATE.DEAD);
        }
    }

    @Override
    public void travelUpdate(){
        velocity.add(0, GRAVITY);
        this.getSprite().setY(this.getSprite().getY() + velocity.y);
        this.getSprite().setX(this.getSprite().getX() + (float) (HORIZONTAL_VELOCITY * Math.cos(projectAngle)));
        this.getSprite().setY(this.getSprite().getY() + velocity.y);

    }



}
