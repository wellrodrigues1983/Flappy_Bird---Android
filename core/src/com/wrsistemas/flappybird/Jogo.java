package com.wrsistemas.flappybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

public class Jogo extends ApplicationAdapter {

	/*private int contador = 0;*/
	private SpriteBatch batch;
	private Texture passaro;

	@Override
	public void create () {
		/*Gdx.app.log("create", "jogo iniciado");*/

		batch = new SpriteBatch();
		passaro = new Texture("passaro1.png");
	}

	@Override
	public void render () {
		/*contador++;
		Gdx.app.log("render", "jogo renderizado: " + contador);*/

		batch.begin();

		batch.draw(passaro, 300, 500);

		batch.end();
	}
	
	@Override
	public void dispose () {

	}
}
