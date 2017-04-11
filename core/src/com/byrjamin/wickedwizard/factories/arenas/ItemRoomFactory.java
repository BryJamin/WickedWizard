package com.byrjamin.wickedwizard.factories.arenas;

import com.badlogic.gdx.utils.Array;
import com.byrjamin.wickedwizard.archive.maps.MapCoords;
import com.byrjamin.wickedwizard.ecs.components.object.DoorComponent;
import com.byrjamin.wickedwizard.factories.BackgroundFactory;
import com.byrjamin.wickedwizard.factories.EntityFactory;
import com.byrjamin.wickedwizard.factories.items.ItemFactory;
import com.byrjamin.wickedwizard.factories.items.PlusOne;
import com.byrjamin.wickedwizard.screens.PlayScreen;
import com.byrjamin.wickedwizard.utils.Measure;

/**
 * Created by Home on 11/04/2017.
 */

public class ItemRoomFactory extends RoomFactory {


    public static Arena createItemRoom(){
        return createItemRoom(new MapCoords(0,0));
    }

    public static Arena createItemRoom(MapCoords defaultCoords) {

        Array<MapCoords> containingCorrds = new Array<MapCoords>();
        containingCorrds.add(defaultCoords);

        Arena arena = new Arena(containingCorrds, Arena.RoomType.ITEM);


        arena.setWidth(SECTION_WIDTH);
        arena.setHeight(SECTION_HEIGHT);

        arena.addEntity(BackgroundFactory.backgroundBags(0,0,
                SECTION_WIDTH,
                SECTION_HEIGHT,
                Measure.units(15),
                PlayScreen.atlas.findRegions("backgrounds/wall")));

        //LEFT WALL
        arena.addEntity(EntityFactory.wallBag(0, WALLWIDTH * 6, WALLWIDTH, HEIGHT));
        arena.addDoor(EntityFactory.doorBag(0, Measure.units(10),
                new MapCoords(defaultCoords.getX(), defaultCoords.getY()),
                new MapCoords(defaultCoords.getX() - 1, defaultCoords.getY()),
                DoorComponent.DIRECTION.left));

        //RIGHT WALL
        arena.addEntity(EntityFactory.wallBag(WIDTH - WALLWIDTH, WALLWIDTH * 6, WALLWIDTH, HEIGHT));
        arena.addDoor(EntityFactory.doorBag(WIDTH - WALLWIDTH, Measure.units(10),
                new MapCoords(defaultCoords.getX(), defaultCoords.getY()),
                new MapCoords(defaultCoords.getX() + 1, defaultCoords.getY()),
                DoorComponent.DIRECTION.right));

        //CEILING
        arena.addEntity(EntityFactory.wallBag(0,  HEIGHT - WALLWIDTH, WIDTH, WALLWIDTH));

        //GROUND
        arena.addEntity(EntityFactory.wallBag(0,  -WALLWIDTH, WIDTH, WALLWIDTH * 3));


        arena.addEntity(ItemFactory.createFloatingItemBag(arena.getWidth() / 2,
                arena.getHeight() / 2, new PlusOne()));

        return arena;
    }






}
