package com.byrjamin.wickedwizard.factories.arenas.levels;

import com.artemis.Aspect;
import com.artemis.Entity;
import com.artemis.World;
import com.artemis.utils.IntBag;
import com.badlogic.gdx.assets.AssetManager;
import com.byrjamin.wickedwizard.MainGame;
import com.byrjamin.wickedwizard.assets.MenuStrings;
import com.byrjamin.wickedwizard.assets.Mix;
import com.byrjamin.wickedwizard.ecs.components.ai.Action;
import com.byrjamin.wickedwizard.ecs.components.ai.ActionAfterTimeComponent;
import com.byrjamin.wickedwizard.ecs.components.identifiers.ChallengeTimerComponent;
import com.byrjamin.wickedwizard.ecs.systems.graphical.MessageBannerSystem;
import com.byrjamin.wickedwizard.ecs.systems.level.EndGameSystem;
import com.byrjamin.wickedwizard.ecs.systems.level.ScreenWipeSystem;
import com.byrjamin.wickedwizard.factories.AbstractFactory;
import com.byrjamin.wickedwizard.factories.arenas.Arena;
import com.byrjamin.wickedwizard.factories.arenas.ArenaCreate;
import com.byrjamin.wickedwizard.factories.arenas.decor.ArenaEnemyPlacementFactory;
import com.byrjamin.wickedwizard.factories.arenas.decor.ArenaShellFactory;
import com.byrjamin.wickedwizard.factories.arenas.decor.DecorFactory;
import com.byrjamin.wickedwizard.factories.arenas.decor.OnLoadFactory;
import com.byrjamin.wickedwizard.factories.arenas.decor.PortalFactory;
import com.byrjamin.wickedwizard.factories.arenas.skins.ArenaSkin;
import com.byrjamin.wickedwizard.factories.chests.ChestFactory;
import com.byrjamin.wickedwizard.screens.DataSave;
import com.byrjamin.wickedwizard.screens.MenuScreen;
import com.byrjamin.wickedwizard.utils.ComponentBag;
import com.byrjamin.wickedwizard.utils.MapCoords;
import com.byrjamin.wickedwizard.utils.Measure;
import com.byrjamin.wickedwizard.utils.enums.Level;

/**
 * Created by BB on 19/08/2017.
 */

public class ReuseableRooms extends AbstractFactory {

    private ArenaShellFactory arenaShellFactory;
    private ChestFactory chestFactory;
    private DecorFactory decorFactory;
    private ArenaEnemyPlacementFactory arenaEnemyPlacementFactory;

    private ArenaSkin arenaSkin;


    public ReuseableRooms(AssetManager assetManager, ArenaSkin arenaSkin) {
        super(assetManager);
        this.arenaShellFactory = new ArenaShellFactory(assetManager, arenaSkin);
        this.chestFactory = new ChestFactory(assetManager);
        this.decorFactory = new DecorFactory(assetManager, arenaSkin);
    }


    public ArenaCreate startingArena(final Level level){
        return new ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {
                Arena arena = arenaShellFactory.createOmniArenaHiddenGrapple(defaultCoords);
                arena.addEntity(new OnLoadFactory().largeMessageBag(level.getName()));
                arena.addEntity(new OnLoadFactory().startMusicEntity(level.getMusic()));
                return arena;
            }
        };
    }


    public ArenaCreate challengeStartingArena(final Mix mix){
        return new ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {
                Arena arena = arenaShellFactory.createOmniArenaHiddenGrapple(defaultCoords);

                arena.addEntity(new OnLoadFactory().startMusicEntity(mix));
                return arena;
            }
        };
    }


    public ArenaCreate challengeEndArena(final String challengeId){
        return new ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {
                Arena arena = arenaShellFactory.createSmallArenaNoGrapple(defaultCoords);



                ComponentBag bag = arena.createArenaBag();
                bag.add(new ActionAfterTimeComponent(new Action() {
                    @Override
                    public void performAction(World world, Entity e) {

                        IntBag intBag = world.getAspectSubscriptionManager().get(Aspect.all(ChallengeTimerComponent.class)).getEntities();

                        for(int i = 0; i < intBag.size(); i++){
                            world.getEntity(intBag.get(i)).deleteFromWorld();
                        }

                        if(!DataSave.isDataAvailable(challengeId)){
                            DataSave.saveChallengeData(challengeId);
                            world.getSystem(MessageBannerSystem.class).createItemBanner(MenuStrings.TRAIL_COMPLETE, MenuStrings.TRAIL_NEW_ITEM);
                        } else {
                            world.getSystem(MessageBannerSystem.class).createItemBanner(MenuStrings.TRAIL_COMPLETE, MenuStrings.TRAIL_OLD_ITEM);
                        }
                        e.deleteFromWorld();
                    }
                }));


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

/*

                ComponentBag messageAction = new ComponentBag();
                messageAction.add(new ActionAfterTimeComponent(new Action() {
                    @Override
                    public void performAction(World world, Entity e) {
                        world.getSystem(MusicSystem.class).playLevelMusic(level);
                    }
                }, 0f, false));
                messageAction.add(new ExpireComponent(1f));



                arena.addEntity(new OnLoadFactory().nextLevelMessageBagAndMusic(level));*/
                return arena;
            }
        };
    }



}