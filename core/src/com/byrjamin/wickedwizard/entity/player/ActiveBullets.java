package com.byrjamin.wickedwizard.entity.player;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.byrjamin.wickedwizard.entity.Entity;
import com.byrjamin.wickedwizard.maps.rooms.Room;
import com.byrjamin.wickedwizard.spelltypes.Projectile;

/**
 * Created by Home on 10/11/2016.
 */
public class ActiveBullets {

    private Array<Projectile> activeBullets = new Array<Projectile>();

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
    public void updateProjectile(float dt, Room r, Entity... targets){

        for(Projectile p : activeBullets) {

            p.update(dt);
            outOfBoundsCheck(p, r);

            if(p.getState() == Projectile.STATE.ALIVE) {
                for(Entity e : targets){
                    if(e.isHit(p.getBounds())){
                        e.reduceHealth(p.getDamage());
                        p.setState(Projectile.STATE.EXPLODING);
                    };
                }

            } else if(p.getState() == Projectile.STATE.DEAD){
                activeBullets.removeValue(p, true);
            }

        }
    }


    //TODO just check the Height and Width and then get RoomGround to see if it hits.
    public void outOfBoundsCheck(Projectile p, Room r){
        if(p.getX() > r.WIDTH || p.getX() < 0
                || p.getY() < r.groundHeight() || p.getY() > r.HEIGHT) {
            p.setState(Projectile.STATE.DEAD);
        }
    }

    public void draw(SpriteBatch batch){

        for(Projectile p : activeBullets){
            p.draw(batch);
        }
    }

    public Array<Projectile> getActiveBullets() {
        return activeBullets;
    }
}
