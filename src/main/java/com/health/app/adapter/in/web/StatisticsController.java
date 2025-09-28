package com.health.app.adapter.in.web;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.health.app.adapter.in.web.dto.DailyStatisticsResponse;
import com.health.app.adapter.in.web.dto.MonthlyStatisticsResponse;
import com.health.app.application.port.in.GetDailyStatisticsQuery;
import com.health.app.application.port.in.GetDailyStatisticsUseCase;
import com.health.app.application.port.in.GetMonthlyStatisticsQuery;
import com.health.app.application.port.in.GetMonthlyStatisticsUseCase;
import com.health.app.domain.health.DailyStatistics;
import com.health.app.domain.health.MonthlyStatistics;

@RestController
@RequestMapping("/api/v1/statistics")
public class StatisticsController {

    private final GetDailyStatisticsUseCase getDailyStatisticsUseCase;
    private final GetMonthlyStatisticsUseCase getMonthlyStatisticsUseCase;

    public StatisticsController(
            GetDailyStatisticsUseCase getDailyStatisticsUseCase,
            GetMonthlyStatisticsUseCase getMonthlyStatisticsUseCase) {
        this.getDailyStatisticsUseCase = getDailyStatisticsUseCase;
        this.getMonthlyStatisticsUseCase = getMonthlyStatisticsUseCase;
    }

    @GetMapping("/daily")
    public ResponseEntity<DailyStatisticsResponse> getDailyStatistics(
            @RequestParam String recordKey,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        GetDailyStatisticsQuery query = new GetDailyStatisticsQuery(recordKey, date);
        Optional<DailyStatistics> dailyStatistics =
                getDailyStatisticsUseCase.getDailyStatistics(query);

        return dailyStatistics
                .map(stats -> ResponseEntity.ok(DailyStatisticsResponse.from(stats)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/monthly")
    public ResponseEntity<MonthlyStatisticsResponse> getMonthlyStatistics(
            @RequestParam String recordKey, @RequestParam int year, @RequestParam int month) {

        GetMonthlyStatisticsQuery query = new GetMonthlyStatisticsQuery(recordKey, year, month);
        Optional<MonthlyStatistics> monthlyStatistics =
                getMonthlyStatisticsUseCase.getMonthlyStatistics(query);

        return monthlyStatistics
                .map(stats -> ResponseEntity.ok(MonthlyStatisticsResponse.from(stats)))
                .orElse(ResponseEntity.notFound().build());
    }
}
