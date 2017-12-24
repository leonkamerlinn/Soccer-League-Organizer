package com.teamtreehouse.model;
public interface TeamBuilder {
    TeamBuilder addPlayer(Player player);
    TeamBuilder removePlayer(Player player);
    boolean isFull();
    Team build();
}
