package com.elevatorgame.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

public class Elevator {

    Person[] passengers;
    int capacity;

    Texture elevatorTexture;
    Rectangle elevatorRect;

    public Elevator(int capacity) {
        this.capacity = capacity;
        this.passengers = new Person[4];

        this.elevatorTexture = new Texture(Gdx.files.internal("elevator.png"));
        this.elevatorRect = new Rectangle();
        elevatorRect.x = 0;
        elevatorRect.y = 0;

        elevatorRect.width = 256;
        elevatorRect.height = 128;
    }

    public boolean addPerson(Person p) {
        for (int i = 0; i < 4; i++) {
            if (passengers[i] == null) {
                passengers[i] = p;
                p.personRect.x = 64 * i;
                p.personRect.y = elevatorRect.y;
                p.status = "On elevator";
                return true;
            }
        }
        return false;
    }

    public void popPerson(int index) {
        Person p = passengers[index];
        passengers[index] = null;
        if (p == null) {
            return;
        }
        if (p.checkLevel()) {
            p.status = "Arrived";
            p.personRect.y = p.type * 160;
            GameScreen.peopleServed++;
            // TODO: Play Ding sound
        } else {
            passengers[index] = p;
        }
    }

    public void move(double amount) {
        if (elevatorRect.y + amount >= 0 && elevatorRect.y + amount + 128 < 600) {
            elevatorRect.y += amount;
            for (Person p : passengers) {
                if (p != null) p.personRect.y += amount;
            }
        }
    }

    public void processClick(int x) {
        if (0 <= x && x < 64) {
            popPerson(0);
        } else if (64 <= x && x < 128) {
            popPerson(1);
        } else if (128 <= x && x < 192) {
            popPerson(2);
        } else if (192 <= x && x < 256) {
            popPerson(3);
        }
    }

}
