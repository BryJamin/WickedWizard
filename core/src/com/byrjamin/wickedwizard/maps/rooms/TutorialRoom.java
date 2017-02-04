package com.byrjamin.wickedwizard.maps.rooms;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Align;
import com.byrjamin.wickedwizard.assets.TextureStrings;
import com.byrjamin.wickedwizard.entity.enemies.Dummy;
import com.byrjamin.wickedwizard.entity.enemies.Enemy;
import com.byrjamin.wickedwizard.helper.AnimationPacker;
import com.byrjamin.wickedwizard.helper.Measure;
import com.byrjamin.wickedwizard.helper.timer.StateTimer;
import com.byrjamin.wickedwizard.maps.MapCoords;
import com.byrjamin.wickedwizard.maps.rooms.layout.GrapplePoint;
import com.byrjamin.wickedwizard.screens.PlayScreen;

/**
 * Created by Home on 26/01/2017.
 */
public class TutorialRoom extends Room {

    BitmapFont font = new BitmapFont();

    OrthographicCamera gamecam;


    private String hello = "Ok, Welcome to the Tutorial";
    private String hello2 = "Now I don't have a lot of time \nso don't go all braindead on me";
    private String hello3 = "This is just a short callibration";

    private enum DIALOGUETREE {
        WELCOME, MOVING, SHOOTING, MOVINGANDSHOOTING, GRAPPLING, ENDING, LEAVING
    }

    private DIALOGUETREE dialogue;


    private enum TUTORIAL_STATE {
        GROUND, SHOOTING, GRAPPLING, LEAVING
    }

    private TUTORIAL_STATE tutorial_state;

    private ShapeRenderer shapeRenderer = new ShapeRenderer();


    private String move = "Tap on the ground to move\nTry it";
    private String move1 = "Neat.";

    private String fire = "Press and hold on the screen to shoot";
    private String fire1 = "You can Drag to change your aim";


    private String movefire = "You can move and fire at the same time\nGive it a shot.";
    private String movefire1 = "You're a real maverick";

    private String grapple1 = "You interact and grapple on objects in the environment\nLike Grates, Doors and Weird white boxes floating in the sky";
    private String grapple2 = "But yea there isn't anything to grapple here at the moment\nSince it's midnight and I haven't finished this room yet";
    private String grapple3 = "Hopefully you can read relatively quickly or some of this\ninformation would be going over your head";
    private String grapple4 = "What a shame if that were to happen";

    private String end = "Oh yea, one more thing before I finish this Tutorial";
    private String end1 = "If you are moving and on the ground, you can't be damaged";
    private String end2 = "To leave the room shoot the block in the top-right corner";
    private String end3 = "Right up there in the corner";
    private String end4 = "The top-right corner. You know, the block that is out of place";
    private String end5 = "C'mon man it's pretty obvious";
    private String end6 = "Just shoot that block right up there";
    private String end7 = "Top-Right Corner.";
    private String end8 = "Block.";
    private String end9 = "Shoot it.";
    private String end10 = "Top-Right Corner...";
    private String end11 = "Block. Top-Right. Shoot It.";
    private String end12 = "If I called it a brick would it make you feel better?";
    private String end13 = "One Second I'll highlight it for you";
    private String end14 = "What do you mean you can't see the hightlight?";
    private String end15 = "If you shoot in that general direction you'll probably hit it";
    private String end16 = "The Top Right Direction.....";
    private String end17 = "Welp I tried, looks like your another dud.";


    private String finish = "Finally, good luck out there";

    private String early_finish = "Looks like you're smarter than you look";


    private String helloArray[] = {hello, hello2, hello3};
    private String grappleArray[] = {grapple1, grapple2, grapple3, grapple4};
    private String endArray[] = {end, end1, end2, end3, end4, end5, end6,end7, end8, end9, end10,
            end11, end12, end13, end14, end15, end16, end17};

    private int arrayPosition;

    private String currentString;

    private StateTimer timer = new StateTimer(7.0f);


    public boolean isTutorialComplete = true;

    private Dummy dummy;

    private float alphaTimer;
    private float alphaTimeLimit = 1.5f;
    private float alphaPercentage;
    private boolean fadeIn = true;




    public TutorialRoom(MapCoords mapCoords){
        super(mapCoords);
        font = new BitmapFont();
        getPlatforms().clear();
        font.getData().setScale(5, 5);
        currentString = hello;
        dialogue = DIALOGUETREE.WELCOME;

        shapeRenderer = new ShapeRenderer();

        tutorial_state = TUTORIAL_STATE.GROUND;

        dummy = new Dummy(WIDTH - Measure.units(10),HEIGHT - Measure.units(10), Measure.units(5), Measure.units(5), PlayScreen.atlas.findRegion("brick"));
        dummy.setDyingAnimation(AnimationPacker.genAnimation(0.5f, TextureStrings.EXPLOSION));

        grapplePoints.add(new GrapplePoint(Measure.units(20), HEIGHT / 2, PlayScreen.atlas.findRegion("grapple")));
        grapplePoints.add(new GrapplePoint(Measure.units(47), HEIGHT / 2, PlayScreen.atlas.findRegion("grapple")));
        grapplePoints.add(new GrapplePoint(WIDTH - Measure.units(25), HEIGHT / 2, PlayScreen.atlas.findRegion("grapple")));

        this.getEnemies().add(dummy);
    }


    @Override
    public void update(float dt, OrthographicCamera gamecam){
        super.update(dt, gamecam);
        this.gamecam = gamecam;



        alphaTimer = fadeIn ? alphaTimer + dt : alphaTimer - dt;
        alphaPercentage = alphaTimer / alphaTimeLimit;
        if(alphaPercentage < 0.1){
            fadeIn = true;
            //alphaPercentage = 0;
        } else if (alphaPercentage > 0.9) {
            fadeIn = false;
            //alphaPercentage = 1;
        }

        timer.update(dt);

        if(timer.isFinished() && alphaPercentage < 0.1){

            switch(tutorial_state){
                case GROUND:
                    tutorial_state = TUTORIAL_STATE.SHOOTING;
                    break;
                case SHOOTING:
                    tutorial_state = TUTORIAL_STATE.GRAPPLING;
                    break;
                case GRAPPLING:
                    tutorial_state = TUTORIAL_STATE.LEAVING;
                    break;
                case LEAVING:
                    tutorial_state = TUTORIAL_STATE.GROUND;
                    break;
            }

            timer.reset();
            alphaTimer = 0.1f;
            fadeIn = true;
        }


        if(state != STATE.ENTRY && state != STATE.EXIT) {

            if(!isTutorialComplete){
                lock();
                dialogueTree(dt);
            } else {
                timer.update(dt);
                unlock();
                if(timer.isFinished()) {
                    currentString = "";
                }
                state = STATE.UNLOCKED;
            }

        }



    }


    public void dialogueTree(float dt){
        if(dialogue == DIALOGUETREE.WELCOME){
            timer.update(dt);
            if(timer.isFinished()){
                timer.reset();
                arrayPosition++;
                //System.out.println(arrayPosition);
                if(arrayPosition < helloArray.length) {
                    currentString = helloArray[arrayPosition];
                } else {
                    dialogue = DIALOGUETREE.MOVING;
                    arrayPosition = 0;
                    timer.reset();
                }
            }
        }

        if(dialogue == DIALOGUETREE.MOVING){
            timer.update(dt);
            if(timer.isFinished()){

                currentString = move;

                if(wizard.isDashing()){
                    currentString = move1;
                    dialogue = DIALOGUETREE.SHOOTING;
                    arrayPosition = 0;
                    timer.reset();
                }
            }
        }

        if(dialogue == DIALOGUETREE.SHOOTING){
            timer.update(dt);
            if(timer.isFinished()){

                currentString = fire;

                if(wizard.isFiring()){
                    currentString = fire1;
                    dialogue = DIALOGUETREE.MOVINGANDSHOOTING;
                    arrayPosition = 0;
                    timer.reset();
                }
            }
        }

        if(dialogue == DIALOGUETREE.MOVINGANDSHOOTING){
            timer.update(dt);
            if(timer.isFinished()){

                currentString = movefire;

                if(wizard.isFiring() && wizard.isDashing()){
                    currentString = movefire1;
                    dialogue = DIALOGUETREE.GRAPPLING;
                    arrayPosition = 0;
                    timer.setCountDown(3.0f);
                    timer.setStartTime(3.0f);
                    //timer.reset();
                }
            }
        }


        if(dialogue == DIALOGUETREE.GRAPPLING){
            timer.update(dt);
            if(timer.isFinished()){
                timer.reset();
                if(arrayPosition < grappleArray.length) {
                    currentString = grappleArray[arrayPosition];
                    arrayPosition++;
                } else {
                    dialogue = DIALOGUETREE.ENDING;
                    arrayPosition = 0;
                    timer.reset();
                }
            }
        }

        if(dialogue == DIALOGUETREE.ENDING){
            timer.update(dt);
            if(timer.isFinished()){
                timer.reset();
                if(arrayPosition < endArray.length) {
                    currentString = endArray[arrayPosition];
                    arrayPosition++;
                }
            }
        }

        if(dummy.state == Enemy.STATE.DYING){
            isTutorialComplete = true;
            if(dialogue != DIALOGUETREE.ENDING) {
                currentString = early_finish;
            } else {
                currentString = finish;
            }

            timer.reset();
        }
    }


    @Override
    public void draw(SpriteBatch batch) {
        super.draw(batch);

        //System.out.println(alphaPercentage);
        batch.end();

        switch(tutorial_state){
            case GROUND:
                Gdx.gl.glEnable(GL20.GL_BLEND);
                shapeRenderer.setProjectionMatrix(gamecam.combined);
                shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
                shapeRenderer.setColor(1,1,1, alphaPercentage);
                shapeRenderer.rect(0,0,WIDTH, 200);
                shapeRenderer.end();
                batch.begin();
                font.setColor(0,0,0,alphaPercentage);
                font.draw(batch, " Touch here to Move ", 0, 150, gamecam.viewportWidth, Align.center, true);
                break;

            case SHOOTING:
                Gdx.gl.glEnable(GL20.GL_BLEND);
                shapeRenderer.setProjectionMatrix(gamecam.combined);
                shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
                shapeRenderer.setColor(1,1,1, alphaPercentage);
                shapeRenderer.rect(0,200,WIDTH, HEIGHT);
                shapeRenderer.end();
                batch.begin();
                font.setColor(0,0,0,alphaPercentage);
                font.draw(batch, " Touch here to Shoot ", 0, gamecam.viewportHeight - 500, gamecam.viewportWidth, Align.center, true);
                break;

            case GRAPPLING:
                Gdx.gl.glEnable(GL20.GL_BLEND);

                shapeRenderer.setProjectionMatrix(gamecam.combined);
                shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
                shapeRenderer.setColor(1,1,1, alphaPercentage);

                for(GrapplePoint gp : grapplePoints){
                    shapeRenderer.rect(gp.getBounds().x, gp.getBounds().y, gp.getBounds().getWidth(), gp.getBounds().getHeight());
                }

                shapeRenderer.end();
                batch.begin();
                font.setColor(1,1,1,alphaPercentage);
                font.draw(batch, " Touch these to grapple ", 0, gamecam.viewportHeight - 300, gamecam.viewportWidth, Align.center, true);
                break;

            case LEAVING:
                Gdx.gl.glEnable(GL20.GL_BLEND);
                shapeRenderer.setProjectionMatrix(gamecam.combined);
                shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
                shapeRenderer.setColor(1,1,1, alphaPercentage);
                shapeRenderer.rect(WIDTH - 100,200,100, 400);
                shapeRenderer.end();
                batch.begin();
                font.setColor(1,1,1,alphaPercentage);
                font.draw(batch, " Leave through this door --> ", 500, gamecam.viewportHeight - 800, gamecam.viewportWidth, Align.center, true);
                break;

        }



/*        font.setColor(1,1,1,alphaPercentage);
        font.draw(batch, "<-- Touch this area to Shoot and Grapple objects -->", 0, gamecam.viewportHeight - 500, gamecam.viewportWidth, Align.center, true);
        font.draw(batch, "Leave through this door -->", 500, gamecam.viewportHeight - 800, gamecam.viewportWidth, Align.center, true);

        font.setColor(Color.RED);
        font.draw(batch, "<-- Touch the ground to Move and Avoid damage-->", 0, 150, gamecam.viewportWidth, Align.center, true);

        batch.end();
        batch.begin();*/

    }
}
