package net.ethyl.lattice_api.core.instances.objects;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class LatticeBlockEntity extends BlockEntity {
    private CompoundTag compoundTag = new CompoundTag();

    public LatticeBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }

    public void setTag(@NotNull String tagId, @NotNull String value) {
        this.compoundTag.putString(tagId, value);
        this.mark();
    }

    public void setTag(@NotNull String tagId, int value) {
        this.compoundTag.putInt(tagId, value);
        this.mark();
    }

    public void setTag(@NotNull String tagId, float value) {
        this.compoundTag.putFloat(tagId, value);
        this.mark();
    }

    public void setTag(@NotNull String tagId, boolean value) {
        this.compoundTag.putBoolean(tagId, value);
        this.mark();
    }

    public String getString(@NotNull String tagId) {
        return this.compoundTag.getString(tagId);
    }

    public int getInt(@NotNull String tagId) {
        return this.compoundTag.getInt(tagId);
    }

    public float getFloat(@NotNull String tagId) {
        return this.compoundTag.getFloat(tagId);
    }

    public boolean getBoolean(@NotNull String tagId) {
        return this.compoundTag.getBoolean(tagId);
    }

    public boolean hasTag(@NotNull String tagId) {
        return this.compoundTag.get(tagId) != null;
    }

    public void mark() {
        this.setChanged();

        if (this.level != null && !this.level.isClientSide()) {
            level.sendBlockUpdated(this.worldPosition, this.getBlockState(), this.getBlockState(), 3);
        }
    }

    @Override
    public @NotNull CompoundTag getUpdateTag(@NotNull HolderLookup.Provider registries) {
        return this.compoundTag.copy();
    }

    @Override
    public void handleUpdateTag(@NotNull CompoundTag compoundTag, @NotNull HolderLookup.Provider lookupProvider) {
        this.compoundTag = compoundTag.copy();
    }

    @Override
    public @Nullable Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void onDataPacket(@NotNull Connection connection, ClientboundBlockEntityDataPacket packet, @NotNull HolderLookup.Provider lookupProvider) {
        handleUpdateTag(packet.getTag(), lookupProvider);
    }
}
