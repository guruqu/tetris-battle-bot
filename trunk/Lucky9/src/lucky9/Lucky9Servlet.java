package lucky9;

import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.CharBuffer;
import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.channel.ChannelMessage;
import com.google.appengine.api.channel.ChannelService;
import com.google.appengine.api.channel.ChannelServiceFactory;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

@SuppressWarnings("serial")
public class Lucky9Servlet extends HttpServlet {

	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		UserService userService = UserServiceFactory.getUserService();
		User currentUser = userService.getCurrentUser();
		EntityManager em = EMFService.get().createEntityManager();

		if (currentUser == null) {
			resp.getWriter().println(
					"<p>Please <a href=\""
							+ userService.createLoginURL(req.getRequestURL()
									.toString()) + "\">sign in</a>.</p>");
			return;
		}
		
		String gameIdParam = req.getParameter("gameId");
		Long gameId = null;
		try{
			gameId = Long.parseLong(gameIdParam);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		Game game = null;
		if(gameId == null){
			game = new Game();
			em.persist(game);
		}else{
			game = em.find(Game.class, gameId);
		}
		
		if (game == null) {
			return;
		}

		String userId = currentUser.getUserId();
		if (!game.getPlayerMap().containsKey(userId)) {
			if (game.getPlayers().size() < 13) {
				MessageService.sendToAllPlayers(game, "UPDATE");
				game.joinPlayer(new Player(currentUser.getNickname(), userId));
			}
		}

		ChannelService channelService = ChannelServiceFactory
				.getChannelService();
		String token = channelService.createChannel(game.getId() + userId);

		FileReader reader = new FileReader("index_template.jsp");
		CharBuffer buffer = CharBuffer.allocate(16384);
		reader.read(buffer);
		String index = new String(buffer.array());
		index = index.replaceAll("\\{\\{ game_key \\}\\}", "GAME");
		index = index.replaceAll("\\{\\{ me \\}\\}", userId);
		index = index.replaceAll("\\{\\{ token \\}\\}", token);
		index = index.replaceAll("\\{\\{ game_link \\}\\}",
				getGameUri(req, game.getId()));

		resp.setContentType("text/html");
		resp.getWriter().write(index);
		reader.close();
	}

	private String getGameUri(HttpServletRequest req, Long gameId)
			throws IOException {
		try {
			String query;
			if (gameId == null) {
				query = "";
			} else {
				query = "gameId=" + gameId;
			}
			URI thisUri = new URI(req.getRequestURL().toString());
			URI uriWithOptionalGameParam = new URI(thisUri.getScheme(),
					thisUri.getUserInfo(), thisUri.getHost(),
					thisUri.getPort(), thisUri.getPath(), query, "");
			return uriWithOptionalGameParam.toString();
		} catch (URISyntaxException e) {
			throw new IOException(e.getMessage());
		}

	}
}
