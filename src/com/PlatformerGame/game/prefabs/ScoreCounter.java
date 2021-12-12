package com.PlatformerGame.game.prefabs;

import com.PlatformerGame.engine.core.*;
import com.PlatformerGame.engine.core.components.QuadRendererComponent;

public class ScoreCounter extends GameObject {
	//array of textures for each number. I didn't have time to implement TTF font rendering so I made PNG files of each number and will use them in an array
	private Texture[] digits = new Texture[] {
			new Texture("/textures/0.png"),
			new Texture("/textures/1.png"),
			new Texture("/textures/2.png"),
			new Texture("/textures/3.png"),
			new Texture("/textures/4.png"),
			new Texture("/textures/5.png"),
			new Texture("/textures/6.png"),
			new Texture("/textures/7.png"),
			new Texture("/textures/8.png"),
			new Texture("/textures/9.png")
		};
	
	private int oldScore = 0;
	
	private GameObject firstDigit = new GameObject(-0.05f, 0f, 0f);
	private GameObject secondDigit = new GameObject(0.05f, 0f, 0f);;
	
	public void onCreate() {
 		firstDigit.addComponent(new QuadRendererComponent(0.1f, 0.25f));
		secondDigit.addComponent(new QuadRendererComponent(0.1f, 0.25f));
		
		int dig1, dig2;
		//sets score counter to oldScore
		if (oldScore > 9) {
			dig1 = oldScore/10;
			dig2 = oldScore%10;
		} else {
			dig1 = 0;
			dig2 = oldScore;
		}
		
		firstDigit.<QuadRendererComponent>getComponent("QuadRenderer").tex = digits[dig1];
		secondDigit.<QuadRendererComponent>getComponent("QuadRenderer").tex = digits[dig2];

		addChild(firstDigit);
		addChild(secondDigit);
	}
	
	//using this instead of onUpdate so we can call here from game code instead of app code
	public void updateScoreCount(int score) {
		int dig1, dig2;
		//sets score counter to oldScore
		if (score > 9) {
			dig1 = score/10;
			dig2 = score%10;
		} else {
			dig1 = 0;
			dig2 = score;
		}
		
		firstDigit.<QuadRendererComponent>getComponent("QuadRenderer").tex = digits[dig1];
		secondDigit.<QuadRendererComponent>getComponent("QuadRenderer").tex = digits[dig2];
		
		oldScore = score;
	}
}
