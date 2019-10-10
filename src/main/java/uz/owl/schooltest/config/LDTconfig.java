package uz.owl.schooltest.config;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Configuration
public class LDTconfig {


    @Bean
    public LocalDateTimeDeserializer getLocalDateTimeDeserializer() {
        JavaTimeModule module = new JavaTimeModule();
        LocalDateTimeDeserializer localDateTimeDeserializer = new LocalDateTimeDeserializer(getFormatter());
        module.addDeserializer(LocalDateTime.class, localDateTimeDeserializer);
        return localDateTimeDeserializer;
    }

    @Bean
    public DateTimeFormatter getFormatter() {
        return DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    }

}
