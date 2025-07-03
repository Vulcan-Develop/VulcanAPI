package net.vulcandev.vulcanapi.vulcantools.events;

import net.vulcandev.vulcantools.enums.ToolType;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Event fired when a tool event is started
 */
public class ToolEventStartEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();

    private final ToolType toolType;
    private int durationSeconds;
    private final CommandSender startedBy;
    private boolean cancelled;
    
    /**
     * Creates a new ToolEventStartEvent.
     *
     * @param toolType the type of tool event being started
     * @param durationSeconds the duration of the event in seconds
     * @param startedBy the command sender who started the event
     */
    public ToolEventStartEvent(ToolType toolType, int durationSeconds, CommandSender startedBy) {
        this.toolType = toolType;
        this.durationSeconds = durationSeconds;
        this.startedBy = startedBy;
        this.cancelled = false;
    }

    /**
     * Gets the type of tool event being started.
     *
     * @return the tool event type
     */
    public ToolType getToolType() {
        return toolType;
    }

    /**
     * Gets the duration of the event in seconds.
     *
     * @return the duration in seconds
     */
    public int getDurationSeconds() {
        return durationSeconds;
    }

    /**
     * Sets the duration of the event in seconds.
     *
     * @param durationSeconds the new duration in seconds
     */
    public void setDurationSeconds(int durationSeconds) {
        this.durationSeconds = durationSeconds;
    }

    /**
     * Gets the command sender who started the event.
     *
     * @return the command sender who started the event
     */
    public CommandSender getStartedBy() {
        return startedBy;
    }

    /**
     * Gets the duration of the event in minutes.
     *
     * @return the duration in minutes
     */
    public double getDurationMinutes() {
        return durationSeconds / 60.0;
    }

    /**
     * Gets a description of the event.
     *
     * @return a string describing the event
     */
    public String getEventDescription() {
        return toolType.niceName() + " event for " + getDurationMinutes() + " minutes";
    }
    
    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
    
    @NotNull
    @Override 
    public HandlerList getHandlers() { 
        return handlers; 
    }
    
    public static HandlerList getHandlerList() {
        return handlers;
    }
}
