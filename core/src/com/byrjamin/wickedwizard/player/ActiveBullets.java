package com.byrjamin.wickedwizard.player;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.byrjamin.wickedwizard.spelltypes.Projectile;
import com.byrjamin.wickedwizard.enemy.Enemy;

/**
 * Created by Home on 10/11/2016.
 */
public class ActiveBullets {

    private Array<Projectile> activeBullets;
    private Array<Projectile> inActiveBullets;

    public ActiveBullets(){
        activeBullets = new Array<Projectile>();
        inActiveBullets = new Array<Projectile>();
    }

    /**
     * Adds a bullet to the active ActiveBullets array
     * @param p - Projectile
     */
    public void addProjectile(Projectile p){
        activeBullets.add(p);
    }

    /**
     * Checks if the Projectile has changed to the 'DEAD' state. If the projectile is dead
     * it is no longer tracked by this class.
     * @param dt - delta time
     */
    public void updateProjectile(float dt){

        for(Projectile p : activeBullets) {
            if(p.getState() == Projectile.STATE.ALIVE) {
                p.update(dt);
            } else {
                inActiveBullets.add(p);
                activeBullets.removeValue(p, true);
            }
        }

        for(Projectile p : inActiveBullets){
            p.update(dt);
            if(p.getState() == Projectile.STATE.DEAD){
                inActiveBullets.removeValue(p, true);
            }
        }

    }

    public void draw(SpriteBatch batch){
        for(Projectile p : activeBullets){
            p.draw(batch);
        }
        for(Projectile p : inActiveBullets){
            p.draw(batch);
        }
    }

    public Array<Projectile> getActiveBullets() {
        return activeBullets;
    }
}
