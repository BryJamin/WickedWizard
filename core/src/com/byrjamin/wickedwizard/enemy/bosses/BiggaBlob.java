package com.byrjamin.wickedwizard.enemy.bosses;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.byrjamin.wickedwizard.MainGame;
import com.byrjamin.wickedwizard.arenas.Arena;
import com.byrjamin.wickedwizard.enemy.Enemy;
import com.byrjamin.wickedwizard.helper.AnimationPacker;
import com.byrjamin.wickedwizard.screens.PlayScreen;

/**
 * Created by Home on 16/12/2016.
 */
public class BiggaBlob extends Enemy {


    private float HEIGHT = MainGame.GAME_UNITS * 35;
    private float WIDTH = MainGame.GAME_UNITS * 35;

    private static final float GRAVITY = -50;

    private float MOVEMENT = MainGame.GAME_UNITS * 5;

    private boolean isFalling = true;


    private Vector2 position;
    private Vector2 velocity;

    private Rectangle bounds;

    private Animation walk;

    private Animation currentAnimation;

    TextureRegion currentFrame;


    public BiggaBlob(float posX, float posY){
        super();

        velocity = new Vector2();
        position = new Vector2(posX, posY);
        //currentFrame = PlayScreen.atlas.findRegion("biggablobba");

        bounds = new Rectangle(posX, posY, WIDTH, HEIGHT);

        walk = AnimationPacker.genAnimation(1 / 15f, "biggablobba", Animation.PlayMode.LOOP);
        currentAnimation = walk;

        time = 0f;

    }



    public void draw(SpriteBatch batch){
        currentFrame = currentAnimation.getKeyFrame(time);
        batch.draw(currentFrame, position.x, position.y, WIDTH, HEIGHT);
    }

    public void applyGravity(float dt, Arena arena){

        if(isFalling){
            Rectangle r = arena.getOverlappingRectangle(bounds);
            if(r != null) {
                this.getSprite().setY(r.getY() + r.getHeight());
                isFalling = false;
            } else {
                this.velocity.add(0, GRAVITY * dt);
                position.add(velocity);
                bounds.setPosition(position);
                //this.getSprite().translateY(velocity.y);
            }

        }
    }


    @Override
    public void update(float dt) {

        //currentFrame = currentAnimation.getKeyFrame(time);
    }

    @Override
    public void update(float dt, Arena a) {
        time += dt;
        applyGravity(dt, a);
    }
}
