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
import com.bryjamin.wickedwizard.assets.TextureStrings;
import com.bryjamin.wickedwizard.ecs.components.ai.Action;
import com.bryjamin.wickedwizard.ecs.components.ai.ActionOnTouchComponent;
import com.bryjamin.wickedwizard.ecs.components.movement.CollisionBoundComponent;
import com.bryjamin.wickedwizard.ecs.components.movement.PositionComponent;
import com.bryjamin.wickedwizard.ecs.components.texture.TextureFontComponent;
import com.bryjamin.wickedwizard.ecs.components.texture.TextureRegionComponent;
import com.bryjamin.wickedwizard.ecs.systems.FindChildSystem;
import com.bryjamin.wickedwizard.ecs.systems.ai.FollowCameraSystem;
import com.bryjamin.wickedwizard.ecs.systems.ai.OnDeathSystem;
import com.bryjamin.wickedwizard.ecs.systems.graphical.AfterUIRenderingSystem;
import com.bryjamin.wickedwizard.ecs.systems.graphical.AnimationSystem;
import com.bryjamin.wickedwizard.ecs.systems.graphical.BoundsDrawingSystem;
import com.bryjamin.wickedwizard.ecs.systems.graphical.FadeSystem;
import com.bryjamin.wickedwizard.ecs.systems.graphical.MessageBannerSystem;
import com.bryjamin.wickedwizard.ecs.systems.graphical.RenderingSystem;
import com.bryjamin.wickedwizard.ecs.systems.input.ActionOnTouchSystem;
import com.bryjamin.wickedwizard.ecs.systems.physics.MovementSystem;
import com.bryjamin.wickedwizard.factories.items.Item;
import com.bryjamin.wickedwizard.factories.items.ItemResource;
import com.bryjamin.wickedwizard.screens.DataSave;
import com.bryjamin.wickedwizard.screens.MenuButton;
import com.bryjamin.wickedwizard.screens.MenuScreen;
import com.bryjamin.wickedwizard.screens.world.WorldContainer;
import com.bryjamin.wickedwizard.utils.AbstractGestureDectector;
import com.bryjamin.wickedwizard.utils.BagToEntity;
import com.bryjamin.wickedwizard.utils.CenterMath;
import com.bryjamin.wickedwizard.utils.ComponentBag;
import com.bryjamin.wickedwizard.utils.GameDelta;
import com.bryjamin.wickedwizard.utils.Measure;

/**
 * Created by BB on 22/08/2017.
 */

public class ItemDisplayWorldContainer extends AbstractGestureDectector implements WorldContainer {

    private final MainGame game;
    private final Viewport gameport;
    private final TextureAtlas atlas;

    private static final float arrowSize = Measure.units(15f);

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
                        new MessageBannerSystem(atlas.findRegion(TextureStrings.BLOCK), gameport.getCamera()),
                        new OnDeathSystem(),
                        new FindChildSystem(),
                        new FadeSystem())
                .with(WorldConfigurationBuilder.Priority.LOW,
                        new RenderingSystem(game.batch, game.assetManager, gameport),
                        new AfterUIRenderingSystem(game, gameport),
                        new FollowCameraSystem(gameport.getCamera()),
                        new BoundsDrawingSystem()
                )
                .build();


        world = new World(config);

        setUpTitle(world, MenuStrings.ITEMS, MenuStrings.TAP_AN_ITEM);
        setUpMainMenuButton(world);

        int count = 0;
        float startY = Measure.units(37.5f);
        float buttonGap = Measure.units(2.5f);

        int maxColumns = 9;
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



        float arrowY = Measure.units(45f);

        arrowEntity(world, Measure.units(5f), arrowY, true);
        arrowEntity(world, gameport.getCamera().viewportWidth - arrowSize - Measure.units(5f), arrowY, false);

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

    private ComponentBag itemIcon(final Item item, float x, float y){
        ComponentBag bag = new ComponentBag();
        bag.add(new PositionComponent(x, y));
        bag.add(new CollisionBoundComponent(new Rectangle(x, y, iconWidth, iconHeight)));

        bag.add(new ActionOnTouchComponent(new Action() {
            @Override
            public void performAction(World world, Entity e) {

                if(DataSave.isItemCollected(item.getValues().getId())) {
                    world.getSystem(MessageBannerSystem.class).createItemBanner(item.getValues().getName(), item.getValues().getDescription(), Measure.units(17.5f));
                } else if(DataSave.isDataAvailable(item.getValues().getChallengeId())){
                    world.getSystem(MessageBannerSystem.class).createItemBanner(MenuStrings.UNIDENTIFIED_ITEM, MenuStrings.UNIDENTIFIED_ITEM_DESCRIPTION,  Measure.units(17.5f));
                }
            }
        }));

        boolean isItemCollected = DataSave.isItemCollected(item.getValues().getId());

        TextureRegionComponent trc = new TextureRegionComponent(atlas.findRegion(item.getValues().getRegion().getLeft(), item.getValues().getRegion().getRight()),
                iconWidth, iconHeight, TextureRegionComponent.ENEMY_LAYER_MIDDLE, item.getValues().getTextureColor());

        if(!isItemCollected) {
            trc.color = new Color(0.2f,0.2f,0.2f, 1);
            trc.DEFAULT = new Color(0.2f,0.2f,0.2f, 1);
        }

        if(!DataSave.isDataAvailable(item.getValues().getChallengeId())){
            trc.region = atlas.findRegion(TextureStrings.SETTINGS_QUESTION_MARK);
        }

        bag.add(trc);

        return bag;

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

