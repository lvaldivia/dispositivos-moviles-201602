package com.mygdx.mario.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.mario.Config;
import com.mygdx.mario.CustomAssetManager;
import com.mygdx.mario.Games;
import com.mygdx.mario.objects.Enemy;
import com.mygdx.mario.objects.Item;
import com.mygdx.mario.objects.ItemDef;
import com.mygdx.mario.objects.Mario;
import com.mygdx.mario.objects.Mushroom;
import com.mygdx.mario.tools.ElementCreator;
import com.mygdx.mario.tools.WorldContactListener;
import com.mygdx.mario.ui.Hud;
import com.mygdx.mario.utils.states.PhysicsState;

import java.util.concurrent.LinkedBlockingDeque;

/**
 * Created by Luis on 04/11/2016.
 */

public class PlayScreen extends PhysicsState {
    protected TextureAtlas textureAtlas;
    private OrthogonalTiledMapRenderer tiledMapRenderer;
    private TmxMapLoader mapLoader;
    private TiledMap map;
    private ElementCreator creator;
    public static PlayScreen instance;
    private Hud hud;
    private LinkedBlockingDeque<ItemDef> itemsToSpawn;
    private Array<Item> items;
    private float elapsedDead;
    private boolean isGameOver;
    private boolean flag;
    private int level;

    public PlayScreen(SpriteBatch spriteBatch, int level) {
        super(spriteBatch);
        flag = false;
        isGameOver = false;
        instance = this;
        this.level = level;
        textureAtlas = new TextureAtlas("mario.pack");
        hud = new Hud(spriteBatch, this.level);
        mapLoader = new TmxMapLoader();
        map = mapLoader.load("level" + "3" + ".tmx");
        tiledMapRenderer = new OrthogonalTiledMapRenderer(map,1/Config.PPM);
        creator =new ElementCreator(world,map,textureAtlas);
        player = new Mario(world,textureAtlas,new Vector2());
        world.setContactListener(new WorldContactListener());
        itemsToSpawn = new LinkedBlockingDeque<ItemDef>();
        items = new Array<Item>();

        Gdx.app.log("level", this.level + "");
    }

    public void EndLevel() {
        Gdx.app.log("level", this.level + "");
        if (!flag)
        {
            flag = true;
            level++;
            if (level>4)
            {
                Games.instance.setScreen(new GameOver(spriteBatch, level));
                dispose();
            }
            Games.instance.setScreen(new PlayScreen(spriteBatch, level));
            dispose();
        }
    }

    public void spawnItem(ItemDef idef){
        itemsToSpawn.add(idef);
    }

    public void handleSpawningItems(){
        if(!itemsToSpawn.isEmpty()){
            ItemDef def = itemsToSpawn.poll();
            if(def.type == Mushroom.class){
                items.add(new Mushroom(world,textureAtlas,def.position));
            }
        }
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        tiledMapRenderer.render();
        spriteBatch.setProjectionMatrix(camera.combined);
        spriteBatch.begin();
        for(Item item: items){
            item.draw(spriteBatch);
        }
        for (Enemy enemy : creator.enemies) {
            enemy.draw(spriteBatch);
        }
        player.draw(spriteBatch);
        spriteBatch.end();
        spriteBatch.setProjectionMatrix(hud.getCamera().combined);
        hud.draw();
    }

    @Override
    public void update(float delta){
        super.update(delta);
        handleSpawningItems();
        for(Item item: items){
            item.update(delta);
        }
        for (Enemy enemy : creator.enemies) {
            enemy.update(delta);
        }
        if (player.getState() != Mario.Status.DEAD)
            camera.position.x = player.body.getPosition().x;

        player.update(delta);
        camera.update();
        tiledMapRenderer.setView(camera);
        hud.update(delta);
        if (player.getState() == Mario.Status.DEAD) {
            elapsedDead += delta;
            if (elapsedDead >=3f) {
                elapsedDead = 0;
                Games.instance.setScreen(new GameOver(spriteBatch, level));
            }
        }
        if (player.getX() > 3300 / Config.PPM)
            EndLevel();

        if (hud.worldTimer <= 0 && !isGameOver)
        {
            isGameOver = true;
            player.kill();
        }
    }

    @Override
    public void dispose() {
        tiledMapRenderer.dispose();
        debugRenderer.dispose();
        player.dispose();
        hud.dispose();
        world.dispose();
        map.dispose();
    }
}
