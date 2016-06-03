package com.panzoid.penguin;

import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.panzoid.penguin.actors.Penguin;

/**
 * Created by weipa on 6/1/16.
 */
public class Utilities {

    public static final Action startMove = new Action() {
        float elapsedTime = 0f;

        @Override
        public boolean act(float delta) {
            elapsedTime += delta;
            if(elapsedTime <= 0.2f) return false;
            Penguin.getInstance().setState(Penguin.STATE_MOVE);
            elapsedTime = 0f;
            return true;
        }
    };

    public static final Action stopMove = new Action() {
        @Override
        public boolean act(float delta) {
            Penguin.getInstance().setState(Penguin.STATE_IDLE);
            return true;
        }
    };

    public static final Action setDirectionLeft = new Action() {
        @Override
        public boolean act(float delta) {
            Penguin.getInstance().setDirection(Constants.LEFT);
            return true;
        }
    };

    public static final Action setDirectionRight = new Action() {
        @Override
        public boolean act(float delta) {
            Penguin.getInstance().setDirection(Constants.RIGHT);
            return true;
        }
    };

    public static final Action setDirectionUp = new Action() {
        @Override
        public boolean act(float delta) {
            Penguin.getInstance().setDirection(Constants.UP);
            return true;
        }
    };

    public static final Action setDirectionDown = new Action() {
        @Override
        public boolean act(float delta) {
            Penguin.getInstance().setDirection(Constants.DOWN);
            return true;
        }
    };

    public static Action getAction(Vector2 position, Vector2 direction, TiledMap tiledMap) {
        TiledMapTileLayer layer = (TiledMapTileLayer)tiledMap.getLayers().get(0);

        int x = (int) position.x;
        int y = (int) position.y;

        while ((x + direction.x) >= 0 && (x + direction.x) < layer.getWidth()
                && (y + direction.y) >= 0 && (y + direction.y) < layer.getHeight()) {
            x += direction.x;
            y += direction.y;
            TiledMapTileLayer.Cell cell = layer.getCell(x, y);
            MapProperties properties = cell.getTile().getProperties();

            if (properties.containsKey(Constants.FLAG)) {
                int flag = Integer.valueOf(properties.get(Constants.FLAG, String.class));
                if ((flag & Constants.PROPERTY_FINISH) != 0) {
                    return getMoveTo(position.x, position.y, x, y);
                } else if ((flag & Constants.PROPERTY_BLOCK) != 0) {
                    return getMoveTo(position.x, position.y, x - direction.x, y - direction.y);
                } else if ((flag & Constants.PROPERTY_LEFT) != 0) {
                    SequenceAction sequence = new SequenceAction(getMoveTo(position.x, position.y, x, y));
                    Action nextAction = getAction(new Vector2(x, y), Constants.LEFT, tiledMap);
                    if(nextAction != null) {
                        sequence.addAction(setDirectionLeft);
                        sequence.addAction(nextAction);
                    }
                    return sequence;
                } else if ((flag & Constants.PROPERTY_RIGHT) != 0) {
                    SequenceAction sequence = new SequenceAction(getMoveTo(position.x, position.y, x, y));
                    Action nextAction = getAction(new Vector2(x, y), Constants.RIGHT, tiledMap);
                    if(nextAction != null) {
                        sequence.addAction(setDirectionRight);
                        sequence.addAction(nextAction);
                    }
                    return sequence;
                } else if ((flag & Constants.PROPERTY_UP) != 0) {
                    SequenceAction sequence = new SequenceAction(getMoveTo(position.x, position.y, x, y));
                    Action nextAction = getAction(new Vector2(x, y), Constants.UP, tiledMap);
                    if(nextAction != null) {
                        sequence.addAction(setDirectionUp);
                        sequence.addAction(nextAction);
                    }
                    return sequence;
                } else if ((flag & Constants.PROPERTY_DOWN) != 0) {
                    SequenceAction sequence = new SequenceAction(getMoveTo(position.x, position.y, x, y));
                    Action nextAction = getAction(new Vector2(x, y), Constants.DOWN, tiledMap);
                    if(nextAction != null) {
                        sequence.addAction(setDirectionDown);
                        sequence.addAction(nextAction);
                    }
                    return sequence;
                }
            }
        }
        return getMoveTo(position.x, position.y, x, y);
    }

    private static Action getMoveTo(float startX, float startY, float endX, float endY) {
        if(startX == endX && startY == endY) {
            return null;
        } else {
            float moveSpeed = Vector2.dst(startX, startY, endX, endY) * Constants.MOVE_SPEED;
            return Actions.moveTo(endX, endY, moveSpeed);
        }
    }

    public static Vector2 getPosition(int flag, TiledMap tiledMap) {
        TiledMapTileLayer layer = (TiledMapTileLayer)tiledMap.getLayers().get(0);

        for(int x=0; x<layer.getWidth(); x++) {
            for(int y=0; y<layer.getHeight(); y++) {
                TiledMapTileLayer.Cell cell = layer.getCell(x, y);
                MapProperties properties = cell.getTile().getProperties();

                if(properties.containsKey(Constants.FLAG)
                        && (Integer.valueOf(properties.get(Constants.FLAG, String.class)) & flag) != 0) {
                    return new Vector2(x, y);
                }
            }
        }
        return null;
    }
}
