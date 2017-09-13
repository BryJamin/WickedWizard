package com.bryjamin.wickedwizard.assets.resourcelayouts;

import com.bryjamin.wickedwizard.factories.arenas.challenges.ChallengesResource;

/**
 * Created by BB on 12/09/2017.
 */

public class ChallengeLayout {

    private final String challengeID;
    private final String name;
    private final String unlockString;


    public ChallengeLayout(ChallengeLayoutBuilder challengeLayoutBuilder){
        this.challengeID = challengeLayoutBuilder.challengeID;
        this.name = challengeLayoutBuilder.name;
        this.unlockString = challengeLayoutBuilder.unlockString;
    };


    public String getChallengeID() {
        return challengeID;
    }

    public String getName() {
        return name;
    }

    public String getUnlockString() {
        return unlockString;
    }

    public static class ChallengeLayoutBuilder {

        public String challengeID;
        public String name = "Undefined";
        public String unlockString = ChallengesResource.TUTORIAL_COMPLETE;

        public ChallengeLayoutBuilder(String id){
            this.challengeID = id;
        }

        public ChallengeLayoutBuilder name(String val)
        { name = val; return this; }

        public ChallengeLayoutBuilder unlockString(String val)
        { unlockString = val; return this; }

        public ChallengeLayout build() {
            return new ChallengeLayout(this);
        }


    }








}
