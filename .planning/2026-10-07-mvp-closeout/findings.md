# Findings & Decisions

## Requirements
-

## Research Findings
- Spring loads root `.env` from `backend`; provider is configured as real. Values were not displayed.
- `POST /api/matches` returns `MatchReport`; `PreparationPlanTool` currently declares a side-effecting `save` method.
- The newer closeout plan explicitly adds a side-effect-free preview to this MVP.
- Baseline Maven tests, frontend build, and `git diff --check` passed.
- Original 17-second model read timeout produced 200/504/504; the 25-second default produced three HTTP 200 results. Three samples do not establish long-term reliability.
- Real tool calling returned five preview tasks with no IDs; Fake returned one preview task. Browser checks covered Real/Fake, 390px, 5,000-character input, and a controlled 502 retry.
- A retrieval/no-retrieval concept exercise answered the current 25-second timeout only when supplied the current source line; this is not product RAG.

## Technical Decisions
| Decision | Rationale |
|----------|-----------|

## Issues Encountered
| Issue | Resolution |
|-------|------------|

## Resources
-
