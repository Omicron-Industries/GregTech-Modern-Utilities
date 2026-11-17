package net.neganote.gtutilities.mixin.gt;

import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.integration.ae2.machine.MEPatternBufferPartMachine;
import com.gregtechceu.gtceu.integration.ae2.machine.MEPatternBufferProxyPartMachine;
import com.gregtechceu.gtceu.integration.ae2.machine.trait.ProxySlotRecipeHandler;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = MEPatternBufferProxyPartMachine.class, remap = false)
public class MEPatternBufferProxyPartMachineMixin {

    @Final
    @Shadow
    private ProxySlotRecipeHandler proxySlotRecipeHandler;

    @ModifyExpressionValue(method = "setBuffer",
                           at = @At(value = "INVOKE",
                                    target = "Lcom/gregtechceu/gtceu/api/machine/MetaMachine;getMachine(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;)Lcom/gregtechceu/gtceu/api/machine/MetaMachine;"))
    public MetaMachine gtmutils$saveMetaMachineSemilocal(MetaMachine original,
                                                         @Share("machine") LocalRef<MetaMachine> machine) {
        machine.set(original);
        return original;
    }

    @Definition(id = "MEPatternBufferPartMachine", type = MEPatternBufferPartMachine.class)
    @Expression("? instanceof MEPatternBufferPartMachine")
    @ModifyExpressionValue(method = "setBuffer", at = @At(value = "MIXINEXTRAS:EXPRESSION"), remap = false)
    public boolean gtmutils$ensureCorrectBufferSize(boolean original, @Share("machine") LocalRef<MetaMachine> machine) {
        if (original) {
            MEPatternBufferPartMachine buffer = (MEPatternBufferPartMachine) (machine.get());
            return proxySlotRecipeHandler.getProxySlotHandlers().size() == buffer.getInternalInventory().length;
        }
        return false;
    }
}
