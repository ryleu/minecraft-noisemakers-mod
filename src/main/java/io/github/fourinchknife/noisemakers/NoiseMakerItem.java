package io.github.fourinchknife.noisemakers;

import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.dispenser.DispenserBehavior;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPointer;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

public class NoiseMakerItem extends Item {
    public static final Item NOISEMAKER = new Item(new FabricItemSettings());
    public static final ItemGroup NOISEMAKERS_GROUP = FabricItemGroupBuilder.build(
            new Identifier("noisemakers", "general"),
            () -> new ItemStack(NOISEMAKER)
    );

    public enum Type {
        BLAZE,
        CREEPER,
        ENDERMAN,
        EVOKER,
        GHAST,
        GUARDIAN,
        PHANTOM,
        PIGLIN,
        PILLAGER,
        SHULKER,
        SKELETON,
        SPIDER,
        ZOMBIE
    }

    public SoundEvent playedSound;
    public String path;

    public NoiseMakerItem(Type type) {
        super(new FabricItemSettings()
                .group(NoiseMakerItem.NOISEMAKERS_GROUP)
                .maxCount(1));
        switch (type){
            case BLAZE:
                playedSound = SoundEvents.ENTITY_BLAZE_AMBIENT;
                path = "blaze_noisemaker";
                break;
            case CREEPER:
                playedSound = SoundEvents.ENTITY_CREEPER_PRIMED;
                path = "creeper_noisemaker";
                break;
            case ENDERMAN:
                playedSound = SoundEvents.ENTITY_ENDERMAN_AMBIENT;
                path = "enderman_noisemaker";
                break;
            case EVOKER:
                playedSound = SoundEvents.ENTITY_EVOKER_AMBIENT;
                path = "evoker_noisemaker";
                break;
            case GHAST:
                playedSound = SoundEvents.ENTITY_GHAST_AMBIENT;
                path = "ghast_noisemaker";
                break;
            case GUARDIAN:
                playedSound = SoundEvents.ENTITY_GUARDIAN_AMBIENT_LAND;
                path = "guardian_noisemaker";
                break;
            case PHANTOM:
                playedSound = SoundEvents.ENTITY_PHANTOM_AMBIENT;
                path = "phantom_noisemaker";
                break;
            case PIGLIN:
                playedSound = SoundEvents.ENTITY_PIGLIN_BRUTE_ANGRY;
                path = "piglin_noisemaker";
                break;
            case PILLAGER:
                playedSound = SoundEvents.ENTITY_PILLAGER_AMBIENT;
                path = "pillager_noisemaker";
                break;
            case SHULKER:
                playedSound = SoundEvents.ENTITY_SHULKER_AMBIENT;
                path = "shulker_noisemaker";
                break;
            case SKELETON:
                playedSound = SoundEvents.ENTITY_SKELETON_AMBIENT;
                path = "skeleton_noisemaker";
                break;
            case SPIDER:
                playedSound = SoundEvents.ENTITY_SPIDER_AMBIENT;
                path = "spider_noisemaker";
                break;
            case ZOMBIE:
                playedSound = SoundEvents.ENTITY_ZOMBIE_AMBIENT;
                path = "zombie_noisemaker";
                break;
            default:
                playedSound = SoundEvents.UI_BUTTON_CLICK;
                path = "noisemaker";
                break;
        }
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity playerEntity, Hand hand) {
        playerEntity.playSound(playedSound, SoundCategory.VOICE, 1.0F, 1.0F);
        playerEntity.getItemCooldownManager().set(this, 10);
        return TypedActionResult.success(playerEntity.getStackInHand(hand));
    }

    public void registerSelf() {
        Registry.register(
                Registry.ITEM,
                new Identifier("noisemakers", path),
                this
        );

        DispenserBlock.registerBehavior(this, new DispenserBehavior() {
            public ItemStack dispense(BlockPointer pointer, ItemStack stack) {
                return dispenseSilently(pointer, stack);
            }
            public ItemStack dispenseSilently(BlockPointer pointer, ItemStack stack){
                pointer.getWorld().playSound(null,pointer.getBlockPos(), playedSound, SoundCategory.VOICE, 1.0F, 1.0F);
                return stack;
            }
        });
    }
}