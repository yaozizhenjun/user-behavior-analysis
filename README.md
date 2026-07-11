# user-behavior-analysis

用户行为分析系统代码框架，基于 Java 8、Spring Boot 2.7 和 Maven 3 构建。

## 设计目标

- 支持事件分析、漏斗分析、留存分析等行为分析工具扩展。
- 在 SQL 翻译与物理查询执行之间增加逻辑加速层。
- 先构建通用 `LogicalQueryPlan` 和 `AccelerationPlan`，再由 StarRocks 等物理引擎执行。
- 为前缀索引、物化视图、滚动窗口指标、结果缓存等优化能力预留统一扩展点。

## 分层结构

```text
interfaces      REST 接口与入参出参 DTO
application     应用服务、用例编排、端口接口
domain          分析配置、逻辑查询计划、加速计划等领域模型
infrastructure  SQL 渲染、物理引擎执行、缓存 Key 等基础设施
shared          通用异常和共享对象
```

## 本地构建

```bash
mvn test
```

`pom.xml` 通过 `maven-enforcer-plugin` 约束 Maven 版本为 `3.6.3 <= Maven < 4.0.0`，Java 版本为 JDK 8。

## 示例接口

```http
POST /api/analysis/query
```

请求体示例：

```json
{
  "resourceId": "event_payment",
  "startDate": "2026-07-01",
  "endDate": "2026-07-31",
  "timeGranularity": "DAY",
  "dimensions": [
    { "field": "city", "alias": "city" }
  ],
  "metrics": [
    {
      "field": "user_id",
      "alias": "user_cnt_last_7d",
      "aggregation": "COUNT_DISTINCT",
      "rollingWindowDays": 7
    }
  ],
  "filters": [
    { "field": "event_name", "operator": "EQ", "value": "payment" }
  ]
}
```
