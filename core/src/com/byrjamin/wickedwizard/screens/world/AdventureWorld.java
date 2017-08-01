package com.byrjamin.wickedwizard.screens.world;

import com.artemis.BaseSystem;
import com.artemis.Component;
import com.artemis.Entity;
import com.artemis.World;
import com.artemis.WorldConfiguration;
import com.artemis.WorldConfigurationBuilder;
import com.artemis.utils.Bag;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.byrjamin.wickedwizard.MainGame;
import com.byrjamin.wickedwizard.assets.Assets;
import com.byrjamin.wickedwizard.assets.FileLocationStrings;
import com.byrjamin.wickedwizard.assets.PreferenceStrings;
import com.byrjamin.wickedwizard.assets.TextureStrings;
import com.byrjamin.wickedwizard.ecs.components.CurrencyComponent;
import com.byrjamin.wickedwizard.ecs.components.StatComponent;
import com.byrjamin.wickedwizard.ecs.systems.BulletSystem;
import com.byrjamin.wickedwizard.ecs.systems.DoorSystem;
import com.byrjamin.wickedwizard.ecs.systems.ExplosionSystem;
import com.byrjamin.wickedwizard.ecs.systems.FindChildSystem;
import com.byrjamin.wickedwizard.ecs.systems.FindPlayerSystem;
import com.byrjamin.wickedwizard.ecs.systems.HealthSystem;
import com.byrjamin.wickedwizard.ecs.systems.LockSystem;
import com.byrjamin.wickedwizard.ecs.systems.LuckSystem;
import com.byrjamin.wickedwizard.ecs.systems.PickUpSystem;
import com.byrjamin.wickedwizard.ecs.systems.audio.MusicSystem;
import com.byrjamin.wickedwizard.ecs.systems.audio.SoundSystem;
import com.byrjamin.wickedwizard.ecs.systems.ai.ActionAfterTimeSystem;
import com.byrjamin.wickedwizard.ecs.systems.ai.ConditionalActionSystem;
import com.byrjamin.wickedwizard.ecs.systems.ai.EnemyCollisionSystem;
import com.byrjamin.wickedwizard.ecs.systems.ai.ExpireSystem;
import com.byrjamin.wickedwizard.ecs.systems.ai.ExpiryRangeSystem;
import com.byrjamin.wickedwizard.ecs.systems.ai.FiringAISystem;
import com.byrjamin.wickedwizard.ecs.systems.ai.FollowPositionSystem;
import com.byrjamin.wickedwizard.ecs.systems.ai.MoveToPlayerAISystem;
import com.byrjamin.wickedwizard.ecs.systems.ai.MoveToSystem;
import com.byrjamin.wickedwizard.ecs.systems.ai.OnDeathSystem;
import com.byrjamin.wickedwizard.ecs.systems.ai.PhaseSystem;
import com.byrjamin.wickedwizard.ecs.systems.ai.ProximitySystem;
import com.byrjamin.wickedwizard.ecs.systems.ai.SpawnerSystem;
import com.byrjamin.wickedwizard.ecs.systems.graphical.AnimationSystem;
import com.byrjamin.wickedwizard.ecs.systems.graphical.BlinkOnHitSystem;
import com.byrjamin.wickedwizard.ecs.systems.graphical.BoundsDrawingSystem;
import com.byrjamin.wickedwizard.ecs.systems.graphical.CameraSystem;
import com.byrjamin.wickedwizard.ecs.systems.graphical.DirectionalSystem;
import com.byrjamin.wickedwizard.ecs.systems.graphical.FadeSystem;
import com.byrjamin.wickedwizard.ecs.systems.graphical.MessageBannerSystem;
import com.byrjamin.wickedwizard.ecs.systems.graphical.RenderingSystem;
import com.byrjamin.wickedwizard.ecs.systems.graphical.StateSystem;
import com.byrjamin.wickedwizard.ecs.systems.graphical.UISystem;
import com.byrjamin.wickedwizard.ecs.systems.input.ActionOnTouchSystem;
import com.byrjamin.wickedwizard.ecs.systems.input.GrapplePointSystem;
import com.byrjamin.wickedwizard.ecs.systems.input.GrappleSystem;
import com.byrjamin.wickedwizard.ecs.systems.input.JumpSystem;
import com.byrjamin.wickedwizard.ecs.systems.input.PlayerInputSystem;
import com.byrjamin.wickedwizard.ecs.systems.level.ChangeLevelSystem;
import com.byrjamin.wickedwizard.ecs.systems.level.EndGameSystem;
import com.byrjamin.wickedwizard.ecs.systems.level.InCombatSystem;
import com.byrjamin.wickedwizard.ecs.systems.level.LevelItemSystem;
import com.byrjamin.wickedwizard.ecs.systems.level.MapTeleportationSystem;
import com.byrjamin.wickedwizard.ecs.systems.level.RoomTransitionSystem;
import com.byrjamin.wickedwizard.ecs.systems.level.RoomTypeSystem;
import com.byrjamin.wickedwizard.ecs.systems.level.ScreenWipeSystem;
import com.byrjamin.wickedwizard.ecs.systems.physics.BounceCollisionSystem;
import com.byrjamin.wickedwizard.ecs.systems.physics.CollisionSystem;
import com.byrjamin.wickedwizard.ecs.systems.physics.FrictionSystem;
import com.byrjamin.wickedwizard.ecs.systems.physics.GravitySystem;
import com.byrjamin.wickedwizard.ecs.systems.physics.GroundCollisionSystem;
import com.byrjamin.wickedwizard.ecs.systems.physics.MovementSystem;
import com.byrjamin.wickedwizard.ecs.systems.physics.OnCollisionActionSystem;
import com.byrjamin.wickedwizard.ecs.systems.physics.OrbitalSystem;
import com.byrjamin.wickedwizard.ecs.systems.physics.PlatformSystem;
import com.byrjamin.wickedwizard.factories.PlayerFactory;
import com.byrjamin.wickedwizard.factories.arenas.Arena;
import com.byrjamin.wickedwizard.factories.arenas.ArenaGUI;
import com.byrjamin.wickedwizard.factories.arenas.JigsawGenerator;
import com.byrjamin.wickedwizard.factories.arenas.JigsawGeneratorConfig;
import com.byrjamin.wickedwizard.factories.items.ItemStore;
import com.byrjamin.wickedwizard.factories.items.PickUp;
import com.byrjamin.wickedwizard.factories.items.pickups.KeyUp;
import com.byrjamin.wickedwizard.factories.items.pickups.MoneyPlus1;
import com.byrjamin.wickedwizard.screens.QuickSave;
import com.byrjamin.wickedwizard.utils.BagSearch;
import com.byrjamin.wickedwizard.utils.ComponentBag;
import com.byrjamin.wickedwizard.utils.Measure;

import java.util.Random;

/**
 * Created by Home on 10/07/2017.
 */

public class AdventureWorld {

    private final AssetManager assetManager;
    private final TextureAtlas atlas;
    private final SpriteBatch batch;
    private final Random random;
    private final Viewport gameport;


    private final static float playerStartPositonX = Measure.units(47.5f);
    private final static float playerStartPositonY = Measure.units(30f);

    private ArenaGUI arenaGUI;
    public World world;

    private ComponentBag player;
    private StatComponent playerStats;
    private CurrencyComponent playerCurrency;

    private MainGame game;

    private JigsawGenerator jigsawGenerator;

    private BitmapFont currencyFont;

    public AdventureWorld(MainGame game, Viewport gameport, JigsawGenerator jigsawGenerator, Random random){
        this.game = game;
        this.assetManager = game.manager;
        this.atlas = assetManager.get(FileLocationStrings.spriteAtlas);
        this.batch = game.batch;
        this.gameport = gameport;
        this.random = random;
        this.player = new PlayerFactory(assetManager).playerBag(Measure.units(50f), Measure.units(45f));
        this.jigsawGenerator = jigsawGenerator;

        playerStats = BagSearch.getObjectOfTypeClass(StatComponent.class, player);
        playerCurrency = BagSearch.getObjectOfTypeClass(CurrencyComponent.class, player);

        this.currencyFont = assetManager.get(Assets.small, BitmapFont.class);// font size 12 pixels

        createAdventureWorld();

    }

    public ComponentBag getPlayer() {
        return player;
    }

    public void setPlayer(ComponentBag player) {
        this.player = player;
        playerStats = BagSearch.getObjectOfTypeClass(StatComponent.class, player);
        playerCurrency = BagSearch.getObjectOfTypeClass(CurrencyComponent.class, player);
    }

    public void setJigsawGenerator(JigsawGenerator jigsawGenerator){
        this.jigsawGenerator = jigsawGenerator;
    }


    public World createAdventureWorld(){


        Arena startingArena = jigsawGenerator.getStartingRoom();
        this.setPlayer(new PlayerFactory(game.manager).playerBag(startingArena.getWidth() / 2, Measure.units(45f)));


        WorldConfiguration config = new WorldConfigurationBuilder()
                .with(WorldConfigurationBuilder.Priority.HIGHEST,
                        new MovementSystem(),
                        //TODO this is here because lock boxes check for a collision but ground collision sets vertical velocity to 0.
                        //TODO either change lock to except next Tos or
                        new CollisionSystem(),
                        new BounceCollisionSystem(),
                        new GroundCollisionSystem(),
                        new OnCollisionActionSystem()
                )
                .with(WorldConfigurationBuilder.Priority.HIGH,
                        new PhaseSystem(),
                        new ExpireSystem(),
                        new ActionAfterTimeSystem(),
                        new ExplosionSystem(),
                        new OrbitalSystem(),
                        new InCombatSystem(),
                        new ExpiryRangeSystem(),
                        new AnimationSystem(),
                        new BlinkOnHitSystem(),
                        //TODO where bullet system used to be
                        new EnemyCollisionSystem(),
                        new MessageBannerSystem(atlas.findRegion(TextureStrings.BLOCK), gameport.getCamera()),
                        new FindPlayerSystem(player),
                        new FiringAISystem(),
                        new GrapplePointSystem(),
                        new LockSystem(),
                        new HealthSystem(),
                        new OnDeathSystem(),
                        new ProximitySystem(),
                        new FindChildSystem(),
                        new PickUpSystem(),
                        new LuckSystem(random),
                        new ActionOnTouchSystem(),
                        new RoomTypeSystem(),
                        new MoveToSystem(),
                        new MoveToPlayerAISystem(),
                        new PlatformSystem(),
                        new JumpSystem(),
                        new PlayerInputSystem(gameport),
                        new StateSystem(),
                        new SpawnerSystem(),
                        new GravitySystem(),
                        new GrappleSystem(),
                        new FrictionSystem(),
                        new ConditionalActionSystem()
                )
                .with(WorldConfigurationBuilder.Priority.LOW,
                        new DirectionalSystem(),
                        new CameraSystem(gameport),
                        new FollowPositionSystem(),
                        new FadeSystem(), //Applies any fade before render
                        new RenderingSystem(batch, assetManager, gameport),
                        new BulletSystem(),
                        new ScreenWipeSystem(batch, assetManager, (OrthographicCamera) gameport.getCamera()),
                        new BoundsDrawingSystem(),
                        new DoorSystem(),
                        new LevelItemSystem(new ItemStore(random), random),
                        new MusicSystem(assetManager),
                        new SoundSystem(assetManager),
                        new ChangeLevelSystem(assetManager, jigsawGenerator, random),
                        new MapTeleportationSystem(jigsawGenerator.getMapTracker()),
                        new RoomTransitionSystem(jigsawGenerator.getStartingMap()),
                        new EndGameSystem(game),
                        new UISystem(game, gameport,
                                arenaGUI = new ArenaGUI(0, 0, jigsawGenerator.getStartingMap().getRoomArray(), jigsawGenerator.getStartingRoom(), atlas),
                                playerStats,
                                playerCurrency)

                )
                .build();


        world = new World(config);
        //TODO CLEAN THIS UP you shouldn'#t need to set the world of the input
        world.getSystem(PlayerInputSystem.class).getPlayerInput().setWorld(world);

        if(!Gdx.app.getPreferences(
                PreferenceStrings.DATA_PREF_KEY).getString(
                PreferenceStrings.DATA_QUICK_SAVE, PreferenceStrings.DATA_QUICK_SAVE_NO_VALID_SAVE)
                .equals(PreferenceStrings.DATA_QUICK_SAVE_NO_VALID_SAVE)) {
            QuickSave.loadQuickSave(world);
        }



        for (Bag<Component> bag : world.getSystem(RoomTransitionSystem.class).getCurrentArena().getBagOfEntities()) {
            Entity entity = world.createEntity();
            for (Component comp : bag) {
                entity.edit().add(comp);
            }
        }

        Entity entity = world.createEntity();
        for (Component comp : player) {
            entity.edit().add(comp);
        }


        world.getSystem(MusicSystem.class).playLevelMusic(world.getSystem(ChangeLevelSystem.class).getLevel());


        return world;

    }


    public World getWorld() {
        return world;
    }

    public void process(float delta){

        if (delta < 0.02f) {
            world.setDelta(delta);
        } else {
            world.setDelta(0.017f);
        }

        world.process();

    }


    public void pauseWorld() {
        for(BaseSystem s: world.getSystems()){
            if(!(s instanceof RenderingSystem || s instanceof UISystem)) {
                s.setEnabled(false);
            }
        }
    }

    public void unPauseWorld() {
        for(BaseSystem s: world.getSystems()){
            s.setEnabled(true);
        }
    }


    public boolean isGameOver(){
        return playerStats.health <= 0;
    }



/*
    public void drawMapAndHud(boolean isPaused){

        if(!isPaused) {
            RoomTransitionSystem rts = world.getSystem(RoomTransitionSystem.class);
            arenaGUI.update(world.delta,
                    gameport.getCamera().position.x + Measure.units(45),
                    gameport.getCamera().position.y + Measure.units(25),
                    rts.getCurrentMap(),
                    rts.getCurrentPlayerLocation());
        }

        drawHUD(world, gameport.getCamera());
        arenaGUI.draw(batch);

        //HUD




    }



    public void drawHUD(World world, Camera gamecam){

        float camX = gamecam.position.x - gamecam.viewportWidth / 2;
        float camY = gamecam.position.y - gamecam.viewportHeight / 2;
        //BORDER

        Array<TextureRegion> healthRegions = new Array<TextureRegion>();

        for(int i = 1; i <= playerStats.health; i++){
            if(i <= playerStats.health && i % 2 == 0) {
                healthRegions.add(atlas.findRegion("item/heart", 0));
            } else if(playerStats.health % 2 != 0 && i == playerStats.health){
                healthRegions.add(atlas.findRegion("item/heart", 1));
            }
        }

        int emptyHealth = playerStats.maxHealth - playerStats.health;
        emptyHealth = (emptyHealth % 2 == 0) ? emptyHealth : emptyHealth - 1;

        for(int i = 1; i <= emptyHealth; i++) {
            if(i <= emptyHealth && i % 2 == 0) {
                healthRegions.add(atlas.findRegion("item/heart", 2));
            }
        }

        float screenoffset = Measure.units(0f);

        int count = 0;

        float otherUIPosition = 0;

        for(int i = 0; i < healthRegions.size; i++) {
            batch.draw(healthRegions.get(i),
                    gamecam.position.x - (gamecam.viewportWidth / 2) + screenoffset + (110 * i),
                    gamecam.position.y + (gamecam.viewportHeight / 2) - Measure.units(5f),
                    MainGame.GAME_UNITS * 5, MainGame.GAME_UNITS * 5);

            count++;
        }

        int otherCount = count;

        for(int i = count; i < playerStats.armor + count; i++) {
            batch.draw(atlas.findRegion("item/armor"),
                    gamecam.position.x - (gamecam.viewportWidth / 2) + screenoffset + (110 * i),
                    gamecam.position.y + (gamecam.viewportHeight / 2) - Measure.units(5f),
                    MainGame.GAME_UNITS * 5, MainGame.GAME_UNITS * 5);
            otherCount++;
        }

        otherUIPosition = otherCount * 110;

        PickUp p = new MoneyPlus1();

        batch.draw(atlas.findRegion(p.getRegionName().getLeft(), p.getRegionName().getRight()),
                camX + otherUIPosition + 55,
                gamecam.position.y + (gamecam.viewportHeight / 2) - Measure.units(3.5f),
                Measure.units(2.5f), Measure.units(2.5f));

        currencyFont.draw(batch, "" + playerCurrency.money,
                camX + otherUIPosition + Measure.units(6f),
                gamecam.position.y + (gamecam.viewportHeight / 2) - Measure.units(1.5f),
                Measure.units(7f), Align.left, true);

        p = new KeyUp();

        batch.draw(atlas.findRegion(p.getRegionName().getLeft(), p.getRegionName().getRight()),
                gamecam.position.x - (gamecam.viewportWidth / 2) + screenoffset,
                gamecam.position.y + (gamecam.viewportHeight / 2) - Measure.units(15f),
                Measure.units(3f), Measure.units(3f));

        currencyFont.draw(batch, "" + playerCurrency.keys,
                gamecam.position.x - (gamecam.viewportWidth / 2) + Measure.units(5f),
                gamecam.position.y + (gamecam.viewportHeight / 2) - Measure.units(12.3f),
                Measure.units(5f), Align.center, true);
    }*/



}
