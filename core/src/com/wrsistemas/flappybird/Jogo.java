package com.wrsistemas.flappybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.ArrayList;
import java.util.List;

public class Jogo extends ApplicationAdapter {

	private int movimentoX = 0;
	private int movimentoY = 0;
	private SpriteBatch batch;
	private List<Texture> passaros = new ArrayList<>();
	private Texture fundo;

	//Atributos de configuração
	private float larguraDispositivos;
	private float alturaDispositivo;
	private float variacao = 0;

	@Override
	public void create () {
		/*Gdx.app.log("create", "jogo iniciado");*/

		batch = new SpriteBatch();
		passaros.add(new Texture("passaro1.png"));
		passaros.add(new Texture("passaro2.png"));
		passaros.add(new Texture("passaro3.png"));
		fundo = new Texture("fundo.png");

		larguraDispositivos = Gdx.graphics.getWidth();
		alturaDispositivo = Gdx.graphics.getHeight();
	}

	@Override
	public void render () {

		batch.begin();//inicio

		if (variacao > 3)//reseta o valor da variação para limitar apenas as 3 imagens do array de passaros
			variacao = 0;

		batch.draw(fundo, 0, 0, larguraDispositivos , alturaDispositivo );//parametros passado = 1 imagen, 2 eixo x, 3 eixo y, 4 altura, 5 largura
		batch.draw(passaros.get((int) variacao), 30, alturaDispositivo / 2);

		variacao += Gdx.graphics.getDeltaTime() * 10;
		movimentoX++;
		movimentoY++;
		batch.end();//fim
	}
	
	@Override
	public void dispose () {

	}
}
