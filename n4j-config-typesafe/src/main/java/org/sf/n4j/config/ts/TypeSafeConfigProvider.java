package org.sf.n4j.config.ts;

import com.google.common.collect.ImmutableMap;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigBeanFactory;
import lombok.NonNull;
import org.sf.n4j.intf.base.ConfigProvider;
import org.sf.n4j.intf.base.ConfigTypeBindingException;
import org.sf.n4j.intf.base.InvalidConfigPathException;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class TypeSafeConfigProvider implements ConfigProvider {

    private Config config;

    public TypeSafeConfigProvider(@NonNull Config configObj) {
        this.config = configObj;
    }

    @Override
    public ConfigProvider getProviderForConfigPath(String configPath) {
        if(config.hasPath(configPath)){
            Config requestedConfig = this.config.getConfig(configPath);
            return new TypeSafeConfigProvider(requestedConfig);
        }else{
            String code = "base.cfg.00001";
            Map<String,Object> context = ImmutableMap.of("requested-config-path",configPath);
            throw new InvalidConfigPathException(code,context);
        }
    }

    @Override
    public <T> T bindTo(Class<T> clazz) {
        try {
            T configT = null;
            //properties has special treatment
            if(Properties.class.isAssignableFrom(clazz)){
                configT = (T)_bindToProperties();
            }else if(Map.class.isAssignableFrom(clazz)) {
                //so too does map
                configT = (T)_bindToMap();
            }else {
                configT = ConfigBeanFactory.create(this.config, clazz);
            }
            return configT;
        }catch (Exception e){
            throw new ConfigTypeBindingException("base.cfg.00002",ImmutableMap.of(
                    "requested-class-to-bind",clazz
            ));
        }
    }

    private Object _bindToMap() {
        Map map  = new HashMap();
        config.entrySet().forEach(entry->{
            map.put(entry.getKey(),entry.getValue().unwrapped());
        });
        return map;
    }

    private Object _bindToProperties() {
        Properties properties = new Properties();
        config.entrySet().forEach(entry->{
            properties.put(entry.getKey(),entry.getValue().unwrapped());
        });
        return properties;
    }
}
