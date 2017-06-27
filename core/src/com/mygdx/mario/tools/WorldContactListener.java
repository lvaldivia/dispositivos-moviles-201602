package com.mygdx.mario.tools;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.mygdx.mario.Config;
import com.mygdx.mario.objects.Enemy;
import com.mygdx.mario.objects.Goal;
import com.mygdx.mario.objects.InteractiveTiledObject;
import com.mygdx.mario.objects.Item;
import com.mygdx.mario.objects.Mario;
import com.mygdx.mario.objects.Mushroom;

/**
 * Created by Luis on 04/11/2016.
 */

public class WorldContactListener implements ContactListener {


    @Override
    public void beginContact(Contact contact) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();

        int cdef = fixtureA.getFilterData().categoryBits | fixtureB.getFilterData().categoryBits;

        switch (cdef){
            case Config.MARIO_HEAD_BIT | Config.BRICK_BIT:
            case Config.MARIO_HEAD_BIT | Config.COIN_BIT:
                if(fixtureA.getFilterData().categoryBits == Config.MARIO_HEAD_BIT){
                    ((InteractiveTiledObject)fixtureB.getUserData()).onHeadHit();
                }else{
                    ((InteractiveTiledObject)fixtureA.getUserData()).onHeadHit();
                }
                break;


            case Config.ITEM_BIT | Config.MARIO_BIT:
                if(fixtureA.getFilterData().categoryBits == Config.ITEM_BIT){
                    ((Item)fixtureA.getUserData()).collect((Mario) fixtureB.getUserData());
                }else {
                    ((Item)fixtureB.getUserData()).collect((Mario)fixtureA.getUserData());
                }
                break;
            case Config.ITEM_BIT | Config.OBJECT_BIT:
                if(fixtureA.getFilterData().categoryBits == Config.ITEM_BIT){
                    ((Item)fixtureA.getUserData()).reverseVelocity(true, false);
                }else {
                    ((Item)fixtureB.getUserData()).reverseVelocity(true, false);
                }
                break;
            case Config.ENEMY_BIT | Config.OBJECT_BIT:
                if(fixtureA.getFilterData().categoryBits == Config.ENEMY_BIT){
                    ((Enemy)fixtureA.getUserData()).reverseVelocity(true, false);
                }else {
                    ((Enemy)fixtureB.getUserData()).reverseVelocity(true, false);
                }
                break;
            case Config.ENEMY_BIT | Config.ENEMY_BIT:
                ((Enemy)fixtureA.getUserData()).OnEnemyHit((Enemy)fixtureB.getUserData());
                ((Enemy)fixtureB.getUserData()).OnEnemyHit((Enemy)fixtureA.getUserData());
                break;
            case Config.ENEMY_HEAD_BIT | Config.MARIO_BIT:
                if(fixtureA.getFilterData().categoryBits == Config.ENEMY_HEAD_BIT){
                    ((Enemy)fixtureA.getUserData()).hideOnHead();
                }else {
                    ((Enemy)fixtureB.getUserData()).hideOnHead();
                }
                break;
            case Config.MARIO_BIT | Config.ENEMY_BIT:
                if(fixtureA.getFilterData().categoryBits == Config.MARIO_BIT){
                    ((Mario)fixtureA.getUserData()).hit((Enemy)fixtureB.getUserData());
                }else {
                    ((Mario)fixtureB.getUserData()).hit((Enemy)fixtureA.getUserData());
                }
                break;
            case Config.MARIO_BIT | Config.GOAL_BIT:
                if(fixtureA.getFilterData().categoryBits == Config.GOAL_BIT){
                    ((Goal)fixtureA.getUserData()).EndLevel();
                }
                break;
        }
    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
