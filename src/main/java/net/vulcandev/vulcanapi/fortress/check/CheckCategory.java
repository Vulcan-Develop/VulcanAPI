package net.vulcandev.vulcanapi.fortress.check;

public enum CheckCategory {
    MOVEMENT,
    PLAYER,
    COMBAT,
    INVENTORY,
    PACKET,
    MISC;

    public String niceName() {
        String name = this.name().toLowerCase();
        return Character.toUpperCase(name.charAt(0)) + name.substring(1);
    }
}
