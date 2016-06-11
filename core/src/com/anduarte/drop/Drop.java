package com.anduarte.drop;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Drop extends Game {
    // Instantiating a SpriteBatch and a BitmapFont that will be
    // shared. It's a bad practice to create multiple objects that can be shared instead
    public SpriteBatch batch;			// Special class used to draw 2D Images
    public BitmapFont font;

    @Override
    public void create() {
        batch = new SpriteBatch();
        // Use libGDX default Arial font
        font = new BitmapFont();
        this.setScreen((new MainMenuScreen(this)));
    }

    public void render() {
        super.render();
    }

    public void dispose() {
        batch.dispose();
        font.dispose();
    }
}
