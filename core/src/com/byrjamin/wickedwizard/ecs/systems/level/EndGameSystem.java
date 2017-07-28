package com.byrjamin.wickedwizard.ecs.systems.level;

import com.artemis.BaseSystem;
import com.byrjamin.wickedwizard.MainGame;
import com.byrjamin.wickedwizard.screens.CreditsScreen;

/**
 * Created by Home on 28/07/2017.
 */

public class EndGameSystem extends BaseSystem {

    MainGame game;

    public EndGameSystem(MainGame game){
        this.game = game;
    }

    @Override
    protected void processSystem() {


    }


    public void startCredits(){
        game.getScreen().dispose();
        game.setScreen(new CreditsScreen(game));
    }

}

