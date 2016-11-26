package com.byrjamin.wickedwizard.sprites.enemies;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.byrjamin.wickedwizard.MainGame;
import com.byrjamin.wickedwizard.deck.cards.spellanims.EnemyBullets;
import com.byrjamin.wickedwizard.deck.cards.spellanims.Projectile;
import com.byrjamin.wickedwizard.screens.PlayScreen;
import com.byrjamin.wickedwizard.sprites.Wizard;

/**
 * Created by Home on 20/11/2016.
 */
public class Turret extends Enemy{

    private enum movement {
        WALKING, ATTACKING
    }

    private float MOVEMENT = MainGame.GAME_UNITS * 30;

    private float reloadTimer;

    private float fireRate = 1f;

    private Projectile projectile;

    private Array<Projectile> activeBullets;


    public Turret(float posX, float posY){
        super(posX, posY);
        this.setHealth(10);
        this.getSprite().setRegion(PlayScreen.atlas.findRegion("blob_0"));
        this.getSprite().setSize(MainGame.GAME_UNITS * 10, MainGame.GAME_UNITS * 10);
        this.getSprite().setCenter(posX, posY);

        Array<TextureRegion> dyingAnimationAr = new Array<TextureRegion>();

        dyingAnimationAr.add(PlayScreen.atlas.findRegion("blob_dying00"));
        dyingAnimationAr.add(PlayScreen.atlas.findRegion("blob_dying01"));
        dyingAnimationAr.add(PlayScreen.atlas.findRegion("blob_dying02"));
        dyingAnimationAr.add(PlayScreen.atlas.findRegion("blob_dying03"));
        dyingAnimationAr.add(PlayScreen.atlas.findRegion("blob_dying04"));
        dyingAnimationAr.add(PlayScreen.atlas.findRegion("blob_dying05"));
        dyingAnimationAr.add(PlayScreen.atlas.findRegion("blob_dying06"));
        dyingAnimationAr.add(PlayScreen.atlas.findRegion("blob_dying07"));
        dyingAnimationAr.add(PlayScreen.atlas.findRegion("blob_dying08"));
        dyingAnimationAr.add(PlayScreen.atlas.findRegion("blob_dying09"));

        this.setDyingAnimation(new Animation(0.05f / 1f, dyingAnimationAr));

        reloadTimer = 3.0f;


    }



    @Override
    public void update(float dt) {




    }

    @Override
    public void update(float dt, Wizard wizard) {
        flashTimer(dt);
        updateMovement(dt);
        if(this.getState() == STATE.DYING){
            dyingUpdate(dt);
        }
        fire(dt,wizard);
    }


    public void updateMovement(float dt){
        if(this.getSprite().getX() + this.getSprite().getWidth() > MainGame.GAME_WIDTH){
            MOVEMENT = -MainGame.GAME_UNITS * 10;
        } else if(this.getSprite().getX() <= 0){
            MOVEMENT = MainGame.GAME_UNITS * 10;
        }
        this.getSprite().translateX(MOVEMENT * dt);
    }


    //public void flipX()



    public void fire(float dt, Wizard wizard){
        reloadTimer -= dt;
        if (reloadTimer <= 0) {
            EnemyBullets.activeBullets.add(new Projectile.ProjectileBuilder(this.getSprite().getX(), this.getSprite().getY(), wizard.getSprite().getX(),wizard.getSprite().getY())
                    .spriteString("fire")
                    .damage(1)
                    .build());
            reloadTimer += fireRate;
        }
    }

    @Override
    public void draw(SpriteBatch batch){
        super.draw(batch);
    }

}
