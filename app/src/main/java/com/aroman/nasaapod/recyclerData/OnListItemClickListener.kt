package com.aroman.nasaapod.recyclerData

interface OnListItemClickListener {
    fun onItemClick(notesList: MutableList<Pair<NoteData, Boolean>>, position: Int)
}