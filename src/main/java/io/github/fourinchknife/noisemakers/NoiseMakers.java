package io.github.fourinchknife.noisemakers;

import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class NoiseMakers implements ModInitializer {
	@Override
	public void onInitialize() {
		// Register the dummy item for the item group icon
		Registry.register(
				Registry.ITEM,
				new Identifier("noisemakers","noisemaker"),
				NoiseMakerItem.NOISEMAKER
			);

		// Register everything else
		for (NoiseMakerItem noisemaker : NoiseMakerItem.getAllTypes()){
			System.out.println("Registering noisemakers:" + noisemaker.getItemName() + "_noisemaker");
			noisemaker.registerSelf();
		}
	}
}
