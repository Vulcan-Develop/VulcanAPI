package net.vulcandev.vulcanapi.vulcanevents.events;

import lombok.Getter;
import net.vulcandev.vulcanapi.wrapper.EventTypeWrapper;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Getter
public class EventEndEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private final EventTypeWrapper eventType;
    private final String eventName;
    private final Map<UUID, Player> finalParticipants;
    private final Map<UUID, Player> finalSpectators;
    private final boolean wasSilent;
    private final boolean hadRewards;

    public EventEndEvent(@NotNull EventTypeWrapper eventType, @NotNull String eventName, @NotNull Map<UUID, Player> finalParticipants, @NotNull Map<UUID, Player> finalSpectators, boolean wasSilent, boolean hadRewards) {
        this.eventType = eventType;
        this.eventName = eventName;
        this.finalParticipants = finalParticipants;
        this.finalSpectators = finalSpectators;
        this.wasSilent = wasSilent;
        this.hadRewards = hadRewards;
    }

    @NotNull
    public List<Player> getWinners() {
        return new ArrayList<>(finalParticipants.values());
    }

    public boolean wasSilent() {
        return wasSilent;
    }

    public boolean hadRewards() {
        return hadRewards;
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
