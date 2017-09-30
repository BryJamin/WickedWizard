package com.bryjamin.wickedwizard.factories.arenas.levels;

import com.artemis.Component;
import com.artemis.Entity;
import com.artemis.World;
import com.artemis.utils.Bag;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.bryjamin.wickedwizard.MainGame;
import com.bryjamin.wickedwizard.assets.ColorResource;
import com.bryjamin.wickedwizard.assets.FontAssets;
import com.bryjamin.wickedwizard.assets.MenuStrings;
import com.bryjamin.wickedwizard.assets.PreferenceStrings;
import com.bryjamin.wickedwizard.assets.TextureStrings;
import com.bryjamin.wickedwizard.ecs.components.ai.Action;
import com.bryjamin.wickedwizard.ecs.components.ai.ActionAfterTimeComponent;
import com.bryjamin.wickedwizard.ecs.components.ai.ProximityTriggerAIComponent;
import com.bryjamin.wickedwizard.ecs.components.movement.CollisionBoundComponent;
import com.bryjamin.wickedwizard.ecs.components.movement.PositionComponent;
import com.bryjamin.wickedwizard.ecs.components.texture.FadeComponent;
import com.bryjamin.wickedwizard.ecs.components.texture.TextureFontComponent;
import com.bryjamin.wickedwizard.ecs.components.texture.TextureRegionComponent;
import com.bryjamin.wickedwizard.ecs.components.texture.UIComponent;
import com.bryjamin.wickedwizard.ecs.systems.graphical.TipsMessageSystem;
import com.bryjamin.wickedwizard.ecs.systems.graphical.UISystem;
import com.bryjamin.wickedwizard.ecs.systems.level.ArenaMap;
import com.bryjamin.wickedwizard.ecs.systems.level.GameSystem;
import com.bryjamin.wickedwizard.ecs.systems.level.ScreenWipeSystem;
import com.bryjamin.wickedwizard.factories.TutorialTipsFactory;
import com.bryjamin.wickedwizard.factories.arenas.Arena;
import com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder;
import com.bryjamin.wickedwizard.factories.arenas.decor.ArenaEnemyPlacementFactory;
import com.bryjamin.wickedwizard.factories.arenas.decor.ArenaShellFactory;
import com.bryjamin.wickedwizard.factories.arenas.decor.DecorFactory;
import com.bryjamin.wickedwizard.factories.arenas.decor.OnLoadFactory;
import com.bryjamin.wickedwizard.factories.arenas.skins.ArenaSkin;
import com.bryjamin.wickedwizard.factories.enemy.BlobFactory;
import com.bryjamin.wickedwizard.screens.DataSave;
import com.bryjamin.wickedwizard.screens.MenuButton;
import com.bryjamin.wickedwizard.utils.BagSearch;
import com.bryjamin.wickedwizard.utils.CenterMath;
import com.bryjamin.wickedwizard.utils.ComponentBag;
import com.bryjamin.wickedwizard.utils.MapCoords;
import com.bryjamin.wickedwizard.utils.Measure;
import com.bryjamin.wickedwizard.utils.enums.Level;

import static com.bryjamin.wickedwizard.ecs.components.texture.TextureRegionComponent.BACKGROUND_LAYER_MIDDLE;
import static com.bryjamin.wickedwizard.ecs.components.texture.TextureRegionComponent.ENEMY_LAYER_FAR;
import static com.bryjamin.wickedwizard.ecs.components.texture.TextureRegionComponent.ENEMY_LAYER_MIDDLE;
import static com.bryjamin.wickedwizard.ecs.components.texture.TextureRegionComponent.FOREGROUND_LAYER_NEAR;

/**
 * Created by Home on 01/04/2017.
 */

public class TutorialFactory extends ArenaShellFactory {

    private final String moveTutorialString1 = "Exit this room on the right";
    private final String moveTutorialString2 = "Touch ANYWHERE BELOW The WHITE LINE to MOVE";
    private final String moveTutorialString3 = "(This Includes the Borders)";


    private static final String jumpTutorialString =
            "Tap ANYWHERE ABOVE your character to jump!";

    private static final String jumpTutorialStringDoubleTapBelow =
            "DOUBLE Tap ANYWHERE BELOW your character to cancel hovering";


    private static final String doubleJumpTutorialString =
            "Tap ANYWHERE ABOVE your character and tap ABOVE AGAIN \n To DOUBLE Jump";

    private static final String enemyTutorialString =
            "TOUCH and HOLD ANYWHERE ABOVE the WHITE LINE \n\n to SHOOT \n \n (DRAG to Change AIM)";

    private static final String movingEnemyTutorial =
            "Oh No, This ENEMY is Moving! \n\n Stop It Before it Learns to do More!";

    private static final String movingEnemyTutorial2 =
            "Ack! Another One! \n\n Take Care Of It!";



    private static final String grappleTutorialString =
            "Tap Grappling Points to Shoot a Grappling Hook \n \n (Grapple Points are Currently Being Highlighted)";
    private static final String endString =
            "Training Complete \n \n Enter the Portal to Begin";


    private static final String controllerTutorialTop =
            "It is Recommended to Hold Your Phone Like This:";

    private static final String controllerTutorialMiddle =
            "When Playing This Game";

    private static final String controllerTutorialBottom =
            "(Note: Drawing Human Anatomy is Hard)";


    public static final String bulletTutorial = "Bullets are bad for you, Do not get hit";


    public static final String guideLineTutorialStringTop = "Would You Like to Disable the White Line? \n\n (You can Change This Later in Settings on the Main Menu)";

    public static final String grappleJumpTutorialString = "You Can Jump AFTER Grappling \n\n To Gain ADDITIONAL Height";


    private static final String platformString =
            "Double Tap AnyWhere DIRECTLY BELOW your character to \n Fall through platforms";



    private Color textColor;

    private TutorialTipsFactory tutorialTipsFactory;

    public TutorialFactory(AssetManager assetManager, ArenaSkin arenaSkin) {
        super(assetManager, arenaSkin);
        this.textColor = new Color(Color.BLACK);
        this.tutorialTipsFactory = new TutorialTipsFactory(assetManager);
    }



    public ArenaMap tutorialMap(){
        Array<Arena> placedArenas = new Array<Arena>();


        Gdx.app.getPreferences(PreferenceStrings.SETTINGS).putBoolean(PreferenceStrings.SETTINGS_GUIDELINE, true).flush();
        Gdx.app.getPreferences(PreferenceStrings.SETTINGS).putBoolean(PreferenceStrings.SETTINGS_AUTOFIRE, false).flush();


        Arena startingArena = groundMovementTutorial(new MapCoords(0,0));


        startingArena.addEntity(new OnLoadFactory().startMusicEntity(Level.ONE.getMusic()));

        placedArenas.add(startingArena);

//        placedArenas.add(grappleJumpTutorial(new MapCoords(1, 0)));
        placedArenas.add(controllerTurtorial(new MapCoords(1, 0)));
        placedArenas.add(jumpTutorial(new MapCoords(2, 0)));
        placedArenas.add(enemyTurtorial(new MapCoords(6,0)));
        placedArenas.add(enemyTutorialPart2(new MapCoords(7,0)));
        placedArenas.add(movingEnemyTurtorial(new MapCoords(8,0)));
        placedArenas.add(movingEnemyTurtorialPart2(new MapCoords(9, 0)));
        placedArenas.add(bulletTutorial(new MapCoords(10, 0)));
        placedArenas.add(platformTutorial(new MapCoords(11,0)));
        placedArenas.add(grappleTutorial(new MapCoords(12,0)));
        placedArenas.add(grappleJumpTutorial(new MapCoords(12, 3)));
        placedArenas.add(guideLineTutorial(new MapCoords(12, 5)));
        placedArenas.add(endTutorial(new MapCoords(13,5)));

        //guideLineTutorial

        return new ArenaMap(startingArena, placedArenas);

    }


    public Arena groundMovementTutorial(MapCoords defaultCoords){

        Arena arena =  new ArenaBuilder(assetManager, arenaSkin, Arena.ArenaType.NORMAL)
                .addSection(new ArenaBuilder.Section(defaultCoords,
                        ArenaBuilder.wall.FULL,
                        ArenaBuilder.wall.DOOR,
                        ArenaBuilder.wall.FULL,
                        ArenaBuilder.wall.FULL)).buildArena();

        Bag<Component> textBag = new Bag<Component>();
        textBag.add(new PositionComponent(MainGame.GAME_WIDTH / 2, Measure.units(40f)));
        TextureFontComponent text = new TextureFontComponent(moveTutorialString1, textColor);
        text.layer = BACKGROUND_LAYER_MIDDLE;
        textBag.add(text);
        arena.addEntity(textBag);



        for(int i = 0; i < 3; i ++)  arena.addEntity(decorFactory.chevronBag(Measure.units(15f + (i * 30)), Measure.units(15f), -90));

        Rectangle textBounds = new Rectangle(0, 0, arena.getWidth(), WALLWIDTH * 2);

        ComponentBag bag = new ComponentBag();
        bag.add(new PositionComponent(textBounds.getX(), textBounds.getY()));
        bag.add(new CollisionBoundComponent(textBounds));
        bag.add(new UIComponent());
        TextureFontComponent tfc = new TextureFontComponent(FontAssets.medium, moveTutorialString2, new Color(Color.WHITE));
        tfc.layer = FOREGROUND_LAYER_NEAR;
        bag.add(tfc);
        arena.addEntity(bag);

        return arena;

    }





    public Arena platformTutorial(MapCoords defaultCoords){

        Arena arena =  new ArenaBuilder(assetManager, arenaSkin, Arena.ArenaType.NORMAL)
                .addSection(new ArenaBuilder.Section(defaultCoords,
                        ArenaBuilder.wall.DOOR,
                        ArenaBuilder.wall.DOOR,
                        ArenaBuilder.wall.NONE,
                        ArenaBuilder.wall.FULL))
                .addSection(new ArenaBuilder.Section(new MapCoords(defaultCoords.getX(), defaultCoords.getY() + 1),
                        ArenaBuilder.wall.FULL,
                        ArenaBuilder.wall.FULL,
                        ArenaBuilder.wall.FULL,
                        ArenaBuilder.wall.NONE)).buildArena();

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

        for(int i = 0; i < 2; i ++)  arena.addEntity(decorFactory.chevronBag(Measure.units(17.5f), Measure.units(17.5f + (i * 25f)), 0));

        TextureFontComponent tfc = new TextureFontComponent(FontAssets.small, platformString, textColor);
        tfc.layer = BACKGROUND_LAYER_MIDDLE;
        ComponentBag bag = new ComponentBag();
        bag.add(new PositionComponent(MainGame.GAME_WIDTH / 2, Measure.units(75f)));
        bag.add(tfc);

        arena.addEntity(bag);

        return arena;

    }


    public Arena jumpTutorial(MapCoords defaultCoords){

        Arena arena = new ArenaBuilder(assetManager, arenaSkin, Arena.ArenaType.NORMAL).addSection(
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
                        ArenaBuilder.wall.NONE,
                        ArenaBuilder.wall.FULL,
                        ArenaBuilder.wall.FULL))
                .addSection(new ArenaBuilder.Section(new MapCoords(defaultCoords.getX() + 3, defaultCoords.getY()),
                                ArenaBuilder.wall.NONE,
                                ArenaBuilder.wall.DOOR,
                                ArenaBuilder.wall.FULL,
                                ArenaBuilder.wall.FULL)).buildArena();

        arena.addEntity(decorFactory.wallBag(Measure.units(30f),  Measure.units(10f), Measure.units(40f), WALLWIDTH * 2, arenaSkin));

        arena.addEntity(decorFactory.wallBag(Measure.units(100f),  Measure.units(10f), Measure.units(40f), WALLWIDTH * 2, arenaSkin));
        arena.addEntity(decorFactory.wallBag(Measure.units(140f),  Measure.units(10f), Measure.units(40f), WALLWIDTH * 4, arenaSkin));

        float wall2PosX = Measure.units(290f);

        arena.addEntity(decorFactory.wallBag(wall2PosX,  Measure.units(10f), Measure.units(60f), WALLWIDTH * 6, arenaSkin));


        Bag<Component> jumpTutorialTextbag = new Bag<Component>();
        jumpTutorialTextbag.add(new PositionComponent(MainGame.GAME_WIDTH / 2, Measure.units(40f)));
        TextureFontComponent jump = new TextureFontComponent(jumpTutorialString, textColor);
        jump.layer = TextureRegionComponent.BACKGROUND_LAYER_NEAR;
        jumpTutorialTextbag.add(jump);
        arena.addEntity(jumpTutorialTextbag);

        Bag<Component> doubleJumpTutorialTextbag = new Bag<Component>();
        doubleJumpTutorialTextbag.add(new PositionComponent(wall2PosX - Measure.units(5f), Measure.units(50f)));
        TextureFontComponent doubleJump = new TextureFontComponent(doubleJumpTutorialString, textColor);
        doubleJump.layer = TextureRegionComponent.BACKGROUND_LAYER_NEAR;
        doubleJumpTutorialTextbag.add(doubleJump);
        arena.addEntity(doubleJumpTutorialTextbag);




        ComponentBag messageBoxTrigger = new ComponentBag();
        messageBoxTrigger.add(new PositionComponent(Measure.units(110f), 0));
        Rectangle rectangle = new Rectangle(Measure.units(90f), 0, Measure.units(160f), Measure.units(70f));
        messageBoxTrigger.add(new CollisionBoundComponent(rectangle));
        messageBoxTrigger.add(new com.bryjamin.wickedwizard.ecs.components.identifiers.ParentComponent());
        messageBoxTrigger.add(new ProximityTriggerAIComponent(new com.bryjamin.wickedwizard.ecs.components.ai.Task() {
            @Override
            public void performAction(World world, Entity e) {
                world.getSystem(TipsMessageSystem.class).createTipsMessage(MenuStrings.Tutorial.DOUBLE_TAP_ID);
                e.deleteFromWorld();
            }

            @Override
            public void cleanUpAction(World world, Entity e) {
            }
        }, new com.bryjamin.wickedwizard.utils.collider.HitBox(rectangle)));

        arena.addEntity(messageBoxTrigger);

        return arena;

    }




    public Arena enemyTurtorial(MapCoords defaultCoords){

        Arena arena = createOmniArenaHiddenGrapple(defaultCoords, Arena.ArenaType.TRAP);

        ComponentBag bag = new ComponentBag();
        bag.add(new PositionComponent(MainGame.GAME_WIDTH / 2, Measure.units(50f)));

        bag.add(new TextureFontComponent(FontAssets.small, enemyTutorialString, TextureRegionComponent.FOREGROUND_LAYER_NEAR, textColor));
        bag.add(new UIComponent());

        arena.addEntity(bag);

        bag = new BlobFactory(assetManager).blob(arena.getWidth() - Measure.units(20), Measure.units(30f),1, 0, 6, true, ColorResource.BLOB_GREEN);
        arena.addEntity(bag);

        threeGroundChevrons(arena);


        return arena;

    }



    public Arena enemyTutorialPart2(MapCoords defaultCoords){

        Arena arena = createOmniArenaHiddenGrapple(defaultCoords, Arena.ArenaType.TRAP);

        ComponentBag bag = new ComponentBag();
        bag.add(new PositionComponent(MainGame.GAME_WIDTH / 2, Measure.units(50f)));
        bag.add(new TextureFontComponent(FontAssets.small, enemyTutorialString, TextureRegionComponent.FOREGROUND_LAYER_NEAR, textColor));
        bag.add(new UIComponent());

        bag = new ComponentBag();
        bag.add(new ActionAfterTimeComponent(new Action() {
            @Override
            public void performAction(World world, Entity e) {
                world.getSystem(TipsMessageSystem.class).createTipsMessage(MenuStrings.Tutorial.AIMING_ID);
                e.deleteFromWorld();
            }
        }));


        arena.addEntity(bag);

        bag = new BlobFactory(assetManager).blob(arena.getWidth() - Measure.units(20), Measure.units(30f),1, 0, 6, true, ColorResource.BLOB_GREEN);
        arena.addEntity(bag);


        threeGroundChevrons(arena);

        return arena;

    }



    public Arena movingEnemyTurtorial(MapCoords defaultCoords){

        Arena arena = createOmniArenaHiddenGrapple(defaultCoords, Arena.ArenaType.TRAP);

        ComponentBag bag = new ComponentBag();
        bag.add(new PositionComponent(MainGame.GAME_WIDTH / 2, Measure.units(50f)));


        TextureFontComponent text = new TextureFontComponent(FontAssets.small, movingEnemyTutorial, textColor);
        text.layer = ENEMY_LAYER_FAR;
        bag.add(text);

        arena.addEntity(bag);

        bag = new ArenaEnemyPlacementFactory(assetManager, arenaSkin, MathUtils.random).spawnBouncer(arena.getWidth() / 2, Measure.units(30), true);
        arena.addEntity(bag);


        bag = new ComponentBag();
        bag.add(new ActionAfterTimeComponent(new Action() {
            @Override
            public void performAction(World world, Entity e) {
                world.getSystem(TipsMessageSystem.class).createTipsMessage(MenuStrings.Tutorial.TWO_THUMBS_ID);
                e.deleteFromWorld();
            }
        }));
        arena.addEntity(bag);


        threeGroundChevrons(arena);

        return arena;

    }


    public Arena movingEnemyTurtorialPart2(MapCoords defaultCoords){

        Arena arena = createOmniArenaHiddenGrapple(defaultCoords, Arena.ArenaType.TRAP);

        ComponentBag bag = new ComponentBag();
        bag.add(new PositionComponent(MainGame.GAME_WIDTH / 2, Measure.units(50f)));


        TextureFontComponent text = new TextureFontComponent(FontAssets.small, movingEnemyTutorial2, textColor);
        text.layer = ENEMY_LAYER_FAR;
        bag.add(text);

        arena.addEntity(bag);

        bag = new ArenaEnemyPlacementFactory(assetManager, arenaSkin, MathUtils.random).spawnBouncer(arena.getWidth() / 2, Measure.units(30), true);
        arena.addEntity(bag);

        threeGroundChevrons(arena);

        bag = arena.createArenaBag();
        bag.add(new ActionAfterTimeComponent(new Action() {
            @Override
            public void performAction(World world, Entity e) {
                world.getSystem(TipsMessageSystem.class).createTipsMessage(MenuStrings.Tutorial.AUTO_FIRE_ID);
                e.deleteFromWorld();
            }
        }));


        return arena;

    }


    public void threeGroundChevrons(Arena arena) {

        for(int i = 0; i < 3; i ++)  {

            ComponentBag chevron = decorFactory.chevronBag(Measure.units(15f + (i * 30)), Measure.units(15f), -90);
            BagSearch.getObjectOfTypeClass(TextureRegionComponent.class, chevron).color.a = 0;
            chevron.add(new com.bryjamin.wickedwizard.ecs.components.ai.InCombatActionComponent(new com.bryjamin.wickedwizard.ecs.components.ai.Task() {
                @Override
                public void performAction(World world, Entity e) {

                }

                @Override
                public void cleanUpAction(World world, Entity e) {
                    e.edit().add(new FadeComponent(true, 0.5f, false));
                }
            }));

            arena.addEntity(chevron);
        }
    }



    public Arena grappleTutorial(MapCoords defaultCoords){

        Arena arena =  new ArenaBuilder(assetManager, arenaSkin, Arena.ArenaType.NORMAL)
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
                        ArenaBuilder.wall.NONE)).buildArena();

        //TODO find less lazy way to add the highlight to the arena
        Bag<Component> bag;

        arena.addEntity(decorFactory.platform(0, arena.getHeight() - Measure.units(35), arena.getWidth()));

        bag = new Bag<Component>();
        bag.add(new PositionComponent(MainGame.GAME_WIDTH / 2, Measure.units(40f)));

        TextureFontComponent text = new TextureFontComponent(grappleTutorialString, textColor);
        text.layer = BACKGROUND_LAYER_MIDDLE;
        bag.add(text);


        arena.addEntity(bag);


        for(int i = 0; i < 5; i ++) {
            bag = decorFactory.grapplePointBag(arena.getWidth() / 2, Measure.units(50 + (i * 30)));
            arena.addEntity(bag);
            arena.addEntity(createTutorialHighlight(BagSearch.getObjectOfTypeClass(CollisionBoundComponent.class, bag).bound));
        }

        return arena;

    }


    public Arena endTutorial(MapCoords defaultCoords){

        Arena arena =  new ArenaBuilder(assetManager, arenaSkin, Arena.ArenaType.NORMAL)
                .addSection(new ArenaBuilder.Section(defaultCoords,
                        ArenaBuilder.wall.DOOR,
                        ArenaBuilder.wall.FULL,
                        ArenaBuilder.wall.FULL,
                        ArenaBuilder.wall.FULL)).buildArena();

        Bag<Component> bag = new Bag<Component>();

        bag.add(new PositionComponent(MainGame.GAME_WIDTH / 2, Measure.units(47.5f)));

        TextureFontComponent text = new TextureFontComponent(endString, textColor);
        text.layer = BACKGROUND_LAYER_MIDDLE;
        bag.add(text);

        arena.addEntity(bag);



        arena.addEntity(new com.bryjamin.wickedwizard.factories.arenas.decor.PortalFactory(assetManager).customSmallPortal(arena.getWidth() / 2, Measure.units(25f),

                new Action() {
                    @Override
                    public void performAction(World world, Entity e) {

                        world.getSystem(ScreenWipeSystem.class).startScreenWipe(ScreenWipeSystem.Transition.FADE, new Action() {
                            @Override
                            public void performAction(World world, Entity e) {

                                MainGame game = world.getSystem(GameSystem.class).getGame();
                                DataSave.saveChallengeData(com.bryjamin.wickedwizard.factories.arenas.challenges.ChallengesResource.TUTORIAL_COMPLETE);
                                game.getScreen().dispose();
                                game.setScreen(new com.bryjamin.wickedwizard.screens.MenuScreen(game));

                            }
                        });
                    }
                }));



        bag = new ComponentBag();
        bag.add(new ActionAfterTimeComponent(new Action() {
            @Override
            public void performAction(World world, Entity e) {
                world.getSystem(TipsMessageSystem.class).createTipsMessage(MenuStrings.Tutorial.UI_ID);
                e.deleteFromWorld();
            }
        }));

        arena.addEntity(bag);

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



    public Arena controllerTurtorial(MapCoords defaultCoords){

        Arena arena =  new ArenaBuilder(assetManager, arenaSkin, Arena.ArenaType.TRAP).addSection(
                new ArenaBuilder.Section(defaultCoords,
                        ArenaBuilder.wall.DOOR,
                        ArenaBuilder.wall.DOOR,
                        ArenaBuilder.wall.FULL,
                        ArenaBuilder.wall.FULL)).buildArena();

        ComponentBag bag = new ComponentBag();
        bag.add(new ActionAfterTimeComponent(new Action() {
            @Override
            public void performAction(World world, Entity e) {
                world.getSystem(TipsMessageSystem.class).createTipsMessage(MenuStrings.Tutorial.CONTROLLER_ID);
                e.deleteFromWorld();
            }
        }));

        arena.addEntity(bag);

        return arena;

    }



    public Arena bulletTutorial(MapCoords defaultCoords){

        Arena arena =  new ArenaBuilder(assetManager, arenaSkin, Arena.ArenaType.TRAP).addSection(
                new ArenaBuilder.Section(defaultCoords,
                        ArenaBuilder.wall.DOOR,
                        ArenaBuilder.wall.DOOR,
                        ArenaBuilder.wall.FULL,
                        ArenaBuilder.wall.FULL)).buildArena();

        DecorFactory decorFactory = new DecorFactory(assetManager, arenaSkin);


        arena.addEntity(decorFactory.fixedWallTurret(Measure.units(47.5f), arena.getHeight() - Measure.units(10f), 180, 1.5f, 0));


        Rectangle textRectangle = new Rectangle(0, 0, MainGame.GAME_WIDTH, Measure.units(10f));

        ComponentBag bag = new ComponentBag();
        bag.add(new PositionComponent(0, Measure.units(40f)));
        TextureFontComponent text = new TextureFontComponent(FontAssets.small, bulletTutorial, textColor);
        text.layer = ENEMY_LAYER_MIDDLE;
        bag.add(new CollisionBoundComponent(textRectangle));
        bag.add(text);

        arena.addEntity(bag);


        return arena;

    }





    public Arena guideLineTutorial(MapCoords defaultCoords){

        Arena arena =  createOmniArenaHiddenGrapple(defaultCoords, Arena.ArenaType.TRAP);

        Rectangle textRectangle = new Rectangle(0, 0, MainGame.GAME_WIDTH, Measure.units(10f));

        ComponentBag bag = new ComponentBag();
        bag.add(new PositionComponent(0, Measure.units(40f)));
        TextureFontComponent text = new TextureFontComponent(FontAssets.small, guideLineTutorialStringTop, textColor);
        text.layer = ENEMY_LAYER_MIDDLE;
        bag.add(new CollisionBoundComponent(textRectangle));
        bag.add(text);

        arena.addEntity(bag);


        final boolean guideLineBool = Gdx.app.getPreferences(PreferenceStrings.SETTINGS).getBoolean(PreferenceStrings.SETTINGS_GUIDELINE, true);


        float width = Measure.units(10f);
        float height = Measure.units(10f);


        MenuButton.MenuButtonBuilder menuButtonBuilder = new MenuButton.MenuButtonBuilder(FontAssets.medium, atlas.findRegion(TextureStrings.BLOCK))
                .width(width)
                .height(height)
                .backgroundColor(new Color(Color.WHITE))
                .foregroundColor(new Color(Color.BLACK))
                .action(new Action() {
                    @Override
                    public void performAction(World world, Entity e) {
                        boolean guideLineBool = !Gdx.app.getPreferences(PreferenceStrings.SETTINGS).getBoolean(PreferenceStrings.SETTINGS_GUIDELINE, true);
                        Gdx.app.getPreferences(PreferenceStrings.SETTINGS).putBoolean(PreferenceStrings.SETTINGS_GUIDELINE, guideLineBool).flush();
                        e.getComponent(TextureFontComponent.class).text = guideLineBool ? MenuStrings.ON : MenuStrings.OFF;
                        world.getSystem(UISystem.class).updateGuideLineUsingPreferences();
                    }
                });


        for(ComponentBag c : menuButtonBuilder.build().createButton(guideLineBool ? MenuStrings.ON : MenuStrings.OFF, CenterMath.offsetX(MainGame.GAME_WIDTH, width),  Measure.units(25f))){
            arena.addEntity(c);
        };


        return arena;

    }





    public Arena grappleJumpTutorial(MapCoords defaultCoords){

        Arena arena =  new ArenaBuilder(assetManager, arenaSkin, Arena.ArenaType.TRAP)
                .addSection(new ArenaBuilder.Section(defaultCoords,
                        ArenaBuilder.wall.DOOR,
                        ArenaBuilder.wall.DOOR,
                        ArenaBuilder.wall.NONE,
                        ArenaBuilder.wall.DOOR))
                .addSection(new ArenaBuilder.Section(new MapCoords(defaultCoords.getX(), defaultCoords.getY() + 1),
                        ArenaBuilder.wall.DOOR,
                        ArenaBuilder.wall.DOOR,
                        ArenaBuilder.wall.GRAPPLE,
                        ArenaBuilder.wall.NONE))


                .buildArena();

        Rectangle textRectangle = new Rectangle(0, 0, MainGame.GAME_WIDTH, Measure.units(10f));

        ComponentBag bag = new ComponentBag();
        bag.add(new PositionComponent(0, Measure.units(37.5f)));
        TextureFontComponent text = new TextureFontComponent(FontAssets.small, grappleJumpTutorialString, textColor);
        text.layer = ENEMY_LAYER_MIDDLE;
        bag.add(new CollisionBoundComponent(textRectangle));
        bag.add(text);

        arena.addEntity(bag);

        arena.addEntity(decorFactory.grapplePointBag(arena.getWidth() / 2, Measure.units(30)));
        arena.addEntity(decorFactory.platform(Measure.units(5f), Measure.units(50f), MainGame.GAME_WIDTH));


        arena.addEntity(decorFactory.wallBag(CenterMath.offsetX(MainGame.GAME_WIDTH, Measure.units(25f)), Measure.units(75f), Measure.units(25f), Measure.units(5f)));


        //Arrows
        for(int i = 0; i < 2; i ++)  arena.addEntity(decorFactory.chevronBag(arena.getWidth() - Measure.units(25f), Measure.units(67.5f + (i * 25f)), 0));
        for(int i = 0; i < 2; i ++)  arena.addEntity(decorFactory.chevronBag(Measure.units(15f), Measure.units(67.5f + (i * 25f)), 0));

        arena.addEntity(decorFactory.chevronBag(arena.getWidth() - Measure.units(25f), Measure.units(17.5f), 0));
        arena.addEntity(decorFactory.chevronBag(Measure.units(15f), Measure.units(17.5f), 0));


        return arena;

    }








}
