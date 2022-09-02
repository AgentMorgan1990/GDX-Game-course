package com.mygdx.game.contacts;

import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.game.services.ContactProcessingService;
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
                ContactProcessingService.destroyContact.add(a.getBody());
                ContactProcessingService.reductionHealthPointContacts.add(b.getBody());
            }
            if (tmpB.equals("bullet") && tmpA.equals("snake")) {
                ContactProcessingService.destroyContact.add(b.getBody());
                ContactProcessingService.reductionHealthPointContacts.add(a.getBody());
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
                ContactProcessingService.destroyContact.add(a.getBody());
            }
            if (tmpB.equals("bullet") && tmpA.equals("wall")) {
                ContactProcessingService.destroyContact.add(b.getBody());
            }
        }
        if (a.getUserData() != null && b.getUserData() != null) {
            String tmpA = (String) a.getUserData();
            String tmpB = (String) b.getUserData();
            if (tmpA.equals("bullet") && tmpB.equals("floor")) {
                ContactProcessingService.destroyContact.add(a.getBody());
            }
            if (tmpB.equals("bullet") && tmpA.equals("floor")) {
                ContactProcessingService.destroyContact.add(b.getBody());
            }
        }
        if (a.getUserData() != null && b.getUserData() != null) {
            String tmpA = (String) a.getUserData();
            String tmpB = (String) b.getUserData();
            if (tmpA.equals("hero") && tmpB.equals("snake")) {
                ContactProcessingService.reductionHealthPointContacts.add(a.getBody());
            }
            if (tmpB.equals("hero") && tmpA.equals("snake")) {
                ContactProcessingService.reductionHealthPointContacts.add(b.getBody());
            }
        }
        if (a.getUserData() != null && b.getUserData() != null) {
            String tmpA = (String) a.getUserData();
            String tmpB = (String) b.getUserData();
            if (tmpA.equals("bottom") && tmpB.equals("snake")) {
                ContactProcessingService.destroyContact.add(b.getBody());
            }
            if (tmpB.equals("bottom") && tmpA.equals("snake")) {
                ContactProcessingService.destroyContact.add(a.getBody());
            }
        }
        if (a.getUserData() != null && b.getUserData() != null) {
            String tmpA = (String) a.getUserData();
            String tmpB = (String) b.getUserData();
            if (tmpA.equals("bottom") && tmpB.equals("hero")) {
                ContactProcessingService.destroyContact.add(b.getBody());
            }
            if (tmpB.equals("bottom") && tmpA.equals("hero")) {
                ContactProcessingService.destroyContact.add(a.getBody());
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
                ContactProcessingService.changesHikingEnemyMoveDirection.add(b.getBody());
            }
            if (tmpB.equals("floor") && tmpA.equals("leftSensor")) {
                ContactProcessingService.changesHikingEnemyMoveDirection.add(a.getBody());
            }
        }
        if (a.getUserData() != null && b.getUserData() != null) {
            String tmpA = (String) a.getUserData();
            String tmpB = (String) b.getUserData();
            if (tmpA.equals("floor") && tmpB.equals("rightSensor")) {
                ContactProcessingService.changesHikingEnemyMoveDirection.add(b.getBody());
            }
            if (tmpB.equals("floor") && tmpA.equals("rightSensor")) {
                ContactProcessingService.changesHikingEnemyMoveDirection.add(a.getBody());
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
