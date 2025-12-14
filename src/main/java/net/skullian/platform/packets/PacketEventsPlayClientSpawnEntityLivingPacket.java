package net.skullian.platform.packets;

import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerSpawnLivingEntity;
import com.loohp.holomobhealth.platform.packets.PlatformPlayClientSpawnEntityLivingPacket;

public class PacketEventsPlayClientSpawnEntityLivingPacket extends PlatformPlayClientSpawnEntityLivingPacket<PacketWrapper<?>> {

    public PacketEventsPlayClientSpawnEntityLivingPacket(PacketWrapper<?> handle) {
        super(handle);
    }

    @Override
    public PacketEventsPlayClientSpawnEntityLivingPacket shallowClone() {
        WrapperPlayServerSpawnLivingEntity spawn = (WrapperPlayServerSpawnLivingEntity) handle;
        return new PacketEventsPlayClientSpawnEntityLivingPacket(new WrapperPlayServerSpawnLivingEntity(spawn.getEntityId(), spawn.getEntityUUID(), spawn.getEntityType(), spawn.getPosition(), spawn.getYaw(), spawn.getPitch(), spawn.getHeadPitch(), spawn.getVelocity(), spawn.getEntityMetadata()));
    }

    @Override
    public int getEntityId() {
        return ((WrapperPlayServerSpawnLivingEntity) handle).getEntityId();
    }

}
