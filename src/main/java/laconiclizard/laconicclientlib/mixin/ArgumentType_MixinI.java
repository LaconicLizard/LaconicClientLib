package laconiclizard.laconicclientlib.mixin;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

public interface ArgumentType_MixinI<T> {

    <S> T parseWithContext(StringReader reader, CommandContext<S> context) throws CommandSyntaxException;

}
