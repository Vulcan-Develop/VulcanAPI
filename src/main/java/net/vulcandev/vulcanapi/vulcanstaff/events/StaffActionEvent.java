package net.vulcandev.vulcanapi.vulcanstaff.events;

import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

@Getter
public class StaffActionEvent extends Event {
    private static final HandlerList handlers = new HandlerList();

    private final Player staff;
    private final ActionType actionType;
    private final String target;
    private final Location location;
    private final String details;

    public StaffActionEvent(Player staff, ActionType actionType, String target, Location location, String details) {
        this.staff = staff;
        this.actionType = actionType;
        this.target = target;
        this.location = location;
        this.details = details;
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

    public enum ActionType {
        BLOCK_BREAK,
        BLOCK_PLACE,
        BLOCK_INTERACT,
        ITEM_DROP,
        ITEM_PICKUP,
        CREATIVE_GRAB,
        COMMAND_EXECUTE,
        CHAT_MESSAGE,
        SPAWNER_CREATE,
        SPAWNER_ADD,
        SPAWNER_REMOVE
    }
}
