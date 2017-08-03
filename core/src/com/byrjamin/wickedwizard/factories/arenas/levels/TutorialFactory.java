package com.byrjamin.wickedwizard.factories.arenas.levels;

import com.artemis.Component;
import com.artemis.utils.Bag;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
import com.byrjamin.wickedwizard.MainGame;
import com.byrjamin.wickedwizard.assets.ColorResource;
import com.byrjamin.wickedwizard.assets.TextureStrings;
import com.byrjamin.wickedwizard.ecs.components.CollisionBoundComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.AccelerantComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.PositionComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.FadeComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.TextureFontComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.TextureRegionComponent;
import com.byrjamin.wickedwizard.factories.arenas.Arena;
import com.byrjamin.wickedwizard.factories.arenas.ArenaBuilder;
import com.byrjamin.wickedwizard.factories.arenas.skins.ArenaSkin;
import com.byrjamin.wickedwizard.factories.enemy.BlobFactory;
import com.byrjamin.wickedwizard.utils.BagSearch;
import com.byrjamin.wickedwizard.utils.Measure;
import com.byrjamin.wickedwizard.utils.MapCoords;
import com.byrjamin.wickedwizard.utils.ComponentBag;

import static com.byrjamin.wickedwizard.ecs.components.texture.TextureRegionComponent.FOREGROUND_LAYER_NEAR;

/**
 * Created by Home on 01/04/2017.
 */

public class TutorialFactory extends com.byrjamin.wickedwizard.factories.arenas.decor.ArenaShellFactory {

    private final String moveTutorialString1 = "Exit this room on the right";
    private final String moveTutorialString2 = "Touch within this area of the screen to move";


    private static final String jumpTutorialString =
            "Tap ABOVE your character to jump and glide! \n \n DOUBLE Tap BELOW your character to cancel glide";


    private static final String doubleJumpTutorialString =
            "Tap ABOVE your character and tap ABOVE AGAIN \n To DOUBLE Jump";

    private static final String enemyTutorialString =
            "Hark! A Worthy Foe! \n \n Touch within this area of the screen to shoot \n \n Drag to change aim";
    private static final String grappleTutorialString =
            "Tap grapple points to shoot your grappling hook \n \n (Grapple points are currently being highlighted)";
    private static final String endString =
            "Yea you seem about ready \n \n Now off to adventure!";

    private static final String platformString =
            "Double Tap Below your character to \n Fall through platforms";

    public TutorialFactory(AssetManager assetManager, ArenaSkin arenaSkin) {
        super(assetManager, arenaSkin);
    }

    public Arena groundMovementTutorial(MapCoords defaultCoords){

        Arena arena = new Arena(arenaSkin, defaultCoords);

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


        for(int i = 0; i < 3; i ++)  arena.addEntity(decorFactory.chevronBag(Measure.units(15f + (i * 30)), Measure.units(15f), -90));

        arena.addEntity(createTutorialHighlight(0,0, WIDTH, WALLWIDTH * 2, new Color(Color.BLACK)));

        return arena;

    }


    public Arena platformTutorial(MapCoords defaultCoords){

        Arena arena = new Arena(arenaSkin, defaultCoords,
                new MapCoords(defaultCoords.getX(), defaultCoords.getY() + 1));

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
        for(int i = 0; i < 2; i ++)  arena.addEntity(decorFactory.chevronBag(arena.getWidth() - Measure.units(27.5f), Measure.units(17.5f + (i * 25f)), 180));

        //arena.addEntity(decorFactory.chevronBag(Measure.units(85f), Measure.units(22.5f), 0));
        //arena.addEntity(decorFactory.chevronBag(Measure.units(55f), Measure.units(22.5f), -90));
        for(int i = 0; i < 2; i ++)  arena.addEntity(decorFactory.chevronBag(Measure.units(17.5f), Measure.units(17.5f + (i * 25f)), 0));

        TextureFontComponent tfc = new TextureFontComponent(platformString);
        ComponentBag bag = new ComponentBag();
        bag.add(new PositionComponent(MainGame.GAME_WIDTH / 2, 1500));
        bag.add(tfc);

        arena.addEntity(bag);

        return arena;

    }


    public Arena jumpTutorial(MapCoords defaultCoords){

        Arena arena = new Arena(arenaSkin, defaultCoords,
                new MapCoords(defaultCoords.getX() + 1, defaultCoords.getY()),
                new MapCoords(defaultCoords.getX() + 2, defaultCoords.getY()));

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

        bag = new BlobFactory(assetManager).blob(arena.getWidth() - Measure.units(12), Measure.units(30f),1, 0, 8, true, ColorResource.BLOB_GREEN);
        arena.addEntity(bag);


        return arena;

    }



    public Arena grappleTutorial(MapCoords defaultCoords){

        Arena arena = new Arena(arenaSkin, defaultCoords,
                new MapCoords(defaultCoords.getX(), defaultCoords.getY() +  1),
                new MapCoords(defaultCoords.getX(), defaultCoords.getY() +  2));

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

        arena.addEntity(decorFactory.platform(0, HEIGHT - Measure.units(35), WIDTH));
/*        bag = decorFactory.grateBag(WIDTH / 2, HEIGHT - Measure.units(15),
                new MapCoords(defaultCoords.getX(), defaultCoords.getY() + 2),
                new MapCoords(defaultCoords.getX(), defaultCoords.getY() + 3),
                Direction.UP);
        //arena.addDoor(bag);
        arena.addEntity(createTutorialHighlight(BagSearch.getObjectOfTypeClass(CollisionBoundComponent.class, bag).bound));*/
/*
        //GROUND
        arena.addEntity(EntityFactory.wallBag(0,  -WALLWIDTH, WIDTH, WALLWIDTH * 3));*/

        bag = new Bag<Component>();
        bag.add(new PositionComponent(MainGame.GAME_WIDTH / 2, 800));
        bag.add(new TextureFontComponent(grappleTutorialString));
        arena.addEntity(bag);


        for(int i = 0; i < 5; i ++) {
            bag = decorFactory.grapplePointBag(arena.getWidth() / 2, Measure.units(50 + (i * 30)));
            arena.addEntity(bag);
            arena.addEntity(createTutorialHighlight(BagSearch.getObjectOfTypeClass(CollisionBoundComponent.class, bag).bound));
        }

        return arena;

    }


    public Arena endTutorial(MapCoords defaultCoords){

        Arena arena = new Arena(arenaSkin, defaultCoords);

        arena.setWidth(SECTION_WIDTH);
        arena.setHeight(SECTION_HEIGHT);

        arena =  new ArenaBuilder(assetManager, arenaSkin)
                .addSection(new ArenaBuilder.Section(defaultCoords,
                        ArenaBuilder.wall.DOOR,
                        ArenaBuilder.wall.DOOR,
                        ArenaBuilder.wall.FULL,
                        ArenaBuilder.wall.FULL)).buildArena(arena);

        Bag<Component> bag = new Bag<Component>();

        bag.add(new PositionComponent(MainGame.GAME_WIDTH / 2, 900));
        bag.add(new TextureFontComponent(endString));
        arena.addEntity(bag);


/*
        arena.addEntity(decorFactory.chevronBag(Measure.units(42.5f), Measure.units(48.5f), -90));
        arena.addEntity(decorFactory.chevronBag(Measure.units(65f), Measure.units(48.5f), 90));
        arena.addEntity(decorFactory.chevronBag(Measure.units(54f), Measure.units(23f), 0));
*/

        for(int i = 0; i < 3; i ++)  arena.addEntity(decorFactory.chevronBag(Measure.units(15f + (i * 30)), Measure.units(15f), -90));
        return arena;

    }

    public ComponentBag createTutorialHighlight(Rectangle r) {
        return createTutorialHighlight(r.x, r.y, r.width, r.height);
    }

    public ComponentBag createTutorialHighlight(float x, float y, float width, float height) {
        ComponentBag bag = createTutorialHighlight(x, y, width, height, new Color(Color.WHITE));
        return bag;
    }

    public ComponentBag createTutorialHighlight(float x, float y, float width, float height, Color c) {
        ComponentBag bag = new ComponentBag();
        bag.add(new PositionComponent(x, y));
        TextureRegionComponent trc = new TextureRegionComponent(atlas.findRegion(TextureStrings.BLOCK), width, height, TextureRegionComponent.FOREGROUND_LAYER_FAR, c);
        bag.add(trc);
        bag.add(new FadeComponent());
        return bag;
    }





}
