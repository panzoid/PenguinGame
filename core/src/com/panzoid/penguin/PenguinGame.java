package com.panzoid.penguin;

import com.badlogic.gdx.Game;
import com.panzoid.penguin.screens.MenuScreen;

public class PenguinGame extends Game {

    private static final String TAG = PenguinGame.class.getSimpleName();
	
	@Override
	public void create () {
        setScreen(new MenuScreen(this));
	}
}
