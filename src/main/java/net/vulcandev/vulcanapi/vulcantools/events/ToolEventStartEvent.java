package net.vulcandev.vulcanapi.vulcantools.events;

import lombok.Getter;
import lombok.Setter;
import net.vulcandev.vulcanapi.event.Cancellable;
import net.vulcandev.vulcanapi.event.VulcanEvent;
import net.vulcandev.vulcanapi.wrapper.ToolTypeWrapper;
import net.vulcandev.vulcantools.enums.ToolType;
import org.bukkit.command.CommandSender;

@Getter
public class ToolEventStartEvent extends VulcanEvent implements Cancellable {
    private final ToolTypeWrapper toolType;
    @Setter
    private int durationSeconds;
    private final CommandSender startedBy;
    @Setter
    private boolean cancelled;

    public ToolEventStartEvent(ToolType toolType, int durationSeconds, CommandSender startedBy) {
        this.toolType = ToolTypeWrapper.fromVulcanToolType(toolType);
        this.durationSeconds = durationSeconds;
        this.startedBy = startedBy;
        this.cancelled = false;
    }

    public double getDurationMinutes() {
        return durationSeconds / 60.0;
    }

    public String getEventDescription() {
        return toolType.getNiceName() + " event for " + getDurationMinutes() + " minutes";
    }

    @Override
    public boolean isCancellable() {
        return true;
    }
}
