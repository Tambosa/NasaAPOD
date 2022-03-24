package com.aroman.nasaapod.recyclerData

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.aroman.nasaapod.R
import kotlinx.android.synthetic.main.recycler_view_item.view.*
import java.text.SimpleDateFormat

class RecyclerNotesActivityAdapter(
    context: Context,
    private val onListItemClickListener: OnListItemClickListener,
    private var notesList: MutableList<Pair<NoteData, Boolean>>
) : RecyclerView.Adapter<RecyclerNotesActivityAdapter.NoteViewHolder>(), ItemTouchHelperAdapter {

    val simpleDateFormat = SimpleDateFormat("HH:mm dd-MM")
    val animWobble = AnimationUtils.loadAnimation(context, R.anim.wobble)
    var isAnimated = false

    inner class NoteViewHolder(view: View) : RecyclerView.ViewHolder(view),
        ItemTouchHelperViewHolder {
        fun bind(note: Pair<NoteData, Boolean>) {
            if (layoutPosition != RecyclerView.NO_POSITION) {
                itemView.recycler_item_content.text = note.first.content
                itemView.recycler_item_date.text =
                    simpleDateFormat.format(note.first.date)
                itemView.setOnClickListener {
                    onListItemClickListener.onItemClick(notesList, layoutPosition)
                }
                if (isAnimated) itemView.startAnimation(animWobble)
                else itemView.clearAnimation()

                itemView.recycler_button_delete.setOnClickListener { removeItem() }
                itemView.recycler_button_move_up.setOnClickListener { moveItemUp() }
                itemView.recycler_button_move_down.setOnClickListener { moveItemDown() }

                itemView.recycler_edit_text.visibility =
                    if (note.second) View.VISIBLE else View.GONE
                itemView.recycler_btn_save.visibility =
                    if (note.second) View.VISIBLE else View.GONE
                itemView.recycler_btn_save.setOnClickListener {
                    note.first.content = itemView.recycler_edit_text.text.toString()
                    notesList[layoutPosition] = notesList[layoutPosition].let { pair ->
                        pair.first to !pair.second
                    }
                    notifyItemChanged(layoutPosition)
                }

                itemView.setOnLongClickListener {
                    isAnimated = true
                    notifyItemChanged(layoutPosition)
                    true
                }
            }
        }

        private fun removeItem() {
            notesList.removeAt(layoutPosition)
            notifyItemRemoved(layoutPosition)
        }

        private fun moveItemUp() {
            layoutPosition.takeIf { it > 0 }?.also { position ->
                notesList.removeAt(position).apply {
                    notesList.add(position - 1, this)
                }
                notifyItemMoved(position, position - 1)
            }
        }

        private fun moveItemDown() {
            layoutPosition.takeIf { it < notesList.size - 1 }?.also { position ->
                notesList.removeAt(position).apply {
                    notesList.add(position + 1, this)
                }
                notifyItemMoved(position, position + 1)
            }
        }

        override fun onItemSelected() {
            itemView.setBackgroundColor(Color.LTGRAY)
        }

        override fun onItemCleared() {
            isAnimated = false
            itemView.setBackgroundColor(Color.WHITE)
            notifyItemChanged(layoutPosition)
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return NoteViewHolder(
            inflater.inflate(
                R.layout.recycler_view_item,
                parent,
                false
            ) as View
        )
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        holder.bind(notesList[position])
    }

    override fun getItemCount() = notesList.size


    fun addItem() {
        notesList.add(Pair(NoteData("Enter new task"), true))
        notifyItemInserted(notesList.size - 1)
    }

    override fun onItemMove(fromPosition: Int, toPosition: Int) {
        notesList.removeAt(fromPosition).apply {
            notesList.add(if (toPosition > fromPosition) toPosition - 1 else toPosition + 1, this)
        }
        notifyItemMoved(fromPosition, toPosition)
    }

    override fun onItemDismiss(position: Int) {
        notesList.removeAt(position)
        notifyItemRemoved(position)
    }
}