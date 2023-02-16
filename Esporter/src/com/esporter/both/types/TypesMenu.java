package com.esporter.both.types;


import com.esporter.client.vue.component.MenuButton;

public enum TypesMenu {
	
	
	VISITOR(new MenuButton[] {/*new MenuButton(true, "Accueil",  vue.visitor.Home.class, null, "menuVisiteurHome"),*/
								new MenuButton(true, "Calendrier", com.esporter.client.vue.Calendar.class, TypesPermission.VISITOR, "menuCalendar"),
								new MenuButton(false, "Classement",  com.esporter.client.vue.Ranking.class, null, "menuRanking"),
								new MenuButton(false, "Ecuries",  com.esporter.client.vue.Stables.class, null, "menuStable")}),
	
	STABLE(new MenuButton[] {	/*new MenuButton(true, "Accueil",  vue.stable.Home.class, null, "menuStableHome"),*/
								new MenuButton(true, "Calendrier",  com.esporter.client.vue.Calendar.class, TypesPermission.STABLE, "menuCalendar"),
								new MenuButton(false, "Classement",  com.esporter.client.vue.Ranking.class, null, "menuRanking"),
								new MenuButton(false, "Ecuries",  com.esporter.client.vue.Stables.class, null, "menuStable"),
								new MenuButton(false, "Gestion des equipes",  com.esporter.client.vue.stable.TeamManagement.class, null, "menuStableManagement")}),
	
	PLAYER(new MenuButton[] {	/*new MenuButton(true, "Accueil",  vue.player.Home.class, null, "menuPlayerHome"),*/
								new MenuButton(true, "Calendrier",  com.esporter.client.vue.Calendar.class, TypesPermission.PLAYER, "menuCalendar"),
								new MenuButton(false, "Classement",  com.esporter.client.vue.Ranking.class, null, "menuRanking"),
								new MenuButton(false, "Ecuries",  com.esporter.client.vue.Stables.class, null, "menuStable")}),
	
	ORGANIZER(new MenuButton[] {/*new MenuButton(true, "Accueil",  vue.organizer.Home.class, null, "menuOrganizerHome"),*/
									new MenuButton(true, "Calendrier",  com.esporter.client.vue.organizer.Calendar.class, null, "menuCalendar"),
									new MenuButton(false, "Classement",   com.esporter.client.vue.Ranking.class, null, "menuRanking"),
									new MenuButton(false, "Ecuries",   com.esporter.client.vue.Stables.class, null, "menuStable")}),
	
	REFEREE(new MenuButton[] {	/*new MenuButton(true, "Accueil",   vue.visitor.Home.class, null, "menuRefereeHome"),*/
								new MenuButton(true, "Calendrier",   com.esporter.client.vue.Calendar.class, TypesPermission.REFEREE, "menuCalendar"),
								new MenuButton(false, "Classement",   com.esporter.client.vue.Ranking.class, null, "menuRanking"),
								new MenuButton(false, "Ecuries",   com.esporter.client.vue.Stables.class, null, "menuStable")});	
	
	private MenuButton[] menu;
	
	
	private TypesMenu(MenuButton[] menu) {
		this.menu = menu;
	}
	
	public MenuButton[] getMenu() {
		return menu;
	}
}
