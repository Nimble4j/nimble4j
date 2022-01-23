package org.sf.n4j.intf.base;

public interface ConfigProvider {

    ConfigProvider getProviderForConfigPath(String configPath);

    <T> T bindTo(Class<T> clazz);

}
