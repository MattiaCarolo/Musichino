package com.unitn.musichino.interfaces;

import com.unitn.musichino.Models.PlaylistModel;

/*
    Creata per far comunicare PlaylistFragment con la sua recycler
    Al suo interno passa la playlist con all'interno gli AudioModel che la compongono
 */

public interface PlaylistToFragment {

    void onClickChange(PlaylistModel playListModel);
}
