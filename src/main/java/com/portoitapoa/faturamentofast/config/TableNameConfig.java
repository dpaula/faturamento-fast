package com.portoitapoa.faturamentofast.config;

import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TableNameConfig {

    @Value("${app.tabela-siscomex}")
    private String siscomexTableName;

    @Bean
    public PhysicalNamingStrategyStandardImpl physicalNamingStrategyStandard() {
        return new PhysicalNamingImpl();
    }

    class PhysicalNamingImpl extends PhysicalNamingStrategyStandardImpl {

        private static final long serialVersionUID = -3359028802760007180L;

        @Override
        public Identifier toPhysicalTableName(final Identifier name, final JdbcEnvironment context) {
            if ("Siscomex".equals(name.getText())) {
                return new Identifier(siscomexTableName, name.isQuoted());
            }
            return super.toPhysicalTableName(name, context);
        }
    }
}