package com.app.mvvmdemo.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.LEFT
import androidx.recyclerview.widget.ItemTouchHelper.RIGHT
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.mvvmdemo.R
import com.app.mvvmdemo.model.Note
import com.app.mvvmdemo.view.adapter.NoteAdapter
import com.app.mvvmdemo.viewmodel.NoteViewModel
import com.app.mvvmdemo.viewmodel.factory.NoteViewModelFactory
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.toast

class MainActivity : AppCompatActivity(), NoteAdapter.OnItemClickListener {

    companion object {
        private val REQUEST_ADD_NOTE = 99
        private val REQUEST_EDIT_NOTE = 100
    }

    private lateinit var noteViewModel: NoteViewModel
    private lateinit var noteAdapter: NoteAdapter
    private var notes = ArrayList<Note>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        init()
    }

    private fun init() {
        noteViewModel = ViewModelProvider(
            this,
            NoteViewModelFactory(application)
        ).get(NoteViewModel::class.java)
        noteViewModel.getAllNotes()?.observe(this,
            Observer<List<Note>> {
                noteAdapter.submitList(it)
            })

        noteAdapter = NoteAdapter(this)

        initViews()
        initListeners()
    }

    private fun initViews() {
        rv.apply {
            adapter = noteAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
        }

        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
            0,
            LEFT or RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                noteViewModel.deleteNote(noteAdapter.getNoteAt(viewHolder.adapterPosition))
                toast("Note Deleted!!")
            }
        }).attachToRecyclerView(rv)
    }

    private fun initListeners() {
        fab.setOnClickListener {
            startActivityForResult(Intent(this, AddEditNoteActivity::class.java), REQUEST_ADD_NOTE)
        }
        noteAdapter.setItemClickListener(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_ADD_NOTE && resultCode == Activity.RESULT_OK) {
            val title = data?.extras?.getString(AddEditNoteActivity.EXTRA_TITLE)
            val desc = data?.extras?.getString(AddEditNoteActivity.EXTRA_DESCRIPTION)
            val priority = data?.extras?.getInt(AddEditNoteActivity.EXTRA_PRIORITY)

            noteViewModel.insertNote(Note(title!!, desc!!, priority!!))
            toast("Note Added!!")
        } else if (requestCode == REQUEST_EDIT_NOTE && resultCode == Activity.RESULT_OK) {
            val id = data?.extras?.getInt(AddEditNoteActivity.EXTRA_ID, -1)
            if (id == -1) {
                toast("Note can't be updated!!")
                return
            }
            val title = data?.extras?.getString(AddEditNoteActivity.EXTRA_TITLE)
            val desc = data?.extras?.getString(AddEditNoteActivity.EXTRA_DESCRIPTION)
            val priority = data?.extras?.getInt(AddEditNoteActivity.EXTRA_PRIORITY)

            val note = Note(title!!, desc!!, priority!!)
            note.id = id!!
            noteViewModel.updateNote(note)
            toast("Note Updated!!")
        } else {
            toast("Note Added Failed!!")
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.menu_delete_all -> {
                noteViewModel.deleteAllNotes()
                toast("All Notes Deleted!!")
                true
            }
            else ->
                super.onOptionsItemSelected(item)
        }
    }

    override fun onItemClick(note: Note) {
        val intent = Intent(this, AddEditNoteActivity::class.java)
        intent.putExtra(AddEditNoteActivity.EXTRA_ID, note.id)
        intent.putExtra(AddEditNoteActivity.EXTRA_TITLE, note.title)
        intent.putExtra(AddEditNoteActivity.EXTRA_DESCRIPTION, note.description)
        intent.putExtra(AddEditNoteActivity.EXTRA_PRIORITY, note.priority)
        startActivityForResult(intent, REQUEST_EDIT_NOTE)
    }
}
