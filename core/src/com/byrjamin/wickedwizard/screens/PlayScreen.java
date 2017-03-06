package com.byrjamin.wickedwizard.screens;

import com.artemis.Entity;
import com.artemis.World;
import com.artemis.WorldConfiguration;
import com.artemis.WorldConfigurationBuilder;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.byrjamin.wickedwizard.MainGame;
import com.byrjamin.wickedwizard.assets.TextureStrings;
import com.byrjamin.wickedwizard.components.CollisionBoundComponent;
import com.byrjamin.wickedwizard.components.GravityComponent;
import com.byrjamin.wickedwizard.components.PlayerComponent;
import com.byrjamin.wickedwizard.components.PositionComponent;
import com.byrjamin.wickedwizard.components.TextureRegionComponent;
import com.byrjamin.wickedwizard.components.VelocityComponent;
import com.byrjamin.wickedwizard.components.WallComponent;
import com.byrjamin.wickedwizard.factories.EntityFactory;
import com.byrjamin.wickedwizard.helper.AbstractGestureDectector;
import com.byrjamin.wickedwizard.helper.Measure;
import com.byrjamin.wickedwizard.helper.RoomInputAdapter;
import com.byrjamin.wickedwizard.maps.Map;
import com.byrjamin.wickedwizard.maps.rooms.components.RoomWall;
import com.byrjamin.wickedwizard.systems.BulletSystem;
import com.byrjamin.wickedwizard.systems.EnemyCollisionSystem;
import com.byrjamin.wickedwizard.systems.GravitySystem;
import com.byrjamin.wickedwizard.systems.HealthSystem;
import com.byrjamin.wickedwizard.systems.MovementSystem;
import com.byrjamin.wickedwizard.systems.PlayerInputSystem;
import com.byrjamin.wickedwizard.systems.RenderingSystem;
import com.byrjamin.wickedwizard.systems.WallCollisionSystem;
import com.byrjamin.wickedwizard.systems.WallSystem;


//TODO


/**
 * Created by Home on 15/10/2016.
 */
public class PlayScreen extends AbstractScreen {

    private OrthographicCamera gamecam;

    private Viewport gamePort;

    public static TextureAtlas atlas;

    private Pixmap pixmap;

    private World world;

    GestureDetector gestureDetector;
    GestureDetector controlschemeDetector;

    Vector3 input = new Vector3();
    Vector3 touchDownInput = new Vector3();
    Vector3 touchUpInput = new Vector3();

    private boolean gameOver = false;

    BitmapFont font = new BitmapFont();

    Map map;

    private RoomInputAdapter roomInputAdapter;

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

/*       pixmap = new Pixmap(32, 32, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.drawCircle(15, 15, 10);


        Pixmap pm = new Pixmap(32, 32, Pixmap.Format.RGBA8888);
        Gdx.graphics.setCursor(Gdx.graphics.newCursor(pixmap, pm.getWidth() / 2, pm.getHeight() / 2));
        pm.dispose();*/

        //Starts in the middle of the screen, on the 1/4 thingie.

        //TODO Decide whetehr to have heath on the screen or have health off in like black space.
        gamePort = new FitViewport(MainGame.GAME_WIDTH, MainGame.GAME_HEIGHT, gamecam);

       // gamePort = new FitViewport(map.getActiveRoom().WIDTH, map.getActiveRoom().HEIGHT, gamecam);

        //Moves the gamecamer to the (0,0) position instead of being in the center.
        gamecam.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);

        roomInputAdapter = new RoomInputAdapter(map.getActiveRoom(), gamePort);


        WorldConfiguration config = new WorldConfigurationBuilder()
                .with(new MovementSystem(), new GravitySystem(),new PlayerInputSystem(gamecam),
                        new WallCollisionSystem(), new BulletSystem(), new HealthSystem(), new EnemyCollisionSystem(),
                        new RenderingSystem(game.batch, gamecam))
                .build();
        world = new World(config);

        EntityFactory.createPlayer(world);
        EntityFactory.createBlob(world, 300, 500);
        EntityFactory.createBlob(world, 500, 500);
        EntityFactory.createBlob(world, 700, 500);
        EntityFactory.createBlob(world, 900, 500);


        for(RoomWall rw : map.getActiveRoom().getRoomWalls()){
            EntityFactory.createWall(world, rw.getBounds());
        }
    }

    public void handleInput(float dt){

        //System.out.println("????");
        InputMultiplexer multiplexer = new InputMultiplexer();

        if(!gameOver) {
            roomInputAdapter.update(map.getActiveRoom(), gamePort, gamecam);
            multiplexer.addProcessor(controlschemeDetector);
            multiplexer.addProcessor(roomInputAdapter);
        } else {
            multiplexer.addProcessor(gestureDetector);
        }

        //Gdx.input.setInputProcessor(multiplexer);


    }

    public void update(float dt){

        //TODO look into proper ways ot do delta time capping, or just make it that on desktop if the mouse is
        //TODO touching the screen pause the game.
        //caps the delta time if the game is paused for some reason.

        if(dt > 0.20) {
            System.out.println(" dt" + dt);
        }

        if(dt < 0.20f) {

            gamecam.position.set((int) map.getActiveRoom().getWizard().getCenterX(),(int) map.getActiveRoom().getWizard().getCenterY(), 0);

            if(gamecam.position.x <= gamePort.getWorldWidth() / 2){
                gamecam.position.set(gamePort.getWorldWidth() / 2,gamecam.position.y, 0);
            } else if(gamecam.position.x + gamecam.viewportWidth / 2 >= map.getActiveRoom().WIDTH){
                gamecam.position.set((int) (map.getActiveRoom().WIDTH - gamecam.viewportWidth / 2),(int) gamecam.position.y, 0);
            }

            if(gamecam.position.y <= gamePort.getWorldHeight() / 2){
                gamecam.position.set(gamecam.position.x, gamePort.getWorldHeight() / 2, 0);
            } else if(gamecam.position.y + gamecam.viewportHeight / 2 >= map.getActiveRoom().HEIGHT){
                gamecam.position.set((int) gamecam.position.x,(int) (map.getActiveRoom().HEIGHT - gamecam.viewportHeight / 2), 0);
            }

            gamecam.update();


            handleInput(dt);
            map.update(dt, gamecam);
            if (map.getActiveRoom().getWizard().isDead()) {
                gameOver = true;
            }
        }
        //handleInput(dt);
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
            game.batch.draw(atlas.findRegion("sprite_health0"), gamecam.position.x - (gamecam.viewportWidth / 2) + (100 * i), gamecam.position.y + (gamecam.viewportHeight / 2) - 220,MainGame.GAME_UNITS * 5, MainGame.GAME_UNITS * 5);
        }


        if(gameOver){
            font.draw(game.batch, "You died :[\nTap to restart", 550, gamecam.viewportHeight - 500, Measure.units(40), Align.center, true);
        }

        game.batch.end();


        if(delta < 0.20f) {
            world.setDelta(delta);
        }

        world.process();

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
       //     System.out.println("longpress");
//            map.getActiveRoom().getWizard().switchControlScheme();
            return true;
        }

        public boolean fling(float velocityX, float velocityY, int button) {
/*


            if(Math.abs(velocityX) > 5000) {
                map.getActiveRoom().getWizard().switchControlScheme();
                System.out.println("fling x velocity :" +velocityX);
            }
*/

            return false;
        }

    }


}


