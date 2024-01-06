package com.example.lockapp.notes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lockapp.R
import com.example.lockapp.selectApps.data.AppListData
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class NotesViewmodel: ViewModel() {

    private var _selectedApp = MutableSharedFlow<AppListData>(1)
    val selectedApp: SharedFlow<AppListData> = _selectedApp.asSharedFlow()

    private var noteSubject = ""
    private var noteContent = ""


    fun setSelectedAppsList(appData: AppListData) {
        viewModelScope.launch {
            _selectedApp.emit(appData)
        }
    }

    fun getNoteSubject(): String {
        return noteSubject
    }

    fun setNoteSubject(sub: String) {
        noteSubject = sub
    }

    fun setNoteContent(content: String) {
        noteContent = content
    }

    fun getNoteContent(): String {
        return noteContent
    }

}