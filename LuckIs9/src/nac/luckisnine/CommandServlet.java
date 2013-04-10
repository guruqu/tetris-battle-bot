/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nac.luckisnine;

import java.io.IOException;
import java.io.PrintWriter;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

/**
 * 
 * @author nathaniel
 */
public class CommandServlet extends HttpServlet {

	private static final long serialVersionUID = -2435473543397050318L;

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();

		final UserService userService = UserServiceFactory.getUserService();
		User currentUser = userService.getCurrentUser();

		if (currentUser == null) {
			return;
		}

		String gameIdParam = request.getParameter("gameId");
		EntityManager em = EMFService.get().createEntityManager();
		Game game = em.find(Game.class, Long.parseLong(gameIdParam));

		if (game == null) {
			return;
		}

		Player me = game.getPlayerMap().get(currentUser.getUserId());
		Command command = Command.valueOf(request.getParameter("command"));

		try {
			if (game != null && me != null) {

				switch (command) {
				case SET_BANKER:
					String ip2 = request.getParameter("ip");
					if (ip2 != null && ip2.length() > 0 && me.isBanker()) {
						game.setBanker(game.getPlayerMap().get(ip2));
					}
					break;
				case HIT_ME:
					if (me.isTurn()) {
						game.turn(me, true);
						me.setStatus(Player.Status.HIT);
					}
					break;
				case STAND:
					if (me.isTurn()) {
						game.turn(me, false);
						me.setStatus(Player.Status.STAND);
					}
					break;
				case BET:
					String bet = request.getParameter("bet");
					if (bet != null && bet.length() > 0) {
						if (game.getState() == Game.State.BETTING) {
							int betAmount = 0;
							try {
								betAmount = Integer.parseInt(bet);
							} catch (Exception e) {
							}
							if (betAmount > 0) {
								me.setStatus(Player.Status.READY);
								me.setBet(betAmount);
							}
							int count = 0;
							for(Player player: game.getPlayers()){
								if(player.getBet() > 0){
									count++;
								}
							}
							if(count >= game.getPlayers().size() - 1){
								game.startTurns();
							}
						}
						
					}
					break;
				case START:
					if (me.isBanker()) {
						game.startBetting();
					}
					break;
				}
				MessageService.sendToAllPlayers(game, "UPDATE");
			}

		} catch (Exception e) {
			e.printStackTrace();
			out.print(e.toString());
		} finally {
			out.close();
		}
	}

	private static enum Command {
		SET_BANKER, HIT_ME, STAND, BET, START
	}
}
