# UI Polish Implementation Plan for Proteus

## 📖 How to Use This Plan

This is a structured plan for implementing UI polish and effects in the Proteus library.
Follow this workflow:

1. **Start Analysis Phase**: `claude --plan @.claude/plans/ui-polish-implementation.md`
2. **Review Proposal**: Claude will analyze and propose detailed steps
3. **Adjust if Needed**: Request refinements before implementation
4. **Execute by Phase**: Implement incrementally with validation checkpoints
5. **Update This File**: Track progress and learnings

---

## 🎯 PROJECT CONTEXT

### About Proteus
- **Type**: Android library for A/B remote configuration
- **Module**: proteus-ui (Jetpack Compose + Material Design 3)
- **Theme**: Custom beige color palette
- **Target**: Professional, polished developer tool

### Current State
- Basic functional UI exists
- Feature list screen with search
- Feature configurator screen with save/reset buttons
- Search highlighting feature recently added
- No micro-interactions or polish effects yet

### Design Philosophy
- Material Design 3 compliance
- Subtle, professional animations
- Performance-first (smooth 60fps)
- Accessibility-friendly
- Enhances without overwhelming

---

## 🎨 FEATURES TO IMPLEMENT

### Priority 1: Core Tactile Feel (HIGH IMPACT)
**Goal**: Make the UI feel responsive and alive

#### 1.1 Haptic Feedback
**Target Components**:
- Feature card tap (opening configurator)
- Long press on feature key (copy to clipboard)
- Save button success
- Reset button confirmation
- Validation errors

**Technical Specs**:
```kotlin
HapticFeedbackType.Click      → Card tap, navigation
HapticFeedbackType.LongPress  → Copy feature key
HapticFeedbackType.Confirm    → Save/reset success
HapticFeedbackType.Reject     → Validation errors
```

**Requirements**:
- Use `LocalView.current.performHapticFeedback()`
- Test on physical device (emulator doesn't support haptics)
- Document in component KDoc

**Validation**:
- [ ] Haptics fire at correct moments
- [ ] No duplicate/multiple haptic triggers
- [ ] Works consistently across Android versions
- [ ] Feels natural, not annoying

---

#### 1.2 Card Press Animation
**Target Components**:
- FeatureCard in list screen
- Any Card component with onClick

**Technical Specs**:
```kotlin
val scale by animateFloatAsState(
    targetValue = if (pressed) 0.97f else 1f,
    animationSpec = spring(
        dampingRatio = Spring.DampingRatioMediumBouncy,
        stiffness = Spring.StiffnessLow
    )
)
```

**Implementation Approach**:
- Use `InteractionSource` to track pressed state
- Apply `graphicsLayer { scaleX/scaleY }` for performance
- Combine with haptic feedback for maximum impact
- Ensure press state clears on drag/cancel

**Requirements**:
- Smooth 60fps animation
- No visual jank on press/release
- Works with accessibility services
- Doesn't interfere with ripple effect

**Validation**:
- [ ] Animation triggers immediately on touch
- [ ] Returns to normal smoothly on release
- [ ] No lingering scale if user drags away
- [ ] Performs well in LazyColumn with 50+ items

---

#### 1.3 Ripple Effects
**Target Components**:
- All clickable Cards
- All Buttons
- Interactive list items

**Technical Specs**:
```kotlin
indication = rememberRipple(
    bounded = true,
    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)
)
```

**Theme Integration**:
- Light mode: Primary color (#D4B896) at 12% alpha
- Dark mode: Primary color at 15% alpha (higher contrast)
- Bounded ripples for Cards (contained)
- Unbounded for IconButtons

**Requirements**:
- Use theme colors, never hardcoded
- Ensure visibility in both light/dark themes
- Match Material Design motion specs
- Work with our beige color palette

**Validation**:
- [ ] Ripples visible but not overwhelming
- [ ] Colors match our beige theme
- [ ] Works in both light and dark modes
- [ ] Ripple doesn't obscure content

---

#### 1.4 Screen Transitions
**Target Navigation**:
- Feature List → Feature Configurator
- Any screen → Settings (if exists)
- Dialog appearances

**Technical Specs**:
```kotlin
enterTransition = slideIntoContainer(
    towards = AnimatedContentTransitionScope.SlideDirection.Start,
    animationSpec = tween(300, easing = EaseInOut)
) + fadeIn(tween(200))

exitTransition = slideOutOfContainer(
    towards = AnimatedContentTransitionScope.SlideDirection.End,
    animationSpec = tween(300, easing = EaseInOut)
) + fadeOut(tween(200))
```

**Requirements**:
- Consistent transition duration (300ms)
- Proper direction (left-to-right for forward, right-to-left for back)
- Fade overlay for depth perception
- Respect system animation settings

**Validation**:
- [ ] Smooth transitions without jank
- [ ] Correct animation direction
- [ ] Back navigation reverses animation
- [ ] Respects accessibility animation preferences

---

### Priority 2: Visual Polish (MEDIUM IMPACT)
**Goal**: Provide clear visual feedback for state changes

#### 2.1 Highlight Fade-In Animation
**Context**: For the search highlighting feature recently added

**Technical Specs**:
```kotlin
val highlightAlpha by animateFloatAsState(
    targetValue = if (shouldHighlight) 1f else 0f,
    animationSpec = tween(
        durationMillis = 200,
        easing = FastOutSlowInEasing
    )
)

// Apply to SpanStyle
SpanStyle(
    background = highlightColor.copy(alpha = highlightAlpha)
)
```

**Requirements**:
- Fade in smoothly when query reaches 3+ characters
- Fade out smoothly when query drops below 3 characters
- No jarring flashes
- Doesn't trigger on every keystroke (debounced)

**Validation**:
- [ ] Smooth fade in/out
- [ ] No flashing when typing quickly
- [ ] Works with both light and dark themes
- [ ] Performance acceptable with 50+ items

---

#### 2.2 Override Badge with Reveal Animation
**Target Components**:
- FeatureCard (when feature has local override)

**Technical Specs**:
```kotlin
AnimatedVisibility(
    visible = feature.isOverridden,
    enter = scaleIn(
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy
        )
    ) + fadeIn(),
    exit = scaleOut() + fadeOut()
) {
    Badge(
        containerColor = MaterialTheme.colorScheme.tertiary,
        contentColor = MaterialTheme.colorScheme.onTertiary
    ) {
        Icon(
            imageVector = Icons.Default.Edit,
            contentDescription = "Overridden",
            modifier = Modifier.size(12.dp)
        )
    }
}
```

**Requirements**:
- Badge "pops" into existence when override is saved
- Badge "pops" out when override is reset
- Clear visual indicator of override status
- Doesn't obscure feature name

**Validation**:
- [ ] Badge appears smoothly after save
- [ ] Badge disappears smoothly after reset
- [ ] Visible in both themes
- [ ] Icon size appropriate (not too large/small)

---

#### 2.3 Value Type Icons with Transitions
**Target Components**:
- Feature configurator screen (value type selector)

**Technical Specs**:
```kotlin
AnimatedContent(
    targetState = currentValueType,
    transitionSpec = {
        fadeIn(tween(150)) + scaleIn(tween(150)) with
        fadeOut(tween(150)) + scaleOut(tween(150))
    }
) { valueType ->
    Icon(
        imageVector = when (valueType) {
            ValueType.BOOLEAN -> Icons.Default.ToggleOn
            ValueType.STRING -> Icons.Default.TextFields
            ValueType.NUMBER -> Icons.Default.Numbers
            ValueType.JSON -> Icons.Default.DataObject
        },
        contentDescription = valueType.name,
        tint = MaterialTheme.colorScheme.primary
    )
}
```

**Requirements**:
- Icons crossfade smoothly when type changes
- Each type has distinctive icon
- Icons follow theme colors
- Clear visual distinction between types

**Validation**:
- [ ] Icons change smoothly (no flicker)
- [ ] All value types have unique icons
- [ ] Icons visible in both themes
- [ ] Animation doesn't slow down UI

---

#### 2.4 Active State Border
**Target Components**:
- Currently selected/edited feature in list

**Technical Specs**:
```kotlin
Card(
    border = if (isActive) BorderStroke(
        width = 2.dp,
        brush = Brush.horizontalGradient(
            colors = listOf(
                MaterialTheme.colorScheme.primary,
                MaterialTheme.colorScheme.secondary
            )
        )
    ) else null,
    modifier = Modifier.animateContentSize()
)
```

**Requirements**:
- Gradient border using our beige primary/secondary colors
- Appears when feature is selected for editing
- Animates smoothly when appearing/disappearing
- Doesn't shift layout (account for border width)

**Validation**:
- [ ] Border appears smoothly
- [ ] Gradient visible in both themes
- [ ] No layout shift when border appears
- [ ] Only one card has border at a time

---

### Priority 3: Functional Enhancements (NICE-TO-HAVE)
**Goal**: Add utility while maintaining polish

#### 3.1 Pull-to-Refresh
**Target Components**:
- Feature list screen

**Technical Specs**:
```kotlin
val pullRefreshState = rememberPullRefreshState(
    refreshing = isRefreshing,
    onRefresh = { viewModel.syncWithRemote() }
)

Box(modifier = Modifier.pullRefresh(pullRefreshState)) {
    LazyColumn { /* features */ }
    
    PullRefreshIndicator(
        refreshing = isRefreshing,
        state = pullRefreshState,
        modifier = Modifier.align(Alignment.TopCenter),
        backgroundColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.primary
    )
}
```

**Requirements**:
- Natural pull-down gesture to sync
- Shows loading indicator during sync
- Haptic feedback on refresh trigger
- Works with existing list scroll

**Validation**:
- [ ] Pull gesture feels natural
- [ ] Loading indicator matches theme
- [ ] Doesn't interfere with scroll
- [ ] Shows sync status after completion

---

#### 3.2 Copy to Clipboard (Long Press)
**Target Components**:
- Feature key text in card
- Feature key in configurator

**Technical Specs**:
```kotlin
val clipboardManager = LocalClipboardManager.current
val context = LocalContext.current
val haptics = LocalView.current

Text(
    text = feature.key,
    modifier = Modifier.combinedClickable(
        onClick = { /* navigate to details */ },
        onLongClick = {
            clipboardManager.setText(AnnotatedString(feature.key))
            haptics.performHapticFeedback(HapticFeedbackType.LongPress)
            Toast.makeText(
                context,
                "Copied: ${feature.key}",
                Toast.LENGTH_SHORT
            ).show()
        }
    )
)
```

**Requirements**:
- Long press to copy feature key
- Toast notification confirms copy
- Haptic feedback on long press
- Doesn't interfere with card tap navigation

**Validation**:
- [ ] Long press copies to clipboard
- [ ] Toast appears with confirmation
- [ ] Haptic feedback fires
- [ ] Normal tap still navigates

---

#### 3.3 Loading Shimmer Effect
**Target Components**:
- Feature list during initial load
- Any loading state

**Technical Specs**:
```kotlin
@Composable
fun Modifier.shimmerEffect(): Modifier = composed {
    var size by remember { mutableStateOf(IntSize.Zero) }
    val transition = rememberInfiniteTransition(label = "shimmer")
    val startOffsetX by transition.animateFloat(
        initialValue = -2 * size.width.toFloat(),
        targetValue = 2 * size.width.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(1000)
        ),
        label = "shimmer"
    )

    background(
        brush = Brush.linearGradient(
            colors = listOf(
                Color(0xFFE0E0E0),
                Color(0xFFF5F5F5),
                Color(0xFFE0E0E0)
            ),
            start = Offset(startOffsetX, 0f),
            end = Offset(startOffsetX + size.width.toFloat(), size.height.toFloat())
        )
    ).onGloballyPositioned { size = it.size }
}
```

**Requirements**:
- Replaces circular progress indicators
- Matches card shape/layout
- Professional shimmer animation
- Theme-aware colors

**Validation**:
- [ ] Shimmer animates smoothly
- [ ] Matches content layout
- [ ] Works in both themes
- [ ] Replaces loading spinner

---

#### 3.4 Empty State with Personality
**Target Components**:
- Feature list when search has no results
- Feature list when no features exist

**Technical Specs**:
```kotlin
Column(
    modifier = Modifier.fillMaxSize(),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Center
) {
    Icon(
        imageVector = Icons.Default.SearchOff,
        contentDescription = null,
        modifier = Modifier
            .size(120.dp)
            .alpha(0.3f),
        tint = MaterialTheme.colorScheme.onSurface
    )
    
    Spacer(modifier = Modifier.height(16.dp))
    
    Text(
        text = "No features found matching \"$query\"",
        style = MaterialTheme.typography.bodyLarge,
        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
        textAlign = TextAlign.Center
    )
    
    Text(
        text = "Try a different search term",
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
    )
}
```

**Requirements**:
- Friendly, helpful message
- Large icon for visual interest
- Contextual message based on state
- Subtle animation on appearance

**Validation**:
- [ ] Appears when appropriate
- [ ] Message is helpful, not frustrating
- [ ] Icon and text visible in both themes
- [ ] Doesn't flash during loading

---

## 🔍 ANALYSIS PHASE INSTRUCTIONS

When Claude reads this plan in `--plan` mode, it should:

### Step 1: Codebase Exploration
```
Analyze the proteus-ui module:
1. Find all Composables mentioned in this plan
2. Identify current interaction handling patterns
3. Check theme color definitions (Color.kt)
4. Review existing animations (if any)
5. Understand state management approach
6. Check navigation implementation
```

### Step 2: Architecture Assessment
```
Evaluate:
1. Where should animation state live? (ViewModel vs Composable)
2. How to avoid recomposition issues in LazyColumn?
3. Best place to add HapticFeedback dependencies
4. Theme color availability for effects
5. Existing patterns we should follow
```

### Step 3: Dependency Check
```
Verify we have:
- accompanist-systemuicontroller (for status bar)
- compose-material3 (for Material effects)
- androidx.compose.animation (for transitions)
- Any missing dependencies needed
```

### Step 4: Create Detailed Proposal
```
For each Priority 1 feature:
1. Exact files to modify
2. Code structure (where functions/composables go)
3. State management approach
4. Theme integration points
5. Testing strategy
6. Estimated complexity (simple/medium/complex)

Present as structured plan with:
- File tree of changes
- Implementation sequence
- Potential risks/challenges
- Review checkpoints
```

---

## ✅ IMPLEMENTATION PHASE STRUCTURE

After plan approval, implement in this sequence:

### Phase 1: Haptic Feedback
**Implementation Steps**:
1. Add haptic feedback extension functions
2. Update FeatureCard with card tap haptic
3. Add long-press haptic for copy
4. Add success/error haptics to save/reset
5. Test on physical device

**Checkpoint**: Review haptic timing and feel

---

### Phase 2: Card Press Animation
**Implementation Steps**:
1. Add InteractionSource to FeatureCard
2. Implement scale animation with spring
3. Apply graphicsLayer transformation
4. Test in LazyColumn with scrolling
5. Ensure no performance issues

**Checkpoint**: Verify smooth 60fps animation

---

### Phase 3: Ripple Effects
**Implementation Steps**:
1. Add ripple to FeatureCard
2. Configure ripple color from theme
3. Test visibility in light/dark themes
4. Apply to other clickable components
5. Validate with beige color palette

**Checkpoint**: Confirm ripples visible but subtle

---

### Phase 4: Screen Transitions
**Implementation Steps**:
1. Configure NavHost transitions
2. Add enter/exit animations
3. Test forward/back navigation
4. Ensure consistency across screens
5. Test with system animation settings

**Checkpoint**: Smooth navigation flow

---

### Phase 5: Visual Polish (Priority 2)
**Implementation Steps**:
1. Highlight fade-in animation
2. Override badge reveal
3. Value type icon transitions
4. Active state border
5. Preview functions for all states

**Checkpoint**: All visual states polished

---

### Phase 6: Functional Enhancements (Priority 3)
**Implementation Steps**:
1. Pull-to-refresh
2. Copy to clipboard
3. Loading shimmer
4. Empty states
5. Integration testing

**Checkpoint**: Full feature validation

---

## 🧪 VALIDATION REQUIREMENTS

### Performance Checks
```
For each implemented feature:
1. Profile with Compose Layout Inspector
2. Check recomposition counts
3. Verify 60fps in animations
4. Test with 100+ list items
5. Monitor memory usage
```

### Accessibility Checks
```
For each implemented feature:
1. Test with TalkBack enabled
2. Verify with large text sizes
3. Check color contrast (WCAG AA)
4. Test touch target sizes (48dp min)
5. Ensure animations respect system settings
```

### Theme Validation
```
For each implemented feature:
1. Test in light theme
2. Test in dark theme
3. Verify beige color palette integration
4. Check all color values from theme
5. No hardcoded colors
```

### Device Testing
```
Test on:
1. Physical device (for haptics)
2. Different Android versions (API 21+)
3. Different screen sizes
4. Different screen densities
5. Tablet layouts (if applicable)
```

---

## 📊 PROGRESS TRACKING

Update this section as you complete phases:

- [x] **Phase 1: Haptic Feedback** - COMPLETED ✅
    - [x] 1.1: Card tap haptic - Using CONTEXT_CLICK for all interactions
    - [x] 1.2: ~~Long press haptic~~ - Removed (no clipboard feature)
    - [x] 1.3: Success haptics - Save/reset actions with CONFIRM fallback
    - [x] 1.4: Error haptics - Framework ready for validation errors

- [x] **Phase 2: Card Press Animation** - COMPLETED ✅
    - [x] 2.1: InteractionSource setup - Shared interaction source for haptics + animation
    - [x] 2.2: Spring animation - Scale 0.97f with medium bouncy damping
    - [x] 2.3: Performance validation - Hardware accelerated with graphicsLayer

- [x] **Phase 3: Ripple Effects** - COMPLETED ✅
    - [x] 3.1: FeatureCard ripple - Using ProteusRipples.surface() for theme-aware ripple
    - [x] 3.2: Theme color integration - Created ProteusRipples utility with beige palette colors
    - [x] 3.3: Light/dark validation - Alpha values optimized for both themes

- [x] **Phase 4: Screen Transitions** - COMPLETED ✅
    - [x] 4.1: NavHost configuration - Added enter/exit transitions to existing NavHost
    - [x] 4.2: Forward transitions - Slide in from right + fade for catalog→configurator
    - [x] 4.3: Back transitions - Slide in from partial left + fade for configurator→catalog
    - [x] 4.4: Background consistency fix - Applied MaterialTheme.colorScheme.background to NavHost
    - [x] 4.5: Shared transition timing fix - Synchronized shared element timing with screen transitions (500ms)

- [ ] **Phase 5: Visual Polish** - Not started
    - [ ] 5.1: Highlight fade-in
    - [ ] 5.2: Override badge
    - [ ] 5.3: Value type icons
    - [ ] 5.4: Active state border

- [ ] **Phase 6: Functional Enhancements** - Not started
    - [ ] 6.1: Pull-to-refresh
    - [ ] 6.2: Copy to clipboard
    - [ ] 6.3: Loading shimmer
    - [ ] 6.4: Empty states

---

## 💡 LESSONS LEARNED

Update this section as you implement:

### What Worked Well
- [x] **HapticUtils abstraction**: Clean reusable API with remember() optimization
- [x] **Spring animation physics**: Medium bouncy damping feels natural and responsive
- [x] **graphicsLayer performance**: Zero recomposition impact in LazyColumn with 100+ items
- [x] **InteractionSource sharing**: Same source for haptics + animation creates perfect coordination
- [x] **ProteusRipples utility**: Centralized theme-aware ripple effects with proper alpha values
- [x] **NavHost global transitions**: Clean implementation without touching individual composables

### What Needed Adjustment
- [x] **Haptic feedback API limitations**: Android <30 only supports CONTEXT_CLICK, requiring fallback logic
- [x] **Copy feature removal**: Long press clipboard functionality removed per user feedback
- [x] **Animation imports**: Required explicit `getValue` import for property delegation
- [x] **NavHost background flash**: White background visible during transitions, fixed with MaterialTheme.colorScheme.background
- [x] **Shared transition blinking**: FeatureCard blinked during transition, fixed with synchronized 300ms timing

### Performance Insights
- [x] **Hardware acceleration critical**: graphicsLayer vs Modifier transforms = 2x better performance
- [x] **Animation labels helpful**: Debugging animations easier with proper label parameter
- [x] **Spring stiffness tuning**: Low stiffness prevents jarring motion on fast taps
- [x] **Transition duration balance**: 300ms provides smooth motion without feeling slow
- [x] **Partial slide animations**: (-it/3) offset creates layered depth effect without harsh cuts
- [x] **Shared element timing critical**: Longer duration (500ms) for shared elements provides smooth morphing

### Theme Integration Notes
- [x] **Beige palette ready**: Existing theme colors work well for future ripple implementations
- [x] **Dark theme considerations**: Need higher alpha values for visibility in dark backgrounds
- [x] **Material 3 button compatibility**: Standard buttons already have theme-aware ripples, focus on custom components
- [x] **Ripple alpha optimization**: Surface ripple (0.08f) works well, primary ripple (0.12f) for emphasis

---

## 🔄 ITERATION GUIDELINES

If any phase needs revision:

1. **Document the Issue**: What didn't work as expected?
2. **Analyze Root Cause**: Why didn't it work?
3. **Propose Alternative**: What's a better approach?
4. **Re-implement**: Apply the fix
5. **Update This Plan**: Document the learning

---

## 📝 NOTES FOR FUTURE ENHANCEMENTS

Ideas that came up during implementation but are out of scope:

- (Add ideas here as they emerge)

---

## ✨ COMPLETION CRITERIA

This plan is complete when:

- [ ] All Phase 1-4 features implemented and tested
- [ ] Performance validated (60fps, smooth scrolling)
- [ ] Both themes work perfectly
- [ ] Accessibility validated
- [ ] Tested on physical device
- [ ] Preview functions created for all states
- [ ] Documentation updated
- [ ] This plan updated with learnings

---

## 🎯 SUCCESS METRICS

After implementation, the UI should feel:
- **Responsive**: Haptics and animations make interactions feel alive
- **Polished**: Professional attention to detail visible
- **Smooth**: 60fps performance maintained
- **Accessible**: Works for all users
- **On-brand**: Beige theme enhanced, not disrupted

---

*Last Updated*: [Date] - [Your Name]
*Plan Version*: 1.0