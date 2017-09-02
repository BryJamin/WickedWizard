package com.bryjamin.wickedwizard.screens;

import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Base64Coder;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.SerializationException;
import com.bryjamin.wickedwizard.assets.PreferenceStrings;
import com.bryjamin.wickedwizard.ecs.components.CurrencyComponent;
import com.bryjamin.wickedwizard.ecs.components.StatComponent;
import com.bryjamin.wickedwizard.ecs.components.ai.Action;
import com.bryjamin.wickedwizard.factories.arenas.JigsawGenerator;
import com.bryjamin.wickedwizard.factories.items.Item;

/**
 * Created by Home on 29/07/2017.
 */

public class QuickSave {



    private static Json json = new Json();


    public static void saveGame(World world){

        SaveData saveData = new SaveData();
        saveData.setStatComponentJSON(json.toJson(world.getSystem(com.bryjamin.wickedwizard.ecs.systems.FindPlayerSystem.class).getPlayerComponent(StatComponent.class)));
        saveData.setLevelJSON(world.getSystem(com.bryjamin.wickedwizard.ecs.systems.level.ChangeLevelSystem.class).getGameCreator().position + "");
        saveData.setCurrencyJSON(json.toJson(world.getSystem(com.bryjamin.wickedwizard.ecs.systems.FindPlayerSystem.class).getPlayerComponent(CurrencyComponent.class)));
        saveData.setItemPoolJSON(json.toJson(world.getSystem(com.bryjamin.wickedwizard.ecs.systems.level.ChangeLevelSystem.class).getJigsawGenerator().getItemStore().getItemStringArray()));

        String saveDataString = json.toJson(saveData);

        try {
            Preferences preferences = Gdx.app.getPreferences(PreferenceStrings.DATA_PREF_KEY);
            preferences.putString(PreferenceStrings.DATA_QUICK_SAVE, Base64Coder.encodeString(saveDataString));
            preferences.flush();
        } catch (SerializationException e){
            e.printStackTrace();
        }



    }



    public static boolean checkQuickSave(){

        Preferences preferences = Gdx.app.getPreferences(PreferenceStrings.DATA_PREF_KEY);
        String loadString = preferences.getString(PreferenceStrings.DATA_QUICK_SAVE, PreferenceStrings.DATA_QUICK_SAVE_NO_VALID_SAVE);

        try {
            SaveData saveData = json.fromJson(SaveData.class, Base64Coder.decodeString(loadString));
            json.fromJson(StatComponent.class, saveData.getStatComponentJSON());
            json.fromJson(CurrencyComponent.class, saveData.getCurrencyJSON());
            json.fromJson(Array.class, saveData.getItemPoolJSON());

            return true;

        } catch (Exception e){
            e.printStackTrace();

            if(!loadString.equals(PreferenceStrings.DATA_QUICK_SAVE_NO_VALID_SAVE)) {
                preferences.putString(PreferenceStrings.DATA_QUICK_SAVE, PreferenceStrings.DATA_QUICK_SAVE_NO_VALID_SAVE);
                preferences.flush();
            }

            return false;
        }

    }


    public static void loadQuickSave(World world){

        Preferences preferences = Gdx.app.getPreferences(PreferenceStrings.DATA_PREF_KEY);
        String loadString = preferences.getString(PreferenceStrings.DATA_QUICK_SAVE, PreferenceStrings.DATA_QUICK_SAVE_NO_VALID_SAVE);


        try {

/*            SaveData saveData = json.fromJson(SaveData.class, Base64Coder.decodeString(loadString));

            StatComponent s = world.getSystem(FindPlayerSystem.class).getPlayerComponent(StatComponent.class);
            s.applyStats(json.fromJson(StatComponent.class, saveData.getStatComponentJSON()));*/


            SaveData saveData = json.fromJson(SaveData.class, Base64Coder.decodeString(loadString));
            final com.bryjamin.wickedwizard.ecs.components.StatComponent s = world.getSystem(com.bryjamin.wickedwizard.ecs.systems.FindPlayerSystem.class).getPlayerComponent(StatComponent.class);
            final com.bryjamin.wickedwizard.ecs.components.StatComponent savedStats = json.fromJson(StatComponent.class, saveData.getStatComponentJSON());

            world.createEntity().edit().add(new com.bryjamin.wickedwizard.ecs.components.ai.OnRoomLoadActionComponent(new Action() {
                @Override
                public void performAction(World world, Entity e) {
                    for(Item i : savedStats.collectedItems){
                        i.applyEffect(world, world.getSystem(com.bryjamin.wickedwizard.ecs.systems.FindPlayerSystem.class).getPlayerEntity());
                    }

                    s.applyStats(savedStats);
                }
            }));


            CurrencyComponent currencyComponent = world.getSystem(com.bryjamin.wickedwizard.ecs.systems.FindPlayerSystem.class).getPlayerComponent(CurrencyComponent.class);
            currencyComponent.updateCurrency(json.fromJson(CurrencyComponent.class, saveData.getCurrencyJSON()));

            String level = json.fromJson(String.class, saveData.getLevelJSON());
            world.getSystem(com.bryjamin.wickedwizard.ecs.systems.level.ChangeLevelSystem.class).getGameCreator().setCurrentLevel(Integer.parseInt(level));

            JigsawGenerator jg = world.getSystem(com.bryjamin.wickedwizard.ecs.systems.level.ChangeLevelSystem.class).getJigsawGenerator();
            jg.getItemStore().updateItemOptions(json.fromJson(Array.class, String.class, saveData.getItemPoolJSON()));

            System.out.println(jg.getItemStore().toString());



            world.getSystem(com.bryjamin.wickedwizard.ecs.systems.level.MapTeleportationSystem.class).createNewLevel();

            preferences.putString(PreferenceStrings.DATA_QUICK_SAVE, PreferenceStrings.DATA_QUICK_SAVE_NO_VALID_SAVE);
            preferences.flush();

        } catch (Exception e){

            preferences.putString(PreferenceStrings.DATA_QUICK_SAVE, PreferenceStrings.DATA_QUICK_SAVE_NO_VALID_SAVE);
            preferences.flush();

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
