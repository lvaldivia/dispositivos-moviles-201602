package com.mygdx.mario.objects;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.mygdx.mario.Config;

/**
 * Created by Alumnos on 05/11/2016.
 */
public class Turtle extends Enemy {
    public static final int LEFT_SPEED = -2;
    public static final int RIGHT_SPEED = 2;
    public enum Status {WALKING, STANDING_SHELL, DEAD, MOVING_SHELL};
    private Animation walkingAnimation;
    public Status current;
    private TextureRegion shell;
    private Status previous;
    private boolean runningRight;
    public Turtle(World world, TextureAtlas textureAtlas, Vector2 position) {
        super(world, textureAtlas, position);
        frames = new Array<TextureRegion>();
        frames.add(new TextureRegion(atlas.findRegion("turtle"), 0, 0, 16, 24));
        frames.add(new TextureRegion(atlas.findRegion("turtle"), 16, 0, 16, 24));
        shell = new TextureRegion(atlas.findRegion("turtle"), 64, 0, 16, 24);
        walkingAnimation = new Animation(0.2f, frames);
        current = Status.WALKING;
        previous = current;
        setBounds(getX(), getY(), 16/ Config.PPM, 16/ Config.PPM);
        velocity.x = 0.7f;
    }

    @Override
    public TextureRegion getFrame(float delta) {
        //return super.getFrame(delta);
        TextureRegion region;
        switch (current) {
            case MOVING_SHELL:
            case STANDING_SHELL:
                region = shell;
                break;
            case WALKING:
                default:
                    region = walkingAnimation.getKeyFrame(stateTime, true);
                    break;
        }

        if((body.getLinearVelocity().x>0) && !region.isFlipX()){
            region.flip(true,false);
            runningRight = false;
        }else if((body.getLinearVelocity().x<0) && region.isFlipX()){
            region.flip(true,false);
            runningRight = true;
        }
        stateTime = current == previous ? stateTime+delta:0;
        previous = current;
        return region;
    }

    public void update(float dt) {
        super.update(dt);
        stateTime += dt;
       if (!destroyed) {
            setPosition(body.getPosition().x - getWidth()/2,body.getPosition().y - getHeight()/2);
            velocity.y = body.getLinearVelocity().y;
            body.setLinearVelocity(velocity);
            setRegion(getFrame(dt));
        }
    }

    public void kick(int direction) {
        velocity.x = direction;
        current = Status.MOVING_SHELL;
    }

    @Override
    public void hideOnHead() {
        if (current != Status.STANDING_SHELL) {
            current = Status.STANDING_SHELL;
            velocity.x = 0;
        } else {

        }
    }

    @Override
    public void OnEnemyHit(Enemy enemy) {
        if (current != Status.MOVING_SHELL) {
            reverseVelocity(true, false);
        }
    }
}
