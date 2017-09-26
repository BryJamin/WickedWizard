package com.bryjamin.wickedwizard.ecs.systems.graphical;

import com.artemis.BaseSystem;
import com.bryjamin.wickedwizard.MainGame;
import com.bryjamin.wickedwizard.screens.PlayScreen;

/**
 * Created by BB on 26/09/2017.
 */

public class TipsMessageSystem extends BaseSystem {

    private MainGame game;

    public TipsMessageSystem(MainGame game) {
        this.game = game;
    }


    @Override
    protected boolean checkProcessing() {
        return false;
    }


    @Override
    protected void processSystem() {
    }


    public void createTipsMessage(String id){
        ((PlayScreen) game.getScreen()).displayTip(id);
    }

}

