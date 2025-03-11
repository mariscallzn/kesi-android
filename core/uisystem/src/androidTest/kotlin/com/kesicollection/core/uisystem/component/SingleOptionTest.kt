package com.kesicollection.core.uisystem.component

import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import com.kesicollection.core.uisystem.theme.KesiTheme
import org.junit.Rule
import org.junit.Test

class SingleOptionTest {
    @get:Rule
    val composeRuleTest = createComposeRule()

    @Test
    fun testContent() {
        with(composeRuleTest) {
            setContent {
                KesiTheme {
                    SingleOption {
                        Text(
                            "This is the line that will be displayed",
                            modifier = Modifier.testTag("text")
                        )
                    }
                }
            }
            onNodeWithTag("text").assertIsDisplayed()
            onNodeWithTag(":core:uisystem:singleOptionCard").assertIsDisplayed()
        }
    }
}