package com.byrjamin.wickedwizard.item;

/**
 * Created by Home on 14/12/2016.
 */
public class Item {


    //Require Parameter
    private String spriteName;

    //Optional
    private float damageIncrease;
    private int healthIncrease;

    public static class ItemBuilder{

        //Required Parameters
        private String spriteName;

        //Optional Parameters
        private float damageIncrease = 0;
        private int healthIncrease = 0;

        public ItemBuilder(String spriteName) {
            this.spriteName = spriteName;
        }

        public ItemBuilder damageIncrease(float val)
        { damageIncrease = val; return this; }

        public ItemBuilder healthIncrease(int val)
        { healthIncrease = val; return this; }

        public Item build() {
            return new Item(this);
        }
    }

    public Item(ItemBuilder builder){
        spriteName = builder.spriteName;
        damageIncrease = builder.damageIncrease;
        healthIncrease = builder.healthIncrease;
    }

    public float getDamageIncrease() {
        return damageIncrease;
    }

    public int getHealthIncrease() {
        return healthIncrease;
    }

    public String getSpriteName() {
        return spriteName;
    }
}
