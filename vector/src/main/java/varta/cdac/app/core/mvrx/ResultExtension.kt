/*
 * Copyright (c) 2021 New Vector Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package varta.cdac.app.core.mvrx

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.Success

/**
 * Note: this will be removed when upgrading to mvrx2
 */
suspend fun <A> runCatchingToAsync(block: suspend () -> A): Async<A> {
    return runCatching {
        block.invoke()
    }.fold(
            { Success(it) },
            { Fail(it) }
    )
}
