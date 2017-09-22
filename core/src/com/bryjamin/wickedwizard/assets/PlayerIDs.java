package com.bryjamin.wickedwizard.assets;

import com.bryjamin.wickedwizard.factories.arenas.challenges.ChallengesResource;

/**
 * Created by BB on 08/09/2017.
 */

public class PlayerIDs {


    public static final String LEAH_ID = "accaf10b-744f-4d76-a099-85f1791c3a58";

    public static final String XI_ID = "6b217e83-0ca8-435a-8dd0-78a3eff155be";
    public static final String XI_UNLOCK_STRING = "bda8bad5-27fa-45c2-8294-8c56e4dde78e";

    public static final String PHI_ID = "7b100db4-14da-4224-a55e-c9073326c653";

    public static final String TESS_ID = "5eb6809e-9e4e-4de4-a788-547c8b9c4188";
    public static final String TESS_UNLOCK_STRING = "98aef33d-3951-45d6-aa62-5d6f386b9d2e";


    public static PlayableCharacter LEAH = new PlayableCharacter.PlayableCharacterBuilder(LEAH_ID)
            .name("Leah")
            .traits("Average")
            .personality("Protagonist")
            .region(TextureStrings.BLOCK_PORTRAIT)
            .build();


    public static PlayableCharacter XI = new PlayableCharacter.PlayableCharacterBuilder(XI_ID)
            .name("Xi")
            .traits("Low Health, Higher Damage")
            .personality("Edgy")
            .region(TextureStrings.XI_PORTRAIT)
            .unlockString(XI_UNLOCK_STRING)
            .build();



    public static PlayableCharacter PHI = new PlayableCharacter.PlayableCharacterBuilder(PHI_ID)
            .name("Phi")
            .traits("High Health, Low Damage and Speed")
            .personality("Low Self-Esteem")
            .region(TextureStrings.PHI_PORTRAIT)
            .unlockString(ChallengesResource.LEVEL_2_COMPLETE)
            .build();


    public static PlayableCharacter TESS = new PlayableCharacter.PlayableCharacterBuilder(TESS_ID)
            .name("Tess")
            .traits("Low Health, High Luck")
            .personality("See-Through")
            .region(TextureStrings.TESS_PORTRAIT)
            .unlockString(TESS_UNLOCK_STRING)
            .build();


    public static PlayableCharacter[] endGameUnlockAbleCharacters = {XI, TESS};




    public static class PlayableCharacter {

        private final String id;
        private final String name;
        private final String traits;
        private final String personality;
        private final String region;
        private final String unlockString;


        public PlayableCharacter(PlayableCharacterBuilder pcb){
            this.id = pcb.id;
            this.name = pcb.name;
            this.traits = pcb.traits;
            this.personality = pcb.personality;
            this.region = pcb.region;
            this.unlockString = pcb.unlockString;
        }



        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getRegion() {
            return region;
        }

        public String getUnlockString() {
            return unlockString;
        }

        public String getTraits() {
            return traits;
        }

        public String getPersonality() {
            return personality;
        }

        public static class PlayableCharacterBuilder {


            //Required
            private String id;
            private String name = "Undefined";
            private String traits = "Undefined";
            private String personality = "Undefinied";
            private String region = TextureStrings.BLOCK;
            private String unlockString = ChallengesResource.TUTORIAL_COMPLETE;


            public PlayableCharacterBuilder(String id) {
                this.id = id;
            }

            public PlayableCharacterBuilder name(String val)
            { name = val; return this; }

            public PlayableCharacterBuilder traits(String val)
            { traits = val; return this; }

            public PlayableCharacterBuilder personality(String val)
            { personality = val; return this; }

            public PlayableCharacterBuilder region(String val)
            { region = val; return this; }

            public PlayableCharacterBuilder unlockString(String val)
            { unlockString = val; return this; }


            public PlayableCharacter build() {
                return new PlayableCharacter(this);
            }


        }


    }


















}
