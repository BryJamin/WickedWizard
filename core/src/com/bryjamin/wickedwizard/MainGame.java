package com.bryjamin.wickedwizard;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;
import com.bryjamin.wickedwizard.assets.SoundFileStrings;
import com.bryjamin.wickedwizard.utils.Measure;


public class MainGame extends Game {

	public static final float GAME_HEIGHT = 1800;
	public static final float GAME_WIDTH = 3000;
	public static final float GAME_BORDER = 150;



	public static final int GAME_UNITS = 30;

	//This means there are 96 tiles wide,
	//60 tiles high

	public SpriteBatch batch;
	public AssetManager assetManager = new AssetManager();


	@Override
	public void create () {
		batch = new SpriteBatch(700);
		//Gdx.input.setCursorCatched(true);
		//Gdx.input.setCursorPosition(0, 0);
		assetManager.load(com.bryjamin.wickedwizard.assets.FileLocationStrings.spriteAtlas, TextureAtlas.class);

		SoundFileStrings.loadSoundsToManager(assetManager);

		FileHandleResolver resolver = new InternalFileHandleResolver();
		assetManager.setLoader(FreeTypeFontGenerator.class, new FreeTypeFontGeneratorLoader(resolver));
		assetManager.setLoader(BitmapFont.class, ".ttf", new FreetypeFontLoader(resolver));


		FreetypeFontLoader.FreeTypeFontLoaderParameter size1Params = new FreetypeFontLoader.FreeTypeFontLoaderParameter();
		size1Params.fontFileName = "fonts/Roboto-Black.ttf";
		size1Params.fontParameters.size = (int) Measure.units(3f);
		size1Params.fontParameters.borderColor = new Color(Color.BLACK);
		size1Params.fontParameters.borderWidth = 0;
		size1Params.fontParameters.minFilter = Texture.TextureFilter.Linear;
		size1Params.fontParameters.magFilter = Texture.TextureFilter.Linear;
		//size1Params.fontParameters.genMipMaps = true;
        //size1Params.fontParameters.minFilter = Texture.TextureFilter.MipMapNearestNearest;
		assetManager.load(com.bryjamin.wickedwizard.assets.FontAssets.small, BitmapFont.class, size1Params);

        FreetypeFontLoader.FreeTypeFontLoaderParameter size2Params = new FreetypeFontLoader.FreeTypeFontLoaderParameter();
        size2Params.fontFileName = "fonts/Roboto-Black.ttf";
        size2Params.fontParameters.size = (int) Measure.units(4f);
        size2Params.fontParameters.borderColor = new Color(Color.BLACK);
        size2Params.fontParameters.borderWidth = 0;
		size2Params.fontParameters.minFilter = Texture.TextureFilter.Linear;
		size2Params.fontParameters.magFilter = Texture.TextureFilter.Linear;
		//size1Params.fontParameters.genMipMaps = true;
        //size1Params.fontParameters.minFilter = Texture.TextureFilter.MipMapNearestNearest;
        assetManager.load(com.bryjamin.wickedwizard.assets.FontAssets.medium, BitmapFont.class, size2Params);


		setScreen(new com.bryjamin.wickedwizard.screens.LoadingScreen(this));
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

