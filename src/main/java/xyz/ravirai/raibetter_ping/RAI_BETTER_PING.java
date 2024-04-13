package xyz.ravirai.raibetter_ping;

import net.citizensnpcs.api.CitizensAPI;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class RAI_BETTER_PING extends JavaPlugin implements Listener {
    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
        getLogger().info("RAI_BETTER_PING plugin has been enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("RAI_BETTER_PING plugin has been disabled!");
    }

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();
        if (CitizensAPI.getNPCRegistry().isNPC(event.getRightClicked())) {
            return;
        }

        if (event.getRightClicked() instanceof Player && player.hasPermission("raibetter_ping.ping")) {
            Player clickedPlayer = (Player) event.getRightClicked();
            updatePingActionBar(player, clickedPlayer);
        }
    }

    private void updatePingActionBar(Player player, Player clickedPlayer) {
        int ping = getPing(clickedPlayer);
        ChatColor color = (ping < 100) ? ChatColor.GREEN : ChatColor.RED;
        String formattedPing = (ping < 100) ? ChatColor.GREEN + Integer.toString(ping) : ChatColor.RED + Integer.toString(ping);
        player.sendActionBar(ChatColor.BOLD + "" + ChatColor.WHITE + "ᴘɪɴɢ ᴏꜰ " + clickedPlayer.getName() + " ɪꜱ " + color + formattedPing);
    }

    private int getPing(Player player) {
        return player.spigot().getPing();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("ping")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(ChatColor.RED + "Only players can use this command!");
                return true;
            }
            Player player = (Player) sender;
            if (args.length == 0) {
                player.sendMessage(ChatColor.WHITE + "ʏᴏᴜʀ ᴘɪɴɢ ɪꜱ " + ((getPing(player) < 100) ? ChatColor.GREEN : ChatColor.RED) + getPing(player));
            } else if (args.length == 1) {
                Player targetPlayer = getServer().getPlayer(args[0]);
                if (targetPlayer == null) {
                    player.sendMessage(ChatColor.RED + "Player not found!");
                    return true;
                }
                player.sendMessage(ChatColor.WHITE + "ᴘɪɴɢ ᴏꜰ " + targetPlayer.getName() + " ɪꜱ " + ((getPing(targetPlayer) < 100) ? ChatColor.GREEN : ChatColor.RED) + getPing(targetPlayer));
            } else {
                player.sendMessage(ChatColor.RED + "Usage: /ping [player]");
            }
            return true;
        }
        return false;
    }
}
