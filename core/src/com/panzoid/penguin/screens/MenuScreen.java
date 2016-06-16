package com.panzoid.penguin.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.panzoid.penguin.PenguinGame;
import com.panzoid.penguin.PenguinLevel;

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
        PenguinLevel[] levels = new PenguinLevel[files.length];
        for(int i=0; i<files.length; i++) {
            levels[i] = new PenguinLevel(files[i]);
        }

        sortLevels(levels);
        for(final PenguinLevel level : levels) {
            TextButton button = new TextButton("Level "+level.getNumber() + ", Moves " + level.getHighScore(), game.getUI());
            button.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    setGameScreen(level);
                }
            });
            verticalGroup.addActor(button);
        }

        ScrollPane scrollPane = new ScrollPane(verticalGroup, game.getUI());
        scrollPane.setScale(stage.getWidth() / scrollPane.getWidth() / 2, stage.getHeight() / scrollPane.getHeight());
        stage.addActor(scrollPane);

        Gdx.input.setInputProcessor(stage);
        Gdx.input.setCatchBackKey(false);
    }



    private void setGameScreen(PenguinLevel level) {
        game.setScreen(new GameScreen(game, level));
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

    private void sortLevels(PenguinLevel[] levels) {
        Arrays.sort(levels, new Comparator<PenguinLevel>() {
            @Override
            public int compare(PenguinLevel o1, PenguinLevel o2) {
                return new Integer(o1.getNumber()).compareTo(o2.getNumber());
            }
        });
    }

    @Override
    public void dispose() {
        super.dispose();
        stage.dispose();
    }
}
