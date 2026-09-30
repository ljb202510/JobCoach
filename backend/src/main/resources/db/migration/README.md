# Database migration draft

`schema.sql` is a reviewable MySQL 8 starting point for the fake-to-persistence transition. It is intentionally not wired into application startup and does not require MySQL for the current demo.

Tables cover the P0 audit path: input/session, structured report, preparation plan/tasks, and execution records. The report is stored as JSON until the response contract stabilizes; searchable fields can be normalized later.

Before applying it, confirm the retention policy in `docs/engineering/data-retention-and-privacy.md`, choose Flyway/Liquibase, add migration versioning, and test foreign-key deletion behavior.
