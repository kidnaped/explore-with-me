package ru.practicum.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "hits")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class EndpointHit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String app;
    private String uri;
    private String ip;
    private LocalDateTime time;

    @Override
    public String toString() {
        return "EndpointHit{" +
                "id=" + id +
                ", app='" + app + '\'' +
                ", uri='" + uri + '\'' +
                ", ip='" + ip + '\'' +
                ", time=" + time +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EndpointHit)) return false;
        EndpointHit that = (EndpointHit) o;
        return Objects.equals(getId(), that.getId())
                && Objects.equals(getUri(), that.getUri())
                && Objects.equals(getIp(), that.getIp());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getUri(), getIp());
    }
}
