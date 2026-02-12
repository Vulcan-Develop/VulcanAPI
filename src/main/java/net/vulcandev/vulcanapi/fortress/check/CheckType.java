package net.vulcandev.vulcanapi.fortress.check;

import lombok.Getter;

@Getter
public enum CheckType {
    // MOVEMENT
    BARITONE("Baritone", "Detects automated pathfinding and movement", CheckCategory.MOVEMENT),
    BLINK("Blink", "Detects players delaying packet transmission", CheckCategory.MOVEMENT),
    MOVE("Move", "Detects invalid movement patterns and speeds", CheckCategory.MOVEMENT),
    FLY("Fly", "Detects if player is flying when they arent allowed", CheckCategory.MOVEMENT),
    SIMULATION("Simulation", "Detects desynced client-server movement", CheckCategory.MOVEMENT),
    TIMER("Timer", "Detects game speed manipulation", CheckCategory.MOVEMENT),
    VELOCITY("Velocity", "Detects knockback modifications", CheckCategory.MOVEMENT),
    // PLAYER
    FASTBREAK("Fast Break", "Detects breaking blocks too quickly", CheckCategory.PLAYER),
    FASTPLACE("Fast Place", "Detects placing blocks too quickly", CheckCategory.PLAYER),
    INVALID_INTERACT("Invalid Interact", "Detects impossible interactions", CheckCategory.PLAYER),
    NUKER("Nuker", "Detects breaking multiple blocks rapidly", CheckCategory.PLAYER),
    SCAFFOLD("Scaffold", "Detects automated block placement", CheckCategory.PLAYER),
    // COMBAT
    AIM("Aim", "Detects aim assistance and snapping", CheckCategory.COMBAT),
    ANALYSIS("Analysis", "Detects combat pattern anomalies", CheckCategory.COMBAT),
    LEFTCLICKER("Left Clicker", "Detects automated clicking patterns", CheckCategory.COMBAT),
    RIGHTCLICKER("Right Clicker", "Detects automated right-click patterns", CheckCategory.COMBAT),
    HITBOX("Hitbox", "Detects expanded hitbox modifications", CheckCategory.COMBAT),
    PIERCING("Piercing", "Detects hitting through blocks", CheckCategory.COMBAT),
    REACH("Reach", "Detects hitting beyond normal distance", CheckCategory.COMBAT),
    WTAP("WTap", "Detects W-Tap sprint manipulation", CheckCategory.COMBAT),
    HEURISTIC("Heuristic", "Detects combat behavior patterns", CheckCategory.COMBAT),
    BACKTRACK("Backtrack", "Detects hit delay exploitation", CheckCategory.COMBAT),
    KEEPSPRINT("KeepSprint", "Detects sprint persistence during combat", CheckCategory.COMBAT),
    KILLAURA("KillAura", "Detects kill aura during combat", CheckCategory.COMBAT),
    MULTI_ACTIONS("Multi Actions", "Detects performing multiple actions simultaneously", CheckCategory.COMBAT),
    // INVENTORY
    INVENTORY("Inventory", "Detects inventory manipulation cheats", CheckCategory.INVENTORY),
    REFILL("Refill", "Detects automated inventory refilling", CheckCategory.INVENTORY),
    THROWPOT("Throw Pot", "Detects automated potion throwing", CheckCategory.INVENTORY),
    // PACKET
    BADPACKETS("Bad Packets", "Detects malformed or invalid packets", CheckCategory.PACKET),
    CONNECTION_ABUSE("Connection Abuse", "Detects connection exploitation", CheckCategory.PACKET),
    CRASHER("Crasher", "Detects server crash attempts", CheckCategory.PACKET),
    PROTOCOL("Protocol", "Detects protocol violations", CheckCategory.PACKET),
    // MISC
    MISC("Misc", "Detects miscellaneous exploits", CheckCategory.MISC),
    NEURAL("Neural", "Detects cheats using neural networks", CheckCategory.MISC);

    private final String value;
    private final String description;
    private final CheckCategory category;

    CheckType(String value, String description, CheckCategory category) {
        this.value = value;
        this.description = description;
        this.category = category;
    }

    public String niceName() {
        String name = this.name().toLowerCase();
        return Character.toUpperCase(name.charAt(0)) + name.substring(1);
    }
}

