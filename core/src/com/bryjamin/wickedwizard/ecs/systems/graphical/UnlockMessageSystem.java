package com.bryjamin.wickedwizard.ecs.systems.graphical;

import com.artemis.BaseSystem;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.utils.Bag;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.bryjamin.wickedwizard.assets.FileLocationStrings;
import com.bryjamin.wickedwizard.assets.FontAssets;
import com.bryjamin.wickedwizard.assets.MenuStrings;
import com.bryjamin.wickedwizard.assets.TextureStrings;
import com.bryjamin.wickedwizard.assets.resourcelayouts.ChallengeLayout;
import com.bryjamin.wickedwizard.ecs.components.movement.CollisionBoundComponent;
import com.bryjamin.wickedwizard.ecs.components.movement.PositionComponent;
import com.bryjamin.wickedwizard.ecs.components.texture.TextureFontComponent;
import com.bryjamin.wickedwizard.ecs.components.texture.TextureRegionComponent;
import com.bryjamin.wickedwizard.ecs.components.texture.UIComponent;
import com.bryjamin.wickedwizard.factories.arenas.challenges.ChallengesResource;
import com.bryjamin.wickedwizard.factories.items.Item;
import com.bryjamin.wickedwizard.factories.items.ItemResource;
import com.bryjamin.wickedwizard.utils.BagToEntity;
import com.bryjamin.wickedwizard.utils.CenterMath;
import com.bryjamin.wickedwizard.utils.ComponentBag;
import com.bryjamin.wickedwizard.utils.Measure;
import com.bryjamin.wickedwizard.utils.TableMath;

/**
 * Created by BB on 14/09/2017.
 */

public class UnlockMessageSystem extends BaseSystem {

    ComponentMapper<PositionComponent> pm;

    private Camera gamecam;
    private AssetManager assetManager;
    private TextureAtlas atlas;
    private boolean processingFlag = false;


    private Array<Bag<ComponentBag>> unlockMessages;


    /**
     * Creates an entity system that uses the specified aspect as a matcher
     * against entities.
     */
    public UnlockMessageSystem(Camera gamecam, AssetManager assetManager) {
        this.gamecam = gamecam;
        this.assetManager = assetManager;
        this.atlas = assetManager.get(FileLocationStrings.spriteAtlas, TextureAtlas.class);
    }


    @Override
    protected boolean checkProcessing() {
        return processingFlag;
    }


    @Override
    protected void processSystem() {

    }




    public void createChallengeUnlockMessage(String unlockId){

       // if(DataSave.isDataAvailable(unlockId)) return;


        Array<ChallengeLayout> unlockedChallenges = new Array<ChallengeLayout>();

        for(ChallengeLayout challengeLayout : ChallengesResource.getAllChallenges()){
            if(challengeLayout.getUnlockString().equals(unlockId)){
                unlockedChallenges.add(challengeLayout);
            }
        }

        if(unlockedChallenges.size <= 0) return;

        System.out.println("UNLOCKED CHALLENGE SIZE " + unlockedChallenges.size);

        Bag<ComponentBag> bagOfBags = new Bag<ComponentBag>();
        bagOfBags.add(createTitle(MenuStrings.CHALLENGE_UNLOCKED));
        bagOfBags.add(createBackDrop());

        BagToEntity.bagToEntity(world.createEntity(), createTitle(MenuStrings.CHALLENGE_UNLOCKED));
        BagToEntity.bagToEntity(world.createEntity(), createBackDrop());



        float camX = gamecam.position.x - gamecam.viewportWidth / 2;
        float camY = gamecam.position.y - gamecam.viewportHeight / 2;



        int maxColumns = 1;

        float iconSize = gamecam.viewportWidth;
        float iconGap = Measure.units(5f);

        float startY = camY + Measure.units(30f);
        float startX = camX + CenterMath.offsetX(gamecam.viewportWidth, (iconSize * maxColumns) + (iconGap * (maxColumns - 1)));


        for(int i = 0; i < unlockedChallenges.size; i++){

           // ComponentBag challengeText = new ComponentBag();

            Entity unlockedItemEntity = world.createEntity();

            float x = TableMath.getXPos(startX, i, maxColumns, iconSize, iconGap);
            float y = TableMath.getYPos(startY, i, maxColumns, iconGap, iconGap);


            System.out.println(x);
            System.out.println(y);

            unlockedItemEntity.edit().add(new PositionComponent(x, y));
            unlockedItemEntity.edit().add(new CollisionBoundComponent(new Rectangle(x, y, gamecam.viewportWidth, Measure.units(10f))));
            unlockedItemEntity.edit().add(new UIComponent());
            unlockedItemEntity.edit().add(new TextureFontComponent(FontAssets.medium, unlockedChallenges.get(i).getName()));

        }



    }


    private ComponentBag createTitle(String title){

        float camX = gamecam.position.x - gamecam.viewportWidth / 2;
        float camY = gamecam.position.y - gamecam.viewportHeight / 2;

        ComponentBag titleText = new ComponentBag();

        Rectangle textRectangle = new Rectangle(camX, camY, gamecam.viewportWidth, Measure.units(10f));
        titleText.add(new PositionComponent(camX, Measure.units(50f)));
        titleText.add(new CollisionBoundComponent(new Rectangle(textRectangle)));
        titleText.add(new UIComponent());
        titleText.add(new TextureFontComponent(FontAssets.medium, title));

        return titleText;
    }


    private ComponentBag createBackDrop(){

        float camX = gamecam.position.x - gamecam.viewportWidth / 2;
        float camY = gamecam.position.y - gamecam.viewportHeight / 2;

        ComponentBag backDrop = new ComponentBag();

        backDrop.add(new PositionComponent(camX, camY));
        backDrop.add(new UIComponent());
        backDrop.add(new TextureRegionComponent(atlas.findRegion(TextureStrings.BLOCK), gamecam.viewportWidth, gamecam.viewportHeight,
                TextureRegionComponent.BACKGROUND_LAYER_FAR,
                new Color(0f,0f,0f, 0.8f)));

        return backDrop;
    }



    public void createItemUnlockMessage(String unlockId){

        Array<Item> unlockedItems = new Array<Item>();

        for(Item i : ItemResource.getAllItems()){
            if(i.getValues().getChallengeId().equals(unlockId)){
                unlockedItems.add(i);
            }
        }


        if(unlockedItems.size <= 0) return;

        float camX = gamecam.position.x - gamecam.viewportWidth / 2;
        float camY = gamecam.position.y - gamecam.viewportHeight / 2;

        BagToEntity.bagToEntity(world.createEntity(), createBackDrop());
        BagToEntity.bagToEntity(world.createEntity(), createTitle(MenuStrings.ITEM_UNLOCKED));

        Entity smallText = world.createEntity();
        smallText.edit().add(new PositionComponent(camX, Measure.units(40)));
       // smallText.edit().add(new CollisionBoundComponent(new Rectangle(textRectangle)));
        smallText.edit().add(new UIComponent());
        smallText.edit().add(new TextureFontComponent(FontAssets.small, MenuStrings.ITEM_UNLOCKED));



        //ITEMS


        int maxColumns = unlockedItems.size < 8 ? unlockedItems.size : 8;

        float iconSize = Measure.units(10f);
        float iconGap = Measure.units(5f);

        float startY = camY + Measure.units(30f);
        float startX = camX + CenterMath.offsetX(gamecam.viewportWidth, (iconSize * maxColumns) + (iconGap * (maxColumns - 1)));
        //Create Item display


        for(int i = 0; i < unlockedItems.size; i++){

            Entity unlockedItemEntity = world.createEntity();

            float x = TableMath.getXPos(startX, i, maxColumns, iconSize, iconGap);
            float y = TableMath.getYPos(startY, i, maxColumns, iconSize, iconGap);

            unlockedItemEntity.edit().add(new PositionComponent(x, y));
            unlockedItemEntity.edit().add(new UIComponent());
            unlockedItemEntity.edit().add(new TextureRegionComponent(atlas.findRegion(unlockedItems.get(i).getValues().getRegion().getLeft(), unlockedItems.get(i).getValues().getRegion().getRight()),
                    iconSize, iconSize, TextureRegionComponent.ENEMY_LAYER_FAR, unlockedItems.get(i).getValues().getTextureColor()));

        }







    }





















}
