package com.kesicollection.feature.discover.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kesicollection.feature.discover.UICategory

fun LazyListScope.categoriesSection(
    categories: List<UICategory>,
    onCategoryClick: (UICategory) -> Unit,
    modifier: Modifier = Modifier
) {
    stickyHeader {
        LazyRow(
            modifier = modifier
                .fillParentMaxWidth()
                .background(MaterialTheme.colorScheme.background),
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(categories) { category ->
                OutlinedButton(onClick = { onCategoryClick(category) }) {
                    Text(text = category.name)
                }
            }
        }
    }
}