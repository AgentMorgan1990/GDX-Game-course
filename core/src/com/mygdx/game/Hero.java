package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;

public class Hero {

    private final Anim walkAnim;
    private final Anim idleAnim;
    private final Anim jumpAnim;
    private final Anim hitAnim;
    private final Anim attackAnim;
    private final Sound shotSound;
    private boolean animationDirectionRight;
    private final Rectangle rectangle;
    private final Body body;
    private int hitPoint;
    private int durationOfHitAnimation;
    private boolean displayHitAnimation;
    private boolean alive;

    public Hero(Rectangle rectangle, Body body) {
        this.walkAnim = new Anim("atlas/Hero.atlas", Animation.PlayMode.LOOP, "Walk");
        this.idleAnim = new Anim("atlas/Hero.atlas", Animation.PlayMode.LOOP, "Idle");
        this.jumpAnim = new Anim("atlas/Hero.atlas", Animation.PlayMode.LOOP, "Jump");
        this.hitAnim = new Anim("atlas/Hero.atlas", Animation.PlayMode.LOOP, "Hit");
        this.attackAnim = new Anim("atlas/Hero.atlas", Animation.PlayMode.LOOP, "Attack");
        this.shotSound = Gdx.audio.newSound(Gdx.files.internal("sounds/shot_2.mp3"));
        this.rectangle = rectangle;
        this.body = body;
        this.animationDirectionRight = true;
        this.hitPoint=3;
        this.durationOfHitAnimation=120;
        this.displayHitAnimation = false;
        this.alive = true;
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public void dispose() {
        walkAnim.dispose();
        idleAnim.dispose();
        jumpAnim.dispose();
        hitAnim.dispose();
        attackAnim.dispose();
        shotSound.dispose();
    }

    public int getHitPoint() {
        return hitPoint;
    }

    public void setHitPoint(int hitPoint) {
        this.hitPoint = hitPoint;
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

    public void setAnimationDirectionRight(boolean animationDirectionRight) {
        this.animationDirectionRight = animationDirectionRight;
    }

    public Anim getWalkAnim() {
        return walkAnim;
    }

    public Anim getIdleAnim() {
        return idleAnim;
    }

    public Anim getJumpAnim() {
        return jumpAnim;
    }

    public Anim getHitAnim() {
        return hitAnim;
    }

    public Anim getAttackAnim() {
        return attackAnim;
    }

    public Sound getShotSound() {
        return shotSound;
    }

    public boolean isAnimationDirectionRight() {
        return animationDirectionRight;
    }

    public Rectangle getRectangle() {
        return rectangle;
    }

    public Body getBody() {
        return body;
    }

    public void updateRectanglePosition() {
        rectangle.x = body.getPosition().x - rectangle.width / 2;
        rectangle.y = body.getPosition().y - rectangle.height / 2;
    }

    public void setTimeToAnimation(float deltaTime) {
        walkAnim.setTime(deltaTime);
        idleAnim.setTime(deltaTime);
        jumpAnim.setTime(deltaTime);
        hitAnim.setTime(deltaTime);
        attackAnim.setTime(deltaTime);
    }
}
