package com.mygdx.game.services;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.physics.box2d.Body;
import com.mygdx.game.Anim;
import com.mygdx.game.Hero;
import com.mygdx.game.Snake;

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
        } else if (hero.getBody().getLinearVelocity().y > 60) {
            hero.setTextureRegion(hero.getJumpAnim().getFrame());
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.CONTROL_LEFT)) {
            hero.setTextureRegion(hero.getAttackAnim().getFrame());
        } else if (hero.getBody().getLinearVelocity().x <= -10 || hero.getBody().getLinearVelocity().x > 10) {
            hero.setTextureRegion(hero.getWalkAnim().getFrame());
        } else {
            hero.setTextureRegion(hero.getIdleAnim().getFrame());
        }
    }

    public static void updateSnakeAnimation(Snake snake) {
        snake.setAnimationDirectionRight(defineAnimationDirectionRight(snake.getBody(), snake.isAnimationDirectionRight()));
        snake.setTimeToAnimation(Gdx.graphics.getDeltaTime());
        updateAnimationDirectionForLeftPicture(snake.getMoveAnimation(), snake.isAnimationDirectionRight());
        updateAnimationDirectionForLeftPicture(snake.getHitAnimation(), snake.isAnimationDirectionRight());

        if (snake.isDisplayHitAnimation()) {
            snake.setDurationOfHitAnimation(snake.getDurationOfHitAnimation() - 1);
        }
        if (snake.getDurationOfHitAnimation() <= 0) {
            snake.setDurationOfHitAnimation(120);
            snake.setDisplayHitAnimation(false);
        }
        if (snake.isDisplayHitAnimation() && snake.isDisplayHitAnimation()) {
            snake.setTextureRegion(snake.getHitAnimation().getFrame());
        } else {
            snake.setTextureRegion(snake.getMoveAnimation().getFrame());
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
        if (body.getLinearVelocity().x <= -10) {
            animationDirectionRight = false;
        }
        if (body.getLinearVelocity().x > 10) {
            animationDirectionRight = true;
        }
        return animationDirectionRight;
    }

}
