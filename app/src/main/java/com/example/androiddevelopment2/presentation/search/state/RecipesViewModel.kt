package com.example.androiddevelopment2.presentation.search.state


// Состояния экранов
//sealed class SearchScreenState {
//    object Idle : SearchScreenState()
//    object Loading : SearchScreenState()
//    data class Success(val recipes: List<RecipeModel>) : SearchScreenState()
//    data class Failure(val reason: FailureReason) : SearchScreenState()
//}

// Причины ошибок
//enum class FailureReason {
//    EmptyInput,
//    NoRecipesFound,
//    RecipeNotFound,
//    InvalidApiKey,
//    NoInternet,
//    UnknownError
//}
//@HiltViewModel
//class SearchViewModel @Inject constructor(
//    private val searchRecipesUseCase: SearchRecipesUseCase
//) : ViewModel() {
//    private val _state = MutableStateFlow<SearchState>(SearchState.Idle)
//    val state: StateFlow<SearchState> = _state
//
//    fun searchRecipes(ingredients: String) {
//        viewModelScope.launch {
//            _state.value = SearchState.Loading
//            _state.value = when (val result = searchRecipesUseCase(ingredients)) {
//                is Result.Success -> SearchState.Success(result.data)
//                is Result.Error -> SearchState.Error(result.message ?: "Unknown error")
//            }
//        }
//    }
//}
//
//sealed class SearchState {
//    object Idle : SearchState()
//    object Loading : SearchState()
//    data class Success(val recipes: List<RecipeModel>) : SearchState()
//    data class Error(val message: String) : SearchState()
//}