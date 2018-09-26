package com.rahul.`in`.androidroomwithview

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import kotlinx.coroutines.experimental.*
import kotlinx.coroutines.experimental.android.Main
import kotlin.coroutines.experimental.CoroutineContext

class WordViewModel(application: Application) : AndroidViewModel(application){
    private val repository:WordRepository
    val allWords: LiveData<List<Word>>
    private var parentJob = Job()
    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Main
    private val scope = CoroutineScope(coroutineContext)

    init {
        val wordDao = WordRoomDatabase.getDatabase(application,scope).wordDao()
        repository = WordRepository(wordDao)
        allWords = repository.allWords
    }

    fun insert(word:Word) = scope.launch(Dispatchers.IO) { repository.insert(word) }

    fun removeWord(word:Word) = scope.launch(Dispatchers.IO) { repository.deleteWord(word) }

    override fun onCleared() {
        super.onCleared()
        parentJob.cancel()
    }

}
