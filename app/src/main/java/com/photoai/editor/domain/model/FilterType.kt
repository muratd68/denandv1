package com.photoai.editor.domain.model

/**
 * Available AI filter types for photo editing
 * @property displayName Name shown to user
 * @property isPremium Whether this filter requires premium subscription
 */
enum class FilterType(
    val displayName: String,
    val isPremium: Boolean = false
) {
    ENHANCE("Enhance", isPremium = false),
    BEAUTIFY("Beautify", isPremium = false),
    PORTRAIT_HD("Portrait HD", isPremium = true),
    CARTOON("Cartoon", isPremium = true),
    VINTAGE("Vintage", isPremium = false),
    BLACK_AND_WHITE("B&W", isPremium = false)
}
