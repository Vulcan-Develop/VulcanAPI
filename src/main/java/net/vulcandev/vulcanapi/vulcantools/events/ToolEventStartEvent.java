package net.vulcandev.vulcanapi.vulcantools.events;

import lombok.Getter;
import lombok.Setter;
import net.vulcandev.vulcanapi.event.Cancellable;
import net.vulcandev.vulcanapi.event.VulcanEvent;
import net.vulcandev.vulcanapi.wrapper.ToolTypeWrapper;
import net.vulcandev.vulcantools.enums.ToolType;
import org.bukkit.command.CommandSender;

/**
 * Event fired when a tool event is started
 */
@Getter
public class ToolEventStartEvent extends VulcanEvent implements Cancellable {
    private final ToolTypeWrapper toolType;
    @Setter
    private int durationSeconds;
    private final CommandSender startedBy;
    @Setter
    private boolean cancelled;
    
    /**
     * Creates a new ToolEventStartEvent.
     *
     * @param toolType the type of tool event being started
     * @param durationSeconds the duration of the event in seconds
     * @param startedBy the command sender who started the event
     */
    public ToolEventStartEvent(ToolType toolType, int durationSeconds, CommandSender startedBy) {
        this.toolType = ToolTypeWrapper.fromVulcanToolType(toolType);
        this.durationSeconds = durationSeconds;
        this.startedBy = startedBy;
        this.cancelled = false;
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
        return toolType.getNiceName() + " event for " + getDurationMinutes() + " minutes";
    }
    @Override
    public boolean isCancellable() {
        return true;
    }
}
