package com.bryjamin.wickedwizard.ecs.systems.level;

import com.artemis.BaseSystem;
import com.bryjamin.wickedwizard.MainGame;
import com.bryjamin.wickedwizard.screens.CreditsScreen;
import com.bryjamin.wickedwizard.screens.MenuScreen;
import com.bryjamin.wickedwizard.screens.PlayScreen;

/**
 * Created by Home on 28/07/2017.
 */

public class GameSystem extends BaseSystem {

    private com.bryjamin.wickedwizard.MainGame game;

    public GameSystem(MainGame game){
        this.game = game;
    }

    @Override
    protected void processSystem() {


    }

    public void pauseGame(){
        game.pause();
    }

    public void unPauseGame(){
        if(game.getScreen() instanceof PlayScreen) {
            ((PlayScreen) game.getScreen()).unpause();
        }
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
        com.bryjamin.wickedwizard.screens.QuickSave.saveGame(world);
        game.getScreen().dispose();
        game.setScreen(new MenuScreen(game));
        MenuScreen.setMenuType(MenuScreen.MenuType.MAIN);

    }

}

