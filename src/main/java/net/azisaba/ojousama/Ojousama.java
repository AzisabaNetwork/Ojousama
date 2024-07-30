package net.azisaba.ojousama;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.azisaba.ojousama.command.OjousamaCommand;
import net.azisaba.ojousama.listener.PlayerListener;
import net.azisaba.ojousama.util.Reqwest;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class Ojousama extends JavaPlugin {
    @Override
    public void onEnable() {
        saveDefaultConfig();
        Bukkit.getPluginManager().registerEvents(new PlayerListener(this), this);
        Objects.requireNonNull(getCommand("ojousama")).setExecutor(new OjousamaCommand(this));
    }

    @Override
    public void onDisable() {
        saveConfig();
    }

    public boolean isOjousama(@NotNull UUID uuid) {
        return getConfig().getBoolean("ojousama." + uuid);
    }

    public @NotNull String getOpenAIApiKey() {
        return Objects.requireNonNull(getConfig().getString("openai-api-key"), "openai-api-key is not set");
    }

    public @NotNull Map<String, String> getOpenAIRequestHeaders() {
        Map<String, String> map = new HashMap<>();
        map.put("Content-Type", "application/json");
        map.put("Authorization", "Bearer " + getOpenAIApiKey());
        if (getConfig().getString("openai-organization") != null) {
            map.put("OpenAI-Organization", getConfig().getString("openai-organization"));
        }
        return map;
    }

    public @NotNull String ask(@Nullable String user, @NotNull String message) {
        String prompt = getConfig().getString("prompt");
        JsonObject systemMessage = new JsonObject();
        systemMessage.addProperty("role", "system");
        systemMessage.addProperty("content", prompt);
        JsonObject userMessage = new JsonObject();
        userMessage.addProperty("role", "user");
        userMessage.addProperty("content", message);
        JsonArray messages = new JsonArray();
        messages.add(systemMessage);
        messages.add(userMessage);
        JsonObject obj = new JsonObject();
        obj.addProperty("model", "gpt-4o");
        obj.addProperty("user", user);
        obj.addProperty("max_tokens", 250);
        obj.add("messages", messages);
        JsonObject responseObject = new Gson().fromJson(Reqwest.post(obj.toString(), "https://api.openai.com/v1/chat/completions", getOpenAIRequestHeaders()), JsonObject.class);
        return responseObject.getAsJsonArray("choices")
                .get(0)
                .getAsJsonObject()
                .getAsJsonObject("message")
                .get("content")
                .getAsString();
    }
}
