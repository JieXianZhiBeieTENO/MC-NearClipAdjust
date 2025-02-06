package nearclipadjust.core;

// import net.minecraft.launchwrapper.IClassTransformer;
// import net.minecraft.launchwrapper.LaunchClassLoader;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
// import org.objectweb.asm.ClassReader;
// import org.objectweb.asm.ClassWriter;
// import org.objectweb.asm.Opcodes;
// import org.objectweb.asm.tree.*;

// import javax.annotation.Nullable;
import java.util.Map;

@IFMLLoadingPlugin.Name("Core_Plugin")
@IFMLLoadingPlugin.MCVersion("1.12.2")
@IFMLLoadingPlugin.TransformerExclusions({"nearclipadjust.core"})
public class Core_Plugin implements IFMLLoadingPlugin {

    @Override
    public String[] getASMTransformerClass() {
        return new String[]{ClippingHelperTransformer.class.getName()};
        //return null;
    }

    @Override
    public String getModContainerClass() {
        return null;
    }

    @Override
    public String getSetupClass() {
        return null;
    }

    @Override
    public void injectData(Map<String, Object> data) {
    }

    @Override
    public String getAccessTransformerClass() {
        return null;
    }
}
