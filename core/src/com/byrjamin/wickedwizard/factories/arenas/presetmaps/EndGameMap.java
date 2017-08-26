package com.byrjamin.wickedwizard.factories.arenas.presetmaps;

import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.byrjamin.wickedwizard.MainGame;
import com.byrjamin.wickedwizard.assets.MenuStrings;
import com.byrjamin.wickedwizard.assets.TextureStrings;
import com.byrjamin.wickedwizard.ecs.components.ai.Action;
import com.byrjamin.wickedwizard.ecs.components.ai.ActionAfterTimeComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.ActionOnTouchComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.Condition;
import com.byrjamin.wickedwizard.ecs.components.ai.ConditionalActionComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.FollowPositionComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.OnRoomLoadActionComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.PositionComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.FadeComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.TextureFontComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.TextureRegionComponent;
import com.byrjamin.wickedwizard.ecs.systems.FindPlayerSystem;
import com.byrjamin.wickedwizard.ecs.systems.graphical.MessageBannerSystem;
import com.byrjamin.wickedwizard.ecs.systems.level.ArenaMap;
import com.byrjamin.wickedwizard.ecs.systems.level.EndGameSystem;
import com.byrjamin.wickedwizard.factories.AbstractFactory;
import com.byrjamin.wickedwizard.factories.arenas.Arena;
import com.byrjamin.wickedwizard.factories.arenas.ArenaBuilder;
import com.byrjamin.wickedwizard.factories.arenas.ArenaCreate;
import com.byrjamin.wickedwizard.factories.arenas.challenges.ChallengesResource;
import com.byrjamin.wickedwizard.factories.arenas.decor.ArenaShellFactory;
import com.byrjamin.wickedwizard.factories.arenas.decor.DecorFactory;
import com.byrjamin.wickedwizard.factories.arenas.skins.ArenaSkin;
import com.byrjamin.wickedwizard.factories.arenas.skins.BrightWhiteSkin;
import com.byrjamin.wickedwizard.screens.DataSave;
import com.byrjamin.wickedwizard.screens.MenuScreen;
import com.byrjamin.wickedwizard.utils.ComponentBag;
import com.byrjamin.wickedwizard.utils.MapCoords;
import com.byrjamin.wickedwizard.utils.Measure;

/**
 * Created by Home on 28/07/2017.
 */

public class EndGameMap extends AbstractFactory {

    private ArenaShellFactory arenaShellFactory;
    private DecorFactory decorFactory;
    private ArenaSkin arenaSkin;

    public EndGameMap(AssetManager assetManager) {
        super(assetManager);
        this.arenaSkin = new BrightWhiteSkin();
        this.arenaShellFactory = new ArenaShellFactory(assetManager, arenaSkin);
        this.decorFactory = new DecorFactory(assetManager, arenaSkin);
    }



    public ArenaMap endGameMap() {
        return new ArenaMap(endRoom().createArena(new MapCoords()));
    }


    private ArenaCreate endRoom(){


        return new ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {

                ArenaBuilder arenaBuilder = new ArenaBuilder(assetManager, arenaSkin);

                arenaBuilder.addSection(new ArenaBuilder.Section(defaultCoords,
                        ArenaBuilder.wall.NONE,
                        ArenaBuilder.wall.NONE,
                        ArenaBuilder.wall.NONE,
                        ArenaBuilder.wall.FULL));

                Arena arena = arenaBuilder.buildArena();


                arena.addEntity(decorFactory.wallBag(-Measure.units(5f), 0, Measure.units(5f), Measure.units(300f)));
                arena.addEntity(decorFactory.wallBag(arena.getWidth(), 0, Measure.units(5f), Measure.units(300f)));






                ComponentBag saveGame = arena.createArenaBag();
                saveGame.add(new OnRoomLoadActionComponent(new Action() {
                    @Override
                    public void performAction(World world, Entity e) {
                        if (!DataSave.isDataAvailable(ChallengesResource.LEVEL_5_COMPLETE)) {
                            DataSave.saveChallengeData(ChallengesResource.LEVEL_5_COMPLETE);
                            world.getSystem(MessageBannerSystem.class).createLevelBanner(MenuStrings.NEW_TRAILS);
                        }
                    }
                }));



                ComponentBag endFade = new ComponentBag();
                endFade.add(new ActionAfterTimeComponent(new Action() {
                    @Override
                    public void performAction(World world, Entity e) {


                        float width = MainGame.GAME_WIDTH * 2;
                        float height = MainGame.GAME_HEIGHT * 2;

                        Entity fadeout = world.createEntity();
                        fadeout.edit().add(new PositionComponent(0,0));
                        fadeout.edit().add(new ActionAfterTimeComponent(new Action() {
                            @Override
                            public void performAction(World world, Entity e) {
                                e.edit().add(new FadeComponent(true, 3.0f, false));
                            }
                        }, 1.5f));
                        fadeout.edit().add(new FollowPositionComponent(world.getSystem(FindPlayerSystem.class).getPlayerComponent(PositionComponent.class).position, -width / 2, -height / 2));
                        fadeout.edit().add(new TextureRegionComponent(atlas.findRegion(TextureStrings.BLOCK), width, height, TextureRegionComponent.FOREGROUND_LAYER_NEAR, new Color(0,0,0,0)));

                        fadeout.edit().add(new ConditionalActionComponent(new Condition() {
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




}
