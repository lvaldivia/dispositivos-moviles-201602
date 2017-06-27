package com.mygdx.mario.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.mario.CustomAssetManager;
import com.mygdx.mario.objects.InteractiveTiledObject;
import com.mygdx.mario.Config;
/**
 * Created by Luis on 04/11/2016.
 */

public class Brick extends InteractiveTiledObject {
    public Brick(World world, TextureAtlas atlas, TiledMap map, Rectangle bounds){
        super(world,atlas,map,bounds);
        this.fixture.setUserData(this);
        setCagoryFilter(Config.BRICK_BIT);
    }

    @Override
    public void onHeadHit() {
        Sound sound =CustomAssetManager.manager.get(CustomAssetManager.BREAK_BLOCK_BUMP);
        sound.play();
        setCagoryFilter(Config.DESTROY_BIT);
        getCell().setTile(null);
    }
}
