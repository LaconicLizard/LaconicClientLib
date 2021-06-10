package laconiclizard.laconicclientlib.mixin;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ArgumentType.class)
public interface ArgumentType_Mixin<T> extends ArgumentType<T>, ArgumentType_MixinI<T> {

    default <S> T parseWithContext(StringReader reader, CommandContext<S> context) throws CommandSyntaxException {
        return parse(reader);
    }

}
