package jp.jeppsson.computer.lib

/**
 * Simple computer definition.
 */
@ExperimentalUnsignedTypes
interface Computer {

    /**
     * Interface for listening to program counter updates.
     */
    interface OnProgramCounterUpdated {
        /**
         * Called when program counter is updated.
         * @param pc new value of program counter
         */
        fun onProgramCounterUpdated(pc: UInt)
    }

    /**
     * Interface for listening to output.
     */
    interface OnOutputListener {

        /**
         * Called when output is available.
         * @param arg the output
         */
        fun print(arg: String)
    }

    /**
     * Supported operations.
     */
    companion object {
        /**
         * Multiples the top elements on stack.
         */
        const val MULT: UShort = 1u

        /**
         * Sends the top element on stack to output.
         */
        const val PRINT: UShort = 2u

        /**
         * Returns to the address on top element of stack.
         */
        const val RET: UShort = 3u

        /**
         * Pushes the data to stack.
         */
        const val PUSH: UShort = 4u

        /**
         * Branches to address specified by data.
         */
        const val CALL: UShort = 5u

        /**
         * Stops this machine.
         */
        const val STOP: UShort = 6u
    }

    /**
     * Sets the stack pointer to specified address.
     * @param address the address
     * @return this computer
     */
    fun setAddress(address: UShort): Computer

    /**
     * Inserts an instruction at top of stack.
     * @param opCode operation code
     * @param data data field
     * @return this computer
     */
    fun insert(opCode: UShort, data: UShort = 0u): Computer

    /**
     * Executes the loaded program.
     * @return 0 on success, negative value on error
     */
    fun execute(): Int

    /**
     * Executes on instruction.
     * @return true if computer is not halted, false otherwise
     */
    fun executeOneTick(): Boolean

    /**
     * Registers listener for program counter updates.
     * @param listener listener that will be called on program counter updates
     */
    fun setOnProgramCounterUpdatedListener(listener: OnProgramCounterUpdated)

    /**
     * Registers listener for stack updates.
     * @param listener listener that will be called on stack updates
     */
    fun setOnStackUpdatedListener(listener: Stack.OnStackUpdatedListener)

    /**
     * Registers listener for outputs.
     * @param listener listener that will be called when output is available
     */
    fun setOnOutputListener(listener: OnOutputListener)
}
