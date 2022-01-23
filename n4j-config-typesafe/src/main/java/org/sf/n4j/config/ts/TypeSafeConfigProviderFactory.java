package org.sf.n4j.config.ts;

import com.typesafe.config.ConfigFactory;
import org.sf.n4j.intf.base.ConfigProvider;

public class TypeSafeConfigProviderFactory {
    public static ConfigProvider load() {
        return new TypeSafeConfigProvider(ConfigFactory.load());
    }
}
