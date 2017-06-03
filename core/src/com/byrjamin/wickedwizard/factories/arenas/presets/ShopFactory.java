package com.byrjamin.wickedwizard.factories.arenas.presets;

import com.artemis.Component;
import com.artemis.utils.Bag;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.IntMap;
import com.byrjamin.wickedwizard.ecs.components.movement.PositionComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.VelocityComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.AnimationComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.AnimationStateComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.TextureRegionComponent;
import com.byrjamin.wickedwizard.factories.arenas.Arena;
import com.byrjamin.wickedwizard.factories.arenas.ArenaBuilder;
import com.byrjamin.wickedwizard.factories.arenas.skins.ArenaSkin;
import com.byrjamin.wickedwizard.factories.arenas.skins.ShopSkin;
import com.byrjamin.wickedwizard.factories.items.pickups.KeyUp;
import com.byrjamin.wickedwizard.utils.MapCoords;
import com.byrjamin.wickedwizard.factories.items.Item;
import com.byrjamin.wickedwizard.factories.items.ItemFactory;
import com.byrjamin.wickedwizard.factories.items.pickups.HealthUp;
import com.byrjamin.wickedwizard.utils.Measure;

/**
 * Created by Home on 18/04/2017.
 */

public class ShopFactory extends com.byrjamin.wickedwizard.factories.arenas.decor.ArenaShellFactory {

    //TODO Shop Tutorial Popup reads: Oh look a shop, buy items by believing in yourself


    private ItemFactory itemFactory;

/*    public ShopFactory(AssetManager assetManager) {
        super(assetManager);
        itemFactory = new ItemFactory(assetManager);
    }*/

    public ShopFactory(AssetManager assetManager, ArenaSkin arenaSkin) {
        super(assetManager, arenaSkin);
        itemFactory = new ItemFactory(assetManager);
        this.arenaSkin = new ShopSkin(atlas);

    }

    public Arena createShop(Item item1, Item item2){
        return createShop(new MapCoords(0,0), item1, item2);
    };

    public Arena createShop(MapCoords defaultCoords, Item item1, Item item2) {

        Arena arena = new Arena(Arena.RoomType.SHOP, arenaSkin, defaultCoords);

        arena.setWidth(SECTION_WIDTH);
        arena.setHeight(SECTION_HEIGHT);

        arena =  new ArenaBuilder(assetManager, arenaSkin)
                .addSection(new ArenaBuilder.Section(defaultCoords,
                        ArenaBuilder.wall.DOOR,
                        ArenaBuilder.wall.DOOR,
                        ArenaBuilder.wall.FULL,
                        ArenaBuilder.wall.FULL)).buildArena(arena);
        

/*        MapCoords[] locations = new MapCoords[]{ new MapCoords((int) Measure.units(20), (int) Measure.units(40)),
                new MapCoords((int) Measure.units(40), (int) Measure.units(40)),
                new MapCoords((int) Measure.units(60), (int) Measure.units(40)),
                new MapCoords((int) Measure.units(80), (int) Measure.units(40))};*/

        for(Bag<Component> b : itemFactory.createShopItemBag(Measure.units(20),Measure.units(40), new HealthUp(), 5)) {
            arena.addEntity(b);
        }

        for(Bag<Component> b : itemFactory.createShopItemBag(Measure.units(40),Measure.units(40), new KeyUp(), 5)) {
            arena.addEntity(b);
        }

        for(Bag<Component> b : itemFactory.createShopItemBag(Measure.units(60),Measure.units(40), item1, 10)) {
            arena.addEntity(b);
        }

        for(Bag<Component> b : itemFactory.createShopItemBag(Measure.units(80),Measure.units(40), item2, 10)) {
            arena.addEntity(b);
        }





        Bag<Component> bag = new Bag<Component>();
        bag.add(new PositionComponent(arena.getWidth() / 2, Measure.units(10f)));
        bag.add(new VelocityComponent(0, 0));
        bag.add(new AnimationStateComponent(0));
        IntMap<Animation<TextureRegion>> animMap = new IntMap<Animation<TextureRegion>>();
        animMap.put(0,  new Animation<TextureRegion>(0.1f, atlas.findRegions("shopkeeper"), Animation.PlayMode.LOOP));
        bag.add(new AnimationComponent(animMap));
        bag.add(new TextureRegionComponent(atlas.findRegion("shopkeeper"),
                0, 0, Measure.units(15f), Measure.units(15f),
                TextureRegionComponent.ENEMY_LAYER_MIDDLE));


        arena.addEntity(bag);



/*        Random random = new Random();


        for(ComponentBag b : ItemFactory.createItemAltarBag(arena.getWidth() / 2,
                Measure.units(12), items[random.nextInt(items.length)])) {
            arena.addEntity(b);
        }*/
        return arena;
    }


    private void createCheapItem(Arena arena, MapCoords arenaCoords){
        for(Bag<Component> b : itemFactory.createShopItemBag(arenaCoords.getX(),arenaCoords.getY(), new HealthUp(), 2)) {
            arena.addEntity(b);
        }
    }






}
