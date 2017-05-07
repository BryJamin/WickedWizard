package com.byrjamin.wickedwizard.factories.arenas;

import com.artemis.Component;
import com.artemis.utils.Bag;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.byrjamin.wickedwizard.MainGame;
import com.byrjamin.wickedwizard.ecs.components.movement.AccelerantComponent;
import com.byrjamin.wickedwizard.ecs.components.CollisionBoundComponent;
import com.byrjamin.wickedwizard.ecs.components.object.DoorComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.PositionComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.FadeComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.ShapeComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.TextureFontComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.TextureRegionComponent;
import com.byrjamin.wickedwizard.factories.arenas.skins.ArenaSkin;
import com.byrjamin.wickedwizard.factories.enemy.BlobFactory;
import com.byrjamin.wickedwizard.utils.BagSearch;
import com.byrjamin.wickedwizard.utils.Measure;
import com.byrjamin.wickedwizard.utils.MapCoords;
import com.byrjamin.wickedwizard.utils.ComponentBag;
import com.byrjamin.wickedwizard.utils.enums.Direction;

import static com.byrjamin.wickedwizard.ecs.components.texture.TextureRegionComponent.FOREGROUND_LAYER_NEAR;

/**
 * Created by Home on 01/04/2017.
 */

public class TutorialFactory extends ArenaShellFactory {

    private final String moveTutorialString1 = "Exit this room on the right";
    private final String moveTutorialString2 = "TOUCH WITHIN THIS AREA TO MOVE!";


    private static final String jumpTutorialString =
            "Tap ABOVE your character to jump and glide! \n \n DOUBLE Tap BELOW your character to cancel glide";


    private static final String doubleJumpTutorialString =
            "Tap ABOVE your character and tap ABOVE AGAIN \n To DOUBLE Jump";

    private static final String enemyTutorialString =
            "Hark! A Worthy Foe! \n \n Hold down within this area to shoot \n \n Drag to change aim";
    private static final String grappleTutorialString =
            "Doubt you can dash out of this one \n \n Tap anything highlighted to grapple to it";
    private static final String endString =
            "Yea you seem about ready \n \n Tap on the hole in the wall to head to adventure!";

    private static final String platformString =
            "Double Tap Below your character to \n Fall through platforms";

    public TutorialFactory(AssetManager assetManager, ArenaSkin arenaSkin) {
        super(assetManager, arenaSkin);
    }

    public Arena groundMovementTutorial(MapCoords defaultCoords){

        Arena arena = new Arena(arenaSkin, defaultCoords);

        //arena.addEntity(SilverHeadFactory.silverHead(1000, 600));
        //arena.addEntity(KugelDuscheFactory.kugelDusche(arena.getWidth() / 2,arena.getHeight() / 2));

        arena.setWidth(SECTION_WIDTH);
        arena.setHeight(SECTION_HEIGHT);

        arena =  new ArenaBuilder(assetManager, arenaSkin)
                .addSection(new ArenaBuilder.Section(defaultCoords,
                        ArenaBuilder.wall.FULL,
                        ArenaBuilder.wall.DOOR,
                        ArenaBuilder.wall.FULL,
                        ArenaBuilder.wall.FULL)).buildArena(arena);

        Bag<Component> textBag = new Bag<Component>();
        textBag.add(new PositionComponent(MainGame.GAME_WIDTH / 2, 800));
        TextureFontComponent text = new TextureFontComponent(moveTutorialString1);
        textBag.add(text);
        arena.addEntity(textBag);

        Bag<Component> bag = new Bag<Component>();
        bag.add(new PositionComponent(arena.getWidth() / 2, 125));
        TextureFontComponent tfc = new TextureFontComponent(moveTutorialString2);
        tfc.layer = FOREGROUND_LAYER_NEAR;
        tfc.setColor(1,1,1,1);
        bag.add(tfc);
        arena.addEntity(bag);


        arena.addEntity(decorFactory.chevronBag(Measure.units(85f), Measure.units(22.5f), -90));
        arena.addEntity(decorFactory.chevronBag(Measure.units(55f), Measure.units(22.5f), -90));
        arena.addEntity(decorFactory.chevronBag(Measure.units(25f), Measure.units(22.5f), -90));

/*        bag = new Bag<Component>();
        createTutorialHighlight(0,0, WIDTH, WALLWIDTH * 2, Color.BLACK);


        bag.add(new PositionComponent(0, 0));
        ShapeComponent sc = new ShapeComponent(WIDTH, WALLWIDTH * 2, FOREGROUND_LAYER_NEAR);
        bag.add(sc);
        bag.add(new FadeComponent());*/
        arena.addEntity(createTutorialHighlight(0,0, WIDTH, WALLWIDTH * 2, Color.BLACK));

        return arena;

    }


    public Arena platformTutorial(MapCoords defaultCoords){

        Array<MapCoords> containingCorrds = new Array<MapCoords>();
        containingCorrds.add(defaultCoords);
        containingCorrds.add(new MapCoords(defaultCoords.getX(), defaultCoords.getY() + 1));

        Arena arena = new Arena(arenaSkin, containingCorrds.toArray());

        arena.setWidth(SECTION_WIDTH);
        arena.setHeight(SECTION_HEIGHT * 2);

        arena =  new ArenaBuilder(assetManager, arenaSkin)
                .addSection(new ArenaBuilder.Section(defaultCoords,
                        ArenaBuilder.wall.DOOR,
                        ArenaBuilder.wall.DOOR,
                        ArenaBuilder.wall.NONE,
                        ArenaBuilder.wall.FULL))
                .addSection(new ArenaBuilder.Section(new MapCoords(defaultCoords.getX(), defaultCoords.getY() + 1),
                        ArenaBuilder.wall.FULL,
                        ArenaBuilder.wall.FULL,
                        ArenaBuilder.wall.FULL,
                        ArenaBuilder.wall.NONE)).buildArena(arena);

        //Blocker
        arena.addEntity(decorFactory.wallBag(Measure.units(40f), Measure.units(10f), Measure.units(20f), Measure.units(50f), arenaSkin));

        //Left platforms
        arena.addEntity(decorFactory.platform(Measure.units(5), Measure.units(30f), Measure.units(35f)));
        arena.addEntity(decorFactory.platform(Measure.units(5), Measure.units(55f), Measure.units(35f)));



        //Right platforms
        arena.addEntity(decorFactory.platform(Measure.units(60f), Measure.units(30f), Measure.units(35f)));
        arena.addEntity(decorFactory.platform(Measure.units(60f), Measure.units(55f), Measure.units(35f)));

        //Arrows
        arena.addEntity(decorFactory.chevronBag(Measure.units(85f), Measure.units(22.5f), 180));
        arena.addEntity(decorFactory.chevronBag(Measure.units(85f), Measure.units(55f), 180));
        //arena.addEntity(decorFactory.chevronBag(Measure.units(85f), Measure.units(22.5f), 0));
        //arena.addEntity(decorFactory.chevronBag(Measure.units(55f), Measure.units(22.5f), -90));
        arena.addEntity(decorFactory.chevronBag(Measure.units(27f), Measure.units(22.5f), 0));
        arena.addEntity(decorFactory.chevronBag(Measure.units(27f), Measure.units(52.5f), 0));

        TextureFontComponent tfc = new TextureFontComponent(platformString);
        ComponentBag bag = new ComponentBag();
        bag.add(new PositionComponent(MainGame.GAME_WIDTH / 2, 1500));
        bag.add(tfc);

        arena.addEntity(bag);

        return arena;

    }


    public Arena jumpTutorial(MapCoords defaultCoords){

        Array<MapCoords> containingCorrds = new Array<MapCoords>();
        containingCorrds.add(defaultCoords);
        containingCorrds.add(new MapCoords(defaultCoords.getX() + 1, defaultCoords.getY()));
        containingCorrds.add(new MapCoords(defaultCoords.getX() + 2, defaultCoords.getY()));

        Arena arena = new Arena(arenaSkin, containingCorrds.toArray());

        arena.setWidth(SECTION_WIDTH * 3);
        arena.setHeight(SECTION_HEIGHT);

        arena = new ArenaBuilder(assetManager, arenaSkin).addSection(
                new ArenaBuilder.Section(defaultCoords,
                        ArenaBuilder.wall.DOOR,
                        ArenaBuilder.wall.NONE,
                        ArenaBuilder.wall.FULL,
                        ArenaBuilder.wall.FULL))
                .addSection(new ArenaBuilder.Section(new MapCoords(defaultCoords.getX() + 1, defaultCoords.getY()),
                                ArenaBuilder.wall.NONE,
                                ArenaBuilder.wall.NONE,
                                ArenaBuilder.wall.FULL,
                                ArenaBuilder.wall.FULL))
                .addSection(new ArenaBuilder.Section(new MapCoords(defaultCoords.getX() + 2, defaultCoords.getY()),
                                ArenaBuilder.wall.NONE,
                                ArenaBuilder.wall.DOOR,
                                ArenaBuilder.wall.FULL,
                                ArenaBuilder.wall.FULL)).buildArena(arena);

/*        //BEFORE GAP
        arena.addEntity(decorFactory.wallBag(0,  -WALLWIDTH, WALLWIDTH * 6, WALLWIDTH * 3, arenaSkin));
        //AFTER GAP
        arena.addEntity(decorFactory.wallBag(WIDTH - WALLWIDTH * 6,  -WALLWIDTH, WALLWIDTH * 6, WALLWIDTH * 3, arenaSkin));
        //HIDDEN SAFETY NET
        arena.addEntity(decorFactory.wallBag(0,  -WALLWIDTH * 3, WIDTH, WALLWIDTH * 3, arenaSkin));*/

        arena.addEntity(decorFactory.wallBag(Measure.units(30f),  Measure.units(10f), Measure.units(70f), WALLWIDTH * 2, arenaSkin));

        arena.addEntity(decorFactory.wallBag(Measure.units(180f),  Measure.units(10f), Measure.units(60f), WALLWIDTH * 6, arenaSkin));


/*        arena.addEntity(decorFactory.chevronBag(Measure.units(85f), Measure.units(22.5f), -90));
        arena.addEntity(decorFactory.chevronBag(Measure.units(55f), Measure.units(22.5f), -90));
        arena.addEntity(decorFactory.chevronBag(Measure.units(25f), Measure.units(22.5f), -90));*/

        Bag<Component> jumpTutorialTextbag = new Bag<Component>();
        jumpTutorialTextbag.add(new PositionComponent(MainGame.GAME_WIDTH / 2, 800));
        TextureFontComponent jump = new TextureFontComponent(jumpTutorialString);
        jump.layer = TextureRegionComponent.BACKGROUND_LAYER_NEAR;
        jumpTutorialTextbag.add(jump);
        arena.addEntity(jumpTutorialTextbag);

        Bag<Component> doubleJumpTutorialTextbag = new Bag<Component>();
        doubleJumpTutorialTextbag.add(new PositionComponent(Measure.units(175f), 1000));
        TextureFontComponent doubleJump = new TextureFontComponent(doubleJumpTutorialString);
        doubleJump.layer = TextureRegionComponent.BACKGROUND_LAYER_NEAR;
        doubleJumpTutorialTextbag.add(doubleJump);
        arena.addEntity(doubleJumpTutorialTextbag);


        return arena;

    }




    public Arena enemyTurtorial(MapCoords defaultCoords){

        Arena arena = new Arena(Arena.RoomType.TRAP, arenaSkin, defaultCoords);


        arena.setWidth(SECTION_WIDTH);
        arena.setHeight(SECTION_HEIGHT);

        arena =  new ArenaBuilder(assetManager, arenaSkin).addSection(
                new ArenaBuilder.Section(defaultCoords,
                        ArenaBuilder.wall.FULL,
                        ArenaBuilder.wall.DOOR,
                        ArenaBuilder.wall.FULL,
                        ArenaBuilder.wall.DOOR)).buildArena(arena);

        Bag<Component> bag = new Bag<Component>();
        bag.add(new PositionComponent(MainGame.GAME_WIDTH / 2, 1000));
        bag.add(new TextureFontComponent(enemyTutorialString));
        arena.addEntity(bag);

        arena.addEntity(createTutorialHighlight(0, WALLWIDTH * 2, WALLWIDTH / 2, HEIGHT));
        arena.addEntity(createTutorialHighlight(0, WALLWIDTH * 2, WIDTH, WALLWIDTH / 2));
        arena.addEntity(createTutorialHighlight(WIDTH - WALLWIDTH / 2, WALLWIDTH * 2, WALLWIDTH / 2, HEIGHT));
        arena.addEntity(createTutorialHighlight(0, HEIGHT - WALLWIDTH / 2, WIDTH, WALLWIDTH / 2));

        bag = new BlobFactory(assetManager).blobBag(arena.getWidth() - Measure.units(12), WALLWIDTH * 4);
        BagSearch.getObjectOfTypeClass(AccelerantComponent.class, bag).maxX = Measure.units(0);
        arena.addEntity(bag);


        return arena;

    }



    public Arena grappleTutorial(MapCoords defaultCoords){

        Array<MapCoords> containingCorrds = new Array<MapCoords>();
        containingCorrds.add(defaultCoords);
        containingCorrds.add(new MapCoords(defaultCoords.getX(), defaultCoords.getY() +  1));
        containingCorrds.add(new MapCoords(defaultCoords.getX(), defaultCoords.getY() +  2));

        Arena arena = new Arena(arenaSkin, containingCorrds.toArray());

        arena.setWidth(SECTION_WIDTH);
        arena.setHeight(SECTION_HEIGHT * 3);

        float HEIGHT = SECTION_HEIGHT * 3;
        float WIDTH = SECTION_WIDTH;

        arena =  new ArenaBuilder(assetManager, arenaSkin)
                .addSection(new ArenaBuilder.Section(defaultCoords,
                        ArenaBuilder.wall.DOOR,
                        ArenaBuilder.wall.FULL,
                        ArenaBuilder.wall.NONE,
                        ArenaBuilder.wall.FULL))
                .addSection(new ArenaBuilder.Section(new MapCoords(defaultCoords.getX(), defaultCoords.getY() +  1),
                        ArenaBuilder.wall.FULL,
                        ArenaBuilder.wall.FULL,
                        ArenaBuilder.wall.NONE,
                        ArenaBuilder.wall.NONE))
                .addSection(new ArenaBuilder.Section(new MapCoords(defaultCoords.getX(), defaultCoords.getY() +  2),
                        ArenaBuilder.wall.FULL,
                        ArenaBuilder.wall.FULL,
                        ArenaBuilder.wall.DOOR,
                        ArenaBuilder.wall.NONE)).buildArena(arena);

        //TODO find less lazy way to add the highlight to the arena
        Bag<Component> bag;
        bag = decorFactory.grateBag(WIDTH / 2, HEIGHT - Measure.units(15),
                new MapCoords(defaultCoords.getX(), defaultCoords.getY() + 2),
                new MapCoords(defaultCoords.getX(), defaultCoords.getY() + 3),
                Direction.UP);
        //arena.addDoor(bag);
        arena.addEntity(createTutorialHighlight(BagSearch.getObjectOfTypeClass(CollisionBoundComponent.class, bag).bound));
/*
        //GROUND
        arena.addEntity(EntityFactory.wallBag(0,  -WALLWIDTH, WIDTH, WALLWIDTH * 3));*/

        bag = new Bag<Component>();
        bag.add(new PositionComponent(MainGame.GAME_WIDTH / 2, 800));
        bag.add(new TextureFontComponent(grappleTutorialString));
        arena.addEntity(bag);


        for(int i = 0; i < 4; i ++) {
            bag = decorFactory.grapplePointBag(arena.getWidth() / 2, Measure.units(50 + (i * 30)));
            arena.addEntity(bag);
            arena.addEntity(createTutorialHighlight(BagSearch.getObjectOfTypeClass(CollisionBoundComponent.class, bag).bound));
        }

        return arena;

    }


    public Arena endTutorial(MapCoords defaultCoords){

        Array<MapCoords> containingCorrds = new Array<MapCoords>();
        containingCorrds.add(defaultCoords);

        Arena arena = new Arena(arenaSkin, containingCorrds.toArray());

        arena.setWidth(SECTION_WIDTH);
        arena.setHeight(SECTION_HEIGHT);

        arena =  new ArenaBuilder(assetManager, arenaSkin)
                .addSection(new ArenaBuilder.Section(defaultCoords,
                        ArenaBuilder.wall.DOOR,
                        ArenaBuilder.wall.FULL,
                        ArenaBuilder.wall.DOOR,
                        ArenaBuilder.wall.FULL)).buildArena(arena);

        Bag<Component> bag = new Bag<Component>();

        bag.add(new PositionComponent(MainGame.GAME_WIDTH / 2, 700));
        bag.add(new TextureFontComponent(endString));
        arena.addEntity(bag);


        arena.addEntity(decorFactory.chevronBag(Measure.units(42.5f), Measure.units(48.5f), -90));
        arena.addEntity(decorFactory.chevronBag(Measure.units(65f), Measure.units(48.5f), 90));
        arena.addEntity(decorFactory.chevronBag(Measure.units(54f), Measure.units(23f), 0));

        return arena;

    }

    public ComponentBag createTutorialHighlight(Rectangle r) {
        return createTutorialHighlight(r.x, r.y, r.width, r.height);
    }

    public ComponentBag createTutorialHighlight(float x, float y, float width, float height) {
        ComponentBag bag = new ComponentBag();
        bag.add(new PositionComponent(x, y));
        ShapeComponent sc = new ShapeComponent(width, height, TextureRegionComponent.FOREGROUND_LAYER_FAR);
        bag.add(sc);
        bag.add(new FadeComponent());
        return bag;
    }

    public ComponentBag createTutorialHighlight(float x, float y, float width, float height, Color c) {
        ComponentBag bag = new ComponentBag();
        bag.add(new PositionComponent(x, y));
        ShapeComponent sc = new ShapeComponent(width, height, TextureRegionComponent.FOREGROUND_LAYER_FAR);
        sc.color = c;
        bag.add(sc);
        bag.add(new FadeComponent());
        return bag;
    }





}
