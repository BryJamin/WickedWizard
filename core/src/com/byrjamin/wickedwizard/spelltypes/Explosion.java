package com.byrjamin.wickedwizard.spelltypes;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.byrjamin.wickedwizard.MainGame;
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

        explosion_animation = new Animation(0.07f / 1f, animation);

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
