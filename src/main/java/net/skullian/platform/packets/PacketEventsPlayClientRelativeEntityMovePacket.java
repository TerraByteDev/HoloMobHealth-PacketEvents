package net.skullian.platform.packets;

import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityRelativeMove;
import com.loohp.holomobhealth.platform.packets.PlatformPlayClientRelativeEntityMovePacket;

public class PacketEventsPlayClientRelativeEntityMovePacket extends PlatformPlayClientRelativeEntityMovePacket<PacketWrapper<?>> {

    public PacketEventsPlayClientRelativeEntityMovePacket(PacketWrapper<?> handle) {
        super(handle);
    }

    @Override
    public PacketEventsPlayClientRelativeEntityMovePacket shallowClone() {
        WrapperPlayServerEntityRelativeMove move = (WrapperPlayServerEntityRelativeMove) handle;
        return new PacketEventsPlayClientRelativeEntityMovePacket(new WrapperPlayServerEntityRelativeMove(move.getEntityId(), move.getDeltaX(), move.getDeltaY(), move.getDeltaZ(), move.isOnGround()));
    }

    @Override
    public int getEntityId() {
        return ((WrapperPlayServerEntityRelativeMove) handle).getEntityId();
    }

}
