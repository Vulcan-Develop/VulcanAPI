package net.vulcandev.vulcanapi.vulcanstaff.events;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

@Getter
public class PlayerReportEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();

    @Setter
    private boolean cancelled = false;
    private final Player reporter;
    private final String reportedPlayer;
    private final String reason;

    public PlayerReportEvent(Player reporter, String reportedPlayer, String reason) {
        this.reporter = reporter;
        this.reportedPlayer = reportedPlayer;
        this.reason = reason;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return handlers;
    }
}
