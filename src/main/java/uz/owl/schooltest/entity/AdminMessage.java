package uz.owl.schooltest.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String message;
    private String cause;
    private String advice;

    @CreationTimestamp
    private LocalDateTime createdTime;

    @ManyToOne
    @JsonIgnore
    private User user;


    @Override
    public String toString() {
        return "AdminMessage{" +
                "id=" + id +
                ", message='" + message + '\'' +
                ", cause='" + cause + '\'' +
                ", advice='" + advice + '\'' +
                ", createdTime=" + createdTime +
                '}';
    }
}
