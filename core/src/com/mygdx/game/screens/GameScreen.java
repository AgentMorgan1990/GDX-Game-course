package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;


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
    public static ArrayList<Body> bodies;
    public static Set<Body> changesHikingEnemyMoveDirection;
    public static ArrayList<Body> reductionHealthPointContacts;
    public static ArrayList<Body> destroyContact;
    public static boolean abilityToJump = false;
    private ArrayList<Snake> snakes;

    public GameScreen(Main game) {

        //Инициализация физики
        physx = new Physics();

        //загрузка карты и её слоев
        TiledMap map = new TmxMapLoader().load("map/map.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(map);
        bg = new int[1];
        bg[0] = map.getLayers().getIndex("background");
        l1 = new int[1];
        l1[0] = map.getLayers().getIndex("layer_1");
        this.lifeImage = new Texture("life.png");

        bodies = new ArrayList<>();
        bullets = new ArrayList<>();
        snakes = new ArrayList<>();
        changesHikingEnemyMoveDirection = new HashSet<>();
        reductionHealthPointContacts = new ArrayList<>();
        destroyContact = new ArrayList<>();

        //todo нарисовать экран старта игры с помощью блоков из игры
        //todo нарисовать game over и restart game
        //todo порефачить код

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
        Array<RectangleMapObject> snakesObject = map.getLayers().get("enemies").getObjects().getByType(RectangleMapObject.class);
        for (RectangleMapObject rmo : snakesObject) {
            Snake snake = new Snake(physx.addObject(rmo), rmo.getRectangle());
            snakes.add(snake);
        }
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {


        executeChangesHikingEnemyMoveDirection();
        executeReductionHealthPointContacts();
        executeDestroyContacts();


        //управление врагами
        for (Snake snake : snakes) {
            if (!snake.isMovementDirectionRight() && snake.getBody().getLinearVelocity().x > -20) {
                snake.getBody().applyForceToCenter(new Vector2(-20000, 0), true);
            }

            if (snake.isMovementDirectionRight() && snake.getBody().getLinearVelocity().x < 20) {
                snake.getBody().applyForceToCenter(new Vector2(20000, 0), true);
            }
        }

        //Управление персонажем
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) hero.getBody().applyForceToCenter(new Vector2(-100000, 0), true);
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) hero.getBody().applyForceToCenter(new Vector2(100000, 0), true);
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) && abilityToJump) {
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


        hero.setTimeToAnimation(Gdx.graphics.getDeltaTime());
        hero.updateRectanglePosition();
        hero.setAnimationDirectionRight(defineAnimationDirectionRight(hero.getBody(), hero.isAnimationDirectionRight()));
        updateAnimationDirectionForRightPicture(hero.getHitAnim(), hero.isAnimationDirectionRight());
        updateAnimationDirectionForRightPicture(hero.getJumpAnim(), hero.isAnimationDirectionRight());
        updateAnimationDirectionForRightPicture(hero.getAttackAnim(), hero.isAnimationDirectionRight());
        updateAnimationDirectionForRightPicture(hero.getWalkAnim(), hero.isAnimationDirectionRight());
        updateAnimationDirectionForRightPicture(hero.getIdleAnim(), hero.isAnimationDirectionRight());

        if (hero.isDisplayHitAnimation()) {
            hero.setDurationOfHitAnimation(hero.getDurationOfHitAnimation() - 1);
        }
        if (hero.getDurationOfHitAnimation() <= 0) {
            hero.setDurationOfHitAnimation(120);
            hero.setDisplayHitAnimation(false);
        }


        for (Snake snake : snakes) {
            snake.updateRectanglePosition();
            snake.setAnimationDirectionRight(defineAnimationDirectionRight(snake.getBody(), snake.isAnimationDirectionRight()));
            snake.setTimeToAnimation(Gdx.graphics.getDeltaTime());
            updateAnimationDirectionForLeftPicture(snake.getMoveAnimation(), snake.isAnimationDirectionRight());
            updateAnimationDirectionForLeftPicture(snake.getHitAnimation(), snake.isAnimationDirectionRight());


            if (snake.isDisplayHitAnimation()) {
                snake.setDurationOfHitAnimation(hero.getDurationOfHitAnimation() - 1);
            }
            if (snake.getDurationOfHitAnimation() <= 0) {
                snake.setDurationOfHitAnimation(120);
                snake.setDisplayHitAnimation(false);
            }
        }

        for (Bullet bullet : bullets) {
            bullet.updateRectanglePosition();
            bullet.setBulletLifeTime(bullet.getBulletLifeTime() - 1);
            if (bullet.getBulletLifeTime() <= 0) {
                bullet.setAlive(false);
                bullet.getBody().setActive(false);
                destroyContact.add(bullet.getBody());
            }
        }

        batch.begin();

        //отрисовка змей
        for (Snake snake : snakes) {
            if (snake.isAlive()) {
                if (snake.isDisplayHitAnimation() && snake.isDisplayHitAnimation()) {
                    batch.draw(snake.getHitAnimation().getFrame(), snake.getRectangle().x, snake.getRectangle().y, snake.getRectangle().width, snake.getRectangle().height);
                } else {
                    batch.draw(snake.getMoveAnimation().getFrame(), snake.getRectangle().x, snake.getRectangle().y, snake.getRectangle().width, snake.getRectangle().height);
                }
            }
        }

        //отрисовка пуль
        for (Bullet bullet : bullets) {
                batch.draw(bullet.getTexture(), bullet.getRectangle().x, bullet.getRectangle().y,
                        bullet.getRectangle().width, bullet.getRectangle().height);
        }

        //отрисовка персонажа
        if (hero.isDisplayHitAnimation() && hero.isDisplayHitAnimation()) {
            batch.draw(hero.getHitAnim().getFrame(), hero.getRectangle().x, hero.getRectangle().y, hero.getRectangle().width, hero.getRectangle().height);
        } else if (hero.getBody().getLinearVelocity().y > 60) {
            batch.draw(hero.getJumpAnim().getFrame(), hero.getRectangle().x, hero.getRectangle().y, hero.getRectangle().width, hero.getRectangle().height);
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.CONTROL_LEFT)) {
            batch.draw(hero.getAttackAnim().getFrame(), hero.getRectangle().x, hero.getRectangle().y, hero.getRectangle().width, hero.getRectangle().height);
        } else if (hero.getBody().getLinearVelocity().x <= -10 || hero.getBody().getLinearVelocity().x > 10) {
            batch.draw(hero.getWalkAnim().getFrame(), hero.getRectangle().x, hero.getRectangle().y, hero.getRectangle().width, hero.getRectangle().height);
        } else {
            batch.draw(hero.getIdleAnim().getFrame(), hero.getRectangle().x, hero.getRectangle().y, hero.getRectangle().width, hero.getRectangle().height);
        }

        //отрисовка кол-ва жизней
        for (int i = 0; i < hero.getHitPoint(); i++) {
            batch.draw(lifeImage, hero.getBody().getPosition().x - (camera.viewportWidth * camera.zoom) / 4 + (i * lifeImage.getWidth()), hero.getBody().getPosition().y + (camera.viewportWidth * camera.zoom) / 5, lifeImage.getWidth(), lifeImage.getHeight());
        }

        batch.end();

        mapRenderer.render(l1);

        physx.step();

        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            dispose();
            game.setScreen(new MenuScreen(game));
        }

        if (!hero.isAlive()) {
            dispose();
            game.setScreen(new GameOverScreen(game));
        }
        //todo дописать Screen победы
        if (snakes.isEmpty()) {
            dispose();
            game.setScreen(new MenuScreen(game));
        }
//        physx.debugDraw(camera);

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

        for (Snake snake : snakes) {
            snake.dispose();
        }
        for (Bullet bullet : bullets) {
            bullet.dispose();
        }
        this.lifeImage.dispose();
    }

    private void updateAnimationDirectionForRightPicture(Anim anim, boolean animationDirectionRight) {
        if (!anim.getFrame().isFlipX() && !animationDirectionRight) {
            anim.getFrame().flip(true, false);
        }
        if (anim.getFrame().isFlipX() && animationDirectionRight) {
            anim.getFrame().flip(true, false);
        }
    }

    private void updateAnimationDirectionForLeftPicture(Anim anim, boolean animationDirectionRight) {
        if (anim.getFrame().isFlipX() && !animationDirectionRight) {
            anim.getFrame().flip(true, false);
        }
        if (!anim.getFrame().isFlipX() && animationDirectionRight) {
            anim.getFrame().flip(true, false);
        }
    }

    private boolean defineAnimationDirectionRight(Body body, boolean animationDirection) {
        boolean animationDirectionRight = animationDirection;
        if (body.getLinearVelocity().x <= -10) {
            animationDirectionRight = false;
        }
        if (body.getLinearVelocity().x > 10) {
            animationDirectionRight = true;
        }
        return animationDirectionRight;
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

    private void executeDestroyContacts() {
        for (Body body : destroyContact) {

            Iterator<Snake> snakeIterator = snakes.iterator();
            while (snakeIterator.hasNext()) {
                Snake snake = snakeIterator.next();
                if (snake.getBody().equals(body)) {
                    body.setActive(false);
                    snake.setAlive(false);
                    snakeIterator.remove();
                }
            }

            Iterator<Bullet> bulletIterator = bullets.iterator();
            while (bulletIterator.hasNext()) {
                Bullet bullet = bulletIterator.next();
                if (bullet.getBody().equals(body)) {
                    bullet.setAlive(false);
                    body.setActive(false);
                    bulletIterator.remove();
                }
            }
            if (hero.getBody().equals(body)) {
                hero.setAlive(false);
            }

        }
        destroyContact.clear();
    }

    private void executeReductionHealthPointContacts() {

        for (Body body : reductionHealthPointContacts) {
            for (Snake snake : snakes) {
                if (snake.getBody().equals(body)) {
                    snake.setDisplayHitAnimation(true);
                    snake.setHitPoints(snake.getHitPoints() - 1);
                    if (snake.getHitPoints() <= 0) {
                        destroyContact.add(body);
                    }
                }
            }
            if (hero.getBody().equals(body)) {
                if (hero.isAnimationDirectionRight()){
                    hero.getBody().applyForceToCenter(new Vector2(-1000000, 1000000), true);
                } else {
                    hero.getBody().applyForceToCenter(new Vector2(1000000, 1000000), true);
                }

                hero.setDisplayHitAnimation(true);
                hero.setHitPoint(hero.getHitPoint() - 1);
                if (hero.getHitPoint() <= 0) {
                    hero.setAlive(false);
                }
            }
        }
        reductionHealthPointContacts.clear();
    }

    private void executeChangesHikingEnemyMoveDirection() {
        for (Body snakeAction : changesHikingEnemyMoveDirection) {
            for (Snake snake : snakes) {
                if (snakeAction.equals(snake.getBody())) {
                    if (snake.isMovementDirectionRight()) {
                        snake.setMovementDirectionRight(false);
                    } else {
                        snake.setMovementDirectionRight(true);
                    }
                }
            }
        }
        changesHikingEnemyMoveDirection.clear();
    }
}
