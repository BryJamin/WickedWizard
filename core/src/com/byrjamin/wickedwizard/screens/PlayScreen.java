package com.byrjamin.wickedwizard.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.byrjamin.wickedwizard.MainGame;
import com.byrjamin.wickedwizard.arenas.Arena;
import com.byrjamin.wickedwizard.deck.Deck;
import com.byrjamin.wickedwizard.arenas.ActiveBullets;
import com.byrjamin.wickedwizard.arenas.EnemyBullets;
import com.byrjamin.wickedwizard.deck.cards.spelltypes.Explosion;
import com.byrjamin.wickedwizard.deck.cards.spelltypes.Projectile;
import com.byrjamin.wickedwizard.sprites.enemies.Blob;


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

    private ActiveBullets activeBullets;

    Vector3 input;

    Arena arena;

    EnemyBullets enemyBullets;

 //   Deck deck;

    //TODO IF you ever click in the deck area don't cast any spells

    public PlayScreen(MainGame game){
        this.game = game;

        gamecam = new OrthographicCamera();

        //Starts in the middle of the screen, on the 1/4 thingie.

        gamePort = new FitViewport(MainGame.GAME_WIDTH, MainGame.GAME_HEIGHT, gamecam);

        //Moves the gamecamer to the (0,0) position instead of being in the center.
        gamecam.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);

        activeBullets = new ActiveBullets();

        gestureDetector = new GestureDetector(new gestures());

        atlas = new TextureAtlas(Gdx.files.internal("sprite.atlas"));
        arena = new Arena();
        enemyBullets = new EnemyBullets();
      //  deck = new Deck();




    }

    public void handleInput(float dt){

        Gdx.input.setInputProcessor(gestureDetector);

        if(Gdx.input.isTouched()) {
            float x1 = Gdx.input.getX();
            float y1 = Gdx.input.getY();
            input = new Vector3(x1, y1, 0);

            //This is so inputs match up to the game co-ordinates.
            gamecam.unproject(input);

            arena.addProjectile(input.x, input.y);

            if(input.y > PlayScreen.GROUND_Y) {
                if(arena.getWizard().getReloader().isReady()){
                }
            }

        }

    }

    public void update(float dt){
        handleInput(dt);
        arena.update(dt, gamecam);
       // deck.update(dt);
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

        arena.draw(game.batch);
        //deck.draw(game.batch);


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

            return true;
        }

        @Override
        public boolean tap(float x, float y, int count, int button) {


            int x1 = Gdx.input.getX();
            int y1 = Gdx.input.getY();
            Vector3 input = new Vector3(x1, y1, 0);

            //This is so inputs match up to the game co-ordinates.
            gamecam.unproject(input);

            //deck.cardSelect(input.x, input.y);


            //enemyBullets.dispellProjectiles();
/*            if(count == 2){
                wizard.teleport(input.x, input.y);
            }*/

/*            if(deck.getSelectedCard().getProjectileType() == Card.ProjectileType.HUMAN && input.y > PlayScreen.GROUND_Y){
                wizard.teleport(input.x, input.y);
            }*/
            return true;
        }

        @Override
        public boolean longPress(float x, float y) {

            System.out.println("LONG PRESS PERFORMED");
            return true;
        }

        @Override
        public boolean fling(float velocityX, float velocityY, int button) {


            arena.dispell(velocityX, velocityY);

            System.out.println("FLING PERFORMED");
            return true;
        }

        @Override
        public boolean pan(float x, float y, float deltaX, float deltaY) {

            System.out.println("PAN PERFORMED");

            return true;
        }


        @Override
        public boolean panStop(float x, float y, int pointer, int button) {
            System.out.println("PAN STOP PERFORMED");

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


