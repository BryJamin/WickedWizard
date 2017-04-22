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

//TODO this allows you to pack textures without needing to open the texture packer just uncomment it
	String projectPath = System.getProperty("user.dir");
		String inputDir = projectPath + "/images/tobepacked";
		String outputDir = projectPath;
		String packFileName = "sprite";

		Settings settings = new Settings();
		settings.maxWidth = 2048;
		settings.maxHeight = 2048;
		settings.filterMin = Texture.TextureFilter.MipMapLinearLinear;
		settings.combineSubdirectories = true;

		TexturePacker.process(settings, inputDir,outputDir,packFileName);

		new LwjglApplication(new MainGame(), config);
	}
}
