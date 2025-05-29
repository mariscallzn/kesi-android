package com.kesicollection.core.uisystem

import io.noties.prism4j.GrammarLocator
import io.noties.prism4j.GrammarUtils
import io.noties.prism4j.Prism4j
import io.noties.prism4j.Prism4j.grammar
import io.noties.prism4j.Prism4j.pattern
import io.noties.prism4j.Prism4j.token
import java.util.regex.Pattern
import java.util.regex.Pattern.compile

/**
 * [GrammarLocator] for the Kotlin language. This class provides the Prism4j grammar
 * for Kotlin, enabling syntax highlighting of Kotlin code within the application.
 * It utilizes a predefined grammar structure to identify various language elements
 * like keywords, comments, strings, etc.
 */
class KotlinGrammarLocator : GrammarLocator {
    /**
     * Retrieves the Prism4j.Grammar for the specified language.
     *
     * @param prism4j The [Prism4j] instance.
     * @param language The name of the language for which to get the grammar.
     *                 This implementation specifically handles "kotlin".
     * @return The [Prism4j.Grammar] for Kotlin if the language is "kotlin", otherwise null
     *         (though in this specific implementation, it always returns the Kotlin grammar
     *         if called, as it's tied to the `languages()` method).
     */
    override fun grammar(
        prism4j: Prism4j,
        language: String
    ): Prism4j.Grammar? = PrismKotlin.create()

    /**
     * Returns a set of language names that this locator can provide grammars for.
     *
     * @return A set containing "kotlin".
     */
    override fun languages(): Set<String?> =
        setOf("kotlin")
}

/**
 * Object responsible for creating and configuring the Prism4j grammar for the Kotlin language.
 * This object defines tokens and patterns that Prism4j uses to parse and highlight Kotlin code.
 */
object PrismKotlin {
    /**
     * Creates and returns a [Prism4j.Grammar] for the Kotlin language.
     * This method defines the various tokens (like comments, strings, keywords, etc.) and their patterns.
     * @return The fully configured [Prism4j.Grammar] for Kotlin.
     */
    fun create(): Prism4j.Grammar {
        val kotlin = grammar(
            "kotlin",
            token(
                "comment",
                pattern(compile("(^|[^\\\\])\\/\\*[\\s\\S]*?(?:\\*\\/|$)"), true),
                pattern(compile("(^|[^\\\\:])\\/\\/.*"), true, true)
            ),
            token(
                "string",
                pattern(
                    compile("([\"'])(?:\\\\(?:\\r\\n|[\\s\\S])|(?!\\1)[^\\\\\\r\\n])*\\1"),
                    false,
                    true
                )
            ),
            token(
                "class-name",
                pattern(
                    compile("((?:\\b(?:class|interface|extends|implements|trait|instanceof|new)\\s+)|(?:catch\\s+\\())[\\w.\\\\]+"),
                    true,
                    false,
                    null,
                    grammar("inside", token("punctuation", pattern(compile("[.\\\\]"))))
                )
            ),
            token(
                "keyword",
                pattern(compile("\\b(?:if|else|while|do|for|return|in|instanceof|function|new|try|throw|catch|finally|null|break|continue)\\b"))
            ),
            token("boolean", pattern(compile("\\b(?:true|false)\\b"))),
            token("function", pattern(compile("[a-z0-9_]+(?=\\()", Pattern.CASE_INSENSITIVE))),
            token(
                "number",
                pattern(
                    compile(
                        "\\b0x[\\da-f]+\\b|(?:\\b\\d+\\.?\\d*|\\B\\.\\d+)(?:e[+-]?\\d+)?",
                        Pattern.CASE_INSENSITIVE
                    )
                )
            ),
            token(
                "operator",
                pattern(compile("--?|\\+\\+?|!=?=?|<=?|>=?|==?=?|&&?|\\|\\|?|\\?|\\*|\\/|~|\\^|%"))
            ),
            token("punctuation", pattern(compile("[{}\\[\\];(),.:]"))),
            token(
                "keyword",
                pattern(
                    compile("(^|[^.])\\b(?:abstract|actual|annotation|as|break|by|catch|class|companion|const|constructor|continue|crossinline|data|do|dynamic|else|enum|expect|external|final|finally|for|fun|get|if|import|in|infix|init|inline|inner|interface|internal|is|lateinit|noinline|null|object|open|operator|out|override|package|private|protected|public|reified|return|sealed|set|super|suspend|tailrec|this|throw|to|try|typealias|val|var|vararg|when|where|while)\\b"),
                    true
                )
            ),
            token(
                "function",
                pattern(compile("\\w+(?=\\s*\\()")),
                pattern(compile("(\\.)\\w+(?=\\s*\\{)"), true)
            ),
            token(
                "number",
                pattern(compile("\\b(?:0[xX][\\da-fA-F]+(?:_[\\da-fA-F]+)*|0[bB][01]+(?:_[01]+)*|\\d+(?:_\\d+)*(?:\\.\\d+(?:_\\d+)*)?(?:[eE][+-]?\\d+(?:_\\d+)*)?[fFL]?)\\b"))
            ),
            token(
                "operator",
                pattern(compile("\\+[+=]?|-[-=>]?|==?=?|!(?:!|==?)?|[\\/*%<>]=?|[?:]:?|\\.\\.|&&|\\|\\||\\b(?:and|inv|or|shl|shr|ushr|xor)\\b"))
            )
        )
        GrammarUtils.insertBeforeToken(
            kotlin, "string",
            token(
                "raw-string",
                pattern(compile("(\"\"\"|''')[\\s\\S]*?\\1"), false, false, "string")
            )
        );

        GrammarUtils.insertBeforeToken(
            kotlin, "keyword",
            token(
                "annotation",
                pattern(
                    compile("\\B@(?:\\w+:)?(?:[A-Z]\\w*|\\[[^\\]]+\\])"),
                    false,
                    false,
                    "builtin"
                )
            )
        );

        GrammarUtils.insertBeforeToken(
            kotlin, "function",
            token("label", pattern(compile("\\w+@|@\\w+"), false, false, "symbol"))
        );

        // this grammar has 1 token: interpolation, which has 2 patterns
        val interpolationInside: Prism4j.Grammar
        run { // Scope function to mimic the Java block
            val tokens = mutableListOf<Prism4j.Token>() // Use mutableListOf for Kotlin
            tokens.add(
                token(
                    "delimiter",
                    pattern(compile("^\\$\\{|\\}$"), false, false, "variable")
                )
            )
            tokens.addAll(kotlin.tokens())

            interpolationInside = grammar(
                "inside",
                token(
                    "interpolation",
                    pattern(
                        compile("\\$\\{[^}]+\\}"),
                        false,
                        false,
                        null,
                        grammar("inside", tokens)
                    ),
                    pattern(compile("\\$\\w+"), false, false, "variable")
                )
            )
        }

        val stringToken = GrammarUtils.findToken(kotlin, "string")
        val rawStringToken = GrammarUtils.findToken(kotlin, "raw-string")

        if (stringToken != null && rawStringToken != null) {
            val stringPattern = stringToken.patterns().get(0)
            val rawStringPattern = rawStringToken.patterns().get(0)

            // It's generally safer to add the new pattern first and then remove the old one,
            // or ensure the list supports modification during iteration if removing first.
            // Kotlin's List from Java interop should behave similarly here.

            stringToken.patterns().add(
                pattern(
                    stringPattern.regex(),
                    stringPattern.lookbehind(),
                    stringPattern.greedy(),
                    stringPattern.alias(),
                    interpolationInside
                )
            )
            stringToken.patterns().removeAt(0) // Use removeAt for indexed removal

            rawStringToken.patterns().add(
                pattern(
                    rawStringPattern.regex(),
                    rawStringPattern.lookbehind(),
                    rawStringPattern.greedy(),
                    rawStringPattern.alias(),
                    interpolationInside
                )
            )
            rawStringToken.patterns().removeAt(0)

        } else {
            // Consider using Kotlin's require or check functions for preconditions
            // For example: require(stringToken != null && rawStringToken != null) { "message" }
            throw RuntimeException(
                "Unexpected state, cannot find `string` and/or `raw-string` tokens " +
                        "inside kotlin grammar"
            )
        }

        return kotlin
    }
}