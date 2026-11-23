package ru.aetherfallsmp.adminpanel;

import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import net.luckperms.api.LuckPerms;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import ru.aetherfallsmp.adminpanel.gui.Menus;
import ru.aetherfallsmp.adminpanel.listeners.MenuListener;
import ru.aetherfallsmp.adminpanel.modules.LuckPermsHook;

import java.util.List;

public final class AetherfallSMPAdminPanel extends JavaPlugin {

    private LuckPermsHook luckPermsHook;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        if (Bukkit.getPluginManager().getPlugin("LuckPerms") != null) {
            RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
            if (provider != null) {
                this.luckPermsHook = new LuckPermsHook(provider.getProvider());
                getLogger().info("Успешно подключен LuckPerms API!");
            }
        }

        Bukkit.getPluginManager().registerEvents(new MenuListener(this), this);

        this.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, event -> {
            Commands commands = event.registrar();
            commands.register(
                    Commands.literal("apanel")
                            .requires(source -> source.getSender().hasPermission("aetherfall.adminpanel.use"))
                            .executes(ctx -> {
                                if (ctx.getSource().getSender() instanceof Player player) {
                                    Menus.openMainMenu(player, this);
                                } else {
                                    ctx.getSource().getSender().sendMessage("Эту команду можно использовать только в игре!");
                                }
                                return 1;
                            })
                            .build(),
                    "Открыть админ-панель AetherfallSMP",
                    List.of("admin")
            );
        });

        getLogger().info("AetherfallSMPAdmin-Panel успешно запущен!");
    }

    public LuckPermsHook getLuckPermsHook() {
        return luckPermsHook;
    }
}
