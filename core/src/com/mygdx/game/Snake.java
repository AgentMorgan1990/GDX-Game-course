package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;

public class Snake {

    private boolean animationDirectionRight;
    private final Anim moveAnimation;
    private final Anim hitAnimation;
    private final Body body;
    private final Rectangle rectangle;
    private final RectangleMapObject leftSnakeBoarder;
    private final RectangleMapObject rightSnakeBoarder;
    private int hitPoints;
    private int durationOfHitAnimation;
    private boolean displayHitAnimation;

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public boolean isAlive() {
        return alive;
    }

    private boolean alive;

    public Snake(Body body, Rectangle rectangle, RectangleMapObject leftSnakeBoarder, RectangleMapObject rightSnakeBoarder) {

        this.moveAnimation = new Anim("atlas/Snake.atlas", Animation.PlayMode.LOOP, "Snake_move");
        this.hitAnimation = new Anim("atlas/Snake.atlas", Animation.PlayMode.LOOP, "Snake_hit");
        this.animationDirectionRight = true;
        this.body = body;
        this.rectangle = rectangle;
        this.leftSnakeBoarder = leftSnakeBoarder;
        this.rightSnakeBoarder = rightSnakeBoarder;
        this.alive = true;
        this.hitPoints = 3;
        this.durationOfHitAnimation = 120;
        this.displayHitAnimation = false;
    }

    public int getDurationOfHitAnimation() {
        return durationOfHitAnimation;
    }

    public void setDurationOfHitAnimation(int durationOfHitAnimation) {
        this.durationOfHitAnimation = durationOfHitAnimation;
    }

    public boolean isDisplayHitAnimation() {
        return displayHitAnimation;
    }

    public void setDisplayHitAnimation(boolean displayHitAnimation) {
        this.displayHitAnimation = displayHitAnimation;
    }

    public Anim getHitAnimation() {
        return hitAnimation;
    }

    public int getHitPoints() {
        return hitPoints;
    }

    public void setHitPoints(int hitPoints) {
        this.hitPoints = hitPoints;
    }

    public void setAnimationDirectionRight(boolean animationDirectionRight) {
        this.animationDirectionRight = animationDirectionRight;
    }

    public boolean isAnimationDirectionRight() {
        return animationDirectionRight;
    }

    public Anim getMoveAnimation() {
        return moveAnimation;
    }

    public Body getBody() {
        return body;
    }

    public Rectangle getRectangle() {
        return rectangle;
    }

    public RectangleMapObject getLeftSnakeBoarder() {
        return leftSnakeBoarder;
    }

    public RectangleMapObject getRightSnakeBoarder() {
        return rightSnakeBoarder;
    }

    public void dispose() {
        this.moveAnimation.dispose();
    }

    public void setTimeToAnimation(float deltaTime) {

        moveAnimation.setTime(deltaTime);
        hitAnimation.setTime(deltaTime);
    }

    public void updateRectanglePosition() {
        rectangle.x = body.getPosition().x - rectangle.width / 2;
        rectangle.y = body.getPosition().y - rectangle.height / 2;
    }
}
