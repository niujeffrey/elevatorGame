package com.elevatorgame.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;

public class GameOverScreen implements Screen {

    OrthographicCamera camera;
    final ElevatorGame game;
    int ppl;

    public GameOverScreen(ElevatorGame game, int ppl) {
        this.game = game;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 600);
        this.ppl = ppl;
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.4f, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();
        game.font.draw(game.batch, "GAME OVER\nScore: " + ppl, 300, 450);
        game.font.draw(game.batch, "Click on left half of screen to quit, click on right half to" +
                "play again", 200, 300);
        game.batch.end();

        if (Gdx.input.justTouched()) {
            int x = Gdx.input.getX();
            if (x < 400) {
                dispose();
                Gdx.app.exit();
            } else {
                game.setScreen(new GameScreen(game));
                dispose();
            }
        }
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void show() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
    }

}
