package com.rainerregan.gamelauncher.flappybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.Random;

public class FlappyBird extends ApplicationAdapter {
	SpriteBatch batch;
	Texture background; // Seperti image | Background untuk game
	Texture[] birds;

	Texture gameOver;

	int flapState = 0;
	float birdY = 0;
	float velocity = 0;
	float gravity = 2;

	int score = 0;
	int scoringTube = 0;

	int gameState = 0;

	Texture topTube;
	Texture bottomTube;
	float gap = 400;

	float maxTubeOffset;
	Random rand;
	float tubeVelocity = 4;
	int numberOfTubes = 4;
	float[] tubeX =  new float[numberOfTubes];
	float[] tubeOffset = new float[numberOfTubes];
	float distanceBetweenTubes;

	Circle birdCircle;
//	ShapeRenderer shapeRenderer;

	Rectangle[] topTubeRectangles;
	Rectangle[] bottomTubeRectangles;

	BitmapFont font;

	@Override
	public void create () {
		batch = new SpriteBatch(); // Managing Sprites
		background = new Texture("bg.png"); // Mengambil sumber dari bg.png
		birds = new Texture[2];
		birds[0] = new Texture("bird.png");
		birds[1] = new Texture("bird2.png");
		gameOver = new Texture("gameover.png");

//		shapeRenderer = new ShapeRenderer();
		font = new BitmapFont();
		font.setColor(Color.WHITE);
		font.getData().setScale(10);

		birdCircle = new Circle();

		topTube = new Texture("toptube.png");
		bottomTube = new Texture("bottomtube.png");
		maxTubeOffset = Gdx.graphics.getHeight()/2- gap/2 -100;
		rand = new Random();
		distanceBetweenTubes = Gdx.graphics.getWidth() * 3/4;
		topTubeRectangles = new Rectangle[numberOfTubes];
		bottomTubeRectangles = new Rectangle[numberOfTubes];

		startGame();
	}

	public void startGame(){
		birdY = Gdx.graphics.getHeight()/2 - birds[0].getHeight()/2;
		for (int i = 0; i < numberOfTubes; i++) {
			tubeOffset[i] = (rand.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - gap - 200);
			tubeX[i] = Gdx.graphics.getWidth() /2 -topTube.getWidth()/2 + Gdx.graphics.getWidth() + i * distanceBetweenTubes;

			topTubeRectangles[i] = new Rectangle();
			bottomTubeRectangles[i] = new Rectangle();
		}
	}

	@Override
	public void render () {

		batch.begin(); // Begin Batch: Memberi tahu render untuk memulai
		batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		// Ketika game sudah dimulai
		if(gameState == 1) {

			if(tubeX[scoringTube] < Gdx.graphics.getWidth() / 2) {
				score++;
				if(scoringTube < numberOfTubes -1){
					scoringTube++;
					Gdx.app.log("Score", String.valueOf(score));
				} else {
					scoringTube = 0;
				}
			}

			// Jika disentuh, maka burung akan terbang karena velocitynya minus, dan akan terbang keatas
			if(Gdx.input.justTouched()){
				velocity = -30;

			}

			for (int i = 0; i < numberOfTubes; i++) {

				if (tubeX[i] < - topTube.getWidth()){
					tubeX[i] += numberOfTubes * distanceBetweenTubes;
					tubeOffset[i] = (rand.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - gap - 200);
				} else {
					tubeX[i] = tubeX[i] - tubeVelocity;

				}

				batch.draw(topTube, tubeX[i], Gdx.graphics.getHeight()/2 + gap/2 + tubeOffset[i]);
				batch.draw(bottomTube, tubeX[i], Gdx.graphics.getHeight()/2 -gap/2 - bottomTube.getHeight() + tubeOffset[i]);

				topTubeRectangles[i] = new Rectangle(tubeX[i], Gdx.graphics.getHeight()/2 + gap/2 + tubeOffset[i], topTube.getWidth(), topTube.getHeight());
				bottomTubeRectangles[i] = new Rectangle(tubeX[i], Gdx.graphics.getHeight()/2 -gap/2 - bottomTube.getHeight() + tubeOffset[i], bottomTube.getWidth(), bottomTube.getHeight());
			}



			if(birdY > 0) {
				// Kalau sudah tidak disentuh, burung akan jatuh kebawah sesuai dengan gravitasi
				velocity = velocity + gravity;
				birdY -= velocity;
			} else{
				gameState = 2;
			}
		} else if (gameState == 0){

			// Jika disentuh, permainan akan mulai
			if(Gdx.input.justTouched()){
				gameState = 1;
			}
		} else if(gameState == 2){
			batch.draw(gameOver, Gdx.graphics.getWidth()/2 - gameOver.getWidth()/2, Gdx.graphics.getHeight()/2 - gameOver.getHeight()/2);

			if (Gdx.input.justTouched()){
				gameState = 1;
				startGame();
				score =0;
				scoringTube = 0;
				velocity =0;
			}
		}

		// Memberi efek kepakan sayap
		if(flapState == 0){
			flapState = 1;
		} else {
			flapState = 0;
		}

		batch.draw(birds[flapState], Gdx.graphics.getWidth() / 2 - birds[flapState].getWidth() / 2, birdY);

		birdCircle.set(Gdx.graphics.getWidth()/2, birdY + birds[flapState].getHeight()/2, birds[flapState].getWidth()/2);

		font.draw(batch, String.valueOf(score), 100, 100);

//		shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
//		shapeRenderer.setColor(Color.RED);
//		shapeRenderer.circle(birdCircle.x, birdCircle.y, birdCircle.radius);

		for (int i = 0; i < numberOfTubes; i++) {
//			shapeRenderer.rect(tubeX[i], Gdx.graphics.getHeight()/2 + gap/2 + tubeOffset[i], topTube.getWidth(), topTube.getHeight());
//			shapeRenderer.rect(tubeX[i], Gdx.graphics.getHeight()/2 -gap/2 - bottomTube.getHeight() + tubeOffset[i], bottomTube.getWidth(), bottomTube.getHeight());

			if(Intersector.overlaps(birdCircle, topTubeRectangles[i]) || Intersector.overlaps(birdCircle, bottomTubeRectangles[i])){
				gameState = 2;
			}
		}

//		shapeRenderer.end();
		batch.end(); // End render
	}
}
