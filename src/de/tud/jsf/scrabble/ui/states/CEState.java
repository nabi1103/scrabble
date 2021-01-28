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
	private ArrayList<Entity> bag_of_letters_ce = new ArrayList<Entity>();

	private ArrayList<String> taken_letter = new ArrayList<String>();

	private Timer timer;

	static boolean play_clickable = false;
	static int limit;
	static int current_player;

	CEState(int sid) {
		this.stateID = sid;
		entityManager = StateBasedEntityManager.getInstance();
	}

	@Override
	public void init(GameContainer arg0, StateBasedGame arg1) throws SlickException {
		// Hanging letters
		GameplayState.bag_of_letters.forEach((letter) -> {
			renderDroppableLetter(letter);
		});

		// Return to Gameplay State (testing only)
		Entity return_button = new Entity("return");

		return_button.setPosition(new Vector2f(850, 220));
		return_button.addComponent(new ImageRenderComponent(new Image("assets/scrabble/ui/grey_boxCross.png")));

		ANDEvent return_to_gameplay_event = new ANDEvent(new MouseEnteredEvent(), new MouseClickedEvent());
		Action new_Game_Action = new ChangeStateAction(Launch.GAMEPLAY_STATE);
		return_to_gameplay_event.addAction(new Action() {
			@Override
			public void update(GameContainer arg0, StateBasedGame arg1, int arg2, Component arg3) {
				timer.cancel();
//				bag_of_letters_ce.clear();
			}
		});
		return_to_gameplay_event.addAction(new_Game_Action);
		return_button.addComponent(return_to_gameplay_event);
		entityManager.addEntity(stateID, return_button);

		// Start button
		Entity play_button = new Entity("test_count");

		play_button.setPosition(new Vector2f(800, 220));
		play_button.addComponent(new ImageRenderComponent(new Image("assets/scrabble/ui/grey_boxCheckmark.png")));

		ANDEvent play_start = new ANDEvent(new MouseEnteredEvent(), new MouseClickedEvent());
		Action click_action = new Action() {
			@Override
			public void update(GameContainer arg0, StateBasedGame arg1, int arg2, Component arg3) {
				if (!play_clickable)
					return;

				play_clickable = false;

				bag_of_letters_ce.forEach((letter) -> {
					if (!GameplayState.bag_of_letters.contains(letter.getID())) {
						bag_of_letters_ce.remove(letter);
						entityManager.removeEntity(stateID, letter);
					}
				});

				timer = new Timer();

				timer.scheduleAtFixedRate(new TimerTask() {
					@Override
					public void run() {
						Entity l = bag_of_letters_ce.get(new Random().nextInt(bag_of_letters_ce.size()));
						bag_of_letters_ce.remove(l);
						dropping(l);
						if (taken_letter.size() >= limit || bag_of_letters_ce.size() <= 0) {
							stopCEGame();
						}
					}
				}, 0, 1000);

			}
		};
		play_start.addAction(click_action);
		play_button.addComponent(play_start);

		entityManager.addEntity(stateID, play_button);

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
	}

	public void dropping(Entity l) {
		Vector2f start_pos = l.getPosition();
		l.setVisible(true);
		LoopEvent loop = new LoopEvent();
		loop.addAction(new MoveDownAction(0.1f * (new Random().nextInt(3) + 1)));
		loop.addAction(new Action() {
			@Override
			public void update(GameContainer arg0, StateBasedGame arg1, int arg2, Component arg3) {
				if (l.getPosition().getY() > arg0.getHeight() || l.getPosition().getY() < 0
						|| taken_letter.size() == limit) {
					l.setPosition(start_pos);
					bag_of_letters_ce.add(l);
					l.removeComponent(loop);
					l.setVisible(false);
				}
			}
		});
		l.addComponent(loop);
	}

	public void renderDroppableLetter(String letter) {
		Entity l = new Entity(letter);
		Vector2f tv = new Vector2f(120 + new Random().nextInt(460), 90);
		l.setVisible(false);
		l.setPosition(tv);
		l.setPassable(false);
		l.setScale(LETTER_SCALE_FACTOR);

		ImageRenderComponent image = null;

		for (String path : LETTERS) {
			char image_id;
			image_id = path.charAt(30);
			if (letter.charAt(0) == Character.toLowerCase(image_id)) {
				try {
					image = new ImageRenderComponent(new Image(path));
					l.addComponent(image);
				} catch (SlickException e) {
					System.err.println("Cannot find file " + path);
					e.printStackTrace();
				}
			}
		}

		// Touching bar
		CollisionEvent touch_bar = new CollisionEvent();
		touch_bar.addAction(new Action() {
			@Override
			public void update(GameContainer arg0, StateBasedGame arg1, int arg2, Component arg3) {
				if (l.collides(bar)) {
					taken_letter.add(l.getID());
					bag_of_letters_ce.remove(l);
					entityManager.removeEntity(stateID, l);
					renderTakenLetter();
				}
			}
		});
		l.addComponent(touch_bar);

		entityManager.addEntity(stateID, l);
		bag_of_letters_ce.add(l);
	}

	public void renderTakenLetter() {
		for (int i = 0; i < taken_letter.size(); i++) {
			Vector2f tv;
			if (i < 4) {
				tv = new Vector2f(710 + 50 * i, 400);
			} else {
				tv = new Vector2f(880 - 50 * (7 - i), 450);
			}
			Letter l = new Letter(taken_letter.get(i), taken_letter.get(i).charAt(0), tv);
			entityManager.addEntity(stateID, l);
		}
	}

	public void stopCEGame() {
		System.out.println("CE game stopped. Traded letters: ");
		taken_letter.forEach((l) -> {
			System.out.print(l + ", ");
		});
		System.out.println("Player to watch: " + current_player);
		timer.cancel();
		for(int i = 0; i < 4; i++) {
			if(GameplayState.players[i].getID() == current_player) {
				for(int j = 0; j < taken_letter.size(); j++) {
					GameplayState.players[i].addLetter(taken_letter.get(j));
				}
			}
		}
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
