package com.mygdx.mario.desktop;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.mario.Config;
import com.mygdx.mario.Games;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = Config.GAME_WIDTH;
		config.height = Config.GAME_HEIGHT;
		config.title = "Mario";
		new LwjglApplication(new Games() {
		}, config);
	}
}
