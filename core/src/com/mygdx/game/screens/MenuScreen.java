package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
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
    private final ShapeRenderer shapeRenderer;
    private final Music menuMusic;
    private  Rectangle startRect;
    private float imgWidth;
    private float imgHeight;
    private float xForPicture;
    private float yForPicture;

    public MenuScreen(Main game) {
        this.game = game;
        batch = new SpriteBatch();

        menuMusic = Gdx.audio.newMusic(Gdx.files.internal("sounds/start_screen.mp3"));
        menuMusic.play();
        menuMusic.setLooping(true);
        menuMusic.setVolume(0.5f);

        img = new Texture("start_game.png");

        startRect = new Rectangle(xForPicture,yForPicture,imgWidth,imgHeight);
        shapeRenderer = new ShapeRenderer();
    }


    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

        ScreenUtils.clear(Color.LIGHT_GRAY);

        batch.begin();
        batch.draw(img, xForPicture, yForPicture, imgWidth, imgHeight);
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
        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            dispose();
            game.setScreen(new GameScreen(game));
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            dispose();
            Gdx.app.exit();
        }

    }

    @Override
    public void resize(int width, int height) {
        imgWidth = Gdx.graphics.getWidth() / 3f;
        imgHeight = Gdx.graphics.getHeight() / 3f;
        xForPicture = Gdx.graphics.getWidth() / 2f -  imgWidth / 2f;
        yForPicture = Gdx.graphics.getHeight() / 2f - imgHeight / 2f;

        startRect.x = xForPicture;
        startRect.y = yForPicture;
        startRect.width = imgWidth;
        startRect.height = imgHeight;
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
        this.menuMusic.dispose();
    }
}
