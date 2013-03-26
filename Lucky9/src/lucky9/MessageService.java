package lucky9;

import com.google.appengine.api.channel.ChannelMessage;
import com.google.appengine.api.channel.ChannelService;
import com.google.appengine.api.channel.ChannelServiceFactory;

public class MessageService {
	private static ChannelService channelService = ChannelServiceFactory
			.getChannelService();
	public static void sendToAllPlayers(Game game, String message){
	
		for (Player player : game.getPlayers()) {
			String channelKey = game.getKey() + player.getIp();
			channelService.sendMessage(new ChannelMessage(channelKey,
					message));
		}
	}
}
