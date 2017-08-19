package com.byrjamin.wickedwizard.ecs.systems.level;

import com.artemis.BaseSystem;
import com.byrjamin.wickedwizard.MainGame;
import com.byrjamin.wickedwizard.screens.CreditsScreen;
import com.byrjamin.wickedwizard.screens.MenuScreen;
import com.byrjamin.wickedwizard.screens.QuickSave;

/**
 * Created by Home on 28/07/2017.
 */

public class EndGameSystem extends BaseSystem {

    private MainGame game;

    public EndGameSystem(MainGame game){
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
        game.setScreen(new CreditsScreen(game));
    }


    public void backToMenu(){
        game.getScreen().dispose();
        game.setScreen(new MenuScreen(game));
    }

    public MainGame getGame() {
        return game;
    }

    public void quickSaveAndBackToMenu(){
        QuickSave.saveGame(world);
        backToMenu();
    }

}

