package ru.practicum;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.model.EndpointHit;
import ru.practicum.model.ViewStats;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsRepository extends JpaRepository<EndpointHit, Long> {
    @Query("select new ru.practicum.model.ViewStats(hit.app, hit.uri, count(hit.ip)) " +
            "from EndpointHit as hit " +
            "where hit.time between cast(:start as date) and cast(:end as date) " +
            "and (hit.uri in :uris or :uris = null) " +
            "group by hit.app, hit.uri " +
            "order by count(hit.ip) desc")
    List<ViewStats> getAllStats(@Param("start")LocalDateTime start,
                                @Param("end")LocalDateTime end,
                                @Param("uris") List<String> uris);

    @Query("select new ru.practicum.model.ViewStats(hit.app, hit.uri, count(distinct hit.ip)) " +
            "from EndpointHit as hit " +
            "where hit.time between cast(:start as date) and cast(:end as date) " +
            "and (hit.uri in :uris or :uris = null) " +
            "group by hit.app, hit.uri " +
            "order by count(distinct hit.ip) desc")
    List<ViewStats> getStatsUniqueIp(@Param("start")LocalDateTime start,
                                     @Param("end")LocalDateTime end,
                                     @Param("uris") List<String> uris);
}
