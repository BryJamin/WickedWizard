package com.byrjamin.wickedwizard.screens.world;

import com.artemis.Component;
import com.artemis.Entity;
import com.artemis.World;
import com.artemis.WorldConfiguration;
import com.artemis.WorldConfigurationBuilder;
import com.artemis.utils.Bag;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.byrjamin.wickedwizard.assets.FileLocationStrings;
import com.byrjamin.wickedwizard.assets.PreferenceStrings;
import com.byrjamin.wickedwizard.assets.TextureStrings;
import com.byrjamin.wickedwizard.ecs.systems.BulletSystem;
import com.byrjamin.wickedwizard.ecs.systems.DoorSystem;
import com.byrjamin.wickedwizard.ecs.systems.ExplosionSystem;
import com.byrjamin.wickedwizard.ecs.systems.FindChildSystem;
import com.byrjamin.wickedwizard.ecs.systems.FindPlayerSystem;
import com.byrjamin.wickedwizard.ecs.systems.HealthSystem;
import com.byrjamin.wickedwizard.ecs.systems.LockSystem;
import com.byrjamin.wickedwizard.ecs.systems.LuckSystem;
import com.byrjamin.wickedwizard.ecs.systems.PickUpSystem;
import com.byrjamin.wickedwizard.ecs.systems.SoundSystem;
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
import com.byrjamin.wickedwizard.ecs.systems.graphical.BlinkSystem;
import com.byrjamin.wickedwizard.ecs.systems.graphical.BoundsDrawingSystem;
import com.byrjamin.wickedwizard.ecs.systems.graphical.CameraSystem;
import com.byrjamin.wickedwizard.ecs.systems.graphical.DirectionalSystem;
import com.byrjamin.wickedwizard.ecs.systems.graphical.FadeSystem;
import com.byrjamin.wickedwizard.ecs.systems.graphical.MessageBannerSystem;
import com.byrjamin.wickedwizard.ecs.systems.graphical.RenderingSystem;
import com.byrjamin.wickedwizard.ecs.systems.graphical.StateSystem;
import com.byrjamin.wickedwizard.ecs.systems.input.ActiveOnTouchSystem;
import com.byrjamin.wickedwizard.ecs.systems.input.GrapplePointSystem;
import com.byrjamin.wickedwizard.ecs.systems.input.GrappleSystem;
import com.byrjamin.wickedwizard.ecs.systems.input.JumpSystem;
import com.byrjamin.wickedwizard.ecs.systems.input.PlayerInputSystem;
import com.byrjamin.wickedwizard.ecs.systems.input.ShoppingSystem;
import com.byrjamin.wickedwizard.ecs.systems.level.ChangeLevelSystem;
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
import com.byrjamin.wickedwizard.factories.arenas.JigsawGenerator;
import com.byrjamin.wickedwizard.factories.arenas.skins.SolitarySkin;
import com.byrjamin.wickedwizard.factories.items.ItemStore;
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

    private ComponentBag player;
    private JigsawGenerator jigsawGenerator;


    public AdventureWorld(AssetManager assetManager, SpriteBatch batch, Viewport gameport, Random random){
        this.assetManager = assetManager;
        this.atlas = assetManager.get(FileLocationStrings.spriteAtlas);
        this.batch = batch;
        this.gameport = gameport;
        this.random = random;
        this.player = new PlayerFactory(assetManager).playerBag(Measure.units(50f), Measure.units(45f));
        this.jigsawGenerator = new JigsawGenerator(assetManager, new SolitarySkin(atlas), 10, new ItemStore(random), random);
    }

    public ComponentBag getPlayer() {
        return player;
    }

    public void setPlayer(ComponentBag player) {
        this.player = player;
    }

    public void setJigsawGenerator(JigsawGenerator jigsawGenerator){
        this.jigsawGenerator = jigsawGenerator;
    }


    public World createAdventureWorld(){

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
                        new ActiveOnTouchSystem(),
                        new AnimationSystem(),
                        new BlinkSystem(),
                        //TODO where bullet system used to be
                        new EnemyCollisionSystem(),
                        new MessageBannerSystem(atlas.findRegion(TextureStrings.BLOCK)),
                        new FindPlayerSystem(player),
                        new FiringAISystem(),
                        new GrapplePointSystem(),
                        new LockSystem(),
                        new HealthSystem(),
                        new OnDeathSystem(),
                        new FadeSystem(),
                        new ProximitySystem(),
                        new FindChildSystem(),
                        new PickUpSystem(),
                        new LuckSystem(random),
                        new ShoppingSystem(),
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
                        new FollowPositionSystem(),
                        new CameraSystem(gameport.getCamera(), gameport),
                        new RenderingSystem(batch, assetManager, gameport),
                        new BulletSystem(),
                        new ScreenWipeSystem(batch, (OrthographicCamera) gameport.getCamera()),
                        new BoundsDrawingSystem(Gdx.app.getPreferences(PreferenceStrings.SETTINGS).getBoolean(PreferenceStrings.SETTINGS_BOUND, false)),
                        new DoorSystem(),
                        new LevelItemSystem(new ItemStore(random), random),
                        new SoundSystem(assetManager),
                        new ChangeLevelSystem(jigsawGenerator, atlas),
                        new MapTeleportationSystem(jigsawGenerator.getMapTracker()),
                        new RoomTransitionSystem(jigsawGenerator.getStartingMap())
                )
                .build();


        World world = new World(config);
        //TODO CLEAN THIS UP you shouldn'#t need to set the world of the input
        world.getSystem(PlayerInputSystem.class).getPlayerInput().setWorld(world);

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


        return world;

    }









}
