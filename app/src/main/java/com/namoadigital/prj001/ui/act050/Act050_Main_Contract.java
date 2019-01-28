package com.namoadigital.prj001.ui.act050;

import com.namoadigital.prj001.model.SO_Favorite_Item;

import java.util.List;

public interface Act050_Main_Contract {

    interface I_Frag_Favorite{
        void populatedFavoritesList(List<SO_Favorite_Item> favorites);
    }

    interface I_Presenter{
        void getFavoriteList();
    }

}
