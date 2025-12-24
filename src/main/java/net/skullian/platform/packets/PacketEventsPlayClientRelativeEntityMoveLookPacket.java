package net.skullian.platform.packets;

import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityRelativeMoveAndRotation;
import com.loohp.holomobhealth.platform.packets.PlatformPlayClientRelativeEntityMoveLookPacket;

public class PacketEventsPlayClientRelativeEntityMoveLookPacket extends PlatformPlayClientRelativeEntityMoveLookPacket<PacketWrapper<?>> {

    public PacketEventsPlayClientRelativeEntityMoveLookPacket(PacketWrapper<?> handle) {
        super(handle);
    }

    @Override
    public PacketEventsPlayClientRelativeEntityMoveLookPacket shallowClone() {
        WrapperPlayServerEntityRelativeMoveAndRotation move = (WrapperPlayServerEntityRelativeMoveAndRotation) handle;
        return new PacketEventsPlayClientRelativeEntityMoveLookPacket(new WrapperPlayServerEntityRelativeMoveAndRotation(move.getEntityId(), move.getDeltaX(), move.getDeltaY(), move.getDeltaZ(), move.getYaw(), move.getPitch(), move.isOnGround()));
    }

    @Override
    public int getEntityId() {
        return ((WrapperPlayServerEntityRelativeMoveAndRotation) handle).getEntityId();
    }

}
