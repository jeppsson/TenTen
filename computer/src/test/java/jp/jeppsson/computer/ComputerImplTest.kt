package jp.jeppsson.computer

import jp.jeppsson.computer.lib.ComputerImpl
import jp.jeppsson.computer.lib.Computer
import org.junit.Test
import org.mockito.Mockito.*

@ExperimentalUnsignedTypes
class ComputerImplTest {

    @Test
    fun simplePrint() {
        val computer = ComputerImpl(100)

        val listener = mock(Computer.OnOutputListener::class.java)
        computer.setOnOutputListener(listener)

        computer.insert(Computer.PUSH, 1009u)
            .insert(Computer.PRINT)
            .insert(Computer.STOP)
            .execute()

        verify(listener, only()).print("1009")
    }
}
