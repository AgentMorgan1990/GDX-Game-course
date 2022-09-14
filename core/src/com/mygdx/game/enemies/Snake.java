package com.mygdx.game.enemies;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.mygdx.game.Anim;

public class Snake extends HikingEnemy {

    public Snake(Body body, Rectangle rectangle) {
        super(body,rectangle);
        this.moveAnimation = new Anim("atlas/Snake.atlas", Animation.PlayMode.LOOP, "Snake_move");
        this.hitAnimation = new Anim("atlas/Snake.atlas", Animation.PlayMode.LOOP, "Snake_hit");
        this.setMovementDirectionRight(false);
        this.setAlive(true);
        this.setHealthPoints(3);
        this.setDurationOfHitAnimation(120);
        this.setDisplayHitAnimation(false);
        this.setAnimationDirectionRight(true);
        this.setMovementSpeed(0.1f);
        this.setStepFrequency(0.2f);
    }
}
