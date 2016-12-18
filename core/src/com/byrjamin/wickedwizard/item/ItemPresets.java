package com.byrjamin.wickedwizard.item;

/**
 * Presets of Items
 */
public class ItemPresets {

    public enum itemList {
        ATTACK_UP {
            @Override
            public Item getItem() {
                return new Item.ItemBuilder("fire").damageIncrease(0.2f).build();
            }
        },
        HEALTH_UP {
            @Override
            public Item getItem() {
                return new Item.ItemBuilder("frost").healthIncrease(1).build();
            }
        };

        public abstract Item getItem();
    }


}
