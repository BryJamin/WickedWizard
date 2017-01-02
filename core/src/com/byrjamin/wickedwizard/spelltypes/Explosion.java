package com.byrjamin.wickedwizard.spelltypes;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.byrjamin.wickedwizard.MainGame;
import com.byrjamin.wickedwizard.helper.AnimationPacker;
import com.byrjamin.wickedwizard.screens.PlayScreen;

/**
 * This class is soon to be irrelevant.
 */
public class Explosion {

    private Animation explosion_animation;
    private Sprite sprite;
    private float time;

    private boolean animationFinished = false;

    private boolean hasHit = false;

    public Explosion(float posX, float posY){

        sprite = PlayScreen.atlas.createSprite("explosion");
        sprite.setSize((float) MainGame.GAME_UNITS * 12, MainGame.GAME_UNITS * 12);
       // sprite.setPosition(posX - sprite.getWidth() / 2, posY - sprite.getHeight() / 2);
        sprite.setCenter(posX, posY);

        explosion_animation = AnimationPacker.genAnimation(0.05f / 1f, "explosion");

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

    public boolean hasHit() {
        return hasHit;
    }

    public void sethasHit(boolean damageFlag) {
        this.hasHit = damageFlag;
    }
}
