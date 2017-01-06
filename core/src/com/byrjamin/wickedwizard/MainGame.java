package com.byrjamin.wickedwizard;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.byrjamin.wickedwizard.screens.PlayScreen;


public class MainGame extends Game {

	public static final float GAME_HEIGHT = 1200;
	public static final float GAME_WIDTH = 1920;


	public static final int GAME_UNITS = 20;

	//This means there are 96 tiles wide,
	//60 tiles high

	public SpriteBatch batch;
	public AssetManager manager = new AssetManager();

	private boolean stop = true;


	Texture img;

	@Override
	public void create () {
		batch = new SpriteBatch();
		manager.load("sprite.atlas", TextureAtlas.class);
	}

	@Override
	public void render () {
		super.render();

		if(manager.update() && stop)
		{
			setScreen(new PlayScreen(this));
			stop = false;
		}

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

