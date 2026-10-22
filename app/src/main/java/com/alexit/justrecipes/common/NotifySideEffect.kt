package com.alexit.justrecipes.common

sealed class NotifySideEffect {
    data class ShowNotify(
        val message: StringResourceHolder,
        val addition: String = "",
        val state: NotifyState
    ) : NotifySideEffect()
}