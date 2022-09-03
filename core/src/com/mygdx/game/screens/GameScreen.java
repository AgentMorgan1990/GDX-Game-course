package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
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
import com.mygdx.game.*;
import com.mygdx.game.enemies.HikingEnemy;
import com.mygdx.game.enemies.Scorpion;
import com.mygdx.game.enemies.Snake;
import com.mygdx.game.services.AnimationService;
import com.mygdx.game.services.ContactProcessingService;

import java.util.ArrayList;


public class GameScreen implements Screen {
    private final SpriteBatch batch;
    private Hero hero;
    private final Main game;
    private final OrthographicCamera camera;
    private final OrthogonalTiledMapRenderer mapRenderer;
    private Physics physx;
    private final int[] bg;
    private final int[] l1;
    private final Texture lifeImage;
    private ArrayList<Bullet> bullets;
    public static boolean abilityToJump = false;
    private ArrayList<HikingEnemy> hikingEnemies;
    private final Music gameMusic;
    public static ArrayList<Body> bodyToDeleting;


    public GameScreen(Main game) {

        //Инициализация физики
        physx = new Physics();

        gameMusic = Gdx.audio.newMusic(Gdx.files.internal("sounds/game_screen.mp3"));
        gameMusic.play();
        gameMusic.setLooping(true);
        gameMusic.setVolume(0.8f);

        //загрузка карты и её слоев
        TiledMap map = new TmxMapLoader().load("map/map.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(map);
        bg = new int[1];
        bg[0] = map.getLayers().getIndex("background");
        l1 = new int[1];
        l1[0] = map.getLayers().getIndex("layer_1");
        this.lifeImage = new Texture("life.png");

        bullets = new ArrayList<>();
        hikingEnemies = new ArrayList<>();
        bodyToDeleting = new ArrayList<>();

        //todo нарисовать экран старта игры с помощью блоков из игры
        //todo нарисовать game over и restart game

        //Инициализация игры и прорисовки
        this.game = game;
        batch = new SpriteBatch();

        //инициализация камеры
        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.zoom = 0.4f;

        //Инициализация героя
        RectangleMapObject tmp = (RectangleMapObject) map.getLayers().get("persons").getObjects().get("hero");
        hero = new Hero(tmp.getRectangle(), physx.addObject(tmp));

        //Инициализация карты
        Array<RectangleMapObject> objects = map.getLayers().get("objects").getObjects().getByType(RectangleMapObject.class);
        for (int i = 0; i < objects.size; i++) {
            physx.addObject(objects.get(i));
        }

        //Инициализация змей/врагов
        Array<RectangleMapObject> enemiesObject = map.getLayers().get("enemies").getObjects().getByType(RectangleMapObject.class);
        for (RectangleMapObject rmo : enemiesObject) {
            HikingEnemy hikingEnemy = null;
            if (rmo.getName().equals("snake")) {
                hikingEnemy = new Snake(physx.addObject(rmo), rmo.getRectangle());
            }
            if (rmo.getName().equals("scorpion")){
                hikingEnemy = new Scorpion(physx.addObject(rmo), rmo.getRectangle());
            }
            hikingEnemies.add(hikingEnemy);
        }
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {

        //Обработка контактов
        ContactProcessingService.executeChangesHikingEnemyMoveDirection(hikingEnemies);
        ContactProcessingService.executeReductionHealthPointContacts(hikingEnemies, hero);
        ContactProcessingService.executeDestroyContacts(hikingEnemies, bullets, hero);


        //управление врагами
        for (HikingEnemy hikingEnemy : hikingEnemies) {
            if (!hikingEnemy.isMovementDirectionRight() && hikingEnemy.getBody().getLinearVelocity().x > -hikingEnemy.getStepFrequency()) {
                hikingEnemy.getBody().applyForceToCenter(new Vector2(-hikingEnemy.getMovementSpeed(), 0), true);
            }

            if (hikingEnemy.isMovementDirectionRight() && hikingEnemy.getBody().getLinearVelocity().x < hikingEnemy.getStepFrequency()) {
                hikingEnemy.getBody().applyForceToCenter(new Vector2(hikingEnemy.getMovementSpeed(), 0), true);
            }
        }

        //Управление персонажем
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) hero.getBody().applyForceToCenter(new Vector2(-100000, 0), true);
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) hero.getBody().applyForceToCenter(new Vector2(100000, 0), true);
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) && abilityToJump) {
            hero.getJumpSound().play(1, 1.0f, 1);
            hero.getBody().applyForceToCenter(new Vector2(0, 10000000), true);
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.CONTROL_LEFT)) {
            createBullet();
            hero.getShotSound().play(1, 1.0f, 1);
        }


        //Управление зумом
        if (Gdx.input.isKeyPressed(Input.Keys.Q)) camera.zoom += 0.01f;
        if (Gdx.input.isKeyPressed(Input.Keys.W) && camera.zoom > 0) camera.zoom -= 0.01f;

        camera.position.x = hero.getBody().getPosition().x;
        camera.position.y = hero.getBody().getPosition().y;
        camera.update();

        ScreenUtils.clear(0, 0, 0, 1);
        mapRenderer.setView(camera);
        mapRenderer.render(bg);
        batch.setProjectionMatrix(camera.combined);


        //Обновление данных объектов по анимации
        hero.updateRectanglePosition();
        AnimationService.updateHeroAnimation(hero);

        for (HikingEnemy hikingEnemy : hikingEnemies) {
            hikingEnemy.updateRectanglePosition();
            AnimationService.updateHikingEnemyAnimation(hikingEnemy);
        }

        for (Bullet bullet : bullets) {
            bullet.updateRectanglePosition();
            bullet.setBulletLifeTime(bullet.getBulletLifeTime() - 1);
            if (bullet.getBulletLifeTime() <= 0) {
                bullet.setAlive(false);
                bullet.getBody().setActive(false);
                ContactProcessingService.destroyContact.add(bullet.getBody());
            }
        }

        batch.begin();

        //отрисовка змей
        for (HikingEnemy hikingEnemy : hikingEnemies) {
            batch.draw(hikingEnemy.getTextureRegion(), hikingEnemy.getRectangle().x, hikingEnemy.getRectangle().y, hikingEnemy.getRectangle().width, hikingEnemy.getRectangle().height);
        }

        //отрисовка пуль
        for (Bullet bullet : bullets) {
            batch.draw(bullet.getTexture(), bullet.getRectangle().x, bullet.getRectangle().y, bullet.getRectangle().width, bullet.getRectangle().height);
        }

        //отрисовка персонажа
        batch.draw(hero.getTextureRegion(), hero.getRectangle().x, hero.getRectangle().y, hero.getRectangle().width, hero.getRectangle().height);

        //отрисовка кол-ва жизней
        for (int i = 0; i < hero.getHealthPoints(); i++) {
            batch.draw(lifeImage, hero.getBody().getPosition().x - (camera.viewportWidth * camera.zoom) / 4 + (i * lifeImage.getWidth()), hero.getBody().getPosition().y + (camera.viewportWidth * camera.zoom) / 5, lifeImage.getWidth(), lifeImage.getHeight());
        }

        batch.end();

        mapRenderer.render(l1);

//        physx.debugDraw(camera);

        physx.step();


        for (Body body : bodyToDeleting) {
            body.setActive(false);
            physx.destroyBody(body);
        }
        bodyToDeleting.clear();


        //управление музыкой
        if (Gdx.input.isKeyJustPressed(Input.Keys.P)) {
            if (gameMusic.isPlaying()){
                gameMusic.pause();
            } else {
                gameMusic.play();
            }

        }
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            dispose();
            game.setScreen(new MenuScreen(game));
        }

        if (!hero.isAlive()) {
            dispose();
            game.setScreen(new GameOverScreen(game));
        }

        if (hikingEnemies.isEmpty()) {
            dispose();
            game.setScreen(new WinScreen(game));
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
        this.hero.dispose();
        this.batch.dispose();
        this.physx.dispose();
        this.gameMusic.dispose();

        for (HikingEnemy hikingEnemy : hikingEnemies) {
            hikingEnemy.dispose();
        }
        for (Bullet bullet : bullets) {
            bullet.dispose();
        }
        this.lifeImage.dispose();
    }
    private void createBullet() {
        Rectangle rectangle = new Rectangle();
        rectangle.setSize(4, 4);
        if (hero.isAnimationDirectionRight()) {
            rectangle.setPosition(hero.getRectangle().x + hero.getRectangle().width / 2, hero.getRectangle().y + hero.getRectangle().height / 3);
        }
        if (!hero.isAnimationDirectionRight()) {
            rectangle.setPosition(hero.getRectangle().x, hero.getRectangle().y + hero.getRectangle().height / 3);
        }
        Body bulletBody = physx.createBullet(rectangle);
        if (hero.isAnimationDirectionRight()) {
            bulletBody.applyForceToCenter(new Vector2(10000000, 0), true);
        }
        if (!hero.isAnimationDirectionRight()) {
            bulletBody.applyForceToCenter(new Vector2(-10000000, 0), true);
        }
        bullets.add(new Bullet(bulletBody, rectangle));
    }
}
