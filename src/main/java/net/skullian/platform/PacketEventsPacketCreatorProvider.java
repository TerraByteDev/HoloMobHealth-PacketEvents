package net.skullian.platform;

import com.github.retrooper.packetevents.protocol.entity.data.EntityData;
import com.github.retrooper.packetevents.protocol.entity.data.EntityDataType;
import com.github.retrooper.packetevents.protocol.entity.data.EntityDataTypes;
import com.github.retrooper.packetevents.protocol.entity.type.EntityTypes;
import com.github.retrooper.packetevents.util.Vector3d;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerDestroyEntities;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityMetadata;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityTeleport;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerSpawnEntity;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerSpawnLivingEntity;
import com.loohp.holomobhealth.HoloMobHealth;
import com.loohp.holomobhealth.holders.DataWatcherField;
import com.loohp.holomobhealth.holders.DataWatcherFieldType;
import com.loohp.holomobhealth.holders.DataWatcherFields;
import com.loohp.holomobhealth.holders.IHoloMobArmorStand;
import com.loohp.holomobhealth.libs.net.kyori.adventure.text.Component;
import com.loohp.holomobhealth.libs.net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import com.loohp.holomobhealth.nms.NMS;
import com.loohp.holomobhealth.platform.PlatformPacketCreatorProvider;
import com.loohp.holomobhealth.platform.packets.PlatformPacket;
import com.loohp.holomobhealth.utils.MCVersion;
import io.github.retrooper.packetevents.util.SpigotReflectionUtil;
import net.kyori.adventure.platform.bukkit.BukkitComponentSerializer;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class PacketEventsPacketCreatorProvider implements PlatformPacketCreatorProvider<PacketWrapper<?>> {

    @SuppressWarnings("unchecked")
    private static void addOrReplaceDataWatcher(List<EntityData<?>> entityData, DataWatcherField field, Object value) {
        for (EntityData<?> data : entityData) {
            if (data.getIndex() == field.getIndex() && data.getType().equals(c(field.getType()))) {
                ((EntityData<Object>) data).setValue(value);
                return;
            }
        }
        entityData.add(new EntityData<>(field.getIndex(), (EntityDataType<Object>) c(field.getType()), value));
    }

    @SuppressWarnings("EnhancedSwitchMigration")
    private static EntityDataType<?> c(DataWatcherFieldType type) {
        switch (type) {
            case OPTIONAL_CHAT:
                return EntityDataTypes.OPTIONAL_ADV_COMPONENT;
            case STRING:
                return EntityDataTypes.STRING;
            case BOOLEAN:
                return EntityDataTypes.BOOLEAN;
            case BYTE:
                return EntityDataTypes.BYTE;
            default:
                throw new IllegalArgumentException("Unknown type " + type);
        }
    }

    @SuppressWarnings("unchecked")
    private static EntityData<?> c(DataWatcherField field, Object value) {
        if (field.getType().equals(DataWatcherFieldType.OPTIONAL_CHAT)) {
            if (value instanceof String) {
                return new EntityData<>(field.getIndex(), EntityDataTypes.OPTIONAL_ADV_COMPONENT, ((String) value).isEmpty() ? Optional.empty() : Optional.of(net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer.legacySection().deserialize((String) value)));
            } else {
                return new EntityData<>(field.getIndex(), EntityDataTypes.OPTIONAL_ADV_COMPONENT, (Optional<net.kyori.adventure.text.Component>) value);
            }
        } else if (field.getType().equals(DataWatcherFieldType.STRING)) {
            if (value instanceof String) {
                return new EntityData<>(field.getIndex(), EntityDataTypes.STRING, (String) value);
            } else {
                Optional<net.kyori.adventure.text.Component> chat = (Optional<net.kyori.adventure.text.Component>) value;
                return new EntityData<>(field.getIndex(), EntityDataTypes.STRING, chat.map(c -> net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer.legacySection().serialize(c)).orElse(""));
            }
        } else if (field.getType().equals(DataWatcherFieldType.BOOLEAN)) {
            if (value instanceof Byte) {
                return new EntityData<>(field.getIndex(), EntityDataTypes.BOOLEAN, (Byte) value != 0);
            } else {
                return new EntityData<>(field.getIndex(), EntityDataTypes.BOOLEAN, (Boolean) value);
            }
        } else if (field.getType().equals(DataWatcherFieldType.BYTE)) {
            if (value instanceof Byte) {
                return new EntityData<>(field.getIndex(), EntityDataTypes.BYTE, (Byte) value);
            } else {
                return new EntityData<>(field.getIndex(), EntityDataTypes.BYTE, (Boolean) value ? (byte) 1 : 0);
            }
        } else {
            throw new IllegalArgumentException("Unknown type " + field.getType());
        }
    }

    private static EntityData<?> getByFieldIndex(List<EntityData<?>> entityData, int index) {
        return entityData.stream().filter(e -> e.getIndex() == index).findFirst().orElse(null);
    }

    private static List<PlatformPacket<PacketWrapper<?>>> c(PacketWrapper<?>... packets) {
        return Arrays.stream(packets).map(PacketEventsGenericCreatorPacket::new).collect(Collectors.toList());
    }

    private static List<PlatformPacket<PacketWrapper<?>>> c(PacketWrapper<?> packet) {
        return Collections.singletonList(new PacketEventsGenericCreatorPacket(packet));
    }

    private static com.github.retrooper.packetevents.protocol.world.Location c(Location location) {
        return new com.github.retrooper.packetevents.protocol.world.Location(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
    }

    private static class PacketEventsGenericCreatorPacket extends PlatformPacket<PacketWrapper<?>> {
        public PacketEventsGenericCreatorPacket(PacketWrapper<?> handle) {
            super(handle);
        }
        @Override
        public PacketEventsGenericCreatorPacket shallowClone() {
            return new PacketEventsGenericCreatorPacket(handle);
        }
    }

    @Override
    public List<PlatformPacket<PacketWrapper<?>>> createEntityDestroyPackets(int... entityIds) {
        return c(new WrapperPlayServerDestroyEntities(entityIds));
    }

    @Override
    public List<PlatformPacket<PacketWrapper<?>>> createEntityTeleportPackets(int entityId, Location location) {
        return c(new WrapperPlayServerEntityTeleport(entityId, c(location), false));
    }

    @SuppressWarnings("UnstableApiUsage")
    @Override
    public List<PlatformPacket<PacketWrapper<?>>> createUpdateEntityPackets(Entity entity) {
        DataWatcherFields fields = NMS.getInstance().getDataWatcherFields();

        List<EntityData<?>> actualEntityData = SpigotReflectionUtil.getEntityMetadata(entity);

        List<EntityData<?>> entityData = new ArrayList<>();
        DataWatcherField name = fields.getCustomNameField();
        entityData.add(c(name, getByFieldIndex(actualEntityData, name.getIndex())));

        DataWatcherField visible = fields.getCustomNameVisibleField();
        entityData.add(c(visible, getByFieldIndex(actualEntityData, visible.getIndex())));

        return c(new WrapperPlayServerEntityMetadata(entity.getEntityId(), entityData));
    }

    @Override
    public List<PlatformPacket<PacketWrapper<?>>> createUpdateEntityMetadataPackets(Entity entity, Component entityNameComponent, boolean visible) {
        DataWatcherFields fields = NMS.getInstance().getDataWatcherFields();

        List<EntityData<?>> entityData = new ArrayList<>();

        DataWatcherField customNameField = fields.getCustomNameField();
        Optional<net.kyori.adventure.text.Component> name = entityNameComponent == null ? Optional.empty() : Optional.of(BukkitComponentSerializer.gson().deserialize(GsonComponentSerializer.gson().serialize(entityNameComponent)));
        entityData.add(c(customNameField, name));

        DataWatcherField visibleField = fields.getCustomNameVisibleField();
        entityData.add(c(visibleField, visible));

        return c(new WrapperPlayServerEntityMetadata(entity.getEntityId(), entityData));
    }

    @SuppressWarnings("deprecation")
    @Override
    public List<PlatformPacket<PacketWrapper<?>>> createArmorStandSpawnPackets(IHoloMobArmorStand entity, Component component, boolean visible) {
        PacketWrapper<?> packet1;
        if (HoloMobHealth.version.isNewerOrEqualTo(MCVersion.V1_19)) {
            packet1 = new WrapperPlayServerSpawnEntity(entity.getEntityId(), entity.getUniqueId(), EntityTypes.getByName(entity.getType().getName()), c(entity.getLocation()), entity.getLocation().getYaw(), 0, null);
        } else {
            packet1 = new WrapperPlayServerSpawnLivingEntity(entity.getEntityId(), entity.getUniqueId(), EntityTypes.getByName(entity.getType().getName()), c(entity.getLocation()), entity.getLocation().getPitch(), Vector3d.zero(), Collections.emptyList());
        }

        DataWatcherFields fields = NMS.getInstance().getDataWatcherFields();

        List<EntityData<?>> entityData = new ArrayList<>();

        DataWatcherField byteField = fields.getByteField();
        byte bitmask = 0x20;
        entityData.add(c(byteField, bitmask));

        DataWatcherField customNameField = fields.getCustomNameField();
        Optional<net.kyori.adventure.text.Component> name = component == null ? Optional.empty() : Optional.of(BukkitComponentSerializer.gson().deserialize(GsonComponentSerializer.gson().serialize(component)));
        entityData.add(c(customNameField, name));

        DataWatcherField visibleField = fields.getCustomNameVisibleField();
        entityData.add(c(visibleField, visible));

        DataWatcherField armorStandByteField = fields.getArmorStandByteField();
        byte standbitmask = 0x01 | 0x10;
        entityData.add(c(armorStandByteField, standbitmask));

        WrapperPlayServerEntityMetadata packet2 = new WrapperPlayServerEntityMetadata(entity.getEntityId(), entityData);

        return c(packet1, packet2);
    }

    @Override
    public List<PlatformPacket<PacketWrapper<?>>> createUpdateArmorStandPackets(IHoloMobArmorStand entity, Component component, boolean visible) {
        DataWatcherFields fields = NMS.getInstance().getDataWatcherFields();

        List<EntityData<?>> entityData = new ArrayList<>();

        DataWatcherField byteField = fields.getByteField();
        byte bitmask = 0x20;
        entityData.add(c(byteField, bitmask));

        DataWatcherField customNameField = fields.getCustomNameField();
        Optional<net.kyori.adventure.text.Component> name = component == null ? Optional.empty() : Optional.of(BukkitComponentSerializer.gson().deserialize(GsonComponentSerializer.gson().serialize(component)));
        entityData.add(c(customNameField, name));

        DataWatcherField visibleField = fields.getCustomNameVisibleField();
        entityData.add(c(visibleField, visible));

        DataWatcherField armorStandByteField = fields.getArmorStandByteField();
        byte standbitmask = 0x01 | 0x10;
        entityData.add(c(armorStandByteField, standbitmask));

        WrapperPlayServerEntityMetadata packet1 = new WrapperPlayServerEntityMetadata(entity.getEntityId(), entityData);

        WrapperPlayServerEntityTeleport packet2 = new WrapperPlayServerEntityTeleport(entity.getEntityId(), c(entity.getLocation()), false);

        return c(packet1, packet2);
    }

    @Override
    public List<PlatformPacket<PacketWrapper<?>>> createUpdateArmorStandLocationPackets(IHoloMobArmorStand entity) {
        return c(new WrapperPlayServerEntityTeleport(entity.getEntityId(), c(entity.getLocation()), false));
    }

    @Override
    public List<PlatformPacket<PacketWrapper<?>>> createSpawnDamageIndicatorPackets(int entityId, UUID uuid, Component entityNameComponent, Location location, Vector velocity, boolean gravity) {
        PacketWrapper<?> packet1;
        if (HoloMobHealth.version.isNewerOrEqualTo(MCVersion.V1_19)) {
            packet1 = new WrapperPlayServerSpawnEntity(entityId, uuid, EntityTypes.ARMOR_STAND, c(location), location.getYaw(), 0, null);
        } else {
            packet1 = new WrapperPlayServerSpawnLivingEntity(entityId, uuid, EntityTypes.ARMOR_STAND, c(location), location.getPitch(), Vector3d.zero(), Collections.emptyList());
        }

        DataWatcherFields fields = NMS.getInstance().getDataWatcherFields();

        List<EntityData<?>> entityData = new ArrayList<>();

        DataWatcherField byteField = fields.getByteField();
        byte bitmask = 0x20;
        entityData.add(c(byteField, bitmask));

        DataWatcherField customNameField = fields.getCustomNameField();
        Optional<net.kyori.adventure.text.Component> name = entityNameComponent == null ? Optional.empty() : Optional.of(BukkitComponentSerializer.gson().deserialize(GsonComponentSerializer.gson().serialize(entityNameComponent)));
        entityData.add(c(customNameField, name));

        DataWatcherField visibleField = fields.getCustomNameVisibleField();
        entityData.add(c(visibleField, true));

        if (fields.hasSilentField()) {
            DataWatcherField silentField = fields.getSilentField();
            entityData.add(c(silentField, true));
        }

        if (fields.hasNoGravityField()) {
            DataWatcherField noGravityField = fields.getNoGravityField();
            entityData.add(c(noGravityField, !gravity));
        }

        DataWatcherField armorStandByteField = fields.getArmorStandByteField();
        byte standbitmask = (byte) 0x01 | 0x08 | 0x10;
        entityData.add(c(armorStandByteField, standbitmask));

        WrapperPlayServerEntityMetadata packet2 = new WrapperPlayServerEntityMetadata(entityId, entityData);

        return c(packet1, packet2);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void modifyDataWatchers(List<?> dataWatchers, Component entityNameComponent, boolean visible) {
        List<EntityData<?>> entityData = (List<EntityData<?>>) dataWatchers;
        DataWatcherFields fields = NMS.getInstance().getDataWatcherFields();
        DataWatcherField customNameField = fields.getCustomNameField();
        DataWatcherField visibleField = fields.getCustomNameVisibleField();
        Optional<net.kyori.adventure.text.Component> name = entityNameComponent == null ? Optional.empty() : Optional.of(BukkitComponentSerializer.gson().deserialize(GsonComponentSerializer.gson().serialize(entityNameComponent)));
        addOrReplaceDataWatcher(entityData, customNameField, name);
        addOrReplaceDataWatcher(entityData, visibleField, visible);
    }
}
