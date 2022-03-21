package sharing.file.ui.screens.edit

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import sharing.file.base.ViewModel
import sharing.file.data.Resource
import sharing.file.data.model.Document
import sharing.file.data.repository.DocumentRepository
import sharing.file.data.repository.UserRepository

class EditDocumentViewModel(path: String?) : ViewModel() {
    private val documentRepository = DocumentRepository
    private val userRepository = UserRepository

    private val _document = MutableStateFlow<Resource<Document>>(Resource.LoadingResource())
    val document = _document.asStateFlow()

    private val _result = MutableStateFlow<Resource<Any>>(Resource.SuccessResource(Any()))
    val result = _result.asStateFlow()

    init {
        viewModelScope.launch {
            _document.emit(path?.let { documentRepository.openDocument(it) } ?: createNewDocument())
        }
    }

    fun save(text: String, path: String) {
        viewModelScope.launch {
            _result.emit(Resource.LoadingResource())
            _result.emit(DocumentRepository.saveDocument(text, path))
        }
    }

    private fun createNewDocument(): Resource<Document> {
        val user = userRepository.user.value.data ?: return Resource.FailedResource(Throwable("empty user"))
        return Resource.SuccessResource(
            Document(
                0,
                0,
                user.name,
                ByteArray(0),
                text = ""
            ).apply {
                verify = Document.Verify.Empty
            }
        )
    }
}