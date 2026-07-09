package net.vulcandev.vulcanapi.vulcanevents;

import lombok.Getter;
import net.vulcandev.vulcanapi.interfaces.events.IEventInstance;
import net.vulcandev.vulcanapi.interfaces.events.IPlayerBan;
import net.vulcandev.vulcanapi.interfaces.events.IVulcanEventsPlugin;
import net.vulcandev.vulcanapi.wrapper.EventStateWrapper;
import net.vulcandev.vulcanapi.wrapper.EventTypeWrapper;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.Map;
import java.util.UUID;

public class VulcanEventsAPI {
    private static VulcanEventsAPI instance;

    @Getter
    private final IVulcanEventsPlugin plugin;

    public VulcanEventsAPI(IVulcanEventsPlugin plugin) {
        this.plugin = plugin;
    }

    public static VulcanEventsAPI getInstance() {
        return instance;
    }

    public static boolean isAvailable() {
        return instance != null && instance.plugin != null;
    }

    public boolean hasActiveEvent() {
        return currentEvent() != null;
    }

    @Nullable
    public EventTypeWrapper getCurrentEventType() {
        IEventInstance currentEvent = currentEvent();
        if (currentEvent == null) return null;
        return EventTypeWrapper.fromString(currentEvent.getEventTypeName());
    }

    @Nullable
    public String getCurrentEventName() {
        IEventInstance currentEvent = currentEvent();
        if (currentEvent == null) return null;
        return currentEvent.getName();
    }

    @Nullable
    public EventStateWrapper getCurrentEventState() {
        IEventInstance currentEvent = currentEvent();
        if (currentEvent == null) return null;
        return EventStateWrapper.fromString(currentEvent.getStateName());
    }

    public int getTimeRemaining() {
        IEventInstance currentEvent = currentEvent();
        if (currentEvent == null) return -1;
        return currentEvent.getSecondsLeft();
    }

    public boolean isPlayerInEvent(@NotNull Player player) {
        IEventInstance currentEvent = currentEvent();
        if (currentEvent == null) return false;
        return currentEvent.isParticipating(player.getUniqueId());
    }

    public boolean isPlayerSpectating(@NotNull Player player) {
        IEventInstance currentEvent = currentEvent();
        if (currentEvent == null) return false;
        return currentEvent.getSpectatorMap().containsKey(player.getUniqueId());
    }

    public int getParticipantCount() {
        IEventInstance currentEvent = currentEvent();
        if (currentEvent == null) return 0;
        return currentEvent.getPlayerMap().size();
    }

    public int getSpectatorCount() {
        IEventInstance currentEvent = currentEvent();
        if (currentEvent == null) return 0;
        return currentEvent.getSpectatorMap().size();
    }

    @NotNull
    public Map<UUID, Player> getParticipants() {
        IEventInstance currentEvent = currentEvent();
        if (currentEvent == null) return Collections.emptyMap();
        return currentEvent.getPlayerMap();
    }

    @NotNull
    public Map<UUID, Player> getSpectators() {
        IEventInstance currentEvent = currentEvent();
        if (currentEvent == null) return Collections.emptyMap();
        return currentEvent.getSpectatorMap();
    }

    public boolean isPlayerBanned(@NotNull Player player) {
        if (!isAvailable()) return false;
        IPlayerBan ban = plugin.getPlayerBan(player.getUniqueId());
        return ban != null && ban.isStillBanned();
    }

    public boolean hasSpace() {
        IEventInstance currentEvent = currentEvent();
        if (currentEvent == null) return false;
        return currentEvent.isSpace();
    }

    public static void initialize(org.bukkit.plugin.Plugin plugin) {
        cleanup();
        if (plugin instanceof IVulcanEventsPlugin) {
            instance = new VulcanEventsAPI((IVulcanEventsPlugin) plugin);
        }
    }

    public static void cleanup() {
        instance = null;
    }

    private IEventInstance currentEvent() {
        return isAvailable() ? plugin.getCurrentEvent() : null;
    }
}
