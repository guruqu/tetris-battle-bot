package lucky9;

import com.google.appengine.api.channel.ChannelMessage;
import com.google.appengine.api.channel.ChannelService;
import com.google.appengine.api.channel.ChannelServiceFactory;
import com.google.appengine.api.datastore.KeyFactory;

public class MessageService {
	private static ChannelService channelService = ChannelServiceFactory.getChannelService();

	public static void sendToAllPlayers(Game game, String message) {

		for (Player player : game.getPlayers()) {
			String channelKey = game.getId() + "_" + player.getId();
			channelService.sendMessage(new ChannelMessage(channelKey, message));
		}
	}
}
