package com.mygdx.mario.objects;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.mario.Config;
import com.mygdx.mario.utils.objects.PhysicsGameObject;

/**
 * Created by Alumnos on 05/11/2016.
 */
public abstract class Enemy extends PhysicsGameObject {

    public Enemy(World world) {
        super(world);
    }

    public Enemy(World world, TextureAtlas textureAtlas, Vector2 position) {
        super(world, textureAtlas, position);
    }

    public Enemy(World world, TextureRegion textureRegion, Vector2 position) {
        super(world, textureRegion, position);
    }

    @Override
    public void defineBody(){
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(getX(), getY());
        bodyDef.type =BodyDef.BodyType.DynamicBody;
        body = world.createBody(bodyDef);
        FixtureDef fixtureDef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(5f/ Config.PPM);
        fixtureDef.filter.categoryBits = Config.ENEMY_BIT;
        fixtureDef.filter.maskBits = Config.ENEMY_BIT |
                Config.MARIO_BIT | Config.GROUND_BIT | Config.OBJECT_BIT;
        fixtureDef.shape = shape;
        body.createFixture(fixtureDef).setUserData(this);
        EdgeShape head = new EdgeShape();
        head.set(new Vector2(-2/Config.PPM,7/Config.PPM),new Vector2(2/Config.PPM,7/Config.PPM));
        fixtureDef.shape = head;
        fixtureDef.restitution = 0.5f;
        fixtureDef.isSensor = true;
        fixtureDef.filter.categoryBits = Config.ENEMY_HEAD_BIT;
        fixtureDef.filter.maskBits = Config.MARIO_BIT;
        body.createFixture(fixtureDef).setUserData(this);
    }

    public abstract void hideOnHead();
    public abstract void OnEnemyHit(Enemy enemy);

}
