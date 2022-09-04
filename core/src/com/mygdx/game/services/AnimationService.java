package com.mygdx.game.services;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.physics.box2d.Body;
import com.mygdx.game.Anim;
import com.mygdx.game.Hero;
import com.mygdx.game.enemies.HikingEnemy;

public class AnimationService {

    public static void updateHeroAnimation(Hero hero){
        hero.setTimeToAnimation(Gdx.graphics.getDeltaTime());
        hero.setAnimationDirectionRight(defineAnimationDirectionRight(hero.getBody(), hero.isAnimationDirectionRight()));
        updateAnimationDirectionForRightPicture(hero.getHitAnim(), hero.isAnimationDirectionRight());
        updateAnimationDirectionForRightPicture(hero.getJumpAnim(), hero.isAnimationDirectionRight());
        updateAnimationDirectionForRightPicture(hero.getAttackAnim(), hero.isAnimationDirectionRight());
        updateAnimationDirectionForRightPicture(hero.getWalkAnim(), hero.isAnimationDirectionRight());
        updateAnimationDirectionForRightPicture(hero.getIdleAnim(), hero.isAnimationDirectionRight());

        if (hero.isDisplayHitAnimation()) {
            hero.setDurationOfHitAnimation(hero.getDurationOfHitAnimation() - 1);
        }
        if (hero.getDurationOfHitAnimation() <= 0) {
            hero.setDurationOfHitAnimation(120);
            hero.setDisplayHitAnimation(false);
        }
        if (hero.isDisplayHitAnimation() && hero.isDisplayHitAnimation()) {
            hero.setTextureRegion(hero.getHitAnim().getFrame());
        } else if (hero.getBody().getLinearVelocity().y > 0.5f) {
            hero.setTextureRegion(hero.getJumpAnim().getFrame());
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.CONTROL_LEFT)) {
            hero.setTextureRegion(hero.getAttackAnim().getFrame());
        } else if (hero.getBody().getLinearVelocity().x <= -0.01f || hero.getBody().getLinearVelocity().x > 0.01f) {
            hero.setTextureRegion(hero.getWalkAnim().getFrame());
        } else {
            hero.setTextureRegion(hero.getIdleAnim().getFrame());
        }
    }

    public static void updateHikingEnemyAnimation(HikingEnemy hikingEnemy) {
        hikingEnemy.setAnimationDirectionRight(defineAnimationDirectionRight(hikingEnemy.getBody(), hikingEnemy.isAnimationDirectionRight()));
        hikingEnemy.setTimeToAnimation(Gdx.graphics.getDeltaTime());
        updateAnimationDirectionForLeftPicture(hikingEnemy.getMoveAnimation(), hikingEnemy.isAnimationDirectionRight());
        updateAnimationDirectionForLeftPicture(hikingEnemy.getHitAnimation(), hikingEnemy.isAnimationDirectionRight());

        if (hikingEnemy.isDisplayHitAnimation()) {
            hikingEnemy.setDurationOfHitAnimation(hikingEnemy.getDurationOfHitAnimation() - 1);
        }
        if (hikingEnemy.getDurationOfHitAnimation() <= 0) {
            hikingEnemy.setDurationOfHitAnimation(120);
            hikingEnemy.setDisplayHitAnimation(false);
        }
        if (hikingEnemy.isDisplayHitAnimation() && hikingEnemy.isDisplayHitAnimation()) {
            hikingEnemy.setTextureRegion(hikingEnemy.getHitAnimation().getFrame());
        } else {
            hikingEnemy.setTextureRegion(hikingEnemy.getMoveAnimation().getFrame());
        }
    }


    private static void updateAnimationDirectionForRightPicture(Anim anim, boolean animationDirectionRight) {
        if (!anim.getFrame().isFlipX() && !animationDirectionRight) {
            anim.getFrame().flip(true, false);
        }
        if (anim.getFrame().isFlipX() && animationDirectionRight) {
            anim.getFrame().flip(true, false);
        }
    }

    private static void updateAnimationDirectionForLeftPicture(Anim anim, boolean animationDirectionRight) {
        if (anim.getFrame().isFlipX() && !animationDirectionRight) {
            anim.getFrame().flip(true, false);
        }
        if (!anim.getFrame().isFlipX() && animationDirectionRight) {
            anim.getFrame().flip(true, false);
        }
    }

    private static boolean defineAnimationDirectionRight(Body body, boolean animationDirection) {
        boolean animationDirectionRight = animationDirection;
        if (body.getLinearVelocity().x <= -0.01f) {
            animationDirectionRight = false;
        }
        if (body.getLinearVelocity().x > 0.01f) {
            animationDirectionRight = true;
        }
        return animationDirectionRight;
    }

}
