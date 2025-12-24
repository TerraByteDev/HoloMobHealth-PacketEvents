package net.skullian;

import com.loohp.holomobhealth.HoloMobHealth;
import net.skullian.platform.PacketEventsPlatform;
import net.skullian.updater.UpdateListener;
import net.skullian.util.ChatUtils;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public final class HoloMobHealthPacketEvents extends JavaPlugin {

    public static HoloMobHealthPacketEvents instance;
    private static boolean debug = false;

    @Override
    public void onLoad() {
        instance = this;

        HoloMobHealth.protocolPlatform = new PacketEventsPlatform();
    }

    @Override
    public void onEnable() {
        saveDefaultConfig();
        debug = HoloMobHealthPacketEvents.instance.getConfig().getBoolean("Debug");

        ChatUtils.sendMessage("Initialising ProtocolProvider.");

        getServer().getPluginManager().registerEvents(new UpdateListener(), this);
    }

    @Override
    public void onDisable() {
    }

    public static void sendDebug(String message) {
        if (debug) {
            instance.getServer().getConsoleSender().sendMessage(ChatColor.GRAY + "[HMHPE|DEBUG] " + message);
        }
    }
}
