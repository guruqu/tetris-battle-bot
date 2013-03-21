<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Lucky 9</title>
<script src='/_ah/channel/jsapi'></script>
<script src="jquery.js"></script>
<script>
	var promptOpened = false;
	var gameKey = '{{ game_key }}'
	function askBet() {
		if (promptOpened == false) {
			promptOpened = true;
			var value = prompt("Bet: ");
			$.post("command", {
				command : "BET",
				game : gameKey,
				bet : value
			}, function(data) {
			});
		}
	}

	$(document).ready(function() {
		update();
		$('#start-game').click(function() {
			$.post("command", {
				command : "START",
				game : gameKey
			}, function(data) {
			});
		});

		$('#hit-me').click(function() {
			$.post("command", {
				command : "HIT_ME",
				game : gameKey
			}, function(data) {
			});
		});
		$('#stand').click(function() {
			$.post("command", {
				command : "STAND",
				game : gameKey
			}, function(data) {
			});
		});
		$('.set-banker').live("click", function() {
			var btn = $(this);
			$.post("command", {
				command : "SET_BANKER",
				ip : btn.data("ip"),
				game : gameKey
			}, function(data) {
			});
		});

		initialize();
	});
	
	update = function(){
		$.get("game.jsp", function(data) {
			$('#gameState').html(data);
		});
	};
	
	onMessage = function(m) {
		console.log(m);
		update();
	}

	initialize = function() {
		var token = '{{ token }}';
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
	<a href="{{ game_link }}"></a>
	<div id="controls">
		<button id="start-game">Start Game</button>
		<!--      <button id="cash-out" style="margin-right:100px;">Cash Out</button>-->
		<button id="hit-me" style="margin: 0px 100px;">Hit Me!</button>
		<button id="stand">Stand</button>
	</div>
	<div id="gameState"></div>
</body>
</html>
