package com.byrjamin.wickedwizard.factories.arenas.challenges;

import com.badlogic.gdx.utils.Array;

/**
 * Created by BB on 20/08/2017.
 */

public class ChallengesResource {


        public static class Rank1Challenges {

            //public static String tutorialSpeedRun = "c5374546-859a-11e7-bb31-be2e44b06b34";
            public static String perfectBlobba = "c53749a6-859a-11e7-bb31-be2e44b06b34";
            public static String perfectAdoj = "c53749a6-859a-11e7-bb31-be2e44b06b34";

            public static Array<String> rank1ChallengesArray = new Array<String>();

            static {
                //rank1ChallengesArray.add(tutorialSpeedRun);
                rank1ChallengesArray.add(perfectBlobba);
                rank1ChallengesArray.add(perfectAdoj);
            }

        }




}
