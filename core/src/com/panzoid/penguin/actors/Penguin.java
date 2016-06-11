package com.panzoid.penguin.actors;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
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
    private final Skin skin;

    private static HashMap<Integer, HashMap<Vector2, String>> spriteMap;
    static {
        spriteMap = new HashMap<Integer, HashMap<Vector2, String>>();

        HashMap<Vector2, String> idleMap = new HashMap<Vector2, String>();
        idleMap.put(Constants.LEFT, "gunter_idle_left");
        idleMap.put(Constants.RIGHT, "gunter_idle_right");
        idleMap.put(Constants.UP, "gunter_idle_back");
        idleMap.put(Constants.DOWN, "gunter_idle_front");
        spriteMap.put(STATE_IDLE, idleMap);

        HashMap<Vector2, String> slideMap = new HashMap<Vector2, String>();
        slideMap.put(Constants.LEFT, "gunter_slide_left");
        slideMap.put(Constants.RIGHT, "gunter_slide_right");
        slideMap.put(Constants.UP, "gunter_slide_back");
        slideMap.put(Constants.DOWN, "gunter_slide_front");
        spriteMap.put(STATE_MOVE, slideMap);
    }

    public Penguin(Skin skin) {
        super(skin.getDrawable("gunter_idle_front"));
        this.skin = skin;
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
        String drawableName = spriteMap.get(state == STATE_FINISH ? STATE_IDLE : state).get(direction);
        super.setDrawable(skin.getDrawable(drawableName));
    }

    @Override
    public void clear() {
        super.clear();
        listeners.clear();
    }
}
