package com.mygdx.game;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

public class Physics {
    private final World world;
    private final Box2DDebugRenderer debugRenderer;
    public Physics() {
        world = new World(new Vector2(0.0f, -9.8f), true);
        world.setContactListener(new MyContactList());
        debugRenderer = new Box2DDebugRenderer();
    }

    public void destroyBody(Body body) {
        //todo если удалять тела, то приложение падает
//        world.destroyBody(body);
    }
    public Body createBullet(Rectangle rectangle){
        BodyDef def = new BodyDef();
        FixtureDef fdef = new FixtureDef();
        PolygonShape polygonShape = new PolygonShape();
        def.type = BodyDef.BodyType.DynamicBody;
        def.position.set(rectangle.x + rectangle.width/2.0f, rectangle.y + rectangle.height/2.0f);
        polygonShape.setAsBox(rectangle.width/2.0f, rectangle.height/2.0f);
        fdef.shape = polygonShape;
        //сила притяжения
        def.gravityScale = 0;
        //трение
        fdef.friction = 1;
        //плотность
        fdef.density = 10;
        //упругость
        fdef.restitution = 0;
        Body body;
        body = world.createBody(def);
        body.createFixture(fdef).setUserData("bullet");
        polygonShape.dispose();
        return body;
    }

    public Body addObject(RectangleMapObject object) {
        Rectangle rect = object.getRectangle();
        String type = (String) object.getProperties().get("BodyType");
        BodyDef def = new BodyDef();
        FixtureDef fdef = new FixtureDef();
        PolygonShape polygonShape = new PolygonShape();

        if (type.equals("StaticBody")) def.type = BodyDef.BodyType.StaticBody;
        if (type.equals("DynamicBody")) def.type = BodyDef.BodyType.DynamicBody;

        def.position.set(rect.x + rect.width/2.0f, rect.y + rect.height/2.0f);

        polygonShape.setAsBox(rect.width/2.0f, rect.height/2.0f);
        fdef.shape = polygonShape;

        //сила притяжения
        def.gravityScale = (float) object.getProperties().get("gravityScale");

        //трение
        fdef.friction = (float) object.getProperties().get("friction");

        //плотность
        fdef.density = (float) object.getProperties().get("density");

        //упругость
        fdef.restitution = (float) object.getProperties().get("restitution");

        Body body;
        body = world.createBody(def);
        String name = object.getName();
        body.createFixture(fdef).setUserData(name);

        if (name != null && name.equals("hero")){
            polygonShape.setAsBox(rect.width/12,rect.height/12,new Vector2(0,-rect.width/2),0);
            body.createFixture(fdef).setSensor(true);
            polygonShape.setAsBox(rect.width/12,rect.height/12,new Vector2(0,rect.width/2),0);
            body.createFixture(fdef).setSensor(true);
            polygonShape.setAsBox(rect.width/12,rect.height/12,new Vector2(rect.width/2,0),0);
            body.createFixture(fdef).setSensor(true);
            polygonShape.setAsBox(rect.width/12,rect.height/12,new Vector2(-rect.width/2,0),0);
            body.createFixture(fdef).setSensor(true);
        }

        if (name != null && name.equals("snake")){

            polygonShape.setAsBox(rect.width/12,rect.height/12,new Vector2(-rect.width/2,-rect.height/2),0);
            body.createFixture(fdef).setUserData("leftSensor");
            body.getFixtureList().get(body.getFixtureList().size-1).setSensor(true);

            polygonShape.setAsBox(rect.width/12,rect.height/12,new Vector2(rect.width/2,-rect.height/2),0);
            body.createFixture(fdef).setUserData("rightSensor");
            body.getFixtureList().get(body.getFixtureList().size-1).setSensor(true);
        }

        polygonShape.dispose();
        return body;
    }

    public void setGravity(Vector2 gravity) {world.setGravity(gravity);}
    public void step() {world.step(1/60.0f, 3, 3);}
    public void debugDraw(OrthographicCamera cam){debugRenderer.render(world, cam.combined);}

    public void dispose() {
        world.dispose();
        debugRenderer.dispose();
    }
}
