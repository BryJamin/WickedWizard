package com.byrjamin.wickedwizard;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.byrjamin.wickedwizard.screens.PlayScreen;



//TODO Global texture region? so it is easier to access the same one through the whole file.


//TODO Add Projectile and instance spell casts.

//TODO allow each spell to have an aoe component to them (If two enemies overlap both get hit).

//TODO Add enemy management (spawn management etc).

//TODO Add a tracker, if the projectile goes off screen stop drawing it


public class MainGame extends Game {

	public static final int GAME_HEIGHT = 1200;
	public static final int GAME_WIDTH = 1920;


	public static final int GAME_UNITS = 20;

	//This means there are 96 tiles wide,
	//60 tiles high

	public SpriteBatch batch;
	Texture img;

	@Override
	public void create () {
		batch = new SpriteBatch();
		setScreen(new PlayScreen(this));
	}

	@Override
	public void render () {
		super.render();
	}
	
/*	@Override
	public void create () {
		batch = new SpriteBatch();
		img = new Texture("badlogic.jpg");
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		batch.draw(img, 0, 0);
		batch.end();
	}*/
}

