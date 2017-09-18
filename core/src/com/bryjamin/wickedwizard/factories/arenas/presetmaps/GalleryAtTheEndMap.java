package com.bryjamin.wickedwizard.factories.arenas.presetmaps;

import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
import com.bryjamin.wickedwizard.MainGame;
import com.bryjamin.wickedwizard.assets.FontAssets;
import com.bryjamin.wickedwizard.assets.MenuStrings;
import com.bryjamin.wickedwizard.assets.PlayerIDs;
import com.bryjamin.wickedwizard.assets.TextureStrings;
import com.bryjamin.wickedwizard.ecs.components.DisablePlayerInputComponent;
import com.bryjamin.wickedwizard.ecs.components.ai.Action;
import com.bryjamin.wickedwizard.ecs.components.ai.ActionAfterTimeComponent;
import com.bryjamin.wickedwizard.ecs.components.ai.ConditionalActionComponent;
import com.bryjamin.wickedwizard.ecs.components.ai.FollowPositionComponent;
import com.bryjamin.wickedwizard.ecs.components.ai.DuringRoomLoadActionComponent;
import com.bryjamin.wickedwizard.ecs.components.movement.CollisionBoundComponent;
import com.bryjamin.wickedwizard.ecs.components.movement.PositionComponent;
import com.bryjamin.wickedwizard.ecs.components.texture.FadeComponent;
import com.bryjamin.wickedwizard.ecs.components.texture.TextureFontComponent;
import com.bryjamin.wickedwizard.ecs.components.texture.TextureRegionComponent;
import com.bryjamin.wickedwizard.ecs.systems.graphical.CameraSystem;
import com.bryjamin.wickedwizard.ecs.systems.graphical.UISystem;
import com.bryjamin.wickedwizard.ecs.systems.graphical.UnlockMessageSystem;
import com.bryjamin.wickedwizard.ecs.systems.input.PlayerInputSystem;
import com.bryjamin.wickedwizard.ecs.systems.level.ArenaMap;
import com.bryjamin.wickedwizard.ecs.systems.level.EndGameSystem;
import com.bryjamin.wickedwizard.factories.AbstractFactory;
import com.bryjamin.wickedwizard.factories.arenas.Arena;
import com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder;
import com.bryjamin.wickedwizard.factories.arenas.ArenaCreate;
import com.bryjamin.wickedwizard.factories.arenas.MapCleaner;
import com.bryjamin.wickedwizard.factories.arenas.challenges.ChallengesResource;
import com.bryjamin.wickedwizard.factories.arenas.decor.ArenaShellFactory;
import com.bryjamin.wickedwizard.factories.arenas.decor.DecorFactory;
import com.bryjamin.wickedwizard.factories.arenas.skins.ArenaSkin;
import com.bryjamin.wickedwizard.factories.arenas.skins.BrightWhiteSkin;
import com.bryjamin.wickedwizard.screens.DataSave;
import com.bryjamin.wickedwizard.utils.BagToEntity;
import com.bryjamin.wickedwizard.utils.CenterMath;
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

    public GalleryAtTheEndMap(AssetManager assetManager) {
        super(assetManager);
        this.arenaSkin = new BrightWhiteSkin();
        this.arenaShellFactory = new ArenaShellFactory(assetManager, arenaSkin);
        this.decorFactory = new DecorFactory(assetManager, arenaSkin);
        this.mapCleaner = new MapCleaner(decorFactory);
    }



    public ArenaMap endGameMap() {

        ArenaMap arenaMap = new ArenaMap(galleryGrappleStart().createArena(new MapCoords(0,0)),
                galleryFirstRoom().createArena(new MapCoords(0,2)),
                galleryCharacterUnlockRoom().createArena(new MapCoords(1, 2)),
                galleryExitRoom().createArena(new MapCoords(2, 2)),
                galleryTheOutSide().createArena(new MapCoords(3, 2))
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

                ArenaBuilder arenaBuilder = new ArenaBuilder(assetManager, arenaSkin, Arena.ArenaType.TRAP);

                arenaBuilder
                        .addSection(new ArenaBuilder.Section(defaultCoords,
                                ArenaBuilder.wall.FULL,
                                ArenaBuilder.wall.FULL,
                                ArenaBuilder.wall.NONE,
                                ArenaBuilder.wall.FULL))
                        .addSection(new ArenaBuilder.Section(new MapCoords(defaultCoords.getX(), defaultCoords.getY() + 1),
                                ArenaBuilder.wall.FULL,
                                ArenaBuilder.wall.FULL,
                                ArenaBuilder.wall.GRAPPLE,
                                ArenaBuilder.wall.NONE));

                Arena arena = arenaBuilder.buildArena();


                arena.addEntity(decorFactory.wallBag(-Measure.units(5f), 0, Measure.units(5f), Measure.units(300f)));
                arena.addEntity(decorFactory.wallBag(arena.getWidth(), 0, Measure.units(5f), Measure.units(300f)));


                ComponentBag saveGame = arena.createArenaBag();
                saveGame.add(new ActionAfterTimeComponent(new Action() {
                    @Override
                    public void performAction(World world, Entity e) {
                        world.getSystem(UnlockMessageSystem.class).createUnlockMessage(ChallengesResource.LEVEL_5_COMPLETE);
                    }

                }));



                arena.addEntity(decorFactory.grapplePointBag(arena.getWidth() / 2, Measure.units(50f)));
                arena.addEntity(decorFactory.platform(0, Measure.units(65f), arena.getWidth()));

                return arena;
            }
        };

    }


    private ArenaCreate galleryFirstRoom(){


        return new ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {

                Arena arena = arenaShellFactory.createOmniArenaHiddenGrapple(defaultCoords, Arena.ArenaType.NORMAL);


                float x = 0;
                float y = Measure.units(40f);

                ComponentBag welcomeText = arena.createArenaBag();
                welcomeText.add(new PositionComponent(x, y));
                welcomeText.add(new CollisionBoundComponent(new Rectangle(x, y, arena.getWidth(), Measure.units(5f))));
                welcomeText.add(new TextureFontComponent(FontAssets.medium, MenuStrings.Gallery.WELCOME, arenaSkin.getWallTint()));


                return arena;
            }
        };

    }


    private ArenaCreate galleryCharacterUnlockRoom(){


        return new ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {

                Arena arena = arenaShellFactory.createOmniArenaHiddenGrapple(defaultCoords, Arena.ArenaType.NORMAL);


                float x = 0;
                float y = Measure.units(45f);



                PlayerIDs.PlayableCharacter[] characterFixedArray = PlayerIDs.endGameUnlockAbleCharacters;


                for(PlayerIDs.PlayableCharacter character : characterFixedArray) {
                    if(!DataSave.isDataAvailable(character.getUnlockString())) {
                       // characterArray.add(character);

                        ComponentBag welcomeText = arena.createArenaBag();
                        welcomeText.add(new PositionComponent(x, y));
                        welcomeText.add(new CollisionBoundComponent(new Rectangle(x, y, arena.getWidth(), Measure.units(5f))));
                        welcomeText.add(new TextureFontComponent(FontAssets.medium, character.getName() + MenuStrings.Gallery.CHARACTER_UNLOCK, arenaSkin.getWallTint()));


                        ComponentBag bag = arena.createArenaBag();

                        float characterSelectWidth = Measure.units(10f);
                        float characterSelectHeight = Measure.units(10f);

                        bag.add(new PositionComponent(CenterMath.offsetX(MainGame.GAME_WIDTH, (characterSelectWidth)), Measure.units(30f)));
                        bag.add(new TextureRegionComponent(atlas.findRegion(character.getRegion()), Measure.units(10f), Measure.units(10f), TextureRegionComponent.BACKGROUND_LAYER_NEAR));


                        DataSave.saveChallengeData(character.getUnlockString());


                        break;


                    }
                }


                return arena;
            }
        };

    }


    private ArenaCreate galleryExitRoom(){


        return new ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {

                Arena arena = arenaShellFactory.createOmniArenaHiddenGrapple(defaultCoords, Arena.ArenaType.NORMAL);

                float x = 0;
                float y = Measure.units(40f);

                ComponentBag welcomeText = arena.createArenaBag();
                welcomeText.add(new PositionComponent(x, y));
                welcomeText.add(new CollisionBoundComponent(new Rectangle(x, y, arena.getWidth(), Measure.units(5f))));
                welcomeText.add(new TextureFontComponent(FontAssets.medium, MenuStrings.Gallery.GALLERY_END, arenaSkin.getWallTint()));

                return arena;
            }
        };

    }





    private ArenaCreate galleryTheOutSide(){


        return new ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {


                Arena arena = new ArenaBuilder(assetManager, arenaSkin, Arena.ArenaType.TRAP)
                        .addSection(new ArenaBuilder.Section(defaultCoords,
                                ArenaBuilder.wall.DOOR,
                                ArenaBuilder.wall.NONE,
                                ArenaBuilder.wall.NONE,
                                ArenaBuilder.wall.FULL))
                        .addSection(new ArenaBuilder.Section(new MapCoords(defaultCoords.getX() + 1, defaultCoords.getY()),
                                ArenaBuilder.wall.NONE,
                                ArenaBuilder.wall.NONE,
                                ArenaBuilder.wall.NONE,
                                ArenaBuilder.wall.FULL))
                        .addSection(new ArenaBuilder.Section(new MapCoords(defaultCoords.getX() + 2, defaultCoords.getY()),
                                ArenaBuilder.wall.NONE,
                                ArenaBuilder.wall.NONE,
                                ArenaBuilder.wall.NONE,
                                ArenaBuilder.wall.FULL))
                        .buildArena();

                arena.setWidth(MainGame.GAME_WIDTH * 2);


                ComponentBag setUp = arena.createArenaBag();
                setUp.add(new DuringRoomLoadActionComponent(new Action() {
                    @Override
                    public void performAction(World world, Entity e) {
                        world.getSystem(UISystem.class).disable = true;

                        Entity playerMover = world.createEntity();
                        playerMover.edit().add(new DisablePlayerInputComponent());
                        playerMover.edit().add(new ActionAfterTimeComponent(new Action() {
                            @Override
                            public void performAction(World world, Entity e) {
                                world.getSystem(PlayerInputSystem.class).autoMove(Measure.units(5000));
                            }
                        }, 0, true));

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
                        fadeout.edit().add(new ActionAfterTimeComponent(new Action() {
                            @Override
                            public void performAction(World world, Entity e) {
                                e.edit().add(new FadeComponent(true, 3.0f, false));
                            }
                        }, 1.5f));
                        fadeout.edit().add(new FollowPositionComponent(world.getSystem(CameraSystem.class).getGamecam().position, -width / 2, -height / 2));
                        fadeout.edit().add(new TextureRegionComponent(atlas.findRegion(TextureStrings.BLOCK), width, height, TextureRegionComponent.FOREGROUND_LAYER_NEAR, new Color(0,0,0,0)));

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




    private ArenaCreate endBossRushRoom(final String bossRushid){


        return new com.bryjamin.wickedwizard.factories.arenas.ArenaCreate() {
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
                                                com.bryjamin.wickedwizard.MainGame game = world.getSystem(EndGameSystem.class).getGame();
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
