package jp.jeppsson.computer

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import jp.jeppsson.computer.lib.Computer
import jp.jeppsson.computer.lib.Stack

@ExperimentalUnsignedTypes
class ComputerViewModel(private val computer: Computer) : ViewModel(),
    Computer.OnOutputListener, Computer.OnProgramCounterUpdated, Stack.OnStackUpdatedListener {

    private val stackMutable: MutableLiveData<List<Pair<UShort, UInt>>> = MutableLiveData()
    private val _stack: Array<Pair<UShort, UInt>> =
        Array(100) { i: Int -> Pair(i.toUShort(), 0u) }

    private val _sp: MutableLiveData<String> = MutableLiveData()
    private val _pc: MutableLiveData<String> = MutableLiveData()
    private val _output: MutableLiveData<String> = MutableLiveData()

    val stack: LiveData<List<Pair<UShort, UInt>>> = stackMutable
    val stackPointer: LiveData<String> = _sp
    val output: LiveData<String> = _output
    val programCounter: LiveData<String> = _pc

    init {
        computer.setOnOutputListener(this)
        computer.setOnProgramCounterUpdatedListener(this)
        computer.setOnStackUpdatedListener(this)
    }

    fun executeOneTick(view: View) {
        computer.executeOneTick()
    }

    override fun print(arg: String) {
        val old = _output.value ?: ""
        _output.postValue(old + '\n' + arg)
    }

    override fun onStackUpdated(address: UShort, data: UInt) {
        _stack[address.toInt()] = Pair(address, data)
        stackMutable.postValue(_stack.toList())
    }

    override fun onStackPointerUpdated(sp: UShort) {
        this._sp.postValue(sp.toString(16))
    }

    override fun onProgramCounterUpdated(pc: UInt) {
        this._pc.postValue(pc.toString(16))
    }

    class Factory(private val computer: Computer) : ViewModelProvider.Factory {

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ComputerViewModel::class.java)) {
                return ComputerViewModel(computer) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
