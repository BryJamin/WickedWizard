package com.byrjamin.wickedwizard.deck.cards.spellanims;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.byrjamin.wickedwizard.MainGame;
import com.byrjamin.wickedwizard.screens.PlayScreen;
import com.byrjamin.wickedwizard.sprites.enemies.Enemy;
import com.byrjamin.wickedwizard.sprites.enemies.EnemySpawner;

/**
 * Created by Home on 10/11/2016.
 */
public class ActiveBullets {

    private Array<Projectile> activeBullets;
    private Array<InstantCast> activeExplosions;

    private float max_x;
    private float max_y;

    private float min_x;
    private float min_y;

    public ActiveBullets(){
        activeBullets = new Array<Projectile>();
    }

    public ActiveBullets(float minimum_x, float minimum_y, float max_x, float max_y){
        activeBullets = new Array<Projectile>();

        min_x = minimum_x;
        min_y = minimum_y;
        this.max_x = max_x;
        this.max_y = max_y;

    }

    public void addProjectile(Projectile p){
        activeBullets.add(p);
    }

    public void addExplosion(InstantCast i){
        activeExplosions.add(i);
    }


    public void addProjectile(float x1,float y1, float x2, float y2){
        activeBullets.add(new Projectile(x1,y1,x2,y2));
    }


    /**
     * Checks if the projectile has either hit an enemy or gone of screen.
     * Either way to destroys the projectile
     * @param dt - delta time
     * @param o - camera
     * @param e - enemyspawner
     */
    public void update(float dt, OrthographicCamera o, EnemySpawner e){
        //TODO If the sprite ever moves instead of the world moving This needs to be changed
        //TODO to use the camera position.
        updateProjectile(dt, o, e);

    }


    public void updateProjectile(float dt, OrthographicCamera o, EnemySpawner e){

        for(Projectile p : activeBullets) {

            //If bullet is offscreen Remove.
            if(p.getSprite().getX() > MainGame.GAME_WIDTH || p.getSprite().getX() < 0
                    || p.getSprite().getY() > MainGame.GAME_HEIGHT
                    || p.getSprite().getY() < 0){
                activeBullets.removeValue(p, true);
            }

            //TODO if bullet hits the ground it shoudl run it's death animation
            if(p.getSprite().getY() < PlayScreen.GROUND_Y){
                activeBullets.removeValue(p, true);
            }

            singleTargetProjectileDamageCheck(p,e);

            p.update(dt);
        }

    }


    public void updateExplosions(float dt, OrthographicCamera o, EnemySpawner e){

    }


    public void draw(SpriteBatch batch){
        for(Projectile p : activeBullets){
            p.draw(batch);
        }
    }


    public void singleTargetProjectileDamageCheck(Projectile p, EnemySpawner enemyspawned){

        for (Enemy e : enemyspawned.getSpawnedEnemies()) {
            if(p.getSprite().getBoundingRectangle().overlaps(e.getSprite().getBoundingRectangle())){
                System.out.println("print something");
                e.reduceHealth(2);
                activeBullets.removeValue(p, true);
            }
        }
    }

    public void multipleTargetProjecTileDamageCheck(EnemySpawner enemyspawned){

        boolean isHit = false;

        for(Projectile p : activeBullets) {
            for (Enemy e : enemyspawned.getSpawnedEnemies()) {
                if(p.getSprite().getBoundingRectangle().overlaps(e.getSprite().getBoundingRectangle())){
                    System.out.println("print something");
                    e.reduceHealth(2);
                    isHit = true;
                }
            }

          //  if(isHit)



        }



    }





}
