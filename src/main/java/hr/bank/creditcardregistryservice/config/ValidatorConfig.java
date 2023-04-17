package hr.bank.creditcardregistryservice.config;

import hr.bank.creditcardregistryservice.validator.OibValidator;
import hr.bank.creditcardregistryservice.validator.constraints.RegularPersonalIdentificationNumber;
import jakarta.validation.ConstraintValidator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ValidatorConfig {

    //in case that this will be used in different country, it should be configured differently
    @Bean
    @ConditionalOnProperty(name = "app.validation.personal-identification-number-country", havingValue = "hr")
    public ConstraintValidator<RegularPersonalIdentificationNumber, String> personalIdentificationNumberValidator(){
        return new OibValidator();
    }



}
