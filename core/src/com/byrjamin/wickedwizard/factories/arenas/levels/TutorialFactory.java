package com.byrjamin.wickedwizard.factories.arenas.levels;

import com.artemis.Component;
import com.artemis.Entity;
import com.artemis.World;
import com.artemis.utils.Bag;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.byrjamin.wickedwizard.MainGame;
import com.byrjamin.wickedwizard.assets.Assets;
import com.byrjamin.wickedwizard.assets.ColorResource;
import com.byrjamin.wickedwizard.assets.PreferenceStrings;
import com.byrjamin.wickedwizard.assets.TextureStrings;
import com.byrjamin.wickedwizard.ecs.components.ai.Action;
import com.byrjamin.wickedwizard.ecs.components.movement.CollisionBoundComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.ExpireComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.FollowPositionComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.InCombatActionComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.ProximityTriggerAIComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.Task;
import com.byrjamin.wickedwizard.ecs.components.identifiers.ChildComponent;
import com.byrjamin.wickedwizard.ecs.components.identifiers.ParentComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.PositionComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.FadeComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.TextureFontComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.TextureRegionComponent;
import com.byrjamin.wickedwizard.ecs.systems.FindChildSystem;
import com.byrjamin.wickedwizard.ecs.systems.ai.OnDeathSystem;
import com.byrjamin.wickedwizard.ecs.systems.graphical.CameraSystem;
import com.byrjamin.wickedwizard.ecs.systems.level.ArenaMap;
import com.byrjamin.wickedwizard.ecs.systems.level.EndGameSystem;
import com.byrjamin.wickedwizard.ecs.systems.level.ScreenWipeSystem;
import com.byrjamin.wickedwizard.factories.arenas.Arena;
import com.byrjamin.wickedwizard.factories.arenas.ArenaBuilder;
import com.byrjamin.wickedwizard.factories.arenas.PresetGames;
import com.byrjamin.wickedwizard.factories.arenas.challenges.ChallengesResource;
import com.byrjamin.wickedwizard.factories.arenas.decor.PortalFactory;
import com.byrjamin.wickedwizard.factories.arenas.skins.ArenaSkin;
import com.byrjamin.wickedwizard.factories.enemy.BlobFactory;
import com.byrjamin.wickedwizard.screens.DataSave;
import com.byrjamin.wickedwizard.screens.MenuScreen;
import com.byrjamin.wickedwizard.screens.PlayScreen;
import com.byrjamin.wickedwizard.screens.QuickSave;
import com.byrjamin.wickedwizard.utils.BagSearch;
import com.byrjamin.wickedwizard.utils.Measure;
import com.byrjamin.wickedwizard.utils.MapCoords;
import com.byrjamin.wickedwizard.utils.ComponentBag;
import com.byrjamin.wickedwizard.utils.collider.HitBox;

import static com.byrjamin.wickedwizard.ecs.components.texture.TextureRegionComponent.FOREGROUND_LAYER_NEAR;

/**
 * Created by Home on 01/04/2017.
 */

public class TutorialFactory extends com.byrjamin.wickedwizard.factories.arenas.decor.ArenaShellFactory {

    private final String moveTutorialString1 = "Exit this room on the right";
    private final String moveTutorialString2 = "Touch within this area of the screen to move";


    private static final String jumpTutorialString =
            "Tap ABOVE your character to jump and glide!";

    private static final String jumpTutorialStringDoubleTapBelow =
            "DOUBLE Tap BELOW your character to cancel a glide";

    private static final String jumpTutorialStringDoubleTapBelowRemove =
            "Tap the message box to make it go away";


    private static final String doubleJumpTutorialString =
            "Tap ABOVE your character and tap ABOVE AGAIN \n To DOUBLE Jump";

    private static final String enemyTutorialString =
            "Hark! A Worthy Foe! \n \n Touch down and hold within this area of the screen to shoot \n \n Drag to change aim";
    private static final String grappleTutorialString =
            "Tap grapple points to shoot your grappling hook \n \n (Grapple points are currently being highlighted)";
    private static final String endString =
            "Training Complete \n \n Enter the portal to begin";

    private static final String platformString =
            "Double Tap Below your character to \n Fall through platforms";

    public TutorialFactory(AssetManager assetManager, ArenaSkin arenaSkin) {
        super(assetManager, arenaSkin);
    }



    public ArenaMap tutorialMap(){
        Array<Arena> placedArenas = new Array<Arena>();

        Arena startingArena = groundMovementTutorial(new MapCoords(0,0));
        placedArenas.add(startingArena);
        placedArenas.add(jumpTutorial(new MapCoords(1, 0)));
        placedArenas.add(platformTutorial(new MapCoords(5,0)));
        placedArenas.add(grappleTutorial(new MapCoords(6,0)));
        placedArenas.add(enemyTurtorial(new MapCoords(6,3)));
        placedArenas.add(endTutorial(new MapCoords(7,3)));


        return new ArenaMap(startingArena, placedArenas);

    }


    public Arena groundMovementTutorial(MapCoords defaultCoords){

        Arena arena =  new ArenaBuilder(assetManager, arenaSkin)
                .addSection(new ArenaBuilder.Section(defaultCoords,
                        ArenaBuilder.wall.FULL,
                        ArenaBuilder.wall.DOOR,
                        ArenaBuilder.wall.FULL,
                        ArenaBuilder.wall.FULL)).buildArena();

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

        arena.addEntity(createTutorialHighlight(0,0, arena.getWidth(), WALLWIDTH * 2, new Color(Color.BLACK)));

        return arena;

    }


    public Arena platformTutorial(MapCoords defaultCoords){

        Arena arena =  new ArenaBuilder(assetManager, arenaSkin)
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

        Arena arena = new ArenaBuilder(assetManager, arenaSkin).addSection(
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

        float wall2PosX = Measure.units(290f);

        arena.addEntity(decorFactory.wallBag(wall2PosX,  Measure.units(10f), Measure.units(60f), WALLWIDTH * 6, arenaSkin));


        Bag<Component> jumpTutorialTextbag = new Bag<Component>();
        jumpTutorialTextbag.add(new PositionComponent(MainGame.GAME_WIDTH / 2, 800));
        TextureFontComponent jump = new TextureFontComponent(jumpTutorialString);
        jump.layer = TextureRegionComponent.BACKGROUND_LAYER_NEAR;
        jumpTutorialTextbag.add(jump);
        arena.addEntity(jumpTutorialTextbag);

        Bag<Component> doubleJumpTutorialTextbag = new Bag<Component>();
        doubleJumpTutorialTextbag.add(new PositionComponent(wall2PosX - Measure.units(5f), 1000));
        TextureFontComponent doubleJump = new TextureFontComponent(doubleJumpTutorialString);
        doubleJump.layer = TextureRegionComponent.BACKGROUND_LAYER_NEAR;
        doubleJumpTutorialTextbag.add(doubleJump);
        arena.addEntity(doubleJumpTutorialTextbag);




        ComponentBag messageBoxTrigger = new ComponentBag();
        messageBoxTrigger.add(new PositionComponent(Measure.units(55f), 0));

        Rectangle rectangle = new Rectangle(Measure.units(55f), 0, Measure.units(160f), Measure.units(70f));

        messageBoxTrigger.add(new CollisionBoundComponent(rectangle));
        messageBoxTrigger.add(new ParentComponent());
        messageBoxTrigger.add(new ProximityTriggerAIComponent(new Task() {
            @Override
            public void performAction(World world, Entity e) {

                ParentComponent parentComponent = e.getComponent(ParentComponent.class);

                world.getSystem(OnDeathSystem.class).killChildComponentsIgnoreOnDeath(parentComponent);

                Camera gamecam = world.getSystem(CameraSystem.class).getGamecam();


                FadeComponent fadeComponent = new FadeComponent(true, 1f, false);


                Entity text = world.createEntity();
                text.edit().add(new PositionComponent(gamecam.position.x, gamecam.position.y + Measure.units(19.5f)));
                text.edit().add(new FollowPositionComponent(gamecam.position, 0, Measure.units(19f)));
                TextureFontComponent textureFontComponent = new TextureFontComponent(Assets.small, jumpTutorialStringDoubleTapBelow);
                textureFontComponent.layer = TextureRegionComponent.FOREGROUND_LAYER_NEAR;
                text.edit().add(textureFontComponent);
                text.edit().add(fadeComponent);
                //text.edit().add(ec);
                text.edit().add(new ChildComponent(parentComponent));

                Entity blackBackingBox = world.createEntity();
                blackBackingBox.edit().add(new PositionComponent(gamecam.position.x - gamecam.viewportHeight / 2, gamecam.position.y + Measure.units(12.5f)));
                blackBackingBox.edit().add(new FollowPositionComponent(gamecam.position, - MainGame.GAME_WIDTH / 2, Measure.units(12.5f)));

                TextureRegionComponent trc = new TextureRegionComponent(atlas.findRegion(TextureStrings.BLOCK), 0,0,
                        MainGame.GAME_WIDTH,
                        Measure.units(10f),
                        TextureRegionComponent.FOREGROUND_LAYER_MIDDLE, new Color(0,0,0,0));
                blackBackingBox.edit().add(trc);
                //blackBackingBox.edit().add(ec);
                blackBackingBox.edit().add(fadeComponent);
                blackBackingBox.edit().add(new ChildComponent(parentComponent));


            }

            @Override
            public void cleanUpAction(World world, Entity e) {

                e.getComponent(ParentComponent.class);

                for(ChildComponent c : e.getComponent(ParentComponent.class).children){
                    Entity child = world.getSystem(FindChildSystem.class).findChildEntity(c);
                    if(child != null){
                        child.edit().remove(FadeComponent.class);
                        child.edit().add(new FadeComponent(false, 1f, false));
                        child.edit().add(new ExpireComponent(1f));
                    }
                }

                //world.getSystem(OnDeathSystem.class).killChildComponents(e.getComponent(ParentComponent.class));

            }
        }, new HitBox(rectangle)));

        arena.addEntity(messageBoxTrigger);

        return arena;

    }




    public Arena enemyTurtorial(MapCoords defaultCoords){

        Arena arena =  new ArenaBuilder(assetManager, arenaSkin).addSection(
                new ArenaBuilder.Section(defaultCoords,
                        ArenaBuilder.wall.FULL,
                        ArenaBuilder.wall.DOOR,
                        ArenaBuilder.wall.FULL,
                        ArenaBuilder.wall.DOOR)).buildArena();

        Bag<Component> bag = new Bag<Component>();
        bag.add(new PositionComponent(MainGame.GAME_WIDTH / 2, 1000));
        bag.add(new TextureFontComponent(enemyTutorialString));
        arena.addEntity(bag);

        float HEIGHT = arena.getHeight();
        float WIDTH = arena.getWidth();

        arena.addEntity(createTutorialHighlight(0, WALLWIDTH * 2, WALLWIDTH / 2, HEIGHT));
        arena.addEntity(createTutorialHighlight(0, WALLWIDTH * 2, WIDTH, WALLWIDTH / 2));
        arena.addEntity(createTutorialHighlight(WIDTH - WALLWIDTH / 2, WALLWIDTH * 2, WALLWIDTH / 2, HEIGHT));
        arena.addEntity(createTutorialHighlight(0, HEIGHT - WALLWIDTH / 2, WIDTH, WALLWIDTH / 2));

        bag = new BlobFactory(assetManager).blob(arena.getWidth() - Measure.units(12), Measure.units(30f),1, 0, 8, true, ColorResource.BLOB_GREEN);
        arena.addEntity(bag);


        for(int i = 0; i < 3; i ++)  {

            ComponentBag chevron = decorFactory.chevronBag(Measure.units(15f + (i * 30)), Measure.units(15f), -90);
            BagSearch.getObjectOfTypeClass(TextureRegionComponent.class, chevron).color.a = 0;
            chevron.add(new InCombatActionComponent(new Task() {
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


        return arena;

    }



    public Arena grappleTutorial(MapCoords defaultCoords){

        Arena arena =  new ArenaBuilder(assetManager, arenaSkin)
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

        Arena arena =  new ArenaBuilder(assetManager, arenaSkin)
                .addSection(new ArenaBuilder.Section(defaultCoords,
                        ArenaBuilder.wall.DOOR,
                        ArenaBuilder.wall.FULL,
                        ArenaBuilder.wall.FULL,
                        ArenaBuilder.wall.FULL)).buildArena();

        Bag<Component> bag = new Bag<Component>();

        bag.add(new PositionComponent(MainGame.GAME_WIDTH / 2, Measure.units(47.5f)));
        bag.add(new TextureFontComponent(endString));
        arena.addEntity(bag);



        arena.addEntity(new PortalFactory(assetManager).customSmallPortal(arena.getWidth() / 2, Measure.units(25f),

                new Action() {
                    @Override
                    public void performAction(World world, Entity e) {

                        world.getSystem(ScreenWipeSystem.class).startScreenWipe(ScreenWipeSystem.Transition.FADE, new Action() {
                            @Override
                            public void performAction(World world, Entity e) {


                                MainGame game = world.getSystem(EndGameSystem.class).getGame();

                                if(DataSave.isDataAvailable(ChallengesResource.TUTORIAL_COMPLETE)){

                                    game.getScreen().dispose();
                                    game.setScreen(new MenuScreen(game));

                                } else {
                                    
                                    DataSave.saveChallengeData(ChallengesResource.TUTORIAL_COMPLETE);
                                    game.getScreen().dispose();
                                    game.setScreen(new PlayScreen(game));

                                }

                            }
                        });
                    }
                }));

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
