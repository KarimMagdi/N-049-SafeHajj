package hajjhackathon.com.team.safehajj.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import hajjhackathon.com.team.safehajj.R


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [CreateOrJoinCircleFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [CreateOrJoinCircleFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class CreateOrJoinCircleFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_or_join_circle, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         * @return A new instance of fragment CreateOrJoinCircleFragment.
         */
        @JvmStatic
        fun newInstance() =
                CreateOrJoinCircleFragment().apply {
                    arguments = Bundle().apply {
                    }
                }
    }
}
