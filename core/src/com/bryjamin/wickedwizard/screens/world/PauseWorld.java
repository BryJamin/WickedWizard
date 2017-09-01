package com.bryjamin.wickedwizard.screens.world;

import com.artemis.Entity;
import com.artemis.World;
import com.artemis.WorldConfiguration;
import com.artemis.WorldConfigurationBuilder;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.bryjamin.wickedwizard.MainGame;
import com.bryjamin.wickedwizard.assets.FileLocationStrings;
import com.bryjamin.wickedwizard.assets.MenuStrings;
import com.bryjamin.wickedwizard.ecs.components.ai.Action;
import com.bryjamin.wickedwizard.ecs.components.movement.PositionComponent;
import com.bryjamin.wickedwizard.ecs.components.texture.TextureFontComponent;
import com.bryjamin.wickedwizard.ecs.components.texture.TextureRegionComponent;
import com.bryjamin.wickedwizard.ecs.systems.graphical.AnimationSystem;
import com.bryjamin.wickedwizard.ecs.systems.graphical.BoundsDrawingSystem;
import com.bryjamin.wickedwizard.ecs.systems.graphical.FadeSystem;
import com.bryjamin.wickedwizard.ecs.systems.graphical.RenderingSystem;
import com.bryjamin.wickedwizard.ecs.systems.input.ActionOnTouchSystem;
import com.bryjamin.wickedwizard.ecs.systems.level.RoomTransitionSystem;
import com.bryjamin.wickedwizard.ecs.systems.physics.MovementSystem;
import com.bryjamin.wickedwizard.factories.arenas.ArenaGUI;
import com.bryjamin.wickedwizard.factories.items.Item;
import com.bryjamin.wickedwizard.factories.weapons.CritCalculator;
import com.bryjamin.wickedwizard.screens.MenuButton;
import com.bryjamin.wickedwizard.utils.CenterMath;
import com.bryjamin.wickedwizard.utils.GameDelta;
import com.bryjamin.wickedwizard.utils.Measure;

import java.util.Locale;

/**
 * Created by ae164 on 19/05/17.
 */

public class PauseWorld implements WorldContainer {

    //Stats
    private static final float statsTitleOffsetX = Measure.units(5.5f);
    private static final float statsTitleOffsetY = Measure.units(60f);


    //Stats
    private static final float statsNamesOffsetX = -Measure.units(2.5f);
    private static final float statsNamesOffsetY = Measure.units(55f);
    private static final float statsNamesGap = -Measure.units(5f);

    private static final float statsNumbersOffsetX = Measure.units(12.5f);
    private static final float statsNumbersOffsetY = Measure.units(55f);
    private static final float statsNumbersGap = -Measure.units(5f);


    //Items
    private static final float itemTitleOffsetX = Measure.units(40f);
    private static final float itemTitleOffsetY = Measure.units(60f);

    private static final int maxColumns = 5;
    private static final int maxDisplayedItems = 30;
    //private static final float statsNamesGap = -Measure.units(5f);


    private static final float itemIconSize = Measure.units(5f);
    private static final float itemIconOffsetX = Measure.units(33.5f);
    private static final float itemIconOffsetY = Measure.units(50f);


    private static final float itemIconGapX = Measure.units(6f);
    private static final float itemIconGapY = -Measure.units(7.5f);


    //Map
    private static final float mapOffsetX = Measure.units(87.5f);
    private static final float mapOffsetY = Measure.units(40f);



    private static final float buttonWidth = Measure.units(37.5f);

    //Resume
    private static final float resumeOffsetX = Measure.units(60f);
    private static final float resumeOffsetY = Measure.units(0);

    //Mainmenu
    private static final float mainmenuOffsetX = Measure.units(0);
    private static final float mainmenuOffsetY = Measure.units(0);


    private SpriteBatch batch;
    private MainGame game;
    private AssetManager manager;
    private TextureAtlas atlas;
    private Viewport gameport;

    private World world;

    private RoomTransitionSystem roomTransitionSystem;

    private com.bryjamin.wickedwizard.ecs.components.StatComponent playerStats;


    private ArenaGUI pauseArenaGUI;


    public PauseWorld(MainGame game, SpriteBatch batch, AssetManager manager, Viewport gameport, RoomTransitionSystem rts, com.bryjamin.wickedwizard.ecs.components.StatComponent playerStats){

            this.game = game;
            this.batch = batch;
            this.manager = manager;
            this.atlas = manager.get(FileLocationStrings.spriteAtlas);
            this.gameport = gameport;
            this.roomTransitionSystem = rts;
            this.pauseArenaGUI = new ArenaGUI(0, 0, Measure.units(2.5f), 8, rts.getCurrentMap(), atlas);
            this.playerStats = playerStats;

            createWorld();



    }


    @Override
    public void createWorld() {
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

        world = new World(config);

        final Camera gamecam = gameport.getCamera();

        float camX = gamecam.position.x - gamecam.viewportWidth / 2 + MainGame.GAME_BORDER;
        float camY = gamecam.position.y - gamecam.viewportHeight / 2 + MainGame.GAME_BORDER;


        MenuButton.MenuButtonBuilder menuButtonBuilder = new MenuButton.MenuButtonBuilder(com.bryjamin.wickedwizard.assets.FontAssets.medium, atlas.findRegion(com.bryjamin.wickedwizard.assets.TextureStrings.BLOCK))
                .width(buttonWidth)
                .height(Measure.units(10f))
                .foregroundColor(new Color(Color.BLACK))
                .backgroundColor(new Color(Color.WHITE));


        Entity returntoMainMenu = menuButtonBuilder
                .action(new Action() {
                    @Override
                    public void performAction(World world, Entity e) {

                        com.bryjamin.wickedwizard.screens.PlayScreen playScreen = (com.bryjamin.wickedwizard.screens.PlayScreen) game.getScreen();
                        playScreen.startAreYouSure();
                    }
                })
                .build()
                .createButton(world, MenuStrings.MAIN_MENU, camX + mainmenuOffsetX
                ,camY + mainmenuOffsetY);


        Entity resume = menuButtonBuilder
                .action(new Action() {
                    @Override
                    public void performAction(World world, Entity e) {
                        com.bryjamin.wickedwizard.screens.PlayScreen playscreen = (com.bryjamin.wickedwizard.screens.PlayScreen) game.getScreen();
                        playscreen.unpause();
                    }
                })
                .build()
                .createButton(world, MenuStrings.RESUME, camX + resumeOffsetX
                ,camY + resumeOffsetY);


        Entity e = world.createEntity();
        e.edit().add(new PositionComponent(camX,camY));
        e.edit().add(new TextureRegionComponent(atlas.findRegion(com.bryjamin.wickedwizard.assets.TextureStrings.BLOCK),
                CenterMath.offsetX(gamecam.viewportWidth, gamecam.viewportWidth * 2),
                CenterMath.offsetY(gamecam.viewportHeight, gamecam.viewportHeight * 2),
                gamecam.viewportWidth * 2, gamecam.viewportHeight * 2, TextureRegionComponent.BACKGROUND_LAYER_FAR, new Color(0,0,0,0.93f)));



        Entity statText = world.createEntity();
        statText.edit().add(new PositionComponent(camX + statsTitleOffsetX, camY + statsTitleOffsetY));
        TextureFontComponent tfc = new TextureFontComponent(com.bryjamin.wickedwizard.assets.FontAssets.medium, MenuStrings.STATS, Measure.units(10f), TextureRegionComponent.BACKGROUND_LAYER_NEAR);
        statText.edit().add(tfc);


        String[][] stats = new String[][]{
                {MenuStrings.Stats.DAMAGE, plusMinusStatText(playerStats.damage)},
                {MenuStrings.Stats.FIRERATE, plusMinusStatText(playerStats.fireRate)},
                {MenuStrings.Stats.SHOTSPEED, plusMinusStatText(playerStats.shotSpeed)},
                {MenuStrings.Stats.RANGE, plusMinusStatText(playerStats.range)},
                {MenuStrings.Stats.ACCURACY, plusMinusStatText(playerStats.accuracy)},
                {MenuStrings.Stats.LUCK, plusMinusStatText(playerStats.luck)},
                {MenuStrings.Stats.SPEED, String.format(Locale.getDefault(), "%.0f", 100 + playerStats.speed * 100) + "%"},
                {MenuStrings.Stats.CRIT, String.format(Locale.getDefault(), "%.2f", CritCalculator.getCritChance(playerStats.crit, playerStats.accuracy, playerStats.luck)) + "%"},
        };

        for(int i = 0; i < stats.length; i++){
            statsText(world, stats[i][0], camX + statsNamesOffsetX, camY + statsNamesOffsetY + (statsNamesGap * i), Align.left);
        }

        for(int i = 0; i < stats.length; i++){
            statsText(world, stats[i][1], camX + statsNumbersOffsetX, camY + statsNamesOffsetY + (statsNamesGap * i), Align.center);
        }

        for(int i = 0; i < playerStats.collectedItems.size; i++){

            if(i >= maxDisplayedItems) continue;

            int mod = i % maxColumns;
            int div = i / maxColumns;

            itemIcon(world, playerStats.collectedItems.get(playerStats.collectedItems.size - i - 1), camX + itemIconOffsetX + (itemIconGapX * mod),
                    camY + itemIconOffsetY + (itemIconGapY * div));
        }


        Entity itemText = world.createEntity();
        itemText.edit().add(new PositionComponent(camX + itemTitleOffsetX, camY + itemTitleOffsetY));
        itemText.edit().add(new TextureFontComponent(com.bryjamin.wickedwizard.assets.FontAssets.medium, MenuStrings.ITEMS, Measure.units(15f), TextureRegionComponent.BACKGROUND_LAYER_NEAR));

    }


    private String plusMinusStatText(float stat){
        return String.format(Locale.getDefault(), (stat >= 0 ? "+" : "") + "%.0f", stat);
    }


    private void statsText(World world, String text, float x, float y, int align){
        Entity damage = world.createEntity();
        damage.edit().add(new PositionComponent(x, y));
        damage.edit().add(new TextureFontComponent(com.bryjamin.wickedwizard.assets.FontAssets.medium, text, Measure.units(20f), TextureRegionComponent.BACKGROUND_LAYER_NEAR));
        damage.getComponent(TextureFontComponent.class).align = align;
    }


    private void itemIcon(World world, Item item, float x, float y){
        Entity itemEntity = world.createEntity();
        itemEntity.edit().add(new PositionComponent(x, y));
        itemEntity.edit().add(new TextureRegionComponent(atlas.findRegion(item.getValues().region.getLeft(), item.getValues().region.getRight()),
                itemIconSize, itemIconSize, TextureRegionComponent.ENEMY_LAYER_MIDDLE));
    }

    public World getWorld() {
        return world;
    }

    public void process(float delta){

        GameDelta.delta(world, delta);

        float camX = gameport.getCamera().position.x - gameport.getCamera().viewportWidth / 2;
        float camY = gameport.getCamera().position.y - gameport.getCamera().viewportHeight / 2;

        pauseArenaGUI.update(world.getDelta(),
                camX + mapOffsetX,
                camY + mapOffsetY,
                roomTransitionSystem.getCurrentMap(),
                roomTransitionSystem.getCurrentPlayerLocation());

        if(!game.batch.isDrawing()) {
            game.batch.begin();
        }

        pauseArenaGUI.draw(game.batch);

        game.batch.end();


    }







    }
