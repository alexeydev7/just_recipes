package com.alexit.justrecipes.domain.usecase

import com.alexit.justrecipes.domain.model.ai.Body
import com.alexit.justrecipes.domain.model.ai.Message
import com.alexit.justrecipes.domain.model.ai.ResponseFormat
import com.alexit.justrecipes.domain.model.ai.ResponseFormatType
import com.alexit.justrecipes.domain.model.ai.Role
import com.alexit.justrecipes.domain.model.ai.Thinking
import com.alexit.justrecipes.domain.model.ai.ThinkingType
import com.alexit.justrecipes.domain.remote.KtorApiService
import io.ktor.client.statement.HttpResponse
import javax.inject.Inject

const val MODEL_GPT = "deepseek-flash"
const val REASONING_EFFORT = "high"

class GetRecipeAiUseCase @Inject constructor(
    private val ktorApiService: KtorApiService
) {
    suspend operator fun invoke(promptUser: String, promptSystem: String): HttpResponse {
        val model = MODEL_GPT
        val messageSystem = Message(
            role = Role.SYSTEM,
            content = promptSystem
        )
        val messageUser = Message(
            role = Role.USER,
            content = promptUser
        )
        val messages: List<Message> = listOf(messageSystem, messageUser)
        val thinking = Thinking(ThinkingType.ENABLED)
        val reasoningEffort = REASONING_EFFORT
        val maxTokens = 4096
        val responseFormat = ResponseFormat(ResponseFormatType.JSON_OBJECT)
        val stream = false
        val temperature = 1.0
        val topP = 1.0
        val logprobs = false

        return ktorApiService.getRecipeAi(
            Body(
                messages = messages,
                model = model,
                thinking = thinking,
                reasoningEffort = reasoningEffort,
                maxTokens = maxTokens,
                responseFormat = responseFormat,
                stream = stream,
                temperature = temperature,
                topP = topP,
                logprobs = logprobs
            )
        )
    }
}