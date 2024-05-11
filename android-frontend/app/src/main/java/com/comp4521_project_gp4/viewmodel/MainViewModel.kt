import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.time.Duration
import com.comp4521_project_gp4.backend.aws.Exercise
import com.comp4521_project_gp4.backend.aws.Food
import com.comp4521_project_gp4.backend.aws.User
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.WeekFields
import java.util.Locale
import java.util.function.Consumer


class MainViewModel : ViewModel() {
  private lateinit var currentUser: User
  
  private val _caloriesBurned = MutableLiveData<Int>()
  val caloriesBurned: LiveData<Int> get() = _caloriesBurned
  
  private val _caloriesIntake = MutableLiveData<Int>()
  val caloriesIntake: LiveData<Int> get() = _caloriesIntake
  
  private val _exerciseTime = MutableLiveData<Int>()
  val exerciseTime: LiveData<Int> get() = _exerciseTime
  
  fun setUser(user: User) {
    currentUser = user
  }
  
  fun updateCaloriesBurned(calories: Int) {
    _caloriesBurned.postValue(calories)
  }
  
  fun updateCaloriesIntake(calories: Int) {
    _caloriesIntake.postValue(calories)
  }
  
  fun updateExerciseTime(time: Int) {
    _exerciseTime.postValue(time)
  }
  
  fun mainScreenOnLoad() {
    println("main screen on load")
    var weeklyCaloriesBurned = 0
    var weeklyExerciseTime = 0
    var weeklyCaloriesIntake = 0
    val today = LocalDate.now()
    val weekFields = WeekFields.of(Locale.getDefault())
    val currentWeek = today.get(weekFields.weekOfWeekBasedYear())
    
    
    currentUser.getCurrentUserExerciseCache().forEach(Consumer { exercise: Exercise ->
      // Define the formatter for the date pattern "May 11, 2024"
      val formatter =
        DateTimeFormatter.ofPattern("MMMM d, yyyy", Locale.ENGLISH)
      
      // Parse the date using the formatter
      val exerciseDate = LocalDate.parse(exercise.date, formatter)
      
      // Assuming you have a WeekFields instance for your locale
      val weekFields =
        WeekFields.of(Locale.getDefault())
      
      // Check if the week of the year matches the current week
      if (exerciseDate[weekFields.weekOfWeekBasedYear()] == currentWeek) {
        weeklyCaloriesBurned += exercise.calories.toInt()
        weeklyExerciseTime += exercise.exerciseLengthInMins.toInt()
      }
    })
    
    currentUser.getCurrentUserFoodCache().forEach(Consumer { food: Food ->
      // Creating a formatter that can handle date and time
      val formatter =
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
      
      // Parse the LocalDateTime from the string
      val foodDateTime = LocalDateTime.parse(food.date, formatter)
      
      // Convert LocalDateTime to LocalDate
      val foodDate = foodDateTime.toLocalDate()
      
      // Assuming you have a WeekFields instance for your locale
      val weekFields =
        WeekFields.of(Locale.getDefault())
      
      // Check if the week of the year matches the current week
      if (foodDate[weekFields.weekOfWeekBasedYear()] == currentWeek) {
        weeklyCaloriesIntake += food.foodCalories.toInt()
      }
    })
    
    println("update value")

    updateCaloriesBurned(weeklyCaloriesBurned)
    updateExerciseTime(weeklyExerciseTime)
    updateCaloriesIntake(weeklyCaloriesIntake)
    
    println(caloriesIntake.value)
  }
}