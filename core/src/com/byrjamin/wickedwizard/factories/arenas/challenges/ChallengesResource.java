package com.byrjamin.wickedwizard.factories.arenas.challenges;

import com.badlogic.gdx.utils.Array;

/**
 * Created by BB on 20/08/2017.
 */

public class ChallengesResource {

    public static final String TUTORIAL_COMPLETE = "f239f5db-f5fe-42ed-b98f-54250522ec6c";

    public static final String LEVEL_1_COMPLETE = "a4b144de-3ff0-4255-8212-9e1426d82cd4";
    public static final String LEVEL_2_COMPLETE = "1a00b3e1-0f77-4d60-be67-12c6ecc04eb7";
    public static final String LEVEL_3_COMPLETE = "61b51ce6-77f4-48e7-bd16-e16fb9f9fab3";
    public static final String LEVEL_4_COMPLETE = "6b311405-8495-41d9-815f-8d6d3bc733f1";
    public static final String LEVEL_5_COMPLETE = "057c393b-b311-4738-9fa5-00af2dbad3b9";



        public static class Rank1Challenges {

            public static String tutorialSpeedRun = "c5374546-859a-11e7-bb31-be2e44b06b34";
            public static String arenaSpeedRun = "8331965c-de7b-4fbe-87e0-42c23d3803ee";
            public static String perfectBlobba = "c53749a6-859a-11e7-bb31-be2e44b06b34";
            public static String perfectAdoj = "524f9b21-c38f-4237-88be-3c66adcb7bdd";

            public static Array<String> rank1ChallengesArray = new Array<String>();

            static {
                rank1ChallengesArray.add(tutorialSpeedRun);
                rank1ChallengesArray.add(arenaSpeedRun);
                rank1ChallengesArray.add(perfectBlobba);
                rank1ChallengesArray.add(perfectAdoj);
            }

        }



    public static class Rank2Challenges {

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
