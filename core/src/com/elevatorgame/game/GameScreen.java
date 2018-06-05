package com.elevatorgame.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.ArrayList;
import java.util.Random;

public class GameScreen implements Screen {

    final ElevatorGame game;

    Texture backgroundImage;
    Texture elevatorImage;
    Array<Texture> personImages;
    Sound arrivalSound;
    Music backgroundMusic;
    OrthographicCamera camera;

    Elevator elevator;
    Array<Person> peopleList;
    ArrayList<Person> queue1, queue2, queue0;

    long lastSpawnTime;
    static int peopleServed = 0;

    public GameScreen(final ElevatorGame game) {
        this.game = game;
        backgroundImage = new Texture(Gdx.files.internal("background.png"));

//        arrivalSound = Gdx.audio.newSound(Gdx.files.internal("ding.wav"));
        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("mall.wav"));
        backgroundMusic.setLooping(true);

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 600);

        elevator = new Elevator(4);
        peopleList = new Array<Person>();

        queue0 = new ArrayList<Person>();
        queue1 = new ArrayList<Person>();
        queue2 = new ArrayList<Person>();
    }

    private void spawnPerson() {
        Random random = new Random();
        int randomInt = random.nextInt(3);
        Person person = new Person(randomInt);
        switch (randomInt) {
            case 0:
                queue0.add(person);
                person.adjustX(queue0.indexOf(person));
                break;
            case 1:
                queue1.add(person);
                person.adjustX(queue1.indexOf(person));
                break;
            case 2:
                queue2.add(person);
                person.adjustX(queue2.indexOf(person));
                break;
            default:
                break;
        }
        peopleList.add(person);
        lastSpawnTime = TimeUtils.nanoTime();
    }

    private void clearPeople() {
        Array<Person> toClear = new Array<Person>();
        for (Person p : peopleList) {
            if (p.status.equals("Arrived")) {
                toClear.add(p);
            }
        }
        for (Person p : toClear) {
            peopleList.removeValue(p, true);
        }
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();

        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();
        game.font.draw(game.batch, "People Served: " + peopleServed, 0, 480);
        game.font.draw(game.batch, "People Waiting: " + Integer.toString(peopleList.size), 10, 480);
        game.batch.draw(backgroundImage, 0, 0);
        for (Person p : peopleList) {
            game.batch.draw(p.personTexture, p.personRect.x, p.personRect.y);
        }
        game.batch.draw(elevator.elevatorTexture, elevator.elevatorRect.x, elevator.elevatorRect.y);
        game.batch.end();

        if (Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.W)) {
            elevator.move(200 * Gdx.graphics.getDeltaTime());
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN) || Gdx.input.isKeyPressed(Input.Keys.S)) {
            elevator.move(-200 * Gdx.graphics.getDeltaTime());
        }
        if (Gdx.input.isTouched()) {
            int x = Gdx.input.getX();
            int y = 600 - Gdx.input.getY();
            if (elevator.elevatorRect.contains(x, y)) {
                elevator.processClick(y);
            }
        }

        if (TimeUtils.nanoTime() - lastSpawnTime > 9000000000L) {
            spawnPerson();
        }

        clearPeople();
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
