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
    private static long nextSpawn = 4000000000L;
    final ElevatorGame game;

    Texture backgroundImage;
    Texture takeNextImage;
    Rectangle takeNext0, takeNext1, takeNext2;

//    Sound arrivalSound;
//    Music backgroundMusic;
    OrthographicCamera camera;

    Elevator elevator;
    Array<Person> peopleList;
    ArrayList<Person> queue1, queue2, queue0;

    long lastSpawnTime;
    static int peopleServed = 0;

    public GameScreen(final ElevatorGame game) {
        this.game = game;
        backgroundImage = new Texture(Gdx.files.internal("background.png"));
        takeNextImage = new Texture(Gdx.files.internal("takenext.png"));

        takeNext0 = new Rectangle();
        takeNext1 = new Rectangle();
        takeNext2 = new Rectangle();
        takeNext0.x = takeNext1.x = takeNext2.x = 270;
        takeNext0.y = 10;
        takeNext1.y = 210;
        takeNext2.y = 410;
        takeNext0.width = takeNext1.width = takeNext2.width = 89;
        takeNext0.height = takeNext1.height = takeNext2.height = 185;

//        arrivalSound = Gdx.audio.newSound(Gdx.files.internal("ding.wav"));
//        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("mall.wav"));
//        backgroundMusic.setLooping(true);

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 600);

        elevator = new Elevator(4);
        peopleList = new Array<Person>();

        queue0 = new ArrayList<Person>();
        queue1 = new ArrayList<Person>();
        queue2 = new ArrayList<Person>();

        peopleServed = 0;
    }

    private void spawnPerson() {
        Random random = new Random();
        int randomInt = random.nextInt(3);
        Person person = new Person(randomInt);
        if (person.personRect.y / 200 == 0) {
            queue0.add(person);
            person.adjustX(queue0.indexOf(person));
        } else if (person.personRect.y / 200 == 1) {
            queue1.add(person);
            person.adjustX(queue1.indexOf(person));
        } else if (person.personRect.y / 200 == 2) {
            queue2.add(person);
            person.adjustX(queue2.indexOf(person));
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
            double x = nextSpawn / 1.01;
            nextSpawn = Math.round(x);
            peopleList.removeValue(p, true);
        }
    }

    private void updatePeople() {
        for (int i = 0; i < queue0.size(); i ++) {
            queue0.get(i).adjustX(i);
        }
        for (int i = 0; i < queue1.size(); i ++) {
            queue1.get(i).adjustX(i);
        }
        for (int i = 0; i < queue2.size(); i ++) {
            queue2.get(i).adjustX(i);
        }
    }

    private void processQueue(ArrayList<Person> queue, Elevator elevator) {
        try {
            Person p = queue.get(0);
            if (elevator.addPerson(p)) {
                queue.remove(0);
            }
        } catch (IndexOutOfBoundsException e) {
        }
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();

        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();
        game.batch.draw(backgroundImage, 0, 0);
        game.batch.draw(elevator.elevatorTexture, elevator.elevatorRect.x, elevator.elevatorRect.y);
        game.batch.draw(takeNextImage, takeNext0.x, takeNext0.y);
        game.batch.draw(takeNextImage, takeNext1.x, takeNext1.y);
        game.batch.draw(takeNextImage, takeNext2.x, takeNext2.y);
        for (Person p : peopleList) {
            game.batch.draw(p.personTexture, p.personRect.x, p.personRect.y);
        }
        game.font2.draw(game.batch, "People Served: " + peopleServed, 5, 590);
        game.font2.draw(game.batch, "People Waiting: " + Integer.toString(peopleList.size), 5, 570);
        game.batch.end();

        if (Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.W)) {
            elevator.move(200 * Gdx.graphics.getDeltaTime());
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN) || Gdx.input.isKeyPressed(Input.Keys.S)) {
            elevator.move(-200 * Gdx.graphics.getDeltaTime());
        }
        if (Gdx.input.justTouched()) {
            int x = Gdx.input.getX();
            int y = 600 - Gdx.input.getY();
            if (elevator.elevatorRect.contains(x, y)) {
                elevator.processClick(x);
            } else if (takeNext0.contains(x, y) && elevator.elevatorRect.y + 128 < 200) {
                processQueue(queue0, elevator);
            } else if (takeNext1.contains(x, y) && elevator.elevatorRect.y >= 200 &&
                    elevator.elevatorRect.y + 128 < 400) {
                processQueue(queue1, elevator);
            } else if (takeNext2.contains(x, y) && elevator.elevatorRect.y >= 400) {
                processQueue(queue2, elevator);
            }
        }

        if (TimeUtils.nanoTime() - lastSpawnTime > nextSpawn) {
            spawnPerson();
        }

        clearPeople();
        updatePeople();

        if (queue0.size() > 9 || queue1.size() > 9 || queue2.size() > 9) {
            game.setScreen(new GameOverScreen(game, peopleServed));
            dispose();
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
        backgroundImage.dispose();
//        backgroundMusic.dispose();
        takeNextImage.dispose();
    }
}
