package net.vulcandev.vulcanapi.vulcanstaff.events;

import net.vulcandev.staff.enums.Chats;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Called when a staff member sends a message in a staff chat channel.
 * This event can be cancelled to prevent the message from being sent.
 */
public class StaffChatEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled = false;
    
    private final Player player;
    private final String message;
    private final Chats chatType;
    
    /**
     * Constructs a new StaffChatEvent.
     * @param player The player sending the message
     * @param message The message content
     * @param chatType The type of staff chat
     */
    public StaffChatEvent(Player player, String message, Chats chatType) {
        this.player = player;
        this.message = message;
        this.chatType = chatType;
    }
    
    /**
     * Gets the player sending the message.
     * @return The player
     */
    public Player getPlayer() {
        return player;
    }
    
    /**
     * Gets the message content.
     * @return The message
     */
    public String getMessage() {
        return message;
    }
    
    /**
     * Gets the type of staff chat.
     * @return The chat type
     */
    public Chats getChatType() {
        return chatType;
    }
    
    @Override
    public boolean isCancelled() {
        return cancelled;
    }
    
    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
    
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
    
    public static HandlerList getHandlerList() {
        return handlers;
    }
}
