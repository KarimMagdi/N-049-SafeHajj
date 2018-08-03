package hajjhackathon.com.team.safehajj.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import hajjhackathon.com.team.safehajj.AppNavigator;
import hajjhackathon.com.team.safehajj.R;
import hajjhackathon.com.team.safehajj.connection.gps.TrackingService;
import hajjhackathon.com.team.safehajj.util.CirclePreference;
import hajjhackathon.com.team.safehajj.util.SharedPreferenceUtil;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RegisterProfileFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RegisterProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegisterProfileFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER


    // TODO: Rename and change types of parameters
    private static String mIsCreateCircle = "mIsCreateCircle";
    public static boolean isCreateCircle;
    private EditText circleIdNameEditText;
    private EditText userIdEditText;
    private EditText userNameEditText;
    private Button signUpCircle;
    private String deepLink;
    private OnFragmentInteractionListener mListener;
    private String circleName;

    public RegisterProfileFragment() {
        // Required empty public constructor
    }

    public static RegisterProfileFragment newInstance(boolean isCreate) {
        RegisterProfileFragment fragment = new RegisterProfileFragment();
        Bundle args = new Bundle();
        args.putBoolean(mIsCreateCircle, isCreate);
        fragment.setArguments(args);
        return fragment;
    }

    public void setDeepLink(String deepLink) {
        this.deepLink = deepLink;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            isCreateCircle = getArguments().getBoolean(mIsCreateCircle);
        }
        if (!isCreateCircle && deepLink != null) {
            parseCircleName(deepLink);
        }
    }

    private void parseCircleName(String deepLink) {
        circleName = deepLink.substring(deepLink.lastIndexOf("/") + 1);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register_profile, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        circleIdNameEditText = view.findViewById(R.id.editText_CircleNameOrId);
        userIdEditText = view.findViewById(R.id.editText_userId);
        userNameEditText = view.findViewById(R.id.editText_userName);
        if (circleName != null) {
            CirclePreference circlePreference =
                    CirclePreference.newInstance();
            circlePreference.setCircleName(circleName);
            circleIdNameEditText.setText(circleName);
        }
        signUpCircle = view.findViewById(R.id.btn_signUpCircle);
        signUpCircle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isCreateCircle) {
                    String newCircleId = TrackingService.getCircleID(true);
                    PreferenceManager.getDefaultSharedPreferences(getContext()).edit().
                            putString(getString(R.string.circle_id_sharedpreferences_key),
                                    newCircleId).apply();
                    circleName = circleIdNameEditText.getText().toString();
                    SharedPreferenceUtil.INSTANCE.setBooleanPreference(getContext(), "isAdmin", true);

                } else {
                    TrackingService.getCircleID(false);
                    SharedPreferenceUtil.INSTANCE.setBooleanPreference(getContext(), "isAdmin", false);
                    TrackingService.circleId = circleIdNameEditText.getText().toString();
                    PreferenceManager.getDefaultSharedPreferences(getContext()).edit().
                            putString(getString(R.string.circle_id_sharedpreferences_key),
                                    TrackingService.circleId).apply();


                }
                AppNavigator.INSTANCE.goToMapsActivity(getActivity(), null, isCreateCircle,
                        circleName);

            }
        });
        if (isCreateCircle) {

            signUpCircle.setText(getString(R.string.create));
        } else
            signUpCircle.setText(getString(R.string.join));

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


}
