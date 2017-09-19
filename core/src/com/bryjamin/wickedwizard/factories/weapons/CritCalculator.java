package com.bryjamin.wickedwizard.factories.weapons;

import com.badlogic.gdx.math.MathUtils;

/**
 * Created by Home on 14/05/2017.
 */

public class CritCalculator {


    private static final float accuracyMultiplier = 2;
    private static final float luckMultiplier = 0.5f;

    public static boolean isCrit(float critChance, float accuracy, float luck){
        return getCritChance(critChance, accuracy, luck) > MathUtils.random.nextInt(100);
    }

    public static float getCritChance(float critChance, float accuracy, float luck){
        return critChance + (accuracy * accuracyMultiplier) + (luck * luckMultiplier);
    }


    public static float calculateFireRate(float weaponBaseFireRate, float fireRate){
        return weaponBaseFireRate / (1 + (fireRate / 10));
    }

}
