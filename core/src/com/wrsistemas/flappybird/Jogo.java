package com.wrsistemas.flappybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

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
	private int pontuacaoMaxima = 0;
	private boolean passouCano = false;
	private int estadodoJogo = 0;
	private float posicaoHorizontalPassaro = 0;

	//Exibição de textos
	BitmapFont textoPontuacao;
	BitmapFont textoReiniciar;
	BitmapFont textoMelhorPontuacao;

	//Exibição dos sons
	Sound somVoando;
	Sound somColisao;
	Sound somPontuação;

	//Objeto salvar pontuação
	Preferences preferencias;

	//Objetos para Câmera
	private OrthographicCamera camera;
	private Viewport viewport;
	private final float VIRTUAL_WIDTH = 720;
	private final float VIRTUAL_HEIGHT = 1280;


	@Override
	public void create () {
		inicializarTexturas();
		inicializarObjetos();
	}

	@Override
	public void render () {

		//Limpar frames anteriores
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

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
				somVoando.play();
			}

		}else if (estadodoJogo == 1){

			//Aplicar eventos de click
			if (toqueTela){
				gravidade = -15; //Aqui configura o pulo do passaro com jogo acontecendo
				somVoando.play();
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

			/*//Aplicar gravidade no pássaro fazendo ele cair após a colisão
			if (posicaoInicialVertical > 0 || toqueTela )
				posicaoInicialVertical = posicaoInicialVertical - gravidade;
			gravidade++;*/

			if (pontos > pontuacaoMaxima){
				pontuacaoMaxima = pontos;
				preferencias.putInteger("pontuacaoMaxima", pontuacaoMaxima);
			}

			posicaoHorizontalPassaro -= Gdx.graphics.getDeltaTime()*500;//Fazer o Passaro após a batida ir para trás

			//Aplicar eventos de click
			if (toqueTela){
				estadodoJogo = 0;
				pontos = 0;
				gravidade = 0;
				posicaoHorizontalPassaro = 0;
				posicaoInicialVertical = alturaDispositivo / 2; // define a posição inicial do passaro para recomeçar o jogo
				posicaoCanoHorizontal = larguraDispositivos; // define a posição inicial dos canos para recomeçar o jogo
			}


		}
	}

	private void detectarColisoes(){

		circuloPassaro.set(50 + posicaoHorizontalPassaro + passaros.get(0).getWidth() /2, posicaoInicialVertical + passaros.get(0).getHeight() / 2, passaros.get(0).getWidth()/2);
		retanguloCanoBaixo.set(posicaoCanoHorizontal, alturaDispositivo / 2 - canoBaixo.getHeight() - espacoEntreCanos/2 + posicaoCanoVertical, canoBaixo.getWidth(), canoBaixo.getHeight());
		retanguloCanoCima.set(posicaoCanoHorizontal, alturaDispositivo / 2 + espacoEntreCanos / 2 + posicaoCanoVertical, canoTopo.getWidth(), canoTopo.getHeight());

		boolean colidiuCanoCima = Intersector.overlaps(circuloPassaro, retanguloCanoCima);
		boolean colidiuCanoBaixo = Intersector.overlaps(circuloPassaro, retanguloCanoBaixo);

		if (colidiuCanoCima || colidiuCanoBaixo){
			Gdx.app.log("Log", "Colidiu");
			if ( estadodoJogo == 1){
				estadodoJogo = 2;
				somColisao.play();
			}

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

		batch.setProjectionMatrix(camera.combined);

		batch.begin();//inicio

		batch.draw(fundo, 0, 0, larguraDispositivos , alturaDispositivo );//parametros passado = 1 imagen, 2 eixo x, 3 eixo y, 4 altura, 5 largura
		batch.draw(passaros.get((int) variacao), 50 + posicaoHorizontalPassaro, posicaoInicialVertical);

		//posiçãoCanoHorizontal
		batch.draw(canoBaixo, posicaoCanoHorizontal, alturaDispositivo / 2 - canoBaixo.getHeight() - espacoEntreCanos/2 + posicaoCanoVertical);
		batch.draw(canoTopo, posicaoCanoHorizontal, alturaDispositivo / 2 + espacoEntreCanos / 2 + posicaoCanoVertical);

		textoPontuacao.draw(batch, String.valueOf(pontos), larguraDispositivos/2, alturaDispositivo - 110);

		if (estadodoJogo == 2){
			batch.draw(gameOver, larguraDispositivos / 2 - gameOver.getWidth() / 2, alturaDispositivo / 2);
			textoReiniciar.draw(batch, "Toque na tela para reiniciar! ", larguraDispositivos /2 - 160, alturaDispositivo/2 - gameOver.getHeight()/2);
			textoMelhorPontuacao.draw(batch, "Seu recorde é: " +  pontuacaoMaxima + " pontos ", larguraDispositivos /2 - 140, alturaDispositivo/2 - gameOver.getHeight());
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
				somPontuação.play();
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

		larguraDispositivos = VIRTUAL_WIDTH;
		alturaDispositivo = VIRTUAL_HEIGHT;
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

		//Inicializando os sons
		somVoando = Gdx.audio.newSound(Gdx.files.internal("som_asa.wav"));
		somColisao = Gdx.audio.newSound(Gdx.files.internal("som_batida.wav"));
		somPontuação = Gdx.audio.newSound(Gdx.files.internal("som_pontos.wav"));

		//Configurar preferencias do jogo
		preferencias = Gdx.app.getPreferences("flappyBird");
		pontuacaoMaxima = preferencias.getInteger("pontuacaomaxima", 0);

		//Configuração da camera
		camera = new OrthographicCamera();
		camera.position.set(VIRTUAL_WIDTH/2, VIRTUAL_HEIGHT/2, 0);
		viewport = new StretchViewport(VIRTUAL_WIDTH, VIRTUAL_HEIGHT, camera);


	}

	@Override
	public void resize(int width, int height) {
		viewport.update(width, height);
	}

	@Override
	public void dispose () {

	}
}
