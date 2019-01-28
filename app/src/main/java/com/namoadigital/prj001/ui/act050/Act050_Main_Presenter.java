package com.namoadigital.prj001.ui.act050;

import com.namoadigital.prj001.model.SO_Favorite_Item;

import java.util.ArrayList;
import java.util.List;

public class Act050_Main_Presenter implements Act050_Main_Contract.I_Presenter {

    Act050_Main_Contract.I_Frag_Favorite mView;

    public Act050_Main_Presenter( Act050_Main_Contract.I_Frag_Favorite mView) {
        this.mView = mView;
    }

    @Override
    public void getFavoriteList() {
        List<SO_Favorite_Item> dummie = new ArrayList<>();
        dummie.add(new SO_Favorite_Item(1, 1,1,"LALALALA","s", "s", 1, "w", "w", 1, "wq","asd","asd", "asd"));
        dummie.add(new SO_Favorite_Item(1, 1,1,"LALALALA","s", "s", 1, "w", "w", 1, "wq","asd","asd", "asd"));
        dummie.add(new SO_Favorite_Item(1, 1,1,"LALALALA","s", "s", 1, "w", "w", 1, "wq","asd","asd", "asd"));
        dummie.add(new SO_Favorite_Item(1, 1,1,"LALALALA","s", "s", 1, "w", "w", 1, "wq","asd","asd", "asd"));
        dummie.add(new SO_Favorite_Item(1, 1,1,"LALALALA","s", "s", 1, "w", "w", 1, "wq","asd","asd", "asd"));
        mView.populatedFavoritesList(dummie);
    }
}
