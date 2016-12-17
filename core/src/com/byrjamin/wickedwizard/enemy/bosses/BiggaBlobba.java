package com.byrjamin.wickedwizard.enemy.bosses;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.byrjamin.wickedwizard.MainGame;
import com.byrjamin.wickedwizard.arenas.Arena;
import com.byrjamin.wickedwizard.enemy.Enemy;
import com.byrjamin.wickedwizard.helper.AnimationPacker;
import com.byrjamin.wickedwizard.helper.BoundsDrawer;
import com.byrjamin.wickedwizard.helper.Measure;
import com.byrjamin.wickedwizard.helper.Reloader;
import com.byrjamin.wickedwizard.screens.PlayScreen;

import org.w3c.dom.css.Rect;

/**
 * Created by Home on 16/12/2016.
 */
public class BiggaBlobba extends Enemy {


    private float TEXTURE_HEIGHT = Measure.units(45);
    private float TEXTURE_WIDTH = Measure.units(45);

    private float BOUNDS_HEIGHT = Measure.units(17);
    private float BOUNDS_WIDTH = Measure.units(33);



    private static final float GRAVITY = -100;

    private float MOVEMENT = MainGame.GAME_UNITS * 5;

    private boolean isFalling = true;
    private boolean test = true;

    private float lowerbodyHeight;


    private Vector2 position;
    private Vector2 velocity;

    private Array<Rectangle> bounds;

    private Animation walk;

    private Animation currentAnimation;

    TextureRegion currentFrame;

    ShapeRenderer shapeRenderer;


    //Phase 1
    private Reloader slimeCannon;
    private Reloader littleSlimer;

    //TODO - Biggablobba needs to summon litte slimes
    //TODO - Biggablobba has a shield

    public BiggaBlobba(float posX, float posY){
        super();

        shapeRenderer = new ShapeRenderer();

        this.setHealth(10000);

        velocity = new Vector2();
        position = new Vector2(posX, posY);
        //currentFrame = PlayScreen.atlas.findRegion("biggablobba");
        bounds = new Array<Rectangle>();

        //17 to 15

        //Lower Body
        Rectangle lowerBody = new Rectangle(posX + Measure.units(6), posY, BOUNDS_WIDTH, Measure.units(17));

        Rectangle upperBody = new Rectangle(posX + Measure.units(8),
                lowerBody.getY() + lowerBody.getHeight(),
                Measure.units(29), Measure.units(5));

        Rectangle upperupperBody = new Rectangle(posX + Measure.units(10),
                upperBody.getY() + upperBody.getHeight(),
                Measure.units(26), Measure.units(5));

        Rectangle upperupperupperBody = new Rectangle(posX + Measure.units(14),
                upperupperBody.getY() + upperupperBody.getHeight(),
                Measure.units(17), Measure.units(3));

        Rectangle crown = new Rectangle(posX + Measure.units(18),
                upperupperupperBody.getY() + upperupperupperBody.getHeight(),
                Measure.units(9),
                Measure.units(9));


        bounds.add(lowerBody);
        bounds.add(upperBody);
        bounds.add(upperupperBody);
        bounds.add(upperupperupperBody);
        bounds.add(crown);

        currentFrame = PlayScreen.atlas.findRegion("biggablobba");
        walk = AnimationPacker.genAnimation(1 / 20f, "biggablobba", Animation.PlayMode.LOOP_PINGPONG);
        currentAnimation = walk;

        bounce();
        //updateLowerBody();

        time = 0f;

    }



    public void draw(SpriteBatch batch){
        if(isFlashing) {
            Color color = batch.getColor();
            batch.setColor(new Color(0.0f,0.0f,0.0f,0.95f));
            batch.draw(currentFrame, position.x, position.y, TEXTURE_WIDTH, TEXTURE_HEIGHT);
            batch.setColor(color);
        } else {
            batch.draw(currentFrame, position.x, position.y, TEXTURE_WIDTH, TEXTURE_HEIGHT);
        }

        BoundsDrawer.drawBounds(batch, bounds);

    }


    /**
     * Bigga blobba bounces with a variances of 2 units.
     */
    public void bounce(){

        //private float frameNo =

        //System.out.println(walk.getAnimationDuration());
        //System.out.println(walk.getFrameDuration());

       System.out.println(walk.getKeyFrameIndex(time));

        //if(walk.getAnimationDuration())
    }

    public void applyGravity(float dt, Arena arena){

        if(isFalling){
            Rectangle r = arena.getOverlappingRectangle(bounds.get(0));
            if(r != null) {
                //this.getSprite().setY(r.getY() + r.getHeight());
                isFalling = false;
            } else {
                this.velocity.add(0, GRAVITY * dt);
                position.add(velocity);

                for(Rectangle bound : bounds) {
                    Vector2 temp = new Vector2();
                    bound.getPosition(temp);
                    temp.add(velocity);
                    bound.setPosition(temp);
                }
                //this.getSprite().translateY(velocity.y);
            }

        }
    }


    public boolean isHit(Rectangle r){

        for(Rectangle bound: bounds){
            if((state == STATE.ALIVE) && bound.overlaps(r)){
                return true;
            }
        }
        return false;
    }



    @Override
    public void update(float dt) {

        //currentFrame = currentAnimation.getKeyFrame(time);
    }

    @Override
    public void update(float dt, Arena a) {

        flashTimer(dt);
        time += dt;
        currentFrame = currentAnimation.getKeyFrame(time);
        applyGravity(dt, a);
        boundsUpdate();

        //TODO - As slime takes damage it falls
/*        if(time > 5 && test == true){
            bounds.removeIndex(0);
            test = false;
            velocity = new Vector2();
            isFalling = true;
        }*/



    }



    public void slimeAttack(){

    }


    public void boundsUpdate(){

        //if(test) {

            float frameIndex = currentAnimation.getKeyFrameIndex(time);


            if (frameIndex == 4) {
                bounds.get(0).setHeight(Measure.units(17f - 2.0f));
            } else {
                bounds.get(0).setHeight(Measure.units(17f - (0.5f * frameIndex)));
            }

            for (int i = 1; i < bounds.size; i++) {
                bounds.get(i).setY(bounds.get(i - 1).getY() + bounds.get(i - 1).getHeight());
            }

        //}

    }


}
