package com.byrjamin.wickedwizard.screens.world.menu;

import com.artemis.Entity;
import com.artemis.World;
import com.artemis.WorldConfiguration;
import com.artemis.WorldConfigurationBuilder;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.byrjamin.wickedwizard.MainGame;
import com.byrjamin.wickedwizard.assets.Assets;
import com.byrjamin.wickedwizard.assets.FileLocationStrings;
import com.byrjamin.wickedwizard.assets.MenuStrings;
import com.byrjamin.wickedwizard.assets.TextureStrings;
import com.byrjamin.wickedwizard.ecs.components.ai.Action;
import com.byrjamin.wickedwizard.ecs.components.ai.ActionOnTouchComponent;
import com.byrjamin.wickedwizard.ecs.systems.graphical.AnimationSystem;
import com.byrjamin.wickedwizard.ecs.systems.graphical.BoundsDrawingSystem;
import com.byrjamin.wickedwizard.ecs.systems.graphical.FadeSystem;
import com.byrjamin.wickedwizard.ecs.systems.graphical.RenderingSystem;
import com.byrjamin.wickedwizard.ecs.systems.input.ActionOnTouchSystem;
import com.byrjamin.wickedwizard.ecs.systems.physics.MovementSystem;
import com.byrjamin.wickedwizard.factories.arenas.challenges.ChallengeMaps;
import com.byrjamin.wickedwizard.factories.arenas.challenges.ChallengesResource;
import com.byrjamin.wickedwizard.screens.DataSave;
import com.byrjamin.wickedwizard.screens.MenuButton;
import com.byrjamin.wickedwizard.screens.MenuScreen;
import com.byrjamin.wickedwizard.screens.PlayScreen;
import com.byrjamin.wickedwizard.screens.world.AreYouSureWorld;
import com.byrjamin.wickedwizard.screens.world.WorldContainer;
import com.byrjamin.wickedwizard.utils.AbstractGestureDectector;
import com.byrjamin.wickedwizard.utils.CenterMath;
import com.byrjamin.wickedwizard.utils.GameDelta;
import com.byrjamin.wickedwizard.utils.Measure;

/**
 * Created by BB on 20/08/2017.
 */

public class ChallengesWorldContainer extends AbstractGestureDectector implements WorldContainer {

    private final MainGame game;
    private final Viewport gameport;
    private final TextureAtlas atlas;


    private static final float buttonWidth = Measure.units(7.5f);
    private static final float buttonHeight = Measure.units(7.5f);

    private static final Color buttonForeground = new Color(Color.BLACK);
    private static final Color buttonBackground = new Color(Color.WHITE);


    private World world;

    public ChallengesWorldContainer(MainGame game, Viewport viewport){
        this.game = game;
        this.gameport = viewport;
        this.atlas = game.assetManager.get(FileLocationStrings.spriteAtlas);
        createWorld();
    }




    public void createWorld() {

        WorldConfiguration config = new WorldConfigurationBuilder()
                .with(WorldConfigurationBuilder.Priority.HIGHEST,
                        new MovementSystem()
                )
                .with(WorldConfigurationBuilder.Priority.HIGH,
                        new AnimationSystem(),
                        //new FindPlayerSystem(player),
                        new ActionOnTouchSystem(),
                        new FadeSystem())
                .with(WorldConfigurationBuilder.Priority.LOW,
                        new RenderingSystem(game.batch, game.assetManager, gameport),
                        new BoundsDrawingSystem()
                )
                .build();


        world = new World(config);


        Entity title = new MenuButton.MenuButtonBuilder(Assets.medium, atlas.findRegion(TextureStrings.BLOCK))
                .width(buttonWidth)
                .height(buttonHeight)
                .foregroundColor(new Color(Color.BLACK))
                .backgroundColor(new Color(0, 0, 0, 0))
                .build()
                .createButton(world,
                        MenuStrings.TRAILS,
                        CenterMath.offsetX(MainGame.GAME_WIDTH, buttonWidth),
                        Measure.units(50f));


        Entity backToMainMenu = new MenuButton.MenuButtonBuilder(Assets.medium, atlas.findRegion(TextureStrings.BLOCK))
                .width(Measure.units(30f))
                .height(buttonHeight)
                .foregroundColor(new Color(Color.BLACK))
                .backgroundColor(new Color(0, 0, 0, 0))
                .action(new Action() {
                    @Override
                    public void performAction(World world, Entity e) {
                        MenuScreen menuScreen = (MenuScreen) game.getScreen();
                        menuScreen.setMenuType(MenuScreen.MenuType.MAIN);
                    }
                })
                .build()
                .createButton(
                        world,
                        MenuStrings.BACK_TO_MAIN_MENU,
                        MainGame.GAME_WIDTH - Measure.units(30f) - Measure.units(5f)
                        , Measure.units(5f));


        MenuButton.MenuButtonBuilder challengeButtonBuilder = new MenuButton.MenuButtonBuilder(Assets.small, atlas.findRegion(TextureStrings.BLOCK))
                .width(buttonWidth)
                .height(buttonHeight)
                .foregroundColor(buttonForeground)
                .backgroundColor(buttonBackground);


        final ChallengeMaps challengeMaps = new ChallengeMaps(game.assetManager, MathUtils.random);

        int count = 0;


        float startY = Measure.units(40f);

        float buttonGap = Measure.units(2.5f);


        int maxColumns = 9;

        float startX = CenterMath.offsetX(MainGame.GAME_WIDTH, (buttonWidth * maxColumns) + (buttonGap * (maxColumns - 1)));

        for(int i = 0; i < ChallengesResource.Rank1Challenges.rank1ChallengesArray.size; i++){

            int mod = i % maxColumns;
            int div = i / maxColumns;

            final String s = ChallengesResource.Rank1Challenges.rank1ChallengesArray.get(i);

            boolean challengeComplete = DataSave.isDataAvailable(s);

            Entity startChallenge = challengeButtonBuilder
                    .foregroundColor(challengeComplete ? buttonBackground : buttonForeground)
                    .backgroundColor(challengeComplete ? buttonForeground : buttonBackground)
                    .action(new Action() {
                        @Override
                        public void performAction(World world, Entity e) {
                            game.getScreen().dispose();
                            game.setScreen(new PlayScreen(game, challengeMaps.getChallenge(s)));
                        }
                    })
                    .build()
                    .createButton(world, Integer.toString(count + 1), startX  + buttonWidth * mod + buttonGap * mod,
                            startY - (div * buttonHeight) - (div * buttonGap));



            count++;

        }


        int preCount = count;

        for(int i = 0; i < ChallengesResource.Rank2Challenges.rank2ChallengesArray.size; i++){

            int mod = count % maxColumns;
            int div = count / maxColumns;

            final String s = ChallengesResource.Rank2Challenges.rank2ChallengesArray.get(i);

            boolean challengeComplete = DataSave.isDataAvailable(s);

            if(DataSave.isDataAvailable(ChallengesResource.Rank2Challenges.rank2ChallengesUnlockString)) {

                Entity startChallenge = challengeButtonBuilder
                        .foregroundColor(challengeComplete ? buttonBackground : buttonForeground)
                        .backgroundColor(challengeComplete ? buttonForeground : buttonBackground)
                        .action(new Action() {
                            @Override
                            public void performAction(World world, Entity e) {
                                game.getScreen().dispose();
                                game.setScreen(new PlayScreen(game, challengeMaps.getChallenge(s)));
                            }
                        })
                        .build()
                        .createButton(world, Integer.toString(count + 1), startX + buttonWidth * mod + buttonGap * mod,
                                startY - (div * buttonHeight) - (div * buttonGap));

            } else {

                Entity startChallenge = challengeButtonBuilder
                        .foregroundColor(buttonForeground)
                        .backgroundColor(buttonForeground)
                        .action(new Action() {
                            @Override
                            public void performAction(World world, Entity e) {
                                
                            }
                        })
                        .build()
                        .createButton(world, "", startX + buttonWidth * mod + buttonGap * mod,
                                startY - (div * buttonHeight) - (div * buttonGap));


            }


            count++;

        }


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
        return world.getSystem(ActionOnTouchSystem.class).touch(touchInput.x, touchInput.y);
    }
}
