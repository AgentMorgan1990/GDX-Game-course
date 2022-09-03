package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;

public class Bullet {
    private final Body body;
    private final Rectangle rectangle;

    private final Texture texture;
    private boolean alive;
    private int bulletLifeTime;

    public Bullet(Body body, Rectangle rectangle) {
        this.body = body;
        this.rectangle = rectangle;
        this.texture = new Texture("bullet.png");
        this.bulletLifeTime = 180;
        alive = true;
    }


    public void updateRectanglePosition() {
        rectangle.x = body.getPosition().x - rectangle.getWidth() / 2;
        rectangle.y = body.getPosition().y - rectangle.getHeight() / 2;

    }

    public void dispose() {
        texture.dispose();
    }

    public int getBulletLifeTime() {
        return bulletLifeTime;
    }

    public void setBulletLifeTime(int bulletLifeTime) {
        this.bulletLifeTime = bulletLifeTime;
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public Body getBody() {
        return body;
    }

    public Rectangle getRectangle() {
        return rectangle;
    }

    public Texture getTexture() {
        return texture;
    }
}
