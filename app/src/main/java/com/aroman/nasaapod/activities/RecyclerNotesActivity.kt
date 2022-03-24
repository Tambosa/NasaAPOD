package com.aroman.nasaapod.activities

import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import com.aroman.nasaapod.R
import com.aroman.nasaapod.recyclerData.*
import kotlinx.android.synthetic.main.activity_recycler.*
import kotlinx.android.synthetic.main.recycler_view_item.view.*

class RecyclerNotesActivity : AppCompatActivity() {

    private lateinit var adapter: RecyclerNotesActivityAdapter
    private var notesList: MutableList<Pair<NoteData, Boolean>> =
        mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycler)

        adapter = RecyclerNotesActivityAdapter(
            this@RecyclerNotesActivity,
            object : OnListItemClickListener {
                override fun onItemClick(
                    notesList: MutableList<Pair<NoteData, Boolean>>,
                    position: Int
                ) {
                    notesList[position] = notesList[position].let {
                        it.first to !it.second
                    }
                    adapter.notifyItemChanged(position)
                }
            }, notesList
        )

        ItemTouchHelper(ItemTouchHelperCallback(adapter)).attachToRecyclerView(recycler_view)

        recycler_view.adapter = adapter
        val divider = DividerItemDecoration(this, LinearLayout.VERTICAL)
        recycler_view.addItemDecoration(divider)

        recycler_activity_fab.setOnClickListener {
            adapter.addItem()
        }
    }
}

