package com.panzoid.penguin;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.panzoid.penguin.screens.MenuScreen;

public class PenguinGame extends Game {

    private static final String TAG = PenguinGame.class.getSimpleName();
	private static final String SPRITES_ATLAS = "sprites/pack.atlas";
	private static final String UI_SKIN = "uiskin.json";

	private AssetManager manager;
	private Skin sprites;
    private boolean loadedOnce = false;
	
	@Override
	public void create () {
		manager = new AssetManager();
		manager.load(SPRITES_ATLAS, TextureAtlas.class);
		manager.load(UI_SKIN, Skin.class);

        // set Loading Screen here
	}

	public Skin getSprites() {
		return sprites;
	}

	public Skin getUI() {
		return manager.get(UI_SKIN, Skin.class);
	}

	public AssetManager getAssetManager() {
		return manager;
	}

	@Override
	public void render() {
		super.render();

		if(manager.update() && !loadedOnce) {
            loadedOnce = true;
            Gdx.app.log(TAG, "Done loading assets");
            sprites = new Skin();
			sprites.addRegions(manager.get(SPRITES_ATLAS, TextureAtlas.class));
			setScreen(new MenuScreen(this));
		}
	}

	@Override
	public void dispose() {
		super.dispose();
		manager.dispose();
	}
}
