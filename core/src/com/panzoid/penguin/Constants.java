package com.panzoid.penguin;

import com.badlogic.gdx.math.Vector2;

/**
 * Created by weipa on 5/29/16.
 */
public class Constants {

    public static final float GAME_WIDTH = 15f;
    public static final float GAME_UNIT = 16f;
    public static final float MOVE_SPEED = 1 / 10f;

    public static final Vector2 LEFT = new Vector2(-1f, 0f);
    public static final Vector2 RIGHT = new Vector2(1f, 0f);
    public static final Vector2 UP = new Vector2(0f, 1f);
    public static final Vector2 DOWN = new Vector2(0f, -1f);

    public static final String FLAG = "flag";
    public static final int PROPERTY_START = 1;
    public static final int PROPERTY_FINISH = 2;
    public static final int PROPERTY_BLOCK = 4;
    public static final int PROPERTY_LEFT = 8;
    public static final int PROPERTY_RIGHT = 16;
    public static final int PROPERTY_UP = 32;
    public static final int PROPERTY_DOWN = 64;

}
