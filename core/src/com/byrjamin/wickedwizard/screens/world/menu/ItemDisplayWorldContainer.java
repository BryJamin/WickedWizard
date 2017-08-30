package com.byrjamin.wickedwizard.screens.world.menu;

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
import com.byrjamin.wickedwizard.MainGame;
import com.byrjamin.wickedwizard.assets.Assets;
import com.byrjamin.wickedwizard.assets.FileLocationStrings;
import com.byrjamin.wickedwizard.assets.MenuStrings;
import com.byrjamin.wickedwizard.assets.TextureStrings;
import com.byrjamin.wickedwizard.ecs.components.ai.Action;
import com.byrjamin.wickedwizard.ecs.components.ai.ActionOnTouchComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.CollisionBoundComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.PositionComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.TextureRegionComponent;
import com.byrjamin.wickedwizard.ecs.systems.graphical.AnimationSystem;
import com.byrjamin.wickedwizard.ecs.systems.graphical.BoundsDrawingSystem;
import com.byrjamin.wickedwizard.ecs.systems.graphical.FadeSystem;
import com.byrjamin.wickedwizard.ecs.systems.graphical.RenderingSystem;
import com.byrjamin.wickedwizard.ecs.systems.input.ActionOnTouchSystem;
import com.byrjamin.wickedwizard.ecs.systems.physics.MovementSystem;
import com.byrjamin.wickedwizard.factories.arenas.challenges.ChallengeMaps;
import com.byrjamin.wickedwizard.factories.items.Item;
import com.byrjamin.wickedwizard.factories.items.ItemResource;
import com.byrjamin.wickedwizard.factories.items.ItemStore;
import com.byrjamin.wickedwizard.screens.DataSave;
import com.byrjamin.wickedwizard.screens.MenuButton;
import com.byrjamin.wickedwizard.screens.MenuScreen;
import com.byrjamin.wickedwizard.screens.world.WorldContainer;
import com.byrjamin.wickedwizard.utils.AbstractGestureDectector;
import com.byrjamin.wickedwizard.utils.BagToEntity;
import com.byrjamin.wickedwizard.utils.CenterMath;
import com.byrjamin.wickedwizard.utils.ComponentBag;
import com.byrjamin.wickedwizard.utils.GameDelta;
import com.byrjamin.wickedwizard.utils.Measure;

/**
 * Created by BB on 22/08/2017.
 */

public class ItemDisplayWorldContainer extends AbstractGestureDectector implements WorldContainer {

    private final MainGame game;
    private final Viewport gameport;
    private final TextureAtlas atlas;


    private static final float arrowSize = Measure.units(5f);

    private static final float iconWidth = Measure.units(7.5f);
    private static final float iconHeight = Measure.units(7.5f);

    private static final float buttonWidth = Measure.units(7.5f);
    private static final float buttonHeight = Measure.units(7.5f);

    private Array<Bag<ComponentBag>> itemComponentBagArray = new Array<Bag<ComponentBag>>();

    private Bag<Entity> currentlyShownItems;
    private int currentlyShownIndex = 0;

    private World world;

    public ItemDisplayWorldContainer(MainGame game, Viewport viewport){
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
                        MenuStrings.ITEMS,
                        CenterMath.offsetX(MainGame.GAME_WIDTH, buttonWidth),
                        Measure.units(50f));


        Entity backToMainMenu = new MenuButton.MenuButtonBuilder(Assets.medium, atlas.findRegion(TextureStrings.BLOCK))
                .width(Measure.units(30f))
                .height(Measure.units(10f))
                .foregroundColor(new Color(Color.BLACK))
                .backgroundColor(new Color(Color.WHITE))
                .action(new Action() {
                    @Override
                    public void performAction(World world, Entity e) {
                        MenuScreen.setMenuType(MenuScreen.MenuType.MAIN);
                    }
                })
                .build()
                .createButton(
                        world,
                        MenuStrings.MAIN_MENU,
                        MainGame.GAME_WIDTH - Measure.units(30f) - Measure.units(5f)
                        , Measure.units(5f));


        int count = 0;
        float startY = Measure.units(40f);
        float buttonGap = Measure.units(2.5f);

        int maxColumns = 8;
        int maxRows = 3;

        float startX = CenterMath.offsetX(MainGame.GAME_WIDTH, (iconWidth * maxColumns) + (buttonGap * (maxColumns - 1)));




        Array<Item> allItems = ItemResource.getAllItems();

        while(allItems.size > 0) {

            Array<Item> copyArray = new Array<Item>();
            Bag<ComponentBag> bagArray = new Bag<ComponentBag>();

            for (int i = 0; i < allItems.size; i++) {

                int mod = i % maxColumns;
                int div = i / maxColumns;

                ComponentBag item = itemIcon(allItems.get(i), startX + iconWidth * mod + buttonGap * mod,
                        startY - (div * iconHeight) - (div * buttonGap));

                bagArray.add(item);
                count++;

                copyArray.add(allItems.get(i));

                if (i + 1 >= maxColumns * maxRows) {
                    break;
                }

            }

            itemComponentBagArray.add(bagArray);

            allItems.removeAll(copyArray, true);

            System.out.println(allItems.size);

        }

        currentlyShownItems = BagToEntity.bagsToEntities(world, itemComponentBagArray.get(currentlyShownIndex));

        arrowEntity(world, Measure.units(90f), CenterMath.offsetY(MainGame.GAME_HEIGHT, arrowSize) + Measure.units(3.25f), false);
        arrowEntity(world, Measure.units(5f), CenterMath.offsetY(MainGame.GAME_HEIGHT, arrowSize) + Measure.units(3.25f), true);

    }

    private Entity arrowEntity(World world, float x, float y, final boolean isLeft){
        Entity arrow = world.createEntity();

        arrow.edit().add(new PositionComponent(x, y));
        arrow.edit().add(new TextureRegionComponent(atlas.findRegion(TextureStrings.CHEVRON),
                arrowSize, arrowSize, TextureRegionComponent.FOREGROUND_LAYER_MIDDLE));
        arrow.edit().add(new CollisionBoundComponent(new Rectangle(x, y, arrowSize, arrowSize)));
        arrow.edit().add(new ActionOnTouchComponent(new Action() {
            @Override
            public void performAction(World world, Entity e) {
                for(Entity item : currentlyShownItems){
                    item.deleteFromWorld();
                }


                currentlyShownIndex = isLeft ? currentlyShownIndex - 1 : currentlyShownIndex + 1;

                if(currentlyShownIndex >= itemComponentBagArray.size){
                    currentlyShownIndex = 0;
                } else if(currentlyShownIndex < 0) {
                    currentlyShownIndex = itemComponentBagArray.size - 1;
                }

                currentlyShownItems = BagToEntity.bagsToEntities(world, itemComponentBagArray.get(currentlyShownIndex));


            }
        }));


        return arrow;

    }

    private ComponentBag itemIcon(Item item, float x, float y){
        ComponentBag bag = new ComponentBag();
        bag.add(new PositionComponent(x, y));

        boolean isItemCollected = DataSave.isItemCollected(item.getValues().id);

        TextureRegionComponent trc = new TextureRegionComponent(atlas.findRegion(item.getValues().region.getLeft(), item.getValues().region.getRight()),
                iconWidth, iconHeight, TextureRegionComponent.ENEMY_LAYER_MIDDLE, item.getValues().textureColor);

        if(!isItemCollected) {
            trc.color = new Color(Color.BLACK);
            trc.DEFAULT = new Color(Color.BLACK);
        }

        if(!DataSave.isDataAvailable(item.getValues().challengeId)){
            trc.region = atlas.findRegion(TextureStrings.SETTINGS_QUESTION_MARK);
        }

        bag.add(trc);

        return bag;

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

