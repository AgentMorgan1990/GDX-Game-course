package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.Anim;
import com.mygdx.game.Main;

public class GameScreen implements Screen {

    private final SpriteBatch batch;
    private boolean movementRight = true;
    private boolean turned = false;
    private int positionX = 0;
    private final Anim anim;
    private final Main game;

    public GameScreen(Main game) {
        this.game = game;
        batch = new SpriteBatch();
//        anim = new Anim("1.png", 6, 5, Animation.PlayMode.LOOP);
        anim = new Anim("atlas/skeleton.atlas", Animation.PlayMode.LOOP);
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {

        ScreenUtils.clear(0, 0, 0, 1);
        anim.setTime(Gdx.graphics.getDeltaTime());

        if (!anim.getFrame().isFlipX() && turned) {
            anim.getFrame().flip(true, false);
        }

        if (anim.getFrame().isFlipX() && !turned) {
            anim.getFrame().flip(true, false);
        }

        if (positionX + anim.getFrame().getRegionWidth() >= Gdx.graphics.getWidth()) {
            movementRight = false;
            turned = true;
        }

        if (positionX == 0) {
            movementRight = true;
            turned = false;
        }

        if (movementRight) {
            positionX = positionX + 5;
        } else {
            positionX = positionX - 5;
        }

        batch.begin();
        batch.draw(anim.getFrame(), positionX, 0);
        batch.end();


        if (Gdx.input.isKeyJustPressed(Input.Keys.ANY_KEY)) {
            dispose();
            game.setScreen(new MenuScreen(game));
        }

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        this.batch.dispose();
        this.anim.dispose();
    }
}
