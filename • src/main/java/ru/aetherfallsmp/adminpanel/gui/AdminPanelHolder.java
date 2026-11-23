package ru.aetherfallsmp.adminpanel.gui;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

public class AdminPanelHolder implements InventoryHolder {

    private final MenuType menuType;
    private final String targetData;
    private Inventory inventory;

    public AdminPanelHolder(MenuType menuType, String targetData) {
        this.menuType = menuType;
        this.targetData = targetData;
    }

    public AdminPanelHolder(MenuType menuType) {
        this(menuType, "");
    }

    @Override
    public @NotNull Inventory getInventory() {
        return inventory;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    public MenuType getMenuType() {
        return menuType;
    }

    public String getTargetData() {
        return targetData;
    }

    public enum MenuType {
        MAIN,
        PLAYERS_LIST,
        PLAYER_PROFILE,
        DONATE_SELECT,
        PUNISH_SELECT,
        PLUGINS_CHECK
    }
}
