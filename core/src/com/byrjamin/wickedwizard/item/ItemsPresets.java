package com.byrjamin.wickedwizard.item;

/**
 * Created by Home on 15/12/2016.
 */
public class ItemsPresets {

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
