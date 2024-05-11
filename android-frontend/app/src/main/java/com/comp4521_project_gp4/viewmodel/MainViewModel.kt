import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.comp4521_project_gp4.backend.aws_lambda.User
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.WeekFields
import java.util.Locale

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
    _caloriesBurned.value = calories
  }
  
  fun updateCaloriesIntake(calories: Int) {
    _caloriesIntake.value = calories
  }
  
  fun updateExerciseTime(time: Int) {
    _exerciseTime.value = time
  }
  
  fun mainScreenOnLoad() {
    var weeklyCaloriesBurned = 0
    var weeklyExerciseTime = 0
    var weeklyCaloriesIntake = 0
    val today = LocalDate.now()
    val weekFields = WeekFields.of(Locale.getDefault())
    val currentWeek = today.get(weekFields.weekOfWeekBasedYear())
    
    
//    currentUser.getCurrentUserExerciseCache().forEach { exercise ->
//      val exerciseDate = LocalDate.parse(exercise.date, DateTimeFormatter.ISO_DATE)
//      if (exerciseDate.get(weekFields.weekOfWeekBasedYear()) == currentWeek) {
//        weeklyCaloriesBurned += exercise.calories.toInt()
//        weeklyExerciseTime += exercise.exerciseLengthInMins.toInt()
//      }
//    }
//
//    currentUser.getCurrentUserFoodCache().forEach { food ->
//      val foodDate = LocalDate.parse(food.date, DateTimeFormatter.ISO_DATE)
//      if (foodDate.get(weekFields.weekOfWeekBasedYear()) == currentWeek) {
//        weeklyCaloriesIntake += food.foodCalories.toInt()
//      }
//    }
//
//    updateCaloriesBurned(weeklyCaloriesBurned)
//    updateExerciseTime(weeklyExerciseTime)
//    updateCaloriesIntake(weeklyCaloriesIntake)
  }
}