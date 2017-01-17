package com.byrjamin.wickedwizard.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.byrjamin.wickedwizard.MainGame;
import com.byrjamin.wickedwizard.helper.AbstractGestureDectector;
import com.byrjamin.wickedwizard.helper.Measure;
import com.byrjamin.wickedwizard.maps.Map;


//TODO


/**
 * Created by Home on 15/10/2016.
 */
public class PlayScreen extends AbstractScreen {

    private OrthographicCamera gamecam;

    private Viewport gamePort;

    public static TextureAtlas atlas;

    GestureDetector gestureDetector;
    GestureDetector controlschemeDetector;

    Vector3 input = new Vector3();
    Vector3 touchDownInput = new Vector3();
    Vector3 touchUpInput = new Vector3();

    private boolean gameOver = false;

    BitmapFont font = new BitmapFont();

    Map map;
    private GestureDetector gameOvergestureDetector;


    //TODO IF you ever click in the deck area don't cast any spells

    public PlayScreen(MainGame game){
        super(game);

        gestureDetector = new GestureDetector(new gestures());
        controlschemeDetector = new GestureDetector(new controlSchemeGesture());


        font.getData().setScale(5, 5);

        atlas = game.manager.get("sprite.atlas", TextureAtlas.class);

        map = new Map();
        gamecam = new OrthographicCamera();

        //Starts in the middle of the screen, on the 1/4 thingie.

        //TODO Decide whetehr to have heath on the screen or have health off in like black space.
        gamePort = new FitViewport(map.getActiveRoom().WIDTH, map.getActiveRoom().HEIGHT, gamecam);

        //Moves the gamecamer to the (0,0) position instead of being in the center.
        gamecam.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);

    }

    public void handleInput(float dt){

        if(!map.isTransitioning()) {

            InputMultiplexer multiplexer = new InputMultiplexer();

            if(!gameOver) {
                multiplexer.addProcessor(controlschemeDetector);
                multiplexer.addProcessor(new InputAdapter() {

                    @Override
                    public boolean touchDown(int x, int y, int pointer, int button) {


                        System.out.println("TOUCHDOWN");

                        float x1 = Gdx.input.getX(pointer);
                        float y1 = Gdx.input.getY(pointer);
                        input = new Vector3(x1, y1, 0);
                        touchDownInput = new Vector3(x1, y1, 0);

                        gamePort.unproject(input);
                        gamePort.unproject(touchDownInput);

                        boolean tapped = map.getActiveRoom().tapArrow(input.x, input.y);


                        System.out.println(pointer);

                        if (!tapped) {

                            if (input.y <= map.getActiveRoom().groundHeight()) {
                                map.getActiveRoom().getWizard().dash(input.x);
                                System.out.println("Inside dash");
                            } else {
                                map.getActiveRoom().getWizard().startFiring(pointer);
                            }
                        }


                        //This is so inputs match up to the game co-ordinates.
                        // your touch down code here
                        return true; // return true to indicate the event was handled
                    }

                    @Override
                    public boolean touchUp(int x, int y, int pointer, int button) {

                        System.out.println(pointer);

                        float x1 = Gdx.input.getX();
                        float y1 = Gdx.input.getY();
                        touchUpInput = new Vector3(x1, y1, 0);

                        gamePort.unproject(touchUpInput);

                        if(map.getActiveRoom().getWizard().getInputPoll() == pointer)
                        map.getActiveRoom().getWizard().stopFiring();

                        // your touch down code here
                        return true; // return true to indicate the event was handled
                    }


                });

            } else {
                multiplexer.addProcessor(gestureDetector);
            }

            Gdx.input.setInputProcessor(multiplexer);

        } else {
            Gdx.input.setInputProcessor(null);
        }

    }

    public void update(float dt){
        handleInput(dt);
        map.update(dt, gamecam);
        if(map.getActiveRoom().getWizard().isDead()){
            gameOver = true;
        }
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        //System.out.println(atlas.getRegions().toString());
        //Updates the positions of all elements on the screen before they are redrawn.
        update(delta);



        //Sets the background color if nothing is on the screen.
        Gdx.gl.glClearColor(0, 0, 0, 0.5f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.setProjectionMatrix(gamecam.combined);
        game.batch.begin();

        map.draw(game.batch);

        for(int i = 1; i <= map.getActiveRoom().getWizard().getHealth(); i++){
            game.batch.draw(atlas.findRegion("sprite_health0"), (100 * i), map.getActiveRoom().HEIGHT - 150,MainGame.GAME_UNITS * 5, MainGame.GAME_UNITS * 5);
        }


        if(gameOver){
            font.draw(game.batch, "You died :[\nTap to restart", 550, gamecam.viewportHeight - 500, Measure.units(40), Align.center, true);

        }

        game.batch.end();

    }

    @Override
    public void resize(int width, int height) {
        //Updates the view port to the designated width and height.
        gamePort.update(width, height);

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }

    public class gestures extends AbstractGestureDectector {


        @Override
        public boolean touchDown(float x, float y, int pointer, int button) {

            return false;
        }

        @Override
        public boolean tap(float x, float y, int count, int button) {

            if(gameOver){
                map = new Map();
                gameOver = false;
            }

            return true;
        }

        @Override
        public boolean longPress(float x, float y) {

            return true;
        }

    }

    public class controlSchemeGesture extends AbstractGestureDectector {

        @Override
        public boolean longPress(float x, float y) {
            System.out.println("longpress");
            map.getActiveRoom().getWizard().switchControlScheme();
            return true;
        }

    }


}


