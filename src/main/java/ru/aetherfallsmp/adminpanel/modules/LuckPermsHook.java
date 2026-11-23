package ru.aetherfallsmp.adminpanel.modules;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.Node;
import net.luckperms.api.node.types.InheritanceNode;

import java.time.Duration;
import java.util.UUID;

public class LuckPermsHook {

    private final LuckPerms luckPerms;

    public LuckPermsHook(LuckPerms luckPerms) {
        this.luckPerms = luckPerms;
    }

    public String getPrimaryGroup(UUID uuid) {
        User user = luckPerms.getUserManager().getUser(uuid);
        return user != null ? user.getPrimaryGroup() : "default";
    }

    public void setGroupPermanent(UUID uuid, String groupName) {
        luckPerms.getUserManager().modifyUser(uuid, user -> {
            user.data().clear(node -> node instanceof InheritanceNode);
            user.data().add(InheritanceNode.builder(groupName).build());
            user.setPrimaryGroup(groupName);
        });
    }

    public void addGroupTemporary(UUID uuid, String groupName, Duration duration) {
        luckPerms.getUserManager().modifyUser(uuid, user -> {
            Node node = InheritanceNode.builder(groupName).expiry(duration).build();
            user.data().add(node);
        });
    }
}
