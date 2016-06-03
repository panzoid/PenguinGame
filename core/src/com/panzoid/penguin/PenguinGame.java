package com.panzoid.penguin;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.panzoid.penguin.screens.GameScreen;

public class PenguinGame extends Game {

    private static final String TAG = PenguinGame.class.getSimpleName();
	
	@Override
	public void create () {
        setScreen(new GameScreen(this, new TmxMapLoader().load("tilemaps/2.tmx")));
	}
}
