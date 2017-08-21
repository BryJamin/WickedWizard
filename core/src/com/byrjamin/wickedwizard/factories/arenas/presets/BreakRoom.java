package com.byrjamin.wickedwizard.factories.arenas.presets;

import com.artemis.BaseSystem;
import com.artemis.Entity;
import com.artemis.World;
import com.artemis.utils.Bag;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.byrjamin.wickedwizard.assets.Assets;
import com.byrjamin.wickedwizard.assets.MenuStrings;
import com.byrjamin.wickedwizard.assets.TextureStrings;
import com.byrjamin.wickedwizard.ecs.components.ai.Action;
import com.byrjamin.wickedwizard.ecs.components.ai.ActionAfterTimeComponent;
import com.byrjamin.wickedwizard.ecs.systems.ai.OnDeathSystem;
import com.byrjamin.wickedwizard.ecs.systems.audio.MusicSystem;
import com.byrjamin.wickedwizard.ecs.systems.level.EndGameSystem;
import com.byrjamin.wickedwizard.ecs.systems.level.MapTeleportationSystem;
import com.byrjamin.wickedwizard.ecs.systems.level.ScreenWipeSystem;
import com.byrjamin.wickedwizard.factories.AbstractFactory;
import com.byrjamin.wickedwizard.factories.arenas.Arena;
import com.byrjamin.wickedwizard.factories.arenas.ArenaBuilder;
import com.byrjamin.wickedwizard.factories.arenas.decor.ArenaShellFactory;
import com.byrjamin.wickedwizard.factories.arenas.skins.AllBlackSkin;
import com.byrjamin.wickedwizard.factories.arenas.skins.ArenaSkin;
import com.byrjamin.wickedwizard.screens.MenuButton;
import com.byrjamin.wickedwizard.utils.ComponentBag;
import com.byrjamin.wickedwizard.utils.MapCoords;
import com.byrjamin.wickedwizard.utils.Measure;

/**
 * Created by Home on 30/07/2017.
 */

public class BreakRoom extends AbstractFactory {

    private ArenaSkin arenaSkin;

    public BreakRoom(AssetManager assetManager) {
        super(assetManager);
        this.arenaSkin = new AllBlackSkin();
    }



    public Arena createBreakRoom(){

        Arena arena = new ArenaBuilder(assetManager, arenaSkin)
                .addSection(new ArenaBuilder.Section(new MapCoords(),
                        ArenaBuilder.wall.FULL,
                        ArenaBuilder.wall.FULL,
                        ArenaBuilder.wall.FULL,
                        ArenaBuilder.wall.FULL))
                .buildArena();


        float buttonWidth = Measure.units(27.5f);
        float buttonHeight = Measure.units(20f);



        ComponentBag componentBag = new ComponentBag();
        componentBag.add(new ActionAfterTimeComponent(new Action() {
            @Override
            public void performAction(World world, Entity e) {
                world.getSystem(MusicSystem.class).fadeOutMusic();
                world.getSystem(OnDeathSystem.class).kill(e);
            }
        }));

        arena.addEntity(componentBag);


        MenuButton.MenuButtonBuilder menuButtonBuilder = new MenuButton.MenuButtonBuilder(Assets.medium, atlas.findRegion(TextureStrings.BLOCK))
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

        Bag<ComponentBag> bags = menuButtonBuilder.build().createButton(MenuStrings.BREAK, Measure.units(10f), Measure.units(30f));


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
                }).build().createButton(MenuStrings.CONTINUE, Measure.units(62.5f), Measure.units(30f));

        for(ComponentBag bag : bags){
            arena.addEntity(bag);
        }


        return arena;

    }




}
