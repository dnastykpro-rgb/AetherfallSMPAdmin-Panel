package ru.aetherfallsmp.adminpanel.gui;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class GuiBuilder {

    private static final MiniMessage MM = MiniMessage.miniMessage();

    public static Component format(String text) {
        return MM.deserialize(text).decoration(TextDecoration.ITALIC, false);
    }

    public static Inventory createGradientBase(AdminPanelHolder holder, String rawTitle) {
        Inventory inv = Bukkit.createInventory(holder, 54, format(rawTitle));
        holder.setInventory(inv);

        ItemStack blackGlass = createGlass(Material.BLACK_STAINED_GLASS_PANE);
        ItemStack darkBlueGlass = createGlass(Material.BLUE_STAINED_GLASS_PANE);
        ItemStack cyanGlass = createGlass(Material.CYAN_STAINED_GLASS_PANE);
        ItemStack lightBlueGlass = createGlass(Material.LIGHT_BLUE_STAINED_GLASS_PANE);

        int[] corners = {0, 8, 45, 53};
        for (int slot : corners) {
            inv.setItem(slot, blackGlass);
        }

        int[] blueBorder = {
                1, 2, 3, 5, 6, 7,
                9, 17, 18, 26, 27, 35, 36, 44,
                46, 47, 48, 50, 51, 52
        };
        for (int slot : blueBorder) {
            inv.setItem(slot, darkBlueGlass);
        }

        int[] cyanAccents = {10, 16, 37, 43};
        for (int slot : cyanAccents) {
            inv.setItem(slot, cyanGlass);
        }

        inv.setItem(4, lightBlueGlass);
        inv.setItem(49, lightBlueGlass);

        return inv;
    }

    private static ItemStack createGlass(Material material) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.displayName(Component.empty());
            meta.setHideTooltip(true);
            item.setItemMeta(meta);
        }
        return item;
    }

    public static ItemStack createButton(Material material, String name, List<String> loreLines, boolean glow) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.displayName(format(name));
            List<Component> lore = new ArrayList<>();
            for (String line : loreLines) {
                lore.add(format(line));
            }
            meta.lore(lore);
            meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
            if (glow) {
                meta.setEnchantmentGlintOverride(true);
            }
            item.setItemMeta(meta);
        }
        return item;
    }

    public static ItemStack createTexturedHead(String base64, String name, List<String> loreLines) {
        ItemStack head = new ItemStack(Material.PLAYER_HEAD);
        if (head.getItemMeta() instanceof SkullMeta meta) {
            PlayerProfile profile = Bukkit.createProfile(UUID.randomUUID());
            profile.setProperty(new ProfileProperty("textures", base64));
            meta.setPlayerProfile(profile);

            meta.displayName(format(name));
            List<Component> lore = new ArrayList<>();
            for (String line : loreLines) {
                lore.add(format(line));
            }
            meta.lore(lore);
            head.setItemMeta(meta);
        }
        return head;
    }

    public static ItemStack createBackButton() {
        return createButton(Material.ARROW, "<gradient:#38bdf8:#818cf8><bold>« Назад</bold></gradient>",
                List.of("<gray>Вернуться в предыдущее меню"), false);
    }
}
