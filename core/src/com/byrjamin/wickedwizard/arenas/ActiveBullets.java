package com.byrjamin.wickedwizard.arenas;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.byrjamin.wickedwizard.deck.cards.spellanims.Explosion;
import com.byrjamin.wickedwizard.deck.cards.spellanims.Projectile;
import com.byrjamin.wickedwizard.sprites.enemies.Enemy;
import com.byrjamin.wickedwizard.arenas.EnemySpawner;

/**
 * Created by Home on 10/11/2016.
 */
public class ActiveBullets {

    private Array<Projectile> activeBullets;
    private Array<Explosion> activeExplosions;

    private float max_x;
    private float max_y;

    private float min_x;
    private float min_y;

    public ActiveBullets(){
        activeBullets = new Array<Projectile>();
        activeExplosions = new Array<Explosion>();
    }

    /**
     * Adds a bullet to the active ActiveBullets array
     * @param p - Projectile
     */
    public void addProjectile(Projectile p){
        activeBullets.add(p);
    }

    public void addExplosion(Explosion i){
        activeExplosions.add(i);
    }

    public void addExplosion(float posX, float posY){
        activeExplosions.add(new Explosion(posX, posY));
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
        updateExplosions(dt, o, e);
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


    //TODO Relic of the past needs to be removed
    public void updateExplosions(float dt, OrthographicCamera o, Array<Enemy> enemies){
       for(Explosion exp : activeExplosions){
            if(exp.isAnimationFinished()){
                activeExplosions.removeValue(exp, true);
            }
           if(!exp.hasHit()){
               exp.sethasHit(true);
               for (Enemy e : enemies) {
                   if(exp.getSprite().getBoundingRectangle().overlaps(e.getSprite().getBoundingRectangle())){
                       e.reduceHealth(2);
                   }
               }
           }

           exp.update(dt);
       }
    }


    public void draw(SpriteBatch batch){
        for(Projectile p : activeBullets){
            p.draw(batch);
        }

        for(Explosion e : activeExplosions){
            e.draw(batch);
        }
    }

}
