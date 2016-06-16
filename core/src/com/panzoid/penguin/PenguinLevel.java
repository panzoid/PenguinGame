package com.panzoid.penguin;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;

/**
 * Created by weipa on 6/13/16.
 */
public class PenguinLevel {

    private final FileHandle file;
    private final int number;
    private final int minimumMoves;
    private final int maximumMoves;
    private int highScore;
    private final Preferences prefs;
    private TiledMap tiledMap;

    public PenguinLevel(FileHandle file) {
        this.file = file;

        String fileName = file.nameWithoutExtension();
        prefs = Gdx.app.getPreferences(fileName);
        highScore = prefs.getInteger("highscore", 0);
        String[] fileParts = fileName.split("\\.");
        number = Integer.parseInt(fileParts[0]);
        minimumMoves = Integer.parseInt(fileParts[1]);
        maximumMoves = Integer.parseInt(fileParts[2]);
    }

    public int getNumber() {
        return number;
    }

    /**
     * Gets the rating for the given moves.
     *
     * @return A rating of 0-3 depending on the moves used.
     */
    public int getStarRating(int moves) {
        double step = (maximumMoves - minimumMoves) / 2d;
        if(moves <= minimumMoves) {
            return 3;
        } else if(moves <= minimumMoves + step) {
            return 2;
        } else if(moves <= minimumMoves + step + step) {
            return 1;
        } else {
            return 0;
        }
    }

    public int getHighScore() {
        return highScore;
    }

    public void setHighScore(int score) {
        if(highScore <= 0 || score < highScore) {
            highScore = score;
            prefs.putInteger("highscore", highScore);
            prefs.flush();
        }
    }

    public TiledMap getTiledMap() {
        if(tiledMap == null) {
            tiledMap = new TmxMapLoader().load("tilemaps/"+file.name());
        }
        return tiledMap;
    }
}
