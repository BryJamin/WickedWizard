package com.byrjamin.wickedwizard.entity.bosses;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.byrjamin.wickedwizard.MainGame;
import com.byrjamin.wickedwizard.entity.enemies.Enemy;
import com.byrjamin.wickedwizard.entity.enemies.EnemyPresets;
import com.byrjamin.wickedwizard.helper.GravMaster2000;
import com.byrjamin.wickedwizard.helper.collider.Collider;
import com.byrjamin.wickedwizard.maps.rooms.Room;
import com.byrjamin.wickedwizard.helper.AnimationPacker;
import com.byrjamin.wickedwizard.helper.BoundsDrawer;
import com.byrjamin.wickedwizard.helper.Measure;
import com.byrjamin.wickedwizard.helper.timer.Reloader;
import com.byrjamin.wickedwizard.screens.PlayScreen;
import com.byrjamin.wickedwizard.spelltypes.Projectile;
import com.byrjamin.wickedwizard.assets.TextureStrings;

/**
 * Created by Home on 16/12/2016.
 */
public class BiggaBlobba extends Enemy {


    private final Rectangle lowerBody;
    private final Rectangle upperBody;
    private final Rectangle upperupperupperBody;
    private final Rectangle upperupperBody;
    private final Rectangle crown;

    private float TEXTURE_HEIGHT = Measure.units(45);
    private float TEXTURE_WIDTH = Measure.units(45);

    private float BOUNDS_HEIGHT = Measure.units(17);
    private float BOUNDS_WIDTH = Measure.units(33);



    private static final float GRAVITY = -100;

    private float MOVEMENT = MainGame.GAME_UNITS * 5;

    private boolean isFalling = true;
    private boolean test = true;

    private float lowerbodyHeight;


    private GravMaster2000 g2000 = new GravMaster2000();

    private Vector2 velocity;

    private Animation<TextureRegion> walk;

    private Animation<TextureRegion> currentAnimation;

    ShapeRenderer shapeRenderer;

    public enum PHASE {
        PHASE1, PHASE2, PHASE3
    }


    PHASE phase;

    private boolean isJumping = true;

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

        this.setHealth(75);

        velocity = new Vector2();
        position = new Vector2(posX, posY);
        bounds = new Array<Rectangle>();

        //17 to 15

        //Lower Body
        lowerBody = new Rectangle(posX + Measure.units(6), posY, BOUNDS_WIDTH, Measure.units(17));

        upperBody = new Rectangle(posX + Measure.units(8),
                lowerBody.getY() + lowerBody.getHeight(),
                Measure.units(29), Measure.units(5));

        upperupperBody = new Rectangle(posX + Measure.units(10),
                upperBody.getY() + upperBody.getHeight(),
                Measure.units(26), Measure.units(5));

        upperupperupperBody = new Rectangle(posX + Measure.units(14),
                upperupperBody.getY() + upperupperBody.getHeight(),
                Measure.units(17), Measure.units(3));

        crown = new Rectangle(posX + Measure.units(18),
                upperupperupperBody.getY() + upperupperupperBody.getHeight(),
                Measure.units(9),
                Measure.units(9));


        bounds.add(lowerBody);
        bounds.add(upperBody);
        bounds.add(upperupperBody);
        bounds.add(upperupperupperBody);
        bounds.add(crown);

        currentFrame = PlayScreen.atlas.findRegion(TextureStrings.BIGGABLOBBA_STANDING);
        walk = AnimationPacker.genAnimation(1 / 20f, TextureStrings.BIGGABLOBBA_STANDING, Animation.PlayMode.LOOP_PINGPONG);
        currentAnimation = walk;

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


    public void phase1Update(float dt, Room a){

        if(slimeCount <= 0){
            phase = PHASE.PHASE2;
            jumpCount = 3;
            return;
        }

        littleSlimer.update(dt);

        if(littleSlimer.isReady()){
            a.getEnemies().add(EnemyPresets.smallBlob(upperBody.x, upperBody.y));
            slimeCount --;
        }

    }

    public void phase2Update(float dt, Room a){


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
                bullets.addProjectile(new Projectile.ProjectileBuilder(
                        this.position.x + Measure.units(25) ,
                        this.position.y + Measure.units(30),
                        a.getWizard().getX(),
                        a.getWizard().getY())
                        .damage(1)
                        .speed(Measure.units(60f))
                        .drawingColor(Color.RED)
                        .build());
            }


        }
/*        System.out.println(position.y <= a.groundHeight());
        System.out.println(position.y);
        System.out.println(a.groundHeight());*/
        if(position.y <= a.groundHeight()) {
/*            System.out.println(position.y <= a.groundHeight());*/
            isLanded = true;
        }

    }


    /**
     * Bigga blobba bounces with a variances of 2 units.
     */
    public void jump(){
        velocity.y = 25;
        isJumping = false;
    }

    public void applyGravity(float dt, Room room){

        velocity.add(0, g2000.GRAVITY);

        position.add(velocity);

        System.out.println(position.y);
        lowerBody.y = position.y;
        System.out.println(lowerBody.y + " y position of lwer body");
        //this.velocity.add(0, GRAVITY * dt);
    }

    @Override
    public void update(float dt, Room r) {

        super.update(dt, r);
        time += dt;
        applyGravity(dt, r);
        boundsUpdate();


        if(state == STATE.ALIVE) {

            if (phase == PHASE.PHASE1) {
                phase1Update(dt, r);
            } else if (phase == PHASE.PHASE2) {
                phase2Update(dt, r);
            }

            if (this.getHealth() < (75 / 2)) {
                currentAnimation.setFrameDuration(1 / 40f);
            }

            for (Rectangle bound : bounds) {
                if (bound.overlaps(r.getWizard().getBounds())) {
                    r.getWizard().reduceHealth(1);
                }
            }

            if(this.getHealth() <= 0 ){
                this.setState(STATE.DYING);
            }

            currentFrame = currentAnimation.getKeyFrame(time);


        } else if(state == STATE.DYING){
            dyingUpdate(dt);
        }
        //TODO - As slime takes damage it falls
/*        if(time > 5 && test == true){
            bounds.removeIndex(0);
            test = false;
            velocity = new Vector2();
            isFalling = true;
        }*/



    }

    public void boundsUpdate(){

        //if(test) {

            float frameIndex = currentAnimation.getKeyFrameIndex(time);


            if (frameIndex == 4) {
                lowerBody.setHeight(Measure.units(17f - 2.0f));
            } else {
                lowerBody.setHeight(Measure.units(17f - (0.5f * frameIndex)));
            }

            for (int i = 1; i < bounds.size; i++) {
                bounds.get(i).setY(bounds.get(i - 1).getY() + bounds.get(i - 1).getHeight());
            }

        //}

    }

    @Override
    public void applyCollision(Collider.Collision collision) {


        System.out.println(collision);
        switch(collision) {
            case TOP:
                if(velocity.y <= 0){
                    velocity.y = 0;
                    //TODO the reason for is
                    position.y = lowerBody.y;
                }

                break;

        }
    }


}
