package com.bryjamin.wickedwizard.screens.world.menu;

import com.artemis.Entity;
import com.artemis.World;
import com.artemis.WorldConfiguration;
import com.artemis.WorldConfigurationBuilder;
import com.artemis.utils.Bag;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.bryjamin.wickedwizard.MainGame;
import com.bryjamin.wickedwizard.assets.FileLocationStrings;
import com.bryjamin.wickedwizard.assets.FontAssets;
import com.bryjamin.wickedwizard.assets.MenuStrings;
import com.bryjamin.wickedwizard.assets.TextureStrings;
import com.bryjamin.wickedwizard.ecs.components.ai.Action;
import com.bryjamin.wickedwizard.ecs.components.ai.ActionOnTouchComponent;
import com.bryjamin.wickedwizard.ecs.components.movement.CollisionBoundComponent;
import com.bryjamin.wickedwizard.ecs.components.movement.PositionComponent;
import com.bryjamin.wickedwizard.ecs.components.texture.TextureFontComponent;
import com.bryjamin.wickedwizard.ecs.components.texture.TextureRegionComponent;
import com.bryjamin.wickedwizard.ecs.systems.graphical.AnimationSystem;
import com.bryjamin.wickedwizard.ecs.systems.graphical.BoundsDrawingSystem;
import com.bryjamin.wickedwizard.ecs.systems.graphical.FadeSystem;
import com.bryjamin.wickedwizard.factories.arenas.GameCreator;
import com.bryjamin.wickedwizard.factories.arenas.challenges.ChallengeLayout;
import com.bryjamin.wickedwizard.factories.arenas.challenges.ChallengesResource;
import com.bryjamin.wickedwizard.factories.arenas.challenges.maps.ChallengeMaps;
import com.bryjamin.wickedwizard.screens.DataSave;
import com.bryjamin.wickedwizard.screens.MenuButton;
import com.bryjamin.wickedwizard.screens.MenuScreen;
import com.bryjamin.wickedwizard.screens.PlayScreen;
import com.bryjamin.wickedwizard.utils.BagToEntity;
import com.bryjamin.wickedwizard.utils.CenterMath;
import com.bryjamin.wickedwizard.utils.ComponentBag;
import com.bryjamin.wickedwizard.utils.GameDelta;
import com.bryjamin.wickedwizard.utils.Measure;

/**
 * Created by BB on 20/08/2017.
 */

public class ChallengesWorldContainer extends com.bryjamin.wickedwizard.utils.AbstractGestureDectector implements com.bryjamin.wickedwizard.screens.world.WorldContainer {

    private final MainGame game;
    private final Viewport gameport;
    private final TextureAtlas atlas;


    private static final float buttonWidth = Measure.units(7.5f);
    private static final float buttonHeight = Measure.units(7.5f);
    private static final float buttonGap = Measure.units(2.5f);

    private static final Color buttonForeground = new Color(Color.BLACK);
    private static final Color buttonBackground = new Color(Color.WHITE);

    private static final int maxColumns = 9;

    private static final float startY = Measure.units(40f);
    private static final float startX = CenterMath.offsetX(MainGame.GAME_WIDTH, (buttonWidth * maxColumns) + (buttonGap * (maxColumns - 1)));

    private Array<Bag<ComponentBag>> challengeComponentBagArray = new Array<Bag<ComponentBag>>();
    private static final float arrowSize = Measure.units(15f);
    private Bag<Entity> currentlyShownChallenge;
    private static int currentlyShownIndex = 0;


    private ChallengeMaps challengeMaps;


    private World world;

    public ChallengesWorldContainer(MainGame game, Viewport viewport){
        this.game = game;
        this.gameport = viewport;
        this.atlas = game.assetManager.get(FileLocationStrings.spriteAtlas);
        this.challengeMaps = new ChallengeMaps(game.assetManager, MathUtils.random);
        createWorld();
    }



    static {


        Array<ChallengeLayout> challengeLayoutArray = ChallengesResource.getAllChallenges();

        for(int i = 0; i < challengeLayoutArray.size; i++){

            boolean challengeUnlocked = DataSave.isDataAvailable(challengeLayoutArray.get(i).getStringToUnlockChallenge());

            if(!challengeUnlocked){
                break;
            }

            if(!DataSave.isDataAvailable(challengeLayoutArray.get(i).getChallengeID())){
                currentlyShownIndex = i;
                break;
            } else {
                currentlyShownIndex = i;
            }

        }






    }



    public void createWorld() {

        WorldConfiguration config = new WorldConfigurationBuilder()
                .with(WorldConfigurationBuilder.Priority.HIGHEST,
                        new com.bryjamin.wickedwizard.ecs.systems.physics.MovementSystem()
                )
                .with(WorldConfigurationBuilder.Priority.HIGH,
                        new AnimationSystem(),
                        //new FindPlayerSystem(player),
                        new com.bryjamin.wickedwizard.ecs.systems.input.ActionOnTouchSystem(),
                        new FadeSystem())
                .with(WorldConfigurationBuilder.Priority.LOW,
                        new com.bryjamin.wickedwizard.ecs.systems.graphical.RenderingSystem(game.batch, game.assetManager, gameport),
                        new BoundsDrawingSystem()
                )
                .build();


        world = new World(config);


        setUpTitle(world, MenuStrings.TRAILS, MenuStrings.TAP_A_CHALLENGE);


        Entity backToMainMenu = new MenuButton.MenuButtonBuilder(FontAssets.medium, atlas.findRegion(TextureStrings.BLOCK))
                .width(Measure.units(30f))
                .height(Measure.units(10f))
                .foregroundColor(new Color(Color.BLACK))
                .backgroundColor(new Color(Color.WHITE))
                .action(new Action() {
                    @Override
                    public void performAction(World world, Entity e) {
                        MenuScreen.goBack();
                    }
                })
                .build()
                .createButton(
                        world,
                        MenuStrings.BACK,
                        MainGame.GAME_WIDTH - Measure.units(30f) - Measure.units(5f)
                        , Measure.units(5f));


        int count = 0;

        createChallenges(world, ChallengesResource.getAllChallenges());

        float arrowY = Measure.units(45f);

        arrowEntity(world, Measure.units(5f), arrowY, true);
        arrowEntity(world, gameport.getCamera().viewportWidth - arrowSize - Measure.units(5f), arrowY, false);

    }




    public void createChallenges(World world, Array<ChallengeLayout> challengeLayouts){

        MenuButton.MenuButtonBuilder challengeButtonBuilder = new com.bryjamin.wickedwizard.screens.MenuButton.MenuButtonBuilder(com.bryjamin.wickedwizard.assets.FontAssets.small, atlas.findRegion(TextureStrings.BLOCK))
                .width(gameport.getCamera().viewportWidth)
                .height(buttonHeight)
                .foregroundColor(buttonForeground)
                .backgroundColor(buttonBackground);

        for(int i = 0; i < challengeLayouts.size; i++){

            Bag<ComponentBag> bagArray = new Bag<ComponentBag>();

            float x = 0;
            float y = Measure.units(32.5f);

            final String s = challengeLayouts.get(i).getChallengeID();

            boolean challengeComplete = DataSave.isDataAvailable(s);

            if(DataSave.isDataAvailable(challengeLayouts.get(i).getStringToUnlockChallenge())) {

                bagArray.addAll(challengeButtonBuilder
                        .foregroundColor(new Color(Color.BLACK))
                        .backgroundColor(new Color(Color.WHITE))
                        .action(new Action() {
                            @Override
                            public void performAction(World world, Entity e) {

                                try {

                                    GameCreator gameCreator = challengeMaps.getChallenge(s);
                                    if(gameCreator == null) throw new Exception("Challenge with id: " + s + " does not exist in the Challenge Maps Array");
                                    game.getScreen().dispose();
                                    game.setScreen(new PlayScreen(game, gameCreator));
                                } catch (Exception exception){

                                    exception.printStackTrace();

                                }
                            }
                        })
                        .build()
                        .createButton(MenuStrings.CHALLENGE + " " + (i + 1), x, y));


                bagArray.addAll(challengeButtonBuilder.build().createButton(challengeLayouts.get(i).getName(), x, y - Measure.units(5f)));


                if(challengeComplete){

                    ComponentBag completed = new ComponentBag();
                    completed.add(new PositionComponent(x, y - Measure.units(10f)));
                    completed.add(new CollisionBoundComponent(new Rectangle(x, y, gameport.getCamera().viewportWidth, Measure.units(5f))));
                    completed.add(new TextureFontComponent(FontAssets.small, MenuStrings.COMPLETED, new Color(Color.WHITE)));

                    bagArray.add(completed);

                    completed = new ComponentBag();
                    completed.add(new PositionComponent(x, y - Measure.units(10f)));
                    completed.add(new CollisionBoundComponent(new Rectangle(x, y, gameport.getCamera().viewportWidth, Measure.units(5f))));
                    completed.add(new TextureRegionComponent(atlas.findRegion(TextureStrings.BLOCK),gameport.getCamera().viewportWidth, Measure.units(5f), TextureRegionComponent.BACKGROUND_LAYER_FAR, new Color(Color.BLACK)));

                    bagArray.add(completed);

                }



            } else {
                nonInteractiveButton(bagArray, x, y, buttonHeight, MenuStrings.CHALLENGE + " " + (i + 1), new Color(Color.WHITE), new Color(Color.BLACK));
                nonInteractiveButton(bagArray, x, y - Measure.units(5), buttonHeight, "LOCKED", new Color(Color.WHITE), new Color(Color.BLACK));
            }

            challengeComponentBagArray.add(bagArray);

        }


        try {
            currentlyShownChallenge = BagToEntity.bagsToEntities(world, challengeComponentBagArray.get(currentlyShownIndex));
        } catch (Exception e){
            currentlyShownChallenge = BagToEntity.bagsToEntities(world, challengeComponentBagArray.first());
        }


    }


    public void nonInteractiveButton(Bag<ComponentBag> bagArray, float x, float y, float height, String text, Color foreground, Color background){


        ComponentBag textBag = new ComponentBag();
        textBag.add(new PositionComponent(x, y));
        textBag.add(new CollisionBoundComponent(new Rectangle(x, y, gameport.getCamera().viewportWidth, height)));
        textBag.add(new TextureFontComponent(FontAssets.small, text, foreground));

        bagArray.add(textBag);

        ComponentBag backing = new ComponentBag();
        backing.add(new PositionComponent(x, y));
        backing.add(new CollisionBoundComponent(new Rectangle(x, y, gameport.getCamera().viewportWidth, height)));
        backing.add(new TextureRegionComponent(atlas.findRegion(TextureStrings.BLOCK),gameport.getCamera().viewportWidth, height,
                TextureRegionComponent.BACKGROUND_LAYER_FAR,
                background));

        bagArray.add(backing);

    }




    public void setUpTitle(World world, String title, String heading){

        float titleY = Measure.units(50f);

        Entity e = world.createEntity();
        e.edit().add(new PositionComponent(0, titleY));
        e.edit().add(new CollisionBoundComponent(new Rectangle(0, titleY, gameport.getCamera().viewportWidth, buttonHeight)));
        e.edit().add(new TextureFontComponent(FontAssets.medium, title, new Color(Color.BLACK)));


        e = world.createEntity();
        e.edit().add(new PositionComponent(0, titleY - Measure.units(5f)));
        e.edit().add(new CollisionBoundComponent(new Rectangle(0, titleY, gameport.getCamera().viewportWidth, buttonHeight)));
        e.edit().add(new TextureFontComponent(FontAssets.small, heading, new Color(Color.BLACK)));

    }




    private Entity arrowEntity(World world, float x, float y, final boolean isLeft){

        Entity arrow = world.createEntity();

        arrow.edit().add(new PositionComponent(x, y));

        TextureRegionComponent textureRegionComponent = new TextureRegionComponent(atlas.findRegion(TextureStrings.ICON_ARROW),
                arrowSize, arrowSize, TextureRegionComponent.FOREGROUND_LAYER_MIDDLE);

        textureRegionComponent.rotation = isLeft ? 90 : 270;

        arrow.edit().add(textureRegionComponent);

        arrow.edit().add(new CollisionBoundComponent(new Rectangle(x, y, arrowSize, arrowSize)));
        arrow.edit().add(new ActionOnTouchComponent(new Action() {
            @Override
            public void performAction(World world, Entity e) {
                for(Entity entity : currentlyShownChallenge){
                    entity.deleteFromWorld();
                }

                currentlyShownIndex = isLeft ? currentlyShownIndex - 1 : currentlyShownIndex + 1;

                if(currentlyShownIndex >= challengeComponentBagArray.size){
                    currentlyShownIndex = 0;
                } else if(currentlyShownIndex < 0) {
                    currentlyShownIndex = challengeComponentBagArray.size - 1;
                }

                currentlyShownChallenge = BagToEntity.bagsToEntities(world, challengeComponentBagArray.get(currentlyShownIndex));

            }
        }));


        return arrow;

    }





    @Override
    public void process(float delta) {
        GameDelta.delta(world, delta);
    }

    @Override
    public World getWorld() {
        return world;
    }


    @Override
    public boolean tap(float x, float y, int count, int button) {
        Vector3 touchInput = gameport.unproject(new Vector3(x, y, 0));
        return world.getSystem(com.bryjamin.wickedwizard.ecs.systems.input.ActionOnTouchSystem.class).touch(touchInput.x, touchInput.y);
    }
}
