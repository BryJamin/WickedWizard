package com.bryjamin.wickedwizard.screens.world.menu;

import com.artemis.Entity;
import com.artemis.World;
import com.artemis.WorldConfiguration;
import com.artemis.WorldConfigurationBuilder;
import com.artemis.utils.Bag;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
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
import com.bryjamin.wickedwizard.screens.MenuButton;
import com.bryjamin.wickedwizard.screens.MenuScreen;
import com.bryjamin.wickedwizard.screens.PlayScreen;
import com.bryjamin.wickedwizard.screens.world.WorldContainer;
import com.bryjamin.wickedwizard.utils.AbstractGestureDectector;
import com.bryjamin.wickedwizard.utils.BagToEntity;
import com.bryjamin.wickedwizard.utils.CenterMath;
import com.bryjamin.wickedwizard.utils.ComponentBag;
import com.bryjamin.wickedwizard.utils.GameDelta;
import com.bryjamin.wickedwizard.utils.Measure;

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

    private static final int maxColumns = 4;
    private static final int maxRows = 2;

    private static final float startY = Measure.units(32.5f);
    private static final float startX = CenterMath.offsetX(MainGame.GAME_WIDTH, (characterSelectWidth * maxColumns) + (characterSelectGap * (maxColumns - 1)));

    private World world;


    private Array<Bag<ComponentBag>> characterComponentBagArray = new Array<Bag<ComponentBag>>();
    private static final float arrowSize = Measure.units(15f);
    private Bag<Entity> currentlyShownCharacter;
    private static int currentlyShownIndex = 0;


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


        setUpTitle(world);
        setUpMainMenuButton(world);
        setUpPlayerEntityBags(world);


        float arrowY = Measure.units(45f);

        arrowEntity(world, Measure.units(5f), arrowY, true);
        arrowEntity(world, gameport.getCamera().viewportWidth - arrowSize - Measure.units(5f), arrowY, false);


    }


    public void setUpPlayerEntityBags(World world){


        PlayerIDs.PlayableCharacter[] playableCharacters = new PlayerIDs.PlayableCharacter[]{PlayerIDs.LEAH, PlayerIDs.PHI, PlayerIDs.XI, PlayerIDs.TESS};


        for(int i = 0; i < playableCharacters.length; i++){

            PlayerIDs.PlayableCharacter pc = playableCharacters[i];

            float x = CenterMath.offsetX(gameport.getCamera().viewportWidth, characterSelectWidth);
            float y = startY;

            Bag<ComponentBag> bagArray = new Bag<ComponentBag>();

            //if(true){
            if(pc.getUnlockString() == null || DataSave.isDataAvailable(pc.getUnlockString())){
                createCharacterSelect(bagArray, pc, x, y);
            } else {

                ComponentBag bag = new ComponentBag();
                bag.add(new PositionComponent(x, y));
                bag.add(new CollisionBoundComponent(new Rectangle(x, y, characterSelectWidth, characterSelectHeight)));
                bag.add(new TextureRegionComponent(atlas.findRegion(TextureStrings.SETTINGS_QUESTION_MARK), characterSelectWidth, characterSelectHeight));

                ComponentBag textBelow = new ComponentBag();
                textBelow.add(new PositionComponent(x, y - Measure.units(5f)));
                textBelow.add(new CollisionBoundComponent(new Rectangle(x, y - Measure.units(5f), characterSelectWidth, Measure.units(5f))));
                textBelow.add(new TextureFontComponent(FontAssets.small, pc.getName()));

                bagArray.add(textBelow);

                bagArray.add(bag);
            }

            characterComponentBagArray.add(bagArray);

        }


        currentlyShownCharacter = BagToEntity.bagsToEntities(world, characterComponentBagArray.get(currentlyShownIndex));



    }



    public Bag<ComponentBag> createCharacterSelect(Bag<ComponentBag> bags, final PlayerIDs.PlayableCharacter pc, float x, float y){

        ComponentBag bag = new ComponentBag();

        bag.add(new PositionComponent(x, y));
        bag.add(new CollisionBoundComponent(new Rectangle(x, y, characterSelectWidth, characterSelectHeight)));

        bag.add(new ActionOnTouchComponent(new Action() {
            @Override
            public void performAction(World world, Entity e) {
                game.getScreen().dispose();
                game.setScreen(new PlayScreen(game, pc.getId()));
            }
        }));


        bag.add(new TextureRegionComponent(atlas.findRegion(pc.getRegion()), characterSelectWidth, characterSelectHeight));


        bags.add(bag);


        ComponentBag textBelow = new ComponentBag();

        textBelow.add(new PositionComponent(x, y - Measure.units(5f)));
        textBelow.add(new CollisionBoundComponent(new Rectangle(x, y - Measure.units(5f), characterSelectWidth, Measure.units(5f))));
        textBelow.add(new TextureFontComponent(FontAssets.small, pc.getName()));

        bags.add(textBelow);



        ComponentBag traits = new ComponentBag();

        traits.add(new PositionComponent(x, y - Measure.units(10f)));
        traits.add(new CollisionBoundComponent(new Rectangle(x, y - Measure.units(10f), characterSelectWidth, Measure.units(5f))));
        traits.add(new TextureFontComponent(FontAssets.small, MenuStrings.SELECT_A_CHARACTER_TRAITS + ": " + pc.getTraits()));

        bags.add(traits);

        ComponentBag synopsis = new ComponentBag();

        synopsis.add(new PositionComponent(x, y - Measure.units(15f)));
        synopsis.add(new CollisionBoundComponent(new Rectangle(x, y - Measure.units(15f), characterSelectWidth, Measure.units(5f))));
        synopsis.add(new TextureFontComponent(FontAssets.small, MenuStrings.SELECT_A_CHARACTER_PERSONALITY + ": " + pc.getPersonality()));

        bags.add(synopsis);


        return bags;
    }



    public void setUpTitle(World world){

        float titleY = Measure.units(50f);

        Entity title = new com.bryjamin.wickedwizard.screens.MenuButton.MenuButtonBuilder(com.bryjamin.wickedwizard.assets.FontAssets.medium, atlas.findRegion(TextureStrings.BLOCK))
                .width(buttonWidth)
                .height(buttonHeight)
                .foregroundColor(new Color(Color.BLACK))
                .backgroundColor(new Color(0, 0, 0, 0))
                .build()
                .createButton(world,
                        MenuStrings.SELECT_A_CHARACTER,
                        CenterMath.offsetX(MainGame.GAME_WIDTH, buttonWidth),
                        titleY);


        Entity bottomBit = new com.bryjamin.wickedwizard.screens.MenuButton.MenuButtonBuilder(FontAssets.small, atlas.findRegion(TextureStrings.BLOCK))
                .width(buttonWidth)
                .height(buttonHeight)
                .foregroundColor(new Color(Color.BLACK))
                .backgroundColor(new Color(0, 0, 0, 0))
                .build()
                .createButton(world,
                        MenuStrings.TAP_A_CHARACTER,
                        CenterMath.offsetX(MainGame.GAME_WIDTH, buttonWidth),
                        titleY - Measure.units(5f));


    }


    public void setUpMainMenuButton(World world){
        Entity backToMainMenu = new MenuButton.MenuButtonBuilder(com.bryjamin.wickedwizard.assets.FontAssets.medium, atlas.findRegion(TextureStrings.BLOCK))
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
                for(Entity entity : currentlyShownCharacter){
                    entity.deleteFromWorld();
                }

                currentlyShownIndex = isLeft ? currentlyShownIndex - 1 : currentlyShownIndex + 1;

                if(currentlyShownIndex >= characterComponentBagArray.size){
                    currentlyShownIndex = 0;
                } else if(currentlyShownIndex < 0) {
                    currentlyShownIndex = characterComponentBagArray.size - 1;
                }

                currentlyShownCharacter = BagToEntity.bagsToEntities(world, characterComponentBagArray.get(currentlyShownIndex));

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
