package com.byrjamin.wickedwizard.screens;

import com.artemis.Aspect;
import com.artemis.World;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.SerializationException;
import com.byrjamin.wickedwizard.assets.PreferenceStrings;
import com.byrjamin.wickedwizard.ecs.components.CurrencyComponent;
import com.byrjamin.wickedwizard.ecs.components.StatComponent;
import com.byrjamin.wickedwizard.ecs.systems.FindPlayerSystem;
import com.byrjamin.wickedwizard.ecs.systems.level.ChangeLevelSystem;
import com.byrjamin.wickedwizard.ecs.systems.level.MapTeleportationSystem;
import com.byrjamin.wickedwizard.ecs.systems.level.RoomTransitionSystem;
import com.byrjamin.wickedwizard.factories.arenas.JigsawGenerator;

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



    public static void loadQuickSave(World world){

        Preferences preferences = Gdx.app.getPreferences(PreferenceStrings.DATA_PREF_KEY);
        String loadString = preferences.getString(PreferenceStrings.DATA_QUICK_SAVE, PreferenceStrings.DATA_QUICK_SAVE_NO_VALID_SAVE);


        SaveData saveData = json.fromJson(SaveData.class, loadString);

        try {

            System.out.println(world.getAspectSubscriptionManager().get(Aspect.all()).getEntities().size());
            System.out.println("INSIFE QUICK SAVE SIZE" + world.getSystem(RoomTransitionSystem.class).getCurrentArena().getBagOfEntities().size());

            StatComponent s = world.getSystem(FindPlayerSystem.class).getPlayerComponent(StatComponent.class);
            s.applyStats(json.fromJson(StatComponent.class, saveData.getStatComponentJSON()));

            CurrencyComponent currencyComponent = world.getSystem(FindPlayerSystem.class).getPlayerComponent(CurrencyComponent.class);
            currencyComponent.updateCurrency(json.fromJson(CurrencyComponent.class, saveData.getCurrencyJSON()));

            String level = json.fromJson(String.class, saveData.getLevelJSON());
            world.getSystem(ChangeLevelSystem.class).setLevel(level);

            JigsawGenerator jg = world.getSystem(ChangeLevelSystem.class).getJigsawGenerator();
            jg.getItemStore().getItemStringArray().clear();
            jg.getItemStore().getItemStringArray().addAll(json.fromJson(Array.class, saveData.getItemPoolJSON()));


            //world.getSystem(RoomTransitionSystem.class).packRoom(world, world.getSystem(RoomTransitionSystem.class).getCurrentArena());

            world.getSystem(MapTeleportationSystem.class).createNewLevel();

            System.out.println("FINAL ENTITY SIZE" + world.getAspectSubscriptionManager().get(Aspect.all()).getEntities().size());

            preferences.putString(PreferenceStrings.DATA_QUICK_SAVE, PreferenceStrings.DATA_QUICK_SAVE_NO_VALID_SAVE);
            preferences.flush();

        } catch (SerializationException e){

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
