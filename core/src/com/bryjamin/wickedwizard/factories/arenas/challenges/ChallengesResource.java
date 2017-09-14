package com.bryjamin.wickedwizard.factories.arenas.challenges;

import com.badlogic.gdx.utils.Array;
import com.bryjamin.wickedwizard.assets.resourcelayouts.ChallengeLayout;

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


    public static class GameCompletion {

        public static final String GAME_COMPLETION_TWO = "ad211c68-cf46-4f01-aac2-51a2ccb251b6";
        public static final String GAME_COMPLETION_THREE = "c1e50d1a-0a19-4260-8eb6-3e4816d43527";
        public static final String GAME_COMPLETION_FOUR = "e3d083d2-e9cb-4591-8706-5b783c31c2bc";
        public static final String GAME_COMPLETION_FIVE = "aa136c06-9d47-48fe-9d9f-fc21f9aaaa79";

    }




    public static class Rank1Challenges {

        public static String tutorialSpeedRun = "c5374546-859a-11e7-bb31-be2e44b06b34";
        public static String arenaSpeedRun = "8331965c-de7b-4fbe-87e0-42c23d3803ee";
        public static String perfectBlobba = "c53749a6-859a-11e7-bb31-be2e44b06b34";
        public static String perfectAdoj = "524f9b21-c38f-4237-88be-3c66adcb7bdd";

        public static ChallengeLayout TUTORIAL_SPEEDRUN = new ChallengeLayout.ChallengeLayoutBuilder(tutorialSpeedRun)
                .unlockString(ChallengesResource.LEVEL_1_COMPLETE)
                .name("Leah's First Run")
                .build();

        public static ChallengeLayout ARENA_SPEEDRUN = new ChallengeLayout.ChallengeLayoutBuilder(arenaSpeedRun)
                .unlockString(ChallengesResource.LEVEL_1_COMPLETE)
                .name("Leah's First Box Fight")
                .build();

        public static ChallengeLayout PERFECT_BLOBBA = new ChallengeLayout.ChallengeLayoutBuilder(perfectBlobba)
                .unlockString(ChallengesResource.LEVEL_1_COMPLETE)
                .name("BiggaBlobba Bounces Back")
                .build();

        public static ChallengeLayout PERFECT_ADOJ = new ChallengeLayout.ChallengeLayoutBuilder(perfectAdoj)
                .unlockString(ChallengesResource.LEVEL_1_COMPLETE)
                .name("Adoj Attacks Again")
                .build();

        public static Array<ChallengeLayout> rank1ChallengesArray = new Array<ChallengeLayout>();

        static {
            rank1ChallengesArray.add(TUTORIAL_SPEEDRUN); //SlimeCoat
            rank1ChallengesArray.add(ARENA_SPEEDRUN); //Anger
            rank1ChallengesArray.add(PERFECT_BLOBBA); //CrownOfBlobba
            rank1ChallengesArray.add(PERFECT_ADOJ); //StatueOfAdoj
        }

    }



    public static class Rank2Challenges {

        public static String perfectWanda = "fb7165a7-67cf-4d6b-a112-5d5dd7793706";
        public static String perfectKugel = "7dc228e4-8eb7-4e92-a3c4-9b915ff18709";
        public static String arenaTrail = "d009bc24-8ddb-488b-bd45-404dcc8bd923";
        public static String rank2TimeTrail = "ce9458d3-29d8-46cb-971f-2f4167c7657a";


        public static ChallengeLayout RANK_TWO_TIME_TRAIL = new ChallengeLayout.ChallengeLayoutBuilder(rank2TimeTrail)
                .unlockString(ChallengesResource.LEVEL_2_COMPLETE)
                .name("Blue Run")
                .build();

        public static ChallengeLayout RANK_TWO_ARENA = new ChallengeLayout.ChallengeLayoutBuilder(arenaTrail)
                .unlockString(ChallengesResource.LEVEL_2_COMPLETE)
                .name("Box Fight the Second")
                .build();


        public static ChallengeLayout PERFECT_WANDA = new ChallengeLayout.ChallengeLayoutBuilder(perfectWanda)
                .unlockString(ChallengesResource.LEVEL_2_COMPLETE)
                .name("Wanda's Wrathful Welcome")
                .build();

        public static ChallengeLayout PERFECT_KUGEL = new ChallengeLayout.ChallengeLayoutBuilder(perfectKugel)
                .unlockString(ChallengesResource.LEVEL_2_COMPLETE)
                .name("Spinner's Second Spin")
                .build();

        public static Array<ChallengeLayout> rank2ChallengesArray = new Array<ChallengeLayout>();

        static {
            rank2ChallengesArray.add(RANK_TWO_TIME_TRAIL); //Shiny Feather
            rank2ChallengesArray.add(RANK_TWO_ARENA); //Forgotten Figment
            rank2ChallengesArray.add(PERFECT_WANDA); //Wanda's Scarf
            rank2ChallengesArray.add(PERFECT_KUGEL); //SpinnyThignie
        }

    }


    public static class Rank3Challenges {


        public static String rank3TimeTrail = "c382bba7-752b-4f45-a7b6-d2d71600cd6b";
        public static String rank3ArenaTrial = "0da6c488-ffd3-4d4d-aaef-3948bb10a47c";
        public static String perfectBoomy = "5971c470-98af-463a-947a-b86db48b0e35";
        public static String perfectAjir = "536e9555-9beb-40fe-867e-d2999be26d32";


        public static ChallengeLayout RANK_THREE_TIME_TRAIL = new ChallengeLayout.ChallengeLayoutBuilder(rank3TimeTrail)
                .unlockString(ChallengesResource.LEVEL_3_COMPLETE)
                .name("Boom-Night Run")
                .build();

        public static ChallengeLayout RANK_THREE_ARENA = new ChallengeLayout.ChallengeLayoutBuilder(rank3ArenaTrial)
                .unlockString(ChallengesResource.LEVEL_3_COMPLETE)
                .name("Boom-Box Fight")
                .build();


        public static ChallengeLayout PERFECT_BOOMY = new ChallengeLayout.ChallengeLayoutBuilder(perfectBoomy)
                .unlockString(ChallengesResource.LEVEL_3_COMPLETE)
                .name("Boomy Booms Boomier")
                .build();

        public static ChallengeLayout PERFECT_AJIR = new ChallengeLayout.ChallengeLayoutBuilder(perfectAjir)
                .unlockString(ChallengesResource.LEVEL_3_COMPLETE)
                .name("Ajir Actually Aims")
                .build();




        public static Array<ChallengeLayout> rank3ChallengesArray = new Array<ChallengeLayout>();

        static {
            rank3ChallengesArray.add(RANK_THREE_TIME_TRAIL); //Angry Slime Coat
            rank3ChallengesArray.add(RANK_THREE_ARENA); //critical Eye
            rank3ChallengesArray.add(PERFECT_BOOMY); //3D Gold
            rank3ChallengesArray.add(PERFECT_AJIR); //statue of ajir
        }

    }



    public static class Rank4Challenges {

        public static String rank4TimeTrail = "c84dc561-a417-4c73-bd48-ec65008009f0";
        public static String rank4Arena = "36ac759a-2fe1-45c4-b8b9-aa96cdfee696";
        public static String perfectAmalgama = "cb425f59-de30-4d19-b851-24e79b13bb0c";
        public static String perfectWraith = "320ce1ee-10fa-4617-a4df-e4cd385a8a5c";


        public static ChallengeLayout RANK_FOUR_TIME_TRAIL = new ChallengeLayout.ChallengeLayoutBuilder(rank4TimeTrail)
                .unlockString(ChallengesResource.LEVEL_4_COMPLETE)
                .name("Light Run")
                .build();

        public static ChallengeLayout RANK_FOUR_ARENA = new ChallengeLayout.ChallengeLayoutBuilder(rank4Arena)
                .unlockString(ChallengesResource.LEVEL_4_COMPLETE)
                .name("Box Fight: Reckoning")
                .build();

        public static ChallengeLayout PERFECT_WRAITH = new ChallengeLayout.ChallengeLayoutBuilder(perfectWraith)
                .unlockString(ChallengesResource.LEVEL_4_COMPLETE)
                .name("Wraith Wrecks Wonderfully")
                .build();

        public static ChallengeLayout PERFECT_AMALGAMA = new ChallengeLayout.ChallengeLayoutBuilder(perfectAmalgama)
                .unlockString(ChallengesResource.LEVEL_4_COMPLETE)
                .name("Amalgama Amalgally Amalgamates")
                .build();


        public static Array<ChallengeLayout> rank4ChallengesArray = new Array<ChallengeLayout>();

        static {
            rank4ChallengesArray.add(RANK_FOUR_TIME_TRAIL); //Quickness
            rank4ChallengesArray.add(RANK_FOUR_ARENA); //Mini Trebuchet
            rank4ChallengesArray.add(PERFECT_WRAITH); //Your Very Own Stalker
            rank4ChallengesArray.add(PERFECT_AMALGAMA); //Eyes of Amalgama
        }

    }


    public static class Rank5Challenges {

        public static String rank5UltimateTimeTrail = "6afbd6e8-2525-4b62-8798-57d9489c43f5";
        public static String rank5NotUltimateArena = "104bb7fe-8756-4a64-895d-81691920ab3a";
        public static String bossRush = "800ba671-257f-48dc-b169-4d6ebd7cdc90";
        public static String perfectBossRush = "3db46e92-2e41-436b-8a7e-039726a97845";


        public static ChallengeLayout RANK_FIVE_TIME_TRAIL = new ChallengeLayout.ChallengeLayoutBuilder(rank5UltimateTimeTrail)
                .unlockString(ChallengesResource.LEVEL_5_COMPLETE)
                .name("Ultimate Run")
                .build();

        public static ChallengeLayout RANK_FIVE_ARENA = new ChallengeLayout.ChallengeLayoutBuilder(rank5NotUltimateArena)
                .unlockString(ChallengesResource.LEVEL_5_COMPLETE)
                .name("Ultimate Box Fight")
                .build();

        public static ChallengeLayout BOSS_RUSH = new ChallengeLayout.ChallengeLayoutBuilder(bossRush)
                .unlockString(ChallengesResource.LEVEL_5_COMPLETE)
                .name("Boss Rush")
                .build();

        public static ChallengeLayout PERFECT_BOSS_RUSH = new ChallengeLayout.ChallengeLayoutBuilder(perfectBossRush)
                .unlockString(ChallengesResource.LEVEL_5_COMPLETE)
                .unlockString(perfectBossRush)
                .name("Finale")
                .build();



        public static Array<ChallengeLayout> rank5ChallengesArray = new Array<ChallengeLayout>();

        static {
            rank5ChallengesArray.add(RANK_FIVE_TIME_TRAIL); //Momentum
            rank5ChallengesArray.add(RANK_FIVE_ARENA); //CannonCube
            rank5ChallengesArray.add(BOSS_RUSH); //I wish you well
            rank5ChallengesArray.add(PERFECT_BOSS_RUSH); // ??
        }

    }




    public static Array<ChallengeLayout> getAllChallenges(){

        Array<ChallengeLayout> challengeLayouts = new Array<ChallengeLayout>();

        challengeLayouts.addAll(Rank1Challenges.rank1ChallengesArray);
        challengeLayouts.addAll(Rank2Challenges.rank2ChallengesArray);
        challengeLayouts.addAll(Rank3Challenges.rank3ChallengesArray);
        challengeLayouts.addAll(Rank4Challenges.rank4ChallengesArray);
        challengeLayouts.addAll(Rank5Challenges.rank5ChallengesArray);

        return challengeLayouts;

    }





}
