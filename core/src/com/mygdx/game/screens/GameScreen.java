package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
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
    private Snake snake;
    private final Main game;
    private final OrthographicCamera camera;
    private final OrthogonalTiledMapRenderer mapRenderer;
    private Physics physx;
    private final int[] bg;
    private final int[] l1;
    private ArrayList<Bullet> bullets;
    public static ArrayList<Body> bodies;
    public static Set<Body> changesSnakeMoveDirection;
    public static boolean abilityToJump = false;

    public GameScreen(Main game) {


        //инициализация физики и загрузка героя и объектов
        physx = new Physics();

        //загрузка карты и её слоев
        TiledMap map = new TmxMapLoader().load("map/map.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(map);
        bg = new int[1];
        bg[0] = map.getLayers().getIndex("background");
        l1 = new int[1];
        l1[0] = map.getLayers().getIndex("layer_1");

        bodies = new ArrayList<>();
        bullets = new ArrayList<>();
        changesSnakeMoveDirection = new HashSet<>();
        //todo при касании дна персонаж умирает
        //todo нарисовать экран старта игры с помощью блоков из игры
        //todo нарисовать game over и restart game
        //todo упростить добавление змей/врагов

        //Инициализация игры и прорисовки
        this.game = game;
        batch = new SpriteBatch();

        //инициализация камеры
        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.zoom = 0.4f;

        //Инициализация героя
        RectangleMapObject tmp = (RectangleMapObject) map.getLayers().get("persons").getObjects().get("hero");
        hero = new Hero(tmp.getRectangle(), physx.addObject(tmp));

        //Инициализация змеи
        RectangleMapObject tmp1 = (RectangleMapObject) map.getLayers().get("persons").getObjects().get("snake");
        snake = new Snake(physx.addObject(tmp1),
                tmp1.getRectangle()
        );

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

        //todo вынести управление врагами в отдельный класс
        for (Body snakeAction : changesSnakeMoveDirection) {
            if (snake.isMovementDirectionRight()) {
                snake.setMovementDirectionRight(false);
            } else {
                snake.setMovementDirectionRight(true);
            }
        }
        changesSnakeMoveDirection.clear();

        if (!snake.isMovementDirectionRight() && snake.getBody().getLinearVelocity().x > -20) {
            snake.getBody().applyForceToCenter(new Vector2(-20000, 0), true);
        }

        if (snake.isMovementDirectionRight() && snake.getBody().getLinearVelocity().x < 20) {
            snake.getBody().applyForceToCenter(new Vector2(20000, 0), true);
        }

        //Управление персонажем
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) hero.getBody().applyForceToCenter(new Vector2(-100000, 0), true);
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) hero.getBody().applyForceToCenter(new Vector2(100000, 0), true);
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) && abilityToJump) {
            hero.getBody().applyForceToCenter(new Vector2(0, 10000000), true);
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.CONTROL_LEFT)) {
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
            hero.getShotSound().play(1, 1.0f, 1);
            bullets.add(new Bullet(bulletBody, rectangle));
        }


        //Управление зумом
        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) camera.zoom += 0.01f;
        if (Gdx.input.isButtonPressed(Input.Buttons.RIGHT) && camera.zoom > 0) camera.zoom -= 0.01f;

        camera.position.x = hero.getBody().getPosition().x;
        camera.position.y = hero.getBody().getPosition().y;
        camera.update();

        ScreenUtils.clear(0, 0, 0, 1);

        hero.setTimeToAnimation(Gdx.graphics.getDeltaTime());

        mapRenderer.setView(camera);
        mapRenderer.render(bg);

        batch.setProjectionMatrix(camera.combined);

        hero.updateRectanglePosition();

        hero.setAnimationDirectionRight(defineAnimationDirectionRight(hero.getBody(), hero.isAnimationDirectionRight()));
        snake.setAnimationDirectionRight(defineAnimationDirectionRight(snake.getBody(), snake.isAnimationDirectionRight()));
        batch.begin();


        if (snake.isAlive()) {
            snake.updateRectanglePosition();
            snake.setTimeToAnimation(Gdx.graphics.getDeltaTime());
            updateAnimationDirectionForLeftPicture(snake.getMoveAnimation(), snake.isAnimationDirectionRight());
            updateAnimationDirectionForLeftPicture(snake.getHitAnimation(), snake.isAnimationDirectionRight());
            if (snake.getDurationOfHitAnimation() <= 0) {
                snake.setDurationOfHitAnimation(120);
                snake.setDisplayHitAnimation(false);
            }

            if (snake.isDisplayHitAnimation() && snake.getDurationOfHitAnimation() >= 0) {
                batch.draw(snake.getHitAnimation().getFrame(), snake.getRectangle().x, snake.getRectangle().y, snake.getRectangle().width, snake.getRectangle().height);
                snake.setDurationOfHitAnimation(snake.getDurationOfHitAnimation() - 1);
            } else {
                batch.draw(snake.getMoveAnimation().getFrame(), snake.getRectangle().x, snake.getRectangle().y, snake.getRectangle().width, snake.getRectangle().height);
            }
        }

        if (!bullets.isEmpty()) {
            for (Bullet bullet : bullets) {
                if (bullet.isAlive()) {
                    bullet.setBulletLifeTime(bullet.getBulletLifeTime() - 1);
                    if (bullet.getBulletLifeTime() <= 0) {
                        bullet.setAlive(false);
                        bullet.getBody().setActive(false);
                        bodies.add(bullet.getBody());
                    }
                    bullet.updateRectanglePosition();
                    batch.draw(bullet.getTexture(), bullet.getRectangle().x, bullet.getRectangle().y,
                            bullet.getRectangle().width, bullet.getRectangle().height);
                }
            }
        }

        if (hero.getDurationOfHitAnimation() <= 0) {
            hero.setDurationOfHitAnimation(120);
            hero.setDisplayHitAnimation(false);
        }
        if (hero.isDisplayHitAnimation() && hero.getDurationOfHitAnimation() >= 0) {
            updateAnimationDirectionForRightPicture(hero.getHitAnim(), hero.isAnimationDirectionRight());
            batch.draw(hero.getHitAnim().getFrame(), hero.getRectangle().x, hero.getRectangle().y, hero.getRectangle().width, hero.getRectangle().height);
            hero.setDurationOfHitAnimation(hero.getDurationOfHitAnimation() - 1);
        } else if (hero.getBody().getLinearVelocity().y > 60) {
            updateAnimationDirectionForRightPicture(hero.getJumpAnim(), hero.isAnimationDirectionRight());
            batch.draw(hero.getJumpAnim().getFrame(), hero.getRectangle().x, hero.getRectangle().y, hero.getRectangle().width, hero.getRectangle().height);
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.CONTROL_LEFT)) {
            updateAnimationDirectionForRightPicture(hero.getAttackAnim(), hero.isAnimationDirectionRight());
            batch.draw(hero.getAttackAnim().getFrame(), hero.getRectangle().x, hero.getRectangle().y, hero.getRectangle().width, hero.getRectangle().height);
        } else if (hero.getBody().getLinearVelocity().x <= -10 || hero.getBody().getLinearVelocity().x > 10) {
            updateAnimationDirectionForRightPicture(hero.getWalkAnim(), hero.isAnimationDirectionRight());
            batch.draw(hero.getWalkAnim().getFrame(), hero.getRectangle().x, hero.getRectangle().y, hero.getRectangle().width, hero.getRectangle().height);
        } else {
            updateAnimationDirectionForRightPicture(hero.getIdleAnim(), hero.isAnimationDirectionRight());
            batch.draw(hero.getIdleAnim().getFrame(), hero.getRectangle().x, hero.getRectangle().y, hero.getRectangle().width, hero.getRectangle().height);
        }
        batch.end();

        mapRenderer.render(l1);

        physx.step();


        Iterator<Body> iterator = bodies.iterator();
        while (iterator.hasNext()) {
            Body body = iterator.next();
            Iterator<Bullet> bullet_iterator = bullets.iterator();
            while (bullet_iterator.hasNext()) {
                Bullet bullet = bullet_iterator.next();
                if (bullet.getBody().equals(body)) {
                    bullet.setAlive(false);
                    body.setActive(false);
                    physx.destroyBody(body);
                    bullet_iterator.remove();
                }
            }

            if (hero.getBody().equals(body)) {
                hero.setDisplayHitAnimation(true);
                hero.setHitPoint(hero.getHitPoint() - 1);
                if (hero.getHitPoint() <= 0) {
                    hero.setAlive(false);
                }
            }

            if (snake.getBody().equals(body)) {
                snake.setDisplayHitAnimation(true);
                snake.setHitPoints(snake.getHitPoints() - 1);
                if (snake.getHitPoints() <= 0) {
                    snake.setAlive(false);
                    body.setActive(false);
                    physx.destroyBody(body);
                }
            }
            iterator.remove();
        }

        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            dispose();
            game.setScreen(new MenuScreen(game));
        }

        if (!hero.isAlive()) {
            dispose();
            game.setScreen(new GameOverScreen(game));
        }
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
        this.hero.dispose();
        this.batch.dispose();
        this.physx.dispose();
        this.snake.dispose();

        for (Bullet bullet : bullets) {
            bullet.dispose();
        }

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
}
