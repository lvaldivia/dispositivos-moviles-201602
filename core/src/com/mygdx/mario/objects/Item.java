package com.mygdx.mario.objects;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.mario.Config;
import com.mygdx.mario.utils.objects.PhysicsGameObject;

/**
 * Created by Luis on 04/11/2016.
 */

public abstract class Item extends PhysicsGameObject {

    public Item(World world, TextureAtlas atlas, Vector2 position) {
        super(world, atlas, position);
        setBounds(getX(),getY(),16/ Config.PPM,Config.PPM);
    }
    public abstract void collect(Mario mario);

    @Override
    public void update(float delta) {
        if (!destroyed && setToDestroy) {
            world.destroyBody(body);
            destroyed = true;
        }
    }
}
