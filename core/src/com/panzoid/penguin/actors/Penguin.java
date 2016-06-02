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

    public interface PenguinStateListener {
        void onStateChange(int state);
    }

    public static final int STATE_IDLE = 0;
    public static final int STATE_MOVE = 1;

    public static final Vector2 LEFT = new Vector2(-1f, 0f);
    public static final Vector2 RIGHT = new Vector2(1f, 0f);
    public static final Vector2 UP = new Vector2(0f, 1f);
    public static final Vector2 DOWN = new Vector2(0f, -1f);

    private Set<PenguinStateListener> listeners = new HashSet<PenguinStateListener>();

    private int state = STATE_IDLE;
    private Vector2 direction = DOWN;

    private static HashMap<Integer, HashMap<Vector2, Sprite>> spriteMap;
    static {
        spriteMap = new HashMap<Integer, HashMap<Vector2, Sprite>>();

        TextureAtlas atlas = new TextureAtlas("sprites/pack.atlas");
        Skin skin = new Skin();
        skin.addRegions(atlas);

        HashMap<Vector2, Sprite> idleMap = new HashMap<Vector2, Sprite>();
        idleMap.put(LEFT, skin.getSprite("gunter_idle_left"));
        idleMap.put(RIGHT, skin.getSprite("gunter_idle_right"));
        idleMap.put(UP, skin.getSprite("gunter_idle_back"));
        idleMap.put(DOWN, skin.getSprite("gunter_idle_front"));
        spriteMap.put(STATE_IDLE, idleMap);

        HashMap<Vector2, Sprite> slideMap = new HashMap<Vector2, Sprite>();
        slideMap.put(LEFT, skin.getSprite("gunter_slide_left"));
        slideMap.put(RIGHT, skin.getSprite("gunter_slide_right"));
        slideMap.put(UP, skin.getSprite("gunter_slide_back"));
        slideMap.put(DOWN, skin.getSprite("gunter_slide_front"));
        spriteMap.put(STATE_MOVE, slideMap);
    }

    public Penguin() {
        super(spriteMap.get(STATE_IDLE).get(DOWN));
        setWidth(getWidth() / Constants.GAME_UNIT);
        setHeight(getHeight() / Constants.GAME_UNIT);
    }

    public boolean addListener(PenguinStateListener listener) {
        return listeners.add(listener);
    }

    public boolean removeListener(PenguinStateListener listener) {
        return listeners.remove(listener);
    }

    public void setDirection(Vector2 direction) {
        if(LEFT.hasSameDirection(direction)) {
            this.direction = LEFT;
        } else if (RIGHT.hasSameDirection(direction)) {
            this.direction = RIGHT;
        } else if (UP.hasSameDirection(direction)) {
            this.direction = UP;
        } else {
            this.direction = DOWN;
        }
        setDrawable();
    }

    public void setState(int state) {
        this.state = state;
        for(PenguinStateListener listener : listeners) {
            listener.onStateChange(state);
        }
        setDrawable();
    }

    public int getState() {
        return state;
    }

    public void setDrawable() {
        Sprite sprite = spriteMap.get(state).get(direction);
        super.setDrawable(new SpriteDrawable(sprite));
    }
}
