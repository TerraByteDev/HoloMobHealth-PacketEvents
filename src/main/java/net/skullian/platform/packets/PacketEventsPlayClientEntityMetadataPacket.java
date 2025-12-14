package net.skullian.platform.packets;

import com.github.retrooper.packetevents.protocol.entity.data.EntityData;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityMetadata;
import com.loohp.holomobhealth.platform.packets.PlatformPlayClientEntityMetadataPacket;

import java.util.List;

public class PacketEventsPlayClientEntityMetadataPacket extends PlatformPlayClientEntityMetadataPacket<PacketWrapper<?>> {

    public PacketEventsPlayClientEntityMetadataPacket(PacketWrapper<?> handle) {
        super(handle);
    }

    @Override
    public PacketEventsPlayClientEntityMetadataPacket shallowClone() {
        WrapperPlayServerEntityMetadata metadata = (WrapperPlayServerEntityMetadata) handle;
        return new PacketEventsPlayClientEntityMetadataPacket(new WrapperPlayServerEntityMetadata(metadata.getEntityId(), metadata.getEntityMetadata()));
    }

    @Override
    public int getEntityId() {
        return ((WrapperPlayServerEntityMetadata) handle).getEntityId();
    }

    @Override
    public List<?> getEntityDataWatchers() {
        return ((WrapperPlayServerEntityMetadata) handle).getEntityMetadata();
    }

    @SuppressWarnings("unchecked")
    @Override
    public void setEntityDataWatchers(List<?> dataWatchers) {
        ((WrapperPlayServerEntityMetadata) handle).setEntityMetadata((List<EntityData<?>>) dataWatchers);
    }

}
