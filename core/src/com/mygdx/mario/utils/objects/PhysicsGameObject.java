package com.mygdx.mario.utils.objects;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Created by Luis on 23/10/2016.
 */

public abstract class PhysicsGameObject extends GameObject {
    protected World world;
    public Body body;
    protected Vector2 velocity;
    protected boolean setToDestroy;
    protected boolean destroyed;


    public PhysicsGameObject(World world){
        super();
        this.world = world;
        velocity = new Vector2();
    }

    public PhysicsGameObject(World world, TextureAtlas textureAtlas, Vector2 position){
        super(textureAtlas, position);
        this.world = world;
        velocity = new Vector2();
        defineBody();
    }

    public PhysicsGameObject(World world, TextureRegion textureRegion, Vector2 position){
        super(textureRegion,position);
        this.world = world;
        velocity = new Vector2();
        defineBody();
    }

    public void reverseVelocity(boolean x, boolean y) {
        if (x) {
            velocity.x = -velocity.x;
        }
        if (y) {
            velocity.y = -velocity.y;
        }
    }

    @Override
    public void draw(Batch batch) {
        if (!destroyed)
            super.draw(batch);
    }

    protected void destroy(){
        setToDestroy = true;
    }

    public abstract void defineBody();
}
