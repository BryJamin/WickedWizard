package com.bryjamin.wickedwizard.ecs.systems.level;

import com.artemis.BaseSystem;

/**
 * Created by Home on 28/07/2017.
 */

public class EndGameSystem extends BaseSystem {

    private com.bryjamin.wickedwizard.MainGame game;

    public EndGameSystem(com.bryjamin.wickedwizard.MainGame game){
        this.game = game;
    }

    @Override
    protected void processSystem() {


    }

    public void pauseGame(){
        game.pause();
    }


    public void startCredits(){
        game.getScreen().dispose();
        game.setScreen(new com.bryjamin.wickedwizard.screens.CreditsScreen(game));
    }


    public void backToMenu(){
        game.getScreen().dispose();
        game.setScreen(new com.bryjamin.wickedwizard.screens.MenuScreen(game));
    }

    public com.bryjamin.wickedwizard.MainGame getGame() {
        return game;
    }

    public void quickSaveAndBackToMenu(){
        com.bryjamin.wickedwizard.screens.QuickSave.saveGame(world);
        backToMenu();
    }

}

