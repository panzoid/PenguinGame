package com.panzoid.penguin;

import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by weipa on 6/1/16.
 */
public class PenguinUtils {

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
