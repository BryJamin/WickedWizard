package com.byrjamin.wickedwizard.screens.world;

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
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.byrjamin.wickedwizard.MainGame;
import com.byrjamin.wickedwizard.assets.Assets;
import com.byrjamin.wickedwizard.assets.FileLocationStrings;
import com.byrjamin.wickedwizard.assets.PreferenceStrings;
import com.byrjamin.wickedwizard.assets.TextureStrings;
import com.byrjamin.wickedwizard.ecs.components.ai.OnRoomLoadActionComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.CollisionBoundComponent;
import com.byrjamin.wickedwizard.ecs.components.CurrencyComponent;
import com.byrjamin.wickedwizard.ecs.components.StatComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.Action;
import com.byrjamin.wickedwizard.ecs.components.ai.ActionOnTouchComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.FollowPositionComponent;
import com.byrjamin.wickedwizard.ecs.components.identifiers.UnpackableComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.PositionComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.TextureRegionComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.UIComponent;
import com.byrjamin.wickedwizard.ecs.systems.BulletSystem;
import com.byrjamin.wickedwizard.ecs.systems.DoorSystem;
import com.byrjamin.wickedwizard.ecs.systems.ExplosionSystem;
import com.byrjamin.wickedwizard.ecs.systems.FindChildSystem;
import com.byrjamin.wickedwizard.ecs.systems.FindPlayerSystem;
import com.byrjamin.wickedwizard.ecs.systems.HealthSystem;
import com.byrjamin.wickedwizard.ecs.systems.LockSystem;
import com.byrjamin.wickedwizard.ecs.systems.LuckSystem;
import com.byrjamin.wickedwizard.ecs.systems.PickUpSystem;
import com.byrjamin.wickedwizard.ecs.systems.ai.ChallengeTimerSystem;
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
import com.byrjamin.wickedwizard.ecs.systems.graphical.CameraShakeSystem;
import com.byrjamin.wickedwizard.ecs.systems.graphical.CameraSystem;
import com.byrjamin.wickedwizard.ecs.systems.graphical.DirectionalSystem;
import com.byrjamin.wickedwizard.ecs.systems.graphical.FadeSystem;
import com.byrjamin.wickedwizard.ecs.systems.graphical.HealthBarSystem;
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
import com.byrjamin.wickedwizard.factories.arenas.GameCreator;
import com.byrjamin.wickedwizard.factories.arenas.JigsawGenerator;
import com.byrjamin.wickedwizard.factories.arenas.PresetGames;
import com.byrjamin.wickedwizard.factories.items.ItemStore;
import com.byrjamin.wickedwizard.factories.items.companions.ItemMiniSpinnyThingie;
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


    private boolean isGameOver;

    private ArenaGUI arenaGUI;
    public World world;

    private ComponentBag player;
    private StatComponent playerStats;
    private CurrencyComponent playerCurrency;

    private MainGame game;

    private JigsawGenerator jigsawGenerator;

    private GameCreator gameCreator;

    private BitmapFont currencyFont;

    private float countDown;

    public AdventureWorld(MainGame game, Viewport gameport, GameCreator gameCreator, Random random) {
        this.game = game;
        this.assetManager = game.assetManager;
        this.atlas = assetManager.get(FileLocationStrings.spriteAtlas);
        this.batch = game.batch;
        this.gameport = gameport;
        this.random = random;
        this.player = new PlayerFactory(assetManager).playerBag(Measure.units(50f), Measure.units(45f));
        this.gameCreator = gameCreator;
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

    public World createAdventureWorld() {

        jigsawGenerator = gameCreator.getCurrentLevel().jigsawGeneratorConfig.build();
        if(gameCreator.getCurrentLevel().isGenerated){
            jigsawGenerator.generate();
        } else {
            jigsawGenerator.cleanArenas();
        }


        Arena startingArena = jigsawGenerator.getStartingRoom();
        this.setPlayer(new PlayerFactory(game.assetManager).playerBag(startingArena.getWidth() / 2, Measure.units(45f)));


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
                        new GrappleSystem(),
                        new FrictionSystem(),
                        new ConditionalActionSystem()
                )
                .with(WorldConfigurationBuilder.Priority.LOW,
                        new DirectionalSystem(),
                        new CameraSystem(gameport),
                        new CameraShakeSystem(gameport),
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
                        new ChangeLevelSystem(gameCreator, jigsawGenerator),
                        new MapTeleportationSystem(jigsawGenerator.getMapTracker()),
                        new RoomTransitionSystem(jigsawGenerator.getStartingMap()),
                        new EndGameSystem(game),
                        new UISystem(game, gameport,
                                arenaGUI = new ArenaGUI(0, 0, jigsawGenerator.getStartingMap().getRoomArray(), jigsawGenerator.getStartingRoom(), atlas),
                                playerStats,
                                playerCurrency),
                        new HealthBarSystem(game, gameport)
                )
                .build();


        world = new World(config);

        world.getSystem(PlayerInputSystem.class).getPlayerInput().setWorld(world);

        if(gameCreator.id.equals(PresetGames.DEFAULT_GAME_ID)) {

            String quickSaveString = Gdx.app.getPreferences(PreferenceStrings.DATA_PREF_KEY).getString(PreferenceStrings.DATA_QUICK_SAVE, PreferenceStrings.DATA_QUICK_SAVE_NO_VALID_SAVE);

            if (!quickSaveString.equals(PreferenceStrings.DATA_QUICK_SAVE_NO_VALID_SAVE)) {
                QuickSave.loadQuickSave(world);
            }

        }

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

        IntBag intBag = world.getAspectSubscriptionManager().get(Aspect.all(OnRoomLoadActionComponent.class)).getEntities();
        for(int i = 0; i < intBag.size(); i++) {
            world.getEntity(intBag.get(i)).getComponent(OnRoomLoadActionComponent.class).action.performAction(world, world.getEntity(intBag.get(i)));
        }








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
        pauseButton.edit().add(new FollowPositionComponent(gameport.getCamera().position, Measure.units(32.5f), Measure.units(30.5f)));
        pauseButton.edit().add(new PositionComponent());
        pauseButton.edit().add(new CollisionBoundComponent(new Rectangle(0, 0, width, height)));
        pauseButton.edit().add(new UnpackableComponent());
        pauseButton.edit().add(new TextureRegionComponent(atlas.findRegion(TextureStrings.ICON_PAUSE), width, height, TextureRegionComponent.ENEMY_LAYER_MIDDLE));


        return world;

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

        if(playerStats.health <= 0 && !isGameOver){
            countDown = 1f;
            world.getSystem(PlayerInputSystem.class).setEnabled(false);
            world.getSystem(CameraSystem.class).setEnabled(false);
            isGameOver = true;
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


}

