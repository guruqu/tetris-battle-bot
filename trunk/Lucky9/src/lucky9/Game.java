/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lucky9;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lucky9.Card.Suit;
import lucky9.Card.Type;

/**
 * 
 * @author Administrator
 */
@Entity
public class Game {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private List<Player> players = new ArrayList<Player>();
	private Map<String, Player> playerMap = new HashMap<String, Player>();
	private List<Card> deck = new ArrayList<Card>(40);
	private Player currentPlayer = null;
	private Player banker = null;
	private Timer timer = null;
	private State state = State.READY;

	public List<Card> getDeck() {
		return deck;
	}

	private void shuffle() {
		deck = new ArrayList<Card>(40);
		for (Suit suit : Suit.values()) {
			for (Type type : Type.values()) {
				deck.add(new Card(suit, type));
			}
		}
		Collections.shuffle(deck);
	}

	public void setBanker(Player player) {
		if (state == State.READY && player.getBet() == 0) {
			if (playerMap.containsKey(player.getIp())) {
				if (banker != null) {
					banker.setBanker(false);
				}
				banker = playerMap.get(player.getIp());
				banker.setBanker(true);
			}
		}
	}

	public Map<String, Player> getPlayerMap() {
		return playerMap;
	}

	public void joinPlayer(Player player) {
		if (!playerMap.containsKey(player.getIp())) {
			players.add(player);
			playerMap.put(player.getIp(), player);
			if (players.size() == 1) {
				setBanker(player);
			}
		}
	}

	public void leavePlayer(Player player) {
		if (player.getCredit() == 0) {
			if (player == banker) {
				banker = null;
			}
			players.remove(player);
			playerMap.remove(player.getIp());
		}
	}

	public void startBetting() {
		if (state == State.READY) {
			state = State.BETTING;
		}
	}

	public State getState() {
		return state;
	}

	public void startTurns() {
		if (state == State.BETTING) {
			state = State.TURNS;
			shuffle();
			for (Player player : players) {
				player.clearHand();
				if (player.getBet() > 0 || player.isBanker()) {
					player.getCards().add(deck.remove(0));
				}
			}
			for (Player player : players) {
				if (player.getBet() > 0 || player.isBanker()) {
					player.getCards().add(deck.remove(0));
				}
			}
			if (banker.getScore() == 9) {
				endGame();
			} else {
				currentPlayer = getNextPlayer(banker);
			}
		}
	}

	private Player getNextPlayer(Player p) {
		for (Player player : players) {
			player.setTurn(false);
		}
		Player next;
		int index = players.indexOf(p) + 1;
		if (index >= players.size()) {
			next = players.get(0);
		} else {
			next = players.get(index);
		}
		if (!next.isBanker() && next.getBet() <= 0) {
			next = getNextPlayer(next);
		}
		next.setTurn(true);
		return next;
	}

	public void turn(Player player, boolean hit) {

		if (state == State.TURNS && currentPlayer == player) {
			if (timer != null) {
				timer.cancel();
				timer.purge();
			}
			if (hit == true) {
				Card card = deck.remove(0);
				currentPlayer.getCards().add(card);
			}
			if (currentPlayer == banker) {
				endGame();
			} else {
				currentPlayer = getNextPlayer(currentPlayer);
			}
		}
	}

	public void endGame() {
		if (state == State.TURNS) {
			state = State.READY;
			for (Player player : players) {
				if (player != banker && player.getBet() > 0) {
					if (player.getScore() > banker.getScore()) {
						player.addCredit(player.getBet());
						banker.addCredit(-player.getBet());
						player.setStatus(Player.Status.WIN);
					} else if (player.getScore() < banker.getScore()) {
						banker.addCredit(player.getBet());
						player.addCredit(-player.getBet());
						player.setStatus(Player.Status.LOSS);
					} else {
						player.setStatus(Player.Status.EVEN);
					}
				}
				player.setBet(0);
			}
		}
	}

	public List<Player> getPlayers() {
		return players;
	}

	public static enum State {

		READY, BETTING, TURNS
	}
	
	public Long getId() {
		return id;
	}
}
