package jp.jeppsson.computer.lib

/**
 * Simple implementation of a stack.
 * @param stackSize the size of stack
 */
@ExperimentalUnsignedTypes
class Stack(stackSize: Int) {

    private val stack: UIntArray = UIntArray(stackSize) { 0u }
    private var sp: UShort = 0u

    private var listener: OnStackUpdatedListener? = null

    /**
     * Interface for listening to stack updates.
     */
    interface OnStackUpdatedListener {
        /**
         * Called when data on stack has been updated.
         * @param address address of the updated element
         * @param data data of the updated element
         */
        fun onStackUpdated(address: UShort, data: UInt)

        /**
         * Called when stack pointer is updated.
         * @param sp new value of stack pointer
         */
        fun onStackPointerUpdated(sp: UShort)
    }

    /**
     * Registers listener for stack updates.
     * @param listener listener that will be called on stack updated
     */
    fun setOnStackUpdatedListener(listener: OnStackUpdatedListener) {
        this.listener = listener
    }

    /**
     * Pushes to top of stack.
     * @param data the data to push
     */
    fun push(data: UInt) {
        stack[sp.toInt()] = data
        listener?.onStackUpdated(sp, data)

        sp++

        if (sp >= stack.size.toUInt()) {
            throw IllegalStateException("Stack overflow")
        }

        listener?.onStackPointerUpdated(sp)
    }

    /**
     * Pops top of stack
     * @return the data on top of stack
     */
    fun pop(): UInt {
        sp--

        if (sp >= stack.size.toUInt()) {
            throw IllegalStateException("Stack underflow")
        }


        listener?.onStackPointerUpdated(sp)

        return stack[sp.toInt()]
    }

    /**
     * Reads from stack at an address
     * @param address the address to read from
     * @return the data at specified address
     */
    fun read(address: UInt): UInt {
        return stack[address.toInt()]
    }

    /**
     * Sets the stack pointer to an address
     * @param address the address to set stack pointer to
     */
    fun setStackPointer(address: UShort) {
        sp = address
    }
}