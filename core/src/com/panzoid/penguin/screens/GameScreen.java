package com.panzoid.penguin.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.panzoid.penguin.Constants;
import com.panzoid.penguin.actors.Penguin;
import com.panzoid.penguin.Utilities;
import com.panzoid.penguin.actors.MoveButton;

/**
 * Created by weipa on 6/1/16.
 */
public class GameScreen extends PenguinScreen {

    private final SpriteBatch batch;
    private final TiledMap tiledMap;
    private final TiledMapRenderer tiledMapRenderer;
    private final OrthographicCamera camera;
    private final Stage stage;
    private final InputMultiplexer input = new InputMultiplexer();

    Penguin penguin;

    public GameScreen(Game game, TiledMap tiledMap) {
        super(game);
        this.tiledMap = tiledMap;
        batch = new SpriteBatch();

        camera = new OrthographicCamera(Constants.GAME_WIDTH, Constants.GAME_WIDTH * Gdx.graphics.getHeight() / Gdx.graphics.getWidth());
        batch.setProjectionMatrix(camera.combined);
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap, 1 / Constants.GAME_UNIT, batch);
        tiledMapRenderer.setView(camera);

        stage = new Stage(new FitViewport(camera.viewportWidth, camera.viewportHeight, camera), batch);
        penguin = Penguin.getInstance();
        stage.addActor(penguin);
        stage.addActor(new MoveButton(new Texture("sprites/arrow_left.png"), penguin, Constants.LEFT, tiledMap));
        stage.addActor(new MoveButton(new Texture("sprites/arrow_right.png"), penguin, Constants.RIGHT, tiledMap));
        stage.addActor(new MoveButton(new Texture("sprites/arrow_up.png"), penguin, Constants.UP, tiledMap));
        stage.addActor(new MoveButton(new Texture("sprites/arrow_down.png"), penguin, Constants.DOWN, tiledMap));

        input.addProcessor(stage);
        Gdx.input.setInputProcessor(input);

        Vector2 startPosition = Utilities.getPosition(Constants.PROPERTY_START, tiledMap);
        penguin.setPosition(startPosition.x, startPosition.y);
        penguin.setState(Penguin.STATE_IDLE);
        camera.position.set(startPosition, camera.position.z);
        camera.update();
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        handleInput();
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        tiledMapRenderer.setView(camera);
        tiledMapRenderer.render();
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    private void handleInput() {
        if(penguin.getState() == Penguin.STATE_MOVE) {
            camera.position.x = penguin.getX();
            camera.position.y = penguin.getY();
        } else {
            if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT) || Gdx.input.isKeyJustPressed(Input.Keys.A)) {
                camera.position.x -= 1f;
            }
            if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT) || Gdx.input.isKeyJustPressed(Input.Keys.D)) {
                camera.position.x += 1f;
            }
            if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN) || Gdx.input.isKeyJustPressed(Input.Keys.S)) {
                camera.position.y -= 1f;
            }
            if (Gdx.input.isKeyJustPressed(Input.Keys.UP) || Gdx.input.isKeyJustPressed(Input.Keys.W)) {
                camera.position.y += 1f;
            }
        }

        camera.zoom = MathUtils.clamp(camera.zoom, 0.1f, 100/camera.viewportWidth);

        float effectiveViewportWidth = camera.viewportWidth * camera.zoom;
        float effectiveViewportHeight = camera.viewportHeight * camera.zoom;

        camera.position.x = MathUtils.clamp(camera.position.x, effectiveViewportWidth / 2f, ((TiledMapTileLayer) tiledMap.getLayers().get(0)).getWidth() - effectiveViewportWidth / 2f);
        camera.position.y = MathUtils.clamp(camera.position.y, effectiveViewportHeight / 2f, ((TiledMapTileLayer) tiledMap.getLayers().get(0)).getHeight() - effectiveViewportHeight / 2f);
    }

    @Override
    public void dispose() {
        super.dispose();
        batch.dispose();
        tiledMap.dispose();
        stage.dispose();
    }
}
