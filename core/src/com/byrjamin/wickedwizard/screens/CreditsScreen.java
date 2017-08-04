package com.byrjamin.wickedwizard.screens;

import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.byrjamin.wickedwizard.MainGame;
import com.byrjamin.wickedwizard.assets.FileLocationStrings;
import com.byrjamin.wickedwizard.assets.PreferenceStrings;
import com.byrjamin.wickedwizard.ecs.systems.input.ActionOnTouchSystem;
import com.byrjamin.wickedwizard.screens.world.CreditsWorld;
import com.byrjamin.wickedwizard.utils.AbstractGestureDectector;
import com.byrjamin.wickedwizard.assets.Assets;

//TODO


/**
 * Created by Home on 26/07/2017.
 */
public class CreditsScreen extends AbstractScreen {

    private OrthographicCamera gamecam;

    private Viewport gameport;

    private TextureAtlas atlas;
    private AssetManager manager;

    private World world;

    private Entity startTutorial;

    private Entity boundOption;
    private Entity godOption;

    private Entity musicSetting;

    private Entity soundSetting;
    private Entity bossStartbutton;



    GestureDetector gestureDetector;

    private Preferences settings;
    private Preferences devToolPrefs;
    private CreditsWorld creditsWorld;

    //TODO IF you ever click in the deck area don't cast any spells

    public CreditsScreen(MainGame game) {
        super(game);

        settings = Gdx.app.getPreferences(PreferenceStrings.SETTINGS);
        devToolPrefs = Gdx.app.getPreferences(PreferenceStrings.DEV_MODE_PREF_KEY);

        gestureDetector = new GestureDetector(new CreditsScreenGestures());
        manager = game.assetManager;
        atlas = game.assetManager.get(FileLocationStrings.spriteAtlas, TextureAtlas.class);
        Assets.initialize(game.assetManager);

        gamecam = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        gameport = new FitViewport(MainGame.GAME_WIDTH, MainGame.GAME_HEIGHT, gamecam);


        gamecam.position.set(gameport.getWorldWidth() / 2, gameport.getWorldHeight() / 2, 0);

        creditsWorld = new CreditsWorld(game, gameport);

    }

    public TextureAtlas getAtlas() {
        return atlas;
    }


    public void handleInput(float dt) {
        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(gestureDetector);
        Gdx.input.setInputProcessor(multiplexer);
    }



    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

        //Sets the background color if nothing is on the screen.
        Gdx.gl.glClearColor(0, 0, 0, 0.5f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.setProjectionMatrix(gamecam.combined);

//        handleInput(world.delta);
        handleInput(delta);
        creditsWorld.process(delta);
    }

    @Override
    public void resize(int width, int height) {
        //Updates the view port to the designated width and height.
        gameport.update(width, height);

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


    private class CreditsScreenGestures extends AbstractGestureDectector {

        @Override
        public boolean tap(float x, float y, int count, int button) {
            Vector3 touchInput = gameport.unproject(new Vector3(x, y, 0));
            creditsWorld.getWorld().getSystem(ActionOnTouchSystem.class).touch(touchInput.x, touchInput.y);
            return true;
        }

    }

}


