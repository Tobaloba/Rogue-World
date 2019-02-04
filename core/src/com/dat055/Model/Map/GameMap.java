package com.dat055.Model.Map;

// Creates two TileMaps: one front and one back
public class GameMap {
    private TileMap front;
    private TileMap back;

    public GameMap(String mapName) {
        //TODO: read map from JSON file

        TileFactory tileFactory = new TileFactory();
        front = tileFactory.getTileMap();
        back = tileFactory.getTileMap();
    }
}
