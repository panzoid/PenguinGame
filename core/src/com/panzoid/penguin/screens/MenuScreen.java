package com.panzoid.penguin.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.panzoid.penguin.PenguinGame;

import java.util.Arrays;
import java.util.Comparator;

/**
 * Created by weipa on 6/1/16.
 */
public class MenuScreen extends PenguinScreen {
    private final Stage stage;

    public MenuScreen(final PenguinGame game) {
        super(game);
        stage = new Stage(new ScreenViewport());
        FileHandle folder = Gdx.files.internal("tilemaps");

        VerticalGroup verticalGroup = new VerticalGroup();
        verticalGroup.center();
        FileHandle[] files = folder.list();
        sortLevels(files);
        for(final FileHandle file : files) {
            TextButton button = new TextButton("Level "+file.nameWithoutExtension(), game.getUI());
            button.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    Gdx.app.log("WEI", "Selected level " + file.nameWithoutExtension());
                    setGameScreen(new TmxMapLoader().load("tilemaps/"+file.name()));
                }
            });
            verticalGroup.addActor(button);
        }

        ScrollPane scrollPane = new ScrollPane(verticalGroup, game.getUI());
        scrollPane.setScale(stage.getWidth() / scrollPane.getWidth(), stage.getHeight() / scrollPane.getHeight());
        stage.addActor(scrollPane);

        Gdx.input.setInputProcessor(stage);
    }

    private void setGameScreen(TiledMap map) {
        game.setScreen(new GameScreen(game, map));
        dispose();
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();
    }

    private void sortLevels(FileHandle[] in) {
        Arrays.sort(in, new Comparator<FileHandle>() {
            @Override
            public int compare(FileHandle o1, FileHandle o2) {
                return new Integer(Integer.parseInt(o1.nameWithoutExtension())).compareTo(Integer.parseInt(o2.nameWithoutExtension()));
            }
        });
    }

    @Override
    public void dispose() {
        super.dispose();
        stage.dispose();
    }
}
