package com.kesicollection.core.uisystem

typealias Reducer<State> = State.() -> State

/**
 * An interface for processing intents that can modify the state of a UI component.
 *
 * This interface defines a method [processIntent] that allows you to send intents to a
 * processor.  Each intent is processed asynchronously and can update the state through a provided [Reducer].
 *
 * @param State The type of the state managed by the component.
 */
interface IntentProcessor<State> {
    suspend fun processIntent(reducer: (Reducer<State>) -> Unit)
}

/**
 * A factory interface for creating [IntentProcessor] instances.
 *
 * This interface provides a way to create [IntentProcessor] instances for specific intents.
 * The factory is responsible for knowing which [IntentProcessor] is appropriate for a given
 * intent and returning a ready-to-use instance.
 *
 * @param State The type of the state managed by the [IntentProcessor].
 * @param Intent The type of the intent to be processed.
 */
interface IntentProcessorFactory<State, Intent> {
    fun create(intent: Intent): IntentProcessor<State>
}