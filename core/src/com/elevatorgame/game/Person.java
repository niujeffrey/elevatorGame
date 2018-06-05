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
        int level = random.nextInt(2);
        while (level == type) {
            level = random.nextInt(2);
        }
        personRect.y = level * 160;
    }

    public boolean checkLevel() {
        return personRect.y <= (type + 1) * 160 && personRect.y >= type * 160;
    }

}
