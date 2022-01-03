package com.example.searchgithubuser.model.dispatcher

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

interface DefaultDispatcher {
    val dispatcher: CoroutineDispatcher
}

class DefaultDispatcherImpl : DefaultDispatcher {
    override val dispatcher = Dispatchers.Default
}

class TestDefaultDispatcherImpl(dispatcher: CoroutineDispatcher) : DefaultDispatcher {
    override val dispatcher = dispatcher
}
