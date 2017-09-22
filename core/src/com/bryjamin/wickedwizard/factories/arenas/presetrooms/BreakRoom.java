package com.bryjamin.wickedwizard.factories.arenas.presetrooms;

import com.artemis.BaseSystem;
import com.artemis.Entity;
import com.artemis.World;
import com.artemis.utils.Bag;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.bryjamin.wickedwizard.assets.MenuStrings;
import com.bryjamin.wickedwizard.assets.TextureStrings;
import com.bryjamin.wickedwizard.ecs.components.DisablePlayerInputComponent;
import com.bryjamin.wickedwizard.ecs.components.ai.Action;
import com.bryjamin.wickedwizard.ecs.components.ai.ActionAfterTimeComponent;
import com.bryjamin.wickedwizard.ecs.components.ai.DuringRoomLoadActionComponent;
import com.bryjamin.wickedwizard.ecs.components.ai.FollowPositionComponent;
import com.bryjamin.wickedwizard.ecs.components.ai.MoveToPositionComponent;
import com.bryjamin.wickedwizard.ecs.components.movement.PositionComponent;
import com.bryjamin.wickedwizard.ecs.components.texture.TextureRegionComponent;
import com.bryjamin.wickedwizard.ecs.systems.ai.OnDeathSystem;
import com.bryjamin.wickedwizard.ecs.systems.audio.MusicSystem;
import com.bryjamin.wickedwizard.ecs.systems.level.EndGameSystem;
import com.bryjamin.wickedwizard.factories.AbstractFactory;
import com.bryjamin.wickedwizard.factories.arenas.Arena;
import com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder;
import com.bryjamin.wickedwizard.factories.arenas.skins.ArenaSkin;
import com.bryjamin.wickedwizard.utils.CenterMath;
import com.bryjamin.wickedwizard.utils.ComponentBag;
import com.bryjamin.wickedwizard.utils.MapCoords;
import com.bryjamin.wickedwizard.utils.Measure;

/**
 * Created by Home on 30/07/2017.
 */

public class BreakRoom extends AbstractFactory {

    private ArenaSkin arenaSkin;


    private static final float buttonWidth =  Measure.units(27.5f);
    private static final float buttonHeight = Measure.units(20f);

    private static final float startY = Measure.units(25f);

    public BreakRoom(AssetManager assetManager) {
        super(assetManager);
        this.arenaSkin = new com.bryjamin.wickedwizard.factories.arenas.skins.AllBlackSkin();
    }



    public Arena createBreakRoom(){

        Arena arena = new ArenaBuilder(assetManager, arenaSkin, Arena.ArenaType.NORMAL)
                .addSection(new ArenaBuilder.Section(new MapCoords(),
                        ArenaBuilder.wall.FULL,
                        ArenaBuilder.wall.FULL,
                        ArenaBuilder.wall.FULL,
                        ArenaBuilder.wall.FULL))
                .buildArena();

        arena.createArenaBag().add(new DuringRoomLoadActionComponent(new Action() {
            @Override
            public void performAction(World world, Entity e) {

                Camera gamecam = world.getSystem(com.bryjamin.wickedwizard.ecs.systems.graphical.CameraSystem.class).getGamecam();

                Entity backScreen = world.createEntity();
                backScreen.edit().add(new PositionComponent());
                backScreen.edit().add(new DisablePlayerInputComponent());
                backScreen.edit().add(new MoveToPositionComponent());
                backScreen.edit().add(new FollowPositionComponent(gamecam.position, -gamecam.viewportWidth / 2, -gamecam.viewportHeight / 2));
                backScreen.edit().add(new TextureRegionComponent(assetManager.get(com.bryjamin.wickedwizard.assets.FileLocationStrings.spriteAtlas, TextureAtlas.class).findRegion(TextureStrings.BLOCK),
                        gamecam.viewportWidth, gamecam.viewportHeight,
                        TextureRegionComponent.PLAYER_LAYER_NEAR, new Color(Color.BLACK)));

            }
        }));





        ComponentBag componentBag = new ComponentBag();
        componentBag.add(new ActionAfterTimeComponent(new Action() {
            @Override
            public void performAction(World world, Entity e) {
                world.getSystem(MusicSystem.class).fadeOutMusic();
                world.getSystem(OnDeathSystem.class).kill(e);
            }
        }, 0f));

        arena.addEntity(componentBag);


        com.bryjamin.wickedwizard.screens.MenuButton.MenuButtonBuilder menuButtonBuilder = new com.bryjamin.wickedwizard.screens.MenuButton.MenuButtonBuilder(com.bryjamin.wickedwizard.assets.FontAssets.medium, atlas.findRegion(TextureStrings.BLOCK))
                .width(buttonWidth)
                .height(buttonHeight)
                .foregroundColor(new Color(Color.BLACK))
                .backgroundColor(new Color(Color.WHITE))
                .action(new Action() {
                    @Override
                    public void performAction(World world, Entity e) {
                        world.getSystem(com.bryjamin.wickedwizard.ecs.systems.level.ScreenWipeSystem.class).startScreenWipe(com.bryjamin.wickedwizard.ecs.systems.level.ScreenWipeSystem.Transition.FADE, new Action() {
                            @Override
                            public void performAction(World world, Entity e) {
                                world.getSystem(EndGameSystem.class).quickSaveAndBackToMenu();
                            }
                        });
                    }
                });

        Bag<ComponentBag> bags = menuButtonBuilder.build().createButton(MenuStrings.BREAK, Measure.units(10f), CenterMath.offsetY(arena.getHeight() - Measure.units(15f), buttonHeight) + Measure.units(10f));


        for(ComponentBag bag : bags){
            arena.addEntity(bag);
        }

        bags = menuButtonBuilder
                .action(new Action() {
                    @Override
                    public void performAction(World world, Entity e) {
                        world.getSystem(com.bryjamin.wickedwizard.ecs.systems.level.ScreenWipeSystem.class).startScreenWipe(com.bryjamin.wickedwizard.ecs.systems.level.ScreenWipeSystem.Transition.FADE, new Action() {
                            @Override
                            public void performAction(World world, Entity e) {
                                world.getSystem(com.bryjamin.wickedwizard.ecs.systems.level.MapTeleportationSystem.class).createNewLevel();
                                for(BaseSystem s: world.getSystems()){
                                    s.setEnabled(true);
                                }
                            }
                        });
                    }
                }).build().createButton(MenuStrings.CONTINUE, Measure.units(62.5f), CenterMath.offsetY(arena.getHeight() - Measure.units(15f), buttonHeight) + Measure.units(10f));

        for(ComponentBag bag : bags){
            arena.addEntity(bag);
        }


        return arena;

    }




}
