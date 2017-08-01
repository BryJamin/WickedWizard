package com.byrjamin.wickedwizard;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;
import com.byrjamin.wickedwizard.assets.Assets;
import com.byrjamin.wickedwizard.assets.FileLocationStrings;
import com.byrjamin.wickedwizard.assets.Mix;
import com.byrjamin.wickedwizard.assets.MusicStrings;
import com.byrjamin.wickedwizard.assets.SoundFileStrings;
import com.byrjamin.wickedwizard.screens.LoadingScreen;
import com.byrjamin.wickedwizard.utils.Measure;


public class MainGame extends Game {

	public static final float GAME_HEIGHT = 1200;
	public static final float GAME_WIDTH = 2000;
	public static final float GAME_BORDER = 100;



	public static final int GAME_UNITS = 20;

	//This means there are 96 tiles wide,
	//60 tiles high

	public SpriteBatch batch;
	public AssetManager manager = new AssetManager();


	@Override
	public void create () {
		batch = new SpriteBatch();
		//Gdx.input.setCursorCatched(true);
		//Gdx.input.setCursorPosition(0, 0);
		manager.load(FileLocationStrings.spriteAtlas, TextureAtlas.class);

		manager.load(MusicStrings.BG_MAIN_MENU.getFileName(), Music.class);
		manager.load(MusicStrings.BG_LEVEL_ONE.getFileName(), Music.class);
		manager.load(MusicStrings.BG_LEVEL_TWO.getFileName(), Music.class);
		manager.load(MusicStrings.BG_LEVEL_THREE.getFileName(), Music.class);
		manager.load(MusicStrings.BG_LEVEL_FOUR.getFileName(), Music.class);
		manager.load(MusicStrings.BG_LEVEL_FIVE.getFileName(), Music.class);

        manager.load(SoundFileStrings.playerFire, Sound.class);
        manager.load(SoundFileStrings.enemyFire, Sound.class);

		for(String s : SoundFileStrings.explosionStrings) manager.load(s, Sound.class);
		for(Mix m : SoundFileStrings.hitMegaMix) manager.load(m.getFileName(), Sound.class);

        manager.load(SoundFileStrings.coinPickUp, Sound.class);
		manager.load(SoundFileStrings.jumpMix.getFileName(), Sound.class);


        System.out.println(Gdx.files.getLocalStoragePath());
		System.out.println(Gdx.files.getExternalStoragePath());


		FileHandleResolver resolver = new InternalFileHandleResolver();
		manager.setLoader(FreeTypeFontGenerator.class, new FreeTypeFontGeneratorLoader(resolver));
		manager.setLoader(BitmapFont.class, ".ttf", new FreetypeFontLoader(resolver));


		FreetypeFontLoader.FreeTypeFontLoaderParameter size1Params = new FreetypeFontLoader.FreeTypeFontLoaderParameter();
		size1Params.fontFileName = "fonts/Roboto-Black.ttf";
		size1Params.fontParameters.size = (int) Measure.units(3f);
		size1Params.fontParameters.borderColor = new Color(Color.BLACK);
		size1Params.fontParameters.borderWidth = 0;
		size1Params.fontParameters.minFilter = Texture.TextureFilter.Linear;
		size1Params.fontParameters.magFilter = Texture.TextureFilter.Linear;
		//size1Params.fontParameters.genMipMaps = true;
        //size1Params.fontParameters.minFilter = Texture.TextureFilter.MipMapNearestNearest;
		manager.load(Assets.small, BitmapFont.class, size1Params);

        FreetypeFontLoader.FreeTypeFontLoaderParameter size2Params = new FreetypeFontLoader.FreeTypeFontLoaderParameter();
        size2Params.fontFileName = "fonts/Roboto-Black.ttf";
        size2Params.fontParameters.size = (int) Measure.units(4f);
        size2Params.fontParameters.borderColor = new Color(Color.BLACK);
        size2Params.fontParameters.borderWidth = 0;
		size2Params.fontParameters.minFilter = Texture.TextureFilter.Linear;
		size2Params.fontParameters.magFilter = Texture.TextureFilter.Linear;
		//size1Params.fontParameters.genMipMaps = true;
        //size1Params.fontParameters.minFilter = Texture.TextureFilter.MipMapNearestNearest;
        manager.load(Assets.medium, BitmapFont.class, size2Params);


		setScreen(new LoadingScreen(this));
		//setScreen(new CreditsScreen(this));
	}

/*	@Override
	public void render () {
		super.render();
	}*/
	
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

