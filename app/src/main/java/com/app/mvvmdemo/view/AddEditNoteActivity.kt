package com.app.mvvmdemo.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.app.mvvmdemo.R
import kotlinx.android.synthetic.main.activity_add_note.*
import org.jetbrains.anko.toast

class AddEditNoteActivity : AppCompatActivity() {

    companion object {
        val EXTRA_ID = "com.app.mvvmdemo.view.EXTRA_ID"
        val EXTRA_TITLE = "com.app.mvvmdemo.view.EXTRA_TITLE"
        val EXTRA_DESCRIPTION = "com.app.mvvmdemo.view.EXTRA_DESCRIPTION"
        val EXTRA_PRIORITY = "com.app.mvvmdemo.view.EXTRA_PRIORITY"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_note)

        init()
    }

    private fun init() {
        np.minValue = 1
        np.maxValue = 20

        actionBar?.setHomeAsUpIndicator(R.drawable.icon_cross)
        if (intent?.hasExtra(EXTRA_ID)!!) {
            title = "Edit Note"
            et_title.setText(intent?.extras?.getString(EXTRA_TITLE))
            et_description.setText(intent?.extras?.getString(EXTRA_DESCRIPTION))
            np.value = intent?.extras?.getInt(EXTRA_PRIORITY, 1)!!
        } else {
            title = "Add Note"
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.add_note, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.menu_save -> {
                saveNote()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun saveNote() {
        val title = et_title.text.toString()
        val desc = et_description.text.toString()
        val priority = np.value

        if ((title.trim().isEmpty()) || (desc.trim().isEmpty())) {
            toast("Fields must not be empty!!")
            return
        }

        val intent = Intent()
        intent.putExtra(EXTRA_TITLE, title)
        intent.putExtra(EXTRA_DESCRIPTION, desc)
        intent.putExtra(EXTRA_PRIORITY, priority)

        val id = this.intent?.extras?.getInt(EXTRA_ID, -1)
        if(id != -1) {
            intent.putExtra(EXTRA_ID, id)
        }

        setResult(Activity.RESULT_OK, intent)
        finish()
    }
}
