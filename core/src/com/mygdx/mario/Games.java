package com.mygdx.mario;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.mario.screens.GameOver;
import com.mygdx.mario.screens.PlayScreen;

public class Games extends Game {
	public static Games instance;
	private SpriteBatch spriteBatch;
	@Override
	public void create () {
        CustomAssetManager.init();
		instance = this;
		spriteBatch = new SpriteBatch();
		setScreen(new PlayScreen(spriteBatch, 1));
	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
		super.dispose();
		spriteBatch.dispose();
        CustomAssetManager.dispose();
	}
}
