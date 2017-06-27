package com.mygdx.mario.objects;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.mario.Config;
import com.mygdx.mario.ui.Hud;

/**
 * Created by Luis on 04/11/2016.
 */

public class Mushroom extends Item {
    public Mushroom(World world, TextureAtlas textureAtlas, Vector2 position){
        super(world,textureAtlas,position);
        setRegion(textureAtlas.findRegion("mushroom"),0,0,16,16);
        setBounds(getX(),getY(),16/ Config.PPM,16/Config.PPM);
        velocity = new Vector2(0.7f, 0);
    }
    @Override
    public void defineBody() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(getX(),getY());
        bodyDef.type =BodyDef.BodyType.DynamicBody;
        body = world.createBody(bodyDef);
        FixtureDef fixtureDef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(5f/ Config.PPM);
        fixtureDef.filter.categoryBits = Config.ITEM_BIT;
        fixtureDef.filter.maskBits =
                Config.MARIO_BIT |
                        Config.COIN_BIT |
                        Config.BRICK_BIT |
                        Config.GROUND_BIT | Config.OBJECT_BIT;
        fixtureDef.shape = shape;
        body.createFixture(fixtureDef).setUserData(this);
    }


    @Override
    public void update(float delta) {
        super.update(delta);
        if (!destroyed){
            setPosition(body.getPosition().x - getWidth()/2,body.getPosition().y - getHeight()/2);
            velocity.y = body.getLinearVelocity().y;
            body.setLinearVelocity(velocity);
        }
    }

    @Override
    public void collect(Mario mario){
        Hud.instance.updateScore(100);
        mario.grow();
        destroy();
    }
}
