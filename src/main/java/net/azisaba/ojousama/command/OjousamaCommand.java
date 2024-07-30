package net.azisaba.ojousama.command;

import net.azisaba.ojousama.Ojousama;
import net.azisaba.ojousama.util.Reqwest;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

public class OjousamaCommand implements TabExecutor {
    private final Ojousama plugin;

    public OjousamaCommand(Ojousama plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (args.length != 0) {
            String user;
            if (sender instanceof Player) {
                user = ((Player) sender).getUniqueId().toString();
            } else {
                user = "console";
            }
            String message = String.join(" ", args);
            String response = plugin.ask(user, message);
            TextComponent component = new TextComponent(response);
            component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponent[]{new TextComponent("AIによって生成された文章です。クリックしてコピーできます。")}));
            component.setClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, response));
            sender.spigot().sendMessage(component);
            return true;
        }
        Player player = (Player) sender;
        if (plugin.getConfig().getBoolean("ojousama." + player.getUniqueId())) {
            plugin.getConfig().set("ojousama." + player.getUniqueId(), false);
            player.sendMessage("§aお嬢様モードを無効にしました");
        } else {
            plugin.getConfig().set("ojousama." + player.getUniqueId(), true);
            player.sendMessage("§aお嬢様モードを有効にしました");
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        return Collections.emptyList();
    }
}
