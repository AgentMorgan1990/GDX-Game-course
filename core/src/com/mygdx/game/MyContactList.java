package com.mygdx.game;

import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.game.screens.GameScreen;

public class MyContactList implements ContactListener {
    @Override
    public void beginContact(Contact contact) {
        Fixture a = contact.getFixtureA();
        Fixture b = contact.getFixtureB();

        if (a.getUserData() != null && b.getUserData() != null) {
            String tmpA = (String) a.getUserData();
            String tmpB = (String) b.getUserData();
            if (tmpA.equals("bullet") && tmpB.equals("snake")) {
                GameScreen.bodies.add(b.getBody());
                GameScreen.bodies.add(a.getBody());
            }
            if (tmpB.equals("bullet") && tmpA.equals("snake")) {
                GameScreen.bodies.add(a.getBody());
                GameScreen.bodies.add(b.getBody());
            }
        }

        if (a.getUserData() != null && b.getUserData() != null) {
            String tmpA = (String) a.getUserData();
            String tmpB = (String) b.getUserData();
            if (tmpA.equals("hero") && tmpB.equals("floor") && !GameScreen.abilityToJump) {
                GameScreen.abilityToJump = true;
            }
            if (tmpB.equals("hero") && tmpA.equals("floor") && !GameScreen.abilityToJump) {
                GameScreen.abilityToJump = true;
            }
        }
        if (a.getUserData() != null && b.getUserData() != null) {
            String tmpA = (String) a.getUserData();
            String tmpB = (String) b.getUserData();
            if (tmpA.equals("bullet") && tmpB.equals("wall")) {
                GameScreen.bodies.add(a.getBody());
            }
            if (tmpB.equals("bullet") && tmpA.equals("wall")) {
                GameScreen.bodies.add(b.getBody());
            }
        }
        if (a.getUserData() != null && b.getUserData() != null) {
            String tmpA = (String) a.getUserData();
            String tmpB = (String) b.getUserData();
            if (tmpA.equals("bullet") && tmpB.equals("floor")) {
                GameScreen.bodies.add(a.getBody());
            }
            if (tmpB.equals("bullet") && tmpA.equals("floor")) {
                GameScreen.bodies.add(b.getBody());
            }
        }
        if (a.getUserData() != null && b.getUserData() != null) {
            String tmpA = (String) a.getUserData();
            String tmpB = (String) b.getUserData();
            if (tmpA.equals("hero") && tmpB.equals("snake")) {
                GameScreen.bodies.add(a.getBody());
            }
            if (tmpB.equals("hero") && tmpA.equals("snake")) {
                GameScreen.bodies.add(b.getBody());
            }
        }
    }

    @Override
    public void endContact(Contact contact) {
        Fixture a = contact.getFixtureA();
        Fixture b = contact.getFixtureB();


        if (a.getUserData() != null && b.getUserData() != null) {
            String tmpA = (String) a.getUserData();
            String tmpB = (String) b.getUserData();
            if (tmpA.equals("hero") && tmpB.equals("floor")&&GameScreen.abilityToJump ) {
                GameScreen.abilityToJump = false;
            }
            if (tmpB.equals("hero") && tmpA.equals("floor")&&GameScreen.abilityToJump) {
                GameScreen.abilityToJump = false;
            }
        }

        if (a.getUserData() != null && b.getUserData() != null) {
            String tmpA = (String) a.getUserData();
            String tmpB = (String) b.getUserData();
            if (tmpA.equals("floor") && tmpB.equals("leftSensor")) {
                System.out.println(tmpB);
                GameScreen.changesSnakeMoveDirection.add(b.getBody());
            }
            if (tmpB.equals("floor") && tmpA.equals("leftSensor")) {
                System.out.println(tmpA);
                GameScreen.changesSnakeMoveDirection.add(a.getBody());
            }
        }
        if (a.getUserData() != null && b.getUserData() != null) {
            String tmpA = (String) a.getUserData();
            String tmpB = (String) b.getUserData();
            if (tmpA.equals("floor") && tmpB.equals("rightSensor")) {
                System.out.println(tmpB);
                GameScreen.changesSnakeMoveDirection.add(b.getBody());
            }
            if (tmpB.equals("floor") && tmpA.equals("rightSensor")) {
                System.out.println(tmpA);
                GameScreen.changesSnakeMoveDirection.add(a.getBody());
            }
        }
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
    }
}
