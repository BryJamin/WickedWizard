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
            rank1ChallengesArray.add(tutorialSpeedRun); //SlimeCoat
            rank1ChallengesArray.add(arenaSpeedRun); //Anger
            rank1ChallengesArray.add(perfectBlobba); //CrownOfBlobba
            rank1ChallengesArray.add(perfectAdoj); //StatueOfAdoj
        }

    }



    public static class Rank2Challenges {

        public static String perfectWanda = "fb7165a7-67cf-4d6b-a112-5d5dd7793706";
        public static String perfectKugel = "7dc228e4-8eb7-4e92-a3c4-9b915ff18709";
        public static String arenaTrail = "d009bc24-8ddb-488b-bd45-404dcc8bd923";
        public static String rank2TimeTrail = "ce9458d3-29d8-46cb-971f-2f4167c7657a";

        public static Array<String> rank2ChallengesArray = new Array<String>();

        static {
            rank2ChallengesArray.add(rank2TimeTrail); //Shiny Feather
            rank2ChallengesArray.add(arenaTrail); //Forgotten Figment
            rank2ChallengesArray.add(perfectWanda); //Wanda's Scarf
            rank2ChallengesArray.add(perfectKugel); //SpinnyThignie
        }

    }


    public static class Rank3Challenges {

        public static String rank3ArenaTrial = "0da6c488-ffd3-4d4d-aaef-3948bb10a47c";
        public static String rank3TimeTrail = "c382bba7-752b-4f45-a7b6-d2d71600cd6b";
        public static String perfectBoomy = "5971c470-98af-463a-947a-b86db48b0e35";
        public static String perfectAjir = "536e9555-9beb-40fe-867e-d2999be26d32";

        public static Array<String> rank3ChallengesArray = new Array<String>();

        static {
            rank3ChallengesArray.add(rank3ArenaTrial); //critical Eye
            rank3ChallengesArray.add(rank3TimeTrail); //Angry Slime Coat
            rank3ChallengesArray.add(perfectBoomy); //3D Gold
            rank3ChallengesArray.add(perfectAjir);
        }

    }



    public static class Rank4Challenges {

        public static String perfectAmalgama = "cb425f59-de30-4d19-b851-24e79b13bb0c";
        public static String perfectWraith = "320ce1ee-10fa-4617-a4df-e4cd385a8a5c";
        public static String rank4Arena = "36ac759a-2fe1-45c4-b8b9-aa96cdfee696";
        public static String rank4TimeTrail = "c84dc561-a417-4c73-bd48-ec65008009f0";

        public static Array<String> rank4ChallengesArray = new Array<String>();

        static {
            rank4ChallengesArray.add(perfectWraith); //Your Very Own Stalker
            rank4ChallengesArray.add(perfectAmalgama); //Eyes of Amalgama
            rank4ChallengesArray.add(rank4Arena); //Mini Trebuchet
            rank4ChallengesArray.add(rank4TimeTrail); //Quickness
        }

    }


    public static class Rank5Challenges {

        public static String rank5UltimateTimeTrail = "6afbd6e8-2525-4b62-8798-57d9489c43f5";
        public static String rank5NotUltimateArena = "104bb7fe-8756-4a64-895d-81691920ab3a";
        public static String bossRush = "800ba671-257f-48dc-b169-4d6ebd7cdc90";
        public static String perfectBossRush = "3db46e92-2e41-436b-8a7e-039726a97845\n";

        public static Array<String> rank5ChallengesArray = new Array<String>();

        static {
            rank5ChallengesArray.add(rank5NotUltimateArena); //CannonCube
            rank5ChallengesArray.add(rank5UltimateTimeTrail); //Momentum
            rank5ChallengesArray.add(bossRush); //I wish you well
            rank5ChallengesArray.add(perfectBossRush); // ??
        }

    }






}
