
package com.example.android.guesstheword.screens.game

import android.os.CountDownTimer
import android.text.format.DateUtils
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel


class GameViewModel : ViewModel() {


    private val _word = MutableLiveData<String>()
    val word: LiveData<String>
        get() = _word


    private val _score = MutableLiveData<Int>()
    val score: LiveData<Int>
        get() = _score




    private lateinit var wordList: MutableList<String>

    private val _eventfinish = MutableLiveData<Boolean>()
    val eventfinish: LiveData<Boolean>
        get() = _eventfinish
    private val _currentTime=MutableLiveData<Long>()
    val currentTime:LiveData<Long>
        get() = _currentTime
    private val timer:CountDownTimer
    val currentTimeString = Transformations.map(currentTime) { time ->
        DateUtils.formatElapsedTime(time)
    }
    val wordHint=Transformations.map(word){word->
        val randomPosition=(1..word.length).random()
        "Current word has "+word.length+" letters"+"\nThe letter at position " + randomPosition + " is " +
                word.get(randomPosition - 1).toUpperCase()
    }


    private fun resetList() {
        wordList = mutableListOf(
            "queen",
            "hospital",
            "basketball",
            "cat",
            "change",
            "snail",
            "soup",
            "calendar",
            "sad",
            "desk",
            "guitar",
            "home",
            "railway",
            "zebra",
            "jelly",
            "car",
            "crow",
            "trade",
            "bag",
            "roll",
            "bubble"
        )
        wordList.shuffle()
    }

    init {
        _score.value = 0
        _word.value = ""
        Log.i("GameViewModel", "GameViewModel created!")
        resetList()
        nextWord()
        timer = object : CountDownTimer(COUNTDOWN_TIME, ONE_SECOND) {

            override fun onTick(millisUntilFinished: Long)
            {
                _currentTime.value = millisUntilFinished/ONE_SECOND
            }

            override fun onFinish() {
                _currentTime.value = DONE
                ongamefinish()
            }
        }

        timer.start()
    }


    override fun onCleared() {
        super.onCleared()

        timer.cancel()
    }




    fun onSkip() {
        _score.value = (score.value)?.minus(1)
        nextWord()
    }

    fun onCorrect() {
        _score.value = (score.value)?.plus(1)
        nextWord()
    }

    /** game complete event method **/
    fun ongamefinish() {
        _eventfinish.value = true
    }
    fun onGameFinishComplete() {
        _eventfinish.value = false
    }



    private fun nextWord() {
        if (wordList.isEmpty()) {
            resetList()
        } else {

            _word.value = wordList.removeAt(0)
        }
    }
    companion object {


        private const val DONE = 0L


        private const val ONE_SECOND = 1000L


        private const val COUNTDOWN_TIME = 60000L

    }
}