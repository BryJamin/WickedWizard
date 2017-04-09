package com.byrjamin.wickedwizard.factories.arenas;

import com.artemis.Component;
import com.artemis.utils.Bag;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.byrjamin.wickedwizard.ecs.components.movement.AccelerantComponent;
import com.byrjamin.wickedwizard.ecs.components.CollisionBoundComponent;
import com.byrjamin.wickedwizard.ecs.components.object.DoorComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.PositionComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.FadeComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.ShapeComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.TextureFontComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.TextureRegionComponent;
import com.byrjamin.wickedwizard.factories.BackgroundFactory;
import com.byrjamin.wickedwizard.factories.EntityFactory;
import com.byrjamin.wickedwizard.factories.enemy.BlobFactory;
import com.byrjamin.wickedwizard.utils.BagSearch;
import com.byrjamin.wickedwizard.utils.Measure;
import com.byrjamin.wickedwizard.archive.maps.MapCoords;
import com.byrjamin.wickedwizard.screens.PlayScreen;
import com.byrjamin.wickedwizard.utils.ComponentBag;

/**
 * Created by Home on 01/04/2017.
 */

public class TutorialFactory extends RoomFactory{

    private final static String moveTutorialString1 = "Exit through the right door";
    private final static String moveTutorialString2 = "Touch in this area to move!";


    private final static String jumpTutorialString =
            "Tap above your character to jump and start gliding! \n \n Tap below your character to cancel your glide";
    private final static String enemyTutorialString =
            "Hark! A Worthy Foe! \n \n Hold down within this area to shoot \n \n Drag to change aim";
    private final static String grappleTutorialString =
            "Doubt you can dash out of this one \n \n Tap anything highlighted to grapple to it";
    private final static String endString =
            "Yea you seem about ready \n \n Now go and adventure!";

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
        bag.add(new PositionComponent(0, 800));
        TextureFontComponent tfc = new TextureFontComponent(moveTutorialString1);
        bag.add(tfc);
        arena.addEntity(bag);

        bag = new Bag<Component>();
        bag.add(new PositionComponent(0, 150));
        tfc = new TextureFontComponent(moveTutorialString2);
        tfc.layer = 6;
        tfc.setColor(0,0,0,1);
        bag.add(tfc);
        arena.addEntity(bag);

        bag = new Bag<Component>();
        bag.add(new PositionComponent(0, 0));
        ShapeComponent sc = new ShapeComponent(WIDTH, WALLWIDTH * 2, TextureRegionComponent.FOREGROUND_LAYER_NEAR);
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
        bag.add(new PositionComponent(0, 800));
        bag.add(new TextureFontComponent(jumpTutorialString));
        arena.addEntity(bag);


        return arena;

    }




    public static Arena enemyTurtorial(MapCoords defaultCoords){

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
/*        arena.addDoor(EntityFactory.doorBag(0, Measure.units(10),
                new MapCoords(defaultCoords.getX(), defaultCoords.getY()),
                new MapCoords(defaultCoords.getX() - 1, defaultCoords.getY()),
                DoorComponent.DIRECTION.left));*/

        //RIGHT WALL
        arena.addEntity(EntityFactory.wallBag(WIDTH - WALLWIDTH, WALLWIDTH * 6, WALLWIDTH, HEIGHT));
        arena.addDoor(EntityFactory.doorBag(WIDTH - WALLWIDTH, Measure.units(10),
                new MapCoords(defaultCoords.getX(), defaultCoords.getY()),
                new MapCoords(defaultCoords.getX() + 1, defaultCoords.getY()),
                DoorComponent.DIRECTION.right));

        //CEILING
        arena.addEntity(EntityFactory.wallBag(0,  HEIGHT - WALLWIDTH, WIDTH, WALLWIDTH));
        arena.addDoor(EntityFactory.grateBag(WIDTH / 2, 400,
                new MapCoords(defaultCoords.getX(), defaultCoords.getY()),
                new MapCoords(defaultCoords.getX(), defaultCoords.getY() -1),
                DoorComponent.DIRECTION.down));


        //GROUND
        arena.addEntity(EntityFactory.wallBag(0,  -WALLWIDTH, WIDTH, WALLWIDTH * 3));

        Bag<Component> bag = new Bag<Component>();
        bag.add(new PositionComponent(0, 1000));
        bag.add(new TextureFontComponent(enemyTutorialString));
        arena.addEntity(bag);

        arena.addEntity(createTutorialHighlight(0, WALLWIDTH * 2, WALLWIDTH / 2, HEIGHT));
        arena.addEntity(createTutorialHighlight(0, WALLWIDTH * 2, WIDTH, WALLWIDTH / 2));
        arena.addEntity(createTutorialHighlight(WIDTH - WALLWIDTH / 2, WALLWIDTH * 2, WALLWIDTH / 2, HEIGHT));
        arena.addEntity(createTutorialHighlight(0, HEIGHT - WALLWIDTH / 2, WIDTH, WALLWIDTH / 2));

        bag = BlobFactory.blobBag(arena.getWidth() - Measure.units(9), WALLWIDTH * 2);
        BagSearch.getObjectOfTypeClass(AccelerantComponent.class, bag).maxX = Measure.units(1);
        arena.addEntity(bag);


        return arena;

    }



    public static Arena grappleTutorial(MapCoords defaultCoords){

        Array<MapCoords> containingCorrds = new Array<MapCoords>();
        containingCorrds.add(defaultCoords);
        containingCorrds.add(new MapCoords(defaultCoords.getX(), defaultCoords.getY() +  1));
        containingCorrds.add(new MapCoords(defaultCoords.getX(), defaultCoords.getY() +  2));

        Arena arena = new Arena(containingCorrds);
        Bag<Component> bag = new Bag<Component>();


        arena.setWidth(SECTION_WIDTH);
        arena.setHeight(SECTION_HEIGHT * 3);

        float HEIGHT = SECTION_HEIGHT * 3;
        float WIDTH = SECTION_WIDTH;

        arena.addEntity(BackgroundFactory.backgroundBags(0,0,
                WIDTH,
                HEIGHT,
                Measure.units(15),
                PlayScreen.atlas.findRegions("backgrounds/wall")));

        //LEFT WALL
        arena.addEntity(EntityFactory.wallBag(0, 0, WALLWIDTH, SECTION_HEIGHT * 2 + WALLWIDTH * 2));
        arena.addEntity(EntityFactory.wallBag(0, SECTION_HEIGHT * 2 + WALLWIDTH * 6, WALLWIDTH, SECTION_HEIGHT * 2 + WALLWIDTH * 2));

        bag = EntityFactory.doorBag(0, Measure.units(10) + SECTION_HEIGHT * 2,
                new MapCoords(defaultCoords.getX(), defaultCoords.getY() + 2),
                new MapCoords(defaultCoords.getX() - 1, defaultCoords.getY() + 2),
                DoorComponent.DIRECTION.left);
        arena.addDoor(bag);
        arena.addEntity(createTutorialHighlight(BagSearch.getObjectOfTypeClass(CollisionBoundComponent.class, bag).bound));

        //RIGHT WALL
        arena.addEntity(EntityFactory.wallBag(WIDTH - WALLWIDTH, 0, WALLWIDTH, HEIGHT));
        //arena.addEntity(EntityFactory.wallBag(WIDTH - WALLWIDTH, 0, WALLWIDTH, SECTION_HEIGHT * 2 + WALLWIDTH * 2));
/*        arena.addEntity(EntityFactory.wallBag(WIDTH - WALLWIDTH, SECTION_HEIGHT * 2 + WALLWIDTH * 6, WALLWIDTH, SECTION_HEIGHT * 2 + WALLWIDTH * 2));
        arena.addDoor(EntityFactory.doorBag(WIDTH - WALLWIDTH, Measure.units(10) + SECTION_HEIGHT * 2,
                new MapCoords(defaultCoords.getX(), defaultCoords.getY() + 2),
                new MapCoords(defaultCoords.getX() + 1, defaultCoords.getY() + 2),
                DoorComponent.DIRECTION.right));*/

        //CEILING
        arena.addEntity(EntityFactory.wallBag(0,  HEIGHT - WALLWIDTH, WIDTH, WALLWIDTH));
        bag = EntityFactory.grateBag(WIDTH / 2, HEIGHT - Measure.units(15),
                new MapCoords(defaultCoords.getX(), defaultCoords.getY() + 2),
                new MapCoords(defaultCoords.getX(), defaultCoords.getY() + 3),
                DoorComponent.DIRECTION.up);
        arena.addDoor(bag);
        arena.addEntity(createTutorialHighlight(BagSearch.getObjectOfTypeClass(CollisionBoundComponent.class, bag).bound));

        //GROUND
        arena.addEntity(EntityFactory.wallBag(0,  -WALLWIDTH, WIDTH, WALLWIDTH * 3));

        bag = new Bag<Component>();
        bag.add(new PositionComponent(0, 800));
        bag.add(new TextureFontComponent(grappleTutorialString));
        arena.addEntity(bag);


        for(int i = 0; i < 4; i ++) {
            bag = EntityFactory.grapplePointBag(arena.getWidth() / 2, Measure.units(50 + (i * 30)));
            arena.addEntity(bag);
            arena.addEntity(createTutorialHighlight(BagSearch.getObjectOfTypeClass(CollisionBoundComponent.class, bag).bound));
        }

/*        arena.addEntity(EntityFactory.grapplePointBag(arena.getWidth() / 2, Measure.units(80)));
        arena.addEntity(EntityFactory.grapplePointBag(arena.getWidth() / 2, Measure.units(110)));
        arena.addEntity(EntityFactory.grapplePointBag(arena.getWidth() / 2, Measure.units(140)));*/

/*        bag = new Bag<Component>();
        bag.add(new PositionComponent(0, 0));
        ShapeComponent sc = new ShapeComponent(WIDTH, WALLWIDTH * 2);
        sc.layer = 5;
        bag.add(sc);
        bag.add(new FadeComponent());
        arena.addEntity(bag);*/

        return arena;

    }


    public static Arena endTutorial(MapCoords defaultCoords){

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
        arena.addEntity(EntityFactory.wallBag(0,  -WALLWIDTH, WIDTH, WALLWIDTH * 3));

        Bag<Component> bag = new Bag<Component>();
        bag.add(new PositionComponent(0, 800));
        bag.add(new TextureFontComponent(endString));
        arena.addEntity(bag);

        return arena;

    }

    public static ComponentBag createTutorialHighlight(Rectangle r) {
        return createTutorialHighlight(r.x, r.y, r.width, r.height);
    }

    public static ComponentBag createTutorialHighlight(float x, float y, float width, float height) {
        ComponentBag bag = new ComponentBag();
        bag.add(new PositionComponent(x, y));
        ShapeComponent sc = new ShapeComponent(width, height, TextureRegionComponent.FOREGROUND_LAYER_NEAR);
        bag.add(sc);
        bag.add(new FadeComponent());
        return bag;
    }





}
