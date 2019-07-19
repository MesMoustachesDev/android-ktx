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

import android.content.Context
import androidx.fragment.app.Fragment

class MediatorStateLiveData<D> : MediatorLiveData<D>() {
    override fun postValue(newValue: D?) {
        if (value != newValue) {
            super.postValue(newValue)
        }
    }
}

fun <T> LiveData<T>.nonNullObserve(lifecycleOwner: LifecycleOwner, action: (t: T) -> Unit) {
    this.observe(lifecycleOwner, Observer<T> {
        it?.let {
            action(it)
        }
    })
}

fun <T> LiveData<T>.nullableObserve(lifecycleOwner: LifecycleOwner, action: (t: T?) -> Unit) {
    this.observe(lifecycleOwner, Observer<T?> {
        action(it)
    })
}

fun <T> LiveData<T>.nonNullObserveWithContext(
    fragment: Fragment,
    action: (t: T, context: Context) -> Unit
) {
    this.observe(fragment, Observer<T> {
        it?.let {
            fragment.context?.let { cont ->
                action(it, cont)
            }

        }
    })
}

fun <T> LiveData<T>.nonNullObserveConsume(owner: LifecycleOwner, action: (t: T) -> Unit) {
    this.observe(owner, Observer {
        it?.let {
            action(it)
            this.postValue(null)
        }
    })
}

fun <T> LiveData<T>.nonNullObserveOnce(owner: LifecycleOwner, observer: (t: T) -> Unit) {
    this.observe(owner, object : Observer<T> {
        override fun onChanged(t: T?) {
            t?.let {
                removeObserver(this)
                observer(it)
            }
        }
    })
}

fun <S, R> LiveData<S>.map(valueWhenNull: R? = null, mapAction: S.() -> R?): LiveData<R> =
    Transformations.map(this) {
        it?.let { eltSource ->
            eltSource.mapAction()
        } ?: valueWhenNull
    }

fun <S, R> LiveData<S>.mapNotResendingSameValue(
    valueWhenNull: R? = null,
    mapAction: S.() -> R
): LiveData<R> =
    MediatorStateLiveData<R>().apply {
        addSource(this@mapNotResendingSameValue) {
            it?.let { eltSource ->
                postValue(eltSource.mapAction())
            } ?: postValue(valueWhenNull)
        }
    }


fun <T, U, W> combineLatestNonNull(
    liveData1: LiveData<T>,
    liveData2: LiveData<U>,
    map: ((t: T, u: U) -> W?)
): MutableLiveData<W> {
    return MediatorLiveData<W>().apply {
        fun emitIfCan() {
            val val1 = liveData1.value
            val val2 = liveData2.value
            if (val1 != null && val2 != null) {
                val newVal = map(val1, val2)
                this.postValue(newVal)
            }
        }

        addSource(liveData1) {
            emitIfCan()
        }
        addSource(liveData2) {
            emitIfCan()
        }

    }

}

fun <T, U, V, W> combineLatestNonNull(
    liveData1: LiveData<T>,
    liveData2: LiveData<U>,
    liveData3: LiveData<V>,
    map: ((t: T, u: U, v: V) -> W?)
): MutableLiveData<W> {
    return MediatorLiveData<W>().apply {
        fun emitIfCan() {
            val val1 = liveData1.value
            val val2 = liveData2.value
            val val3 = liveData3.value
            if (val1 != null && val2 != null && val3 != null) {
                val newVal = map(val1, val2, val3)
                if (this.value != newVal) {
                    this.postValue(newVal)
                }
            }
        }

        addSource(liveData1) {
            emitIfCan()
        }
        addSource(liveData2) {
            emitIfCan()
        }
        addSource(liveData3) {
            emitIfCan()
        }
    }
}


fun <T : Any, L : LiveData<T>> LifecycleOwner.observe(liveData: L, body: (T?) -> Unit) {
    liveData.observe(this, Observer(body))
}

fun <T : Any, L : LiveData<T>> LifecycleOwner.nonNullObserve(liveData: L, body: (T) -> Unit) {
    liveData.observe(this, Observer {
        it?.let { t -> body.invoke(t) }
    })
}

fun <T : Any, L : LiveData<T>> LifecycleOwner.nonNullObserveConsume(
    liveData: L,
    body: (T) -> Unit
) {
    liveData.observe(this, Observer {
        it?.let { t ->
            body.invoke(t)
            liveData.value = null
        }
    })
}
