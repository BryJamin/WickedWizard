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
    private static final float tapToRestartY = Measure.units(47.5f);


    private static final float youAreHereIconsStartY = Measure.units(40);



    private static final float itemTitleY = Measure.units(22.5f);
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

                Entity title = world.createEntity();
                title.edit().add(new PositionComponent());
                title.edit().add(new FollowCameraComponent(0, adventureTitleY));
                title.edit().add(new CollisionBoundComponent(new Rectangle(0,0, gameport.getCamera().viewportWidth, Measure.units(10f))));
                title.edit().add(new FadeComponent(true, screenFadeTime, false));
                title.edit().add(new TextureFontComponent(FontAssets.medium,
                        MenuStrings.Death.ADVENTURE_LEVEL_1_FLAVOR_TEXT[MathUtils.random.nextInt(MenuStrings.Death.ADVENTURE_LEVEL_1_FLAVOR_TEXT.length)],
                        new Color(Color.WHITE)));




                GameCreator gameCreator = adventureWorld.getGameCreator();


                int maxColumns =  (gameCreator.gameLevels.size * 3) - 2;

                float size = Measure.units(5f);
                float gap = Measure.units(0.5f);

                float startX = CenterMath.offsetX(gameport.getCamera().viewportWidth, (size * maxColumns) + (gap * (maxColumns - 1)));

                int currentPosition = gameCreator.getPosition();

                System.out.println("currentpostion is " + currentPosition);

                for(int i = 0; i < gameCreator.gameLevels.size; i++){


                    float x = TableMath.getXPos(startX, i * 3, maxColumns, size, gap);
                    float y = youAreHereIconsStartY;

                    Entity square = world.createEntity();
                    square.edit().add(new PositionComponent());
                    square.edit().add(new FadeComponent(true, screenFadeTime, false));
                    square.edit().add(new FollowCameraComponent(x, y));
                    square.edit().add(new TextureRegionComponent(atlas.findRegion(TextureStrings.BLOCK), size, size, TextureRegionComponent.BACKGROUND_LAYER_NEAR,
                    i < currentPosition ? new Color(Color.WHITE) : new Color(0.5f, 0.5f, 0.5f, 1)
                    ));


                    if(i != gameCreator.gameLevels.size - 1) {

                        for (int j = 1; j <= 2; j++) {

                            float x2 = TableMath.getXPos(startX, (i * 3) + j, maxColumns, size, gap) + CenterMath.offsetX(size, size / 2);
                            float y2 = y + CenterMath.offsetY(size, size / 2);

                            Color color = (i < currentPosition) ? new Color(Color.WHITE) : new Color(0.5f, 0.5f, 0.5f, 1);

                            Entity smallSquare = world.createEntity();
                            smallSquare.edit().add(new PositionComponent());
                            smallSquare.edit().add(new FadeComponent(true, screenFadeTime, false));
                            smallSquare.edit().add(new FollowCameraComponent(x2, y2));
                            smallSquare.edit().add(new TextureRegionComponent(atlas.findRegion(TextureStrings.BLOCK), size / 2, size / 2, TextureRegionComponent.FOREGROUND_LAYER_NEAR,
                                    color));


                        }

                    }


                    if(i == currentPosition){

                        Entity flashingSqaure = world.createEntity();
                        flashingSqaure.edit().add(new PositionComponent());
                        flashingSqaure.edit().add(new FadeComponent(true, screenFadeTime, false));
                        flashingSqaure.edit().add(new FollowCameraComponent(x, y));
                        flashingSqaure.edit().add(new TextureRegionComponent(atlas.findRegion(TextureStrings.BLOCK), size, size, TextureRegionComponent.ENEMY_LAYER_MIDDLE,
                                new Color(Color.WHITE)));
                        flashingSqaure.edit().add(new ActionAfterTimeComponent(new Action() {

                            boolean bool = true;

                            @Override
                            public void performAction(World world, Entity e) {

                                e.edit().remove(FadeComponent.class);

                                e.getComponent(ActionAfterTimeComponent.class).resetTime = 1.0f;
                                e.getComponent(ActionAfterTimeComponent.class).timeUntilAction = 0f;

                                e.getComponent(ActionAfterTimeComponent.class).action = new Action() {
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
                        flashingSqaureText.edit().add(new FollowCameraComponent(x + CenterMath.offsetX(size, gameport.getCamera().viewportWidth), y - Measure.units(7.5f)));
                        flashingSqaureText.edit().add(new CollisionBoundComponent(new Rectangle(0, 0, gameport.getCamera().viewportWidth, Measure.units(10f))));
                        flashingSqaureText.edit().add(new TextureFontComponent(FontAssets.small, MenuStrings.Death.YOU_REACHED_HERE, TextureRegionComponent.ENEMY_LAYER_MIDDLE,
                                new Color(Color.WHITE)));



                    }

                }



                //Items Collected

                createItemStatisticSection(world);

                //END




                Entity exit = world.createEntity();
                exit.edit().add(new PositionComponent());
                exit.edit().add(new FadeComponent(true, screenFadeTime, false));
                exit.edit().add(new FollowCameraComponent(0, tapToRestartY));
                exit.edit().add(new CollisionBoundComponent(new Rectangle(0,0, gameport.getCamera().viewportWidth, Measure.units(10f))));
                exit.edit().add(new TextureFontComponent(FontAssets.medium,
                        MenuStrings.Death.RESTART,
                        new Color(Color.WHITE)));
                exit.edit().add(new ActionOnTouchComponent(returnToMainMenu()));

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



    public void createItemStatisticSection(World world){


        Array<Item> itemArray = adventureWorld.getPlayerStats().collectedItems;

        if(itemArray.size <= 0) return;

        Entity itemsText = world.createEntity();
        itemsText.edit().add(new PositionComponent());
        itemsText.edit().add(new FollowCameraComponent(0, itemTitleY));
        itemsText.edit().add(new CollisionBoundComponent(new Rectangle(0,0, gameport.getCamera().viewportWidth, Measure.units(10f))));
        itemsText.edit().add(new FadeComponent(true, screenFadeTime, false));
        itemsText.edit().add(new TextureFontComponent(FontAssets.medium,
                MenuStrings.Death.ITEMS,
                new Color(Color.WHITE)));


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
        world.setDelta(delta < 0.030f ? delta : 0.030f);
        world.process();
    }


    public World getWorld() {
        return world;
    }
}
