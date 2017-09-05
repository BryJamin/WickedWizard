package com.bryjamin.wickedwizard.factories.arenas.decor;

import com.badlogic.gdx.assets.AssetManager;
import com.bryjamin.wickedwizard.MainGame;
import com.bryjamin.wickedwizard.ecs.components.object.DoorComponent;
import com.bryjamin.wickedwizard.factories.AbstractFactory;
import com.bryjamin.wickedwizard.factories.arenas.Arena;
import com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder;
import com.bryjamin.wickedwizard.factories.arenas.skins.ArenaSkin;
import com.bryjamin.wickedwizard.utils.ComponentBag;
import com.bryjamin.wickedwizard.utils.MapCoords;
import com.bryjamin.wickedwizard.utils.Measure;
import com.bryjamin.wickedwizard.utils.enums.Direction;

/**
 * Created by Home on 18/03/2017.
 */

public class  ArenaShellFactory extends AbstractFactory {

    protected DecorFactory decorFactory;
    protected ArenaSkin arenaSkin;

    public ArenaShellFactory(AssetManager assetManager, ArenaSkin arenaSkin) {
        super(assetManager);
        this.decorFactory = new DecorFactory(assetManager, arenaSkin);
        this.arenaSkin = arenaSkin;
    }

    //TODO move this constant somewhere else
    public static final float SECTION_WIDTH = MainGame.GAME_WIDTH;
    public static final float SECTION_HEIGHT = MainGame.GAME_HEIGHT;

    public final float WALLWIDTH = Measure.units(5);

    public Arena createOmniArenaSquareCenter(MapCoords defaultCoords, Arena.ArenaType arenaType) {

        Arena arena = new ArenaBuilder(assetManager, arenaSkin, arenaType)
                .addSection(new ArenaBuilder.Section(defaultCoords,
                        ArenaBuilder.wall.DOOR,
                        ArenaBuilder.wall.DOOR,
                        ArenaBuilder.wall.DOOR,
                        ArenaBuilder.wall.DOOR))
                .buildArena();

        arena.addEntity(decorFactory.wallBag(Measure.units(40f), Measure.units(25f), Measure.units(20f), Measure.units(5f), arenaSkin));

        return arena;
    }

    public Arena createSmallArenaNoGrapple(MapCoords defaultCoords, Arena.ArenaType arenaType) {

        Arena arena = new ArenaBuilder(assetManager, arenaSkin, arenaType)
                .addSection(new ArenaBuilder.Section(defaultCoords,
                        ArenaBuilder.wall.DOOR,
                        ArenaBuilder.wall.DOOR,
                        ArenaBuilder.wall.DOOR,
                        ArenaBuilder.wall.DOOR))
                .buildArena();

        return arena;
    }


    public Arena createSmallArenaNoGrapple(MapCoords defaultCoords, int doorToNotSpawn, Arena.ArenaType arenaType) {

        Arena arena = new ArenaBuilder(assetManager, arenaSkin, arenaType)
                .addSection(new ArenaBuilder.Section(defaultCoords,
                        doorToNotSpawn != 0 ? ArenaBuilder.wall.DOOR : ArenaBuilder.wall.FULL,
                        doorToNotSpawn != 1 ? ArenaBuilder.wall.DOOR : ArenaBuilder.wall.FULL,
                        doorToNotSpawn != 2 ? ArenaBuilder.wall.DOOR : ArenaBuilder.wall.FULL,
                        doorToNotSpawn != 3 ? ArenaBuilder.wall.DOOR : ArenaBuilder.wall.FULL)).buildArena();

        return arena;
    }

    public Arena createOmniArenaHiddenGrapple(MapCoords defaultCoords, Arena.ArenaType arenaType) {

        Arena arena = new ArenaBuilder(assetManager, arenaSkin, arenaType)
                .addSection(new ArenaBuilder.Section(defaultCoords,
                        ArenaBuilder.wall.DOOR,
                        ArenaBuilder.wall.DOOR,
                        ArenaBuilder.wall.DOOR,
                        ArenaBuilder.wall.DOOR))
                .buildArena();

        //TODO in order to clean up this grapple if a top route doesn't exist a mark it with a door.

        ComponentBag bag = decorFactory.hiddenGrapplePointBag(arena.getWidth() / 2, (arena.getHeight() / 4) * 3);
        DoorComponent dc = new DoorComponent(new MapCoords(defaultCoords.getX(), defaultCoords.getY()),
                new MapCoords(defaultCoords.getX(), defaultCoords.getY() + 1), Direction.UP);
        dc.ignore = true;
        bag.add(dc);

        arena.addDoor(bag);

        return arena;
    }

    public Arena createOmniArenaHiddenGrapple(MapCoords defaultCoords, int doorToNotSpawn, Arena.ArenaType arenaType) {

        Arena arena = new Arena(arenaSkin, defaultCoords);

        arena = new ArenaBuilder(assetManager, arenaSkin, arenaType)
                .addSection(new ArenaBuilder.Section(defaultCoords,
                        doorToNotSpawn != 0 ? ArenaBuilder.wall.DOOR : ArenaBuilder.wall.FULL,
                        doorToNotSpawn != 1 ? ArenaBuilder.wall.DOOR : ArenaBuilder.wall.FULL,
                        doorToNotSpawn != 2 ? ArenaBuilder.wall.GRAPPLE : ArenaBuilder.wall.FULL,
                        doorToNotSpawn != 3 ? ArenaBuilder.wall.DOOR : ArenaBuilder.wall.FULL))
                .buildArena();

        //TODO in order to clean up this grapple if a top route doesn't exist a mark it with a door.

        return arena;
    }


    public Arena createSmallArena(MapCoords defaultCoords, Arena.ArenaType arenaType, boolean leftDoor, boolean rightDoor, boolean ceilingDoor, boolean topDoor) {

        Arena arena = new Arena(arenaSkin, defaultCoords);

        arena = new ArenaBuilder(assetManager, arenaSkin, arenaType)
                .addSection(new ArenaBuilder.Section(defaultCoords,
                        leftDoor ? ArenaBuilder.wall.DOOR : ArenaBuilder.wall.FULL,
                        rightDoor ? ArenaBuilder.wall.DOOR : ArenaBuilder.wall.FULL,
                        ceilingDoor ? ArenaBuilder.wall.GRAPPLE : ArenaBuilder.wall.FULL,
                        topDoor ? ArenaBuilder.wall.DOOR : ArenaBuilder.wall.FULL))
                .buildArena();

        return arena;
    }


    public Arena createWidth2DeadEndArena(MapCoords defaultCoords, boolean deadEndOnLeft, Arena.ArenaType arenaType) {

        ArenaBuilder.Section left;
        ArenaBuilder.Section right;

        if(deadEndOnLeft) {
            left = new ArenaBuilder.Section(defaultCoords,
                    ArenaBuilder.wall.FULL,
                    ArenaBuilder.wall.NONE,
                    ArenaBuilder.wall.FULL,
                    ArenaBuilder.wall.FULL);

            right = new ArenaBuilder.Section(new MapCoords(defaultCoords.getX() + 1, defaultCoords.getY()),
                    ArenaBuilder.wall.NONE,
                    ArenaBuilder.wall.DOOR,
                    ArenaBuilder.wall.FULL,
                    ArenaBuilder.wall.DOOR);
        } else {
            left = new ArenaBuilder.Section(defaultCoords,
                    ArenaBuilder.wall.DOOR,
                    ArenaBuilder.wall.NONE,
                    ArenaBuilder.wall.FULL,
                    ArenaBuilder.wall.DOOR);

            right = new ArenaBuilder.Section(new MapCoords(defaultCoords.getX() + 1, defaultCoords.getY()),
                    ArenaBuilder.wall.NONE,
                    ArenaBuilder.wall.FULL,
                    ArenaBuilder.wall.FULL,
                    ArenaBuilder.wall.FULL);
        }

        Arena arena =  new ArenaBuilder(assetManager, arenaSkin, arenaType)
                .addSection(left)
                .addSection(right).buildArena();

        return arena;
    }


    public Arena createEitherNoLeftOrNoRightArena(MapCoords defaultCoords, Arena.ArenaType arenaType, boolean deadEndOnLeft) {

        ArenaBuilder.wall left;
        ArenaBuilder.wall right;


        if(deadEndOnLeft) {
            left = ArenaBuilder.wall.FULL;
            right = ArenaBuilder.wall.DOOR;
        } else {
            left = ArenaBuilder.wall.DOOR;
            right = ArenaBuilder.wall.FULL;
        }

        Arena arena =  new ArenaBuilder(assetManager, arenaSkin, arenaType)
                .addSection(new ArenaBuilder.Section(defaultCoords,
                        left,
                        right,
                        ArenaBuilder.wall.GRAPPLE,
                        ArenaBuilder.wall.DOOR)).buildArena();

        return arena;
    }




    public Arena createLetterIArena(MapCoords defaultCoords, Arena.ArenaType arenaType) {


        Arena arena =  new ArenaBuilder(assetManager, arenaSkin, arenaType)
                .addSection(new ArenaBuilder.Section(defaultCoords,
                        ArenaBuilder.wall.DOOR,
                        ArenaBuilder.wall.NONE,
                        ArenaBuilder.wall.FULL,
                        ArenaBuilder.wall.FULL))
                .addSection(new ArenaBuilder.Section(new MapCoords(defaultCoords.getX() + 1, defaultCoords.getY()),
                        ArenaBuilder.wall.NONE,
                        ArenaBuilder.wall.NONE,
                        ArenaBuilder.wall.NONE,
                        ArenaBuilder.wall.FULL ))
                .addSection(new ArenaBuilder.Section(new MapCoords(defaultCoords.getX() + 2, defaultCoords.getY()),
                        ArenaBuilder.wall.NONE,
                        ArenaBuilder.wall.DOOR,
                        ArenaBuilder.wall.FULL,
                        ArenaBuilder.wall.FULL))
                .addSection(new ArenaBuilder.Section(new MapCoords(defaultCoords.getX() + 1, defaultCoords.getY() + 1),
                        ArenaBuilder.wall.NONE,
                        ArenaBuilder.wall.NONE,
                        ArenaBuilder.wall.NONE,
                        ArenaBuilder.wall.NONE))
                .addSection(new ArenaBuilder.Section(new MapCoords(defaultCoords.getX(), defaultCoords.getY() + 1),
                        ArenaBuilder.wall.FULL,
                        ArenaBuilder.wall.FULL,
                        ArenaBuilder.wall.NONE,
                        ArenaBuilder.wall.NONE))
                .addSection(new ArenaBuilder.Section(new MapCoords(defaultCoords.getX() + 2, defaultCoords.getY() + 1),
                        ArenaBuilder.wall.FULL,
                        ArenaBuilder.wall.FULL,
                        ArenaBuilder.wall.NONE,
                        ArenaBuilder.wall.NONE))
                .addSection(new ArenaBuilder.Section(new MapCoords(defaultCoords.getX(), defaultCoords.getY() + 2),
                        ArenaBuilder.wall.DOOR,
                        ArenaBuilder.wall.NONE,
                        ArenaBuilder.wall.FULL,
                        ArenaBuilder.wall.FULL))
                .addSection(new ArenaBuilder.Section(new MapCoords(defaultCoords.getX() + 1, defaultCoords.getY() + 2),
                        ArenaBuilder.wall.NONE,
                        ArenaBuilder.wall.NONE,
                        ArenaBuilder.wall.FULL,
                        ArenaBuilder.wall.NONE))
                .addSection(new ArenaBuilder.Section(new MapCoords(defaultCoords.getX() + 2, defaultCoords.getY() + 2),
                        ArenaBuilder.wall.NONE,
                        ArenaBuilder.wall.DOOR,
                        ArenaBuilder.wall.FULL,
                        ArenaBuilder.wall.FULL)).buildArena();

        arena.addEntity(decorFactory.wallBag(0, SECTION_HEIGHT, SECTION_WIDTH, SECTION_HEIGHT, arenaSkin));
        arena.addEntity(decorFactory.wallBag(SECTION_WIDTH * 2, SECTION_HEIGHT, SECTION_WIDTH, SECTION_HEIGHT, arenaSkin));

        return arena;
    }

}
