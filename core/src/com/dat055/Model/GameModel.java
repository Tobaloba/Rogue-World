package com.dat055.Model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.dat055.Model.Entity.Player;
import com.dat055.Model.Map.GameMap;

public class GameModel extends Model {
    // Determines what character the player is controlling (single player)
    public enum Mode {
        Front, Back
    }

    public GameMap map;
    public Player player;
    public OrthographicCamera cam;

    /**
     * Crea
     * @param fileName
     */
    public void createMap(String fileName) {
        map = new GameMap(fileName);
        Mode mode = Mode.Front;
        cam = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        player = new Player(1, 80, 64, "red_penguin_64x80.png",
                "Towbie", 5, 8);
    }

    public void update() {
        float deltaTime = Gdx.graphics.getDeltaTime();

        // Camera transition
        float lerp = 2f;
        Vector2 playerPosition = player.getVector2();
        Vector3 camPosition = cam.position;
        camPosition.x += (playerPosition.x - camPosition.x) * lerp * deltaTime;
        camPosition.y += (playerPosition.y - playerPosition.y) * lerp * deltaTime;
        cam.update();

        //Todo: collisionhandler here

        // Player logic
        player.checkKeyboardInput();
        player.update();

        //TODO: Other entities here

    }
}
