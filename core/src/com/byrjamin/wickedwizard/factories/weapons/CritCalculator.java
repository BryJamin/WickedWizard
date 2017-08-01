package com.byrjamin.wickedwizard.factories.weapons;

import java.util.Random;

/**
 * Created by Home on 14/05/2017.
 */

public class CritCalculator {

    private Random random;

    private static final float accuracyMultiplier = 2;
    private static final float luckMultiplier = 0.5f;

    public CritCalculator(Random random){
        this.random = random;
    }

    public boolean isCrit(float critChance, float accuracy, float luck){
        //System.out.println(getCritChance(critChance, accuracy, luck));
        return getCritChance(critChance, accuracy, luck) > random.nextInt(100);
    }

    public float getCritChance(float critChance, float accuracy, float luck){
        return critChance + (accuracy * accuracyMultiplier) + (luck * luckMultiplier);
    }


}
