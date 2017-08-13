package com.byrjamin.wickedwizard.utils.enums;

import com.byrjamin.wickedwizard.factories.arenas.skins.ArenaSkin;
import com.byrjamin.wickedwizard.factories.arenas.skins.Bourbon;
import com.byrjamin.wickedwizard.factories.arenas.skins.DarkGraySkin;
import com.byrjamin.wickedwizard.factories.arenas.skins.DarkPurpleAndBrown;
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
                    return new LightGraySkin();
                case TWO: return new FoundarySkin();
                case THREE: return new DarkPurpleAndBrown();
                case FOUR: return new DarkGraySkin();
                case FIVE: return new InfernalSkin();
            }
        }

}
