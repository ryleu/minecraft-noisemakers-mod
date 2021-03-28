package io.github.fourinchknife.noisemakers;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
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

import java.util.ArrayList;

public class NoiseMakerItem extends Item {
    public static final Item NOISEMAKER = new Item(new FabricItemSettings());
    public static final ItemGroup NOISEMAKERS_GROUP = FabricItemGroupBuilder.build(
            new Identifier("noisemakers", "general"),
            () -> new ItemStack(NOISEMAKER)
    );

    private enum Type {
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
    private final Type itemType;

    private static ArrayList<NoiseMakerItem> allItems = null;

    private final SoundEvent playedSound;
    private final String craftingItem;

    private NoiseMakerItem(Type type) {
        super(new FabricItemSettings()
                .group(NoiseMakerItem.NOISEMAKERS_GROUP)
                .maxCount(1));
        itemType = type;
        switch (itemType){
            case BLAZE:
                playedSound = SoundEvents.ENTITY_BLAZE_AMBIENT;
                craftingItem = "minecraft:blaze_rod";
                break;
            case CREEPER:
                playedSound = SoundEvents.ENTITY_CREEPER_PRIMED;
                craftingItem = "minecraft:gunpowder";
                break;
            case ENDERMAN:
                playedSound = SoundEvents.ENTITY_ENDERMAN_AMBIENT;
                craftingItem = "minecraft:ender_pearl";
                break;
            case EVOKER:
                playedSound = SoundEvents.ENTITY_EVOKER_AMBIENT;
                craftingItem = "minecraft:totem_of_undying";
                break;
            case GHAST:
                playedSound = SoundEvents.ENTITY_GHAST_AMBIENT;
                craftingItem = "minecraft:ghast_tear";
                break;
            case GUARDIAN:
                playedSound = SoundEvents.ENTITY_GUARDIAN_AMBIENT_LAND;
                craftingItem = "minecraft:prismarine_shard";
                break;
            case PHANTOM:
                playedSound = SoundEvents.ENTITY_PHANTOM_AMBIENT;
                craftingItem = "minecraft:phantom_membrane";
                break;
            case PIGLIN:
                playedSound = SoundEvents.ENTITY_PIGLIN_BRUTE_ANGRY;
                craftingItem = "minecraft:gold_ingot";
                break;
            case PILLAGER:
                playedSound = SoundEvents.ENTITY_PILLAGER_AMBIENT;
                craftingItem = "minecraft:iron_axe";
                break;
            case SHULKER:
                playedSound = SoundEvents.ENTITY_SHULKER_AMBIENT;
                craftingItem = "minecraft:shulker_shell";
                break;
            case SKELETON:
                playedSound = SoundEvents.ENTITY_SKELETON_AMBIENT;
                craftingItem = "minecraft:bone";
                break;
            case SPIDER:
                playedSound = SoundEvents.ENTITY_SPIDER_AMBIENT;
                craftingItem = "minecraft:spider_eye";
                break;
            case ZOMBIE:
                playedSound = SoundEvents.ENTITY_ZOMBIE_AMBIENT;
                craftingItem = "minecraft:rotten_flesh";
                break;
            default:
                playedSound = null;
                craftingItem = null;
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
                new Identifier("noisemakers", getItemName() + "_noisemaker"),
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
    public JsonObject getRecipe() {
        //Creating a new json object, where we will store our recipe.
        JsonObject json = new JsonObject();
        //The "type" of the recipe we are creating. In this case, a shaped recipe.
        json.addProperty("type", "minecraft:crafting_shaped");
        //This creates:
        //"type": "minecraft:crafting_shaped"

        //We create a new Json Element, and add our crafting pattern to it.
        JsonArray jsonArray = new JsonArray();
        jsonArray.add("SSS");
        jsonArray.add("IMI");
        //Then we add the pattern to our json object.
        json.add("pattern", jsonArray);
        //This creates:
        //"pattern": [
        //  "SSS",
        //  "IMI"
        //]

        //Next we need to define what the keys in the pattern are. For this we need different JsonObjects per key definition, and one main JsonObject that will contain all of the defined keys.

        JsonObject keyList = new JsonObject(); //The main key object, containing all the keys

        // minecraft:string
        JsonObject individualKey = new JsonObject();
        individualKey.addProperty("item", "minecraft:string");
        keyList.add("S", individualKey);

        // minecraft:iron_ingot
        individualKey = new JsonObject();
        individualKey.addProperty("item", "minecraft:iron_ingot");
        keyList.add("I", individualKey);

        // other mob-specific item
        individualKey = new JsonObject();
        individualKey.addProperty("item", craftingItem);
        keyList.add("M", individualKey);


        // Adds all of those together
        json.add("key", keyList);

        //Finally, we define our result object
        JsonObject result = new JsonObject();
        result.addProperty("item", "noisemakers:" + getItemName() + "_noisemaker");
        result.addProperty("count", 1);
        json.add("result", result);
        //This creates:
        //"result": {
        //  "item": "noisemakers:<something>_noisemaker",
        //  "count": 1
        //}

        return json;
    }

    public static ArrayList<NoiseMakerItem> getAllTypes (){
        if (allItems == null){
            ArrayList<NoiseMakerItem> tempItems = new ArrayList<>();
            for (Type type : Type.values()){
                tempItems.add(new NoiseMakerItem(type));
            }
            allItems = tempItems;
        }
        return allItems;
    }

    public String getItemName() {
        return itemType.toString().toLowerCase();
    }
}