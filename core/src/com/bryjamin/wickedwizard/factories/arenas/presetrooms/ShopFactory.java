package com.bryjamin.wickedwizard.factories.arenas.presetrooms;

import com.artemis.Component;
import com.artemis.utils.Bag;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.IntMap;
import com.bryjamin.wickedwizard.assets.TextureStrings;
import com.bryjamin.wickedwizard.ecs.components.movement.PositionComponent;
import com.bryjamin.wickedwizard.ecs.components.movement.VelocityComponent;
import com.bryjamin.wickedwizard.ecs.components.texture.AnimationComponent;
import com.bryjamin.wickedwizard.ecs.components.texture.AnimationStateComponent;
import com.bryjamin.wickedwizard.ecs.components.texture.TextureRegionComponent;
import com.bryjamin.wickedwizard.factories.AbstractFactory;
import com.bryjamin.wickedwizard.factories.arenas.Arena;
import com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder;
import com.bryjamin.wickedwizard.factories.arenas.skins.ArenaSkin;
import com.bryjamin.wickedwizard.factories.arenas.skins.ShopSkin;
import com.bryjamin.wickedwizard.factories.items.ItemFactory;
import com.bryjamin.wickedwizard.factories.items.pickups.PickUpArmorUp;
import com.bryjamin.wickedwizard.factories.items.pickups.PickUpHalfHealthUp;
import com.bryjamin.wickedwizard.utils.MapCoords;
import com.bryjamin.wickedwizard.utils.Measure;

/**
 * Created by Home on 18/04/2017.
 */

public class ShopFactory extends AbstractFactory {

    //TODO Shop Tutorial Popup reads: Oh look a shop, buy items by believing in yourself

    private static final int pickUpPrice = 5;
    private static final int itemPrice = 15;



    private ItemFactory itemFactory;
    private ArenaSkin arenaSkin;

    public ShopFactory(AssetManager assetManager) {
        super(assetManager);
        itemFactory = new ItemFactory(assetManager);
        this.arenaSkin = new ShopSkin();

    }


    public Arena createShop(MapCoords defaultCoords) {

        Arena arena =  new ArenaBuilder(assetManager, arenaSkin, Arena.ArenaType.SHOP)
                .addSection(new ArenaBuilder.Section(defaultCoords,
                        ArenaBuilder.wall.DOOR,
                        ArenaBuilder.wall.DOOR,
                        ArenaBuilder.wall.GRAPPLE,
                        ArenaBuilder.wall.DOOR))
                .buildArena();

        for(Bag<Component> b : itemFactory.createShopPickUpBag(Measure.units(20), Measure.units(37.5f), new PickUpHalfHealthUp(), pickUpPrice)) {
            arena.addEntity(b);
        }

        for(Bag<Component> b : itemFactory.createShopPickUpBag(Measure.units(40), Measure.units(37.5f), new PickUpArmorUp(), pickUpPrice)) {
            arena.addEntity(b);
        }

        for(Bag<Component> b : itemFactory.createShopItemBag(Measure.units(60), Measure.units(37.5f), itemPrice)) {
            arena.addEntity(b);
        }

        for(Bag<Component> b : itemFactory.createShopItemBag(Measure.units(80), Measure.units(37.5f), itemPrice)) {
            arena.addEntity(b);
        }

        Bag<Component> bag = new Bag<Component>();
        bag.add(new PositionComponent(Measure.units(67.5f), Measure.units(10f)));
        bag.add(new VelocityComponent(0, 0));
        bag.add(new AnimationStateComponent(0));
        IntMap<Animation<TextureRegion>> animMap = new IntMap<Animation<TextureRegion>>();
        animMap.put(0,  new Animation<TextureRegion>(0.1f, atlas.findRegions(TextureStrings.SHOPKEEPER), Animation.PlayMode.LOOP));
        bag.add(new AnimationComponent(animMap));
        bag.add(new TextureRegionComponent(atlas.findRegion(TextureStrings.SHOPKEEPER),
                0, 0, Measure.units(15f), Measure.units(15f),
                TextureRegionComponent.ENEMY_LAYER_MIDDLE));


        arena.addEntity(bag);


        return arena;
    }






}
