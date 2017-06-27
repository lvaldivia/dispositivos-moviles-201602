package com.mygdx.mario.utils.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.mario.Config;
import com.mygdx.mario.CustomAssetManager;
import com.mygdx.mario.objects.Mario;

/**
 * Created by Luis on 22/10/2016.
 */

public class PhysicsState extends State {

    protected Viewport viewport;
    protected Mario player;
    protected World world;
    protected Box2DDebugRenderer debugRenderer;
    public PhysicsState(SpriteBatch spriteBatch){
        super(spriteBatch);
        viewport = new FitViewport(Config.GAME_WIDTH/Config.PPM,Config.GAME_HEIGHT/Config.PPM,camera);
        camera.position.set(viewport.getWorldWidth()/2f,viewport.getWorldHeight()/2f,0);
        world = new World(new Vector2(0,-10),true);
        debugRenderer = new Box2DDebugRenderer();
        Music bg = CustomAssetManager.manager.get(CustomAssetManager.MARIO_MUSIC);
        bg.play();
        bg.setVolume(0.25f);
        bg.setLooping(true);
    }

    @Override
    public void render(float delta) {
        super.render(delta);

    }

    @Override
    public void handleInput(float delta){
        if (player.getState() != Mario.Status.DEAD)
        {
            if(Gdx.input.isKeyJustPressed(Input.Keys.UP) && player.body.getLinearVelocity().y ==0f){
                player.body.applyLinearImpulse(new Vector2(0,4f),player.body.getWorldCenter(),true);
            }
            if(Gdx.input.isKeyPressed(Input.Keys.LEFT) && player.body.getLinearVelocity().x>-1.5f){
                player.body.applyLinearImpulse(new Vector2(-0.1f,0),player.body.getWorldCenter(),true);
            }
            if(Gdx.input.isKeyPressed(Input.Keys.RIGHT) && player.body.getLinearVelocity().x<=1.5f){
                player.body.applyLinearImpulse(new Vector2(0.1f,0),player.body.getWorldCenter(),true);
            }
        }
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width,height);
    }

    @Override
    public void update(float dt) {
        super.update(dt);
        world.step(1/60f,1,2);
    }
}
