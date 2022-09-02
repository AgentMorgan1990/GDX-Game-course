package com.mygdx.game.services;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.mygdx.game.Bullet;
import com.mygdx.game.Hero;
import com.mygdx.game.Snake;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class ContactProcessingService {

    public static Set<Body> changesHikingEnemyMoveDirection = new HashSet<>();
    public static ArrayList<Body> reductionHealthPointContacts = new ArrayList<>();
    public static ArrayList<Body> destroyContact = new ArrayList<>();


    public static void executeDestroyContacts(ArrayList<Snake> snakes, ArrayList<Bullet>bullets, Hero hero) {
        for (Body body : destroyContact) {

            Iterator<Snake> snakeIterator = snakes.iterator();
            while (snakeIterator.hasNext()) {
                Snake snake = snakeIterator.next();
                if (snake.getBody().equals(body)) {
                    body.setActive(false);
                    snake.setAlive(false);
                    snakeIterator.remove();
                }
            }

            Iterator<Bullet> bulletIterator = bullets.iterator();
            while (bulletIterator.hasNext()) {
                Bullet bullet = bulletIterator.next();
                if (bullet.getBody().equals(body)) {
                    bullet.setAlive(false);
                    body.setActive(false);
                    bulletIterator.remove();
                }
            }
            if (hero.getBody().equals(body)) {
                hero.setAlive(false);
            }

        }
        destroyContact.clear();
    }

    public static void executeReductionHealthPointContacts(ArrayList<Snake> snakes, Hero hero) {

        for (Body body : reductionHealthPointContacts) {
            for (Snake snake : snakes) {
                if (snake.getBody().equals(body)) {
                    snake.setDisplayHitAnimation(true);
                    snake.setHealthPoints(snake.getHealthPoints() - 1);
                    if (snake.getHealthPoints() <= 0) {
                        destroyContact.add(body);
                    }
                }
            }
            if (hero.getBody().equals(body)) {
                if (hero.isAnimationDirectionRight()){
                    hero.getBody().applyForceToCenter(new Vector2(-1000000, 1000000), true);
                } else {
                    hero.getBody().applyForceToCenter(new Vector2(1000000, 1000000), true);
                }

                hero.setDisplayHitAnimation(true);
                hero.setHealthPoints(hero.getHealthPoints() - 1);
                if (hero.getHealthPoints() <= 0) {
                    hero.setAlive(false);
                }
            }
        }
        reductionHealthPointContacts.clear();
    }

    public static void executeChangesHikingEnemyMoveDirection(ArrayList<Snake> snakes) {
        for (Body snakeAction : changesHikingEnemyMoveDirection) {
            for (Snake snake : snakes) {
                if (snakeAction.equals(snake.getBody())) {
                    if (snake.isMovementDirectionRight()) {
                        snake.setMovementDirectionRight(false);
                    } else {
                        snake.setMovementDirectionRight(true);
                    }
                }
            }
        }
        changesHikingEnemyMoveDirection.clear();
    }

}
