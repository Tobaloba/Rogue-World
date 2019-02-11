package com.dat055.Controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.dat055.Model.Collision.CollisionHandler;
import com.dat055.Model.Entity.Entity;
import com.dat055.Model.Entity.Player;
import com.dat055.Model.GameModel;
import com.dat055.Model.Map.GameMap;
import com.dat055.View.GameView;


public class GameController extends Controller {
    private boolean isPaused;
    private Player currentPlayer;
    private Player player1;
    private Player player2;
    private Camera cam;
    private CollisionHandler handler1;
    private CollisionHandler handler2;

    private boolean isRotating = false;
    private float rotationTimer = 0;

    private boolean isDebug = false;

    public GameController(GameModel model, GameView view) {
        super(model, view);
        initialize();
    }

    public void initialize() {
        isPaused = false;

        player1 = ((GameModel)model).getPlayer1();
        player2 = ((GameModel)model).getPlayer2();
        currentPlayer = player1;
        cam = ((GameModel)model).getCam();
    }

    @Override
    public void update(float deltaTime) {
        checkKeyboardInput(); // Handles keyboard input

        // Tile rotation map transition
        if(isRotating) {
            rotationTimer+= 2f;
        }


        if(rotationTimer >= 180f) {
            isRotating = false;
            rotationTimer = 180f;
        }
        ((GameView)view).setRotationTimer(rotationTimer);

        // Camera transition
        float lerp = 2f;
        Vector2 playerPosition = currentPlayer.getPosition();
        Vector3 camPosition = cam.position;
        camPosition.x += (playerPosition.x - camPosition.x) * lerp * deltaTime;
        camPosition.y += (playerPosition.y - camPosition.y) * lerp * deltaTime;
        cam.update();

        if(!isPaused) {
            // Updates entities position, health etc.
            // This includes the players
            for(Entity entity : ((GameModel)model).entities) {
                entity.update();
            }

            handler1.checkCollision(player1);
            handler2.checkCollision(player2);

            //TODO: Other entities here

            if(isDebug)
                ((GameModel)model).getDebugCam().update();
        }
    }

    /**
     * Handles keyboard input for a specific player.
     */
    private void checkKeyboardInput() {
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            currentPlayer.move(-1);
            currentPlayer.setMoving(true);
        }
        else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            currentPlayer.move(1);
            currentPlayer.setMoving(true);
        } else {
            currentPlayer.setMoving(false);
            currentPlayer.move(0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT))
            currentPlayer.attack();

        if (Gdx.input.isKeyPressed(Input.Keys.SPACE))
            currentPlayer.jump();
        if (Gdx.input.isKeyPressed(Input.Keys.D))
            System.out.println(toString());

        if(Gdx.input.isKeyJustPressed(Input.Keys.T))
            toggleCurrentPlayer();

        if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE))
            togglePause();

        // Enter debug mode
        if(Gdx.input.isKeyJustPressed((Input.Keys.B)))
            toggleDebug();
    }

    /**
     * Calls gamemodel to create a game map for a specific map.
     * @param fileName of a json file in assets/maps/
     */
    public void startMap(String fileName) {
        ((GameModel)model).createMap(fileName, 64);
        GameMap map = ((GameModel)model).getGameMap();
        handler1 = new CollisionHandler(map.getFrontTileMap());
        handler2 = new CollisionHandler(map.getBackTileMap());

        //TODO: Set player spawn positions
    }

    //TODO: Create timer for rotating map
    private void toggleCurrentPlayer() {
        if(((GameModel)model).getMode() == GameModel.Mode.FRONT) {
            ((GameModel)model).setMode(GameModel.Mode.BACK);
            currentPlayer = player1;
            rotationTimer = 0;
        }
        else {
            ((GameModel)model).setMode(GameModel.Mode.FRONT);
            currentPlayer = player2;
            rotationTimer = 0;
        }
        isRotating = true; // Will start rotation timer

        if(isDebug)
            currentPlayer = ((GameModel)model).getDebugCam();
    }
    private void togglePause() {
        if(isPaused)
            isPaused = false;
        else
            isPaused = true;
    }
    private void toggleDebug() {
        if(((GameView)view).getShowRectangle()) {
            isDebug = false;
            ((GameView) view).setShowRectangle(false);
        }
        else {
            isDebug = true;
            ((GameView)view).setShowRectangle(true);
            currentPlayer = ((GameModel)model).getDebugCam();
        }
    }
}
