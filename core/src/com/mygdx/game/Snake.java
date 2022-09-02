package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;

public class Snake {

    private boolean movementDirectionRight;
    private boolean animationDirectionRight;
    private final Anim moveAnimation;
    private final Anim hitAnimation;
    private final Body body;
    private final Rectangle rectangle;
    private int healthPoints;
    private int durationOfHitAnimation;
    private boolean displayHitAnimation;
    private TextureRegion textureRegion;

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public boolean isAlive() {
        return alive;
    }

    private boolean alive;

    public Snake(Body body, Rectangle rectangle) {

        this.moveAnimation = new Anim("atlas/Snake.atlas", Animation.PlayMode.LOOP, "Snake_move");
        this.hitAnimation = new Anim("atlas/Snake.atlas", Animation.PlayMode.LOOP, "Snake_hit");
        this.movementDirectionRight = false;
        this.body = body;
        this.rectangle = rectangle;
        this.alive = true;
        this.healthPoints = 3;
        this.durationOfHitAnimation = 120;
        this.displayHitAnimation = false;
        this.animationDirectionRight = true;
    }

    public TextureRegion getTextureRegion() {
        return textureRegion;
    }

    public void setTextureRegion(TextureRegion textureRegion) {
        this.textureRegion = textureRegion;
    }

    public boolean isAnimationDirectionRight() {
        return animationDirectionRight;
    }

    public void setAnimationDirectionRight(boolean animationDirectionRight) {
        this.animationDirectionRight = animationDirectionRight;
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

    public int getHealthPoints() {
        return healthPoints;
    }

    public void setHealthPoints(int healthPoints) {
        this.healthPoints = healthPoints;
    }

    public void setMovementDirectionRight(boolean movementDirectionRight) {
        this.movementDirectionRight = movementDirectionRight;
    }

    public boolean isMovementDirectionRight() {
        return movementDirectionRight;
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
