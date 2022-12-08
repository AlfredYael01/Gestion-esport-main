package socket;

import java.io.Serializable;

public enum Response implements Serializable {
	ERROR_LOGIN, ERROR_PERMISSION, LOGIN, UPDATE_TOURNAMENT, UPDATE_CALENDAR, UPDATE_STABLE, UPDATE_PLAYER, UPDATE_TEAM, UPDATE_ALL, ERROR;
}
