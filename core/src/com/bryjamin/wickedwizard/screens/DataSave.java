package com.bryjamin.wickedwizard.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Base64Coder;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.OrderedMap;
import com.bryjamin.wickedwizard.assets.FileLocationStrings;
import com.bryjamin.wickedwizard.assets.PreferenceStrings;

/**
 * Created by BB on 16/08/2017.
 */

public class DataSave {

    private static Json json = new Json();

    private static DataSaveStore dataSaveStore = new DataSaveStore();



    static {

        Preferences preferences = Gdx.app.getPreferences(PreferenceStrings.DATA_PREF_KEY);
        FileHandle file = Gdx.files.local(FileLocationStrings.playerData);

        try {
            if(file.exists()) {
                dataSaveStore = json.fromJson(DataSaveStore.class, Base64Coder.decodeString(file.readString()));
            }
        } catch(Exception e){
            e.printStackTrace();
        }

        if (preferences.contains(PreferenceStrings.DATA_PERMANENT_DATA)) {
            try {

                    String loadString = preferences.getString(PreferenceStrings.DATA_PERMANENT_DATA, PreferenceStrings.DATA_QUICK_SAVE_NO_VALID_SAVE);

                    if (!loadString.equals(PreferenceStrings.DATA_QUICK_SAVE_NO_VALID_SAVE)) {

                        DataSaveStore temp = json.fromJson(DataSaveStore.class, Base64Coder.decodeString(loadString));

                        for (String key : temp.keyStoreBoolean.keys().toArray()) {
                            if (!dataSaveStore.keyStoreBoolean.containsKey(key)) {
                                dataSaveStore.keyStoreBoolean.put(key, false);
                            }
                        }

                        for (String key : temp.itemStoreBoolean.keys().toArray()) {
                            if (!dataSaveStore.itemStoreBoolean.containsKey(key)) {
                                dataSaveStore.itemStoreBoolean.put(key, false);
                            }
                        }
                    }

                    preferences.remove(PreferenceStrings.DATA_PERMANENT_DATA);
                    preferences.flush();
                    saveData();

            } catch (Exception e) {
                e.printStackTrace();
                preferences.remove(PreferenceStrings.DATA_PERMANENT_DATA);
                preferences.flush();
            }


        }


    }

    public static void saveChallengeData(String id){
        if(!dataSaveStore.keyStoreBoolean.containsKey(id)) {
            dataSaveStore.keyStoreBoolean.put(id, true);
            saveData();
        }
    }

    public static void saveItemData(String id){
        if(!dataSaveStore.itemStoreBoolean.containsKey(id)) {
            dataSaveStore.itemStoreBoolean.put(id, true);
            saveData();
        }
    }



    public static void saveData(){

        FileHandle file = Gdx.files.local(FileLocationStrings.playerData);
        String saveDataString = json.toJson(dataSaveStore);
        file.writeString(Base64Coder.encodeString(saveDataString), false);

    }


    public static void clearData(){
        dataSaveStore = new DataSaveStore();
        String saveDataString = json.toJson(dataSaveStore);
        FileHandle file = Gdx.files.local(FileLocationStrings.playerData);
        file.writeString(saveDataString, false);
    }


    public static boolean isDataAvailable(String id){
        return dataSaveStore.keyStoreBoolean.containsKey(id);
    }

    public static boolean isItemCollected(String id){
        return dataSaveStore.itemStoreBoolean.containsKey(id);
    }

    private static class DataSaveStore {

        public OrderedMap<String, Boolean> keyStoreBoolean = new OrderedMap<String, Boolean>();
        public OrderedMap<String, Boolean> itemStoreBoolean = new OrderedMap<String, Boolean>();
        public OrderedMap<String, Integer> keyStoreTally = new OrderedMap<String, Integer>();

    }




















    }
