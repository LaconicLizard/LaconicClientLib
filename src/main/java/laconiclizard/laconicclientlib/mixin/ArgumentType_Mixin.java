package laconiclizard.laconicclientlib.mixin;

import com.mojang.brigadier.arguments.ArgumentType;
import laconiclizard.laconicclientlib.ContextualArgumentType;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ArgumentType.class)
public interface ArgumentType_Mixin<T> extends ContextualArgumentType<T> {

}
