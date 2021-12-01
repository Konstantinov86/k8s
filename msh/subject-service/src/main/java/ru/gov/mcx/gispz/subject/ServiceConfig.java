package ru.gov.mcx.gispz.subject;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jdbc.repository.config.AbstractJdbcConfiguration;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import ru.gov.mcx.gispz.fias.FiasProvider;

import javax.persistence.EntityManagerFactory;


@Configuration
@EnableTransactionManagement
@ComponentScan({
    // утилиты для работы с json
    "ru.gov.mcx.gispz.commons.json",
    // контрагенты организации (адресная книга)
    "ru.gov.mcx.gispz.directory",
    // работа с контрагентами
    "ru.gov.mcx.gispz.subject.service",

})
@EntityScan(basePackages = {
    "ru.gov.mcx.gispz.model"
})
public class ServiceConfig extends AbstractJdbcConfiguration {
    @Bean
    public SubjectProvider subjectProvider() {
        return new SubjectProvider();
    }


    @Bean
    public FiasProvider fiasProvider() {
        return new FiasProvider();
    }


    @Bean
    public DivisionProvider divisionProvider() {
        return new DivisionProvider();
    }

    @Bean
    public UserSubjectProvider userSubjectProvider() {
        return new UserSubjectProvider();
    }


    @Bean
    public SubjectDivisionStaffProvider subjectDivisionStaffProvider() {
        return new SubjectDivisionStaffProvider();
    }


    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        JpaTransactionManager txManager = new JpaTransactionManager();
        txManager.setEntityManagerFactory(entityManagerFactory);
        return txManager;
    }
}
