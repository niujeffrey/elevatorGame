package com.elevatorgame.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class ElevatorGame extends Game {

    SpriteBatch batch;
    BitmapFont font;
    BitmapFont font2;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
        font = new BitmapFont();
        font2 = new BitmapFont(Gdx.files.internal("other.fnt"));
        this.setScreen(new MainMenuScreen(this));
	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}
}
