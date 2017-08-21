package com.byrjamin.wickedwizard.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Base64Coder;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.OrderedMap;
import com.byrjamin.wickedwizard.assets.PreferenceStrings;

import java.util.Map;

/**
 * Created by BB on 16/08/2017.
 */

public class DataSave {


    private static Json json = new Json();

    private static DataSaveStore dataSaveStore = new DataSaveStore();


    static {

        Preferences preferences = Gdx.app.getPreferences(PreferenceStrings.DATA_PREF_KEY);
        String loadString = preferences.getString(PreferenceStrings.DATA_PERMANENT_DATA, PreferenceStrings.DATA_QUICK_SAVE_NO_VALID_SAVE);

        try {

            dataSaveStore = json.fromJson(DataSaveStore.class, Base64Coder.decodeString(loadString));

        } catch(Exception e){
            e.printStackTrace();
        }
    }



    public static final String TUTORIAL_COMPLETE = "f239f5db-f5fe-42ed-b98f-54250522ec6c";



    public static void saveData(String id){

        Preferences preferences = Gdx.app.getPreferences(PreferenceStrings.DATA_PREF_KEY);
        String loadString = preferences.getString(PreferenceStrings.DATA_PERMANENT_DATA, PreferenceStrings.DATA_QUICK_SAVE_NO_VALID_SAVE);


        try {
            try {
                dataSaveStore = json.fromJson(DataSaveStore.class, Base64Coder.decodeString(loadString));
            } catch(Exception e){
                e.printStackTrace();
            }

            if(!dataSaveStore.keyStoreBoolean.containsKey(id)) {
                dataSaveStore.keyStoreBoolean.put(id, true);
            }


            String saveDataString = json.toJson(dataSaveStore);

            preferences = Gdx.app.getPreferences(PreferenceStrings.DATA_PREF_KEY);
            preferences.putString(PreferenceStrings.DATA_PERMANENT_DATA, Base64Coder.encodeString(saveDataString));
            preferences.flush();

        } catch(Exception e){
            e.printStackTrace();
        }



    }

    public static void clearData(){
        Preferences preferences = Gdx.app.getPreferences(PreferenceStrings.DATA_PREF_KEY);
        preferences.putString(PreferenceStrings.DATA_PERMANENT_DATA, PreferenceStrings.DATA_QUICK_SAVE_NO_VALID_SAVE);
        preferences.flush();
        dataSaveStore = new DataSaveStore();
    }



    public static boolean isDataAvailable(String id){
        return dataSaveStore.keyStoreBoolean.containsKey(id);
    }







    private static class DataSaveStore {

        public OrderedMap<String, Boolean> keyStoreBoolean = new OrderedMap<String, Boolean>();
        public OrderedMap<String, Integer> keyStoreTally = new OrderedMap<String, Integer>();


    }






















}
