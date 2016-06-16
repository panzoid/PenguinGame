package com.panzoid.penguin.generator;

import java.io.IOException;

import static com.panzoid.penguin.generator.GeneratorUtils.doNextMove;
import static com.panzoid.penguin.generator.GeneratorUtils.getDefaultTiles;
import static com.panzoid.penguin.generator.GeneratorUtils.random;
import static com.panzoid.penguin.generator.GeneratorUtils.writeTmxFile;
import static com.panzoid.penguin.generator.TmxConstants.TILE_FINISH;
import static com.panzoid.penguin.generator.TmxConstants.TILE_START;
import static com.panzoid.penguin.generator.TmxConstants.TMX_HEIGHT;
import static com.panzoid.penguin.generator.TmxConstants.TMX_WIDTH;
import static com.panzoid.penguin.generator.TmxConstants.Vector2;

/**
 * Created by weipa on 6/14/16.
 */
public class LevelGenerator {

    public static void main(String[] args) throws IOException {
        int levelNumber = Integer.parseInt(args[0]);
        int minimumMoves = Integer.parseInt(args[1]);

        int[][] tiles = generateLevel(minimumMoves);
        writeTmxFile(levelNumber, minimumMoves, tiles);
    }

    private static int[][] generateLevel(int minimumMoves) {
        int[][] tiles = getDefaultTiles(TMX_WIDTH, TMX_HEIGHT);

        Vector2 currentPosition = new Vector2(random(1, TMX_WIDTH - 2), random(1, TMX_HEIGHT - 2));
        tiles[currentPosition.x][currentPosition.y] = TILE_START;
        Vector2 currentDirection = null;

        for(int i=0; i<minimumMoves; i++) {
            Vector2[] nextMove = doNextMove(currentPosition, currentDirection, tiles);

            currentPosition = nextMove[0];
            currentDirection = nextMove[1];
        }
        tiles[currentPosition.x][currentPosition.y] = TILE_FINISH;
        return tiles;
    }
}
