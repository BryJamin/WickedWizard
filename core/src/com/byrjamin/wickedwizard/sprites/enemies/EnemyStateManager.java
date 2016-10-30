package com.byrjamin.wickedwizard.sprites.enemies;

/**
 * Created by Home on 30/10/2016.
 */
public class EnemyStateManager {


    public static boolean targetable = false;


    public static boolean isTargetable() {
        return targetable;
    }

    public static void setTargetable(boolean targetable) {
        EnemyStateManager.targetable = targetable;
    }
}
