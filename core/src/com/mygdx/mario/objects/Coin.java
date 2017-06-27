package com.mygdx.mario.objects;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.mario.Config;
import com.mygdx.mario.CustomAssetManager;
import com.mygdx.mario.screens.PlayScreen;

/**
 * Created by Luis on 04/11/2016.
 */

public class Coin extends InteractiveTiledObject {
    private static TiledMapTileSet set;
    private final int BLANK_COIN = 28;

    public Coin(World world, TextureAtlas atlas, TiledMap map, Rectangle rectangle){
        super(world,atlas,map,rectangle);
        set = map.getTileSets().getTileSet("tileset_gutter");
        this.fixture.setUserData(this);
        setCagoryFilter(Config.COIN_BIT);
    }

    @Override
    public void onHeadHit() {
        if(getCell().getTile().getId() == BLANK_COIN){
            Sound sound = CustomAssetManager.manager.get(CustomAssetManager.BUMP_SOUND);
            sound.play();
        }else{
            Sound sound = CustomAssetManager.manager.get(CustomAssetManager.COIN_SOUND);
            sound.play();
            getCell().setTile(set.getTile(28));
            PlayScreen.instance.spawnItem(new ItemDef(new Vector2(body.getPosition().x, body.getPosition().y +16/Config.PPM),Mushroom.class));
        }

    }
}
