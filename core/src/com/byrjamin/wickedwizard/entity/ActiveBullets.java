package com.byrjamin.wickedwizard.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.byrjamin.wickedwizard.entity.Entity;
import com.byrjamin.wickedwizard.maps.rooms.Room;
import com.byrjamin.wickedwizard.maps.rooms.components.RoomDoor;
import com.byrjamin.wickedwizard.maps.rooms.components.RoomWall;
import com.byrjamin.wickedwizard.spelltypes.Projectile;

/**
 * Created by Home on 10/11/2016.
 */
public class ActiveBullets {

    private Array<Projectile> bullets = new Array<Projectile>();

    /**
     * Adds a bullet to the active ActiveBullets array
     * @param p - Projectile
     */
    public void addProjectile(Projectile p){
        bullets.add(p);
    }

    /**
     * Checks if the Projectile has changed to the 'DEAD' state. If the projectile is dead
     * it is no longer tracked by this class.
     * @param dt - delta time
     */
    public void updateProjectile(float dt, Room r, Entity... targets){

        for(Projectile p : bullets) {

            p.update(dt);

            if(p.getState() == Projectile.STATE.ALIVE) {
                for(Entity e : targets){
                    if(e.isHit(p.getBounds())){
                        e.reduceHealth(p.getDamage());
                        p.setState(Projectile.STATE.EXPLODING);
                    }
                }
                outOfBoundsCheck(p, r);

            } else if(p.getState() == Projectile.STATE.DEAD){
                bullets.removeValue(p, true);
            }

        }
    }


    //TODO just check the Height and Width and then get RoomGround to see if it hits.
    public void outOfBoundsCheck(Projectile p, Room r){

        for(RoomWall rw : r.getRoomWalls()){
            if(p.getBounds().overlaps(rw.getBounds())) {
                p.setState(Projectile.STATE.EXPLODING);
                return;
            }
        }

        for(RoomDoor rd : r.getRoomDoors()){
            if(p.getBounds().overlaps(rd.getBounds()) && !rd.isUnlocked()) {
                p.setState(Projectile.STATE.EXPLODING);
                return;
            }
        }

        if(p.getX() > r.WIDTH || p.getX() < r.getX()
                || p.getY() < r.getY() || p.getY() > r.HEIGHT) {
            p.setState(Projectile.STATE.EXPLODING);
        }
    }

    public void draw(SpriteBatch batch){
        for(Projectile p : bullets){
            p.draw(batch);
        }
    }

    public Array<Projectile> getBullets() {
        return bullets;
    }
}
