package com.byrjamin.wickedwizard.sprites.enemies;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
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

        activeBullets = new Array<Projectile>();


    }



    @Override
    public void update(float dt) {


    }

    @Override
    public void update(float dt, Wizard wizard) {


        flashTimer(dt);

        if(this.getState() == STATE.DYING){
            dyingUpdate(dt);
        }


        EnemyBullets.activeBullets.add(new Projectile(this.getSprite().getX(), this.getSprite().getY(), wizard.getSprite().getX(),wizard.getSprite().getY(),
                "fire", 1));


        for(Projectile p : activeBullets){
            p.update(dt);
        }


    }


    public void dyingUpdate(float dt){
        time+=dt;
        this.getSprite().setRegion(this.getDyingAnimation().getKeyFrame(time));
        if(this.getDyingAnimation().isAnimationFinished(time)){
            this.setState(STATE.DEAD);
        }
    }

    @Override
    public void draw(SpriteBatch batch){
        super.draw(batch);
        for(Projectile p : activeBullets){
            p.draw(batch);
        }
    }

}
