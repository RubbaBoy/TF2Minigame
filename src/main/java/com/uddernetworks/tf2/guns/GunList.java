package com.uddernetworks.tf2.guns;

import com.uddernetworks.tf2.utils.ClassEnum;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.stream.Collectors;

public class GunList {

    static int size = 0;

    static ArrayList<GunObject> guns = new ArrayList<>();

    static HashMap<Integer, GunObject> gun_ids = new HashMap<>();

    public static void registerGun(GunObject gun) {
        gun_ids.put(size, gun);
        guns.add(gun);
        size += 1;
    }

    public static boolean isGunId(int id) {
        return gun_ids.containsKey(id);
    }

    public static ArrayList<GunObject> getGunsOfType(ClassEnum type) {
        return gun_ids.values().stream().filter(gun -> gun.getClassType() == type).collect(Collectors.toCollection(ArrayList::new));
    }

    public static void addGunItem(int id, GunObject gun) {
        gun_ids.put(id, gun);
    }

    public static void unregisterGun(GunObject gun) {
        if (guns.contains(gun)) {
            guns.remove(gun);
        }
    }

    public static GunObject getWrench() {
        for (GunObject gun : guns) {
            if (gun.getName().equals(ChatColor.GRAY + "" + ChatColor.BOLD + "Wrench")) {
                return gun;
            }
        }
        System.out.println("The wrench object was not found in the gun list, expect errors");
        return null;
    }

    public static void deleteGunItem(int id) {
        gun_ids.remove(id);
    }

    public static int getIndexOf(GunObject gunObject) {
        return guns.indexOf(gunObject);
    }

    public static GunObject getGunAt(int index) {
        return guns.get(index);
    }

    public static ArrayList<GunObject> getGunlist() {
        return guns;
    }

}
