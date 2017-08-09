package com.byrjamin.wickedwizard.screens.world;

import com.artemis.Entity;
import com.artemis.World;
import com.artemis.WorldConfiguration;
import com.artemis.WorldConfigurationBuilder;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.byrjamin.wickedwizard.MainGame;
import com.byrjamin.wickedwizard.assets.Assets;
import com.byrjamin.wickedwizard.assets.FileLocationStrings;
import com.byrjamin.wickedwizard.assets.MenuStrings;
import com.byrjamin.wickedwizard.assets.TextureStrings;
import com.byrjamin.wickedwizard.ecs.components.CollisionBoundComponent;
import com.byrjamin.wickedwizard.ecs.components.StatComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.Action;
import com.byrjamin.wickedwizard.ecs.components.ai.ActionOnTouchComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.PositionComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.TextureFontComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.TextureRegionComponent;
import com.byrjamin.wickedwizard.ecs.systems.graphical.AnimationSystem;
import com.byrjamin.wickedwizard.ecs.systems.graphical.BoundsDrawingSystem;
import com.byrjamin.wickedwizard.ecs.systems.graphical.FadeSystem;
import com.byrjamin.wickedwizard.ecs.systems.graphical.RenderingSystem;
import com.byrjamin.wickedwizard.ecs.systems.input.ActionOnTouchSystem;
import com.byrjamin.wickedwizard.ecs.systems.level.RoomTransitionSystem;
import com.byrjamin.wickedwizard.ecs.systems.physics.MovementSystem;
import com.byrjamin.wickedwizard.factories.arenas.ArenaGUI;
import com.byrjamin.wickedwizard.factories.weapons.CritCalculator;
import com.byrjamin.wickedwizard.screens.MenuButton;
import com.byrjamin.wickedwizard.screens.MenuScreen;
import com.byrjamin.wickedwizard.screens.PlayScreen;
import com.byrjamin.wickedwizard.utils.CenterMath;
import com.byrjamin.wickedwizard.utils.Measure;
import com.byrjamin.wickedwizard.utils.Pair;

import java.util.Locale;

/**
 * Created by ae164 on 19/05/17.
 */

public class PauseWorld {


    private World world;

    private SpriteBatch batch;
    private MainGame game;
    private AssetManager manager;
    private TextureAtlas atlas;
    private Viewport gameport;

    private MenuButton menuButton;


    private static final float buttonWidth = Measure.units(37.5f);

    private RoomTransitionSystem roomTransitionSystem;


    private ArenaGUI pauseArenaGUI;


        public PauseWorld(MainGame game, SpriteBatch batch, AssetManager manager, Viewport gameport, RoomTransitionSystem rts, StatComponent playerStats){

            this.game = game;
            this.batch = batch;
            this.manager = manager;
            this.atlas = manager.get(FileLocationStrings.spriteAtlas);
            this.gameport = gameport;
            this.roomTransitionSystem = rts;
            this.pauseArenaGUI = new ArenaGUI(0, 0, Measure.units(2.5f), 8, rts.getCurrentMap(), atlas);
            menuButton = new MenuButton(Assets.medium, atlas.findRegion(TextureStrings.BLOCK));

            world = startWorld(playerStats);



    }

    public World startWorld(final StatComponent playerStats){

        WorldConfiguration config = new WorldConfigurationBuilder()
                .with(WorldConfigurationBuilder.Priority.HIGHEST,
                        new MovementSystem()
                )
                .with(WorldConfigurationBuilder.Priority.HIGH,
                        new AnimationSystem(),
                        new ActionOnTouchSystem(),
                        //new FindPlayerSystem(player),
                        new FadeSystem())
                .with(WorldConfigurationBuilder.Priority.LOW,
                        new RenderingSystem(batch, manager, gameport),
                        new BoundsDrawingSystem()
                )
                .build();

        World world = new World(config);

        final Camera gamecam = gameport.getCamera();

        float camX = gamecam.position.x - gamecam.viewportWidth / 2 + MainGame.GAME_BORDER;
        float camY = gamecam.position.y - gamecam.viewportHeight / 2 + MainGame.GAME_BORDER;

        Entity returntoMainMenu = menuButton.createButton(world, MenuStrings.MAIN_MENU, camX
                ,camY,buttonWidth, Measure.units(10f), new Color(Color.BLACK), new Color(Color.WHITE));
        returntoMainMenu.edit().add(new ActionOnTouchComponent(new Action() {
            @Override
            public void performAction(World world, Entity e) {

                PlayScreen playScreen = (PlayScreen) game.getScreen();
                playScreen.startAreYouSure();
            }
        }));

        Entity resume = menuButton.createButton(world, MenuStrings.RESUME, camX + Measure.units(62.5f)
                ,camY, buttonWidth, Measure.units(10f), new Color(Color.BLACK), new Color(Color.WHITE));
        resume.edit().add(new ActionOnTouchComponent(new Action() {
            @Override
            public void performAction(World world, Entity e) {
                PlayScreen playscreen = (PlayScreen) game.getScreen();
                playscreen.unpause();
            }
        }));


        Entity e = world.createEntity();
        e.edit().add(new PositionComponent(camX,camY));
        e.edit().add(new TextureRegionComponent(atlas.findRegion(TextureStrings.BLOCK),
                CenterMath.offsetX(gamecam.viewportWidth, gamecam.viewportWidth * 2),
                CenterMath.offsetY(gamecam.viewportHeight, gamecam.viewportHeight * 2),
                gamecam.viewportWidth * 2, gamecam.viewportHeight * 2, TextureRegionComponent.BACKGROUND_LAYER_FAR, new Color(0,0,0,0.93f)));



        Entity statText = world.createEntity();
        statText.edit().add(new PositionComponent(camX + Measure.units(10f), camY + Measure.units(55f)));
        statText.edit().add(new TextureFontComponent(Assets.medium, "Stats", Measure.units(10f), TextureRegionComponent.BACKGROUND_LAYER_NEAR));


        String[][] stats = new String[][]{
                {"Dmg", String.format(Locale.getDefault(), "+%.0f", playerStats.damage)},
                {"Fr", String.format(Locale.getDefault(), "+%.0f", playerStats.fireRate)},
                {"Shotspd", String.format(Locale.getDefault(), "+%.0f", playerStats.shotSpeed)},
                {"Range", String.format(Locale.getDefault(), "+%.0f", playerStats.range)},
                {"Acc", String.format(Locale.getDefault(), "+%.0f", playerStats.accuracy)},
                {"Luck", String.format(Locale.getDefault(), "+%.0f", playerStats.luck)},
                {"Speed", String.format(Locale.getDefault(), "+%.0f", playerStats.speed * 100)},
                {"Crit", String.format(Locale.getDefault(), "%.2f", CritCalculator.getCritChance(playerStats.crit, playerStats.accuracy, playerStats.luck)) + "%"},
/*                {"Dmg", Float.toString(playerStats.damage)},
                {"Dmg", Float.toString(playerStats.damage)},
                {"Dmg", Float.toString(playerStats.damage)},
                {"Dmg", Float.toString(playerStats.damage)},
                {"Dmg", Float.toString(playerStats.damage)},
                {"Dmg", Float.toString(playerStats.damage)},
                {"Dmg", Float.toString(playerStats.damage)},
                {"Dmg", Float.toString(playerStats.damage)},*/

        };
/*

        String[] stats = new String[]
                {
                        String.format(Locale.getDefault(), "Dmg %s", playerStats.damage),
                        String.format(Locale.getDefault(), "Fir %s", playerStats.fireRate),
                        String.format(Locale.getDefault(), "Rng %s", playerStats.range),
                        String.format(Locale.getDefault(), "Acc %s", playerStats.accuracy),
                        String.format(Locale.getDefault(), "Lck %s", playerStats.luck),
                        String.format(Locale.getDefault(), "Crt %.2f", CritCalculator.getCritChance(playerStats.crit, playerStats.accuracy, playerStats.luck)) + "%",
                        String.format(Locale.getDefault(), "Spd %.0f", playerStats.speed * 100) + "%",
                };
*/


        for(int i = 0; i < stats.length; i++){
            statsText(world, stats[i][0], camX - Measure.units(5f), camY + Measure.units(50f - (5 * i)));
        }

        for(int i = 0; i < stats.length; i++){
            statsText(world, stats[i][1], camX + Measure.units(10), camY + Measure.units(50f - (5 * i)));
        }


        Entity itemText = world.createEntity();
        itemText.edit().add(new PositionComponent(camX + Measure.units(30f), camY + Measure.units(55f)));
        itemText.edit().add(new TextureFontComponent(Assets.medium, "Items", Measure.units(15f), TextureRegionComponent.BACKGROUND_LAYER_NEAR));

        return world;

    }

    public void statsText(World world, String text, float x, float y){
        Entity damage = world.createEntity();
        damage.edit().add(new PositionComponent(x, y));
        damage.edit().add(new TextureFontComponent(Assets.medium, text, Measure.units(20f), TextureRegionComponent.BACKGROUND_LAYER_NEAR));
    }


    public World getWorld() {
        return world;
    }

    public void process(float delta){

        if (delta < 0.02f) {
            world.setDelta(delta);
        } else {
            world.setDelta(0.02f);
        }

        world.process();

        float camX = gameport.getCamera().position.x - gameport.getCamera().viewportWidth / 2;
        float camY = gameport.getCamera().position.y - gameport.getCamera().viewportHeight / 2;

        pauseArenaGUI.update(world.getDelta(),
                camX + Measure.units(85f),
                camY + Measure.units(35f),
                roomTransitionSystem.getCurrentMap(),
                roomTransitionSystem.getCurrentPlayerLocation());

        if(!game.batch.isDrawing()) {
            game.batch.begin();
        }

        pauseArenaGUI.draw(game.batch);

        game.batch.end();


    }







    }
