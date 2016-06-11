package com.panzoid.penguin.actors;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.panzoid.penguin.Constants;
import com.panzoid.penguin.actions.PenguinActions;
import com.panzoid.penguin.screens.GameScreen;

/**
 * Created by weipa on 6/1/16.
 */
public class MoveButton extends Image implements Penguin.PenguinListener {

    private final Vector2 direction;
    private final GameScreen gameScreen;

    public MoveButton(GameScreen gameScreen, Drawable drawable, Vector2 direction) {
        super(drawable);
        this.gameScreen = gameScreen;
        this.direction = direction;

        setScale(Constants.GAME_SCALE);
        setX(gameScreen.penguin.getX() + direction.x);
        setY(gameScreen.penguin.getY() + direction.y);

        addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return touched();
            }
        });

        this.gameScreen.penguin.addListener(this);
    }

    private boolean touched() {
        Action action = PenguinActions.getAction(new Vector2(gameScreen.penguin.getX(), gameScreen.penguin.getY()), direction, gameScreen.tiledMap);
        if(action != null) {
            gameScreen.moves++;
            gameScreen.penguin.setDirection(direction);
            gameScreen.penguin.addAction(Actions.sequence(
                    PenguinActions.startMove,
                    action,
                    PenguinActions.stopMove));
        }
        return true;
    }

    @Override
    public void onStateChange(int state) {
        if(state == Penguin.STATE_IDLE && canMove()) {
            setX(gameScreen.penguin.getX() + direction.x);
            setY(gameScreen.penguin.getY() + direction.y);
            setVisible(true);
        } else {
            setVisible(false);
        }
    }

    private boolean canMove() {
        return PenguinActions.getAction(new Vector2(gameScreen.penguin.getX(), gameScreen.penguin.getY()), direction, gameScreen.tiledMap) != null;
    }
}
