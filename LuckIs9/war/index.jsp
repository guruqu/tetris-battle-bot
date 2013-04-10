<%@page import="javax.persistence.TypedQuery"%>
<%@page import="nac.luckisnine.Game"%>
<%@page import="nac.luckisnine.EMFService"%>
<%@page import="com.google.appengine.api.datastore.KeyFactory"%>
<%@page import="java.util.List"%>
<%@page import="javax.persistence.EntityManager"%>
<%@page import="com.google.appengine.api.users.User"%>
<%@page import="com.google.appengine.api.users.UserService"%>
<%@page import="com.google.appengine.api.users.UserServiceFactory"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="ISO-8859-1"%>
<%
	UserService userService = UserServiceFactory.getUserService();
	User currentUser = userService.getCurrentUser();
	EntityManager em = EMFService.get().createEntityManager();

	if (currentUser == null) {
		out.println("<p>Please <a href=\"" + userService.createLoginURL(request.getRequestURL().toString()) + "\">sign in</a>.</p>");
		return;
	}
%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Luck Is 9</title>
</head>
<body>
	<%

		TypedQuery<Game> gameQuery = em.createQuery("select g from Game as g", Game.class);
		List<Game> games = gameQuery.getResultList();
		out.print(games.size());
		for (Game game : games) {
	%><div>
		<a href='game.jsp?id=<%=game.getId()%>'><%=game.getId()%></a>
	</div>
	<%
		}
	%>
</body>
</html>