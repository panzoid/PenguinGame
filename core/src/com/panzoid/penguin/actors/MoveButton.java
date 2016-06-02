package com.panzoid.penguin.actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.panzoid.penguin.Constants;
import com.panzoid.penguin.Utilities;

/**
 * Created by weipa on 6/1/16.
 */
public class MoveButton extends Image implements Penguin.PenguinStateListener {
    private final Penguin penguin;
    private final Vector2 direction;
    private final TiledMap tiledMap;

    private final Action startMove = new Action() {
        @Override
        public boolean act(float delta) {
            penguin.setDirection(direction);
            penguin.setState(Penguin.STATE_MOVE);
            return true;
        }
    };

    private final Action stopMove = new Action() {
        @Override
        public boolean act(float delta) {
            penguin.setState(Penguin.STATE_IDLE);
            return true;
        }
    };

    public MoveButton(final Texture texture, final Penguin penguin, final Vector2 direction, final TiledMap map) {
        super(texture);
        setWidth(getWidth() / Constants.GAME_UNIT);
        setHeight(getHeight() / Constants.GAME_UNIT);
        setX(penguin.getX() + direction.x);
        setY(penguin.getY() + direction.y);

        this.penguin = penguin;
        this.direction = direction;
        this.tiledMap = map;

        addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                Vector2 moveableTo = Utilities.getMoveableTo(new Vector2(penguin.getX(), penguin.getY()), direction, tiledMap);
                // 10 units/second
                float moveSpeed = moveableTo.dst(penguin.getX(), penguin.getY()) / 10f;
                penguin.addAction(Actions.sequence(
                        startMove,
                        Actions.moveTo(moveableTo.x, moveableTo.y, moveSpeed),
                        stopMove));
                return true;
            }
        });

        penguin.addListener(this);
    }


    @Override
    public void onStateChange(int state) {
        setX(penguin.getX() + direction.x);
        setY(penguin.getY() + direction.y);
        setVisible(state == Penguin.STATE_IDLE && canMove());
    }

    private boolean canMove() {
        return Utilities.getMoveableTo(new Vector2(penguin.getX(), penguin.getY()), direction, tiledMap).dst(penguin.getX(), penguin.getY()) >= 1f;
    }
}
