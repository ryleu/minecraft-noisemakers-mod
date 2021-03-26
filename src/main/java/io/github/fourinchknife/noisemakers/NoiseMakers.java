package io.github.fourinchknife.noisemakers;

import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class NoiseMakers implements ModInitializer {
	@Override
	public void onInitialize() {
		Registry.register(
				Registry.ITEM,
				new Identifier("noisemakers","noisemaker"),
				NoiseMakerItem.NOISEMAKER
			);

		for (NoiseMakerItem.Type type : NoiseMakerItem.Type.values()){
			NoiseMakerItem noisemaker = new NoiseMakerItem(type);
			System.out.println("Loading Noisemakers: " + noisemaker.path);
			noisemaker.registerSelf();
		}
	}
}
