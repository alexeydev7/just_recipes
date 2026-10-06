package com.alexit.justrecipes.presentation.feature.requestai.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexit.justrecipes.R
import com.alexit.justrecipes.common.NotifyState
import com.alexit.justrecipes.common.StringResourceHolder
import com.alexit.justrecipes.domain.model.ai.RecipeAi
import com.alexit.justrecipes.domain.model.ai.ResponseAi
import com.alexit.justrecipes.domain.usecase.AddAiRecipeUseCase
import com.alexit.justrecipes.domain.usecase.GetRecipeAiUseCase
import com.alexit.justrecipes.presentation.components.NotifySideEffect
import dagger.hilt.android.lifecycle.HiltViewModel
import io.ktor.client.call.body
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class AnswerAiViewModel @Inject constructor(
    private val getRecipeAiUseCase: GetRecipeAiUseCase,
    private val addAiRecipeUseCase: AddAiRecipeUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(AnswerAiUiState())
    val uiState: StateFlow<AnswerAiUiState> = _uiState.asStateFlow()

    private val _sideEffect = Channel<NotifySideEffect>()
    val sideEffect: Flow<NotifySideEffect> = _sideEffect.receiveAsFlow()


    fun getRecipeAi(promptUser: String, promptSystem: String) {
        if (uiState.value.responseAi == null) {
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    val recipeAiAnswer = withContext(Dispatchers.IO) {
                        getRecipeAiUseCase(promptUser, promptSystem)
                    }
                    when (recipeAiAnswer.status.value) {
                        in 200..299 -> {
                            val responseAi = recipeAiAnswer.body<ResponseAi>()
                            _uiState.update { currentState ->
                                currentState.copy(responseAi = responseAi)
                            }
                        }
                        else -> {
                            _sideEffect.send(
                                NotifySideEffect.ShowNotify(
                                    message = StringResourceHolder.StringResource(R.string.ai_not_available),
                                    addition = recipeAiAnswer.status.value.toString(),
                                    state = NotifyState.WARNING
                                )
                            )
                        }
                    }
                } catch (err: Throwable) {
                    _sideEffect.send(
                        NotifySideEffect.ShowNotify(
                            message = StringResourceHolder.StringResource(R.string.ai_not_available),
                            addition = err.message.toString(),
                            state = NotifyState.WARNING
                        )
                    )
                }
            }
        }
    }

    fun changeRecipeAiState(recipeAi: RecipeAi) {
        _uiState.update { currentState ->
            currentState.copy(recipeAi = recipeAi)
        }
    }

    fun changeRecipeAiTask() {
        _uiState.update { currentState ->
            val isRecipeAiOk = uiState.value.isRecipeAiOk
            currentState.copy(isRecipeAiOk = !isRecipeAiOk)
        }
    }

    fun saveRecipeAi() {
        uiState.value.recipeAi?.let {
            viewModelScope.launch {
                try {
                    addAiRecipeUseCase(it)
                    _sideEffect.send(
                        NotifySideEffect.ShowNotify(
                            message = StringResourceHolder.StringResource(R.string.added_new_recipe),
                            addition = it.name,
                            state = NotifyState.INFO
                        )
                    )
                    _uiState.update { currentState ->
                        currentState.copy(isSaved = true)
                    }
                } catch (_: Exception) {
                    _sideEffect.send(
                        NotifySideEffect.ShowNotify(
                            message = StringResourceHolder.StringResource(R.string.hardware_error_add_new_recipe),
                            addition = it.name,
                            state = NotifyState.DANGER
                        )
                    )
                }
            }
        }
    }
}
