package com.byrjamin.wickedwizard.arenas;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.byrjamin.wickedwizard.spelltypes.Projectile;
import com.byrjamin.wickedwizard.enemy.Enemy;

/**
 * Created by Home on 10/11/2016.
 */
public class ActiveBullets {

    private Array<Projectile> activeBullets;

    public ActiveBullets(){
        activeBullets = new Array<Projectile>();
    }

    /**
     * Adds a bullet to the active ActiveBullets array
     * @param p - Projectile
     */
    public void addProjectile(Projectile p){
        activeBullets.add(p);
    }

    /**
     * Checks if the projectile has either hit an enemy or gone of screen.
     * Either way to destroys the projectile
     * @param dt - delta time
     * @param o - camera
     * @param e - enemyspawner
     */
    public void update(float dt, OrthographicCamera o, Array<Enemy> e){
        //TODO If the sprite ever moves instead of the world moving This needs to be changed
        //TODO to use the camera position.
        updateProjectile(dt, o, e);
    }


    /**
     * Checks if the Projectile has changed to the 'DEAD' state. If the projectile is dead
     * it is no longer tracked by this class.
     * @param dt - delta time
     * @param o - camera
     * @param e - Enemy Spawned (could be changed to just the enemy array)
     */
    public void updateProjectile(float dt, OrthographicCamera o, Array<Enemy> e){

        for(Projectile p : activeBullets) {
            if(p.getState() != Projectile.STATE.DEAD) {
                p.update(dt, e);
            } else {
                activeBullets.removeValue(p, true);
            }
        }

    }


    public void draw(SpriteBatch batch){
        for(Projectile p : activeBullets){
            p.draw(batch);
        }
    }

}
