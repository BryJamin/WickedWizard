package com.byrjamin.wickedwizard.screens;

import com.artemis.World;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.SerializationException;
import com.byrjamin.wickedwizard.assets.PreferenceStrings;
import com.byrjamin.wickedwizard.ecs.components.CurrencyComponent;
import com.byrjamin.wickedwizard.ecs.components.StatComponent;
import com.byrjamin.wickedwizard.ecs.systems.FindPlayerSystem;
import com.byrjamin.wickedwizard.ecs.systems.level.ChangeLevelSystem;

/**
 * Created by Home on 29/07/2017.
 */

public class QuickSave {



    private static Json json = new Json();


    public static void saveGame(World world){

        SaveData saveData = new SaveData();
        saveData.setStatComponentJSON(json.toJson(world.getSystem(FindPlayerSystem.class).getPlayerComponent(StatComponent.class)));
        saveData.setLevelJSON(world.getSystem(ChangeLevelSystem.class).getLevel().name());
        saveData.setCurrencyJSON(json.toJson(world.getSystem(FindPlayerSystem.class).getPlayerComponent(CurrencyComponent.class)));
        saveData.setItemPoolJSON(json.toJson(world.getSystem(ChangeLevelSystem.class).getJigsawGenerator().getItemStore().getItemStringArray()));


        System.out.println(saveData.toString());

        String saveDataString = json.toJson(saveData);

        try {
            Preferences preferences = Gdx.app.getPreferences(PreferenceStrings.DATA_PREF_KEY);
            preferences.putString(PreferenceStrings.DATA_QUICK_SAVE, saveDataString);
            preferences.flush();
        } catch (SerializationException e){
            e.printStackTrace();
        }



    }



    private static class SaveData{

        private String statComponentJSON;
        private String levelJSON;
        private String itemsJSON;
        private String currencyJSON;
        private String itemPoolJSON;

        public String getStatComponentJSON() {
            return statComponentJSON;
        }

        public void setStatComponentJSON(String statComponentJSON) {
            this.statComponentJSON = statComponentJSON;
        }

        public String getLevelJSON() {
            return levelJSON;
        }

        public void setLevelJSON(String levelJSON) {
            this.levelJSON = levelJSON;
        }

        public String getItemsJSON() {
            return itemsJSON;
        }

        public void setItemsJSON(String itemsJSON) {
            this.itemsJSON = itemsJSON;
        }

        public String getCurrencyJSON() {
            return currencyJSON;
        }

        public void setCurrencyJSON(String currencyJSON) {
            this.currencyJSON = currencyJSON;
        }

        public String getItemPoolJSON() {
            return itemPoolJSON;
        }

        public void setItemPoolJSON(String itemPoolJSON) {
            this.itemPoolJSON = itemPoolJSON;
        }


        // SaveData


        @Override
        public String toString() {
            return "SaveData{" +
                    "statComponentJSON='" + statComponentJSON + '\'' +
                    ", levelJSON='" + levelJSON + '\'' +
                    ", itemsJSON='" + itemsJSON + '\'' +
                    ", currencyJSON='" + currencyJSON + '\'' +
                    ", itemPoolJSON='" + itemPoolJSON + '\'' +
                    '}';
        }
    }


}
