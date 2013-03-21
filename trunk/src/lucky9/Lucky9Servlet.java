package lucky9;

import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.CharBuffer;
import java.util.List;

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
		final UserService userService = UserServiceFactory.getUserService();
		User currentUser = userService.getCurrentUser();

		if (currentUser == null) {
			resp.getWriter().println(
					"<p>Please <a href=\""
							+ userService.createLoginURL(req.getRequestURL()
									.toString()) + "\">sign in</a>.</p>");
			return;
		}

		Game game = (Game) getServletContext().getAttribute("GAME");
		if (game == null) {
			game = new Game();
			getServletContext().setAttribute("GAME", game);
		}

		String userId = currentUser.getUserId();
		if (!game.getPlayerMap().containsKey(userId)) {
			if (game.getPlayers().size() < 13) {
				List<Player> players = game.getPlayers();
				ChannelService channelService = ChannelServiceFactory
						.getChannelService();
				for (Player player : players) {
					String channelKey = "GAME_" + player.getIp();
					channelService.sendMessage(new ChannelMessage(channelKey,
							"TEST"));
				}
				game.joinPlayer(new Player(currentUser.getNickname(), userId));
			}
		}

		ChannelService channelService = ChannelServiceFactory
				.getChannelService();
		String token = channelService.createChannel("GAME_" + userId);

		FileReader reader = new FileReader("index_template.jsp");
		CharBuffer buffer = CharBuffer.allocate(16384);
		reader.read(buffer);
		String index = new String(buffer.array());
		index = index.replaceAll("\\{\\{ game_key \\}\\}", "GAME");
		index = index.replaceAll("\\{\\{ me \\}\\}", userId);
		index = index.replaceAll("\\{\\{ token \\}\\}", token);
		index = index.replaceAll("\\{\\{ game_link \\}\\}",
				getGameUri(req, "GAME"));

		resp.setContentType("text/html");
		resp.getWriter().write(index);
		reader.close();
	}

	private String getGameUri(HttpServletRequest req, String gameKey)
			throws IOException {
		try {
			String query;
			if (gameKey == null) {
				query = "";
			} else {
				query = "game=" + gameKey;
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
