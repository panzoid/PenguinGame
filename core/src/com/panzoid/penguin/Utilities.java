package com.panzoid.penguin;

import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by weipa on 6/1/16.
 */
public class Utilities {

    public static Vector2 getMoveableTo(Vector2 position, Vector2 direction, TiledMap tiledMap) {
        TiledMapTileLayer layer = (TiledMapTileLayer)tiledMap.getLayers().get(0);

        int x = (int) position.x;
        int y = (int) position.y;

        while ((x + direction.x) >= 0 && (x + direction.x) < layer.getWidth()
                && (y + direction.y) >= 0 && (y + direction.y) < layer.getHeight()) {
            x += direction.x;
            y += direction.y;
            TiledMapTileLayer.Cell cell = layer.getCell(x, y);
            MapProperties properties = cell.getTile().getProperties();

            if (properties.containsKey(Constants.FLAG)) {
                int flag = Integer.valueOf(properties.get(Constants.FLAG, String.class));
                if ((flag & Constants.PROPERTY_FINISH) != 0) {
                    return new Vector2(x, y);
                } else if ((flag & Constants.PROPERTY_BLOCK) != 0) {
                    return new Vector2(x - direction.x, y - direction.y);
                }
            }
        }

        return new Vector2(x, y);
    }

    public static Vector2 getPosition(int flag, TiledMap tiledMap) {
        TiledMapTileLayer layer = (TiledMapTileLayer)tiledMap.getLayers().get(0);

        for(int x=0; x<layer.getWidth(); x++) {
            for(int y=0; y<layer.getHeight(); y++) {
                TiledMapTileLayer.Cell cell = layer.getCell(x, y);
                MapProperties properties = cell.getTile().getProperties();

                if(properties.containsKey(Constants.FLAG)
                        && (Integer.valueOf(properties.get(Constants.FLAG, String.class)) & flag) != 0) {
                    return new Vector2(x, y);
                }
            }
        }
        return null;
    }
}
