package jp.jeppsson.computer

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import jp.jeppsson.computer.databinding.ActivityMainBinding
import jp.jeppsson.computer.lib.Computer.Companion.CALL
import jp.jeppsson.computer.lib.Computer.Companion.MULT
import jp.jeppsson.computer.lib.Computer.Companion.PRINT
import jp.jeppsson.computer.lib.Computer.Companion.PUSH
import jp.jeppsson.computer.lib.Computer.Companion.RET
import jp.jeppsson.computer.lib.Computer.Companion.STOP
import jp.jeppsson.computer.lib.ComputerImpl

@ExperimentalUnsignedTypes
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Create new computer with a stack of 100 addresses
        val computer = ComputerImpl(100)

        // Create view model for out computer
        val model =
            ViewModelProviders.of(this, ComputerViewModel.Factory(computer))
                .get(ComputerViewModel::class.java)

        // Android data binding
        val binding: ActivityMainBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this
        binding.viewmodel = model

        findViewById<RecyclerView>(R.id.stack).adapter = StackAdapter()

        // Instructions for the print_tenten function
        computer.setAddress(PRINT_TENTEN_BEGIN).insert(MULT).insert(PRINT).insert(RET)

        // The start of the main function
        computer.setAddress(MAIN_BEGIN).insert(PUSH, 1009u).insert(PRINT)

        // Return address for when print_tenten function finishes
        computer.insert(PUSH, 6u)

        // Setup arguments and call print_tenten
        computer.insert(PUSH, 101u).insert(PUSH, 10u).insert(CALL, PRINT_TENTEN_BEGIN)

        // Stop the program
        computer.insert(STOP)
        computer.setAddress(53u)
    }

    companion object {
        private const val PRINT_TENTEN_BEGIN: UShort = 50u
        private const val MAIN_BEGIN: UShort = 0u
    }
}

@ExperimentalUnsignedTypes
@BindingAdapter("submitStack")
fun submitStack(recyclerView: RecyclerView, list: List<Pair<UShort, UInt>>?) {
    if (list != null) {
        (recyclerView.adapter as StackAdapter).submitList(list)
    }
}
