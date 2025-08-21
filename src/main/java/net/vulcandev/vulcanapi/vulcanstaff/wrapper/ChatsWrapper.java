package net.vulcandev.vulcanapi.vulcanstaff.wrapper;

import net.vulcandev.staff.enums.Chats;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Wrapper for VulcanStaff Chats to provide a clean API without exposing internal types
 */
public class ChatsWrapper {
    
    public enum Chat {
        PUBLIC,
        STAFF,
        ADMIN
    }
    
    private final Chat chat;
    
    public ChatsWrapper(@NotNull Chat chat) {
        this.chat = chat;
    }
    
    /**
     * Gets the chat type
     * @return the chat type
     */
    @NotNull
    public Chat getChat() {
        return chat;
    }
    
    /**
     * Creates a ChatsWrapper from a VulcanStaff Chats enum
     * @param chats the VulcanStaff Chats enum
     * @return ChatsWrapper instance
     */
    public static ChatsWrapper fromVulcanChats(@NotNull Chats chats) {
        try {
            String name = chats.name();
            Chat wrapperChat = Chat.valueOf(name);
            return new ChatsWrapper(wrapperChat);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
    
    /**
     * Creates a ChatsWrapper from a string
     * @param chatName the chat type name
     * @return ChatsWrapper instance
     */
    public static ChatsWrapper fromString(@NotNull String chatName) {
        try {
            Chat chat = Chat.valueOf(chatName.toUpperCase());
            return new ChatsWrapper(chat);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        ChatsWrapper that = (ChatsWrapper) obj;

        return chat == that.chat;
    }
    
    @Override
    public String toString() {
        return chat.name();
    }
}