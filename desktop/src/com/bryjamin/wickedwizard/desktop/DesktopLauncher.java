package com.bryjamin.wickedwizard.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.bryjamin.wickedwizard.MainGame;

import java.io.IOException;

public class DesktopLauncher {
	public static void main (String[] arg) throws IOException{
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.forceExit = true;

//TODO this allows you to pack textures without needing to open the texture packer just uncomment it

/*		 settings = new Settings();
		settings.maxWidth = 1024;
		settings.maxHeight = 1024;
		settings.filterMin = Texture.TextureFilter.Linear;
		settings.filterMag = Texture.TextureFilter.Linear;
		settings.duplicatePadding = true;
		settings.combineSubdirectories = true;*/
/*
		TexturePacker.process(settings, projectPath + "/images/tobepacked/item",outputDir,"item");*/

		new LwjglApplication(new MainGame(), config);
	}
}
