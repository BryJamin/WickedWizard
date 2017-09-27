package com.bryjamin.wickedwizard.factories.arenas.presetmaps;

import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.bryjamin.wickedwizard.MainGame;
import com.bryjamin.wickedwizard.assets.ColorResource;
import com.bryjamin.wickedwizard.assets.MusicStrings;
import com.bryjamin.wickedwizard.assets.TextureStrings;
import com.bryjamin.wickedwizard.ecs.components.DisablePlayerInputComponent;
import com.bryjamin.wickedwizard.ecs.components.ai.Action;
import com.bryjamin.wickedwizard.ecs.components.ai.ActionAfterTimeComponent;
import com.bryjamin.wickedwizard.ecs.components.ai.ConditionalActionComponent;
import com.bryjamin.wickedwizard.ecs.components.ai.DuringRoomLoadActionComponent;
import com.bryjamin.wickedwizard.ecs.components.ai.FollowCameraComponent;
import com.bryjamin.wickedwizard.ecs.components.identifiers.EnemyComponent;
import com.bryjamin.wickedwizard.ecs.components.movement.PositionComponent;
import com.bryjamin.wickedwizard.ecs.components.texture.FadeComponent;
import com.bryjamin.wickedwizard.ecs.components.texture.TextureRegionComponent;
import com.bryjamin.wickedwizard.ecs.systems.graphical.AfterUIRenderingSystem;
import com.bryjamin.wickedwizard.ecs.systems.graphical.UISystem;
import com.bryjamin.wickedwizard.ecs.systems.graphical.UnlockMessageSystem;
import com.bryjamin.wickedwizard.ecs.systems.input.PlayerInputSystem;
import com.bryjamin.wickedwizard.ecs.systems.level.ArenaMap;
import com.bryjamin.wickedwizard.ecs.systems.level.GameSystem;
import com.bryjamin.wickedwizard.factories.AbstractFactory;
import com.bryjamin.wickedwizard.factories.arenas.Arena;
import com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder;
import com.bryjamin.wickedwizard.factories.arenas.ArenaCreate;
import com.bryjamin.wickedwizard.factories.arenas.MapCleaner;
import com.bryjamin.wickedwizard.factories.arenas.decor.ArenaShellFactory;
import com.bryjamin.wickedwizard.factories.arenas.decor.DecorFactory;
import com.bryjamin.wickedwizard.factories.arenas.decor.OnLoadFactory;
import com.bryjamin.wickedwizard.factories.arenas.skins.ArenaSkin;
import com.bryjamin.wickedwizard.factories.arenas.skins.BrightWhiteSkin;
import com.bryjamin.wickedwizard.utils.BagToEntity;
import com.bryjamin.wickedwizard.utils.ComponentBag;
import com.bryjamin.wickedwizard.utils.MapCoords;
import com.bryjamin.wickedwizard.utils.Measure;

/**
 * Created by Home on 28/07/2017.
 */

public class GalleryAtTheEndMap extends AbstractFactory {

    private com.bryjamin.wickedwizard.factories.arenas.decor.ArenaShellFactory arenaShellFactory;
    private com.bryjamin.wickedwizard.factories.arenas.decor.DecorFactory decorFactory;
    private ArenaSkin arenaSkin;
    private MapCleaner mapCleaner;

    private static final float timeUntilcreditFade = 2.0f;
    private static final float characterPauseTime = 2.0f;
    private static final float creditFadeDuration = 2.0f;

    public GalleryAtTheEndMap(AssetManager assetManager) {
        super(assetManager);
        this.arenaSkin = new BrightWhiteSkin();
        this.arenaShellFactory = new ArenaShellFactory(assetManager, arenaSkin);
        this.decorFactory = new DecorFactory(assetManager, arenaSkin);
        this.mapCleaner = new MapCleaner(decorFactory);
    }



    public ArenaMap galleryMap() {

        ArenaMap arenaMap = new ArenaMap(galleryGrappleStart().createArena(new MapCoords(0,0)),
                galleryTheOutSide().createArena(new MapCoords(0, 4))
                );

        mapCleaner.cleanArenas(arenaMap.getRoomArray());


        return arenaMap;
    }


    public ArenaMap endBossRushMap(String bossRushId) {
        return new ArenaMap(endBossRushRoom(bossRushId).createArena(new MapCoords()));
    }


    private ArenaCreate galleryGrappleStart(){


        return new ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {


                Arena arena =  new ArenaBuilder(assetManager, arenaSkin, Arena.ArenaType.TRAP)
                        .addSection(new ArenaBuilder.Section(defaultCoords,
                                ArenaBuilder.wall.FULL,
                                ArenaBuilder.wall.FULL,
                                ArenaBuilder.wall.NONE,
                                ArenaBuilder.wall.FULL))
                        .addSection(new ArenaBuilder.Section(new MapCoords(defaultCoords.getX(), defaultCoords.getY() + 1),
                                ArenaBuilder.wall.FULL,
                                ArenaBuilder.wall.FULL,
                                ArenaBuilder.wall.NONE,
                                ArenaBuilder.wall.NONE))
                        .addSection(new ArenaBuilder.Section(new MapCoords(defaultCoords.getX(), defaultCoords.getY() + 2),
                                ArenaBuilder.wall.FULL,
                                ArenaBuilder.wall.FULL,
                                ArenaBuilder.wall.NONE,
                                ArenaBuilder.wall.NONE))
                        .addSection(new ArenaBuilder.Section(new MapCoords(defaultCoords.getX(), defaultCoords.getY() + 3),
                                ArenaBuilder.wall.FULL,
                                ArenaBuilder.wall.FULL,
                                ArenaBuilder.wall.GRAPPLE,
                                ArenaBuilder.wall.NONE)).buildArena();

                arena.addEntity(decorFactory.wallBag(-Measure.units(5f), 0, Measure.units(5f), Measure.units(300f)));
                arena.addEntity(decorFactory.wallBag(arena.getWidth(), 0, Measure.units(5f), Measure.units(300f)));

                for(int i = 0; i < 6; i++){
                    arena.addEntity(decorFactory.grapplePointBag(arena.getWidth() / 2, Measure.units(50f) + Measure.units(30f) * i));
                }

                arena.addEntity(new OnLoadFactory().startMusicEntity(MusicStrings.BG_MAIN_MENU));

                return arena;
            }
        };

    }


    /**
     * Arena That Displays the Outside of the labyrinth, where the player loses input control and the player
     * character walks off screen.
     * @return
     */
    private ArenaCreate galleryTheOutSide(){


        return new ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {


                Arena arena = new ArenaBuilder(assetManager, arenaSkin, Arena.ArenaType.TRAP)
                        .addSection(new ArenaBuilder.Section(defaultCoords,
                                ArenaBuilder.wall.NONE,
                                ArenaBuilder.wall.NONE,
                                ArenaBuilder.wall.NONE,
                                ArenaBuilder.wall.DOOR))
                        .addSection(new ArenaBuilder.Section(new MapCoords(defaultCoords.getX() - 1, defaultCoords.getY()),
                                ArenaBuilder.wall.NONE,
                                ArenaBuilder.wall.NONE,
                                ArenaBuilder.wall.NONE,
                                ArenaBuilder.wall.NONE))
                        .addSection(new ArenaBuilder.Section(new MapCoords(defaultCoords.getX() + 1, defaultCoords.getY()),
                                ArenaBuilder.wall.NONE,
                                ArenaBuilder.wall.NONE,
                                ArenaBuilder.wall.NONE,
                                ArenaBuilder.wall.NONE))
                        .isSafe(false)
                        .buildArena();

                arena.setWidth(MainGame.GAME_WIDTH);

                arena.addEntity(decorFactory.wallBag(-arena.getWidth() * 5, Measure.units(0), arena.getWidth() * 5, Measure.units(10f)));
                arena.addEntity(decorFactory.wallBag(arena.getWidth(), Measure.units(0), arena.getWidth() * 5, Measure.units(10f)));


                ComponentBag lock = new ComponentBag();
                lock.add(new EnemyComponent());
                arena.addEntity(lock);


                ComponentBag background = arena.createArenaBag();

                background.add(new PositionComponent(0,0));
                background.add(new TextureRegionComponent(atlas.findRegion(TextureStrings.MAIN_MENU_BACKDROP),
                        MainGame.GAME_WIDTH ,
                        MainGame.GAME_HEIGHT,
                        TextureRegionComponent.BACKGROUND_LAYER_MIDDLE,
                        ColorResource.RGBtoColor(137, 207, 240, 1)));
                arena.addEntity(background);



                ComponentBag setUp = arena.createArenaBag();
                setUp.add(new DuringRoomLoadActionComponent(new Action() {
                    @Override
                    public void performAction(World world, Entity e) {
                        world.getSystem(UISystem.class).disable = true;
                        world.getSystem(AfterUIRenderingSystem.class).disable = true;

                        Entity playerMover = world.createEntity();
                        playerMover.edit().add(new DisablePlayerInputComponent());
                        playerMover.edit().add(new ActionAfterTimeComponent(new Action() {

                            float destination = MathUtils.random.nextBoolean() ? Measure.units(5000) : -Measure.units(5000);

                            @Override
                            public void performAction(World world, Entity e){
                                e.getComponent(ActionAfterTimeComponent.class).resetTime = 0f;
                                world.getSystem(PlayerInputSystem.class).autoMove(destination, -0.3f);
                            }
                        }, characterPauseTime, true));

                    }
                }));


                ComponentBag endFade = new ComponentBag();
                endFade.add(new ActionAfterTimeComponent(new Action() {
                    @Override
                    public void performAction(World world, Entity e) {


                        float width = com.bryjamin.wickedwizard.MainGame.GAME_WIDTH * 2;
                        float height = com.bryjamin.wickedwizard.MainGame.GAME_HEIGHT * 2;

                        Entity fadeout = world.createEntity();
                        fadeout.edit().add(new PositionComponent(0,0));
                        fadeout.edit().add(new FadeComponent(true, creditFadeDuration, false));
                        fadeout.edit().add(new FollowCameraComponent(0,0));
                        fadeout.edit().add(new TextureRegionComponent(atlas.findRegion(TextureStrings.BLOCK), width, height, TextureRegionComponent.FOREGROUND_LAYER_NEAR, new Color(0,0,0,0)));

                        fadeout.edit().add(new ConditionalActionComponent(new com.bryjamin.wickedwizard.ecs.components.ai.Condition() {
                            @Override
                            public boolean condition(World world, Entity entity) {
                                return entity.getComponent(TextureRegionComponent.class).color.a >= 1f;
                            }
                        }, new Action() {
                            @Override
                            public void performAction(World world, Entity e) {
                                world.getSystem(GameSystem.class).startCredits();
                            }
                        }));



                    }
                }, timeUntilcreditFade));

                arena.addEntity(endFade);
                return arena;
            }
        };

    }




    private ArenaCreate endBossRushRoom(final String bossRushid){


        return new ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {

                ArenaBuilder arenaBuilder = new ArenaBuilder(assetManager, arenaSkin, Arena.ArenaType.TRAP);

                arenaBuilder.addSection(new ArenaBuilder.Section(defaultCoords,
                        ArenaBuilder.wall.NONE,
                        ArenaBuilder.wall.NONE,
                        ArenaBuilder.wall.NONE,
                        ArenaBuilder.wall.FULL));

                Arena arena = arenaBuilder.buildArena();


                arena.addEntity(decorFactory.wallBag(-Measure.units(5f), 0, Measure.units(5f), Measure.units(300f)));
                arena.addEntity(decorFactory.wallBag(arena.getWidth(), 0, Measure.units(5f), Measure.units(300f)));




                arena.createArenaBag().add(new ActionAfterTimeComponent(new Action() {
                    @Override
                    public void performAction(World world, Entity e) {

                        Arena arena = world.getSystem(com.bryjamin.wickedwizard.ecs.systems.level.RoomTransitionSystem.class).getCurrentArena();

                        BagToEntity.bagToEntity(world.createEntity(), new com.bryjamin.wickedwizard.factories.arenas.decor.PortalFactory(assetManager).customSmallPortal(arena.getWidth() / 2, Measure.units(45f),

                                new Action() {
                                    @Override
                                    public void performAction(World world, Entity e) {
                                        world.getSystem(com.bryjamin.wickedwizard.ecs.systems.level.ScreenWipeSystem.class).startScreenWipe(com.bryjamin.wickedwizard.ecs.systems.level.ScreenWipeSystem.Transition.FADE, new Action() {
                                            @Override
                                            public void performAction(World world, Entity e) {
                                                com.bryjamin.wickedwizard.MainGame game = world.getSystem(GameSystem.class).getGame();
                                                game.getScreen().dispose();
                                                game.setScreen(new com.bryjamin.wickedwizard.screens.MenuScreen(game));
                                            }
                                        });
                                    }
                                }));



                    }
                }, 0.5f));

                ComponentBag saveGame = arena.createArenaBag();
                saveGame.add(new ActionAfterTimeComponent(new Action() {
                    @Override
                    public void performAction(World world, Entity e) {
                        world.getSystem(UnlockMessageSystem.class).createUnlockMessage(bossRushid);
                    }
                }));

                return arena;
            }
        };

    }



}
