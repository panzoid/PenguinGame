package com.panzoid.penguin.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.panzoid.penguin.Constants;
import com.panzoid.penguin.PenguinGame;
import com.panzoid.penguin.PenguinLevel;
import com.panzoid.penguin.PenguinUtils;
import com.panzoid.penguin.actors.MoveButton;
import com.panzoid.penguin.actors.Penguin;

/**
 * Created by weipa on 6/1/16.
 */
public class GameScreen extends PenguinScreen {

    private final SpriteBatch batch;
    private final TiledMapRenderer tiledMapRenderer;
    private final InputMultiplexer input;
    private final OrthographicCamera camera;
    private final Stage stage;

    private final OrthographicCamera uiCamera;
    private final Stage uiStage;

    public final Penguin penguin;
    public final PenguinLevel level;
    public int moves;


    public GameScreen(PenguinGame game, PenguinLevel level) {
        super(game);
        this.level = level;
        batch = new SpriteBatch();
        camera = new OrthographicCamera(Constants.GAME_WIDTH, Constants.GAME_WIDTH * Gdx.graphics.getHeight() / Gdx.graphics.getWidth());
        batch.setProjectionMatrix(camera.combined);
        tiledMapRenderer = new OrthogonalTiledMapRenderer(level.getTiledMap(), Constants.GAME_SCALE, batch);
        tiledMapRenderer.setView(camera);

        uiCamera = new OrthographicCamera(camera.viewportWidth / Constants.GAME_SCALE, camera.viewportHeight / Constants.GAME_SCALE);
        uiStage = new Stage(new FitViewport(uiCamera.viewportWidth, uiCamera.viewportHeight, uiCamera));

        stage = new Stage(new FitViewport(camera.viewportWidth, camera.viewportHeight, camera), batch);
        penguin = new Penguin(game.getSprites());
        penguin.addListener(new Penguin.PenguinListener() {
            @Override
            public void onStateChange(int state) {
                if(state == Penguin.STATE_FINISH) {
                    showScore();
                }
            }
        });
        stage.addActor(penguin);
        stage.addActor(new MoveButton(this, game.getSprites().getDrawable("arrow_left"), Constants.LEFT));
        stage.addActor(new MoveButton(this, game.getSprites().getDrawable("arrow_right"), Constants.RIGHT));
        stage.addActor(new MoveButton(this, game.getSprites().getDrawable("arrow_up"), Constants.UP));
        stage.addActor(new MoveButton(this, game.getSprites().getDrawable("arrow_down"), Constants.DOWN));

        input = new InputMultiplexer();
        input.addProcessor(uiStage);
        input.addProcessor(stage);
        /*
        input.addProcessor(new GestureDetector(new GestureDetector.GestureAdapter() {
            @Override
            public boolean zoom (float initialDistance, float distance) {
                camera.zoom += distance / 100f;
                return false;
            }
        }));
        */
        input.addProcessor(new InputAdapter() {
            private int x;
            private int y;

            @Override
            public boolean touchDown (int x, int y, int pointer, int button) {
                this.x = x;
                this.y = y;
                return true;
            }

            @Override
            public boolean touchDragged (int x, int y, int pointer) {
                camera.position.x += (x - this.x) / 100f;
                camera.position.y += (y - this.y) / 100f;
                this.x = x;
                this.y = y;
                updateCamera();
                return true;
            }

            @Override
            public boolean keyDown (int keycode) {
                if(keycode == Input.Keys.BACK) {
                    onBack();
                    return true;
                }
                return false;
            }
        });
        Gdx.input.setInputProcessor(input);
        Gdx.input.setCatchBackKey(true);

        reset();
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        updateCamera();
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        tiledMapRenderer.setView(camera);
        tiledMapRenderer.render();
        stage.act(delta);
        stage.getViewport().apply();
        stage.draw();
        uiStage.act(delta);
        uiStage.getViewport().apply();
        uiStage.draw();
    }

    private void updateCamera() {
        if(penguin.getState() == Penguin.STATE_MOVE) {
            camera.position.x = penguin.getX();
            camera.position.y = penguin.getY();
        }

        //camera.zoom = MathUtils.clamp(camera.zoom, 0.5f, camera.viewportWidth/camera.viewportHeight);

        float effectiveViewportWidth = camera.viewportWidth * camera.zoom;
        float effectiveViewportHeight = camera.viewportHeight * camera.zoom;

        camera.position.x = MathUtils.clamp(camera.position.x, effectiveViewportWidth / 2f, ((TiledMapTileLayer) level.getTiledMap().getLayers().get(0)).getWidth() - effectiveViewportWidth / 2f);
        camera.position.y = MathUtils.clamp(camera.position.y, effectiveViewportHeight / 2f, ((TiledMapTileLayer) level.getTiledMap().getLayers().get(0)).getHeight() - effectiveViewportHeight / 2f);
    }

    private void reset() {
        penguin.clearActions();
        Vector2 startPosition = PenguinUtils.getPosition(Constants.PROPERTY_START, level.getTiledMap());
        penguin.setPosition(startPosition.x, startPosition.y);
        penguin.setState(Penguin.STATE_IDLE);
        penguin.setDirection(Constants.DOWN);
        camera.position.set(startPosition, camera.position.z);
        camera.update();
        moves = 0;
    }

    private void onBack() {
        if(moves == 0) {
            setMenuScreen();
        } else {
            reset();
        }
    }

    private void showScore() {
        level.setHighScore(moves);

        HorizontalGroup horizontalGroup = new HorizontalGroup();
        int starRating = level.getStarRating(moves);
        for(int i=0; i<3; i++) {
            if(i < starRating) {
                horizontalGroup.addActor(new Image(game.getSprites().getDrawable("star_yellow")));
            } else {
                horizontalGroup.addActor(new Image(game.getSprites().getDrawable("star_blank")));
            }
        }

        TextButton button = new TextButton("OK", game.getUI());
        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                setMenuScreen();
            }
        });

        VerticalGroup verticalGroup = new VerticalGroup();
        verticalGroup.addActor(horizontalGroup);
        verticalGroup.addActor(button);
        verticalGroup.center();
        verticalGroup.setPosition(uiStage.getCamera().viewportWidth / 2, uiStage.getCamera().viewportHeight / 2);

        uiStage.addActor(verticalGroup);
    }

    private void setMenuScreen() {
        game.setScreen(new MenuScreen(game));
        dispose();
    }

    @Override
    public void dispose() {
        super.dispose();
        batch.dispose();
        level.getTiledMap().dispose();
        stage.dispose();
        uiStage.dispose();
    }
}
