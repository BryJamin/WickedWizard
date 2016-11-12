package com.byrjamin.wickedwizard.deck.cards.spellanims;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.byrjamin.wickedwizard.MainGame;
import com.byrjamin.wickedwizard.screens.PlayScreen;

/**
 * Created by Home on 07/11/2016.
 */
public class InstantCast {

    private Animation explosion_animation;
    private Sprite sprite;
    private float time;

    private boolean animationFinished = false;

    private boolean damageFlag = false;

    public InstantCast(float posX, float posY){

        sprite = PlayScreen.atlas.createSprite("explosion0");
        sprite.setSize((float) MainGame.GAME_UNITS * 12, MainGame.GAME_UNITS * 12);
       // sprite.setPosition(posX - sprite.getWidth() / 2, posY - sprite.getHeight() / 2);
        sprite.setCenter(posX, posY);

        Array<TextureRegion> animation;

        animation = new Array<TextureRegion>();

        // Create an array of TextureRegions
        animation.add(PlayScreen.atlas.findRegion("explosion0"));
        animation.add(PlayScreen.atlas.findRegion("explosion1"));
        animation.add(PlayScreen.atlas.findRegion("explosion2"));
        animation.add(PlayScreen.atlas.findRegion("explosion3"));

        explosion_animation = new Animation(0.05f / 1f, animation);

        time = 0;

    }




    public void update(float dt){
        time += dt;
        animationFinished = explosion_animation.isAnimationFinished(time);
        this.getSprite().setRegion(explosion_animation.getKeyFrame(time));
    }


    public void draw(SpriteBatch batch){

        if(!animationFinished){
            this.getSprite().draw(batch);
        }
    }



    public Sprite getSprite() {
        return sprite;
    }

    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
    }

    public boolean isAnimationFinished() {
        return animationFinished;
    }

    public void setAnimationFinished(boolean animationFinished) {
        this.animationFinished = animationFinished;
    }

    public boolean isDamageFlag() {
        return damageFlag;
    }

    public void setDamageFlag(boolean damageFlag) {
        this.damageFlag = damageFlag;
    }
}
