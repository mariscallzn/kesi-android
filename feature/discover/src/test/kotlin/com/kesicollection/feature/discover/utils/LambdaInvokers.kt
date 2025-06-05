package com.kesicollection.feature.discover.utils

import com.kesicollection.feature.discover.UICategory
import com.kesicollection.feature.discover.UIContent

// This interface is built to avoid Robolectric and Mockk to duplicate class definitions on sdk
// version 23, for other versions we could use just mockk regular lambdas. But we need to test 23
interface OnSeeAllInvoker {
    operator fun invoke(category: UICategory)
}

// This interface is built to avoid Robolectric and Mockk to duplicate class definitions on sdk
// version 23, for other versions we could use just mockk regular lambdas. But we need to test 23
interface OnContentInvoker {
    operator fun invoke(content: UIContent)
}

// This interface is built to avoid Robolectric and Mockk to duplicate class definitions on sdk
// version 23, for other versions we could use just mockk regular lambdas. But we need to test 23
interface OnRetryInvoker {
    operator fun invoke()
}