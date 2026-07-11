package com.yaozizhenjun.uba.infrastructure.query.sql;

import com.yaozizhenjun.uba.domain.acceleration.model.AccelerationPlan;
import com.yaozizhenjun.uba.domain.acceleration.model.EngineType;
import com.yaozizhenjun.uba.domain.analysis.model.MetricAggregation;
import com.yaozizhenjun.uba.domain.analysis.model.ResourceRef;
import com.yaozizhenjun.uba.domain.analysis.model.TimeGranularity;
import com.yaozizhenjun.uba.domain.query.model.LogicalQueryPlan;
import com.yaozizhenjun.uba.domain.query.model.QueryDimension;
import com.yaozizhenjun.uba.domain.query.model.QueryMeasure;
import com.yaozizhenjun.uba.domain.query.model.QuerySignature;
import com.yaozizhenjun.uba.domain.query.model.QueryTimeWindow;
import com.yaozizhenjun.uba.domain.query.model.SqlPlan;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

class StarRocksSqlRendererTest {

    @Test
    void renderRollingDistinctUserSqlWithDateSeries() {
        LogicalQueryPlan plan = new LogicalQueryPlan(
                new ResourceRef("event_payment", "EVENT", "event_table"),
                new QueryTimeWindow(LocalDate.parse("2026-07-01"), LocalDate.parse("2026-07-03"), 7),
                TimeGranularity.DAY,
                Collections.singletonList(new QueryDimension("city", "city")),
                Collections.singletonList(new QueryMeasure("user_id", "user_cnt_last_7d", MetricAggregation.COUNT_DISTINCT, 7)),
                Collections.emptyList(),
                new QuerySignature("test")
        );
        AccelerationPlan accelerationPlan = new AccelerationPlan(
                plan,
                plan,
                EngineType.STARROCKS,
                Collections.emptyList(),
                new QuerySignature("test|acc")
        );

        SqlPlan sqlPlan = new StarRocksSqlRenderer().render(plan, accelerationPlan);

        assertThat(sqlPlan.getSql()).contains("DATE '2026-06-25' AS window_start");
        assertThat(sqlPlan.getSql()).contains("dt >= DATE '2026-06-25'");
        assertThat(sqlPlan.getSql()).contains("e.dt >= d.window_start");
        assertThat(sqlPlan.getSql()).contains("COUNT(DISTINCT e.user_id) AS user_cnt_last_7d");
        assertThat(sqlPlan.getSql()).contains("GROUP BY\n  d.stat_date,\n  e.city");
    }

    @Test
    void supportsStarRocksOnly() {
        StarRocksSqlRenderer renderer = new StarRocksSqlRenderer();

        assertThat(renderer.supports(EngineType.STARROCKS)).isTrue();
        assertThat(renderer.supports(EngineType.CLICKHOUSE)).isFalse();
    }
}
