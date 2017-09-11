package com.bryjamin.wickedwizard.assets;

import com.bryjamin.wickedwizard.factories.arenas.challenges.ChallengesResource;

/**
 * Created by BB on 08/09/2017.
 */

public class PlayerIDs {

    public static final String LEAH_ID = "accaf10b-744f-4d76-a099-85f1791c3a58";

    public static final String XI_ID = "6b217e83-0ca8-435a-8dd0-78a3eff155be";

    public static final String PHI_ID = "6b217e83-0ca8-435a-8dd0-78a3eff155be";


    public static PlayableCharacter LEAH = new PlayableCharacter.PlayableCharacterBuilder("accaf10b-744f-4d76-a099-85f1791c3a58")
            .name("Leah")
            .region(TextureStrings.BLOCK_FACING)
            .build();


    public static PlayableCharacter XI = new PlayableCharacter.PlayableCharacterBuilder("6b217e83-0ca8-435a-8dd0-78a3eff155be")
            .name("Xi")
            .region(TextureStrings.XI_FACING)
            .unlockString(ChallengesResource.LEVEL_2_COMPLETE)
            .build();


    public static class PlayableCharacter {

        private final String id;
        private final String name;
        private final String region;
        private final String unlockString;


        public PlayableCharacter(PlayableCharacterBuilder pcb){
            this.id = pcb.id;
            this.name = pcb.name;
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

        public static class PlayableCharacterBuilder {


            //Required
            private String id;
            public String name = "Undefined";
            public String region = TextureStrings.BLOCK;
            public String unlockString = null;


            public PlayableCharacterBuilder(String id) {
                this.id = id;
            }

            public PlayableCharacterBuilder name(String val)
            { name = val; return this; }

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
