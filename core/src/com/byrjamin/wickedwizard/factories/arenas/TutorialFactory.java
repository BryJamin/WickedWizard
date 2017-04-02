package com.byrjamin.wickedwizard.factories.arenas;

import com.artemis.Component;
import com.artemis.utils.Bag;
import com.badlogic.gdx.utils.Array;
import com.byrjamin.wickedwizard.MainGame;
import com.byrjamin.wickedwizard.ecs.components.DoorComponent;
import com.byrjamin.wickedwizard.ecs.components.PositionComponent;
import com.byrjamin.wickedwizard.ecs.components.RoomTypeComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.FadeComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.ShapeComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.TextureFontComponent;
import com.byrjamin.wickedwizard.factories.BackgroundFactory;
import com.byrjamin.wickedwizard.factories.EntityFactory;
import com.byrjamin.wickedwizard.helper.Measure;
import com.byrjamin.wickedwizard.maps.MapCoords;
import com.byrjamin.wickedwizard.screens.PlayScreen;

/**
 * Created by Home on 01/04/2017.
 */

public class TutorialFactory extends RoomFactory{

    public static Arena groundMovementTutorial(MapCoords defaultCoords){

        Array<MapCoords> containingCorrds = new Array<MapCoords>();
        containingCorrds.add(defaultCoords);

        Arena arena = new Arena(containingCorrds);

        arena.setWidth(SECTION_WIDTH);
        arena.setHeight(SECTION_HEIGHT);

        arena.addEntity(BackgroundFactory.backgroundBags(0,0,
                SECTION_WIDTH,
                SECTION_HEIGHT,
                Measure.units(15),
                PlayScreen.atlas.findRegions("backgrounds/wall")));

        //LEFT WALL
        arena.addEntity(EntityFactory.wallBag(0, 0, WALLWIDTH, HEIGHT));

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

        Bag<Component> bag = new Bag<Component>();
        bag.add(new RoomTypeComponent(RoomTypeComponent.Type.BATTLE));
        arena.addEntity(bag);

        bag = new Bag<Component>();
        bag.add(new PositionComponent(0, 800));
        bag.add(new TextureFontComponent("Touch the flashing area to move! \n \n Exit through the right door"));
        arena.addEntity(bag);

        bag = new Bag<Component>();
        bag.add(new PositionComponent(0, 0));
        ShapeComponent sc = new ShapeComponent(WIDTH, WALLWIDTH * 2);
        sc.layer = 5;
        bag.add(sc);
        bag.add(new FadeComponent());
        arena.addEntity(bag);

        return arena;

    }


    public static Arena jumpTutorial(MapCoords defaultCoords){

        Array<MapCoords> containingCorrds = new Array<MapCoords>();
        containingCorrds.add(defaultCoords);

        Arena arena = new Arena(containingCorrds);

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

        //BEFORE GAP
        arena.addEntity(EntityFactory.wallBag(0,  -WALLWIDTH, WALLWIDTH * 6, WALLWIDTH * 3));
        //AFTER GAP
        arena.addEntity(EntityFactory.wallBag(WIDTH - WALLWIDTH * 6,  -WALLWIDTH, WALLWIDTH * 6, WALLWIDTH * 3));
        //HIDDEN SAFETY NET
        arena.addEntity(EntityFactory.wallBag(0,  -WALLWIDTH * 3, WIDTH, WALLWIDTH * 3));

        Bag<Component> bag = new Bag<Component>();
        bag.add(new RoomTypeComponent(RoomTypeComponent.Type.BATTLE));
        arena.addEntity(bag);

        bag = new Bag<Component>();
        bag.add(new PositionComponent(0, 800));
        bag.add(new TextureFontComponent("Tap within the flashing area to dash! \n \n Use that to cross the 'dangerous' pit"));
        arena.addEntity(bag);


        bag = new Bag<Component>();
        bag.add(new PositionComponent(0, WALLWIDTH * 2));
        bag.add(new ShapeComponent(WALLWIDTH / 2, HEIGHT));
        bag.add(new FadeComponent());
        arena.addEntity(bag);

        bag = new Bag<Component>();
        bag.add(new PositionComponent(0, WALLWIDTH * 2));
        bag.add(new ShapeComponent(WIDTH, WALLWIDTH / 2));
        bag.add(new FadeComponent());
        arena.addEntity(bag);

        bag = new Bag<Component>();
        bag.add(new PositionComponent(WIDTH - WALLWIDTH / 2, WALLWIDTH * 2));
        bag.add(new ShapeComponent(WALLWIDTH / 2, HEIGHT));
        bag.add(new FadeComponent());
        arena.addEntity(bag);

        bag = new Bag<Component>();
        bag.add(new PositionComponent(0, HEIGHT - WALLWIDTH / 2));
        bag.add(new ShapeComponent(WIDTH, WALLWIDTH / 2));
        bag.add(new FadeComponent());
        arena.addEntity(bag);

        return arena;

    }





}
