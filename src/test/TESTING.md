# Test Coverage

- Tests: 131 run, 0 failures, 0 errors, 0 skipped
- Overall line coverage: 3,011 / 3,729 (80.75%)
- Overall branch coverage: 765 / 982 (77.90%)
- Use-case interactor line coverage: 497 / 508 (97.83%)
- Use-case interactor branch coverage: 188 / 212 (88.68%)
- Entire use-case package line coverage: 732 / 750 (97.60%)
- Entire use-case package branch coverage: 192 / 218 (88.07%)

The figures above were generated on August 10, 2026 from one run of the
complete Maven test suite after the Cito API implementation was merged. Run:

```shell
mvn org.jacoco:jacoco-maven-plugin:0.8.13:prepare-agent \
    test \
    org.jacoco:jacoco-maven-plugin:0.8.13:report
```

The browsable report is generated at `target/site/jacoco/index.html`.

## Cito API test strategy

The tests do not require a real API key or make calls to the live Cito
service. Test-only OkHttp interceptors provide deterministic responses for:

- successful requests, request headers, endpoint normalization, and caching;
- stale-cache recovery after HTTP and connection failures;
- invalid JSON, missing configuration, and non-success status codes;
- fighter directory mapping, pagination, deduplication, and era fallbacks;
- profile/stat hydration and US1 to US2 selected-fighter handoff;
- complete rankings, malformed rows, incomplete rankings, and offline fallback.

This gives the newly implemented Cito mapper 325 / 339 covered lines and the
Cito UFC data-access adapter 180 / 202 covered lines without depending on an
external system.

## Deliberate unit-test boundaries

- The live Cito service itself is outside the repository. Its availability,
  production data quality, authentication infrastructure, and network latency
  are integration concerns. The application's request and response behavior
  is tested deterministically at the HTTP boundary instead.
- `Main` and composition factories primarily construct and connect objects.
  Their meaningful downstream behavior is covered through interactors,
  presenters, adapters, and view models; launching the complete desktop app is
  better suited to a manual smoke test.
- Swing pixel layout, native file chooser behavior, and operating-system window
  management are not stable unit-test targets. View actions and state changes
  with application logic are covered, while purely visual placement remains a
  manual UI check.
- Defensive branches for unavailable JVM primitives (for example, SHA-256 not
  existing in a conforming Java runtime) are not forced through reflection or
  JVM modification because doing so would test the platform rather than the
  project behavior.

These boundaries do not replace the rubric threshold: the measured overall
line coverage remains above 70% and interactor line coverage remains above 90%.
