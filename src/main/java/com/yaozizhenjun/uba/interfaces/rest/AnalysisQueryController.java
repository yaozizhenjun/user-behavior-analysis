package com.yaozizhenjun.uba.interfaces.rest;

import com.yaozizhenjun.uba.application.service.AnalysisQueryApplicationService;
import com.yaozizhenjun.uba.application.service.AnalysisQueryResult;
import com.yaozizhenjun.uba.domain.acceleration.model.AccelerationContext;
import com.yaozizhenjun.uba.domain.acceleration.model.EngineCapability;
import com.yaozizhenjun.uba.domain.acceleration.model.EngineType;
import com.yaozizhenjun.uba.domain.acceleration.model.FreshnessRequirement;
import com.yaozizhenjun.uba.domain.analysis.model.AnalysisConfig;
import com.yaozizhenjun.uba.domain.analysis.model.DimensionSpec;
import com.yaozizhenjun.uba.domain.analysis.model.FilterCondition;
import com.yaozizhenjun.uba.domain.analysis.model.FilterTree;
import com.yaozizhenjun.uba.domain.analysis.model.MetricAggregation;
import com.yaozizhenjun.uba.domain.analysis.model.MetricSpec;
import com.yaozizhenjun.uba.domain.analysis.model.ResourceRef;
import com.yaozizhenjun.uba.domain.analysis.model.TimeGranularity;
import com.yaozizhenjun.uba.domain.analysis.model.TimeRange;
import com.yaozizhenjun.uba.interfaces.dto.AnalysisQueryRequest;
import com.yaozizhenjun.uba.interfaces.dto.AnalysisQueryResponse;
import com.yaozizhenjun.uba.interfaces.dto.DimensionRequest;
import com.yaozizhenjun.uba.interfaces.dto.FilterRequest;
import com.yaozizhenjun.uba.interfaces.dto.MetricRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/analysis")
public class AnalysisQueryController {

    private final AnalysisQueryApplicationService analysisQueryApplicationService;

    public AnalysisQueryController(AnalysisQueryApplicationService analysisQueryApplicationService) {
        this.analysisQueryApplicationService = analysisQueryApplicationService;
    }

    @PostMapping("/query")
    public AnalysisQueryResponse query(@Valid @RequestBody AnalysisQueryRequest request) {
        AnalysisQueryResult result = analysisQueryApplicationService.query(toConfig(request), toContext(request));
        AnalysisQueryResponse response = new AnalysisQueryResponse();
        response.setSql(result.getSql());
        response.setCacheKey(result.getCacheKey());
        response.setAccelerationStrategies(result.getAccelerationStrategies());
        response.setRows(result.getRows());
        return response;
    }

    private AnalysisConfig toConfig(AnalysisQueryRequest request) {
        return new AnalysisConfig(
                new ResourceRef(
                        request.getResourceId(),
                        defaultString(request.getResourceType(), "EVENT"),
                        defaultString(request.getPhysicalTable(), "event_table")
                ),
                new TimeRange(LocalDate.parse(request.getStartDate()), LocalDate.parse(request.getEndDate())),
                parseTimeGranularity(request.getTimeGranularity()),
                toDimensions(request.getDimensions()),
                toMetrics(request.getMetrics()),
                new FilterTree("AND", toFilters(request.getFilters()))
        );
    }

    private AccelerationContext toContext(AnalysisQueryRequest request) {
        return new AccelerationContext(
                EngineType.STARROCKS,
                defaultString(request.getTenantId(), "default"),
                defaultString(request.getUserId(), "anonymous"),
                request.isPreRun(),
                EngineCapability.starRocksDefault(),
                FreshnessRequirement.realtime()
        );
    }

    private List<DimensionSpec> toDimensions(List<DimensionRequest> requests) {
        if (requests == null) {
            return Collections.emptyList();
        }
        List<DimensionSpec> dimensions = new ArrayList<DimensionSpec>();
        for (DimensionRequest request : requests) {
            dimensions.add(new DimensionSpec(request.getField(), request.getAlias()));
        }
        return dimensions;
    }

    private List<MetricSpec> toMetrics(List<MetricRequest> requests) {
        if (requests == null) {
            return Collections.emptyList();
        }
        List<MetricSpec> metrics = new ArrayList<MetricSpec>();
        for (MetricRequest request : requests) {
            metrics.add(new MetricSpec(
                    request.getField(),
                    request.getAlias(),
                    MetricAggregation.valueOf(defaultString(request.getAggregation(), "COUNT").toUpperCase()),
                    request.getRollingWindowDays()
            ));
        }
        return metrics;
    }

    private List<FilterCondition> toFilters(List<FilterRequest> requests) {
        if (requests == null) {
            return Collections.emptyList();
        }
        List<FilterCondition> filters = new ArrayList<FilterCondition>();
        for (FilterRequest request : requests) {
            filters.add(new FilterCondition(request.getField(), request.getOperator(), request.getValue()));
        }
        return filters;
    }

    private TimeGranularity parseTimeGranularity(String value) {
        return TimeGranularity.valueOf(defaultString(value, "DAY").toUpperCase());
    }

    private String defaultString(String value, String defaultValue) {
        return value == null || value.trim().length() == 0 ? defaultValue : value;
    }
}
