package com.wrsistemas.flappybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

public class Jogo extends ApplicationAdapter {

	private int movimentoX = 0;
	private int movimentoY = 0;
	private SpriteBatch batch;
	private Texture passaro;
	private Texture fundo;

	//Atributos de configuração
	private float larguraDispositivos;
	private float alturaDispositivo;

	@Override
	public void create () {
		/*Gdx.app.log("create", "jogo iniciado");*/

		batch = new SpriteBatch();
		passaro = new Texture("passaro1.png");
		fundo = new Texture("fundo.png");

		larguraDispositivos = Gdx.graphics.getWidth();
		alturaDispositivo = Gdx.graphics.getHeight();
	}

	@Override
	public void render () {
		/*contador++;
		Gdx.app.log("render", "jogo renderizado: " + contador);*/

		batch.begin();//inicio


		batch.draw(fundo, 0, 0, larguraDispositivos , alturaDispositivo );//parametros passado = 1 imagen, 2 eixo x, 3 eixo y, 4 altura, 5 largura
		batch.draw(passaro, movimentoX, 500);

		movimentoX++;
		movimentoY++;
		batch.end();//fim
	}
	
	@Override
	public void dispose () {

	}
}
