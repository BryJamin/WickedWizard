package com.byrjamin.wickedwizard.factories.arenas;

import com.artemis.Component;
import com.artemis.utils.Bag;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.utils.Array;
import com.byrjamin.wickedwizard.factories.arenas.skins.ArenaSkin;
import com.byrjamin.wickedwizard.utils.MapCoords;
import com.byrjamin.wickedwizard.factories.enemy.BlobFactory;
import com.byrjamin.wickedwizard.factories.items.Item;
import com.byrjamin.wickedwizard.factories.items.ItemFactory;
import com.byrjamin.wickedwizard.factories.items.PickUp;
import com.byrjamin.wickedwizard.factories.items.passives.ChangeColor;
import com.byrjamin.wickedwizard.factories.items.passives.DamageUp;
import com.byrjamin.wickedwizard.factories.items.passives.FireRateUp;
import com.byrjamin.wickedwizard.factories.items.passives.PlusOne;
import com.byrjamin.wickedwizard.factories.items.pickups.HealthUp;
import com.byrjamin.wickedwizard.utils.Measure;

/**
 * Created by Home on 18/04/2017.
 */

public class ShopFactory extends ArenaShellFactory {

    private ItemFactory itemFactory;

/*    public ShopFactory(AssetManager assetManager) {
        super(assetManager);
        itemFactory = new ItemFactory(assetManager);
    }*/

    public ShopFactory(AssetManager assetManager, ArenaSkin arenaSkin) {
        super(assetManager, arenaSkin);
        itemFactory = new ItemFactory(assetManager);
    }

    public Arena createShop(){
        return createShop(new MapCoords(0,0));
    };

    public Arena createShop(MapCoords defaultCoords) {

        Array<MapCoords> containingCorrds = new Array<MapCoords>();
        containingCorrds.add(defaultCoords);

        Arena arena = new Arena(containingCorrds, Arena.RoomType.SHOP);

        arena.setWidth(SECTION_WIDTH);
        arena.setHeight(SECTION_HEIGHT);

        arena =  new ArenaBuilder(assetManager, arenaSkin)
                .addSection(new ArenaBuilder.Section(defaultCoords,
                        ArenaBuilder.wall.DOOR,
                        ArenaBuilder.wall.DOOR,
                        ArenaBuilder.wall.FULL,
                        ArenaBuilder.wall.FULL)).buildArena(arena);

        Item[] items = {new PlusOne(), new DamageUp(), new FireRateUp()};
        PickUp[] pickUps = {new HealthUp()};

/*        MapCoords[] locations = new MapCoords[]{ new MapCoords((int) Measure.units(20), (int) Measure.units(40)),
                new MapCoords((int) Measure.units(40), (int) Measure.units(40)),
                new MapCoords((int) Measure.units(60), (int) Measure.units(40)),
                new MapCoords((int) Measure.units(80), (int) Measure.units(40))};*/

        for(Bag<Component> b : itemFactory.createShopItemBag(Measure.units(20),Measure.units(40), new HealthUp(), 1)) {
            arena.addEntity(b);
        }

        for(Bag<Component> b : itemFactory.createShopItemBag(Measure.units(40),Measure.units(40), new DamageUp(), 5)) {
            arena.addEntity(b);
        }

        for(Bag<Component> b : itemFactory.createShopItemBag(Measure.units(60),Measure.units(40), new FireRateUp(), 5)) {
            arena.addEntity(b);
        }

        for(Bag<Component> b : itemFactory.createShopItemBag(Measure.units(80),Measure.units(40), new ChangeColor(), 5)) {
            arena.addEntity(b);
        }



        arena.addEntity(new BlobFactory(assetManager).shopKeeperBlob(arena.getWidth() / 2, arena.getHeight() / 4));



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
