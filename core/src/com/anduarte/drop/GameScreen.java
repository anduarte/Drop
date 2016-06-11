package com.anduarte.drop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.Iterator;

/**
 * Created by andre on 11/06/2016.
 */
public class GameScreen implements Screen {
    final Drop game;

    // load the assets and store a reference to them
    private Texture dropImage;
    private Texture bucketImage;
    private Sound dropSound;
    private Music rainMusic;

    private OrthographicCamera camera;

    private Rectangle bucket;
    private Array<Rectangle> raindrops; // LibGDX is a class util to be used instead of standard Java Collections
    // The main difference is that minimizes garbage as much possible
    private long lastDropTime;          // Keep track of the last time we spawned a raindrop

    private int dropsGathered;


    public GameScreen(final Drop game) {
        this.game = game;

        // load the images for the droplet and the bucket, 64x64 pixels each
        dropImage = new Texture(Gdx.files.internal("droplet.png"));
        bucketImage = new Texture(Gdx.files.internal("bucket.png"));

        // load the drop sound effect and the rain background "music"
        dropSound = Gdx.audio.newSound(Gdx.files.internal("drop.wav"));
        rainMusic = Gdx.audio.newMusic(Gdx.files.internal("rain.mp3"));

        // set the loop of the background music
        rainMusic.setLooping(true);

        // Creating the camera
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);

        bucket = new Rectangle();
        bucket.x = 800 / 2 - 64 / 2;    // Position in the middle of the screen
        bucket.y = 20;                  // Position bottom of the screen
        bucket.width = 64;
        bucket.height = 64;

        raindrops = new Array<Rectangle>();
        spawnRainDrop();
    }

    /**
     * Method which instantiates a new Rectangle(raindrop) sets it to a random position
     * at the top edge of the screen and adds it to the raindrops array
     */
    private void spawnRainDrop() {
        Rectangle raindrop = new Rectangle();
        raindrop.x = MathUtils. random(0, 800 -64);
        raindrop.y = 480;
        raindrop.width = 64;
        raindrop.height = 64;
        raindrops.add(raindrop);
        lastDropTime = TimeUtils.nanoTime();
    }

    @Override
    public void show() {
        // start the playback of the background music
        // when the screen is shown
        rainMusic.play();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);         // Clear the screen with dark blue color in a range of [0,1]
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Cameras are responsible for setting up coordinate system for rendering
        camera.update(); // Good practice to update camera once per frame (Don´t need in this game

        // Rendering the bucket
        game.batch.setProjectionMatrix(camera.combined); // Tells to SpriteBatch to use the coordinate system specified by the camera

        // SpriteBatch class will record all drawing commands in between begin and end
        // Once we call the end() it will submit all drawing requests at once to OpenGL
        game.batch.begin();
        game.batch.draw(bucketImage, bucket.x, bucket.y);

        game.font.draw(game.batch, "Drops Collected: " + dropsGathered, 0, 480);

        // render the raindrops
        for (Rectangle raindrop : raindrops) {
            game.batch.draw(dropImage, raindrop.x, raindrop.y);
        }

        game.batch.end();

        // Make the bucket move by touche/Mouse click
        if (Gdx.input.isTouched()) {
            Vector3 touchPos = new Vector3(); // In desktop there isn't a problem instantiate every time a new vector3 but in android we should use an instance class instead
            // Convert the touch/mouse coordinates to camera's coordinate
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPos); // Transform the coordinates to camera coordinate
            bucket.x = touchPos.x - (64 / 2);
        }

        // Make the bucket move by keyboard
        // Gdx.graphics.getDeltaTime() implements time-based movement
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            bucket.x -= 200 * Gdx.graphics.getDeltaTime();
        }

        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            bucket.x += 200 * Gdx.graphics.getDeltaTime();
        }

        // Limited the bucket to the screen size
        if (bucket.x < 0) {
            bucket.x = 0;
        } else if (bucket.x > 800 - 64) {
            bucket.x = 800 - 64;
        }

        // It will check how much time has passed since we spawned a new raindrop
        // and creates a new one if necessary
        if (TimeUtils.nanoTime() - lastDropTime > 1000000000) {
            spawnRainDrop();
        }

        // Move raindrops and remove it from the raindrops if goes of the bottom of the screen
        Iterator<Rectangle> iterator = raindrops.iterator();
        while (iterator.hasNext()) {
            Rectangle raindrop = iterator.next();
            raindrop.y -= 200 * Gdx.graphics.getDeltaTime();

            if(raindrop.y + 64 < 0) {
                iterator.remove();
            }

            // If the raindrop hits the bucket playback the drop sound
            // and remove the raindrop from the bucket
            if (raindrop.overlaps(bucket)) { // Checks if the rectangle overlaps with another rectangle
                dropsGathered++;
                dropSound.play();
                iterator.remove();
            }
        }
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    /**
     * Method that helps the SO to clean up when the user close the application
     * It will dispose every textures, sounds and SpriteBatch
     */
    public void dispose() {
        // Disposable are usually native resources which are not handled by the Java Garbage collector
        // This is why we need to manually dispose of them
        dropImage.dispose();
        bucketImage.dispose();
        dropSound.dispose();
        rainMusic.dispose();
    }
}
