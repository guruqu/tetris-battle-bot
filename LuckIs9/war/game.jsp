<%@page import="nac.luckisnine.Player"%>
<%@page import="nac.luckisnine.MessageService"%>
<%@page import="nac.luckisnine.Game"%>
<%@page import="nac.luckisnine.EMFService"%>
<%@page import="com.google.appengine.api.users.UserService"%>
<%@page import="com.google.appengine.api.channel.ChannelServiceFactory"%>
<%@page import="com.google.appengine.api.channel.ChannelService"%>
<%@page import="com.google.appengine.api.datastore.KeyFactory"%>
<%@page import="javax.persistence.EntityManager"%>
<%@page import="com.google.appengine.api.users.User"%>
<%@page import="com.google.appengine.api.users.UserServiceFactory"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
	UserService userService = UserServiceFactory.getUserService();
	User currentUser = userService.getCurrentUser();
	EntityManager em = EMFService.get().createEntityManager();
	Game game = null;
	String token = "";

	if (currentUser == null) {
		response.getWriter().println(
				"<p>Please <a href=\""
						+ userService.createLoginURL(request
								.getRequestURL().toString())
						+ "\">sign in</a>.</p>");
		return;
	}

	String gameId = request.getParameter("id");
	if (gameId == null) {
		em.getTransaction().begin();
		game = new Game();
		System.out.print("ddddd");
		em.persist(game);
		em.flush();
		em.getTransaction().commit();
	} else {
		game = em.find(Game.class, KeyFactory.stringToKey(gameId));
	}

	System.out.println("game:" + game);
	System.out.println("game:" + game.getId());

	if (game == null) {
		return;
	}

	String userId = currentUser.getUserId();
	if (!game.getPlayerMap().containsKey(userId)) {
		if (game.getPlayers().size() < 13) {
			MessageService.sendToAllPlayers(game, "UPDATE");
			Player p = new Player(currentUser.getNickname());
			em.getTransaction().begin();
			em.persist(p);
			em.flush();
			em.getTransaction().commit();
			System.out.println("p:" + p.getId());
			game.joinPlayer(p);
		}
	}

	ChannelService channelService = ChannelServiceFactory
			.getChannelService();
	token = channelService.createChannel(game.getId() + userId);
%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Lucky 9</title>
<script src='/_ah/channel/jsapi'></script>
<script src="jquery.js"></script>
<script>
	var promptOpened = false;
	var gameId = '<%=game.getId()%>'
	function askBet() {
		if (promptOpened == false) {
			promptOpened = true;
			var value = prompt("Bet: ");
			$.post("command", {
				command : "BET",
				game : gameId,
				bet : value
			}, function(data) {
			});
		}
	}

	$(document).ready(function() {
// 		update();
		$('#start-game').click(function() {
			$.post("command", {
				command : "START",
				game : gameId
			}, function(data) {
			});
		});

		$('#hit-me').click(function() {
			$.post("command", {
				command : "HIT_ME",
				game : gameId
			}, function(data) {
			});
		});
		$('#stand').click(function() {
			$.post("command", {
				command : "STAND",
				game : gameId
			}, function(data) {
			});
		});
		$('.set-banker').live("click", function() {
			var btn = $(this);
			$.post("command", {
				command : "SET_BANKER",
				ip : btn.data("ip"),
				game : gameId
			}, function(data) {
			});
		});

		initialize();
	});

	update = function() {
		$.get("gamestate.jsp", function(data) {
			$('#gameState').html(data);
		});
	};

	onMessage = function(m) {
		console.log(m);
// 		update();
	}

	initialize = function() {
		var token = '<%=token%>
	';
		var channel = new goog.appengine.Channel(token);
		var handler = {
			'onopen' : function(e) {
				console.log("opened");
			},
			'onmessage' : onMessage,
			'onerror' : function(e) {
				console.log("error: " + e);
			},
			'onclose' : function(e) {
				console.log("closed");
			}
		};
		var socket = channel.open(handler);
		socket.onmessage = onMessage;
	}
</script>
<link href="index.css" rel="stylesheet">
<link href="cards.css" rel="stylesheet">
</head>
<body>
	<a href='game.jsp?id=<%=game.getId()%>'>game.jsp?id=<%=game.getId()%></a>
	<div id="controls">
		<button id="start-game">Start Game</button>
		<button id="hit-me" style="margin: 0px 100px;">Hit Me!</button>
		<button id="stand">Stand</button>
	</div>
	<div id="gameState"></div>
</body>
</html>
