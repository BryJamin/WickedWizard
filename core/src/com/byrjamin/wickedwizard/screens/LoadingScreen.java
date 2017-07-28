package com.byrjamin.wickedwizard.screens;

import com.badlogic.gdx.Screen;
import com.byrjamin.wickedwizard.MainGame;

/**
 * Created by Home on 06/01/2017.
 */
public class LoadingScreen extends AbstractScreen {


    public LoadingScreen(MainGame game) {
        super(game);
    }

    @Override
    public void show() {


    }

    @Override
    public void render(float delta) {

        if(game.manager.update())
        {
            game.setScreen(new CreditsScreen(game));
            //game.setScreen(new MenuScreen(game));
        }

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
