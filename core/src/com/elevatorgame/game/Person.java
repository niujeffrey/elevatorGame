package com.elevatorgame.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

import java.util.Random;

public class Person {

    private static final String[] spriteList = {"person0.png", "person1.png", "person2.png"};
    private static final Random random = new Random();

    String status; // Describes whether a Person is walking to the elevator, waiting, inside, or
                   // walking away from the elevator
    int type; // Describes the level that the Person wishes to go to

    Texture personTexture;
    Rectangle personRect;

    public Person(int type) {
        this.type = type;
        this.status = "Towards";
        this.personTexture = new Texture(Gdx.files.internal(spriteList[type]));

        this.personRect = new Rectangle();
        personRect.x = 700;
        int level = random.nextInt(3);
        while (level == type) {
            level = random.nextInt(3);
        }
        personRect.y = level * 200;
    }

    public boolean checkLevel() {
        return personRect.y + 128 <= (type + 1) * 200 && personRect.y >= type * 200;
    }

    public void adjustX(int queuePosition) {
        personRect.x = 256 + (queuePosition * 64);
    }

    public String toString() {
        return status + type;
    }
}
