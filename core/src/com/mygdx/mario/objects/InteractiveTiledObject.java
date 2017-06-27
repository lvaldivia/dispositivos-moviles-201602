package com.mygdx.mario.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.mario.Config;
import com.mygdx.mario.utils.objects.PhysicsGameObject;

/**
 * Created by Luis on 04/11/2016.
 */

public abstract class InteractiveTiledObject extends PhysicsGameObject {
    protected TiledMap tiledMap;
    private Rectangle bounds;
    protected Fixture fixture;
    public InteractiveTiledObject(World world, TextureAtlas textureAtlas, TiledMap map, Rectangle bounds){
        super(world, textureAtlas,new Vector2());
        this.tiledMap = map;
        this.bounds = bounds;
        define();
    }

    private void define(){
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set((bounds.getX()+bounds.getWidth()/2)/ Config.PPM,(bounds.getY()+ bounds.getHeight()/2)/Config.PPM);
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(bounds.getWidth()/2/ Config.PPM,bounds.getHeight()/2/Config.PPM);
        body = world.createBody(bodyDef);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixture =body.createFixture(fixtureDef);
    }

    @Override
    public void defineBody() {

    }

    public abstract void onHeadHit();
    public void setCagoryFilter(short filterBit){
        Filter filter = new Filter();
        filter.categoryBits = filterBit;
        fixture.setFilterData(filter);
    }

    public TiledMapTileLayer.Cell getCell(){
        TiledMapTileLayer layer = (TiledMapTileLayer) tiledMap.getLayers().get(1);
        return layer.getCell((int)(body.getPosition().x * Config.PPM / 16)
                ,(int)(body.getPosition().y * Config.PPM/16));
    }
}
