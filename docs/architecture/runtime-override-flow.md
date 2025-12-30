# Runtime Override Flow

```mermaid
flowchart TD
    START([App Requests Config Value])
    PROVIDER{FeatureConfigProvider}
    MOCK_CHECK{MockConfigProvider<br/>has override?}
    OVERRIDE_VALUE[Return Override Value]
    REMOTE_CHECK{Remote Provider<br/>has value?}
    REMOTE_VALUE[Return Remote Value]
    DEFAULT_VALUE[Return Default Value]
    END_SUCCESS([Value Returned])
    END_DEFAULT([Default Returned])

    subgraph "Override Management"
        UI_OVERRIDE[Override UI Action]
        SET_OVERRIDE[Set Override in Mock]
        PERSIST[Persist to SharedPreferences]
        REMOVE_OVERRIDE[Remove Override]
        DELETE_PERSIST[Delete from SharedPreferences]
    end

    %% Main Flow
    START --> PROVIDER
    PROVIDER --> MOCK_CHECK
    MOCK_CHECK -->|Yes| OVERRIDE_VALUE
    OVERRIDE_VALUE --> END_SUCCESS

    MOCK_CHECK -->|No| REMOTE_CHECK
    REMOTE_CHECK -->|Yes| REMOTE_VALUE
    REMOTE_VALUE --> END_SUCCESS

    REMOTE_CHECK -->|No| DEFAULT_VALUE
    DEFAULT_VALUE --> END_DEFAULT

    %% Override Management Flow
    UI_OVERRIDE --> SET_OVERRIDE
    SET_OVERRIDE --> PERSIST
    PERSIST --> MOCK_CHECK

    UI_OVERRIDE --> REMOVE_OVERRIDE
    REMOVE_OVERRIDE --> DELETE_PERSIST
    DELETE_PERSIST --> MOCK_CHECK

    %% Styling
    classDef startEnd fill:#B59D82,stroke:#7A6650,color:#fff
    classDef process fill:#F5EFE6,stroke:#B59D82,color:#5C4D3D
    classDef decision fill:#EBE0D0,stroke:#9D8468,color:#5C4D3D
    classDef override fill:#C7B299,stroke:#7A6650,color:#fff

    class START,END_SUCCESS,END_DEFAULT startEnd
    class PROVIDER,OVERRIDE_VALUE,REMOTE_VALUE,DEFAULT_VALUE process
    class MOCK_CHECK,REMOTE_CHECK decision
    class UI_OVERRIDE,SET_OVERRIDE,PERSIST,REMOVE_OVERRIDE,DELETE_PERSIST override
```

## Override Priority

1. **Mock Override** (Highest Priority)
   - Set via UI or programmatically
   - Persisted in SharedPreferences
   - Survives app restarts

2. **Remote Provider Value** (Medium Priority)
   - Firebase Remote Config
   - Custom provider implementation
   - CleverTap or other services

3. **Default Value** (Lowest Priority)
   - Defined in feature definition
   - Used when no override or remote value exists
   - Ensures app never crashes from missing config

## Persistence

- Overrides are stored in SharedPreferences
- Key format: `proteus_override_{feature_key}`
- Values are serialized as JSON with type information
- Cleared when override is explicitly removed