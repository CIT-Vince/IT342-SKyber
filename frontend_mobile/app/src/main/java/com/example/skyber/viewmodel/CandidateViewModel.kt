package com.example.skyber.viewmodel
/*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.skyber.dataclass.CandidateProfile
import com.example.skyber.repository.CandidateRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File

class CandidateViewModel : ViewModel() {
    private val repository = CandidateRepository()

    private val _candidates = MutableStateFlow<List<CandidateProfile>>(emptyList())
    val candidates: StateFlow<List<CandidateProfile>> = _candidates.asStateFlow()

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    fun refreshCandidates() {
        viewModelScope.launch {
            _loading.value = true
            try {
                repository.getAllCandidates()
                    .onSuccess { candidates ->
                        _candidates.value = candidates
                        _error.value = null
                    }
                    .onFailure { exception ->
                        _error.value = exception.message
                    }
            } finally {
                _loading.value = false
            }
        }
    }

    fun createCandidateWithImage(
        firstName: String,
        lastName: String,
        age: String?,
        address: String?,
        partylist: String?,
        platform: String?,
        imageFile: File?
    ) {
        viewModelScope.launch {
            _loading.value = true
            try {
                repository.createCandidateWithImage(
                    firstName, lastName, age, address, partylist, platform, imageFile
                ).onSuccess {
                    refreshCandidates()
                    _error.value = null
                }.onFailure { exception ->
                    _error.value = exception.message
                }
            } finally {
                _loading.value = false
            }
        }
    }

    fun updateCandidate(id: String, candidate: CandidateProfile) {
        viewModelScope.launch {
            _loading.value = true
            try {
                repository.updateCandidate(id, candidate)
                    .onSuccess {
                        refreshCandidates()
                        _error.value = null
                    }
                    .onFailure { exception ->
                        _error.value = exception.message
                    }
            } finally {
                _loading.value = false
            }
        }
    }

    fun deleteCandidate(id: String) {
        viewModelScope.launch {
            _loading.value = true
            try {
                repository.deleteCandidate(id)
                    .onSuccess {
                        refreshCandidates()
                        _error.value = null
                    }
                    .onFailure { exception ->
                        _error.value = exception.message
                    }
            } finally {
                _loading.value = false
            }
        }
    }
}

 */