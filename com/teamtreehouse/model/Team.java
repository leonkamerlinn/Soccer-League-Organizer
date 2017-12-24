package com.teamtreehouse.model;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

public class Team implements Comparable<Team>, Serializable, TeamBuilder {
	private final String mTeamName;
	private final String mCoach;
	private final Set<Player> mPlayers = new TreeSet<>();

	private Team(String teamName, String coach) {
		mTeamName = teamName;
		mCoach = coach;
  	}

  	public static TeamBuilder Builder(String teamName, String coach) {
	    return new Team(teamName, coach);
    }

	@Override
  	public TeamBuilder addPlayer(Player player) {
		mPlayers.add(player);
		return this;
	}

	@Override
	public TeamBuilder removePlayer(Player player) {
		mPlayers.remove(player);
		return this;
	}

	@Override
	public boolean isFull() {
		return mPlayers.size() >= 11;
	}

	@Override
	public Team build() {
		return this;
	}

	public Set<Player> getPlayers() {
		return mPlayers;
	}

  	public String getTeamName() {
		return mTeamName;
	}

	public String getCoachName() {
		return mCoach;
	}

	public void heightReport(){
		Map<String,List<Player>> heightGroupPlayers = new TreeMap<>();

		List<Player> shortPlayers = mPlayers.stream()
				.filter(player -> (player.getHeightInInches() >= 35 && player.getHeightInInches() <= 40))
				.collect(Collectors.toList());

		List<Player> normalPlayers = mPlayers.stream()
				.filter(player -> (player.getHeightInInches() >= 41 && player.getHeightInInches() <= 46))
				.collect(Collectors.toList());

		List<Player> tallPlayers = mPlayers.stream()
				.filter(player -> (player.getHeightInInches() >= 47 && player.getHeightInInches() <= 50))
				.collect(Collectors.toList());


		if (shortPlayers.size() > 0) {
			heightGroupPlayers.put("35 - 40", shortPlayers);
		}

		if (normalPlayers.size() > 0) {
			heightGroupPlayers.put("41 - 46", normalPlayers);
		}

		if (tallPlayers.size() > 0) {
			heightGroupPlayers.put("47 - 50", tallPlayers);
		}



		for(Map.Entry<String, List<Player>>display: heightGroupPlayers.entrySet()) {
			System.out.printf("%s - %s %n", display.getKey(), display.getValue());
		}
	}

	public void balanceReport(){
		Map<String, List<Player>>ExInPlayers = new TreeMap<>();

		ExInPlayers.put("Experienced", mPlayers
				.stream()
				.filter(Player::isPreviousExperience)
				.collect(Collectors.toList()));


		ExInPlayers.put("Inexperienced", mPlayers
				.stream()
				.filter(player -> !player.isPreviousExperience())
				.collect(Collectors.toList()));



		for(Map.Entry<String, List<Player>>display: ExInPlayers.entrySet()){
			System.out.printf("%s - %s %n", display.getKey(), display.getValue());
		}
	}

	public void printRoster(){
		for(Player player: mPlayers){
			System.out.printf("%s %s%n", player.getLastName(), player.getFirstName());
		}
	}



	@Override
	public String toString() {
		return String.format("Team: %s, Coach: %s", getTeamName(), getCoachName());
	}

	@Override
	public int compareTo(Team o) {
		if (getTeamName().equals(o.getTeamName())) {
			return getCoachName().compareTo(o.getCoachName());
		}
		return getTeamName().compareTo(o.getTeamName());
	}
}


