package uz.owl.schooltest.config.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SecurityDto {
    private String name;
    private String token;

    @Override
    public String toString() {
        return "SecurityDto{" +
                "name='" + name + '\'' +
                ", token='" + token + '\'' +
                '}';
    }

    public String json(){
        return "{" +
                "\"name\":\"" + name + '\"' +
                ",\"token\":\"" + token + '\"' +
                '}';
    }
}
