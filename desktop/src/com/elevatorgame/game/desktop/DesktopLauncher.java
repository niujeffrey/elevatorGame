package com.elevatorgame.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.elevatorgame.game.ElevatorGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Elevator Game";
		config.width = 800;
		config.height = 600;
		new LwjglApplication(new ElevatorGame(), config);
	}
}
