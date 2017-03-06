package com.neocampus.wifishared.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.neocampus.wifishared.R;
import com.neocampus.wifishared.listeners.OnActivitySetListener;
import com.neocampus.wifishared.views.SessionHistoView;
import com.neocampus.wifishared.views.UsersHistoView;

import java.util.List;
import java.util.Locale;

/**
 * FragmentSession est un fragment qui affiche une vue contenant l'historique des session de partage
 *
 * @see Fragment
 */
public class FragmentSession extends Fragment implements AdapterView.OnItemClickListener, View.OnClickListener {

    /**
     * Interface de communication avec l'activit? principale {@link com.neocampus.wifishared.activity.MainActivity}
     * #see {@link OnActivitySetListener}
     */
    private OnActivitySetListener mListener;

    /**
     * Objet graphique qui permet de changer la vue et d'afficher les d?tails d'une session de partage
     */
    private ViewSwitcher simpleViewSwitcher;

    /**
     * Objet graphique qui permet d'afficher la liste des sessions de partages.
     */
    private UsersHistoView histoView;

    /**
     * Objet graphique qui permet d'afficher le title des d?tails de la session de partage selectionn?
     */
    private TextView titleView;

    /**
     * Constructeur du fragment
     */
    public FragmentSession() {
        // Required empty public constructor
    }

    /**
     * Cr?e la vue affich? par le fragment
     *
     * @see Fragment#onCreateView(LayoutInflater, ViewGroup, Bundle)
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.mListener.hideAppBarSaveConfig();
        this.mListener.hideAppBarRefresh();

        View view = inflater.inflate(R.layout.fragment_session, container, false);

        simpleViewSwitcher = (ViewSwitcher) view.findViewById(R.id.simpleViewSwitcher);
        SessionHistoView sessionHisto = (SessionHistoView) view.findViewById(R.id.session_historique);
        ListView lvItems = (ListView) sessionHisto.findViewById(R.id.lvClients);
        lvItems.setOnItemClickListener(this);
        sessionHisto.changeSessions(mListener.getAllConsommations());

        TextView backView = (TextView) view.findViewById(R.id.consommation_back);
        backView.setOnClickListener(this);
        titleView = (TextView) view.findViewById(R.id.title_historique);
        histoView = (UsersHistoView) view.findViewById(R.id.users_historique);

        return view;
    }

    /**
     *
     * @param context Context de l'application
     *
     * @see Fragment#onAttach(Context)
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnActivitySetListener) {
            mListener = (OnActivitySetListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    /**
     * En cas de clique sur une session de partage, cette m?thode
     * affiche les d?tails de la session de partage selectionn?e
     *
     * @param parent objet graphique contenant la vue de la session de partage
     * @param view objet graphique repr?sentant la vue de la session de partage
     * @param position position de l'item dans la liste des sessions de partage
     * @param id identifiant de l'item dans la liste des sessions de partage
     *
     * @see android.widget.AdapterView.OnItemClickListener#onItemClick(AdapterView, View, int, long)
     * @see UsersHistoView#changeUtilisateurs(List)
     * @see OnActivitySetListener#getUtilisateurs(int)
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        histoView.changeUtilisateurs(mListener.getUtilisateurs(view.getId()));
        titleView.setText(String.format(Locale.FRANCE, "D?tails de la session N?%d", view.getId()));
        simpleViewSwitcher.showNext();
    }

    /**
     * En cas de clique sur le bouton Revenir, r?affiche la liste des sessions de partage
     * @param v button sur lequel l'utilisateur a cliqu?
     */
    @Override
    public void onClick(View v) {
        simpleViewSwitcher.setDisplayedChild(0);
    }
}
