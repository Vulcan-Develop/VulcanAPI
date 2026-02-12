package net.vulcandev.vulcanapi.fortress.player;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.UUID;

@AllArgsConstructor
@Getter
@Setter
public class PlayerProfile {
    private final UUID uuid;
    private final String playerName;
    private String clientName;
    private String version;
    private long currentTick;
    private long ping;
    private Location location;

    /**
     * Get the Bukkit player object (may be null if offline)
     */
    public Player getPlayer() {
        return Bukkit.getPlayer(uuid);
    }

    /**
     * Check if player is online
     */
    public boolean isOnline() {
        return getPlayer() != null;
    }
}