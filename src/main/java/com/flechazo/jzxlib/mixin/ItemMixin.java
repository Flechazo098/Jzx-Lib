package com.flechazo.jzxlib.mixin;

import com.flechazo.jzxlib.common.extension.SlashBladeItemExtensions;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Item.class)
public class ItemMixin implements SlashBladeItemExtensions {
}
