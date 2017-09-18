package com.bryjamin.wickedwizard.factories.arenas.levels;

import com.artemis.Aspect;
import com.artemis.Entity;
import com.artemis.World;
import com.artemis.utils.IntBag;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.math.Rectangle;
import com.bryjamin.wickedwizard.MainGame;
import com.bryjamin.wickedwizard.assets.FontAssets;
import com.bryjamin.wickedwizard.assets.Mix;
import com.bryjamin.wickedwizard.assets.SoundFileStrings;
import com.bryjamin.wickedwizard.assets.resourcelayouts.ChallengeLayout;
import com.bryjamin.wickedwizard.ecs.components.ai.Action;
import com.bryjamin.wickedwizard.ecs.components.ai.ActionAfterTimeComponent;
import com.bryjamin.wickedwizard.ecs.components.ai.DuringRoomLoadActionComponent;
import com.bryjamin.wickedwizard.ecs.components.identifiers.ChallengeTimerComponent;
import com.bryjamin.wickedwizard.ecs.components.movement.CollisionBoundComponent;
import com.bryjamin.wickedwizard.ecs.components.movement.PositionComponent;
import com.bryjamin.wickedwizard.ecs.components.texture.TextureFontComponent;
import com.bryjamin.wickedwizard.ecs.components.texture.TextureRegionComponent;
import com.bryjamin.wickedwizard.ecs.systems.audio.SoundSystem;
import com.bryjamin.wickedwizard.ecs.systems.graphical.UnlockMessageSystem;
import com.bryjamin.wickedwizard.ecs.systems.level.EndGameSystem;
import com.bryjamin.wickedwizard.ecs.systems.level.ScreenWipeSystem;
import com.bryjamin.wickedwizard.factories.AbstractFactory;
import com.bryjamin.wickedwizard.factories.arenas.Arena;
import com.bryjamin.wickedwizard.factories.arenas.ArenaCreate;
import com.bryjamin.wickedwizard.factories.arenas.challenges.ChallengesResource;
import com.bryjamin.wickedwizard.factories.arenas.decor.ArenaEnemyPlacementFactory;
import com.bryjamin.wickedwizard.factories.arenas.decor.ArenaShellFactory;
import com.bryjamin.wickedwizard.factories.arenas.decor.OnLoadFactory;
import com.bryjamin.wickedwizard.factories.arenas.decor.PortalFactory;
import com.bryjamin.wickedwizard.factories.arenas.skins.ArenaSkin;
import com.bryjamin.wickedwizard.factories.chests.ChestFactory;
import com.bryjamin.wickedwizard.screens.MenuScreen;
import com.bryjamin.wickedwizard.utils.CenterMath;
import com.bryjamin.wickedwizard.utils.ComponentBag;
import com.bryjamin.wickedwizard.utils.MapCoords;
import com.bryjamin.wickedwizard.utils.Measure;
import com.bryjamin.wickedwizard.utils.enums.Level;

;

/**
 * Created by BB on 19/08/2017.
 */

public class ReuseableRooms extends AbstractFactory {

    private ArenaShellFactory arenaShellFactory;
    private ChestFactory chestFactory;
    private com.bryjamin.wickedwizard.factories.arenas.decor.DecorFactory decorFactory;
    private ArenaEnemyPlacementFactory arenaEnemyPlacementFactory;

    private ArenaSkin arenaSkin;


    public ReuseableRooms(AssetManager assetManager, ArenaSkin arenaSkin) {
        super(assetManager);
        this.arenaShellFactory = new ArenaShellFactory(assetManager, arenaSkin);
        this.chestFactory = new ChestFactory(assetManager);
        this.decorFactory = new com.bryjamin.wickedwizard.factories.arenas.decor.DecorFactory(assetManager, arenaSkin);
        this.arenaSkin = arenaSkin;
    }


    public ArenaCreate startingArena(final Level level){
        return new ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {
                Arena arena = arenaShellFactory.createOmniArenaHiddenGrapple(defaultCoords, Arena.ArenaType.NORMAL);

                float width = Measure.units(10f);
                float height = Measure.units(10f);

                float x = CenterMath.offsetX(arena.getWidth(), width);
                float y = CenterMath.offsetY(arena.getHeight() - Measure.units(15f), height) + Measure.units(10f);

                ComponentBag bag = arena.createArenaBag();
                bag.add(new PositionComponent(x, y));
                bag.add(new CollisionBoundComponent(new Rectangle(x,y, width, height)));

                TextureFontComponent textureFontComponent = new TextureFontComponent(FontAssets.medium, level.getName(), level.getArenaSkin().getWallTint());
                textureFontComponent.layer = TextureRegionComponent.BACKGROUND_LAYER_NEAR;
                bag.add(textureFontComponent);



                arena.addEntity(new OnLoadFactory().startMusicEntity(level.getMusic()));
                return arena;
            }
        };
    }


    public ArenaCreate challengeStartingArena(final ChallengeLayout challengeLayout, final Mix mix){
        return new ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {
                Arena arena = arenaShellFactory.createOmniArenaHiddenGrapple(defaultCoords, Arena.ArenaType.NORMAL);

                ComponentBag bag = arena.createArenaBag();
                bag.add(new PositionComponent(0, Measure.units(35f)));
                bag.add(new CollisionBoundComponent(new Rectangle(0,Measure.units(35f), MainGame.GAME_WIDTH, Measure.units(5f))));

                TextureFontComponent textureFontComponent = new TextureFontComponent(FontAssets.medium, challengeLayout.getName(), arenaSkin.getWallTint());
                textureFontComponent.layer = TextureRegionComponent.BACKGROUND_LAYER_NEAR;
                bag.add(textureFontComponent);

                arena.addEntity(new OnLoadFactory().startMusicEntity(mix));
                return arena;
            }
        };
    }


    public void challengeRoadOnLoadActionEntity(Arena arena, final String challengeId){

        ComponentBag bag = arena.createArenaBag();


        bag.add(new ActionAfterTimeComponent(new Action() {
            @Override
            public void performAction(World world, Entity e) {
                world.getSystem(UnlockMessageSystem.class).createUnlockMessage(challengeId);
                world.getSystem(UnlockMessageSystem.class).createUnlockMessage(ChallengesResource.LEVEL_4_COMPLETE);
                world.getSystem(UnlockMessageSystem.class).createUnlockMessage(ChallengesResource.LEVEL_3_COMPLETE);
            }
        }));

        bag.add(new DuringRoomLoadActionComponent(new Action() {
            @Override
            public void performAction(World world, Entity e) {

                IntBag intBag = world.getAspectSubscriptionManager().get(Aspect.all(ChallengeTimerComponent.class)).getEntities();

                for(int i = 0; i < intBag.size(); i++){
                    world.getEntity(intBag.get(i)).deleteFromWorld();
                }

                world.getSystem(SoundSystem.class).playSound(SoundFileStrings.itemPickUpMix1);
            }
        }));

    }


    public ArenaCreate challengeEndArenaRightPortal(final String challengeId){
        return new ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {
                Arena arena = arenaShellFactory.createSmallArenaNoGrapple(defaultCoords, Arena.ArenaType.NORMAL);
                challengeRoadOnLoadActionEntity(arena, challengeId);

                arena.addEntity(new PortalFactory(assetManager).customSmallPortal(arena.getWidth() / 4 * 3, Measure.units(32.5f),

                        new Action() {
                            @Override
                            public void performAction(World world, Entity e) {
                                world.getSystem(ScreenWipeSystem.class).startScreenWipe(ScreenWipeSystem.Transition.FADE, new Action() {
                                    @Override
                                    public void performAction(World world, Entity e) {
                                        MainGame game = world.getSystem(EndGameSystem.class).getGame();
                                        game.getScreen().dispose();
                                        game.setScreen(new MenuScreen(game));
                                    }
                                });
                            }
                        }));

                return arena;
            }
        };
    }


    public ArenaCreate challengeEndArenaMiddlePortal(final String challengeId){
        return new ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {
                Arena arena = arenaShellFactory.createSmallArenaNoGrapple(defaultCoords, Arena.ArenaType.NORMAL);
                challengeRoadOnLoadActionEntity(arena, challengeId);

                arena.addEntity(new PortalFactory(assetManager).customSmallPortal(arena.getWidth() / 2, Measure.units(32.5f),

                        new Action() {
                            @Override
                            public void performAction(World world, Entity e) {
                                world.getSystem(ScreenWipeSystem.class).startScreenWipe(ScreenWipeSystem.Transition.FADE, new Action() {
                                    @Override
                                    public void performAction(World world, Entity e) {
                                        MainGame game = world.getSystem(EndGameSystem.class).getGame();
                                        game.getScreen().dispose();
                                        game.setScreen(new MenuScreen(game));
                                    }
                                });
                            }
                        }));

                return arena;
            }
        };
    }



}