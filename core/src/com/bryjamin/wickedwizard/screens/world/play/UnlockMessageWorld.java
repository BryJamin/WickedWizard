package com.bryjamin.wickedwizard.screens.world.play;

import com.artemis.Entity;
import com.artemis.World;
import com.artemis.WorldConfiguration;
import com.artemis.WorldConfigurationBuilder;
import com.artemis.utils.Bag;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Rectangle;
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
import com.bryjamin.wickedwizard.ecs.components.ai.ExpireComponent;
import com.bryjamin.wickedwizard.ecs.components.ai.FollowCameraComponent;
import com.bryjamin.wickedwizard.ecs.components.ai.FollowPositionComponent;
import com.bryjamin.wickedwizard.ecs.components.identifiers.ChildComponent;
import com.bryjamin.wickedwizard.ecs.components.identifiers.ParentComponent;
import com.bryjamin.wickedwizard.ecs.components.movement.CollisionBoundComponent;
import com.bryjamin.wickedwizard.ecs.components.movement.PositionComponent;
import com.bryjamin.wickedwizard.ecs.components.texture.FadeComponent;
import com.bryjamin.wickedwizard.ecs.components.texture.TextureFontComponent;
import com.bryjamin.wickedwizard.ecs.components.texture.TextureRegionComponent;
import com.bryjamin.wickedwizard.ecs.systems.FindChildSystem;
import com.bryjamin.wickedwizard.ecs.systems.ai.FollowCameraSystem;
import com.bryjamin.wickedwizard.ecs.systems.ai.FollowPositionSystem;
import com.bryjamin.wickedwizard.ecs.systems.ai.OnDeathSystem;
import com.bryjamin.wickedwizard.ecs.systems.graphical.AnimationSystem;
import com.bryjamin.wickedwizard.ecs.systems.graphical.BoundsDrawingSystem;
import com.bryjamin.wickedwizard.ecs.systems.graphical.CameraSystem;
import com.bryjamin.wickedwizard.ecs.systems.graphical.FadeSystem;
import com.bryjamin.wickedwizard.ecs.systems.graphical.RenderingSystem;
import com.bryjamin.wickedwizard.ecs.systems.input.ActionOnTouchSystem;
import com.bryjamin.wickedwizard.ecs.systems.physics.MovementSystem;
import com.bryjamin.wickedwizard.factories.arenas.challenges.ChallengeLayout;
import com.bryjamin.wickedwizard.factories.arenas.challenges.ChallengesResource;
import com.bryjamin.wickedwizard.factories.items.Item;
import com.bryjamin.wickedwizard.factories.items.ItemResource;
import com.bryjamin.wickedwizard.screens.DataSave;
import com.bryjamin.wickedwizard.screens.PlayScreen;
import com.bryjamin.wickedwizard.screens.world.WorldContainer;
import com.bryjamin.wickedwizard.utils.BagToEntity;
import com.bryjamin.wickedwizard.utils.CenterMath;
import com.bryjamin.wickedwizard.utils.ComponentBag;
import com.bryjamin.wickedwizard.utils.GameDelta;
import com.bryjamin.wickedwizard.utils.Measure;
import com.bryjamin.wickedwizard.utils.TableMath;

/**
 * Created by BB on 18/09/2017.
 */

public class UnlockMessageWorld implements WorldContainer {

    private SpriteBatch batch;
    private MainGame game;
    private AssetManager manager;
    private TextureAtlas atlas;
    private Viewport gameport;
    private Camera gamecam;

    private World world;

    public UnlockMessageWorld(MainGame game, Viewport gameport){
        this.game = game;
        this.batch = game.batch;
        this.manager = game.assetManager;
        this.atlas = manager.get(FileLocationStrings.spriteAtlas);
        this.gameport = gameport;
        this.gamecam = gameport.getCamera();
        createWorld();
    }



    @Override
    public void createWorld() {

        WorldConfiguration config = new WorldConfigurationBuilder()
                .with(WorldConfigurationBuilder.Priority.HIGHEST,
                        new MovementSystem()
                )
                .with(WorldConfigurationBuilder.Priority.HIGH,
                        new AnimationSystem(),
                        new OnDeathSystem(),
                        new FindChildSystem(),
                        new ActionOnTouchSystem(),
                        //new FindPlayerSystem(player),
                        new FollowPositionSystem(),
                        new FadeSystem())
                .with(WorldConfigurationBuilder.Priority.LOW,
                        new FollowCameraSystem(gamecam),
                        new RenderingSystem(batch, manager, gameport),
                        new BoundsDrawingSystem()
                )
                .build();

        world = new World(config);

    }

    @Override
    public void process(float delta) {
        GameDelta.delta(world, delta);
    }

    @Override
    public World getWorld() {
        return world;
    }

    /**
     * Depending on the Unlock Id creates a message to the player to show which items and Challenges they have unlock
     *
     * @param unlockIds
     */
    public boolean createUnlockMessage(String... unlockIds){


        final Array<Bag<ComponentBag>> bagArray = new Array<Bag<ComponentBag>>();


        Array<String> unlockIdArray = new Array<String>(unlockIds);
        //All Separated Since The Unlock Display Order is

        //Character -> Item -> Challenges

        //Which means that are added to the array Challenges -> Item -> Character

        for(String unlockId: unlockIds){
            if (!DataSave.isDataAvailable(unlockId)) {
                DataSave.saveChallengeData(unlockId);
                unlockIdArray.add(unlockId);
            }
        }

        for(String unlockId : unlockIdArray) createChallengeUnlockMessage(bagArray, unlockId);
        for(String unlockId : unlockIdArray) createItemUnlockMessage(bagArray, unlockId);
        for(String unlockId : unlockIdArray) createCharacterUnlockMessage(bagArray, unlockId);

        if(bagArray.size <= 0) return false;


        Bag<Entity> entityBag = BagToEntity.bagsToEntities(world, bagArray.pop());

        float camX = gamecam.position.x - gamecam.viewportWidth / 2;
        float camY = gamecam.position.y - gamecam.viewportHeight / 2;

        ParentComponent pc = new ParentComponent();

        for(Entity e: entityBag){
            e.edit().add(new ChildComponent(pc));
        }

        Entity next = world.createEntity();
        next.edit().add(pc);
        next.edit().add(new PositionComponent(camX, camY + Measure.units(5f)));
        next.edit().add(CameraSystem.createFollowCameraComponent(gamecam, 0, Measure.units(5f)));
        next.edit().add(new CollisionBoundComponent(new Rectangle(camX, camY + Measure.units(5f), Measure.units(50f), Measure.units(10f))));
        next.edit().add(new TextureFontComponent(FontAssets.medium, MenuStrings.TAP_TO_CONTINUE));
        next.edit().add(new ActionOnTouchComponent(new Action() {
            @Override
            public void performAction(World world, Entity e) {

                ParentComponent pc = e.getComponent(ParentComponent.class);
                world.getSystem(OnDeathSystem.class).killChildComponentsIgnoreOnDeath(pc);

                if(bagArray.size > 0){
                    for(ComponentBag components : bagArray.pop()){
                        BagToEntity.bagToEntity(world.createEntity(), components).edit().add(new ChildComponent(pc));
                    }
                } else {
                    e.deleteFromWorld();
                    PlayScreen playScreen = (PlayScreen) game.getScreen();
                    playScreen.unpause();
                }


            }
        }));

        Entity flash = world.createEntity();

        flash.edit().add(new PositionComponent(camX, camY));
        flash.edit().add(new TextureRegionComponent(atlas.findRegion(TextureStrings.BLOCK), gamecam.viewportWidth, gamecam.viewportHeight,
                TextureRegionComponent.FOREGROUND_LAYER_NEAR,
                new Color(Color.WHITE)));
        flash.edit().add(CameraSystem.createFollowCameraComponent(gamecam, 0, 0));
        flash.edit().add(new FadeComponent(false, 1f, false));
        flash.edit().add(new ExpireComponent(1f));


        return true;

    }


    private ComponentBag createTitle(String title){

        float camX = gamecam.position.x - gamecam.viewportWidth / 2;
        float camY = gamecam.position.y - gamecam.viewportHeight / 2;

        ComponentBag titleText = new ComponentBag();

        Rectangle textRectangle = new Rectangle(camX, camY, gamecam.viewportWidth, Measure.units(10f));
        titleText.add(new PositionComponent(camX, Measure.units(55f)));
        titleText.add(new FollowPositionComponent(gamecam.position, -gamecam.viewportWidth / 2, -gamecam.viewportHeight / 2 + Measure.units(55f)));
        titleText.add(new CollisionBoundComponent(new Rectangle(textRectangle)));
        titleText.add(new TextureFontComponent(FontAssets.medium, title));

        return titleText;
    }


    private ComponentBag createBackDrop(){

        float camX = gamecam.position.x - gamecam.viewportWidth / 2;
        float camY = gamecam.position.y - gamecam.viewportHeight / 2;

        ComponentBag backDrop = new ComponentBag();

        backDrop.add(new PositionComponent(camX, camY));
        backDrop.add(new FollowPositionComponent(gamecam.position, -gamecam.viewportWidth / 2, -gamecam.viewportHeight / 2));
        backDrop.add(new TextureRegionComponent(atlas.findRegion(TextureStrings.BLOCK), gamecam.viewportWidth, gamecam.viewportHeight,
                TextureRegionComponent.BACKGROUND_LAYER_FAR,
                new Color(0f,0f,0f, 0.8f)));

        return backDrop;
    }


    public void createItemUnlockMessage(Array<Bag<ComponentBag>> bagArray, String unlockId){

        Array<Item> unlockedItems = new Array<Item>();

        for(Item i : ItemResource.getAllItems()){
            if(i.getValues().getChallengeId().equals(unlockId)){
                unlockedItems.add(i);
            }
        }


        if(unlockedItems.size <= 0) return;

        //ITEMS

        float iconSize = Measure.units(10f);

        float y = CenterMath.offsetY(gamecam.viewportHeight, iconSize) + Measure.units(5f);
        float x = CenterMath.offsetX(gamecam.viewportWidth, iconSize);

        for(int i = 0; i < unlockedItems.size; i++){

            Bag<ComponentBag> bagOfBags = new Bag<ComponentBag>();
            bagOfBags.add(createBackDrop());
            bagOfBags.add(createTitle(MenuStrings.ITEM_UNLOCKED));

            ComponentBag unlockedItemBag = new ComponentBag();

            unlockedItemBag.add(new PositionComponent(x, y));
            unlockedItemBag.add(CameraSystem.createFollowCameraComponent(gamecam, x, y));
            unlockedItemBag.add(new TextureRegionComponent(atlas.findRegion(unlockedItems.get(i).getValues().getRegion().getLeft(), unlockedItems.get(i).getValues().getRegion().getRight()),
                    iconSize, iconSize, TextureRegionComponent.ENEMY_LAYER_FAR, unlockedItems.get(i).getValues().getTextureColor()));

            bagOfBags.add(unlockedItemBag);


            ComponentBag textBelow = new ComponentBag();
            textBelow.add(new PositionComponent(x, y - Measure.units(5.5f)));
            textBelow.add(CameraSystem.createFollowCameraComponent(gamecam, x, y - Measure.units(5.5f)));
            textBelow.add(new TextureFontComponent(FontAssets.medium, "\"" + unlockedItems.get(i).getValues().getName() + "\""));
            textBelow.add(new CollisionBoundComponent(new Rectangle(x, y - Measure.units(5f), iconSize, Measure.units(5f))));
            bagOfBags.add(textBelow);

            bagArray.add(bagOfBags);

        }


    }



    public void createChallengeUnlockMessage(Array<Bag<ComponentBag>> bagArray, String unlockId){

        Array<ChallengeLayout> unlockedChallenges = new Array<ChallengeLayout>();

        for(ChallengeLayout challengeLayout : ChallengesResource.getAllChallenges()){
            if(challengeLayout.getStringToUnlockChallenge().equals(unlockId)){
                unlockedChallenges.add(challengeLayout);
            }
        }

        if(unlockedChallenges.size <= 0) return;

        Bag<ComponentBag> bagOfBags = new Bag<ComponentBag>();
        bagOfBags.add(createTitle(unlockedChallenges.size > 1 ? MenuStrings.CHALLENGE_UNLOCKED_PLURAL : MenuStrings.CHALLENGE_UNLOCKED));
        bagOfBags.add(createBackDrop());

        int maxColumns = 1;

        float iconSize = gamecam.viewportWidth;
        float iconGap = Measure.units(5f);

        float x = CenterMath.offsetX(gamecam.viewportWidth, iconSize);


        for(int i = 0; i < unlockedChallenges.size; i++){

            ComponentBag unlockedItemBag = new ComponentBag();

            float y = TableMath.getYPosTopToBottom(Measure.units(45f), i, maxColumns, iconGap, iconGap);

            unlockedItemBag.add(new PositionComponent(x, y));
            unlockedItemBag.add(new CollisionBoundComponent(new Rectangle(x, y, gamecam.viewportWidth, Measure.units(10f))));
            unlockedItemBag.add(CameraSystem.createFollowCameraComponent(gamecam, x, y));
            unlockedItemBag.add(new TextureFontComponent(FontAssets.medium, unlockedChallenges.get(i).getName()));

            bagOfBags.add(unlockedItemBag);

        }

        bagArray.add(bagOfBags);

    }



    public void createCharacterUnlockMessage(Array<Bag<ComponentBag>> bagArray, String unlockId){

        Array<PlayerIDs.PlayableCharacter> unlockedCharacters = new Array<PlayerIDs.PlayableCharacter>();



        for(PlayerIDs.PlayableCharacter i : PlayerIDs.getAllCharacters()){
            if(i.getUnlockString().equals(unlockId)){
                unlockedCharacters.add(i);
            }
        }

        if(unlockedCharacters.size <= 0) return;

        //ITEMS

        float iconSize = Measure.units(12.5f);

        float y = CenterMath.offsetY(gamecam.viewportHeight, iconSize) + Measure.units(5f);
        float x = CenterMath.offsetX(gamecam.viewportWidth, iconSize);

        for(int i = 0; i < unlockedCharacters.size; i++){

            Bag<ComponentBag> bagOfBags = new Bag<ComponentBag>();
            bagOfBags.add(createBackDrop());
            bagOfBags.add(createTitle(MenuStrings.CHARACTER_UNLOCKED));

            ComponentBag unlockedItemBag = new ComponentBag();

            unlockedItemBag.add(new PositionComponent(x, y));
            unlockedItemBag.add(new FollowCameraComponent(x, y));
            unlockedItemBag.add(new TextureRegionComponent(atlas.findRegion(unlockedCharacters.get(i).getRegion()),
                    iconSize, iconSize, TextureRegionComponent.ENEMY_LAYER_FAR));

            bagOfBags.add(unlockedItemBag);


            ComponentBag textBelow = new ComponentBag();
            textBelow.add(new PositionComponent(x, y - Measure.units(7.5f)));
            textBelow.add(new FollowCameraComponent(x, y - Measure.units(7.5f)));
            textBelow.add(new TextureFontComponent(FontAssets.medium, "\"" + unlockedCharacters.get(i).getName() + "\""));
            textBelow.add(new CollisionBoundComponent(new Rectangle(x, y - Measure.units(5f), iconSize, Measure.units(5f))));
            bagOfBags.add(textBelow);

            bagArray.add(bagOfBags);

        }

    }




}
