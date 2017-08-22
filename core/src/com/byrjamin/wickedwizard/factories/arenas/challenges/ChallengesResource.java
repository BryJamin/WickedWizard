package com.byrjamin.wickedwizard.factories.arenas.challenges;

import com.badlogic.gdx.utils.Array;

/**
 * Created by BB on 20/08/2017.
 */

public class ChallengesResource {


        public static class Rank1Challenges {

            public static String tutorialSpeedRun = "c5374546-859a-11e7-bb31-be2e44b06b34";
            public static String perfectBlobba = "c53749a6-859a-11e7-bb31-be2e44b06b34";
            public static String perfectAdoj = "524f9b21-c38f-4237-88be-3c66adcb7bdd";

            public static Array<String> rank1ChallengesArray = new Array<String>();

            static {
                rank1ChallengesArray.add(tutorialSpeedRun);
                rank1ChallengesArray.add(perfectBlobba);
                rank1ChallengesArray.add(perfectAdoj);
            }

        }



    public static class Rank2Challenges {

        public static String rank2ChallengesUnlockString = "98f8af9f-4581-4cf8-8ea8-f6b0fdf6af8b";

        public static String tutorialSpeedRun = "c5374546-859a-11e7-bb31-be2e44b06b34";
        public static String perfectBlobba = "c53749a6-859a-11e7-bb31-be2e44b06b34";
        public static String perfectAdoj = "524f9b21-c38f-4237-88be-3c66adcb7bdd";

        public static Array<String> rank2ChallengesArray = new Array<String>();

        static {
            rank2ChallengesArray.add(tutorialSpeedRun);
            rank2ChallengesArray.add(perfectBlobba);
            rank2ChallengesArray.add(perfectAdoj);
        }

    }





}
