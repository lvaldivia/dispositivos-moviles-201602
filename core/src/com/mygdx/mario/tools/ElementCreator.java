package com.mygdx.mario.tools;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.Map;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.mygdx.mario.Config;
import com.mygdx.mario.objects.Brick;
import com.mygdx.mario.objects.Coin;
import com.mygdx.mario.objects.Enemy;
import com.mygdx.mario.objects.Goal;
import com.mygdx.mario.objects.Goomba;
import com.mygdx.mario.objects.Turtle;


/**
 * Created by Luis on 04/11/2016.
 */

public class ElementCreator {
    public Array<Enemy> enemies;

    public ElementCreator(World world, TiledMap map, TextureAtlas atlas){
        BodyDef bodyDef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fixtureDef = new FixtureDef();
        Body body;
        enemies = new Array<Enemy>();

        for (MapObject mapObject: map.getLayers().get(2).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject)mapObject).getRectangle();
            bodyDef.type= BodyDef.BodyType.StaticBody;
            bodyDef.position.set((rect.getX()+rect.getWidth()/2)/ Config.PPM,(rect.getY()+ rect.getHeight()/2)/Config.PPM);
            shape.setAsBox(rect.getWidth()/2/Config.PPM,rect.getHeight()/2/Config.PPM);
            body = world.createBody(bodyDef);
            fixtureDef.shape = shape;
            fixtureDef.filter.categoryBits = Config.GROUND_BIT;
            body.createFixture(fixtureDef);
        }

        for (MapObject mapObject: map.getLayers().get(3).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject)mapObject).getRectangle();
            bodyDef.type= BodyDef.BodyType.StaticBody;
            bodyDef.position.set((rect.getX()+rect.getWidth()/2)/Config.PPM,(rect.getY()+ rect.getHeight()/2)/Config.PPM);
            shape.setAsBox(rect.getWidth()/2/Config.PPM,rect.getHeight()/2/Config.PPM);
            body = world.createBody(bodyDef);
            fixtureDef.shape = shape;
            fixtureDef.filter.categoryBits = Config.OBJECT_BIT;
            body.createFixture(fixtureDef);
        }

        for (MapObject mapObject: map.getLayers().get(4).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject)mapObject).getRectangle();
            new Coin(world,atlas,map,rect);
        }

        for (MapObject mapObject: map.getLayers().get(5).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject)mapObject).getRectangle();
            new Brick(world,atlas,map,rect);
        }

        for (MapObject mapObject: map.getLayers().get(6).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject)mapObject).getRectangle();
            Vector2 position =  new Vector2(
                    (rect.getX() + rect.getWidth()/2)/Config.PPM,
                    (rect.getY() + rect.getHeight()/2)/Config.PPM
            );
            enemies.add(new Goomba(world, atlas, position));
        }

        for (MapObject mapObject: map.getLayers().get(7).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject)mapObject).getRectangle();
            Vector2 position =  new Vector2(
                    (rect.getX() + rect.getWidth()/2)/Config.PPM,
                    (rect.getY() + rect.getHeight()/2)/Config.PPM
            );
            enemies.add(new Turtle(world, atlas, position));
        }


    }
}
