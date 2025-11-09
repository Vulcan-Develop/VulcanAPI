package net.vulcandev.vulcanapi.interfaces.tools;

import net.vulcandev.vulcanapi.vulcantools.interfaces.IBoosterManager;
import net.vulcandev.vulcanapi.vulcantools.interfaces.ICurrencyManager;
import net.vulcandev.vulcanapi.vulcantools.interfaces.IEventManager;
import org.jetbrains.annotations.NotNull;

/**
 * Interface for VulcanTools plugin to avoid direct class dependencies
 * This allows VulcanAPI to work with VulcanTools across classloader boundaries
 */
public interface IVulcanToolsPlugin {
    /**
     * Gets the currency manager interface
     * @return the currency manager interface
     */
    @NotNull
    ICurrencyManager getCurrencyManager();

    /**
     * Gets the event manager interface
     * @return the event manager interface
     */
    @NotNull
    IEventManager getEventManager();

    /**
     * Gets the booster manager interface
     * @return the booster manager interface
     */
    @NotNull
    IBoosterManager getBoosterManager();
}
