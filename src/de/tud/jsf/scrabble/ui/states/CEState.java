package de.tud.jsf.scrabble.ui.states;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Random;

import org.lwjgl.input.Keyboard;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import eea.engine.action.Action;
import eea.engine.action.basicactions.ChangeStateAction;
import eea.engine.action.basicactions.MoveDownAction;
import eea.engine.action.basicactions.MoveLeftAction;
import eea.engine.action.basicactions.MoveRightAction;
import eea.engine.component.Component;
import eea.engine.component.render.ImageRenderComponent;
import eea.engine.entity.Entity;
import eea.engine.entity.StateBasedEntityManager;
import eea.engine.event.ANDEvent;
import eea.engine.event.basicevents.CollisionEvent;
import eea.engine.event.basicevents.KeyDownEvent;
import eea.engine.event.basicevents.LoopEvent;
import eea.engine.event.basicevents.MouseClickedEvent;
import eea.engine.event.basicevents.MouseEnteredEvent;

import de.tud.jsf.scrabble.constants.GameParameters;
import de.tud.jsf.scrabble.ui.entity.Letter;

public class CEState extends BasicGameState implements GameParameters {

	private int stateID;
	private StateBasedEntityManager entityManager;

	private Entity left_bound, right_bound, bar;
	private ArrayList<Letter> bag_of_letters_ce = new ArrayList<Letter>();

	private Timer timer = new Timer();


	CEState(int sid) {
		this.stateID = sid;
		entityManager = StateBasedEntityManager.getInstance();
	}

	@Override
	public void init(GameContainer arg0, StateBasedGame arg1) throws SlickException {
		// Background
		Entity background = new Entity("menu");
		background.setPosition(new Vector2f(480, 360));
		background.addComponent(new ImageRenderComponent(new Image("/assets/background.png")));
//    	entityManager.addEntity(stateID, background);	


		// Return to Gameplay State (testing only)
		Entity new_Game_Entity = new Entity("return");

		new_Game_Entity.setPosition(new Vector2f(850, 220));
		new_Game_Entity.addComponent(new ImageRenderComponent(new Image("assets/scrabble/ui/grey_boxCross.png")));

		ANDEvent mainEvents = new ANDEvent(new MouseEnteredEvent(), new MouseClickedEvent());
		Action new_Game_Action = new ChangeStateAction(Launch.GAMEPLAY_STATE);
		mainEvents.addAction(new Action() {
			@Override
			public void update(GameContainer arg0, StateBasedGame arg1, int arg2, Component arg3) {
				bag_of_letters_ce.clear();
			}
		});
		mainEvents.addAction(new_Game_Action);
		new_Game_Entity.addComponent(mainEvents);
		entityManager.addEntity(stateID, new_Game_Entity);
		
		// Start button
		Entity test_box = new Entity("test_count");

		test_box.setPosition(new Vector2f(800, 220));
		test_box.addComponent(new ImageRenderComponent(new Image("assets/scrabble/ui/grey_boxCheckmark.png")));

		ANDEvent click_event = new ANDEvent(new MouseEnteredEvent(), new MouseClickedEvent());
		Action click_action = new Action() {
			@Override
			public void update(GameContainer arg0, StateBasedGame arg1, int arg2, Component arg3) {
				bag_of_letters_ce.forEach((letter) -> {
					if(!GameplayState.bag_of_letters.contains(letter.getID())) {
						bag_of_letters_ce.remove(letter);
					} else {
						System.out.println(letter.getValue());
					}
				});

				timer.scheduleAtFixedRate(new TimerTask() {
					@Override
					public void run() {
						Letter l = bag_of_letters_ce.get(new Random().nextInt(bag_of_letters_ce.size()));
						
					}
				}, 0, 1000);
				
				System.out.println();
			}
		};
		click_event.addAction(click_action);
		test_box.addComponent(click_event);
		entityManager.addEntity(stateID, test_box);

		// Border
		left_bound = setLeftBound();
		right_bound = setRightBound();

		// Bar
		bar = setBar();

		CollisionEvent touch_bound = new CollisionEvent();
		touch_bound.addAction(new Action() {

			@Override
			public void update(GameContainer container, StateBasedGame g, int i, Component c) {

				if (bar.collides(left_bound)) {

					bar.setPosition(new Vector2f(left_bound.getShape().getCenterX() + left_bound.getSize().getX() * 0.5f
							+ bar.getSize().getX() * 0.5f, bar.getPosition().getY()));
				}
				if (bar.collides(right_bound)) {
					bar.setPosition(
							new Vector2f(right_bound.getShape().getCenterX() - right_bound.getSize().getX() * 0.5f
									- bar.getSize().getX() * 0.5f, bar.getPosition().getY()));
				}
			}
		});
		bar.addComponent(touch_bound);
		
		GameplayState.bag_of_letters.forEach((letter) -> {
			Vector2f tv = new Vector2f(120 + new Random().nextInt(460), 90);
			Letter l = new Letter(letter, letter.charAt(0), tv);
			letterDown(l);
			bag_of_letters_ce.add(l);
		});
		
		LoopEvent fall = new LoopEvent();
		fall.addAction(new MoveDownAction(0.1f));
		bag_of_letters_ce.get(0).addComponent(fall);
	
	}

	public void letterDown(Letter l) {
		entityManager.addEntity(stateID, l);
	}

	public Entity setLeftBound() {
		Entity left_bound = new Entity("left_bound");

		left_bound.setPosition(new Vector2f(100, 360));
		left_bound.setSize(new Vector2f(5, 720));

		left_bound.setVisible(false);
		left_bound.setPassable(false);

		entityManager.addEntity(stateID, left_bound);

		return left_bound;
	}

	public Entity setRightBound() {

		Entity right_bound = new Entity("right_bound");

		right_bound.setPosition(new Vector2f(600, 360));
		right_bound.setSize(new Vector2f(5, 720));

		right_bound.setVisible(false);
		right_bound.setPassable(false);

		entityManager.addEntity(stateID, right_bound);

		return right_bound;
	}

	public Entity setBar() throws SlickException {
		Entity bar = new Entity("bar");
		bar.setScale(0.5f);
		bar.setPosition(new Vector2f(350, 707.5f));
		bar.setVisible(true);
		bar.setPassable(false);

		KeyDownEvent move_right = new KeyDownEvent(Keyboard.KEY_RIGHT);
		move_right.addAction(new MoveRightAction(0.5f));
		bar.addComponent(move_right);

		KeyDownEvent move_left = new KeyDownEvent(Keyboard.KEY_LEFT);
		move_left.addAction(new MoveLeftAction(0.5f));
		bar.addComponent(move_left);
	
		bar.addComponent(new ImageRenderComponent(new Image("assets/scrabble/ui/bar.png")));
		entityManager.addEntity(stateID, bar);

		return bar;
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics graphic) throws SlickException {
		entityManager.renderEntities(container, game, graphic);

		graphic.setColor(new Color(255, 255, 255));
		Shape s = new Rectangle(100, 70, 500, 650);
//		graphic.fillRect(s.getX(), s.getY(), s.getWidth(), s.getHeight());
		graphic.draw(s);
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		entityManager.updateEntities(container, game, delta);
	}

	@Override
	public int getID() {
		return stateID;
	}

}
