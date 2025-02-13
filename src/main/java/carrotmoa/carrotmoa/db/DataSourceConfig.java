package carrotmoa.carrotmoa.db;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import okhttp3.Route;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateProperties;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateSettings;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableTransactionManagement
public class DataSourceConfig {

    private final String MASTER_DATASOURCE = "masterDataSource";
    private final String SLAVE_DATASOURCE = "slaveDataSource";
    private final String ROUTING_DATASOURCE = "routingDataSource";
    private final String LAZY_ROUTING_DATASOURCE = "lazyRoutingDataSource";
    private HibernateProperties hibernateProperties;
    private JpaProperties jpaProperties;

    public DataSourceConfig(JpaProperties jpaProperties) {
        this.jpaProperties = jpaProperties; // application.yml에서 정의한 JPA 설정 읽어옴
        this.hibernateProperties = new HibernateProperties(); // hibernate 설정관리 (hibernate의 ddl-auto, hiberate.dialect..)
        hibernateProperties.setDdlAuto("none"); // 데이터베이스 스키마 변경 안함
    }

    /*
     * yml 파일에 정의한 Master DB 정보를 불러와 DataSource Bean 등록
     */
    @Bean(MASTER_DATASOURCE)
    @ConfigurationProperties(prefix = "spring.datasource.hikari.master")
    public DataSource masterDataSource() {
        return DataSourceBuilder.create()
                .build();
    }

    /*
     * yml 파일에 정의한 Slave DB 정보를 불러와 DataSource Bean 등록
     */
    @Bean(SLAVE_DATASOURCE)
    @ConfigurationProperties(prefix = "spring.datasource.hikari.slave")
    public DataSource slaveDataSource() {
        return DataSourceBuilder.create()
                .build();
    }

    @DependsOn({MASTER_DATASOURCE, SLAVE_DATASOURCE}) // master, slave_datasource 먼저 초기화
    @Bean(ROUTING_DATASOURCE)
    public AbstractRoutingDataSource routingDataSource(
            @Qualifier(MASTER_DATASOURCE) DataSource masterDataSource,
            @Qualifier(SLAVE_DATASOURCE) DataSource slaveDataSource
    ) {
        AbstractRoutingDataSource routingDataSource = new AbstractRoutingDataSource() {
            @Override
            protected Object determineCurrentLookupKey() {
                // 현재 데이터소스를 결정하는 로직에 로그 추가
                RouteDataSource.DataSourceType dataSourceType = RouteDataSourceManager.getCurrentDataSourceName();
                // 데이터소스를 결정할 때마다 로그 출력
                System.out.println("Determining DataSource: " + dataSourceType);  // 로그 출력
                return dataSourceType;
            }
        };
        Map<Object, Object> targetDataSources = new HashMap<>();
        targetDataSources.put(RouteDataSource.DataSourceType.MASTER, masterDataSource);
        targetDataSources.put(RouteDataSource.DataSourceType.SLAVE, slaveDataSource);

        routingDataSource.setTargetDataSources(targetDataSources);
        routingDataSource.setDefaultTargetDataSource(masterDataSource); // 데이터소스 결정할 수 없을 때 기본적으로 사용되는 데이터소스

        return routingDataSource;
    }

    @DependsOn(ROUTING_DATASOURCE)
    @Bean(LAZY_ROUTING_DATASOURCE)
    public LazyConnectionDataSourceProxy lazyRoutingDataSource(
            @Qualifier(ROUTING_DATASOURCE) DataSource routingDataSource
    ) {
        return new LazyConnectionDataSourceProxy(routingDataSource); // 지연 로딩
    }

    @Bean
    public EntityManagerFactoryBuilder entityManagerFactoryBuilder() {
        return new EntityManagerFactoryBuilder(new HibernateJpaVendorAdapter(), new HashMap<>(), null);
    }

    @DependsOn(LAZY_ROUTING_DATASOURCE)
    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
            EntityManagerFactoryBuilder builder,
            @Qualifier(LAZY_ROUTING_DATASOURCE) DataSource lazyRoutingDataSource) {

        var props = hibernateProperties.determineHibernateProperties(jpaProperties.getProperties(),
                new HibernateSettings());

        return builder
                .dataSource(lazyRoutingDataSource)
                .packages("carrotmoa.carrotmoa.entity")
                .properties(props)
                .persistenceUnit("common")
                .build();
    }

    @Bean
    public PlatformTransactionManager transactionManager(
            EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }

    @Bean
    public JPAQueryFactory jpaQueryFactory(EntityManager entityManager) {
        return new JPAQueryFactory(entityManager);
    }

}
