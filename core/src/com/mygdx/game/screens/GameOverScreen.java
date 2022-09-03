package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.Main;

public class GameOverScreen implements Screen {

    private final Main game;
    private final SpriteBatch batch;
    private final Texture img;

    public GameOverScreen(Main game){
        this.game = game;
        batch = new SpriteBatch();
        img = new Texture("game_over.jpg");
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

        ScreenUtils.clear(Color.BLACK);

        float width = Gdx.graphics.getWidth() / 1.5f;
        float height = Gdx.graphics.getHeight() / 1.5f;
        float x = Gdx.graphics.getWidth() / 2f - width / 2f;
        float y = Gdx.graphics.getHeight() / 2f - height / 2f;

        batch.begin();
        batch.draw(img, x, y, width, height);
        batch.end();

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
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
        this.img.dispose();
    }
}
