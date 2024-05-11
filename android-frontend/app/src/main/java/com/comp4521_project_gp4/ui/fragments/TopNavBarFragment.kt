import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.comp4521_project_gp4.R
import com.google.android.material.appbar.MaterialToolbar

class ToolbarFragment : Fragment() {
  
  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    // Inflate the layout for this fragment
    return inflater.inflate(R.layout.fragment_top_nav_bar, container, false)
  }
  
  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    val toolbar: MaterialToolbar = view.findViewById(R.id.topAppBar)
    (activity as? AppCompatActivity)?.setSupportActionBar(toolbar)
    toolbar.setNavigationOnClickListener {
      activity?.onBackPressed()
    }
  }
  
  fun setToolbarTitle(title: String) {
    val toolbar: MaterialToolbar = view?.findViewById(R.id.topAppBar) ?: return
    (activity as? AppCompatActivity)?.setSupportActionBar(toolbar)
    toolbar.title = title
  }
}