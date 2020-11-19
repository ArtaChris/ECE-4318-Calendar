package edu.cpp4310.calclkrem.ui.calendar

import android.os.Build
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.annotation.StringRes
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import edu.cpp4310.calclkrem.databinding.HomeOptionsItemViewBinding
import edu.cpp4310.calclkrem.R

data class ExampleItem(
    @StringRes val titleRes: Int,
    val createView: () -> BaseFragment
)

class HomeOptionsAdapter(val onClick: (ExampleItem) -> Unit) :
    RecyclerView.Adapter<HomeOptionsAdapter.HomeOptionsViewHolder>() {

    @RequiresApi(Build.VERSION_CODES.O)
    val examples = listOf(
        ExampleItem(R.string.calendar_title) { CalendarFragment() },
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeOptionsViewHolder {
        return HomeOptionsViewHolder(
            HomeOptionsItemViewBinding.inflate(parent.context.layoutInflater, parent, false)
        )
    }

    override fun onBindViewHolder(viewHolder: HomeOptionsViewHolder, position: Int) {
        viewHolder.bind(examples[position])
    }

    override fun getItemCount(): Int = examples.size

    inner class HomeOptionsViewHolder(private val binding: HomeOptionsItemViewBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
            itemView.setOnClickListener {
                onClick(examples[bindingAdapterPosition])
            }
        }

        fun bind(item: ExampleItem) {
            val context = itemView.context
            binding.itemOptionTitle.apply {
                text = if (item.titleRes != 0) context.getString(item.titleRes) else null
                isVisible = text.isNotBlank()
            }
        }
    }
}
