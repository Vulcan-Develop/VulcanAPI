package net.vulcandev.vulcanapi.wrapper;

import net.vulcandev.genblocks.interfaces.Gen;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

/**
 * Wrapper that adapts VulcanGenBlocks' internal Gen to a clean API.
 * This allows events to expose Gen functionality without exposing internal types.
 */
public class GenWrapper {
    private final Gen internalGen;

    public GenWrapper(@NotNull Gen internalGen) {
        this.internalGen = internalGen;
    }

    /**
     * Creates a GenWrapper from a VulcanGenBlocks internal Gen
     * @param internalGen the VulcanGenBlocks internal Gen
     * @return GenWrapper instance
     */
    @NotNull
    public static GenWrapper fromVulcanGen(@NotNull Gen internalGen) {
        return new GenWrapper(internalGen);
    }

    public void setCancelled(boolean cancelled) {
        internalGen.setCancelled(cancelled);
    }

    public void startGen() {
        internalGen.startGen();
    }

    public void stopGen() {
        internalGen.stopGen();
    }

    public void preventGen() {
        internalGen.preventGen();
    }

    public void reverseGen() {
        internalGen.reverseGen();
    }

    public void setReversed(boolean reversed) {
        internalGen.setReversed(reversed);
    }

    public void setPaused(boolean paused) {
        internalGen.setPaused(paused);
    }

    public boolean isReversed() {
        return internalGen.isReversed();
    }

    public boolean getCancelled() {
        return internalGen.getCancelled();
    }

    @Nullable
    public GenDirectionWrapper getDirection() {
        return GenDirectionWrapper.fromVulcanGenDirection(internalGen.getDirection());
    }

    @Nullable
    public GenTypeWrapper getType() {
        return GenTypeWrapper.fromVulcanGenType(internalGen.getType());
    }

    public int getHorizontalLength() {
        return internalGen.getHorizontalLength();
    }

    public int getGenRate() {
        return internalGen.getGenRate();
    }

    public UUID getPlayerUUID() {
        return internalGen.getPlayerUUID();
    }

    public Material getMaterial() {
        return internalGen.getMaterial();
    }

    public int getData() {
        return internalGen.getData();
    }

    public Location getStartLocation() {
        return internalGen.getStartLocation();
    }

    public World getWorld() {
        return internalGen.getWorld();
    }

    public int getX() {
        return internalGen.getX();
    }

    public int getY() {
        return internalGen.getY();
    }

    public int getZ() {
        return internalGen.getZ();
    }

    public BukkitTask getBukkitTask() {
        return internalGen.getBukkitTask();
    }

    public double getCost() {
        return internalGen.getCost();
    }

    public boolean isPatch() {
        return internalGen.isPatch();
    }

    public boolean isCancelled() {
        return internalGen.isCancelled();
    }

    public boolean isGravity() {
        return internalGen.isGravity();
    }

    public boolean isPaused() {
        return internalGen.isPaused();
    }

    public boolean isShifted() {
        return internalGen.isShifted();
    }

    /**
     * Get the internal VulcanGenBlocks Gen object.
     * @return the internal Gen
     */
    @NotNull
    public Gen getInternalGen() {
        return internalGen;
    }
}