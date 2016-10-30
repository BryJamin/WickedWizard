package com.byrjamin.wickedwizard.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.byrjamin.wickedwizard.MainGame;
import com.byrjamin.wickedwizard.sprites.Player;
import com.byrjamin.wickedwizard.sprites.enemies.Blob;
import com.byrjamin.wickedwizard.sprites.enemies.Enemy;

import java.util.ArrayList;


/**
 * Created by Home on 15/10/2016.
 */
public class PlayScreen implements Screen {


    private MainGame game;

    private OrthographicCamera gamecam;

    Texture img;

    private Viewport gamePort;

    private Vector2 backgroundMovement;

    public static TextureAtlas atlas;

    GestureDetector gestureDetector;


    private boolean up;

    private Player player;

    private Blob blob1;
    private Blob blob2;


    Texture texture;

    Sprite sprite;
    Sprite swordCard;
    Sprite spellCard;

    ArrayList<Enemy> enemyArray;

    public PlayScreen(MainGame game){
        this.game = game;
        //texture = new Texture("rockbackground.png");

       gamecam = new OrthographicCamera();

        //Starts in the middle of the screen, on the 1/4 thingie.

        gamePort = new FitViewport(MainGame.GAME_WIDTH, MainGame.GAME_HEIGHT, gamecam);

        //Moves the gamecamer to the (0,0) position instead of being in the center.
        gamecam.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);

        gestureDetector = new GestureDetector(new gestures());

        img = new Texture("badlogic.jpg");

        atlas = new TextureAtlas(Gdx.files.internal("card.atlas"));
        swordCard = atlas.createSprite("card_sword");
        spellCard = atlas.createSprite("card_spell");


        player = new Player();
        blob1 = new Blob(600, 400);
        blob2 = new Blob(900, 400);

        enemyArray = new ArrayList<Enemy>();
        enemyArray.add(blob1);
        enemyArray.add(blob2);



    }

    public void handleInput(float dt){

        Gdx.input.setInputProcessor(gestureDetector);


    }

    public void update(float dt){
        handleInput(dt);

        gamecam.update();

    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        TextureAtlas atlas;
        atlas = new TextureAtlas(Gdx.files.internal("test.atlas"));
        //System.out.println(atlas.getRegions().toString());
        TextureAtlas.AtlasRegion region = atlas.findRegion("wiz");
        sprite = atlas.createSprite("wiz");
        //Updates the positions of all elements on the screen before they are redrawn.
        update(delta);

        //Sets the background color if nothing is on the screen.
        Gdx.gl.glClearColor(1, 0, 0, 0.5f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.setProjectionMatrix(gamecam.combined);

        game.batch.begin();

        game.batch.draw(img, -10, 0);


        game.batch.draw(region.getTexture(), 200,200);

        player.getSprite().draw(game.batch);

        for (Enemy e : enemyArray){
            e.draw(game.batch);
        }
/*        blob1.getSprite().draw(game.batch);
        blob2.getSprite().draw(game.batch);*/

        //game.batch.draw(pl);


        //game.batch.draw
        game.batch.draw(swordCard, 300,0, MainGame.GAME_UNITS * 10, MainGame.GAME_UNITS * 15);
        game.batch.draw(spellCard, 600,0, MainGame.GAME_UNITS * 10, MainGame.GAME_UNITS * 15);


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


            int x1 = Gdx.input.getX();
            int y1 = Gdx.input.getY();
            Vector3 input = new Vector3(x1, y1, 0);
            gamecam.unproject(input);

            System.out.println(input.x);
            System.out.println(enemyArray.get(0).getPosition().x);

            for(int i = 0; i < enemyArray.size(); i++){
                System.out.println("inside");
                if(enemyArray.get(i).getSprite().getBoundingRectangle().contains(input.x, input.y)){
                    System.out.println("OW said Blob " + i);
                    enemyArray.get(i).reduceHealth(1);
                }
            }


            System.out.println("Button:" + button);
            System.out.println("TOUCH DOWN PERFORMED");
            return true;
        }

        @Override
        public boolean tap(float x, float y, int count, int button) {
            System.out.println("TAP PERFORMED");
            return true;
        }

        @Override
        public boolean longPress(float x, float y) {
            System.out.println("LONGPRESS PERFORMED");
            return true;
        }

        @Override
        public boolean fling(float velocityX, float velocityY, int button) {
            System.out.println("FLING PERFORMED");
            System.out.println("VELOCITY X IS" + velocityX);
            System.out.println("VELOCITY Y IS" + velocityY);

            return true;
        }

        @Override
        public boolean pan(float x, float y, float deltaX, float deltaY) {
            return true;
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


