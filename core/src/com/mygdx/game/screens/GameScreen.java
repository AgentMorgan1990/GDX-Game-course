package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.Anim;
import com.mygdx.game.Main;
import com.mygdx.game.Physics;


public class GameScreen implements Screen {
    private final SpriteBatch batch;
//    private boolean movementRight = true;
    private boolean turned = false;
//    private int positionX = 0;
    private final Anim walkAnim;
    private final Anim idleAnim;
    private final Anim jumpAnim;
    private final Anim hitAnim;
    private final Anim attackAnim;
    private final Main game;
    private final Texture bulletImage;
    private final OrthographicCamera camera;
    private final OrthogonalTiledMapRenderer mapRenderer;
    private Physics physx;
    private final int[] bg;
    private final int[] l1;
    private Body body;
    private final Rectangle heroRect;

    public GameScreen(Main game) {

        //инициализация игры и прорисовки
        this.game = game;
        batch = new SpriteBatch();

        //загрузка анимаций героя
        walkAnim = new Anim("atlas/Hero.atlas", Animation.PlayMode.LOOP,"Walk");
        idleAnim = new Anim("atlas/Hero.atlas", Animation.PlayMode.LOOP,"Idle");
        jumpAnim = new Anim("atlas/Hero.atlas", Animation.PlayMode.LOOP,"Jump");
        hitAnim = new Anim("atlas/Hero.atlas", Animation.PlayMode.LOOP,"Hit");
        attackAnim = new Anim("atlas/Hero.atlas", Animation.PlayMode.LOOP,"Attack");

        bulletImage = new Texture("bullet.png");

        //инициализация камеры
        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.zoom = 0.7f;

        //загрузка карты и её слоев
        TiledMap map = new TmxMapLoader().load("map/map.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(map);
        bg = new int[1];
        bg[0] = map.getLayers().getIndex("background");
        l1 = new int[1];
        l1[0] = map.getLayers().getIndex("layer_1");

        //инициализация физики и загрузка героя и объектов
        physx = new Physics();
        RectangleMapObject tmp = (RectangleMapObject) map.getLayers().get("persons").getObjects().get("hero");
        heroRect = tmp.getRectangle();
        body = physx.addObject(tmp);
        Array<RectangleMapObject> objects = map.getLayers().get("objects").getObjects().getByType(RectangleMapObject.class);
        for (int i = 0; i < objects.size; i++) {
            physx.addObject(objects.get(i));
        }

    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {

        //Управление персонажем
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) body.applyForceToCenter (new Vector2(-100000, 0), true);
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) body.applyForceToCenter (new Vector2(100000, 0), true);
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) body.applyForceToCenter (new Vector2(0, 1000000), true);

        //Управление зумом
        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) camera.zoom += 0.01f;
        if (Gdx.input.isButtonPressed(Input.Buttons.RIGHT) && camera.zoom > 0) camera.zoom -= 0.01f;


        camera.position.x = body.getPosition().x;
        camera.position.y = body.getPosition().y;
        camera.update();

        ScreenUtils.clear(0, 0, 0, 1);


        walkAnim.setTime(Gdx.graphics.getDeltaTime());
        idleAnim.setTime(Gdx.graphics.getDeltaTime());
        jumpAnim.setTime(Gdx.graphics.getDeltaTime());
        hitAnim.setTime(Gdx.graphics.getDeltaTime());
        attackAnim.setTime(Gdx.graphics.getDeltaTime());

        mapRenderer.setView(camera);
        mapRenderer.render(bg);

        batch.setProjectionMatrix(camera.combined);
        heroRect.x = body.getPosition().x - heroRect.width / 2;
        heroRect.y = body.getPosition().y - heroRect.height / 2;

        batch.begin();
        if (body.getLinearVelocity().y > 2 || body.getLinearVelocity().y < -2) {
            updateAnimationDirection(jumpAnim, body);
            batch.draw(jumpAnim.getFrame(), heroRect.x, heroRect.y, heroRect.width, heroRect.height);
        } else if (Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT)) {
            updateAnimationDirection(attackAnim, body);
            batch.draw(attackAnim.getFrame(), heroRect.x, heroRect.y, heroRect.width, heroRect.height);
        } else if (body.getLinearVelocity().x != 0) {
            updateAnimationDirection(walkAnim, body);
            batch.draw(walkAnim.getFrame(), heroRect.x, heroRect.y, heroRect.width, heroRect.height);
            //todo определить условия получения урона и прописать отрисовку hitAnim сюда
        } else {
            batch.draw(idleAnim.getFrame(), heroRect.x, heroRect.y, heroRect.width, heroRect.height);
        }
        batch.end();

        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            dispose();
            game.setScreen(new MenuScreen(game));
        }
        mapRenderer.render(l1);

        physx.step();
        physx.debugDraw(camera);

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
        this.walkAnim.dispose();
        this.jumpAnim.dispose();
        this.hitAnim.dispose();
        this.attackAnim.dispose();
        this.bulletImage.dispose();
        this.physx.dispose();
    }

    private void updateAnimationDirection(Anim anim, Body body) {
        if (!anim.getFrame().isFlipX() && body.getLinearVelocity().x < 0) {
            anim.getFrame().flip(true, false);
        }
        if (anim.getFrame().isFlipX() && body.getLinearVelocity().x > 0) {
            anim.getFrame().flip(true, false);
        }
    }
}
