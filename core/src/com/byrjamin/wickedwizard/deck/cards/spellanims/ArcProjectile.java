package com.byrjamin.wickedwizard.deck.cards.spellanims;

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
    private float HORIZONTAL_VELOCITY = 30f;

    private Vector2 velocity;



    public ArcProjectile(float x1, float y1, float x2, float y2, String s) {
        super(x1, y1, x2, y2, s);
        velocity = new Vector2(0,(float) (VERTICAL_VELOCITY * Math.sin(projectAngle)));
    }

    @Override
    public void aliveUpdate(float dt, Array<Enemy> e){

        if(getSprite().getX() > MainGame.GAME_WIDTH || getSprite().getX() < 0
                || getSprite().getY() < 0){
            this.setSTATE(Projectile.DEAD);
        }

        //TODO if bullet hits the ground it shoudl run it's death animation
        if(getSprite().getY() < PlayScreen.GROUND_Y){
            this.setSTATE(Projectile.EXPLODING);
        }

        velocity.add(0, GRAVITY);

        this.getSprite().setX(this.getSprite().getX() + (float) (HORIZONTAL_VELOCITY * Math.cos(projectAngle)));
        this.getSprite().setY(this.getSprite().getY() + velocity.y);

       // this.getSprite().translateY(GRAVITY);

        singleTargetProjectileDamageCheck(e);

    }

}
