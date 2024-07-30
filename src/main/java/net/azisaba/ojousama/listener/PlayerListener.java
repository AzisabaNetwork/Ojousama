package net.azisaba.ojousama.listener;

import net.azisaba.ojousama.Ojousama;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.jetbrains.annotations.NotNull;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PlayerListener implements Listener {
    private static final Pattern CHANNEL_CHAT_COMMAND_PATTERN =
            Pattern.compile("^/(?:lunachat:)?(?:ch|channel) (.+?) (.+)$");
    private final Ojousama plugin;
    private boolean chatProcessing = false;
    private boolean commandProcessing = false;

    public PlayerListener(@NotNull Ojousama plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onAsyncPlayerChat(AsyncPlayerChatEvent e) {
        if (chatProcessing) return;
        if (!plugin.isOjousama(e.getPlayer().getUniqueId())) return;
        e.setCancelled(true);
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            String message = plugin.ask(e.getPlayer().getUniqueId().toString(), e.getMessage());
            Bukkit.getScheduler().runTask(plugin, () -> {
                chatProcessing = true;
                try {
                    e.getPlayer().chat(message);
                } finally {
                    chatProcessing = false;
                }
            });
        });
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent e) {
        if (commandProcessing) return;
        if (!plugin.isOjousama(e.getPlayer().getUniqueId())) return;
        Matcher matcher = CHANNEL_CHAT_COMMAND_PATTERN.matcher(e.getMessage());
        if (!matcher.matches()) return;
        e.setCancelled(true);
        e.setMessage("/");
        String channelName = matcher.group(1);
        String message = matcher.group(2);
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            String response = plugin.ask(e.getPlayer().getUniqueId().toString(), message);
            Bukkit.getScheduler().runTask(plugin, () -> {
                commandProcessing = true;
                try {
                    e.getPlayer().chat("/lunachat:ch " + channelName + " " + response);
                } finally {
                    commandProcessing = false;
                }
            });
        });
    }
}
