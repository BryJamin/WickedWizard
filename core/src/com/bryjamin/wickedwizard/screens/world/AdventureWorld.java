package com.bryjamin.wickedwizard.screens.world;

import com.artemis.Aspect;
import com.artemis.BaseSystem;
import com.artemis.Component;
import com.artemis.Entity;
import com.artemis.World;
import com.artemis.WorldConfiguration;
import com.artemis.WorldConfigurationBuilder;
import com.artemis.utils.Bag;
import com.artemis.utils.IntBag;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.bryjamin.wickedwizard.MainGame;
import com.bryjamin.wickedwizard.assets.FileLocationStrings;
import com.bryjamin.wickedwizard.assets.PreferenceStrings;
import com.bryjamin.wickedwizard.assets.TextureStrings;
import com.bryjamin.wickedwizard.ecs.components.CurrencyComponent;
import com.bryjamin.wickedwizard.ecs.components.StatComponent;
import com.bryjamin.wickedwizard.ecs.components.ai.Action;
import com.bryjamin.wickedwizard.ecs.components.ai.ActionOnTouchComponent;
import com.bryjamin.wickedwizard.ecs.components.ai.DuringRoomLoadActionComponent;
import com.bryjamin.wickedwizard.ecs.components.ai.FollowCameraComponent;
import com.bryjamin.wickedwizard.ecs.components.identifiers.UnpackableComponent;
import com.bryjamin.wickedwizard.ecs.components.movement.CollisionBoundComponent;
import com.bryjamin.wickedwizard.ecs.components.movement.PositionComponent;
import com.bryjamin.wickedwizard.ecs.components.texture.TextureRegionComponent;
import com.bryjamin.wickedwizard.ecs.components.texture.UIComponent;
import com.bryjamin.wickedwizard.ecs.systems.BulletSystem;
import com.bryjamin.wickedwizard.ecs.systems.DoorSystem;
import com.bryjamin.wickedwizard.ecs.systems.ExplosionSystem;
import com.bryjamin.wickedwizard.ecs.systems.FindChildSystem;
import com.bryjamin.wickedwizard.ecs.systems.FindPlayerSystem;
import com.bryjamin.wickedwizard.ecs.systems.HealthSystem;
import com.bryjamin.wickedwizard.ecs.systems.LockSystem;
import com.bryjamin.wickedwizard.ecs.systems.LuckSystem;
import com.bryjamin.wickedwizard.ecs.systems.PickUpSystem;
import com.bryjamin.wickedwizard.ecs.systems.ai.ActionAfterTimeSystem;
import com.bryjamin.wickedwizard.ecs.systems.ai.ChallengeTimerSystem;
import com.bryjamin.wickedwizard.ecs.systems.ai.ConditionalActionSystem;
import com.bryjamin.wickedwizard.ecs.systems.ai.EnemyCollisionSystem;
import com.bryjamin.wickedwizard.ecs.systems.ai.ExpireSystem;
import com.bryjamin.wickedwizard.ecs.systems.ai.ExpiryRangeSystem;
import com.bryjamin.wickedwizard.ecs.systems.ai.FiringAISystem;
import com.bryjamin.wickedwizard.ecs.systems.ai.FollowCameraSystem;
import com.bryjamin.wickedwizard.ecs.systems.ai.FollowPositionSystem;
import com.bryjamin.wickedwizard.ecs.systems.ai.MoveToPlayerAISystem;
import com.bryjamin.wickedwizard.ecs.systems.ai.MoveToSystem;
import com.bryjamin.wickedwizard.ecs.systems.ai.OnDeathSystem;
import com.bryjamin.wickedwizard.ecs.systems.ai.PhaseSystem;
import com.bryjamin.wickedwizard.ecs.systems.ai.ProximitySystem;
import com.bryjamin.wickedwizard.ecs.systems.ai.SpawnerSystem;
import com.bryjamin.wickedwizard.ecs.systems.audio.MusicSystem;
import com.bryjamin.wickedwizard.ecs.systems.audio.SoundSystem;
import com.bryjamin.wickedwizard.ecs.systems.graphical.AfterUIRenderingSystem;
import com.bryjamin.wickedwizard.ecs.systems.graphical.AnimationSystem;
import com.bryjamin.wickedwizard.ecs.systems.graphical.BlinkOnHitSystem;
import com.bryjamin.wickedwizard.ecs.systems.graphical.BoundsDrawingSystem;
import com.bryjamin.wickedwizard.ecs.systems.graphical.CameraShakeSystem;
import com.bryjamin.wickedwizard.ecs.systems.graphical.CameraSystem;
import com.bryjamin.wickedwizard.ecs.systems.graphical.ColorChangeSystem;
import com.bryjamin.wickedwizard.ecs.systems.graphical.DirectionalSystem;
import com.bryjamin.wickedwizard.ecs.systems.graphical.FadeSystem;
import com.bryjamin.wickedwizard.ecs.systems.graphical.HealthBarSystem;
import com.bryjamin.wickedwizard.ecs.systems.graphical.MessageBannerSystem;
import com.bryjamin.wickedwizard.ecs.systems.graphical.RenderingSystem;
import com.bryjamin.wickedwizard.ecs.systems.graphical.StateSystem;
import com.bryjamin.wickedwizard.ecs.systems.graphical.UISystem;
import com.bryjamin.wickedwizard.ecs.systems.graphical.UnlockMessageSystem;
import com.bryjamin.wickedwizard.ecs.systems.input.ActionOnTouchSystem;
import com.bryjamin.wickedwizard.ecs.systems.input.DisablePlayerInputSystem;
import com.bryjamin.wickedwizard.ecs.systems.input.GrapplePointSystem;
import com.bryjamin.wickedwizard.ecs.systems.input.GrappleSystem;
import com.bryjamin.wickedwizard.ecs.systems.input.JumpSystem;
import com.bryjamin.wickedwizard.ecs.systems.input.PlayerInputSystem;
import com.bryjamin.wickedwizard.ecs.systems.level.BossDefeatUnlockSystem;
import com.bryjamin.wickedwizard.ecs.systems.level.ChangeLevelSystem;
import com.bryjamin.wickedwizard.ecs.systems.level.EndGameSystem;
import com.bryjamin.wickedwizard.ecs.systems.level.InCombatSystem;
import com.bryjamin.wickedwizard.ecs.systems.level.LevelItemSystem;
import com.bryjamin.wickedwizard.ecs.systems.level.MapTeleportationSystem;
import com.bryjamin.wickedwizard.ecs.systems.level.RoomTransitionSystem;
import com.bryjamin.wickedwizard.ecs.systems.level.RoomTypeSystem;
import com.bryjamin.wickedwizard.ecs.systems.level.ScreenWipeSystem;
import com.bryjamin.wickedwizard.ecs.systems.physics.BounceCollisionSystem;
import com.bryjamin.wickedwizard.ecs.systems.physics.CollisionSystem;
import com.bryjamin.wickedwizard.ecs.systems.physics.FrictionSystem;
import com.bryjamin.wickedwizard.ecs.systems.physics.GravitySystem;
import com.bryjamin.wickedwizard.ecs.systems.physics.GroundCollisionSystem;
import com.bryjamin.wickedwizard.ecs.systems.physics.MovementSystem;
import com.bryjamin.wickedwizard.ecs.systems.physics.OnCollisionActionSystem;
import com.bryjamin.wickedwizard.ecs.systems.physics.OrbitalSystem;
import com.bryjamin.wickedwizard.ecs.systems.physics.PlatformSystem;
import com.bryjamin.wickedwizard.factories.PlayerFactory;
import com.bryjamin.wickedwizard.factories.arenas.Arena;
import com.bryjamin.wickedwizard.factories.arenas.ArenaGUI;
import com.bryjamin.wickedwizard.factories.arenas.GameCreator;
import com.bryjamin.wickedwizard.factories.arenas.JigsawGenerator;
import com.bryjamin.wickedwizard.factories.arenas.PresetGames;
import com.bryjamin.wickedwizard.factories.items.Item;
import com.bryjamin.wickedwizard.factories.items.ItemStore;
import com.bryjamin.wickedwizard.screens.MenuScreen;
import com.bryjamin.wickedwizard.screens.PlayScreen;
import com.bryjamin.wickedwizard.screens.QuickSave;
import com.bryjamin.wickedwizard.utils.BagSearch;
import com.bryjamin.wickedwizard.utils.ComponentBag;
import com.bryjamin.wickedwizard.utils.Measure;

import java.util.Random;

;

/**
 * Created by Home on 10/07/2017.
 */

public class AdventureWorld extends InputAdapter {

    private MainGame game;

    private final AssetManager assetManager;
    private final TextureAtlas atlas;
    private final SpriteBatch batch;
    private final Random random;
    private final Viewport gameport;


    private String playerId;

    private boolean isGameOver;
    private boolean isQuickSave;

    public World world;

    private ComponentBag player;
    private StatComponent playerStats;
    private StatComponent savedStats;

    private GameCreator gameCreator;
    private float countDown;

    public AdventureWorld(MainGame game, Viewport gameport, GameCreator gameCreator, String playerId, Random random) {
        this.game = game;
        this.assetManager = game.assetManager;
        this.atlas = assetManager.get(FileLocationStrings.spriteAtlas);
        this.batch = game.batch;
        this.gameport = gameport;
        this.playerId = playerId;
        this.random = random;
        this.gameCreator = gameCreator;
        createAdventureWorld();
    }

    public ComponentBag getPlayer() {
        return player;
    }

    public void setPlayer(ComponentBag player) {
        this.player = player;
        this.playerStats = BagSearch.getObjectOfTypeClass(StatComponent.class, player);
    }



    public void setPlayer(String id){
        new PlayerFactory(assetManager).playerBag(id, 0,0);
    }



    public World createAdventureWorld() {

        JigsawGenerator jigsawGenerator;

        jigsawGenerator = gameCreator.getCurrentLevel().jigsawGeneratorConfig.build();
        if(gameCreator.getCurrentLevel().isGenerated){
            jigsawGenerator.generate();
        } else {
            jigsawGenerator.cleanArenas();
        }

        Arena startingArena = jigsawGenerator.getStartingRoom();

        if(gameCreator.id.equals(PresetGames.DEFAULT_GAME_ID)) {

            if (QuickSave.doesQuickSaveExist()) {

                QuickSave.loadQuickSave(gameCreator, assetManager, this);


                jigsawGenerator = gameCreator.getCurrentLevel().jigsawGeneratorConfig.build();
                if(gameCreator.getCurrentLevel().isGenerated){
                    jigsawGenerator.generate();
                } else {
                    jigsawGenerator.cleanArenas();
                }

                Arena loadedStartingArena = jigsawGenerator.getStartingRoom();

                BagSearch.getObjectOfTypeClass(PositionComponent.class, player).position.set(loadedStartingArena.getWidth() / 2, Measure.units(45f), 0);
                savedStats = new StatComponent();
                savedStats.applyStats(BagSearch.getObjectOfTypeClass(StatComponent.class, player));

            } else {
                this.setPlayer(new PlayerFactory(game.assetManager).playerBag(playerId, startingArena.getWidth() / 2, Measure.units(45f)));
            }

        } else {
            this.setPlayer(new PlayerFactory(game.assetManager).playerBag(playerId, startingArena.getWidth() / 2, Measure.units(45f)));
        }


        createWorld(jigsawGenerator);


        world.getSystem(PlayerInputSystem.class).getPlayerInput().setWorld(world);

        Entity entity = world.createEntity();
        for (Component comp : player) {
            entity.edit().add(comp);
        }

        for (Bag<Component> bag : world.getSystem(RoomTransitionSystem.class).getCurrentArena().getBagOfEntities()) {
            entity = world.createEntity();
            for (Component comp : bag) {
                entity.edit().add(comp);
            }
        }

        world.process();

        IntBag intBag = world.getAspectSubscriptionManager().get(Aspect.all(DuringRoomLoadActionComponent.class)).getEntities();
        for(int i = 0; i < intBag.size(); i++) {
            world.getEntity(intBag.get(i)).getComponent(DuringRoomLoadActionComponent.class).action.performAction(world, world.getEntity(intBag.get(i)));
        }

        for(Item i : world.getSystem(FindPlayerSystem.class).getPlayerComponent(StatComponent.class).collectedItems){
            i.applyEffect(world, world.getSystem(FindPlayerSystem.class).getPlayerEntity());
        }

        if(savedStats != null){
            playerStats.applyStats(savedStats);
        }

        createPauseButton(world);

        if(Gdx.app.getPreferences(PreferenceStrings.DEV_MODE_PREF_KEY).getBoolean(PreferenceStrings.DEV_GODMODE, false) && MenuScreen.isDevDevice()) {
            turnOnGodMode(world.getSystem(FindPlayerSystem.class).getPlayerComponent(StatComponent.class),
                    world.getSystem(FindPlayerSystem.class).getPlayerComponent(CurrencyComponent.class));
        }



        return world;





    }


    public void createPauseButton(World world){

        float width = Measure.units(4.5f);
        float height = Measure.units(4.5f);

        Entity pauseButton = world.createEntity();
        pauseButton.edit().add(new UIComponent());
        pauseButton.edit().add(new ActionOnTouchComponent(new Action() {
            @Override
            public void performAction(World world, Entity e) {
                world.getSystem(EndGameSystem.class).pauseGame();
            }
        }));
        pauseButton.edit().add(new FollowCameraComponent(Measure.units(30.5f), Measure.units(30.5f)));
        pauseButton.edit().add(new PositionComponent());
        pauseButton.edit().add(new CollisionBoundComponent(new Rectangle(0, 0, width, height)));
        pauseButton.edit().add(new UnpackableComponent());
        pauseButton.edit().add(new TextureRegionComponent(atlas.findRegion(TextureStrings.ICON_PAUSE), width, height, TextureRegionComponent.BACKGROUND_LAYER_FAR));

    }



    public void createWorld(JigsawGenerator jigsawGenerator){
        WorldConfiguration config = new WorldConfigurationBuilder()
                .with(WorldConfigurationBuilder.Priority.HIGHEST,
                        new MovementSystem(),
                        new ChallengeTimerSystem(),
                        new GravitySystem(),
                        //TODO this is here because lock boxes check for a collision but ground collision sets vertical velocity to 0.
                        //TODO either change lock to except next Tos or
                        new CollisionSystem(),
                        new BounceCollisionSystem(),
                        new GroundCollisionSystem(),
                        new OnCollisionActionSystem(),
                        new FollowPositionSystem()
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
                        new GrapplePointSystem(),
                        new LockSystem(),
                        new HealthSystem(),
                        new OnDeathSystem(),
                        new ProximitySystem(),
                        new FindChildSystem(),
                        new PickUpSystem(),
                        new LuckSystem(assetManager, random),
                        new ActionOnTouchSystem(),
                        new RoomTypeSystem(),
                        new MoveToSystem(),
                        new MoveToPlayerAISystem(),
                        new PlatformSystem(),
                        new JumpSystem(),
                        new DisablePlayerInputSystem(),
                        new PlayerInputSystem(gameport),
                        new StateSystem(),
                        new SpawnerSystem(),
                        new GrappleSystem(),
                        new FrictionSystem(),
                        new ConditionalActionSystem()
                )
                .with(WorldConfigurationBuilder.Priority.LOW,
                        new DirectionalSystem(),
                        new CameraSystem(gameport),
                        new CameraShakeSystem(gameport),
                        new FollowCameraSystem(gameport.getCamera()),
                        new FiringAISystem(),
                        new FadeSystem(), //Applies any fade before render
                        new ColorChangeSystem(),
                        new RenderingSystem(batch, assetManager, gameport),
                        new BulletSystem(),
                        new ScreenWipeSystem(batch, assetManager, (OrthographicCamera) gameport.getCamera()),
                        new BoundsDrawingSystem(),
                        new DoorSystem(),
                        new LevelItemSystem(new ItemStore(random), random),
                        new MusicSystem(),
                        new SoundSystem(assetManager),
                        new ChangeLevelSystem(gameCreator, jigsawGenerator),
                        new BossDefeatUnlockSystem(game, gameCreator),
                        new UnlockMessageSystem(game),
                        new MapTeleportationSystem(jigsawGenerator.getMapTracker()),
                        new RoomTransitionSystem(jigsawGenerator.getStartingMap()),
                        new EndGameSystem(game),
                        new UISystem(game, gameport,
                                new ArenaGUI(0, 0, jigsawGenerator.getStartingMap().getRoomArray(), jigsawGenerator.getStartingRoom(), atlas),
                                BagSearch.getObjectOfTypeClass(StatComponent.class, player),
                                BagSearch.getObjectOfTypeClass(CurrencyComponent.class, player)),
                        new AfterUIRenderingSystem(game, gameport),
                        new HealthBarSystem(game, gameport)
                )
                .build();


        world = new World(config);
    }



    public void turnOnGodMode(StatComponent stats, CurrencyComponent currencyComponent){
        stats.damage = 99f;
        stats.accuracy = 99f;
        stats.fireRate = 99f;
        stats.speed = 0.5f;
        stats.luck = 99f;

      //  stats.damage = 5;
      //  stats.accuracy = 0;

        currencyComponent.money = 99;

    }


    public World getWorld() {
        return world;
    }

    public void process(float delta) {

        if (delta < 0.02f) {
            world.setDelta(delta);
        } else {
            world.setDelta(0.017f);
        }

        if(playerStats.getHealth() <= 0 && !isGameOver){
            countDown = 1f;
            world.getSystem(PlayerInputSystem.class).setEnabled(false);
            world.getSystem(CameraSystem.class).setEnabled(false);
            world.getSystem(CameraShakeSystem.class).setEnabled(false);
            isGameOver = true;

            ((PlayScreen) game.getScreen()).startGameOver();
        }

        if(isGameOver){

            countDown -= delta;
            if(countDown <= 0){
                pauseWorld();
            }

        }

        world.process();

    }


    public void pauseWorld() {

        world.getSystem(MusicSystem.class).pauseMusic();

        for (BaseSystem s : world.getSystems()) {
            if (!(s instanceof RenderingSystem || s instanceof UISystem)) {
                s.setEnabled(false);
            }
        }
    }



    public void unPauseWorld() {

        world.getSystem(MusicSystem.class).resumeMusic();

        for (BaseSystem s : world.getSystems()) {
            s.setEnabled(true);
        }
    }


    public boolean isGameOver() {
        return isGameOver;
    }


    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {

        Vector3 touchInput = new Vector3(screenX, screenY, 0);
        gameport.unproject(touchInput);

        if(world.getSystem(ActionOnTouchSystem.class).touch(touchInput.x, touchInput.y)) return true;

        return false;
    }
}

