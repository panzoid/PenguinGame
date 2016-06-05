package com.panzoid.penguin.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
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
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
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
    private final TiledMapRenderer tiledMapRenderer;
    private final OrthographicCamera camera;
    private final Stage stage;
    private final InputMultiplexer input;

    private final Skin skin;

    public final Penguin penguin;
    public final TiledMap tiledMap;

    public int moves;


    public GameScreen(Game game, TiledMap tiledMap) {
        super(game);
        this.tiledMap = tiledMap;
        batch = new SpriteBatch();
        camera = new OrthographicCamera(Constants.GAME_WIDTH, Constants.GAME_WIDTH * Gdx.graphics.getHeight() / Gdx.graphics.getWidth());
        batch.setProjectionMatrix(camera.combined);
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap, Constants.GAME_SCALE, batch);
        tiledMapRenderer.setView(camera);

        skin = new Skin(Gdx.files.internal("uiskin.json"));

        stage = new Stage(new FitViewport(camera.viewportWidth, camera.viewportHeight, camera), batch);
        penguin = new Penguin();
        penguin.addListener(new Penguin.PenguinListener() {
            @Override
            public void onStateChange(int state) {
                if(state == Penguin.STATE_FINISH) {
                    showScore();
                }
            }
        });
        stage.addActor(penguin);
        stage.addActor(new MoveButton(this, new Texture("sprites/arrow_left.png"), Constants.LEFT));
        stage.addActor(new MoveButton(this, new Texture("sprites/arrow_right.png"), Constants.RIGHT));
        stage.addActor(new MoveButton(this, new Texture("sprites/arrow_up.png"), Constants.UP));
        stage.addActor(new MoveButton(this, new Texture("sprites/arrow_down.png"), Constants.DOWN));

        input = new InputMultiplexer();
        input.addProcessor(stage);
        input.addProcessor(new InputAdapter() {
            private int x;
            private int y;

            @Override
            public boolean touchDown (int x, int y, int pointer, int button) {
                this.x = x;
                this.y = y;
                return true;
            }

            public boolean touchDragged (int x, int y, int pointer) {
                camera.position.x += (x - this.x) / 100f;
                camera.position.y += (y - this.y) / 100f;
                this.x = x;
                this.y = y;
                updateCamera();
                return true;
            }
        });
        Gdx.input.setInputProcessor(input);

        Vector2 startPosition = Utilities.getPosition(Constants.PROPERTY_START, tiledMap);
        penguin.setPosition(startPosition.x, startPosition.y);
        penguin.setState(Penguin.STATE_IDLE);
        penguin.setDirection(Constants.DOWN);
        camera.position.set(startPosition, camera.position.z);
        camera.update();

        moves = 0;
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        updateCamera();
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        tiledMapRenderer.setView(camera);
        tiledMapRenderer.render();
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    private void updateCamera() {
        if(penguin.getState() == Penguin.STATE_MOVE) {
            camera.position.x = penguin.getX();
            camera.position.y = penguin.getY();
        }

        camera.zoom = MathUtils.clamp(camera.zoom, 0.1f, 100/camera.viewportWidth);

        float effectiveViewportWidth = camera.viewportWidth * camera.zoom;
        float effectiveViewportHeight = camera.viewportHeight * camera.zoom;

        camera.position.x = MathUtils.clamp(camera.position.x, effectiveViewportWidth / 2f, ((TiledMapTileLayer) tiledMap.getLayers().get(0)).getWidth() - effectiveViewportWidth / 2f);
        camera.position.y = MathUtils.clamp(camera.position.y, effectiveViewportHeight / 2f, ((TiledMapTileLayer) tiledMap.getLayers().get(0)).getHeight() - effectiveViewportHeight / 2f);
    }

    private void showScore() {
        TextButton button = new TextButton("OK", skin, "default");
        //button.setScale(Constants.GAME_SCALE);
        button.setWidth(camera.viewportWidth / 2);
        button.setHeight(camera.viewportHeight / 2);
        button.setPosition(camera.viewportWidth / 4, camera.viewportHeight / 4);

        stage.addActor(button);
    }

    @Override
    public void dispose() {
        super.dispose();
        batch.dispose();
        tiledMap.dispose();
        stage.dispose();
    }
}
