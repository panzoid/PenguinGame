package com.panzoid.penguin.generator;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

import static com.panzoid.penguin.generator.TmxConstants.DIRECTIONS;
import static com.panzoid.penguin.generator.TmxConstants.TILE_BLOCK;
import static com.panzoid.penguin.generator.TmxConstants.TILE_DEFAULT;
import static com.panzoid.penguin.generator.TmxConstants.TILE_START;
import static com.panzoid.penguin.generator.TmxConstants.TMX_FILE;
import static com.panzoid.penguin.generator.TmxConstants.TMX_FOOTER;
import static com.panzoid.penguin.generator.TmxConstants.TMX_HEADER;
import static com.panzoid.penguin.generator.TmxConstants.TMX_HEIGHT;
import static com.panzoid.penguin.generator.TmxConstants.TMX_WIDTH;
import static com.panzoid.penguin.generator.TmxConstants.Vector2;

/**
 * Created by weipa on 6/15/16.
 */
public class GeneratorUtils {

    private static Random random = new Random();

    public static int random(int min, int max) {
        if(min > max) {
            return random.nextInt((min - max) + 1) + max;
        }
        return random.nextInt((max - min) + 1) + min;
    }

    public static int random(int max) {
        return random(0, max);
    }

    public static int clamp(int value, int min, int max) {
        if(value < min) {
            return min;
        } else if(value > max) {
            return max;
        } else {
            return value;
        }
    }

    public static int[][] getDefaultTiles(int width, int height) {
        int[][] tiles = new int[width][height];

        for(int x=0; x<width; x++) {
            for(int y=0; y<height; y++) {
                tiles[x][y] = TILE_DEFAULT;
            }
        }

        return tiles;
    }

    public static Vector2 getRandomDirection(Vector2 excluded) {
        Vector2 result = null;
        while(result == null) {
            result = DIRECTIONS[random(DIRECTIONS.length - 1)];
            if(excluded != null && (result.hasSameDirection(excluded) || result.hasOppositeDirection(excluded))) {
                result = null;
            }
        }
        return result.cpy();
    }

    public static Vector2[] doNextMove(Vector2 currentPosition, Vector2 currentDirection, int[][] tiles) {

        Vector2 nextPosition;
        Vector2 nextDirection;

        do {
            nextDirection = getRandomDirection(currentDirection);
            nextPosition = currentPosition.cpy();

            while (nextPosition.x > 0 && nextPosition.x < TMX_WIDTH - 1 && nextPosition.y > 0 && nextPosition.y < TMX_HEIGHT - 1) {
                if (tiles[nextPosition.x + nextDirection.x][nextPosition.y + nextDirection.y] == TILE_BLOCK) {
                    return new Vector2[]{nextPosition, nextDirection};
                }
                nextPosition.add(nextDirection);
                if(tiles[nextPosition.x][nextPosition.y] == TILE_START) {
                    nextPosition = currentPosition.cpy();
                }
            }
            nextPosition.x = clamp(nextPosition.x, 1, TMX_WIDTH - 2);
            nextPosition.y = clamp(nextPosition.y, 1, TMX_HEIGHT - 2);
            //System.out.println("Current: "+currentPosition+", next: "+nextPosition);
        } while(currentPosition.equals(nextPosition));

        nextPosition = new Vector2(
                random(currentPosition.x + nextDirection.x, nextPosition.x),
                random(currentPosition.y + nextDirection.y, nextPosition.y));

        tiles[nextPosition.x + nextDirection.x][nextPosition.y + nextDirection.y] = TILE_BLOCK;

        return new Vector2[]{nextPosition, nextDirection};
    }

    public static void writeTmxFile(int levelNumber, int minimumMoves, int[][] tiles) throws IOException {
        int maximumMoves = minimumMoves + minimumMoves / 2;
        String fileName = String.format(TMX_FILE, levelNumber, minimumMoves, maximumMoves);
        PrintWriter writer = new PrintWriter(fileName, "UTF-8");
        writer.print(String.format(TMX_HEADER, TMX_WIDTH, TMX_HEIGHT, TMX_WIDTH, TMX_HEIGHT));

        StringBuilder builder = new StringBuilder();
        for(int y=0; y<TMX_HEIGHT; y++) {
            for(int x=0; x<TMX_WIDTH; x++) {
                builder.append(tiles[x][y]).append(",");
            }
        }
        builder.deleteCharAt(builder.length()-1); //Delete the last comma

        writer.print(builder.toString());
        writer.print(TMX_FOOTER);
        writer.close();
    }
}
