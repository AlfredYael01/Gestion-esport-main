package controleur;

public enum State {
	HOME_VISITOR, HOME_ORGANIZER, HOME_STABLE, HOME_REFEREE, HOME_PLAYER,
	CALENDAR/*_ORGANIZER, CALENDAR_PLAYER, CALENDAR_REFEREE, CALENDAR_STANDARD*/,
	RANKING,
	STABLE_LIST, STABLE_INFO, PLAYER_INFO,
	STABLE_MANAGEMENT, ADD_TEAM, ADD_PLAYER, MODIFY_TEAM, MODIFY_PLAYER,
	ERROR,
	LOGIN,
	MODIFY_TOURNAMENT, ADD_TOURNAMENT, POOL, INSCRIPTION;
}