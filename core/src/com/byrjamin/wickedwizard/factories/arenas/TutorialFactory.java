package com.byrjamin.wickedwizard.factories.arenas;

import com.artemis.Component;
import com.artemis.utils.Bag;
import com.badlogic.gdx.graphics.Color;
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
import com.byrjamin.wickedwizard.factories.EntityFactory;
import com.byrjamin.wickedwizard.factories.enemy.BlobFactory;
import com.byrjamin.wickedwizard.utils.BagSearch;
import com.byrjamin.wickedwizard.utils.Measure;
import com.byrjamin.wickedwizard.utils.MapCoords;
import com.byrjamin.wickedwizard.utils.ComponentBag;

import static com.byrjamin.wickedwizard.ecs.components.texture.TextureRegionComponent.FOREGROUND_LAYER_NEAR;

/**
 * Created by Home on 01/04/2017.
 */

public class TutorialFactory extends ArenaShellFactory {

    private final static String moveTutorialString1 = "Exit through the right door";
    private final static String moveTutorialString2 = "Touch in this area to move!";


    private final static String jumpTutorialString =
            "Tap above your character to jump and start gliding! \n \n DOUBLE Tap below your character to cancel gliding";
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

        //arena.addEntity(SilverHeadFactory.silverHead(1000, 600));
        //arena.addEntity(KugelDuscheFactory.kugelDusche(arena.getWidth() / 2,arena.getHeight() / 2));

        arena.setWidth(SECTION_WIDTH);
        arena.setHeight(SECTION_HEIGHT);

        new ArenaBuilder.Builder(defaultCoords, arena)
                .section(new ArenaBuilder.Section(defaultCoords,
                        ArenaBuilder.wall.FULL,
                        ArenaBuilder.wall.DOOR,
                        ArenaBuilder.wall.FULL,
                        ArenaBuilder.wall.FULL)).build();

        Bag<Component> bag = new Bag<Component>();
        bag.add(new PositionComponent(0, 800));
        TextureFontComponent tfc = new TextureFontComponent(moveTutorialString1);
        bag.add(tfc);
        arena.addEntity(bag);

        bag = new Bag<Component>();
        bag.add(new PositionComponent(0, 150));
        tfc = new TextureFontComponent(moveTutorialString2);
        tfc.layer = FOREGROUND_LAYER_NEAR;
        tfc.setColor(1,1,1,1);
        bag.add(tfc);
        arena.addEntity(bag);

/*        bag = new Bag<Component>();
        createTutorialHighlight(0,0, WIDTH, WALLWIDTH * 2, Color.BLACK);


        bag.add(new PositionComponent(0, 0));
        ShapeComponent sc = new ShapeComponent(WIDTH, WALLWIDTH * 2, FOREGROUND_LAYER_NEAR);
        bag.add(sc);
        bag.add(new FadeComponent());*/
        arena.addEntity(createTutorialHighlight(0,0, WIDTH, WALLWIDTH * 2, Color.BLACK));

        return arena;

    }


    public static Arena jumpTutorial(MapCoords defaultCoords){

        Array<MapCoords> containingCorrds = new Array<MapCoords>();
        containingCorrds.add(defaultCoords);

        Arena arena = new Arena(containingCorrds);

        arena.setWidth(SECTION_WIDTH);
        arena.setHeight(SECTION_HEIGHT);

        new ArenaBuilder.Builder(defaultCoords, arena)
                .section(new ArenaBuilder.Section(defaultCoords,
                        ArenaBuilder.wall.DOOR,
                        ArenaBuilder.wall.DOOR,
                        ArenaBuilder.wall.FULL,
                        ArenaBuilder.wall.NONE)).build();

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

        Arena arena = new Arena(containingCorrds, Arena.RoomType.TRAP);


        arena.setWidth(SECTION_WIDTH);
        arena.setHeight(SECTION_HEIGHT);

       new ArenaBuilder.Builder(defaultCoords, arena)
                .section(new ArenaBuilder.Section(defaultCoords,
                        ArenaBuilder.wall.FULL,
                        ArenaBuilder.wall.DOOR,
                        ArenaBuilder.wall.FULL,
                        ArenaBuilder.wall.DOOR)).build();

        Bag<Component> bag = new Bag<Component>();
        bag.add(new PositionComponent(0, 1000));
        bag.add(new TextureFontComponent(enemyTutorialString));
        arena.addEntity(bag);

        arena.addEntity(createTutorialHighlight(0, WALLWIDTH * 2, WALLWIDTH / 2, HEIGHT));
        arena.addEntity(createTutorialHighlight(0, WALLWIDTH * 2, WIDTH, WALLWIDTH / 2));
        arena.addEntity(createTutorialHighlight(WIDTH - WALLWIDTH / 2, WALLWIDTH * 2, WALLWIDTH / 2, HEIGHT));
        arena.addEntity(createTutorialHighlight(0, HEIGHT - WALLWIDTH / 2, WIDTH, WALLWIDTH / 2));

        bag = BlobFactory.blobBag(arena.getWidth() - Measure.units(12), WALLWIDTH * 4);
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

        arena.setWidth(SECTION_WIDTH);
        arena.setHeight(SECTION_HEIGHT * 3);

        float HEIGHT = SECTION_HEIGHT * 3;
        float WIDTH = SECTION_WIDTH;

        new ArenaBuilder.Builder(defaultCoords, arena)
                .section(new ArenaBuilder.Section(defaultCoords,
                        ArenaBuilder.wall.DOOR,
                        ArenaBuilder.wall.FULL,
                        ArenaBuilder.wall.NONE,
                        ArenaBuilder.wall.FULL))
                .section(new ArenaBuilder.Section(new MapCoords(defaultCoords.getX(), defaultCoords.getY() +  1),
                        ArenaBuilder.wall.FULL,
                        ArenaBuilder.wall.FULL,
                        ArenaBuilder.wall.NONE,
                        ArenaBuilder.wall.NONE))
                        .section(new ArenaBuilder.Section(new MapCoords(defaultCoords.getX(), defaultCoords.getY() +  2),
                        ArenaBuilder.wall.FULL,
                        ArenaBuilder.wall.FULL,
                                ArenaBuilder.wall.DOOR,
                                ArenaBuilder.wall.NONE)).build();

        //TODO find less lazy way to add the highlight to the arena
        Bag<Component> bag;
        bag = EntityFactory.grateBag(WIDTH / 2, HEIGHT - Measure.units(15),
                new MapCoords(defaultCoords.getX(), defaultCoords.getY() + 2),
                new MapCoords(defaultCoords.getX(), defaultCoords.getY() + 3),
                DoorComponent.DIRECTION.up);
        //arena.addDoor(bag);
        arena.addEntity(createTutorialHighlight(BagSearch.getObjectOfTypeClass(CollisionBoundComponent.class, bag).bound));
/*
        //GROUND
        arena.addEntity(EntityFactory.wallBag(0,  -WALLWIDTH, WIDTH, WALLWIDTH * 3));*/

        bag = new Bag<Component>();
        bag.add(new PositionComponent(0, 800));
        bag.add(new TextureFontComponent(grappleTutorialString));
        arena.addEntity(bag);


        for(int i = 0; i < 4; i ++) {
            bag = EntityFactory.grapplePointBag(arena.getWidth() / 2, Measure.units(50 + (i * 30)));
            arena.addEntity(bag);
            arena.addEntity(createTutorialHighlight(BagSearch.getObjectOfTypeClass(CollisionBoundComponent.class, bag).bound));
        }

        return arena;

    }


    public static Arena endTutorial(MapCoords defaultCoords){

        Array<MapCoords> containingCorrds = new Array<MapCoords>();
        containingCorrds.add(defaultCoords);

        Arena arena = new Arena(containingCorrds);

        arena.setWidth(SECTION_WIDTH);
        arena.setHeight(SECTION_HEIGHT);

        new ArenaBuilder.Builder(defaultCoords, arena)
                .section(new ArenaBuilder.Section(defaultCoords,
                        ArenaBuilder.wall.DOOR,
                        ArenaBuilder.wall.DOOR,
                        ArenaBuilder.wall.FULL,
                        ArenaBuilder.wall.FULL)).build();

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
        ShapeComponent sc = new ShapeComponent(width, height, TextureRegionComponent.FOREGROUND_LAYER_FAR);
        bag.add(sc);
        bag.add(new FadeComponent());
        return bag;
    }

    public static ComponentBag createTutorialHighlight(float x, float y, float width, float height, Color c) {
        ComponentBag bag = new ComponentBag();
        bag.add(new PositionComponent(x, y));
        ShapeComponent sc = new ShapeComponent(width, height, TextureRegionComponent.FOREGROUND_LAYER_FAR);
        sc.color = c;
        bag.add(sc);
        bag.add(new FadeComponent());
        return bag;
    }





}
