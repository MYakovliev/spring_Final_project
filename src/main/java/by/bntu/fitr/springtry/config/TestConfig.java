package by.bntu.fitr.springtry.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.session.config.annotation.web.http.EnableSpringHttpSession;

import javax.sql.DataSource;

@Configuration
@EnableJpaRepositories(basePackages = "by.bntu.fitr.springtry")
@ComponentScan(basePackages = "by.bntu.fitr.springtry")
@Profile("test")
public class TestConfig {
    private static final String PACKAGES_TO_SCAN = "by.bntu.fitr.springtry";

    @Bean
    public DataSource dataSource(){
        return new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .addScript("schema.sql").addScript("init.sql").build();
    }

    @Bean
    public LocalSessionFactoryBean entityManagerFactory(DataSource dataSource){
        LocalSessionFactoryBean factory = new LocalSessionFactoryBean();
        factory.setDataSource(dataSource);
        factory.setPackagesToScan(PACKAGES_TO_SCAN);
        return factory;
    }

}
