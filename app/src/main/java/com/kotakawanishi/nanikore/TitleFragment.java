package com.kotakawanishi.nanikore;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TitleFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TitleFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 *
 */


public class TitleFragment extends Fragment{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;


    //Views
    private FrameLayout layout;
    private ImageButton playBtn;
    private ImageButton howToPlayBtn;
    private ImageButton soundBtn;

    private FrameLayout explanationBackgroundLayout;
    private FrameLayout explanationLayout;
    private ImageButton nextArrow;
    private ImageButton backArrow;
    private ImageButton backBtn;
    private ImageView explanationView;
    private ImageView titleLabel;

    //Data
    private int pageNumber = 1;
    private ViewTreeObserver.OnGlobalLayoutListener globalLayoutListener;
    final Scale explanationSizeScale = new Scale(0.8048, 0.7193);


    public TitleFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TitleFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TitleFragment newInstance(String param1, String param2) {
        TitleFragment fragment = new TitleFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_title, container, false);
        layout = (FrameLayout) view.findViewById(R.id.layout_title);
        playBtn = (ImageButton) view.findViewById(R.id.play_button);
        howToPlayBtn = (ImageButton) view.findViewById(R.id.how_to_play_button);
        soundBtn = (ImageButton) view.findViewById(R.id.sound_button);
        titleLabel = (ImageView) view.findViewById(R.id.title_label);
        playBtn.setEnabled(false);
        howToPlayBtn.setEnabled(false);
        soundBtn.setEnabled(false);
        setGlobalLayoutListener();
        this.configureViews();

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    /**
     * Called when all saved state has been restored into the view hierarchy
     * of the fragment.  This can be used to do initialization based on saved
     * state that you are letting the view hierarchy track itself, such as
     * whether check box widgets are currently checked.  This is called
     * after {@link #onActivityCreated(Bundle)} and before
     * {@link #onStart()}.
     *
     * @param savedInstanceState If the fragment is being re-created from
     *                           a previous saved state, this is the state.
     */
    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    /**
     * Called immediately after {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}
     * has returned, but before any saved state has been restored in to the view.
     * This gives subclasses a chance to initialize themselves once
     * they know their view hierarchy has been completely created.  The fragment's
     * view hierarchy is not however attached to its parent at this point.
     *
     * @param view               The View returned by {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     */


    /**
     * Called when the fragment is visible to the user and actively running.
     * This is generally
     * tied to {@link Activity#onResume() Activity.onResume} of the containing
     * Activity's lifecycle.
     */
    @Override
    public void onResume() {
        super.onResume();
        playBtn.setEnabled(true);
        howToPlayBtn.setEnabled(true);
        soundBtn.setEnabled(true);
    }

    private void configureViews() {
        final Scale btnScale = new Scale(0.0, 0.6039, 0.3621, 0.0908);
        final double intervalScaleBetweenBtn = 0.0402;
        final Scale soundBtnScale = new Scale(0.041, 0.0336, 0.1892, 0.0);
        final Scale titleLabelScale = new Scale(0.0, 0.174, 1.0, 0.249);


        double titleLabelHeight = mListener.getReferenceSize().y * titleLabelScale.heightScale;
        FrameLayout.LayoutParams lpTitleLabel = new FrameLayout.LayoutParams(mListener.getReferenceSize().x, (int) titleLabelHeight);
        double titleLabelTopMargin = mListener.getReferenceSize().y * titleLabelScale.yScale;
        lpTitleLabel.topMargin = (int)titleLabelTopMargin;
        titleLabel.setLayoutParams(lpTitleLabel);

        double btnWidth = mListener.getReferenceSize().x * btnScale.widthScale;
        double btnHeight = mListener.getReferenceSize().y * btnScale.heightScale;
        double btnIntervalLength = mListener.getReferenceSize().x * intervalScaleBetweenBtn;
        double btnTopMargin = mListener.getReferenceSize().y * btnScale.yScale;
        FrameLayout.LayoutParams lpPlayBtn = new FrameLayout.LayoutParams((int) btnWidth, (int) btnHeight);
        lpPlayBtn.leftMargin = (int)(mListener.getReferenceSize().x / 2.0) - (int)btnWidth - (int)btnIntervalLength;
        lpPlayBtn.topMargin = (int)btnTopMargin;
        playBtn.setLayoutParams(lpPlayBtn);
        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onPlayBtn();
            }
        });

        FrameLayout.LayoutParams lpHowToPlayBtn = new FrameLayout.LayoutParams((int) btnWidth, (int) btnHeight);
        lpHowToPlayBtn.leftMargin = mListener.getReferenceSize().x / 2 + (int) btnIntervalLength;
        lpHowToPlayBtn.topMargin = (int) btnTopMargin;
        howToPlayBtn.setLayoutParams(lpHowToPlayBtn);
        howToPlayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.touchSound();
                playBtn.setEnabled(false);
                howToPlayBtn.setEnabled(false);
                soundBtn.setEnabled(false);

                //setExplanationLp
                explanationView.setImageResource(R.drawable.explanation1);
                pageNumber = 1;
                layout.addView(explanationBackgroundLayout);
                explanationBackgroundLayout.addView(explanationLayout);
                explanationLayout.addView(explanationView);
                explanationBackgroundLayout.addView(backBtn);
                setArrow();
            }
        });

        double soundBtnWidth = mListener.getReferenceSize().x * soundBtnScale.widthScale;
        double soundBtnHeight = mListener.getReferenceSize().x * soundBtnScale.widthScale;
        FrameLayout.LayoutParams lpSoundBtn = new FrameLayout.LayoutParams((int) soundBtnWidth, (int) soundBtnHeight);
        double soundBtnLeftMargin = mListener.getReferenceSize().x * soundBtnScale.xScale;
        double soundBtnTopMargin = mListener.getReferenceSize().y * soundBtnScale.yScale;
        lpSoundBtn.leftMargin = (int) soundBtnLeftMargin;
        lpSoundBtn.topMargin = (int) soundBtnTopMargin;
        soundBtn.setLayoutParams(lpSoundBtn);
        soundBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mListener.switchSound()){
                    soundBtn.setImageResource(R.drawable.sound_on_btn);
                }else{
                    soundBtn.setImageResource(R.drawable.sound_off_btn);
                }
            }
        });


    }

    private void setGlobalLayoutListener(){
        globalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                setUpExplanationViews();
                layout.getViewTreeObserver().removeOnGlobalLayoutListener(globalLayoutListener);
            }
        };
        layout.getViewTreeObserver().addOnGlobalLayoutListener(globalLayoutListener);
    }


    private void setUpExplanationViews() {
        final Scale backBtnScale = new Scale(0.6933, 0.0416, 0.24, 0.075);
        final Scale arrowScale = new Scale(0.0289, 0.8799, 0.2979, 0.0925);


        explanationBackgroundLayout = new FrameLayout(this.getContext());
        double alpha = 255 * 0.8;
        explanationBackgroundLayout.setBackgroundColor(Color.argb((int) alpha, 0, 0, 0));
        explanationBackgroundLayout.setPadding(0, 0, 0, 0);
        explanationBackgroundLayout.setLayoutParams(new FrameLayout.LayoutParams(layout.getWidth(), layout.getHeight() - mListener.getAdHeight()));


        double arrowWidth = explanationBackgroundLayout.getLayoutParams().width * arrowScale.widthScale;
        double arrowHeight = explanationBackgroundLayout.getLayoutParams().height * arrowScale.heightScale;
        nextArrow = new ImageButton(this.getContext());
        nextArrow.setImageResource(R.drawable.next_arrow);
        backArrow = new ImageButton(this.getContext());
        nextArrow.setScaleType(ImageView.ScaleType.FIT_XY);
        backArrow.setScaleType(ImageView.ScaleType.FIT_XY);
        nextArrow.setPadding(0, 0, 0, 0);
        backArrow.setPadding(0, 0, 0, 0);
        backArrow.setImageResource(R.drawable.back_arrow);
        nextArrow.setEnabled(true);
        backArrow.setEnabled(true);

        FrameLayout.LayoutParams lpBackArrow = new FrameLayout.LayoutParams((int) arrowWidth, (int) arrowHeight);
        double backArrowLeftMargin = mListener.getReferenceSize().x * arrowScale.xScale;
        double backArrowTopMargin = explanationBackgroundLayout.getLayoutParams().height * arrowScale.yScale;
        lpBackArrow.leftMargin = (int) backArrowLeftMargin;
        lpBackArrow.topMargin = (int) backArrowTopMargin;
        backArrow.setLayoutParams(lpBackArrow);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backPage();
            }
        });


        FrameLayout.LayoutParams lpNextArrow = new FrameLayout.LayoutParams((int) arrowWidth, (int) arrowHeight);
        double nextArrowLeftMargin = mListener.getReferenceSize().x - backArrowLeftMargin - arrowWidth;
        lpNextArrow.leftMargin = (int) nextArrowLeftMargin;
        lpNextArrow.topMargin = (int) backArrowTopMargin;
        nextArrow.setLayoutParams(lpNextArrow);
        nextArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextPage();
            }
        });


        explanationLayout = new FrameLayout(this.getContext());
        double explanationLayoutWidth = mListener.getReferenceSize().x * explanationSizeScale.widthScale;
        double explanationLayoutHeight = explanationBackgroundLayout.getLayoutParams().height * explanationSizeScale.heightScale;
        FrameLayout.LayoutParams lpExplanation = new FrameLayout.LayoutParams((int) explanationLayoutWidth, (int) explanationLayoutHeight);
        double explanationLeftMargin = mListener.getReferenceSize().x / 2.0 - explanationLayoutWidth / 2.0;
        double explanationTopMargin = explanationBackgroundLayout.getLayoutParams().height / 2.0 - explanationLayoutHeight / 2.0;
        lpExplanation.leftMargin = (int) explanationLeftMargin;
        lpExplanation.topMargin = (int) explanationTopMargin;
        explanationLayout.setLayoutParams(lpExplanation);

        explanationView = new ImageView(getContext());
        FrameLayout.LayoutParams lpExplanationView = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        explanationView.setLayoutParams(lpExplanationView);

        backBtn = new ImageButton(getContext());
        backBtn.setImageResource(R.drawable.back_btn);
        backBtn.setScaleType(ImageView.ScaleType.FIT_XY);
        backBtn.setPadding(0, 0, 0, 0);

        double backBtnWidth = mListener.getReferenceSize().x * backBtnScale.widthScale;
        double backBtnHeight = explanationBackgroundLayout.getLayoutParams().height * backBtnScale.heightScale;
        FrameLayout.LayoutParams lpBackBtn = new FrameLayout.LayoutParams((int)backBtnWidth, (int)backBtnHeight);
        double backBtnLeftMargin = mListener.getReferenceSize().x * backBtnScale.xScale;
        double backBtnTopMargin = explanationBackgroundLayout.getLayoutParams().height * backBtnScale.yScale;
        lpBackBtn.leftMargin = (int) backBtnLeftMargin;
        lpBackBtn.topMargin = (int) backBtnTopMargin;
        backBtn.setLayoutParams(lpBackBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backHome();
            }
        });
    }

    public void onPlayBtn() {
        mListener.touchSound();
        mListener.goToGameLevelFragment();
    }



    private void nextPage() {
        mListener.touchSound2();
        pageNumber += 1;
        explanationView.setImageResource(R.drawable.explanation2);
        this.setArrow();
    }

    private void backPage() {
        mListener.touchSound2();
        pageNumber -= 1;
        explanationView.setImageResource(R.drawable.explanation1);
        this.setArrow();
    }

    private void backHome() {
        mListener.touchSound2();
        playBtn.setEnabled(true);
        howToPlayBtn.setEnabled(true);
        soundBtn.setEnabled(true);
        explanationLayout.removeAllViewsInLayout();
        explanationBackgroundLayout.removeAllViewsInLayout();
        layout.removeViewInLayout(explanationBackgroundLayout);
    }

    private void setArrow() {
        if (nextArrow.isShown())
            explanationBackgroundLayout.removeView(nextArrow);
        if (backArrow.isShown())
            explanationBackgroundLayout.removeView(backArrow);
        switch (pageNumber) {
            case 1:
                explanationBackgroundLayout.addView(nextArrow);
                break;
            case 2:
                explanationBackgroundLayout.addView(backArrow);
                break;
            default:
                break;
        }
    }

    /**
     * Called to ask the fragment to save its current dynamic state, so it
     * can later be reconstructed in a new instance of its process is
     * restarted.  If a new instance of the fragment later needs to be
     * created, the data you place in the Bundle here will be available
     * in the Bundle given to {@link #onCreate(Bundle)},
     * {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}, and
     * {@link #onActivityCreated(Bundle)}.
     * <p>
     * <p>This corresponds to {@link Activity#onSaveInstanceState(Bundle)
     * Activity.onSaveInstanceState(Bundle)} and most of the discussion there
     * applies here as well.  Note however: <em>this method may be called
     * at any time before {@link #onDestroy()}</em>.  There are many situations
     * where a fragment may be mostly torn down (such as when placed on the
     * back stack with no UI showing), but its state will not be saved until
     * its owning activity actually needs to save its state.
     *
     * @param outState Bundle in which to place your saved state.
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
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
        Point getReferenceSize();
        void touchSound();
        void touchSound2();
        void goToGameLevelFragment();
        boolean switchSound();
        int getAdHeight();
    }
}
