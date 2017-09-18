package com.bryjamin.wickedwizard.utils.enums;

import com.bryjamin.wickedwizard.assets.Mix;
import com.bryjamin.wickedwizard.assets.MusicStrings;
import com.bryjamin.wickedwizard.factories.arenas.skins.ArenaSkin;
import com.bryjamin.wickedwizard.factories.arenas.skins.DarkGraySkin;
import com.bryjamin.wickedwizard.factories.arenas.skins.DarkPurpleAndBrown;
import com.bryjamin.wickedwizard.factories.arenas.skins.FoundarySkin;

/**
 * Created by BB on 09/08/2017.
 */

public enum Level {

        ONE, TWO, THREE, FOUR, FIVE;

        public ArenaSkin getArenaSkin(){
            switch (this){
                case ONE:
                default:
                    return new com.bryjamin.wickedwizard.factories.arenas.skins.SeaFoamGreenAndBrown();
                case TWO: return new FoundarySkin();
                case THREE: return new DarkPurpleAndBrown();
                case FOUR: return new DarkGraySkin();
                case FIVE: return new com.bryjamin.wickedwizard.factories.arenas.skins.InfernalSkin();
            }
        }


    public String getName(){
        switch (this){
            case ONE:
            default:
                return "I";
            case TWO: return "II";
            case THREE: return "III";
            case FOUR: return "IV";
            case FIVE: return "V";
        }
    }


    public Mix getMusic(){
        switch (this){
            case ONE:
            default:
                return MusicStrings.BG_LEVEL_ONE;
            case TWO: return MusicStrings.BG_LEVEL_TWO;
            case THREE: return MusicStrings.BG_LEVEL_THREE;
            case FOUR: return MusicStrings.BG_LEVEL_FOUR;
            case FIVE: return MusicStrings.BG_LEVEL_FIVE;
        }
    }


}
