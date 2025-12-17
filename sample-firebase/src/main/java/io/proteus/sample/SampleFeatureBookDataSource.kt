package io.proteus.sample

import io.proteus.core.data.FeatureBookDataSource
import io.proteus.core.domain.Feature
import io.proteus.core.domain.FeatureContext

class SampleFeatureBookDataSource : FeatureBookDataSource {

    override suspend fun getFeatureBook(): Result<List<FeatureContext<*>>> {
        return Result.success(getFeatures())
    }

    private fun getFeatures(): List<FeatureContext<*>> {
        return listOf(
            // AI & Machine Learning Features
            Feature(
                key = "ai_assistant_mode",
                defaultValue = "ADVANCED",
                valueClass = String::class,
            ),
            Feature(
                key = "ai_response_timeout_ms",
                defaultValue = 5000L,
                valueClass = Long::class,
            ),
            Feature(
                key = "ml_prediction_confidence_threshold",
                defaultValue = 0.85,
                valueClass = Double::class,
            ),
            Feature(
                key = "smart_suggestions_enabled",
                defaultValue = true,
                valueClass = Boolean::class,
            ),
            Feature(
                key = "ai_voice_synthesis_provider",
                defaultValue = "azure",
                valueClass = String::class,
            ),

            // Authentication & Security Features
            Feature(
                key = "multifactor_login",
                defaultValue = false,
                valueClass = Boolean::class,
            ),
            Feature(
                key = "biometric_auth_enabled",
                defaultValue = true,
                valueClass = Boolean::class,
            ),
            Feature(
                key = "session_timeout_minutes",
                defaultValue = 30L,
                valueClass = Long::class,
            ),
            Feature(
                key = "password_complexity_level",
                defaultValue = "high",
                valueClass = String::class,
            ),
            Feature(
                key = "oauth_provider_priority",
                defaultValue = "google,apple,github",
                valueClass = String::class,
            ),

            // UI & Design Features
            Feature(
                key = "design_v2",
                defaultValue = false,
                valueClass = Boolean::class,
            ),
            Feature(
                key = "dark_mode_auto_switch",
                defaultValue = true,
                valueClass = Boolean::class,
            ),
            Feature(
                key = "animation_duration_multiplier",
                defaultValue = 1.0,
                valueClass = Double::class,
            ),
            Feature(
                key = "theme_color_scheme",
                defaultValue = "material_you",
                valueClass = String::class,
            ),
            Feature(
                key = "profile_icon_alpha",
                defaultValue = 1.0,
                valueClass = Double::class,
            ),
            Feature(
                key = "card_corner_radius_dp",
                defaultValue = 16L,
                valueClass = Long::class,
            ),
            Feature(
                key = "blur_effect_intensity",
                defaultValue = 0.7,
                valueClass = Double::class,
            ),

            // Navigation & Layout Features
            Feature(
                key = "flexible_navigation",
                defaultValue = false,
                valueClass = Boolean::class,
            ),
            Feature(
                key = "bottom_nav_style",
                defaultValue = "material3",
                valueClass = String::class,
            ),
            Feature(
                key = "drawer_swipe_threshold",
                defaultValue = 0.3,
                valueClass = Double::class,
            ),
            Feature(
                key = "tab_animation_enabled",
                defaultValue = true,
                valueClass = Boolean::class,
            ),
            Feature(
                key = "navigation_gesture_sensitivity",
                defaultValue = 0.6,
                valueClass = Double::class,
            ),

            // Performance & Optimization Features
            Feature(
                key = "image_cache_size_mb",
                defaultValue = 128L,
                valueClass = Long::class,
            ),
            Feature(
                key = "lazy_loading_enabled",
                defaultValue = true,
                valueClass = Boolean::class,
            ),
            Feature(
                key = "prefetch_distance_items",
                defaultValue = 5L,
                valueClass = Long::class,
            ),
            Feature(
                key = "compression_quality",
                defaultValue = 0.8,
                valueClass = Double::class,
            ),
            Feature(
                key = "background_refresh_interval_minutes",
                defaultValue = 15L,
                valueClass = Long::class,
            ),

            // Communication Features
            Feature(
                key = "max_group_chat_size",
                defaultValue = 25L,
                valueClass = Long::class,
            ),
            Feature(
                key = "message_encryption_level",
                defaultValue = "e2e",
                valueClass = String::class,
            ),
            Feature(
                key = "typing_indicator_timeout_seconds",
                defaultValue = 3L,
                valueClass = Long::class,
            ),
            Feature(
                key = "voice_message_max_duration_seconds",
                defaultValue = 300L,
                valueClass = Long::class,
            ),
            Feature(
                key = "read_receipts_enabled",
                defaultValue = true,
                valueClass = Boolean::class,
            ),

            // Media & Content Features
            Feature(
                key = "max_file_upload_mb",
                defaultValue = 100L,
                valueClass = Long::class,
            ),
            Feature(
                key = "auto_play_videos",
                defaultValue = false,
                valueClass = Boolean::class,
            ),
            Feature(
                key = "image_quality_preset",
                defaultValue = "balanced",
                valueClass = String::class,
            ),
            Feature(
                key = "video_compression_rate",
                defaultValue = 0.75,
                valueClass = Double::class,
            ),
            Feature(
                key = "thumbnail_size_dp",
                defaultValue = 120L,
                valueClass = Long::class,
            ),

            // Server & Network Features
            Feature(
                key = "primary_server",
                defaultValue = "https://api.production.com",
                valueClass = String::class,
            ),
            Feature(
                key = "optional_server",
                defaultValue = "https://api.staging.com",
                valueClass = String::class,
            ),
            Feature(
                key = "cdn_base_url",
                defaultValue = "https://cdn.fastly.com",
                valueClass = String::class,
            ),
            Feature(
                key = "api_timeout_seconds",
                defaultValue = 30L,
                valueClass = Long::class,
            ),
            Feature(
                key = "retry_attempts",
                defaultValue = 3L,
                valueClass = Long::class,
            ),
            Feature(
                key = "connection_pool_size",
                defaultValue = 20L,
                valueClass = Long::class,
            ),

            // Analytics & Tracking Features
            Feature(
                key = "analytics_enabled",
                defaultValue = true,
                valueClass = Boolean::class,
            ),
            Feature(
                key = "crash_reporting_enabled",
                defaultValue = true,
                valueClass = Boolean::class,
            ),
            Feature(
                key = "user_behavior_tracking_level",
                defaultValue = "basic",
                valueClass = String::class,
            ),
            Feature(
                key = "event_batching_size",
                defaultValue = 50L,
                valueClass = Long::class,
            ),
            Feature(
                key = "metrics_collection_interval_minutes",
                defaultValue = 5L,
                valueClass = Long::class,
            ),

            // Notification Features
            Feature(
                key = "push_notifications_enabled",
                defaultValue = true,
                valueClass = Boolean::class,
            ),
            Feature(
                key = "notification_sound_enabled",
                defaultValue = true,
                valueClass = Boolean::class,
            ),
            Feature(
                key = "badge_count_enabled",
                defaultValue = true,
                valueClass = Boolean::class,
            ),
            Feature(
                key = "notification_priority_threshold",
                defaultValue = 3L,
                valueClass = Long::class,
            ),
            Feature(
                key = "quiet_hours_start",
                defaultValue = "22:00",
                valueClass = String::class,
            ),
            Feature(
                key = "quiet_hours_end",
                defaultValue = "08:00",
                valueClass = String::class,
            ),

            // Experimental Features
            Feature(
                key = "experimental_ar_features",
                defaultValue = false,
                valueClass = Boolean::class,
            ),
            Feature(
                key = "beta_voice_commands",
                defaultValue = false,
                valueClass = Boolean::class,
            ),
            Feature(
                key = "preview_gesture_navigation",
                defaultValue = false,
                valueClass = Boolean::class,
            ),
            Feature(
                key = "alpha_neural_search",
                defaultValue = false,
                valueClass = Boolean::class,
            ),
            Feature(
                key = "experimental_3d_touch_pressure",
                defaultValue = 0.5,
                valueClass = Double::class,
            ),

            // Accessibility Features
            Feature(
                key = "font_scale_factor",
                defaultValue = 1.0,
                valueClass = Double::class,
            ),
            Feature(
                key = "high_contrast_mode",
                defaultValue = false,
                valueClass = Boolean::class,
            ),
            Feature(
                key = "screen_reader_optimized",
                defaultValue = false,
                valueClass = Boolean::class,
            ),
            Feature(
                key = "voice_over_speed",
                defaultValue = "normal",
                valueClass = String::class,
            ),
            Feature(
                key = "haptic_feedback_intensity",
                defaultValue = 0.8,
                valueClass = Double::class,
            ),

            // Localization Features
            Feature(
                key = "auto_detect_language",
                defaultValue = true,
                valueClass = Boolean::class,
            ),
            Feature(
                key = "fallback_language",
                defaultValue = "en",
                valueClass = String::class,
            ),
            Feature(
                key = "rtl_layout_enabled",
                defaultValue = false,
                valueClass = Boolean::class,
            ),
            Feature(
                key = "translation_service_provider",
                defaultValue = "google_translate",
                valueClass = String::class,
            ),
            Feature(
                key = "currency_format",
                defaultValue = "USD",
                valueClass = String::class,
            ),

            // Gaming & Gamification Features
            Feature(
                key = "achievements_enabled",
                defaultValue = true,
                valueClass = Boolean::class,
            ),
            Feature(
                key = "leaderboard_enabled",
                defaultValue = false,
                valueClass = Boolean::class,
            ),
            Feature(
                key = "daily_streak_bonus_multiplier",
                defaultValue = 1.5,
                valueClass = Double::class,
            ),
            Feature(
                key = "reward_points_per_action",
                defaultValue = 10L,
                valueClass = Long::class,
            ),
            Feature(
                key = "mini_games_enabled",
                defaultValue = false,
                valueClass = Boolean::class,
            ),

            // Commerce & Payments Features
            Feature(
                key = "in_app_purchases_enabled",
                defaultValue = true,
                valueClass = Boolean::class,
            ),
            Feature(
                key = "payment_provider",
                defaultValue = "stripe",
                valueClass = String::class,
            ),
            Feature(
                key = "subscription_trial_days",
                defaultValue = 14L,
                valueClass = Long::class,
            ),
            Feature(
                key = "price_currency_conversion_enabled",
                defaultValue = true,
                valueClass = Boolean::class,
            ),
            Feature(
                key = "discount_threshold_percentage",
                defaultValue = 0.15,
                valueClass = Double::class,
            ),

            // Social Features
            Feature(
                key = "social_sharing_enabled",
                defaultValue = true,
                valueClass = Boolean::class,
            ),
            Feature(
                key = "friend_recommendation_algorithm",
                defaultValue = "mutual_contacts",
                valueClass = String::class,
            ),
            Feature(
                key = "profile_visibility_default",
                defaultValue = "friends",
                valueClass = String::class,
            ),
            Feature(
                key = "activity_feed_refresh_rate_minutes",
                defaultValue = 2L,
                valueClass = Long::class,
            ),
            Feature(
                key = "social_login_providers",
                defaultValue = "facebook,twitter,linkedin",
                valueClass = String::class,
            ),

            // Search Features
            Feature(
                key = "search_suggestions_enabled",
                defaultValue = true,
                valueClass = Boolean::class,
            ),
            Feature(
                key = "search_autocomplete_delay_ms",
                defaultValue = 300L,
                valueClass = Long::class,
            ),
            Feature(
                key = "search_results_per_page",
                defaultValue = 20L,
                valueClass = Long::class,
            ),
            Feature(
                key = "fuzzy_search_threshold",
                defaultValue = 0.6,
                valueClass = Double::class,
            ),
            Feature(
                key = "search_history_enabled",
                defaultValue = true,
                valueClass = Boolean::class,
            ),

            // Content Moderation Features
            Feature(
                key = "auto_moderation_enabled",
                defaultValue = true,
                valueClass = Boolean::class,
            ),
            Feature(
                key = "profanity_filter_level",
                defaultValue = "medium",
                valueClass = String::class,
            ),
            Feature(
                key = "spam_detection_threshold",
                defaultValue = 0.75,
                valueClass = Double::class,
            ),
            Feature(
                key = "user_reporting_enabled",
                defaultValue = true,
                valueClass = Boolean::class,
            ),
            Feature(
                key = "content_review_queue_size",
                defaultValue = 100L,
                valueClass = Long::class,
            ),

            // Advanced Configuration Features
            Feature(
                key = "feature_flag_refresh_interval_minutes",
                defaultValue = 10L,
                valueClass = Long::class,
            ),
            Feature(
                key = "config_cache_ttl_hours",
                defaultValue = 24L,
                valueClass = Long::class,
            ),
            Feature(
                key = "ab_test_participation_rate",
                defaultValue = 0.1,
                valueClass = Double::class,
            ),
            Feature(
                key = "rollout_percentage",
                defaultValue = 0.05,
                valueClass = Double::class,
            ),
            Feature(
                key = "canary_deployment_enabled",
                defaultValue = false,
                valueClass = Boolean::class,
            )
        )
    }
}