package com.kesicollection.feature.quiztopics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kesicollection.core.model.Topic
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.UUID

/**
 * This ViewModel implementations follows MVI. For more info read:
 * - [proandroiddev - MVI](https://proandroiddev.com/mvi-architecture-with-kotlin-flows-and-channels-d36820b2028d)
 * - [Orbit-MVI](https://github.com/orbit-mvi/orbit-mvi)
 */
class QuizTopicsViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(
        State(uiState = QuizTopicsUiState.Loading)
    )
    val uiState: StateFlow<State>
        get() = _uiState.asStateFlow().onStart {
            sendEvent(QuizTopicsUiEvent.FetchTopics)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = State(uiState = QuizTopicsUiState.Loading)
        )

    private val uiEvent = MutableSharedFlow<QuizTopicsUiEvent>()

    private val _uiEffect = Channel<QuizTopicsUiEffect>()
    val uiEffect = _uiEffect.receiveAsFlow()

    init {
        viewModelScope.launch {
            uiEvent.asSharedFlow().collect {
                handleEvent(it)
            }
        }
    }

    //Normally called intent in the MVI architecture
    fun sendEvent(event: QuizTopicsUiEvent) {
        viewModelScope.launch { uiEvent.emit(event) }
    }

    //Again this will be called Intent on a MVI
    private fun handleEvent(event: QuizTopicsUiEvent) {
        when (event) {
            is QuizTopicsUiEvent.FetchTopics -> fetchTopics()
        }
    }

    private fun reduce(reducer: State.() -> State) {
        val newState = _uiState.value.reducer()
        _uiState.value = newState
    }

    private fun launchEffect(effect: () -> QuizTopicsUiEffect) {
        viewModelScope.launch { _uiEffect.send(effect()) }
    }

    private fun fetchTopics() {
        viewModelScope.launch {
//            try {
            val topics = listOf(
                Topic(UUID.randomUUID().toString(), "Jetpack Compose"),
                Topic(UUID.randomUUID().toString(), "Jetpack Navigation"),
                Topic(UUID.randomUUID().toString(), "Jetpack Room"),
                Topic(UUID.randomUUID().toString(), "Jetpack DataStore"),
            )
            delay(1500)
            reduce { copy(uiState = QuizTopicsUiState.Topics(topics)) }
//            } catch (e: Exception) {

//            }
        }
    }

}