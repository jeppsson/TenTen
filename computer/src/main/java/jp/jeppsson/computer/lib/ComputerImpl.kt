package jp.jeppsson.computer.lib

import jp.jeppsson.computer.lib.Computer.Companion.CALL
import jp.jeppsson.computer.lib.Computer.Companion.MULT
import jp.jeppsson.computer.lib.Computer.Companion.PRINT
import jp.jeppsson.computer.lib.Computer.Companion.PUSH
import jp.jeppsson.computer.lib.Computer.Companion.RET
import jp.jeppsson.computer.lib.Computer.Companion.STOP
import java.lang.IllegalStateException

/**
 * Simple implementation of Computer.
 * @param stackSize the size of stack for this computer
 */
@ExperimentalUnsignedTypes
class ComputerImpl(
    stackSize: Int
) : Computer {

    private val stack: Stack = Stack(stackSize)

    private var pc: UInt = 0u // Program counter

    private var outputListener: Computer.OnOutputListener? = null
    private var listener: Computer.OnProgramCounterUpdated? = null

    override fun setOnProgramCounterUpdatedListener(listener: Computer.OnProgramCounterUpdated) {
        this.listener = listener
    }

    override fun setOnStackUpdatedListener(listener: Stack.OnStackUpdatedListener) {
        stack.setOnStackUpdatedListener(listener)
    }

    override fun setOnOutputListener(listener: Computer.OnOutputListener) {
        this.outputListener = listener
    }

    override fun setAddress(address: UShort): Computer {
        stack.setStackPointer(address)

        return this
    }

    override fun insert(opCode: UShort, data: UShort): Computer {
        stack.push(opCode.shl(16) + data)

        return this
    }

    override fun execute(): Int {
        while (true) {
            if (!executeOneTick()) {
                return 0
            }
        }
    }

    override fun executeOneTick(): Boolean {
        val instruction = stack.read(pc)
        val opCode = instruction.shr(16).toUShort()
        val data = instruction.toUShort().toUInt() // TODO: could be done nicer

        when (opCode) {
            PUSH -> {
                stack.push(data)

                pc++
            }
            PRINT -> {
                out = stack.pop()

                pc++
            }
            MULT -> {
                val multiplier = stack.pop()
                val multiplicand = stack.pop()
                val product = multiplier * multiplicand
                if (product >= UShort.MAX_VALUE) {
                    throw IllegalStateException("overflow!")
                }
                stack.push(multiplier * multiplicand)

                pc++
            }
            CALL -> pc = data
            RET -> pc = stack.pop()
            STOP -> return false
            else -> throw IllegalArgumentException()
        }

        listener?.onProgramCounterUpdated(pc)

        return true
    }

    private var out: UInt = 0u
        set(value) {
            outputListener?.print(value.toString())
            field = value
        }

    /** Shifts this value left by the [bitCount] number of bits. */
    private fun UShort.shl(bitCount: Int): UInt {
        return toUInt().shl(bitCount)
    }
}
