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
                Action action = Utilities.getAction(new Vector2(penguin.getX(), penguin.getY()), direction, tiledMap);
                if(action != null) {
                    penguin.setDirection(direction);
                    penguin.addAction(Actions.sequence(
                            Utilities.startMove,
                            action,
                            Utilities.stopMove));
                }
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
        return Utilities.getAction(new Vector2(penguin.getX(), penguin.getY()), direction, tiledMap) != null;
    }
}
