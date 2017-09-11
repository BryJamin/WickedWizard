package com.bryjamin.wickedwizard.screens.world.menu;

import com.artemis.Entity;
import com.artemis.World;
import com.artemis.WorldConfiguration;
import com.artemis.WorldConfigurationBuilder;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.bryjamin.wickedwizard.MainGame;
import com.bryjamin.wickedwizard.assets.FileLocationStrings;
import com.bryjamin.wickedwizard.assets.FontAssets;
import com.bryjamin.wickedwizard.assets.MenuStrings;
import com.bryjamin.wickedwizard.assets.TextureStrings;
import com.bryjamin.wickedwizard.ecs.components.ai.Action;
import com.bryjamin.wickedwizard.ecs.components.movement.PositionComponent;
import com.bryjamin.wickedwizard.ecs.components.texture.TextureRegionComponent;
import com.bryjamin.wickedwizard.ecs.systems.graphical.AnimationSystem;
import com.bryjamin.wickedwizard.ecs.systems.graphical.BoundsDrawingSystem;
import com.bryjamin.wickedwizard.ecs.systems.graphical.FadeSystem;
import com.bryjamin.wickedwizard.factories.arenas.challenges.ChallengeMaps;
import com.bryjamin.wickedwizard.factories.arenas.challenges.ChallengesResource;
import com.bryjamin.wickedwizard.screens.DataSave;
import com.bryjamin.wickedwizard.screens.MenuButton;
import com.bryjamin.wickedwizard.screens.PlayScreen;
import com.bryjamin.wickedwizard.utils.CenterMath;
import com.bryjamin.wickedwizard.utils.GameDelta;
import com.bryjamin.wickedwizard.utils.Measure;
import com.bryjamin.wickedwizard.utils.TableMath;

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

    private ChallengeMaps challengeMaps;


    private World world;

    public ChallengesWorldContainer(MainGame game, Viewport viewport){
        this.game = game;
        this.gameport = viewport;
        this.atlas = game.assetManager.get(FileLocationStrings.spriteAtlas);
        this.challengeMaps = new ChallengeMaps(game.assetManager, MathUtils.random);
        createWorld();
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


        Entity title = new MenuButton.MenuButtonBuilder(FontAssets.medium, atlas.findRegion(TextureStrings.BLOCK))
                .width(buttonWidth)
                .height(buttonHeight)
                .foregroundColor(new Color(Color.BLACK))
                .backgroundColor(new Color(0, 0, 0, 0))
                .build()
                .createButton(world,
                        MenuStrings.TRAILS,
                        CenterMath.offsetX(MainGame.GAME_WIDTH, buttonWidth),
                        Measure.units(50f));


        Entity backToMainMenu = new MenuButton.MenuButtonBuilder(FontAssets.medium, atlas.findRegion(TextureStrings.BLOCK))
                .width(Measure.units(30f))
                .height(Measure.units(10f))
                .foregroundColor(new Color(Color.BLACK))
                .backgroundColor(new Color(Color.WHITE))
                .action(new Action() {
                    @Override
                    public void performAction(World world, Entity e) {
                        com.bryjamin.wickedwizard.screens.MenuScreen.setMenuType(com.bryjamin.wickedwizard.screens.MenuScreen.MenuType.MAIN);
                    }
                })
                .build()
                .createButton(
                        world,
                        MenuStrings.MAIN_MENU,
                        MainGame.GAME_WIDTH - Measure.units(30f) - Measure.units(5f)
                        , Measure.units(5f));


        int count = 0;

        Array<String> allChallenges = new Array<String>();
        allChallenges.addAll();


        count = createChallengeButtons(world, count, ChallengesResource.Rank1Challenges.rank1ChallengesArray,
                ChallengesResource.LEVEL_1_COMPLETE);
        count = createChallengeButtons(world, count, ChallengesResource.Rank2Challenges.rank2ChallengesArray,
                ChallengesResource.LEVEL_2_COMPLETE);
        count = createChallengeButtons(world, count, ChallengesResource.Rank3Challenges.rank3ChallengesArray,
                ChallengesResource.LEVEL_3_COMPLETE);
        count = createChallengeButtons(world, count, ChallengesResource.Rank4Challenges.rank4ChallengesArray,
                ChallengesResource.LEVEL_4_COMPLETE);
        count = createChallengeButtons(world, count, ChallengesResource.Rank5Challenges.rank5ChallengesArray,
                com.bryjamin.wickedwizard.factories.arenas.challenges.ChallengesResource.LEVEL_5_COMPLETE);

    }




    public int createChallengeButtons(World world, int startCount, Array<String> challengeIds, String unlockString){


        MenuButton.MenuButtonBuilder challengeButtonBuilder = new com.bryjamin.wickedwizard.screens.MenuButton.MenuButtonBuilder(com.bryjamin.wickedwizard.assets.FontAssets.small, atlas.findRegion(TextureStrings.BLOCK))
                .width(buttonWidth)
                .height(buttonHeight)
                .foregroundColor(buttonForeground)
                .backgroundColor(buttonBackground);

        int count = startCount;

        for(int i = 0; i < challengeIds.size; i++){


            final String s = challengeIds.get(i);

            boolean challengeComplete = DataSave.isDataAvailable(s);

            if(DataSave.isDataAvailable(unlockString)) {

                Entity startChallenge = challengeButtonBuilder
                        .foregroundColor(challengeComplete ? buttonBackground : buttonForeground)
                        .backgroundColor(challengeComplete ? buttonForeground : buttonBackground)
                        .action(new Action() {
                            @Override
                            public void performAction(World world, Entity e) {

                                try {

                                    System.out.println(s);

                                    com.bryjamin.wickedwizard.factories.arenas.GameCreator gameCreator = challengeMaps.getChallenge(s);
                                    if(gameCreator == null) throw new Exception("Challenge with id: " + s + " does not exist in the Challenge Maps Array");
                                    game.getScreen().dispose();
                                    game.setScreen(new PlayScreen(game, gameCreator));
                                } catch (Exception exception){

                                    exception.printStackTrace();

                                }
                            }
                        })
                        .build()
                        .createButton(world, Integer.toString(count + 1), x, y);



            } else {

                Entity lockedChallenge = world.createEntity();
                lockedChallenge.edit().add(new PositionComponent(x, y));
                lockedChallenge.edit().add(new TextureRegionComponent(atlas.findRegion(TextureStrings.SETTINGS_LOCK), buttonWidth, buttonHeight, TextureRegionComponent.ENEMY_LAYER_MIDDLE));

            }


            count++;

        }


        return count;


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
