package net.skullian.platform.packets;

import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerSpawnEntity;
import com.loohp.holomobhealth.platform.packets.PlatformPlayClientSpawnEntityPacket;

public class PacketEventsPlayClientSpawnEntityPacket extends PlatformPlayClientSpawnEntityPacket<PacketWrapper<?>> {

    public PacketEventsPlayClientSpawnEntityPacket(PacketWrapper<?> handle) {
        super(handle);
    }

    @Override
    public PacketEventsPlayClientSpawnEntityPacket shallowClone() {
        WrapperPlayServerSpawnEntity spawn = (WrapperPlayServerSpawnEntity) handle;
        return new PacketEventsPlayClientSpawnEntityPacket(new WrapperPlayServerSpawnEntity(spawn.getEntityId(), spawn.getUUID(), spawn.getEntityType(), spawn.getPosition(), spawn.getYaw(), spawn.getPitch(), spawn.getHeadYaw(), spawn.getData(), spawn.getVelocity()));
    }

    @Override
    public int getEntityId() {
        return ((WrapperPlayServerSpawnEntity) handle).getEntityId();
    }

}
