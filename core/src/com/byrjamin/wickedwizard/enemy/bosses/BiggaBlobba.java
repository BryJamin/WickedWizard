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
import com.byrjamin.wickedwizard.arenas.EnemyBullets;
import com.byrjamin.wickedwizard.enemy.Enemy;
import com.byrjamin.wickedwizard.enemy.EnemyPresets;
import com.byrjamin.wickedwizard.helper.AnimationPacker;
import com.byrjamin.wickedwizard.helper.BoundsDrawer;
import com.byrjamin.wickedwizard.helper.Measure;
import com.byrjamin.wickedwizard.helper.Reloader;
import com.byrjamin.wickedwizard.screens.PlayScreen;
import com.byrjamin.wickedwizard.spelltypes.Dispellable;
import com.byrjamin.wickedwizard.spelltypes.Projectile;

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

    public enum PHASE {
        PHASE1, PHASE2, PHASE3
    }


    PHASE phase;

    //Phase 1
    private Reloader littleSlimer;
    private int slimeCount = 5;

    //Phase 2
    private Reloader jumper = new Reloader(4.0f);
    private Reloader launcher = new Reloader(1.0f);
    private int jumpCount = 3;
    private boolean isLanded;

    //TODO - Biggablobba needs to summon litte slimes
    //TODO - Biggablobba has a shield
    //TODO - Clean up this class.

    public BiggaBlobba(float posX, float posY){
        super();

        shapeRenderer = new ShapeRenderer();

        this.setHealth(100);

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

        //bounce();
        //updateLowerBody();


        phase = PHASE.PHASE1;
         littleSlimer = new Reloader(0.5f, 3.0f);

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


    public void phase1Update(float dt, Arena a){

        if(slimeCount <= 0){
            phase = PHASE.PHASE2;
            jumpCount = 3;
            return;
        }

        littleSlimer.update(dt);

        if(littleSlimer.isReady()){
            a.getEnemies().add(EnemyPresets.smallBlob(position.x + Measure.units(15), a.ARENA_HEIGHT));
            slimeCount --;
            System.out.println(slimeCount);
        }

    }

    public void phase2Update(float dt, Arena a){


        if(jumpCount <= 0){
            phase = PHASE.PHASE1;
            slimeCount = 5;
            return;
        }

        jumper.update(dt);
        launcher.update(dt);


        if(jumper.isReady()){
            jump();
            jumpCount --;
            isLanded = false;
        } else if(isLanded){

            if(launcher.isReady()){
                EnemyBullets.activeBullets.add(new Projectile.ProjectileBuilder(
                        this.position.x + Measure.units(25) ,
                        this.position.y + Measure.units(30),
                        a.getWizard().getX(),
                        a.getWizard().getY())
                        .spriteString("bullet")
                        .damage(1)
                        .HORIZONTAL_VELOCITY(15f)
                        .dispellable(new Dispellable(Dispellable.DISPELL.HORIZONTAL))
                        .build());
            }


        }
        System.out.println(position.y <= a.groundHeight());
        System.out.println(position.y);
        System.out.println(a.groundHeight());
        if(position.y <= a.groundHeight()) {
            System.out.println(position.y <= a.groundHeight());
            isLanded = true;
        }

    }


    /**
     * Bigga blobba bounces with a variances of 2 units.
     */
    public void jump(){
        this.velocity.y = 50;
    }

    public void applyGravity(float dt, Arena arena){

        if(velocity.y <= 0){
            //System.out.println(isFalling);
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
            }
        } else {

            this.velocity.add(0, GRAVITY * dt);
            position.add(velocity);


            for(Rectangle bound : bounds) {
                Vector2 temp = new Vector2();
                bound.getPosition(temp);
                temp.add(velocity);
                bound.setPosition(temp);
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

        if(this.getHealth() <= 0 ){
            this.setState(STATE.DEAD);
        }

        if(state == STATE.ALIVE) {

            if (phase == PHASE.PHASE1) {
                phase1Update(dt, a);
            } else if (phase == PHASE.PHASE2) {
                phase2Update(dt, a);
            }

            if (this.getHealth() < (100 / 2)) {
                currentAnimation.setFrameDuration(1 / 40f);
            }

            for (Rectangle bound : bounds) {
                if (bound.overlaps(a.getWizard().getSprite().getBoundingRectangle())) {
                    a.getWizard().reduceHealth(1);
                }
            }

        }
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
