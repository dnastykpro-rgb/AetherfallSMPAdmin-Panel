package ru.aetherfallsmp.adminpanel.listeners;

import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import ru.aetherfallsmp.adminpanel.AetherfallSMPAdminPanel;
import ru.aetherfallsmp.adminpanel.gui.AdminPanelHolder;
import ru.aetherfallsmp.adminpanel.gui.GuiBuilder;
import ru.aetherfallsmp.adminpanel.gui.Menus;

import java.time.Duration;

public class MenuListener implements Listener {

    private final AetherfallSMPAdminPanel plugin;

    public MenuListener(AetherfallSMPAdminPanel plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getInventory().getHolder() instanceof AdminPanelHolder holder)) {
            return;
        }

        event.setCancelled(true);
        if (!(event.getWhoClicked() instanceof Player admin)) return;

        ItemStack clicked = event.getCurrentItem();
        if (clicked == null || clicked.getType().isAir()) return;

        int slot = event.getRawSlot();
        if (slot >= 54) return;
        switch (holder.getMenuType()) {
            case MAIN -> {
                if (slot == 20) Menus.openPlayersMenu(admin, plugin);
                else if (slot == 22) Menus.openPluginsMenu(admin, plugin);
                else if (slot == 24) {
                    plugin.reloadConfig();
                    admin.sendMessage(GuiBuilder.format("<gradient:#38bdf8:#818cf8>[AetherfallSMP]</gradient> <green>Конфиг успешно перезагружен!"));
                    admin.closeInventory();
                }
            }
            case PLAYERS_LIST -> {
                if (slot == 49) {
                    Menus.openMainMenu(admin, plugin);
                    return;
                }
                if (clicked.getItemMeta() != null && clicked.getItemMeta().displayName() != null) {
                    String targetName = PlainTextComponentSerializer.plainText().serialize(clicked.getItemMeta().displayName());
                    Player target = Bukkit.getPlayerExact(targetName);
                    if (target != null) {
                        Menus.openPlayerProfile(admin, target, plugin);
                    } else {
                        admin.sendMessage(GuiBuilder.format("<red>Игрок вышел с сервера."));
                    }
                }
            }
            case PLAYER_PROFILE -> {
                String targetName = holder.getTargetData();
                if (slot == 49) {
                    Menus.openPlayersMenu(admin, plugin);
                } else if (slot == 20) {
                    Menus.openDonateMenu(admin, targetName, plugin);
                } else if (slot == 22) {
                    Player target = Bukkit.getPlayerExact(targetName);
                    if (target != null) {
                        admin.teleport(target.getLocation());
                        admin.sendMessage(GuiBuilder.format("<green>Вы телепортировались к <white>" + targetName));
                        admin.closeInventory();
                    }
                } else if (slot == 24) {
                    Menus.openPunishMenu(admin, targetName, plugin);
                }
            }
            case DONATE_SELECT -> {
                String targetName = holder.getTargetData();
                if (slot == 49) {
                    Player target = Bukkit.getPlayerExact(targetName);
                    if (target != null) Menus.openPlayerProfile(admin, target, plugin);
                    else Menus.openPlayersMenu(admin, plugin);
                    return;
                }

                ConfigurationSection groups = plugin.getConfig().getConfigurationSection("donate-groups");
                if (groups == null || plugin.getLuckPermsHook() == null) return;

                for (String groupKey : groups.getKeys(false)) {
                    if (groups.getInt(groupKey + ".slot") == slot) {
                        Player target = Bukkit.getPlayerExact(targetName);
                        if (target == null) {
                            admin.sendMessage(GuiBuilder.format("<red>Игрок должен быть онлайн!"));
                            return;
                        }
                        if (event.isRightClick()) {
                            plugin.getLuckPermsHook().addGroupTemporary(target.getUniqueId(), groupKey, Duration.ofDays(30));
                            admin.sendMessage(GuiBuilder.format("<green>Выдана привилегия <aqua>" + groupKey + " <green>на 30 дней игроку <white>" + targetName));
                        } else {
                            plugin.getLuckPermsHook().setGroupPermanent(target.getUniqueId(), groupKey);
                            admin.sendMessage(GuiBuilder.format("<green>Выдана привилегия <aqua>" + groupKey + " <green>навсегда игроку <white>" + targetName));
                        }
                        admin.closeInventory();
                        break;
                    }
                }
            }
            case PUNISH_SELECT -> {
                String targetName = holder.getTargetData();
                if (slot == 49) {
                    Player target = Bukkit.getPlayerExact(targetName);
                    if (target != null) Menus.openPlayerProfile(admin, target, plugin);
                    else Menus.openPlayersMenu(admin, plugin);
                    return;
                }

                ConfigurationSection presets = plugin.getConfig().getConfigurationSection("punishments.presets");
                if (presets == null) return;

                for (String key : presets.getKeys(false)) {
                    if (presets.getInt(key + ".slot") == slot) {
                        String type = presets.getString(key + ".type", "KICK").toLowerCase();
                        String time = presets.getString(key + ".time", "");
                        String reason = presets.getString(key + ".reason", "Нарушение правил");

                        String cmdTemplate = plugin.getConfig().getString("punishments.commands." + type, "kick %player% %reason%");
                        String finalCmd = cmdTemplate
                                .replace("%player%", targetName)
                                .replace("%time%", time)
                                .replace("%reason%", reason);

                        Bukkit.dispatchCommand(admin, finalCmd);
                        admin.closeInventory();
                        break;
                    }
                }
            }
            case PLUGINS_CHECK -> {
                if (slot == 49) {
                    Menus.openMainMenu(admin, plugin);
                    return;
                }
                if (clicked.getItemMeta() != null && clicked.getItemMeta().displayName() != null) {
                    String pluginName = PlainTextComponentSerializer.plainText().serialize(clicked.getItemMeta().displayName());
                    Plugin targetPlugin = Bukkit.getPluginManager().getPlugin(pluginName);
                    if (targetPlugin != null) {
                        admin.sendMessage(GuiBuilder.format("<gradient:#38bdf8:#818cf8><bold>Плагин " + targetPlugin.getName() + ":</bold></gradient> <gray>Версия " + targetPlugin.getPluginMeta().getVersion()));
                    }
                }
            }
        }
    }
}
