package com.bryjamin.wickedwizard.factories.items;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Array;
import com.bryjamin.wickedwizard.factories.arenas.challenges.ChallengesResource;
import com.bryjamin.wickedwizard.utils.Pair;
import com.bryjamin.wickedwizard.utils.enums.ItemType;

/**
 * Created by BB on 15/09/2017.
 */

public class ItemLayout {

    private final String id;
    private final com.bryjamin.wickedwizard.utils.Pair<String, Integer> region;
    private final Color textureColor;
    private final String name;
    private final String description;
    private final Array<ItemType> itemTypes;


    private boolean isSet = false;
    private String challengeId;


    public String getId() {
        return id;
    }

    public Pair<String, Integer> getRegion() {
        return region;
    }

    public Color getTextureColor() {
        return textureColor;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Array<ItemType> getItemTypes() {
        return itemTypes;
    }

    public String getChallengeId() {
        return challengeId;
    }

    public void setChallengeId(String challengeId) {
        if(!isSet) {
            this.challengeId = challengeId;
            isSet = true;
        }
    }

    public static class ItemValueBuilder {

        //Required
        private String id;

        //Optional
        private com.bryjamin.wickedwizard.utils.Pair<String, Integer> region = new com.bryjamin.wickedwizard.utils.Pair<String,Integer>("item/SarcasticLion", 0);
        private Color textureColor = new Color(Color.WHITE);
        private String name = "Default Item";
        private String description = "You forgot to set this value";
        private Array<com.bryjamin.wickedwizard.utils.enums.ItemType> itemTypes = new Array<com.bryjamin.wickedwizard.utils.enums.ItemType>();
        private String challengeId = ChallengesResource.TUTORIAL_COMPLETE;


        public ItemValueBuilder(String id){
            this.id = id;
            this.itemTypes.addAll(com.bryjamin.wickedwizard.utils.enums.ItemType.BOSS, com.bryjamin.wickedwizard.utils.enums.ItemType.ITEM, com.bryjamin.wickedwizard.utils.enums.ItemType.SHOP);
        }

        public ItemValueBuilder region(String s)
        { region = new com.bryjamin.wickedwizard.utils.Pair<String, Integer>(s,0); return this; }

        public ItemValueBuilder region(String s, int index)
        { region = new com.bryjamin.wickedwizard.utils.Pair<String, Integer>(s, index); return this; }


        public ItemValueBuilder textureColor(Color val)
        { textureColor = val; return this; }

        public ItemValueBuilder name(String val)
        { name = val; return this; }

        public ItemValueBuilder description(String val)
        { description = val; return this; }

        public ItemValueBuilder itemTypes(com.bryjamin.wickedwizard.utils.enums.ItemType... val) {
            itemTypes.clear();
            itemTypes.addAll(val);
            return this;
        }

        public ItemLayout build() {
            return new ItemLayout(this);
        }


    }

    public ItemLayout(ItemValueBuilder ivb){
        this.id = ivb.id;
        this.region = ivb.region;
        this.textureColor = ivb.textureColor;
        this.name = ivb.name;
        this.description = ivb.description;
        this.itemTypes = ivb.itemTypes;
        this.challengeId = ivb.challengeId;
    }


}
