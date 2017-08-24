package com.byrjamin.wickedwizard.screens;

import com.artemis.Entity;
import com.artemis.World;
import com.artemis.WorldConfiguration;
import com.artemis.WorldConfigurationBuilder;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.byrjamin.wickedwizard.MainGame;
import com.byrjamin.wickedwizard.assets.FileLocationStrings;
import com.byrjamin.wickedwizard.assets.MenuStrings;
import com.byrjamin.wickedwizard.assets.PreferenceStrings;
import com.byrjamin.wickedwizard.assets.TextureStrings;
import com.byrjamin.wickedwizard.ecs.components.ai.Action;
import com.byrjamin.wickedwizard.ecs.systems.audio.MusicSystem;
import com.byrjamin.wickedwizard.ecs.systems.graphical.StateSystem;
import com.byrjamin.wickedwizard.ecs.systems.input.ActionOnTouchSystem;
import com.byrjamin.wickedwizard.ecs.systems.physics.GravitySystem;
import com.byrjamin.wickedwizard.ecs.systems.physics.CollisionSystem;
import com.byrjamin.wickedwizard.factories.arenas.challenges.ChallengesResource;
import com.byrjamin.wickedwizard.screens.world.menu.DevModeMenuWorld;
import com.byrjamin.wickedwizard.screens.world.menu.ItemDisplayWorldContainer;
import com.byrjamin.wickedwizard.screens.world.menu.MenuBackDropWorld;
import com.byrjamin.wickedwizard.screens.world.menu.SettingsWorld;
import com.byrjamin.wickedwizard.screens.world.menu.ChallengesWorldContainer;
import com.byrjamin.wickedwizard.utils.AbstractGestureDectector;
import com.byrjamin.wickedwizard.assets.Assets;
import com.byrjamin.wickedwizard.ecs.systems.graphical.BoundsDrawingSystem;
import com.byrjamin.wickedwizard.ecs.systems.graphical.AnimationSystem;
import com.byrjamin.wickedwizard.ecs.systems.physics.MovementSystem;
import com.byrjamin.wickedwizard.ecs.systems.graphical.RenderingSystem;
import com.byrjamin.wickedwizard.utils.CenterMath;
import com.byrjamin.wickedwizard.utils.GameDelta;
import com.byrjamin.wickedwizard.utils.Measure;

//TODO


/**
 * Created by Home on 15/10/2016.
 */
public class MenuScreen extends AbstractScreen {

    private OrthographicCamera gamecam;

    private Viewport gameport;

    private TextureAtlas atlas;
    private AssetManager manager;

    private World world;

    private DevModeMenuWorld devModeMenuWorld;
    private SettingsWorld settingsWorld;
    private MenuBackDropWorld menuBackDropWorld;
    private ChallengesWorldContainer challengesWorldContainer;
    private ItemDisplayWorldContainer itemDisplayWorldContainer;

    private GestureDetector gestureDetector;
    private GestureDetector settingsDetector;
    private GestureDetector backDropDetector;
    private GestureDetector trailsWorldDectector;
    private GestureDetector itemsDisplayWorldDectector;

    private Preferences devSettings;
    private Preferences preferences;

    private static final float buttonWidth = Measure.units(30f);
    private static final float buttonHeight = Measure.units(10f);


    private static final Color buttonForeground = new Color(Color.BLACK);
    private static final Color buttonBackground = new Color(Color.WHITE);



    public enum MenuType {
        MAIN, DEV, SETTING, CHALLENGES, ITEMS;
    }

    private MenuType menuType;


    //TODO IF you ever click in the deck area don't cast any spells

    public MenuScreen(MainGame game) {
        super(game);

        devSettings = Gdx.app.getPreferences(PreferenceStrings.DEV_MODE_PREF_KEY);
        preferences = Gdx.app.getPreferences(PreferenceStrings.DATA_PREF_KEY);
        String menuTypeName = Gdx.app.getPreferences(PreferenceStrings.DEV_MODE_PREF_KEY).getString(PreferenceStrings.DEV_MENU_IS_DEV, MenuType.DEV.name());

        menuType = menuTypeName.equals(MenuType.DEV.name()) ? MenuType.DEV : MenuType.MAIN;

        gestureDetector = new GestureDetector(new MainMenuGestures());
        manager = game.assetManager;
        atlas = game.assetManager.get(FileLocationStrings.spriteAtlas, TextureAtlas.class);
        Assets.initialize(game.assetManager);

        gamecam = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        gameport = new FitViewport(MainGame.GAME_WIDTH, MainGame.GAME_HEIGHT, gamecam);
        gamecam.position.set(gameport.getWorldWidth() / 2, gameport.getWorldHeight() / 2, 0);

        createMenu();

        devModeMenuWorld = new DevModeMenuWorld(game, gameport);
        settingsWorld = new SettingsWorld(game, gameport);
        menuBackDropWorld = new MenuBackDropWorld(game, gameport);
        challengesWorldContainer = new ChallengesWorldContainer(game, gameport);
        itemDisplayWorldContainer = new ItemDisplayWorldContainer(game, gameport);
        settingsDetector = new GestureDetector(settingsWorld);
        backDropDetector = new GestureDetector(menuBackDropWorld);
        trailsWorldDectector = new GestureDetector(challengesWorldContainer);
        itemsDisplayWorldDectector = new GestureDetector(itemDisplayWorldContainer);
        Gdx.input.setCatchBackKey(false);

    }

    public TextureAtlas getAtlas() {
        return atlas;
    }


    public void handleInput(float dt) {
        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(gestureDetector);
        multiplexer.addProcessor(backDropDetector);

        switch (menuType){
            case SETTING: multiplexer.addProcessor(settingsDetector);
                break;
            case CHALLENGES: multiplexer.addProcessor(trailsWorldDectector);
            default:
                break;
            case ITEMS: multiplexer.addProcessor(itemsDisplayWorldDectector);
                break;
        }
        //multiplexer.addProcessor(gestureDetector);
        Gdx.input.setInputProcessor(multiplexer);
    }


    public void createMenu(){
        WorldConfiguration config = new WorldConfigurationBuilder()
                .with(WorldConfigurationBuilder.Priority.HIGHEST,
                        new MovementSystem()
                )
                .with(WorldConfigurationBuilder.Priority.HIGH,
                        new AnimationSystem(),
                        new StateSystem(),
                        new CollisionSystem(),
                        new ActionOnTouchSystem(),
                        new GravitySystem())
                .with(WorldConfigurationBuilder.Priority.LOW,
                        new RenderingSystem(game.batch, manager, gameport),
                        new BoundsDrawingSystem(),
                        new MusicSystem(manager)

                )
                .build();



        world = new World(config);
        world.getSystem(MusicSystem.class).playMainMenuMusic();


        boolean isTutorialComplete = DataSave.isDataAvailable(ChallengesResource.TUTORIAL_COMPLETE);

        if(isTutorialComplete){
            setUpMenuScreenStartAndTutorial();
        } else {
            setUpMenuScreenOnlyTutorial();
        }


    }

    private void setUpMenuScreenOnlyTutorial(){

        MenuButton.MenuButtonBuilder menuButtonBuilder = new MenuButton.MenuButtonBuilder(Assets.medium, atlas.findRegion(TextureStrings.BLOCK))
                .width(buttonWidth)
                .height(buttonHeight)
                .foregroundColor(buttonForeground)
                .backgroundColor(buttonBackground)
                .action(new Action() {
                    @Override
                    public void performAction(World world, Entity e) {
                        game.setScreen(new PlayScreen(game, new PlayScreenConfig(PlayScreenConfig.Spawn.TUTORIAL, 0)));
                    }
                });

        Entity startGame = menuButtonBuilder.build()
                .createButton(
                        world,
                        MenuStrings.START,
                        CenterMath.offsetX(MainGame.GAME_WIDTH, buttonWidth)
                        ,CenterMath.offsetY(MainGame.GAME_HEIGHT, buttonHeight));

    }


    public MenuType getMenuType() {
        return menuType;
    }

    public void setMenuType(MenuType menuType) {
        this.menuType = menuType;
    }

    private void setUpMenuScreenStartAndTutorial() {

        final boolean quickSaveDataIsDefault =
                preferences.getString(PreferenceStrings.DATA_QUICK_SAVE, PreferenceStrings.DATA_QUICK_SAVE_NO_VALID_SAVE).equals(PreferenceStrings.DATA_QUICK_SAVE_NO_VALID_SAVE);

        final boolean quickSaveDataIsReadable = QuickSave.checkQuickSave();

        final boolean isInvalidData = !quickSaveDataIsDefault && !quickSaveDataIsReadable;


        MenuButton.MenuButtonBuilder menuButtonBuilder = new MenuButton.MenuButtonBuilder(Assets.medium, atlas.findRegion(TextureStrings.BLOCK))
                .width(buttonWidth)
                .height(buttonHeight)
                .foregroundColor(buttonForeground)
                .backgroundColor(buttonBackground);


        Entity startGame = menuButtonBuilder
                .action(new Action() {
                    @Override
                    public void performAction(World world, Entity e) {
                        game.getScreen().dispose();
                        game.setScreen(new PlayScreen(game));
                    }
                })
                .build().createButton(world,
                        quickSaveDataIsReadable ? MenuStrings.CONTINUE : MenuStrings.START,
                        CenterMath.offsetX(MainGame.GAME_WIDTH, Measure.units(30f)),
                        MainGame.GAME_HEIGHT / 2 + Measure.units(5f));

        Entity trails = menuButtonBuilder
                .action((new Action() {
                    @Override
                    public void performAction(World world, Entity e) {
                        menuType = MenuType.CHALLENGES;
                    }
                }))
                .build()
                .createButton(world, MenuStrings.TRAILS, CenterMath.offsetX(MainGame.GAME_WIDTH, Measure.units(30f))
                ,MainGame.GAME_HEIGHT / 2 - Measure.units(10f));


        Entity startTutorial = menuButtonBuilder
                .action(new Action() {
                    @Override
                    public void performAction(World world, Entity e) {
                        game.getScreen().dispose();
                        game.setScreen(new PlayScreen(game, new PlayScreenConfig(PlayScreenConfig.Spawn.TUTORIAL, 0)));

                    }
                })
                .build().createButton(world, MenuStrings.TUTORIAL, Measure.units(65f)
                ,Measure.units(5f));

    }

    @Override
    public void render(float delta) {

        //Sets the background color if nothing is on the screen.
        Gdx.gl.glClearColor(0, 0, 0, 0.5f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.setProjectionMatrix(gamecam.combined);

        GameDelta.delta(world, delta);

        handleInput(world.delta);

        menuBackDropWorld.process(delta);

        switch (menuType){
            case MAIN:
                world.process();
                break;
            case DEV:
                world.process();
                devModeMenuWorld.process(delta);
                break;
            case SETTING:
                settingsWorld.process(delta);
                break;
            case CHALLENGES:
                challengesWorldContainer.process(delta);
                break;
            case ITEMS:
                itemDisplayWorldContainer.process(delta);
                break;

        }


    }

    @Override
    public void resize(int width, int height) {
        gameport.update(width, height);

    }

    @Override
    public void pause() {

    }

    @Override
    public void dispose() {
        world.dispose();
    }

    private class MainMenuGestures extends AbstractGestureDectector {

        @Override
        public boolean tap(float x, float y, int count, int button) {
            Vector3 touchInput = new Vector3(x, y, 0);
            gameport.unproject(touchInput);

            switch(menuType){
                case MAIN:
                    return world.getSystem(ActionOnTouchSystem.class).touch(touchInput.x, touchInput.y);
                case DEV:
                    devModeMenuWorld.getWorld().getSystem(ActionOnTouchSystem.class).touch(touchInput.x, touchInput.y);
                    return world.getSystem(ActionOnTouchSystem.class).touch(touchInput.x, touchInput.y);

            }

            return false;
        }


        @Override
        public boolean longPress(float x, float y) {

            switch(menuType) {
                case DEV: menuType = MenuType.MAIN;
                    devSettings.putString(PreferenceStrings.DEV_MENU_IS_DEV, menuType.name());
                    devSettings.flush();
                    break;
                case MAIN: menuType = MenuType.DEV;
                    devSettings.putString(PreferenceStrings.DEV_MENU_IS_DEV, menuType.name());
                    devSettings.flush();
                    break;
            }

            return true;
        }
    }

}


