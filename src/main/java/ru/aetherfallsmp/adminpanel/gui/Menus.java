package ru.aetherfallsmp.adminpanel.gui;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.Plugin;
import ru.aetherfallsmp.adminpanel.AetherfallSMPAdminPanel;

import java.util.ArrayList;
import java.util.List;

public class Menus {

    public static void openMainMenu(Player admin, AetherfallSMPAdminPanel plugin) {
        AdminPanelHolder holder = new AdminPanelHolder(AdminPanelHolder.MenuType.MAIN);
        Inventory inv = GuiBuilder.createGradientBase(holder, plugin.getConfig().getString("titles.main-menu"));

        inv.setItem(20, GuiBuilder.createTexturedHead(
                plugin.getConfig().getString("icons.players-head"),
                "<gradient:#1d4ed8:#38bdf8><bold>УПРАВЛЕНИЕ ИГРОКАМИ</bold></gradient>",
                List.of(
                        "<dark_gray>━━━━━━━━━━━━━━━━━━━━━━━━━━━━",
                        "<gray>Выдача доната (LuckPerms),",
                        "<gray>баны, муты и модерация.",
                        "",
                        "<dark_gray>▸ <white>Онлайн: <aqua>" + Bukkit.getOnlinePlayers().size() + " игроков",
                        "<gradient:#38bdf8:#818cf8>▶ Нажмите, чтобы открыть</gradient>",
                        "<dark_gray>━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
                )
        ));

        inv.setItem(22, GuiBuilder.createTexturedHead(
                plugin.getConfig().getString("icons.plugins-head"),
                "<gradient:#1d4ed8:#38bdf8><bold>ПРОВЕРКА ПЛАГИНОВ</bold></gradient>",
                List.of(
                        "<dark_gray>━━━━━━━━━━━━━━━━━━━━━━━━━━━━",
                        "<gray>Состояние плагинов сервера,",
                        "<gray>версии, авторы и зависимости.",
                        "",
                        "<dark_gray>▸ <white>Установлено: <aqua>" + Bukkit.getPluginManager().getPlugins().length,
                        "<gradient:#38bdf8:#818cf8>▶ Нажмите, чтобы проверить</gradient>",
                        "<dark_gray>━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
                )
        ));

        inv.setItem(24, GuiBuilder.createTexturedHead(
                plugin.getConfig().getString("icons.server-head"),
                "<gradient:#1d4ed8:#38bdf8><bold>ИНСТРУМЕНТЫ СЕРВЕРА</bold></gradient>",
                List.of(
                        "<dark_gray>━━━━━━━━━━━━━━━━━━━━━━━━━━━━",
                        "<gray>Перезагрузка конфигурации",
                        "<gray>админ-панели AetherfallSMP.",
                        "",
                        "<gradient:#38bdf8:#818cf8>▶ Нажмите для Reload конфига</gradient>",
                        "<dark_gray>━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
                )
        ));

        admin.openInventory(inv);
    }
    public static void openPlayersMenu(Player admin, AetherfallSMPAdminPanel plugin) {
        AdminPanelHolder holder = new AdminPanelHolder(AdminPanelHolder.MenuType.PLAYERS_LIST);
        Inventory inv = GuiBuilder.createGradientBase(holder, plugin.getConfig().getString("titles.players-menu"));

        int[] contentSlots = {
                11, 12, 13, 14, 15,
                19, 20, 21, 22, 23, 24, 25,
                28, 29, 30, 31, 32, 33, 34
        };

        int index = 0;
        for (Player target : Bukkit.getOnlinePlayers()) {
            if (index >= contentSlots.length) break;
            ItemStack head = new ItemStack(Material.PLAYER_HEAD);
            if (head.getItemMeta() instanceof SkullMeta meta) {
                meta.setOwningPlayer(target);
                meta.displayName(GuiBuilder.format("<gradient:#38bdf8:#818cf8><bold>" + target.getName() + "</bold></gradient>"));
                String group = plugin.getLuckPermsHook() != null
                        ? plugin.getLuckPermsHook().getPrimaryGroup(target.getUniqueId())
                        : "Неизвестно";
                meta.lore(List.of(
                        GuiBuilder.format("<dark_gray>▸ <gray>Привилегия: <aqua>" + group),
                        GuiBuilder.format("<dark_gray>▸ <gray>Пинг: <green>" + target.getPing() + " ms"),
                        GuiBuilder.format(""),
                        GuiBuilder.format("<yellow>▶ Кликните для управления")
                ));
                head.setItemMeta(meta);
            }
            inv.setItem(contentSlots[index++], head);
        }

        inv.setItem(49, GuiBuilder.createBackButton());
        admin.openInventory(inv);
    }

    public static void openPlayerProfile(Player admin, Player target, AetherfallSMPAdminPanel plugin) {
        AdminPanelHolder holder = new AdminPanelHolder(AdminPanelHolder.MenuType.PLAYER_PROFILE, target.getName());
        String title = plugin.getConfig().getString("titles.player-profile").replace("%player%", target.getName());
        Inventory inv = GuiBuilder.createGradientBase(holder, title);

        inv.setItem(20, GuiBuilder.createButton(
                Material.NETHER_STAR,
                "<gradient:#22c55e:#38bdf8><bold>ВЫДАТЬ ДОНАТ (LUCKPERMS)</bold></gradient>",
                List.of("<gray>Нажмите, чтобы выбрать привилегию", "<gray>для игрока <white>" + target.getName()),
                true
        ));

        inv.setItem(22, GuiBuilder.createButton(
                Material.ENDER_PEARL,
                "<gradient:#38bdf8:#818cf8><bold>ТЕЛЕПОРТИРОВАТЬСЯ</bold></gradient>",
                List.of("<gray>Мгновенный ТП к игроку"),
                false
        ));

        inv.setItem(24, GuiBuilder.createButton(
                Material.NETHERITE_AXE,
                "<gradient:#ef4444:#f97316><bold>НАКАЗАНИЯ (БАН / МУТ / КИК)</bold></gradient>",
                List.of("<gray>Открыть меню модерации и", "<gray>быстрых наказаний"),
                false
        ));

        inv.setItem(49, GuiBuilder.createBackButton());
        admin.openInventory(inv);
    }
    public static void openDonateMenu(Player admin, String targetName, AetherfallSMPAdminPanel plugin) {
        AdminPanelHolder holder = new AdminPanelHolder(AdminPanelHolder.MenuType.DONATE_SELECT, targetName);
        String title = plugin.getConfig().getString("titles.donate-menu").replace("%player%", targetName);
        Inventory inv = GuiBuilder.createGradientBase(holder, title);

        ConfigurationSection groups = plugin.getConfig().getConfigurationSection("donate-groups");
        if (groups != null) {
            for (String groupKey : groups.getKeys(false)) {
                String name = groups.getString(groupKey + ".display-name", groupKey);
                Material mat = Material.matchMaterial(groups.getString(groupKey + ".material", "DIAMOND"));
                int slot = groups.getInt(groupKey + ".slot", 11);

                inv.setItem(slot, GuiBuilder.createButton(
                        mat != null ? mat : Material.DIAMOND,
                        name,
                        List.of(
                                "<dark_gray>Группа LP: <gray>" + groupKey,
                                "",
                                "<green>ЛКМ <dark_gray>» <white>Выдать навсегда",
                                "<aqua>ПКМ <dark_gray>» <white>Выдать на 30 дней"
                        ),
                        true
                ));
            }
        }

        inv.setItem(49, GuiBuilder.createBackButton());
        admin.openInventory(inv);
    }

    public static void openPunishMenu(Player admin, String targetName, AetherfallSMPAdminPanel plugin) {
        AdminPanelHolder holder = new AdminPanelHolder(AdminPanelHolder.MenuType.PUNISH_SELECT, targetName);
        String title = plugin.getConfig().getString("titles.punish-menu").replace("%player%", targetName);
        Inventory inv = GuiBuilder.createGradientBase(holder, title);

        ConfigurationSection presets = plugin.getConfig().getConfigurationSection("punishments.presets");
        if (presets != null) {
            for (String key : presets.getKeys(false)) {
                String name = presets.getString(key + ".name", key);
                String reason = presets.getString(key + ".reason", "Нарушение правил");
                String time = presets.getString(key + ".time", "");
                Material mat = Material.matchMaterial(presets.getString(key + ".material", "BARRIER"));
                int slot = presets.getInt(key + ".slot", 11);

                List<String> lore = new ArrayList<>();
                lore.add("<gray>Причина: <white>" + reason);
                if (!time.isEmpty()) lore.add("<gray>Срок: <yellow>" + time);
                lore.add("");
                lore.add("<red>▶ Нажмите, чтобы применить");

                inv.setItem(slot, GuiBuilder.createButton(mat != null ? mat : Material.BARRIER, name, lore, false));
            }
        }

        inv.setItem(49, GuiBuilder.createBackButton());
        admin.openInventory(inv);
    }

    public static void openPluginsMenu(Player admin, AetherfallSMPAdminPanel plugin) {
        AdminPanelHolder holder = new AdminPanelHolder(AdminPanelHolder.MenuType.PLUGINS_CHECK);
        Inventory inv = GuiBuilder.createGradientBase(holder, plugin.getConfig().getString("titles.plugins-menu"));

        int[] contentSlots = {
                11, 12, 13, 14, 15,
                19, 20, 21, 22, 23, 24, 25,
                28, 29, 30, 31, 32, 33, 34
        };

        Plugin[] plugins = Bukkit.getPluginManager().getPlugins();
        for (int i = 0; i < plugins.length && i < contentSlots.length; i++) {
            Plugin p = plugins[i];
            boolean enabled = p.isEnabled();
            Material mat = enabled ? Material.LIME_DYE : Material.RED_DYE;
            String status = enabled ? "<green>Работает (Enabled)" : "<red>Отключен (Disabled)";

            inv.setItem(contentSlots[i], GuiBuilder.createButton(
                    mat,
                    (enabled ? "<gradient:#22c55e:#86efac>" : "<gradient:#ef4444:#fca5a5>") + "<bold>" + p.getName() + "</bold></gradient>",
                    List.of(
                            "<dark_gray>▸ <gray>Статус: " + status,
                            "<dark_gray>▸ <gray>Версия: <aqua>" + p.getPluginMeta().getVersion(),
                            "<dark_gray>▸ <gray>Авторы: <white>" + String.join(", ", p.getPluginMeta().getAuthors()),
                            "",
                            "<yellow>▶ Нажмите для списка команд в чат"
                    ),
                    false
            ));
        }

        inv.setItem(49, GuiBuilder.createBackButton());
        admin.openInventory(inv);
    }
}
