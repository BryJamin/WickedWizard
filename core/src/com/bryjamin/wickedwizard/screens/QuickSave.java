package com.bryjamin.wickedwizard.screens;

import com.artemis.World;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Base64Coder;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.OrderedMap;
import com.badlogic.gdx.utils.SerializationException;
import com.bryjamin.wickedwizard.assets.FileLocationStrings;
import com.bryjamin.wickedwizard.ecs.components.CurrencyComponent;
import com.bryjamin.wickedwizard.ecs.components.StatComponent;
import com.bryjamin.wickedwizard.ecs.components.identifiers.PlayerComponent;
import com.bryjamin.wickedwizard.ecs.systems.FindPlayerSystem;
import com.bryjamin.wickedwizard.ecs.systems.level.ChangeLevelSystem;
import com.bryjamin.wickedwizard.factories.PlayerFactory;
import com.bryjamin.wickedwizard.factories.arenas.GameCreator;
import com.bryjamin.wickedwizard.screens.world.AdventureWorld;
import com.bryjamin.wickedwizard.utils.BagSearch;
import com.bryjamin.wickedwizard.utils.ComponentBag;

/**
 * Created by Home on 29/07/2017.
 */

public class QuickSave {



    private static Json json = new Json();

    private static String PLAYER_ID_STRING = "4c2ad909-f3f1-42e7-9463-9fb72182edd1";
    private static String STAT_COMPONENT_STRING = "1a4544a0-5ad3-4951-85b1-befea3bf4c3b";
    private static String CURRENCY_COMPONENT_STRING = "a3c1b456-2f03-47cf-85ea-9627513871b3";
    private static String LEVEL_POSITION_STRING = "2dcaafdb-b3a5-4780-982d-7cbeaf5e4706";
    private static String ITEMPOOL_COMPONENT_STRING = "449de27a-3f35-431d-983f-8bffd0d4b2ce";



    public static void saveGame(World world){


        OrderedMap<String, String> saveMap = new OrderedMap<String, String>();

        saveMap.put(PLAYER_ID_STRING, json.toJson(world.getSystem(FindPlayerSystem.class).getPlayerComponent(PlayerComponent.class).id));

        saveMap.put(STAT_COMPONENT_STRING, json.toJson(world.getSystem(FindPlayerSystem.class).getPlayerComponent(StatComponent.class)));

        saveMap.put(CURRENCY_COMPONENT_STRING, json.toJson(world.getSystem(FindPlayerSystem.class).getPlayerComponent(CurrencyComponent.class)));

        saveMap.put(LEVEL_POSITION_STRING, json.toJson(world.getSystem(ChangeLevelSystem.class).getGameCreator().position + ""));

        saveMap.put(ITEMPOOL_COMPONENT_STRING, json.toJson(world.getSystem(ChangeLevelSystem.class).getJigsawGenerator().getItemStore().getItemStringArray()));

        String saveDataString = json.toJson(saveMap);



        try {

            FileHandle file = Gdx.files.local(FileLocationStrings.playerQuickSaveData);
            file.writeString(Base64Coder.encodeString(saveDataString), false);

            //Preferences preferences = Gdx.app.getPreferences(PreferenceStrings.DATA_PREF_KEY);
            //preferences.putString(PreferenceStrings.DATA_QUICK_SAVE, Base64Coder.encodeString(saveDataString));
            //preferences.flush();
        } catch (SerializationException e){
            e.printStackTrace();
        }

    }



    public static boolean checkQuickSave(){

        FileHandle file = Gdx.files.local(FileLocationStrings.playerQuickSaveData);

        if(!file.exists()) return false;
        String loadString = file.readString();

        try {

            OrderedMap<String, String> saveMap = json.fromJson(OrderedMap.class, Base64Coder.decodeString(loadString));

            json.fromJson(String.class, saveMap.get(PLAYER_ID_STRING));

            json.fromJson(StatComponent.class, saveMap.get(STAT_COMPONENT_STRING));

            json.fromJson(CurrencyComponent.class, saveMap.get(CURRENCY_COMPONENT_STRING));

            json.fromJson(String.class, saveMap.get(LEVEL_POSITION_STRING));

            json.fromJson(Array.class, saveMap.get(ITEMPOOL_COMPONENT_STRING));

            return true;

        } catch (Exception e){
            e.printStackTrace();

            if(file.exists()) file.delete();

/*            if(!loadString.equals(PreferenceStrings.DATA_QUICK_SAVE_NO_VALID_SAVE)) {
                preferences.putString(PreferenceStrings.DATA_QUICK_SAVE, PreferenceStrings.DATA_QUICK_SAVE_NO_VALID_SAVE);
                preferences.flush();
            }*/

            return false;
        }

    }


    public static boolean doesQuickSaveExist(){
        return Gdx.files.local(FileLocationStrings.playerQuickSaveData).exists();
    }


    public static boolean loadQuickSave(GameCreator gameCreator, AssetManager assetManager, AdventureWorld adventureWorld){

        FileHandle file = Gdx.files.local(FileLocationStrings.playerQuickSaveData);

        if(!file.exists()) return false;
        String loadString = file.readString();

        try {

            OrderedMap<String, String> saveMap = json.fromJson(OrderedMap.class, Base64Coder.decodeString(loadString));

            String level = json.fromJson(String.class, saveMap.get(LEVEL_POSITION_STRING));
            gameCreator.setCurrentLevel(Integer.parseInt(level));

            gameCreator.getNextLevel().jigsawGeneratorConfig.itemStore
                    .updateItemOptions(json.fromJson(Array.class, saveMap.get(ITEMPOOL_COMPONENT_STRING)));

            PlayerFactory playerFactory = new PlayerFactory(assetManager);

            ComponentBag player = playerFactory.playerBag(json.fromJson(String.class, saveMap.get(PLAYER_ID_STRING)), 0, 0);
            BagSearch.getObjectOfTypeClass(StatComponent.class, player)
                    .applyStats(json.fromJson(StatComponent.class, saveMap.get(STAT_COMPONENT_STRING)));

            BagSearch.getObjectOfTypeClass(CurrencyComponent.class, player)
                    .updateCurrency(json.fromJson(CurrencyComponent.class, saveMap.get(CURRENCY_COMPONENT_STRING)));

            adventureWorld.setPlayer(player);

        } catch (Exception e){
            e.printStackTrace();
        }

        file.delete();

        return true;


    }

}
