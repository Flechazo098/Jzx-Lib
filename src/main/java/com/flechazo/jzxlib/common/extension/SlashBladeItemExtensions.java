package com.flechazo.jzxlib.common.extension;

import com.google.common.collect.Multimap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public interface SlashBladeItemExtensions {
    private Item self()
    {
        return (Item) this;
    }

    /**
     * ItemStack sensitive version of getItemAttributeModifiers
     */
    @SuppressWarnings("deprecation")
    default Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack)
    {
        return self().getDefaultAttributeModifiers(slot);
    }

    /**
     * Checks whether an item can be enchanted with a certain enchantment. This
     * applies specifically to enchanting an item in the enchanting table and is
     * called when retrieving the list of possible enchantments for an item.
     * Enchantments may additionally (or exclusively) be doing their own checks in
     * check the individual implementation for reference. By default this will check
     * if the enchantment type is valid for this item type.
     *
     * @param stack       the item stack to be enchanted
     * @param enchantment the enchantment to be applied
     * @return true if the enchantment can be applied to this item
     */
    default boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment)
    {
        return enchantment.category.canEnchant(stack.getItem());
    }

    /**
     * Set the damage for this itemstack. Note, this method is responsible for zero
     * checking.
     *
     * @param stack  the stack
     * @param damage the new damage value
     */
    default void setDamage(ItemStack stack, int damage)
    {
        stack.getOrCreateTag().putInt("Damage", Math.max(0, damage));
    }

    /**
     * Reduce the durability of this item by the amount given.
     * This can be used to e.g. consume power from NBT before durability.
     *
     * @param stack The itemstack to damage
     * @param amount The amount to damage
     * @param entity The entity damaging the item
     * @param onBroken The on-broken callback from vanilla
     * @return The amount of damage to pass to the vanilla logic
     */
    default <T extends LivingEntity> int damageItem(ItemStack stack, int amount, T entity, Consumer<T> onBroken) {
        return amount;
    }

    /**
     * Override this method to change the NBT data being sent to the client. You
     * should ONLY override this when you have no other choice, as this might change
     * behavior client side!
     *
     * Note that this will sometimes be applied multiple times, the following MUST
     * be supported:
     *   Item item = stack.getItem();
     *   NBTTagCompound nbtShare1 = item.getNBTShareTag(stack);
     *   stack.setTagCompound(nbtShare1);
     *   NBTTagCompound nbtShare2 = item.getNBTShareTag(stack);
     *   assert nbtShare1.equals(nbtShare2);
     *
     * @param stack The stack to send the NBT tag for
     * @return The NBT tag
     */
    @Nullable
    default CompoundTag getShareTag(ItemStack stack)
    {
        return stack.getTag();
    }

    /**
     * Override this method to decide what to do with the NBT data received from
     * getNBTShareTag().
     *
     * @param stack The stack that received NBT
     * @param nbt   Received NBT, can be null
     */
    default void readShareTag(ItemStack stack, @Nullable CompoundTag nbt)
    {
        stack.setTag(nbt);
    }

    /**
     * Return the itemDamage represented by this ItemStack. Defaults to the Damage
     * entry in the stack NBT, but can be overridden here for other sources.
     *
     * @param stack The itemstack that is damaged
     * @return the damage value
     */
    default int getDamage(ItemStack stack)
    {
        return !stack.hasTag() ? 0 : stack.getTag().getInt("Damage");
    }

    /**
     * Return the maxDamage for this ItemStack. Defaults to the maxDamage field in
     * this item, but can be overridden here for other sources such as NBT.
     *
     * @param stack The itemstack that is damaged
     * @return the damage value
     */
    @SuppressWarnings("deprecation")
    default int getMaxDamage(ItemStack stack)
    {
        return self().getMaxDamage();
    }

    /**
     * Called when a entity tries to play the 'swing' animation.
     *
     * @param entity The entity swinging the item.
     * @return True to cancel any further processing by EntityLiving
     */
    default boolean onEntitySwing(ItemStack stack, LivingEntity entity)
    {
        return false;
    }

    /**
     * Called by the default implemetation of EntityItem's onUpdate method, allowing
     * for cleaner control over the update of the item without having to write a
     * subclass.
     *
     * @param entity The entity Item
     * @return Return true to skip any further update code.
     */
    default boolean onEntityItemUpdate(ItemStack stack, ItemEntity entity)
    {
        return false;
    }

    /**
     * Retrieves the normal 'lifespan' of this item when it is dropped on the ground
     * as a EntityItem. This is in ticks, standard result is 6000, or 5 mins.
     *
     * @param itemStack The current ItemStack
     * @param level     The level the entity is in
     * @return The normal lifespan in ticks.
     */
    default int getEntityLifespan(ItemStack itemStack, Level level)
    {
        return 6000;
    }
}
