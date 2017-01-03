package com.byrjamin.wickedwizard.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.byrjamin.wickedwizard.MainGame;
import com.byrjamin.wickedwizard.maps.Map;
import com.byrjamin.wickedwizard.player.ActiveBullets;
import com.byrjamin.wickedwizard.enemy.EnemyBullets;
import com.byrjamin.wickedwizard.spelltypes.blastwaves.BlastWave;


//TODO


/**
 * Created by Home on 15/10/2016.
 */
public class PlayScreen implements Screen {


    private MainGame game;

    private OrthographicCamera gamecam;

    public static int GROUND_Y = 200;

    private Viewport gamePort;

    public static TextureAtlas atlas;

    GestureDetector gestureDetector;

    Vector3 input = new Vector3();
    Vector3 touchDownInput = new Vector3();
    Vector3 touchUpInput = new Vector3();

    Map map;


    //TODO IF you ever click in the deck area don't cast any spells

    public PlayScreen(MainGame game){
        this.game = game;

        gestureDetector = new GestureDetector(new gestures());

        atlas = new TextureAtlas(Gdx.files.internal("sprite.atlas"));

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
            multiplexer.addProcessor(gestureDetector);
            multiplexer.addProcessor(new InputAdapter() {

                @Override
                public boolean touchDown(int x, int y, int pointer, int button) {


                    //System.out.println("TOUCHDOWN");

                    float x1 = Gdx.input.getX();
                    float y1 = Gdx.input.getY();
                    input = new Vector3(x1, y1, 0);
                    touchDownInput = new Vector3(x1, y1, 0);

                    gamePort.unproject(input);
                    gamePort.unproject(touchDownInput);

                    boolean tapped = map.getActiveRoom().tapArrow(input.x, input.y);



                    if (!tapped) {

                        if(input.y <= map.getActiveRoom().groundHeight()){
                            map.getActiveRoom().getWizard().dash(input.x);
                        } else {
                            map.getActiveRoom().getWizard().startFiring();
                        }
                    }


                    //This is so inputs match up to the game co-ordinates.
                    // your touch down code here
                    return true; // return true to indicate the event was handled
                }

                @Override
                public boolean touchUp(int x, int y, int pointer, int button) {

                    float x1 = Gdx.input.getX();
                    float y1 = Gdx.input.getY();
                    touchUpInput = new Vector3(x1, y1, 0);

                    gamePort.unproject(touchUpInput);

                    if (map.getActiveRoom().getWizard().isCharing()) {
                        map.getActiveRoom().getWizard().dispel(touchDownInput, touchUpInput);
                    }

                    map.getActiveRoom().getWizard().stopFiring();
                    // your touch down code here
                    return true; // return true to indicate the event was handled
                }


            });

            Gdx.input.setInputProcessor(multiplexer);

        } else {
            Gdx.input.setInputProcessor(null);
        }

    }

    public void update(float dt){
        handleInput(dt);
        map.update(dt, gamecam);
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

    public class gestures implements GestureDetector.GestureListener {


        @Override
        public boolean touchDown(float x, float y, int pointer, int button) {

            return false;
        }

        @Override
        public boolean tap(float x, float y, int count, int button) {

/*            int x1 = Gdx.input.getX();
            int y1 = Gdx.input.getY();
            Vector3 input = new Vector3(x1, y1, 0);

            //This is so inputs match up to the game co-ordinates.
            gamePort.unproject(input);


            //System.out.println("Is wizard not firing?" + !map.getActiveRoom().getWizard().isFiring());

            map.getActiveRoom().shift(input.x);
            map.getActiveRoom().getWizard().stopFiring();

            System.out.println("X is " + input.x);
            System.out.println("Y is " + input.y);*/

            //map.getActiveRoom().itemGet(input.x, input.y);
            return false;
        }

        @Override
        public boolean longPress(float x, float y) {
            System.out.println("LongPress");
            return true;
        }

        @Override
        public boolean fling(float velocityX, float velocityY, int button) {

            return false;
        }

        @Override
        public boolean pan(float x, float y, float deltaX, float deltaY) {

            return false;
        }


        @Override
        public boolean panStop(float x, float y, int pointer, int button) {
            return false;
        }

        @Override
        public boolean zoom(float initialDistance, float distance) {
            return false;
        }

        @Override
        public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
            return false;
        }

    }


}


