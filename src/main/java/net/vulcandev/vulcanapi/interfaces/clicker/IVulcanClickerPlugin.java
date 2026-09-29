package net.vulcandev.vulcanapi.interfaces.clicker;

import net.vulcandev.vulcanapi.vulcanclicker.ClickerType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

@ApiStatus.Internal
public interface IVulcanClickerPlugin {

    boolean isArmed(@NotNull UUID uuid, @NotNull ClickerType type);

    boolean setArmed(@NotNull Player player, @NotNull ClickerType type, boolean armed);

    boolean isClicking(@NotNull UUID uuid, @NotNull ClickerType type);

    double getCps(@NotNull UUID uuid, @NotNull ClickerType type);

    @Nullable
    Double getCpsOverride(@NotNull UUID uuid, @NotNull ClickerType type);

    void setCpsOverride(@NotNull UUID uuid, @NotNull ClickerType type, @Nullable Double cps);

    boolean isWhitelisted(@Nullable ItemStack item, @NotNull ClickerType type);

    boolean isClickerEnabled(@NotNull ClickerType type);

    void setClickerEnabled(@NotNull ClickerType type, boolean enabled);

    boolean isBlocked(@NotNull UUID uuid);

    void setBlocked(@NotNull UUID uuid, boolean blocked);
}
