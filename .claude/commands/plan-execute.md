# Execute Phase

## Description
Execute a specific phase or sub-task from a migration plan with progress tracking and task management.

## Usage
```
plan-execute <plan> <phase_number>
```

## Arguments
- `plan` (required): Path to the plan markdown file (relative to .claude/plans/)
  - Example: `kmp-migration-plan.md`
  - Example: `github-documentation-plan.md`

- `phase_number` (required): The phase or sub-task to execute
  - Major phase: `1`, `2`, `3`, etc.
  - Sub-task: `1.1`, `1.2`, `2.3`, etc.
  - Example: `1` - Execute entire Phase 1
  - Example: `1.3` - Execute only task 3 of Phase 1

## Examples
```bash
# Execute Phase 1 of KMP migration
plan-execute kmp-migration-plan.md 1

# Execute only sub-task 1.3 (platform storage implementation)
plan-execute kmp-migration-plan.md 1.3

# Execute Phase 7.1 from documentation plan (Medium article)
plan-execute github-documentation-plan.md 7.1

# Execute UI migration phase
plan-execute kmp-migration-plan.md 2
```

## What I'll Do

I will:
1. **Parse the specified plan file** from `.claude/plans/`
2. **Locate the requested phase or sub-task** using the phase number
3. **Execute the tasks** defined in that section
4. **Track progress** with a todo list
5. **Report completion status**

### Dynamic Phase Execution

When you specify a phase (e.g., `1` or `1.3`), I will:
- Read the plan file to find the exact tasks
- For major phase (e.g., `1`): Execute all sub-tasks in that phase
- For sub-task (e.g., `1.3`): Execute only that specific task
- Create appropriate files and code based on the plan
- Update progress tracking

## Progress Tracking

I will track progress **directly in the plan file** by:
- Adding status indicators to each phase/sub-task
- Updating deliverables checkboxes in real-time
- Adding completion dates and notes inline
- Maintaining a todo list during execution

### Status Format in Plan
```markdown
### Phase 1.1: Convert existing modules to KMP ✅ COMPLETED (2025-01-08)
**Status**: ✅ Completed | 🔄 In Progress | ⏳ Pending | 🚫 Blocked

#### Tasks:
- [x] Update `proteus-core/build.gradle.kts` for multiplatform ✅
- [x] Create source sets (commonMain, androidMain, iosMain) ✅
- [x] Move existing code from `src/main/java` to `src/androidMain/kotlin` ✅

#### Progress Notes:
- **Completed**: 2025-01-08
- **Key changes**: Converted build system, created KMP structure
- **Verification**: Both Android and iOS compilation successful
```

## Documentation Updates

Progress will be tracked in:
1. **Plan file itself** - Real-time status updates
2. **Summary docs** - Final phase reports in `docs/` for reference
3. **Todo lists** - Temporary progress tracking during execution

## Success Criteria

For each phase, I'll verify:
- [ ] All code compiles for both platforms
- [ ] Tests pass (>80% coverage)
- [ ] Documentation is updated
- [ ] No regression in existing functionality
- [ ] Performance benchmarks met

## Plan File Format

The plan file should be a markdown file in `.claude/plans/` with:
- Phases marked as `## Phase N:` or `### N.N`
- Tasks listed under each phase
- Clear deliverables and timelines

## Notes
- Phases can be executed in order or independently
- Sub-tasks allow granular execution (e.g., `1.3` for just storage implementation)
- Each phase builds on previous work
- I'll check prerequisites before starting
- Progress is saved between sessions
- Plan file must exist in `.claude/plans/` directory

## Related Commands
- `plan-status <plan>` - Check plan progress
- `plan-rollback <plan> <phase>` - Rollback a phase
- `plan-test <plan> <platform>` - Run platform tests