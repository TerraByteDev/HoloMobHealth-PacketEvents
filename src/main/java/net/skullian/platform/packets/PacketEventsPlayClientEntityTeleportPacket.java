package net.skullian.platform.packets;

import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityTeleport;
import com.loohp.holomobhealth.platform.packets.PlatformPlayClientEntityTeleportPacket;

public class PacketEventsPlayClientEntityTeleportPacket extends PlatformPlayClientEntityTeleportPacket<PacketWrapper<?>> {

    public PacketEventsPlayClientEntityTeleportPacket(PacketWrapper<?> handle) {
        super(handle);
    }

    @Override
    public PacketEventsPlayClientEntityTeleportPacket shallowClone() {
        WrapperPlayServerEntityTeleport teleport = (WrapperPlayServerEntityTeleport) handle;
        return new PacketEventsPlayClientEntityTeleportPacket(new WrapperPlayServerEntityTeleport(teleport.getEntityId(), teleport.getValues(), teleport.getRelativeFlags(), teleport.isOnGround()));
    }

    @Override
    public int getEntityId() {
        return ((WrapperPlayServerEntityTeleport) handle).getEntityId();
    }

}
