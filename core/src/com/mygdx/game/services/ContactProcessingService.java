package com.mygdx.game.services;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.mygdx.game.Bullet;
import com.mygdx.game.Hero;
import com.mygdx.game.enemies.HikingEnemy;
import com.mygdx.game.screens.GameScreen;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class ContactProcessingService {

    public static Set<Body> changesHikingEnemyMoveDirection = new HashSet<>();
    public static ArrayList<Body> reductionHealthPointContacts = new ArrayList<>();
    public static ArrayList<Body> destroyContact = new ArrayList<>();


    public static void executeDestroyContacts(ArrayList<HikingEnemy> hikingEnemies, ArrayList<Bullet>bullets, Hero hero) {
        for (Body body : destroyContact) {

            Iterator<HikingEnemy> hikingEnemyIterator = hikingEnemies.iterator();
            while (hikingEnemyIterator.hasNext()) {
                HikingEnemy hikingEnemy = hikingEnemyIterator.next();
                if (hikingEnemy.getBody().equals(body)) {
                    body.setActive(false);
                    hikingEnemy.setAlive(false);
                    GameScreen.bodyToDeleting.add(body);
                    hikingEnemyIterator.remove();
                }
            }

            Iterator<Bullet> bulletIterator = bullets.iterator();
            while (bulletIterator.hasNext()) {
                Bullet bullet = bulletIterator.next();
                if (bullet.getBody().equals(body)) {
                    bullet.setAlive(false);
                    body.setActive(false);
                    GameScreen.bodyToDeleting.add(body);
                    bulletIterator.remove();
                }
            }
            if (hero.getBody().equals(body)) {
                hero.setAlive(false);
            }

        }
        destroyContact.clear();
    }

    public static void executeReductionHealthPointContacts(ArrayList<HikingEnemy> hikingEnemies, Hero hero) {

        for (Body body : reductionHealthPointContacts) {
            for (HikingEnemy hikingEnemy : hikingEnemies) {
                if (hikingEnemy.getBody().equals(body)) {
                    hikingEnemy.setDisplayHitAnimation(true);
                    hikingEnemy.setHealthPoints( hikingEnemy.getHealthPoints() - 1);
                    if ( hikingEnemy.getHealthPoints() <= 0) {
                        destroyContact.add(body);
                    }
                }
            }
            if (hero.getBody().equals(body)) {
                if (hero.isAnimationDirectionRight()){
                    hero.getBody().applyForceToCenter(new Vector2(-1.5f, 5.5f), true);
                } else {
                    hero.getBody().applyForceToCenter(new Vector2(1.5f, 5.5f), true);
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

    public static void executeChangesHikingEnemyMoveDirection(ArrayList<HikingEnemy> hikingEnemies) {
        for (Body snakeAction : changesHikingEnemyMoveDirection) {
            for (HikingEnemy hikingEnemy : hikingEnemies) {
                if (snakeAction.equals(hikingEnemy.getBody())) {
                    if (hikingEnemy.isMovementDirectionRight()) {
                        hikingEnemy.setMovementDirectionRight(false);
                    } else {
                        hikingEnemy.setMovementDirectionRight(true);
                    }
                }
            }
        }
        changesHikingEnemyMoveDirection.clear();
    }

}
