package com.unitn.musichino.Models;

import java.util.ArrayList;

public class ModelClass {

    private ArrayList<String> images = new ArrayList<>();

    public ModelClass() {
        getImages();
    }

    public String getItem(int i) {
        return images.get(i);
    }

    void getImages() {
        images.add("https://rukminim1.flixcart.com/image/832/832/book/0/1/9/rich-dad-poor-dad-original-imadat2a4f5vwgzn.jpeg?q=70");
        images.add("https://www.seeken.in/wp-content/uploads/2017/06/The-4-Hour-Work-Week.jpeg");
        images.add("https://www.seeken.in/wp-content/uploads/2017/06/Managing-Oneself.jpeg");
        images.add("https://www.seeken.in/wp-content/uploads/2017/07/How-to-Win-Friends-and-Influence-People.jpeg");
        images.add("https://www.seeken.in/wp-content/uploads/2017/07/THINK-LIKE-DA-VINCI-7-Easy-Steps-to-Boosting-your-Everyday-Genius.jpeg");
        images.add("https://www.seeken.in/wp-content/uploads/2017/07/How-To-Stop-Worrying-And-Start-Living.jpg");
        images.add("https://www.seeken.in/wp-content/uploads/2017/08/THE-INTELLIGENT-INVESTOR.jpeg");
        images.add("https://www.seeken.in/wp-content/uploads/2017/08/Awaken-the-Giant-within-How-to-Take-Immediate-Control-of-Your-Mental-Emotional-Physical-and-Financial-Life.jpg");
        images.add("https://www.seeken.in/wp-content/uploads/2017/08/E-MYTH-REVISITED.jpeg");
        images.add("https://images-na.ssl-images-amazon.com/images/I/41axGE4CehL._SX353_BO1,204,203,200_.jpg");
        images.add("https://rukminim1.flixcart.com/image/832/832/book/0/1/9/rich-dad-poor-dad-original-imadat2a4f5vwgzn.jpeg?q=70");

    }
}
