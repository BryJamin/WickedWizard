package com.byrjamin.wickedwizard.factories.arenas.presets;

import com.artemis.BaseSystem;
import com.artemis.Entity;
import com.artemis.World;
import com.artemis.utils.Bag;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.byrjamin.wickedwizard.assets.FontAssets;
import com.byrjamin.wickedwizard.assets.FileLocationStrings;
import com.byrjamin.wickedwizard.assets.MenuStrings;
import com.byrjamin.wickedwizard.assets.TextureStrings;
import com.byrjamin.wickedwizard.ecs.components.ai.Action;
import com.byrjamin.wickedwizard.ecs.components.ai.ActionAfterTimeComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.FollowPositionComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.MoveToPositionComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.OnRoomLoadActionComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.PositionComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.TextureRegionComponent;
import com.byrjamin.wickedwizard.ecs.systems.ai.OnDeathSystem;
import com.byrjamin.wickedwizard.ecs.systems.audio.MusicSystem;
import com.byrjamin.wickedwizard.ecs.systems.graphical.CameraSystem;
import com.byrjamin.wickedwizard.ecs.systems.level.EndGameSystem;
import com.byrjamin.wickedwizard.ecs.systems.level.MapTeleportationSystem;
import com.byrjamin.wickedwizard.ecs.systems.level.ScreenWipeSystem;
import com.byrjamin.wickedwizard.factories.AbstractFactory;
import com.byrjamin.wickedwizard.factories.arenas.Arena;
import com.byrjamin.wickedwizard.factories.arenas.ArenaBuilder;
import com.byrjamin.wickedwizard.factories.arenas.skins.AllBlackSkin;
import com.byrjamin.wickedwizard.factories.arenas.skins.ArenaSkin;
import com.byrjamin.wickedwizard.screens.MenuButton;
import com.byrjamin.wickedwizard.utils.CenterMath;
import com.byrjamin.wickedwizard.utils.ComponentBag;
import com.byrjamin.wickedwizard.utils.MapCoords;
import com.byrjamin.wickedwizard.utils.Measure;

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
        this.arenaSkin = new AllBlackSkin();
    }



    public Arena createBreakRoom(){

        Arena arena = new ArenaBuilder(assetManager, arenaSkin, Arena.ArenaType.NORMAL)
                .addSection(new ArenaBuilder.Section(new MapCoords(),
                        ArenaBuilder.wall.FULL,
                        ArenaBuilder.wall.FULL,
                        ArenaBuilder.wall.FULL,
                        ArenaBuilder.wall.FULL))
                .buildArena();

        arena.createArenaBag().add(new OnRoomLoadActionComponent(new Action() {
            @Override
            public void performAction(World world, Entity e) {

                Camera gamecam = world.getSystem(CameraSystem.class).getGamecam();

                Entity backScreen = world.createEntity();
                backScreen.edit().add(new PositionComponent());
                backScreen.edit().add(new MoveToPositionComponent());
                backScreen.edit().add(new FollowPositionComponent(gamecam.position, -gamecam.viewportWidth / 2, -gamecam.viewportHeight / 2));
                backScreen.edit().add(new TextureRegionComponent(assetManager.get(FileLocationStrings.spriteAtlas, TextureAtlas.class).findRegion(TextureStrings.BLOCK),
                        gamecam.viewportWidth, gamecam.viewportHeight,
                        TextureRegionComponent.PLAYER_LAYER_NEAR, new Color(Color.BLACK)));


            }
        }));


        ComponentBag componentBag = new ComponentBag();
        componentBag.add(new ActionAfterTimeComponent(new Action() {
            @Override
            public void performAction(World world, Entity e) {
                //world.getSystem(PlayerInputSystem.class).setEnabled(false);
                world.getSystem(MusicSystem.class).fadeOutMusic();
                world.getSystem(OnDeathSystem.class).kill(e);
            }
        }));

        arena.addEntity(componentBag);


        MenuButton.MenuButtonBuilder menuButtonBuilder = new MenuButton.MenuButtonBuilder(FontAssets.medium, atlas.findRegion(TextureStrings.BLOCK))
                .width(buttonWidth)
                .height(buttonHeight)
                .foregroundColor(new Color(Color.WHITE))
                .backgroundColor( new Color(Color.BLACK))
                .action(new Action() {
                    @Override
                    public void performAction(World world, Entity e) {
                        world.getSystem(ScreenWipeSystem.class).startScreenWipe(ScreenWipeSystem.Transition.FADE, new Action() {
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
                        world.getSystem(ScreenWipeSystem.class).startScreenWipe(ScreenWipeSystem.Transition.FADE, new Action() {
                            @Override
                            public void performAction(World world, Entity e) {
                                world.getSystem(MapTeleportationSystem.class).createNewLevel();
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
