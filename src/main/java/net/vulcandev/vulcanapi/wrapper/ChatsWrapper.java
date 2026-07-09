package net.vulcandev.vulcanapi.wrapper;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import net.vulcandev.staff.enums.Chats;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Getter
@EqualsAndHashCode
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

    @Nullable
    public static ChatsWrapper fromVulcanChats(@NotNull Chats chats) {
        try {
            return new ChatsWrapper(Chat.valueOf(chats.name()));
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    @NotNull
    public Chats toVulcanChats() {
        return Chats.valueOf(chat.name());
    }

    @Override
    public String toString() {
        return chat.name();
    }
}
