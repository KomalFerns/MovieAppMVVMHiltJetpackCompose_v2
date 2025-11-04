# Benedict Cumberbatch Movie App

## Overview

This is a mobile application that displays movies starring Benedict Cumberbatch using the TMDB (The Movie Database) API. The app demonstrates a hybrid Android architecture using both XML layouts and Jetpack Compose, following MVVM (Model-View-ViewModel) architecture pattern.

This project was created as part of the TELUS Health Virtual Care Mobile Engineering Challenge.

## Features

### User Story 1: Movie List Screen (XML-based)
- Displays a list of all movies starring Benedict Cumberbatch
- Each list item shows:
  - Movie poster thumbnail
  - Movie title
- Handles loading states and network errors gracefully with appropriate user feedback
- Provides retry functionality for failed requests

### User Story 2: Movie Detail Screen (Jetpack Compose)
- Displays detailed information for a selected movie:
  - Movie title
  - Movie poster (full-size)
  - Movie synopsis (overview)
- Navigates from the XML list screen to the Compose detail screen seamlessly

### User Story 3: Similar Movies (Optional - Implemented)
- Displays a horizontal list of similar movies on the detail screen
- Allows navigation to similar movies by tapping on them
- Demonstrates recursive navigation within the same Compose screen

## Setup Instructions

### Prerequisites
1. Android Studio Hedgehog (2023.1.1) or later
2. JDK 17 or later
3. TMDB API key - Get one by:
   - Creating an account at [https://www.themoviedb.org/](https://www.themoviedb.org/)
   - Going to Settings > API > Request API key
   - Copying your API key

### Configuration

1. **Clone or Extract the Project**
   - Extract the project to a folder
   - Open Android Studio and select "Open an Existing Project"
   - Navigate to the project folder and select it

2. **Add TMDB API Key**
   - Open `local.properties` in the project root (create it if it doesn't exist)
   - Add your TMDB API key:
     ```
     tmdb.api.key=YOUR_API_KEY_HERE
     ```
   
   **Note:** The `local.properties` file is typically git-ignored, so it won't be committed to version control.

3. **Sync and Build**
   - Android Studio will automatically sync Gradle files
   - Wait for the sync to complete
   - Build the project (Build > Make Project)

4. **Run the App**
   - Connect an Android device or start an emulator
   - Click Run (or press Shift+F10)
   - The app will launch and display the movie list

## Architecture Decisions

### Hybrid Architecture (XML + Compose)

The app demonstrates a hybrid architecture approach:

1. **XML for List Screen** (`MovieListFragment`):
   - Uses traditional XML layouts with RecyclerView
   - Demonstrates legacy XML codebase integration
   - Provides better performance for simple list displays

2. **Jetpack Compose for Detail Screen** (`MovieDetailFragment`):
   - Uses modern Jetpack Compose for the detail view
   - Demonstrates modern UI development practices
   - Better for complex, dynamic layouts
   - Seamless integration with XML screens via Navigation Component

### MVVM Architecture Pattern

The app follows the MVVM (Model-View-ViewModel) architecture pattern:

- **Model**: Data models (`Movie`, `MovieResponse`) and Repository for data access
- **View**: UI components (XML layouts and Compose screens)
- **ViewModel**: Manages UI-related data and business logic, survives configuration changes

#### Architecture Layers

1. **Data Layer**
   - `MovieRemoteDataSource`: Handles API calls to TMDB
   - `MovieRepository`: Abstracts data sources and provides business logic
   - Data models are clean and lean (no UI dependencies)

2. **Domain Layer**
   - Business logic encapsulated in the Repository
   - Clean separation from UI concerns

3. **UI Layer**
   - **XML Screen**: `MovieListViewModel` (standard ViewModel with LiveData)
   - **Compose Screen**: `MovieDetailViewModel` (ViewModel with StateFlow)
   - ViewModels expose observable state to Views
   - Views observe ViewModel state and update UI accordingly

### State Management

- **XML Fragment**: Uses LiveData in ViewModel, observed by Fragment
- **Compose Screen**: Uses StateFlow in ViewModel, collected using `collectAsState()`
- State is immutable and represents the current UI state
- ViewModels handle all business logic and data transformation

### Navigation

- Uses Jetpack Navigation Component with Safe Args
- Navigation graph: `app/src/main/res/navigation/movie.xml`
- Type-safe navigation between XML and Compose screens
- Supports recursive navigation for similar movies (Detail → Detail)

### Dependency Injection

- Uses Dagger Hilt for dependency injection
- `MovieModule` provides all movie-related dependencies
- Singleton scope for repositories and data sources

## Libraries Used

### Core Libraries
1. **Retrofit** (`squareup-retrofit`)
   - **Why**: Industry-standard for HTTP networking in Android
   - **Usage**: API service interface for TMDB endpoints

2. **Gson** (`squareup-converter-gson`)
   - **Why**: JSON serialization/deserialization for Retrofit
   - **Usage**: Converting API responses to Kotlin data classes

3. **OkHttp** (`okhttp3`)
   - **Why**: HTTP client used by Retrofit
   - **Usage**: Network client for API calls with logging interceptor

4. **Glide** (`bumptech-glide` & `bumptech-glide-compose`)
   - **Why**: Efficient image loading and caching
   - **Usage**: Loading movie poster images in both XML (ImageView) and Compose (GlideImage)

5. **Jetpack Compose** (Material 3)
   - **Why**: Modern UI toolkit for building native Android UIs
   - **Usage**: Movie detail screen UI

6. **Dagger Hilt** (`dagger-hilt`)
   - **Why**: Dependency injection framework
   - **Usage**: Managing dependencies across the app

7. **Navigation Component** (`navigation-safeargs`)
   - **Why**: Type-safe navigation between screens
   - **Usage**: Navigation between XML and Compose screens

### Testing Libraries
8. **MockK** (`mockk`)
   - **Why**: Mocking framework for Kotlin coroutines support
   - **Usage**: Unit testing ViewModels and Repository

## Error Handling

The app implements comprehensive error handling:

1. **Network Errors**
   - Catches exceptions from Retrofit calls
   - Displays user-friendly error messages
   - Provides retry functionality

2. **Empty States**
   - Shows appropriate messages when no data is available

3. **Loading States**
   - Loading indicators during data fetching
   - Prevents multiple simultaneous requests

## Accessibility Support

The app includes basic accessibility support:

1. **Content Descriptions**
   - All images have descriptive `contentDescription` attributes
   - Text elements have semantic descriptions where appropriate

2. **TalkBack Labels**
   - Movie titles have clear descriptions
   - Navigation actions are accessible
   - Error messages are properly labeled

3. **Semantic Structure**
   - Proper use of semantic modifiers in Compose
   - Logical focus order in XML layouts

## Code Quality

### Best Practices Followed

1. **Clean Architecture**
   - Separation of concerns (Data, Domain, UI)
   - Dependency inversion principle
   - Repository pattern for data abstraction

2. **Kotlin Best Practices**
   - Data classes for models
   - Coroutines for asynchronous operations
   - Flow for reactive state management

3. **Android Best Practices**
   - ViewBinding/ComposeView for view access
   - ViewModel lifecycle awareness
   - Proper resource management
   - Material Design 3 guidelines

4. **Testing**
   - Unit tests for ViewModels
   - Unit tests for Repository
   - Mock dependencies for isolated testing

## Project Structure

```
app/src/main/java/com/telushealth/movies/
├── data/
│   └── movie/
│       ├── MovieResponse.kt          # Data models
│       ├── TmdbApiService.kt         # Retrofit API interface
│       ├── MovieRemoteDataSource.kt   # Remote data source
│       └── MovieRepository.kt         # Repository
├── di/
│   └── MovieModule.kt                # Hilt dependency injection module
└── ui/
    └── movie/
        ├── list/
        │   ├── MovieListFragment.kt      # XML-based Fragment
        │   ├── MovieListViewModel.kt       # ViewModel for list (LiveData)
        │   └── MovieListAdapter.kt        # RecyclerView adapter
        └── detail/
            ├── MovieDetailFragment.kt      # Compose Fragment
            ├── MovieDetailViewModel.kt      # ViewModel for detail (StateFlow)
            ├── MovieDetailState.kt         # State data class (includes similar movies)
            └── MovieDetailScreen.kt        # Compose UI (includes similar movies section)
```

## Challenges Encountered

1. **API Key Management**
   - **Challenge**: Securely storing API key without BuildConfig for a challenge project
   - **Solution**: Used `local.properties` with fallback to placeholder (documented for production use)

2. **Hybrid Navigation**
   - **Challenge**: Seamless navigation between XML and Compose screens
   - **Solution**: Used Navigation Component with Safe Args, which works seamlessly across both UI frameworks

3. **Image Loading**
   - **Challenge**: Consistent image loading in both XML and Compose
   - **Solution**: Used Glide for XML and Glide Compose integration for Compose screens

4. **State Management**
   - **Challenge**: Different state management patterns for XML (LiveData) vs Compose (StateFlow)
   - **Solution**: Used standard ViewModel with LiveData for XML, ViewModel with StateFlow for Compose

## What Would I Improve With More Time?

1. **Caching**
   - Implement Room database for offline movie caching
   - Cache movie images more aggressively
   - Store recently viewed movies

2. **Testing**
   - Add more comprehensive unit tests with TestDispatcher for coroutines
   - Add UI tests using Espresso and Compose testing libraries
   - Add integration tests for API calls

3. **UI/UX Enhancements**
   - Add pull-to-refresh on list screen
   - Add search/filter functionality
   - Add movie rating display
   - Add favorite/bookmark functionality
   - Better error UI with retry buttons
   - Skeleton loading states

4. **Performance**
   - Implement pagination for movie list
   - Optimize image loading with better placeholders
   - Add analytics tracking
   - Performance monitoring

5. **Architecture**
   - Extract Use Cases from Repository (Domain layer)
   - Add caching layer abstraction
   - Implement proper error handling with sealed classes
   - Add loading state management utilities

6. **Accessibility**
   - More comprehensive TalkBack support
   - Keyboard navigation support
   - High contrast mode support
   - Dynamic font scaling

7. **Code Quality**
   - Add more documentation/comments
   - Refactor duplicate code (image URL construction)
   - Add linting rules compliance
   - Better error messages with localization

## API Endpoints Used

1. **Discover Movies** - `GET /discover/movie?with_people=71580`
   - Gets all movies starring Benedict Cumberbatch

2. **Movie Details** - `GET /movie/{movie_id}`
   - Gets detailed information for a specific movie

3. **Similar Movies** - `GET /movie/{movie_id}/similar`
   - Gets movies similar to the selected movie

**Image Base URL**: `https://image.tmdb.org/t/p/w500` (or `w200` for thumbnails)

## Notes

- The app follows Android best practices and modern development patterns
- All code follows Kotlin best practices
- The implementation uses MVVM architecture pattern
- XML list screen uses LiveData, Compose detail screen uses StateFlow
- The implementation is production-ready with proper error handling and state management
- The hybrid architecture demonstrates best practices for migrating from XML to Compose

## License

This code was developed as part of a coding challenge for TELUS Health Virtual Care.
