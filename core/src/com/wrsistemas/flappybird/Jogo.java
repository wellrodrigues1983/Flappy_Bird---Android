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

	//Texturas
	private SpriteBatch batch;
	private List<Texture> passaros = new ArrayList<>();
	private Texture fundo;
	private Texture canoBaixo;
	private Texture canoTopo;

	//Atributos de configuração
	private float larguraDispositivos;
	private float alturaDispositivo;
	private float variacao = 0;
	private float gravidade = 0;
	private float posicaoInicialVertical = 0;
	private float posicaoCanoHorizontal;
	private float espacoEntreCanos;

	@Override
	public void create () {
		inicializarTexturas();
		inicializarObjetos();
	}

	@Override
	public void render () {

		verificarEstadodoJogo();
		desenharTexturas();

	}

	private void verificarEstadodoJogo(){



		//Aplicar eventos de click
		boolean toqueTela = Gdx.input.justTouched();
		if (toqueTela){
			gravidade = -25;
		}

		//Aplicar gravidade no pássaro
		if (posicaoInicialVertical > 0 || toqueTela )
			posicaoInicialVertical = posicaoInicialVertical - gravidade;

		variacao += Gdx.graphics.getDeltaTime() * 10;

		//Verifica a variação para bater asas dp pássaro
		if (variacao > 3)//reseta o valor da variação para limitar apenas as 3 imagens do array de passaros
			variacao = 0;

		gravidade++;

	}

	private void desenharTexturas(){

		batch.begin();//inicio

		batch.draw(fundo, 0, 0, larguraDispositivos , alturaDispositivo );//parametros passado = 1 imagen, 2 eixo x, 3 eixo y, 4 altura, 5 largura
		batch.draw(passaros.get((int) variacao), 30, posicaoInicialVertical);
		batch.draw(canoBaixo, posicaoCanoHorizontal - 100, alturaDispositivo / 2 - canoBaixo.getHeight() - espacoEntreCanos/2);
		batch.draw(canoTopo, posicaoCanoHorizontal -100, alturaDispositivo / 2 + espacoEntreCanos / 2);

		batch.end();//fim

	}

	private void inicializarTexturas(){

		passaros.add(new Texture("passaro1.png"));
		passaros.add(new Texture("passaro2.png"));
		passaros.add(new Texture("passaro3.png"));

		fundo = new Texture("fundo.png");
		canoBaixo = new Texture("cano_baixo_maior.png");
		canoTopo = new Texture("cano_topo_maior.png");
	}

	private void inicializarObjetos(){

		batch = new SpriteBatch();

		larguraDispositivos = Gdx.graphics.getWidth();
		alturaDispositivo = Gdx.graphics.getHeight();
		posicaoInicialVertical = alturaDispositivo / 2;
		posicaoCanoHorizontal = larguraDispositivos;
		espacoEntreCanos = 150;
	}
	
	@Override
	public void dispose () {

	}
}
