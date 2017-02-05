package com.byrjamin.wickedwizard.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.Texture;
import com.byrjamin.wickedwizard.MainGame;

import com.badlogic.gdx.tools.texturepacker.TexturePacker;
import com.badlogic.gdx.tools.texturepacker.TexturePacker.Settings;

import java.io.IOException;

public class DesktopLauncher {
	public static void main (String[] arg) throws IOException{
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.forceExit = true;


/*
	String projectPath = System.getProperty("user.dir");
		String inputDir = projectPath + "/images/tobepacked";
		String outputDir = projectPath + "/android/assets";
		String packFileName = "sprite";

		Settings settings = new Settings();
		settings.maxWidth = 2048;
		settings.maxHeight = 2048;
		settings.filterMin = Texture.TextureFilter.MipMapLinearLinear;
		settings.combineSubdirectories = true;

		TexturePacker.process(settings, inputDir,outputDir,packFileName);
*/

/*		Settings settings = new Settings();
		settings.maxWidth = 512;
		settings.maxHeight = 512;
		TexturePacker.process(settings, "../images", "../game-android/assets", "game");*/


		new LwjglApplication(new MainGame(), config);
	}
}
