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
import de.tud.jsf.scrabble.model.player.Players;
import de.tud.jsf.scrabble.ui.entity.DialogueButton;
import de.tud.jsf.scrabble.ui.entity.Letter;

public class CEState extends BasicGameState implements GameParameters {

	private int stateID;
	private StateBasedEntityManager entityManager;

	private Entity left_bound, right_bound, bar;
	private ArrayList<Letter> bag_of_letters_ce = new ArrayList<Letter>();
	private ArrayList<Letter> falling_letters = new ArrayList<Letter>();
	private ArrayList<Letter> dead_letters = new ArrayList<Letter>();
	private ArrayList<Letter> redundant = new ArrayList<Letter>();

	private ArrayList<String> taken_letters = new ArrayList<String>();

	private Timer timer;
	private ArrayList<Entity> current_display_taken_letter = new ArrayList<Entity>();

	// Button
	private boolean return_clickable = false;
	static boolean play_clickable = false;
	private boolean difficulty_clickable = true;

	private DialogueButton return_button;
	Vector2f difficulty_pos = new Vector2f(825, 180);
	private DialogueButton difficulty = new DialogueButton("difficulty_button", difficulty_pos, "easy");
	private boolean difficult = false;

	// Gameplay
	static int limit;
	static int current_player;

	// Warning text
	static String warning_text = "";

	CEState(int sid) {
		this.stateID = sid;
		entityManager = StateBasedEntityManager.getInstance();
	}

	public int getTakenLettersSize() {
		return taken_letters.size();
	}

	public boolean getReturnClickable() {
		return return_clickable;
	}

	@Override
	public void init(GameContainer arg0, StateBasedGame arg1) throws SlickException {
		Entity background = new Entity("background");
		background.setPosition(new Vector2f(WINDOW_WIDTH / 2, WINDOW_HEIGHT / 2));
		if (!Launch.debug)
			background.addComponent(new ImageRenderComponent(new Image(BACKGROUND)));
		entityManager.addEntity(stateID, background);

		// Hanging letters
		GameplayState.bag_of_letters.forEach((letter) -> {
			renderHangingLetter(letter);
		});

		// Start button
		DialogueButton play_button = new DialogueButton("play_button", new Vector2f(770, 120), "start");
		play_button.addImageComponent();

		ANDEvent play_start = new ANDEvent(new MouseEnteredEvent(), new MouseClickedEvent());
		Action click_action = new Action() {
			@Override
			public void update(GameContainer arg0, StateBasedGame arg1, int arg2, Component arg3) {
				if (!play_clickable)
					return;
				play_clickable = false;
				difficulty_clickable = false;
				warning_text = "";

				bag_of_letters_ce.forEach((letter) -> {
					if (!GameplayState.bag_of_letters.contains(letter.getID())) {
						redundant.add(letter);
					}
				});

				redundant.forEach((letter) -> {
					bag_of_letters_ce.remove(letter);
				});

				timer = new Timer();

				timer.scheduleAtFixedRate(new TimerTask() {
					@Override
					public void run() {
						Letter l = bag_of_letters_ce.get(new Random().nextInt(bag_of_letters_ce.size()));
						bag_of_letters_ce.remove(l);
						falling_letters.add(l);
						dropping(l);
						if (taken_letters.size() >= limit || bag_of_letters_ce.size() <= 0) {
							stopCEGame();
							warning_text = "Minigame complete. Click the RETURN button to return to the game!!!";
						}
					}
				}, 0, 1000);

			}
		};
		play_start.addAction(click_action);
		play_button.addComponent(play_start);

		entityManager.addEntity(stateID, play_button);

		// Return to Gameplay State
		return_button = new DialogueButton("return_button", new Vector2f(880, 120), "return");
		return_button.addImageComponent();

		triggerReturn(return_button);
		entityManager.addEntity(stateID, return_button);

		// Difficulty button

		difficulty = new DialogueButton("difficulty_button", difficulty_pos, "easy");
		difficulty.addImageComponent();
		entityManager.addEntity(stateID, difficulty);
		triggerDifficulty(difficulty);

		// Border
		left_bound = setLeftBound();
		right_bound = setRightBound();

		// Bar
		bar = setBar();
	}

	public void dropping(Letter l) {
		Vector2f start_pos = l.getPosition();
		l.setVisible(true);
		Random random = new Random();
		int rotation;

		// Movement
		if (difficult) {
			rotation = random.nextInt(91) - 45;
		} else {
			rotation = 0;
		}
		float speed = 0.15f * (random.nextInt(2) + 1);

		MoveDownAction move_down = new MoveDownAction(speed * (float) Math.cos(Math.toRadians(rotation)));
		MoveRightAction move_right = new MoveRightAction(speed * (float) Math.sin(Math.toRadians(rotation)));

		LoopEvent loop = new LoopEvent();
		loop.addAction(move_down);
		loop.addAction(move_right);

		l.setIntRotation(rotation);
		l.setSpeed(speed);
		l.addComponent(loop);

		// Collision with bounds

		CollisionEvent touch_bound = new CollisionEvent();
		touch_bound.addAction(new Action() {
			@Override
			public void update(GameContainer arg0, StateBasedGame arg1, int arg2, Component arg3) {
				if (l.collides(left_bound) || l.collides(right_bound)) {
					l.setIntRotation(-l.getIntRotation());
					loop.clearActions();
					calculateMovement(l).forEach((a) -> {
						loop.addAction(a);
					});
				}
			}
		});

		l.addComponent(touch_bound);

		// Collision with letters

		CollisionEvent touch_letter = new CollisionEvent();
		touch_letter.addAction(new Action() {
			@Override
			public void update(GameContainer arg0, StateBasedGame arg1, int arg2, Component arg3) {
				if (l.collides(left_bound) || l.collides(right_bound) || l.collides(bar)) {
					return;
				}
				if (falling_letters.contains(touch_letter.getCollidedEntity())) {
					l.setIntRotation(0);

					loop.clearActions();
					calculateMovement(l).forEach((a) -> {
						loop.addAction(a);
					});
				}
			}
		});

		l.addComponent(touch_letter);
		LoopEvent delete = new LoopEvent();
		CollisionEvent touch_bar = new CollisionEvent();

		// Touching bar
		touch_bar.addAction(new Action() {
			@Override
			public void update(GameContainer arg0, StateBasedGame arg1, int arg2, Component arg3) {
				if (l.collides(bar)) {
					l.setPosition(start_pos);
					taken_letters.add(l.getID());

					redundant.add(l);
					l.setPosition(start_pos);
					if (!dead_letters.contains(l)) {
						dead_letters.add(l);
					}
					falling_letters.remove(l);
					l.setVisible(false);

					renderTakenLetter();

					l.removeComponent(loop);
					l.removeComponent(touch_bound);
					l.removeComponent(touch_letter);
					l.removeComponent(delete);
					l.removeComponent(touch_bar);
				}
			}
		});
		l.addComponent(touch_bar);

		// Delete
		delete.addAction(new Action() {
			@Override
			public void update(GameContainer arg0, StateBasedGame arg1, int arg2, Component arg3) {
				if (l.getPosition().getY() > arg0.getHeight() || l.getPosition().getY() < 0
						|| l.getPosition().getX() > arg0.getWidth() || l.getPosition().getX() < 0
						|| taken_letters.size() == limit || bag_of_letters_ce.size() <= 0) {
					l.setPosition(start_pos);
					if (!dead_letters.contains(l)) {
						dead_letters.add(l);
					}
					falling_letters.remove(l);
					l.setVisible(false);

					l.removeComponent(loop);
					l.removeComponent(touch_bound);
					l.removeComponent(touch_letter);
					l.removeComponent(delete);
					l.removeComponent(touch_bar);
				}
			}
		});

		l.addComponent(delete);
	}

	public ArrayList<Action> calculateMovement(Letter l) {
		int rotation = l.getIntRotation();
		float speed = l.getSpeed();

		MoveDownAction move_down = new MoveDownAction(speed * (float) Math.cos(Math.toRadians(rotation)));
		MoveRightAction move_right = new MoveRightAction(speed * (float) Math.sin(Math.toRadians(rotation)));

		ArrayList<Action> result = new ArrayList<Action>();

		result.add(move_right);
		result.add(move_down);

		return result;
	}

	public void triggerReturn(DialogueButton button) {
		ANDEvent return_to_gameplay_event = new ANDEvent(new MouseEnteredEvent(), new MouseClickedEvent());
		Action new_Game_Action = new ChangeStateAction(Launch.GAMEPLAY_STATE);
		return_to_gameplay_event.addAction(new Action() {
			@Override
			public void update(GameContainer arg0, StateBasedGame arg1, int arg2, Component arg3) {
				if (!return_clickable) {
					warning_text = "You must complete the CE Minigame before returning to the Scrabble board.";
					return_to_gameplay_event.removeAction(new_Game_Action);
					button.removeComponent(return_to_gameplay_event);
					return;
				}

				current_display_taken_letter.forEach((l) -> {
					entityManager.removeEntity(stateID, l);
				});

				dead_letters.forEach((l) -> {
					if (!bag_of_letters_ce.contains(l)) {
						bag_of_letters_ce.add(l);
					}

				});

				redundant.forEach((l) -> {
					if (!bag_of_letters_ce.contains(l)) {
						bag_of_letters_ce.add(l);
					}
				});

				falling_letters.forEach((l) -> {
					if (!bag_of_letters_ce.contains(l)) {
						bag_of_letters_ce.add(l);
					}
				});

				taken_letters.forEach((l) -> {
					GameplayState.bag_of_letters.remove(l);
				});

				falling_letters.clear();
				redundant.clear();
				dead_letters.clear();
				taken_letters.clear();
				bar.setPosition(new Vector2f(350, 707.5f));
				if (difficulty.getType() == "hard") {
					difficult = false;
					difficulty.setType("easy");
					difficulty.addImageComponent();
				}

				warning_text = "";
				return_clickable = false;
				difficulty_clickable = true;
			}
		});
		return_to_gameplay_event.addAction(new_Game_Action);
		button.addComponent(return_to_gameplay_event);
	}

	public void triggerDifficulty(DialogueButton button) {
		ANDEvent clickEvent = new ANDEvent(new MouseEnteredEvent(), new MouseClickedEvent());
		Action clickOnButton = new Action() {
			@Override
			public void update(GameContainer arg0, StateBasedGame arg1, int arg2, Component arg3) {
				if (!difficulty_clickable) {
					return;
				}
				if (button.getType() == "easy") {
					difficult = true;
					button.setType("hard");
					button.addImageComponent();
				} else if (button.getType() == "hard") {
					difficult = false;
					button.setType("easy");
					button.addImageComponent();
				}
			}
		};
		clickEvent.addAction(clickOnButton);
		button.addComponent(clickEvent);
	}

	public void renderHangingLetter(String letter) {

		Vector2f tv = new Vector2f(120 + new Random().nextInt(460), 90);
		Letter l = new Letter(letter, letter.charAt(0), tv, false);
		l.setVisible(false);
		l.setPassable(true);
		l.setPosition(tv);

		l.setScale(LETTER_SCALE_FACTOR);

		ImageRenderComponent image = null;

		if (!Launch.debug) {
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
		}

		entityManager.addEntity(stateID, l);
		bag_of_letters_ce.add(l);
	}

	public void renderTakenLetter() {
		for (int i = 0; i < taken_letters.size(); i++) {
			Vector2f tv;
			if (i < 4) {
				tv = new Vector2f(750 + 50 * i, 400);
			} else {
				tv = new Vector2f(925 - 50 * (7 - i), 450);
			}
			Letter l = new Letter(taken_letters.get(i), taken_letters.get(i).charAt(0), tv, false);
			current_display_taken_letter.add(l);
			entityManager.addEntity(stateID, l);
		}
	}

	public void stopCEGame() {
		System.out.println("CE game stopped. Traded letters: ");
		taken_letters.forEach((l) -> {
			System.out.print(l + ", ");
		});
		System.out.println("Player to watch: " + Players.currentPlayer.getID());
		timer.cancel();
		play_clickable = false;
		return_clickable = true;
		for (int i = 0; i < Players.getNumberOfPlayers(); i++) {
			if (Players.getPlayers().get(i).getID() == current_player) {
				for (int j = 0; j < taken_letters.size(); j++) {
					Players.getPlayers().get(i).addLetter(taken_letters.get(j));
				}
			}
		}

		triggerReturn(return_button);
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
		bar.setPassable(true);
		bar.setSize(new Vector2f(190 * 0.5f, 45 * 0.5f));

		KeyDownEvent move_right = new KeyDownEvent(Keyboard.KEY_RIGHT);
		move_right.addAction(new MoveRightAction(0.5f));
		bar.addComponent(move_right);

		KeyDownEvent move_left = new KeyDownEvent(Keyboard.KEY_LEFT);
		move_left.addAction(new MoveLeftAction(0.5f));
		bar.addComponent(move_left);

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

		if (!Launch.debug)
			bar.addComponent(new ImageRenderComponent(new Image("assets/scrabble/ui/bar.png")));
		entityManager.addEntity(stateID, bar);

		return bar;
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics graphic) throws SlickException {
		entityManager.renderEntities(container, game, graphic);

		int[] players_ui = new int[Players.getNumberOfPlayers()];
		int[] players_ui_score = new int[Players.getNumberOfPlayers()];
		for (int i = 0; i < Players.getNumberOfPlayers(); i++) {
			players_ui[i] = Players.getPlayers().get(i).getID();
			players_ui_score[i] = Players.getPlayers().get(i).getScore();
		}

		for (int i = 0; i < players_ui.length; i++) {
			if (players_ui[i] == current_player) {
				graphic.setColor(new Color(255, 0, 0));
				graphic.drawString("Player " + Players.getPlayers().get(i).getName() + ": " + players_ui_score[i],
						90 + 160 * i, 50);
			} else {
				graphic.setColor(new Color(0, 0, 0));
				graphic.drawString("Player " + Players.getPlayers().get(i).getName() + ": " + players_ui_score[i],
						90 + 160 * i, 50);
			}
		}

		graphic.setColor(new Color(255, 255, 255));
		Shape s = new Rectangle(100, 70, 500, 650);
		graphic.draw(s);

		graphic.setColor(new Color(255, 0, 0));
		graphic.drawString(warning_text, 90, 10);
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
