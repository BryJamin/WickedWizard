package com.byrjamin.wickedwizard.entity.enemies;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.byrjamin.wickedwizard.assets.TextureStrings;
import com.byrjamin.wickedwizard.helper.AnimationPacker;
import com.byrjamin.wickedwizard.helper.BoundsDrawer;
import com.byrjamin.wickedwizard.helper.Measure;
import com.byrjamin.wickedwizard.helper.collider.Collider;
import com.byrjamin.wickedwizard.helper.timer.StateTimer;
import com.byrjamin.wickedwizard.maps.rooms.Room;
import com.byrjamin.wickedwizard.screens.PlayScreen;

import java.util.Random;

/**
 * Created by Home on 19/02/2017.
 */
public class Bouncer extends Enemy{

    private float HEIGHT = Measure.units(5f);
    private float WIDTH = Measure.units(5f);

    private float MOVEMENT = 600f;
    private float STOMPSPEED = -1200f;

    private Animation<TextureRegion> defaultAnimation;
    private Animation<TextureRegion> stompAnimation;

    private Animation<TextureRegion> currentAnimation;

    private StateTimer stompTimer = new StateTimer(0.5f, 0.0f);

    private enum ACTION {
        DEFAULT, STOMP
    }

    private ACTION action = ACTION.DEFAULT;

    public static class BouncerBuilder extends GroundedEnemy.GBuilder {
        public BouncerBuilder(float posX, float posY) {
            super(posX, posY);
            health(5);
        }

        @Override
        public Bouncer build() {
            return new Bouncer(this);
        }
    }


    public Bouncer(BouncerBuilder b){
        super(b);
        health *= scale;
        HEIGHT *= scale;
        WIDTH *= scale;
        MOVEMENT *= speed;
        collisionBound = new Rectangle(position.x, position.y, WIDTH, HEIGHT);
        velocity = new Vector2();
        Random random = new Random();
        velocity.x = random.nextBoolean() ? MOVEMENT : -MOVEMENT;
        velocity.y = MOVEMENT;
        defaultAnimation = AnimationPacker.genAnimation(1.0f / 15f, "bouncer", Animation.PlayMode.LOOP);
        stompAnimation = AnimationPacker.genAnimation(1.0f / 7f, "bouncer_stomp", Animation.PlayMode.LOOP);
        currentAnimation = defaultAnimation;
        currentFrame = currentAnimation.getKeyFrame(time);
        bounds.add(collisionBound);
    }


    @Override
    public void draw(SpriteBatch batch) {
        if(isFlashing) {

            System.out.println("SHOULD BE FLASH");

            Color color = batch.getColor();
            batch.setColor(new Color(0.0f,0.0f,0.0f,0.95f));
            batch.draw(currentFrame, position.x, position.y, WIDTH, HEIGHT);
            batch.setColor(color);
        } else {
            batch.draw(currentFrame, position.x, position.y, WIDTH, HEIGHT);
        }
        BoundsDrawer.drawBounds(batch, collisionBound);
    }

    @Override
    public void update(float dt, Room r) {
        super.update(dt, r);

        time+=dt;

        if(state == STATE.ALIVE) {

            currentFrame = currentAnimation.getKeyFrame(time);

            stompTimer.update(dt);

            position.x = action == ACTION.DEFAULT ? position.x + (velocity.x * dt) : position.x;
            position.y = position.y + (velocity.y * dt);
            collisionBound.x = position.x;
            collisionBound.y = position.y;

            if (collisionBound.contains(r.getWizard().getCenterX(), position.y) && stompTimer.isFinished() && !r.getWizard().isDead()) {
                currentAnimation = stompAnimation;
                time = 0;
                action = ACTION.STOMP;
                velocity.y = STOMPSPEED;
                stompTimer.reset();
            }
        } else {

        }

    }


    @Override
    public void applyCollision(Collider.Collision collision) {
        switch(collision){
            case TOP:
                if(state == STATE.ALIVE) {
                    if (action == ACTION.STOMP) {
                        action = ACTION.DEFAULT;
                        velocity.x = -velocity.x;
                        currentAnimation = defaultAnimation;
                    }
                    velocity.y = MOVEMENT;
                } else {
                    velocity.x = 0;
                    velocity.y = 0;
                }
                break;
            case BOTTOM: velocity.y = -MOVEMENT;
                break;
            case LEFT: velocity.x = -MOVEMENT;
                break;
            case RIGHT: velocity.x = MOVEMENT;
                break;
        }
    }
}
