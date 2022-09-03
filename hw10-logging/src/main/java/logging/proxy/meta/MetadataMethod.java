package logging.proxy.meta;

import java.util.*;

public class MetadataMethod {
    private final String methodName;

    private final List<Class<?>> params;

    private MetadataMethod(String methodName, List<Class<?>> params) {
        this.methodName = methodName;
        this.params = params;
    }

    public static MetadataMethod of(String methodName, List<Class<?>> params) {
        return new MetadataMethod(methodName, params);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MetadataMethod that)) return false;

        if (!Objects.equals(methodName, that.methodName)) return false;
        return params.equals(that.params);
    }

    @Override
    public int hashCode() {
        int result = methodName != null ? methodName.hashCode() : 0;
        result = 31 * result + params.hashCode();
        return result;
    }
}
