package ru.practicum;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.model.EndpointHit;
import ru.practicum.model.ViewStats;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsRepository extends JpaRepository<EndpointHit, Long> {
    @Query("select new ru.practicum.model.ViewStats(hit.app, hit.uri, count(hit.ip)) " +
            "from EndpointHit as hit " +
            "where hit.time between ?1 and ?2 " +
            "and (hit.uri in ?3 or ?3 = null) " +
            "group by hit.app, hit.uri " +
            "order by count(hit.ip) desc")
    List<ViewStats> getAllStats(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query("select new ru.practicum.model.ViewStats(hit.app, hit.uri, count(distinct hit.ip)) " +
            "from EndpointHit as hit " +
            "where hit.time between ?1 and ?2 " +
            "and (hit.uri in ?3 or ?3 = null) " +
            "group by hit.app, hit.uri " +
            "order by count(distinct hit.ip) desc")
    List<ViewStats> getStatsUniqueIp(LocalDateTime start, LocalDateTime end, List<String> uris);
}
