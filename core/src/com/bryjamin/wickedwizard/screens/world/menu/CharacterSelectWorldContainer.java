package com.bryjamin.wickedwizard.screens.world.menu;

import com.artemis.Entity;
import com.artemis.World;
import com.artemis.WorldConfiguration;
import com.artemis.WorldConfigurationBuilder;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.bryjamin.wickedwizard.MainGame;
import com.bryjamin.wickedwizard.assets.FileLocationStrings;
import com.bryjamin.wickedwizard.assets.FontAssets;
import com.bryjamin.wickedwizard.assets.MenuStrings;
import com.bryjamin.wickedwizard.assets.PlayerIDs;
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
import com.bryjamin.wickedwizard.screens.DataSave;
import com.bryjamin.wickedwizard.screens.PlayScreen;
import com.bryjamin.wickedwizard.screens.world.WorldContainer;
import com.bryjamin.wickedwizard.utils.AbstractGestureDectector;
import com.bryjamin.wickedwizard.utils.CenterMath;
import com.bryjamin.wickedwizard.utils.GameDelta;
import com.bryjamin.wickedwizard.utils.Measure;
import com.bryjamin.wickedwizard.utils.TableMath;

/**
 * Created by BB on 09/09/2017.
 */

public class CharacterSelectWorldContainer extends AbstractGestureDectector implements WorldContainer {

    private final MainGame game;
    private final Viewport gameport;
    private final TextureAtlas atlas;


    private static final float characterSelectWidth = Measure.units(10f);
    private static final float characterSelectHeight = Measure.units(10f);
    private static final float characterSelectGap = Measure.units(5f);

    private static final float buttonWidth = Measure.units(7.5f);
    private static final float buttonHeight = Measure.units(7.5f);
    private static final float buttonGap = Measure.units(2.5f);

    private static final Color buttonForeground = new Color(Color.BLACK);
    private static final Color buttonBackground = new Color(Color.WHITE);

    private static final int maxColumns = 4;
    private static final int maxRows = 2;

    private static final float startY = Measure.units(37.5f);
    private static final float startX = CenterMath.offsetX(MainGame.GAME_WIDTH, (characterSelectWidth * maxColumns) + (characterSelectGap * (maxColumns - 1)));

    private World world;

    public CharacterSelectWorldContainer(MainGame game, Viewport viewport) {
        this.game = game;
        this.gameport = viewport;
        this.atlas = game.assetManager.get(FileLocationStrings.spriteAtlas);
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


        Entity title = new com.bryjamin.wickedwizard.screens.MenuButton.MenuButtonBuilder(com.bryjamin.wickedwizard.assets.FontAssets.medium, atlas.findRegion(TextureStrings.BLOCK))
                .width(buttonWidth)
                .height(buttonHeight)
                .foregroundColor(new Color(Color.BLACK))
                .backgroundColor(new Color(0, 0, 0, 0))
                .build()
                .createButton(world,
                        MenuStrings.SELECT_A_CHARACTER,
                        CenterMath.offsetX(MainGame.GAME_WIDTH, buttonWidth),
                        Measure.units(50f));


        Entity backToMainMenu = new com.bryjamin.wickedwizard.screens.MenuButton.MenuButtonBuilder(com.bryjamin.wickedwizard.assets.FontAssets.medium, atlas.findRegion(TextureStrings.BLOCK))
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


        PlayerIDs.PlayableCharacter[] playableCharacters = new PlayerIDs.PlayableCharacter[]{PlayerIDs.LEAH, PlayerIDs.PHI, PlayerIDs.XI, PlayerIDs.TESS};


        for(int i = 0; i < playableCharacters.length; i++){

            PlayerIDs.PlayableCharacter pc = playableCharacters[i];

            float x = TableMath.getXPos(startX, i, maxColumns, characterSelectWidth, characterSelectGap);
            float y = TableMath.getYPos(startY, i, maxColumns, characterSelectHeight, characterSelectGap);

            if(pc.getUnlockString() == null){
                createCharacterSelect(world, pc, x, y);
            } else if(DataSave.isDataAvailable(pc.getUnlockString())){
                createCharacterSelect(world, pc, x, y);
            } else {

                Entity e = world.createEntity();
                e.edit().add(new PositionComponent(x, y));
                e.edit().add(new CollisionBoundComponent(new Rectangle(x, y, characterSelectWidth, characterSelectHeight)));
                e.edit().add(new TextureRegionComponent(atlas.findRegion(TextureStrings.SETTINGS_QUESTION_MARK), characterSelectWidth, characterSelectHeight));
            }

        }
    }



    public Entity createCharacterSelect(World world, final PlayerIDs.PlayableCharacter pc, float x, float y){

        Entity e = world.createEntity();

        e.edit().add(new PositionComponent(x, y));
        e.edit().add(new CollisionBoundComponent(new Rectangle(x, y, characterSelectWidth, characterSelectHeight)));

        e.edit().add(new ActionOnTouchComponent(new Action() {
            @Override
            public void performAction(World world, Entity e) {
                game.getScreen().dispose();
                game.setScreen(new PlayScreen(game, pc.getId()));
            }
        }));


        e.edit().add(new TextureRegionComponent(atlas.findRegion(pc.getRegion()), characterSelectWidth, characterSelectHeight));


        Entity textBelow = world.createEntity();
        textBelow.edit().add(new PositionComponent(x, y - Measure.units(5f)));
        textBelow.edit().add(new CollisionBoundComponent(new Rectangle(x, y - Measure.units(5f), characterSelectWidth, Measure.units(5f))));
        textBelow.edit().add(new TextureFontComponent(FontAssets.small, pc.getName()));


        return e;
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
