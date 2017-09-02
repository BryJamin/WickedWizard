package com.bryjamin.wickedwizard.factories.arenas.presets;

import com.artemis.Component;
import com.artemis.utils.Bag;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.IntMap;
import com.bryjamin.wickedwizard.utils.Measure;

/**
 * Created by Home on 18/04/2017.
 */

public class ShopFactory extends com.bryjamin.wickedwizard.factories.AbstractFactory {

    //TODO Shop Tutorial Popup reads: Oh look a shop, buy items by believing in yourself

    private static final int pickUpPrice = 5;
    private static final int itemPrice = 15;



    private com.bryjamin.wickedwizard.factories.items.ItemFactory itemFactory;
    private com.bryjamin.wickedwizard.factories.arenas.skins.ArenaSkin arenaSkin;

/*    public ShopFactory(AssetManager assetManager) {
        super(assetManager);
        itemFactory = new ItemFactory(assetManager);
    }*/

    public ShopFactory(AssetManager assetManager) {
        super(assetManager);
        itemFactory = new com.bryjamin.wickedwizard.factories.items.ItemFactory(assetManager);
        this.arenaSkin = new com.bryjamin.wickedwizard.factories.arenas.skins.ShopSkin();

    }


    public com.bryjamin.wickedwizard.factories.arenas.Arena createShop(com.bryjamin.wickedwizard.utils.MapCoords defaultCoords) {

        com.bryjamin.wickedwizard.factories.arenas.Arena arena =  new com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder(assetManager, arenaSkin, com.bryjamin.wickedwizard.factories.arenas.Arena.ArenaType.SHOP)
                .addSection(new com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.Section(defaultCoords,
                        com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.DOOR,
                        com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.DOOR,
                        com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.GRAPPLE,
                        com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.DOOR))
                .buildArena();

        for(Bag<Component> b : itemFactory.createShopPickUpBag(Measure.units(20), Measure.units(37.5f), new com.bryjamin.wickedwizard.factories.items.pickups.PickUpHalfHealthUp(), pickUpPrice)) {
            arena.addEntity(b);
        }

        for(Bag<Component> b : itemFactory.createShopPickUpBag(Measure.units(40), Measure.units(37.5f), new com.bryjamin.wickedwizard.factories.items.pickups.PickUpArmorUp(), pickUpPrice)) {
            arena.addEntity(b);
        }

        for(Bag<Component> b : itemFactory.createShopItemBag(Measure.units(60), Measure.units(37.5f), itemPrice)) {
            arena.addEntity(b);
        }

        for(Bag<Component> b : itemFactory.createShopItemBag(Measure.units(80), Measure.units(37.5f), itemPrice)) {
            arena.addEntity(b);
        }

        Bag<Component> bag = new Bag<Component>();
        bag.add(new com.bryjamin.wickedwizard.ecs.components.movement.PositionComponent(Measure.units(67.5f), Measure.units(10f)));
        bag.add(new com.bryjamin.wickedwizard.ecs.components.movement.VelocityComponent(0, 0));
        bag.add(new com.bryjamin.wickedwizard.ecs.components.texture.AnimationStateComponent(0));
        IntMap<Animation<TextureRegion>> animMap = new IntMap<Animation<TextureRegion>>();
        animMap.put(0,  new Animation<TextureRegion>(0.1f, atlas.findRegions(com.bryjamin.wickedwizard.assets.TextureStrings.SHOPKEEPER), Animation.PlayMode.LOOP));
        bag.add(new com.bryjamin.wickedwizard.ecs.components.texture.AnimationComponent(animMap));
        bag.add(new com.bryjamin.wickedwizard.ecs.components.texture.TextureRegionComponent(atlas.findRegion(com.bryjamin.wickedwizard.assets.TextureStrings.SHOPKEEPER),
                0, 0, Measure.units(15f), Measure.units(15f),
                com.bryjamin.wickedwizard.ecs.components.texture.TextureRegionComponent.ENEMY_LAYER_MIDDLE));


        arena.addEntity(bag);



/*        Random random = new Random();


        for(ComponentBag b : ItemFactory.createItemAltarBag(arena.getWidth() / 2,
                Measure.units(12), items[random.nextInt(items.length)])) {
            arena.addEntity(b);
        }*/
        return arena;
    }


    private void createCheapItem(com.bryjamin.wickedwizard.factories.arenas.Arena arena, com.bryjamin.wickedwizard.utils.MapCoords arenaCoords){
        for(Bag<Component> b : itemFactory.createShopPickUpBag(arenaCoords.getX(),arenaCoords.getY(), new com.bryjamin.wickedwizard.factories.items.pickups.PickUpHalfHealthUp(), 2)) {
            arena.addEntity(b);
        }
    }






}
