package com.bryjamin.wickedwizard.factories.arenas.presetmaps;

import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.bryjamin.wickedwizard.assets.MenuStrings;
import com.bryjamin.wickedwizard.ecs.components.ai.Action;
import com.bryjamin.wickedwizard.ecs.components.ai.ActionAfterTimeComponent;
import com.bryjamin.wickedwizard.ecs.components.ai.ConditionalActionComponent;
import com.bryjamin.wickedwizard.ecs.components.texture.FadeComponent;
import com.bryjamin.wickedwizard.ecs.components.texture.TextureRegionComponent;
import com.bryjamin.wickedwizard.ecs.systems.level.ArenaMap;
import com.bryjamin.wickedwizard.ecs.systems.level.EndGameSystem;
import com.bryjamin.wickedwizard.factories.AbstractFactory;
import com.bryjamin.wickedwizard.factories.arenas.Arena;
import com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder;
import com.bryjamin.wickedwizard.factories.arenas.skins.ArenaSkin;
import com.bryjamin.wickedwizard.screens.DataSave;

/**
 * Created by Home on 28/07/2017.
 */

public class GalleryAtTheEndMap extends AbstractFactory {

    private com.bryjamin.wickedwizard.factories.arenas.decor.ArenaShellFactory arenaShellFactory;
    private com.bryjamin.wickedwizard.factories.arenas.decor.DecorFactory decorFactory;
    private ArenaSkin arenaSkin;

    public GalleryAtTheEndMap(AssetManager assetManager) {
        super(assetManager);
        this.arenaSkin = new com.bryjamin.wickedwizard.factories.arenas.skins.BrightWhiteSkin();
        this.arenaShellFactory = new com.bryjamin.wickedwizard.factories.arenas.decor.ArenaShellFactory(assetManager, arenaSkin);
        this.decorFactory = new com.bryjamin.wickedwizard.factories.arenas.decor.DecorFactory(assetManager, arenaSkin);
    }



    public ArenaMap endGameMap() {
        return new ArenaMap(endRoom().createArena(new com.bryjamin.wickedwizard.utils.MapCoords()));
    }


    public ArenaMap endBossRushMap(String bossRushId) {
        return new ArenaMap(endBossRushRoom(bossRushId).createArena(new com.bryjamin.wickedwizard.utils.MapCoords()));
    }


    private com.bryjamin.wickedwizard.factories.arenas.ArenaCreate endRoom(){


        return new com.bryjamin.wickedwizard.factories.arenas.ArenaCreate() {
            @Override
            public Arena createArena(com.bryjamin.wickedwizard.utils.MapCoords defaultCoords) {

                ArenaBuilder arenaBuilder = new ArenaBuilder(assetManager, arenaSkin, Arena.ArenaType.TRAP);

                arenaBuilder.addSection(new ArenaBuilder.Section(defaultCoords,
                        ArenaBuilder.wall.NONE,
                        ArenaBuilder.wall.NONE,
                        ArenaBuilder.wall.NONE,
                        ArenaBuilder.wall.FULL));

                Arena arena = arenaBuilder.buildArena();


                arena.addEntity(decorFactory.wallBag(-com.bryjamin.wickedwizard.utils.Measure.units(5f), 0, com.bryjamin.wickedwizard.utils.Measure.units(5f), com.bryjamin.wickedwizard.utils.Measure.units(300f)));
                arena.addEntity(decorFactory.wallBag(arena.getWidth(), 0, com.bryjamin.wickedwizard.utils.Measure.units(5f), com.bryjamin.wickedwizard.utils.Measure.units(300f)));


                com.bryjamin.wickedwizard.utils.ComponentBag saveGame = arena.createArenaBag();
                saveGame.add(new com.bryjamin.wickedwizard.ecs.components.ai.OnRoomLoadActionComponent(new Action() {
                    @Override
                    public void performAction(World world, Entity e) {
                        if (!DataSave.isDataAvailable(com.bryjamin.wickedwizard.factories.arenas.challenges.ChallengesResource.LEVEL_5_COMPLETE)) {
                            DataSave.saveChallengeData(com.bryjamin.wickedwizard.factories.arenas.challenges.ChallengesResource.LEVEL_5_COMPLETE);
                            world.getSystem(com.bryjamin.wickedwizard.ecs.systems.graphical.MessageBannerSystem.class).createLevelBanner(MenuStrings.NEW_TRAILS);
                        }
                    }
                }));



                com.bryjamin.wickedwizard.utils.ComponentBag endFade = new com.bryjamin.wickedwizard.utils.ComponentBag();
                endFade.add(new ActionAfterTimeComponent(new Action() {
                    @Override
                    public void performAction(World world, Entity e) {


                        float width = com.bryjamin.wickedwizard.MainGame.GAME_WIDTH * 2;
                        float height = com.bryjamin.wickedwizard.MainGame.GAME_HEIGHT * 2;

                        Entity fadeout = world.createEntity();
                        fadeout.edit().add(new com.bryjamin.wickedwizard.ecs.components.movement.PositionComponent(0,0));
                        fadeout.edit().add(new ActionAfterTimeComponent(new Action() {
                            @Override
                            public void performAction(World world, Entity e) {
                                e.edit().add(new FadeComponent(true, 3.0f, false));
                            }
                        }, 1.5f));
                        fadeout.edit().add(new com.bryjamin.wickedwizard.ecs.components.ai.FollowPositionComponent(world.getSystem(com.bryjamin.wickedwizard.ecs.systems.FindPlayerSystem.class).getPlayerComponent(com.bryjamin.wickedwizard.ecs.components.movement.PositionComponent.class).position, -width / 2, -height / 2));
                        fadeout.edit().add(new TextureRegionComponent(atlas.findRegion(com.bryjamin.wickedwizard.assets.TextureStrings.BLOCK), width, height, TextureRegionComponent.FOREGROUND_LAYER_NEAR, new Color(0,0,0,0)));

                        fadeout.edit().add(new ConditionalActionComponent(new com.bryjamin.wickedwizard.ecs.components.ai.Condition() {
                            @Override
                            public boolean condition(World world, Entity entity) {
                                return entity.getComponent(TextureRegionComponent.class).color.a >= 1f;
                            }
                        }, new Action() {
                            @Override
                            public void performAction(World world, Entity e) {
                                world.getSystem(EndGameSystem.class).startCredits();
                            }
                        }));



                    }
                }, 2.0f));

                arena.addEntity(endFade);

                return arena;
            }
        };

    }




    private com.bryjamin.wickedwizard.factories.arenas.ArenaCreate endBossRushRoom(final String bossRushid){


        return new com.bryjamin.wickedwizard.factories.arenas.ArenaCreate() {
            @Override
            public Arena createArena(com.bryjamin.wickedwizard.utils.MapCoords defaultCoords) {

                ArenaBuilder arenaBuilder = new ArenaBuilder(assetManager, arenaSkin, Arena.ArenaType.TRAP);

                arenaBuilder.addSection(new ArenaBuilder.Section(defaultCoords,
                        ArenaBuilder.wall.NONE,
                        ArenaBuilder.wall.NONE,
                        ArenaBuilder.wall.NONE,
                        ArenaBuilder.wall.FULL));

                Arena arena = arenaBuilder.buildArena();


                arena.addEntity(decorFactory.wallBag(-com.bryjamin.wickedwizard.utils.Measure.units(5f), 0, com.bryjamin.wickedwizard.utils.Measure.units(5f), com.bryjamin.wickedwizard.utils.Measure.units(300f)));
                arena.addEntity(decorFactory.wallBag(arena.getWidth(), 0, com.bryjamin.wickedwizard.utils.Measure.units(5f), com.bryjamin.wickedwizard.utils.Measure.units(300f)));




                arena.createArenaBag().add(new ActionAfterTimeComponent(new Action() {
                    @Override
                    public void performAction(World world, Entity e) {

                        Arena arena = world.getSystem(com.bryjamin.wickedwizard.ecs.systems.level.RoomTransitionSystem.class).getCurrentArena();

                        com.bryjamin.wickedwizard.utils.BagToEntity.bagToEntity(world.createEntity(), new com.bryjamin.wickedwizard.factories.arenas.decor.PortalFactory(assetManager).customSmallPortal(arena.getWidth() / 2, com.bryjamin.wickedwizard.utils.Measure.units(45f),

                                new Action() {
                                    @Override
                                    public void performAction(World world, Entity e) {
                                        world.getSystem(com.bryjamin.wickedwizard.ecs.systems.level.ScreenWipeSystem.class).startScreenWipe(com.bryjamin.wickedwizard.ecs.systems.level.ScreenWipeSystem.Transition.FADE, new Action() {
                                            @Override
                                            public void performAction(World world, Entity e) {
                                                com.bryjamin.wickedwizard.MainGame game = world.getSystem(EndGameSystem.class).getGame();
                                                game.getScreen().dispose();
                                                game.setScreen(new com.bryjamin.wickedwizard.screens.MenuScreen(game));
                                            }
                                        });
                                    }
                                }));



                    }
                }, 0.5f));

                com.bryjamin.wickedwizard.utils.ComponentBag saveGame = arena.createArenaBag();
                saveGame.add(new com.bryjamin.wickedwizard.ecs.components.ai.OnRoomLoadActionComponent(new Action() {
                    @Override
                    public void performAction(World world, Entity e) {

                        if(!DataSave.isDataAvailable(bossRushid)){
                            DataSave.saveChallengeData(bossRushid);
                            world.getSystem(com.bryjamin.wickedwizard.ecs.systems.graphical.MessageBannerSystem.class).createItemBanner(MenuStrings.TRAIL_COMPLETE, MenuStrings.TRAIL_NEW_ITEM);
                        } else {
                            world.getSystem(com.bryjamin.wickedwizard.ecs.systems.graphical.MessageBannerSystem.class).createItemBanner(MenuStrings.TRAIL_COMPLETE, MenuStrings.TRAIL_OLD_ITEM);
                        }
                    }
                }));

                return arena;
            }
        };

    }



}
