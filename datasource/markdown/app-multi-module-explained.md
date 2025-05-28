Hello everyone! In this post, I'll walk you through how Kesi Android **divided** its codebase into distinct **modules** to achieve a **multi-module architecture**. I'll explain the **reasoning behind each module**, and discuss how this approach helps us **adhere to SOLID principles** and significantly **enhances project scalability**. Let's dive right into it!

At the root level of Kesi Android, I've structured the project with the following key modules:

- `app`
- `core`
- `data`
- `domain`
- `feature`
- `testing`

## App

In an Android app using a multi-module architecture, the **`:app` module** serves as the **primary entry point and the orchestrator of the application**. It's the module that ultimately gets compiled into the runnable APK (Android Package Kit) or AAB (Android App Bundle) that users install.

Think of it as the main assembly point. Here's a breakdown of its key responsibilities and characteristics:

- **Application Entry Point:** It contains the `Application` class, `MainActivity` (or other entry activities), and the `AndroidManifest.xml` that defines the application's components and permissions.
- **Dependency Aggregation:** The `:app` module depends on various **feature modules** (which we will talk about them later), **core and data modules**. It brings together these independent pieces to form the complete application.
- **Navigation Host:** It manages the top-level navigation graph, coordinating transitions between different features. While individual features manage their internal navigation, the `:app` module handles navigation _between_ features.
- **Initialization of Global Services:** Application-wide services and libraries (like dependency injection frameworks, analytics, logging) are initialized within the `:app` module's `Application` class.
- **Build Configuration Hub:** It's where I defined application-level build configurations, such as buildConfigField and signing configurations. It can also manage different build variants: debug and release.

In essence, the `:app` module acts like the "conductor" of an orchestra, ensuring all the individual "musicians" (feature and core modules) play together harmoniously to deliver the final "symphony" (the application). This separation helps in achieving better code organization, faster build times (by only rebuilding changed modules), and improved scalability for larger projects.

## Core

The `core` module acts as a parent directory. It groups together several sub-modules that provide essential, foundational functionalities for the entire Kesi Android application. These sub-modules include:

- `:core:app`
- `:core:model`
- `:core:uisystem`

The `:core:app` module serves as a foundational library defining **abstractions and contracts** for core application-wide functionalities that are needed across various parts of the application, including feature modules. It dictates _what_ essential services should do, not _how_ they do it.

The `:core:model` module is responsible for defining the **domain models and data structures** that represent the core business logic and data of Kesi Android. It's the single source of truth for the application's data.

The `:core:uisystem` module encapsulates **foundational UI elements, themes, styling, base UI components, and UI-related utilities** that are shared across multiple feature modules to ensure a consistent look and feel.

## Data

The `data` module, similar to `core`, acts as a parent directory. It groups together several sub-modules that provide a good way to structure the data layer, focusing on abstracting away the concrete implementations of data sources. These sub-modules include:

- `:data:api`
- `:data:repository`
- `:data:retrofit`
- `:data:datastore`

The `:data:api` module serves as the **central contract or abstraction layer for all data operations** in your application. It defines _what_ data can be fetched or manipulated and _what_ operations are available, but not _how_ these operations are implemented or _where_ the data comes from. All other modules that need to interact with data (like feature modules or use cases) will depend solely on this module for their data-related interfaces, promoting loose coupling and making it easier to swap data source implementations without affecting the rest of the app.

The `:data:repository` module is responsible for **defining the data access contracts (repository interfaces) for the application's domain and providing their concrete implementations.** These implementations act as the single source of truth for specific domain data, coordinating operations between different underlying data sources (e.g., remote network API, local database, in-memory cache). It encapsulates the logic of how and where to fetch or store data.

The `:data:retrofit` module contains the **concrete implementation for making network API calls using the Retrofit library**. It provides the actual networking logic that fulfills the contracts defined by the Retrofit service interfaces in `:data:api`

The `:data:datastore` module contains the **concrete implementation for storing and retrieving data using Jetpack DataStore**. It implements the relevant data source abstractions defined in `:data:api`

## Domain

The `:domain` module represents the **core business logic and rules** of your application. It defines _what_ your application does at a high level, without specifying _how_ it's presented to the user or _how_ data is stored or retrieved. This module is a pure Kotlin module, with **no dependencies on the Android framework** or any specific external libraries (like networking or database libraries).

## Feature

A **feature module** is a self-contained unit that groups all the code, resources, and logic related to a specific feature or user flow of the application. It aims to be as independent as possible from other feature modules to promote modularity, parallel development, and potentially dynamic delivery.

Kesi Android at the moment offers:

- `:feature:article`: Displays individual blog posts, including the one you're currently reading.
- `:feature:articles`: Presents a list of all available blog posts.
- `:feature:audioplayer`: Provides functionality to listen to AI-generated audio summaries of posts.
- `:feature:discover`: Serves as the main landing screen or discovery hub for Kesi Android content.

## Testing

The `testing` module serves as a centralized hub for our testing infrastructure. This is **where** I host all the **test doubles** (like mocks or fakes) for our abstractions and provide common **test data**. Furthermore, this module declares dependencies on all the necessary **testing libraries** (e.g., JUnit, Mockito, Espresso). It's designed to be included by other modules primarily using Gradle's `testImplementation` or `androidTestImplementation` configurations, ensuring that testing utilities don't leak into the production app code.

## Convention plugins

So, that's the rundown of Kesi Android's module structure as of version 1.0.3! While this organization brings many benefits, managing dependencies and versions across all these modules can quickly become a **huge pain in the neck** if not handled correctly.

Don't worry, I won't leave you hanging! In an upcoming post, I'll dive into how I maintain **consistency** for libraries and plugins across all modules using **convention plugins**. We'll also explore how **modern Android development** leverages **version catalogs** (those handy `.toml` files) to streamline this entire process. **Stay tuned** for that deep dive!