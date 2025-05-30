# Kotlin Generics Demystified: `in`, `out`, `where`, and the Mighty `*`!

Generics. The word alone can evoke a mix of appreciation for type safety and a slight dread of complex angle-bracket syntax. But if you're a Kotlin developer, you're in luck! Kotlin's approach to generics is designed to be more intuitive and powerful, especially with its clear keywords: `in`, `out`, and `where`.

Ever scratched your head over Java's wildcards (`? extends T`, `? super T`)? Kotlin offers a cleaner way with **declaration-site variance** and **type projections**. Ready to master them and write more robust, flexible, and elegant Kotlin code? Let's dive in!

## What's the Big Deal with Variance?

Before we jump into `in` and `out`, let's quickly understand **variance**. In the context of generics, variance describes how subtyping between simpler types relates to subtyping between more complex types (like generic classes).

Consider a simple class hierarchy: `Any` is a supertype of `String`. Now, if we have a generic class `Box<T>`, how does `Box<String>` relate to `Box<Any>`?

- **Covariance:** If `Box<String>` is a subtype of `Box<Any>`, the type `Box<T>` is **covariant** in `T`. This means you can use a `Box<String>` where a `Box<Any>` is expected. (Think "produces" or "outputs" `T`).

- **Contravariance:** If `Box<Any>` is a subtype of `Box<String>` (yes, it's the other way around!), the type `Box<T>` is **contravariant** in `T`. This means you can use a `Box<Any>` where a `Box<String>` is expected. (Think "consumes" or "accepts" `T`).

- **Invariance:** If `Box<String>` and `Box<Any>` have no subtype relationship (neither is a subtype of the other), the type `Box<T>` is **invariant** in `T`.


Understanding these concepts is key to unlocking the power of `in` and `out`.

## Declaration-Site Variance: `out` and `in` – The Kotlin Elegance

One of Kotlin's most elegant features is **declaration-site variance**. This means you can specify the variance behavior of a generic type parameter directly in its class or interface declaration.

### `out T`: Covariance – The Producer 🍓

When you declare a generic type parameter `T` with the `out` keyword (e.g., `interface Source<out T>`), you're telling the compiler two things:

1. The type `Source<T>` is **covariant** in `T`. So, if `S` is a subtype of `U`, then `Source<S>` is a subtype of `Source<U>`.

    - Example: `Source<String>` can be safely used as a `Source<Any>`.

2. The class/interface `Source` can only _produce_ values of type `T`. This means `T` can only appear in "out" positions, primarily as return types of functions. It cannot be used as a parameter type for its methods (except in constructor parameters if they are `val` or `var` and appropriately restricted).


**Why this restriction?** Safety! If `Source<String>` is treated as `Source<Any>`, you should only be able to get `Any` (or `String`) out of it. If you could _put_ an `Int` into it (via a hypothetical `add(item: T)` method), you'd break the `Source<String>`'s promise to only deal with `String`s.

**Example: A Read-Only Source**

```
interface Producer<out T> {
    fun produce(): T
    // fun consume(item: T) // Error: Type parameter T is declared as 'out'
                        // and occurs in 'in' position in type T
}

class StringProducer : Producer<String> {
    override fun produce(): String = "Hello, Kotlin!"
}

class AnyProducer(private val value: Any) : Producer<Any> {
    override fun produce(): Any = value
}

fun main() {
    val stringProducer: Producer<String> = StringProducer()
    val anyProducer: Producer<Any> = stringProducer // This is fine! Covariance in action.

    println(anyProducer.produce()) // Outputs: Hello, Kotlin!

    // val anotherAnyProducer: Producer<Any> = AnyProducer(123)
    // val anotherStringProducer: Producer<String> = anotherAnyProducer // Error! Any is not String
}
```

Kotlin's `List<out E>` is a perfect example of a covariant type. You can get elements out of it, but you can't add new ones (it's read-only).

### `in T`: Contravariance – The Consumer 🍽️

When you declare a generic type parameter `T` with the `in` keyword (e.g., `interface Consumer<in T>`), it means:

1. The type `Consumer<T>` is **contravariant** in `T`. So, if `S` is a subtype of `U`, then `Consumer<U>` is a subtype of `Consumer<S>`.

    - Example: `Consumer<Any>` can be safely used as a `Consumer<String>`. If you have something that can consume _any_ object, it can certainly consume a `String`.

2. The class/interface `Consumer` can only _consume_ values of type `T`. This means `T` can only appear in "in" positions, primarily as parameter types of functions. It cannot be used as a return type (unless it's a parameter to another generic type that is also `in`).


**Why this restriction?** Again, safety! If you have a `Consumer<Any>` (e.g., something that prints any object) and you assign it to a variable of type `Consumer<String>`, you should still be able to pass `String`s to it. If `Consumer<T>` could return `T`, and `Consumer<Any>` returned an `Int`, assigning it to `Consumer<String>` would lead to expecting a `String` but getting an `Int`.

**Example: A Generic Sink**

```
interface Sink<in T> {
    fun consume(item: T)
    // fun produce(): T // Error: Type parameter T is declared as 'in'
                     // and occurs in 'out' position in type T
}

class AnySink : Sink<Any> {
    override fun consume(item: Any) {
        println("Consuming (Any): $item")
    }
}

class StringSink : Sink<String> {
    override fun consume(item: String) {
        println("Consuming (String): ${item.uppercase()}")
    }
}

fun main() {
    val anySink: Sink<Any> = AnySink()
    val stringSink: Sink<String> = anySink // This is fine! Contravariance.
                                         // A sink for Any can handle Strings.

    stringSink.consume("hello world") // Uses AnySink, outputs: Consuming (Any): hello world

    val specificStringSink: Sink<String> = StringSink()
    // val anotherAnySink: Sink<Any> = specificStringSink // Error! A String sink cannot handle Any.
    specificStringSink.consume("test") // Uses StringSink, outputs: Consuming (String): TEST
}
```

Kotlin's `Comparable<in T>` is a classic example. If you have something that can compare `Any` objects, it can surely compare `String` objects.

### Invariance: The Default

If you don't specify `in` or `out`, the generic type is **invariant**. This is the case for types that both consume and produce `T`, like `MutableList<T>`.

- `MutableList<String>` is **not** a `MutableList<Any>`.

- `MutableList<Any>` is **not** a `MutableList<String>`.


This is because if `MutableList<String>` were a `MutableList<Any>`, you could add an `Int` to it, breaking its type promise. If `MutableList<Any>` were a `MutableList<String>`, you could try to retrieve an element expecting a `String` but get an `Int` that was previously added.

## Use-Site Variance: Type Projections (Hello, Java Wildcards!)

Sometimes, you don't control the declaration of a class, or a class is naturally invariant (like `MutableList`), but you want to use it in a covariant or contravariant way in a specific context (e.g., a function parameter). This is where **use-site variance** or **type projections** come in. They are Kotlin's answer to Java's `? extends T` and `? super T`.

### `out T` Projection: Restricting to a Producer

If a function takes a `MutableList<T>` but only _reads_ from it, you can project `T` as an `out` type.

```
fun printAll(list: MutableList<out Any>) { // list is effectively read-only here
    for (item in list) {
        println(item)
    }
    // list.add(42) // Error! 'add' is not available for MutableList<out Any>
                  // because T is projected as 'out'
}

fun main() {
    val strings: MutableList<String> = mutableListOf("a", "b", "c")
    printAll(strings) // Works! MutableList<String> can be passed as MutableList<out Any>

    val ints: MutableList<Int> = mutableListOf(1, 2, 3)
    printAll(ints) // Works! MutableList<Int> can be passed as MutableList<out Any>
}
```

Here, `MutableList<out Any>` means "a mutable list of some specific type that is a subtype of `Any`, but I'll only treat it as a producer of `Any`."

### `in T` Projection: Restricting to a Consumer

If a function takes a `MutableList<T>` but only _writes_ to it (or consumes `T`), you can project `T` as an `in` type.

```
fun addGreetings(list: MutableList<in String>, count: Int) {
    for (i in 1..count) {
        list.add("Hello #$i") // Works! We can add Strings
    }
    // val item: String = list.get(0) // Error! 'get' returns Any? for MutableList<in String>
                                    // because T is projected as 'in'
}

fun main() {
    val anyList: MutableList<Any> = mutableListOf(1, true)
    addGreetings(anyList, 2) // Works! We can add Strings to a list of Any.
    println(anyList) // Output: [1, true, Hello #1, Hello #2]

    val stringList: MutableList<String> = mutableListOf()
    addGreetings(stringList, 1)
    println(stringList) // Output: [Hello #1]
}
```

Here, `MutableList<in String>` means "a mutable list of some specific type that is a supertype of `String`, and I'll only treat it as a consumer of `String`s." When you read from it, you only get `Any?` because you don't know the exact supertype.

## Star-Projection (`*`): When You Truly Don't Care (Safely!)

What if you have a generic type, but you don't know or care about the specific type argument at a particular point? This is where **star-projection** (`*`) comes in. It's Kotlin's way of saying "I know this is a `Box` of _something_, but I don't care what that something is."

- For `MyType<out T>`: `MyType<*>` is equivalent to `MyType<out Any?>`. You can safely read values as `Any?`.

- For `MyType<in T>`: `MyType<*>` is equivalent to `MyType<in Nothing>`. You can't safely pass anything to its methods that expect `T`.

- For `MyType<T>` (invariant):

    - When reading: `MyType<*>` is like `MyType<out Any?>`.

    - When writing: `MyType<*>` is like `MyType<in Nothing>`.


**Example:**

```
fun printBoxContent(box: Box<*>) { // Box<T> is invariant
    // val item = box.put("test") // Error: Cannot write with star projection on invariant type
    val item = box.get()        // OK: item is of type Any?
    println("Box contains: $item")
}

class Box<T>(private var item: T) {
    fun get(): T = item
    fun put(newItem: T) {
        item = newItem
    }
}

fun main() {
    val stringBox = Box("Starry Night")
    val intBox = Box(123)

    printBoxContent(stringBox) // Box contains: Starry Night
    printBoxContent(intBox)    // Box contains: 123

    val listOfAny: List<*> = listOf(1, "two", true) // List<out E>
    val firstElement: Any? = listOfAny[0] // Reading is fine, type is Any?
    println(firstElement)
}
```

Star-projection is useful when you only need to know that an object is an instance of a generic type, without needing to know its specific type parameter. For example, `if (value is List<*>)` checks if `value` is any kind of list.

## Generic Constraints: The `where` Clause for Superpowers

Sometimes, you need your generic type parameter `T` to have certain capabilities, like implementing multiple interfaces or extending a specific class _and_ implementing interfaces. This is where **generic constraints** come in, often using the `where` clause for more complex scenarios.

A simple upper bound can be specified with a colon:

```
fun <T : Comparable<T>> sort(list: List<T>): List<T> {
    return list.sorted()
}
```

Here, `T` must be a subtype of `Comparable<T>`.

For **multiple upper bounds**, you use the `where` clause:

```
interface Archivable {
    fun archive()
}

interface Serializable {
    fun serialize(): String
}

// T must implement both CharSequence and Archivable
fun <T> processDocument(doc: T) where T : CharSequence, T : Archivable {
    println("Document content length: ${doc.length}") // Access CharSequence methods
    doc.archive() // Access Archivable methods
}

class Report(val content: String) : CharSequence by content, Archivable {
    override fun archive() {
        println("Archiving report: ${content.take(20)}...")
    }
}

fun main() {
    val myReport = Report("This is a very long and important report about Kotlin generics.")
    processDocument(myReport)
    // Output:
    // Document content length: 61
    // Archiving report: This is a very long...

    // val justAString = "test"
    // processDocument(justAString) // Error: String does not implement Archivable
}
```

The `where` clause allows you to demand more from your type parameters, making your generic functions more powerful and type-safe by ensuring that `T` has all the necessary methods and properties you intend to use.

## Conclusion: Embrace the Generic Power!

Kotlin's generics system, with its declaration-site variance (`in`, `out`), use-site projections, star-projection (`*`), and flexible `where` clauses, offers a significant improvement in terms of clarity and safety over older approaches.

By understanding and utilizing these features, you can:

- Design more flexible and reusable APIs.

- Enhance type safety and reduce runtime errors.

- Write cleaner, more readable generic code.


So, go forth and conquer those angle brackets! Use `in` for consumers, `out` for producers, `*` when the type is unknown but you need to operate safely, and `where` to give your generics superpowers. Your future self (and your team) will thank you for it.

**What are your favorite Kotlin generics features or tricky scenarios you've encountered?**