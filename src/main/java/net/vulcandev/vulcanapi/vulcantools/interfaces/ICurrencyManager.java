package net.vulcandev.vulcanapi.vulcantools.interfaces;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.Set;

/**
 * Interface for managing currencies in VulcanTools.
 * This interface provides access to currency operations for external plugins.
 */
public interface ICurrencyManager {

    /**
     * Gets the balance of a player for a specific currency.
     *
     * @param player the player to check
     * @param currency the currency identifier
     * @return the player's balance for the specified currency
     */
    long getBalance(OfflinePlayer player, String currency);

    /**
     * Gives currency to a player.
     *
     * @param player the player to give currency to
     * @param currency the currency identifier
     * @param amount the amount to give
     */
    void giveCurrency(OfflinePlayer player, String currency, long amount);

    /**
     * Removes currency from a player.
     *
     * @param player the player to remove currency from
     * @param currency the currency identifier
     * @param amount the amount to remove
     */
    void removeCurrency(OfflinePlayer player, String currency, long amount);

    /**
     * Checks if a player has enough of a specific currency.
     *
     * @param player the player to check
     * @param currency the currency identifier
     * @param amount the amount to check for
     * @return true if the player has enough currency, false otherwise
     */
    boolean hasEnough(OfflinePlayer player, String currency, long amount);

    /**
     * Transfers currency from one player to another.
     *
     * @param sender the player sending the currency
     * @param receiver the player receiving the currency
     * @param currency the currency identifier
     * @param amount the amount to transfer
     * @return true if the transfer was successful, false otherwise
     */
    boolean payCurrency(Player sender, OfflinePlayer receiver, String currency, long amount);

    /**
     * Gets the currency identifier associated with a specific tool type.
     *
     * @param toolType the tool type
     * @return the currency identifier, or null if no currency is associated
     */
    String getCurrencyIdByTool(String toolType);

    /**
     * Gets all available currency identifiers.
     *
     * @return a set of all currency identifiers
     */
    Set<String> getAllCurrencyIds();

    /**
     * Checks if a currency exists.
     *
     * @param currency the currency identifier to check
     * @return true if the currency exists, false otherwise
     */
    boolean currencyExists(String currency);
}
