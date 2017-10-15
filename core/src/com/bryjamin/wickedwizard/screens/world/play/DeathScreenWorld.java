package com.bryjamin.wickedwizard.screens.world.play;

import com.artemis.Entity;
import com.artemis.World;
import com.artemis.WorldConfiguration;
import com.artemis.WorldConfigurationBuilder;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.bryjamin.wickedwizard.MainGame;
import com.bryjamin.wickedwizard.assets.FileLocationStrings;
import com.bryjamin.wickedwizard.assets.FontAssets;
import com.bryjamin.wickedwizard.assets.MenuStrings;
import com.bryjamin.wickedwizard.assets.TextureStrings;
import com.bryjamin.wickedwizard.ecs.components.ai.Action;
import com.bryjamin.wickedwizard.ecs.components.ai.ActionAfterTimeComponent;
import com.bryjamin.wickedwizard.ecs.components.ai.ActionOnTouchComponent;
import com.bryjamin.wickedwizard.ecs.components.ai.FollowCameraComponent;
import com.bryjamin.wickedwizard.ecs.components.movement.CollisionBoundComponent;
import com.bryjamin.wickedwizard.ecs.components.movement.PositionComponent;
import com.bryjamin.wickedwizard.ecs.components.texture.FadeComponent;
import com.bryjamin.wickedwizard.ecs.components.texture.TextureFontComponent;
import com.bryjamin.wickedwizard.ecs.components.texture.TextureRegionComponent;
import com.bryjamin.wickedwizard.ecs.systems.ai.ActionAfterTimeSystem;
import com.bryjamin.wickedwizard.ecs.systems.ai.FollowCameraSystem;
import com.bryjamin.wickedwizard.ecs.systems.graphical.AnimationSystem;
import com.bryjamin.wickedwizard.ecs.systems.graphical.BoundsDrawingSystem;
import com.bryjamin.wickedwizard.ecs.systems.graphical.FadeSystem;
import com.bryjamin.wickedwizard.ecs.systems.graphical.RenderingSystem;
import com.bryjamin.wickedwizard.ecs.systems.input.ActionOnTouchSystem;
import com.bryjamin.wickedwizard.ecs.systems.physics.MovementSystem;
import com.bryjamin.wickedwizard.factories.arenas.GameCreator;
import com.bryjamin.wickedwizard.factories.items.Item;
import com.bryjamin.wickedwizard.screens.world.AdventureWorld;
import com.bryjamin.wickedwizard.utils.CenterMath;
import com.bryjamin.wickedwizard.utils.GameDelta;
import com.bryjamin.wickedwizard.utils.Measure;
import com.bryjamin.wickedwizard.utils.TableMath;

/**
 * Created by Home on 26/07/2017.
 */

public class DeathScreenWorld {


    private Viewport gameport;

    private GameCreator gameCreator;

    private AdventureWorld adventureWorld;

    private TextureAtlas atlas;

    private MainGame game;

    private World world;

    private static final float buttonWidth = Measure.units(50f);
    private static final float buttonHeight = Measure.units(15f);

    private static final float itemIconSize = Measure.units(5f);
    private static final float itemIconGap = Measure.units(2.5f);

    private static final float adventureTitleY = Measure.units(55f);
    private static final float tapToRestartY = Measure.units(2.5f);


    private static final float youAreHereIconsStartY = Measure.units(47.5f);

    private static final float squareSize = Measure.units(5f);
    private static final float squareGap = Measure.units(0.5f);


    private static final float itemTitleY = Measure.units(30f);
    private static final float itemIconsStartY = itemTitleY - Measure.units(5);


    private static final float screenFadeTime = 2.0f;

    public DeathScreenWorld(MainGame game, AdventureWorld adventureWorld, Viewport gameport){
        this.game = game;
        this.adventureWorld = adventureWorld;
        this.atlas  = game.assetManager.get(FileLocationStrings.spriteAtlas);
        this.gameport = gameport;
        createDeathScreenWorld();
    }


    public void createDeathScreenWorld(){

        WorldConfiguration config = new WorldConfigurationBuilder()
                .with(WorldConfigurationBuilder.Priority.HIGHEST,
                        new MovementSystem()
                )
                .with(WorldConfigurationBuilder.Priority.HIGH,
                        new AnimationSystem(),
                        //new FindPlayerSystem(player),
                        new ActionAfterTimeSystem(),
                        new ActionOnTouchSystem(),
                        new FadeSystem())
                .with(WorldConfigurationBuilder.Priority.LOW,
                        new FollowCameraSystem(gameport.getCamera()),
                        new RenderingSystem(game.batch, game.assetManager, gameport),
                        new BoundsDrawingSystem()
                )
                .build();

        world = new World(config);







        switch (adventureWorld.getGameCreator().getGameType()){

            case ADVENTURE:

                //TITLE
                createDeathScreenAdventureTitle(world);

                //YOU ARE HERE SQUARES
                createYouReachedHereSquares(adventureWorld.getGameCreator());

                //Items Collected
                createItemStatisticSection(world);

                //TAP TO EXIT
                Entity exit = world.createEntity();
                exit.edit().add(new PositionComponent());
                //exit.edit().add(new FadeComponent(true, screenFadeTime, false));
                exit.edit().add(new FollowCameraComponent(0, tapToRestartY));
                exit.edit().add(new CollisionBoundComponent(new Rectangle(0,0, gameport.getCamera().viewportWidth, Measure.units(10f))));
                exit.edit().add(new TextureFontComponent(FontAssets.medium,
                        MenuStrings.Death.RESTART,
                        new Color(1,1,1,0)));
                exit.edit().add(new ActionAfterTimeComponent(new Action() {
                    @Override
                    public void performAction(World world, Entity e) {
                        e.edit().add(new FadeComponent(true, 0.25f, false));
                        e.edit().add(new ActionOnTouchComponent(returnToMainMenu()));
                    }
                }, screenFadeTime));

                break;


            case TUTORIAL:

                createDeathScreenText(world,
                        MenuStrings.Death.TUTORIAL_FLAVOR_TEXT[MathUtils.random.nextInt(MenuStrings.Death.TUTORIAL_FLAVOR_TEXT.length)],
                        MenuStrings.Death.RESTART
                );

                break;

            case CHALLENGE:

                createDeathScreenText(world,
                        MenuStrings.Death.CHALLENGE_FLAVOR_TEXT[MathUtils.random.nextInt(MenuStrings.Death.CHALLENGE_FLAVOR_TEXT.length)],
                        MenuStrings.Death.RESTART
                        );

                break;
        }


        createBlackScreen(world, new FadeComponent(true, screenFadeTime, false));
    }


    public Action returnToMainMenu(){
        return  new Action() {
            @Override
            public void performAction(World world, Entity e) {
                game.getScreen().dispose();
                game.setScreen(new com.bryjamin.wickedwizard.screens.MenuScreen(game));
            }
        };
    }



    public Entity createSquare(World world, float size, float x, float y, Color color){

        Entity square = world.createEntity();
        square.edit().add(new PositionComponent());
        square.edit().add(new FadeComponent(true, screenFadeTime, false));
        square.edit().add(new FollowCameraComponent(x, y));
        square.edit().add(new TextureRegionComponent(atlas.findRegion(TextureStrings.BLOCK), size, size, TextureRegionComponent.FOREGROUND_LAYER_NEAR,
                color));

        return square;


    }

    public void createDeathScreenAdventureTitle(World world){

        Entity title = world.createEntity();
        title.edit().add(new PositionComponent());
        title.edit().add(new FollowCameraComponent(0, adventureTitleY));
        title.edit().add(new CollisionBoundComponent(new Rectangle(0,0, gameport.getCamera().viewportWidth, Measure.units(10f))));
        title.edit().add(new FadeComponent(true, screenFadeTime, false));
        title.edit().add(new TextureFontComponent(FontAssets.medium,
                MenuStrings.Death.ADVENTURE_FLAVOR_TEXT[MathUtils.random.nextInt(MenuStrings.Death.ADVENTURE_FLAVOR_TEXT.length)],
                new Color(Color.WHITE)));


    }



    public void createYouReachedHereSquares(GameCreator gameCreator){

        int maxColumns =  (gameCreator.gameLevels.size * 3) - 2;

        float startX = CenterMath.offsetX(gameport.getCamera().viewportWidth, (squareSize * maxColumns) + (squareGap * (maxColumns - 1)));

        int currentPosition = gameCreator.getPosition();

        for(int i = 0; i < gameCreator.gameLevels.size; i++){

            float x = TableMath.getXPos(startX, i * 3, maxColumns, squareSize, squareGap);
            float y = youAreHereIconsStartY;

            Color squareColor = i < currentPosition ? new Color(Color.WHITE) : new Color(0.5f, 0.5f, 0.5f, 1);
            createSquare(world, squareSize, x, y, squareColor);

            if(i != gameCreator.gameLevels.size - 1) {

                for (int j = 1; j <= 2; j++) {

                    float x2 = TableMath.getXPos(startX, (i * 3) + j, maxColumns, squareSize, squareGap) + CenterMath.offsetX(squareSize, squareSize / 2);
                    float y2 = y + CenterMath.offsetY(squareSize, squareSize / 2);

                    Color color = (i < currentPosition) ? new Color(Color.WHITE) : new Color(0.5f, 0.5f, 0.5f, 1);
                    createSquare(world, squareSize / 2, x2, y2, color);
                }

            }


            if(i == currentPosition){

                Entity flashingSqaure = createSquare(world, squareSize, x, y, new Color(Color.WHITE));
                flashingSqaure.edit().add(new ActionAfterTimeComponent(new Action() {


                    @Override
                    public void performAction(World world, Entity e) {
                        e.edit().remove(FadeComponent.class);
                        e.getComponent(ActionAfterTimeComponent.class).resetTime = 1.0f;
                        e.getComponent(ActionAfterTimeComponent.class).action = new Action() {

                            boolean bool = true;

                            @Override
                            public void performAction(World world, Entity e) {
                                TextureRegionComponent trc = e.getComponent(TextureRegionComponent.class);
                                trc.color.a = bool ? 0 : 1;
                                bool = !bool;
                            }
                        };
                    }
                }, screenFadeTime, true));


                Entity flashingSqaureText = world.createEntity();
                flashingSqaureText.edit().add(new PositionComponent());
                flashingSqaureText.edit().add(new FadeComponent(true, screenFadeTime, false));
                flashingSqaureText.edit().add(new FollowCameraComponent(x + CenterMath.offsetX(squareSize, gameport.getCamera().viewportWidth), y - Measure.units(7.5f)));
                flashingSqaureText.edit().add(new CollisionBoundComponent(new Rectangle(0, 0, gameport.getCamera().viewportWidth, Measure.units(10f))));
                flashingSqaureText.edit().add(new TextureFontComponent(FontAssets.small, MenuStrings.Death.YOU_REACHED_HERE, TextureRegionComponent.ENEMY_LAYER_MIDDLE,
                        new Color(Color.WHITE)));



            }

        }


    }



    public void createItemStatisticSection(World world){


        Array<Item> itemArray = adventureWorld.getPlayerStats().collectedItems;


        Entity itemsText = world.createEntity();
        itemsText.edit().add(new PositionComponent());
        itemsText.edit().add(new FollowCameraComponent(0, itemTitleY));
        itemsText.edit().add(new CollisionBoundComponent(new Rectangle(0,0, gameport.getCamera().viewportWidth, Measure.units(10f))));
        itemsText.edit().add(new FadeComponent(true, screenFadeTime, false));
        itemsText.edit().add(new TextureFontComponent(FontAssets.medium,
                MenuStrings.Death.ITEMS,
                new Color(Color.WHITE)));

        if(itemArray.size <= 0) {

            Entity noItemsText = world.createEntity();
            noItemsText.edit().add(new PositionComponent());
            noItemsText.edit().add(new FollowCameraComponent(0, itemTitleY - Measure.units(10f)));
            noItemsText.edit().add(new CollisionBoundComponent(new Rectangle(0,0, gameport.getCamera().viewportWidth, Measure.units(10f))));
            noItemsText.edit().add(new FadeComponent(true, screenFadeTime, false));
            noItemsText.edit().add(new TextureFontComponent(FontAssets.small,
                    MenuStrings.Death.NO_FOUND_ITEMS,
                    new Color(Color.WHITE)));

            return;
        }


        int maxColumns = (itemArray.size > 12) ? 12 : itemArray.size;
        int maxRows = 2;

        float startX = CenterMath.offsetX(gameport.getCamera().viewportWidth, (itemIconSize * maxColumns) + (itemIconGap * (maxColumns - 1)));


        for(int i = 0; i < itemArray.size; i++){

            if(i >= maxColumns * maxRows){
                break;
            }

            float x = TableMath.getXPos(startX, i, maxColumns, itemIconSize, itemIconGap);
            float y = TableMath.getYPosTopToBottom(itemIconsStartY, i, maxColumns, itemIconSize, itemIconGap);

            createItemIcon(world, itemArray.get(itemArray.size - i - 1), x, y);

        }
    }




    public void createItemIcon(World world, Item item, float x, float y){
        Entity itemEntity = world.createEntity();
        itemEntity.edit().add(new PositionComponent(x, y));
        itemEntity.edit().add(new FadeComponent(true, screenFadeTime, false));
        itemEntity.edit().add(new FollowCameraComponent(x, y));
        itemEntity.edit().add(new TextureRegionComponent(
                atlas.findRegion(item.getValues().getRegion().getLeft(), item.getValues().getRegion().getRight()),
                itemIconSize,itemIconSize, TextureRegionComponent.ENEMY_LAYER_MIDDLE));
    }


    public void createDeathScreenText(World world, String topText, String bottomText){



        float camX = gameport.getCamera().position.x - gameport.getCamera().viewportWidth / 2;
        float camY = gameport.getCamera().position.y - gameport.getCamera().viewportHeight / 2;

        Entity restartEntity = world.createEntity();
        restartEntity.edit().add(new PositionComponent(camX + CenterMath.offsetX(gameport.getCamera().viewportWidth, Measure.units(50f))
                ,camY + CenterMath.offsetY(gameport.getCamera().viewportHeight, Measure.units(10f))));
        restartEntity.edit().add(new CollisionBoundComponent(new Rectangle(gameport.getCamera().position.x
                ,gameport.getCamera().position.y - gameport.getWorldHeight() / 2 + 800, buttonWidth, buttonHeight)));
        restartEntity.edit().add(new ActionOnTouchComponent(returnToMainMenu()));
        restartEntity.edit().add(new FadeComponent(true, screenFadeTime, false));


        PositionComponent restartEntityPosition = restartEntity.getComponent(PositionComponent.class);
        CollisionBoundComponent restartEntityBound = restartEntity.getComponent(CollisionBoundComponent.class);

        Entity upperTextEntity = world.createEntity();
        upperTextEntity.edit().add(new PositionComponent(restartEntityPosition.getX(), restartEntityPosition.getY() + restartEntityBound.bound.getHeight() / 2));
        TextureFontComponent tfc = new TextureFontComponent(topText);
        upperTextEntity.edit().add(new CollisionBoundComponent(new Rectangle(gameport.getCamera().position.x
                ,gameport.getCamera().position.y - gameport.getWorldHeight() / 2 + 800, restartEntityBound.bound.getWidth(), restartEntityBound.bound.getHeight() / 2)));
        upperTextEntity.edit().add(tfc);
        upperTextEntity.edit().add(new FadeComponent(true, screenFadeTime, false));

        Entity lowerTextEntity = world.createEntity();
        lowerTextEntity.edit().add(new PositionComponent(restartEntityPosition.getX(), restartEntityPosition.getY()));
        tfc = new TextureFontComponent(bottomText);
        lowerTextEntity.edit().add(new CollisionBoundComponent(new Rectangle(gameport.getCamera().position.x
                ,gameport.getCamera().position.y - gameport.getWorldHeight() / 2 + 800, restartEntityBound.bound.getWidth(), restartEntityBound.bound.getHeight() / 2)));
        lowerTextEntity.edit().add(tfc);
        lowerTextEntity.edit().add(new FadeComponent(true, screenFadeTime, false));



    }




    public void createBlackScreen(World world, FadeComponent fc){

        float width = gameport.getCamera().viewportWidth * 2;
        float height = gameport.getCamera().viewportHeight * 2;



        Entity blackScreen = world.createEntity();
        blackScreen.edit().add(new PositionComponent(gameport.getCamera().position.x - width / 2,
                gameport.getCamera().position.y - height / 2));
        blackScreen.edit().add(new FollowCameraComponent(0, 0));
        TextureRegionComponent trc = new TextureRegionComponent(atlas.findRegion(TextureStrings.BLOCK),
                width,
                height,
                TextureRegionComponent.BACKGROUND_LAYER_MIDDLE,
                new Color(Color.BLACK));

        blackScreen.edit().add(trc);
        blackScreen.edit().add(fc);


    }



    public Entity createRestartTextEntity(World world, FadeComponent fadeComponent){

        float camX = gameport.getCamera().position.x - gameport.getCamera().viewportWidth / 2;
        float camY = gameport.getCamera().position.y - gameport.getCamera().viewportHeight / 2;

        Entity restartEntity = world.createEntity();
        restartEntity.edit().add(new PositionComponent(camX + CenterMath.offsetX(gameport.getCamera().viewportWidth, Measure.units(50f))
                ,camY + CenterMath.offsetY(gameport.getCamera().viewportHeight, Measure.units(10f))));
        restartEntity.edit().add(new CollisionBoundComponent(new Rectangle(gameport.getCamera().position.x
                ,gameport.getCamera().position.y - gameport.getWorldHeight() / 2 + 800, buttonWidth, buttonHeight)));
        restartEntity.edit().add(new ActionOnTouchComponent(new Action() {
            @Override
            public void performAction(World world, Entity e) {
                game.getScreen().dispose();
                game.setScreen(new com.bryjamin.wickedwizard.screens.MenuScreen(game));
            }
        }));
        restartEntity.edit().add(fadeComponent);


        return restartEntity;


    }


    public void process(float delta){
        GameDelta.delta(world, delta);
    }


    public World getWorld() {
        return world;
    }
}
