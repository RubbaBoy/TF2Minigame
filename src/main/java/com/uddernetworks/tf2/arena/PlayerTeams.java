package com.uddernetworks.tf2.arena;

import com.uddernetworks.tf2.exception.ExceptionReporter;
import com.uddernetworks.tf2.utils.TeamEnum;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Collectors;

public class PlayerTeams {

    static HashMap<Player, TeamEnum> teams = new HashMap<>();

    public static void addPlayer(Player player, TeamEnum team) {
        try {
            teams.put(player, team);
        } catch (Throwable throwable) {
            new ExceptionReporter(throwable);
        }
    }

    public static ArrayList<Player> getPlayersInTeam(TeamEnum team) {
        try {
            return teams.keySet().stream().filter(player -> teams.get(player) == team).collect(Collectors.toCollection(ArrayList::new));
        } catch (Throwable throwable) {
            new ExceptionReporter(throwable);
            return null;
        }
    }

    public static TeamEnum getPlayer(Player player) {
        try {
            if (teams.containsKey(player)) {
                return teams.get(player);
            } else {
                System.out.println("Tried to get player not in team list, reload plugin");
                return null;
            }
        } catch (Throwable throwable) {
            new ExceptionReporter(throwable);
            return null;
        }
    }

    public static boolean isSet(Player player) {
        try {
            return teams.containsKey(player);
        } catch (Throwable throwable) {
            new ExceptionReporter(throwable);
            return false;
        }
    }

}