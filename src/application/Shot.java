package application;

import application.Player;
import javafx.scene.paint.Color;

import static application.SpaceInvaders.*;

public class Shot {

    public boolean toRemove;

    int posX, posY, speed = 10;
    static final int size = 6;

    public Shot(int posX, int posY) {
        this.posX = posX;
        this.posY = posY;
    }

    public void update() {
        posY-=speed;
    }

    public void draw() {
        gc.setFill(Color.RED);
        gc.fillRect(posX, posY, size - 2, size + 5);
    }

    public boolean collide(Player player) {
        int distance = distance(this.posX + size / 2, this.posY + size / 2,
                player.posX + player.size / 2, player.posY + player.size / 2);
        return distance  < player.size / 2 + size / 2;
    }


}
