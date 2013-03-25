/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lucky9;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * 
 * @author Administrator
 */
@Entity
public class Player {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name = "";
	private boolean banker = false;
	private int credit = 0;
	private int bet = 0;
	private List<Card> cards = new ArrayList<Card>();
	private boolean turn = false;
	private Status status = Status.EVEN;
	private String ip = "";

	public Player(String name, String ip) {
		this.name = name;
		this.ip = ip;
	}

	public void setCredit(int credit) {
		this.credit = credit;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIp() {
		return ip;
	}

	public boolean isTurn() {
		return turn;
	}

	public void setTurn(boolean turn) {
		this.turn = turn;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Status getStatus() {
		return status;
	}

	public List<Card> getCards() {
		return cards;
	}

	public void clearHand() {
		status = Status.READY;
		cards = new ArrayList<Card>();
	}

	public String getName() {
		return name;
	}

	public boolean isBanker() {
		return banker;
	}

	public int getCredit() {
		return credit;
	}

	public void addCredit(int value) {
		credit += value;
	}

	public int getScore() {
		int score = 0;
		for (Card card : cards) {
			score += card.getType().value();
		}
		String str = score + "";
		String x = str.substring(str.length() - 1);
		return Integer.parseInt(x);
	}

	public void setBet(int bet) {
		if (!banker) {
			this.bet = bet;
		}
	}

	public int getBet() {
		return bet;
	}

	public void setBanker(boolean b) {
		if (b) {
			bet = 0;
		}
		banker = b;
	}

	public static enum Status {

		WIN, LOSS, BETTING, READY, EVEN, HIT, STAND
	}
	
	public Long getId() {
		return id;
	}
}
