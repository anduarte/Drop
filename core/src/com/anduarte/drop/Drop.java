package com.anduarte.drop;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

public class Drop extends ApplicationAdapter {
	// load the assets and store a reference to them
	private Texture dropImage;
	private Texture bucketImage;
	private Sound dropSound;
	private Music rainMusic;

    private OrthographicCamera camera;
    private SpriteBatch batch;			// Special class used to draw 2D Images

    private Rectangle bucket;

	@Override
	public void create () {
        // load the images for the droplet and the bucket, 64x64 pixels each
        dropImage = new Texture(Gdx.files.internal("droplet.png"));
        bucketImage = new Texture(Gdx.files.internal("bucket.png"));

        // load the drop sound effect and the rain background "music"
        dropSound = Gdx.audio.newSound(Gdx.files.internal("drop.wav"));
        rainMusic = Gdx.audio.newMusic(Gdx.files.internal("rain.mp3"));

        // start the playback of the background music immediately
        rainMusic.setLooping(true);
        rainMusic.play();

        // Creating the camera
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);

        batch = new SpriteBatch();

        bucket = new Rectangle();
        bucket.x = 800 / 2 - 64 / 2;    // Position in the middle of the screen
        bucket.y = 20;                  // Position bottom of the screen
        bucket.width = 64;
        bucket.height = 64;
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0.2f, 1);         // Clear the screen with dark blue color
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Cameras are responsible for setting up coordinate system for rendering
        // camera.update(); // Good practice to update camera once per frame (Don´t need in this game

        // Rendering the bucket
        batch.setProjectionMatrix(camera.combined); // Tells to SpriteBatch to use the coordinate system specified by the camera

        // SpriteBatch class will record all drawing commands in between begin and end
        // Once we call the end() it will submit all drawing requests at once to OpenGL
        batch.begin();
        batch.draw(bucketImage, bucket.x, bucket.y);
        batch.end();

        // Make the bucket move by touche/Mouse click
        if (Gdx.input.isTouched()) {
            Vector3 touchPos = new Vector3(); // In desktop there isn't a problem instantiate every time a new vector3 but in android we should use an instance class instead
            // Convert the touch/mouse coordinates to camera's coordinate
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPos); // Transform the coordinates to camera coordinate
            bucket.x = touchPos.x - (64 / 2);
        }
	}
}
