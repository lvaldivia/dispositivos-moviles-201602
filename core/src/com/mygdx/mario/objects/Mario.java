package com.mygdx.mario.objects;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.mario.Config;
import com.mygdx.mario.CustomAssetManager;
import com.mygdx.mario.utils.objects.PhysicsGameObject;

/**
 * Created by Luis on 25/10/2016.
 */

public class Mario extends PhysicsGameObject {
    private TextureRegion marioStand;
    public enum Status {FALLING, JUMPING, STANDING, RUNNING, GROWING,DEAD};
    private boolean runningRight;
    private Status currentState;
    private Status prevState;
    private Animation marioRunning;
    private TextureRegion marioJumping;
    private Animation marioGrowing;
    private boolean isBig;
    private boolean runGrowAnimation;
    private boolean timeToDefineBigMario;
    private boolean timeToRedefineMario = false;
    private boolean isDead = false;
    private TextureRegion bigMarioJump, bigMarioStand;
    private TextureRegion deadAnimation;
    private Animation bigMarioRunning;

    public Mario(World world, TextureAtlas textureAtlas,Vector2 position){
        super(world,textureAtlas,position);
        currentState = Status.STANDING;
        prevState = Status.STANDING;

        runningRight = true;
        for (int i = 1; i < 4; i++) {
            frames.add(new TextureRegion(textureAtlas.findRegion("little_mario"),i * 16,0,16,16));
        }
        marioRunning = new Animation(0.1f,frames);

        marioStand = new TextureRegion(textureAtlas.findRegion("little_mario"),0,0,16,16);
        marioJumping = new TextureRegion(textureAtlas.findRegion("little_mario"),80,0,16,16);
        deadAnimation = new TextureRegion(textureAtlas.findRegion("little_mario"),96,0,16,16);

        setBounds(0,0,16/Config.PPM,16/Config.PPM);
        setRegion(marioStand);

        bigMarioStand = new TextureRegion(textureAtlas.findRegion("big_mario"),0,0,16,32);
        bigMarioJump = new TextureRegion(textureAtlas.findRegion("big_mario"),80,0,16,32);

        frames.clear();
        for (int i = 1; i < 4; i++) {
            frames.add(new TextureRegion(textureAtlas.findRegion("big_mario"),i * 16,0,16,32));
        }
        bigMarioRunning = new Animation(0.1f,frames);

        frames.clear();
        frames.add(new TextureRegion(textureAtlas.findRegion("big_mario"),240,0,16,32));
        frames.add(new TextureRegion(textureAtlas.findRegion("big_mario"),0,0,16,32));
        frames.add(new TextureRegion(textureAtlas.findRegion("big_mario"),240,0,16,32));
        frames.add(new TextureRegion(textureAtlas.findRegion("big_mario"),0,0,16,32));
        marioGrowing = new Animation(0.2f,frames);
    }

    public void update(float delta){
        if(!isBig){
            setPosition(body.getPosition().x-getWidth()*0.5f,body.getPosition().y-getHeight()*0.5f);
        }else{
            setPosition(body.getPosition().x-getWidth()*0.5f,body.getPosition().y-getHeight()*0.5f - 6/Config.PPM);
        }
        setRegion(getFrame(delta));
        if(timeToDefineBigMario){
            defineBigMario();
        }
        if(timeToRedefineMario){
            redefineMario();
        }
        if (getY() < -getHeight()) {
            isDead = true;
            destroy();
        }
    }

    public void kill() {
        isDead = true;
        Filter filter = new Filter();
        filter.maskBits = Config.NOTHING;
        for (Fixture fix : body.getFixtureList()) {
            fix.setFilterData(filter);
        }
        body.applyLinearImpulse(new Vector2(0, 4f), body.getWorldCenter(), true);
        Music bg = CustomAssetManager.manager.get(CustomAssetManager.MARIO_MUSIC, Music.class);
        bg.stop();
        Sound dead = CustomAssetManager.manager.get(CustomAssetManager.DEAD, Sound.class);
        dead.play();
    }

    private void defineBigMario(){
        Vector2 currentPosition = body.getPosition();
        world.destroyBody(body);
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(currentPosition.add(0,10/Config.PPM));
        bodyDef.type =BodyDef.BodyType.DynamicBody;
        body = world.createBody(bodyDef);
        FixtureDef fixtureDef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(5f/ Config.PPM);
        fixtureDef.filter.categoryBits = Config.MARIO_BIT;
        fixtureDef.filter.maskBits = Config.ITEM_BIT | Config.BRICK_BIT | Config.GROUND_BIT | Config.COIN_BIT | Config.OBJECT_BIT | Config.ENEMY_BIT | Config.ENEMY_HEAD_BIT | Config.GOAL_BIT;
        fixtureDef.shape = shape;
        body.createFixture(fixtureDef).setUserData(this);
        shape.setPosition(new Vector2(0,-14/Config.PPM));
        body.createFixture(fixtureDef).setUserData(this);
        EdgeShape head = new EdgeShape();
        head.set(new Vector2(-2/Config.PPM,7/Config.PPM),new Vector2(2/Config.PPM,7/Config.PPM));
        fixtureDef.shape = head;
        fixtureDef.isSensor = true;
        fixtureDef.filter.categoryBits = Config.MARIO_HEAD_BIT;
        body.createFixture(fixtureDef).setUserData(this);
        timeToDefineBigMario = false;
    }

    public void hit(Enemy enemy) {
        if(enemy instanceof Turtle && ((Turtle)enemy).current == Turtle.Status.STANDING_SHELL) {
            Turtle turtle = (Turtle) enemy;
            turtle.kick(this.getX() < turtle.getX() ? Turtle.RIGHT_SPEED : Turtle.LEFT_SPEED);
        }
        else if (isBig) {
            isBig = false;
            timeToRedefineMario = true;
            Sound sound = CustomAssetManager.manager.get(CustomAssetManager.POWER_DOWN, Sound.class);
            sound.play();
            setBounds(getX(), getY(), getWidth(), getHeight() /2f);
        } else {
            kill();
        }
    }

    private void redefineMario() {
        Vector2 currentPosition = body.getPosition();
        world.destroyBody(body);
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(currentPosition);
        bodyDef.type =BodyDef.BodyType.DynamicBody;
        body = world.createBody(bodyDef);
        FixtureDef fixtureDef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(5f/ Config.PPM);
        fixtureDef.filter.categoryBits = Config.MARIO_BIT;
        fixtureDef.filter.maskBits = Config.ITEM_BIT |
                Config.BRICK_BIT | Config.GROUND_BIT | Config.COIN_BIT | Config.OBJECT_BIT | Config.ENEMY_BIT | Config.ENEMY_HEAD_BIT | Config.GOAL_BIT;
        fixtureDef.shape = shape;
        body.createFixture(fixtureDef).setUserData(this);
        EdgeShape head = new EdgeShape();
        head.set(new Vector2(-2/Config.PPM,7/Config.PPM),new Vector2(2/Config.PPM,7/Config.PPM));
        fixtureDef.shape = head;
        fixtureDef.isSensor = true;
        fixtureDef.filter.categoryBits = Config.MARIO_HEAD_BIT;
        body.createFixture(fixtureDef).setUserData(this);
        timeToRedefineMario = false;
    }

    public void grow(){
        if(!isBig){
            Sound sound = CustomAssetManager.manager.get(CustomAssetManager.POWER_UP, Sound.class);
            sound.play();
            runGrowAnimation = true;
            timeToDefineBigMario = true;
            isBig = true;
            setBounds(getX(),getY(),getWidth(),getHeight()*2f);
        }
    }

    @Override
    public TextureRegion getFrame(float delta){
        currentState =getState();
        TextureRegion region = new TextureRegion();
        switch (currentState){
            case DEAD:
                region = deadAnimation;
                break;
            case GROWING:
                region = marioGrowing.getKeyFrame(stateTime);
                if(marioGrowing.isAnimationFinished(stateTime)){
                    runGrowAnimation = false;
                }
                break;
            case JUMPING:
                region = isBig ? bigMarioJump : marioJumping;
                break;
            case RUNNING:
                region = isBig ? bigMarioRunning.getKeyFrame(stateTime,true) : marioRunning.getKeyFrame(stateTime,true);
                break;
            case FALLING:
            case STANDING:
                region = isBig ? bigMarioStand : marioStand;
                break;

        }
        if((body.getLinearVelocity().x<0 || !runningRight) && !region.isFlipX()){
            region.flip(true,false);
            runningRight = false;
        }else if((body.getLinearVelocity().x>0 || runningRight) && region.isFlipX()){
            region.flip(true,false);
            runningRight = true;
        }
        stateTime = currentState == prevState ? stateTime+delta:0;
        prevState = currentState;
        return region;
    }

    public Status getState(){
        if (isDead) {
            return Status.DEAD;
        }
        else if(runGrowAnimation){
            return Status.GROWING;
        }
        else if(body.getLinearVelocity().y>0 || (body.getLinearVelocity().y<0 && prevState == Status.JUMPING))
            return Status.JUMPING;
        else if(body.getLinearVelocity().y<0)
            return Status.FALLING;
        else if(body.getLinearVelocity().x!=0)
            return Status.RUNNING;
        else
            return Status.STANDING;
    }
    @Override
    public void defineBody(){
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(75/Config.PPM,100/Config.PPM);
        bodyDef.type =BodyDef.BodyType.DynamicBody;
        body = world.createBody(bodyDef);
        FixtureDef fixtureDef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(5f/ Config.PPM);
        fixtureDef.filter.categoryBits = Config.MARIO_BIT;
        fixtureDef.filter.maskBits = Config.ITEM_BIT |
                        Config.BRICK_BIT | Config.GROUND_BIT | Config.COIN_BIT | Config.OBJECT_BIT | Config.ENEMY_BIT | Config.ENEMY_HEAD_BIT | Config.GOAL_BIT;
        fixtureDef.shape = shape;
        body.createFixture(fixtureDef).setUserData(this);
        EdgeShape head = new EdgeShape();
        head.set(new Vector2(-2/Config.PPM,7/Config.PPM),new Vector2(2/Config.PPM,7/Config.PPM));
        fixtureDef.shape = head;
        fixtureDef.isSensor = true;
        fixtureDef.filter.categoryBits = Config.MARIO_HEAD_BIT;
        body.createFixture(fixtureDef).setUserData(this);
    }

    @Override
    public void dispose() {
        super.dispose();

    }
}
