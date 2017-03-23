package com.byrjamin.wickedwizard.screens;

import com.artemis.Component;
import com.artemis.Entity;
import com.artemis.World;
import com.artemis.WorldConfiguration;
import com.artemis.WorldConfigurationBuilder;
import com.artemis.utils.Bag;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.byrjamin.wickedwizard.MainGame;
import com.byrjamin.wickedwizard.ecs.systems.ActiveOnTouchSystem;
import com.byrjamin.wickedwizard.ecs.systems.BoundsDrawingSystem;
import com.byrjamin.wickedwizard.ecs.systems.MoveToSystem;
import com.byrjamin.wickedwizard.factories.Arena;
import com.byrjamin.wickedwizard.factories.ArenaGUI;
import com.byrjamin.wickedwizard.factories.BackgroundFactory;
import com.byrjamin.wickedwizard.factories.EntityFactory;
import com.byrjamin.wickedwizard.factories.JigsawGenerator;
import com.byrjamin.wickedwizard.factories.RoomFactory;
import com.byrjamin.wickedwizard.helper.AbstractGestureDectector;
import com.byrjamin.wickedwizard.helper.Measure;
import com.byrjamin.wickedwizard.helper.RoomInputAdapter;
import com.byrjamin.wickedwizard.maps.Map;
import com.byrjamin.wickedwizard.maps.MapCoords;
import com.byrjamin.wickedwizard.maps.MapJigsawGenerator;
import com.byrjamin.wickedwizard.maps.rooms.Room;
import com.byrjamin.wickedwizard.maps.rooms.components.RoomWall;
import com.byrjamin.wickedwizard.ecs.systems.AnimationSystem;
import com.byrjamin.wickedwizard.ecs.systems.BlinkSystem;
import com.byrjamin.wickedwizard.ecs.systems.BounceCollisionSystem;
import com.byrjamin.wickedwizard.ecs.systems.BulletSystem;
import com.byrjamin.wickedwizard.ecs.systems.DoorSystem;
import com.byrjamin.wickedwizard.ecs.systems.EnemyCollisionSystem;
import com.byrjamin.wickedwizard.ecs.systems.FindPlayerSystem;
import com.byrjamin.wickedwizard.ecs.systems.FiringAISystem;
import com.byrjamin.wickedwizard.ecs.systems.GrappleSystem;
import com.byrjamin.wickedwizard.ecs.systems.GravitySystem;
import com.byrjamin.wickedwizard.ecs.systems.HealthSystem;
import com.byrjamin.wickedwizard.ecs.systems.MoveToPlayerAISystem;
import com.byrjamin.wickedwizard.ecs.systems.MovementSystem;
import com.byrjamin.wickedwizard.ecs.systems.PlayerInputSystem;
import com.byrjamin.wickedwizard.ecs.systems.RenderingSystem;
import com.byrjamin.wickedwizard.ecs.systems.RoomTransitionSystem;
import com.byrjamin.wickedwizard.ecs.systems.StateSystem;
import com.byrjamin.wickedwizard.ecs.systems.GroundCollisionSystem;

import java.util.Random;


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

    private ArenaGUI arenaGUI;

    private RoomInputAdapter roomInputAdapter;

    private GestureDetector gameOvergestureDetector;

    private Array<Arena> testArray;


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

        Random random = new Random();
        JigsawGenerator jg = new JigsawGenerator(10,random);

        testArray = jg.generateJigsaw();
        Arena b = jg.getStartingRoom();
        RoomFactory.cleanArenas(testArray);

        int i = 0;
        for(Arena a : testArray){
            System.out.println(" coords fors arena " + i + " " + a.cotainingCoords);
                    i++;
        }

        MapJigsawGenerator mjg = new MapJigsawGenerator(10, random);

        i = 0;
        for(Room r : mjg.generateJigsaw()){
            System.out.println("Room coords for room " + i + " " + r.getMapCoordsArray());
            i++;
        }

        RoomTransitionSystem rts = new RoomTransitionSystem(b, testArray);

        WorldConfiguration config = new WorldConfigurationBuilder()
                .with(
                        new ActiveOnTouchSystem(),
                        new AnimationSystem(),
                        new BlinkSystem(),
                        new BounceCollisionSystem(),
                        new BulletSystem(),
                        new EnemyCollisionSystem(),
                        new FindPlayerSystem(),
                        new FiringAISystem(),
                        new GrappleSystem(),
                        new GravitySystem(),
                        new GroundCollisionSystem(),
                        new HealthSystem(),
                        new MovementSystem(),
                        new MoveToSystem(),
                        new MoveToPlayerAISystem(),
                        new PlayerInputSystem(gamecam),
                        new StateSystem(),
                        new RenderingSystem(game.batch, gamecam),
                        new BoundsDrawingSystem(),
                        rts,
                        new DoorSystem())
                .build();
        world = new World(config);
        for(Bag<Component> bag : b.getBagOfEntities()){
            Entity entity = world.createEntity();
            for(Component comp : bag){
                entity.edit().add(comp);
            }
        }

        Entity entity = world.createEntity();
        for(Component comp : EntityFactory.playerBag()){
            entity.edit().add(comp);
        }



        arenaGUI = new ArenaGUI(0,0,testArray, b);


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
        //update(delta);



        //Sets the background color if nothing is on the screen.
        Gdx.gl.glClearColor(0, 0, 0, 0.5f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.setProjectionMatrix(gamecam.combined);


/*        game.batch.begin();

       // map.draw(game.batch);


        for(int i = 1; i <= map.getActiveRoom().getWizard().getHealth(); i++){
            game.batch.draw(atlas.findRegion("sprite_health0"), gamecam.position.x - (gamecam.viewportWidth / 2) + (100 * i), gamecam.position.y + (gamecam.viewportHeight / 2) - 220,MainGame.GAME_UNITS * 5, MainGame.GAME_UNITS * 5);
        }


        if(gameOver){
            font.draw(game.batch, "You died :[\nTap to restart", 550, gamecam.viewportHeight - 500, Measure.units(40), Align.center, true);
        }

        game.batch.end();*/


        if(delta < 0.20f) {
            world.setDelta(delta);
        }

        world.process();


        arenaGUI.update(world.delta,
                gamecam,
                world.getSystem(RoomTransitionSystem.class).getRoomArray(),
                world.getSystem(RoomTransitionSystem.class).getCurrentArena());

        arenaGUI.draw(game.batch);

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


