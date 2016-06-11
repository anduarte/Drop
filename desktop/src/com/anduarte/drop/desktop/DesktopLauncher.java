package com.anduarte.drop.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.anduarte.drop.Drop;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

		// Configuration for the window
        config.title = "Drop";
        config.width = 800;
        config.height = 480;

		new LwjglApplication(new Drop(), config);
	}
}
