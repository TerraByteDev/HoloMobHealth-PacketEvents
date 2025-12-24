package net.skullian.platform;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.PacketEventsAPI;
import com.github.retrooper.packetevents.event.ProtocolPacketEvent;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import com.loohp.holomobhealth.platform.ProtocolPlatform;
import com.loohp.holomobhealth.platform.packets.PlatformPacket;
import net.skullian.HoloMobHealthPacketEvents;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class PacketEventsPlatform implements ProtocolPlatform<ProtocolPacketEvent, PacketWrapper<?>> {

    private final PacketEventsPacketListenerProvider listenerProvider;
    private final PacketEventsPacketCreatorProvider creatorProvider;

    public PacketEventsPlatform() {
        this.listenerProvider = new PacketEventsPacketListenerProvider(this);
        this.creatorProvider = new PacketEventsPacketCreatorProvider();
    }

    public PacketEventsAPI<?> getPacketEventsAPI() {
        return PacketEvents.getAPI();
    }

    @Override
    public int getProtocolVersion(Player player) {
        return getPacketEventsAPI().getProtocolManager().getClientVersion(player).getProtocolVersion();
    }

    @Override
    public void sendServerPacket(Player player, PlatformPacket<?> platformPacket, boolean filtered) {
        PacketWrapper<?> packet = (PacketWrapper<?>) platformPacket.shallowClone().getHandle();
        if (filtered) {
            getPacketEventsAPI().getPlayerManager().sendPacket(player, packet);
        } else {
            getPacketEventsAPI().getPlayerManager().sendPacketSilently(player, packet);
        }
    }

    @Override
    public PacketEventsPacketListenerProvider getPlatformPacketListenerProvider() {
        return listenerProvider;
    }

    @Override
    public PacketEventsPacketCreatorProvider getPlatformPacketCreatorProvider() {
        return creatorProvider;
    }

    @Override
    public Plugin getRegisteredPlugin() {
        return HoloMobHealthPacketEvents.instance;
    }

    @Override
    public Plugin getProtocolPlatformPlugin() {
        return (Plugin) getPacketEventsAPI().getPlugin();
    }
}

