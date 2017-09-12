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


    private static String CHALLENGE_MAP_KEY = "43eac2e9-63b1-4739-8d74-0cc5885bfa00";
    private static String ITEM_MAP_KEY = "f34c29cb-e264-42b6-a748-b3ea88540eb2";

    private static Json json = new Json();

    private static DataSaveStore dataSaveStore = new DataSaveStore();


    private static OrderedMap<String, OrderedMap> dataSaveMap = new OrderedMap<String, OrderedMap>();

    static {
        dataSaveMap.put(CHALLENGE_MAP_KEY, new OrderedMap<String, Boolean>());
        dataSaveMap.put(ITEM_MAP_KEY, new OrderedMap<String, Boolean>());
    }



    static {

        Preferences preferences = Gdx.app.getPreferences(PreferenceStrings.DATA_PREF_KEY);
        String loadString = preferences.getString(PreferenceStrings.DATA_PERMANENT_DATA, PreferenceStrings.DATA_QUICK_SAVE_NO_VALID_SAVE);


        FileHandle file = Gdx.files.local(FileLocationStrings.playerData);

        try {

            if(file.exists()) {

                OrderedMap<String, OrderedMap> orderedMap = json.fromJson(OrderedMap.class, Base64Coder.decodeString(file.readString()));
               // dataSaveMap = json.fromJson(OrderedMap.class, Base64Coder.decodeString(file.readString()));

                if(!orderedMap.containsKey(CHALLENGE_MAP_KEY)) throw new Exception("Invalid Save Data");
                if(!orderedMap.containsKey(ITEM_MAP_KEY)) throw new Exception("Invalid Save Data");

                dataSaveMap = orderedMap;
            }

        } catch (Exception e){
            e.printStackTrace();
        }


        try {

            if (!loadString.equals(PreferenceStrings.DATA_QUICK_SAVE_NO_VALID_SAVE)) {
                dataSaveStore = json.fromJson(DataSaveStore.class, Base64Coder.decodeString(loadString));


                for (String key : dataSaveStore.keyStoreBoolean.keys().toArray()) {
                    if (!dataSaveMap.get(CHALLENGE_MAP_KEY).containsKey(key)) {
                        dataSaveMap.get(CHALLENGE_MAP_KEY).put(key, dataSaveStore.keyStoreBoolean.get(key));
                    }
                }


                for (String key : dataSaveStore.itemStoreBoolean.keys().toArray()) {
                    if (!dataSaveMap.get(ITEM_MAP_KEY).containsKey(key)) {
                        dataSaveMap.get(ITEM_MAP_KEY).put(key, dataSaveStore.itemStoreBoolean.get(key));
                    }
                }
                preferences.putString(PreferenceStrings.DATA_PERMANENT_DATA, PreferenceStrings.DATA_QUICK_SAVE_NO_VALID_SAVE);
                preferences.flush();
            }
        } catch (Exception e){
            e.printStackTrace();
        }


    }




    public static void saveChallengeData(String id){
        try {
            dataSaveMap.get(CHALLENGE_MAP_KEY).put(id, false);
        }catch (Exception e) {
            e.printStackTrace();;
        }
        saveData();
    }

    public static void saveItemData(String id){

        try {
            dataSaveMap.get(ITEM_MAP_KEY).put(id, false);
        }catch (Exception e) {
            e.printStackTrace();;
        }

        saveData();
    }


    public static void saveData(){

        FileHandle file = Gdx.files.local(FileLocationStrings.playerData);
        String saveDataString = json.toJson(dataSaveMap);
        file.writeString(Base64Coder.encodeString(saveDataString), false);


    }

    public static void clearData(){

        dataSaveMap = new OrderedMap<String, OrderedMap>();
        String saveDataString = Base64Coder.encodeString(json.toJson(dataSaveMap));

        FileHandle file = Gdx.files.local(FileLocationStrings.playerData);
        file.writeString(saveDataString, false);

    }



    public static boolean isDataAvailable(String id){
        try {
            return dataSaveMap.get(CHALLENGE_MAP_KEY).containsKey(id);
        } catch (Exception e){
            return  false;
        }
    }

    public static boolean isItemCollected(String id){
        try {
            return dataSaveMap.get(ITEM_MAP_KEY).containsKey(id);
        } catch (Exception e){
            return  false;
        }
    }




    private static class DataSaveStore {

        public OrderedMap<String, Boolean> keyStoreBoolean = new OrderedMap<String, Boolean>();
        public OrderedMap<String, Boolean> itemStoreBoolean = new OrderedMap<String, Boolean>();
        public OrderedMap<String, Integer> keyStoreTally = new OrderedMap<String, Integer>();


    }






















}
