package com.byrjamin.wickedwizard.entity.enemies;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.byrjamin.wickedwizard.entity.ActiveBullets;
import com.byrjamin.wickedwizard.helper.AnimationPacker;
import com.byrjamin.wickedwizard.helper.BoundsDrawer;
import com.byrjamin.wickedwizard.helper.GravMaster2000;
import com.byrjamin.wickedwizard.helper.Measure;
import com.byrjamin.wickedwizard.helper.collider.Collider;
import com.byrjamin.wickedwizard.helper.timer.StateTimer;
import com.byrjamin.wickedwizard.maps.rooms.Room;
import com.byrjamin.wickedwizard.spelltypes.Projectile;
import com.byrjamin.wickedwizard.spelltypes.blastwaves.BlastWave;
import com.byrjamin.wickedwizard.assets.TextureStrings;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;

/**
 * Created by Home on 01/01/2017.
 */
public class SilverHead extends GroundedEnemy {

    private Animation standingAnimation;
    private Animation chargingAnimation;
    private Animation closingAnimation;
    private Animation openingAnimation;
    private Animation<TextureRegion> currentAnimation;

    private StateTimer standingTime;

    private Array<BlastWave> blastWaveArray = new Array<BlastWave>();

    //private ActiveBullets activeBullets = new ActiveBullets();

    private enum ACTION {
        STANDING, CHARGING, OPENING, CLOSING
    }

    private ACTION action = ACTION.STANDING;

    public static class SilverHeadBuilder extends GroundedEnemy.GBuilder {
        public SilverHeadBuilder(float posX, float posY) {
            super(posX, posY);
            health(7);
        }

        @Override
        public SilverHead build() {
            return new SilverHead(this);
        }
    }


    public SilverHead(SilverHeadBuilder b){
        super(b);
        HEIGHT = Measure.units(10);
        WIDTH = Measure.units(10);

        collisionBound = new Rectangle(position.x + (Measure.units(1.5f) * scale), position.y,
                WIDTH - (Measure.units(3f) * scale),
                HEIGHT - (Measure.units(2.5f) * scale));

        bounds.add(collisionBound);

        standingAnimation = AnimationPacker.genAnimation(0.1f, TextureStrings.SILVERHEAD_ST, Animation.PlayMode.LOOP);
        chargingAnimation = AnimationPacker.genAnimation(0.1f, TextureStrings.SILVERHEAD_CHARGING);
        closingAnimation = AnimationPacker.genAnimation(0.1f, TextureStrings.SILVERHEAD_HIDING);
        openingAnimation = AnimationPacker.genAnimation(0.1f, TextureStrings.SILVERHEAD_HIDING, Animation.PlayMode.REVERSED);
        currentAnimation = standingAnimation;
        currentFrame = currentAnimation.getKeyFrame(time);
        this.setDyingAnimation(AnimationPacker.genAnimation(0.1f, TextureStrings.EXPLOSION));
        standingTime = new StateTimer(2f);
    }

    @Override
    public void update(float dt, Room r) {
        super.update(dt, r);
        time += dt;

        if(getState() == STATE.ALIVE) {
            performAction(dt);

            if(this.getHealth() <= 0 ){
                this.setState(STATE.DYING);
                time = 0;
            }

        } else if(getState() == STATE.DYING){
            dyingUpdate(dt);
        }
    }



    public void performAction(float dt){

        if(action == ACTION.STANDING){
            standingTime.update(dt);

            if(standingTime.isFinished()){
                standingTime.reset();
                action = ACTION.CLOSING;
                changeAnimation(closingAnimation);
            }

        } else if(action == ACTION.CHARGING){
            if(currentAnimation.isAnimationFinished(time)){
                action = ACTION.OPENING;
                changeAnimation(openingAnimation);

                int[] angles = new int[] {0,30,60,80,100,120,150,180};

                for(int i : angles){
                    bullets.addProjectile(getSilverheadProjectile(i));
                }

                BlastWave b = new BlastWave(this.position.x + WIDTH / 2, this.position.y + HEIGHT / 2);
                b.setSpeed(0.25f);
                blastWaveArray.add(b);
            }

        } else if(action == ACTION.CLOSING) {
            if(currentAnimation.isAnimationFinished(time)){
                action = ACTION.CHARGING;
                changeAnimation(chargingAnimation);
            }

        } else if(action == ACTION.OPENING){
            if(currentAnimation.isAnimationFinished(time)){
                action = ACTION.STANDING;
                changeAnimation(standingAnimation);
            }

        }

        currentFrame = currentAnimation.getKeyFrame(time);

    }


    public Projectile getSilverheadProjectile(double angle){
        return new Projectile.ProjectileBuilder(position.x + WIDTH / 2 , position.y + HEIGHT / 2, angle)
                .damage(1)
                .drawingColor(Color.RED)
                .speed(Measure.units(75f))
                .scale(0.7f)
                .gravity(true)
                .build();
    }



    public void changeAnimation(Animation a){
        currentAnimation = a;
        time = 0;
    }

    public void draw(SpriteBatch batch){

        bullets.draw(batch);

        if(isFlashing) {

            Color color = batch.getColor();
            batch.setColor(new Color(0.0f,0.0f,0.0f,0.95f));
            batch.draw(currentFrame, position.x, position.y, WIDTH, HEIGHT);
            batch.setColor(color);
        } else {
            batch.draw(currentFrame, position.x, position.y, WIDTH, HEIGHT);
        }

        System.out.println(bullets.getBullets().size);

        BoundsDrawer.drawBounds(batch, bounds);
    }


    @Override
    public void reduceHealth(float dmg){
        if(action != ACTION.CHARGING) {
            super.reduceHealth(dmg);
        }
    }


}
