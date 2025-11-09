package net.vulcandev.vulcanapi.vulcantools;

import net.vulcandev.vulcanapi.interfaces.tools.IVulcanToolsPlugin;
import net.vulcandev.vulcanapi.vulcantools.interfaces.IBoosterManager;
import net.vulcandev.vulcanapi.vulcantools.interfaces.ICurrencyManager;
import net.vulcandev.vulcanapi.vulcantools.interfaces.IEventManager;

  /**
   * Main API class for VulcanTools.
   * This class provides a clean interface for external plugins to interact with VulcanTools.
   */
  public class VulcanToolsAPI {
      private static VulcanToolsAPI instance;
      private final IVulcanToolsPlugin plugin;

      public VulcanToolsAPI(IVulcanToolsPlugin plugin) {
          this.plugin = plugin;
      }

      /**
       * Gets the API instance.
       *
       * @return the VulcanToolsAPI instance, or null if VulcanTools is not loaded
       */
      public static VulcanToolsAPI getInstance() {return instance;}

      /**
       * Checks if VulcanTools is available and loaded.
       *
       * @return true if VulcanTools is available, false otherwise
       */
      public static boolean isAvailable() {return instance != null && instance.plugin != null;}

      /**
       * Gets the currency manager interface.
       *
       * @return the currency manager interface
       */
      public ICurrencyManager getCurrencyManager() {
          return plugin.getCurrencyManager();
      }

      /**
       * Gets the event manager interface.
       *
       * @return the event manager interface
       */
      public IEventManager getEventManager() {
          return plugin.getEventManager();
      }

      /**
       * Gets the booster manager interface.
       *
       * @return the booster manager interface
       */
      public IBoosterManager getBoosterManager() {
          return plugin.getBoosterManager();
      }

      public static void initialize(org.bukkit.plugin.Plugin plugin) {
          cleanup();
          if (plugin instanceof IVulcanToolsPlugin) {
              IVulcanToolsPlugin vulcanTools = (IVulcanToolsPlugin) plugin;
              instance = new VulcanToolsAPI(vulcanTools);
          }
      }

      /**
       * Clean up the API instance
       */
      public static void cleanup() {
          instance = null;
      }
  }