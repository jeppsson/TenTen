package jp.jeppsson.computer

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import jp.jeppsson.computer.lib.Computer

@ExperimentalUnsignedTypes
class StackAdapter :
    ListAdapter<Pair<UShort, UInt>, StackAdapter.StackItemViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): StackItemViewHolder {
        return StackItemViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.stack_entry, parent, false)
        )
    }

    class StackItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val txtAddress: TextView = view.findViewById(R.id.address)
        private val txtData: TextView = view.findViewById(R.id.data)
        private val txtEncoded: TextView = view.findViewById(R.id.encoded)

        fun bind(address: Int, data: UInt) {
            txtAddress.text = txtAddress.context.getString(R.string.hex_32).format(address)
            txtData.text = txtData.context.getString(R.string.hex_32).format(data.toInt())
            txtEncoded.text = txtData.context.getString(R.string.hex_16)
                .format(opCodeToString(data.shr(16).toUShort()), data.toUShort().toInt())
        }

        private fun opCodeToString(opCode: UShort): String {
            return when (opCode) {
                Computer.PUSH -> "PUSH "
                Computer.PRINT -> "PRINT"
                Computer.MULT -> "MULT "
                Computer.CALL -> "CALL "
                Computer.RET -> "RET    "
                Computer.STOP -> "STOP "
                else -> "?         "
            }
        }
    }

    override fun onBindViewHolder(holder: StackItemViewHolder, position: Int) {
        holder.bind(position, getItem(position).second)
    }

    private class DiffCallback : DiffUtil.ItemCallback<Pair<UShort, UInt>>() {
        override fun areItemsTheSame(
            oldItem: Pair<UShort, UInt>,
            newItem: Pair<UShort, UInt>
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: Pair<UShort, UInt>,
            newItem: Pair<UShort, UInt>
        ): Boolean {
            return oldItem.second == newItem.second
        }
    }
}