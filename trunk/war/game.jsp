<%-- 
    Document   : index
    Created on : Mar 15, 2013, 11:35:18 AM
    Author     : Administrator
--%>

<%@page import="com.google.appengine.api.users.User"%>
<%@page import="com.google.appengine.api.users.UserService"%>
<%@page import="com.google.appengine.api.users.UserServiceFactory"%>
<%@page import="com.google.appengine.api.channel.ChannelService"%>
<%@page import="com.google.appengine.api.channel.ChannelServiceFactory"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page import="lucky9.Card"%>
<%@page import="java.util.List"%>
<%@page import="lucky9.Player"%>
<%@page import="lucky9.Game"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
	Game game = (Game) application.getAttribute("GAME");
  
	final UserService userService = UserServiceFactory.getUserService();
	User currentUser = userService.getCurrentUser();
	  Player me = game.getPlayerMap().get(currentUser.getUserId());
  if (game != null && me != null) {
%>

<table border="2">
	<tr>
		<th colspan="2">Player</th>
		<th>Credit</th>
		<th>Bet</th>
		<th>Cards</th>
		<th>Score</th>
		<th>Status</th>
	</tr>
	<%
		List<Player> players = game.getPlayers();
			    for (Player player : players) {
			      String classes = "";

			      if (game.getState() == Game.State.TURNS) {
			        if (player.isTurn()) {
			          classes += "turn ";
			        }
			      } else if (game.getState() == Game.State.READY) {
			        if (!player.isBanker()) {
			          if (player.getStatus() == Player.Status.WIN) {
			            classes += "win ";
			          } else if (player.getStatus() == Player.Status.LOSS) {
			            classes += "loss";
			          }
			        }
			      }
	%>
	<tr class="<%=classes%>">
		<td><c:out value="<%=player.getName()%>" escapeXml="true"></c:out></td>
		<td>
			<%
				if (player.isBanker()) {
					out.print("Banker");
				}
			%>
		</td>
		<td>Php <%=player.getCredit()%> <%
 	if (me.isBanker() && me != player) {
 %> <!--      <button class="set-as-claimed" data-ip="<%=player.getIp()%>">Set As Claimed</button>-->
			<%
				}
			%>
		</td>
		<td><%=player.isBanker() ? "" : player.getBet()%></td>
		<td>
			<%
				if (game.getState() == Game.State.TURNS) {
					          if (player == me) {
					            for (Card card : player.getCards()) {
					              out.print("<div style='float:left' class='" + card.toString() + "'></div>");
					            }
					          } else {
					            if (player.getStatus() == Player.Status.HIT) {
					              Card card = (player.getCards().size() > 2 ? player.getCards().get(2) : null);
					              out.print("<div style='float:left' class='" + card.toString() + "'></div>");
					            }
					          }
					        } else{
					          for (Card card : player.getCards()) {
					            out.print("<div style='float:left' class='" + card.toString() + "'></div>");
					          }
					        }
			%>
		</td>
		<td>
			<%
				if (game.getState() == Game.State.TURNS) {
					        if (player == me) {
					          out.print(player.getScore());
					        }
					      } else {
					        out.print(player.getScore());
					      }
			%>
		</td>
		<td>
			<%
				out.print(player.getStatus());
			%>
		</td>
	</tr>
	<%
		}
	%>
</table>
<div>
	<%
		if (game.getState() == Game.State.READY) {
	%>
	<h3>Remaining cards:</h3>
	<%
		for (Card card : game.getDeck()) {
			        out.print("<div style='float:left' class='" + card.toString() + "'></div>");
			      }
			    }
	%>
</div>

<%
	if (game.getState() == Game.State.READY) {
%>
<script>
	promptOpened = false;
	$('#hit-me').attr("disabled", "disabled");
	$('#stand').attr("disabled", "disabled");
	$('.set-banker').removeAttr("disabled");
<%if (me.isBanker()) {%>
	$('#start-game').removeAttr("disabled");
<%} else {%>
	$('#start-game').attr("disabled", "disabled");
<%}%>
	
</script>
<%
	} else if (game.getState() == Game.State.BETTING) {
%>
<script>
	
<%if (!me.isBanker()) {%>
	askBet();
<%}%>
	$('#start-game').attr("disabled", "disabled");
	$('.set-banker').attr("disabled", "disabled");
	$('#hit-me').attr("disabled", "disabled");
	$('#stand').attr("disabled", "disabled");
</script>
<%
	} else if (game.getState() == Game.State.TURNS) {
%>
<script>
	$('#start-game').attr("disabled", "disabled");
	$('.set-banker').attr("disabled", "disabled");
<%if (me.isTurn()) {%>
	$('#hit-me').removeAttr("disabled");
	$('#stand').removeAttr("disabled");
<%} else {%>
	$('#hit-me').attr("disabled", "disabled");
	$('#stand').attr("disabled", "disabled");
<%}%>
	
</script>
<%
	}
%>

<%
	}
%>
