package com.byrjamin.wickedwizard.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.byrjamin.wickedwizard.MainGame;

import java.io.IOException;

public class DesktopLauncher {
	public static void main (String[] arg) throws IOException{
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		new LwjglApplication(new MainGame(), config);
	}
}
