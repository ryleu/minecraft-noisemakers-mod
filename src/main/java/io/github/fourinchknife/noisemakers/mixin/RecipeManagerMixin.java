package io.github.fourinchknife.noisemakers.mixin;

import com.google.gson.JsonElement;
import io.github.fourinchknife.noisemakers.NoiseMakerItem;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.Profiler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(RecipeManager.class)
public class RecipeManagerMixin {
    @Inject(method = "apply", at = @At("HEAD"))
    public void interceptApply(Map<Identifier, JsonElement> map, ResourceManager resourceManager, Profiler profiler, CallbackInfo info) {
        for (NoiseMakerItem noisemaker : NoiseMakerItem.getAllTypes()){
            map.put(new Identifier("noisemakers", noisemaker.getItemName() + "_noisemaker"), noisemaker.getRecipe());
        }
    }
}
