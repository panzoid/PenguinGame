package com.panzoid.penguin.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.util.Arrays;
import java.util.Comparator;

/**
 * Created by weipa on 6/1/16.
 */
public class MenuScreen extends PenguinScreen {
    private final Stage stage;
    private final Skin skin;

    public MenuScreen(final Game game) {
        super(game);
        stage = new Stage(new ScreenViewport());
        skin = new Skin(Gdx.files.internal("uiskin.json"));
        FileHandle folder = Gdx.files.internal("tilemaps");

        VerticalGroup verticalGroup = new VerticalGroup();
        verticalGroup.center();
        FileHandle[] files = folder.list();
        sortLevels(files);
        for(final FileHandle file : files) {
            TextButton button = new TextButton("Level "+file.nameWithoutExtension(), skin);
            button.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    game.setScreen(new GameScreen(game, new TmxMapLoader().load("tilemaps/"+file.name())));
                }
            });
            verticalGroup.addActor(button);
        }

        ScrollPane scrollPane = new ScrollPane(verticalGroup, skin);
        scrollPane.setScale(stage.getWidth() / scrollPane.getWidth(), stage.getHeight() / scrollPane.getHeight());
        stage.addActor(scrollPane);

        Gdx.input.setInputProcessor(stage);
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
        skin.dispose();
    }
}
