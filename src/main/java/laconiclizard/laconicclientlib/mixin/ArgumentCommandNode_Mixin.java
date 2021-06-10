package laconiclizard.laconicclientlib.mixin;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContextBuilder;
import com.mojang.brigadier.context.ParsedArgument;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.ArgumentCommandNode;
import laconiclizard.laconicclientlib.command.ContextualArgumentType;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ArgumentCommandNode.class)
public abstract class ArgumentCommandNode_Mixin<S, T> {

    @Shadow(remap = false) @Final private ArgumentType<T> type;
    @Shadow(remap = false) @Final private String name;

    @Inject(method = "parse(Lcom/mojang/brigadier/StringReader;Lcom/mojang/brigadier/context/CommandContextBuilder;)V",
            at = @At("HEAD"), cancellable = true, remap = false)
    public void pre_parse(StringReader reader, CommandContextBuilder<S> contextBuilder, CallbackInfo ci) throws CommandSyntaxException {
        if (ci.isCancelled()) return;
        // copied from source
        final int start = reader.getCursor();
        // this line is changed
        final T result = ((ContextualArgumentType<T>) type).parseWithContext(reader, contextBuilder.build(reader.getRead()));
        final ParsedArgument<S, T> parsed = new ParsedArgument<>(start, reader.getCursor(), result);
        contextBuilder.withArgument(name, parsed);
        //noinspection unchecked,ConstantConditions
        contextBuilder.withNode((ArgumentCommandNode<S, T>) (Object) this, parsed.getRange());

        ci.cancel();
    }

    @Inject(method = "isValidInput(Ljava/lang/String;)Z",
            at = @At("HEAD"), cancellable = true, remap = false)
    public void pre_isValidInput(String input, CallbackInfoReturnable<Boolean> cir) {
        if (cir.isCancelled()) return;
        final StringReader reader = new StringReader(input);
        cir.setReturnValue(((ContextualArgumentType<T>) type).isPotentiallyValidInput(reader));
        cir.cancel();
    }

}
