package com.bryjamin.wickedwizard.factories.arenas.challenges;

/**
 * Created by BB on 12/09/2017.
 */

public class ChallengeLayout {

    private final String challengeID;
    private final String name;
    private final String stringToUnlockChallenge;


    public ChallengeLayout(ChallengeLayoutBuilder challengeLayoutBuilder){
        this.challengeID = challengeLayoutBuilder.challengeID;
        this.name = challengeLayoutBuilder.name;
        this.stringToUnlockChallenge = challengeLayoutBuilder.unlockString;
    };


    public String getChallengeID() {
        return challengeID;
    }

    public String getName() {
        return name;
    }

    public String getStringToUnlockChallenge() {
        return stringToUnlockChallenge;
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
