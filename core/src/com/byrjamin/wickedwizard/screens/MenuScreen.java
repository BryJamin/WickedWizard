package com.byrjamin.wickedwizard.screens;

import com.artemis.Entity;
import com.artemis.World;
import com.artemis.WorldConfiguration;
import com.artemis.WorldConfigurationBuilder;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
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
import com.byrjamin.wickedwizard.assets.FontAssets;
import com.byrjamin.wickedwizard.assets.MenuStrings;
import com.byrjamin.wickedwizard.assets.PreferenceStrings;
import com.byrjamin.wickedwizard.assets.TextureStrings;
import com.byrjamin.wickedwizard.ecs.components.ai.Action;
import com.byrjamin.wickedwizard.ecs.components.movement.PositionComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.TextureRegionComponent;
import com.byrjamin.wickedwizard.ecs.systems.audio.MusicSystem;
import com.byrjamin.wickedwizard.ecs.systems.graphical.StateSystem;
import com.byrjamin.wickedwizard.ecs.systems.input.ActionOnTouchSystem;
import com.byrjamin.wickedwizard.ecs.systems.physics.GravitySystem;
import com.byrjamin.wickedwizard.ecs.systems.physics.CollisionSystem;
import com.byrjamin.wickedwizard.factories.arenas.challenges.ChallengesResource;
import com.byrjamin.wickedwizard.factories.arenas.levels.PresetGenerators;
import com.byrjamin.wickedwizard.screens.world.menu.DevModeMenuWorld;
import com.byrjamin.wickedwizard.screens.world.menu.ItemDisplayWorldContainer;
import com.byrjamin.wickedwizard.screens.world.menu.MenuBackDropWorld;
import com.byrjamin.wickedwizard.screens.world.menu.SettingsWorld;
import com.byrjamin.wickedwizard.screens.world.menu.ChallengesWorldContainer;
import com.byrjamin.wickedwizard.utils.AbstractGestureDectector;
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

    private Preferences preferences;

    private static final float logoStartX = Measure.units(4.5f);
    private static final float logoStartY = Measure.units(35f);
    private static final float logoWidth = Measure.units(25f);
    private static final float logoHeight = Measure.units(25f);


    private static final float buttonWidth = Measure.units(30f);
    private static final float buttonHeight = Measure.units(10f);


    private static final Color buttonForeground = new Color(Color.BLACK);
    private static final Color buttonBackground = new Color(Color.WHITE);



    public enum MenuType {
        MAIN, DEV, SETTING, CHALLENGES, ITEMS;
    }

    private static MenuType menuType;
    private static MenuType defaultMenuType = MenuType.MAIN;


    //TODO IF you ever click in the deck area don't cast any spells

    public MenuScreen(MainGame game) {
        super(game);


        preferences = Gdx.app.getPreferences(PreferenceStrings.DATA_PREF_KEY);

        if(menuType == null) {

            if(isDevDevice()) {
                String menuTypeName = Gdx.app.getPreferences(PreferenceStrings.DEV_MODE_PREF_KEY).getString(PreferenceStrings.DEV_MENU_IS_DEV, MenuType.DEV.name());
                menuType = menuTypeName.equals(MenuType.DEV.name()) ? MenuType.DEV : MenuType.MAIN;
                defaultMenuType = menuTypeName.equals(MenuType.DEV.name()) ? MenuType.DEV : MenuType.MAIN;
            } else {
                menuType = MenuType.MAIN;
                defaultMenuType = MenuType.MAIN;
            }
        } else {
            menuType = defaultMenuType;
        }


        gestureDetector = new GestureDetector(new MainMenuGestures());
        manager = game.assetManager;
        atlas = game.assetManager.get(FileLocationStrings.spriteAtlas, TextureAtlas.class);

        gamecam = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        gameport = new FitViewport(MainGame.GAME_WIDTH, MainGame.GAME_HEIGHT, gamecam);
        gamecam.position.set(gameport.getWorldWidth() / 2, gameport.getWorldHeight() / 2, 0);

        createMenu();

        if(MenuScreen.isDevDevice()) devModeMenuWorld = new DevModeMenuWorld(game, gameport);

        settingsWorld = new SettingsWorld(game, gameport);
        menuBackDropWorld = new MenuBackDropWorld(game, gameport);
        challengesWorldContainer = new ChallengesWorldContainer(game, gameport);
        itemDisplayWorldContainer = new ItemDisplayWorldContainer(game, gameport);
        settingsDetector = new GestureDetector(settingsWorld);
        backDropDetector = new GestureDetector(menuBackDropWorld);
        trailsWorldDectector = new GestureDetector(challengesWorldContainer);
        itemsDisplayWorldDectector = new GestureDetector(itemDisplayWorldContainer);

        Gdx.input.setCatchBackKey(true);

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


        multiplexer.addProcessor(new InputAdapter() {
            @Override
            public boolean keyDown(int keycode) {

                if(menuType !=  MenuType.MAIN) {

                    if (keycode == Input.Keys.BACK || keycode == Input.Keys.ESCAPE) {
                        MenuScreen.setMenuType(MenuType.MAIN);
                    }

                } else {
                    Gdx.app.exit();
                }

                return super.keyDown(keycode);
            }
        });






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
                        new MusicSystem()

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


        Entity logo = world.createEntity();
        logo.edit().add(new PositionComponent(logoStartX, logoStartY));
        logo.edit().add(new TextureRegionComponent(atlas.findRegion(TextureStrings.ICON_LOGO), logoWidth, logoHeight));



    }

    private void setUpMenuScreenOnlyTutorial(){

        MenuButton.MenuButtonBuilder menuButtonBuilder = new MenuButton.MenuButtonBuilder(FontAssets.medium, atlas.findRegion(TextureStrings.BLOCK))
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


    public static MenuType getMenuType() {
        return menuType;
    }

    public static void setMenuType(MenuType menuType) {
        if(menuType == MenuType.CHALLENGES || menuType == MenuType.MAIN){
            MenuScreen.defaultMenuType = menuType;
        }
        MenuScreen.menuType = menuType;
    }


    public static void resetMenuType() {
        MenuScreen.menuType = (MenuScreen.defaultMenuType != null) ? defaultMenuType : MenuType.MAIN;
    }


    private void setUpMenuScreenStartAndTutorial() {

        final boolean quickSaveDataIsDefault =
                preferences.getString(PreferenceStrings.DATA_QUICK_SAVE, PreferenceStrings.DATA_QUICK_SAVE_NO_VALID_SAVE).equals(PreferenceStrings.DATA_QUICK_SAVE_NO_VALID_SAVE);

        final boolean quickSaveDataIsReadable = QuickSave.checkQuickSave();

        final boolean isInvalidData = !quickSaveDataIsDefault && !quickSaveDataIsReadable;


        MenuButton.MenuButtonBuilder menuButtonBuilder = new MenuButton.MenuButtonBuilder(FontAssets.medium, atlas.findRegion(TextureStrings.BLOCK))
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
                        Measure.units(37.5f));

        Entity trails = menuButtonBuilder
                .action((new Action() {
                    @Override
                    public void performAction(World world, Entity e) {
                        menuType = MenuType.CHALLENGES;
                        defaultMenuType = MenuType.CHALLENGES;
                    }
                }))
                .build()
                .createButton(world, MenuStrings.TRAILS, CenterMath.offsetX(MainGame.GAME_WIDTH, Measure.units(30f))
                ,Measure.units(22.5f));


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
                    if(!isDevDevice()) return false;
                    devModeMenuWorld.getWorld().getSystem(ActionOnTouchSystem.class).touch(touchInput.x, touchInput.y);
                    return world.getSystem(ActionOnTouchSystem.class).touch(touchInput.x, touchInput.y);

            }

            return false;
        }


        @Override
        public boolean longPress(float x, float y) {

            if(!MenuScreen.isDevDevice()) return false;


            Preferences devSettings = Gdx.app.getPreferences(PreferenceStrings.DEV_MODE_PREF_KEY);

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




    public static boolean isDevDevice(){
        return Gdx.app.getPreferences(PreferenceStrings.DEV_MODE_PREF_KEY).getString(PreferenceStrings.DEV_MENU_ID).equals(PreferenceStrings.DEV_MENU_ID);
    }

}


