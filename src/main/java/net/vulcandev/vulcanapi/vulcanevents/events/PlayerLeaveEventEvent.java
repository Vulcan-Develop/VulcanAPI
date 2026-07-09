package net.vulcandev.vulcanapi.vulcanevents.events;

import lombok.Getter;
import net.vulcandev.vulcanapi.wrapper.EventTypeWrapper;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

@Getter
public class PlayerLeaveEventEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private final Player player;
    private final EventTypeWrapper eventType;
    private final String eventName;
    private final boolean wasParticipant;
    private final boolean wasSpectator;
    private final boolean sendMessage;

    public PlayerLeaveEventEvent(@NotNull Player player, @NotNull EventTypeWrapper eventType, @NotNull String eventName, boolean wasParticipant, boolean wasSpectator, boolean sendMessage) {
        this.player = player;
        this.eventType = eventType;
        this.eventName = eventName;
        this.wasParticipant = wasParticipant;
        this.wasSpectator = wasSpectator;
        this.sendMessage = sendMessage;
    }

    public boolean wasParticipant() {
        return wasParticipant;
    }

    public boolean wasSpectator() {
        return wasSpectator;
    }

    public boolean willSendMessage() {
        return sendMessage;
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
