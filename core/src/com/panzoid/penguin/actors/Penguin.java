package com.panzoid.penguin.actors;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.panzoid.penguin.Constants;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by weipa on 5/23/16.
 */
public class Penguin extends Image {

    public interface PenguinListener {
        void onStateChange(int state);
    }

    public static final int STATE_IDLE = 0;
    public static final int STATE_MOVE = 1;
    public static final int STATE_FINISH = 2;

    private final Set<PenguinListener> listeners;

    private int state;
    private Vector2 direction;

    private static HashMap<Integer, HashMap<Vector2, Sprite>> spriteMap;
    static {
        spriteMap = new HashMap<Integer, HashMap<Vector2, Sprite>>();

        TextureAtlas atlas = new TextureAtlas("sprites/pack.atlas");
        Skin skin = new Skin();
        skin.addRegions(atlas);

        HashMap<Vector2, Sprite> idleMap = new HashMap<Vector2, Sprite>();
        idleMap.put(Constants.LEFT, skin.getSprite("gunter_idle_left"));
        idleMap.put(Constants.RIGHT, skin.getSprite("gunter_idle_right"));
        idleMap.put(Constants.UP, skin.getSprite("gunter_idle_back"));
        idleMap.put(Constants.DOWN, skin.getSprite("gunter_idle_front"));
        spriteMap.put(STATE_IDLE, idleMap);

        HashMap<Vector2, Sprite> slideMap = new HashMap<Vector2, Sprite>();
        slideMap.put(Constants.LEFT, skin.getSprite("gunter_slide_left"));
        slideMap.put(Constants.RIGHT, skin.getSprite("gunter_slide_right"));
        slideMap.put(Constants.UP, skin.getSprite("gunter_slide_back"));
        slideMap.put(Constants.DOWN, skin.getSprite("gunter_slide_front"));
        spriteMap.put(STATE_MOVE, slideMap);
    }

    public Penguin() {
        super(spriteMap.get(STATE_IDLE).get(Constants.DOWN));
        setScale(Constants.GAME_SCALE);
        state = STATE_IDLE;
        direction = Constants.DOWN;
        listeners = new HashSet<PenguinListener>();
    }

    public boolean addListener(PenguinListener listener) {
        return listeners.add(listener);
    }

    public boolean removeListener(PenguinListener listener) {
        return listeners.remove(listener);
    }

    public void setDirection(Vector2 direction) {
        if(Constants.LEFT.hasSameDirection(direction)) {
            this.direction = Constants.LEFT;
        } else if (Constants.RIGHT.hasSameDirection(direction)) {
            this.direction = Constants.RIGHT;
        } else if (Constants.UP.hasSameDirection(direction)) {
            this.direction = Constants.UP;
        } else {
            this.direction = Constants.DOWN;
        }
        setDrawable();
    }

    public void setState(int state) {
        this.state = state;
        for(PenguinListener listener : listeners) {
            listener.onStateChange(state);
        }
        setDrawable();
    }

    public int getState() {
        return state;
    }

    public void setDrawable() {
        Sprite sprite = spriteMap.get(state == STATE_FINISH ? STATE_IDLE : state).get(direction);
        super.setDrawable(new SpriteDrawable(sprite));
    }
}
