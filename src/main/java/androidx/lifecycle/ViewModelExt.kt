/*
 * Copyright (C) 2019 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package androidx.lifecycle

import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

fun ViewModel.launchDataLoad(
    loadingLiveData: MutableLiveData<Boolean> ?= null,
    errorLiveData: MutableLiveData<String> ?= null,
    getErrorMessage: (Exception) -> String,
    block: suspend () -> Unit
): Job {
    return viewModelScope.launch {
        try {
            loadingLiveData?.value = true
            block()
        } catch (error: Exception) {
            errorLiveData?.value = getErrorMessage.invoke(error)
        } finally {
            loadingLiveData?.value = false
        }
    }
}