package brickteamdevelopment.brickpong;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class TutorialFragment extends Fragment {

    public static final String ARG_PAGE = "page";

    private int mPageNumber;

    public static TutorialFragment create(int pageNumber){

        TutorialFragment fragment   =   new TutorialFragment();
        Bundle args                 =   new Bundle();

        args.putInt(ARG_PAGE, pageNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public TutorialFragment(){}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPageNumber = getArguments().getInt(ARG_PAGE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootview = (ViewGroup)inflater.inflate(R.layout.tutorial_layout,container,false);

        ((TextView)rootview.findViewById(R.id.tutorial_title)).setTypeface(Typeface.createFromAsset(getActivity().getAssets() , "fonts/Chava-Regular.otf"));

        switch (mPageNumber){
            case 0:

                ((TextView)rootview.findViewById(R.id.tutorial_title)).setText(R.string.tutorial_title_1);
                ((ImageView)rootview.findViewById(R.id.imageTutorial)).setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.tutorial));
                break;
            case 1:

                ((TextView)rootview.findViewById(R.id.tutorial_title)).setText(R.string.tutorial_title_2);
                ((ImageView)rootview.findViewById(R.id.imageTutorial)).setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.tutorial2));
                break;
        }
        return rootview;
    }

    public int getPageNumber() {
        return mPageNumber;
    }
}
