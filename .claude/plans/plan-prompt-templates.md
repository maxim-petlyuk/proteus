### Template #1 — Phase Implementation

Let's implement Phase %PHASE_NUMBER% from the --plan @.claude/plans/release-notes-generator.md

Before making any changes:

1. List all files that will be created or modified
2. Show me the complete content of each file you plan to create
3. For existing files, show the exact diff of changes

Wait for my explicit approval before creating or modifying any files.

### Template #2 — Progress Tracking & Transition

Review Phase %COMPLETED_PHASE% implementation against --plan @.claude/plans/release-notes-generator.md:

1. List all checkpoint items for Phase %COMPLETED_PHASE%
2. Verify each item is complete by checking the actual files in the project
3. Report any incomplete items or discrepancies

If all checkpoints pass, summarize what we accomplished and let's move to Phase %NEXT_PHASE%.

### Template #3 — Progress Tracking without check

Update the progress tracking section in @.claude/plans/release-notes-generator.md
to mark Phase %s as complete

### Template #4 — Resume Session (Start of Each Day)
Resume Proteus release notes implementation:

1. Read --plan @.claude/plans/release-notes-generator.md to refresh context
2. Check which files from the plan exist in the project
3. Determine current phase and progress
4. List next steps to continue

Provide a brief summary of where we left off.