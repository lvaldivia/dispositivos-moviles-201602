package com.mygdx.mario.objects;

import com.badlogic.gdx.math.Vector2;

/**
 * Created by Luis on 04/11/2016.
 */

public class ItemDef {
    public Vector2 position;
    public Class<?> type;

    public ItemDef(Vector2 position, Class<?>type){
        this.position = position;
        this.type = type;
    }
}
