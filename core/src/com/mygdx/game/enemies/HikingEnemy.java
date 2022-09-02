package com.mygdx.game.enemies;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.mygdx.game.Anim;

public abstract class HikingEnemy {
    private boolean movementDirectionRight;
    private boolean animationDirectionRight;
    Anim moveAnimation;
    Anim hitAnimation;
    Anim idleAnim;
    private Body body;
    private Rectangle rectangle;
    private int healthPoints;
    private int durationOfHitAnimation;
    private boolean displayHitAnimation;
    private TextureRegion textureRegion;
    private boolean alive;
    private int movementSpeed;
    private int stepFrequency;

    public HikingEnemy(Body body, Rectangle rectangle ){
        this.body = body;
        this.rectangle = rectangle;
    }

    public void dispose() {
        this.moveAnimation.dispose();
        this.hitAnimation.dispose();
    }

    public void setTimeToAnimation(float deltaTime) {
        moveAnimation.setTime(deltaTime);
        hitAnimation.setTime(deltaTime);
    }
    public void updateRectanglePosition() {
        rectangle.x = body.getPosition().x - rectangle.width / 2;
        rectangle.y = body.getPosition().y - rectangle.height / 2;
    }

    public boolean isMovementDirectionRight() {
        return movementDirectionRight;
    }

    public void setMovementDirectionRight(boolean movementDirectionRight) {
        this.movementDirectionRight = movementDirectionRight;
    }

    public boolean isAnimationDirectionRight() {
        return animationDirectionRight;
    }

    public void setAnimationDirectionRight(boolean animationDirectionRight) {
        this.animationDirectionRight = animationDirectionRight;
    }

    public Anim getMoveAnimation() {
        return moveAnimation;
    }

    public void setMoveAnimation(Anim moveAnimation) {
        this.moveAnimation = moveAnimation;
    }

    public Anim getHitAnimation() {
        return hitAnimation;
    }

    public void setHitAnimation(Anim hitAnimation) {
        this.hitAnimation = hitAnimation;
    }

    public Anim getIdleAnim() {
        return idleAnim;
    }

    public void setIdleAnim(Anim idleAnim) {
        this.idleAnim = idleAnim;
    }

    public Body getBody() {
        return body;
    }

    public void setBody(Body body) {
        this.body = body;
    }

    public Rectangle getRectangle() {
        return rectangle;
    }

    public void setRectangle(Rectangle rectangle) {
        this.rectangle = rectangle;
    }

    public int getHealthPoints() {
        return healthPoints;
    }

    public void setHealthPoints(int healthPoints) {
        this.healthPoints = healthPoints;
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

    public TextureRegion getTextureRegion() {
        return textureRegion;
    }

    public void setTextureRegion(TextureRegion textureRegion) {
        this.textureRegion = textureRegion;
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public int getMovementSpeed() {
        return movementSpeed;
    }

    public void setMovementSpeed(int movementSpeed) {
        this.movementSpeed = movementSpeed;
    }

    public int getStepFrequency() {
        return stepFrequency;
    }

    public void setStepFrequency(int stepFrequency) {
        this.stepFrequency = stepFrequency;
    }
}
