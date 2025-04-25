package com.kesicollection.core.analytics

/**
 * Interface for wrapping analytics logging functionality.
 *
 * This interface provides a standardized way to log events and their associated
 * parameters to various analytics platforms. Implementations of this interface
 * can handle the specific details of sending data to different analytics services.
 */
interface AnalyticsWrapper {

    /**
     * The event associated with this data.
     *
     * This property represents the specific [Event] that this data is related to.
     * It could be a user action, a system event, or any other significant occurrence
     * that is being tracked or processed.
     *
     * @see Event
     */
    val event: Event

    /**
     * The parameter configuration for this operation.
     *
     * This object defines the specific input parameters required or available for the associated operation.
     * It could specify things like required fields, data types, default values, or constraints.
     *
     * @see Param
     */
    val param: Param

    /**
     * Represents a generic event that can be tracked within the application.
     *
     * Currently, this interface only defines a `screenView` property, which represents
     * the name or identifier of the screen the event is associated with.
     *
     * In the future, this interface can be extended to include other relevant
     * event properties like user actions, timestamps, and event categories.
     */
    interface Event {
        val screenView: String
    }

    /**
     * Represents a parameter object containing information about a screen.
     *
     * This interface is used to encapsulate data related to a specific screen, such as its name and class.
     * It can be implemented by classes that need to provide screen-specific information.
     */
    interface Param {
        val screenName: String
        val screenClass: String
    }

    /**
     * Logs an event with the given name and optional parameters.
     *
     * This function provides a centralized way to log events for analytics or tracking purposes.
     * It accepts an event name and an optional map of parameters to be associated with the event.
     *
     * @param eventName The name of the event being logged. This should be a descriptive string
     *                  identifying the type of event. Examples: "user_login", "product_view",
     *                  "button_click". It should not be empty.
     * @param params An optional map of key-value pairs representing parameters associated with the event.
     *               The keys should be strings, and the values can be of any type (e.g., String, Int, Boolean).
     *               This allows for providing additional context to the event. If no parameters are needed,
     *               this can be omitted or passed as null.
     *
     */
    fun logEvent(eventName: String, params: Map<String, Any>? = null)
}