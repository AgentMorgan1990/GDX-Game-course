package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.Anim;
import com.mygdx.game.Main;

public class GameScreen implements Screen {

    private final SpriteBatch batch;
    private boolean movementRight = true;
    private boolean turned = false;
    private int positionX = 0;
//    private final Anim anim;
    private final Main game;
    private final OrthographicCamera camera;
    private final OrthogonalTiledMapRenderer mapRenderer;


    public GameScreen(Main game) {
        this.game = game;
        batch = new SpriteBatch();
//        anim = new Anim("1.png", 6, 5, Animation.PlayMode.LOOP);
//        anim = new Anim("atlas/skeleton.atlas", Animation.PlayMode.LOOP);
        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.zoom = 0.7f;
        TiledMap map = new TmxMapLoader().load("map/карта.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(map);

        RectangleMapObject tmp = (RectangleMapObject) map.getLayers().get("objects").getObjects().get("cameraStartLocation");
        camera.position.x = tmp.getRectangle().x;
        camera.position.y = tmp.getRectangle().y;

    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {

        float STEP = 3;
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) camera.position.x -= STEP;
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) camera.position.x += STEP;
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) camera.position.y += STEP;
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) camera.position.y -= STEP;

        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) camera.zoom += 0.01f;
        if (Gdx.input.isButtonPressed(Input.Buttons.RIGHT) && camera.zoom > 0) camera.zoom -= 0.01f;

        camera.update();

        ScreenUtils.clear(0, 0, 0, 1);

//        anim.setTime(Gdx.graphics.getDeltaTime());

//        if (!anim.getFrame().isFlipX() && turned) {
//            anim.getFrame().flip(true, false);
//        }
//
//        if (anim.getFrame().isFlipX() && !turned) {
//            anim.getFrame().flip(true, false);
//        }
//
//        if (positionX + anim.getFrame().getRegionWidth() >= Gdx.graphics.getWidth()) {
//            movementRight = false;
//            turned = true;
//        }
//
//        if (positionX == 0) {
//            movementRight = true;
//            turned = false;
//        }
//
//        if (movementRight) {
//            positionX = positionX + 5;
//        } else {
//            positionX = positionX - 5;
//        }

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
//        batch.draw(anim.getFrame(), positionX, 0);
        batch.end();


        mapRenderer.setView(camera);
        mapRenderer.render();

        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            dispose();
            game.setScreen(new MenuScreen(game));
        }

    }

    @Override
    public void resize(int width, int height) {
        camera.viewportWidth = width;
        camera.viewportHeight = height;
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
//        this.anim.dispose();
    }
}
