package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.Main;

public class MenuScreen implements Screen {
    private final Main game;
    private final SpriteBatch batch;
    private final Texture img;
    private final Rectangle startRect;
    private final ShapeRenderer shapeRenderer;

    public MenuScreen(Main game) {
        this.game = game;
        batch = new SpriteBatch();

        Pixmap fullSize = new Pixmap(Gdx.files.internal("start_game.png"));
        Pixmap smallSize = new Pixmap(300, 200, fullSize.getFormat());
        smallSize.drawPixmap(fullSize,
                0, 0, fullSize.getWidth(), fullSize.getHeight(),
                0, 0, smallSize.getWidth(), smallSize.getHeight()
        );

        img = new Texture(smallSize);
        startRect = new Rectangle(
                Gdx.graphics.getWidth()/2f-img.getWidth()/2f,
                Gdx.graphics.getHeight()/2f-img.getHeight()/2f,
                img.getWidth(),
                img.getHeight());
        shapeRenderer = new ShapeRenderer();

    }


    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

        ScreenUtils.clear(Color.LIGHT_GRAY);

        batch.begin();
        batch.draw(img,
                Gdx.graphics.getWidth()/2f-img.getWidth()/2f,
                Gdx.graphics.getHeight()/2f-img.getHeight()/2f);
        batch.end();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.WHITE);
        shapeRenderer.rect(startRect.x, startRect.y, startRect.width, startRect.height);
        shapeRenderer.end();

        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            int x = Gdx.input.getX();
            int y = Gdx.graphics.getHeight() - Gdx.input.getY();
            if (startRect.contains(x, y)) {
                dispose();
                game.setScreen(new GameScreen(game));
            }
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
        this.shapeRenderer.dispose();
    }
}
