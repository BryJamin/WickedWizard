package com.byrjamin.wickedwizard.deck.cards.spellanims;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.byrjamin.wickedwizard.MainGame;
import com.byrjamin.wickedwizard.screens.PlayScreen;
import com.byrjamin.wickedwizard.sprites.enemies.Enemy;

import javafx.scene.shape.Arc;

/**
 * This class extends projectile but this fires a projectile that respects Gravity.
 * This could be used for 'Grenade' type spells.
 */
public class ArcProjectile extends  Projectile {

    private float GRAVITY = -1;
    private float VERTICAL_VELOCITY = 40f;
    private float HORIZONTAL_VELOCITY = 40f;

    private Vector2 velocity;

    private ArcProjectile(ProjectileBuilder builder) {
        super(builder);
        velocity = new Vector2(0,(float) (VERTICAL_VELOCITY * Math.sin(projectAngle)));
    }


    /**
     *  This constructor is for projectiles that explode on landing.
     *
     * @param x1 - Starting X- co-ordinate
     * @param y1 - Starting Y - co-ordinate
     * @param x2 - X co-ordinate touched by the user
     * @param y2 - Y co-ordinate touched by the user
     * @param s - String used for the Texture Atlas
     * @param damage - The damage the Projectile deals when it hits the enemy
     * @param r - The radius of the explosion produced by this Projectile
     */
/*    public ArcProjectile(float x1, float y1, float x2, float y2, String s, int damage, Rectangle r) {
        super(x1, y1, x2, y2, s, damage, r);
        velocity = new Vector2(0,(float) (VERTICAL_VELOCITY * Math.sin(projectAngle)));
    }*/

    @Override
    public void outOfBoundsCheck(){
        if(getSprite().getX() > MainGame.GAME_WIDTH || getSprite().getX() < 0
                || getSprite().getY() < 0) {
            this.setState(STATE.DEAD);
        }
    }

    /**
     * Changes the vertical Velocity to have gravity attached to it.
     * Why there are two I don't currently know. //TODO look here please.
     */
    @Override
    public void travelUpdate(){
        velocity.add(0, GRAVITY);
        this.getSprite().setY(this.getSprite().getY() + velocity.y);
        this.getSprite().setX(this.getSprite().getX() + (float) (HORIZONTAL_VELOCITY * Math.cos(projectAngle)));
        this.getSprite().setY(this.getSprite().getY() + velocity.y);
    }



}
