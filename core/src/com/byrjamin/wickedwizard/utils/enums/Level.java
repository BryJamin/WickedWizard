package com.byrjamin.wickedwizard.utils.enums;

import com.byrjamin.wickedwizard.assets.Mix;
import com.byrjamin.wickedwizard.assets.MusicStrings;
import com.byrjamin.wickedwizard.factories.arenas.skins.ArenaSkin;
import com.byrjamin.wickedwizard.factories.arenas.skins.Bourbon;
import com.byrjamin.wickedwizard.factories.arenas.skins.DarkGraySkin;
import com.byrjamin.wickedwizard.factories.arenas.skins.DarkPurpleAndBrown;
import com.byrjamin.wickedwizard.factories.arenas.skins.FieldSkin;
import com.byrjamin.wickedwizard.factories.arenas.skins.FoundarySkin;
import com.byrjamin.wickedwizard.factories.arenas.skins.InfernalSkin;
import com.byrjamin.wickedwizard.factories.arenas.skins.LightGraySkin;

/**
 * Created by BB on 09/08/2017.
 */

public enum Level {

        ONE, TWO, THREE, FOUR, FIVE;

        public ArenaSkin getArenaSkin(){
            switch (this){
                case ONE:
                default:
                    return new FieldSkin();
                case TWO: return new FoundarySkin();
                case THREE: return new DarkPurpleAndBrown();
                case FOUR: return new DarkGraySkin();
                case FIVE: return new InfernalSkin();
            }
        }


    public String getName(){
        switch (this){
            case ONE:
            default:
                return "Field";
            case TWO: return "Tundra";
            case THREE: return "Cave";
            case FOUR: return "Mire";
            case FIVE: return "Finale";
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
