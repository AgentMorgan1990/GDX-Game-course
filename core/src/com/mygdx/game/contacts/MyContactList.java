package com.mygdx.game.contacts;

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
                GameScreen.destroyContact.add(a.getBody());
                GameScreen.reductionHealthPointContacts.add(b.getBody());
            }
            if (tmpB.equals("bullet") && tmpA.equals("snake")) {
                GameScreen.destroyContact.add(b.getBody());
                GameScreen.reductionHealthPointContacts.add(a.getBody());
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
                GameScreen.destroyContact.add(a.getBody());
            }
            if (tmpB.equals("bullet") && tmpA.equals("wall")) {
                GameScreen.destroyContact.add(b.getBody());
            }
        }
        if (a.getUserData() != null && b.getUserData() != null) {
            String tmpA = (String) a.getUserData();
            String tmpB = (String) b.getUserData();
            if (tmpA.equals("bullet") && tmpB.equals("floor")) {
                GameScreen.destroyContact.add(a.getBody());
            }
            if (tmpB.equals("bullet") && tmpA.equals("floor")) {
                GameScreen.destroyContact.add(b.getBody());
            }
        }
        if (a.getUserData() != null && b.getUserData() != null) {
            String tmpA = (String) a.getUserData();
            String tmpB = (String) b.getUserData();
            if (tmpA.equals("hero") && tmpB.equals("snake")) {
                GameScreen.reductionHealthPointContacts.add(a.getBody());
            }
            if (tmpB.equals("hero") && tmpA.equals("snake")) {
                GameScreen.reductionHealthPointContacts.add(b.getBody());
            }
        }
        if (a.getUserData() != null && b.getUserData() != null) {
            String tmpA = (String) a.getUserData();
            String tmpB = (String) b.getUserData();
            if (tmpA.equals("bottom") && tmpB.equals("snake")) {
                GameScreen.destroyContact.add(b.getBody());
            }
            if (tmpB.equals("bottom") && tmpA.equals("snake")) {
                GameScreen.destroyContact.add(a.getBody());
            }
        }
        if (a.getUserData() != null && b.getUserData() != null) {
            String tmpA = (String) a.getUserData();
            String tmpB = (String) b.getUserData();
            if (tmpA.equals("bottom") && tmpB.equals("hero")) {
                GameScreen.destroyContact.add(b.getBody());
            }
            if (tmpB.equals("bottom") && tmpA.equals("hero")) {
                GameScreen.destroyContact.add(a.getBody());
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
                GameScreen.changesHikingEnemyMoveDirection.add(b.getBody());
            }
            if (tmpB.equals("floor") && tmpA.equals("leftSensor")) {
                GameScreen.changesHikingEnemyMoveDirection.add(a.getBody());
            }
        }
        if (a.getUserData() != null && b.getUserData() != null) {
            String tmpA = (String) a.getUserData();
            String tmpB = (String) b.getUserData();
            if (tmpA.equals("floor") && tmpB.equals("rightSensor")) {
                GameScreen.changesHikingEnemyMoveDirection.add(b.getBody());
            }
            if (tmpB.equals("floor") && tmpA.equals("rightSensor")) {
                GameScreen.changesHikingEnemyMoveDirection.add(a.getBody());
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
