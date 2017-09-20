package com.bryjamin.wickedwizard.factories.arenas.challenges.adventure;

import com.badlogic.gdx.utils.OrderedMap;
import com.bryjamin.wickedwizard.assets.PlayerIDs;
import com.bryjamin.wickedwizard.factories.arenas.challenges.ChallengesResource;
import com.bryjamin.wickedwizard.factories.items.ItemLayout;
import com.bryjamin.wickedwizard.factories.items.ItemResource;

/**
 * Created by BB on 18/09/2017.
 */

public class AdventureUnlocks {

    public static String LEAH_1_UNLOCK = "3787fbb9-5eec-4860-b0fc-d72b09683da2";
    public static String LEAH_2_UNLOCK = "58735829-3cac-49cc-b631-69afdbf24581";
    public static String LEAH_3_UNLOCK = "7d3ed7a6-9eda-462b-8894-129ed9aff8dd";
    public static String LEAH_4_UNLOCK = "c17a5238-aa6a-4018-b41a-b384c1defab3";
    public static String LEAH_5_UNLOCK = "d85a76f5-df21-4320-ba58-6b0c4db5ced5";

    public static String PHI_1_UNLOCK = "a8dcffaf-efa5-48ec-a54a-52a941b410b9";
    public static String PHI_2_UNLOCK = "0f606fdb-6433-4cc0-a872-48b0d800c899";
    public static String PHI_3_UNLOCK = "80b70925-bfa7-4af0-9e5e-19418d403603";
    public static String PHI_4_UNLOCK = "3ef15101-345e-41af-ad25-1d90356ba467";
    public static String PHI_5_UNLOCK = "61663a0f-6a7b-4f3a-895d-6ebfc5b114e0";

    public static String XI_1_UNLOCK = "90ab808c-deba-409b-b6f7-13ea69e8a538";
    public static String XI_2_UNLOCK = "ad20cecf-5ecb-4916-a559-52c83c9a9ce9";
    public static String XI_3_UNLOCK = "86b1a25f-693a-4dc3-a0ff-51637b131c4c";
    public static String XI_4_UNLOCK = "61076462-f9ca-4a89-9978-781bf78f567e";
    public static String XI_5_UNLOCK = "ad4a9e4a-c7f5-416d-9d74-e5daee3b53fe";

    public static String TESS_1_UNLOCK = "f8f5da7e-3d04-4b32-a841-a617e576f770";
    public static String TESS_2_UNLOCK = "9cf7dda2-a312-4d8a-b12a-1bf566dc1c7d";
    public static String TESS_3_UNLOCK = "f505c10c-1271-4e6e-ad42-76dbc1aa75f3";
    public static String TESS_4_UNLOCK = "76d4ce9a-0d57-426d-a088-c9695db5b65e";
    public static String TESS_5_UNLOCK = "3fa527cf-4bd8-4113-8d59-3bfb6dd4fb85";

    public static OrderedMap<String, OrderedMap<String, String>> adventureUnlockMap = new OrderedMap<String, OrderedMap<String, String>>();



    static {

        putUnlock(PlayerIDs.LEAH_ID, ChallengesResource.LEVEL_1_COMPLETE, LEAH_1_UNLOCK, ItemResource.Armor.steppingStones);
        putUnlock(PlayerIDs.LEAH_ID, ChallengesResource.LEVEL_2_COMPLETE, LEAH_2_UNLOCK, ItemResource.ShotSize.leafCutter);
        putUnlock(PlayerIDs.LEAH_ID, ChallengesResource.LEVEL_3_COMPLETE, LEAH_3_UNLOCK, ItemResource.Range.laserScope);
        putUnlock(PlayerIDs.LEAH_ID, ChallengesResource.LEVEL_4_COMPLETE, LEAH_4_UNLOCK, ItemResource.ShotSpeed.blueEyeGems);
        putUnlock(PlayerIDs.LEAH_ID, ChallengesResource.LEVEL_5_COMPLETE, LEAH_5_UNLOCK, ItemResource.Range.leahsScarf);


        putUnlock(PlayerIDs.PHI_ID, ChallengesResource.LEVEL_1_COMPLETE, PHI_1_UNLOCK, ItemResource.Health.healingTonic);
        putUnlock(PlayerIDs.PHI_ID, ChallengesResource.LEVEL_2_COMPLETE, PHI_2_UNLOCK, ItemResource.ShotSpeed.personalBubble);
        putUnlock(PlayerIDs.PHI_ID, ChallengesResource.LEVEL_3_COMPLETE, PHI_3_UNLOCK, ItemResource.Health.dilation);
        putUnlock(PlayerIDs.PHI_ID, ChallengesResource.LEVEL_4_COMPLETE, PHI_4_UNLOCK, ItemResource.Health.healingTonicPlus);
        putUnlock(PlayerIDs.PHI_ID, ChallengesResource.LEVEL_5_COMPLETE, PHI_5_UNLOCK, ItemResource.Health.phisScarf);


        putUnlock(PlayerIDs.XI_ID, ChallengesResource.LEVEL_1_COMPLETE, XI_1_UNLOCK, ItemResource.Health.secondWind);
        putUnlock(PlayerIDs.XI_ID, ChallengesResource.LEVEL_2_COMPLETE, XI_2_UNLOCK, ItemResource.Health.poisonedDrop);
        putUnlock(PlayerIDs.XI_ID, ChallengesResource.LEVEL_3_COMPLETE, XI_3_UNLOCK, ItemResource.Companion.xisGlare);
        putUnlock(PlayerIDs.XI_ID, ChallengesResource.LEVEL_4_COMPLETE, XI_4_UNLOCK, ItemResource.Armor.bookOfXi);
        putUnlock(PlayerIDs.XI_ID, ChallengesResource.LEVEL_5_COMPLETE, XI_5_UNLOCK, ItemResource.Armor.xisOldScarf);


    }


    public static void putUnlock(String playerID, String levelKey, String unlockId, ItemLayout itemLayout){

        if(!adventureUnlockMap.containsKey(playerID)){
            adventureUnlockMap.put(playerID, new OrderedMap<String, String>());
        }

        adventureUnlockMap.get(playerID).put(levelKey, unlockId);
        itemLayout.setChallengeId(unlockId);

    }


    public static String getUnlock(String playerId, String levelId){

        if(adventureUnlockMap.containsKey(playerId)){
            OrderedMap<String, String> orderedMap = adventureUnlockMap.get(playerId);

            if(orderedMap.containsKey(levelId)){
                return orderedMap.get(levelId);
            }

        }

        return null;
    }






































}
