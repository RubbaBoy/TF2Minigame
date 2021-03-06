package com.uddernetworks.tf2.arena;

import com.uddernetworks.tf2.exception.ExceptionReporter;
import com.uddernetworks.tf2.game.Game;
import com.uddernetworks.tf2.game.GameState;
import com.uddernetworks.tf2.main.Main;
import com.uddernetworks.tf2.utils.TeamEnum;
import com.uddernetworks.tf2.utils.data.Locations;
import net.minecraft.server.v1_10_R1.Entity;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.craftbukkit.v1_10_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.material.Door;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class TeamChooser implements Listener {

    Main main;

    static ArrayList<Player> players = new ArrayList<>();

    static HashMap<Player, Location> locs = new HashMap<>();

    static boolean blueDoor = true;
    static boolean redDoor = true;

    public TeamChooser(Main main) {
        this.main = main;
    }

    public static void setBothDoorTrue() {
        blueDoor = true;
        redDoor = true;
    }

    public void sendPlayers() {
        try {
            for (Player player : Bukkit.getOnlinePlayers()) {
                locs.put(player, player.getLocation());
                Entity en = ((CraftPlayer) player).getHandle();
                en.setInvisible(true);
                player.teleport(Locations.teamChooseSpawn);
                players.add(player);
            }
        } catch (NullPointerException ignored) {}
        catch (Throwable throwable) {
            new ExceptionReporter(throwable);
        }
    }

    public void sendPlayer(Player player) {
        try {
            if (!players.contains(player)) {
                locs.put(player, player.getLocation());
                Entity en = ((CraftPlayer) player).getHandle();
                en.setInvisible(true);
                player.teleport(Locations.teamChooseSpawn);
                players.add(player);
            }
        } catch (Throwable throwable) {
            new ExceptionReporter(throwable);
        }
    }

    public void sendBack(Player player) {
        try {
            player.teleport(locs.get(player));
        } catch (Throwable throwable) {
            new ExceptionReporter(throwable);
        }
    }

    @EventHandler
    public void onDoorClick(PlayerInteractEvent event) {
        try {
            if (Game.getGameState() == GameState.INGAME) {
                if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                    Player player = event.getPlayer();
                    Block block = event.getClickedBlock();
                    if (block.getLocation().equals(Locations.blueDoor) || block.getLocation().equals(Locations.blueDoor.clone().add(0, 1, 0)) || block.getLocation().equals(Locations.redDoor) || block.getLocation().equals(Locations.redDoor.clone().add(0, 1, 0)) || block.getLocation().equals(Locations.randomDoor) || block.getLocation().equals(Locations.randomDoor.clone().add(0, 1, 0))) {
                        if (block.getType() == Material.DARK_OAK_DOOR || block.getType() == Material.SPRUCE_DOOR || block.getType() == Material.WOODEN_DOOR) {
                            Door door = (Door) block.getState().getData();
                            if (door.isTopHalf()) {
                                block = block.getRelative(BlockFace.DOWN);
                            }
                        }
                        if (!PlayerTeams.isSet(player) || PlayerTeams.getPlayer(player) == TeamEnum.BLUE || PlayerTeams.getPlayer(player) == TeamEnum.RED) {
                            event.setCancelled(true);
                            if (block.getLocation().serialize().equals(Locations.blueDoor.serialize())) {
                                if (blueDoor) {
                                    PlayerTeams.addPlayer(player, TeamEnum.BLUE);
                                    reloadAbleDoors();
                                    Entity en = ((CraftPlayer) player).getHandle();
                                    en.setInvisible(false);
                                    Game game = new Game(main);
                                    game.sendPlayer(player);
                                    changeSignText(Locations.blueSign, "", ChatColor.BLUE + "" + ChatColor.BOLD + "Blue", ChatColor.BOLD + "" + PlayerTeams.getPlayersInTeam(TeamEnum.BLUE).size() + "/" + (PlayerTeams.getPlayersInTeam(TeamEnum.BLUE).size() + PlayerTeams.getPlayersInTeam(TeamEnum.RED).size()), "");
                                    changeSignText(Locations.redSign, "", ChatColor.BLUE + "" + ChatColor.RED + "Red", ChatColor.BOLD + "" + PlayerTeams.getPlayersInTeam(TeamEnum.RED).size() + "/" + (PlayerTeams.getPlayersInTeam(TeamEnum.BLUE).size() + PlayerTeams.getPlayersInTeam(TeamEnum.RED).size()), "");
                                } else {
                                    player.sendMessage(ChatColor.BLUE + "Joining this team would cause team unbalances");
                                }
                            } else if (block.getLocation().serialize().equals(Locations.redDoor.serialize())) {
                                if (redDoor) {
                                    PlayerTeams.addPlayer(player, TeamEnum.RED);
                                    reloadAbleDoors();
                                    Entity en = ((CraftPlayer) player).getHandle();
                                    en.setInvisible(false);
                                    Game game = new Game(main);
                                    game.sendPlayer(player);
                                    changeSignText(Locations.blueSign, "", ChatColor.BLUE + "" + ChatColor.BOLD + "Blue", ChatColor.BOLD + "" + PlayerTeams.getPlayersInTeam(TeamEnum.BLUE).size() + "/" + (PlayerTeams.getPlayersInTeam(TeamEnum.BLUE).size() + PlayerTeams.getPlayersInTeam(TeamEnum.RED).size()), "");
                                    changeSignText(Locations.redSign, "", ChatColor.BLUE + "" + ChatColor.RED + "Red", ChatColor.BOLD + "" + PlayerTeams.getPlayersInTeam(TeamEnum.RED).size() + "/" + (PlayerTeams.getPlayersInTeam(TeamEnum.BLUE).size() + PlayerTeams.getPlayersInTeam(TeamEnum.RED).size()), "");
                                } else {
                                    player.sendMessage(ChatColor.RED + "Joining this team would cause team unbalances");
                                }
                            } else if (block.getLocation().serialize().equals(Locations.randomDoor.serialize())) {
                                Random random = new Random();
                                if (blueDoor && redDoor) {
                                    if (random.nextBoolean()) {
                                        PlayerTeams.addPlayer(player, TeamEnum.BLUE);
                                        reloadAbleDoors();
                                        Entity en = ((CraftPlayer) player).getHandle();
                                        en.setInvisible(false);
                                        Game game = new Game(main);
                                        game.sendPlayer(player);
                                        changeSignText(Locations.blueSign, "", ChatColor.BLUE + "" + ChatColor.BOLD + "Blue", ChatColor.BOLD + "" + PlayerTeams.getPlayersInTeam(TeamEnum.BLUE).size() + "/" + (PlayerTeams.getPlayersInTeam(TeamEnum.BLUE).size() + PlayerTeams.getPlayersInTeam(TeamEnum.RED).size()), "");
                                        changeSignText(Locations.redSign, "", ChatColor.BLUE + "" + ChatColor.RED + "Red", ChatColor.BOLD + "" + PlayerTeams.getPlayersInTeam(TeamEnum.RED).size() + "/" + (PlayerTeams.getPlayersInTeam(TeamEnum.BLUE).size() + PlayerTeams.getPlayersInTeam(TeamEnum.RED).size()), "");
                                    } else {
                                        PlayerTeams.addPlayer(player, TeamEnum.RED);
                                        reloadAbleDoors();
                                        Entity en = ((CraftPlayer) player).getHandle();
                                        en.setInvisible(false);
                                        Game game = new Game(main);
                                        game.sendPlayer(player);
                                        changeSignText(Locations.blueSign, "", ChatColor.BLUE + "" + ChatColor.BOLD + "Blue", ChatColor.BOLD + "" + PlayerTeams.getPlayersInTeam(TeamEnum.BLUE).size() + "/" + (PlayerTeams.getPlayersInTeam(TeamEnum.BLUE).size() + PlayerTeams.getPlayersInTeam(TeamEnum.RED).size()), "");
                                        changeSignText(Locations.redSign, "", ChatColor.BLUE + "" + ChatColor.RED + "Red", ChatColor.BOLD + "" + PlayerTeams.getPlayersInTeam(TeamEnum.RED).size() + "/" + (PlayerTeams.getPlayersInTeam(TeamEnum.BLUE).size() + PlayerTeams.getPlayersInTeam(TeamEnum.RED).size()), "");
                                    }
                                } else if (blueDoor) {
                                    PlayerTeams.addPlayer(player, TeamEnum.BLUE);
                                    reloadAbleDoors();
                                    Entity en = ((CraftPlayer) player).getHandle();
                                    en.setInvisible(false);
                                    Game game = new Game(main);
                                    game.sendPlayer(player);
                                    changeSignText(Locations.blueSign, "", ChatColor.BLUE + "" + ChatColor.BOLD + "Blue", ChatColor.BOLD + "" + PlayerTeams.getPlayersInTeam(TeamEnum.BLUE).size() + "/" + (PlayerTeams.getPlayersInTeam(TeamEnum.BLUE).size() + PlayerTeams.getPlayersInTeam(TeamEnum.RED).size()), "");
                                    changeSignText(Locations.redSign, "", ChatColor.BLUE + "" + ChatColor.RED + "Red", ChatColor.BOLD + "" + PlayerTeams.getPlayersInTeam(TeamEnum.RED).size() + "/" + (PlayerTeams.getPlayersInTeam(TeamEnum.BLUE).size() + PlayerTeams.getPlayersInTeam(TeamEnum.RED).size()), "");
                                } else if (redDoor) {
                                    PlayerTeams.addPlayer(player, TeamEnum.RED);
                                    reloadAbleDoors();
                                    Entity en = ((CraftPlayer) player).getHandle();
                                    en.setInvisible(false);
                                    Game game = new Game(main);
                                    game.sendPlayer(player);
                                    changeSignText(Locations.blueSign, "", ChatColor.BLUE + "" + ChatColor.BOLD + "Blue", ChatColor.BOLD + "" + PlayerTeams.getPlayersInTeam(TeamEnum.BLUE).size() + "/" + (PlayerTeams.getPlayersInTeam(TeamEnum.BLUE).size() + PlayerTeams.getPlayersInTeam(TeamEnum.RED).size()), "");
                                    changeSignText(Locations.redSign, "", ChatColor.BLUE + "" + ChatColor.RED + "Red", ChatColor.BOLD + "" + PlayerTeams.getPlayersInTeam(TeamEnum.RED).size() + "/" + (PlayerTeams.getPlayersInTeam(TeamEnum.BLUE).size() + PlayerTeams.getPlayersInTeam(TeamEnum.RED).size()), "");
                                }
                            }
                        } else {
                            player.sendMessage("You already are set in the team list!");
                        }
                    }
                }
            }
        } catch (Throwable throwable) {
            new ExceptionReporter(throwable);
        }
    }

    public void reloadAbleDoors() {
        try {
            if (PlayerTeams.getPlayersInTeam(TeamEnum.BLUE) != null && PlayerTeams.getPlayersInTeam(TeamEnum.RED) != null) {
                if (PlayerTeams.getPlayersInTeam(TeamEnum.BLUE).size() > PlayerTeams.getPlayersInTeam(TeamEnum.RED).size()) {
                    blueDoor = false;
                    redDoor = true;
                    changeSignText(Locations.blueSign, "", ChatColor.STRIKETHROUGH + "" + ChatColor.BLUE + "" + ChatColor.BOLD + "Blue", ChatColor.BOLD + "" + PlayerTeams.getPlayersInTeam(TeamEnum.BLUE).size() + "/" + (PlayerTeams.getPlayersInTeam(TeamEnum.BLUE).size() + PlayerTeams.getPlayersInTeam(TeamEnum.RED).size()), "");
                } else if (PlayerTeams.getPlayersInTeam(TeamEnum.BLUE).size() < PlayerTeams.getPlayersInTeam(TeamEnum.RED).size()) {
                    blueDoor = true;
                    redDoor = false;
                    changeSignText(Locations.redSign, "", ChatColor.STRIKETHROUGH + "" + ChatColor.BLUE + "" + ChatColor.RED + "Red", ChatColor.BOLD + "" + PlayerTeams.getPlayersInTeam(TeamEnum.RED).size() + "/" + (PlayerTeams.getPlayersInTeam(TeamEnum.BLUE).size() + PlayerTeams.getPlayersInTeam(TeamEnum.RED).size()), "");
                } else if (PlayerTeams.getPlayersInTeam(TeamEnum.BLUE).size() == PlayerTeams.getPlayersInTeam(TeamEnum.RED).size()) {
                    blueDoor = true;
                    redDoor = true;
                }
            }
        } catch (Throwable throwable) {
            new ExceptionReporter(throwable);
        }
    }

    public void changeSignText(Location location, String line1, String line2, String line3, String line4) {
        try {
            if (location.getBlock().getState() instanceof Sign) {
                Sign sign = (Sign) location.getBlock().getState();
                sign.setLine(0, line1);
                sign.setLine(1, line2);
                sign.setLine(2, line3);
                sign.setLine(3, line4);
                sign.update();
            } else {
                System.out.println("Location was not a red or blue sign!");
            }
        } catch (Throwable throwable) {
            new ExceptionReporter(throwable);
        }
    }

}