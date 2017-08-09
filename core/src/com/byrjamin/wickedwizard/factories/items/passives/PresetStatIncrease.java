package com.byrjamin.wickedwizard.factories.items.passives;

import com.byrjamin.wickedwizard.ecs.components.StatComponent;

/**
 * Created by Home on 13/05/2017.
 */

public class PresetStatIncrease {

    public static final float minor = 1.0f;
    public static final float major = 2.0f;
    public static final float massive = 3.0f;


    public static class Speed {
        public static final float minor = 0.05f;
        public static final float major = 0.1f;
        public static final float massive = 0.15f;
    }



    public static class Health {
        public static int increase(int i){
            return i * 2;
        }
    }

}
