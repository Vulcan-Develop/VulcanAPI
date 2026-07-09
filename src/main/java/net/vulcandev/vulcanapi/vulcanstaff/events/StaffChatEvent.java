package net.vulcandev.vulcanapi.vulcanstaff.events;

import lombok.Getter;
import lombok.Setter;
import net.vulcandev.staff.enums.Chats;
import net.vulcandev.vulcanapi.wrapper.ChatsWrapper;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

@Getter
public class StaffChatEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();

    @Setter
    private boolean cancelled = false;
    private final Player player;
    private final String message;
    private final ChatsWrapper chatType;

    public StaffChatEvent(Player player, String message, Chats chatType) {
        this.player = player;
        this.message = message;
        this.chatType = ChatsWrapper.fromVulcanChats(chatType);
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
