package com.byrjamin.wickedwizard.enemy.enemies;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.byrjamin.wickedwizard.enemy.Enemy;
import com.byrjamin.wickedwizard.enemy.EnemyBullets;
import com.byrjamin.wickedwizard.helper.AnimationPacker;
import com.byrjamin.wickedwizard.helper.BoundsDrawer;
import com.byrjamin.wickedwizard.helper.Measure;
import com.byrjamin.wickedwizard.helper.StateTimer;
import com.byrjamin.wickedwizard.maps.rooms.Room;
import com.byrjamin.wickedwizard.spelltypes.Dispellable;
import com.byrjamin.wickedwizard.spelltypes.Projectile;
import com.byrjamin.wickedwizard.spelltypes.blastwaves.BlastWave;
import com.byrjamin.wickedwizard.staticstrings.TextureStrings;

/**
 * Created by Home on 01/01/2017.
 */
public class SilverHead extends Enemy {

    private float HEIGHT = Measure.units(10);
    private float WIDTH = Measure.units(10);

    private Animation standingAnimation;
    private Animation chargingAnimation;
    private Animation closingAnimation;
    private Animation openingAnimation;
    private Animation currentAnimation;

    private TextureRegion currentFrame;

    private Rectangle bounds;

    private StateTimer standingTime;

    private Vector2 velocity;
    private Vector2 position;

    private Array<BlastWave> blastWaveArray = new Array<BlastWave>();

    private enum ACTION {
        STANDING, CHARGING, OPENING, CLOSING
    }

    private ACTION action = ACTION.STANDING;

    public static class SilverHeadBuilder {

        //Required Parameters
        private final float posX;
        private final float posY;

        //Optional Parameters
        private float health = 4;
        private float scale = 1;

        public SilverHeadBuilder(float posX, float posY) {
            this.posX = posX;
            this.posY = posY;
        }

        public SilverHeadBuilder health(float val)
        { health = val; return this; }

        public SilverHeadBuilder scale(float val)
        { scale = val; return this; }

        public SilverHead build() {
            return new SilverHead(this);
        }


    }


    public SilverHead(SilverHeadBuilder silverHeadBuilder){

        super();

        position = new Vector2(silverHeadBuilder.posX, silverHeadBuilder.posY);
        standingAnimation = AnimationPacker.genAnimation(0.15f, TextureStrings.SILVERHEAD_ST, Animation.PlayMode.LOOP);
        chargingAnimation = AnimationPacker.genAnimation(0.15f, TextureStrings.SILVERHEAD_CHARGING);
        closingAnimation = AnimationPacker.genAnimation(0.15f, TextureStrings.SILVERHEAD_HIDING);
        openingAnimation = AnimationPacker.genAnimation(0.15f, TextureStrings.SILVERHEAD_HIDING, Animation.PlayMode.REVERSED);
        currentAnimation = standingAnimation;
        currentFrame = currentAnimation.getKeyFrame(time);

        standingTime = new StateTimer(2f);


    }

    @Override
    public void update(float dt, Room r) {

        time += dt;

        for(BlastWave b : blastWaveArray){
            b.update(dt);
        }

        if(action == ACTION.STANDING){
            currentAnimation = standingAnimation;
            standingTime.update(dt);

            if(standingTime.isFinished()){
                standingTime.reset();
                action = ACTION.CLOSING;
                time = 0;
            }

        } else if(action == ACTION.CHARGING){
            currentAnimation = chargingAnimation;

            if(currentAnimation.isAnimationFinished(time)){
                action = ACTION.OPENING;
                time = 0;

                BlastWave b = new BlastWave(this.position.x + WIDTH / 2, this.position.y + HEIGHT / 2);
                b.setSpeed(0.1f);

                blastWaveArray.add(b);
            }

        } else if(action == ACTION.CLOSING) {
            currentAnimation = closingAnimation;

            if(currentAnimation.isAnimationFinished(time)){
                action = ACTION.CHARGING;
                currentAnimation = chargingAnimation;
                time = 0;
            }

        } else if(action == ACTION.OPENING){
            currentAnimation = openingAnimation;

            if(currentAnimation.isAnimationFinished(time)){
                action = ACTION.STANDING;
                currentAnimation = standingAnimation;
                time = 0;
            }

        }

    }


    public void draw(SpriteBatch batch){
        if(isFlashing) {
            Color color = batch.getColor();
            batch.setColor(new Color(0.0f,0.0f,0.0f,0.95f));
            batch.draw(currentAnimation.getKeyFrame(time), position.x, position.y, WIDTH, HEIGHT);
            batch.setColor(color);
        } else {
            batch.draw(currentAnimation.getKeyFrame(time), position.x, position.y, WIDTH, HEIGHT);
        }

        for(BlastWave b : blastWaveArray){
            b.draw(batch);
        }

//        BoundsDrawer.drawBounds(batch, bounds);
    }

}
