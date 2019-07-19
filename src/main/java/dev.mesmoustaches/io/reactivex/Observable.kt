//package io.reactivex
//
//import io.reactivex.functions.BiFunction
//import java.util.concurrent.TimeUnit
//
//fun <T> Observable<T>.delayEach(interval: Long, timeUnit: TimeUnit): Observable<T> =
//        Observable.zip(this, Observable.interval(interval, timeUnit), BiFunction { item:T, _: Long -> item })