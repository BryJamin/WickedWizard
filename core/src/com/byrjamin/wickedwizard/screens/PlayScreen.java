package com.byrjamin.wickedwizard.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
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
import com.byrjamin.wickedwizard.cards.Card;
import com.byrjamin.wickedwizard.cards.Spell;
import com.byrjamin.wickedwizard.cards.SpellManager;
import com.byrjamin.wickedwizard.cards.Sword;
import com.byrjamin.wickedwizard.cards.spellanims.Projectile;
import com.byrjamin.wickedwizard.sprites.Player;
import com.byrjamin.wickedwizard.sprites.enemies.Blob;
import com.byrjamin.wickedwizard.sprites.enemies.Enemy;
import com.byrjamin.wickedwizard.sprites.enemies.EnemySpawner;

import java.util.ArrayList;


//TODO


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

    private float timeAux;

    private Projectile projectile;


    Texture texture;

    Sprite sprite;
    Sprite swordCard;
    Sprite spellCard;

    SpellManager spellManager;

    ShapeRenderer sr;

    EnemySpawner enemySpawner;

    ArrayList<Card> deck;

    public PlayScreen(MainGame game){
        this.game = game;
        //texture = new Texture("rockbackground.png");

       gamecam = new OrthographicCamera();
        sr = new ShapeRenderer();

        projectile = new Projectile();

        //Starts in the middle of the screen, on the 1/4 thingie.

        gamePort = new FitViewport(MainGame.GAME_WIDTH, MainGame.GAME_HEIGHT, gamecam);

        //Moves the gamecamer to the (0,0) position instead of being in the center.
        gamecam.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);

        gestureDetector = new GestureDetector(new gestures());

        atlas = new TextureAtlas(Gdx.files.internal("sprite.atlas"));

        spellManager = new SpellManager();



        player = new Player();

        enemySpawner = new EnemySpawner();
        enemySpawner.startSpawningBlobs(MainGame.GAME_WIDTH, (int) player.getPosition().y);

        Spell spell = new Spell(600,0, Card.CardType.FIRE);
        Sword sword = new Sword(300,0, Card.CardType.FIRE);


        deck = new ArrayList<Card>();
        deck.add(spell);
        deck.add(sword);




    }

    public void handleInput(float dt){

        Gdx.input.setInputProcessor(gestureDetector);


    }

    public void update(float dt){
        handleInput(dt);
        enemySpawner.update(dt, player);
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
        Gdx.gl.glClearColor(1, 0, 0, 0.5f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.setProjectionMatrix(gamecam.combined);
        game.batch.begin();
        player.draw(game.batch);


        for(Card c : deck){
            c.draw(game.batch);
        }

        enemySpawner.draw(game.batch);

        game.batch.end();


        sr.setProjectionMatrix(gamecam.combined);
        spellManager.drawSpellSlots(sr);

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

            //This is so inputs match up to the game co-ordinates.
            gamecam.unproject(input);

            System.out.println(input.x);

            projectile.calculateAngle(player.getPosition().x, player.getPosition().y, input.x, input.y);
            projectile.calculateLine(player.getPosition().x, player.getPosition().y, input.x, input.y);


            //TODO when spells are cast that fire at their target,
            //Therefor they have their own properties and can check if they hit or not
            //this is temporary but will most liley not be used.
            if(enemySpawner.hitScan(spellManager.castSpells(),input.x, input.y)){
                spellManager.resetSpell();
            };

            //Checks which card the player tapped on
            //Stores the card inside the spell Manager.
            for(int i = 0; i < deck.size(); i++){
                System.out.println("inside");
                if(deck.get(i).getSprite().getBoundingRectangle().contains(input.x, input.y)){
                    System.out.println("You used me said Card " + i);
                    spellManager.add(deck.get(i));
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


