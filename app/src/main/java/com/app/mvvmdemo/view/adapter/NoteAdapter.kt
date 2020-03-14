package com.app.mvvmdemo.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.app.mvvmdemo.R
import com.app.mvvmdemo.model.Note
import kotlinx.android.synthetic.main.note_row.view.*

class NoteAdapter(private val context: Context) :
    ListAdapter<Note, NoteAdapter.NoteHolder>(DIFF_CALLBACK) {
    private lateinit var listener: OnItemClickListener

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Note>() {
            override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
                return (oldItem.title == newItem.title) and
                        (oldItem.description == newItem.description) and
                        (oldItem.priority == newItem.priority)
            }
        }
    }

    fun getNoteAt(index: Int): Note {
        return getItem(index)
    }

    fun setItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    class NoteHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteHolder {
        return NoteHolder(
            LayoutInflater
                .from(context)
                .inflate(
                    R.layout.note_row,
                    parent,
                    false
                )
        )
    }

    override fun onBindViewHolder(holder: NoteHolder, position: Int) {
        val note = getItem(position)

        holder.itemView.apply {
            tv_title.text = note.title
            tv_priority.text = note.priority.toString()
            tv_description.text = note.description

            setOnClickListener {
                if (position != RecyclerView.NO_POSITION)
                    listener!!.onItemClick(note)
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(note: Note)
    }
}