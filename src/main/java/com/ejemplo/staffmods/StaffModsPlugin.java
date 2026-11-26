package com.ejemplo.staffmods;

import com.lunarclient.apollo.Apollo;
import com.lunarclient.apollo.module.staffmod.StaffMod;
import com.lunarclient.apollo.module.staffmod.StaffModModule;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class StaffModsPlugin extends JavaPlugin implements Listener {

    private final List<StaffMod> mods = Arrays.asList(
        StaffMod.BUNNY_HOP, StaffMod.XRAY, StaffMod.NAME_TAGS
    );
    private final Set<UUID> active = new HashSet<>();
    private StaffModModule module;

    @Override
    public void onEnable() {
        module = Apollo.getModuleManager().getModule(StaffModModule.class);
        getServer().getPluginManager().registerEvents(this, this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player player)) return true;

        Apollo.getPlayerManager().getPlayer(player.getUniqueId()).ifPresentOrElse(apolloPlayer -> {
            if (active.remove(player.getUniqueId())) {
                module.disableStaffMods(apolloPlayer, mods);
                player.sendMessage("§cStaff mods desactivados.");
            } else {
                active.add(player.getUniqueId());
                module.enableStaffMods(apolloPlayer, mods);
                player.sendMessage("§aStaff mods activados.");
            }
        }, () -> player.sendMessage("§cNecesitas Lunar Client."));

        return true;
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        active.remove(e.getPlayer().getUniqueId());
    }
}
