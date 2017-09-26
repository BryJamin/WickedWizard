package com.bryjamin.wickedwizard.ecs.systems.graphical;

import com.artemis.BaseSystem;
import com.badlogic.gdx.utils.Array;
import com.bryjamin.wickedwizard.MainGame;
import com.bryjamin.wickedwizard.screens.PlayScreen;

/**
 * Created by BB on 14/09/2017.
 */

public class UnlockMessageSystem extends BaseSystem {

    private MainGame game;


    private Array<String> unlockIds = new Array<String>();

    /**
     * Creates an entity system that uses the specified aspect as a matcher
     * against entities.
     */
    public UnlockMessageSystem(MainGame game) {
        this.game = game;
    }


    @Override
    protected boolean checkProcessing() {
        return unlockIds.size > 0;
    }


    @Override
    protected void processSystem() {

        String[] stringarray = unlockIds.toArray(String.class);
        ((PlayScreen) game.getScreen()).unlock(stringarray);
        unlockIds.clear();
    }

    /**
     * Depending on the Unlock Id creates a message to the player to show which items and Challenges they have unlock
     *
     * @param unlockId
     */
    public void createUnlockMessage(String unlockId){
        unlockIds.add(unlockId);
       // ((PlayScreen) game.getScreen()).unlock(unlockId);
    }

}
