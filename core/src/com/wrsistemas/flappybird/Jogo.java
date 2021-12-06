package com.wrsistemas.flappybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Jogo extends ApplicationAdapter {

	//Texturas
	private SpriteBatch batch;
	private List<Texture> passaros = new ArrayList<>();
	private Texture fundo;
	private Texture canoBaixo;
	private Texture canoTopo;
	private Texture gameOver;


	//Formas para colisao
	private ShapeRenderer shapeRenderer;
	private Circle circuloPassaro;
	private Rectangle retanguloCanoCima;
	private Rectangle retanguloCanoBaixo;

	//Atributos de configuração
	private float larguraDispositivos;
	private float alturaDispositivo;
	private float variacao = 0;
	private float gravidade = 0;
	private float posicaoInicialVertical = 0;
	private float posicaoCanoHorizontal;
	private float posicaoCanoVertical;
	private float espacoEntreCanos;
	private Random random;
	private int pontos = 0;
	private boolean passouCano = false;
	private int estadodoJogo = 0;

	//Exibição de textos
	BitmapFont textoPontuacao;
	BitmapFont textoReiniciar;
	BitmapFont textoMelhorPontuacao;


	@Override
	public void create () {
		inicializarTexturas();
		inicializarObjetos();
	}

	@Override
	public void render () {

		verificarEstadodoJogo();
		validarPontos();
		desenharTexturas();
		detectarColisoes();

	}


	/*0 - Jogo inicial, passaro parado
	 * 1 - Começa o jogo
	 * 2 - Colidiu
	 */

	private void verificarEstadodoJogo(){

		boolean toqueTela = Gdx.input.justTouched();

		if (estadodoJogo == 0){

			//Aplicar eventos de click
			if (toqueTela){
				gravidade = -15; //Aqui configura o pulo do passaro com jogo parado
				estadodoJogo = 1;
			}

		}else if (estadodoJogo == 1){

			//Aplicar eventos de click
			if (toqueTela){
				gravidade = -15; //Aqui configura o pulo do passaro com jogo acontecendo
			}

			//Movimentar Cano
			posicaoCanoHorizontal -= Gdx.graphics.getDeltaTime() * 200;
			if (posicaoCanoHorizontal < -canoTopo.getWidth() ){
				posicaoCanoHorizontal = larguraDispositivos;
				posicaoCanoVertical = random.nextInt(400) -200; //variação de espaços dos canos para subir ou descer
				passouCano = false;
			}

			//Aplicar gravidade no pássaro
			if (posicaoInicialVertical > 0 || toqueTela )
				posicaoInicialVertical = posicaoInicialVertical - gravidade;

			gravidade++;

		}else if (estadodoJogo == 2){

		}
	}

	private void detectarColisoes(){

		circuloPassaro.set(50 + passaros.get(0).getWidth() /2, posicaoInicialVertical + passaros.get(0).getHeight() / 2, passaros.get(0).getWidth()/2);
		retanguloCanoBaixo.set(posicaoCanoHorizontal, alturaDispositivo / 2 - canoBaixo.getHeight() - espacoEntreCanos/2 + posicaoCanoVertical, canoBaixo.getWidth(), canoBaixo.getHeight());
		retanguloCanoCima.set(posicaoCanoHorizontal, alturaDispositivo / 2 + espacoEntreCanos / 2 + posicaoCanoVertical, canoTopo.getWidth(), canoTopo.getHeight());

		boolean colidiuCanoCima = Intersector.overlaps(circuloPassaro, retanguloCanoCima);
		boolean colidiuCanoBaixo = Intersector.overlaps(circuloPassaro, retanguloCanoBaixo);

		if (colidiuCanoCima || colidiuCanoBaixo){
			Gdx.app.log("Log", "Colidiu");
			estadodoJogo = 2;
		}
//Esse codigo comentado abaixo é para visualizar os desenho para fazer a colisao

		/*shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		shapeRenderer.setColor(Color.RED);

		shapeRenderer.circle(50 + passaros.get(0).getWidth() /2, posicaoInicialVertical + passaros.get(0).getHeight() / 2, passaros.get(0).getWidth()/2);

		//Cano Topo
		shapeRenderer.rect(posicaoCanoHorizontal, alturaDispositivo / 2 + espacoEntreCanos / 2 + posicaoCanoVertical, canoTopo.getWidth(), canoTopo.getHeight());

		//CanoBaixo
		shapeRenderer.rect(posicaoCanoHorizontal, alturaDispositivo / 2 - canoBaixo.getHeight() - espacoEntreCanos/2 + posicaoCanoVertical, canoBaixo.getWidth(), canoBaixo.getHeight());


		shapeRenderer.end();*/

	}

	private void desenharTexturas(){

		batch.begin();//inicio

		batch.draw(fundo, 0, 0, larguraDispositivos , alturaDispositivo );//parametros passado = 1 imagen, 2 eixo x, 3 eixo y, 4 altura, 5 largura
		batch.draw(passaros.get((int) variacao), 50, posicaoInicialVertical);

		//posiçãoCanoHorizontal
		batch.draw(canoBaixo, posicaoCanoHorizontal, alturaDispositivo / 2 - canoBaixo.getHeight() - espacoEntreCanos/2 + posicaoCanoVertical);
		batch.draw(canoTopo, posicaoCanoHorizontal, alturaDispositivo / 2 + espacoEntreCanos / 2 + posicaoCanoVertical);

		textoPontuacao.draw(batch, String.valueOf(pontos), larguraDispositivos/2, alturaDispositivo - 110);

		if (estadodoJogo == 2){
			batch.draw(gameOver, larguraDispositivos / 2 - gameOver.getWidth() / 2, alturaDispositivo / 2);
			textoReiniciar.draw(batch, "Toque na tela para reiniciar! ", larguraDispositivos /2 - 160, alturaDispositivo/2 - gameOver.getHeight()/2);
			textoMelhorPontuacao.draw(batch, "Seu recorde é: 0 pontos", larguraDispositivos /2 - 140, alturaDispositivo/2 - gameOver.getHeight());
		}


		batch.end();//fim

	}

	private void inicializarTexturas(){

		passaros.add(new Texture("passaro1.png"));
		passaros.add(new Texture("passaro2.png"));
		passaros.add(new Texture("passaro3.png"));

		fundo = new Texture("fundo.png");
		canoBaixo = new Texture("cano_baixo_maior.png");
		canoTopo = new Texture("cano_topo_maior.png");
		gameOver = new Texture("game_over.png");

	}

	public void validarPontos(){

		if (posicaoCanoHorizontal < 50-passaros.get(0).getWidth()){//Passou da posição do passaro
			if (!passouCano){
				pontos++;
				passouCano = true;
			}
		}

		variacao += Gdx.graphics.getDeltaTime() * 10;
		//Verifica a variação para bater asas dp pássaro
		if (variacao > 3)//reseta o valor da variação para limitar apenas as 3 imagens do array de passaros
			variacao = 0;
	}

	private void inicializarObjetos(){

		batch = new SpriteBatch();
		random = new Random();

		larguraDispositivos = Gdx.graphics.getWidth();
		alturaDispositivo = Gdx.graphics.getHeight();
		posicaoInicialVertical = alturaDispositivo / 2;
		posicaoCanoHorizontal = larguraDispositivos;
		espacoEntreCanos = 350; //Aqui define o espaço entre canos

		//Configurações dos textos
		textoPontuacao = new BitmapFont();
		textoPontuacao.setColor(Color.WHITE);
		textoPontuacao.getData().setScale(10);

		textoReiniciar = new BitmapFont();
		textoReiniciar.setColor(Color.GREEN);
		textoReiniciar.getData().setScale(2);

		textoMelhorPontuacao = new BitmapFont();
		textoMelhorPontuacao.setColor(Color.RED);
		textoMelhorPontuacao.getData().setScale(2);

		//Formas geometricas para colisoes
		shapeRenderer = new ShapeRenderer();
		circuloPassaro = new Circle();
		retanguloCanoBaixo = new Rectangle();
		retanguloCanoCima = new Rectangle();
	}
	
	@Override
	public void dispose () {

	}
}
