package com.mygdx.game.enemies;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.mygdx.game.Anim;

public class Scorpion extends HikingEnemy {

    public  Scorpion(Body body, Rectangle rectangle) {
        super(body, rectangle);
        this.moveAnimation = new Anim("atlas/scorpion.atlas", Animation.PlayMode.LOOP, "scorpion_move");
        this.hitAnimation = new Anim("atlas/scorpion.atlas", Animation.PlayMode.LOOP, "scorpion_hit");
        this.idleAnim = new Anim("atlas/scorpion.atlas", Animation.PlayMode.LOOP, "scorpion_idle");
        this.setMovementDirectionRight(false);
        this.setAlive(true);
        this.setHealthPoints(5);
        this.setDurationOfHitAnimation(120);
        this.setDisplayHitAnimation(false);
        this.setAnimationDirectionRight(true);
        this.setMovementSpeed(0.2f);
        this.setStepFrequency(0.4f);
    }
    @Override
    public void dispose(){
        this.moveAnimation.dispose();
        this.hitAnimation.dispose();
        this.idleAnim.dispose();
    }
}
