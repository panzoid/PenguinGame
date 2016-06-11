package com.panzoid.penguin.actions;

import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.panzoid.penguin.Constants;
import com.panzoid.penguin.Utilities;
import com.panzoid.penguin.actors.Penguin;

/**
 * Created by weipa on 6/4/16.
 */
public class PenguinActions {

    public static final Action startMove = new Action() {
        float elapsedTime = 0f;

        @Override
        public boolean act(float delta) {
            elapsedTime += delta;
            if(elapsedTime <= 0.2f) return false;
            ((Penguin)getActor()).setState(Penguin.STATE_MOVE);
            elapsedTime = 0f;
            return true;
        }

        @Override
        public void setTarget(Actor target) {
        }
    };

    public static final Action stopMove = new Action() {
        @Override
        public boolean act(float delta) {
            ((Penguin)getActor()).setState(Penguin.STATE_IDLE);
            return true;
        }

        @Override
        public void setTarget(Actor target) {
        }
    };

    public static final Action setStateFinish = new Action() {
        @Override
        public boolean act(float delta) {
            ((Penguin)getActor()).setState(Penguin.STATE_FINISH);
            return true;
        }

        @Override
        public void setTarget(Actor target) {
        }
    };

    public static final Action setDirectionLeft = new Action() {
        @Override
        public boolean act(float delta) {
            ((Penguin)getActor()).setDirection(Constants.LEFT);
            return true;
        }

        @Override
        public void setTarget(Actor target) {
        }
    };

    public static final Action setDirectionRight = new Action() {
        @Override
        public boolean act(float delta) {
            ((Penguin)getActor()).setDirection(Constants.RIGHT);
            return true;
        }

        @Override
        public void setTarget(Actor target) {
        }
    };

    public static final Action setDirectionUp = new Action() {
        @Override
        public boolean act(float delta) {
            ((Penguin)getActor()).setDirection(Constants.UP);
            return true;
        }

        @Override
        public void setTarget(Actor target) {
        }
    };

    public static final Action setDirectionDown = new Action() {
        @Override
        public boolean act(float delta) {
            ((Penguin)getActor()).setDirection(Constants.DOWN);
            return true;
        }

        @Override
        public void setTarget(Actor target) {
        }
    };

    public static Action getAction(Vector2 position, Vector2 direction, TiledMap tiledMap) {
        TiledMapTileLayer layer = (TiledMapTileLayer)tiledMap.getLayers().get(0);

        int x = (int) position.x;
        int y = (int) position.y;

        if(position.equals(Utilities.getPosition(Constants.PROPERTY_FINISH, tiledMap))) {
            return null;
        }

        while ((x + direction.x) >= 0 && (x + direction.x) < layer.getWidth()
                && (y + direction.y) >= 0 && (y + direction.y) < layer.getHeight()) {
            x += direction.x;
            y += direction.y;
            TiledMapTileLayer.Cell cell = layer.getCell(x, y);
            MapProperties properties = cell.getTile().getProperties();

            if (properties.containsKey(Constants.FLAG)) {
                int flag = Integer.valueOf(properties.get(Constants.FLAG, String.class));
                if ((flag & Constants.PROPERTY_FINISH) != 0) {
                    return Actions.sequence(getMoveTo(position.x, position.y, x, y), setStateFinish);
                } else if ((flag & Constants.PROPERTY_BLOCK) != 0) {
                    return getMoveTo(position.x, position.y, x - direction.x, y - direction.y);
                } else if ((flag & Constants.PROPERTY_LEFT) != 0) {
                    SequenceAction sequence = Actions.sequence(getMoveTo(position.x, position.y, x, y));
                    Action nextAction = getAction(new Vector2(x, y), Constants.LEFT, tiledMap);
                    if(nextAction != null) {
                        sequence.addAction(setDirectionLeft);
                        sequence.addAction(nextAction);
                    }
                    return sequence;
                } else if ((flag & Constants.PROPERTY_RIGHT) != 0) {
                    SequenceAction sequence = Actions.sequence(getMoveTo(position.x, position.y, x, y));
                    Action nextAction = getAction(new Vector2(x, y), Constants.RIGHT, tiledMap);
                    if(nextAction != null) {
                        sequence.addAction(setDirectionRight);
                        sequence.addAction(nextAction);
                    }
                    return sequence;
                } else if ((flag & Constants.PROPERTY_UP) != 0) {
                    SequenceAction sequence = Actions.sequence(getMoveTo(position.x, position.y, x, y));
                    Action nextAction = getAction(new Vector2(x, y), Constants.UP, tiledMap);
                    if(nextAction != null) {
                        sequence.addAction(setDirectionUp);
                        sequence.addAction(nextAction);
                    }
                    return sequence;
                } else if ((flag & Constants.PROPERTY_DOWN) != 0) {
                    SequenceAction sequence = Actions.sequence(getMoveTo(position.x, position.y, x, y));
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
            float duration = Vector2.dst(startX, startY, endX, endY) * Constants.MOVE_SPEED;
            return Actions.moveTo(endX, endY, duration);
        }
    }
}
