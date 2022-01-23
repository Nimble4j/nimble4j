package org.sf.n4j.impl.jdbc.jdbi;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.jdbi.v3.core.Jdbi;
import org.sf.n4j.intf.base.ConfigProvider;
import org.sf.n4j.intf.base.annotations.Config;

import javax.inject.Named;
import javax.inject.Singleton;
import java.util.Properties;

public class JdbiModule extends AbstractModule {

    public static final String CONFIG_KEY = "jdbc.datasource.default";

    @Provides
    @Singleton
    @Named(CONFIG_KEY)
    public ConfigProvider getHikariCPConfigProvider(@Config ConfigProvider appConfigProvider){
        return appConfigProvider.getProviderForConfigPath(CONFIG_KEY);
    }

    @Provides
    @Singleton
    @Named(CONFIG_KEY)
    public HikariDataSource getHikariCPDatasource(@Named(CONFIG_KEY) ConfigProvider hikariDefaultDSConfigProvider){
        Properties hikariCPProperties = hikariDefaultDSConfigProvider.bindTo(Properties.class);
        HikariConfig hikariConfig = new HikariConfig(hikariCPProperties);
        return new HikariDataSource(hikariConfig);
    }



    @Provides
    @Singleton
    @Named(CONFIG_KEY)
    public Jdbi getJdbi(@Named(CONFIG_KEY) HikariDataSource dataSource){
        return Jdbi.create(dataSource);
    }

}
