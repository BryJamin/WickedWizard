package com.byrjamin.wickedwizard.factories.arenas.presets;

import com.artemis.Entity;
import com.artemis.World;
import com.artemis.utils.Bag;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.byrjamin.wickedwizard.assets.Assets;
import com.byrjamin.wickedwizard.assets.TextureStrings;
import com.byrjamin.wickedwizard.ecs.components.ai.Action;
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

    private ArenaShellFactory arenaShellFactory;

    private ArenaSkin arenaSkin;

    public BreakRoom(AssetManager assetManager) {
        super(assetManager);
        this.arenaSkin = new AllBlackSkin(atlas);
        this.arenaShellFactory = new ArenaShellFactory(assetManager, arenaSkin);
    }



    public Arena createBreakRoom(){

        Arena arena = new Arena(new AllBlackSkin(atlas), new MapCoords());
        arena = new ArenaBuilder(assetManager, arenaSkin)
                .addSection(new ArenaBuilder.Section(new MapCoords(),
                        ArenaBuilder.wall.FULL,
                        ArenaBuilder.wall.FULL,
                        ArenaBuilder.wall.FULL,
                        ArenaBuilder.wall.FULL))
                .buildArena(arena);


        MenuButton menuButton = new MenuButton(Assets.medium, atlas.findRegion(TextureStrings.BLOCK));

        Bag<ComponentBag> bags = menuButton.createButtonWithAction("Take A Break", Measure.units(10f), Measure.units(30f), Measure.units(20f), Measure.units(10f),
                new Color(Color.WHITE), new Color(Color.BLACK), new Action() {
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


        for(ComponentBag bag : bags){
            arena.addEntity(bag);
        }

        bags = menuButton.createButtonWithAction("Continue", Measure.units(70f), Measure.units(30f), Measure.units(20f), Measure.units(10f),
                new Color(Color.WHITE), new Color(Color.BLACK), new Action() {
                    @Override
                    public void performAction(World world, Entity e) {
                        world.getSystem(ScreenWipeSystem.class).startScreenWipe(ScreenWipeSystem.Transition.FADE, new Action() {
                            @Override
                            public void performAction(World world, Entity e) {
                                world.getSystem(MapTeleportationSystem.class).createNewLevel();
                            }
                        });
                    }
                });

        for(ComponentBag bag : bags){
            arena.addEntity(bag);
        }


        return arena;

    }




}
