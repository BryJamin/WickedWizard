package com.bryjamin.wickedwizard.factories.arenas.levels;

import com.artemis.Component;
import com.artemis.Entity;
import com.artemis.World;
import com.artemis.utils.Bag;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.bryjamin.wickedwizard.MainGame;
import com.bryjamin.wickedwizard.assets.ColorResource;
import com.bryjamin.wickedwizard.ecs.components.ai.Action;
import com.bryjamin.wickedwizard.ecs.components.ai.ExpireComponent;
import com.bryjamin.wickedwizard.ecs.components.ai.ProximityTriggerAIComponent;
import com.bryjamin.wickedwizard.ecs.components.movement.CollisionBoundComponent;
import com.bryjamin.wickedwizard.ecs.components.movement.PositionComponent;
import com.bryjamin.wickedwizard.ecs.components.texture.FadeComponent;
import com.bryjamin.wickedwizard.ecs.components.texture.TextureFontComponent;
import com.bryjamin.wickedwizard.ecs.components.texture.TextureRegionComponent;
import com.bryjamin.wickedwizard.ecs.systems.graphical.CameraSystem;
import com.bryjamin.wickedwizard.ecs.systems.level.ArenaMap;
import com.bryjamin.wickedwizard.ecs.systems.level.EndGameSystem;
import com.bryjamin.wickedwizard.ecs.systems.level.ScreenWipeSystem;
import com.bryjamin.wickedwizard.factories.arenas.Arena;
import com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder;
import com.bryjamin.wickedwizard.factories.arenas.decor.ArenaShellFactory;
import com.bryjamin.wickedwizard.factories.arenas.decor.OnLoadFactory;
import com.bryjamin.wickedwizard.factories.arenas.skins.ArenaSkin;
import com.bryjamin.wickedwizard.factories.enemy.BlobFactory;
import com.bryjamin.wickedwizard.screens.DataSave;
import com.bryjamin.wickedwizard.utils.Measure;

import static com.bryjamin.wickedwizard.ecs.components.texture.TextureRegionComponent.BACKGROUND_LAYER_MIDDLE;
import static com.bryjamin.wickedwizard.ecs.components.texture.TextureRegionComponent.FOREGROUND_LAYER_NEAR;

/**
 * Created by Home on 01/04/2017.
 */

public class TutorialFactory extends ArenaShellFactory {

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

        Arena startingArena = groundMovementTutorial(new com.bryjamin.wickedwizard.utils.MapCoords(0,0));


        startingArena.addEntity(new OnLoadFactory().startMusicEntity(com.bryjamin.wickedwizard.utils.enums.Level.ONE.getMusic()));

        placedArenas.add(startingArena);
        placedArenas.add(jumpTutorial(new com.bryjamin.wickedwizard.utils.MapCoords(1, 0)));
        placedArenas.add(platformTutorial(new com.bryjamin.wickedwizard.utils.MapCoords(5,0)));
        placedArenas.add(grappleTutorial(new com.bryjamin.wickedwizard.utils.MapCoords(6,0)));
        placedArenas.add(enemyTurtorial(new com.bryjamin.wickedwizard.utils.MapCoords(6,3)));
        placedArenas.add(endTutorial(new com.bryjamin.wickedwizard.utils.MapCoords(7,3)));


        return new ArenaMap(startingArena, placedArenas);

    }


    public Arena groundMovementTutorial(com.bryjamin.wickedwizard.utils.MapCoords defaultCoords){

        Arena arena =  new ArenaBuilder(assetManager, arenaSkin, Arena.ArenaType.NORMAL)
                .addSection(new ArenaBuilder.Section(defaultCoords,
                        ArenaBuilder.wall.FULL,
                        ArenaBuilder.wall.DOOR,
                        ArenaBuilder.wall.FULL,
                        ArenaBuilder.wall.FULL)).buildArena();

        Bag<Component> textBag = new Bag<Component>();
        textBag.add(new PositionComponent(MainGame.GAME_WIDTH / 2, 800));
        TextureFontComponent text = new TextureFontComponent(moveTutorialString1, arenaSkin.getWallTint());
        text.layer = BACKGROUND_LAYER_MIDDLE;
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


    public Arena platformTutorial(com.bryjamin.wickedwizard.utils.MapCoords defaultCoords){

        Arena arena =  new ArenaBuilder(assetManager, arenaSkin, Arena.ArenaType.NORMAL)
                .addSection(new ArenaBuilder.Section(defaultCoords,
                        ArenaBuilder.wall.DOOR,
                        ArenaBuilder.wall.DOOR,
                        ArenaBuilder.wall.NONE,
                        ArenaBuilder.wall.FULL))
                .addSection(new ArenaBuilder.Section(new com.bryjamin.wickedwizard.utils.MapCoords(defaultCoords.getX(), defaultCoords.getY() + 1),
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

        TextureFontComponent tfc = new TextureFontComponent(platformString, arenaSkin.getWallTint());
        tfc.layer = BACKGROUND_LAYER_MIDDLE;
        com.bryjamin.wickedwizard.utils.ComponentBag bag = new com.bryjamin.wickedwizard.utils.ComponentBag();
        bag.add(new PositionComponent(MainGame.GAME_WIDTH / 2, 1500));
        bag.add(tfc);

        arena.addEntity(bag);

        return arena;

    }


    public Arena jumpTutorial(com.bryjamin.wickedwizard.utils.MapCoords defaultCoords){

        Arena arena = new ArenaBuilder(assetManager, arenaSkin, Arena.ArenaType.NORMAL).addSection(
                new ArenaBuilder.Section(defaultCoords,
                        ArenaBuilder.wall.DOOR,
                        ArenaBuilder.wall.NONE,
                        ArenaBuilder.wall.FULL,
                        ArenaBuilder.wall.FULL))
                .addSection(new ArenaBuilder.Section(new com.bryjamin.wickedwizard.utils.MapCoords(defaultCoords.getX() + 1, defaultCoords.getY()),
                                ArenaBuilder.wall.NONE,
                                ArenaBuilder.wall.NONE,
                                ArenaBuilder.wall.FULL,
                                ArenaBuilder.wall.FULL))
                .addSection(new ArenaBuilder.Section(new com.bryjamin.wickedwizard.utils.MapCoords(defaultCoords.getX() + 2, defaultCoords.getY()),
                        ArenaBuilder.wall.NONE,
                        ArenaBuilder.wall.NONE,
                        ArenaBuilder.wall.FULL,
                        ArenaBuilder.wall.FULL))
                .addSection(new ArenaBuilder.Section(new com.bryjamin.wickedwizard.utils.MapCoords(defaultCoords.getX() + 3, defaultCoords.getY()),
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
        TextureFontComponent jump = new TextureFontComponent(jumpTutorialString, arenaSkin.getWallTint());
        jump.layer = TextureRegionComponent.BACKGROUND_LAYER_NEAR;
        jumpTutorialTextbag.add(jump);
        arena.addEntity(jumpTutorialTextbag);

        Bag<Component> doubleJumpTutorialTextbag = new Bag<Component>();
        doubleJumpTutorialTextbag.add(new PositionComponent(wall2PosX - Measure.units(5f), 1000));
        TextureFontComponent doubleJump = new TextureFontComponent(doubleJumpTutorialString, arenaSkin.getWallTint());
        doubleJump.layer = TextureRegionComponent.BACKGROUND_LAYER_NEAR;
        doubleJumpTutorialTextbag.add(doubleJump);
        arena.addEntity(doubleJumpTutorialTextbag);




        com.bryjamin.wickedwizard.utils.ComponentBag messageBoxTrigger = new com.bryjamin.wickedwizard.utils.ComponentBag();
        messageBoxTrigger.add(new PositionComponent(Measure.units(55f), 0));

        Rectangle rectangle = new Rectangle(Measure.units(55f), 0, Measure.units(160f), Measure.units(70f));

        messageBoxTrigger.add(new CollisionBoundComponent(rectangle));
        messageBoxTrigger.add(new com.bryjamin.wickedwizard.ecs.components.identifiers.ParentComponent());
        messageBoxTrigger.add(new ProximityTriggerAIComponent(new com.bryjamin.wickedwizard.ecs.components.ai.Task() {
            @Override
            public void performAction(World world, Entity e) {

                com.bryjamin.wickedwizard.ecs.components.identifiers.ParentComponent parentComponent = e.getComponent(com.bryjamin.wickedwizard.ecs.components.identifiers.ParentComponent.class);

                world.getSystem(com.bryjamin.wickedwizard.ecs.systems.ai.OnDeathSystem.class).killChildComponentsIgnoreOnDeath(parentComponent);

                Camera gamecam = world.getSystem(CameraSystem.class).getGamecam();


                FadeComponent fadeComponent = new FadeComponent(true, 1f, false);


                //MESSAGE BOX

                Entity text = world.createEntity();
                text.edit().add(new PositionComponent(gamecam.position.x, gamecam.position.y + Measure.units(19.5f)));
                text.edit().add(new com.bryjamin.wickedwizard.ecs.components.ai.FollowPositionComponent(gamecam.position, 0, Measure.units(19f)));
                TextureFontComponent textureFontComponent = new TextureFontComponent(com.bryjamin.wickedwizard.assets.FontAssets.small, jumpTutorialStringDoubleTapBelow);
                textureFontComponent.layer = TextureRegionComponent.FOREGROUND_LAYER_NEAR;
                text.edit().add(textureFontComponent);
                text.edit().add(fadeComponent);
                //text.edit().add(ec);
                text.edit().add(new com.bryjamin.wickedwizard.ecs.components.identifiers.ChildComponent(parentComponent));

                Entity blackBackingBox = world.createEntity();
                blackBackingBox.edit().add(new PositionComponent(gamecam.position.x - gamecam.viewportHeight / 2, gamecam.position.y + Measure.units(12.5f)));
                blackBackingBox.edit().add(new com.bryjamin.wickedwizard.ecs.components.ai.FollowPositionComponent(gamecam.position, - MainGame.GAME_WIDTH / 2, Measure.units(12.5f)));

                TextureRegionComponent trc = new TextureRegionComponent(atlas.findRegion(com.bryjamin.wickedwizard.assets.TextureStrings.BLOCK), 0,0,
                        MainGame.GAME_WIDTH,
                        Measure.units(10f),
                        TextureRegionComponent.FOREGROUND_LAYER_MIDDLE, new Color(0,0,0,0));
                blackBackingBox.edit().add(trc);
                //blackBackingBox.edit().add(ec);
                blackBackingBox.edit().add(fadeComponent);
                blackBackingBox.edit().add(new com.bryjamin.wickedwizard.ecs.components.identifiers.ChildComponent(parentComponent));


            }

            @Override
            public void cleanUpAction(World world, Entity e) {

                e.getComponent(com.bryjamin.wickedwizard.ecs.components.identifiers.ParentComponent.class);

                for(com.bryjamin.wickedwizard.ecs.components.identifiers.ChildComponent c : e.getComponent(com.bryjamin.wickedwizard.ecs.components.identifiers.ParentComponent.class).children){
                    Entity child = world.getSystem(com.bryjamin.wickedwizard.ecs.systems.FindChildSystem.class).findChildEntity(c);
                    if(child != null){
                        child.edit().remove(FadeComponent.class);
                        child.edit().add(new FadeComponent(false, 1f, false));
                        child.edit().add(new ExpireComponent(1f));
                    }
                }

                //world.getSystem(OnDeathSystem.class).killChildComponents(e.getComponent(ParentComponent.class));

            }
        }, new com.bryjamin.wickedwizard.utils.collider.HitBox(rectangle)));

        arena.addEntity(messageBoxTrigger);

        return arena;

    }




    public Arena enemyTurtorial(com.bryjamin.wickedwizard.utils.MapCoords defaultCoords){

        Arena arena =  new ArenaBuilder(assetManager, arenaSkin, Arena.ArenaType.TRAP).addSection(
                new ArenaBuilder.Section(defaultCoords,
                        ArenaBuilder.wall.FULL,
                        ArenaBuilder.wall.DOOR,
                        ArenaBuilder.wall.FULL,
                        ArenaBuilder.wall.DOOR)).buildArena();

        Bag<Component> bag = new Bag<Component>();
        bag.add(new PositionComponent(MainGame.GAME_WIDTH / 2, 1000));

        TextureFontComponent text = new TextureFontComponent(enemyTutorialString, arenaSkin.getWallTint());
        text.layer = BACKGROUND_LAYER_MIDDLE;
        bag.add(text);

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

            com.bryjamin.wickedwizard.utils.ComponentBag chevron = decorFactory.chevronBag(Measure.units(15f + (i * 30)), Measure.units(15f), -90);
            com.bryjamin.wickedwizard.utils.BagSearch.getObjectOfTypeClass(TextureRegionComponent.class, chevron).color.a = 0;
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


        return arena;

    }



    public Arena grappleTutorial(com.bryjamin.wickedwizard.utils.MapCoords defaultCoords){

        Arena arena =  new ArenaBuilder(assetManager, arenaSkin, Arena.ArenaType.NORMAL)
                .addSection(new ArenaBuilder.Section(defaultCoords,
                        ArenaBuilder.wall.DOOR,
                        ArenaBuilder.wall.FULL,
                        ArenaBuilder.wall.NONE,
                        ArenaBuilder.wall.FULL))
                .addSection(new ArenaBuilder.Section(new com.bryjamin.wickedwizard.utils.MapCoords(defaultCoords.getX(), defaultCoords.getY() +  1),
                        ArenaBuilder.wall.FULL,
                        ArenaBuilder.wall.FULL,
                        ArenaBuilder.wall.NONE,
                        ArenaBuilder.wall.NONE))
                .addSection(new ArenaBuilder.Section(new com.bryjamin.wickedwizard.utils.MapCoords(defaultCoords.getX(), defaultCoords.getY() +  2),
                        ArenaBuilder.wall.FULL,
                        ArenaBuilder.wall.FULL,
                        ArenaBuilder.wall.DOOR,
                        ArenaBuilder.wall.NONE)).buildArena();

        //TODO find less lazy way to add the highlight to the arena
        Bag<Component> bag;

        arena.addEntity(decorFactory.platform(0, arena.getHeight() - Measure.units(35), arena.getWidth()));

        bag = new Bag<Component>();
        bag.add(new PositionComponent(MainGame.GAME_WIDTH / 2, 800));

        TextureFontComponent text = new TextureFontComponent(grappleTutorialString, arenaSkin.getWallTint());
        text.layer = BACKGROUND_LAYER_MIDDLE;
        bag.add(text);


        arena.addEntity(bag);


        for(int i = 0; i < 5; i ++) {
            bag = decorFactory.grapplePointBag(arena.getWidth() / 2, Measure.units(50 + (i * 30)));
            arena.addEntity(bag);
            arena.addEntity(createTutorialHighlight(com.bryjamin.wickedwizard.utils.BagSearch.getObjectOfTypeClass(CollisionBoundComponent.class, bag).bound));
        }

        return arena;

    }


    public Arena endTutorial(com.bryjamin.wickedwizard.utils.MapCoords defaultCoords){

        Arena arena =  new ArenaBuilder(assetManager, arenaSkin, Arena.ArenaType.NORMAL)
                .addSection(new ArenaBuilder.Section(defaultCoords,
                        ArenaBuilder.wall.DOOR,
                        ArenaBuilder.wall.FULL,
                        ArenaBuilder.wall.FULL,
                        ArenaBuilder.wall.FULL)).buildArena();

        Bag<Component> bag = new Bag<Component>();

        bag.add(new PositionComponent(MainGame.GAME_WIDTH / 2, Measure.units(47.5f)));

        TextureFontComponent text = new TextureFontComponent(endString, arenaSkin.getWallTint());
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


                                MainGame game = world.getSystem(EndGameSystem.class).getGame();

                                if(DataSave.isDataAvailable(com.bryjamin.wickedwizard.factories.arenas.challenges.ChallengesResource.TUTORIAL_COMPLETE)){

                                    game.getScreen().dispose();
                                    game.setScreen(new com.bryjamin.wickedwizard.screens.MenuScreen(game));

                                } else {

                                    DataSave.saveChallengeData(com.bryjamin.wickedwizard.factories.arenas.challenges.ChallengesResource.TUTORIAL_COMPLETE);
                                    Screen s = game.getScreen();
                                    game.setScreen(new com.bryjamin.wickedwizard.screens.PlayScreen(game));
                                    s.dispose();
                                }

                            }
                        });
                    }
                }));

        return arena;


    }

    public com.bryjamin.wickedwizard.utils.ComponentBag createTutorialHighlight(Rectangle r) {
        return createTutorialHighlight(r.x, r.y, r.width, r.height);
    }

    public com.bryjamin.wickedwizard.utils.ComponentBag createTutorialHighlight(float x, float y, float width, float height) {
        com.bryjamin.wickedwizard.utils.ComponentBag bag = createTutorialHighlight(x, y, width, height, new Color(Color.WHITE));
        return bag;
    }

    public com.bryjamin.wickedwizard.utils.ComponentBag createTutorialHighlight(float x, float y, float width, float height, Color c) {
        com.bryjamin.wickedwizard.utils.ComponentBag bag = new com.bryjamin.wickedwizard.utils.ComponentBag();
        bag.add(new PositionComponent(x, y));
        TextureRegionComponent trc = new TextureRegionComponent(atlas.findRegion(com.bryjamin.wickedwizard.assets.TextureStrings.BLOCK), width, height, TextureRegionComponent.FOREGROUND_LAYER_FAR, c);
        bag.add(trc);
        bag.add(new FadeComponent());
        return bag;
    }





}
