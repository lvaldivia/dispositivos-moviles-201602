package com.mygdx.mario.objects;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.mario.Config;
import com.mygdx.mario.screens.PlayScreen;

/**
 * Created by Alumnos on 26/11/2016.
 */
public class Goal extends InteractiveTiledObject {
    boolean flag;
    public Goal(World world, TextureAtlas textureAtlas, TiledMap map, Rectangle bounds) {
        super(world, textureAtlas, map, bounds);
        this.fixture.setUserData(this);
        setCagoryFilter(Config.GOAL_BIT);
        flag = false;
    }

    public void EndLevel() {
        if (!flag) {
            flag = true;
            PlayScreen.instance.EndLevel();
        }
    }

    @Override
    public void onHeadHit() {

    }
}
