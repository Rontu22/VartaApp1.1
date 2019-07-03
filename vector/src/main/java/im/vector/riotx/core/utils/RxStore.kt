/*
 * Copyright 2019 New Vector Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package im.vector.riotx.core.utils

import com.jakewharton.rxrelay2.BehaviorRelay
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers

open class RxStore<T>(defaultValue: T? = null) {

    private val storeSubject: BehaviorRelay<T> = if (defaultValue == null) {
        BehaviorRelay.create<T>()
    } else {
        BehaviorRelay.createDefault(defaultValue)
    }

    fun observe(): Observable<T> {
        return storeSubject.hide().observeOn(Schedulers.computation())
    }

    fun post(value: T) {
        storeSubject.accept(value)
    }
}
