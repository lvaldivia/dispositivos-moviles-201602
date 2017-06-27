package com.mygdx.mario.utils.objects;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;

/**
 * Created by Luis on 22/10/2016.
 */

public class GameObject extends Sprite implements Disposable {

    protected TextureAtlas atlas;
    protected Vector2 position;
    protected Array<TextureRegion>frames;
    protected float stateTime;
    protected boolean movingRight;

    public GameObject(){

    }

    public TextureRegion getFrame(float delta) {
        return new TextureRegion();


    }

    public GameObject(TextureAtlas atlas, Vector2 position){
        this.atlas = atlas;
        frames = new Array<TextureRegion>();
        this.position = position;
        setPosition(position.x,position.y);
        movingRight = false;
        stateTime = 0f;
    }

    public GameObject(TextureRegion region, Vector2 position){
        super(region);
        this.position = position;
        frames = new Array<TextureRegion>();
        setPosition(position.x,position.y);
        movingRight = false;
        stateTime = 0f;
    }

    public void update(float dt){

    }

    @Override
    public void dispose() {

    }
}
