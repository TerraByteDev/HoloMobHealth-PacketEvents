package net.skullian.platform;

import com.github.retrooper.packetevents.event.PacketListener;
import com.github.retrooper.packetevents.event.PacketListenerPriority;
import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.event.ProtocolPacketEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityMetadata;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityRelativeMove;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityRelativeMoveAndRotation;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityTeleport;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerSpawnEntity;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerSpawnLivingEntity;
import com.loohp.holomobhealth.platform.PlatformPacketEventListener;
import com.loohp.holomobhealth.platform.PlatformPacketListenerPriority;
import com.loohp.holomobhealth.platform.PlatformPacketListenerProvider;
import com.loohp.holomobhealth.platform.packets.PlatformPlayClientEntityMetadataPacket;
import com.loohp.holomobhealth.platform.packets.PlatformPlayClientEntityTeleportPacket;
import com.loohp.holomobhealth.platform.packets.PlatformPlayClientRelativeEntityMoveLookPacket;
import com.loohp.holomobhealth.platform.packets.PlatformPlayClientRelativeEntityMovePacket;
import com.loohp.holomobhealth.platform.packets.PlatformPlayClientSpawnEntityLivingPacket;
import com.loohp.holomobhealth.platform.packets.PlatformPlayClientSpawnEntityPacket;
import net.skullian.platform.packets.PacketEventsPlayClientEntityMetadataPacket;
import net.skullian.platform.packets.PacketEventsPlayClientEntityTeleportPacket;
import net.skullian.platform.packets.PacketEventsPlayClientRelativeEntityMoveLookPacket;
import net.skullian.platform.packets.PacketEventsPlayClientRelativeEntityMovePacket;
import net.skullian.platform.packets.PacketEventsPlayClientSpawnEntityLivingPacket;
import net.skullian.platform.packets.PacketEventsPlayClientSpawnEntityPacket;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public class PacketEventsPacketListenerProvider implements PlatformPacketListenerProvider<ProtocolPacketEvent, PacketWrapper<?>> {

    private static PacketListenerPriority c(PlatformPacketListenerPriority priority) {
        return PacketListenerPriority.valueOf(priority.name());
    }

    private final PacketEventsPlatform platform;

    public PacketEventsPacketListenerProvider(PacketEventsPlatform platform) {
        this.platform = platform;
    }

    @Override
    public void listenToPlayClientEntityMeta(Plugin plugin, PlatformPacketListenerPriority priority, PlatformPacketEventListener<ProtocolPacketEvent, PacketWrapper<?>, PlatformPlayClientEntityMetadataPacket<PacketWrapper<?>>> listener) {
        platform.getPacketEventsAPI().getEventManager().registerListener(new PacketListener() {
            @Override
            public void onPacketSend(@NotNull PacketSendEvent event) {
                PacketTypeCommon type = event.getPacketType();
                if (type == PacketType.Play.Server.ENTITY_METADATA) {
                    listener.handle(new PacketEventsPacketEvent<>(event, e -> {
                        WrapperPlayServerEntityMetadata wrapper = new WrapperPlayServerEntityMetadata((PacketSendEvent) e);
                        return new PacketEventsPlayClientEntityMetadataPacket(wrapper);
                    }));
                }
            }
        }, c(priority));
    }

    @Override
    public void listenToPlayClientSpawnEntityLiving(Plugin plugin, PlatformPacketListenerPriority priority, PlatformPacketEventListener<ProtocolPacketEvent, PacketWrapper<?>, PlatformPlayClientSpawnEntityLivingPacket<PacketWrapper<?>>> listener) {
        platform.getPacketEventsAPI().getEventManager().registerListener(new PacketListener() {
            @Override
            public void onPacketSend(@NotNull PacketSendEvent event) {
                PacketTypeCommon type = event.getPacketType();
                if (type == PacketType.Play.Server.SPAWN_LIVING_ENTITY) {
                    listener.handle(new PacketEventsPacketEvent<>(event, e -> {
                        WrapperPlayServerSpawnLivingEntity wrapper = new WrapperPlayServerSpawnLivingEntity((PacketSendEvent) e);
                        return new PacketEventsPlayClientSpawnEntityLivingPacket(wrapper);
                    }));
                }
            }
        }, c(priority));
    }

    @Override
    public void listenToPlayClientSpawnEntity(Plugin plugin, PlatformPacketListenerPriority priority, PlatformPacketEventListener<ProtocolPacketEvent, PacketWrapper<?>, PlatformPlayClientSpawnEntityPacket<PacketWrapper<?>>> listener) {
        platform.getPacketEventsAPI().getEventManager().registerListener(new PacketListener() {
            @Override
            public void onPacketSend(@NotNull PacketSendEvent event) {
                PacketTypeCommon type = event.getPacketType();
                if (type == PacketType.Play.Server.SPAWN_ENTITY) {
                    listener.handle(new PacketEventsPacketEvent<>(event, e -> {
                        WrapperPlayServerSpawnEntity wrapper = new WrapperPlayServerSpawnEntity((PacketSendEvent) e);
                        return new PacketEventsPlayClientSpawnEntityPacket(wrapper);
                    }));
                }
            }
        }, c(priority));
    }

    @Override
    public void listenToPlayClientEntityTeleport(Plugin plugin, PlatformPacketListenerPriority priority, PlatformPacketEventListener<ProtocolPacketEvent, PacketWrapper<?>, PlatformPlayClientEntityTeleportPacket<PacketWrapper<?>>> listener) {
        platform.getPacketEventsAPI().getEventManager().registerListener(new PacketListener() {
            @Override
            public void onPacketSend(@NotNull PacketSendEvent event) {
                PacketTypeCommon type = event.getPacketType();
                if (type == PacketType.Play.Server.ENTITY_TELEPORT) {
                    listener.handle(new PacketEventsPacketEvent<>(event, e -> {
                        WrapperPlayServerEntityTeleport wrapper = new WrapperPlayServerEntityTeleport((PacketSendEvent) e);
                        return new PacketEventsPlayClientEntityTeleportPacket(wrapper);
                    }));
                }
            }
        }, c(priority));
    }

    @Override
    public void listenToPlayClientRelativeEntityMove(Plugin plugin, PlatformPacketListenerPriority priority, PlatformPacketEventListener<ProtocolPacketEvent, PacketWrapper<?>, PlatformPlayClientRelativeEntityMovePacket<PacketWrapper<?>>> listener) {
        platform.getPacketEventsAPI().getEventManager().registerListener(new PacketListener() {
            @Override
            public void onPacketSend(@NotNull PacketSendEvent event) {
                PacketTypeCommon type = event.getPacketType();
                if (type == PacketType.Play.Server.ENTITY_RELATIVE_MOVE) {
                    listener.handle(new PacketEventsPacketEvent<>(event, e -> {
                        WrapperPlayServerEntityRelativeMove wrapper = new WrapperPlayServerEntityRelativeMove((PacketSendEvent) e);
                        return new PacketEventsPlayClientRelativeEntityMovePacket(wrapper);
                    }));
                }
            }
        }, c(priority));
    }

    @Override
    public void listenToPlayClientRelativeEntityMoveLook(Plugin plugin, PlatformPacketListenerPriority priority, PlatformPacketEventListener<ProtocolPacketEvent, PacketWrapper<?>, PlatformPlayClientRelativeEntityMoveLookPacket<PacketWrapper<?>>> listener) {
        platform.getPacketEventsAPI().getEventManager().registerListener(new PacketListener() {
            @Override
            public void onPacketSend(@NotNull PacketSendEvent event) {
                PacketTypeCommon type = event.getPacketType();
                if (type == PacketType.Play.Server.ENTITY_RELATIVE_MOVE_AND_ROTATION) {
                    listener.handle(new PacketEventsPacketEvent<>(event, e -> {
                        WrapperPlayServerEntityRelativeMoveAndRotation wrapper = new WrapperPlayServerEntityRelativeMoveAndRotation((PacketSendEvent) e);
                        return new PacketEventsPlayClientRelativeEntityMoveLookPacket(wrapper);
                    }));
                }
            }
        }, c(priority));
    }
}
