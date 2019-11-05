package com.example.notes

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.partial_note.view.*

class MainActivity : AppCompatActivity() {

    var notesList = ArrayList<Note>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Toast.makeText(this, "onCreate", Toast.LENGTH_SHORT).show()
        LoadQuery("%")
    }

    override fun onResume() {
        super.onResume()
        Toast.makeText(this, "onResume", Toast.LENGTH_SHORT).show()
        LoadQuery("%")
    }

    override fun onStart() {
        super.onStart()
        Toast.makeText(this, "onStart", Toast.LENGTH_SHORT).show()
    }

    override fun onStop() {
        super.onStop()
        Toast.makeText(this, "onStop", Toast.LENGTH_SHORT).show()
    }

    override fun onRestart() {
        super.onRestart()
        Toast.makeText(this, "onRestart", Toast.LENGTH_SHORT).show()
    }

    override fun onPause() {
        super.onPause()
        Toast.makeText(this, "onPause", Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        Toast.makeText(this, "onDestroy", Toast.LENGTH_SHORT).show()
    }

    fun LoadQuery(title: String) {
        var dbManager = DBManager(this)
        val projection = arrayOf("ID", "Title", "Description")
        val selectionArgs = arrayOf(title)
        val cursor = dbManager.query(projection, "Title like ?", selectionArgs, "Title")
        notesList.clear()
        if (cursor.moveToFirst()) {
            do {
                val ID = cursor.getInt(cursor.getColumnIndex("ID"))
                val Title = cursor.getString(cursor.getColumnIndex("Title"))
                val Description = cursor.getString(cursor.getColumnIndex("Description"))
                notesList.add(Note(ID, Title, Description))

            } while (cursor.moveToNext())
        }
        var NotesAdapter = NotesAdapter(notesList)
        listNotes.adapter = NotesAdapter
    }

    inner class NotesAdapter : BaseAdapter {
        var notesList = ArrayList<Note>()

        constructor(notesList: ArrayList<Note>) : super() {
            this.notesList = notesList
        }

        override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View? {
            val note = notesList[p0]
//            val myView = LayoutInflater.from(p2!!.context).inflate(R.layout.partial_note, null)
            val myView = layoutInflater.inflate(R.layout.partial_note, null)
            myView.txtTitle.text = note.name
            myView.txtContent.text = note.content

            myView.btnDelete.setOnClickListener {
                var dbManager = DBManager(this@MainActivity)
                val selectionArgs = arrayOf(note.id.toString())
                dbManager.delete("ID=?", selectionArgs)
                LoadQuery("%")
            }
            myView.btnEdit.setOnClickListener {
                val intent = Intent(this@MainActivity, AddNotes::class.java)
                intent.putExtra("ID", note.id)
                intent.putExtra("Title", note.name)
                intent.putExtra("Description", note.content)
                startActivity(intent)
            }

            return myView
        }

        override fun getItem(p0: Int): Any {
            return notesList[p0]
        }

        override fun getItemId(p0: Int): Long {
            return p0.toLong()
        }

        override fun getCount(): Int {
            return notesList.size
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)

        val sv = menu?.findItem(R.id.searchNote)?.actionView as SearchView
        val sm = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        sv.setSearchableInfo(sm.getSearchableInfo(componentName))
        sv.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                LoadQuery("%$p0%")
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                LoadQuery("%$p0%")
                return false
            }
        })
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.addNote -> {
                var intent = Intent(this, AddNotes::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
